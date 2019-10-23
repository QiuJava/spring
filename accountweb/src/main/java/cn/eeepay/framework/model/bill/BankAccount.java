package cn.eeepay.framework.model.bill;

import java.io.Serializable;

/**
 * 银行账户
 * @author Administrator
 *
 */
public class BankAccount implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private String bankName ;		//开户行全称
	private String accountName ;		//开户名
	private String accountNo ;		//开户账户
	private String orgNo ;		//支付机构号
	private String currencyNo ;		//货币种类
	private String accountType ;		//账户类别
	private String subjectNo ;		//科目号
	private String cnapsNo ;		//联行行号
	private String insAccountNo ;		//内部（虚拟）账户
	
	private InsAccount insAccount ;		//内部账号
	
	
	

	public InsAccount getInsAccount() {
		return insAccount;
	}
	public void setInsAccount(InsAccount insAccount) {
		this.insAccount = insAccount;
	}
	public BankAccount(String bankName, String accountName, String accountNo, String orgNo, String currencyNo,
			String accountType, String subjectNo, String cnapsNo) {
		super();
		this.bankName = bankName;
		this.accountName = accountName;
		this.accountNo = accountNo;
		this.orgNo = orgNo;
		this.currencyNo = currencyNo;
		this.accountType = accountType;
		this.subjectNo = subjectNo;
		this.cnapsNo = cnapsNo;
	}
	public BankAccount() {
		super();
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}
	public String getCurrencyNo() {
		return currencyNo;
	}
	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public String getCnapsNo() {
		return cnapsNo;
	}
	public void setCnapsNo(String cnapsNo) {
		this.cnapsNo = cnapsNo;
	}
	public String getInsAccountNo() {
		return insAccountNo;
	}
	public void setInsAccountNo(String insAccountNo) {
		this.insAccountNo = insAccountNo;
	}
	
	
	
}
