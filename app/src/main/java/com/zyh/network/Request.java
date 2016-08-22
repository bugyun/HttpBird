package com.zyh.network;

import java.util.Map;

/**
 * Created by ruoyun on 16/8/21.
 */
public class Request {

    public interface Method {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }

    public String url;
    public Map<String, String> headers;
    public int method;
    public ICallback callback;

    public void setCallback(ICallback callback) {
        this.callback = callback;
    }

}
