package com.banzhiyan.util;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class Hex {
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public Hex() {
    }

    public static char[] encode(byte[] data) {
        int len = data.length;
        char[] out = new char[len << 1];
        int i = 0;

        for(int var4 = 0; i < len; ++i) {
            out[var4++] = HEX[(240 & data[i]) >>> 4];
            out[var4++] = HEX[15 & data[i]];
        }

        return out;
    }

    public static byte[] decode(char[] data) {
        int len = data.length;
        if((len & 1) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        } else {
            byte[] out = new byte[len >> 1];
            int i = 0;

            for(int j = 0; j < len; ++i) {
                int f = toDigit(data[j], j) << 4;
                ++j;
                f |= toDigit(data[j], j);
                ++j;
                out[i] = (byte)(f & 255);
            }

            return out;
        }
    }

    private static int toDigit(char ch, int index) {
        int digit = Character.digit(ch, 16);
        if(digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        } else {
            return digit;
        }
    }

    public static String encodeHexString(byte[] data) {
        return new String(encode(data));
    }

    public static byte[] decodeHexString(String data) {
        return decode(data.toCharArray());
    }
}

