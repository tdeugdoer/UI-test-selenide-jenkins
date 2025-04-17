package utils;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionFactory;

public class CustomAwait {
    public static ConditionFactory await() {
        return Awaitility.await()
                .pollInSameThread();
    }

    public static ConditionFactory await(String alias) {
        return Awaitility.await(alias)
                .pollInSameThread();
    }

}
