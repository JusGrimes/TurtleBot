package info.justingrimes.turtlebot;

import info.justingrimes.turtlebot.chat.listeners.PingListener;
import info.justingrimes.turtlebot.chat.listeners.TwitterListener;
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
        addChatListeners(jda);


    }

    private static void addChatListeners(JDA jda) {
        jda.addEventListener(new PingListener());

        String twitterConsumerKey = configProps.getProperty("twitter.ConsumerKey");
        String twitterConsumerSecret = configProps.getProperty("twitter.ConsumerSecret");
        String twitterAccessToken = configProps.getProperty("twitter.AccessToken");
        String twitterAccessTokenSecret = configProps.getProperty("twitter.AccessTokenSecret");

        jda.addEventListener(
                new TwitterListener(
                        twitterConsumerKey,
                        twitterConsumerSecret,
                        twitterAccessToken,
                        twitterAccessTokenSecret
                ));

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
