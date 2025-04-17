package pages.account;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AccountInformationPage extends AccountBasePage {
    private final SelenideElement message = $x("//div[@class='woocommerce-message']");

}
