package pages.bonus;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class BonusPage extends BasePage {
    private final SelenideElement
            usernameInput = $x("//input[@id='bonus_username']"),
            phoneInput = $x("//input[@id='bonus_phone']"),
            submitButton = $x("//button[@type='submit' and @name='bonus']"),
            resultMessage = $x("//div[@id='bonus_main']/h3");

    public BonusPage fillOutBonusForm(String username, String phone) {
        enterField(usernameInput, username);
        enterField(phoneInput, phone);
        return this;
    }

}
