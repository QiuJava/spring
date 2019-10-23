package cn.eeepay.framework.model.bill;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 快捷对账信息
 * @author YT
 *
 */
public class FastCheckAccDetail {
	
	private Long id;
	private String checkBatchNo;//对账批次号
	private String acqTransSerialNo;//收单机构交易流水号
	private String acqTransType;//交易类型
	
	private String acqOrderNo;//收单机构订单号
	
	private String acqTransOrderNo;//收单机构交易订单号
	
	private Date acqOrderTime;//收单机构下单时间
	
	private String acqOrderStatus;//收单机构订单状态
	
	private BigDecimal acqTransAmount;//收单机构交易金额
	
	private BigDecimal acqRefundAmount;//收单机构佣金金额
	
	private Date acqCheckDate;//收单机构对账日期
	
	private String acqEnname;//收单机构英文名称
	
	private String plateOrderNo;//平台订单号
	
	private String plateCardNo;//平台交易卡号
	
	private String plateAgentNo;//平台代理商编号
	
	private String plateMerchantNo;//平台商户号
	
	private String plateAcqSerialNo;//平台收单机构流水号
	
	private BigDecimal plateTransAmount;//平台交易金额
	
	private BigDecimal plateAcqMerchantFee;//平台收单机构商户手续费
	
	private BigDecimal plateMerchantFee;//平台商户手续费
	
	private BigDecimal plateAgentFee;//平台代理商手续费
	
	private Date plateMerchantSettleDate;//平台商户结算日期
	
	private Date plateOrderTime;//平台收单机构交易时间
	
	private Date platePayTime;//平台收单机构支付时间
	
	private String plateTransType;//平台交易类型
	
	private String plateTransStatus;//平台交易状态
	
	private String plateTransId;
	
	private String checkAccountStatus;//对账状态
	
	private String errorHandleStatus;//对账差错处理状态
	
	private String errorHandleCreator;//对账差错处理人
	
	private String errorMsg;//失败原因
	
	private Date createTime;//创建时间
	
	private Integer recordStatus;
	private String remark;
	
	private String settlementMethod;   //'结算方式 0 t0 ,1 t1'
	private Integer settleStatus;     //'结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败  4.转T1结算'
	private Integer account;   //'0:未记账，1:记账成功，2:记账失败'

	private BigDecimal taskAmount;  //出账任务金额

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}

	public Integer getSettleStatus() {
		return settleStatus;
	}

	public void setSettleStatus(Integer settleStatus) {
		this.settleStatus = settleStatus;
	}

	public Integer getAccount() {
		return account;
	}

	public void setAccount(Integer account) {
		this.account = account;
	}

	public BigDecimal getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(BigDecimal taskAmount) {
		this.taskAmount = taskAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAcqTransSerialNo() {
		return acqTransSerialNo;
	}

	public void setAcqTransSerialNo(String acqTransSerialNo) {
		this.acqTransSerialNo = acqTransSerialNo;
	}


	public String getPlateCardNo() {
		return plateCardNo;
	}

	public void setPlateCardNo(String plateCardNo) {
		this.plateCardNo = plateCardNo;
	}

	public String getAcqOrderNo() {
		return acqOrderNo;
	}

	public void setAcqOrderNo(String acqOrderNo) {
		this.acqOrderNo = acqOrderNo;
	}

	public Date getAcqOrderTime() {
		return acqOrderTime;
	}

	public void setAcqOrderTime(Date acqOrderTime) {
		this.acqOrderTime = acqOrderTime;
	}

	public String getAcqOrderStatus() {
		return acqOrderStatus;
	}

	public void setAcqOrderStatus(String acqOrderStatus) {
		this.acqOrderStatus = acqOrderStatus;
	}

	public BigDecimal getAcqTransAmount() {
		return acqTransAmount;
	}

	public void setAcqTransAmount(BigDecimal acqTransAmount) {
		this.acqTransAmount = acqTransAmount;
	}

	public BigDecimal getAcqRefundAmount() {
		return acqRefundAmount;
	}

	public void setAcqRefundAmount(BigDecimal acqRefundAmount) {
		this.acqRefundAmount = acqRefundAmount;
	}

	public Date getAcqCheckDate() {
		return acqCheckDate;
	}

	public void setAcqCheckDate(Date acqCheckDate) {
		this.acqCheckDate = acqCheckDate;
	}

	public String getAcqEnname() {
		return acqEnname;
	}

	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}

	

	public String getPlateAgentNo() {
		return plateAgentNo;
	}

	public void setPlateAgentNo(String plateAgentNo) {
		this.plateAgentNo = plateAgentNo;
	}

	public String getPlateMerchantNo() {
		return plateMerchantNo;
	}

	public void setPlateMerchantNo(String plateMerchantNo) {
		this.plateMerchantNo = plateMerchantNo;
	}

	public String getPlateAcqSerialNo() {
		return plateAcqSerialNo;
	}

	public void setPlateAcqSerialNo(String plateAcqSerialNo) {
		this.plateAcqSerialNo = plateAcqSerialNo;
	}

	public BigDecimal getPlateTransAmount() {
		return plateTransAmount;
	}

	public void setPlateTransAmount(BigDecimal plateTransAmount) {
		this.plateTransAmount = plateTransAmount;
	}

	public BigDecimal getPlateAcqMerchantFee() {
		return plateAcqMerchantFee;
	}

	public void setPlateAcqMerchantFee(BigDecimal plateAcqMerchantFee) {
		this.plateAcqMerchantFee = plateAcqMerchantFee;
	}

	public BigDecimal getPlateMerchantFee() {
		return plateMerchantFee;
	}

	public void setPlateMerchantFee(BigDecimal plateMerchantFee) {
		this.plateMerchantFee = plateMerchantFee;
	}

	public BigDecimal getPlateAgentFee() {
		return plateAgentFee;
	}

	public void setPlateAgentFee(BigDecimal plateAgentFee) {
		this.plateAgentFee = plateAgentFee;
	}

	public Date getPlateMerchantSettleDate() {
		return plateMerchantSettleDate;
	}

	public void setPlateMerchantSettleDate(Date plateMerchantSettleDate) {
		this.plateMerchantSettleDate = plateMerchantSettleDate;
	}

	

	public String getAcqTransType() {
		return acqTransType;
	}

	public void setAcqTransType(String acqTransType) {
		this.acqTransType = acqTransType;
	}

	public Date getPlateOrderTime() {
		return plateOrderTime;
	}

	public void setPlateOrderTime(Date plateOrderTime) {
		this.plateOrderTime = plateOrderTime;
	}

	public Date getPlatePayTime() {
		return platePayTime;
	}

	public void setPlatePayTime(Date platePayTime) {
		this.platePayTime = platePayTime;
	}

	public String getPlateTransType() {
		return plateTransType;
	}

	public void setPlateTransType(String plateTransType) {
		this.plateTransType = plateTransType;
	}

	public String getPlateTransStatus() {
		return plateTransStatus;
	}

	public void setPlateTransStatus(String plateTransStatus) {
		this.plateTransStatus = plateTransStatus;
	}

	public String getCheckAccountStatus() {
		return checkAccountStatus;
	}

	public void setCheckAccountStatus(String checkAccountStatus) {
		this.checkAccountStatus = checkAccountStatus;
	}

	public String getCheckBatchNo() {
		return checkBatchNo;
	}

	public void setCheckBatchNo(String checkBatchNo) {
		this.checkBatchNo = checkBatchNo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPlateOrderNo() {
		return plateOrderNo;
	}

	public void setPlateOrderNo(String plateOrderNo) {
		this.plateOrderNo = plateOrderNo;
	}

	public String getPlateTransId() {
		return plateTransId;
	}

	public void setPlateTransId(String plateTransId) {
		this.plateTransId = plateTransId;
	}

	public String getAcqTransOrderNo() {
		return acqTransOrderNo;
	}

	public void setAcqTransOrderNo(String acqTransOrderNo) {
		this.acqTransOrderNo = acqTransOrderNo;
	}

	public String getErrorHandleStatus() {
		return errorHandleStatus;
	}

	public void setErrorHandleStatus(String errorHandleStatus) {
		this.errorHandleStatus = errorHandleStatus;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorHandleCreator() {
		return errorHandleCreator;
	}

	public void setErrorHandleCreator(String errorHandleCreator) {
		this.errorHandleCreator = errorHandleCreator;
	}


}
