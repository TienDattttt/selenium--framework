package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.Properties;

public final class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static final ConfigReader INSTANCE = new ConfigReader();

    private final Properties properties = new Properties();

    private ConfigReader() {
        try (InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (Objects.isNull(inputStream)) {
                throw new IllegalStateException("Không tìm thấy file cấu hình: " + CONFIG_FILE);
            }
            properties.load(inputStream);
        } catch (IOException exception) {
            throw new UncheckedIOException("Không thể tải file cấu hình: " + CONFIG_FILE, exception);
        }
    }

    public static ConfigReader getInstance() {
        return INSTANCE;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}