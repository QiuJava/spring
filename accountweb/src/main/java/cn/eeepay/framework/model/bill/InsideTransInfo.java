package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class InsideTransInfo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String accountNo;

    private BigDecimal recordAmount;

    private BigDecimal balance;

    private BigDecimal avaliBalance;

    private String serialNo;

    private String childSerialNo;

    private Date recordDate;

    private Time recordTime;

    private String debitCreditSide;

    private String summaryInfo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public BigDecimal getRecordAmount() {
        return recordAmount;
    }

    public void setRecordAmount(BigDecimal recordAmount) {
        this.recordAmount = recordAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAvaliBalance() {
        return avaliBalance;
    }

    public void setAvaliBalance(BigDecimal avaliBalance) {
        this.avaliBalance = avaliBalance;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getChildSerialNo() {
        return childSerialNo;
    }

    public void setChildSerialNo(String childSerialNo) {
        this.childSerialNo = childSerialNo == null ? null : childSerialNo.trim();
    }

	@JsonSerialize(using=CustomDateSerializer.class)  
    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    

  
	public Time getRecordTime() {
		return recordTime;
	}

	public void setRecordTime(Time recordTime) {
		this.recordTime = recordTime;
	}

	public String getDebitCreditSide() {
        return debitCreditSide;
    }

    public void setDebitCreditSide(String debitCreditSide) {
        this.debitCreditSide = debitCreditSide == null ? null : debitCreditSide.trim();
    }

    public String getSummaryInfo() {
        return summaryInfo;
    }

    public void setSummaryInfo(String summaryInfo) {
        this.summaryInfo = summaryInfo == null ? null : summaryInfo.trim();
    }
}