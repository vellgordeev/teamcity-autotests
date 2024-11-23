package ru.gordeev.teamcity.api.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static Config config;
    private final Properties properties;
    private final Logger logger;

    private Config() {
        this.logger = LogManager.getLogger(Config.class);

        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES);
    }

    private static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public void loadProperties(String fileName) {
        try (InputStream stream = Config.class.getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                logger.error("File not found {}", fileName);
            }
            properties.load(stream);
        } catch (IOException e) {
            logger.error("Error during file reading {}", fileName);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return getConfig().properties.getProperty(key);
    }
}
