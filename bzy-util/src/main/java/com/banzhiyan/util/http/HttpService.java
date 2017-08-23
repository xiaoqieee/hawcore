package com.banzhiyan.util.http;

import org.apache.http.client.ResponseHandler;

import java.util.Map;

/**
 * Created by xn025665 on 2017/8/23.
 */

public final class HttpService {
    private HttpService() {
    }

    public static String sendRequest(HttpRequest httpRequest) throws HttpClientException {
        try {
            return HttpClient.get(httpRequest.getCharset()).requestWithStringResult(httpRequest);
        } catch (Throwable var2) {
            if(var2 instanceof HttpClientException) {
                throw (HttpClientException)var2;
            } else {
                throw new HttpClientException(var2);
            }
        }
    }

    public static <T> T sendRequest(HttpRequest httpRequest, ResponseHandler<T> responseHandler) throws HttpClientException {
        try {
            return HttpClient.get(httpRequest.getCharset()).request(httpRequest, responseHandler);
        } catch (Throwable var3) {
            if(var3 instanceof HttpClientException) {
                throw (HttpClientException)var3;
            } else {
                throw new HttpClientException(var3);
            }
        }
    }

    public static String sendPostRequest(String url, Map<String, String> parameters) throws HttpClientException {
        return sendPostRequest(url, parameters, "UTF-8");
    }

    private static String sendPostRequest(String url, Map<String, String> parameters, String charset) throws HttpClientException {
        HttpRequest req = new HttpRequest(url, charset);
        req.setMethod(HttpRequest.METHOD.POST);
        req.setParameters(parameters);

        try {
            return HttpClient.get(req.getCharset()).requestWithStringResult(req);
        } catch (Throwable var5) {
            if(var5 instanceof HttpClientException) {
                throw (HttpClientException)var5;
            } else {
                throw new HttpClientException(var5);
            }
        }
    }

    private static String sendGetRequest(String url, Map<String, String> parameters, String charset) throws HttpClientException {
        HttpRequest req = new HttpRequest(url, charset);
        req.setParameters(parameters);
        req.setMethod(HttpRequest.METHOD.GET);

        try {
            return HttpClient.get(req.getCharset()).requestWithStringResult(req);
        } catch (Throwable var5) {
            if(var5 instanceof HttpClientException) {
                throw (HttpClientException)var5;
            } else {
                throw new HttpClientException(var5);
            }
        }
    }

    public static String sendGetRequest(String url, Map<String, String> parameters) throws HttpClientException {
        return sendGetRequest(url, parameters, "UTF-8");
    }

    public static String sendGetRequest(String url) throws HttpClientException {
        return sendGetRequest(url, (Map)null, "UTF-8");
    }
}

