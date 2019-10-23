package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;

public class BusinessAccountDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer  businessId;
	private String   transNo; //'交易序号',
	private String  outUserNo; //'调出外部用户（商户、代理商、收单机构）编号',
	private String  inUserNo; //'调入外部用户（商户、代理商、收单机构）编号',
	private String accountType; //'调账类型',
	private BigDecimal  amount; //'金额',
	private String   reason; //'调账原因',
	private Integer recordStatus;  //记账状态
	private String recordResult;  //记账结果
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public String getOutUserNo() {
		return outUserNo;
	}
	public void setOutUserNo(String outUserNo) {
		this.outUserNo = outUserNo;
	}
	public String getInUserNo() {
		return inUserNo;
	}
	public void setInUserNo(String inUserNo) {
		this.inUserNo = inUserNo;
	}
	
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Integer getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
	public String getRecordResult() {
		return recordResult;
	}
	public void setRecordResult(String recordResult) {
		this.recordResult = recordResult;
	}
	
}
