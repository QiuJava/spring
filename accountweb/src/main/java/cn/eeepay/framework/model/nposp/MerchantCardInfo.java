package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.util.Date;

public class MerchantCardInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String merchantNo; //'商户ID',
	private String cardType; //'类型:借记卡1、贷记卡2',
	private String quickPay; //'是否快捷支付:1-是,2-否',
	private String defQuickPay; //'是否默认快捷支付:1-是,2-否',
	private String defSettleCard; //'是否默认结算卡:1-是,2-否',
	private String accountType; //'账户类型:1-对公,2-对私',
	private String bankName; //'开户行全称',
	private String cnapsNo; //'联行行号',
	private String accountName; //'开户名',
	private String accountNo; //'开户账号',
	private String status; //'状态',
	private Date createTime; // '创建时间',
	
	private String mobilephone;
	private String province;
	private String city;
	
	private String accountProvince; // '开户行地区：省',
	private String accountCity; // '开户行地区：市',
	private String accountDistrict; // '开户行地区：区或县'

	private String merchantName;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
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
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
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
	
}
