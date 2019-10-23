package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HlfAgentDebtRecord implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String agentNo;
    private String agentName;
    private String agentLevel;
    private String agentNode;
    private String parentAgentNo;
    private String parentAgentName;
    private String oneAgentNo;
    private String oneAgentName;
    private BigDecimal debtAmount;
    private BigDecimal adjustAmount;
    private BigDecimal shouldDebtAmount;
    private String orderNo;
    private Date debtTime;
    private String remark;
    private String merchantNo;
    
    private String date1;
    private String date2;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getParentAgentNo() {
		return parentAgentNo;
	}
	public void setParentAgentNo(String parentAgentNo) {
		this.parentAgentNo = parentAgentNo;
	}
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public BigDecimal getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
	}
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	public BigDecimal getShouldDebtAmount() {
		return shouldDebtAmount;
	}
	public void setShouldDebtAmount(BigDecimal shouldDebtAmount) {
		this.shouldDebtAmount = shouldDebtAmount;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Date getDebtTime() {
		return debtTime;
	}
	public void setDebtTime(Date debtTime) {
		this.debtTime = debtTime;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date1) {
		this.date1 = date1;
	}
	public String getDate2() {
		return date2;
	}
	public void setDate2(String date2) {
		this.date2 = date2;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getParentAgentName() {
		return parentAgentName;
	}
	public void setParentAgentName(String parentAgentName) {
		this.parentAgentName = parentAgentName;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

    
  
}
