package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {
    public final String EMAIL_SUFFIX = "@a.a";

    @UtilityClass
    public class Urls {
        public final String BASE_URL = "https://pizzeria.skillbox.cc";
        public final String MENU_URL = "/product-category/menu";
        public final String REGISTER_URL = "/register";
        public final String MY_ACCOUNT_URL = "/my-account";
        public final String CART_URL = "/cart";
        public final String DELIVERY_URL = "/delivery";
        public final String BONUS_URL = "/bonus";

    }

}
