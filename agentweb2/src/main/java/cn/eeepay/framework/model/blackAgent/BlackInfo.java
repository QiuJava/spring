package cn.eeepay.framework.model.blackAgent;

import java.util.Date;

public class BlackInfo {
    private Long id;
    //黑名单源单 编号
    private String orderNo;
    //商户编号
    private String merchantNo;
    //商户名称
    private String merchantName;
    //代理商编号
    private String agentNo;
    //代理商名称
    private String agentName;
    //黑名单内容
    private String blackCreateRemark="商户触犯风控规则，已列入黑名单";
    //商户最后处理状态
    private String merLastDealStatus;
    //风控最终处理状态
    private String riskLastDealStatus;
    private Date createTime;
    //商户最后回复日期
    private Date merLastDealTime;
    //风控最后回复日期
    private Date riskLastDealTime;
    private String createTimeStart;
    private String createTimeEnd;
    //用于判断是否为第一次触犯风控规则
    private Boolean firstRecord;
    private String status;//0 完成 1 正常

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getBlackCreateRemark() {
        return blackCreateRemark;
    }

    public void setBlackCreateRemark(String blackCreateRemark) {
        this.blackCreateRemark = blackCreateRemark;
    }

    public String getMerLastDealStatus() {
        return merLastDealStatus;
    }

    public void setMerLastDealStatus(String merLastDealStatus) {
        this.merLastDealStatus = merLastDealStatus;
    }

    public String getRiskLastDealStatus() {
        return riskLastDealStatus;
    }

    public void setRiskLastDealStatus(String riskLastDealStatus) {
        this.riskLastDealStatus = riskLastDealStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getMerLastDealTime() {
        return merLastDealTime;
    }

    public void setMerLastDealTime(Date merLastDealTime) {
        this.merLastDealTime = merLastDealTime;
    }


    public Date getRiskLastDealTime() {
        return riskLastDealTime;
    }

    public void setRiskLastDealTime(Date riskLastDealTime) {
        this.riskLastDealTime = riskLastDealTime;
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

    public Boolean getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(Boolean firstRecord) {
        this.firstRecord = firstRecord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "BlackInfo{" +
                "id=" + id +
                ", orderNo='" + orderNo + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", agentName='" + agentName + '\'' +
                ", blackCreateRemark='" + blackCreateRemark + '\'' +
                ", merLastDealStatus='" + merLastDealStatus + '\'' +
                ", riskLastDealStatus='" + riskLastDealStatus + '\'' +
                ", createTime=" + createTime +
                ", merLastDealTime=" + merLastDealTime +
                ", riskLastDealTime=" + riskLastDealTime +
                ", createTimeStart='" + createTimeStart + '\'' +
                ", createTimeEnd='" + createTimeEnd + '\'' +
                ", firstRecord=" + firstRecord +
                '}';
    }
}
