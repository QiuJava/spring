package cn.eeepay.framework.model.surveyOrder;

import java.util.List;

/**
 * @author MXG
 * create 2018/09/07
 */
public class SurveyOrderInfo {
    private String id;
    private String orderNo;//调单单号
    private String transOrderNo;//交易订单编号
    private String transOrderDatabase;//交易订单所在数据库 now当前库 old历史库
    private String acqReferenceNo;//收单机构参考号
    private String orderTypeCode;//调单类型编号
    private String replyStatus;//回复状态
    private String dealStatus;//处理状态
    private String agentNode;//商户所属代理商编号节点
    private String agentName;//商户所属代理商名称
    private String merchantNo;//商户编号
    private String payMethod;//交易方式
    private String transAccountNo;//交易卡号
    private String transTime;//交易时间
    private String orderServiceCode;//业务类型 对应字典表 1-支付收单 2-超级还
    private String createTime;//创建时间
    private String createTimeStart;
    private String createTimeEnd;
    private String lastUpdateTime;//数据最后更新时间
    private String lastUpdateTimeStart;
    private String lastUpdateTimeEnd;
    private String urgeNum;//催单次数
    private String transAmount;//交易金额
    private String dealRemark;//调单说明
    private String replyEndTime;//回复截止时间
    private String replyEndTimeStart;
    private String replyEndTimeEnd;
    private String orderRemark;//调单备注

    private String transStatus;//交易状态 trans_info
    private String serialNo;//流水号 trans_info

    private String bankName;//发卡行 pos_card_bin
    private String cardType;//卡类型 pos_card_bin

    private String merchantName;//商户名称  merchant_info
    private String mobilephone;//商户手机号 merchant_info

    private String containSubordinate;//是否包含下级 1-是  0-否 默认是
    private String canReply;//1-可以回复 0-不可以回复
    private String canCheck;//1-可审核 0-不可审核
    private String canEdit;//1-可修改 0-不可修改
    private String templateFilesName;
    private List<String> templateList;//模板数组
    private String overdue;//1-逾期  0-未逾期
    private String merTeamId;//商户组织id
    private String merGroup;//商户组织名称

    public String getTransOrderDatabase() {
        return transOrderDatabase;
    }

    public void setTransOrderDatabase(String transOrderDatabase) {
        this.transOrderDatabase = transOrderDatabase;
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

    public String getTransOrderNo() {
        return transOrderNo;
    }

    public void setTransOrderNo(String transOrderNo) {
        this.transOrderNo = transOrderNo;
    }

    public String getAcqReferenceNo() {
        return acqReferenceNo;
    }

    public void setAcqReferenceNo(String acqReferenceNo) {
        this.acqReferenceNo = acqReferenceNo;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getAgentName() {
        return agentName;
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

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getTransAccountNo() {
        return transAccountNo;
    }

    public void setTransAccountNo(String transAccountNo) {
        this.transAccountNo = transAccountNo;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getOrderServiceCode() {
        return orderServiceCode;
    }

    public void setOrderServiceCode(String orderServiceCode) {
        this.orderServiceCode = orderServiceCode;
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

    public String getLastUpdateTimeStart() {
        return lastUpdateTimeStart;
    }

    public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
        this.lastUpdateTimeStart = lastUpdateTimeStart;
    }

    public String getLastUpdateTimeEnd() {
        return lastUpdateTimeEnd;
    }

    public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
        this.lastUpdateTimeEnd = lastUpdateTimeEnd;
    }

    public String getUrgeNum() {
        return urgeNum;
    }

    public void setUrgeNum(String urgeNum) {
        this.urgeNum = urgeNum;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getDealRemark() {
        return dealRemark;
    }

    public void setDealRemark(String dealRemark) {
        this.dealRemark = dealRemark;
    }

    public String getReplyEndTime() {
        return replyEndTime;
    }

    public void setReplyEndTime(String replyEndTime) {
        this.replyEndTime = replyEndTime;
    }

    public String getReplyEndTimeStart() {
        return replyEndTimeStart;
    }

    public void setReplyEndTimeStart(String replyEndTimeStart) {
        this.replyEndTimeStart = replyEndTimeStart;
    }

    public String getReplyEndTimeEnd() {
        return replyEndTimeEnd;
    }

    public void setReplyEndTimeEnd(String replyEndTimeEnd) {
        this.replyEndTimeEnd = replyEndTimeEnd;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
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

    public String getContainSubordinate() {
        return containSubordinate;
    }

    public void setContainSubordinate(String containSubordinate) {
        this.containSubordinate = containSubordinate;
    }

    public String getCanReply() {
        return canReply;
    }

    public void setCanReply(String canReply) {
        this.canReply = canReply;
    }

    public String getCanCheck() {
        return canCheck;
    }

    public void setCanCheck(String canCheck) {
        this.canCheck = canCheck;
    }

    public String getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(String canEdit) {
        this.canEdit = canEdit;
    }

    public String getTemplateFilesName() {
        return templateFilesName;
    }

    public void setTemplateFilesName(String templateFilesName) {
        this.templateFilesName = templateFilesName;
    }

    public List<String> getTemplateList() {
        return templateList;
    }

    public void setTemplateList(List<String> templateList) {
        this.templateList = templateList;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public String getMerTeamId() {
        return merTeamId;
    }

    public void setMerTeamId(String merTeamId) {
        this.merTeamId = merTeamId;
    }

    public String getMerGroup() {
        return merGroup;
    }

    public void setMerGroup(String merGroup) {
        this.merGroup = merGroup;
    }
}
