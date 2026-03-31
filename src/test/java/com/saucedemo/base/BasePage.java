package com.saucedemo.base;

import com.saucedemo.utils.ConfigReader;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        long explicitWait = Long.parseLong(ConfigReader.getInstance().getProperty("explicit.wait"));
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
    }

    /**
     * Chờ element clickable rồi click. Dùng thay cho driver.findElement().click() trực tiếp.
     */
    public void waitAndClick(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    /**
     * Chờ element visible, xóa nội dung cũ rồi gõ text mới.
     */
    public void waitAndType(By locator, String text) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(text);
    }

    /**
     * Chờ element visible rồi lấy text hiển thị.
     */
    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }

    /**
     * Kiểm tra element có hiển thị không. Xử lý StaleElementReferenceException khi DOM re-render.
     */
    public boolean isElementVisible(By locator) {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException | TimeoutException exception) {
            return false;
        }
    }

    /**
     * Cuộn trang đến element chỉ định.
     */
    public void scrollToElement(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Chờ toàn bộ trang load xong (document.readyState = complete).
     */
    public void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
            webDriver -> "complete".equals(
                ((JavascriptExecutor) webDriver).executeScript("return document.readyState")
            )
        );
    }

    /**
     * Lấy giá trị attribute của element (ví dụ: value, href, class).
     */
    public String getAttribute(By locator, String attribute) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute(attribute);
    }
}