package com.example.auth_spring.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseSeleniumTest {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final String baseUrl = "http://localhost:8080";

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.setBinary("/usr/bin/chromium-browser");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void login(String username, String password) {
        driver.get(baseUrl + "/auth/login");
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    protected void register(String login, String password, String email, boolean isManager) {
        driver.get(baseUrl + "/auth/register");
        driver.findElement(By.id("login")).sendKeys(login);
        driver.findElement(By.id("password")).sendKeys(password);
        if (email != null) {
            driver.findElement(By.id("email")).sendKeys(email);
        }
        if (isManager) {
            driver.findElement(By.id("role")).click();
        }
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }
}
