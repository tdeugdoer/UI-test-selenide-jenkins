package listeners;

import com.codeborne.selenide.Browsers;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.awaitility.Awaitility;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.CustomAwait;
import utils.TestConstants;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.open;

public class UIListener implements ITestListener {
    @Override
    public void onStart(ITestContext context) {
        Configuration.baseUrl = System.getProperty("base_url", TestConstants.Urls.BASE_URL);
        Configuration.browser = System.getProperty("browser", Browsers.EDGE);
        Configuration.browserSize = "1920x1080";
        Configuration.headless = System.getProperty("execution", "").equals("jenkins") ? Boolean.TRUE : Boolean.FALSE;
        Awaitility.setDefaultPollInterval(Duration.ofMillis(500));
        Awaitility.setDefaultPollDelay(Duration.ofMillis(500));

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(Boolean.parseBoolean(System.getProperty("allure_screenshots", Boolean.TRUE.toString())))
                .savePageSource(Boolean.parseBoolean(System.getProperty("allure_page_sources", Boolean.TRUE.toString())))
        );
        CustomAwait.await().until(() -> {
            open(TestConstants.Urls.BONUS_URL);
            boolean b = WebDriverRunner.hasWebDriverStarted();
            System.out.println("----------------------------------------------------");
            System.out.println(b);
            System.out.println("----------------------------------------------------");
            return b;
        });
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
