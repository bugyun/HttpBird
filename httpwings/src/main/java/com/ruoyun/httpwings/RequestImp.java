package com.ruoyun.httpwings;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fanpu on 2017/9/21.
 */

public class RequestImp implements Request {

    public String url;
    private HttpURLConnection urlConnection;

    @Override
    public void createHttpClient() throws Exception {
        try {
            URL url = new URL("http://www.android.com/");
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //            readStream(in);
        } catch (Exception e) {
            throw new Exception("网络错误");
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    @Override
    public void setSsl() {

    }

    @Override
    public void setHeadProperty() {

    }

    @Override
    public void handleResult() {

    }
}
