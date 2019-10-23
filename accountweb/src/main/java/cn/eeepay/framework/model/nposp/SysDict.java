package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
/**
 * 
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
	 private String sysValue;
	 private String orderNo;
	 private String status;
	 private String remark;
	 private String type;
	 private String parentId;
	 
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
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
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
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
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof SysDict) {   
//			SysDict sysDict = (SysDict) obj;   
//            return this.id.equals(sysDict.id);   
//        }   
//        return super.equals(obj); 
//	}
	
	 
}
