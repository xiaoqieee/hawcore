package com.banzhiyan.util.http;

/**
 * Created by xn025665 on 2017/8/23.
 */
class HttpClientShutdownHook extends Thread {
    private final HttpClient httpClient;

    public HttpClientShutdownHook(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void run() {
        if(this.httpClient != null) {
            this.httpClient.close();
        }

    }
}
