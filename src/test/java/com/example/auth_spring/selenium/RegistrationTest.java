package com.example.auth_spring.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование модуля регистрации пользователя")
public class RegistrationTest extends BaseSeleniumTest {

    @Test
    @DisplayName("2.1 Валидная регистрация пользователя (USER)")
    void testValidUserRegistration() {
        String login = "testuser_" + System.currentTimeMillis();
        register(login, "StrongPass123!", "test@example.com", false);
        
        // После успешной регистрации должен быть редирект на логин
        assertTrue(driver.getCurrentUrl().contains("/login"), "Должен произойти переход на страницу входа");
    }

    @Test
    @DisplayName("2.2 Регистрация с коротким логином (менее 5 символов)")
    void testShortLoginRegistration() {
        register("abc", "StrongPass123!", "test@example.com", false);
        
        // Ожидаем ошибку на странице регистрации
        WebElement errorMsg = driver.findElement(By.className("text-danger"));
        assertNotNull(errorMsg, "Должно отображаться сообщение об ошибке");
        assertTrue(driver.getCurrentUrl().contains("/register"), "Пользователь должен остаться на странице регистрации");
    }

    @Test
    @DisplayName("2.5 Регистрация со слишком коротким паролем (< 8)")
    void testShortPasswordRegistration() {
        register("validlogin", "weak", "test@example.com", false);
        
        WebElement errorMsg = driver.findElement(By.className("text-danger"));
        assertNotNull(errorMsg, "Должно отображаться сообщение об ошибке");
        assertTrue(driver.getCurrentUrl().contains("/register"));
    }

    @Test
    @DisplayName("2.8 Регистрация с невалидным форматом email")
    void testInvalidEmailRegistration() {
        register("validlogin2", "StrongPass123!", "invalid-email", false);
        
        // HTML5 валидация может предотвратить отправку, либо сервер вернет ошибку
        // В данном случае проверяем, что не ушли на страницу логина
        assertFalse(driver.getCurrentUrl().contains("/login"), "Регистрация не должна быть успешной с плохим email");
    }

    @Test
    @DisplayName("2.10 Регистрация с ролью MANAGER")
    void testManagerRegistration() {
        String login = "manager_" + System.currentTimeMillis();
        register(login, "StrongPass123!", "manager@example.com", true);
        
        assertTrue(driver.getCurrentUrl().contains("/login"), "Менеджер должен успешно регистрироваться");
    }
}
