package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户收入信息
 * Created by 666666 on 2017/10/21.
 */
public class MerchantIncomeBean {
    private int id;                 // 主键id
    private String merchantNo;      // 商户编号
    private String merchantName;    // 商户名称
    private String mobilephone;    // 商户手机号
    private BigDecimal transAmount; // 交易金额
    private BigDecimal transFee;    // 活动交易费率
    private BigDecimal discountFee;  // 优惠后手续费
    private BigDecimal merchantProfit;  // 收益金额
    private BigDecimal transRate;       // 活动交易费率
    private String activityCode;       // 活动类型
    private String orderNo;             // 交易订单号
    private Date createTime;          // 创建时间
    private String createPerson;        // 创建人
    private String remark;              // 备注

    private BigDecimal minMerchantProfit;   // 最小商户收益
    private BigDecimal maxMerchantProfit;   // 最大商户收益
    private BigDecimal minTransAmount;      // 最小交易金额
    private BigDecimal maxTransAmount;      // 最大交易金额
    private String loginAgentNode;
    private String startCreateTime;         // 开始创建时间
    private String endCreateTime;           // 结束创建时间

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

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getLoginAgentNode() {
        return loginAgentNode;
    }

    public void setLoginAgentNode(String loginAgentNode) {
        this.loginAgentNode = loginAgentNode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public BigDecimal getTransFee() {
        return transFee;
    }

    public void setTransFee(BigDecimal transFee) {
        this.transFee = transFee;
    }

    public BigDecimal getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(BigDecimal discountFee) {
        this.discountFee = discountFee;
    }

    public BigDecimal getMerchantProfit() {
        return merchantProfit;
    }

    public void setMerchantProfit(BigDecimal merchantProfit) {
        this.merchantProfit = merchantProfit;
    }

    public BigDecimal getTransRate() {
        return transRate;
    }

    public void setTransRate(BigDecimal transRate) {
        this.transRate = transRate;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getMinMerchantProfit() {
        return minMerchantProfit;
    }

    public void setMinMerchantProfit(BigDecimal minMerchantProfit) {
        this.minMerchantProfit = minMerchantProfit;
    }

    public BigDecimal getMaxMerchantProfit() {
        return maxMerchantProfit;
    }

    public void setMaxMerchantProfit(BigDecimal maxMerchantProfit) {
        this.maxMerchantProfit = maxMerchantProfit;
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
}
