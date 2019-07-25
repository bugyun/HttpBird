package vip.ruoyun.httpbird.task;


import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import vip.ruoyun.httpbird.db.ThreadDAO;
import vip.ruoyun.httpbird.entities.FileInfo;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;
import vip.ruoyun.httpbird.managers.DownloadManager;
import vip.ruoyun.httpbird.utils.ExecutorDelivery;
import vip.ruoyun.httpbird.utils.L;

/**
 * 下载任务类
 * Created by ruoyun on 2015/8/21.
 */
public class DownloadTask implements Task {
    /**
     * 流程
     * 1、获取网络文件的长度
     * 2、在本地创建一个文件，设置其长度
     * 3、从数据库中获得上次下载的进度
     * 4、从上次下载的位置下载数据，同事保存进度到数据库
     * 5、讲下载进度回传到activity
     * 6、下载完成后删除下载信息
     */
    private FileInfo mFileInfo;
    private int mFinished = 0;
    public boolean isPause = false;
    private ThreadDAO mDao;
    private FileLoadingListener listener;
    private ExecutorDelivery delivery;

    public DownloadTask(FileInfo fileInfo, ThreadDAO mDao, FileLoadingListener listener, ExecutorDelivery delivery) {
        this.mFileInfo = fileInfo;
        this.mDao = mDao;
        this.listener = listener;
        this.delivery = delivery;
    }

    public void cancel() {
        isPause = true;
    }

    @Override
    public void run() {
        if (mFileInfo.getLength() > 0) {
            delivery.postResponse(ExecutorDelivery.MESSAGE_UPDATE, listener, mFileInfo);
        }
        HttpURLConnection connection = null;
        RandomAccessFile raf = null;
        InputStream inputStream = null;

        try {
            L.d("DownloadTask开始下载");
            URL url = new URL(mFileInfo.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            // 设置下载位置
            long start = mFileInfo.getStart() + mFileInfo.getFinished();
            //设置range 属性的话，服务器认为我们是部分下载，返回的状态值为206 mFileInfo.getEnd()109231
            connection.setRequestProperty("Range", "bytes=" + start + "-");
            // 设置文件写入位置,这里如果用new file("","")会出现权限错误
            File file = new File(mFileInfo.getFilePath() + File.separator + mFileInfo.getFileName());
            //            result.file = file;
            int length = -1;
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                // 获得文件的长度
                length = connection.getContentLength();
                L.d("DownloadTask文件大小为" + length);
                if (length <= 0) {
                    mFileInfo.setException("文件大小小于0");
                    delivery.postResponse(ExecutorDelivery.MESSAGE_FAILURE, listener, mFileInfo);
                    return;
                }
                //输入流
                if (!file.exists()) {
                    file.createNewFile();
                }
                raf = new RandomAccessFile(file, "rwd");
                raf.setLength(length);
                raf.seek(start);
                if (mFileInfo.getLength() == 0) {
                    mFileInfo.setLength(length);
                }
                mFinished += mFileInfo.getFinished();
                // 读取数据
                inputStream = connection.getInputStream();
                byte buf[] = new byte[1024 << 2];
                int len = -1;
                long time = System.currentTimeMillis();
                while ((len = inputStream.read(buf)) != -1) {
                    // 写入文件
                    raf.write(buf, 0, len);
                    // 把下载进度发送广播给Activity
                    mFinished += len;
                    if (System.currentTimeMillis() - time > 500) {
                        time = System.currentTimeMillis();
                        L.d("DownloadTask当前完成度：" + mFinished);
                        mFileInfo.setFinished(mFinished);
                        delivery.postResponse(ExecutorDelivery.MESSAGE_UPDATE, listener, mFileInfo);
                    }
                    // 在下载暂停时，保存下载进度
                    if (isPause) {
                        L.d("DownloadTask暂停" + mFinished);
                        mFileInfo.setPause(isPause);
                        mFileInfo.setFinished(mFinished);
                        delivery.postResponse(ExecutorDelivery.MESSAGE_UPDATE, listener, mFileInfo);
                        delivery.postResponse(ExecutorDelivery.MESSAGE_PAUSE, listener, mFileInfo);
                        mDao.updateFileInfo(mFileInfo);
                        return;
                    }
                }
                // 删除线程信息
                mFileInfo.setFinished(mFinished);
                mFileInfo.setOver(true);
                delivery.postResponse(ExecutorDelivery.MESSAGE_UPDATE, listener, mFileInfo);
                delivery.postResponse(ExecutorDelivery.MESSAGE_SUCCESS, listener, mFileInfo);
                mDao.updateFileInfo(mFileInfo);
                L.d("DownloadTask下载完毕");
            } else {
                L.d("DownloadTask失败了" + responseCode);
                //这里表示失败了
                mFileInfo.setFinished(0);
                mFileInfo.setOver(false);
                mFileInfo.setException("错误为：" + responseCode);
                delivery.postResponse(ExecutorDelivery.MESSAGE_FAILURE, listener, mFileInfo);
                mDao.updateFileInfo(mFileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mFileInfo.setFinished(0);
            mFileInfo.setOver(false);
            mFileInfo.setException(e.getMessage().toString());
            delivery.postResponse(ExecutorDelivery.MESSAGE_FAILURE, listener, mFileInfo);
            mDao.updateFileInfo(mFileInfo);
        } finally {
            DownloadManager.getInstance().deleteTask(mFileInfo.getUrl());
            try {
                if (connection != null) {
                    connection.disconnect();
                }
                if (raf != null) {
                    raf.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

}
