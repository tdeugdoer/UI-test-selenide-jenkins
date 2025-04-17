package pages.auth;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class RegisterPage extends BasePage {
    private final SelenideElement
            usernameField = $x("//input[@id='reg_username']"),
            emailField = $x("//input[@id='reg_email']"),
            passwordField = $x("//input[@id='reg_password']"),
            registerButton = $x("//button[@name='register']"),
            successRegisterMessage = $x("//div[@class='content-page']/div"),
            errorMessage = $x("//ul[@class='woocommerce-error']/li");

    public RegisterPage fillOutRegisterForm(String username, String email, String password) {
        enterField(usernameField, username);
        enterField(emailField, email);
        enterField(passwordField, password);
        return this;
    }

}
