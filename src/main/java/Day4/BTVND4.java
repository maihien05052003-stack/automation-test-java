package Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BTVND4 {
    static WebDriver driver;
    static WebDriverWait wait;

    // Lưu tên và giá của 2 sản phẩm đã thêm vào giỏ để đối chiếu ở các bước sau
    static String[] names = new String[2];
    static double[] prices = new double[2];

    // ===================== HÀM DÙNG CHUNG =====================

    // Chờ phần tử hiển thị rồi trả về phần tử đó
    static WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // Chờ nhiều phần tử hiển thị rồi trả về danh sách
    static List<WebElement> findAll(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    // Lấy text của phần tử
    static String text(By locator) {
        return find(locator).getText().trim();
    }

    // Đổi "Item total: $59.98" hoặc "$29.99" thành số 59.98 / 29.99
    static double toMoney(String s) {
        return Double.parseDouble(s.replaceAll("[^0-9.]", ""));
    }

    // So sánh 2 số tiền (sai lệch nhỏ hơn 1 cent thì coi là bằng nhau)
    static boolean same(double a, double b) {
        return Math.abs(a - b) < 0.01;
    }

    // In PASS/FAIL ra màn hình
    static void check(String moTa, boolean ketQua) {
        System.out.println((ketQua ? "PASS: " : "FAIL: ") + moTa);
    }

    // ===================== CÁC STEP =====================

    // Step 1: Login
    static void login() {
        driver.get("https://www.saucedemo.com/");
        find(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        wait.until(ExpectedConditions.urlContains("inventory.html"));
    }

    // Step 2: Sắp xếp Price (low to high)
    static void sortLowToHigh() {
        new Select(find(By.className("product_sort_container"))).selectByValue("lohi");
    }

    // Step 3: Thêm 2 sản phẩm đầu tiên vào giỏ
    static void addTwoProducts() {
        List<WebElement> products = findAll(By.className("inventory_item"));

        for (int i = 0; i < 2; i++) {
            WebElement p = products.get(i);
            names[i] = p.findElement(By.className("inventory_item_name")).getText().trim();
            prices[i] = toMoney(p.findElement(By.className("inventory_item_price")).getText());
            p.findElement(By.tagName("button")).click();
        }

        check("Giỏ hàng hiển thị số 2", text(By.className("shopping_cart_badge")).equals("2"));
    }

    // Kiểm tra danh sách sản phẩm (dùng chung cho trang Your Cart và trang Overview)
    static void checkProductList(String manHinh) {
        List<WebElement> items = findAll(By.className("cart_item"));
        check(manHinh + " hiển thị đúng 2 sản phẩm", items.size() == 2);

        for (int i = 0; i < 2; i++) {
            String name = items.get(i).findElement(By.className("inventory_item_name")).getText().trim();
            double price = toMoney(items.get(i).findElement(By.className("inventory_item_price")).getText());
            String qty = items.get(i).findElement(By.className("cart_quantity")).getText().trim();

            check(manHinh + " - sản phẩm " + (i + 1) + ": tên đúng", name.equals(names[i]));
            check(manHinh + " - sản phẩm " + (i + 1) + ": giá đúng", same(price, prices[i]));
            check(manHinh + " - sản phẩm " + (i + 1) + ": số lượng = 1", qty.equals("1"));
        }
    }

    // Step 4: Vào giỏ hàng và kiểm tra
    static void openCartAndCheck() {
        driver.findElement(By.className("shopping_cart_link")).click();
        wait.until(ExpectedConditions.urlContains("cart.html"));

        checkProductList("Your Cart");

        int soNutRemove = driver.findElements(By.xpath("//button[text()='Remove']")).size();
        check("Hiển thị 2 button Remove", soNutRemove == 2);
    }

    // Step 5: Bấm Checkout và nhập thông tin
    static void fillCheckoutInfo() {
        driver.findElement(By.id("checkout")).click();
        find(By.id("first-name")).sendKeys("Mai");
        driver.findElement(By.id("last-name")).sendKeys("Hien");
        driver.findElement(By.id("postal-code")).sendKeys("100000");
    }

    // Step 6: Bấm Continue và kiểm tra trang Overview
    static void continueAndCheckOverview() {
        driver.findElement(By.id("continue")).click();
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));

        checkProductList("Overview");

        String shipping = text(By.xpath("//div[text()='Shipping Information:']/following-sibling::div"));
        check("Shipping Information = Free Pony Express Delivery!",
                shipping.equals("Free Pony Express Delivery!"));

        double itemTotal = prices[0] + prices[1];
        double tax = Math.round(itemTotal * 0.08 * 100) / 100.0;   // thuế 8%, làm tròn 2 số

        check("Item total đúng tổng tiền 2 sản phẩm",
                same(toMoney(text(By.className("summary_subtotal_label"))), itemTotal));
        check("Tax đúng (8%)",
                same(toMoney(text(By.className("summary_tax_label"))), tax));
        check("Total = Item total + Tax",
                same(toMoney(text(By.className("summary_total_label"))), itemTotal + tax));

        check("Button Finish hiển thị", find(By.id("finish")).isDisplayed());
    }

    // Step 7: Bấm Finish và kiểm tra trang Complete
    static void finishAndCheckComplete() {
        driver.findElement(By.id("finish")).click();
        wait.until(ExpectedConditions.urlContains("checkout-complete.html"));

        check("Hiển thị Checkout: Complete!",
                text(By.className("title")).equals("Checkout: Complete!"));
        check("Hiển thị Thank you for your order!",
                text(By.className("complete-header")).equals("Thank you for your order!"));
        check("Hiển thị thông báo giao hàng",
                text(By.className("complete-text")).equals(
                        "Your order has been dispatched, and will arrive just as fast as the pony can get there!"));
        check("Hiển thị button Back Home", find(By.id("back-to-products")).isDisplayed());
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();

        try {
            login();                      // Step 1
            sortLowToHigh();              // Step 2
            addTwoProducts();             // Step 3
            openCartAndCheck();           // Step 4
            fillCheckoutInfo();           // Step 5
            continueAndCheckOverview();   // Step 6
            finishAndCheckComplete();     // Step 7
        } finally {
            driver.quit();
        }
    }
}
