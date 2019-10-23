package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商解冻表
 * @author zouruijin
 * 2017年5月26日14:56:06
 *
 */
public class AgentUnfreeze implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String agentNo;		
	private String agentName;		
	private Date unfreezeTime;
	private String operater;
	private BigDecimal amount;
	private BigDecimal terminalFreezeAmount;
	private BigDecimal otherFreezeAmount;
	private BigDecimal fenFreezeAmount;
	private BigDecimal activityFreezeAmount;

	private String remark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public Date getUnfreezeTime() {
		return unfreezeTime;
	}
	public void setUnfreezeTime(Date unfreezeTime) {
		this.unfreezeTime = unfreezeTime;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getTerminalFreezeAmount() {
		return terminalFreezeAmount;
	}

	public void setTerminalFreezeAmount(BigDecimal terminalFreezeAmount) {
		this.terminalFreezeAmount = terminalFreezeAmount;
	}

	public BigDecimal getOtherFreezeAmount() {
		return otherFreezeAmount;
	}

	public void setOtherFreezeAmount(BigDecimal otherFreezeAmount) {
		this.otherFreezeAmount = otherFreezeAmount;
	}

	public BigDecimal getFenFreezeAmount() {
		return fenFreezeAmount;
	}

	public void setFenFreezeAmount(BigDecimal fenFreezeAmount) {
		this.fenFreezeAmount = fenFreezeAmount;
	}

	public BigDecimal getActivityFreezeAmount() {
		return activityFreezeAmount;
	}

	public void setActivityFreezeAmount(BigDecimal activityFreezeAmount) {
		this.activityFreezeAmount = activityFreezeAmount;
	}
}
