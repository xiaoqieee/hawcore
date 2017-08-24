package com.banzhiyan.security.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class PasswordAdapter {
    private static final Pattern PATTERN = Pattern.compile("ENC\\(([A-Za-z0-9]+)\\)");

    public PasswordAdapter() {
    }

    public static String decodePasswordIfNeeded(String password) {
        Matcher matcher = PATTERN.matcher(password);
        return matcher.matches()?PasswordUtils.decrypt(matcher.group(1)):password;
    }
}
