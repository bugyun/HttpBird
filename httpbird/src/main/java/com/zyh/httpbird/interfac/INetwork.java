package com.zyh.httpbird.interfac;

import com.zyh.httpbird.core.NetworkResponse;
import com.zyh.httpbird.exception.HttpBirdError;
import com.zyh.httpbird.request.Request;

/**
 * Created by ruoyun on 16/8/21.
 * An interface for performing requests.
 * 网络基类
 */
public interface INetwork {

    /**
     * Performs the specified request.
     *
     * @param request Request to process
     * @return A {@link NetworkResponse} with data and caching metadata; will never be null
     * @throws HttpBirdError on errors
     */
    public NetworkResponse performRequest(Request<?> request) throws HttpBirdError;
}
