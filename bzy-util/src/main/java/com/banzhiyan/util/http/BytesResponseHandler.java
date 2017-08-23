package com.banzhiyan.util.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by xn025665 on 2017/8/23.
 */

class BytesResponseHandler implements ResponseHandler<byte[]> {
    BytesResponseHandler() {
    }

    public byte[] handleResponse(HttpResponse response) throws IOException {
        HttpEntity entity = null;

        byte[] var3;
        try {
            entity = ResponseHandlerHelper.handleNon2xxStatus(response);
            var3 = EntityUtils.toByteArray(entity);
        } finally {
            EntityUtils.consume(entity);
        }

        return var3;
    }
}

