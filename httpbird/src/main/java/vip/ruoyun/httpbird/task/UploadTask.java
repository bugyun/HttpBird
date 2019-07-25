package vip.ruoyun.httpbird.task;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

import vip.ruoyun.httpbird.core.HttpBirdConfiguration;
import vip.ruoyun.httpbird.entities.FileInfo;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;
import vip.ruoyun.httpbird.managers.DownloadManager;
import vip.ruoyun.httpbird.utils.ExecutorDelivery;
import vip.ruoyun.httpbird.utils.L;

/**
 * 上传任务类 适应HttpURLConnection ，但是不能获得上传的进度，所以不需要的进度的可以考虑这个
 * Created by ruoyun on 2015/8/21.
 */
public class UploadTask implements Task {
    private static final int TIME_OUT = 10 * 1000; // 超时时间
    private static final String CHARSET = "utf-8"; // 设置编码
    private static final String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    private static final String PREFIX = "--", LINE_END = "\r\n";
    private static final String CONTENT_TYPE = "multipart/form-data"; // 内容类型
    private final Map<String, String> paramMap;
    private final Map<String, File> fileMap;
    private final Map<String, String> headParams;

    public boolean isCancel = false;
    private FileLoadingListener listener;
    private FileInfo fileInfo;
    private ExecutorDelivery delivery;

    public UploadTask(Map<String, String> paramMap, Map<String, File> fileMap, Map<String, String> headParams, FileInfo fileInfo, FileLoadingListener listener, ExecutorDelivery delivery) {
        this.paramMap = paramMap;
        this.fileMap = fileMap;
        this.headParams = headParams;
        this.fileInfo = fileInfo;
        this.listener = listener;
        this.delivery = delivery;
    }

    public void cancel() {
        isCancel = true;
    }


    @Override
    public void run() {
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        FileInputStream is = null;
        InputStream input = null;
        ByteArrayOutputStream outputStream = null;
        try {
            URL url = new URL(fileInfo.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            /** http头
             * 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃,此方法用于在预先不知道内容长度时启用,
             * 没有进行内部缓冲的 HTTP 请求正文的流。
             */
            if (HttpBirdConfiguration.isChunked) {
                conn.setChunkedStreamingMode(1024 * 1024 * 2);//设置上传文件的缓存大小2mb
            }
            conn.setReadTimeout(TIME_OUT);
            conn.setConnectTimeout(TIME_OUT);
            conn.setDoInput(true); // 允许输入流
            conn.setDoOutput(true); // 允许输出流
            conn.setUseCaches(false); // 不允许使用缓存
            conn.setRequestMethod("POST"); // 请求方式
            conn.setRequestProperty("Charset", CHARSET); // 设置编码
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (headParams != null) {
                for (Map.Entry<String, String> entry : headParams.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            dos = new DataOutputStream(conn.getOutputStream());
            getPostParamString(paramMap, dos);
            getPostParamFile(fileMap, dos);
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            //测试代码
            //            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "heyiliang.txt");
            //            if (!file.exists()) {
            //                file.createNewFile();
            //            }
            //            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file));
            //            getPostParamString(paramMap, dataOutputStream);
            //            getPostParamFile(fileMap, dataOutputStream);
            //            dataOutputStream.write(end_data);
            //            dataOutputStream.flush();
            //            dataOutputStream.close();
            /**
             * 获取响应码  200=成功
             * 当响应成功，获取响应的流
             */
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                L.d("UploadTask上传成功" + responseCode);
                // 在调用下边的getInputStream()函数时才将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
                input = conn.getInputStream();
                //byte[] data
                outputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024 * 4];
                int ss = -1;
                while ((ss = input.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, ss);
                }
                String resultString = outputStream.toString(CHARSET);
                fileInfo.setResponse(resultString);
                delivery.postResponse(ExecutorDelivery.MESSAGE_SUCCESS, listener, fileInfo);
            } else {
                fileInfo.setException("错误代码为：" + responseCode);
                delivery.postResponse(ExecutorDelivery.MESSAGE_FAILURE, listener, fileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fileInfo.setException("错误为：" + e.getMessage());
            delivery.postResponse(ExecutorDelivery.MESSAGE_FAILURE, listener, fileInfo);
        } finally {
            DownloadManager.getInstance().deleteTask(fileInfo.getUrl());
            try {
                if (outputStream != null) {
                    outputStream.flush();
                    outputStream.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
                if (dos != null) {
                    dos.close();
                }
                if (is != null) {
                    is.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    public void getPostParamString(Map<String, String> map, DataOutputStream outputStream) throws IOException {
        if (map == null)
            return;
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            L.d("key= " + entry.getKey() + " and value= " + entry.getValue());
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINE_END);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            sb.append(entry.getValue());
            sb.append(LINE_END);
        }
        outputStream.write(sb.toString().getBytes());
    }

    public void getPostParamFile(Map<String, File> map, DataOutputStream outputStream) throws IOException {
        if (map == null)
            return;
        for (Map.Entry<String, File> entry : map.entrySet()) {
            fileInfo.setLength(fileInfo.getLength() + entry.getValue().length());
        }
        int mFinished = 0;
        int len = 0;
        for (Map.Entry<String, File> entry : map.entrySet()) {
            StringBuffer sb = new StringBuffer();
            L.d("key= " + entry.getKey() + " and value= " + entry.getValue());
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"; filename=\"" + entry.getValue().getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            outputStream.write(sb.toString().getBytes());
            InputStream is = new FileInputStream(entry.getValue());
            byte[] bytes = new byte[4096];
            while ((len = is.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                mFinished += len;
                L.d("UploadTask当前完成度：" + mFinished);
                if (listener != null) {
                    listener.onProgressUpdate(fileInfo.getUrl(), mFinished, fileInfo.getLength());
                }
//                fileInfo.setFinished(mFinished);
//                delivery.postResponse(ExecutorDelivery.MESSAGE_UPDATE, listener, fileInfo);
            }
            is.close();
            outputStream.write(LINE_END.getBytes());
        }
    }

}
