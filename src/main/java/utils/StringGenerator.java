package utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringGenerator {
    public String getUniqueString() {
        return Long.toHexString(System.nanoTime());
    }

}
