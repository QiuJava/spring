package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ShadowAccount  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountNo;
	private String accountFlag;
	private Date transDate;
	private BigDecimal debitAmount;
	private BigDecimal creditAmount;
	private String bookedFlag;
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getAccountFlag() {
		return accountFlag;
	}
	public void setAccountFlag(String accountFlag) {
		this.accountFlag = accountFlag;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public BigDecimal getDebitAmount() {
		return debitAmount;
	}
	public void setDebitAmount(BigDecimal debitAmount) {
		this.debitAmount = debitAmount;
	}
	public BigDecimal getCreditAmount() {
		return creditAmount;
	}
	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}
	public String getBookedFlag() {
		return bookedFlag;
	}
	public void setBookedFlag(String bookedFlag) {
		this.bookedFlag = bookedFlag;
	}

	
}
