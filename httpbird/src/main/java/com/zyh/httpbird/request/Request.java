package com.zyh.httpbird.request;

/**
 * Created by ruoyun on 16/8/21.
 * <p/>
 * Base class for all network requests.
 *
 * @param <T> The type of parsed response this request expects.
 *            用来定义不同的返回类型
 */
public abstract class Request<T> {
    /**
     * Default encoding for POST or PUT parameters.
     * 默认的访问格式
     */
    private static final String DEFAULT_PARAMS_ENCODING = "UTF-8";

    /**
     * Supported request methods.
     * 支持的访问方式
     */
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

    /**
     * Request method of this request.  Currently supports GET, POST, PUT, DELETE, HEAD, OPTIONS,
     * TRACE, and PATCH.
     */
    private int mMethod;

    /**
     * URL of this request.
     */
    private String mUrl;

    /**
     * 访问缓存的方式
     */
    private int mCacheMode;

    /**
     * Whether or not responses to this request should be cached.
     * 是否应该缓存
     */
    private boolean mShouldCache = true;

    /**
     * Whether or not this request has been canceled.
     * 是否取消请求
     */
    private boolean mCanceled = false;


}