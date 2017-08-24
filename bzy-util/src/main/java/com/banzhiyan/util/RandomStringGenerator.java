package com.banzhiyan.util;


import java.security.SecureRandom;
import java.util.UUID;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class RandomStringGenerator {
    private static final int DEFAULT_LENGTH = 8;
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERIC = "1234567890";
    private static final SecureRandom wheel = new SecureRandom();

    public RandomStringGenerator() {
    }

    public static String randomString(int length) {
        int len = length > 0?length:8;
        return generator(len, "abcdefghijklmnopqrstuvwxyz1234567890");
    }

    private static String generator(int len, String chars) {
        char[] randomChars = new char[len];

        for(int i = 0; i < len; ++i) {
            randomChars[i] = chars.charAt(wheel.nextInt(chars.length()));
        }

        return new String(shuffle(randomChars));
    }

    private static char[] shuffle(char[] arr) {
        for(int i = arr.length; i > 1; --i) {
            swap(arr, i - 1, wheel.nextInt(i));
        }

        return arr;
    }

    private static void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    public static String randomAlphabetic(int length) {
        int len = length > 0?length:8;
        return generator(len, "abcdefghijklmnopqrstuvwxyz");
    }

    public static String randomNumeric(int length) {
        int len = length > 0?length:8;
        return generator(len, "1234567890");
    }

    public static String randomString() {
        String uuid = UUID.randomUUID().toString();
        return StringUtils.replace(uuid, "-", "");
    }
}

