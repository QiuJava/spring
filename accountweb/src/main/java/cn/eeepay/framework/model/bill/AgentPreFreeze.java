package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商预冻结表
 * @author zouruijin
 * 2017年4月15日11:58:47
 *
 */
public class AgentPreFreeze implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String agentNo;		
	private String agentName;		
	private Date freezeTime;
	private String operater;
	private BigDecimal terminalFreezeAmount;
	private BigDecimal otherFreezeAmount;
	private BigDecimal fenFreezeAmount;
	private BigDecimal activityFreezeAmount;
	private BigDecimal freezeAmount;
	private String freezeReason;
	private String remark;

	//vo
	private String freezeTime1;
	private String freezeTime2;
	private String preFreezeAmount;
	private String controlAmount;
	private String controlAmount2;



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
	public Date getFreezeTime() {
		return freezeTime;
	}
	public void setFreezeTime(Date freezeTime) {
		this.freezeTime = freezeTime;
	}
	public String getOperater() {
		return operater;
	}
	public void setOperater(String operater) {
		this.operater = operater;
	}
	public String getFreezeReason() {
		return freezeReason;
	}
	public void setFreezeReason(String freezeReason) {
		this.freezeReason = freezeReason;
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
	public String getFreezeTime1() {
		return freezeTime1;
	}
	public void setFreezeTime1(String freezeTime1) {
		this.freezeTime1 = freezeTime1;
	}
	public String getFreezeTime2() {
		return freezeTime2;
	}
	public void setFreezeTime2(String freezeTime2) {
		this.freezeTime2 = freezeTime2;
	}

	public String getPreFreezeAmount() {
		return preFreezeAmount;
	}

	public void setPreFreezeAmount(String preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
	}

	public String getControlAmount() {
		return controlAmount;
	}

	public void setControlAmount(String controlAmount) {
		this.controlAmount = controlAmount;
	}

	public String getControlAmount2() {
		return controlAmount2;
	}

	public void setControlAmount2(String controlAmount2) {
		this.controlAmount2 = controlAmount2;
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

	public BigDecimal getFreezeAmount() {
		return freezeAmount;
	}

	public void setFreezeAmount(BigDecimal freezeAmount) {
		this.freezeAmount = freezeAmount;
	}
}
