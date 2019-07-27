package vip.ruoyun.httpbird.entities;

/**
 * 下载文件类
 * Created by ruoyun on 2015/8/21.
 */
public class FileInfo {
    /**
     * 网络路径，唯一
     */
    private String url;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件保存路径
     */
    private String filePath;
    /**
     * 大小长度
     */
    private long length = 0;
    /**
     * 完成多少字节
     */
    private long finished;
    /**
     * 下载文件是否完成
     */
    private boolean isOver;

    /**
     * 下载开始的位置，保留字段（多线程下载使用）
     */
    private long startPosition;

    /**
     * 下载结束的位置，保留字段（多线程下载使用）
     */
    private long endPosition;

    private String exception;

    private boolean isPause;
    /**
     * 是否支持断点下载
     */
    private boolean isSupportRanges;
    /**
     * 重定向地址
     */
    private String movedTempUrl;
    /**
     * 请求返回数据
     */
    private String response;

    public FileInfo() {

    }

    public FileInfo(String url) {
        this(url, "", "");
    }

    public FileInfo(String url, String fileName, String filePath) {
        this.url = url;
        this.fileName = fileName;
        this.filePath = filePath;
        this.length = 0;
        this.finished = 0;
        this.isOver = false;
        this.isPause = false;
        this.startPosition = 0;
        this.endPosition = 0;
        this.exception = "没有异常";
        this.isSupportRanges = false;
    }


    public String getMovedTempUrl() {
        return movedTempUrl;
    }

    public void setMovedTempUrl(String movedTempUrl) {
        this.movedTempUrl = movedTempUrl;
    }

    public boolean isSupportRanges() {
        return isSupportRanges;
    }

    public void setSupportRanges(boolean supportRanges) {
        isSupportRanges = supportRanges;
    }

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        isPause = pause;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }


    public long getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(long startPosition) {
        this.startPosition = startPosition;
    }

    public long getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(long endPosition) {
        this.endPosition = endPosition;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "url='" + url + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", length=" + length +
                ", finished=" + finished +
                ", isOver=" + isOver +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", exception='" + exception + '\'' +
                ", isPause=" + isPause +
                ", isSupportRanges=" + isSupportRanges +
                ", movedTempUrl='" + movedTempUrl + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
