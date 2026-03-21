package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Locators;
import java.time.Duration;

public class BookingPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public BookingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public BookingPage open(String baseUrl, int companyId, int serviceId) {
        driver.get(baseUrl + "/book/" + companyId + "/" + serviceId);
        return this;
    }

    public BookingPage selectDate(String date) {
        WebElement picker = wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.BookingPage.DATE_PICKER));
        picker.sendKeys(date);
        driver.findElement(Locators.BookingPage.DATE_FORM).submit();
        return this;
    }

    public BookingPage selectExecutor() {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(Locators.BookingPage.EXECUTOR_RADIO));
        radio.click();
        driver.findElement(Locators.BookingPage.EXECUTOR_FORM).submit();
        return this;
    }

    public BookingPage selectTimeslot() {
        WebElement radio = wait.until(ExpectedConditions.elementToBeClickable(Locators.BookingPage.TIMESLOT_RADIO));
        radio.click();
        return this;
    }

    public void clickSubmit() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(Locators.BookingPage.SUBMIT_BUTTON));
        btn.click();
    }

    public boolean isBookingSuccessful() {
        return driver.getCurrentUrl().contains("/profile");
    }

    public boolean isOnBookingPage() {
        return driver.getCurrentUrl().contains("/book/");
    }
}
