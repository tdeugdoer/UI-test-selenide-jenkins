package ui.checkout;

import formData.CheckoutFormData;
import listeners.UIListener;
import org.assertj.core.api.SoftAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.auth.LoginPage;
import pages.cart.CartPage;
import pages.checkout.CheckoutDataFillingPage;
import pages.checkout.CheckoutOrderInformationPage;
import pages.main.MainPage;
import pages.menu.MenuPage;
import utils.DateUtils;
import utils.FailMessages;
import utils.TestConstants;
import utils.data.CheckoutData;
import utils.data.LoginData;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class CheckoutFillingDataPageTest {
    private final LoginPage loginPage = new LoginPage();
    private final MainPage mainPage = new MainPage();
    private final MenuPage menuPage = new MenuPage();
    private final CartPage cartPage = new CartPage();
    private final CheckoutDataFillingPage checkoutDataFillingPage = new CheckoutDataFillingPage();
    private final CheckoutOrderInformationPage checkoutOrderInformationPage = new CheckoutOrderInformationPage();

    private final CheckoutFormData checkoutFormData = getCheckoutFormData();

    @BeforeMethod
    public void setUp() {
        open(TestConstants.Urls.MY_ACCOUNT_URL);
        loginPage.fillOutLoginForm(LoginData.EXISTING_EMAIL, LoginData.EXISTING_PASSWORD)
                .getLoginButton().click();

        mainPage.getLinkToCart().shouldBe(clickable).click();
        cartPage.clearCart()
                .getMenuPageButton().click();

        menuPage.addToCartFirstProduct()
                .getLinkToCart().shouldBe(clickable).click();

        cartPage.loadingProductTable();
        cartPage.getProceedToPaymentButton().shouldBe(clickable).click();
    }

    @Test(description = "Установка даты заказа")
    public void orderTime() {
        String tomorrowString = DateUtils.getTomorrowDateString();

        checkoutDataFillingPage
                .fillOutOrderDetails(checkoutFormData)
                .enterOrderDate(tomorrowString)
                .getTermsCheckbox().click();
        checkoutDataFillingPage.getPlaceOrderButton().click();

        String title = checkoutOrderInformationPage
                .getPostTitle().shouldHave(text("ЗАКАЗ ПОЛУЧЕН")).getText();

        assertThat(title)
                .as(FailMessages.STRING_NOT_MATCH_EXPECTED)
                .isEqualTo("ЗАКАЗ ПОЛУЧЕН");
    }

    @Test(description = "Успешное оформление заказа с оплатой наличными")
    public void paymentInCash() {
        checkoutDataFillingPage
                .fillOutOrderDetails(checkoutFormData)
                .getPaymentInCashOnDeliveryRadioButton().click();
        checkoutDataFillingPage.getTermsCheckbox().click();
        checkoutDataFillingPage.getPlaceOrderButton().click();

        String title = checkoutOrderInformationPage
                .getPostTitle().shouldHave(text("ЗАКАЗ ПОЛУЧЕН")).getText();
        String paymentMethod = checkoutOrderInformationPage.getPaymentMethod().getText();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(title)
                    .as(FailMessages.STRING_NOT_MATCH_EXPECTED)
                    .isEqualTo("ЗАКАЗ ПОЛУЧЕН");
            softly.assertThat(paymentMethod)
                    .as(FailMessages.STRING_NOT_MATCH_EXPECTED)
                    .isEqualTo("Оплата при доставке");
        });
    }

    private CheckoutFormData getCheckoutFormData() {
        return CheckoutFormData.builder()
                .firstName(CheckoutData.FIRST_NAME)
                .lastName(CheckoutData.LAST_NAME)
                .country(CheckoutData.COUNTRY)
                .address(CheckoutData.ADDRESS)
                .city(CheckoutData.CITY)
                .state(CheckoutData.STATE)
                .postcode(CheckoutData.POSTCODE)
                .phone(CheckoutData.PHONE)
                .build();
    }

}
