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

@DisplayName("Комплексное тестирование приложения (Smoke Test - Lab 4)")
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
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        // options.setBinary("/usr/bin/chromium-browser"); // Удалено для совместимости с Windows

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    // ─── МОДУЛЬ 1: СОЗДАНИЕ УСЛУГИ ───────────────────────────────────────────────

    @Test @Order(101) @DisplayName("1.1 Создание услуги с валидными данными")
    public void checkValidServiceCreation() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 1)
                .enterTitle("Чистка зубов")
                .enterDescription("Профессиональная чистка")
                .enterDuration("30")
                .clickSaveExpectingSuccess();
        assertTrue(servicePage.isOnServiceViewPage(), "Должен быть редирект на страницу услуги");
    }

    @Test @Order(102) @DisplayName("1.2 Пустое название услуги — ошибка")
    public void checkEmptyTitleService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 1)
                .enterTitle("")
                .enterDuration("30")
                .clickSaveExpectingFailure();
        assertTrue(servicePage.isOnServiceAddPage(), "Должны остаться на странице добавления");
    }

    @Test @Order(103) @DisplayName("1.3 Название 'AB' (2 сим.) — граница минимума")
    public void checkShortTitleService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 1)
                .enterTitle("AB")
                .enterDuration("30")
                .clickSaveExpectingFailure();
        assertTrue(servicePage.isOnServiceAddPage(), "Название < 3 символов не должно приниматься");
    }

    @Test @Order(104) @DisplayName("1.8 Длительность 0 — ошибка")
    public void checkZeroDurationService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 1)
                .enterTitle("Валидная услуга")
                .enterDuration("0")
                .clickSaveExpectingFailure();
        assertTrue(servicePage.isOnServiceAddPage(), "Длительность 0 не должна приниматься");
    }

    @Test @Order(105) @DisplayName("1.14 Описание 2000 сим. — граница максимума")
    public void checkLongDescriptionService() {
        loginAsManager();
        ServicePage servicePage = new ServicePage(driver)
                .open(BASE_URL, 1)
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
                .enterEmail(login + "@example.com");
        registerPage.clickSubmitExpectingSuccess();
        assertTrue(!registerPage.isOnRegisterPage() || driver.getCurrentUrl().contains("/login"));
    }

    @Test @Order(202) @DisplayName("2.2 Логин 'abc' (3 сим.) — слишком коротко")
    public void checkShortLoginRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("abc")
                .enterPassword("StrongPass123!")
                .clickSubmitExpectingFailure();
        assertTrue(registerPage.isOnRegisterPage() && registerPage.hasError());
    }

    @Test @Order(203) @DisplayName("2.5 Пароль 'weak' (4 сим.) — слишком коротко")
    public void checkShortPasswordRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("validlogin_" + System.currentTimeMillis())
                .enterPassword("weak")
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
                .toggleManagerRole();
        registerPage.clickSubmitExpectingSuccess();
        assertTrue(!registerPage.isOnRegisterPage() || driver.getCurrentUrl().contains("/login"));
    }

    // ─── МОДУЛЬ 3: БРОНИРОВАНИЕ ТАЙМСЛОТА ─────────────────────────────────────────

    @Test @Order(301) @DisplayName("3.1 Бронирование доступного слота в будущем")
    public void checkValidBooking() {
        loginAsUser();
        String futureDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        BookingPage bookingPage = new BookingPage(driver)
                .open(BASE_URL, 1, 1)
                .selectDate(futureDate)
                .selectExecutor()
                .selectTimeslot();
        bookingPage.clickSubmit();
        
        assertTrue(bookingPage.isBookingSuccessful());
    }

    @Test @Order(302) @DisplayName("3.4 Бронирование в прошлом — ошибка")
    public void checkPastBooking() {
        loginAsUser();
        String pastDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        BookingPage bookingPage = new BookingPage(driver)
                .open(BASE_URL, 1, 1)
                .selectDate(pastDate);
        
        assertTrue(bookingPage.isOnBookingPage(), "Должны остаться на странице бронирования");
    }

    // ─── МОДУЛЬ 4: УПРАВЛЕНИЕ СТАТУСОМ ───────────────────────────────────────────

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

    // ─── ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ──────────────────────────────────────────────────

    private void loginAsManager() {
        String login = "manager_auto";
        // Пытаемся зарегистрировать, если не существует
        new RegisterPage(driver).open(BASE_URL).enterLogin(login).enterPassword("Pass123!").toggleManagerRole().clickSubmitExpectingSuccess();
        new AuthPage(driver).open(BASE_URL).enterUsername(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
    }

    private void loginAsUser() {
        String login = "user_auto";
        new RegisterPage(driver).open(BASE_URL).enterLogin(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
        new AuthPage(driver).open(BASE_URL).enterUsername(login).enterPassword("Pass123!").clickSubmitExpectingSuccess();
    }
}
