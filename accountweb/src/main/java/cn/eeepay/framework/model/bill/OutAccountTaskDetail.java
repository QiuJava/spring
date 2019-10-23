package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutAccountTaskDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private Integer outAccountTaskId ;
	private String acqOrgNo ;
	private BigDecimal todayAmount ;
	private BigDecimal upBalance ;
	private BigDecimal todayBalance ;
	private BigDecimal outAccountAmount ;
	private Date sysTime ;
	private Date createTime ;
	
	private SysDict sysDict;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOutAccountTaskId() {
		return outAccountTaskId;
	}
	public void setOutAccountTaskId(Integer outAccountTaskId) {
		this.outAccountTaskId = outAccountTaskId;
	}
 
	public String getAcqOrgNo() {
		return acqOrgNo;
	}
	public void setAcqOrgNo(String acqOrgNo) {
		this.acqOrgNo = acqOrgNo;
	}
	public BigDecimal getTodayAmount() {
		return todayAmount;
	}
	public void setTodayAmount(BigDecimal todayAmount) {
		this.todayAmount = todayAmount;
	}

	public BigDecimal getUpBalance() {
		return upBalance;
	}
	public void setUpBalance(BigDecimal upBalance) {
		this.upBalance = upBalance;
	}
	public BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
	}
	public BigDecimal getOutAccountAmount() {
		return outAccountAmount;
	}
	public void setOutAccountAmount(BigDecimal outAccountAmount) {
		this.outAccountAmount = outAccountAmount;
	}
	public Date getSysTime() {
		return sysTime;
	}
	public void setSysTime(Date sysTime) {
		this.sysTime = sysTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public SysDict getSysDict() {
		return sysDict;
	}
	public void setSysDict(SysDict sysDict) {
		this.sysDict = sysDict;
	}
	
		
}
