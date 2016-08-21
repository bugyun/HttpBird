package com.zyh.httpbird.core;

import com.zyh.httpbird.exception.AuthFailureError;
import com.zyh.httpbird.interfac.IHttpStack;
import com.zyh.httpbird.request.Request;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by ruoyun on 16/8/21.
 */
public class HurlStack implements IHttpStack {
    @Override
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {

    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        return null;
    }
}
