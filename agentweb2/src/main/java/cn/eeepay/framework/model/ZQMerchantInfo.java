package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table  zq_merchant_info
 * desc 直清商户信息表
 */
public class ZQMerchantInfo {

    private String unionpayMerNo;   // 银联报备商户号
    private String bpId;            // 商户进件编号
    private String merchantNo;      // 商户编号
    private String syncStatus;      // 同步状态
    private String syncRemark;      // 同步备注
    private String mobilephone;     // 手机号
    private String merchantName;    // 商户名称
    private String createTime;      // 创建时间
    private String idCardNo;        // 身份证号
    private String terminalId;      // 终端id
    private String idCardName;      // 持卡人姓名
    private String startCreateTime; // 开始创建时间
    private String endCreateTime;   // 开始创建时间

    public String getUnionpayMerNo() {
        return unionpayMerNo;
    }

    public void setUnionpayMerNo(String unionpayMerNo) {
        this.unionpayMerNo = unionpayMerNo;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(String syncStatus) {
        this.syncStatus = syncStatus;
    }

    public String getSyncRemark() {
        return syncRemark;
    }

    public void setSyncRemark(String syncRemark) {
        this.syncRemark = syncRemark;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

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
}