package vip.ruoyun.httpbird.core;

import android.content.Context;
import android.text.TextUtils;

/**
 * Created by Andy on 15/8/26.
 * 预留其他接口类，可以配置其他参数 (Description)
 */
public class HttpBirdConfiguration {
    /**
     * Default on-disk cache directory.
     */
    private static final String DEFAULT_CACHE_DIR = "httpBird";
    public final Context context;
    public final String cachePath;
    public static boolean isDebug;
    public static boolean isChunked;


    public HttpBirdConfiguration(Builder builder) {
        this.context = builder.context;
        this.cachePath = builder.cachePath;
        this.isDebug = builder.isDebug;
        this.isChunked = builder.isChunked;
    }


    public static class Builder {
        private Context context;
        private String cachePath;
        private boolean isDebug;
        private boolean isChunked;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
            this.cachePath = null;
            this.isDebug = false;
        }


        public Builder builderCachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        public Builder builderIsDebug(boolean isDebug) {
            this.isDebug = isDebug;
            return this;
        }

        public Builder builderIsChunked(boolean isChunked) {
            this.isChunked = isChunked;
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
        }
    }
}
