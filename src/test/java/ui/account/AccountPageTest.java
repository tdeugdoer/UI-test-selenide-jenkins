package ui.account;

import formData.CheckoutFormData;
import listeners.UIListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.account.AccountBasePage;
import pages.account.AccountEditPage;
import pages.account.AccountInformationPage;
import pages.account.AccountOrdersPage;
import pages.account.AccountViewOrderPage;
import pages.auth.LoginPage;
import pages.cart.CartPage;
import pages.checkout.CheckoutDataFillingPage;
import pages.main.MainPage;
import pages.menu.MenuPage;
import utils.FileDownloader;
import utils.StringGenerator;
import utils.TestConstants;
import utils.data.CheckoutData;
import utils.data.LoginData;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class AccountPageTest {
    private final LoginPage loginPage = new LoginPage();
    private final AccountBasePage accountBasePage = new AccountBasePage();
    private final AccountEditPage accountEditPage = new AccountEditPage();
    private final AccountInformationPage accountInformationPage = new AccountInformationPage();
    private final AccountOrdersPage accountOrdersPage = new AccountOrdersPage();
    private final AccountViewOrderPage accountViewOrderPage = new AccountViewOrderPage();
    private final MainPage mainPage = new MainPage();
    private final CartPage cartPage = new CartPage();
    private final MenuPage menuPage = new MenuPage();
    private final CheckoutDataFillingPage checkoutDataFillingPage = new CheckoutDataFillingPage();


    @BeforeMethod
    public void setUp() {
        open(TestConstants.Urls.MY_ACCOUNT_URL);
        loginPage.fillOutLoginForm(LoginData.EXISTING_EMAIL, LoginData.EXISTING_PASSWORD)
                .getLoginButton().click();
    }

    @Test(description = "Загрузка файла")
    public void changeAccountImage() {
        accountBasePage.getEditAccountLink().click();
        accountEditPage.getUploadFileInput().sendKeys(FileDownloader.downloadFileToTemp("https://yt3.googleusercontent.com/ytc/AIdro_ljFZmd80sh4pvzQENH22j-J9HBKD1_hFa8hp2ga9DgtN4=s900-c-k-c0x00ffffff-no-rj",
                "Profile", ".png"));
        accountEditPage.getSaveAccountDetailsButton().click();
        String message = accountInformationPage.getMessage().getText();

        assertThat(message)
                .as("Проверка сообщения об успешном изменении данных")
                .isEqualTo("Account details changed successfully.");
    }

    @Test(description = "Создание заказа и проверка его появления в заказах на странице пользователя")
    public void checkAddingOrder() {
        CheckoutFormData checkoutFormData = getCheckoutFormData();

        mainPage.getMenuPageButton().click();

        menuPage.addToCartFirstProduct()
                .getLinkToCart().shouldBe(clickable).click();

        cartPage.loadingProductTable();
        cartPage.getProceedToPaymentButton().shouldBe(clickable).click();

        checkoutDataFillingPage.fillOutOrderDetails(checkoutFormData)
                .getTermsCheckbox().click();
        checkoutDataFillingPage.getPlaceOrderButton().click();
        checkoutDataFillingPage.getAccountPageButton().click();

        accountBasePage.getOrdersAccountLink().click();
        accountOrdersPage.redirectToFirstOrder();
        String customerDetails = accountViewOrderPage.getCustomerDetails().getText();

        assertThat(customerDetails)
                .as("Проверка, что детали заказа содержат имя пользователя")
                .contains(checkoutFormData.getFirstName());
    }

    private CheckoutFormData getCheckoutFormData() {
        return CheckoutFormData.builder()
                .firstName(StringGenerator.getUniqueString())
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
