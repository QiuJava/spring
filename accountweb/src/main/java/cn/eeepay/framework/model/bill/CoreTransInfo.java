package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * 交易流水表
 * @author zouruijin
 * 2016年3月29日14:04:20
 *
 */
public class CoreTransInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String accountNo; //账号
    private BigDecimal transAmount; //交易金额
    private String serialNo;//交易流水号
    private String childSerialNo;//子交易流水号
    private String journalNo;//分录号
    private String subjectNo;//科目内部编号
    private String currencyNo;//币种号
    private Date transDate;//交易日期
    private String reverseFlag;//冲销标志:NORMAL正常，REVERSED-已冲销）
    private String accountFlag;//内部账外部账标志:0-外部账号，1-内部账号
    private String debitCreditFlag;//借贷平衡检查标志:0-不平衡，1-平衡
    private String debitCreditSide;//借贷方向:debit-借方,credit-贷方
    private String summaryInfo;//摘要
    private Time transTime ;	//交易时间
    
    private String accountType;//用户类型
	private String userId;//外部用户编号
    private String orgNo;//支付机构号
    
    private Integer importId ; //交易流水导入表的ID
    
    private String subjectName;
    
    ////
    private String transType;
    
    public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Integer getImportId() {
		return importId;
	}
	public void setImportId(Integer importId) {
		this.importId = importId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getOrgNo() {
		return orgNo;
	}
	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
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
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getChildSerialNo() {
		return childSerialNo;
	}
	public void setChildSerialNo(String childSerialNo) {
		this.childSerialNo = childSerialNo;
	}
	public String getJournalNo() {
		return journalNo;
	}
	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
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
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getReverseFlag() {
		return reverseFlag;
	}
	public void setReverseFlag(String reverseFlag) {
		this.reverseFlag = reverseFlag;
	}
	public String getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}
	public String getDebitCreditFlag() {
		return debitCreditFlag;
	}
	public void setDebitCreditFlag(String debitCreditFlag) {
		this.debitCreditFlag = debitCreditFlag;
	}
	public String getDebitCreditSide() {
		return debitCreditSide;
	}
	public void setDebitCreditSide(String debitCreditSide) {
		this.debitCreditSide = debitCreditSide;
	}
	public String getSummaryInfo() {
		return summaryInfo;
	}
	public void setSummaryInfo(String summaryInfo) {
		this.summaryInfo = summaryInfo;
	}
	public Time getTransTime() {
		return transTime;
	}
	public void setTransTime(Time transTime) {
		this.transTime = transTime;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
}

   