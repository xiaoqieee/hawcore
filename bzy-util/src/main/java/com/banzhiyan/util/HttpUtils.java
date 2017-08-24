package com.banzhiyan.util;

import com.banzhiyan.core.util.PropertiesLoader;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xn025665 on 2017/8/24.
 */

public final class HttpUtils {
    private HttpUtils() {
    }

    public static String getRemoteIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if(ip != null) {
            String[] arr$ = ip.split(",");
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String _ip = arr$[i$];
                if(_ip != null && !"unknown".equalsIgnoreCase(_ip)) {
                    ip = _ip;
                    break;
                }
            }
        } else {
            ip = "unknown";
        }

        return ip;
    }

    public static String getRemoteIpAddresses(HttpServletRequest request) {
        return getRemoteIpAddr(request);
    }

    public static String getServerURL(ServletRequest request) {
        StringBuffer url = new StringBuffer(32);
        boolean fromUrl = false;
        String scheme = PropertiesLoader.getProperty("http.url.scheme");
        if(scheme == null || scheme.length() == 0) {
            scheme = request.getScheme();
            fromUrl = true;
        }

        url.append(scheme).append("://").append(request.getServerName());
        if(fromUrl) {
            int port = request.getServerPort();
            if(port < 0) {
                port = 80;
            }

            if(scheme.equals("http") && port != 80 || scheme.equals("https") && port != 443) {
                url.append(':').append(port);
            }
        } else {
            String port = PropertiesLoader.getProperty("http.url.port");
            if(port != null) {
                url.append(':').append(port);
            }
        }

        return url.toString();
    }

    public static String getRequestURL(HttpServletRequest request) {
        StringBuffer url = new StringBuffer(getServerURL(request));
        url.append(request.getRequestURI());
        return url.toString();
    }
}

