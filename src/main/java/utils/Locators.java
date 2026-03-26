package utils;

import org.openqa.selenium.By;

public interface Locators {
    interface AuthPage {
        By USERNAME_INPUT = By.id("login");
        By PASSWORD_INPUT = By.id("password");
        By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
        By HEADING = By.xpath("//h2[contains(text(),'Вход')]");
        By ERROR_MESSAGE = By.className("text-danger");
    }

    interface RegisterPage {
        By LOGIN_INPUT = By.id("login");
        By PASSWORD_INPUT = By.id("password");
        By EMAIL_INPUT = By.id("email");
        By PHONE_INPUT = By.id("phone");
        By NAME_INPUT = By.id("name");
        By SURNAME_INPUT = By.id("surname");
        By PATRONYMIC_INPUT = By.id("patronymic");
        By ROLE_SWITCH = By.id("role");
        By SUBMIT_BUTTON = By.cssSelector("button[type='submit']");
        By ERROR_MESSAGE = By.className("text-danger");
    }

    interface ServicePage {
        By TITLE_INPUT = By.id("title");
        By DESCRIPTION_INPUT = By.id("description");
        By DURATION_INPUT = By.id("duration");
        By SAVE_BUTTON = By.xpath("//button[@type='submit' and normalize-space()='Сохранить']");
        By DELETE_BUTTON = By.cssSelector("button.btn-danger");
        By PAGE_TITLE = By.className("page_title");
        By ERROR_MESSAGE = By.className("text-danger");
    }

    interface BookingPage {
        By DATE_PICKER = By.id("datePicker");
        By DATE_FORM = By.id("dateSelection");
        By EXECUTOR_RADIO = By.xpath("(//div[contains(@class,'radio-div')])[1]");
        By EXECUTOR_FORM = By.id("executorSelection");
        By TIMESLOT_RADIO = By.xpath("(//div[contains(@class,'radio-div') and .//input[@name='timeslots_radio']])[1]");
        By SUBMIT_BUTTON = By.id("submitBtn");
        By CLEAR_BUTTON = By.id("clearButton");
    }

    interface ManageAssignsPage {
        By NOT_APPROVED_COLUMN = By.id("not_approved");
        By APPROVED_COLUMN = By.id("approved");
        By CANCELED_COLUMN = By.id("canceled");
        By BOOKING_CARDS = By.cssSelector(".card-wrapper");
        By APPROVED_CARD = By.xpath("(//div[contains(@class,'card-wrapper') and contains(@class,'approved')])[1]");
        By CANCELLED_CARD = By.xpath("(//div[contains(@class,'card-wrapper') and contains(@class,'cancelled')])[1]");
        By NOT_APPROVED_CARD = By.xpath("(//div[contains(@class,'card-wrapper') and contains(@class,'not_approved')])[1]");
        String CARD_BY_ID = ".card-wrapper[data-id='%s']";
    }

    interface NavBar {
        By PROFILE_LINK = By.xpath("//a[@href='/profile']");
        By MANAGE_ASSIGNS_LINK = By.xpath("//a[@href='/manage-assigns']");
        By LOGOUT_LINK = By.xpath("//a[@href='/logout']");
    }
}
