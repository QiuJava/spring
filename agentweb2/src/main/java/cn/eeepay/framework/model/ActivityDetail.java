package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 活动明细
 * 
 * @author Administrator
 *
 */
public class ActivityDetail {
	private String activityName;
	private String activityTypeName;

	private Integer id;
	private String activityCode;// 活动类型,活动代码
	private Integer activityId;
	private String merchantNo;
	private String agentNode;
	private Date cashTime;
	private Date createTime;

	private String minBillingTime;// 入账时间
	private String maxBillingTime;// 入账时间
	private Date billingTime;// 入账时间
	private Integer billingStatus;// 入账状态
	private Integer status;// 活动状态
	private String cashOrder;
	private String activeOrder;// 激活流水号
	private Date activeTime;// 激活时间
	private Date enterTime;// 进件时间
	private BigDecimal frozenAmout;
	private BigDecimal transTotal;// 交易金额
	private BigDecimal targetAmout;
	private String agentNo;
	private String agentName;
	private String merchantName;
	private String agentN;
	private String merchantN;
	private String mobilephone;//商户手机号

	private Integer checkStatus;// 核算状态：1：同意，2：不同意，3：未核算
	private Integer discountStatus;// 扣回状态：0：未扣回，1：已扣回
	private String activeTimeStart;
	private String activeTimeEnd;
	private String acqEnname;// 收单机构英文名称
	private BigDecimal merchantFee;// 商户交易手续费
	private String settleTransferId;// 出款明细ID
	private BigDecimal merchantFeeAmount;// 商户提现费
	private BigDecimal merchantOutAmount;// 商户到账金额
	private Date merchantSettleDate;// 商户体现时间
	private String oneAgentNo;// 一级代理商
	private String oneAgentName;// 一级代理商名称
	private String cashBackAmount;// 欢乐返返现金额
	private String checkStatusStr;
	private String statusStr;

	private String parentNo;
	private String parentName;

	private String liquidationStatus;// 清算核算状态
	private String liquidationOperator;// 清算核算操作人
	private Date liquidationTime;// 清算操作时间
	private String liquidationTimeStart;
	private String liquidationTimeEnd;
	private String accountCheckStatus;// 账务核算状态
	private String accountCheckOperator;// 账务核算操作人
	private Date accountCheckTime;// 账务操作时间
	private String merchantType;// 商户类型 "1" 直营商户, "2" 所有代理商商户 "3" 所有商户

	private String cumulateTransAmount; // 累计交易金额（欢乐返）
	private Date endCumulateTime; // 截止累计日期（欢乐返）
	private String cumulateAmountMinus; // 累计交易（扣）（欢乐返）
	private String cumulateAmountAdd; // 累计交易（奖）（欢乐返）
	private String emptyAmount; // 未满扣N元（欢乐返）
	private String emptyAmountJoin; // 未满扣N元（欢乐返）
	public String getEmptyAmountJoin() {
		return emptyAmountJoin;
	}

	public void setEmptyAmountJoin(String emptyAmountJoin) {
		this.emptyAmountJoin = emptyAmountJoin;
	}

	public String getFullAmountJoin() {
		return fullAmountJoin;
	}

	public void setFullAmountJoin(String fullAmountJoin) {
		this.fullAmountJoin = fullAmountJoin;
	}

	private String fullAmount; // 满奖M元（欢乐返）
	private String fullAmountJoin; // 满奖M元（欢乐返）
	private String isStandard; // 奖励是否达标,0:未达标, 1:已达标（欢乐返）
	private Date standardTime; // 奖励达标时间（欢乐返）
	private Date minusAmountTime; // 扣款时间（欢乐返）
	private Date addAmountTime; // 奖励时间（欢乐返)
	private Date overdueTime;
	private Date minOverdueTime;

	private String minCumulateTransAmount;
	private String maxCumulateTransAmount;
	private String minStandardTime;
	private String maxStandardTime;
	private String minMinusAmountTime;
	private String maxMinusAmountTime;
	private String minAddAmountTime;
	private String maxAddAmountTime;

	private String activityTypeNo;// 欢乐返字类型编号
	private String currentAgentNo;// 当前登录代理商编号
	private String cashBackSwitch;// 返现开关 1-打开, 0-关闭'
	private String entryStatus;// 返现入账状态
	private String directAgentNo;// 直属下级代理商编号
	private String directAgentName;// 直属下级代理商名称
	private String directCashBackSwitch;// 直属下级返现开关状态
	private String directCashBackAmount;// 直属下级返现金额
	private String directEntryStatus;// 直属下级返现入账状态
	private Integer repeatRegister;//是否重复注册 1是0否

	private String merTeamId;//商户组织
	private String merGroup;//商户组织名称
	private Integer agentLevel;
	private String agentType;
	
	private String teamEntryId;

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public String getDirectAgentNo() {
		return directAgentNo;
	}

	public void setDirectAgentNo(String directAgentNo) {
		this.directAgentNo = directAgentNo;
	}

	public String getDirectAgentName() {
		return directAgentName;
	}

	public void setDirectAgentName(String directAgentName) {
		this.directAgentName = directAgentName;
	}

	public String getDirectCashBackSwitch() {
		return directCashBackSwitch;
	}

	public void setDirectCashBackSwitch(String directCashBackSwitch) {
		this.directCashBackSwitch = directCashBackSwitch;
	}

	public String getDirectCashBackAmount() {
		return directCashBackAmount;
	}

	public void setDirectCashBackAmount(String directCashBackAmount) {
		this.directCashBackAmount = directCashBackAmount;
	}

	public String getDirectEntryStatus() {
		return directEntryStatus;
	}

	public void setDirectEntryStatus(String directEntryStatus) {
		this.directEntryStatus = directEntryStatus;
	}

	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public String getCurrentAgentNo() {
		return currentAgentNo;
	}

	public void setCurrentAgentNo(String currentAgentNo) {
		this.currentAgentNo = currentAgentNo;
	}

	public String getCashBackSwitch() {
		return cashBackSwitch;
	}

	public void setCashBackSwitch(String cashBackSwitch) {
		this.cashBackSwitch = cashBackSwitch;
	}

	public String getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(String merchantType) {
		this.merchantType = merchantType;
	}

	public String getLiquidationStatus() {
		return liquidationStatus;
	}

	public void setLiquidationStatus(String liquidationStatus) {
		this.liquidationStatus = liquidationStatus;
	}

	public String getLiquidationOperator() {
		return liquidationOperator;
	}

	public void setLiquidationOperator(String liquidationOperator) {
		this.liquidationOperator = liquidationOperator;
	}

	public Date getLiquidationTime() {
		return liquidationTime;
	}

	public void setLiquidationTime(Date liquidationTime) {
		this.liquidationTime = liquidationTime;
	}

	public String getAccountCheckStatus() {
		return accountCheckStatus;
	}

	public void setAccountCheckStatus(String accountCheckStatus) {
		this.accountCheckStatus = accountCheckStatus;
	}

	public String getAccountCheckOperator() {
		return accountCheckOperator;
	}

	public void setAccountCheckOperator(String accountCheckOperator) {
		this.accountCheckOperator = accountCheckOperator;
	}

	public Date getAccountCheckTime() {
		return accountCheckTime;
	}

	public void setAccountCheckTime(Date accountCheckTime) {
		this.accountCheckTime = accountCheckTime;
	}

	public String getParentNo() {
		return parentNo;
	}

	public void setParentNo(String parentNo) {
		this.parentNo = parentNo;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getAgentN() {
		return agentN;
	}

	public void setAgentN(String agentN) {
		this.agentN = agentN;
	}

	public String getMerchantN() {
		return merchantN;
	}

	public void setMerchantN(String merchantN) {
		this.merchantN = merchantN;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public Date getCashTime() {
		return cashTime;
	}

	public void setCashTime(Date cashTime) {
		this.cashTime = cashTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCashOrder() {
		return cashOrder;
	}

	public void setCashOrder(String cashOrder) {
		this.cashOrder = cashOrder;
	}

	public String getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(String activeOrder) {
		this.activeOrder = activeOrder;
	}

	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	public Date getEnterTime() {
		return enterTime;
	}

	public void setEnterTime(Date enterTime) {
		this.enterTime = enterTime;
	}

	public BigDecimal getFrozenAmout() {
		return frozenAmout;
	}

	public void setFrozenAmout(BigDecimal frozenAmout) {
		this.frozenAmout = frozenAmout;
	}

	public BigDecimal getTransTotal() {
		return transTotal;
	}

	public void setTransTotal(BigDecimal transTotal) {
		this.transTotal = transTotal;
	}

	public BigDecimal getTargetAmout() {
		return targetAmout;
	}

	public void setTargetAmout(BigDecimal targetAmout) {
		this.targetAmout = targetAmout;
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

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getDiscountStatus() {
		return discountStatus;
	}

	public void setDiscountStatus(Integer discountStatus) {
		this.discountStatus = discountStatus;
	}

	public String getActiveTimeStart() {
		return activeTimeStart;
	}

	public ActivityDetail setActiveTimeStart(String activeTimeStart) {
		this.activeTimeStart = activeTimeStart;
		return this;
	}

	public String getActiveTimeEnd() {
		return activeTimeEnd;
	}

	public ActivityDetail setActiveTimeEnd(String activeTimeEnd) {
		this.activeTimeEnd = activeTimeEnd;
		return this;
	}

	public String getLiquidationTimeStart() {
		return liquidationTimeStart;
	}

	public ActivityDetail setLiquidationTimeStart(String liquidationTimeStart) {
		this.liquidationTimeStart = liquidationTimeStart;
		return this;
	}

	public String getLiquidationTimeEnd() {
		return liquidationTimeEnd;
	}

	public ActivityDetail setLiquidationTimeEnd(String liquidationTimeEnd) {
		this.liquidationTimeEnd = liquidationTimeEnd;
		return this;
	}

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	public BigDecimal getMerchantFee() {
		return merchantFee;
	}

	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}

	public BigDecimal getMerchantFeeAmount() {
		return merchantFeeAmount;
	}

	public void setMerchantFeeAmount(BigDecimal merchantFeeAmount) {
		this.merchantFeeAmount = merchantFeeAmount;
	}

	public BigDecimal getMerchantOutAmount() {
		return merchantOutAmount;
	}

	public void setMerchantOutAmount(BigDecimal merchantOutAmount) {
		this.merchantOutAmount = merchantOutAmount;
	}

	public Date getMerchantSettleDate() {
		return merchantSettleDate;
	}

	public void setMerchantSettleDate(Date merchantSettleDate) {
		this.merchantSettleDate = merchantSettleDate;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getCheckStatusStr() {
		return checkStatusStr;
	}

	public void setCheckStatusStr(String checkStatusStr) {
		this.checkStatusStr = checkStatusStr;
	}

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getSettleTransferId() {
		return settleTransferId;
	}

	public void setSettleTransferId(String settleTransferId) {
		this.settleTransferId = settleTransferId;
	}

	public String getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(String cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getCumulateTransAmount() {
		return cumulateTransAmount;
	}

	public ActivityDetail setCumulateTransAmount(String cumulateTransAmount) {
		this.cumulateTransAmount = cumulateTransAmount;
		return this;
	}

	public String getCumulateAmountMinus() {
		return cumulateAmountMinus;
	}

	public ActivityDetail setCumulateAmountMinus(String cumulateAmountMinus) {
		this.cumulateAmountMinus = cumulateAmountMinus;
		return this;
	}

	public String getCumulateAmountAdd() {
		return cumulateAmountAdd;
	}

	public ActivityDetail setCumulateAmountAdd(String cumulateAmountAdd) {
		this.cumulateAmountAdd = cumulateAmountAdd;
		return this;
	}

	public String getEmptyAmount() {
		return emptyAmount;
	}

	public ActivityDetail setEmptyAmount(String emptyAmount) {
		this.emptyAmount = emptyAmount;
		return this;
	}

	public String getFullAmount() {
		return fullAmount;
	}

	public ActivityDetail setFullAmount(String fullAmount) {
		this.fullAmount = fullAmount;
		return this;
	}

	public String getIsStandard() {
		return isStandard;
	}

	public ActivityDetail setIsStandard(String isStandard) {
		this.isStandard = isStandard;
		return this;
	}

	public String getMinCumulateTransAmount() {
		return minCumulateTransAmount;
	}

	public ActivityDetail setMinCumulateTransAmount(String minCumulateTransAmount) {
		this.minCumulateTransAmount = minCumulateTransAmount;
		return this;
	}

	public Date getEndCumulateTime() {
		return endCumulateTime;
	}

	public ActivityDetail setEndCumulateTime(Date endCumulateTime) {
		this.endCumulateTime = endCumulateTime;
		return this;
	}

	public Date getStandardTime() {
		return standardTime;
	}

	public ActivityDetail setStandardTime(Date standardTime) {
		this.standardTime = standardTime;
		return this;
	}

	public Date getMinusAmountTime() {
		return minusAmountTime;
	}

	public ActivityDetail setMinusAmountTime(Date minusAmountTime) {
		this.minusAmountTime = minusAmountTime;
		return this;
	}

	public Date getAddAmountTime() {
		return addAmountTime;
	}

	public ActivityDetail setAddAmountTime(Date addAmountTime) {
		this.addAmountTime = addAmountTime;
		return this;
	}

	public String getMaxCumulateTransAmount() {
		return maxCumulateTransAmount;
	}

	public ActivityDetail setMaxCumulateTransAmount(String maxCumulateTransAmount) {
		this.maxCumulateTransAmount = maxCumulateTransAmount;
		return this;
	}

	public String getMinStandardTime() {
		return minStandardTime;
	}

	public ActivityDetail setMinStandardTime(String minStandardTime) {
		this.minStandardTime = minStandardTime;
		return this;
	}

	public String getMaxStandardTime() {
		return maxStandardTime;
	}

	public ActivityDetail setMaxStandardTime(String maxStandardTime) {
		this.maxStandardTime = maxStandardTime;
		return this;
	}

	public String getMinMinusAmountTime() {
		return minMinusAmountTime;
	}

	public ActivityDetail setMinMinusAmountTime(String minMinusAmountTime) {
		this.minMinusAmountTime = minMinusAmountTime;
		return this;
	}

	public String getMaxMinusAmountTime() {
		return maxMinusAmountTime;
	}

	public ActivityDetail setMaxMinusAmountTime(String maxMinusAmountTime) {
		this.maxMinusAmountTime = maxMinusAmountTime;
		return this;
	}

	public String getMinAddAmountTime() {
		return minAddAmountTime;
	}

	public ActivityDetail setMinAddAmountTime(String minAddAmountTime) {
		this.minAddAmountTime = minAddAmountTime;
		return this;
	}

	public String getMaxAddAmountTime() {
		return maxAddAmountTime;
	}

	public ActivityDetail setMaxAddAmountTime(String maxAddAmountTime) {
		this.maxAddAmountTime = maxAddAmountTime;
		return this;
	}

	public Date getOverdueTime() {
		return overdueTime;
	}

	public ActivityDetail setOverdueTime(Date overdueTime) {
		this.overdueTime = overdueTime;
		return this;
	}

	public Integer getRepeatRegister() {
		return repeatRegister;
	}

	public void setRepeatRegister(Integer repeatRegister) {
		this.repeatRegister = repeatRegister;
	}

	public Date getMinOverdueTime() {
		return minOverdueTime;
	}

	public void setMinOverdueTime(Date minOverdueTime) {
		this.minOverdueTime = minOverdueTime;
	}

	public String getMerTeamId() {
		return merTeamId;
	}

	public void setMerTeamId(String merTeamId) {
		this.merTeamId = merTeamId;
	}

	public String getMerGroup() {
		return merGroup;
	}

	public void setMerGroup(String merGroup) {
		this.merGroup = merGroup;
	}

	public Date getBillingTime() {
		return billingTime;
	}

	public void setBillingTime(Date billingTime) {
		this.billingTime = billingTime;
	}

	public Integer getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(Integer billingStatus) {
		this.billingStatus = billingStatus;
	}

	public String getMinBillingTime() {
		return minBillingTime;
	}

	public void setMinBillingTime(String minBillingTime) {
		this.minBillingTime = minBillingTime;
	}

	public String getMaxBillingTime() {
		return maxBillingTime;
	}

	public void setMaxBillingTime(String maxBillingTime) {
		this.maxBillingTime = maxBillingTime;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

}
