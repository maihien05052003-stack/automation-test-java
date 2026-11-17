
package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class DetailedTestReport implements ITestListener {

    private static ExtentReports extent;
    private static ExtentTest test;

    // Khởi tạo báo cáo khi bắt đầu chạy TestNG
    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter spark =
                new ExtentSparkReporter("test-output/ExtentReport.html");

        extent = new ExtentReports();
        extent.attachReporter(spark);

        extent.setSystemInfo("Tester", "Mai Hien");
        extent.setSystemInfo("Framework", "Selenium + TestNG");
    }

    // Khi test thành công
    @Override
    public void onTestSuccess(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.pass("Test Passed");
    }

    // Khi test thất bại
    @Override
    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.fail(result.getThrowable());
    }

    // Khi test bị bỏ qua
    @Override
    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.skip("Test Skipped");
    }

    // Kết thúc toàn bộ quá trình chạy
    @Override
    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
        }
    }
}
