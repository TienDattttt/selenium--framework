package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.InventoryPage;
import com.saucedemo.pages.LoginPage;
import com.saucedemo.utils.ConfigReader;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Đăng nhập")
public class LoginTest extends BaseTest {
    @Test(description = "TC001")
    @Story("Đăng nhập thành công với tài khoản hợp lệ")
    @Description("Xác minh người dùng hợp lệ đăng nhập thành công và được chuyển đến trang inventory.")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginSuccess() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        loginPage.login(getUsername(), getPassword());

        Assert.assertTrue(inventoryPage.isInventoryPageDisplayed(), "Inventory page phải hiển thị sau khi đăng nhập thành công.");
        Assert.assertTrue(getDriver().getCurrentUrl().contains("inventory"), "URL hiện tại phải chứa 'inventory'.");
    }

    @Test(description = "TC002")
    @Story("Đăng nhập thất bại khi sai mật khẩu")
    @Description("Xác minh hệ thống hiển thị lỗi phù hợp khi nhập sai mật khẩu.")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithWrongPassword() {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login(getUsername(), "wrongpassword");

        Assert.assertTrue(loginPage.isErrorDisplayed(), "Thông báo lỗi phải hiển thị khi nhập sai mật khẩu.");
        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username and password do not match"),
            "Thông báo lỗi phải chứa nội dung xác thực sai mật khẩu."
        );
    }

    @Test(description = "TC003")
    @Story("Đăng nhập thất bại khi để trống username")
    @Description("Xác minh hệ thống hiển thị lỗi bắt buộc nhập username.")
    @Severity(SeverityLevel.NORMAL)
    public void testLoginWithEmptyUsername() {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login("", getPassword());

        Assert.assertTrue(
            loginPage.getErrorMessage().contains("Username is required"),
            "Thông báo lỗi phải chứa nội dung yêu cầu nhập username."
        );
    }

    @Test(description = "TC004_INTENTIONAL_FAIL")
    @Story("Demo fail pipeline để kiểm tra artifact screenshot")
    @Description("Đăng nhập thành công nhưng cố tình assert sai title để GitHub Actions lưu artifact khi test fail.")
    @Severity(SeverityLevel.MINOR)
    public void testLoginIntentionalFail() {
        LoginPage loginPage = new LoginPage(getDriver());
        InventoryPage inventoryPage = new InventoryPage(getDriver());

        loginPage.login(getUsername(), getPassword());

        // INTENTIONAL FAIL - dùng để demo CI pipeline fail + artifact screenshot
        Assert.assertEquals(inventoryPage.getPageTitle(), "WRONG_TITLE_FOR_CI_DEMO");
    }

    private String getUsername() {
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            return ConfigReader.getInstance().getProperty("app.username");
        }
        return username;
    }

    private String getPassword() {
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            return ConfigReader.getInstance().getProperty("app.password");
        }
        return password;
    }
}