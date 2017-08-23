package com.banzhiyan.core.util;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class EnvUtils {
    private EnvUtils() {
    }

    public static String get(String key) {
        String value = System.getProperty(key);
        if(value == null || value.trim().length() == 0) {
            value = System.getenv(key);
        }

        return value != null?value.trim():null;
    }

    public static String getOrThrow(String key) {
        String value = get(key);
        if(value != null && !value.isEmpty()) {
            return value;
        } else {
            throw new NullPointerException("Key: " + key + " not found.");
        }
    }
}

