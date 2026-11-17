package feature.Day6;
import action.BookDemoAction;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import ui.BookDemoUI;
import utils.ReadFileD5;

import java.util.HashMap;
import java.util.Map;


public class BookDemoTest {
    private WebDriver driver;
    private BookDemoAction action;

    private static final String VALID_EMAIL = "qa.test@example.com";

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-US");
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("intl.accept_languages", "en-US,en");
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        action = new BookDemoAction(driver);
        action.openPage();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) driver.quit();
    }

    @Test(description = "TC01 - Mở trang Book Your Demo")
    public void tc01_openBookDemoPage() {
        Assert.assertTrue(driver.getCurrentUrl().contains("/request-demo"));
        Assert.assertTrue(action.isDisplayed(BookDemoUI.EMAIL));
        Assert.assertTrue(action.isDisplayed(BookDemoUI.COMPANY));
    }

    @Test(description = "TC02 - Email công ty hợp lệ làm hiển thị các trường bổ sung")
    public void tc02_validBusinessEmailShowsExtraFields() {
        action.enterEmail(VALID_EMAIL);
        Assert.assertTrue(action.isDisplayed(BookDemoUI.FIRST_NAME));
        Assert.assertTrue(action.isDisplayed(BookDemoUI.LAST_NAME));
        Assert.assertTrue(action.isDisplayed(BookDemoUI.PHONE));
        Assert.assertTrue(action.isDisplayed(BookDemoUI.COUNTRY));
    }

    @Test(description = "TC03 - Email Gmail không được chấp nhận")
    public void tc03_gmailIsRejected() {
        action.enterEmail("qa.test@gmail.com");
        Assert.assertTrue(action.isDisplayed(BookDemoUI.BUSINESS_EMAIL_ERROR));
        Assert.assertTrue(driver.getCurrentUrl().contains("/request-demo"));
    }

    @Test(description = "TC04 - Email Yahoo không được chấp nhận")
    public void tc04_yahooIsRejected() {
        action.enterEmail("qa.test@yahoo.com");
        Assert.assertTrue(action.isDisplayed(BookDemoUI.BUSINESS_EMAIL_ERROR));
    }

    @Test(description = "TC05 - Email Outlook không được chấp nhận")
    public void tc05_outlookIsRejected() {
        action.enterEmail("qa.test@outlook.com");
        Assert.assertTrue(action.isDisplayed(BookDemoUI.BUSINESS_EMAIL_ERROR));
    }

    @Test(description = "TC06 - Kiểm tra thuộc tính maxlength của Company")
    public void tc06_companyMaxLengthIs256() {
        action.enterEmail(VALID_EMAIL);
        Assert.assertEquals(action.getMaxLength(BookDemoUI.COMPANY), 256);
    }

    @Test(description = "TC07 - Country United States hiển thị State")
    public void tc07_usCountryShowsState() {
        action.enterEmail(VALID_EMAIL);
        action.selectByText(BookDemoUI.COUNTRY, "United States");
        Assert.assertTrue(action.isDisplayed(BookDemoUI.STATE));
    }

    @Test(description = "TC08 - Country khác United States không hiển thị State")
    public void tc08_vietnamDoesNotShowState() {
        action.enterEmail(VALID_EMAIL);
        action.selectByText(BookDemoUI.COUNTRY, "Vietnam");
        Assert.assertFalse(isElementPresentAndDisplayed(BookDemoUI.STATE));
    }

    @Test(description = "TC09 - Use Case có giá trị mặc định")
    public void tc09_useCaseDefaultValue() {
        action.enterEmail(VALID_EMAIL);
        String value = action.getValue(BookDemoUI.USE_CASE);
        Assert.assertFalse(value == null || value.isBlank(), "Use Case không có giá trị mặc định");
    }

    @Test(description = "TC10 - Checkbox Consent ban đầu chưa được chọn khi hiển thị")
    public void tc10_consentInitiallyUnchecked() {
        action.enterEmail(VALID_EMAIL);
        action.selectByText(BookDemoUI.COUNTRY, "Germany");
        Assert.assertTrue(action.isDisplayed(BookDemoUI.CONSENT));
        Assert.assertFalse(action.isCheckboxSelected());
    }

    @Test(description = "TC11 - Có thể tick và bỏ tick Consent")
    public void tc11_toggleConsentCheckbox() {
        action.enterEmail(VALID_EMAIL);
        action.selectByText(BookDemoUI.COUNTRY, "Germany");
        action.clickCheckbox();
        Assert.assertTrue(action.isCheckboxSelected());
        action.clickCheckbox();
        Assert.assertFalse(action.isCheckboxSelected());
    }

    @Test(description = "TC12 - Kiểm tra validation khi bỏ trống First Name")
    public void tc12_emptyFirstNameHasBrowserValidation() {
        action.enterEmail(VALID_EMAIL);
        action.fillText(BookDemoUI.LAST_NAME, "Nguyen");
        action.fillText(BookDemoUI.COMPANY, "QA Training");
        action.fillText(BookDemoUI.PHONE, "0900000000");
        String message = action.getValidationMessage(BookDemoUI.FIRST_NAME);
        Assert.assertFalse(message == null || message.isBlank());
    }

    @Test(description = "TC13 - Kiểm tra validation khi bỏ trống Company")
    public void tc13_emptyCompanyHasBrowserValidation() {
        action.enterEmail(VALID_EMAIL);
        action.fillText(BookDemoUI.FIRST_NAME, "An");
        action.fillText(BookDemoUI.LAST_NAME, "Nguyen");
        action.fillText(BookDemoUI.PHONE, "0900000000");
        String message = action.getValidationMessage(BookDemoUI.COMPANY);
        Assert.assertFalse(message == null || message.isBlank());
    }

    @Test(description = "TC14 - Kiểm tra dữ liệu form theo Excel dataDay5",
            dataProvider = "bookDemoData", dataProviderClass = ReadFileD5.class)
    public void tc14_dataDrivenFromExcel(Object email, Object firstName, Object lastName,
                                         Object company, Object phone, Object country, Object state,
                                         Object useCase, Object comments, Object expectedResult) {
        String emailValue = stringValue(email);
        String firstValue = stringValue(firstName);
        String lastValue = stringValue(lastName);
        String companyValue = stringValue(company);
        String phoneValue = stringValue(phone);
        String countryValue = stringValue(country);
        String stateValue = stringValue(state);
        String useCaseValue = stringValue(useCase);
        String commentsValue = stringValue(comments);
        String expected = stringValue(expectedResult);

        if (!emailValue.isBlank()) action.enterEmail(emailValue);

        if (emailValue.contains("@gmail.") || emailValue.contains("@yahoo.") || emailValue.contains("@outlook.")) {
            Assert.assertTrue(action.isDisplayed(BookDemoUI.BUSINESS_EMAIL_ERROR),
                    "Email cá nhân phải bị từ chối");
            return;
        }

        if (!firstValue.isBlank()) action.fillText(BookDemoUI.FIRST_NAME, firstValue);
        if (!lastValue.isBlank()) action.fillText(BookDemoUI.LAST_NAME, lastValue);
        if (!companyValue.isBlank()) action.fillText(BookDemoUI.COMPANY, companyValue);
        if (!phoneValue.isBlank()) action.fillText(BookDemoUI.PHONE, phoneValue);
        if (!countryValue.isBlank()) action.selectByText(BookDemoUI.COUNTRY, countryValue);
        if (!stateValue.isBlank()) action.selectByText(BookDemoUI.STATE, stateValue);
        if (!useCaseValue.isBlank()) action.selectByText(BookDemoUI.USE_CASE, useCaseValue);
        if (!commentsValue.isBlank()) action.fillText(BookDemoUI.COMMENTS, commentsValue);

        if ("Hợp lệ".equalsIgnoreCase(expected)) {
            Assert.assertTrue(action.isEnabled(BookDemoUI.SUBMIT),
                    "Dữ liệu được đánh dấu hợp lệ nhưng nút Submit đang bị disable");
        } else {
            Assert.assertTrue(driver.getCurrentUrl().contains("/request-demo"),
                    "Vẫn phải ở lại trang form khi dữ liệu không hợp lệ");
        }
    }

    private String stringValue(Object value) {
        return value == null ? "" : value.toString().trim();
    }

    private boolean isElementPresentAndDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

}
