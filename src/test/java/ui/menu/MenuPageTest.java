package ui.menu;

import listeners.UIListener;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.cart.CartPage;
import pages.menu.MenuPage;
import utils.FailMessages;
import utils.TestConstants;

import java.util.List;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class MenuPageTest {
    private final MenuPage menuPage = new MenuPage();
    private final CartPage cartPage = new CartPage();

    @BeforeMethod
    public void setUp() {
        open(TestConstants.Urls.MENU_URL);
    }

    @Test(description = "Применение сортировки пицц")
    public void sorting() {
        menuPage.getPriceAscOrderingOption().click();

        List<Float> menuCardsPrices = menuPage.getMenuCardsPrices();
        List<Float> sortedCardsPrices = menuCardsPrices.stream()
                .sorted()
                .toList();

        assertThat(menuCardsPrices)
                .as(FailMessages.MENU_NOT_SORTED_BY_PRICE_ASC)
                .isEqualTo(sortedCardsPrices);
    }

    @Test(description = "Фильтрация пицц по цене")
    public void priceFiltering() {
        Integer expectedMinPrice = 300;
        Integer expectedMaxPrice = 480;

        menuPage
                .changeMinPrice(expectedMinPrice)
                .changeMaxPrice(expectedMaxPrice)
                .getPriceFilteringButton().click();
        List<Float> menuCardsPrices = menuPage.getMenuCardsPrices();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(menuCardsPrices)
                    .as(FailMessages.MENU_NOT_FILTERED_BY_MIN_PRICE)
                    .allMatch(price -> price >= expectedMinPrice);
            softly.assertThat(menuCardsPrices)
                    .as(FailMessages.MENU_NOT_FILTERED_BY_MAX_PRICE)
                    .allMatch(price -> price <= expectedMaxPrice);
        });
    }

    @Test(description = "Добавление пиццы в корзину")
    public void additionToCart() {
        menuPage.addToCartFirstProduct()
                .getLinkToCart().shouldBe(clickable).click();
        Boolean cartExist = cartPage.loadingProductTable();

        assertThat(cartExist)
                .as(FailMessages.ELEMENT_NOT_EXIST)
                .isTrue();
    }

}
