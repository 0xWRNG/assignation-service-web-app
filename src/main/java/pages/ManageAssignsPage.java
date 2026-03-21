package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Locators;
import java.time.Duration;
import java.util.List;

public class ManageAssignsPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public ManageAssignsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public ManageAssignsPage open(String baseUrl) {
        driver.get(baseUrl + "/manage-assigns");
        return this;
    }

    public ManageAssignsPage dragFirstNotApprovedToApproved() {
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#not_approved .card-wrapper")));
        WebElement target = driver.findElement(Locators.ManageAssignsPage.APPROVED_COLUMN);
        
        Actions actions = new Actions(driver);
        actions.dragAndDrop(card, target).perform();
        return this;
    }

    public ManageAssignsPage dragFirstNotApprovedToCanceled() {
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#not_approved .card-wrapper")));
        WebElement target = driver.findElement(Locators.ManageAssignsPage.CANCELED_COLUMN);
        
        Actions actions = new Actions(driver);
        actions.dragAndDrop(card, target).perform();
        return this;
    }

    public ManageAssignsPage dragFirstApprovedToCanceled() {
        WebElement card = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#approved .card-wrapper")));
        WebElement target = driver.findElement(Locators.ManageAssignsPage.CANCELED_COLUMN);
        
        Actions actions = new Actions(driver);
        actions.dragAndDrop(card, target).perform();
        return this;
    }

    public boolean isFirstCardApproved() {
        try {
            Thread.sleep(500); // Ожидание fetch запроса
            WebElement card = driver.findElement(Locators.ManageAssignsPage.BOOKING_CARDS);
            return card.getAttribute("class").contains("approved");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFirstCardCanceled() {
        try {
            Thread.sleep(500); // Ожидание fetch запроса
            WebElement card = driver.findElement(Locators.ManageAssignsPage.BOOKING_CARDS);
            return card.getAttribute("class").contains("canceled");
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasBookings() {
        List<WebElement> cards = driver.findElements(Locators.ManageAssignsPage.BOOKING_CARDS);
        return !cards.isEmpty();
    }
}
