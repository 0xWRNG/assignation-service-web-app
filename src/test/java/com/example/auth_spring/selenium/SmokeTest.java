package com.example.auth_spring.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import pages.AuthPage;
import pages.RegisterPage;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Комплексное тестирование приложения (Smoke Test)")
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
        options.setBinary("/usr/bin/chromium-browser");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    @Test
    @DisplayName("2.1 Валидная регистрация пользователя (USER)")
    public void checkValidUserRegistration() {
        String login = "user_" + System.currentTimeMillis();
        
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin(login)
                .enterPassword("StrongPass123!")
                .enterEmail(login + "@example.com");
        
        AuthPage authPage = registerPage.clickSubmitExpectingSuccess();

        assertTrue(!registerPage.isOnRegisterPage() || authPage.isOnLoginPage(),
                "После успешной регистрации должен быть переход на страницу логина");
    }

    @Test
    @DisplayName("2.2 Регистрация с коротким логином (менее 5 символов)")
    public void checkShortLoginRegistration() {
        RegisterPage registerPage = new RegisterPage(driver)
                .open(BASE_URL)
                .enterLogin("abc")
                .enterPassword("StrongPass123!")
                .enterEmail("test@example.com")
                .clickSubmitExpectingFailure();

        assertTrue(registerPage.isOnRegisterPage(), "Должны остаться на странице регистрации");
        assertTrue(registerPage.hasError(), "Должна отображаться ошибка");
    }

    @Test
    @DisplayName("Вход с неверными данными — ошибка")
    public void checkLoginWithInvalidCredentials() {
        AuthPage authPage = new AuthPage(driver)
                .open(BASE_URL)
                .enterUsername("nonexistent")
                .enterPassword("wrongpassword")
                .clickSubmitExpectingFailure();

        assertTrue(authPage.isOnLoginPage(), "Должны остаться на странице логина");
        assertTrue(authPage.hasError(), "Должна отображаться ошибка");
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }
}
