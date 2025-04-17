package ui.cart;

import io.qameta.allure.Link;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import listeners.UIListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.cart.CartPage;
import pages.checkout.CheckoutBasePage;
import pages.main.MainPage;
import pages.menu.MenuPage;
import pages.promo.PromoPage;
import utils.FailMessages;
import utils.TestConstants;
import utils.data.LoginData;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class CartPageTest {
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final CartPage cartPage = new CartPage();
    private final MenuPage menuPage = new MenuPage();
    private final CheckoutBasePage checkoutBasePage = new CheckoutBasePage();
    private final PromoPage promoPage = new PromoPage();

    @BeforeMethod
    public void setUp() {
        step("Аутентификация", () -> {
            open(TestConstants.Urls.MY_ACCOUNT_URL);
            loginPage.fillOutLoginForm(LoginData.EXISTING_EMAIL, LoginData.EXISTING_PASSWORD)
                    .getLoginButton().click();
        });
        step("Переходим в корзину", () ->
                mainPage.getLinkToCart().shouldBe(clickable).click()
        );
        step("Очищаем корзину и переходим на страницу меню", () ->
                cartPage.clearCart().getMenuPageButton().click()
        );
        step("Добавление продукта в корзину и переход в неё", () ->
                menuPage.addToCartFirstProduct().getLinkToCart().shouldBe(clickable).click()
        );
        step("Удаления имеющихся купонов", () -> {
            cartPage.loadingProductTable();
            cartPage.tryRemoveCoupons();
        });
    }

    @AfterMethod
    public void tearDown() {
        step("Очистка корзины", () -> {
            open(TestConstants.Urls.CART_URL);
            cartPage.clearCart();
        });
    }

    @Test(description = "Увеличение и уменьшение количества товара")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Yahor Tserashkevich")
    @Link(name = "Website", url = "https://pizzeria.skillbox.cc/cart/")
    public void changeProductQuantity() {
        step("Увеличение количества первого продукта в корзине на один", () -> {
            Integer beforeQuantity = cartPage.getFirstProductQuantity();
            cartPage.enterFirstProductQuantity(beforeQuantity + 1);
            Integer afterQuantity = cartPage.getFirstProductQuantity();

            assertThat(afterQuantity)
                    .as(FailMessages.NUMBER_NOT_MATCH_EXPECTED)
                    .isEqualTo(beforeQuantity + 1);
        });
    }

    @Test(description = "Обновление корзины после изменения содержимого")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Yahor Tserashkevich")
    @Link(name = "Website", url = "https://pizzeria.skillbox.cc/cart/")
    public void updateAfterChanges() {
        step("Обновления количество продуктов в корзине", () -> {
            Integer beforeTotalCartAmount = cartPage.getFirstProductQuantity();
            cartPage.enterFirstProductQuantity(cartPage.getFirstProductQuantity() + 1)
                    .getUpdateCartButton().click();
            cartPage.getTotalCartAmount();
            Integer afterTotalCartAmount = cartPage.getFirstProductQuantity();

            assertThat(afterTotalCartAmount)
                    .as(FailMessages.PRICE_SHOULD_INCREASE)
                    .isGreaterThan(beforeTotalCartAmount);
        });
    }

    @Test(description = "Переход к оплате (авторизованный пользователь)")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Yahor Tserashkevich")
    @Link(name = "Website", url = "https://pizzeria.skillbox.cc/cart/")
    public void proceedToCheckoutForLoggedInUser() {
        step("Переход к оплате заказа", () -> {
            cartPage.getProceedToPaymentButton().shouldBe(clickable).click();
            String message = checkoutBasePage.getPostTitle().getText();

            assertThat(message)
                    .as(FailMessages.STRING_NOT_MATCH_EXPECTED)
                    .isEqualTo("ОФОРМЛЕНИЕ ЗАКАЗА");
        });
    }

    @Test(description = "Применение промокода из раздела «Акции»")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Yahor Tserashkevich")
    @Link(name = "Website", url = "https://pizzeria.skillbox.cc/cart/")
    public void applyPromoCodeFromSalesSection() {
        step("Получение и применение промокода", () -> {
            cartPage.getPromoPageButton().click();

            String coupon = promoPage.getFirstCoupon().getText();
            promoPage.getLinkToCart().shouldBe(clickable).click();

            Float beforeTotalCartAmount = cartPage.getTotalCartAmount();
            cartPage
                    .enterCouponCode(coupon)
                    .getApplyCouponButton().shouldBe(interactable).click();
            Float afterTotalCartAmount = cartPage.getTotalCartAmount();

            assertThat(afterTotalCartAmount)
                    .as(FailMessages.PRICE_SHOULD_DECREASE)
                    .isLessThan(beforeTotalCartAmount);
        });
    }

}
