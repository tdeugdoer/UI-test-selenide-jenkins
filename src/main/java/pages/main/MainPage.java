package pages.main;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.Keys;
import pages.BasePage;
import utils.CustomAwait;

import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.actions;

@Getter
public class MainPage extends BasePage {
    private final SelenideElement
            pizzaSliderUl = $x("//h2[@class='prod-title' and contains(text(), 'Пицца')]/ancestor::aside/ul[@class='new-prod-slide remove-overload slick-initialized slick-slider']"),
            prevPizzaSlick = $x("//h2[@class='prod-title' and contains(text(), 'Пицца')]/ancestor::aside/ul[@class='new-prod-slide remove-overload slick-initialized slick-slider']//a[@class='slick-prev']"),
            nextPizzaSlick = $x("//h2[@class='prod-title' and contains(text(), 'Пицца')]/ancestor::aside/ul[@class='new-prod-slide remove-overload slick-initialized slick-slider']//a[@class='slick-next']");
    private final ElementsCollection
            pizzaSliderLi = $$x("//h2[@class='prod-title' and contains(text(), 'Пицца')]/ancestor::aside/ul[@class='new-prod-slide remove-overload slick-initialized slick-slider']//div[@class='slick-track']/li"),
            drinkImages = $$x("//h2[@class='prod-title' and contains(text(), 'Напитки')]/ancestor::aside//img[@class='attachment-shop_catalog size-shop_catalog wp-post-image']"),
            dessertPageLinks = $$x("//h2[@class='prod-title' and contains(text(), 'Десерты')]/ancestor::aside//img[@class='attachment-shop_catalog size-shop_catalog wp-post-image']/parent::a");

    public MainPage slideLeftPizzaSlider(Integer times) {
        IntStream.range(0, times).forEach(i -> {
            List<SelenideElement> initialItems = getVisiblePizzaInSlider();
            prevPizzaSlick.hover().click();
            waitingChangedVisibleItems(initialItems);
        });
        return this;
    }

    public MainPage slideRightPizzaSlider(Integer times) {
        IntStream.range(0, times).forEach(i -> {
            List<SelenideElement> initialItems = getVisiblePizzaInSlider();
            nextPizzaSlick.hover().click();
            waitingChangedVisibleItems(initialItems);
        });
        return this;
    }

    public MainPage slidePizzaSlider(Integer times, Keys keys) {
        pizzaSliderUl.click();
        IntStream.range(0, times).forEach(i -> {
            List<SelenideElement> initialItems = getVisiblePizzaInSlider();
            actions().sendKeys(keys).perform();
            waitingChangedVisibleItems(initialItems);
        });
        return this;
    }

    public List<SelenideElement> getVisiblePizzaInSlider() {
        return pizzaSliderLi.stream()
                .filter(el -> Objects.equals(el.getAttribute("aria-hidden"), "false"))
                .toList();
    }

    public Long getCountDrinkWithVisibleCartButtons() {
        return drinkImages.stream()
                .filter(SelenideElement::isDisplayed)
                .count();
    }

    public MainPage moveToFirstDrinkImage() {
        drinkImages.first().hover();
        return this;
    }

    public String getFirstDesertTitle() {
        return dessertPageLinks.first().getAttribute("title");
    }

    public void redirectToFirstDesert() {
        dessertPageLinks.first().click();
    }

    private void waitingChangedVisibleItems(List<SelenideElement> initialItems) {
        CustomAwait.await("Ждём изменения отображаемых элементов в слайдере")
                .until(() -> !getVisiblePizzaInSlider().equals(initialItems));
    }

}
