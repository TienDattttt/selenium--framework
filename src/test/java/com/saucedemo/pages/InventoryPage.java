package com.saucedemo.pages;

import com.saucedemo.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class InventoryPage extends BasePage {
    private static final By INVENTORY_CONTAINER = By.id("inventory_container");
    private static final By PAGE_TITLE = By.cssSelector(".title");
    private static final By CART_ICON = By.cssSelector(".shopping_cart_link");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public boolean isInventoryPageDisplayed() {
        return isElementVisible(INVENTORY_CONTAINER);
    }

    public String getPageTitle() {
        return getText(PAGE_TITLE);
    }
}