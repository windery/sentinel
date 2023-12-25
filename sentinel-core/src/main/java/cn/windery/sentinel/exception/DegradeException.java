package cn.windery.sentinel.exception;

public class DegradeException extends BlockException {

    public DegradeException() {
    }

    public DegradeException(String message) {
        super(message);
    }

    public DegradeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DegradeException(Throwable cause) {
        super(cause);
    }

    public DegradeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
