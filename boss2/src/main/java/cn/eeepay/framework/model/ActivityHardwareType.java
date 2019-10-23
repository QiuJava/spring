package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class ActivityHardwareType {

	private Integer id;
	private String activityTypeNo;
	private String agentNo;
	private String agentNode;
	private String activityTypeName;
	private String activityCode;
	private BigDecimal transAmount;
	private BigDecimal cashBackAmount;
	private String remark;
	private Date createTime;
	private Date updateTime;
	private BigDecimal repeatRegisterAmount;
	private BigDecimal repeatRegisterRatio;

	private BigDecimal cashLastAllyAmount;
	private BigDecimal cashLastTeamAmount;

	private String functionName;//活动名称
	private BigDecimal taxRate;//税额百分比
	private Integer updateAgentStatus;
	private Long hpId;
	private String typeName;

	private String countTradeScope;//欢乐返子类型交易统计类型范围,多类型以,分隔。值为数据字典key为PAY_METHOD_TYPE的值
	private Integer ruleId;
	private String ruleName;

	private BigDecimal emptyAmount;//未满扣N元（欢乐返）
	private BigDecimal fullAmount;//满奖M元（欢乐返）
	private BigDecimal repeatEmptyAmount;//重复注册不满扣N值（欢乐返）
	private BigDecimal repeatFullAmount;//重复注册满奖M值（欢乐返）

	public String getCountTradeScope() {
		return countTradeScope;
	}

	public void setCountTradeScope(String countTradeScope) {
		this.countTradeScope = countTradeScope;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public BigDecimal getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(BigDecimal cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
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

	public BigDecimal getCashLastAllyAmount() {
		return cashLastAllyAmount;
	}

	public void setCashLastAllyAmount(BigDecimal cashLastAllyAmount) {
		this.cashLastAllyAmount = cashLastAllyAmount;
	}

	public BigDecimal getCashLastTeamAmount() {
		return cashLastTeamAmount;
	}

	public void setCashLastTeamAmount(BigDecimal cashLastTeamAmount) {
		this.cashLastTeamAmount = cashLastTeamAmount;
	}

	public BigDecimal getRepeatRegisterAmount() {
		return repeatRegisterAmount;
	}

	public void setRepeatRegisterAmount(BigDecimal repeatRegisterAmount) {
		this.repeatRegisterAmount = repeatRegisterAmount;
	}

	public BigDecimal getRepeatRegisterRatio() {
		return repeatRegisterRatio;
	}

	public void setRepeatRegisterRatio(BigDecimal repeatRegisterRatio) {
		this.repeatRegisterRatio = repeatRegisterRatio;
	}

	public Integer getUpdateAgentStatus() {
		return updateAgentStatus;
	}

	public void setUpdateAgentStatus(Integer updateAgentStatus) {
		this.updateAgentStatus = updateAgentStatus;
	}

	public Long getHpId() {
		return hpId;
	}

	public void setHpId(Long hpId) {
		this.hpId = hpId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public BigDecimal getEmptyAmount() {
		return emptyAmount;
	}

	public void setEmptyAmount(BigDecimal emptyAmount) {
		this.emptyAmount = emptyAmount;
	}

	public BigDecimal getFullAmount() {
		return fullAmount;
	}

	public void setFullAmount(BigDecimal fullAmount) {
		this.fullAmount = fullAmount;
	}

	public BigDecimal getRepeatEmptyAmount() {
		return repeatEmptyAmount;
	}

	public void setRepeatEmptyAmount(BigDecimal repeatEmptyAmount) {
		this.repeatEmptyAmount = repeatEmptyAmount;
	}

	public BigDecimal getRepeatFullAmount() {
		return repeatFullAmount;
	}

	public void setRepeatFullAmount(BigDecimal repeatFullAmount) {
		this.repeatFullAmount = repeatFullAmount;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
}
