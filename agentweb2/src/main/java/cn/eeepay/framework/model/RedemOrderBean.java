package cn.eeepay.framework.model;

/**
 * Created by 666666 on 2018/5/7.
 */
public class RedemOrderBean {
    private String id;
    private String orderNo;     // 订单ID
    private String oemNo;       // 所属组织
    private String orderStatus; // 订单状态
    private String agentNo;     // 代理编号
    private String agentNode;   // 代理节点
    private String merchantNo;  // 商户编号/贡献人ID
    private String userName;    // 商户名称/贡献人名称
    private String mobile;      // 手机号/贡献人手机号
    private String profitMerchantNo;    //收益人ID
    private String profitUserName;  //收益人姓名
    private String profitMobile;   //受益人手机号
    private String profitMerCapa;   //收益人身份
    private String checkStatus;     // 核销状态
    private String shareAmount; //收益人分润
    private String shareGrade; //当前分润层级
    private String amount;      // 金额
    private String createTime;  // 创建时间
    private String payTime;     // 支付时间
    private String payOrderNo;  // 关联支付订单
    private String oemName;     // 品牌商的名称
    private String plateShare;  // 平台分润
    private String accStatus;   // 入账状态
    private String provideAmout;// 奖励总金额
    private String oemShare;     // 品牌分润汇总
    private String orgCode;     // 兑换机构编号
    private String orgName;     // 兑换机构名称
    private String orderType;     // 订单类型
    private String receiveStatus;
    private String createTimeStart;
    private String createTimeEnd;
    private String payTimeStart;
    private String payTimeEnd;

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getProfitMerchantNo() {
        return profitMerchantNo;
    }

    public void setProfitMerchantNo(String profitMerchantNo) {
        this.profitMerchantNo = profitMerchantNo;
    }

    public String getProfitUserName() {
        return profitUserName;
    }

    public void setProfitUserName(String profitUserName) {
        this.profitUserName = profitUserName;
    }

    public String getProfitMobile() {
        return profitMobile;
    }

    public void setProfitMobile(String profitMobile) {
        this.profitMobile = profitMobile;
    }

    public String getProfitMerCapa() {
        return profitMerCapa;
    }

    public void setProfitMerCapa(String profitMerCapa) {
        this.profitMerCapa = profitMerCapa;
    }

    public String getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(String shareAmount) {
        this.shareAmount = shareAmount;
    }

    public String getShareGrade() {
        return shareGrade;
    }

    public void setShareGrade(String shareGrade) {
        this.shareGrade = shareGrade;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }

    public String getPlateShare() {
        return plateShare;
    }

    public void setPlateShare(String plateShare) {
        this.plateShare = plateShare;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    public String getProvideAmout() {
        return provideAmout;
    }

    public void setProvideAmout(String provideAmout) {
        this.provideAmout = provideAmout;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOemShare() {
        return oemShare;
    }

    public void setOemShare(String oemShare) {
        this.oemShare = oemShare;
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

    public String getPayTimeStart() {
        return payTimeStart;
    }

    public void setPayTimeStart(String payTimeStart) {
        this.payTimeStart = payTimeStart;
    }

    public String getPayTimeEnd() {
        return payTimeEnd;
    }

    public void setPayTimeEnd(String payTimeEnd) {
        this.payTimeEnd = payTimeEnd;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}
