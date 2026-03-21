package utils;

import org.openqa.selenium.By;

public interface Locators {
    interface AuthPage {
        By USERNAME_INPUT = By.id("username");
        By PASSWORD_INPUT = By.id("password");
        By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
        By HEADING = By.xpath("//h2[contains(text(),'Вход')]");
        By ERROR_MESSAGE = By.className("text-danger");
    }

    interface RegisterPage {
        By LOGIN_INPUT = By.id("login");
        By PASSWORD_INPUT = By.id("password");
        By EMAIL_INPUT = By.id("email");
        By ROLE_SWITCH = By.id("role");
        By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
        By ERROR_MESSAGE = By.className("text-danger");
    }

    interface ServicePage {
        By TITLE_INPUT = By.id("title");
        By DESCRIPTION_INPUT = By.id("description");
        By DURATION_INPUT = By.id("duration");
        By SAVE_BUTTON = By.cssSelector("button[type='submit']");
        By PAGE_TITLE = By.className("page_title");
    }

    interface BookingPage {
        By DATE_PICKER = By.id("datePicker");
        By EXECUTOR_RADIO = By.name("executors");
        By TIMESLOT_RADIO = By.name("timeslots_radio");
        By SUBMIT_BUTTON = By.id("submitBtn");
    }
}
