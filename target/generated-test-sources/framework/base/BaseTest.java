package framework.base;

import framework.config.ConfigReader;
import framework.driver.DriverFactory;
import framework.utils.ScreenshotUtil;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

public abstract class BaseTest {
    private static ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();

    protected WebDriver getDriver() {
        return tlDriver.get();
    }

    @BeforeMethod(alwaysRun = true)
    @Parameters({"browser", "env"})
    public void setUp(@Optional("chrome") String browser, @Optional("dev") String env) {
        System.setProperty("env", env);

        ConfigReader configReader = ConfigReader.getInstance();
        String requestedBrowser = browser == null || browser.isBlank() ? configReader.getBrowser() : browser;

        WebDriver driver = DriverFactory.createDriver(requestedBrowser);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get(configReader.getBaseUrl());
        tlDriver.set(driver);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        WebDriver driver = getDriver();
        try {
            if (driver != null && result.getStatus() == ITestResult.FAILURE) {
                ScreenshotUtil.capture(driver, result.getName());
                Allure.addAttachment("Screenshot", new ByteArrayInputStream(ScreenshotUtil.captureAsBytes(driver)));
            }
        } finally {
            if (driver != null) {
                driver.quit();
            }
            tlDriver.remove();
        }
    }
}
