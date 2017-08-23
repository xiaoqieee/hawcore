package com.banzhiyan.util.http;

import com.banzhiyan.logging.Logger;
import com.banzhiyan.logging.LoggerFactory;
import com.banzhiyan.util.Charsets;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import javax.net.ssl.SSLContext;
import java.net.*;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by xn025665 on 2017/8/23.
 */

class HttpClient {
    private static final ConcurrentMap<Charset, HttpClient> HOLDER;
    private final Logger logger = LoggerFactory.getLogger(HttpClient.class);
    private final int defaultIdleConnTimeout = 1800;
    private final int defaultMaxPerRoute = 128;
    private final int defaultMaxTotalConn = 128;
    private final PoolingHttpClientConnectionManager connectionManager;
    private final IdleConnectionMonitorThread ict;

    public static HttpClient get() throws HttpClientException {
        return get(Charsets.UTF_8);
    }

    public static HttpClient get(Charset charset) throws HttpClientException {
        HttpClient httpClient = (HttpClient)HOLDER.get(charset);
        if(httpClient == null) {
            httpClient = new HttpClient(charset);
            HttpClient old = (HttpClient)HOLDER.putIfAbsent(charset, httpClient);
            if(old != null) {
                httpClient = old;
            }
        }

        return httpClient;
    }

    private HttpClient(Charset charset) throws HttpClientException {
        PlainConnectionSocketFactory plainsf = PlainConnectionSocketFactory.INSTANCE;

        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            LayeredConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
            Registry r = RegistryBuilder.create().register("http", plainsf).register("https", sslsf).build();
            this.connectionManager = new PoolingHttpClientConnectionManager(r);
            this.connectionManager.setDefaultMaxPerRoute(128);
            this.connectionManager.setMaxTotal(128);
            this.connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom().setCharset(charset).build());
            this.connectionManager.setDefaultSocketConfig(SocketConfig.custom().setSoKeepAlive(true).build());
            this.ict = new IdleConnectionMonitorThread(this.connectionManager, 1800);
            this.ict.start();
        } catch (Throwable var6) {
            throw new HttpClientException(var6);
        }

        Runtime.getRuntime().addShutdownHook(new HttpClientShutdownHook(this));
    }

    public String requestWithStringResult(HttpRequest request) throws HttpClientException {
        return (String)this.request(request, new StringResponseHandler(request.getCharset()));
    }

    public byte[] requestWithBytesResult(HttpRequest request) throws HttpClientException {
        return (byte[])this.request(request, new BytesResponseHandler());
    }

    public <T> T request(HttpRequest request, ResponseHandler<T> responseHandler) throws HttpClientException {
        CookieHandler.setDefault(new CookieManager((CookieStore)null, CookiePolicy.ACCEPT_ALL));
        CloseableHttpClient httpclient = this.initHttpClient(request);
        HttpRequestBase method = null;

        T var5;
        try {
            method = this.initHttpMethod(request);
            if(this.logger.isDebugEnabled()) {
                this.logger.debug("开始发送http请求:targetUrl[{}],parameters[{}]", new Object[]{request.getTargetUrl(), request.getParameters()});
            }

            var5 = httpclient.execute(method, responseHandler);
        } catch (Throwable var14) {
            throw new HttpClientException(var14);
        } finally {
            if(method != null) {
                try {
                    method.releaseConnection();
                } catch (Throwable var13) {
                    ;
                }
            }

        }

        return var5;
    }

    private CloseableHttpClient initHttpClient(HttpRequest request) {
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.disableAuthCaching().setRedirectStrategy(new LaxRedirectStrategy());
        return builder.setConnectionManager(this.connectionManager).build();
    }

    private HttpRequestBase initHttpMethod(HttpRequest request) throws URISyntaxException {
        HttpRequestBase method;
        if(HttpRequest.METHOD.GET.equals(request.getMethod())) {
            method = this.initGet(request);
        } else {
            method = this.initPost(request);
        }

        RequestConfig reqConfig = RequestConfig.custom().setSocketTimeout(request.getTimeout()).setConnectTimeout(request.getConnectionTimeout()).setRedirectsEnabled(true).setCircularRedirectsAllowed(true).setRelativeRedirectsAllowed(true).setConnectionRequestTimeout(request.getConnectionTimeout()).setExpectContinueEnabled(false).build();
        method.setConfig(reqConfig);
        Iterator i$ = request.getHeaders().entrySet().iterator();

        while(i$.hasNext()) {
            Map.Entry<String, String> header = (Map.Entry)i$.next();
            method.addHeader((String)header.getKey(), (String)header.getValue());
        }

        method.setHeader("Connection", "keep-alive");
        method.removeHeaders("Expect");
        return method;
    }

    private HttpRequestBase initGet(HttpRequest request) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(request.getTargetUrl());
        Map<String, String> parameters = request.getParameters();
        Iterator i$ = parameters.keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            builder.addParameter(key, (String)parameters.get(key));
        }

        return new HttpGet(builder.build());
    }

    private HttpRequestBase initPost(HttpRequest request) {
        HttpPost method = new HttpPost(request.getTargetUrl());
        Map<String, String> parameters = request.getParameters();
        List<NameValuePair> nvps = new ArrayList(parameters.size());
        Iterator i$ = parameters.keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            nvps.add(new BasicNameValuePair(key, (String)parameters.get(key)));
        }

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, request.getCharset());
        method.setEntity(urlEncodedFormEntity);
        method.addHeader("Content-Type", "application/x-www-form-urlencoded; text/html; charset=" + request.getCharset());
        return method;
    }

    public void close() {
        this.ict.shutdown();
        this.connectionManager.shutdown();
    }

    static {
        System.setProperty("jsse.enableSNIExtension", "false");
        HOLDER = new ConcurrentHashMap();
    }
}
