package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易部分信息表
 * 只有交易的一部分数据
 * @author zouruijin
 * 2017年4月15日11:58:47
 *
 */
public class TransShortInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String plateOrderNo ;
	private Date transTime;		
	private Integer hardwareProduct;
	private Integer businessProductId;	
	private Integer serviceId;	
	private String cardType;	
	private String merchantNo;	
	private String merchantName;	
	private String oneAgentNo;	
	private String oneAgentName;
	private String agentNo;	
	private String agentName;	
	private String agentNode;
	private String agentLevel;	
	private String parentAgentNo;	
	private BigDecimal transAmount;		
	private String merchantRate;		
	private BigDecimal merchantFee;		
	private Integer acqOrgId;		
	private String acqEnname;		
	private BigDecimal acqOutCost;		
	private BigDecimal agentShareAmount;
	private BigDecimal merCashFee;
	private BigDecimal cashAgentShareAmount;
	private BigDecimal daiCost;
	private BigDecimal dianCost;

    private BigDecimal deductionFee;//抵扣商户提现手续费
    //	private String agentShareCollect;
	private String saleName;
	private BigDecimal acqOutProfit;
	private String hardwareProductName;
	private String businessProductName;
	private String serviceName;

	//vo
	private String checkAccountStatus;//对账状态
	private String transTime1;	
	private String transTime2;	
	private String collectionTime1;	
	private String collectionTime2;
	
	private String agentProfitCollectionStatus;
	private String collectionBatchNo;//汇总批次号
	private Date agentProfitGroupTime;

	private String plateMerchantRate;

	private String plateAcqMerchantRate;

	private BigDecimal oneAgentShareAmount;

	private BigDecimal oneCashAgentShareAmount;

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
	
	
    private BigDecimal settleProfits1;

    private BigDecimal settleProfits2;

    private BigDecimal settleProfits3;

    private BigDecimal settleProfits4;

    private BigDecimal settleProfits5;

    private BigDecimal settleProfits6;

    private BigDecimal settleProfits7;

    private BigDecimal settleProfits8;

    private BigDecimal settleProfits9;

    private BigDecimal settleProfits10;

    private BigDecimal settleProfits11;

    private BigDecimal settleProfits12;

    private BigDecimal settleProfits13;

    private BigDecimal settleProfits14;

    private BigDecimal settleProfits15;

    private BigDecimal settleProfits16;

    private BigDecimal settleProfits17;

    private BigDecimal settleProfits18;

    private BigDecimal settleProfits19;

    private BigDecimal settleProfits20;
    
    
	private BigDecimal transDeductionFee; //抵扣交易商户手续费
	private BigDecimal actualFee; //实际交易商户手续费
	private BigDecimal merchantPrice; //自选商户手续费
	private BigDecimal deductionMerFee; //抵扣自选商户手续费
	private BigDecimal actualOptionalFee;//实际自选商户手续费
    

    public BigDecimal getDeductionFee() {
        return deductionFee;
    }

    public void setDeductionFee(BigDecimal deductionFee) {
        this.deductionFee = deductionFee;
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
	public String getParentAgentNo() {
		return parentAgentNo;
	}
	public void setParentAgentNo(String parentAgentNo) {
		this.parentAgentNo = parentAgentNo;
	}
	public String getPlateOrderNo() {
		return plateOrderNo;
	}
	public void setPlateOrderNo(String plateOrderNo) {
		this.plateOrderNo = plateOrderNo;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public Integer getHardwareProduct() {
		return hardwareProduct;
	}
	public void setHardwareProduct(Integer hardwareProduct) {
		this.hardwareProduct = hardwareProduct;
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
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public String getMerchantRate() {
		return merchantRate;
	}
	public void setMerchantRate(String merchantRate) {
		this.merchantRate = merchantRate;
	}
	public BigDecimal getMerchantFee() {
		return merchantFee;
	}
	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}
	public Integer getAcqOrgId() {
		return acqOrgId;
	}
	public void setAcqOrgId(Integer acqOrgId) {
		this.acqOrgId = acqOrgId;
	}
	
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public BigDecimal getAcqOutCost() {
		return acqOutCost;
	}
	public void setAcqOutCost(BigDecimal acqOutCost) {
		this.acqOutCost = acqOutCost;
	}
	public BigDecimal getAgentShareAmount() {
		return agentShareAmount;
	}
	public void setAgentShareAmount(BigDecimal agentShareAmount) {
		this.agentShareAmount = agentShareAmount;
	}
	public BigDecimal getMerCashFee() {
		return merCashFee;
	}
	public void setMerCashFee(BigDecimal merCashFee) {
		this.merCashFee = merCashFee;
	}
	public BigDecimal getCashAgentShareAmount() {
		return cashAgentShareAmount;
	}
	public void setCashAgentShareAmount(BigDecimal cashAgentShareAmount) {
		this.cashAgentShareAmount = cashAgentShareAmount;
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
//	public String getAgentShareCollect() {
//		return agentShareCollect;
//	}
//	public void setAgentShareCollect(String agentShareCollect) {
//		this.agentShareCollect = agentShareCollect;
//	}
	public String getCheckAccountStatus() {
		return checkAccountStatus;
	}
	public void setCheckAccountStatus(String checkAccountStatus) {
		this.checkAccountStatus = checkAccountStatus;
	}
	public String getCollectionBatchNo() {
		return collectionBatchNo;
	}
	public void setCollectionBatchNo(String collectionBatchNo) {
		this.collectionBatchNo = collectionBatchNo;
	}
	public String getTransTime1() {
		return transTime1;
	}
	public void setTransTime1(String transTime1) {
		this.transTime1 = transTime1;
	}
	public String getTransTime2() {
		return transTime2;
	}
	public void setTransTime2(String transTime2) {
		this.transTime2 = transTime2;
	}
	public String getAgentProfitCollectionStatus() {
		return agentProfitCollectionStatus;
	}
	public void setAgentProfitCollectionStatus(String agentProfitCollectionStatus) {
		this.agentProfitCollectionStatus = agentProfitCollectionStatus;
	}
	
	public Date getAgentProfitGroupTime() {
		return agentProfitGroupTime;
	}
	public void setAgentProfitGroupTime(Date agentProfitGroupTime) {
		this.agentProfitGroupTime = agentProfitGroupTime;
	}
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	public BigDecimal getAcqOutProfit() {
		return acqOutProfit;
	}
	public void setAcqOutProfit(BigDecimal acqOutProfit) {
		this.acqOutProfit = acqOutProfit;
	}
	public String getHardwareProductName() {
		return hardwareProductName;
	}
	public void setHardwareProductName(String hardwareProductName) {
		this.hardwareProductName = hardwareProductName;
	}
	public String getBusinessProductName() {
		return businessProductName;
	}
	public void setBusinessProductName(String businessProductName) {
		this.businessProductName = businessProductName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getCollectionTime1() {
		return collectionTime1;
	}
	public void setCollectionTime1(String collectionTime1) {
		this.collectionTime1 = collectionTime1;
	}
	public String getCollectionTime2() {
		return collectionTime2;
	}
	public void setCollectionTime2(String collectionTime2) {
		this.collectionTime2 = collectionTime2;
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
	public BigDecimal getSettleProfits1() {
		return settleProfits1;
	}
	public void setSettleProfits1(BigDecimal settleProfits1) {
		this.settleProfits1 = settleProfits1;
	}
	public BigDecimal getSettleProfits2() {
		return settleProfits2;
	}
	public void setSettleProfits2(BigDecimal settleProfits2) {
		this.settleProfits2 = settleProfits2;
	}
	public BigDecimal getSettleProfits3() {
		return settleProfits3;
	}
	public void setSettleProfits3(BigDecimal settleProfits3) {
		this.settleProfits3 = settleProfits3;
	}
	public BigDecimal getSettleProfits4() {
		return settleProfits4;
	}
	public void setSettleProfits4(BigDecimal settleProfits4) {
		this.settleProfits4 = settleProfits4;
	}
	public BigDecimal getSettleProfits5() {
		return settleProfits5;
	}
	public void setSettleProfits5(BigDecimal settleProfits5) {
		this.settleProfits5 = settleProfits5;
	}
	public BigDecimal getSettleProfits6() {
		return settleProfits6;
	}
	public void setSettleProfits6(BigDecimal settleProfits6) {
		this.settleProfits6 = settleProfits6;
	}
	public BigDecimal getSettleProfits7() {
		return settleProfits7;
	}
	public void setSettleProfits7(BigDecimal settleProfits7) {
		this.settleProfits7 = settleProfits7;
	}
	public BigDecimal getSettleProfits8() {
		return settleProfits8;
	}
	public void setSettleProfits8(BigDecimal settleProfits8) {
		this.settleProfits8 = settleProfits8;
	}
	public BigDecimal getSettleProfits9() {
		return settleProfits9;
	}
	public void setSettleProfits9(BigDecimal settleProfits9) {
		this.settleProfits9 = settleProfits9;
	}
	public BigDecimal getSettleProfits10() {
		return settleProfits10;
	}
	public void setSettleProfits10(BigDecimal settleProfits10) {
		this.settleProfits10 = settleProfits10;
	}
	public BigDecimal getSettleProfits11() {
		return settleProfits11;
	}
	public void setSettleProfits11(BigDecimal settleProfits11) {
		this.settleProfits11 = settleProfits11;
	}
	public BigDecimal getSettleProfits12() {
		return settleProfits12;
	}
	public void setSettleProfits12(BigDecimal settleProfits12) {
		this.settleProfits12 = settleProfits12;
	}
	public BigDecimal getSettleProfits13() {
		return settleProfits13;
	}
	public void setSettleProfits13(BigDecimal settleProfits13) {
		this.settleProfits13 = settleProfits13;
	}
	public BigDecimal getSettleProfits14() {
		return settleProfits14;
	}
	public void setSettleProfits14(BigDecimal settleProfits14) {
		this.settleProfits14 = settleProfits14;
	}
	public BigDecimal getSettleProfits15() {
		return settleProfits15;
	}
	public void setSettleProfits15(BigDecimal settleProfits15) {
		this.settleProfits15 = settleProfits15;
	}
	public BigDecimal getSettleProfits16() {
		return settleProfits16;
	}
	public void setSettleProfits16(BigDecimal settleProfits16) {
		this.settleProfits16 = settleProfits16;
	}
	public BigDecimal getSettleProfits17() {
		return settleProfits17;
	}
	public void setSettleProfits17(BigDecimal settleProfits17) {
		this.settleProfits17 = settleProfits17;
	}
	public BigDecimal getSettleProfits18() {
		return settleProfits18;
	}
	public void setSettleProfits18(BigDecimal settleProfits18) {
		this.settleProfits18 = settleProfits18;
	}
	public BigDecimal getSettleProfits19() {
		return settleProfits19;
	}
	public void setSettleProfits19(BigDecimal settleProfits19) {
		this.settleProfits19 = settleProfits19;
	}
	public BigDecimal getSettleProfits20() {
		return settleProfits20;
	}
	public void setSettleProfits20(BigDecimal settleProfits20) {
		this.settleProfits20 = settleProfits20;
	}

	public String getPlateMerchantRate() {
		return plateMerchantRate;
	}

	public void setPlateMerchantRate(String plateMerchantRate) {
		this.plateMerchantRate = plateMerchantRate;
	}

	public String getPlateAcqMerchantRate() {
		return plateAcqMerchantRate;
	}

	public void setPlateAcqMerchantRate(String plateAcqMerchantRate) {
		this.plateAcqMerchantRate = plateAcqMerchantRate;
	}

	public BigDecimal getOneAgentShareAmount() {
		return oneAgentShareAmount;
	}

	public void setOneAgentShareAmount(BigDecimal oneAgentShareAmount) {
		this.oneAgentShareAmount = oneAgentShareAmount;
	}

	public BigDecimal getOneCashAgentShareAmount() {
		return oneCashAgentShareAmount;
	}

	public void setOneCashAgentShareAmount(BigDecimal oneCashAgentShareAmount) {
		this.oneCashAgentShareAmount = oneCashAgentShareAmount;
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
