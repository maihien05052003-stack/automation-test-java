import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginChrome {
    public static void main(String[] args) {

        // 1. Khởi tạo trình duyệt Chrome
        WebDriver driver = new ChromeDriver();

        try {
            // 2. Mở website theo yêu cầu
            driver.get("https://serenity-bdd.github.io/");

            // 3. In tiêu đề trang ra màn hình Console
            System.out.println("Tên website: " + driver.getTitle());

            // 4. Chờ 5 giây để quan sát trình duyệt
            Thread.sleep(10000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();

        } finally {
            // 5. Đóng trình duyệt sau khi hoàn thành
            driver.quit();
        }
    }
}
