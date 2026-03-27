package com.example.auth_spring.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Лаб6")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SmokeTest {
    private WebDriver driver;
    private final String BASE_URL = "http://localhost:8080";

    @BeforeAll
    public static void setupWebDriver() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void initBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test @Order(101) @DisplayName("1.1 Создание услуги с валидными данными")
    public void checkValidServiceCreation() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 24)
                .enterTitle("Чистка зубов")
                .enterDescription("Профессиональная чистка")
                .enterDuration("30")
                .clickSaveExpectingSuccess();
        assertTrue(servicePage.isOnServiceViewPage(), "Должен быть редирект на страницу услуги");
    }

    @Test
    @Order(102)
    @DisplayName("1.2 Пустое название услуги")
    public void checkEmptyTitleService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 24)
                .enterTitle("")
                .enterDuration("30")
                .clickSaveExpectingFailure();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertTrue(servicePage.isOnServiceAddPage());
    }

    @Test @Order(103) @DisplayName("1.3 Название 'AB'")
    public void checkShortTitleService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 24)
                .enterTitle("AB")
                .enterDuration("30")
                .clickSaveExpectingFailure();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        assertTrue(servicePage.isOnServiceAddPage(), "Название < 3 символов");
    }

    @Test @Order(104) @DisplayName("1.8 Длительность 0")
    public void checkZeroDurationService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 24)
                .enterTitle("Валидная услуга")
                .enterDuration("0")
                .clickSaveExpectingFailure();
        assertTrue(servicePage.isOnServiceAddPage());
    }

    @Test @Order(105) @DisplayName("1.14 Описание 2000 сим. — граница максимума")
    public void checkLongDescriptionService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 24)
                .enterTitle("Длинное описание")
                .enterDescription("A".repeat(2000))
                .enterDuration("30")
                .clickSaveExpectingSuccess();
        assertTrue(servicePage.isOnServiceViewPage());
    }

    // ─── МОДУЛЬ 2: РЕГИСТРАЦИЯ ПОЛЬЗОВАТЕЛЯ ──────────────────────────────────────

    @Test @Order(201) @DisplayName("2.1 Валидная регистрация USER")
    public void checkValidUserRegistration() {
        String login = "user_" + System.currentTimeMillis();
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin(login)
                .enterPassword("StrongPass123!")
                .enterEmail(login + "@example.com")
                .enterPhone("+01234567890")
                .enterName("Name")
                .enterSurname("Surname")
                .enterPatronymic("Patronymic");
        registerPage.clickSubmitExpectingSuccess();
        assertTrue(!registerPage.isOnRegisterPage() || driver.getCurrentUrl().contains("/login"));
    }

    @Test
    @Order(202)
    @DisplayName("2.2 Логин 'abc'")
    public void checkShortLoginRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("abc")
                .enterPassword("StrongPass123!")
                .enterEmail("abc@example.com")
                .enterPhone("+01234567890")
                .enterName("Name")
                .enterSurname("Surname")
                .enterPatronymic("Patronymic")
                .clickSubmitExpectingFailure();
        assertTrue(registerPage.isOnRegisterPage() || registerPage.hasError());
    }

    @Test
    @Order(203)
    @DisplayName("2.5 Пароль 'weak'")
    public void checkShortPasswordRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("validlogin_" + System.currentTimeMillis())
                .enterPassword("weak")
                .enterEmail("abc@example.com")
                .enterPhone("+01234567890")
                .enterName("Name")
                .enterSurname("Surname")
                .enterPatronymic("Patronymic")
                .clickSubmitExpectingFailure();
        assertTrue(registerPage.isOnRegisterPage());
    }

    @Test @Order(204) @DisplayName("2.8 Невалидный email — ошибка")
    public void checkInvalidEmailRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("validlogin_" + System.currentTimeMillis())
                .enterPassword("StrongPass123!")
                .enterEmail("invalid-email")
                .enterPhone("+01234567890")
                .enterName("Name")
                .enterSurname("Surname")
                .enterPatronymic("Patronymic")
                .clickSubmitExpectingFailure();
        assertTrue(registerPage.isOnRegisterPage());
    }

    @Test @Order(205) @DisplayName("2.10 Регистрация MANAGER")
    public void checkManagerRegistration() {
        String login = "manager_" + System.currentTimeMillis();
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin(login)
                .enterPassword("StrongPass123!")
                .enterEmail(System.currentTimeMillis() + "@example.com")
                .enterPhone("+01234567890")
                .enterName("Name")
                .enterSurname("Surname")
                .enterPatronymic("Patronymic")
                .toggleManagerRole();
        registerPage.clickSubmitExpectingSuccess();
        assertTrue(!registerPage.isOnRegisterPage() || driver.getCurrentUrl().contains("/login"));
    }

    // ─── МОДУЛЬ 3: БРОНИРОВАНИЕ ТАЙМСЛОТА ─────────────────────────────────────────

    @Test @Order(301) @DisplayName("3.1 Бронирование доступного слота в будущем")
    public void checkValidBooking() {
        loginAsUser();
        String futureDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        BookingPage bookingPage = new BookingPage(driver)
                .open(BASE_URL, 18, 14)
                .selectDate(futureDate)
                .selectExecutor()
                .selectTimeslot();
        bookingPage.clickSubmit();
        
        assertTrue(bookingPage.isBookingSuccessful());
    }

    @Test @Order(302) @DisplayName("3.4 Бронирование в прошлом")
    public void checkPastBooking() {
        loginAsUser();
        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        
        BookingPage bookingPage = new BookingPage(driver)
                .open(BASE_URL, 18, 14)
                .selectDate(pastDate)
                .selectExecutor()
                .selectTimeslot();
        bookingPage.clickSubmit();
        
        assertTrue(bookingPage.isOnBookingPage(), "Должны остаться на странице бронирования");
    }

    @Test @Order(401) @DisplayName("4.1 Статус: Неподтвержденная -> Подтвержденная")
    public void checkStatusChangeToApproved() {
        loginAsManager();
        ManageAssignsPage managePage = new ManageAssignsPage(driver)
                .open(BASE_URL);
        
        if (managePage.hasBookings()) {
            managePage.dragFirstNotApprovedToApproved();
            assertTrue(managePage.isFirstCardApproved());
        }
    }

    @Test @Order(402) @DisplayName("4.2 Статус: Неподтвержденная -> Отмененная")
    public void checkStatusChangeToCanceled() {
        loginAsManager();
        ManageAssignsPage managePage = new ManageAssignsPage(driver)
                .open(BASE_URL);
        
        if (managePage.hasBookings()) {
            managePage.dragFirstNotApprovedToCanceled();
            assertTrue(managePage.isFirstCardCanceled());
        }
    }

    private void loginAsManager() {
        String login = "manager_auto";
        new RegisterPage(driver).open(BASE_URL).enterLogin(login).enterPassword("Pass123!").toggleManagerRole().clickSubmitExpectingSuccess();
        new AuthPage(driver).open(BASE_URL).enterUsername(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
    }

    private void loginAsUser() {
        String login = "user_auto";
        new RegisterPage(driver).open(BASE_URL).enterLogin(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
        new AuthPage(driver).open(BASE_URL).enterUsername(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
    }
}
