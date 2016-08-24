package com.zyh.httpbird.interfac;

import com.zyh.httpbird.request.Request;

/**
 * Created by ruoyun on 16/8/24.
 * 任务控制中心
 */
public interface IControlCenter {
    /**
     * 开始操作
     */
    void start();

    /**
     * 结束操作
     */
    void stop();

    interface RequestFilter {
        boolean apply(Request<?> request);
    }

    /**
     * 取消所有操作
     *
     * @param filter
     */
    void cancelAll(RequestFilter filter);

    void cancelAll(final Object tag);

    <T> Request<T> add(Request<T> request);

    void finish(Request<?> request);

}
