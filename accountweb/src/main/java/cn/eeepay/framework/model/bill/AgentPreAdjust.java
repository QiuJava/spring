package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商预调账表
 * @author zouruijin
 * 2017年4月15日11:58:47
 *
 */
public class AgentPreAdjust implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private Date adjustTime;		
	private String applicant;		
	private String agentNo;		
	private String agentName;		
	private BigDecimal openBackAmount;
	private BigDecimal rateDiffAmount;
	private BigDecimal tuiCostAmount;
	private BigDecimal riskSubAmount;
	private BigDecimal bailSubAmount;
	private BigDecimal merMgAmount;
	private BigDecimal otherAmount;
	private BigDecimal terminalFreezeAmount;
	private BigDecimal otherFreezeAmount;
	private BigDecimal adjustAmount;
	private String adjustReason;
	
	private String remark;

	private BigDecimal activityAvailableAmount;
	private BigDecimal activityFreezeAmount;
	private BigDecimal generateAmount;

	//vo
	private String adjustTime1;
	private String adjustTime2;

	private int rowflag;

	public int getRowflag() {
		return rowflag;
	}

	public void setRowflag(int rowflag) {
		this.rowflag = rowflag;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getAdjustTime() {
		return adjustTime;
	}
	public void setAdjustTime(Date adjustTime) {
		this.adjustTime = adjustTime;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
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
	public BigDecimal getOpenBackAmount() {
		return openBackAmount;
	}
	
	public BigDecimal getTuiCostAmount() {
		return tuiCostAmount;
	}
	public void setTuiCostAmount(BigDecimal tuiCostAmount) {
		this.tuiCostAmount = tuiCostAmount;
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
	public BigDecimal getRiskSubAmount() {
		return riskSubAmount;
	}
	public void setRiskSubAmount(BigDecimal riskSubAmount) {
		this.riskSubAmount = riskSubAmount;
	}
	public BigDecimal getBailSubAmount() {
		return bailSubAmount;
	}
	public void setBailSubAmount(BigDecimal bailSubAmount) {
		this.bailSubAmount = bailSubAmount;
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
	public String getAdjustReason() {
		return adjustReason;
	}
	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}
	public BigDecimal getAdjustAmount() {
		return adjustAmount;
	}
	public void setAdjustAmount(BigDecimal adjustAmount) {
		this.adjustAmount = adjustAmount;
	}
	public String getAdjustTime1() {
		return adjustTime1;
	}
	public void setAdjustTime1(String adjustTime1) {
		this.adjustTime1 = adjustTime1;
	}
	public String getAdjustTime2() {
		return adjustTime2;
	}
	public void setAdjustTime2(String adjustTime2) {
		this.adjustTime2 = adjustTime2;
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


	public BigDecimal getActivityAvailableAmount() {
		return activityAvailableAmount;
	}

	public void setActivityAvailableAmount(BigDecimal activityAvailableAmount) {
		this.activityAvailableAmount = activityAvailableAmount;
	}

	public BigDecimal getActivityFreezeAmount() {
		return activityFreezeAmount;
	}

	public void setActivityFreezeAmount(BigDecimal activityFreezeAmount) {
		this.activityFreezeAmount = activityFreezeAmount;
	}

	public BigDecimal getGenerateAmount() {
		return generateAmount;
	}

	public void setGenerateAmount(BigDecimal generateAmount) {
		this.generateAmount = generateAmount;
	}
}

