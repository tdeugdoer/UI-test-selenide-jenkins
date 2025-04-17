package pages.account;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class AccountEditPage extends AccountBasePage {
    private final SelenideElement
            uploadFileInput = $x("//input[@id='uploadFile']"),
            saveAccountDetailsButton = $x("//button[@name='save_account_details']");

}
