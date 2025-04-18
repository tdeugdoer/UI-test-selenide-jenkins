package listeners;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.awaitility.Awaitility;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.TestConstants;

import java.time.Duration;

public class UIListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        Configuration.baseUrl = System.getProperty("base_url", TestConstants.Urls.BASE_URL);
        Configuration.browser = System.getProperty("browser", Browsers.EDGE);
        Configuration.browserSize = "1920x1080";

        if (System.getProperty("execution", "").equals("jenkins") ? Boolean.TRUE : Boolean.FALSE) {
            Configuration.headless = true;

            switch (Configuration.browser.toLowerCase()) {
                case "chrome":
                    Configuration.browserCapabilities = getChromeOptions();
                    break;
                case "edge":
                    Configuration.browserCapabilities = getEdgeOptions();
                    break;
            }


        }

        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
        Awaitility.setDefaultPollDelay(Duration.ofMillis(500));

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(Boolean.parseBoolean(System.getProperty("allure_screenshots", Boolean.TRUE.toString())))
                .savePageSource(Boolean.parseBoolean(System.getProperty("allure_page_sources", Boolean.TRUE.toString())))
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        clearBrowser();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        clearBrowser();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        clearBrowser();
    }

    private void clearBrowser() {
        Selenide.clearBrowserCookies();
        Selenide.clearBrowserLocalStorage();
    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "enable-automation",
                "--lang=en-US",
                "--test-type",
                "--remote-allow-origins=*",
                "--start-maximized",
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu"
        );
        return options;
    }

    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments(
                "enable-automation",
                "--lang=en-US",
                "--remote-allow-origins=*",
                "--start-maximized",
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu"
        );
        return options;
    }

}
