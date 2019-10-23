package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutAccountTask implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private Date transTime ;
	private BigDecimal transAmount ;
	private BigDecimal upBalance ;
	private BigDecimal upTodayBalance ;
	private BigDecimal outAccountTaskAmount ;
	private Integer upCompanyCount ;
	private Integer outAccountId ;
	private Date sysTime ;
	private Date createTime ;//创建日期
	private String creator ;//创建人
	private String updator ;//修改人
	private Date updateTime ;//更新时间
	private String acqEnname;
	private Integer billStatus;
	
	private String outBillRange;//出账范围
	
	//辅助字段VO
	private String outBillStatus;
	private String dayPhase;
    /*private List<OutAccountTaskDetail> outAccountTaskDetails;
	
	
	public List<OutAccountTaskDetail> getOutAccountTaskDetails() {
		return outAccountTaskDetails;
	}
	public void setOutAccountTaskDetails(List<OutAccountTaskDetail> outAccountTaskDetails) {
		this.outAccountTaskDetails = outAccountTaskDetails;
	}*/
	
	public Integer getId() {
		return id;
	}
	public String getOutBillStatus() {
		return outBillStatus;
	}
	public void setOutBillStatus(String outBillStatus) {
		this.outBillStatus = outBillStatus;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public BigDecimal getUpBalance() {
		return upBalance;
	}
	public void setUpBalance(BigDecimal upBalance) {
		this.upBalance = upBalance;
	}
	public BigDecimal getUpTodayBalance() {
		return upTodayBalance;
	}
	public void setUpTodayBalance(BigDecimal upTodayBalance) {
		this.upTodayBalance = upTodayBalance;
	}
	
	public BigDecimal getOutAccountTaskAmount() {
		return outAccountTaskAmount;
	}
	public void setOutAccountTaskAmount(BigDecimal outAccountTaskAmount) {
		this.outAccountTaskAmount = outAccountTaskAmount;
	}
	public Integer getUpCompanyCount() {
		return upCompanyCount;
	}
	public void setUpCompanyCount(Integer upCompanyCount) {
		this.upCompanyCount = upCompanyCount;
	}
	public Integer getOutAccountId() {
		return outAccountId;
	}
	public void setOutAccountId(Integer outAccountId) {
		this.outAccountId = outAccountId;
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
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	public String getDayPhase() {
		return dayPhase;
	}
	public void setDayPhase(String dayPhase) {
		this.dayPhase = dayPhase;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getOutBillRange() {
		return outBillRange;
	}
	public void setOutBillRange(String outBillRange) {
		this.outBillRange = outBillRange;
	}
	
}
