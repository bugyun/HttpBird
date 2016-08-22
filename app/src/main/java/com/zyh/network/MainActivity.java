package com.zyh.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Request request = new Request();
        request.setCallback(new ICallback() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}
