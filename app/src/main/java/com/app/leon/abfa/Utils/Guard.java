package com.app.leon.abfa.Utils;

/**
 * Created by user on 3/6/2018.
 */

public final class Guard {
    private Guard() {
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.length() < 1;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }
}
