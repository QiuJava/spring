package cn.eeepay.framework.exception;


public class RequestLimitException extends RuntimeException {
	private static final long serialVersionUID = 1364225358754654702L;

    public RequestLimitException(){
//        super("HTTP请求超出设定的限制,请求过于频繁!");
        super("查询频率过快!");
    }

    public RequestLimitException(String message){
        super(message);
    }
}
