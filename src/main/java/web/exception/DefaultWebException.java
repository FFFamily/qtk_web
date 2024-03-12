package web.exception;

/**
 * 默认web异常
 */
public class DefaultWebException extends RuntimeException{
    public DefaultWebException(String message) {
        super(message);
    }
}
