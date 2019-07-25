package vip.ruoyun.httpbird;


import android.content.Context;

import java.io.File;
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
     * @param url      路径
     * @param target   保存名称
     * @param listener 回调方法
     * @return
     */
    public static void download(String url, String target, FileLoadingListener listener) {
        L.d("下载方法开始");
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        } else if (target == null) {
            throw new IllegalArgumentException("context may not be null");
        } else {
            DownloadManager.getInstance().download(url, target, listener);
        }
    }


    /**
     * 下载方法
     *
     * @param url    路径
     * @param target 保存名称
     * @return
     */
    public static void download(String url, String target) {
        download(url, target, null);
    }

    /**
     * 暂停方法
     *
     * @param url
     */
    public static void pause(String url) {
        L.d("暂停方法执行");
        if (url == null) {
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
        if (url == null) {
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
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().conTinue(url, listener);
    }

    /**
     * 上传方法
     */


    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, FileLoadingListener listener) {
        L.d("上传方法开始");
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().upload(url, paramMap, fileMap, null, listener);
    }

    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap) {
        L.d("上传方法开始");
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().upload(url, paramMap, fileMap, null, null);
    }

    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, Map<String, String> headerMap) {
        L.d("上传方法开始");
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().upload(url, paramMap, fileMap, headerMap, null);
    }

    public static void upload(String url, Map<String, String> paramMap, Map<String, File> fileMap, Map<String, String> headerMap, FileLoadingListener listener) {
        L.d("上传方法开始");
        if (url == null) {
            throw new IllegalArgumentException("url may not be null");
        }
        DownloadManager.getInstance().upload(url, paramMap, fileMap, headerMap, listener);
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

    //    public static void upload(File file, String url, Map<String, String> params, FileLoadingListener listener) {
//        L.d("上传方法开始");
//        if (url == null) {
//            throw new IllegalArgumentException("url may not be null");
//        }
//        if (file == null) {
//            throw new IllegalArgumentException("file may not be null");
//        }
//        DownloadManager.getInstance().upload(file, url, params, listener);
//    }


//    public static void upload(File file, String url, Map<String, String> params) {
//        upload(file, url, params, null);
//    }

}
