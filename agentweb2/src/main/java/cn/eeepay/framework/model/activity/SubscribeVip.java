package cn.eeepay.framework.model.activity;

import java.math.BigDecimal;

/**
 * VIP优享订单
 * @author MXG
 * create 2019/03/25
 */
public class SubscribeVip {
    private Integer id;
    private String orderNo; // 会员订单号
    private String userId; // 订阅会员的用户ID，为商户号
    private String agentNo;//代理商编号
    private String agentName;
    private String merchantNo;
    private String merchantName;
    private String currentAgentNode;//当前登录代理商节点
    private String mobilephone;// 商户手机号
    private String paymentOrderNo;//支付订单
    private String vipType; // 订阅的会员类型 activity_vip的id
    private String name; // 服务名称
    private Integer time; // 天数
    private String subscribeStatus; // 订阅状态
    private String subscribeRemark; // 订阅状态描述
    private String validityStart; // 会员生效时间
    private String validityEnd; // 会员截止时间
    private BigDecimal amount;
    private String paymentType;// 付款方式
    private String createTime; // 创建时间
    private String createTimeStart;
    private String createTimeEnd;
    private String lastUpdateTime; // 最后修改时间

    public String getAgentName() {
        return agentName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getCurrentAgentNode() {
        return currentAgentNode;
    }

    public void setCurrentAgentNode(String currentAgentNode) {
        this.currentAgentNode = currentAgentNode;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getVipType() {
        return vipType;
    }

    public void setVipType(String vipType) {
        this.vipType = vipType;
    }

    public String getSubscribeStatus() {
        return subscribeStatus;
    }

    public void setSubscribeStatus(String subscribeStatus) {
        this.subscribeStatus = subscribeStatus;
    }

    public String getSubscribeRemark() {
        return subscribeRemark;
    }

    public void setSubscribeRemark(String subscribeRemark) {
        this.subscribeRemark = subscribeRemark;
    }

    public String getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(String validityStart) {
        this.validityStart = validityStart;
    }

    public String getValidityEnd() {
        return validityEnd;
    }

    public void setValidityEnd(String validityEnd) {
        this.validityEnd = validityEnd;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
