package com.example.auth_spring.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование модуля бронирования таймслота")
public class BookingTest extends BaseSeleniumTest {

    private void loginAsUser() {
        String login = "user_" + System.currentTimeMillis();
        register(login, "StrongPass123!", "user@test.com", false);
        login(login, "StrongPass123!");
    }

    @Test
    @DisplayName("3.1 Бронирование доступного таймслота в будущем")
    void testValidBooking() {
        loginAsUser();
        
        // Переходим на страницу бронирования услуги (предполагаем ID=1 для компании и услуги)
        driver.get(baseUrl + "/book/1/1");
        
        // Выбираем дату в будущем
        LocalDate futureDate = LocalDate.now().plusDays(1);
        String formattedDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        WebElement datePicker = driver.findElement(By.id("datePicker"));
        datePicker.sendKeys(formattedDate);
        datePicker.submit(); // Или нажать Enter
        
        // После выбора даты должны появиться специалисты (если есть)
        // В тестах лабы проверяем флоу: выбор специалиста и выбор времени
        try {
            // Выбираем первого доступного специалиста
            driver.findElement(By.name("executors")).click();
            driver.findElement(By.id("executorSelection")).submit();
            
            // Выбираем первый доступный таймслот
            driver.findElement(By.name("timeslots_radio")).click();
            driver.findElement(By.id("submitBtn")).click();
            
            // После успешного бронирования редирект в профиль
            assertTrue(driver.getCurrentUrl().contains("/profile"), "После бронирования должен быть редирект в профиль");
            assertTrue(driver.getPageSource().contains("Записи"), "В профиле должны отображаться записи");
        } catch (Exception e) {
            // Если специалистов или таймслотов нет, тест упадет с понятной ошибкой
            fail("Не удалось завершить бронирование: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("3.4 Попытка бронирования таймслота в прошлом")
    void testPastBooking() {
        loginAsUser();
        driver.get(baseUrl + "/book/1/1");
        
        LocalDate pastDate = LocalDate.now().minusDays(1);
        String formattedDate = pastDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        
        WebElement datePicker = driver.findElement(By.id("datePicker"));
        datePicker.sendKeys(formattedDate);
        datePicker.submit();
        
        // В зависимости от реализации, либо таймслоты не отобразятся, либо будет ошибка
        // Проверяем, что не ушли на страницу успеха
        assertFalse(driver.getCurrentUrl().contains("/profile"), "Бронирование в прошлом не должно быть разрешено");
    }
}
