package cn.eeepay.framework.model.bill;

import java.io.Serializable;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public class SysDict implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private Integer id;
	 private String sysKey;
	 private String sysName;
	 private String htmlName;
	 private String sysValue;
	 private Integer orderNo;
	 private String status;
	 private String remark;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSysKey() {
		return sysKey;
	}
	public void setSysKey(String sysKey) {
		this.sysKey = sysKey;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getSysValue() {
		return sysValue;
	}
	public void setSysValue(String sysValue) {
		this.sysValue = sysValue;
	}
	
	public Integer getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHtmlName() {
		return htmlName;
	}
	public void setHtmlName(String htmlName) {
		this.htmlName = htmlName;
	}
	
	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof SysDict) {   
//			SysDict sysDict = (SysDict) obj;   
//            return this.id.equals(sysDict.id);   
//        }   
//        return super.equals(obj); 
//	}
	
	 
}
