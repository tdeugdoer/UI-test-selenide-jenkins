package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.executeJavaScript;

@Getter
public abstract class BasePage {
    private final SelenideElement logoutButton = $x("//a[@class='logout']");
    private final SelenideElement menuPageButton = $x("//li[contains(@class, 'menu-item menu-item-type-taxonomy menu-item-object-product_cat menu-item-has-children')]/a");
    private final ElementsCollection menuCategoryButtons = $$x("//ul[@class='sub-menu']//a");
    private final SelenideElement accountPageButton = $x("//ul[@id='menu-primary-menu']//a[text()='Мой аккаунт']");
    private final SelenideElement promoPageButton = $x("//ul[@id='menu-primary-menu']//a[text()='Акции']");
    private final SelenideElement linkToCart = $x("//a[@class='cart-contents wcmenucart-contents']");
    private final SelenideElement topScrollArrow = $x("//div[@id='ak-top']");
    private final SelenideElement instagramButton = $x("//div[@class='banner-text wow fadeInLeft']//a[text()='Instagram']");

    protected Integer parseIntPriceValue(String priceValue) {
        return Integer.parseInt(priceValue.replace("₽", ""));
    }

    protected Float parseFloatPriceValue(String priceValue) {
        return Float.parseFloat(priceValue
                .replace(",", ".")
                .replace("₽", ""));
    }

    protected void enterField(SelenideElement inputField, String value) {
        inputField.clear();
        inputField.sendKeys(value);
    }

    public void clickMenuCategoryButton(String category) {
        menuPageButton.hover();
        menuCategoryButtons.stream()
                .filter(button -> button.getText().equalsIgnoreCase(category))
                .findFirst()
                .ifPresent(SelenideElement::click);
    }

    public Boolean isTopScrollArrowVisible() {
        executeJavaScript("window.scrollBy(0,100)");
        return topScrollArrow.shouldBe(visible).isDisplayed();
    }

}
