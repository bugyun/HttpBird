package com.zyh.httpbird.core;

import com.zyh.httpbird.exception.HttpBirdError;
import com.zyh.httpbird.interfac.ICache;
import com.zyh.httpbird.interfac.IHttpStack;
import com.zyh.httpbird.interfac.INetwork;
import com.zyh.httpbird.request.Request;
import com.zyh.httpbird.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruoyun on 16/8/21.
 */
public class Network implements INetwork {

    protected final IHttpStack mHttpStack;

    public Network(IHttpStack httpStack) {
        mHttpStack = httpStack;
    }

    @Override
    public NetworkResponse performRequest(Request<?> request) throws HttpBirdError {
        while (true) {
            HttpResponse httpResponse = null;
            byte[] responseContents = null;
            HashMap<String, String> responseHeaders = new HashMap<>();
            try {
                // 标记Http响应头在Cache中的tag
                Map<String, String> headers = new HashMap<>();

//                addCacheHeaders(headers, request.getCacheEntry());
                httpResponse = mHttpStack.performRequest(request, headers);

                int statusCode = httpResponse.getResponseCode();
                responseHeaders = httpResponse.getHeaders();

//                if (statusCode == HttpURLConnection.HTTP_NOT_MODIFIED) { // 304
//                    return new NetworkResponse(HttpURLConnection.HTTP_NOT_MODIFIED, request.getCacheEntry() == null ? null : request.getCacheEntry().data, responseHeaders, true);
//                }

                if (httpResponse.getContentStream() != null) {
                    //                    if (request instanceof FileRequest) {
                    //                        responseContents = ((FileRequest) request).handleResponse(httpResponse);
                    //                    } else {
                    responseContents = entityToBytes(httpResponse);
                    //                    }
                } else {
                    responseContents = new byte[0];
                }

                if (statusCode < 200 || statusCode > 299) {
                    throw new IOException();
                }
                return new NetworkResponse(statusCode, responseContents, responseHeaders, false);
            } catch (SocketTimeoutException e) {
                attemptRetryOnException("socket", request, new HttpBirdError(new SocketTimeoutException("socket timeout")));
            } catch (MalformedURLException e) {
//                attemptRetryOnException("connection", request, new HttpBirdError("Bad URL " + request.getUrl(), e));
            } catch (IOException e) {
                int statusCode;
                NetworkResponse networkResponse;
                if (httpResponse != null) {
                    statusCode = httpResponse.getResponseCode();
                } else {
                    throw new HttpBirdError("NoConnection error", e);
                }
                //                Loger.debug(String.format("Unexpected response code %d for %s", statusCode, request.getUrl()));
                if (responseContents != null) {
                    networkResponse = new NetworkResponse(statusCode, responseContents, responseHeaders, false);
                    if (statusCode == HttpURLConnection.HTTP_UNAUTHORIZED || statusCode == HttpURLConnection.HTTP_FORBIDDEN) {
                        attemptRetryOnException("auth", request, new HttpBirdError(networkResponse));
                    } else {
                        throw new HttpBirdError(networkResponse);
                    }
                } else {
//                    throw new HttpBirdError(String.format("Unexpected response code %d for %s", statusCode, request.getUrl()));
                }
            }
        }
    }


    /**
     * Attempts to prepare the request for a retry. If there are no more
     * attempts remaining in the request's retry policy, a timeout exception is
     * thrown.
     *
     * @param request The request to use.
     */
    private static void attemptRetryOnException(String logPrefix, Request<?> request, HttpBirdError exception) throws HttpBirdError {
//        RetryPolicy retryPolicy = request.getRetryPolicy();
//        int oldTimeout = request.getTimeoutMs();
//
//        try {
//            if (retryPolicy != null) {
//                retryPolicy.retry(exception);
//            } else {
//                //                Loger.debug("not retry policy");
//            }
//        } catch (HttpBirdError e) {
//            //            Loger.debug(String.format("%s-timeout-giveup [timeout=%s]", logPrefix, oldTimeout));
//            throw e;
//        }
//        //        Loger.debug(String.format("%s-retry [timeout=%s]", logPrefix, oldTimeout));
    }

    /**
     * 标记Respondeader响应头在Cache中的tag
     */
    private void addCacheHeaders(Map<String, String> headers, ICache.Entry entry) {
        if (entry == null) {
            return;
        }
        if (entry.etag != null) {
            headers.put("If-None-Match", entry.etag);
        }
        if (entry.serverDate > 0) {
            Date refTime = new Date(entry.serverDate);
            DateFormat sdf = SimpleDateFormat.getDateTimeInstance();
            headers.put("If-Modified-Since", sdf.format(refTime));

        }
    }

    /**
     * 把HttpEntry转换为byte[]
     *
     * @throws IOException
     * @throws HttpBirdError
     */
    private byte[] entityToBytes(HttpResponse entity) throws IOException, HttpBirdError {
        PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(ByteArrayPool.get(), (int) entity.getContentLength());
        byte[] buffer = null;
        try {
            InputStream in = entity.getContentStream();
            if (in == null) {
                throw new HttpBirdError("server error");
            }
            buffer = ByteArrayPool.get().getBuf(1024);
            int count;
            while ((count = in.read(buffer)) != -1) {
                bytes.write(buffer, 0, count);
            }
            return bytes.toByteArray();
        } finally {
            FileUtils.closeIO(entity.getContentStream());
            ByteArrayPool.get().returnBuf(buffer);
            FileUtils.closeIO(bytes);
        }
    }
}
