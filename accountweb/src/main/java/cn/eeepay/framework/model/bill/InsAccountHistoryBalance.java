package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部帐账户历史余额表
 * @author zouruijin
 * 2016年3月3日10:01:40
 *
 */
public class InsAccountHistoryBalance implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date billDate; 
    private String accountNo; 
    private String orgNo;
    private String currencyNo;
    private String subjectNo;//科目内部编号
    private BigDecimal currBalance; 
    private BigDecimal controlAmount; 
    private BigDecimal parentTransBalance;    
    private String accountStatus;
    private String accountName ;
    
    public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	private OrgInfo orgInfo ;
    
	public OrgInfo getOrgInfo() {
		return orgInfo;
	}
	public void setOrgInfo(OrgInfo orgInfo) {
		this.orgInfo = orgInfo;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getBillDate() {
		return billDate;
	}
	public void setBillDate(Date billDate) {
		this.billDate = billDate;
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
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public BigDecimal getCurrBalance() {
		return currBalance;
	}
	public void setCurrBalance(BigDecimal currBalance) {
		this.currBalance = currBalance;
	}
	public BigDecimal getControlAmount() {
		return controlAmount;
	}
	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
	}
	public BigDecimal getParentTransBalance() {
		return parentTransBalance;
	}
	public void setParentTransBalance(BigDecimal parentTransBalance) {
		this.parentTransBalance = parentTransBalance;
	}
	public String getAccountStatus() {
		return accountStatus;
	}
	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}

}