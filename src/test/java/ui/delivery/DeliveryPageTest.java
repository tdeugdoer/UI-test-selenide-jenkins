package ui.delivery;

import io.qameta.allure.Description;
import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import listeners.UIListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.delivery.DeliveryPage;
import utils.FailMessages;
import utils.TestConstants;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class DeliveryPageTest {
    private final DeliveryPage deliveryPage = new DeliveryPage();

    @BeforeMethod
    public void setUp() {
        step("Открываем страницу условиями доставки", () ->
                open(TestConstants.Urls.DELIVERY_URL)
        );
    }

    @Test(description = "Минимальная сумма доставки")
    @Description("Проверка, что на сайте в этом разделе отображается текст «Минимальная сумма заказа — 800 рублей».")
    @Severity(SeverityLevel.MINOR)
    @Owner("Yahor Tserashkevich")
    @Link(name = "Website", url = "https://pizzeria.skillbox.cc/delivery/")
    public void minDeliveryAmount() {
        step("Получение условий доставки", () -> {
            List<String> termsOfDelivery = deliveryPage.getTermsOfDelivery();

            assertThat(termsOfDelivery)
                    .as(FailMessages.EXPECTED_STRING_MISSING)
                    .contains("Минимальная сумма заказа — 800 рублей");
        });
    }

}
