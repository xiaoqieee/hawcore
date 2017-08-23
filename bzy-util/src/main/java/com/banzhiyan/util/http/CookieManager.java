package com.banzhiyan.util.http;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xn025665 on 2017/8/23.
 */
class CookieManager {
    private static final String SCHEME_SEPARATOR = "://";
    private static final CookieManager cookieManager = new CookieManager();
    private ConcurrentMap<String, Map<String, String>> COOKIES = new ConcurrentHashMap();
    private ConcurrentMap<String, String> URLS = new ConcurrentHashMap();

    private CookieManager() {
    }

    public static CookieManager instance() {
        return cookieManager;
    }

    public String getCookies(String url) {
        Map<String, String> urlCookies = (Map)this.COOKIES.get(this.getServerHost(url));
        if(urlCookies != null) {
            StringBuilder sb = new StringBuilder();

            Map.Entry cookieEntry;
            for(Iterator i$ = urlCookies.entrySet().iterator(); i$.hasNext(); sb.append((String)cookieEntry.getKey()).append("=").append((String)cookieEntry.getValue())) {
                cookieEntry = (Map.Entry)i$.next();
                if(sb.length() > 0) {
                    sb.append("; ");
                }
            }

            return sb.toString();
        } else {
            return "";
        }
    }

    public void setCookies(String url, String cookieStr) {
        String serverHost = this.getServerHost(url);
        Map<String, String> urlCookies = (Map)this.COOKIES.get(serverHost);
        if(urlCookies == null) {
            Map<String, String> map = new ConcurrentHashMap();
            urlCookies = (Map)this.COOKIES.putIfAbsent(serverHost, map);
            if(urlCookies == null) {
                urlCookies = map;
            }
        }

        String[] cookies = cookieStr.split(";");
        String[] arr$ = cookies;
        int len$ = cookies.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String cookie = arr$[i$];
            if(cookie != null && cookie.indexOf("=") > 0) {
                cookie = cookie.trim();
                ((Map)urlCookies).put(cookie.split("=")[0].trim(), cookie.split("=")[1].trim());
            }
        }

    }

    public void removeCookies(String url) {
        this.COOKIES.remove(this.getServerHost(url));
    }

    private String getServerHost(String url) {
        if(url != null && !url.isEmpty()) {
            if(!this.URLS.containsKey(url)) {
                String host = url;
                int index = url.indexOf("://");
                if(index > -1) {
                    host = url.substring(index + "://".length());
                }

                index = host.indexOf("/");
                if(index > -1) {
                    host = host.substring(0, index);
                }

                this.URLS.putIfAbsent(url, host);
            }

            return (String)this.URLS.get(url);
        } else {
            return "";
        }
    }
}
