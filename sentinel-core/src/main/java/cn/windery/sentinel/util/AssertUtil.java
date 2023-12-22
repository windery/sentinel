package cn.windery.sentinel.util;

public class AssertUtil {

    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object obj, String message) {
        isTrue(obj != null, message);
    }

}
