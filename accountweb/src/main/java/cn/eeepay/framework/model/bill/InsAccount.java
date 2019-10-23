package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部账账户表实体类
 * @author Administrator
 *
 */
public class InsAccount implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String accountNo;  //内部账号

    private String orgNo;   // 机构号

    private String currencyNo;//币种号

    private String subjectNo;//科目内部编号
   
    private Subject subject;//科目
    
    private String accountName;//账户名称

    private BigDecimal currBalance;//当前余额

    private BigDecimal controlAmount;//控制金额
    
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
    
    
    public BigDecimal getAvailBalance() {
		return availBalance;
	}


	public void setAvailBalance(BigDecimal availBalance) {
		this.availBalance = availBalance;
	}

    public String getAccountNo() {
        return accountNo;
    }

   
	public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo == null ? null : orgNo.trim();
    }

    public String getCurrencyNo() {
        return currencyNo;
    }

    public void setCurrencyNo(String currencyNo) {
        this.currencyNo = currencyNo == null ? null : currencyNo.trim();
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo == null ? null : subjectNo.trim();
    }

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
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
        this.accountStatus = accountStatus == null ? null : accountStatus.trim();
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
        this.balanceAddFrom = balanceAddFrom == null ? null : balanceAddFrom.trim();
    }

    public String getBalanceFrom() {
        return balanceFrom;
    }

    public void setBalanceFrom(String balanceFrom) {
        this.balanceFrom = balanceFrom == null ? null : balanceFrom.trim();
    }

    public String getDayBalFlag() {
        return dayBalFlag;
    }

    public void setDayBalFlag(String dayBalFlag) {
        this.dayBalFlag = dayBalFlag == null ? null : dayBalFlag.trim();
    }

    public String getSumFlag() {
        return sumFlag;
    }

    public void setSumFlag(String sumFlag) {
        this.sumFlag = sumFlag == null ? null : sumFlag.trim();
    }


	public String getCreator() {
		return creator;
	}


	public void setCreator(String creator) {
		this.creator = creator;
	}
    
    
}