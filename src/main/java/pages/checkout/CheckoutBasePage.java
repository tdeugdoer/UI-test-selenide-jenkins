package pages.checkout;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class CheckoutBasePage extends BasePage {
    private final SelenideElement postTitle = $x("//h2[@class='post-title']");

}
