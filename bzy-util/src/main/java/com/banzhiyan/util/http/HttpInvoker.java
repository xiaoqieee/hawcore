package com.banzhiyan.util.http;

import com.banzhiyan.util.ResourceTracker;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by xn025665 on 2017/8/24.
 */

public class HttpInvoker {
    private static CookieManager cookieManager;
    private static int connectTimeout;
    private static int readTimeout;
    private static Charset defaultCharset;

    private HttpInvoker() {
    }

    public static String get(String url) throws IOException {
        return get(url, (Map)null);
    }

    public static String get(String url, Map<String, String> headers) throws IOException {
        ResourceTracker tracker = new ResourceTracker(HttpInvoker.Method.GET.name());
        HttpURLConnection conn = getConnection(url, HttpInvoker.Method.GET, headers);

        String var10;
        try {
            List<String> cookies = (List)conn.getHeaderFields().get("Set-Cookie");
            if(cookies != null) {
                Iterator i$ = cookies.iterator();

                while(i$.hasNext()) {
                    String cookie = (String)i$.next();
                    if(cookie != null && cookie.length() > 0) {
                        cookieManager.setCookies(url, cookie);
                    }
                }
            }

            var10 = handleResponse(tracker, conn);
        } finally {
            tracker.clear();
        }

        return var10;
    }

    public static String post(String url, String parameter) throws IOException {
        return post(url, (String)parameter, (Map)null);
    }

    public static String post(String url, String parameter, Map<String, String> headers) throws IOException {
        ResourceTracker tracker = new ResourceTracker(HttpInvoker.Method.POST.name());
        HttpURLConnection conn = getConnection(url, HttpInvoker.Method.POST, headers);

        String var6;
        try {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            tracker.attach(out);
            out.write((parameter == null?"":parameter).getBytes(defaultCharset));
            out.flush();
            var6 = handleResponse(tracker, conn);
        } finally {
            tracker.clear();
        }

        return var6;
    }

    public static String post(String url, Map<String, String> parameter) throws IOException {
        return post(url, (Map)parameter, (Map)null);
    }

    public static String post(String url, Map<String, String> parameter, Map<String, String> headers) throws IOException {
        ResourceTracker tracker = new ResourceTracker(HttpInvoker.Method.POST.name());
        HttpURLConnection conn = getConnection(url, HttpInvoker.Method.POST, headers);

        String var6;
        try {
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            tracker.attach(out);
            out.write(createLinkString(parameter).getBytes(defaultCharset));
            out.flush();
            var6 = handleResponse(tracker, conn);
        } finally {
            tracker.clear();
        }

        return var6;
    }

    private static HttpURLConnection getConnection(String url, HttpInvoker.Method method, Map<String, String> headers) throws IOException {
        CookieHandler.setDefault(new java.net.CookieManager((CookieStore)null, CookiePolicy.ACCEPT_ALL));
        URLConnection urlConnection = (new URL(url)).openConnection();
        HttpURLConnection conn = (HttpURLConnection)urlConnection;
        conn.setInstanceFollowRedirects(true);
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "keep-alive");
        conn.setRequestProperty("Accept-Charset", defaultCharset.name());
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
        conn.setRequestProperty("Cookie", cookieManager.getCookies(url));
        if(HttpInvoker.Method.POST == method) {
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + defaultCharset);
            conn.setDoOutput(true);
            conn.setDoInput(true);
        } else {
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "text/html; charset=" + defaultCharset);
        }

        if(headers != null) {
            Iterator i$ = headers.entrySet().iterator();

            while(i$.hasNext()) {
                Map.Entry<String, String> header = (Map.Entry)i$.next();
                conn.addRequestProperty((String)header.getKey(), (String)header.getValue());
            }
        }

        conn.connect();
        return conn;
    }

    public static String createLinkString(Map<String, String> params) throws IOException {
        Map<String, String> parameter = paramsFilter(params);
        List<String> keys = new ArrayList(parameter.keySet());
        Collections.sort(keys);
        StringBuilder builder = new StringBuilder(128);

        for(int i = 0; i < keys.size(); ++i) {
            if(i > 0) {
                builder.append("&");
            }

            String key = (String)keys.get(i);
            builder.append(key).append("=").append((String)parameter.get(key));
        }

        return builder.toString();
    }

    private static Map<String, String> paramsFilter(Map<String, String> parameters) throws IOException {
        Map<String, String> result = new ConcurrentHashMap();
        if(parameters != null && parameters.size() > 0) {
            Iterator i$ = parameters.keySet().iterator();

            while(i$.hasNext()) {
                String key = (String)i$.next();
                if(key != null && key.length() > 0) {
                    String value = (String)parameters.get(key);
                    if(value != null && value.length() > 0) {
                        result.put(key, URLEncoder.encode(value, defaultCharset.name()));
                    }
                }
            }

            return result;
        } else {
            return result;
        }
    }

    private static String handleResponse(ResourceTracker tracker, HttpURLConnection conn) throws IOException {
        String contentType = conn.getHeaderField("Content-Type");
        String charset = null;
        if(contentType != null) {
            String[] arr$ = contentType.replace(" ", "").split(";");
            int len$ = arr$.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String param = arr$[i$];
                if(param.startsWith("charset=")) {
                    charset = param.split("=", 2)[1];
                    break;
                }
            }
        }

        StringBuilder builder = new StringBuilder(128);
        BufferedReader reader;
        if(charset != null) {
            reader = new BufferedReader(new InputStreamReader(getContent(conn), charset));
        } else {
            reader = new BufferedReader(new InputStreamReader(getContent(conn), defaultCharset));
        }

        tracker.attach(reader);

        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return builder.toString();
    }

    private static InputStream getContent(HttpURLConnection connection) throws IOException {
        InputStream in;
        if(isSuccess(connection.getResponseCode())) {
            try {
                in = connection.getInputStream();
            } catch (IOException var3) {
                in = connection.getErrorStream();
            }
        } else {
            in = connection.getErrorStream();
        }

        return in;
    }

    private static boolean isSuccess(int statusCode) {
        return statusCode >= 200 && statusCode < 300;
    }

    public static void setConnectTimeout(int connectTimeout) {
        connectTimeout = connectTimeout;
    }

    public static void setReadTimeout(int readTimeout) {
        readTimeout = readTimeout;
    }

    public static void setDefaultCharset(Charset defaultCharset) {
        defaultCharset = defaultCharset;
    }

    static {
        TrustManager[] trustAllCertificates = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        HostnameVerifier trustAllHostnames = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init((KeyManager[])null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (GeneralSecurityException var3) {
            throw new ExceptionInInitializerError(var3);
        }

        cookieManager = CookieManager.instance();
        connectTimeout = 20000;
        readTimeout = 20000;
        defaultCharset = StandardCharsets.UTF_8;
    }

    private static enum Method {
        POST,
        GET;

        private Method() {
        }
    }
}
