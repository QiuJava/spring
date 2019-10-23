package cn.eeepay.framework.model.blackAgent;

public class RiskNewAnswer {
    //风控处理单号
    private String orderNo;
    //黑名单源单 单号
    private String origOrderNo;
    //触犯风控模板编号
    private String riskDealTemplateNo;
    //风控回复信息
    private String riskDealMsg;
    private String createTime;
    //代理商编号
    private String oneAgentNo;
    //代理商名称
    private String oneAgentName;
    //商户编号
    private String merchantNo;
    //商户名称
    private String merchantName;
    //用于判断是否初次触犯风控原则
    private Boolean firstRecord;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrigOrderNo() {
        return origOrderNo;
    }

    public void setOrigOrderNo(String origOrderNo) {
        this.origOrderNo = origOrderNo;
    }

    public String getRiskDealTemplateNo() {
        return riskDealTemplateNo;
    }

    public void setRiskDealTemplateNo(String riskDealTemplateNo) {
        this.riskDealTemplateNo = riskDealTemplateNo;
    }

    public String getRiskDealMsg() {
        return riskDealMsg;
    }

    public void setRiskDealMsg(String riskDealMsg) {
        this.riskDealMsg = riskDealMsg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
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

    public Boolean getFirstRecord() {
        return firstRecord;
    }

    public void setFirstRecord(Boolean firstRecord) {
        this.firstRecord = firstRecord;
    }

    @Override
    public String toString() {
        return "RiskNewAnswer{" +
                "orderNo='" + orderNo + '\'' +
                ", origOrderNo='" + origOrderNo + '\'' +
                ", riskDealTemplateNo='" + riskDealTemplateNo + '\'' +
                ", riskDealMsg='" + riskDealMsg + '\'' +
                ", createTime='" + createTime + '\'' +
                ", oneAgentNo='" + oneAgentNo + '\'' +
                ", oneAgentName='" + oneAgentName + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", firstRecord=" + firstRecord +
                '}';
    }
}
