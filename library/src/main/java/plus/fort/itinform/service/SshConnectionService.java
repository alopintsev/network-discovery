package plus.fort.itinform.service;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.sshd.client.ClientBuilder;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.kex.BuiltinDHFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import plus.fort.itinform.model.CiscoPatterns;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

@Service
public class SshConnectionService implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(SshConnectionService.class);
    private final ApplicationContext ctx;
    int port = 22;
    long defaultTimeoutSeconds = 10;
    String host = "";
    SshClient client;
    ClientSession session;
    ClientChannel clientChannel;
    OutputStream outStream;
    InputStream inStream;
//    private CredentialHandler credentialHandler;

    @Autowired
    UserDetailsService userDetailsService;


    public SshConnectionService(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    @Override
    public void close() {
        //false - graceful closing
        clientChannel.close(false);
        client.stop();
    }

    public void connect(String host) throws Exception {
//        credentialHandler = ctx.getBean(CredentialHandler.class);

        this.host = host;
        startClient(host);
        openChannel();
        readUntilPattern();
        sendMessage("terminal length 0\n");
        readUntilPattern();

    }

    public String readUntilPattern() throws Exception {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inStream.read(buffer)) > 0) {
                result.write(buffer, 0, length);
                if (CiscoPatterns.prompt.matcher(result.toString()).find())
                    break;
            }
        } catch (Exception e) {
            logger.log(Level.ERROR, "got exception while readUntilPattern() for host:" + host + "," + e.getMessage());
            throw new Exception(e);
        }
        return result.toString();
    }

    public void sendMessage(String message) throws Exception {
        try {
            outStream.write(message.getBytes());
            outStream.flush();
        } catch (Exception e) {
            logger.log(Level.ERROR, "got exception while sendMessage() for host:" + host + "," + e.getMessage());
            throw new Exception(e);

        }
    }

    private void startClient(String host) {
        client = SshClient.setUpDefaultClient();
        client.setKeyExchangeFactories(NamedFactory.setUpTransformedFactories(false, BuiltinDHFactories.VALUES, ClientBuilder.DH2KEX));
//        client.setServerKeyVerifier(AcceptAllServerKeyVerifier.INSTANCE);
        client.start();
    }

    private void openChannel() throws Exception {
        UserDetails userDetails = userDetailsService.getCurrentUser();

        if (host == "") {
            logger.log(Level.WARN, "can`t open channel, host is not specified");
            return;
        }
        try {
            session = client.connect(userDetails.getUsername(), host, port).verify(defaultTimeoutSeconds, TimeUnit.SECONDS).getSession();
        } catch (IOException e) {
            logger.log(Level.ERROR, "exception while openChannel client.connect to host:" + host + ", " + e.getMessage());
            throw new Exception(e);

            //e.printStackTrace();
        }
        session.addPasswordIdentity(userDetails.getPassword());
        try {
            session.auth().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
        } catch (IOException e) {
            logger.log(Level.ERROR, "exception while openChannel session.auth().verify to host:" + host + ", " + e.getMessage());
            throw new Exception(e);

            //            e.printStackTrace();
        }
        try {
            clientChannel = session.createChannel(Channel.CHANNEL_SHELL);
            clientChannel.open().verify(defaultTimeoutSeconds, TimeUnit.SECONDS);
            outStream = clientChannel.getInvertedIn();
            inStream = clientChannel.getInvertedOut();
//            clientChannel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));

//            clientChannel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(defaultTimeoutSeconds));
        } catch (Exception e) {
            logger.log(Level.ERROR, "exception while make channel to host:" + host + ", " + e.getMessage());
            throw new Exception(e);

        }
    }

}
