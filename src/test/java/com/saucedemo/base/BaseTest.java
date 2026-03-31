package com.saucedemo.base;

import com.saucedemo.utils.ConfigReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public class BaseTest {
    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final DateTimeFormatter SCREENSHOT_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        String effectiveBrowser = getSystemPropertyOrDefault("browser", browser);
        String effectiveEnv = getSystemPropertyOrDefault("env", env);
        String gridUrl = System.getProperty("grid.url", "").trim();

        WebDriver webDriver = gridUrl.isEmpty()
            ? DriverFactory.createDriver(effectiveBrowser)
            : DriverFactory.createRemoteDriver(effectiveBrowser, gridUrl);

        DRIVER.set(webDriver);

        long implicitWait = Long.parseLong(ConfigReader.getInstance().getProperty("implicit.wait"));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

        if (System.getenv("CI") == null) {
            try {
                webDriver.manage().window().maximize();
            } catch (WebDriverException exception) {
                System.out.println("[BaseTest] Unable to maximize browser window: " + exception.getMessage());
            }
        }

        System.setProperty("env", effectiveEnv);
        webDriver.get(ConfigReader.getInstance().getProperty("app.url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver webDriver = getDriver();
        try {
            if (webDriver != null && result.getStatus() == ITestResult.FAILURE) {
                captureScreenshot(result.getMethod().getMethodName(), webDriver);
            }
        } finally {
            if (webDriver != null) {
                webDriver.quit();
            }
            DRIVER.remove();
        }
    }

    public WebDriver getDriver() {
        return DRIVER.get();
    }

    private void captureScreenshot(String testName, WebDriver driver) {
        String screenshotDirectory = ConfigReader.getInstance().getProperty("screenshot.dir");
        Path screenshotPath = Path.of(screenshotDirectory);
        try {
            Files.createDirectories(screenshotPath);
            Path destination = screenshotPath.resolve(testName + "_" + LocalDateTime.now().format(SCREENSHOT_FORMATTER) + ".png");
            Path source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException exception) {
            throw new RuntimeException("Không thể lưu screenshot cho test fail: " + testName, exception);
        }
    }

    private String getSystemPropertyOrDefault(String key, String fallbackValue) {
        String value = System.getProperty(key);
        if (value == null || value.isBlank()) {
            return fallbackValue;
        }
        return value;
    }
}