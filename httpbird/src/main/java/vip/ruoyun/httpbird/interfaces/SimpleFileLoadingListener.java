package vip.ruoyun.httpbird.interfaces;

import java.io.File;

/**
 * Created by ruoyun on 2015/8/26.
 * 空方法实现
 */
public class SimpleFileLoadingListener implements FileLoadingListener {
    @Override
    public void onLoadingStarted(String url) {

    }

    @Override
    public void onProgressUpdate(String url, long current, long total) {

    }

    @Override
    public void onLoadingComplete(String url, File loadedFile, String response) {

    }

    @Override
    public void onLoadingFailed(String url, String message) {

    }

    @Override
    public void onLoadingCancelled(String url) {

    }
}
