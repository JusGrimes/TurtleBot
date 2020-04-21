import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    private static Properties configProps;



    public static void main(String[] args) {
        initializeConfig();

        String apiKey = configProps.getProperty("apikey");

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
