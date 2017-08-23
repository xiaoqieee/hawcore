package com.banzhiyan.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.springframework.util.Assert;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by xn025665 on 2017/8/23.
 */

class StringResponseHandler implements ResponseHandler<String> {
    private final Charset charset;

    public StringResponseHandler(Charset charset) {
        Assert.notNull(charset);
        this.charset = charset;
    }

    public String handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        HttpEntity entity = null;

        String var4;
        try {
            entity = ResponseHandlerHelper.handleNon2xxStatus(response);
            Charset responseCharset = ResponseHandlerHelper.getResponseCharset(entity);
            if(responseCharset == null) {
                responseCharset = this.charset;
            }

            var4 = EntityUtils.toString(entity, responseCharset);
        } finally {
            EntityUtils.consume(entity);
        }

        return var4;
    }
}

