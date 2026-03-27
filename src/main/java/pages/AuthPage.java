package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Locators;
import java.time.Duration;

public class AuthPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public AuthPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public AuthPage open(String baseUrl) {
        driver.get(baseUrl + "/auth/login");
        return this;
    }

    public AuthPage enterUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.AuthPage.USERNAME_INPUT));
        input.clear();
        input.sendKeys(username);
        return this;
    }

    public AuthPage enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.AuthPage.PASSWORD_INPUT));
        input.clear();
        input.sendKeys(password);
        return this;
    }


    public AuthPage clickSubmitExpectingSuccess() {
        driver.findElement(Locators.AuthPage.SUBMIT_BUTTON).click();
        return this;
    }

    public AuthPage clickSubmitExpectingFailure() {
        driver.findElement(Locators.AuthPage.SUBMIT_BUTTON).click();
        return this;
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    public boolean hasError() {
        try {
            return driver.findElement(Locators.AuthPage.ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
