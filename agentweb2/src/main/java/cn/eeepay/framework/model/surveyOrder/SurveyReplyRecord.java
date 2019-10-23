package cn.eeepay.framework.model.surveyOrder;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * 调单回复
 * @author MXG
 * create 2018/09/11
 */
public class SurveyReplyRecord {
    private String id;
    private String orderNo;//调单单号
    private String agentNode;//代理商节点编号
    private String replyRoleType;//回复的角色类型
    private String replyRoleNo;//回复的角色编号 商户编号/代理商编号
    private String replyResult;//回复结果
    private String merName;//商户名称
    private String merMobile;//商户电话
    private String cardPersonName;//持卡人姓名
    private String cardPersonMobile;//持卡人电话
    private String realName;//商户真实名称
    private String province;//归属省
    private String city;//归属市
    private String transAddress;//真实交易地址
    private String replyFilesName;//回复文件名称
    private String replyRemark;//回复说明
    private String createTime;//创建时间
    private String lastUpdateTime;//最后更新时间
    private List<String> fileList;
    private String lastDealStatus;
    private String lastDealRemark;//备注说明

    public String getLastDealStatus() {
        return lastDealStatus;
    }

    public void setLastDealStatus(String lastDealStatus) {
        this.lastDealStatus = lastDealStatus;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public void setFileList(List<String> fileList) {
        this.fileList = fileList;
    }

    public String getReplyRoleNo() {
        return replyRoleNo;
    }

    public void setReplyRoleNo(String replyRoleNo) {
        this.replyRoleNo = replyRoleNo;
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

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getReplyRoleType() {
        return replyRoleType;
    }

    public void setReplyRoleType(String replyRoleType) {
        this.replyRoleType = replyRoleType;
    }

    public String getReplyResult() {
        return replyResult;
    }

    public void setReplyResult(String replyResult) {
        this.replyResult = replyResult;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerMobile() {
        return merMobile;
    }

    public void setMerMobile(String merMobile) {
        this.merMobile = merMobile;
    }

    public String getCardPersonName() {
        return cardPersonName;
    }

    public void setCardPersonName(String cardPersonName) {
        this.cardPersonName = cardPersonName;
    }

    public String getCardPersonMobile() {
        return cardPersonMobile;
    }

    public void setCardPersonMobile(String cardPersonMobile) {
        this.cardPersonMobile = cardPersonMobile;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTransAddress() {
        return transAddress;
    }

    public void setTransAddress(String transAddress) {
        this.transAddress = transAddress;
    }

    public String getReplyFilesName() {
        return replyFilesName;
    }

    public void setReplyFilesName(String replyFilesName) {
        this.replyFilesName = replyFilesName;
    }

    public String getReplyRemark() {
        return replyRemark;
    }

    public void setReplyRemark(String replyRemark) {
        this.replyRemark = replyRemark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getLastDealRemark() {
        return lastDealRemark;
    }

    public void setLastDealRemark(String lastDealRemark) {
        this.lastDealRemark = lastDealRemark;
    }
}
