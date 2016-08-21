package com.zyh.httpbird.core;

/**
 * Created by ruoyun on 2016年08月21日
 * json转换接口
 */
public interface JsonConvertFactory {

    public abstract <T> T fromJson(String json, Class<T> clzz) throws Exception;

    public abstract <T> String toJson(T bean);
}
