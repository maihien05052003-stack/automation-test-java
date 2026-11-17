package action;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ui.BookDemoUI;

import java.time.Duration;
public class BookDemoAction {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public BookDemoAction(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void openPage() {
        driver.get(BookDemoUI.URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(BookDemoUI.EMAIL));
    }

    public void enterEmail(String email) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(BookDemoUI.EMAIL));
        element.clear();
        element.sendKeys(email);
        driver.findElement(BookDemoUI.COMPANY).click();
    }

    public void fillText(By locator, String value) {
        if (value == null) return;
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    public void selectByText(By locator, String value) {
        if (value == null || value.isBlank()) return;
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        new Select(element).selectByVisibleText(value);
    }

    public void fillForm(String email, String firstName, String lastName, String company,
                         String phone, String country, String state, String useCase, String comments) {
        if (email != null && !email.isBlank()) {
            enterEmail(email);
            // Chờ các trường động xuất hiện sau khi email công ty được chấp nhận.
            wait.until(ExpectedConditions.visibilityOfElementLocated(BookDemoUI.FIRST_NAME));
        }
        fillText(BookDemoUI.FIRST_NAME, firstName);
        fillText(BookDemoUI.LAST_NAME, lastName);
        fillText(BookDemoUI.COMPANY, company);
        fillText(BookDemoUI.PHONE, phone);
        selectByText(BookDemoUI.COUNTRY, country);
        if (state != null && !state.isBlank()) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(BookDemoUI.STATE));
            selectByText(BookDemoUI.STATE, state);
        }
        selectByText(BookDemoUI.USE_CASE, useCase);
        fillText(BookDemoUI.COMMENTS, comments);
    }

    public boolean isDisplayed(By locator) {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public boolean isEnabled(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).isEnabled();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    public String getValue(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator)).getAttribute("value");
    }

    public String getText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    public String getValidationMessage(By locator) {
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", element);
    }

    public boolean isCheckboxSelected() {
        return wait.until(ExpectedConditions.presenceOfElementLocated(BookDemoUI.CONSENT)).isSelected();
    }

    public void clickCheckbox() {
        wait.until(ExpectedConditions.elementToBeClickable(BookDemoUI.CONSENT)).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public int getMaxLength(By locator) {
        return Integer.parseInt(wait.until(ExpectedConditions.presenceOfElementLocated(locator))
                .getAttribute("maxlength"));
    }

    public void submitIfAllowed() {
        // Chỉ dùng khi bài kiểm thử được phép gửi form thật.
        wait.until(ExpectedConditions.elementToBeClickable(BookDemoUI.SUBMIT)).click();
    }

}
