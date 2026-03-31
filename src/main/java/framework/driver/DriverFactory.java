package framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.Locale;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public final class DriverFactory {
    private DriverFactory() {
    }

    public static WebDriver createDriver(String browser) {
        String normalizedBrowser = browser == null ? "chrome" : browser.trim().toLowerCase(Locale.ROOT);

        switch (normalizedBrowser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver(new FirefoxOptions());
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(new ChromeOptions());
        }
    }
}
