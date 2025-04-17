package pages.auth;

import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import pages.BasePage;

import static com.codeborne.selenide.Selenide.$x;

@Getter
public class LoginPage extends BasePage {
    private final SelenideElement
            usernameField = $x("//input[@id='username']"),
            passwordField = $x("//input[@id='password']"),
            loginButton = $x("//button[@name='login']");

    public LoginPage fillOutLoginForm(String username, String password) {
        enterField(usernameField, username);
        enterField(passwordField, password);
        return this;
    }

}
