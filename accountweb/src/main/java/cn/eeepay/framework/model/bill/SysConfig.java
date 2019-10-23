package cn.eeepay.framework.model.bill;

import java.io.Serializable;

public class SysConfig implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String paramKey;
	private String paramValue;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}
	public String getParamValue() {
		return paramValue;
	}
	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}
	
	

}
