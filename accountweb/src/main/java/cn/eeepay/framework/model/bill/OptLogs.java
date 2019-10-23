package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class OptLogs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;//序号

	private String checkBatchNo;// 对账批次号
	
	private Integer outBillId;//出账单ID
	
	private String operateContent;// 操作内容
	
	private String operator;//操作人
	
	private Date operateTime;//操作时间
	
	private String logType;//日志类型

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCheckBatchNo() {
		return checkBatchNo;
	}

	public void setCheckBatchNo(String checkBatchNo) {
		this.checkBatchNo = checkBatchNo;
	}

	public Integer getOutBillId() {
		return outBillId;
	}

	public void setOutBillId(Integer outBillId) {
		this.outBillId = outBillId;
	}

	public String getOperateContent() {
		return operateContent;
	}

	public void setOperateContent(String operateContent) {
		this.operateContent = operateContent;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getLogType() {
		return logType;
	}

	public void setLogType(String logType) {
		this.logType = logType;
	}

}
