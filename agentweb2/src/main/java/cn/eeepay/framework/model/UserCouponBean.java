package cn.eeepay.framework.model;

/**
 * Created by Administrator on 2017/6/20.
 */
public class UserCouponBean {

    private long id;
    private String couponNo;       //优惠券编号
    private String faceValue;       //优惠券面值
    private String balance;         //优惠券可用金额
    private String usedMoney;       // 已经使用的金额 usedMoney = faceValue - balance
    private String couponCode;     //优惠券类型(详见字典表 COUPON_CODE)
    private String cancelVerificationCode;    //核销方式(详见字典表CANCEL_VERIFICATION_CODE)
    private String couponStatus;       //优惠券状态(详见字典表)
    private String startTime;      //优惠券开始时间
    private String endTime;            //优惠券结束时间
    private String token;               //唯一标识
    private String merchantNo;         //商户编号
    private String mobilephone;         // 商户手机号
    private String merchantName;        // 商户名称
    private String agentName;           // 商户所属代理商名称
    private String agentNo;           // 商户所属代理商编号
    private String overdueMoney;      // 过期余额
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getFaceValue() {
        return faceValue;
    }

    public void setFaceValue(String faceValue) {
        this.faceValue = faceValue;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCancelVerificationCode() {
        return cancelVerificationCode;
    }

    public void setCancelVerificationCode(String cancelVerificationCode) {
        this.cancelVerificationCode = cancelVerificationCode;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getUsedMoney() {
        return usedMoney;
    }

    public void setUsedMoney(String usedMoney) {
        this.usedMoney = usedMoney;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getOverdueMoney() {
        return overdueMoney;
    }

    public void setOverdueMoney(String overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    @Override
    public String toString() {
        return "UserCouponBean{" +
                "id=" + id +
                ", couponNo='" + couponNo + '\'' +
                ", faceValue='" + faceValue + '\'' +
                ", balance='" + balance + '\'' +
                ", usedMoney='" + usedMoney + '\'' +
                ", couponCode='" + couponCode + '\'' +
                ", cancelVerificationCode='" + cancelVerificationCode + '\'' +
                ", couponStatus='" + couponStatus + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", token='" + token + '\'' +
                ", merchantNo='" + merchantNo + '\'' +
                ", mobilephone='" + mobilephone + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", agentName='" + agentName + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", overdueMoney='" + overdueMoney + '\'' +
                '}';
    }
}
