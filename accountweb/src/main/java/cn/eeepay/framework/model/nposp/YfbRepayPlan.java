package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class YfbRepayPlan implements Serializable{

    private Integer id;
    private String merchantNo;
    private String batchNo;
    private String cardNo;
    private BigDecimal repayAmount;
    private Date repayBeginTime;
    private Date repayEndTime;
    private String ensureAmountRate;
    private BigDecimal ensureAmount;
    private String status;
    private String repayFeeRate;
    private BigDecimal repayFee;
    private String rowLock;
    private Date createTime;
    private Date lastUpdateTime;

    //扩展
    private String userName;
    private String mobileNo;

    private String ruStatus;       //入账状态
    private Date tallyTime;     //入账时间

    private Date countTime;    //汇总时间
    private String serviceOrderNo;    //汇总批次

    //查询
    //入账时间
    private String tallyTime1;
    private String tallyTime2;

    private BigDecimal repayAmount1;        //任务金额
    private BigDecimal repayAmount2;

    private BigDecimal ensureAmount1;       //保证金
    private BigDecimal ensureAmount2;

    private BigDecimal repayFee1;           //服务费
    private BigDecimal repayFee2;



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

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public Date getRepayBeginTime() {
        return repayBeginTime;
    }

    public void setRepayBeginTime(Date repayBeginTime) {
        this.repayBeginTime = repayBeginTime;
    }

    public Date getRepayEndTime() {
        return repayEndTime;
    }

    public void setRepayEndTime(Date repayEndTime) {
        this.repayEndTime = repayEndTime;
    }

    public String getEnsureAmountRate() {
        return ensureAmountRate;
    }

    public void setEnsureAmountRate(String ensureAmountRate) {
        this.ensureAmountRate = ensureAmountRate;
    }

    public BigDecimal getEnsureAmount() {
        return ensureAmount;
    }

    public void setEnsureAmount(BigDecimal ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRepayFeeRate() {
        return repayFeeRate;
    }

    public void setRepayFeeRate(String repayFeeRate) {
        this.repayFeeRate = repayFeeRate;
    }

    public BigDecimal getRepayFee() {
        return repayFee;
    }

    public void setRepayFee(BigDecimal repayFee) {
        this.repayFee = repayFee;
    }

    public String getRowLock() {
        return rowLock;
    }

    public void setRowLock(String rowLock) {
        this.rowLock = rowLock;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getRuStatus() {
        return ruStatus;
    }

    public void setRuStatus(String ruStatus) {
        this.ruStatus = ruStatus;
    }

    public Date getTallyTime() {
        return tallyTime;
    }

    public void setTallyTime(Date tallyTime) {
        this.tallyTime = tallyTime;
    }

    public String getTallyTime1() {
        return tallyTime1;
    }

    public void setTallyTime1(String tallyTime1) {
        this.tallyTime1 = tallyTime1;
    }

    public String getTallyTime2() {
        return tallyTime2;
    }

    public void setTallyTime2(String tallyTime2) {
        this.tallyTime2 = tallyTime2;
    }

    public BigDecimal getRepayAmount1() {
        return repayAmount1;
    }

    public void setRepayAmount1(BigDecimal repayAmount1) {
        this.repayAmount1 = repayAmount1;
    }

    public BigDecimal getRepayAmount2() {
        return repayAmount2;
    }

    public void setRepayAmount2(BigDecimal repayAmount2) {
        this.repayAmount2 = repayAmount2;
    }

    public BigDecimal getEnsureAmount1() {
        return ensureAmount1;
    }

    public void setEnsureAmount1(BigDecimal ensureAmount1) {
        this.ensureAmount1 = ensureAmount1;
    }

    public BigDecimal getEnsureAmount2() {
        return ensureAmount2;
    }

    public void setEnsureAmount2(BigDecimal ensureAmount2) {
        this.ensureAmount2 = ensureAmount2;
    }

    public BigDecimal getRepayFee1() {
        return repayFee1;
    }

    public void setRepayFee1(BigDecimal repayFee1) {
        this.repayFee1 = repayFee1;
    }

    public BigDecimal getRepayFee2() {
        return repayFee2;
    }

    public void setRepayFee2(BigDecimal repayFee2) {
        this.repayFee2 = repayFee2;
    }

    public Date getCountTime() {
        return countTime;
    }

    public void setCountTime(Date countTime) {
        this.countTime = countTime;
    }

    public String getServiceOrderNo() {
        return serviceOrderNo;
    }

    public void setServiceOrderNo(String serviceOrderNo) {
        this.serviceOrderNo = serviceOrderNo;
    }
}
