package widgets;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Button {
    private final WebElement element;
    private final WebDriver driver;

    public Button(WebElement element, WebDriver driver) {
        this.element = element;
        this.driver = driver;
    }

    public void click() {
        element.click();
    }
}
