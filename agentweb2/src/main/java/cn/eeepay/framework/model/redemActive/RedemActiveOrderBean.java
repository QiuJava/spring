package cn.eeepay.framework.model.redemActive;

/**
 * Created by 666666 on 2018/5/7.
 */
public class RedemActiveOrderBean {
    private String id;
    private String orderNo;     // 订单ID
    private String oemNo;       // 所属组织
    private String orderStatus; // 订单状态
    private String agentNo;     // 代理编号
    private String agentNode;   // 代理节点
    private String merchantNo;  // 商户编号/用户ID
    private String userName;    // 商户名称/用户名称
    private String mobile;      // 手机号/用户手机号
    private String checkStatus;     // 核销状态
    private String checkTime;     // 核销时间
    private String checkOper;     // 核销人
    private String shareAmount; //收益人分润
    private String shareGrade; //当前分润层级
    private String amount;      // 金额
    private String createTime;  // 创建时间
    private String oemName;     // 品牌商的名称
    private String accStatus;   // 入账状态
    private String orgCode;     // 兑换机构编号
    private String orgName;     // 兑换机构名称
    private String orderType;     // 订单类型
    private String oemShare;     // 品牌分润
    private String receiveStatus;
    private String createTimeStart;
    private String createTimeEnd;
    private String checkTimeStart;     // 开始核销时间
    private String checkTimeEnd;     // 结束核销时间


    // 收款订单的属性
    private String receiveMerchantNo; // 收款商户ID
    private String sourceOrderNo;  // 关联订单号
    private String payMethod;  // 支付方式
    private String rate;  // 品牌发放总奖金扣率
    private String provideAmout;    //品牌发放总奖金

    // 还款订单的属性
    private String repayMerchantNo;     //还款商户ID
    private String repayStatus;     //还款状态
    private String planAmount;     //目标还款金额
    private String actualAmount;     //实际还款金额
    private String completionTime;  // 订单完成时间

    public String getProvideAmout() {
        return provideAmout;
    }

    public void setProvideAmout(String provideAmout) {
        this.provideAmout = provideAmout;
    }

    public String getRepayMerchantNo() {
        return repayMerchantNo;
    }

    public void setRepayMerchantNo(String repayMerchantNo) {
        this.repayMerchantNo = repayMerchantNo;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(String planAmount) {
        this.planAmount = planAmount;
    }

    public String getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(String completionTime) {
        this.completionTime = completionTime;
    }

    public String getReceiveMerchantNo() {
        return receiveMerchantNo;
    }

    public void setReceiveMerchantNo(String receiveMerchantNo) {
        this.receiveMerchantNo = receiveMerchantNo;
    }

    public String getSourceOrderNo() {
        return sourceOrderNo;
    }

    public void setSourceOrderNo(String sourceOrderNo) {
        this.sourceOrderNo = sourceOrderNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOemShare() {
        return oemShare;
    }

    public void setOemShare(String oemShare) {
        this.oemShare = oemShare;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckOper() {
        return checkOper;
    }

    public void setCheckOper(String checkOper) {
        this.checkOper = checkOper;
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

    public String getOemName() {
        return oemName;
    }

    public void setOemName(String oemName) {
        this.oemName = oemName;
    }

    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getReceiveStatus() {
        return receiveStatus;
    }

    public void setReceiveStatus(String receiveStatus) {
        this.receiveStatus = receiveStatus;
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

    public String getCheckTimeStart() {
        return checkTimeStart;
    }

    public void setCheckTimeStart(String checkTimeStart) {
        this.checkTimeStart = checkTimeStart;
    }

    public String getCheckTimeEnd() {
        return checkTimeEnd;
    }

    public void setCheckTimeEnd(String checkTimeEnd) {
        this.checkTimeEnd = checkTimeEnd;
    }
}

