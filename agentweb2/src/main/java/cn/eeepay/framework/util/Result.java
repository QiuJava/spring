package cn.eeepay.framework.util;

import java.io.Serializable;

/**
 * 服务器返回数据结果
 * tans
 * 2017-11-29
 */
public class Result implements Serializable {
    
	private static final long serialVersionUID = 3677878008034002428L;

	//是否处理成功
	private boolean status = false;
	
	//提示信息
	private String msg;
	
	//状态码
	private Integer code = 400;
	
	//数据
	private Object data;

	
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Result [status=" + status + ", msg=" + msg + ", code=" + code + ", data=" + data + "]";
	}
	
}
