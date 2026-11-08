package Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day4 {
    // ================= CẤU HÌNH =================

    static final String URL =
            "https://saucelabs.com/request-demo";

    // false: Không gửi form thật
    static final boolean SUBMIT_REAL_FORM = false;

    static final String VALID_EMAIL = "hin.test@ex.com";

    static final String MSG_EMPTY_FIELD =
            "Please fill out this field.";

    static final String MSG_EMPTY_SELECT =
            "Please select an item in the list.";

    static final String MSG_BUSINESS_EMAIL =
            "Please enter a valid business email.";

    // ================= LOCATOR =================

    static final By EMAIL = By.id("Business-Email");
    static final By FIRST_NAME = By.name("First Name");
    static final By LAST_NAME = By.name("Last Name");
    static final By COMPANY = By.id("Company");
    static final By PHONE = By.id("Phone");
    static final By COUNTRY = By.id("Country");
    static final By STATE = By.id("State");
    static final By USE_CASE = By.id("Use-Case");
    static final By COMMENTS = By.id("Comments");
    static final By CONSENT = By.id("Checkbox");

    static final By SUBMIT = By.xpath(
            "//form[@id='wf-form--2']//button[@type='submit']"
    );

    static final By BUSINESS_EMAIL_ERROR = By.xpath(
            "//div[text()='" + MSG_BUSINESS_EMAIL + "']"
    );

    static WebDriver driver;

    static int passed = 0;
    static int failed = 0;

    // ================= MAIN =================

    public static void main(String[] args) {

        driver = new ChromeDriver(createOptions());

        driver.manage().window().maximize();

        try {

            // HAPPY CASE
            runTest("TC01 - Điền đầy đủ thông tin hợp lệ",
                    () -> tc01_happyCase());

            runTest("TC02 - Chọn United States và State",
                    () -> tc02_happyCaseUS());

            // UNHAPPY CASE - BỎ TRỐNG TRƯỜNG BẮT BUỘC

            runTest("TC03 - Bỏ trống Company",
                    () -> tc03_emptyCompany());

            runTest("TC04 - Bỏ trống Business Email",
                    () -> tc04_emptyEmail());

            runTest("TC05 - Bỏ trống First Name",
                    () -> tc05_emptyFirstName());

            runTest("TC06 - Bỏ trống Last Name",
                    () -> tc06_emptyLastName());

            runTest("TC07 - Bỏ trống Phone Number",
                    () -> tc07_emptyPhone());

            runTest("TC08 - Không chọn Country",
                    () -> tc08_noCountry());

            runTest("TC09 - Không chọn State khi Country là United States",
                    () -> tc09_usWithoutState());

            // UNHAPPY CASE - EMAIL KHÔNG HỢP LỆ

            runTest("TC10 - Email thiếu ký tự @",
                    () -> tc10_emailWithoutAt());

            runTest("TC11 - Email thiếu tên miền",
                    () -> tc11_emailWithoutDomain());

            runTest("TC12 - Nhập email Gmail",
                    () -> tc12_freeEmail());

            // CÁC CASE KHÁC

            runTest("TC13 - Company vượt quá 256 ký tự",
                    () -> tc13_companyMaxLength());

            runTest("TC14 - Kiểm tra Use Case mặc định",
                    () -> tc14_useCaseDefault());

            runTest("TC15 - Chọn Germany kiểm tra Checkbox",
                    () -> tc15_consentEurope());

        } finally {

            driver.quit();

        }

        System.out.println("\n========== TỔNG KẾT ==========");
        System.out.println("PASS: " + passed);
        System.out.println("FAIL: " + failed);
        System.out.println("TOTAL: " + (passed + failed));

    }

    // =================================================
    //                  TEST CASES
    // =================================================

    // TC01: HAPPY CASE
    // Nhập email công ty và điền đầy đủ 7 trường

    static void tc01_happyCase() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "AN",
                "NGUYEN",
                "QA",
                "0900000000",
                "Vietnam",
                ""
        );

        // Chọn Use Case
        new Select(driver.findElement(USE_CASE))
                .selectByVisibleText("Cross-Browser Testing");

        // Nhập Comments (không bắt buộc)
        driver.findElement(COMMENTS)
                .sendKeys("Automation testing practice");

        // Lấy form
        WebElement form = driver.findElement(
                By.id("wf-form--2")
        );

        // Kiểm tra form có hợp lệ không
        boolean isValid = (Boolean)
                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript(
                                "return arguments[0].checkValidity();",
                                form
                        );

        verify(
                isValid,
                "Form không hợp lệ"
        );

        // Kiểm tra nút Submit được bật
        verify(
                driver.findElement(SUBMIT).isEnabled(),
                "Nút Submit đang bị khóa"
        );

        // Không submit lên website thật
        if (SUBMIT_REAL_FORM) {

            driver.findElement(SUBMIT).click();

            Thread.sleep(5000);

            verify(
                    driver.getCurrentUrl().contains("/thank-you-demo"),
                    "Không chuyển đến trang cảm ơn"
            );

        } else {

            System.out.println(
                    "Không submit form thật"
            );

        }
    }

    // TC02: HAPPY CASE
    // Chọn Country = United States và chọn State

    static void tc02_happyCaseUS() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "Nguyen",
                "QA Training",
                "0900000000",
                "United States",
                "New York"
        );

        // Kiểm tra State hiển thị
        verify(
                driver.findElement(STATE).isDisplayed(),
                "Ô State không hiển thị"
        );

        // Kiểm tra State hợp lệ
        verify(
                validationMessage(STATE).isEmpty(),
                "State vẫn báo lỗi"
        );

        verify(
                driver.findElement(SUBMIT).isEnabled(),
                "Nút Submit đang bị khóa"
        );
    }

    // TC03: BỎ TRỐNG COMPANY

    static void tc03_emptyCompany() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "Nguyen",
                "",
                "0900000000",
                "Vietnam",
                ""
        );

        // Click Submit
        clickSubmit();

        // Kiểm tra thông báo validation của Company
        verifyBlockedBy(
                COMPANY,
                MSG_EMPTY_FIELD
        );
    }

    // TC04: BỎ TRỐNG BUSINESS EMAIL

    static void tc04_emptyEmail() throws InterruptedException {

        openForm();

        // Company hiển thị ngay từ đầu
        driver.findElement(COMPANY)
                .sendKeys("QA Training");

        clickSubmit();

        verifyBlockedBy(
                EMAIL,
                MSG_EMPTY_FIELD
        );

        verify(
                !driver.findElement(FIRST_NAME).isDisplayed(),
                "First Name lẽ ra phải đang ẩn"
        );
    }

    // TC05: BỎ TRỐNG FIRST NAME

    static void tc05_emptyFirstName() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "",
                "Nguyen",
                "QA Training",
                "0900000000",
                "Vietnam",
                ""
        );

        clickSubmit();

        verifyBlockedBy(
                FIRST_NAME,
                MSG_EMPTY_FIELD
        );
    }

    // TC06: BỎ TRỐNG LAST NAME

    static void tc06_emptyLastName() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "",
                "QA Training",
                "0900000000",
                "Vietnam",
                ""
        );

        clickSubmit();

        verifyBlockedBy(
                LAST_NAME,
                MSG_EMPTY_FIELD
        );
    }

    // TC07: BỎ TRỐNG PHONE NUMBER

    static void tc07_emptyPhone() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "Nguyen",
                "QA Training",
                "",
                "Vietnam",
                ""
        );

        clickSubmit();

        verifyBlockedBy(
                PHONE,
                MSG_EMPTY_FIELD
        );
    }

    // TC08: KHÔNG CHỌN COUNTRY

    static void tc08_noCountry() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "Nguyen",
                "QA Training",
                "0900000000",
                "",
                ""
        );

        clickSubmit();

        verifyBlockedBy(
                COUNTRY,
                MSG_EMPTY_SELECT
        );
    }

    // TC09: UNITED STATES NHƯNG KHÔNG CHỌN STATE

    static void tc09_usWithoutState() throws InterruptedException {

        openForm();

        fillForm(
                VALID_EMAIL,
                "An",
                "Nguyen",
                "QA Training",
                "0900000000",
                "United States",
                ""
        );

        clickSubmit();

        verifyBlockedBy(
                STATE,
                MSG_EMPTY_SELECT
        );
    }

    // TC10: EMAIL THIẾU KÝ TỰ @

    static void tc10_emailWithoutAt() throws InterruptedException {

        openForm();

        enterEmail("abc");

        clickSubmit();

        String message = validationMessage(EMAIL);

        verify(
                isStillOnForm(),
                "Form không được gửi"
        );

        verify(
                message.contains("missing an '@'"),
                "Thông báo email không đúng: " + message
        );

        verify(
                !driver.findElement(FIRST_NAME).isDisplayed(),
                "First Name lẽ ra phải đang ẩn"
        );
    }

    // TC11: EMAIL THIẾU TÊN MIỀN

    static void tc11_emailWithoutDomain() throws InterruptedException {

        openForm();

        enterEmail("abc@");

        clickSubmit();

        String message = validationMessage(EMAIL);

        verify(
                isStillOnForm(),
                "Form không được gửi"
        );

        verify(
                message.contains("following '@'"),
                "Thông báo email không đúng: " + message
        );
    }

    // TC12: EMAIL GMAIL
    // Kiểm tra thông báo lỗi và nút Submit bị khóa

    static void tc12_freeEmail() throws InterruptedException {

        openForm();

        enterEmail("qa.test@gmail.com");

        // Kiểm tra thông báo email cá nhân
        WebElement error = driver.findElement(
                BUSINESS_EMAIL_ERROR
        );

        verify(
                error.isDisplayed(),
                "Không hiển thị lỗi Business Email"
        );

        // Kiểm tra nút Submit bị khóa
        WebElement button = driver.findElement(SUBMIT);

        verify(
                !button.isEnabled(),
                "Nút Submit lẽ ra phải bị khóa khi dùng Gmail"
        );

        verify(
                !driver.findElement(FIRST_NAME).isDisplayed(),
                "First Name lẽ ra phải đang ẩn"
        );
    }

    // TC13: COMPANY VƯỢT QUÁ 256 KÝ TỰ

    static void tc13_companyMaxLength() throws InterruptedException {

        openForm();

        String text300 = "A".repeat(300);

        WebElement company = driver.findElement(COMPANY);

        company.sendKeys(text300);

        int actualLength = company
                .getAttribute("value")
                .length();

        verify(
                actualLength == 256,
                "Độ dài thực tế = " + actualLength
                        + ", mong đợi = 256"
        );
    }

    // TC14: KIỂM TRA USE CASE MẶC ĐỊNH

    static void tc14_useCaseDefault() throws InterruptedException {

        openForm();

        Select useCase = new Select(
                driver.findElement(USE_CASE)
        );

        // Kiểm tra giá trị mặc định
        String defaultOption = useCase
                .getFirstSelectedOption()
                .getText();

        verify(
                defaultOption.equals("AI Test Authoring"),
                "Giá trị mặc định không đúng: " + defaultOption
        );

        // Kiểm tra số lượng lựa chọn
        List<WebElement> options = useCase.getOptions();

        verify(
                options.size() == 12,
                "Số lựa chọn thực tế = " + options.size()
        );

        // Kiểm tra không có lựa chọn trống
        for (WebElement option : options) {

            verify(
                    !option.getAttribute("value").isEmpty(),
                    "Có lựa chọn trống trong Use Case"
            );

        }
    }

    // TC15: CHỌN GERMANY - KIỂM TRA CHECKBOX

    static void tc15_consentEurope() throws InterruptedException {

        openForm();

        enterEmail(VALID_EMAIL);

        // Ban đầu Checkbox đang ẩn
        verify(
                !driver.findElement(CONSENT).isDisplayed(),
                "Checkbox lẽ ra đang ẩn"
        );

        // Chọn Germany
        new Select(driver.findElement(COUNTRY))
                .selectByVisibleText("Germany");

        Thread.sleep(1000);

        WebElement consent = driver.findElement(CONSENT);

        // Kiểm tra Checkbox hiển thị
        verify(
                consent.isDisplayed(),
                "Checkbox không hiển thị khi chọn Germany"
        );

        // Kiểm tra Checkbox mặc định chưa tick
        verify(
                !consent.isSelected(),
                "Checkbox lẽ ra chưa được tick"
        );
    }

    // =================================================
    //                  HÀM HỖ TRỢ
    // =================================================

    // Cấu hình Chrome sử dụng tiếng Anh
    static ChromeOptions createOptions() {

        ChromeOptions options = new ChromeOptions();

        options.addArguments("--lang=en-US");

        Map<String, Object> prefs = new HashMap<>();

        prefs.put(
                "intl.accept_languages",
                "en-US,en"
        );

        options.setExperimentalOption(
                "prefs",
                prefs
        );

        return options;
    }

    // Mở trang Book Your Demo
    static void openForm() throws InterruptedException {

        driver.get(URL);

        Thread.sleep(3000);

    }

    // Nhập email và chờ form hiển thị
    static void enterEmail(String email) throws InterruptedException {

        WebElement emailBox = driver.findElement(EMAIL);

        emailBox.clear();

        emailBox.sendKeys(email);

        // Click sang Company để kích hoạt kiểm tra email
        driver.findElement(COMPANY).click();

        Thread.sleep(1500);

    }

    // Nhập dữ liệu vào form
    // Truyền chuỗi rỗng "" nếu muốn bỏ trống trường

    static void fillForm(
            String email,
            String firstName,
            String lastName,
            String company,
            String phone,
            String country,
            String state
    ) throws InterruptedException {

        // Nhập email trước
        if (!email.isEmpty()) {

            enterEmail(email);

        }

        typeIfNotEmpty(FIRST_NAME, firstName);

        typeIfNotEmpty(LAST_NAME, lastName);

        typeIfNotEmpty(COMPANY, company);

        typeIfNotEmpty(PHONE, phone);

        // Chọn Country
        if (!country.isEmpty()) {

            new Select(driver.findElement(COUNTRY))
                    .selectByVisibleText(country);

            Thread.sleep(1000);

        }

        // Chọn State nếu có
        if (!state.isEmpty()) {

            new Select(driver.findElement(STATE))
                    .selectByVisibleText(state);

        }
    }

    // Nhập dữ liệu nếu không phải chuỗi rỗng
    static void typeIfNotEmpty(By locator, String text) {

        if (!text.isEmpty()) {

            WebElement element = driver.findElement(locator);

            element.clear();

            element.sendKeys(text);

        }
    }

    // Click nút Submit
    static void clickSubmit() throws InterruptedException {

        driver.findElement(SUBMIT).click();

        Thread.sleep(1500);

    }

    // Lấy thông báo validation của trường
    static String validationMessage(By locator) {

        WebElement element = driver.findElement(locator);

        return (String) (
                (org.openqa.selenium.JavascriptExecutor) driver
        ).executeScript(
                "return arguments[0].validationMessage;",
                element
        );
    }

    // Kiểm tra vẫn đang ở trang form
    static boolean isStillOnForm() {

        return driver.getCurrentUrl().contains("/request-demo");

    }

    // Kiểm tra form bị chặn bởi validation
    static void verifyBlockedBy(
            By field,
            String expectedMessage
    ) {

        verify(
                isStillOnForm(),
                "Form không được gửi. URL: " + driver.getCurrentUrl()
        );

        String actualMessage = validationMessage(field);

        verify(
                actualMessage.equals(expectedMessage),
                "Thông báo mong đợi: '" + expectedMessage
                        + "' - Thực tế: '" + actualMessage + "'"
        );
    }

    // Kiểm tra điều kiện
    static void verify(
            boolean condition,
            String failMessage
    ) {

        if (!condition) {

            throw new AssertionError(failMessage);

        }
    }

    // Interface chạy test case
    interface TestCase {

        void run() throws Exception;

    }

    // Chạy từng test case và thống kê kết quả
    static void runTest(
            String name,
            TestCase testCase
    ) {

        try {

            testCase.run();

            passed++;

            System.out.println(
                    "PASS - " + name
            );

        } catch (AssertionError e) {

            failed++;

            System.out.println(
                    "FAIL - " + name
                            + "\nLý do: " + e.getMessage()
            );

        } catch (Exception e) {

            failed++;

            System.out.println(
                    "ERROR - " + name
                            + "\nLý do: " + e.getClass().getSimpleName()
                            + ": " + e.getMessage()
            );

        }
    }
}
