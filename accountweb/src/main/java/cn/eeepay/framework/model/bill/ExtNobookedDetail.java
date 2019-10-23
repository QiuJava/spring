package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 外部账未入账流水表
 * @author hj
 * 2016年7月5日  下午2:52:31
 */
public class ExtNobookedDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String accountNo; 
    private BigDecimal recordAmount; 
    private String serialNo;
    private String childSerialNo;
    private Date recordDate;
    private String debitCreditSide; 
    private String bookedFlag; 
    private String summaryInfo;
	
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
	public BigDecimal getRecordAmount() {
		return recordAmount;
	}
	public void setRecordAmount(BigDecimal recordAmount) {
		this.recordAmount = recordAmount;
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
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
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