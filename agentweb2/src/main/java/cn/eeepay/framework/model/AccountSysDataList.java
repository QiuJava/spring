package cn.eeepay.framework.model;

import java.math.BigDecimal;

public class AccountSysDataList {
	private Long id;
	private String agentLevel;
	private String agentNo;
	private String agentNode;
	private BigDecimal debtAmount;
	private String debtTime;
	private String parentAgentNo;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public BigDecimal getDebtAmount() {
		return debtAmount;
	}
	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
	}
	public String getDebtTime() {
		return debtTime;
	}
	public void setDebtTime(String debtTime) {
		this.debtTime = debtTime;
	}
	public String getParentAgentNo() {
		return parentAgentNo;
	}
	public void setParentAgentNo(String parentAgentNo) {
		this.parentAgentNo = parentAgentNo;
	}
	
	
}
