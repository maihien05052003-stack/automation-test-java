package Day5;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Map;

public class BTVNDay5 {
    public static void main(String[] args) {
        String EXCEL_PATH = "dataDay5.xlsx";
        String SHEET_NAME = "Sheet1";
        String URL = "https://saucelabs.com/request-demo";

        // Đọc dữ liệu từ file Excel
        List<Map<String, String>> testData =
                ReadFileD5.readExcelData(EXCEL_PATH, SHEET_NAME);

        // Lặp qua từng dòng dữ liệu trong Excel
        for (Map<String, String> row : testData) {

            WebDriver driver = new ChromeDriver();

            try {

                driver.get(URL);
                driver.manage().window().maximize();

                System.out.println("--------------------------------");
                System.out.println("Đang chạy test case: " + row.get("Comments"));

                // Nhập Business Email
                driver.findElement(By.id("Business-Email"))
                        .sendKeys(row.get("Business Email"));

                // Nhập First Name
                driver.findElement(By.name("First Name"))
                        .sendKeys(row.get("First Name"));

                // Nhập Last Name
                driver.findElement(By.name("Last Name"))
                        .sendKeys(row.get("Last Name"));

                // Nhập Company
                driver.findElement(By.id("Company"))
                        .sendKeys(row.get("Company"));

                // Nhập Phone
                driver.findElement(By.id("Phone"))
                        .sendKeys(row.get("Phone"));

                // Chọn Country
                if (!row.get("Country").isEmpty()) {
                    Select country = new Select(
                            driver.findElement(By.id("Country"))
                    );
                    country.selectByVisibleText(row.get("Country"));
                }

                // Chọn State nếu có dữ liệu
                if (!row.get("State").isEmpty()) {
                    Select state = new Select(
                            driver.findElement(By.id("State"))
                    );
                    state.selectByVisibleText(row.get("State"));
                }

                // Chọn Use Case
                if (!row.get("Use Case").isEmpty()) {
                    Select useCase = new Select(
                            driver.findElement(By.id("Use-Case"))
                    );
                    useCase.selectByVisibleText(row.get("Use Case"));
                }

                // Nhập Comments
                driver.findElement(By.id("Comments"))
                        .sendKeys(row.get("Comments"));

                // Click Submit
                driver.findElement(
                        By.xpath("//form[@id='wf-form--2']//button[@type='submit']")
                ).click();

                System.out.println("Expected Result: " + row.get("Expected Result"));
                System.out.println("Đã thực hiện nhập dữ liệu và Submit!");

                Thread.sleep(2000);

            } catch (Exception e) {
                System.out.println("Test case bị lỗi: " + e.getMessage());

            } finally {
                driver.quit();
            }
        }
    }
}
