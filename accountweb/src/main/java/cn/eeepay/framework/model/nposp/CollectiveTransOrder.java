package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单总表
 * @author yt
 * 2016年9月29日
 *
 */
public class CollectiveTransOrder implements Serializable {
	private Integer id;                  
	private String orderNo;            
	private String mobileNo;           
	private String merchantNo;        
	private BigDecimal transAmount;        
	private String payMethod;          
	private String transStatus;        
	private String transType;          
	private Date transTime;          
	private Date createTime;         
	private String accountNo;          
	private String cardType;           
	private String holidaysMark;       
	private String businessProductId; 
	private Integer serviceId;          
	private Integer acqOrgId;          
	private String acqName;           
	private String acqEnname;          
	private Integer acqServiceId;      
	private BigDecimal merchantFee;        
	private String merchantRate;       
	private String merchantRateType;  
	private String acqMerchantNo;
	private BigDecimal acqMerchantFee;    
	private String acqMerchantRate;   
	private String acqRateType;       
	private String agentNode;          
	private String settlementMethod;   
	private Integer settleStatus;       
	private String settleMsg;          
	private Integer synStatus;          
	private String freezeStatus;       
	private String deviceSn;           
	private Integer hardwareProduct;    
	private BigDecimal profits1;
	private BigDecimal num;
	
	private Integer account;
	
	private String settleType;

	private String acqReferenceNo;
	private String unionpayMerNo;
	private String mbpId;
	private String agentNo;
	private String terminalNo;
	private String acqBatchNo;
	private String acqSerialNo;
	private String batchNo;
	private String serialNo;
	private String transSource;
	private String bagSettle;
	private String posType;
	private String merchantSettleDate;
	private BigDecimal singleShareAmount;

	public String getMerchantSettleDate() {
		return merchantSettleDate;
	}

	public void setMerchantSettleDate(String merchantSettleDate) {
		this.merchantSettleDate = merchantSettleDate;
	}

	public BigDecimal getSingleShareAmount() {
		return singleShareAmount;
	}

	public void setSingleShareAmount(BigDecimal singleShareAmount) {
		this.singleShareAmount = singleShareAmount;
	}

	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
	}

	public String getBagSettle() {
		return bagSettle;
	}

	public void setBagSettle(String bagSettle) {
		this.bagSettle = bagSettle;
	}

	public String getTransSource() {
		return transSource;
	}

	public void setTransSource(String transSource) {
		this.transSource = transSource;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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

	public String getAcqBatchNo() {
		return acqBatchNo;
	}

	public void setAcqBatchNo(String acqBatchNo) {
		this.acqBatchNo = acqBatchNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getUnionpayMerNo() {
		return unionpayMerNo;
	}

	public void setUnionpayMerNo(String unionpayMerNo) {
		this.unionpayMerNo = unionpayMerNo;
	}

	public String getMbpId() {
		return mbpId;
	}

	public void setMbpId(String mbpId) {
		this.mbpId = mbpId;
	}

	public String getAcqReferenceNo() {
		return acqReferenceNo;
	}

	public void setAcqReferenceNo(String acqReferenceNo) {
		this.acqReferenceNo = acqReferenceNo;
	}

	public BigDecimal getNum() {
		return num;
	}

	public void setNum(BigDecimal num) {
		this.num = num;
	}

	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}
	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
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
		this.orderNo = orderNo;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
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
		this.payMethod = payMethod;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
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
		this.accountNo = accountNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getHolidaysMark() {
		return holidaysMark;
	}
	public void setHolidaysMark(String holidaysMark) {
		this.holidaysMark = holidaysMark;
	}
	public String getBusinessProductId() {
		return businessProductId;
	}
	public void setBusinessProductId(String businessProductId) {
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
		this.acqName = acqName;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
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
		this.merchantRate = merchantRate;
	}
	public String getMerchantRateType() {
		return merchantRateType;
	}
	public void setMerchantRateType(String merchantRateType) {
		this.merchantRateType = merchantRateType;
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
		this.acqMerchantRate = acqMerchantRate;
	}
	public String getAcqRateType() {
		return acqRateType;
	}
	public void setAcqRateType(String acqRateType) {
		this.acqRateType = acqRateType;
	}
	public String getAgentNode() {
		return agentNode;
	}
	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}
	public String getSettlementMethod() {
		return settlementMethod;
	}
	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	public Integer getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(Integer settleStatus) {
		this.settleStatus = settleStatus;
	}
	public String getSettleMsg() {
		return settleMsg;
	}
	public void setSettleMsg(String settleMsg) {
		this.settleMsg = settleMsg;
	}
	public Integer getSynStatus() {
		return synStatus;
	}
	public void setSynStatus(Integer synStatus) {
		this.synStatus = synStatus;
	}
	public String getFreezeStatus() {
		return freezeStatus;
	}
	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
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
	
    
}

   