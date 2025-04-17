package pages.account;

import com.codeborne.selenide.ElementsCollection;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$$x;

@Getter
public class AccountOrdersPage extends AccountBasePage {
    private final ElementsCollection orderNumbers = $$x("//td[@class='woocommerce-orders-table__cell woocommerce-orders-table__cell-order-number']/a");

    public void redirectToFirstOrder() {
        orderNumbers.first().click();
    }

}
