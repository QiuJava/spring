package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易流水表
 * @author zouruijin
 * 2016年3月29日14:04:20
 *
 */
public class TransInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String orderNo;
    private String acqEnname; 
    private String acqCode; 
    private String acqMerchantNo; 
    private String acqTerminalNo; 
    private String acqAuthNo; 
    private String acqReferenceNo; 
    private String acqBatchNo; 
    private String acqSerialNo; 
    private String acqResponseCode; 
    private String agentNo; 
    private String merchantNo; 
    private String terminalNo; 
    private String batchNo; 
    private String serialNo; 
    private String accountNo; 
    private String cardType; 
    private String currencyType; 
    private BigDecimal transAmount; 
    private BigDecimal merchantFee; 
    private String merchantRate; 
    private BigDecimal acqMerchantFee; 
    private String acqMerchantRate; 
    private String transType; 
    private String transStatus; 
    private String transSource; 
    private String oriAcqBatchNo; 
    private String oriAcqSerialNo; 
    private String oriBatchNo; 
    private String oriSerialNo; 
    private Date acqSettleDate; 
    private Date merchantSettleDate; 
    private Integer mySettle; 
    private String reviewStatus; 
    private Date transTime; 
    private Date lastUpdateTime; 
    private Date createTime; 
    private String settleStatus; 
    private Integer belongPay; 
    private String bagSettle; 
    private String transMsg; 
    private String cardholderPhone; 
    private String transId; 
    private String freezeStatus; 
    private String signImg; 
    private String deviceSn; 
    private String signCheckPerson; 
    private Date signCheckTime; 
    private Integer isIcCard; 
    private String icMsg; 
    private Integer insurance; 
    private String expired; 
    private String posType;
    private String msgId;
    private Integer accStatus;
    private Integer issuedStatus;
    private String serviceId;
    private BigDecimal singleShareAmount;
    private String acqServiceId;
    
    private BigDecimal profits1;
    private String settlementMethod;
    
    private String settleType;
    
    //VO
    private Integer account;

    
    
	public String getSettleType() {
		return settleType;
	}
	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getSettlementMethod() {
		return settlementMethod;
	}
	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	public String getAcqServiceId() {
		return acqServiceId;
	}
	public void setAcqServiceId(String acqServiceId) {
		this.acqServiceId = acqServiceId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getAcqCode() {
		return acqCode;
	}
	public void setAcqCode(String acqCode) {
		this.acqCode = acqCode;
	}
	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}
	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}
	public String getAcqTerminalNo() {
		return acqTerminalNo;
	}
	public void setAcqTerminalNo(String acqTerminalNo) {
		this.acqTerminalNo = acqTerminalNo;
	}
	public String getAcqAuthNo() {
		return acqAuthNo;
	}
	public void setAcqAuthNo(String acqAuthNo) {
		this.acqAuthNo = acqAuthNo;
	}
	public String getAcqReferenceNo() {
		return acqReferenceNo;
	}
	public void setAcqReferenceNo(String acqReferenceNo) {
		this.acqReferenceNo = acqReferenceNo;
	}
	public String getAcqBatchNo() {
		return acqBatchNo;
	}
	public void setAcqBatchNo(String acqBatchNo) {
		this.acqBatchNo = acqBatchNo;
	}
	public String getAcqSerialNo() {
		return acqSerialNo;
	}
	public void setAcqSerialNo(String acqSerialNo) {
		this.acqSerialNo = acqSerialNo;
	}
	public String getAcqResponseCode() {
		return acqResponseCode;
	}
	public void setAcqResponseCode(String acqResponseCode) {
		this.acqResponseCode = acqResponseCode;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
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
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
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
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getTransStatus() {
		return transStatus;
	}
	public void setTransStatus(String transStatus) {
		this.transStatus = transStatus;
	}
	public String getTransSource() {
		return transSource;
	}
	public void setTransSource(String transSource) {
		this.transSource = transSource;
	}
	public String getOriAcqBatchNo() {
		return oriAcqBatchNo;
	}
	public void setOriAcqBatchNo(String oriAcqBatchNo) {
		this.oriAcqBatchNo = oriAcqBatchNo;
	}
	public String getOriAcqSerialNo() {
		return oriAcqSerialNo;
	}
	public void setOriAcqSerialNo(String oriAcqSerialNo) {
		this.oriAcqSerialNo = oriAcqSerialNo;
	}
	public String getOriBatchNo() {
		return oriBatchNo;
	}
	public void setOriBatchNo(String oriBatchNo) {
		this.oriBatchNo = oriBatchNo;
	}
	public String getOriSerialNo() {
		return oriSerialNo;
	}
	public void setOriSerialNo(String oriSerialNo) {
		this.oriSerialNo = oriSerialNo;
	}
	public Date getAcqSettleDate() {
		return acqSettleDate;
	}
	public void setAcqSettleDate(Date acqSettleDate) {
		this.acqSettleDate = acqSettleDate;
	}
	public Date getMerchantSettleDate() {
		return merchantSettleDate;
	}
	public void setMerchantSettleDate(Date merchantSettleDate) {
		this.merchantSettleDate = merchantSettleDate;
	}
	public Integer getMySettle() {
		return mySettle;
	}
	public void setMySettle(Integer mySettle) {
		this.mySettle = mySettle;
	}
	public String getReviewStatus() {
		return reviewStatus;
	}
	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}
	public Date getTransTime() {
		return transTime;
	}
	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}
	public Integer getBelongPay() {
		return belongPay;
	}
	public void setBelongPay(Integer belongPay) {
		this.belongPay = belongPay;
	}
	public String getBagSettle() {
		return bagSettle;
	}
	public void setBagSettle(String bagSettle) {
		this.bagSettle = bagSettle;
	}
	public String getTransMsg() {
		return transMsg;
	}
	public void setTransMsg(String transMsg) {
		this.transMsg = transMsg;
	}
	public String getCardholderPhone() {
		return cardholderPhone;
	}
	public void setCardholderPhone(String cardholderPhone) {
		this.cardholderPhone = cardholderPhone;
	}
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getFreezeStatus() {
		return freezeStatus;
	}
	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}
	public String getSignImg() {
		return signImg;
	}
	public void setSignImg(String signImg) {
		this.signImg = signImg;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getSignCheckPerson() {
		return signCheckPerson;
	}
	public void setSignCheckPerson(String signCheckPerson) {
		this.signCheckPerson = signCheckPerson;
	}
	public Date getSignCheckTime() {
		return signCheckTime;
	}
	public void setSignCheckTime(Date signCheckTime) {
		this.signCheckTime = signCheckTime;
	}
	
	public Integer getIsIcCard() {
		return isIcCard;
	}
	public void setIsIcCard(Integer isIcCard) {
		this.isIcCard = isIcCard;
	}
	public String getIcMsg() {
		return icMsg;
	}
	public void setIcMsg(String icMsg) {
		this.icMsg = icMsg;
	}
	public Integer getInsurance() {
		return insurance;
	}
	public void setInsurance(Integer insurance) {
		this.insurance = insurance;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public Integer getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(Integer accStatus) {
		this.accStatus = accStatus;
	}
	public Integer getIssuedStatus() {
		return issuedStatus;
	}
	public void setIssuedStatus(Integer issuedStatus) {
		this.issuedStatus = issuedStatus;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getExpired() {
		return expired;
	}
	public void setExpired(String expired) {
		this.expired = expired;
	}
	public BigDecimal getSingleShareAmount() {
		return singleShareAmount;
	}
	public void setSingleShareAmount(BigDecimal singleShareAmount) {
		this.singleShareAmount = singleShareAmount;
	}
	@Override
	public String toString() {
		return "TransInfo [id=" + id + ", acqEnname=" + acqEnname + ", acqCode=" + acqCode + ", acqMerchantNo="
				+ acqMerchantNo + ", acqTerminalNo=" + acqTerminalNo + ", acqAuthNo=" + acqAuthNo + ", acqReferenceNo="
				+ acqReferenceNo + ", acqBatchNo=" + acqBatchNo + ", acqSerialNo=" + acqSerialNo + ", acqResponseCode="
				+ acqResponseCode + ", agentNo=" + agentNo + ", merchantNo=" + merchantNo + ", terminalNo=" + terminalNo
				+ ", batchNo=" + batchNo + ", serialNo=" + serialNo + ", accountNo=" + accountNo + ", cardType="
				+ cardType + ", currencyType=" + currencyType + ", transAmount=" + transAmount + ", merchantFee="
				+ merchantFee + ", merchantRate=" + merchantRate + ", acqMerchantFee=" + acqMerchantFee
				+ ", acqMerchantRate=" + acqMerchantRate + ", transType=" + transType + ", transStatus=" + transStatus
				+ ", transSource=" + transSource + ", oriAcqBatchNo=" + oriAcqBatchNo + ", oriAcqSerialNo="
				+ oriAcqSerialNo + ", oriBatchNo=" + oriBatchNo + ", oriSerialNo=" + oriSerialNo + ", acqSettleDate="
				+ acqSettleDate + ", merchantSettleDate=" + merchantSettleDate + ", mySettle=" + mySettle
				+ ", reviewStatus=" + reviewStatus + ", transTime=" + transTime + ", lastUpdateTime=" + lastUpdateTime
				+ ", createTime=" + createTime + ", settleStatus=" + settleStatus + ", belongPay=" + belongPay
				+ ", bagSettle=" + bagSettle + ", transMsg=" + transMsg + ", cardholderPhone=" + cardholderPhone
				+ ", transId=" + transId + ", freezeStatus=" + freezeStatus + ", signImg=" + signImg + ", deviceSn="
				+ deviceSn + ", signCheckPerson=" + signCheckPerson + ", signCheckTime=" + signCheckTime + ", isIcCard="
				+ isIcCard + ", icMsg=" + icMsg + ", insurance=" + insurance + ", expired=" + expired + ", posType="
				+ posType + ", msgId=" + msgId + ", accStatus=" + accStatus + ", issuedStatus=" + issuedStatus
				+ ", serviceId=" + serviceId + ", singleShareAmount=" + singleShareAmount + "]";
	}
	public BigDecimal getProfits1() {
		return profits1;
	}
	public void setProfits1(BigDecimal profits1) {
		this.profits1 = profits1;
	}
	public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}
	
}

   