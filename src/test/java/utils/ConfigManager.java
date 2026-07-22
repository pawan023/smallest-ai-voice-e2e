package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private ConfigManager() {
        // Utility class: object creation is not required.
    }

    private static void loadProperties() {
        try (InputStream inputStream = ConfigManager.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "config.properties was not found under src/test/resources"
                );
            }

            PROPERTIES.load(inputStream);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to load config.properties",
                    exception
            );
        }
    }

    public static String getBaseUrl() {
        return getRequiredProperty("base.url");
    }

    public static String getApiKey() {
        return getRequiredProperty("api.key");
    }

    private static String getRequiredProperty(String key) {
        String value = PROPERTIES.getProperty(key);

        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(
                    "Missing required property: " + key
            );
        }

        return value.trim();
    }
}