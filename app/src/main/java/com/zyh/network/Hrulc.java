package com.zyh.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by ruoyun on 16/8/21.
 */
public class Hrulc {


    public static String exeute(Request request) {
        switch (request.method) {
            case Request.Method.DELETE:
                post(request);
                break;

        }

        return null;
    }

    public static String post(Request request) {
        HttpURLConnection conn;
        URL url;
        InputStream is;
        OutputStream os;
        StringBuffer sb = new StringBuffer();
        try {
            url = new URL(request.url);

            //打开服务器
            conn = (HttpURLConnection) url.openConnection();
            //设置输入输出流
            conn.setDoOutput(true);
            conn.setDoInput(true);
            //设置请求的方法为Post
            conn.setRequestMethod("POST");
            //Post方式不能缓存数据，则需要手动设置使用缓存的值为false
            conn.setUseCaches(false);
            addHeaders(conn, request.headers);
            //连接数据库
            conn.connect();
            /**写入参数**/
            os = conn.getOutputStream();
            //封装写给服务器的数据（这里是要传递的参数）
            DataOutputStream dos = new DataOutputStream(os);
            //写方法：name是key值不能变，编码方式使用UTF-8可以用中文
            dos.writeBytes("name=" + URLEncoder.encode("", "UTF-8"));
            //关闭外包装流
            dos.close();
            /**读服务器数据**/
            is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public static void addHeaders(HttpURLConnection connection, Map<String, String> headers) {


    }


}
