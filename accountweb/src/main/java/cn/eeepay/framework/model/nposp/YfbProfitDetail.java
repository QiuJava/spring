package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class YfbProfitDetail implements Serializable {

    private Integer id;
    private String profitNo;    //分润明细编号
    private String orderNo;  //关联订单ID
    private Date transTime;     //订单时间
    private String profitMerNo;     //服务商编号
    private String agentName;      //服务商名称
    private BigDecimal shareAmount;      //代理商分润金额
    private BigDecimal repayAmount;         //任务金额
    private BigDecimal ensureAmount;     //保证金额
    private BigDecimal repayFee;        //服务费
    private BigDecimal actualPayFee;  //实际交易手续费
    private BigDecimal actualWithdrawFee; //实际代付手续费
    private String repayFeeRate;            //实际费率
    private String merchantNo; //用户编号
    private String userName;    //用户名称
    private String collectionBatchNo;       //汇总批次号
    private Date collectionTime;     //汇总时间
    private BigDecimal toProfitAmount;      //产生分润金额
    private String fenMoney1;       //分润金额1
    private String fenMoney2;       //分润金额2
    private String orderTime1;      //订单日期1
    private String orderTime2;      //订单日期2
    private String agentLevel;      //代理商级别
    private String parentId;        //代理商父级ID
    private String oneLevelId;        //一级代理商ID
    private String oneAgentName;    //一级代理商名称

    private BigDecimal successPayAmount;        //成功消费总金额
    private BigDecimal successRepayAmount;      //成功还款总金额
    private String acqCode;    //收单机构

    private BigDecimal fee;     //收单扣率
    private BigDecimal payFl;       //收单手续费

    private String profitType;      //订单类型


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

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getProfitMerNo() {
        return profitMerNo;
    }

    public void setProfitMerNo(String profitMerNo) {
        this.profitMerNo = profitMerNo;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public BigDecimal getRepayFee() {
        return repayFee;
    }

    public void setRepayFee(BigDecimal repayFee) {
        this.repayFee = repayFee;
    }

    public BigDecimal getActualPayFee() {
        return actualPayFee;
    }

    public void setActualPayFee(BigDecimal actualPayFee) {
        this.actualPayFee = actualPayFee;
    }

    public BigDecimal getActualWithdrawFee() {
        return actualWithdrawFee;
    }

    public void setActualWithdrawFee(BigDecimal actualWithdrawFee) {
        this.actualWithdrawFee = actualWithdrawFee;
    }

    public String getRepayFeeRate() {
        return repayFeeRate;
    }

    public void setRepayFeeRate(String repayFeeRate) {
        this.repayFeeRate = repayFeeRate;
    }

    public BigDecimal getRepayAmount() {
        return repayAmount;
    }

    public void setRepayAmount(BigDecimal repayAmount) {
        this.repayAmount = repayAmount;
    }

    public BigDecimal getEnsureAmount() {
        return ensureAmount;
    }

    public void setEnsureAmount(BigDecimal ensureAmount) {
        this.ensureAmount = ensureAmount;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getCollectionBatchNo() {
        return collectionBatchNo;
    }

    public void setCollectionBatchNo(String collectionBatchNo) {
        this.collectionBatchNo = collectionBatchNo;
    }

    public Date getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(Date collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getProfitNo() {
        return profitNo;
    }

    public void setProfitNo(String profitNo) {
        this.profitNo = profitNo;
    }

    public String getFenMoney1() {
        return fenMoney1;
    }

    public void setFenMoney1(String fenMoney1) {
        this.fenMoney1 = fenMoney1;
    }

    public String getFenMoney2() {
        return fenMoney2;
    }

    public void setFenMoney2(String fenMoney2) {
        this.fenMoney2 = fenMoney2;
    }

    public String getOrderTime1() {
        return orderTime1;
    }

    public void setOrderTime1(String orderTime1) {
        this.orderTime1 = orderTime1;
    }

    public String getOrderTime2() {
        return orderTime2;
    }

    public void setOrderTime2(String orderTime2) {
        this.orderTime2 = orderTime2;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getOneLevelId() {
        return oneLevelId;
    }

    public void setOneLevelId(String oneLevelId) {
        this.oneLevelId = oneLevelId;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public BigDecimal getSuccessPayAmount() {
        return successPayAmount;
    }

    public void setSuccessPayAmount(BigDecimal successPayAmount) {
        this.successPayAmount = successPayAmount;
    }

    public BigDecimal getSuccessRepayAmount() {
        return successRepayAmount;
    }

    public void setSuccessRepayAmount(BigDecimal successRepayAmount) {
        this.successRepayAmount = successRepayAmount;
    }

    public String getAcqCode() {
        return acqCode;
    }

    public void setAcqCode(String acqCode) {
        this.acqCode = acqCode;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getPayFl() {
        return payFl;
    }

    public void setPayFl(BigDecimal payFl) {
        this.payFl = payFl;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public BigDecimal getToProfitAmount() {
        return toProfitAmount;
    }

    public void setToProfitAmount(BigDecimal toProfitAmount) {
        this.toProfitAmount = toProfitAmount;
    }
}


