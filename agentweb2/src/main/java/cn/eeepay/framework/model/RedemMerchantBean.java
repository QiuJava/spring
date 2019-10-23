package cn.eeepay.framework.model;

/**
 * Created by 666666 on 2018/5/9.
 */
public class RedemMerchantBean {


    private String merchantNo;
    private String merNode;
    private String oemNo;
    private String accountName;
    private String userName;
    private String mobileUsername;
    private String merCapa;
    private String merAccount;
    private String createTime;
    private String creTimeStart;
    private String creTimeEnd;
    private int accountType;
    private String accountNo;
    private String bankName;
    private String accountProvince;
    private String accountCity;
    private String zhName;
    private long cnapsNo;
    private String ordparProShare;
    private String goldparProShare;
    private String diamparProShare;
    private String email;
    private String phone;
    private String address;
    private String linkName;
    private String saleName;
    private String agentArea;
    private String province;
    private String city;
    private String area;
    private boolean directMerchant;   // 是否为直推商户
    private boolean notOpenAgent;       // 是否未开通代理商
    private String parMerNo;        // 上级商户
    private String agentNo;
    private String agentNode;

    private String oemFee;//oem分润成本
    private String agentFee;//代理商分润成本

    public String getMerNode() {
        return merNode;
    }

    public void setMerNode(String merNode) {
        this.merNode = merNode;
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

    public String getParMerNo() {
        return parMerNo;
    }

    public void setParMerNo(String parMerNo) {
        this.parMerNo = parMerNo;
    }

    public boolean isDirectMerchant() {
        return directMerchant;
    }

    public void setDirectMerchant(boolean directMerchant) {
        this.directMerchant = directMerchant;
    }

    public boolean isNotOpenAgent() {
        return notOpenAgent;
    }

    public void setNotOpenAgent(boolean notOpenAgent) {
        this.notOpenAgent = notOpenAgent;
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

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getAgentArea() {
        return agentArea;
    }

    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileUsername() {
        return mobileUsername;
    }

    public void setMobileUsername(String mobileUsername) {
        this.mobileUsername = mobileUsername;
    }

    public String getMerCapa() {
        return merCapa;
    }

    public void setMerCapa(String merCapa) {
        this.merCapa = merCapa;
    }

    public String getMerAccount() {
        return merAccount;
    }

    public void setMerAccount(String merAccount) {
        this.merAccount = merAccount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreTimeStart() {
        return creTimeStart;
    }

    public void setCreTimeStart(String creTimeStart) {
        this.creTimeStart = creTimeStart;
    }

    public String getCreTimeEnd() {
        return creTimeEnd;
    }

    public void setCreTimeEnd(String creTimeEnd) {
        this.creTimeEnd = creTimeEnd;
    }


    public String getOemNo() {
        return oemNo;
    }

    public void setOemNo(String oemNo) {
        this.oemNo = oemNo;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountProvince() {
        return accountProvince;
    }

    public void setAccountProvince(String accountProvince) {
        this.accountProvince = accountProvince;
    }

    public String getAccountCity() {
        return accountCity;
    }

    public void setAccountCity(String accountCity) {
        this.accountCity = accountCity;
    }

    public String getZhName() {
        return zhName;
    }

    public void setZhName(String zhName) {
        this.zhName = zhName;
    }

    public long getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(long cnapsNo) {
        this.cnapsNo = cnapsNo;
    }

    public String getOrdparProShare() {
        return ordparProShare;
    }

    public void setOrdparProShare(String ordparProShare) {
        this.ordparProShare = ordparProShare;
    }

    public String getGoldparProShare() {
        return goldparProShare;
    }

    public void setGoldparProShare(String goldparProShare) {
        this.goldparProShare = goldparProShare;
    }

    public String getDiamparProShare() {
        return diamparProShare;
    }

    public void setDiamparProShare(String diamparProShare) {
        this.diamparProShare = diamparProShare;
    }

    public String getOemFee() {
        return oemFee;
    }

    public void setOemFee(String oemFee) {
        this.oemFee = oemFee;
    }

    public String getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(String agentFee) {
        this.agentFee = agentFee;
    }

    @Override
    public String toString() {
        return "RedemMerchantBean{" +
                "merchantNo='" + merchantNo + '\'' +
                ", merNode='" + merNode + '\'' +
                ", oemNo='" + oemNo + '\'' +
                ", accountName='" + accountName + '\'' +
                ", userName='" + userName + '\'' +
                ", mobileUsername='" + mobileUsername + '\'' +
                ", merCapa='" + merCapa + '\'' +
                ", merAccount='" + merAccount + '\'' +
                ", createTime='" + createTime + '\'' +
                ", creTimeStart='" + creTimeStart + '\'' +
                ", creTimeEnd='" + creTimeEnd + '\'' +
                ", accountType=" + accountType +
                ", accountNo='" + accountNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountProvince='" + accountProvince + '\'' +
                ", accountCity='" + accountCity + '\'' +
                ", zhName='" + zhName + '\'' +
                ", cnapsNo=" + cnapsNo +
                ", ordparProShare='" + ordparProShare + '\'' +
                ", goldparProShare='" + goldparProShare + '\'' +
                ", diamparProShare='" + diamparProShare + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", linkName='" + linkName + '\'' +
                ", saleName='" + saleName + '\'' +
                ", agentArea='" + agentArea + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", area='" + area + '\'' +
                ", directMerchant=" + directMerchant +
                ", notOpenAgent=" + notOpenAgent +
                ", parMerNo='" + parMerNo + '\'' +
                ", agentNo='" + agentNo + '\'' +
                ", agentNode='" + agentNode + '\'' +
                ", oemFee='" + oemFee + '\'' +
                ", agentFee='" + agentFee + '\'' +
                '}';
    }
}
