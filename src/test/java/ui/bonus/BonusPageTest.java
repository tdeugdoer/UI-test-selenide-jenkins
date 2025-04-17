package ui.bonus;

import listeners.UIListener;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.bonus.BonusPage;
import utils.TestConstants;
import utils.data.BonusData;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.confirm;
import static com.codeborne.selenide.Selenide.open;

@Listeners(UIListener.class)
public class BonusPageTest {
    private final BonusPage bonusPage = new BonusPage();

    @BeforeMethod
    public void setUp() {
        open(TestConstants.Urls.BONUS_URL);
    }

    @Test(description = "Успешное оформление карты с проверкой текста «Заявка отправлена, дождитесь, пожалуйста, оформления карты!»")
    public void createBonusCard() {
        bonusPage.fillOutBonusForm(BonusData.USERNAME, BonusData.PHONE_NUMBER)
                .getSubmitButton().click();
        String alertMessage = confirm();
        String resultMessage = bonusPage.getResultMessage().shouldBe(visible, Duration.ofSeconds(10)).getText();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(alertMessage)
                    .as("Проверка сообщения о статусе заявки")
                    .isEqualTo("Заявка отправлена, дождитесь, пожалуйста, оформления карты!");
            softly.assertThat(resultMessage)
                    .as("Проверка финального статуса карты")
                    .isEqualTo("Ваша карта оформлена!");
        });
    }


}
