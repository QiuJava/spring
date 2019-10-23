package cn.eeepay.framework.model;

/**
 * table  zq_merchant_info
 * desc 直清商户信息表
 */
public class ZQTransOrderInfo {

    private String orderNo;         // 交易订单号
    private String merchantName;    // 商户名称
    private String mobilephone;     // 手机号
    private String unionpayMerNo;   // 银联报备商户号
    private String accountNo;       // 交易卡号
    private String transAmount;     // 交易金额
    private String transType;       // 交易类型
                                    // 消费 PURCHASE, 冲正 REVERSED,
                                    // 消费撤销 PURCHASE_VOID,预授权 PRE_AUTH,
                                    // 预授权撤销 PRE_AUTH_VOID, 预授权完成 PRE_AUTH_COMPLETA,
                                    // 预授权完成撤销 PRE_AUTH_COMPLETE_VOID, 退货 PURCHASE_REFUND,
                                    // 查余额 BALANCE_QUERY,转账 TRANSFER_ACCOUNTS;
    private String transStatus;     // 交易状态
                                    // SUCCESS：成功,FAILED：失败,INIT：
                                    // 初始化,REVERSED：已冲正,REVOKED：
                                    // 已撤销,SETTLE：已结算,OVERLIMIT：已退款,REFUND：
                                    // 失败,COMPLETE：已完成,CLOSED：关闭;
    private String payMethod;
    private String channelCode;     // 通道名称
    private String transTime;       // 交易时间

    private String bpId;            // 商户进件编号
    private String terminalId;      // 终端号
    private String accountName;     // 持卡人姓名
    private String idCardNo;        // 身份证
    private String startTransTime;  // 开始交易时间
    private String endTransTime;    // 结束交易时间

    private String merchantNo;    // 商户编号
    private String bankName;        // 发卡行
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

    public String getUnionpayMerNo() {
        return unionpayMerNo;
    }

    public void setUnionpayMerNo(String unionpayMerNo) {
        this.unionpayMerNo = unionpayMerNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getTransTime() {
        return transTime;
    }

    public void setTransTime(String transTime) {
        this.transTime = transTime;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getStartTransTime() {
        return startTransTime;
    }

    public void setStartTransTime(String startTransTime) {
        this.startTransTime = startTransTime;
    }

    public String getEndTransTime() {
        return endTransTime;
    }

    public void setEndTransTime(String endTransTime) {
        this.endTransTime = endTransTime;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}