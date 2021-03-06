package vip.ruoyun.httpbird;


import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import vip.ruoyun.httpbird.core.HttpBirdConfiguration;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;
import vip.ruoyun.httpbird.managers.DownloadManager;
import vip.ruoyun.httpbird.managers.ThreadManager;
import vip.ruoyun.httpbird.utils.L;

/**
 * 上传与下载帮助类
 * Created by ruoyun on 2015/8/21.
 */
public class HttpBird {

    /**
     * 可以配置各种属性
     *
     * @param configuration
     */
    public static synchronized void init(HttpBirdConfiguration configuration) {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration can not be initialized with null");
        } else {
            DownloadManager.getInstance().init(configuration);
        }
    }

    /**
     * 默认构造方法
     *
     * @param context
     */
    public static synchronized void init(Context context) {
        if (context == null) {
            throw new IllegalArgumentException("context can not be initialized with null");
        } else {
            HttpBirdConfiguration configuration = new HttpBirdConfiguration.Builder(context.getApplicationContext()).build();
            DownloadManager.getInstance().init(configuration);
        }
    }

    /**
     * 下载方法
     *
     * @param url 路径
     * @return
     */
    public static DownloadBuilder download(String url) {
        return new DownloadBuilder(url);
    }

    /**
     * 暂停方法
     *
     * @param url
     */
    public static void pause(String url) {
        L.d("暂停方法执行");
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().pause(url);
    }

    /**
     * 取消方法
     *
     * @param url
     */
    public static void cancel(String url) {
        L.d("取消方法执行");
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().cancel(url);
    }

    /**
     * 继续方法，这个方法不推荐使用，推荐使用download
     *
     * @param url
     * @param listener
     */
    public static void conTinue(String url, FileLoadingListener listener) {
        L.d("conTinue方法执行");
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().conTinue(url, listener);
    }

    /**
     * 上传方法
     */
    public static UploadBuilder upload(String url) {
        return new UploadBuilder(url);
    }

    /**
     * 获得线程池
     *
     * @return 线程池实例
     */
    public static ThreadManager.ThreadPoolProxy getThreadPool() {
        return ThreadManager.getThreadPool();
    }

    public static String getCachePath() {
        return DownloadManager.getInstance().getCachePath();
    }

    public static class DownloadBuilder {
        private String url;
        private String targetName;
        private FileLoadingListener listener;

        private DownloadBuilder(String url) {
            this.url = url;
        }

        public DownloadBuilder targetName(String targetName) {
            this.targetName = targetName;
            return this;
        }

        public DownloadBuilder listener(FileLoadingListener listener) {
            this.listener = listener;
            return this;
        }

        public void go() {
            L.d("下载方法开始");
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            if (TextUtils.isEmpty(url)) {
                listener.onLoadingFailed(url, "url may not be empty");
                return;
            }
            if (TextUtils.isEmpty(targetName)) {
                listener.onLoadingFailed(url, "targetName may not be empty");
                return;
            }
            DownloadManager.getInstance().download(url, targetName, listener);
        }
    }


    public static class UploadBuilder {
        private final String url;
        private Map<String, String> paramMap;
        private Map<String, File> fileMap;
        private Map<String, String> headerMap;
        private FileLoadingListener listener;

        public UploadBuilder(String url) {
            this.url = url;
        }

        public UploadBuilder param(String key, String value) {
            if (paramMap == null) {
                paramMap = new HashMap<>();
            }
            paramMap.put(key, value);
            return this;
        }

        public UploadBuilder file(String key, File file) {
            if (fileMap == null) {
                fileMap = new HashMap<>();
            }
            fileMap.put(key, file);
            return this;
        }


        public UploadBuilder header(String key, String value) {
            if (headerMap == null) {
                headerMap = new HashMap<>();
            }
            headerMap.put(key, value);
            return this;
        }

        public UploadBuilder listener(FileLoadingListener listener) {
            this.listener = listener;
            return this;
        }

        public void go() {
            if (listener == null) {
                throw new IllegalArgumentException("listener must not be null");
            }
            if (TextUtils.isEmpty(url)) {
                listener.onLoadingFailed(url, "targetName may not be empty");
                return;
            }
            DownloadManager.getInstance().upload(url, paramMap, fileMap, headerMap, listener);
        }

    }

}
