package com.zyh.httpbird;

import android.content.Context;

import com.zyh.httpbird.configuration.HttpBirdConfiguration;

/**
 * Created by ruoyun on 16/8/21.
 */
public class HttpBird {
    private volatile static HttpBird mInstance;
    private HttpBirdConfiguration mConfiguration;


    private HttpBird() {

    }

    public static HttpBird getInstance() {
        if (mInstance == null) {
            synchronized (HttpBird.class) {
                if (mInstance == null) {
                    mInstance = new HttpBird();
                }
            }
        }
        return mInstance;
    }

    public synchronized void init(Context context) {
        if (context == null) {
            throw new RuntimeException("context is null!~~");
        }
        mConfiguration = new HttpBirdConfiguration.Builder(context).build();
    }


    public synchronized void init(HttpBirdConfiguration configuration) {
        if (configuration == null) {
            throw new RuntimeException("configuration is null!~~");
        }
        this.mConfiguration = configuration;
    }


}
