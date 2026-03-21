package com.example.auth_spring.selenium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Тестирование модуля управления статусом бронирования")
public class BookingStatusTest extends BaseSeleniumTest {

    private void loginAsManager() {
        String login = "manager_" + System.currentTimeMillis();
        register(login, "StrongPass123!", "manager@test.com", true);
        login(login, "StrongPass123!");
    }

    @Test
    @DisplayName("4.1 Смена статуса с 'Неподтвержденная' на 'Подтвержденная' (Drag-and-drop)")
    void testChangeStatusToApproved() {
        loginAsManager();
        
        // Переходим в панель управления записями
        driver.get(baseUrl + "/manage-assigns");
        
        // В тестах лабы используется Drag-and-drop
        // Находим карточку бронирования в колонке "not_approved"
        try {
            List<WebElement> notApprovedBookings = driver.findElements(By.cssSelector(".not_approved .card-wrapper"));
            if (notApprovedBookings.isEmpty()) {
                // Если записей нет, тест пропускаем или помечаем как успех (в зависимости от требований)
                // Но для лабы лучше, чтобы запись была
                fail("Нет неподтвержденных записей для теста");
            }
            
            WebElement firstBooking = notApprovedBookings.get(0);
            WebElement approvedColumn = driver.findElement(By.id("approved"));
            
            // Выполняем drag and drop
            Actions actions = new Actions(driver);
            actions.dragAndDrop(firstBooking, approvedColumn).perform();
            
            // После перемещения проверяем, что статус обновился (через класс карточки или JS)
            // В draggable-bookings.js вызывается fetch /update-status/
            // Даем немного времени на выполнение запроса
            Thread.sleep(1000);
            
            assertTrue(firstBooking.getAttribute("class").contains("approved"), "Карточка должна получить класс 'approved'");
        } catch (Exception e) {
            fail("Ошибка при выполнении drag and drop: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("4.2 Смена статуса с 'Неподтвержденная' на 'Отмененная'")
    void testChangeStatusToCanceled() {
        loginAsManager();
        driver.get(baseUrl + "/manage-assigns");
        
        try {
            List<WebElement> notApprovedBookings = driver.findElements(By.cssSelector(".not_approved .card-wrapper"));
            if (notApprovedBookings.isEmpty()) {
                fail("Нет неподтвержденных записей для теста");
            }
            
            WebElement firstBooking = notApprovedBookings.get(0);
            WebElement canceledColumn = driver.findElement(By.id("canceled"));
            
            Actions actions = new Actions(driver);
            actions.dragAndDrop(firstBooking, canceledColumn).perform();
            
            Thread.sleep(1000);
            assertTrue(firstBooking.getAttribute("class").contains("canceled"), "Карточка должна получить класс 'canceled'");
        } catch (Exception e) {
            fail("Ошибка при выполнении drag and drop: " + e.getMessage());
        }
    }
}
