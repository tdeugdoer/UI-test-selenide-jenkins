package ui.main;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import listeners.UIListener;
import org.assertj.core.api.SoftAssertions;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import pages.main.MainPage;
import utils.FailMessages;

import java.util.List;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.switchTo;
import static org.assertj.core.api.Assertions.assertThat;

@Listeners(UIListener.class)
public class MainPageTest {
    private final MainPage mainPage = new MainPage();

    @BeforeMethod
    public void setUp() {
        open("/");
    }

    @Test(description = "Переключение пиццы в слайдере нажатием на клавиатуре стрелки влево и вправо")
    public void slidePizzaSliderByKeys() {
        List<SelenideElement> initialVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        mainPage.slidePizzaSlider(4, Keys.LEFT);
        List<SelenideElement> afterLeftSlideVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        mainPage.slidePizzaSlider(3, Keys.RIGHT);
        List<SelenideElement> afterRightSlideVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(afterLeftSlideVisiblePizzaList)
                    .as(FailMessages.SLIDE_NOT_CHANGE_ELEMENTS)
                    .isNotEqualTo(initialVisiblePizzaList);
            softly.assertThat(afterRightSlideVisiblePizzaList)
                    .as(FailMessages.SLIDE_NOT_CHANGE_ELEMENTS)
                    .isNotEqualTo(initialVisiblePizzaList);
        });
    }

    @Test(description = "Переключение пиццы в слайдере нажатием стрелки влево и вправо")
    public void slidePizzaSliderByButtons() {
        List<SelenideElement> initialVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        mainPage.slideLeftPizzaSlider(4);
        List<SelenideElement> afterLeftSlideVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        mainPage.slideRightPizzaSlider(2);
        List<SelenideElement> afterRightSlideVisiblePizzaList = mainPage.getVisiblePizzaInSlider();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(afterLeftSlideVisiblePizzaList)
                    .as(FailMessages.SLIDE_NOT_CHANGE_ELEMENTS)
                    .isNotEqualTo(initialVisiblePizzaList);
            softly.assertThat(afterRightSlideVisiblePizzaList)
                    .as(FailMessages.SLIDE_NOT_CHANGE_ELEMENTS)
                    .isNotEqualTo(initialVisiblePizzaList);
        });
    }

    @Test(description = "Проверка отображения ссылки 'В корзину' при наведении на картинку напитка")
    public void addToCartLinkAppearsOnHover() {
        Long beforeCountVisibleDrinkToCartButtons = mainPage.getCountDrinkWithVisibleCartButtons();
        mainPage.moveToFirstDrinkImage();
        Long afterCountVisibleDrinkToCartButtons = mainPage.getCountDrinkWithVisibleCartButtons();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(beforeCountVisibleDrinkToCartButtons)
                    .as(FailMessages.ELEMENT_COUNT_NOT_MATCH_EXPECTED)
                    .isEqualTo(0);
            softly.assertThat(afterCountVisibleDrinkToCartButtons)
                    .as(FailMessages.ELEMENT_COUNT_NOT_MATCH_EXPECTED)
                    .isEqualTo(1);
        });
    }

    @Test(description = "Проверка перехода на страницу десерта при клике по его картинке")
    public void clickOnDessertImageRedirectsToDessertPage() {
        String firstDesertName = mainPage.getFirstDesertTitle();
        mainPage.redirectToFirstDesert();
        String currentPageTitle = Selenide.title();

        assertThat(currentPageTitle)
                .as(FailMessages.STRING_SHOULD_NOT_BE_NULL)
                .isNotNull()
                .as(FailMessages.STRING_NOT_HAS_EXPECTED)
                .contains(firstDesertName);
    }

    @Test(description = "Проверка отображения стрелки 'Наверх' при скроллинге вниз страницы")
    public void scrollToTopArrowAppearsWhenScrolledDown() {
        Boolean isScrollArrowVisible = mainPage.isTopScrollArrowVisible();

        assertThat(isScrollArrowVisible)
                .as(FailMessages.ELEMENT_NOT_VISIBLE)
                .isTrue();
    }

    @Test(description = "Проверка открытия ссылок на соцсети в новой вкладке")
    public void socialMediaLinksOpenInNewTab() {
        Integer beforeWindowCount = getWindowCount();

        mainPage.getInstagramButton().click();
        switchTo().window(1);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(beforeWindowCount)
                    .as(FailMessages.WINDOW_COUNT_NOT_MATCH_EXPECTED)
                    .isEqualTo(1);
            softly.assertThat(getWindowCount())
                    .as(FailMessages.WINDOW_COUNT_NOT_MATCH_EXPECTED)
                    .isEqualTo(2);
            softly.assertThat(WebDriverRunner.url())
                    .as(FailMessages.STRING_SHOULD_NOT_BE_NULL)
                    .isNotNull()
                    .as(FailMessages.NEW_WINDOW_NOT_MATCH_EXPECTED)
                    .contains("www.instagram.com");
        });
    }

    private Integer getWindowCount() {
        return WebDriverRunner.getWebDriver().getWindowHandles().size();
    }

}
