package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;  

/**
 * 外部用户账户交易明细表实体类
 * @author zouruijin
 * 2016年3月4日16:03:24
 *
 */
public class ExtTransInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
    private String accountNo; 
    
    private BigDecimal recordAmount; 

    private BigDecimal balance;    

    private BigDecimal avaliBalance;//可用余额
    
    private BigDecimal controlAmount;//控制金额
    
    private BigDecimal settlingAmount;//结算中金额
    
    private BigDecimal preFreezeAmount;//预冻结金额

    private String serialNo;
  
    private String childSerialNo;
    
    private Date recordDate;
    
    private Time recordTime;
    
    private String debitCreditSide;
    
    private String summaryInfo;
    
    private String transOrderNo;

    private String transType;
    
	private ExtAccount extAccount;
	
	//VO
	 private String balanceAddFrom;//余额增加借贷方向:debit-借方,credit-贷方
    
    public String getTransOrderNo() {
		return transOrderNo;
	}

	public void setTransOrderNo(String transOrderNo) {
		this.transOrderNo = transOrderNo;
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
		this.serialNo = serialNo;
	}

	public String getChildSerialNo() {
		return childSerialNo;
	}

	public void setChildSerialNo(String childSerialNo) {
		this.childSerialNo = childSerialNo;
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
		this.debitCreditSide = debitCreditSide;
	}

	public String getSummaryInfo() {
		return summaryInfo;
	}

	public void setSummaryInfo(String summaryInfo) {
		this.summaryInfo = summaryInfo;
	}

	public ExtAccount getExtAccount() {
		return extAccount;
	}

	public void setExtAccount(ExtAccount extAccount) {
		this.extAccount = extAccount;
	}

	public BigDecimal getControlAmount() {
		return controlAmount;
	}

	public void setControlAmount(BigDecimal controlAmount) {
		this.controlAmount = controlAmount;
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

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getBalanceAddFrom() {
		return balanceAddFrom;
	}

	public void setBalanceAddFrom(String balanceAddFrom) {
		this.balanceAddFrom = balanceAddFrom;
	}
    
}

   