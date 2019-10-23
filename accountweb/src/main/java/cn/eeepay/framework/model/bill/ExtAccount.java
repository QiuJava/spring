package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 外部账账户表实体类
 * @author zouruijin
 * 2016年3月3日10:01:40
 *
 */
public class ExtAccount implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accountNo;  //外部账号
    
    private String accountName;//账户名称
    
    private String accountType;//账户类型
    
    private String orgNo;   // 机构号

    private String currencyNo;//币种号

    private String subjectNo;//科目内部编号

    private BigDecimal currBalance;//当前余额

    private BigDecimal controlAmount;//控制金额
    
    private BigDecimal settlingAmount;//结算中金额
    
    private BigDecimal settlingHoldAmount;//结算保留金额
    
    private BigDecimal preFreezeAmount;//预冻结金额
    
    private BigDecimal availBalance;//可用余额

    private Date parentTransDay;//上一个交易日

    private BigDecimal parentTransBalance;//上一个交易余额

    private String accountStatus; //账户状态:1-正常，2-销户，3-冻结
    
    private String creator;
    
    private Date createTime;//开户日期时间

    private String balanceAddFrom;//余额增加借贷方向:debit-借方,credit-贷方

    private String balanceFrom;//余额方向：debit-借方,credit-贷方
    
    private String dayBalFlag;//日终修改余额标志:0-日间，1-日终

    private String sumFlag;//汇总入明细标志:0-日间单笔，1-日终单笔，2-日终汇总
    
    private String controlBeginAmount;//冻结金额-前
    
    private String controlEndAmount;//冻结金额-后
    
    private String currBalanceBeginAmount;//余额 -前
    
    private String currBalanceEndAmount;//余额 -后
    
    //2017-03-28 优化新加
    private String accountOwner;
    private String userId; //外部账用户编号
    private String subjectName; //科目名称
    
    private ExtAccountInfo extAccountInfo;//外部用户账户关系表
    private List<ExtTransInfo> extTransInfos;
    private Subject subject;
    


	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public List<ExtTransInfo> getExtTransInfos() {
		return extTransInfos;
	}

	public void setExtTransInfos(List<ExtTransInfo> extTransInfos) {
		this.extTransInfos = extTransInfos;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public Date getParentTransDay() {
		return parentTransDay;
	}

	public void setParentTransDay(Date parentTransDay) {
		this.parentTransDay = parentTransDay;
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

	public ExtAccountInfo getExtAccountInfo() {
		return extAccountInfo;
	}

	public void setExtAccountInfo(ExtAccountInfo extAccountInfo) {
		this.extAccountInfo = extAccountInfo;
	}

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public BigDecimal getAvailBalance() {
		return availBalance;
	}

	public void setAvailBalance(BigDecimal availBalance) {
		this.availBalance = availBalance;
	}

	public BigDecimal getSettlingAmount() {
		return settlingAmount;
	}

	public void setSettlingAmount(BigDecimal settlingAmount) {
		this.settlingAmount = settlingAmount;
	}

	
	public BigDecimal getSettlingHoldAmount() {
		return settlingHoldAmount;
	}

	public void setSettlingHoldAmount(BigDecimal settlingHoldAmount) {
		this.settlingHoldAmount = settlingHoldAmount;
	}

	public BigDecimal getPreFreezeAmount() {
		return preFreezeAmount;
	}

	public void setPreFreezeAmount(BigDecimal preFreezeAmount) {
		this.preFreezeAmount = preFreezeAmount;
	}

	public String getDayBalFlag() {
		return dayBalFlag;
	}

	public void setDayBalFlag(String dayBalFlag) {
		this.dayBalFlag = dayBalFlag;
	}

	public String getSumFlag() {
		return sumFlag;
	}

	public void setSumFlag(String sumFlag) {
		this.sumFlag = sumFlag;
	}

	public String getControlBeginAmount() {
		return controlBeginAmount;
	}

	public void setControlBeginAmount(String controlBeginAmount) {
		this.controlBeginAmount = controlBeginAmount;
	}

	public String getControlEndAmount() {
		return controlEndAmount;
	}

	public void setControlEndAmount(String controlEndAmount) {
		this.controlEndAmount = controlEndAmount;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCurrBalanceBeginAmount() {
		return currBalanceBeginAmount;
	}

	public void setCurrBalanceBeginAmount(String currBalanceBeginAmount) {
		this.currBalanceBeginAmount = currBalanceBeginAmount;
	}

	public String getCurrBalanceEndAmount() {
		return currBalanceEndAmount;
	}

	public void setCurrBalanceEndAmount(String currBalanceEndAmount) {
		this.currBalanceEndAmount = currBalanceEndAmount;
	}
	
	
}