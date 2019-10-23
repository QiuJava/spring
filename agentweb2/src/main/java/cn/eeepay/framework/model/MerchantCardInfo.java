package cn.eeepay.framework.model;

import java.util.Date;

/**
 * table merchant_card_info
 * desc 商户银行卡表
 */
public class MerchantCardInfo {
    private Long id;

    private String merchantId;

    private String cardType;

    private String quickPay;

    private String defQuickPay;

    private String defSettleCard;

    private String accountType;

    private String bankName;

    private String cnapsNo;

    private String accountName;

    private String accountNo;

    private String status;

    private Date createTime;
    
    private String accountProvince;
    private String accountCity;
    private String accountDistrict;
    private String accountArea;//开户行地区
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getQuickPay() {
		return quickPay;
	}
	public void setQuickPay(String quickPay) {
		this.quickPay = quickPay;
	}
	public String getDefQuickPay() {
		return defQuickPay;
	}
	public void setDefQuickPay(String defQuickPay) {
		this.defQuickPay = defQuickPay;
	}
	public String getDefSettleCard() {
		return defSettleCard;
	}
	public void setDefSettleCard(String defSettleCard) {
		this.defSettleCard = defSettleCard;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getCnapsNo() {
		return cnapsNo;
	}
	public void setCnapsNo(String cnapsNo) {
		this.cnapsNo = cnapsNo;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getAccountDistrict() {
		return accountDistrict;
	}
	public void setAccountDistrict(String accountDistrict) {
		this.accountDistrict = accountDistrict;
	}
	public String getAccountArea() {
		return accountArea;
	}
	public void setAccountArea(String accountArea) {
		this.accountArea = accountArea;
	}
    


}