package cn.eeepay.framework.model;

import cn.eeepay.framework.exception.AgentWebException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/19.
 */
public class ResponseBean {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ResponseBean.class);
    private String message;
    private boolean success;
    private Object data;
    private int count;

    public ResponseBean(Object obj, boolean success){
        this(obj);
        setSuccess(success);
    }

    public ResponseBean(Object obj, int count){
        this(obj);
        setCount(count);
    }

    public ResponseBean(Object obj){
        if(obj == null){
            setSuccess(true);
        }else if(obj instanceof Boolean){
            setSuccess((Boolean) obj);
        }else if(obj instanceof String){
            setMessage(obj.toString());
            setSuccess(false);
        }else if(obj instanceof Collection){
            setData(obj);
            setCount(((Collection) obj).size());
            setSuccess(true);
        }else if(obj instanceof Map){
            setData(obj);
            setCount(((Map) obj).size());
            setSuccess(true);
        }else if(obj instanceof Throwable){
            LOGGER.warn("发生异常: " + obj);
            if (obj instanceof AgentWebException){
                setMessage(((AgentWebException) obj).getMessage());
            }else{
                setMessage("服务器异常.");
            }
            setSuccess(false);
        }else{
            setData(obj);
            setCount(1);
            setSuccess(true);
        }
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

}
