package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 外部账账户冻结解冻记录表实体类
 * @author zouruijin
 * 2016年3月3日10:01:40
 *
 */
public class ExtAccountOpRecord implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accountNo;  //外部账号
    private Date recordDate;
    private String serialNo;
    private String operationType;
    private BigDecimal operationBalance;
    
    private String transOrderNo ;
    private String summaryInfo ;
    
	public String getTransOrderNo() {
		return transOrderNo;
	}
	public void setTransOrderNo(String transOrderNo) {
		this.transOrderNo = transOrderNo;
	}
	public String getSummaryInfo() {
		return summaryInfo;
	}
	public void setSummaryInfo(String summaryInfo) {
		this.summaryInfo = summaryInfo;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}
	public BigDecimal getOperationBalance() {
		return operationBalance;
	}
	public void setOperationBalance(BigDecimal operationBalance) {
		this.operationBalance = operationBalance;
	}

}