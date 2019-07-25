package vip.ruoyun.httpbird.managers;


import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vip.ruoyun.httpbird.core.HttpBirdConfiguration;
import vip.ruoyun.httpbird.db.ThreadDAO;
import vip.ruoyun.httpbird.db.ThreadDAOImpl;
import vip.ruoyun.httpbird.entities.FileInfo;
import vip.ruoyun.httpbird.interfaces.FileLoadingListener;
import vip.ruoyun.httpbird.interfaces.SimpleFileLoadingListener;
import vip.ruoyun.httpbird.task.DownloadTask;
import vip.ruoyun.httpbird.task.Task;
import vip.ruoyun.httpbird.task.UploadTask;
import vip.ruoyun.httpbird.utils.ExecutorDelivery;
import vip.ruoyun.httpbird.utils.L;

/**
 * Created by ruoyun on 2015/8/24.
 */
public class DownloadManager {
    /**
     * 数据库
     */
    private ThreadDAO mDao;
    /**
     * 默认回调方法
     */
    private FileLoadingListener defaultListener = new SimpleFileLoadingListener();
    /**
     * 正在下载中的任务，ConcurrentHashMap  比 Collections.synchronizedMap(new HashMap()); 好用
     */
    private Map<String, Task> mDownloadMap = new ConcurrentHashMap<String, Task>();
    /**
     * 文件随机命名方式，时间戳
     */
    //    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
    /**
     * http 配置参数  /sdcard/Android/data/<application package>/cache/downFile
     */
    private HttpBirdConfiguration configuration;
    /**
     * 单例变量
     */
    private volatile static DownloadManager instance;
    /**
     * 全局使用一个handler来操作主线程
     */
    private Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 全局操作分发任务
     */
    private ExecutorDelivery delivery = new ExecutorDelivery(handler);


    private DownloadManager() {
    }

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }

    public synchronized void init(HttpBirdConfiguration configuration) {
        if (configuration == null) {
            throw new RuntimeException("configuration is null!~~");
        } else {
            this.configuration = configuration;
            mDao = new ThreadDAOImpl(configuration.context);
        }
    }


    /**
     * @param url      路径
     * @param target   名称
     * @param listener 回调方法
     */
    public void download(String url, String target, FileLoadingListener listener) {
        if (mDownloadMap.containsKey(url)) { //从正在运行的mDownloadMap集合中查找有没有这个任务
            L.d("正在下载中，请稍后、。。");
            return;
        }
        if (listener == null) {
            listener = defaultListener;
        }
        listener.onLoadingStarted(url);
        checkLocalFileIsExistsAndMkdir(configuration.cachePath + File.separator + target);//检测目录是否存在，并自动创建
        L.d("DownloadManager检查目录成功");
        //如果没有正在进行下载中的话，从数据库中查找
        FileInfo fileInfo = mDao.getFileInfo(url);
        if (fileInfo == null) {//说明这个任务是不存的，那么就创建这个任务
            L.d("DownloadManager说明这个任务是不存的，那么就创建这个任务");
            fileInfo = new FileInfo(url, target, configuration.cachePath);
            mDao.insertFileInfo(fileInfo);
            DownloadTask task = new DownloadTask(fileInfo, mDao, listener, delivery);
            L.d("DownloadManager添加任务到任务栈中");
            mDownloadMap.put(url, task);
            ThreadManager.getThreadPool().execute(task);
            L.d("DownloadManager添加任务完成");
        } else {//如果数据库中有这条数据的话
            if (fileInfo.isOver()) {//判断是否完成,已经完成
                L.d("DownloadManager数据库中记录中完成");
                //数据库中记录中完成
                if (checkLocalFileIsExists(fileInfo.getFilePath() + File.separator + fileInfo.getFileName())) {//检查本地文件
                    L.d("DownloadManager本地文件中存在");
                    //本地文件中存在
                    listener.onProgressUpdate(url, 1, 1);
                    listener.onLoadingComplete(url, new File(fileInfo.getFilePath() + File.separator + fileInfo.getFileName()), "");
                } else {
                    L.d("DownloadManager本地文件不存在，重新下载");
                    //本地文件不存在，重新下载
                    fileInfo.setOver(false);
                    fileInfo.setFinished(0);
                    fileInfo.setFileName(target);
                    fileInfo.setFilePath("");
                    mDao.updateFileInfo(fileInfo);
                    DownloadTask task = new DownloadTask(fileInfo, mDao, listener, delivery);
                    mDownloadMap.put(url, task);
                    ThreadManager.getThreadPool().execute(task);
                }
            } else {//没有完成，继续下载
                L.d("DownloadManager没有完成，继续下载");
                DownloadTask task = new DownloadTask(fileInfo, mDao, listener, delivery);
                mDownloadMap.put(url, task);
                ThreadManager.getThreadPool().execute(task);
            }
        }
    }

    /**
     * 删除任务
     *
     * @param url
     */
    public void deleteTask(String url) {
        L.d("DownloadManager删除任务");
        mDownloadMap.remove(url);
    }

    /**
     * 检测文件是否存在
     *
     * @param url
     * @return
     */
    private boolean checkLocalFileIsExists(String url) {
        File file = new File(url);
        return file.exists();
    }

    /**
     * 创建文件夹
     *
     * @param url
     */
    private void checkLocalFileIsExistsAndMkdir(String url) {
        url = url.substring(0, url.lastIndexOf("/"));
        File dir = new File(url);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }


    /**
     * 暂停方法
     *
     * @param url
     */
    public void pause(String url) {
        if (mDownloadMap.containsKey(url)) {
            mDownloadMap.get(url).cancel();
            mDownloadMap.remove(url);
        }
    }

    /**
     * 取消下载，逻辑和暂停类似，只是需要删除已下载的文件
     *
     * @param url
     */
    public void cancel(String url) {
        if (mDownloadMap.containsKey(url)) {
            mDownloadMap.remove(url).cancel();
        }
        //查询数据库
        FileInfo fileInfo = mDao.getFileInfo(url);
        if (fileInfo != null) {
            mDao.deleteFileInfo(url);//删除数据库中的数据
            //删除本地文件
            File file = new File(fileInfo.getFileName());
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 此方法不建议使用，可以直接使用download()方法
     *
     * @param url
     * @param listener
     */
    public void conTinue(String url, FileLoadingListener listener) {
        listener.onLoadingStarted(url);
        if (mDownloadMap.containsKey(url)) {
            listener.onLoadingFailed(url, "正在下载中，不能继续");
            return;
        }
        //查询数据库
        FileInfo fileInfo = mDao.getFileInfo(url);
        if (fileInfo != null) {
            DownloadTask task = new DownloadTask(fileInfo, mDao, listener, delivery);
            mDownloadMap.put(url, task);
            ThreadManager.getThreadPool().execute(task);
        } else {
            listener.onLoadingFailed(url, "之前没有下载，不能继续");
        }
    }


    //    public void upload(File file, String url, Map<String, String> params, FileLoadingListener listener) {
    //        if (mDownloadMap.containsKey(url)) { //从正在运行的mDownloadMap集合中查找有没有这个任务
    //            L.d("正在上传中，请稍后");
    //            return;
    //        }
    //        if (listener == null) {
    //            listener = defaultListener;
    //        }
    //        if (!file.exists()) {
    //            listener.onLoadingFailed(url, "文件不存在");
    //            return;
    //        }
    //        listener.onLoadingStarted(url);
    //        //File file, String requestURL, Map<String, String> params, FileLoadingListener listener
    //        UploadHttpClientTask task = new UploadHttpClientTask(file, url, params, listener);
    //        mDownloadMap.put(url, task);
    //        ThreadManager.getThreadPool().execute(task);
    //    }


    /**
     * 上传方法，使用HttpURLConnection
     *
     * @param url
     * @param params
     * @param fileMap
     * @param listener
     */
    public void upload(String url, Map<String, String> params, Map<String, File> fileMap, Map<String, String> headParams, FileLoadingListener listener) {
        if (mDownloadMap.containsKey(url)) { //从正在运行的mDownloadMap集合中查找有没有这个任务
            L.d("正在上传中，请稍后");
            return;
        }
        if (listener == null) {
            listener = defaultListener;
        }
        listener.onLoadingStarted(url);
        FileInfo fileInfo = new FileInfo(url);
        //File file, String requestURL, Map<String, String> params, FileLoadingListener listener
        UploadTask task = new UploadTask(params, fileMap, headParams, fileInfo, listener, delivery);
        mDownloadMap.put(url, task);
        ThreadManager.getThreadPool().execute(task);
    }

    public String getCachePath() {
        return configuration.cachePath;
    }
}
