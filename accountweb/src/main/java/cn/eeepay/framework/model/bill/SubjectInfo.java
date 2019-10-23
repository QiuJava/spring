package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 科目动态信息表
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月27日11:42:47
 *
 */
public class SubjectInfo   implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String subjectNo; //科目内部编号

    private String orgNo; //机构号

    private String currencyNo; //币种号

    private Date createDate; 

    private Integer subjectLevel; //科目级别

    private String balanceFrom; //余额方向：debit-借方,credit-贷方

    private BigDecimal todayBalance; //本日日终余额

    private BigDecimal todayDebitAmount; //本日借方发生额

    private BigDecimal todayCreditAmount; //本日贷方发生额

    private BigDecimal yesterdayAmount; //昨日日终余额
    
    private Subject subject ;

	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}

	public String getSubjectNo() {
		return subjectNo;
	}

	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
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

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getSubjectLevel() {
		return subjectLevel;
	}

	public void setSubjectLevel(Integer subjectLevel) {
		this.subjectLevel = subjectLevel;
	}

	public String getBalanceFrom() {
		return balanceFrom;
	}

	public void setBalanceFrom(String balanceFrom) {
		this.balanceFrom = balanceFrom;
	}

	public BigDecimal getTodayBalance() {
		return todayBalance;
	}

	public void setTodayBalance(BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
	}

	public BigDecimal getTodayDebitAmount() {
		return todayDebitAmount;
	}

	public void setTodayDebitAmount(BigDecimal todayDebitAmount) {
		this.todayDebitAmount = todayDebitAmount;
	}

	public BigDecimal getTodayCreditAmount() {
		return todayCreditAmount;
	}

	public void setTodayCreditAmount(BigDecimal todayCreditAmount) {
		this.todayCreditAmount = todayCreditAmount;
	}

	public BigDecimal getYesterdayAmount() {
		return yesterdayAmount;
	}

	public void setYesterdayAmount(BigDecimal yesterdayAmount) {
		this.yesterdayAmount = yesterdayAmount;
	}


    
}