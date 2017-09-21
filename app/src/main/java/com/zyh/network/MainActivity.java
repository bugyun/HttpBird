package com.zyh.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    OkHttpClient client = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testOkhttp();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        testOkhttp();
        return super.onTouchEvent(event);
    }

    public void testOkhttp() {

        Request request = new Request.Builder()
                .url("http://square.github.io/okhttp/")
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "失败", Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Toast.makeText(getApplicationContext(), "成功", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
