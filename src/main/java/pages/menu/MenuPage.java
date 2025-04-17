package pages.menu;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.json.JsonException;
import pages.BasePage;
import utils.Constants;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;

@Getter
public class MenuPage extends BasePage {
    private final ElementsCollection menuCardsPrices = $$x("//div[@class='price-cart']/descendant::bdi");
    private final ElementsCollection addToCartButtons = $$x("//a[@class='button product_type_simple add_to_cart_button ajax_add_to_cart']");
    private final SelenideElement minPriceSlider = $x("//span[contains(@class, 'ui-slider-handle ui-state-default ui-corner-all')][1]");
    private final SelenideElement maxPriceSlider = $x("//span[contains(@class, 'ui-slider-handle ui-state-default ui-corner-all')][2]");
    private final SelenideElement minPriceValue = $x("//span[@class='from']");
    private final SelenideElement maxPriceValue = $x("//span[@class='to']");
    private final SelenideElement priceFilteringButton = $x("//div[@class='price_slider_amount']/button[@type='submit']");
    private final SelenideElement priceAscOrderingOption = $x("//option[@value='price']");
    private final SelenideElement title = $x("//h1[@class='entry-title ak-container']");

    public List<Float> getMenuCardsPrices() {
        return menuCardsPrices.stream()
                .map(SelenideElement::getText)
                .map(this::parseFloatPriceValue)
                .toList();
    }

    public MenuPage addToCartFirstProduct() {
        if (!addToCartButtons.isEmpty()) {
            addToCartButtons.first().click();
        } else throw new IllegalStateException(Constants.ExceptionMessage.UNABLE_ADD_TO_CART_ERROR);

        return this;
    }

    public MenuPage changeMinPrice(Integer newValue) {
        if (newValue < 0) {
            throw new IllegalStateException(Constants.ExceptionMessage.NEGATIVE_PRICE_ERROR);
        }

        if (newValue > parseIntPriceValue(minPriceValue.getText())) {
            increasePriceValueToValue(minPriceSlider, minPriceValue, newValue);
        } else throw new IllegalStateException(Constants.ExceptionMessage.REDUCTION_MIN_PRICE_ERROR);

        return this;
    }

    public MenuPage changeMaxPrice(Integer newValue) {
        if (newValue < 0) {
            throw new IllegalStateException(Constants.ExceptionMessage.NEGATIVE_PRICE_ERROR);
        }

        if (newValue < parseIntPriceValue(maxPriceValue.getText())) {
            reducePriceValueToValue(maxPriceSlider, maxPriceValue, newValue);
        } else throw new IllegalStateException(Constants.ExceptionMessage.INCREASE_MAX_PRICE_ERROR);

        return this;
    }

    private void increasePriceValueToValue(SelenideElement slider, SelenideElement priceValue, Integer newValue) {
        while (!parseIntPriceValue(priceValue.getText()).equals(newValue)) {
            moveRightSlider(slider);
        }
    }

    private void reducePriceValueToValue(SelenideElement slider, SelenideElement priceValue, Integer newValue) {
        while (!parseIntPriceValue(priceValue.getText()).equals(newValue)) {
            moveLeftSlider(slider);
        }
    }

    private void moveRightSlider(SelenideElement slider) {
        try {
            actions()
                    .dragAndDropBy(slider, 1, 0)
                    .perform();
        } catch (JsonException e) {
            throw new IllegalStateException(Constants.ExceptionMessage.SLIDER_OUT_OF_BOUNDS_ERROR);
        }
    }

    private void moveLeftSlider(SelenideElement slider) {
        try {
            actions()
                    .clickAndHold(slider)
                    .sendKeys(Keys.ARROW_LEFT)
                    .release()
                    .perform();
        } catch (JsonException e) {
            throw new IllegalStateException(Constants.ExceptionMessage.SLIDER_OUT_OF_BOUNDS_ERROR);
        }
    }

}
