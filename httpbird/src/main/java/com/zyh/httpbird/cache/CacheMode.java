package com.zyh.httpbird.cache;

/**
 * Created by ruoyun on 16/8/21.
 * 缓存的方式
 */
public class CacheMode {
    /**
     * 按照HTTP协议的默认缓存规则，例如有304响应头时缓存
     */
    int DEFAULT = 0;

    /**
     * 不使用缓存
     */
    int NO_CACHE = 1;

    /**
     * 请求网络失败后，读取缓存
     */
    int REQUEST_FAILED_READ_CACHE = 2;

    /**
     * 如果缓存不存在才请求网络，否则使用缓存
     */
    int IF_NONE_CACHE_REQUEST = 3;

    /**
     * 先使用缓存，不管是否存在，仍然请求网络
     */
    int FIRST_CACHE_THEN_REQUEST = 4;


}
