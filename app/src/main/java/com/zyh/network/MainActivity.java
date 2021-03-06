package com.zyh.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;

import vip.ruoyun.httpbird.HttpBird;
import vip.ruoyun.httpbird.core.HttpBirdConfiguration;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;

public class MainActivity extends AppCompatActivity {


    private TextView mTvDown;
    private ProgressBar mPbProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        HttpBirdConfiguration configuration = new HttpBirdConfiguration.Builder(this).builderIsDebug(true).build();
//        HttpBirdConfiguration configuration = new HttpBirdConfiguration.Builder(this)
//                .builderIsDebug(true)
//                .builderIsChunked(true)
//                .builderCachePath("")
//                .build();
        HttpBird.init(configuration);


        mTvDown = findViewById(R.id.mTvDown);
        mPbProgress = findViewById(R.id.mPbProgress);
        mTvDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testDownBuilder();
            }
        });
    }

    private void testDownBuilder() {
        HttpBird.download("http://www.imooc.com/mobile/imooc.apk")
                .targetName("imooc.apk")
                .listener(new FileLoadingListener() {
                    @Override
                    public void onLoadingStarted(String url) {
                        mTvDown.setText("正在下载");
                    }

                    @Override
                    public void onProgressUpdate(String url, long current, long total) {
                        mPbProgress.setMax((int) total);
                        mPbProgress.setProgress((int) current);
                    }

                    @Override
                    public void onLoadingComplete(String url, File loadedFile, String response) {
                        mTvDown.setText("下载完成");
                    }

                    @Override
                    public void onLoadingFailed(String url, String message) {
                        mTvDown.setText("下载失败");
                    }

                    @Override
                    public void onLoadingCancelled(String url) {
                        mTvDown.setText("下载暂停");
                    }
                }).go();
    }

    private void testUploadBuilder() {
        HttpBird.upload("")
                .file("", new File(""))
                .param("", "")
                .header("", "")
                .listener(new FileLoadingListener() {
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
                }).go();
    }
}
