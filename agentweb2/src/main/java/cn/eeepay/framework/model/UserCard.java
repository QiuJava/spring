package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户结算银行卡表 user_card
 * 
 * @author Administrator
 *
 */
public class UserCard {
	private Long id;
	private String userCode;
	private Date createDate;
	private String cardNo;// 银行卡号
	private String cnapsNo;
	private String bankName;
	private String bankBranchName;
	private String bankProvince;
	private String bankCity;
	private String bankAdress;
	private String status;
	private String positivePhoto;// 银行卡正面照片

	private String debitCreditSide;// 操作
	private Date recordDate;// 记账日期
	private Date recordTime;// 记账时间
	private BigDecimal controlAmount;// 冻结金额
	private BigDecimal balance;// 可用余额
	private BigDecimal recordAmount;// 冻结金额
	private BigDecimal avaliBalance;// 余额
	private String accountNo;// 账号
	private String accountName;// 姓名
	private String accountIdNo;// 身份证号
	private String accountPhone;// 手机号

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountIdNo() {
		return accountIdNo;
	}

	public void setAccountIdNo(String accountIdNo) {
		this.accountIdNo = accountIdNo;
	}

	public String getAccountPhone() {
		return accountPhone;
	}

	public void setAccountPhone(String accountPhone) {
		this.accountPhone = accountPhone;
	}

	public Date getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCnapsNo() {
		return cnapsNo;
	}

	public void setCnapsNo(String cnapsNo) {
		this.cnapsNo = cnapsNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBranchName() {
		return bankBranchName;
	}

	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankAdress() {
		return bankAdress;
	}

	public void setBankAdress(String bankAdress) {
		this.bankAdress = bankAdress;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPositivePhoto() {
		return positivePhoto;
	}

	public void setPositivePhoto(String positivePhoto) {
		this.positivePhoto = positivePhoto;
	}

	public String getDebitCreditSide() {
		return debitCreditSide;
	}

	public void setDebitCreditSide(String debitCreditSide) {
		this.debitCreditSide = debitCreditSide;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public BigDecimal getControlAmount() {
		return controlAmount;
	}

	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public BigDecimal getRecordAmount() {
		return recordAmount;
	}

	public void setRecordAmount(BigDecimal recordAmount) {
		this.recordAmount = recordAmount;
	}

	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}

	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}

}
