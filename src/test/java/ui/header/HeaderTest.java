package ui.header;

import listeners.UIListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.main.MainPage;
import pages.menu.MenuPage;
import utils.FailMessages;

import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class HeaderTest {
    private final MainPage mainPage = new MainPage();
    private final MenuPage menuPage = new MenuPage();

    @BeforeMethod
    public void setUp() {
        open("/");
    }

    /**
     * Навигационное меню: переход по всем разделам меню:
     * 1) "Меню" → «Пицца»
     * 2) «Меню» → «Десерты»
     * 3) «Меню» → «Напитки»
     */
    @Test(dataProvider = "provideCategories")
    public void menuCategories(String category) {
        mainPage.clickMenuCategoryButton(category);
        String title = menuPage.getTitle().getText();

        assertThat(category)
                .as(FailMessages.STRING_NOT_MATCH_EXPECTED)
                .isEqualToIgnoringCase(title);
    }

    @DataProvider(name = "provideCategories")
    public Object[][] provideCategories() {
        return new Object[][]{
                {"Пицца"},
                {"Десерты"},
                {"Напитки"}
        };
    }

}
