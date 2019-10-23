package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class SubOutBillDetailLogs implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;// 主键
	private String subOutBillDetailId;//子出账单明细id
	private String outBillDetailId;// 出账单明细编号
	private Integer outBillId;// 出账单ID
	private Date transTime;// 交易时间
	private Date settleTime;// 结算时间
	private BigDecimal transAmount;// 交易金额
	private String orderReferenceNo;// 订单参考号
	private String outAccountNote;// 出账备注

	private String recordStatus;// 记账状态
	private Integer outBillStatus;// 出账结果
	private String verifyFlag;// 校验通过
	private String verifyMsg;// 校验错误信息
	private String acqEnname;// 收单机构
	private String acqOrgNo;// 收单机构编号
	private String merchantNo;// 商户编号
	private BigDecimal merchantBalance;// 商户结算中金额
	private BigDecimal outAccountTaskAmount;// 商户出账金额
	private String changeRemark;// 财务调账备注
	private Integer changeOperatorId;// 操作id
	private String changeOperatorName;// 操作人

	private Date createTime;// 创建时间

	// 下面的7个参数作为商户出账结果查询参数
	private String merchantName;
	private String mobile;
	private String merNos;
	private String outAmount1;
	private String outAmount2;
	private String startTime;
	private String endTime;
	
	private String transTimeStart;
	private String transTimesEnd;
	
	

	public Date getSettleTime() {
		return settleTime;
	}

	public void setSettleTime(Date settleTime) {
		this.settleTime = settleTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubOutBillDetailId() {
		return subOutBillDetailId;
	}

	public void setSubOutBillDetailId(String subOutBillDetailId) {
		this.subOutBillDetailId = subOutBillDetailId;
	}

	public String getTransTimeStart() {
		return transTimeStart;
	}

	public void setTransTimeStart(String transTimeStart) {
		this.transTimeStart = transTimeStart;
	}

	public String getTransTimesEnd() {
		return transTimesEnd;
	}

	public void setTransTimesEnd(String transTimesEnd) {
		this.transTimesEnd = transTimesEnd;
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

	public String getChangeRemark() {
		return changeRemark;
	}

	public void setChangeRemark(String changeRemark) {
		this.changeRemark = changeRemark;
	}

	public String getAcqOrgNo() {
		return acqOrgNo;
	}

	public void setAcqOrgNo(String acqOrgNo) {
		this.acqOrgNo = acqOrgNo;
	}


	public String getOutBillDetailId() {
		return outBillDetailId;
	}

	public void setOutBillDetailId(String outBillDetailId) {
		this.outBillDetailId = outBillDetailId;
	}

	public Integer getOutBillId() {
		return outBillId;
	}

	public void setOutBillId(Integer outBillId) {
		this.outBillId = outBillId;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getOrderReferenceNo() {
		return orderReferenceNo;
	}

	public void setOrderReferenceNo(String orderReferenceNo) {
		this.orderReferenceNo = orderReferenceNo;
	}

	public String getOutAccountNote() {
		return outAccountNote;
	}

	public void setOutAccountNote(String outAccountNote) {
		this.outAccountNote = outAccountNote;
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

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

}
