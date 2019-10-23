package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;

public class RecordAccountRuleConfig implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ruleConfigId ;
	private String ruleNo ;
	private String journalNo ;
	private String childTransNo ;
	private String accountFlag ;
	private String debitCreditSide ;
	private String debitCreditFlag ;
	private String subjectNo ;
	private String remark ;
	
	private String accountType ;
	private String currencyNo ;
	private String amount ;
	private String subjectName ;
	
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getCurrencyNo() {
		return currencyNo;
	}
	public void setCurrencyNo(String currencyNo) {
		this.currencyNo = currencyNo;
	}
	public Integer getRuleConfigId() {
		return ruleConfigId;
	}
	public void setRuleConfigId(Integer ruleConfigId) {
		this.ruleConfigId = ruleConfigId;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getJournalNo() {
		return journalNo;
	}
	public void setJournalNo(String journalNo) {
		this.journalNo = journalNo;
	}
	public String getChildTransNo() {
		return childTransNo;
	}
	public void setChildTransNo(String childTransNo) {
		this.childTransNo = childTransNo;
	}
	public String getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}
	public String getDebitCreditSide() {
		return debitCreditSide;
	}
	public void setDebitCreditSide(String debitCreditSide) {
		this.debitCreditSide = debitCreditSide;
	}
	public String getDebitCreditFlag() {
		return debitCreditFlag;
	}
	public void setDebitCreditFlag(String debitCreditFlag) {
		this.debitCreditFlag = debitCreditFlag;
	}
	public String getSubjectNo() {
		return subjectNo;
	}
	public void setSubjectNo(String subjectNo) {
		this.subjectNo = subjectNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return "RecordAccountRuleConfig [ruleConfigId=" + ruleConfigId + ", ruleNo=" + ruleNo + ", journalNo="
				+ journalNo + ", childTransNo=" + childTransNo + ", accountFlag=" + accountFlag + ", debitCreditSide="
				+ debitCreditSide + ", debitCreditFlag=" + debitCreditFlag + ", subjectNo=" + subjectNo + ", remark="
				+ remark + ", accountType=" + accountType + ", currencyNo=" + currencyNo + ", amount=" + amount + "]";
	}
	
	
	
}
