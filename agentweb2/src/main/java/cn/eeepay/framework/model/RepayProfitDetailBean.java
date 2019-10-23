package cn.eeepay.framework.model;

import cn.eeepay.framework.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by 666666 on 2017/11/20.
 */
public class RepayProfitDetailBean {
    // 主键
    private String id;
    // 分润明细编号
    private String profitNo;
    // 订单号/计划批次号
    private String orderNo;
    // 分润的商户类型 A：代理商  M:商户
    private String profitMerType;
    // 是否包含下级
    private String bool;
    // 商户号/代理商编号
    private String profitMerNo;
    private String agentName;
    // 交易金额
    private String transAmount;
    // 交易时间/计划终态时间
    private String transTime;
    // 支付商户
    private String merchantNo;
    // 交易商户直属代理商node
    private String agentNode;
    // 分润总金额
    private String shareAmount;
    // 交易分润金额
    private String sharePayAmount;
    // 产生分润金额
    private String toProfitAmount;
    // 代付分润金额
    private String shareWithdrawAmount;
    // 交易分润费率+代付分润金额，如：0.001+1
    private String shareRate;
    // 创建时间
    private String createTime;
    // 汇总批次号
    private String collectionBatchNo;
    // 汇总时间
    private String collectionTime;
    // 分润订单类型
    private String profitType;

    private String repayAmount;
    private String ensureAmount;
    private String repayFee;
    private String successPayAmount;
    private String successRepayAmount;
    private String actualPayFee;
    private String actualWithdrawFee;
    private String batchNo;
    private Date orderCreateTime;
    private String minShareAmount;
    private String maxShareAmount;
    private String minOrderCreateTime;
    private String maxOrderCreateTime;




    // 终态时间
    private Date completeTime;
    // 订单状态
    private String orderStatus;
    // 开始终态时间
    private String completeTimeBegin;
    // 结束终态时间
    private String completeTimeEnd;

    public String getToProfitAmount() {
        return toProfitAmount;
    }

    public RepayProfitDetailBean setToProfitAmount(String toProfitAmount) {
        this.toProfitAmount = toProfitAmount;
        return this;
    }

    public String getAgentName() {
        return agentName;
    }

    public RepayProfitDetailBean setAgentName(String agentName) {
        this.agentName = agentName;
        return this;
    }

    public String getMinShareAmount() {
        return minShareAmount;
    }

    public RepayProfitDetailBean setMinShareAmount(String minShareAmount) {
        this.minShareAmount = minShareAmount;
        return this;
    }

    public String getMaxShareAmount() {
        return maxShareAmount;
    }

    public RepayProfitDetailBean setMaxShareAmount(String maxShareAmount) {
        this.maxShareAmount = maxShareAmount;
        return this;
    }

    public String getMinOrderCreateTime() {
        return minOrderCreateTime;
    }

    public RepayProfitDetailBean setMinOrderCreateTime(String minOrderCreateTime) {
        this.minOrderCreateTime = minOrderCreateTime;
        return this;
    }

    public String getMaxOrderCreateTime() {
        return maxOrderCreateTime;
    }

    public RepayProfitDetailBean setMaxOrderCreateTime(String maxOrderCreateTime) {
        this.maxOrderCreateTime = maxOrderCreateTime;
        return this;
    }

    public String getId() {
        return id;
    }

    public RepayProfitDetailBean setId(String id) {
        this.id = id;
        return this;
    }

    public String getProfitNo() {
        return profitNo;
    }

    public RepayProfitDetailBean setProfitNo(String profitNo) {
        this.profitNo = profitNo;
        return this;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public RepayProfitDetailBean setOrderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public String getProfitMerType() {
        return profitMerType;
    }

    public RepayProfitDetailBean setProfitMerType(String profitMerType) {
        this.profitMerType = profitMerType;
        return this;
    }

    public String getProfitMerNo() {
        return profitMerNo;
    }

    public RepayProfitDetailBean setProfitMerNo(String profitMerNo) {
        this.profitMerNo = profitMerNo;
        return this;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public RepayProfitDetailBean setTransAmount(String transAmount) {
        this.transAmount = transAmount;
        return this;
    }

    public String getTransTime() {
        return transTime;
    }

    public RepayProfitDetailBean setTransTime(String transTime) {
        this.transTime = transTime;
        return this;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public RepayProfitDetailBean setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
        return this;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public RepayProfitDetailBean setAgentNode(String agentNode) {
        this.agentNode = agentNode;
        return this;
    }

    public String getShareAmount() {
        return shareAmount;
    }

    public RepayProfitDetailBean setShareAmount(String shareAmount) {
        this.shareAmount = shareAmount;
        return this;
    }

    public String getSharePayAmount() {
        return sharePayAmount;
    }

    public RepayProfitDetailBean setSharePayAmount(String sharePayAmount) {
        this.sharePayAmount = sharePayAmount;
        return this;
    }

    public String getShareWithdrawAmount() {
        return shareWithdrawAmount;
    }

    public RepayProfitDetailBean setShareWithdrawAmount(String shareWithdrawAmount) {
        this.shareWithdrawAmount = shareWithdrawAmount;
        return this;
    }

    public String getShareRate() {
        return shareRate;
    }

    public RepayProfitDetailBean setShareRate(String shareRate) {
        this.shareRate = shareRate;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public RepayProfitDetailBean setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getCollectionBatchNo() {
        return collectionBatchNo;
    }

    public RepayProfitDetailBean setCollectionBatchNo(String collectionBatchNo) {
        this.collectionBatchNo = collectionBatchNo;
        return this;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public RepayProfitDetailBean setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
        return this;
    }

    public String getRepayAmount() {
        return repayAmount;
    }

    public RepayProfitDetailBean setRepayAmount(String repayAmount) {
        this.repayAmount = repayAmount;
        return this;
    }

    public String getEnsureAmount() {
        return ensureAmount;
    }

    public RepayProfitDetailBean setEnsureAmount(String ensureAmount) {
        this.ensureAmount = ensureAmount;
        return this;
    }

    public String getRepayFee() {
        return repayFee;
    }

    public RepayProfitDetailBean setRepayFee(String repayFee) {
        this.repayFee = repayFee;
        return this;
    }

    public String getSuccessPayAmount() {
        return successPayAmount;
    }

    public RepayProfitDetailBean setSuccessPayAmount(String successPayAmount) {
        this.successPayAmount = successPayAmount;
        return this;
    }

    public String getSuccessRepayAmount() {
        return successRepayAmount;
    }

    public RepayProfitDetailBean setSuccessRepayAmount(String successRepayAmount) {
        this.successRepayAmount = successRepayAmount;
        return this;
    }

    public String getActualPayFee() {
        return actualPayFee;
    }

    public RepayProfitDetailBean setActualPayFee(String actualPayFee) {
        this.actualPayFee = actualPayFee;
        return this;
    }

    public String getActualWithdrawFee() {
        return actualWithdrawFee;
    }

    public RepayProfitDetailBean setActualWithdrawFee(String actualWithdrawFee) {
        this.actualWithdrawFee = actualWithdrawFee;
        return this;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public RepayProfitDetailBean setBatchNo(String batchNo) {
        this.batchNo = batchNo;
        return this;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public RepayProfitDetailBean setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
        return this;
    }

    public String getProfitType() {
        return profitType;
    }

    public RepayProfitDetailBean setProfitType(String profitType) {
        this.profitType = profitType;
        return this;
    }

    public String getBool() {
        return bool;
    }

    public RepayProfitDetailBean setBool(String bool) {
        this.bool = bool;
        return this;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public RepayProfitDetailBean setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
        return this;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public RepayProfitDetailBean setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
        return this;
    }

    public String getCompleteTimeBegin() {
        return completeTimeBegin;
    }

    public RepayProfitDetailBean setCompleteTimeBegin(String completeTimeBegin) {
        this.completeTimeBegin = completeTimeBegin;
        return this;
    }

    public String getCompleteTimeEnd() {
        return completeTimeEnd;
    }

    public RepayProfitDetailBean setCompleteTimeEnd(String completeTimeEnd) {
        this.completeTimeEnd = completeTimeEnd;
        return this;
    }
}
