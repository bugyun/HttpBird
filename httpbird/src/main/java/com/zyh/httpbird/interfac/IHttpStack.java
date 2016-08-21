package com.zyh.httpbird.interfac;

import com.zyh.httpbird.core.HttpResponse;
import com.zyh.httpbird.exception.AuthFailureError;
import com.zyh.httpbird.request.Request;

import java.io.IOException;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by ruoyun on 16/8/21.
 */
public interface IHttpStack {
    /**
     * 设置全局的ssl
     *
     * @param sslSocketFactory
     */
    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory);

    /**
     * Performs an HTTP request with the given parameters.
     * <p>
     * <p>A GET request is sent if request.getPostBody() == null. A POST request is sent otherwise,
     * and the Content-Type header is set to request.getPostBodyContentType().</p>
     *
     * @param request           the request to perform
     * @param additionalHeaders additional headers to be sent together with
     * @return the HTTP response
     */
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError;

}
