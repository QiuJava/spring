package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代理商分润日结表
 * @author zouruijin
 * 2017年4月15日11:58:47
 *
 */
public class AgentShareDaySettle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private String collectionBatchNo;		
	private Date groupTime;		
	private Date transDate;		
	private String oneAgentNo;		
	private String oneAgentName;		
	private String agentNo;
	private String agentName;
	private String agentNode;
	private String agentLevel;
	private String parentAgentNo;
	private String saleName;		
	private BigDecimal transTotalAmount;		
	private Integer transTotalNum;		
	private BigDecimal duiSuccTransTotalAmount;		
	private Integer duiSuccTransTotalNum;
	private Integer cashTotalNum;
	private BigDecimal merFee;
	private BigDecimal merCashFee;
	private BigDecimal deductionFee;//抵扣商户提现手续费
	private BigDecimal acqOutCost;
	private BigDecimal acqOutProfit;
	private BigDecimal daiCost;
	private BigDecimal dianCost;
	private BigDecimal preTransShareAmount;
	private BigDecimal preTransCashAmount;
	private BigDecimal openBackAmount;
	private BigDecimal rateDiffAmount;
	private BigDecimal tuiCostAmount;
	private BigDecimal riskSubAmount;
	private BigDecimal bailSubAmount;
	private BigDecimal merMgAmount;
	private BigDecimal otherAmount;
	private BigDecimal adjustTransShareAmount;
	private BigDecimal adjustTransCashAmount;
	private BigDecimal adjustTotalShareAmount;
	private BigDecimal terminalFreezeAmount;
	private BigDecimal otherFreezeAmount;
	
	private String parentAgentName;
	private String enterAccountStatus;
	private String enterAccountMessage;
	private BigDecimal realEnterShareAmount;


	private Date enterAccountTime;
	private String operator;
	
	//vo
	private String transDate1;
	private String transDate2;
	private String groupTime1;
	private String groupTime2;

	
	private BigDecimal transDeductionFee; //抵扣交易商户手续费
	private BigDecimal actualFee; //实际交易商户手续费
	private BigDecimal merchantPrice; //自选商户手续费
	private BigDecimal deductionMerFee; //抵扣自选商户手续费
	private BigDecimal actualOptionalFee;//实际自选商户手续费
	
	public AgentShareDaySettle() {
	}


	public BigDecimal getDeductionFee() {
		return deductionFee;
	}

	public void setDeductionFee(BigDecimal deductionFee) {
		this.deductionFee = deductionFee;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCollectionBatchNo() {
		return collectionBatchNo;
	}
	public void setCollectionBatchNo(String collectionBatchNo) {
		this.collectionBatchNo = collectionBatchNo;
	}
	public Date getGroupTime() {
		return groupTime;
	}
	public void setGroupTime(Date groupTime) {
		this.groupTime = groupTime;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public String getOneAgentName() {
		return oneAgentName;
	}
	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	public BigDecimal getTransTotalAmount() {
		return transTotalAmount;
	}
	public void setTransTotalAmount(BigDecimal transTotalAmount) {
		this.transTotalAmount = transTotalAmount;
	}
	public Integer getTransTotalNum() {
		return transTotalNum;
	}
	public void setTransTotalNum(Integer transTotalNum) {
		this.transTotalNum = transTotalNum;
	}
	public BigDecimal getDuiSuccTransTotalAmount() {
		return duiSuccTransTotalAmount;
	}
	public void setDuiSuccTransTotalAmount(BigDecimal duiSuccTransTotalAmount) {
		this.duiSuccTransTotalAmount = duiSuccTransTotalAmount;
	}
	public Integer getDuiSuccTransTotalNum() {
		return duiSuccTransTotalNum;
	}
	public void setDuiSuccTransTotalNum(Integer duiSuccTransTotalNum) {
		this.duiSuccTransTotalNum = duiSuccTransTotalNum;
	}
	public Integer getCashTotalNum() {
		return cashTotalNum;
	}
	public void setCashTotalNum(Integer cashTotalNum) {
		this.cashTotalNum = cashTotalNum;
	}
	public BigDecimal getMerFee() {
		return merFee;
	}
	public void setMerFee(BigDecimal merFee) {
		this.merFee = merFee;
	}
	public BigDecimal getMerCashFee() {
		return merCashFee;
	}
	public void setMerCashFee(BigDecimal merCashFee) {
		this.merCashFee = merCashFee;
	}
	public BigDecimal getAcqOutCost() {
		return acqOutCost;
	}
	public void setAcqOutCost(BigDecimal acqOutCost) {
		this.acqOutCost = acqOutCost;
	}
	public BigDecimal getAcqOutProfit() {
		return acqOutProfit;
	}
	public void setAcqOutProfit(BigDecimal acqOutProfit) {
		this.acqOutProfit = acqOutProfit;
	}
	public BigDecimal getDaiCost() {
		return daiCost;
	}
	public void setDaiCost(BigDecimal daiCost) {
		this.daiCost = daiCost;
	}
	public BigDecimal getDianCost() {
		return dianCost;
	}
	public void setDianCost(BigDecimal dianCost) {
		this.dianCost = dianCost;
	}
	public BigDecimal getPreTransShareAmount() {
		return preTransShareAmount;
	}
	public void setPreTransShareAmount(BigDecimal preTransShareAmount) {
		this.preTransShareAmount = preTransShareAmount;
	}
	public BigDecimal getPreTransCashAmount() {
		return preTransCashAmount;
	}
	public void setPreTransCashAmount(BigDecimal preTransCashAmount) {
		this.preTransCashAmount = preTransCashAmount;
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
	public BigDecimal getAdjustTransShareAmount() {
		return adjustTransShareAmount;
	}
	public void setAdjustTransShareAmount(BigDecimal adjustTransShareAmount) {
		this.adjustTransShareAmount = adjustTransShareAmount;
	}
	public BigDecimal getAdjustTransCashAmount() {
		return adjustTransCashAmount;
	}
	public void setAdjustTransCashAmount(BigDecimal adjustTransCashAmount) {
		this.adjustTransCashAmount = adjustTransCashAmount;
	}
	public BigDecimal getAdjustTotalShareAmount() {
		return adjustTotalShareAmount;
	}
	public void setAdjustTotalShareAmount(BigDecimal adjustTotalShareAmount) {
		this.adjustTotalShareAmount = adjustTotalShareAmount;
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
	public String getEnterAccountStatus() {
		return enterAccountStatus;
	}
	public void setEnterAccountStatus(String enterAccountStatus) {
		this.enterAccountStatus = enterAccountStatus;
	}
	public String getTransDate1() {
		return transDate1;
	}
	public void setTransDate1(String transDate1) {
		this.transDate1 = transDate1;
	}
	public String getTransDate2() {
		return transDate2;
	}
	public void setTransDate2(String transDate2) {
		this.transDate2 = transDate2;
	}
	public String getGroupTime1() {
		return groupTime1;
	}
	public void setGroupTime1(String groupTime1) {
		this.groupTime1 = groupTime1;
	}
	public String getGroupTime2() {
		return groupTime2;
	}
	public void setGroupTime2(String groupTime2) {
		this.groupTime2 = groupTime2;
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
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getAgentLevel() {
		return agentLevel;
	}
	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}
	public BigDecimal getRealEnterShareAmount() {
		return realEnterShareAmount;
	}
	public void setRealEnterShareAmount(BigDecimal realEnterShareAmount) {
		this.realEnterShareAmount = realEnterShareAmount;
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
	public String getEnterAccountMessage() {
		return enterAccountMessage;
	}
	public void setEnterAccountMessage(String enterAccountMessage) {
		this.enterAccountMessage = enterAccountMessage;
	}
	public Date getEnterAccountTime() {
		return enterAccountTime;
	}

	public void setEnterAccountTime(Date enterAccountTime) {
		this.enterAccountTime = enterAccountTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}


	public BigDecimal getTransDeductionFee() {
		return transDeductionFee;
	}


	public void setTransDeductionFee(BigDecimal transDeductionFee) {
		this.transDeductionFee = transDeductionFee;
	}


	public BigDecimal getActualFee() {
		return actualFee;
	}


	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
	}


	public BigDecimal getMerchantPrice() {
		return merchantPrice;
	}


	public void setMerchantPrice(BigDecimal merchantPrice) {
		this.merchantPrice = merchantPrice;
	}


	public BigDecimal getDeductionMerFee() {
		return deductionMerFee;
	}


	public void setDeductionMerFee(BigDecimal deductionMerFee) {
		this.deductionMerFee = deductionMerFee;
	}


	public BigDecimal getActualOptionalFee() {
		return actualOptionalFee;
	}


	public void setActualOptionalFee(BigDecimal actualOptionalFee) {
		this.actualOptionalFee = actualOptionalFee;
	}
	
}
