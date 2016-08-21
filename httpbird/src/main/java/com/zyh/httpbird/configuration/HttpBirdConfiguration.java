package com.zyh.httpbird.configuration;

import android.content.Context;
import android.text.TextUtils;

import com.zyh.httpbird.core.HurlStack;
import com.zyh.httpbird.core.JsonConvertFactory;
import com.zyh.httpbird.interfac.IHttpStack;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ruoyun on 16/8/21.
 * <p/>
 * 现在缺少全局的heads设置，以后添加全局的头设置
 */
public class HttpBirdConfiguration {

    private static final String DEFAULT_CACHE_DIR = "httpBird";
    public final Context context;
    public final String cachePath;
    public final int cacheSize;
    public static boolean isDebug;
    public static boolean hasNoHttpConnectCache;
    public final IHttpStack IHttpStack;
    public final JsonConvertFactory jsonConvertFactory;

    public HttpBirdConfiguration(Builder builder) {
        this.context = builder.context;
        this.cachePath = builder.cachePath;
        this.isDebug = builder.isDebug;
        this.hasNoHttpConnectCache = builder.hasNoHttpConnectCache;
        this.IHttpStack = builder.IHttpStack;
        this.jsonConvertFactory = builder.jsonConvertFactory;
        //        this.sslContext = builder.sslContext;
        this.cacheSize = builder.cacheSize;
    }


    public static class Builder {
        private Context context;
        private String cachePath;
        private int cacheSize;
        private boolean isDebug;
        private boolean hasNoHttpConnectCache;
        private IHttpStack IHttpStack;
        private JsonConvertFactory jsonConvertFactory;
        //        private SSLContext sslContext;//http urlconnection 的SSLSocketFactory类是不同的。
        //证书，可以是多个证书 jks 单向验证
        private List certificatesList = new ArrayList();
        //private InputStream[] certificates;
        //private String[] certificatesPath;

        //双向验证 bks
        private InputStream bksFile;
        private String bksPassword;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.cachePath = null;
            this.isDebug = false;
            this.hasNoHttpConnectCache = false;
            this.IHttpStack = null;
            this.jsonConvertFactory = null;
            this.bksFile = null;
            this.bksPassword = null;
            this.cacheSize = DefaultConfigurationFactory.DEFAULT_DISK_USAGE_BYTES;
        }

        public Builder builderCachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public Builder builderCacheSize(int cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public Builder builderIsDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;

        }

        public Builder builderHasNoHttpConnectCache(boolean isHas) {
            this.hasNoHttpConnectCache = isHas;
            return this;
        }

        public Builder builderHttpStack(IHttpStack IHttpStack) {
            this.IHttpStack = IHttpStack;
            return this;
        }

        public Builder builderJsonConvertFactory(JsonConvertFactory jsonConvertFactory) {
            this.jsonConvertFactory = jsonConvertFactory;
            return this;
        }

        public Builder builderSetCertificates(InputStream... certificates) {//单项验证
            certificatesList.addAll(Arrays.asList(certificates));
            return this;
        }

        public Builder builderSetCertificatesFromAssets(String... certificatesPath) {
            for (String path : certificatesPath) {
                try {
                    certificatesList.add(context.getAssets().open(path));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        public Builder builderClientKeyManagerFromAssets(String bksFilePath, String bksPassword) {
            try {
                this.bksFile = context.getAssets().open(bksFilePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.bksPassword = bksPassword;
            return this;
        }

        public HttpBirdConfiguration build() {
            initEmptyFieldsWithDefaultValues();
            return new HttpBirdConfiguration(this);
        }


        private void initEmptyFieldsWithDefaultValues() {
            if (TextUtils.isEmpty(cachePath)) {
                this.cachePath = DefaultConfigurationFactory.createDiskCache(context, HttpBirdConfiguration.DEFAULT_CACHE_DIR);
            }
            if (IHttpStack == null) {
                IHttpStack = new HurlStack();
            }
            if (certificatesList.size() > 0 || bksFile != null) {
                IHttpStack.setSslSocketFactory(DefaultConfigurationFactory.setCertificates((InputStream[]) certificatesList.toArray(), bksFile, bksPassword).getSocketFactory());
            }

            //默认使用gson 解析
            if (jsonConvertFactory == null) {
                //                jsonConvertFactory = new GsonFactory();
            }
        }
    }

}
