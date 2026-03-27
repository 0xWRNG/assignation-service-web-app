package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Locators;
import java.time.Duration;

public class ServicePage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ServicePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public ServicePage open(String baseUrl, int companyId) {
        driver.get(baseUrl + "/service/add/" + companyId);
        System.out.println("AFTER GET URL: " + driver.getCurrentUrl());
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                Locators.ServicePage.TITLE_INPUT
        ));
        return this;
    }

    public ServicePage enterTitle(String title) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.ServicePage.TITLE_INPUT));
        input.clear();
        input.sendKeys(title);
        return this;
    }

    public ServicePage enterDescription(String description) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.ServicePage.DESCRIPTION_INPUT));
        input.clear();
        input.sendKeys(description);
        return this;
    }

    public ServicePage enterDuration(String duration) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.ServicePage.DURATION_INPUT));
        input.clear();
        input.sendKeys(duration);
        return this;
    }

    public ServicePage clickSaveExpectingSuccess() {
        driver.findElement(Locators.ServicePage.SAVE_BUTTON).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        return this;
    }

    public ServicePage clickSaveExpectingFailure() {
        driver.findElement(Locators.ServicePage.SAVE_BUTTON).click();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
        return this;
    }

    public boolean isOnServiceViewPage() {
        String currentUrl = driver.getCurrentUrl();
        return driver.getCurrentUrl().contains("/service/") && !driver.getCurrentUrl().contains("/add/");
    }

    public boolean isOnServiceAddPage() {
        return driver.getCurrentUrl().contains("/service/add/");
    }

    public boolean hasError() {
        try {
            return driver.findElement(Locators.ServicePage.ERROR_MESSAGE).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
