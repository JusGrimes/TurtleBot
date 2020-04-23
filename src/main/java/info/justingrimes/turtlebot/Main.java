package info.justingrimes.turtlebot;
import info.justingrimes.turtlebot.chat.listeners.PingListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static Properties configProps;



    public static void main(String[] args) {
        initializeConfig();

        String token = configProps.getProperty("token");

        JDA jda = null;
        try {
            jda = JDABuilder.createDefault(token).build().awaitReady();
        } catch (LoginException e) {
            logger.error("Cannot Login with token", e);
            System.exit(1);
        } catch (InterruptedException e) {
            logger.error("Never became ready", e);
            System.exit(1);
        }

        jda.addEventListener(new PingListener());


    }

    private static void initializeConfig() {
        String configLocation = "config.properties";
        Properties props = new Properties();
        try (FileInputStream configFileStream = new FileInputStream(configLocation)) {
            props.load(configFileStream);
        } catch (IOException e) {
            System.out.println("Could not load " + configLocation);
            e.printStackTrace();
        }
        configProps = props;
    }
}
