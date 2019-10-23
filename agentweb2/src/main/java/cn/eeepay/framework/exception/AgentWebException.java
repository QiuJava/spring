package cn.eeepay.framework.exception;

/**
 * Created by Administrator on 2017/5/18.
 */
public class AgentWebException extends RuntimeException {
    public AgentWebException() {
        super();
    }

    public AgentWebException(String message) {
        super(message);
    }

    public AgentWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgentWebException(Throwable cause) {
        super(cause);
    }

    protected AgentWebException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
