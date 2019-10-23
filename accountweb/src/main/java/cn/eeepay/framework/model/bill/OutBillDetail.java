package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutBillDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Integer outBillId;
	private String merchantNo;
	private BigDecimal merchantBalance;
	private BigDecimal outAccountTaskAmount;
	private BigDecimal outAmount;
	private String acqOrgNo;
	private BigDecimal todayBalance;
	private Date createTime;
	private String changeRemark;
	private Integer changeOperatorId;
	private String changeOperatorName;
	private String outBillResult;
	private Integer exportStatus;
	private String exportFileSerial;
	private Date exportDate;
	
	private String verifyFlag;
	private String verifyMsg;
	private Integer outBillStatus;
	private String recordStatus;

	private String orderReferenceNo;
	private Date transTime;

	//下面的7个参数作为商户出账结果查询参数
	private String merchantName;
	private String mobile;
	private String merNos;
	private String outAmount1;
	private String outAmount2;
	private String startTime;
	private String endTime;

    private String plateMerchantEntryNo;
	private String acqMerchantNo;

    public String getPlateMerchantEntryNo() {
        return plateMerchantEntryNo;
    }

    public void setPlateMerchantEntryNo(String plateMerchantEntryNo) {
        this.plateMerchantEntryNo = plateMerchantEntryNo;
    }

    public String getAcqMerchantNo() {
		return acqMerchantNo;
	}

	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}

	public String getOutAmount1() {
		return outAmount1;
	}
	public void setOutAmount1(String outAmount1) {
		this.outAmount1 = outAmount1;
	}
	public String getOutAmount2() {
		return outAmount2;
	}
	public void setOutAmount2(String outAmount2) {
		this.outAmount2 = outAmount2;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getOutBillId() {
		return outBillId;
	}
	public void setOutBillId(Integer outBillId) {
		this.outBillId = outBillId;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	

	public BigDecimal getMerchantBalance() {
		return merchantBalance;
	}
	public void setMerchantBalance(BigDecimal merchantBalance) {
		this.merchantBalance = merchantBalance;
	}
	public BigDecimal getOutAccountTaskAmount() {
		return outAccountTaskAmount;
	}
	public void setOutAccountTaskAmount(BigDecimal outAccountTaskAmount) {
		this.outAccountTaskAmount = outAccountTaskAmount;
	}
	public BigDecimal getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	public String getAcqOrgNo() {
		return acqOrgNo;
	}
	public void setAcqOrgNo(String acqOrgNo) {
		this.acqOrgNo = acqOrgNo;
	}
	public BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getChangeRemark() {
		return changeRemark;
	}
	public void setChangeRemark(String changeRemark) {
		this.changeRemark = changeRemark;
	}
	public Integer getChangeOperatorId() {
		return changeOperatorId;
	}
	public void setChangeOperatorId(Integer changeOperatorId) {
		this.changeOperatorId = changeOperatorId;
	}
	public String getChangeOperatorName() {
		return changeOperatorName;
	}
	public void setChangeOperatorName(String changeOperatorName) {
		this.changeOperatorName = changeOperatorName;
	}
	public String getOutBillResult() {
		return outBillResult;
	}
	public void setOutBillResult(String outBillResult) {
		this.outBillResult = outBillResult;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMerNos() {
		return merNos;
	}
	public void setMerNos(String merNos) {
		this.merNos = merNos;
	}
	public Integer getExportStatus() {
		return exportStatus;
	}
	public void setExportStatus(Integer exportStatus) {
		this.exportStatus = exportStatus;
	}
	public String getExportFileSerial() {
		return exportFileSerial;
	}
	public void setExportFileSerial(String exportFileSerial) {
		this.exportFileSerial = exportFileSerial;
	}
	public Date getExportDate() {
		return exportDate;
	}
	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}
	public String getVerifyFlag() {
		return verifyFlag;
	}
	public void setVerifyFlag(String verifyFlag) {
		this.verifyFlag = verifyFlag;
	}
	public String getVerifyMsg() {
		return verifyMsg;
	}
	public void setVerifyMsg(String verifyMsg) {
		this.verifyMsg = verifyMsg;
	}

	
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public Integer getOutBillStatus() {
		return outBillStatus;
	}
	public void setOutBillStatus(Integer outBillStatus) {
		this.outBillStatus = outBillStatus;
	}

	public String getOrderReferenceNo() {
		return orderReferenceNo;
	}

	public void setOrderReferenceNo(String orderReferenceNo) {
		this.orderReferenceNo = orderReferenceNo;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}
}
