package tech.gen.utils;

import java.io.IOException;
import java.util.Properties;

import static java.lang.String.format;

public class ConfigProperty {

    private static final String PROPERTIES_FILE = "/application.properties";

    private static final ConfigProperty INSTANCE = new ConfigProperty();

    public static ConfigProperty getInstance() {
        return INSTANCE;
    }

    private final Properties properties;

    private ConfigProperty() {
        properties = new Properties();
        loadPropertiesFromFile();
    }

    private void loadPropertiesFromFile() {
        try {
            properties.load(ConfigProperty.class.getResourceAsStream(PROPERTIES_FILE));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(format("Can't get properties from %s", PROPERTIES_FILE), e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
