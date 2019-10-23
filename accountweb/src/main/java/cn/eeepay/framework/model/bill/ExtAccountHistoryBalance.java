package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 外部用户账户历史余额表
 * @author zouruijin
 * 2016年3月3日10:01:40
 *
 */
public class ExtAccountHistoryBalance implements Serializable {
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
    
    private Date createTime ;
    private String balanceAddFrom ;
    private String balanceFrom ;
    private String accountName ;
    private String overdraftFlag ;
    private BigDecimal overdraftAmount ;
    private String day_balFlag ;
    private String sumFlag ;
    private BigDecimal settlingAmount ;
    private BigDecimal preFreezeAmount ;
    
    private OrgInfo orgInfo ;
    private ExtAccountInfo extAccountInfo ;
    
	public OrgInfo getOrgInfo() {
		return orgInfo;
	}
	public void setOrgInfo(OrgInfo orgInfo) {
		this.orgInfo = orgInfo;
	}
	public ExtAccountInfo getExtAccountInfo() {
		return extAccountInfo;
	}
	public void setExtAccountInfo(ExtAccountInfo extAccountInfo) {
		this.extAccountInfo = extAccountInfo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getBalanceAddFrom() {
		return balanceAddFrom;
	}
	public void setBalanceAddFrom(String balanceAddFrom) {
		this.balanceAddFrom = balanceAddFrom;
	}
	public String getBalanceFrom() {
		return balanceFrom;
	}
	public void setBalanceFrom(String balanceFrom) {
		this.balanceFrom = balanceFrom;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getOverdraftFlag() {
		return overdraftFlag;
	}
	public void setOverdraftFlag(String overdraftFlag) {
		this.overdraftFlag = overdraftFlag;
	}
	public BigDecimal getOverdraftAmount() {
		return overdraftAmount;
	}
	public void setOverdraftAmount(BigDecimal overdraftAmount) {
		this.overdraftAmount = overdraftAmount;
	}
	public String getDay_balFlag() {
		return day_balFlag;
	}
	public void setDay_balFlag(String day_balFlag) {
		this.day_balFlag = day_balFlag;
	}
	public String getSumFlag() {
		return sumFlag;
	}
	public void setSumFlag(String sumFlag) {
		this.sumFlag = sumFlag;
	}
	public BigDecimal getSettlingAmount() {
		return settlingAmount;
	}
	public void setSettlingAmount(BigDecimal settlingAmount) {
		this.settlingAmount = settlingAmount;
	}
	public BigDecimal getPreFreezeAmount() {
		return preFreezeAmount;
	}
	public void setPreFreezeAmount(BigDecimal preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
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