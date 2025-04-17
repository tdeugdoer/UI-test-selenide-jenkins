package pages.checkout;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class CheckoutOrderInformationPage extends CheckoutBasePage {
    private final SelenideElement paymentMethod = $x("//li[@class='woocommerce-order-overview__payment-method method']/strong");

}
