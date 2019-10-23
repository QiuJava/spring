package cn.eeepay.framework.model.bill;

import java.io.Serializable;

/**
 * 外部账账户表实体类
 * @author zouruijin
 * 2016年3月3日10:01:40
 *
 */
public class ExtAccountInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
    private String accountNo; 
    
    private String accountType;
    
    private String userId; //外部账用户编号

    private String accountOwner;    

    private String cardNo;

    private String subjectNo;//科目内部编号
  
    private String currencyNo;
    
    
    private String userName ;//外部账用户名称(从boss拿)
    private String mobilephone ;//外部账用户手机号(从boss拿)
    
    
    private ExtAccount extAccount ;

	public ExtAccount getExtAccount() {
		return extAccount;
	}

	public void setExtAccount(ExtAccount extAccount) {
		this.extAccount = extAccount;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}

	public String getCurrencyNo() {
		return currencyNo;
	}

	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}  


	


}