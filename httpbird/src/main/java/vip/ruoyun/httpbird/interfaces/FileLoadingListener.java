package vip.ruoyun.httpbird.interfaces;

import java.io.File;

/**
 * Created by Andy on 15/8/24.
 * 回调方法类 (Description)
 */
public interface FileLoadingListener {
    /**
     * 开始
     *
     * @param url 网络路径
     */
    void onLoadingStarted(String url);

    /**
     * 进度条
     *
     * @param url     网络路径
     * @param current 当前进度
     * @param total   总长度
     */
    void onProgressUpdate(String url, long current, long total);

    /**
     * @param url        网路路径
     * @param loadedFile 文件
     */
    void onLoadingComplete(String url, File loadedFile, String response);

    /**
     * 失败方法
     *
     * @param url
     * @param message
     */
    void onLoadingFailed(String url, String message);

    /**
     * 任务取消的方法
     *
     * @param url
     */
    void onLoadingCancelled(String url);
}
