package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 代理商预记账累计表
 * @author zouruijin
 * 2017年4月15日11:58:47
 *
 */
public class AgentPreRecordTotal implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String agentNo;		
	private String agentName;		
	private BigDecimal openBackAmount;
	private BigDecimal rateDiffAmount;
	private BigDecimal tuiCostAmount;
	private BigDecimal riskSubAmount;
	private BigDecimal merMgAmount;
	private BigDecimal bailSubAmount;
	private BigDecimal otherAmount;
	private BigDecimal terminalFreezeAmount;
	private BigDecimal otherFreezeAmount;

	private String subjectNo;
	private String accountNo;

	//vo
	private BigDecimal preFreezeAmount;//当前余额
	private BigDecimal currBalance;//当前余额
    private BigDecimal controlAmount;//控制金额
    private BigDecimal settlingAmount;//结算中金额
    private BigDecimal availBalance;//可用余额

	private BigDecimal activitySubsidyFreeze;		//活动补贴账户已冻结金额
	private BigDecimal activitySubsidyBalance;		//活动补贴账户余额
	private BigDecimal activitySubsidyAvailableBalance;		//活动补贴账户可用余额

	private String agentLevel;
	
    
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
	public BigDecimal getOpenBackAmount() {
		return openBackAmount;
	}
	public void setOpenBackAmount(BigDecimal openBackAmount) {
		this.openBackAmount = openBackAmount;
	}
	public BigDecimal getRateDiffAmount() {
		return rateDiffAmount;
	}
	public void setRateDiffAmount(BigDecimal rateDiffAmount) {
		this.rateDiffAmount = rateDiffAmount;
	}
	public BigDecimal getTuiCostAmount() {
		return tuiCostAmount;
	}
	public void setTuiCostAmount(BigDecimal tuiCostAmount) {
		this.tuiCostAmount = tuiCostAmount;
	}
	public BigDecimal getRiskSubAmount() {
		return riskSubAmount;
	}
	public void setRiskSubAmount(BigDecimal riskSubAmount) {
		this.riskSubAmount = riskSubAmount;
	}
	public BigDecimal getMerMgAmount() {
		return merMgAmount;
	}
	public void setMerMgAmount(BigDecimal merMgAmount) {
		this.merMgAmount = merMgAmount;
	}
	public BigDecimal getOtherAmount() {
		return otherAmount;
	}
	public void setOtherAmount(BigDecimal otherAmount) {
		this.otherAmount = otherAmount;
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
	public BigDecimal getBailSubAmount() {
		return bailSubAmount;
	}
	public void setBailSubAmount(BigDecimal bailSubAmount) {
		this.bailSubAmount = bailSubAmount;
	}
	public BigDecimal getCurrBalance() {
		return currBalance;
	}
	public void setCurrBalance(BigDecimal currBalance) {
		this.currBalance = currBalance;
	}
	public BigDecimal getControlAmount() {
		return controlAmount;
	}
	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
	}
	public BigDecimal getSettlingAmount() {
		return settlingAmount;
	}
	public void setSettlingAmount(BigDecimal settlingAmount) {
		this.settlingAmount = settlingAmount;
	}
	public BigDecimal getAvailBalance() {
		return availBalance;
	}
	public void setAvailBalance(BigDecimal availBalance) {
		this.availBalance = availBalance;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public BigDecimal getActivitySubsidyFreeze() {
		return activitySubsidyFreeze;
	}

	public void setActivitySubsidyFreeze(BigDecimal activitySubsidyFreeze) {
		this.activitySubsidyFreeze = activitySubsidyFreeze;
	}

	public BigDecimal getActivitySubsidyBalance() {
		return activitySubsidyBalance;
	}

	public void setActivitySubsidyBalance(BigDecimal activitySubsidyBalance) {
		this.activitySubsidyBalance = activitySubsidyBalance;
	}

	public BigDecimal getActivitySubsidyAvailableBalance() {
		return activitySubsidyAvailableBalance;
	}

	public void setActivitySubsidyAvailableBalance(BigDecimal activitySubsidyAvailableBalance) {
		this.activitySubsidyAvailableBalance = activitySubsidyAvailableBalance;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getPreFreezeAmount() {
		return preFreezeAmount;
	}

	public void setPreFreezeAmount(BigDecimal preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
	}
}
