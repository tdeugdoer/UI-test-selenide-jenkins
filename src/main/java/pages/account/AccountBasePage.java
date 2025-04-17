package pages.account;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AccountBasePage extends BasePage {
    private final SelenideElement
            editAccountLink = $x("//li[contains(@class, 'edit-account')]"),
            ordersAccountLink = $x("//li[contains(@class, 'orders')]/a");

}
