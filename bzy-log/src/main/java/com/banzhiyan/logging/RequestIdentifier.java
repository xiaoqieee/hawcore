package com.banzhiyan.logging;

import java.util.UUID;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class RequestIdentifier {
    private static final ThreadLocal<String> identifiers = new ThreadLocal<String>() {
        protected String initialValue() {
            return RequestIdentifier.uuid();
        }
    };

    private RequestIdentifier() {
    }

    public static void set(String value) {
        identifiers.set(value);
    }

    public static void set() {
        set(uuid());
    }

    public static String get() {
        String identifier = identifiers.get();
        if(identifier == null) {
            set();
        }

        return identifiers.get();
    }

    public static void remove() {
        identifiers.remove();
    }

    private static String uuid() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-", "");
    }
}

