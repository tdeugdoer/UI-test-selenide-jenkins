package pages.delivery;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.WebElement;
import pages.BasePage;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.switchTo;

public class DeliveryPage extends BasePage {
    private final SelenideElement termsOfDeliveryIframe = $x("//iframe[contains(@src,'delivery.html')]");
    private final ElementsCollection termsOfDeliveryUl = $$x("//ul");

    public List<String> getTermsOfDelivery() {
        switchTo().frame(termsOfDeliveryIframe);
        List<String> termsOfDelivery = termsOfDeliveryUl.stream()
                .map(WebElement::getText)
                .toList();
        switchTo().defaultContent();
        return termsOfDelivery;
    }

}
