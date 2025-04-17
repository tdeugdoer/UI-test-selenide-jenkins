package pages.cart;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.awaitility.core.ConditionTimeoutException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import utils.CustomAwait;

import java.util.Objects;

import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.interactable;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.refresh;

@Getter
public class CartPage extends BasePage {
    private final SelenideElement
            productTable = $x("//table[contains(@class,'shop_table')]"),
            updateCartButton = $x("//button[@name='update_cart']"),
            totalCartAmount = $x("//tr[@class='order-total']//span[contains(@class,'amount')]"),
            proceedToPaymentButton = $x("//a[contains(@class,'checkout-button')]"),
            couponCodeInput = $x("//input[@id='coupon_code']"),
            applyCouponButton = $x("//button[@name='apply_coupon']");
    private final ElementsCollection
            removeButtons = $$x("//a[@class='remove']"),
            quantityInputs = $$x("//div[@class='quantity']//input"),
            removeCouponButtons = $$x("//a[contains(@class,'remove-coupon')]");


    public Integer getFirstProductQuantity() {
        return Integer.valueOf(Objects.requireNonNull(quantityInputs.first().getDomProperty("value")));
    }

    public CartPage enterFirstProductQuantity(Integer quantity) {
        SelenideElement firstQuantityInput = quantityInputs.first();
        CustomAwait.await("Ожидание очистки input number")
                .until(() -> {
                    firstQuantityInput.clear();
                    return firstQuantityInput.is(empty);
                });
        firstQuantityInput.sendKeys(String.valueOf(quantity));
        return this;
    }

    public Float getTotalCartAmount() {
        return parseFloatPriceValue(totalCartAmount.getText());
    }

    public CartPage enterCouponCode(String couponCode) {
        enterField(couponCodeInput, couponCode);
        couponCodeInput.sendKeys(Keys.ENTER);
        return this;
    }

    public CartPage tryRemoveCoupons() {
        try {
            removeCouponButtons.forEach(WebElement::click);
        } catch (NoSuchElementException ignored) {
        }
        return this;
    }

    public Boolean loadingProductTable() {
        try {
            CustomAwait.await("Ждем отображения корзины")
                    .until(() -> {
                        if (productTable.isDisplayed()) {
                            return true;
                        }
                        refresh();
                        return false;
                    });
            return true;
        } catch (ConditionTimeoutException e) {
            return false;
        }
    }

    public CartPage clearCart() {
        removeButtons.forEach(button ->
                button.shouldBe(interactable).click()
        );
        return this;
    }

}
