package com.banzhiyan.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.*;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class UrlCodecUtils {
    public UrlCodecUtils() {
    }

    public static String arrayToString(Object[] array, String delimiter) {
        if(array != null && array.length != 0) {
            StringBuilder b = new StringBuilder(64);
            int i = 0;

            for(int len = array.length; i < len; ++i) {
                if(i > 0) {
                    b.append(delimiter);
                }

                b.append(array[i]);
            }

            return b.toString();
        } else {
            return "";
        }
    }

    public static String base64Decode(String base64String) {
        return org.apache.commons.lang3.StringUtils.isBlank(base64String)?base64String:new String(decodeBase64(base64String), Charsets.UTF_8);
    }

    public static byte[] decodeBase64(String base64String) {
        return (new Base64(true)).decode(base64String);
    }

    public static byte[] encodeBase64(String base64String) {
        return (new Base64(true)).encode(base64String.getBytes());
    }

    public static String base64Encode(String base64String) {
        return org.apache.commons.lang3.StringUtils.isBlank(base64String)?base64String:new String(encodeBase64(base64String), Charsets.UTF_8);
    }
}

