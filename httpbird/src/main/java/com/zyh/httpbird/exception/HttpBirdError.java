package com.zyh.httpbird.exception;

import com.zyh.httpbird.core.NetworkResponse;

/**
 * Created by ruoyun on 16/8/21.
 * 异常处理类
 */
public class HttpBirdError extends Exception {
    public final NetworkResponse networkResponse;

    public HttpBirdError() {
        networkResponse = null;
    }

    public HttpBirdError(NetworkResponse response) {
        networkResponse = response;
    }

    public HttpBirdError(String exceptionMessage) {
        super(exceptionMessage);
        networkResponse = null;
    }

    public HttpBirdError(String exceptionMessage, Throwable reason) {
        super(exceptionMessage, reason);
        networkResponse = null;
    }

    public HttpBirdError(Throwable cause) {
        super(cause);
        networkResponse = null;
    }

}
