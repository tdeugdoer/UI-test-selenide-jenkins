package pages.account;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AccountViewOrderPage extends AccountBasePage {
    private final SelenideElement customerDetails = $x("//section[@class='woocommerce-customer-details']/address");

}
