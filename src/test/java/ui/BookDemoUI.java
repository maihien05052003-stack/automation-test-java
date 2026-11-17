package ui;

import org.openqa.selenium.By;

public class BookDemoUI {
    public static final String URL = "https://saucelabs.com/request-demo";

    public static final By EMAIL = By.id("Business-Email");
    public static final By FIRST_NAME = By.name("First Name");
    public static final By LAST_NAME = By.name("Last Name");
    public static final By COMPANY = By.id("Company");
    public static final By PHONE = By.id("Phone");
    public static final By COUNTRY = By.id("Country");
    public static final By STATE = By.id("State");
    public static final By USE_CASE = By.id("Use-Case");
    public static final By COMMENTS = By.id("Comments");
    public static final By CONSENT = By.id("Checkbox");
    public static final By SUBMIT = By.xpath("//form//button[@type='submit']");
    public static final By BUSINESS_EMAIL_ERROR =
            By.xpath("//*[contains(normalize-space(.),'Please enter a valid business email')]");

}
