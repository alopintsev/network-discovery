package plus.fort.itinform.application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = "plus.fort.itinform", exclude = {SecurityAutoConfiguration.class})
public class ItinformServer {

    private static final Logger log = LogManager.getRootLogger();

    public static void main(String[] args) {
        SpringApplication.run(ItinformServer.class, args);

    }

}
