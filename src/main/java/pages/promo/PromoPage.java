package pages.promo;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class PromoPage extends BasePage {
    private final SelenideElement firstCoupon = $x("//div[@class='content-page']/ul/li[1]/strong");

}
