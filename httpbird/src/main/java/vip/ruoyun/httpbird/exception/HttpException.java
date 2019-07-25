package vip.ruoyun.httpbird.exception;

/**
 * Created by Andy on 15/8/24.
 * 类 (Description)
 */
public class HttpException extends Exception {
    private static final long serialVersionUID = 1L;

    public HttpException() {
    }

    public HttpException(String detailMessage) {
        super(detailMessage);
    }

    public HttpException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public HttpException(Throwable throwable) {
        super(throwable);
    }
}
