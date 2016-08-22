package com.zyh.network;

/**
 * Created by ruoyun on 16/8/21.
 */
public interface ICallback {

    void onSuccess(String result);

    void onFailure(Exception e);


}
