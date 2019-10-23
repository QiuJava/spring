package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CollectiveTransOrder {
	// 活动类型
	private String activityType;

	private String recommendedSource;

	private Integer id;

	private String agentLevel;

	private String orderNo;

	private String mobileNo;

	private String merchantNo;

	private BigDecimal transAmount;

	private String payMethod;

	private String transStatus;

	private Date transTime;

	private Date createTime;

	private String accountNo;

	private String cardType;

	private String holidaysMark;

	private Integer businessProductId;

	private Integer serviceId;

	private Integer acqOrgId;

	private String acqName;

	private Integer acqServiceId;

	private BigDecimal merchantFee;

	private String merchantRate;

	private String merchantRateType;

	private BigDecimal acqMerchantFee;

	private String acqMerchantRate;

	private String acqRateType;

	private String agentNode;

	private String settlementMethod;

	private Integer hardwareProduct;

	private BigDecimal profits1;

	private BigDecimal profits2;

	private BigDecimal profits3;

	private BigDecimal profits4;

	private BigDecimal profits5;

	private BigDecimal profits6;

	private BigDecimal profits7;

	private BigDecimal profits8;

	private BigDecimal profits9;

	private BigDecimal profits10;

	private BigDecimal profits11;

	private BigDecimal profits12;

	private BigDecimal profits13;

	private BigDecimal profits14;

	private BigDecimal profits15;

	private BigDecimal profits16;

	private BigDecimal profits17;

	private BigDecimal profits18;

	private BigDecimal profits19;

	private BigDecimal profits20;

	private String num;
	private String num1;
	private String num2;

	private String freezeStatus;
	private String typeName;
	private String agentName;
	private String oneAgentNo;
	private String bool;
	private String initAgentNo;
	private String acqMerchantName;
	private String bpName;
	private String serviceName;
	private String sdate;
	private String edate;
	private String merchantName;
	private String totalNum;
	private String totalMoney;
	private String agentNo;
	private String mobilephone;
	private String serviceType;

	private String terType;
	private String transStatus1;
	private String transType1;
	private String payMethod1;
	private String cardType1;
	private String settleMsg;
	private String settleStatus;
	private String settleStatus1;
	private String terminalNo;
	private Date merchantSettleDate;
	private String currencyType;
	private String settleErrCode;

	private String productType;
	private String parentAgentNo;
	private String parentAgentName;
	private String directlyAgentNo; // 直属下级代理商编号
	private String directlyAgentName;// 直属下级代理商名称

	// 商户创建时间
	private String startCreateTime;
	private String endCreateTime;
	private Date ct;

	// 交易金额
	private BigDecimal minTransAmount;
	private BigDecimal maxTransAmount;
	private String orderType;
	private String deductionFee;
	private String sn;
	private String belongUserCode;// 所属盟主编号
	
	private String merTeamId;
	private String merGroup;
	
	private String teamEntryId;// 商户子组织
	
	public String getMerGroup() {
		return merGroup;
	}

	public void setMerGroup(String merGroup) {
		this.merGroup = merGroup;
	}

	public String getMerTeamId() {
		return merTeamId;
	}

	public void setMerTeamId(String merTeamId) {
		this.merTeamId = merTeamId;
	}

	public String getBelongUserCode() {
		return belongUserCode;
	}

	public void setBelongUserCode(String belongUserCode) {
		this.belongUserCode = belongUserCode;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getDeductionFee() {
		return deductionFee;
	}

	public void setDeductionFee(String deductionFee) {
		this.deductionFee = deductionFee;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public Date getCt() {
		return ct;
	}

	public void setCt(Date ct) {
		this.ct = ct;
	}

	public BigDecimal getMinTransAmount() {
		return minTransAmount;
	}

	public void setMinTransAmount(BigDecimal minTransAmount) {
		this.minTransAmount = minTransAmount;
	}

	public BigDecimal getMaxTransAmount() {
		return maxTransAmount;
	}

	public void setMaxTransAmount(BigDecimal maxTransAmount) {
		this.maxTransAmount = maxTransAmount;
	}

	public String getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public String getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public String getDirectlyAgentNo() {
		return directlyAgentNo;
	}

	public void setDirectlyAgentNo(String directlyAgentNo) {
		this.directlyAgentNo = directlyAgentNo;
	}

	public String getDirectlyAgentName() {
		return directlyAgentName;
	}

	public void setDirectlyAgentName(String directlyAgentName) {
		this.directlyAgentName = directlyAgentName;
	}

	private String tisId;
	private String signImg;
	private String batchNo;
	private String acqSerialNo;
	private String acqReferenceNo;
	private String acqAuthNo;
	private String acqTerminalNo;

	public String getAcqTerminalNo() {
		return acqTerminalNo;
	}

	public void setAcqTerminalNo(String acqTerminalNo) {
		this.acqTerminalNo = acqTerminalNo;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getAcqSerialNo() {
		return acqSerialNo;
	}

	public void setAcqSerialNo(String acqSerialNo) {
		this.acqSerialNo = acqSerialNo;
	}

	public String getAcqReferenceNo() {
		return acqReferenceNo;
	}

	public void setAcqReferenceNo(String acqReferenceNo) {
		this.acqReferenceNo = acqReferenceNo;
	}

	public String getAcqAuthNo() {
		return acqAuthNo;
	}

	public void setAcqAuthNo(String acqAuthNo) {
		this.acqAuthNo = acqAuthNo;
	}

	public String getSignImg() {
		return signImg;
	}

	public void setSignImg(String signImg) {
		this.signImg = signImg;
	}

	public String getTisId() {
		return tisId;
	}

	public void setTisId(String tisId) {
		this.tisId = tisId;
	}

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	public String getRecommendedSource() {
		return recommendedSource;
	}

	public void setRecommendedSource(String recommendedSource) {
		this.recommendedSource = recommendedSource;
	}

	public String getParentAgentNo() {
		return parentAgentNo;
	}

	public void setParentAgentNo(String parentAgentNo) {
		this.parentAgentNo = parentAgentNo;
	}

	public String getParentAgentName() {
		return parentAgentName;
	}

	public void setParentAgentName(String parentAgentName) {
		this.parentAgentName = parentAgentName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo == null ? null : mobileNo.trim();
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo == null ? null : merchantNo.trim();
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod == null ? null : payMethod.trim();
	}

	public String getTransStatus() {
		return transStatus;
	}

	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus == null ? null : transStatus.trim();
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo == null ? null : accountNo.trim();
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType == null ? null : cardType.trim();
	}

	public String getHolidaysMark() {
		return holidaysMark;
	}

	public void setHolidaysMark(String holidaysMark) {
		this.holidaysMark = holidaysMark == null ? null : holidaysMark.trim();
	}

	public Integer getBusinessProductId() {
		return businessProductId;
	}

	public void setBusinessProductId(Integer businessProductId) {
		this.businessProductId = businessProductId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getAcqOrgId() {
		return acqOrgId;
	}

	public void setAcqOrgId(Integer acqOrgId) {
		this.acqOrgId = acqOrgId;
	}

	public String getAcqName() {
		return acqName;
	}

	public void setAcqName(String acqName) {
		this.acqName = acqName == null ? null : acqName.trim();
	}

	public Integer getAcqServiceId() {
		return acqServiceId;
	}

	public void setAcqServiceId(Integer acqServiceId) {
		this.acqServiceId = acqServiceId;
	}

	public BigDecimal getMerchantFee() {
		return merchantFee;
	}

	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}

	public String getMerchantRate() {
		return merchantRate;
	}

	public void setMerchantRate(String merchantRate) {
		this.merchantRate = merchantRate == null ? null : merchantRate.trim();
	}

	public String getMerchantRateType() {
		return merchantRateType;
	}

	public void setMerchantRateType(String merchantRateType) {
		this.merchantRateType = merchantRateType == null ? null : merchantRateType.trim();
	}

	public BigDecimal getAcqMerchantFee() {
		return acqMerchantFee;
	}

	public void setAcqMerchantFee(BigDecimal acqMerchantFee) {
		this.acqMerchantFee = acqMerchantFee;
	}

	public String getAcqMerchantRate() {
		return acqMerchantRate;
	}

	public void setAcqMerchantRate(String acqMerchantRate) {
		this.acqMerchantRate = acqMerchantRate == null ? null : acqMerchantRate.trim();
	}

	public String getAcqRateType() {
		return acqRateType;
	}

	public void setAcqRateType(String acqRateType) {
		this.acqRateType = acqRateType == null ? null : acqRateType.trim();
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode == null ? null : agentNode.trim();
	}

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod == null ? null : settlementMethod.trim();
	}

	public Integer getHardwareProduct() {
		return hardwareProduct;
	}

	public void setHardwareProduct(Integer hardwareProduct) {
		this.hardwareProduct = hardwareProduct;
	}

	public BigDecimal getProfits1() {
		return profits1;
	}

	public void setProfits1(BigDecimal profits1) {
		this.profits1 = profits1;
	}

	public BigDecimal getProfits2() {
		return profits2;
	}

	public void setProfits2(BigDecimal profits2) {
		this.profits2 = profits2;
	}

	public BigDecimal getProfits3() {
		return profits3;
	}

	public void setProfits3(BigDecimal profits3) {
		this.profits3 = profits3;
	}

	public BigDecimal getProfits4() {
		return profits4;
	}

	public void setProfits4(BigDecimal profits4) {
		this.profits4 = profits4;
	}

	public BigDecimal getProfits5() {
		return profits5;
	}

	public void setProfits5(BigDecimal profits5) {
		this.profits5 = profits5;
	}

	public BigDecimal getProfits6() {
		return profits6;
	}

	public void setProfits6(BigDecimal profits6) {
		this.profits6 = profits6;
	}

	public BigDecimal getProfits7() {
		return profits7;
	}

	public void setProfits7(BigDecimal profits7) {
		this.profits7 = profits7;
	}

	public BigDecimal getProfits8() {
		return profits8;
	}

	public void setProfits8(BigDecimal profits8) {
		this.profits8 = profits8;
	}

	public BigDecimal getProfits9() {
		return profits9;
	}

	public void setProfits9(BigDecimal profits9) {
		this.profits9 = profits9;
	}

	public BigDecimal getProfits10() {
		return profits10;
	}

	public void setProfits10(BigDecimal profits10) {
		this.profits10 = profits10;
	}

	public BigDecimal getProfits11() {
		return profits11;
	}

	public void setProfits11(BigDecimal profits11) {
		this.profits11 = profits11;
	}

	public BigDecimal getProfits12() {
		return profits12;
	}

	public void setProfits12(BigDecimal profits12) {
		this.profits12 = profits12;
	}

	public BigDecimal getProfits13() {
		return profits13;
	}

	public void setProfits13(BigDecimal profits13) {
		this.profits13 = profits13;
	}

	public BigDecimal getProfits14() {
		return profits14;
	}

	public void setProfits14(BigDecimal profits14) {
		this.profits14 = profits14;
	}

	public BigDecimal getProfits15() {
		return profits15;
	}

	public void setProfits15(BigDecimal profits15) {
		this.profits15 = profits15;
	}

	public BigDecimal getProfits16() {
		return profits16;
	}

	public void setProfits16(BigDecimal profits16) {
		this.profits16 = profits16;
	}

	public BigDecimal getProfits17() {
		return profits17;
	}

	public void setProfits17(BigDecimal profits17) {
		this.profits17 = profits17;
	}

	public BigDecimal getProfits18() {
		return profits18;
	}

	public void setProfits18(BigDecimal profits18) {
		this.profits18 = profits18;
	}

	public BigDecimal getProfits19() {
		return profits19;
	}

	public void setProfits19(BigDecimal profits19) {
		this.profits19 = profits19;
	}

	public BigDecimal getProfits20() {
		return profits20;
	}

	public void setProfits20(BigDecimal profits20) {
		this.profits20 = profits20;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getBool() {
		return bool;
	}

	public void setBool(String bool) {
		this.bool = bool;
	}

	public String getInitAgentNo() {
		return initAgentNo;
	}

	public void setInitAgentNo(String initAgentNo) {
		this.initAgentNo = initAgentNo;
	}

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getTerType() {
		return terType;
	}

	public void setTerType(String terType) {
		this.terType = terType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getNum1() {
		return num1;
	}

	public void setNum1(String num1) {
		this.num1 = num1;
	}

	public String getNum2() {
		return num2;
	}

	public void setNum2(String num2) {
		this.num2 = num2;
	}

	public String getTransStatus1() {
		return transStatus1;
	}

	public void setTransStatus1(String transStatus1) {
		this.transStatus1 = transStatus1;
	}

	public String getCardType1() {
		return cardType1;
	}

	public void setCardType1(String cardType1) {
		this.cardType1 = cardType1;
	}

	public String getSettleMsg() {
		return settleMsg;
	}

	public void setSettleMsg(String settleMsg) {
		this.settleMsg = settleMsg;
	}

	public String getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public Date getMerchantSettleDate() {
		return merchantSettleDate;
	}

	public void setMerchantSettleDate(Date merchantSettleDate) {
		this.merchantSettleDate = merchantSettleDate;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getSettleErrCode() {
		return settleErrCode;
	}

	public void setSettleErrCode(String settleErrCode) {
		this.settleErrCode = settleErrCode;
	}

	public String getPayMethod1() {
		return payMethod1;
	}

	public void setPayMethod1(String payMethod1) {
		this.payMethod1 = payMethod1;
	}

	public String getTransType1() {
		return transType1;
	}

	public void setTransType1(String transType1) {
		this.transType1 = transType1;
	}

	public String getSettleStatus1() {
		return settleStatus1;
	}

	public void setSettleStatus1(String settleStatus1) {
		this.settleStatus1 = settleStatus1;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}

}