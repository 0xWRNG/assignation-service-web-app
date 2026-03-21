package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Locators;
import java.time.Duration;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public RegisterPage open(String baseUrl) {
        driver.get(baseUrl + "/auth/register");
        return this;
    }

    public RegisterPage enterLogin(String login) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.RegisterPage.LOGIN_INPUT));
        input.clear();
        input.sendKeys(login);
        return this;
    }

    public RegisterPage enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.RegisterPage.PASSWORD_INPUT));
        input.clear();
        input.sendKeys(password);
        return this;
    }

    public RegisterPage enterEmail(String email) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.RegisterPage.EMAIL_INPUT));
        input.clear();
        input.sendKeys(email);
        return this;
    }

    public RegisterPage toggleManagerRole() {
        WebElement toggle = wait.until(ExpectedConditions.elementToBeClickable(Locators.RegisterPage.ROLE_SWITCH));
        toggle.click();
        return this;
    }

    public AuthPage clickSubmitExpectingSuccess() {
        driver.findElement(Locators.RegisterPage.SUBMIT_BUTTON).click();
        return new AuthPage(driver);
    }

    public RegisterPage clickSubmitExpectingFailure() {
        driver.findElement(Locators.RegisterPage.SUBMIT_BUTTON).click();
        return this;
    }

    public boolean hasError() {
        try {
            return driver.findElement(Locators.RegisterPage.ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isOnRegisterPage() {
        return driver.getCurrentUrl().contains("/register");
    }
}
