package com.banzhiyan.util.http;

import com.banzhiyan.util.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by xn025665 on 2017/8/23.
 */

class ResponseHandlerHelper {
    ResponseHandlerHelper() {
    }

    public static HttpEntity handleNon2xxStatus(HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        HttpEntity entity = response.getEntity();
        if(statusLine.getStatusCode() >= 300) {
            Charset responseCharset = getResponseCharset(entity);
            if(responseCharset == null) {
                responseCharset = Charsets.UTF_8;
            }

            String result = EntityUtils.toString(entity, responseCharset);
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase() + ':' + result);
        } else {
            return entity;
        }
    }

    public static Charset getResponseCharset(HttpEntity entity) {
        ContentType contentType = ContentType.getOrDefault(entity);
        return contentType.getCharset();
    }
}

