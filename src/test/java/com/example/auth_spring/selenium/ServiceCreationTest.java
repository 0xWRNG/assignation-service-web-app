package com.example.auth_spring.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование модуля создания услуги")
public class ServiceCreationTest extends BaseSeleniumTest {

    private void loginAsManager() {
        // Предполагаем, что менеджер уже зарегистрирован
        // В реальном сценарии можно создать его перед тестом
        String login = "manager_" + System.currentTimeMillis();
        register(login, "StrongPass123!", "manager@test.com", true);
        login(login, "StrongPass123!");
    }

    private void createService(String title, String description, String duration) {
        // Для создания услуги нужно зайти на страницу компании
        // В данном приложении сначала нужно создать компанию, чтобы добавить услугу
        // В тестах лабы подразумевается проверка полей формы
        driver.get(baseUrl + "/service/add/1"); // Предполагаем, что компания с ID=1 существует
        
        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("description")).sendKeys(description);
        driver.findElement(By.id("duration")).sendKeys(duration);
        
        driver.findElement(By.cssSelector("button[type='submit']")).click();
    }

    @Test
    @DisplayName("1.1 Создание услуги с валидными данными")
    void testValidServiceCreation() {
        loginAsManager();
        createService("Чистка зубов", "Профессиональная чистка зубов", "30");
        
        // Должен быть редирект на страницу услуги
        assertTrue(driver.getCurrentUrl().contains("/service/"), "Должен произойти переход на страницу созданной услуги");
        assertTrue(driver.getPageSource().contains("Чистка зубов"));
    }

    @Test
    @DisplayName("1.3 Создание услуги с коротким названием (< 3 сим.)")
    void testShortTitleServiceCreation() {
        loginAsManager();
        createService("AB", "Описание", "30");
        
        // Ожидаем ошибку или что не перешли со страницы добавления
        assertFalse(driver.getCurrentUrl().contains("/service/"), "Услуга не должна быть создана с коротким названием");
    }

    @Test
    @DisplayName("1.8 Создание услуги с нулевой длительностью")
    void testZeroDurationServiceCreation() {
        loginAsManager();
        createService("Валидное название", "Описание", "0");
        
        assertFalse(driver.getCurrentUrl().contains("/service/"), "Услуга не должна быть создана с нулевой длительностью");
    }

    @Test
    @DisplayName("1.14 Создание услуги с максимально длинным описанием (2000 сим.)")
    void testLongDescriptionServiceCreation() {
        loginAsManager();
        String longDesc = "A".repeat(2000);
        createService("Длинное описание", longDesc, "30");
        
        assertTrue(driver.getCurrentUrl().contains("/service/"), "Услуга должна успешно создаваться с 2000 символов описания");
    }
}
