package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author MXG
 * create 2018/11/27
 */
public class YfbPayOrder {
    private Integer id;
    private String merchantNo;
    private String orderNo;
    private String accountNo;
    private String accountName;
    private String service;
    private String serviceOrderNo;
    private BigDecimal transAmount;
    private BigDecimal transFee;
    private String transFeeRate;
    private String transType;
    private String acqCode;
    private String acqMerchantNo;
    private String acqFeeRate;
    private BigDecimal acqFee;
    private String acqOrderNo;
    private BigDecimal oneLevelProfit;
    private String body;
    private Date createTime;
    private String transStatus;
    private Date transTime;
    private String payToken;
    private String bankType;
    private String resCode;
    private String resMsg;
    private String cardType;
    private Integer successNotifyNum;

    private String nickname;//昵称
    private String mobileNo;//手机号
    private String recordStatus;//记账状态  0未记账 1记账中  2记账成功  3记账失败

    private String bankName;
    private String oneAgentNo;
    private String oneAgentName;
    private String payMerchantName;//收单商户名称

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public BigDecimal getTransFee() {
        return transFee;
    }

    public void setTransFee(BigDecimal transFee) {
        this.transFee = transFee;
    }

    public String getTransFeeRate() {
        return transFeeRate;
    }

    public void setTransFeeRate(String transFeeRate) {
        this.transFeeRate = transFeeRate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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

    public String getAcqFeeRate() {
        return acqFeeRate;
    }

    public void setAcqFeeRate(String acqFeeRate) {
        this.acqFeeRate = acqFeeRate;
    }

    public BigDecimal getAcqFee() {
        return acqFee;
    }

    public void setAcqFee(BigDecimal acqFee) {
        this.acqFee = acqFee;
    }

    public String getAcqOrderNo() {
        return acqOrderNo;
    }

    public void setAcqOrderNo(String acqOrderNo) {
        this.acqOrderNo = acqOrderNo;
    }

    public BigDecimal getOneLevelProfit() {
        return oneLevelProfit;
    }

    public void setOneLevelProfit(BigDecimal oneLevelProfit) {
        this.oneLevelProfit = oneLevelProfit;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getPayToken() {
        return payToken;
    }

    public void setPayToken(String payToken) {
        this.payToken = payToken;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Integer getSuccessNotifyNum() {
        return successNotifyNum;
    }

    public void setSuccessNotifyNum(Integer successNotifyNum) {
        this.successNotifyNum = successNotifyNum;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getPayMerchantName() {
        return payMerchantName;
    }

    public void setPayMerchantName(String payMerchantName) {
        this.payMerchantName = payMerchantName;
    }
}
