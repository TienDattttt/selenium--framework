package com.saucedemo.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverFactory {
    public static WebDriver createDriver(String browser) {
        boolean isCI = System.getenv("CI") != null;
        return switch (browser.toLowerCase()) {
            case "firefox" -> createFirefoxDriver(isCI);
            default -> createChromeDriver(isCI);
        };
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            System.out.println("[DriverFactory] Chrome HEADLESS mode (CI)");
        } else {
            options.addArguments("--start-maximized");
            System.out.println("[DriverFactory] Chrome normal mode (Local)");
        }
        WebDriverManager.chromedriver().setup();
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        if (headless) {
            options.addArguments("-headless");
            System.out.println("[DriverFactory] Firefox HEADLESS mode (CI)");
        }
        WebDriverManager.firefoxdriver().setup();
        return new FirefoxDriver(options);
    }

    public static WebDriver createRemoteDriver(String browser, String gridUrl) {
        MutableCapabilities options;
        if (browser.equalsIgnoreCase("firefox")) {
            options = new FirefoxOptions();
        } else {
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--no-sandbox", "--disable-dev-shm-usage");
            options = chromeOptions;
        }
        try {
            URL gridEndpoint = new URL(gridUrl + "/wd/hub");
            RemoteWebDriver driver = new RemoteWebDriver(gridEndpoint, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("[DriverFactory] RemoteWebDriver created | Grid: " + gridUrl + " | Browser: " + browser + " | Session: " + driver.getSessionId());
            return driver;
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Grid URL không hợp lệ: " + gridUrl, exception);
        }
    }
}