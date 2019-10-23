package cn.eeepay.framework.model.redemActive;

import java.math.BigDecimal;

/**
 * Created by 666666 on 2017/10/27.
 */
public class RedemProviderBean {
    private String agentNo;
    private String agentName;
    private String agentLevel;
    private String mobilephone;
    private BigDecimal shareRate;
    private BigDecimal oemFee;
    private String shareRateStr;
    private String oemFeeStr;
    private String parentId;

    private String receiveShareRate;
    private String repaymentShareRate;


    public RedemProviderBean() {
    }

    public RedemProviderBean(String agentNo, BigDecimal shareRate, BigDecimal oemFee) {
        this.agentNo = agentNo;
        this.shareRate = shareRate;
        this.oemFee = oemFee;
    }

    public String getReceiveShareRate() {
        return receiveShareRate;
    }

    public void setReceiveShareRate(String receiveShareRate) {
        this.receiveShareRate = receiveShareRate;
    }

    public String getRepaymentShareRate() {
        return repaymentShareRate;
    }

    public void setRepaymentShareRate(String repaymentShareRate) {
        this.repaymentShareRate = repaymentShareRate;
    }

    public BigDecimal getOemFee() {
        return oemFee;
    }

    public void setOemFee(BigDecimal oemFee) {
        this.oemFee = oemFee;
    }

    public String getOemFeeStr() {
        return oemFeeStr;
    }

    public void setOemFeeStr(String oemFeeStr) {
        this.oemFeeStr = oemFeeStr;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getShareRateStr() {
        return shareRateStr;
    }

    public void setShareRateStr(String shareRateStr) {
        this.shareRateStr = shareRateStr;
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

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public BigDecimal getShareRate() {
        return shareRate;
    }

    public void setShareRate(BigDecimal shareRate) {
        this.shareRate = shareRate;
    }
}
