package com.zyh.httpbird.interfac;

import com.zyh.httpbird.exception.HttpBirdError;
import com.zyh.httpbird.request.Request;

/**
 * Created by ruoyun on 16/8/22.
 * <p/>
 * 回调方法
 */
public interface IHttpAction {


    /**
     * 请求数据之后调用的方法
     *
     * @param isSucceed  请求是否成功
     * @param response   返回的字符串
     * @param netWorkNum 访问序号
     * @param error      错误信息
     */
    public abstract <T> void onDataLoaded(boolean isSucceed, T response, int netWorkNum, HttpBirdError error);

    /**
     * 请求数据之前调用的方法,设置一些请求头信息、请求参数
     *
     * @param request
     * @param netWorkNum 访问序号
     */
    public abstract void setLoadParams(Request request, int netWorkNum);
}
