package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class HappyBackNotFullDeductDetailList {
	private BigDecimal adjustAmount;
	private BigDecimal debtAmount;
	private BigDecimal shouldDebtAmount;

	private String orderNo;
	private String agentNo;
	private String agentName;
	
	private Date debtTime;

	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}

	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}

	public BigDecimal getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
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

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Date getDebtTime() {
		return debtTime;
	}

	public void setDebtTime(Date debtTime) {
		this.debtTime = debtTime;
	}

}
