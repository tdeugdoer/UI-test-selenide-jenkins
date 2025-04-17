package listeners;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.awaitility.Awaitility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.TestConstants;

import java.time.Duration;

public class UIListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        Configuration.baseUrl = System.getProperty("base.url", TestConstants.Urls.BASE_URL);
        Configuration.browser = System.getProperty("browser", Browsers.EDGE);
        Configuration.browserSize = "1920x1080";
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", Boolean.TRUE.toString()));

        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
        Awaitility.setDefaultPollDelay(Duration.ofMillis(500));

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(Boolean.parseBoolean(System.getProperty("allure.screenshots", Boolean.TRUE.toString())))
                .savePageSource(Boolean.parseBoolean(System.getProperty("allure.page.sources", Boolean.TRUE.toString())))
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

}
