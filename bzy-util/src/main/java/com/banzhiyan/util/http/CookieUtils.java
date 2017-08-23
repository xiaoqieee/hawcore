package com.banzhiyan.util.http;

import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by xn025665 on 2017/8/23.
 */

public class CookieUtils {
    private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);

    public CookieUtils() {
    }

    public static String getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null && cookies.length != 0 && name != null) {
            Cookie[] arr$ = cookies;
            int len$ = cookies.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                Cookie cookie = arr$[i$];
                if(cookie != null && name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, -1);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
        doSetCookie(response, name, value, "/", maxAge);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, String path) {
        doSetCookie(response, name, value, path, -1);
    }

    public static void setCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        doSetCookie(response, name, value, path, maxAge);
    }

    public static void deleteCookie(HttpServletResponse response, String name) {
        doSetCookie(response, name, "", "/", 0);
    }

    public static void deleteCookie(HttpServletResponse response, String name, String path) {
        doSetCookie(response, name, "", path, 0);
    }

    private static final void doSetCookie(HttpServletResponse response, String name, String value, String path, int maxAge) {
        try {
            Cookie cookie = new Cookie(name, value);
            cookie.setMaxAge(maxAge);
            cookie.setHttpOnly(true);
            cookie.setPath(path);
            response.addCookie(cookie);
        } catch (Exception var6) {
            logger.error(var6.getMessage(), var6);
        }

    }
}

