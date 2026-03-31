package framework.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public final class ConfigReader {
    private static volatile ConfigReader instance;

    private final Properties properties = new Properties();
    private final String environment;

    private ConfigReader(String environment) {
        this.environment = environment;
        String resourceName = "config-" + environment + ".properties";

        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream == null) {
                throw new IllegalStateException("Cannot find configuration file: " + resourceName);
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new IllegalStateException("Cannot load configuration file: " + resourceName, exception);
        }
    }

    public static ConfigReader getInstance() {
        String currentEnvironment = System.getProperty("env", "dev").trim();
        if (currentEnvironment.isEmpty()) {
            currentEnvironment = "dev";
        }

        ConfigReader localInstance = instance;
        if (localInstance == null || !Objects.equals(localInstance.environment, currentEnvironment)) {
            synchronized (ConfigReader.class) {
                localInstance = instance;
                if (localInstance == null || !Objects.equals(localInstance.environment, currentEnvironment)) {
                    instance = new ConfigReader(currentEnvironment);
                    localInstance = instance;
                }
            }
        }

        return localInstance;
    }

    public String getBaseUrl() {
        return getRequiredProperty("base.url");
    }

    public String getBrowser() {
        return getRequiredProperty("browser");
    }

    public int getRetryCount() {
        return Integer.parseInt(getRequiredProperty("retry.count"));
    }

    public String getScreenshotPath() {
        return getRequiredProperty("screenshot.path");
    }

    private String getRequiredProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config value for key: " + key);
        }
        return value.trim();
    }
}
