package Day4;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class BTVND4 {
    static WebDriver driver;
    static WebDriverWait wait;

    // Hàm lấy text sau khi phần tử hiển thị
    public static String getText(By locator) {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(locator)
        ).getText().trim();
    }

    // Hàm kiểm tra kết quả
    public static void verify(String description, boolean condition) {
        if (condition) {
            System.out.println("PASS: " + description);
        } else {
            System.out.println("FAIL: " + description);
            throw new AssertionError(description);
        }
    }

    // Chuyển giá tiền từ $29.99 thành BigDecimal 29.99
    public static BigDecimal getPrice(String price) {
        return new BigDecimal(price.replace("$", "").trim());
    }

    public static void main(String[] args) {

        try {
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            driver.manage().window().maximize();

            // STEP 1: LOGIN
            driver.get("https://www.saucedemo.com/");

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("user-name")
            )).sendKeys("standard_user");

            driver.findElement(By.id("password"))
                    .sendKeys("secret_sauce");

            driver.findElement(By.id("login-button")).click();

            wait.until(ExpectedConditions.urlContains("inventory.html"));

            System.out.println("STEP 1: Login thành công");

            // STEP 2: SORT PRICE LOW TO HIGH
            WebElement sortDropdown = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.className("product_sort_container")
                    )
            );

            Select select = new Select(sortDropdown);
            select.selectByValue("lohi");

            System.out.println("STEP 2: Đã chọn Price (low to high)");

            // STEP 3: ADD 2 PRODUCTS TO CART

            List<WebElement> products = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.className("inventory_item")
                    )
            );

            // Lưu tên và giá 2 sản phẩm đầu tiên sau khi sắp xếp
            List<String> productNames = new ArrayList<>();
            List<BigDecimal> productPrices = new ArrayList<>();

            for (int i = 0; i < 2; i++) {

                WebElement product = products.get(i);

                String name = product.findElement(
                        By.className("inventory_item_name")
                ).getText().trim();

                String price = product.findElement(
                        By.className("inventory_item_price")
                ).getText().trim();

                productNames.add(name);
                productPrices.add(getPrice(price));

                // Click Add to cart
                product.findElement(By.tagName("button")).click();

                System.out.println("Đã thêm sản phẩm: " + name);
            }

            // Kiểm tra số lượng sản phẩm trên giỏ hàng
            String cartBadge = getText(By.className("shopping_cart_badge"));

            verify("Giỏ hàng hiển thị số lượng 2",
                    cartBadge.equals("2"));
            // STEP 4: CLICK CART

            driver.findElement(By.className("shopping_cart_link")).click();

            wait.until(ExpectedConditions.urlContains("cart.html"));


            System.out.println("STEP 4: Đã chuyển sang trang Your Cart");
            List<WebElement> cartItems = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.className("cart_item")
                    )
            );

            // Kiểm tra số lượng sản phẩm
            verify("Your Cart hiển thị đúng 2 sản phẩm",
                    cartItems.size() == 2);

            // Kiểm tra tên và giá sản phẩm
            for (int i = 0; i < 2; i++) {

                String actualName = cartItems.get(i).findElement(
                        By.className("inventory_item_name")
                ).getText().trim();

                String actualPrice = cartItems.get(i).findElement(
                        By.className("inventory_item_price")
                ).getText().trim();

                verify("Tên sản phẩm thứ " + (i + 1) + " chính xác",
                        actualName.equals(productNames.get(i)));

                verify("Giá sản phẩm thứ " + (i + 1) + " chính xác",
                        getPrice(actualPrice).compareTo(productPrices.get(i)) == 0);
            }

            // Kiểm tra có 2 nút Remove
            List<WebElement> removeButtons = driver.findElements(
                    By.xpath("//button[text()='Remove']")
            );

            verify("Hiển thị đúng 2 button Remove",
                    removeButtons.size() == 2);

            // Click Continue
            driver.findElement(By.id("checkout")).click();

            // STEP 5: NHẬP THÔNG TIN CHECKOUT

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.id("first-name")
            )).sendKeys("Mai");

            driver.findElement(By.id("last-name"))
                    .sendKeys("Hien");

            driver.findElement(By.id("postal-code"))
                    .sendKeys("100000");

            System.out.println("STEP 5: Đã nhập thông tin checkout");

            // STEP 6: CLICK CONTINUE

            driver.findElement(By.id("continue")).click();

            wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));

            // Kiểm tra số lượng sản phẩm
            List<WebElement> overviewItems = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(
                            By.className("cart_item")
                    )
            );

            verify("Description hiển thị đúng 2 sản phẩm",
                    overviewItems.size() == 2);

            BigDecimal itemTotal = BigDecimal.ZERO;

            // Kiểm tra tên, giá và số lượng
            for (int i = 0; i < 2; i++) {

                WebElement item = overviewItems.get(i);

                String actualName = item.findElement(
                        By.className("inventory_item_name")
                ).getText().trim();

                String actualPrice = item.findElement(
                        By.className("inventory_item_price")
                ).getText().trim();

                String actualQuantity = item.findElement(
                        By.className("cart_quantity")
                ).getText().trim();

                verify("Tên sản phẩm overview thứ " + (i + 1),
                        actualName.equals(productNames.get(i)));

                verify("Giá sản phẩm overview thứ " + (i + 1),
                        getPrice(actualPrice).compareTo(productPrices.get(i)) == 0);

                verify("Số lượng sản phẩm thứ " + (i + 1) + " bằng 1",
                        actualQuantity.equals("1"));

                itemTotal = itemTotal.add(productPrices.get(i));
            }

            // Kiểm tra Shipping Information
            String shippingInfo = getText(
                    By.xpath("//div[text()='Shipping Information:']/following-sibling::div")
            );

            verify("Shipping Information hiển thị đúng",
                    shippingInfo.equals("Free Pony Express Delivery!"));

            // Kiểm tra Item total
            String subtotalText = getText(
                    By.className("summary_subtotal_label")
            );

            BigDecimal actualSubtotal = getPrice(
                    subtotalText.replace("Item total:", "").trim()
            );

            verify("Item total hiển thị đúng tổng tiền 2 sản phẩm",
                    actualSubtotal.compareTo(itemTotal) == 0);

            // Tính Tax (8%)
            BigDecimal expectedTax = itemTotal.multiply(
                    new BigDecimal("0.08")
            ).setScale(2, RoundingMode.HALF_UP);

            String taxText = getText(By.className("summary_tax_label"));

            BigDecimal actualTax = getPrice(
                    taxText.replace("Tax:", "").trim()
            );

            verify("Tax hiển thị đúng",
                    actualTax.compareTo(expectedTax) == 0);

            // Kiểm tra Total = Item total + Tax
            BigDecimal expectedTotal = itemTotal.add(expectedTax);

            String totalText = getText(By.className("summary_total_label"));

            BigDecimal actualTotal = getPrice(
                    totalText.replace("Total:", "").trim()
            );

            verify("Total = Item total + Tax",
                    actualTotal.compareTo(expectedTotal) == 0);

            // Kiểm tra button Finish
            WebElement finishButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("finish")
                    )
            );

            verify("Button Finish hiển thị",
                    finishButton.isDisplayed());

            // STEP 7: CLICK FINISH

            finishButton.click();

            wait.until(ExpectedConditions.urlContains("checkout-complete.html"));

            // Kiểm tra tiêu đề Checkout Complete
            String pageTitle = getText(By.className("title"));

            verify("Hiển thị Checkout: Complete!",
                    pageTitle.equals("Checkout: Complete!"));

            // Kiểm tra Thank you
            String thankYou = getText(By.className("complete-header"));

            verify("Hiển thị Thank you for your order!",
                    thankYou.equals("Thank you for your order!"));

            // Kiểm tra thông báo giao hàng
            String completeMessage = getText(By.className("complete-text"));

            verify("Hiển thị thông báo giao hàng chính xác",
                    completeMessage.equals(
                            "Your order has been dispatched, and will arrive just as fast as the pony can get there!"
                    ));

            // Kiểm tra button Back Home
            WebElement backHomeButton = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.id("back-to-products")
                    )
            );

            verify("Button Back Home hiển thị",
                    backHomeButton.isDisplayed());

            System.out.println("----------------------------------");
            System.out.println("CHECKOUT SUCCESS - ALL TEST CASES PASSED");
            System.out.println("Tổng tiền sản phẩm: $" + itemTotal);
            System.out.println("Tax: $" + expectedTax);
            System.out.println("Total: $" + expectedTotal);
            System.out.println("----------------------------------");

        } catch (Exception e) {

            System.out.println("TEST FAILED: " + e.getMessage());
            e.printStackTrace();

        } finally {

            if (driver != null) {
                driver.quit();
            }
        }
    }
}
