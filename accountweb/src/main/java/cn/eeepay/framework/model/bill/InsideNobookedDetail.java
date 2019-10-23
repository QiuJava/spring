package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 内部账未入账流水表
 * @author Administrator
 *
 */
public class InsideNobookedDetail implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountNo;  //内部账号
    private BigDecimal transAmount;   // 记账金额
    private String serialNo;//交易流水号
    private String childSerialNo;//子交易流水号
    private Date transDate;//交易日期
    private String debitCreditSide;//借贷方向:debit-借方,credit-贷方
	private String bookedFlag;//入账标志:0-未入账，1-入账
	private String summaryInfo;
	
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
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getDebitCreditSide() {
		return debitCreditSide;
	}
	public void setDebitCreditSide(String debitCreditSide) {
		this.debitCreditSide = debitCreditSide;
	}
	public String getBookedFlag() {
		return bookedFlag;
	}
	public void setBookedFlag(String bookedFlag) {
		this.bookedFlag = bookedFlag;
	}
	public String getSummaryInfo() {
		return summaryInfo;
	}
	public void setSummaryInfo(String summaryInfo) {
		this.summaryInfo = summaryInfo;
	}
}