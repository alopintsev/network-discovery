package plus.fort.itinform.application;

import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StopWatch;
import plus.fort.itinform.domain.CdpRecord;
import plus.fort.itinform.domain.Connection;
import plus.fort.itinform.domain.Device;
import plus.fort.itinform.domain.Interface;
import plus.fort.itinform.model.ConnectionModel;
import plus.fort.itinform.model.DeviceModel;
import plus.fort.itinform.model.InterfaceModel;
import plus.fort.itinform.model.NetworkModel;
import plus.fort.itinform.service.UserDetailsService;

import java.io.IOException;
import java.util.List;

@SpringBootApplication(scanBasePackages = "plus.fort.itinform")
@EntityScan
@EnableAutoConfiguration
public class networkScannerCli implements CommandLineRunner {
    private static final Logger logger = LogManager.getRootLogger();
    private final NetworkModel networkModel;

    @Autowired
    UserDetailsService userDetailsService;

    private DeviceModel deviceModel;
    private ConnectionModel connectionModel;
    private InterfaceModel interfaceModel;
    private StopWatch discoveryStopWatch;
    private int nestingLevel;
    private String initialHost = "";
    private boolean saveToDatabase = false;
    private String loginName;

    public networkScannerCli(NetworkModel networkModel) {
        this.networkModel = networkModel;
    }

    public static void main(String[] args) {
        SpringApplication.run(networkScannerCli.class, args);
    }

    @Override
    public void run(String... args) {

        parseCommandLine(args);

        String password = null;
        try {
            password = readPassword();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDetails user = User.builder().username(loginName).password(password).authorities("DISCOVERY").build();
        userDetailsService.saveCurrentUser(user);

        try {
            logger.log(Level.INFO, "starting with host:" + initialHost);
            discoveryStopWatch = new StopWatch();
            discoveryStopWatch.start("CDP neighbors discovery");
            networkModel.setMaxNestingLevel(nestingLevel);
            List<CdpRecord> records = networkModel.discovery(initialHost);
            discoveryStopWatch.stop();

            if (saveToDatabase)
                saveToDatabase(records);

            logger.log(Level.INFO, "work time:" + discoveryStopWatch.prettyPrint());

        } catch (Exception e) {
            logger.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
        }

        if (LogManager.getContext() instanceof LoggerContext) {
            logger.debug("Shutting down log4j2");
            Configurator.shutdown((LoggerContext) LogManager.getContext());
        }

    }

    private String readPassword() throws IOException {

        Terminal terminal = TerminalBuilder.terminal();
        LineReader reader = LineReaderBuilder.builder()
                .terminal(terminal).build();
        String password = reader.readLine("password:", '*');
        return password;
    }

    private void parseCommandLine(String... args) {
        Options options = new Options();

        Option host = new Option("h", "host", true, "first host to scan");
        host.setRequired(true);
        options.addOption(host);

        Option nest = new Option("n", "nest", true, "nesting level");
        nest.setRequired(false);
        options.addOption(nest);

        Option db = new Option("db", "db-save", false, "save results to a database");
        db.setRequired(false);
        options.addOption(db);

        Option login = new Option("l", "login", true, "login name");
        login.setRequired(true);
        options.addOption(login);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("networkScannerCli", options);
            System.exit(1);
        }

        initialHost = cmd.getOptionValue("host");
        nestingLevel = new Integer(cmd.getOptionValue("nest", "0"));
        if (cmd.hasOption("db"))
            saveToDatabase = true;
        loginName = cmd.getOptionValue("l");
    }

    private void saveToDatabase(List<CdpRecord> records) {
        initializeModel();
        discoveryStopWatch.start("save records");
        deviceModel.saveDevices(networkModel.getHosts());
        discoveryStopWatch.stop();

        discoveryStopWatch.start("save records");
        saveRecords(records);
        discoveryStopWatch.stop();
    }

    private void initializeModel() {
        deviceModel = new DeviceModel();
        connectionModel = new ConnectionModel();
        interfaceModel = new InterfaceModel();
    }

    private void saveRecords(List<CdpRecord> records) {

        for (CdpRecord record : records) {
            try {
                logger.log(Level.DEBUG, "proceed record: " + record);

                Connection connection = connectionModel.updateFromModel(new Connection(record.toString()));
                if (connection.getId() != null) {
                    logger.log(Level.DEBUG, "known connection:" + record + ", nothing to save");
                    continue;
                }
                Device localDevice = deviceModel.updateFromModel(record.localInterface.getDevice());
                Device remoteDevice = deviceModel.updateFromModel(record.remoteInterface.getDevice());
                Interface localInterface = interfaceModel.updateFromModel(new Interface(record.localInterface));
                Interface remoteInterface = interfaceModel.updateFromModel(new Interface(record.remoteInterface));

                localInterface.setConnection(connection);
                localInterface.setDevice(localDevice);
                localInterface.setName(record.localInterface.getName());

                remoteInterface.setConnection(connection);
                remoteInterface.setDevice(remoteDevice);
                remoteInterface.setName(record.remoteInterface.getName());

                connectionModel.saveConnection(localInterface, remoteInterface);
            } catch (Exception e) {
                logger.log(Level.ERROR, "exception while trying to save " + record + ", " + e.getMessage());
            }
        }
    }
}
