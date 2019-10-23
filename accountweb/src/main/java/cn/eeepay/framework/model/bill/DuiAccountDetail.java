package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DuiAccountDetail implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String acqMerchantOrderNo;
	
	//国彩新加三个字段
	private String acqOrderNo;//收单机构订单号
	private String acqTransOrderNo;//收单机构交易订单号
	private String acqTransType;//收单机构交易类型
	
	private String orderReferenceNo;//订单参考号
	private String plateAccountCardNo;//平台交易卡号
	private String acqAuthNo;//收单机构授权码
	
	private String acqTransSerialNo;//收单机构交易流水号
	
	private String acqMerchantNo;//收单机构商户号
	
	private String acqMerchantName;//收单机构商户名称
	
	private String acqTerminalNo;//收单机构终端号
	
	private String accessOrgNo;//接入机构编号
	
	private String accessOrgName;//接入机构名称
	
	private String acqBatchNo;//收单机构批次号
	
	private String acqSerialNo;//收单机构流水号
	
	private String acqAccountNo;//收单机构卡号
	
	private String acqCardSequenceNo;//收单机构卡序列号
	
	private Date acqTransTime;//收单机构交易时间
	
	private String acqReferenceNo;//收单机构系统参考号
	
	private Date acqSettleDate;//收单机构入账日期
	
	private String acqTransCode;//收单机构交易吗
	
	private String acqTransStatus;//收单机构交易状态
	
	private BigDecimal acqTransAmount;//收单机构交易金额
	
	private BigDecimal acqRefundAmount;//收单机构退货金额
	
	private Date acqCheckDate;//收单机构对账日期
	
	private String acqOriTransSerialNo;//收单机构原交易流水号
	
	private String acqEnname;//收单机构英文名称

	private  Long plateTransId;//平台交易id
	
	private String plateAcqMerchantNo;//平台收单机构商户号
	
	private String plateAcqTerminalNo;//平台收单机构终端号
	
	
	private String plateMerchantNo;//平台商户号
	
	private String plateTerminalNo;//平台终端号
	
	private String plateAcqBatchNo;//平台收单机构批次号
	
	private String plateAcqSerialNo;//平台收单机构流水号
	
	private String plateBatchNo;//平台批次号
	
	private String plateSerialNo;//平台流水号
	
	private String plateAccountNo;//平台交易账号
	
	private BigDecimal plateTransAmount;//平台交易金额
	
	private BigDecimal plateAcqMerchantFee;//平台收单机构商户手续费
	
	private BigDecimal plateMerchantFee;//平台商户手续费
	
	private BigDecimal plateAgentFee;//平台代理商手续费
	
	private String plateAcqMerchantRate;//收单机构商户扣率
	
	private String plateMerchantRate;//商户扣率
	
	private String plateAcqReferenceNo;//平台收单机构系统参考号
	
	private Date plateAcqTransTime;//平台收单机构交易时间
	
	private Date plateMerchantSettleDate;//平台商户结算日期

	private String plateTransType;//平台交易类型
	
	private String plateTransStatus;//平台交易状态
	
	private String checkAccountStatus;//对账状态
	
	private String checkBatchNo;//对账批次号
	
	private String mendBatchNo;//补单批次号
	
	private String description;//描述
	
	private Date createTime;//创建时间
	
	private String plateAgentNo;//平台代理商编号
	
	private String plateTransSource;//平台交易渠道
	
	private String mySettle;//是否优质商户
	
	private String bagSettle;//是否钱包
	
	private String posType;//设备类型
	
	private String errorHandleStatus;//对账差错处理状态
	
	private String errorHandleCreator;//对账差错处理人
	
	private String errorMsg;//失败原因
	
	private String riqieMatch;//自定义字段，日切匹配
	
	private Integer recordStatus;
	
	private BigDecimal plateAgentShareAmount ;//代理商分润金额
	
	private String remark;
	
	private String settlementMethod;   //'结算方式 0 t0 ,1 t1'
	private Integer settleStatus;     //'结算状态1：已结算  0或空：未结算   2.结算中   3.结算失败  4.转T1结算'
	private Integer account;   //'0:未记账，1:记账成功，2:记账失败'
	
	private String freezeStatus; //冻结标识
	
	private String isAddBill;//是否加入出账单
	
	private String outBillStatus;//出账状态

	private BigDecimal taskAmount;  //出账任务金额
	
	private String outAccountBillMethod;  //出账方式
	
	private String settleType;  //出账类型
	
	private Date plateOrderTime; //平台订单时间
	private String plateOrderNo; // 平台订单号
	private String plateCardNo; // 平台交易卡号
	
	private String plateMerchantEntryNo;//商户进件号

	//vo
	private String plateAcqTransTime1;
	private String plateAcqTransTime2;
	private String acqTransTime1;
	private String acqTransTime2;
	private String merchantBlack;		//商户黑名单提示


	private String dbCutFlag;//单边日切标识
	private String treatmentMethod;//处理方式
	private String checkStatus;//初审状态
	private String checkNo;//初审编号
	private String errorCheckCreator;//初审人
	private Date errorTime;//差错处理时间
	private String checkRemark;//初审备注

	private BigDecimal deductionFee;  //抵扣交易手续费
	private BigDecimal actualFee;  //出账任务金额
	private String couponNos;//抵扣券编号
	private String merFee2;//商户手续费（抵扣金额+商户实际手续费）

	private String payMethod;

	private BigDecimal quickRate;		//云闪付费率
	private BigDecimal quickFee;		//云闪付金额

	private BigDecimal merchantPrice;		//自选商户手续费
	private BigDecimal deductionMerFee;	//抵扣自选商户手续费

	private BigDecimal actualMerFee;	//实际自选商户手续费 实际自选商户手费=自选商户手续费-抵扣自选商户手续费


	public BigDecimal getActualMerFee() {
		return actualMerFee;
	}

	public void setActualMerFee(BigDecimal actualMerFee) {
		this.actualMerFee = actualMerFee;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getPlateAcqTransTime1() {
        return plateAcqTransTime1;
    }

    public void setPlateAcqTransTime1(String plateAcqTransTime1) {
        this.plateAcqTransTime1 = plateAcqTransTime1;
    }

    public String getPlateAcqTransTime2() {
        return plateAcqTransTime2;
    }

    public void setPlateAcqTransTime2(String plateAcqTransTime2) {
        this.plateAcqTransTime2 = plateAcqTransTime2;
    }

    public String getAcqTransTime1() {
        return acqTransTime1;
    }

    public void setAcqTransTime1(String acqTransTime1) {
        this.acqTransTime1 = acqTransTime1;
    }

    public String getAcqTransTime2() {
        return acqTransTime2;
    }

    public void setAcqTransTime2(String acqTransTime2) {
        this.acqTransTime2 = acqTransTime2;
    }

    public String getDbCutFlag() {
        return dbCutFlag;
    }

    public void setDbCutFlag(String dbCutFlag) {
        this.dbCutFlag = dbCutFlag;
    }

    public String getMerFee2() {
		return merFee2;
	}

	public void setMerFee2(String merFee2) {
		this.merFee2 = merFee2;
	}

	public BigDecimal getDeductionFee() {
		return deductionFee;
	}

	public void setDeductionFee(BigDecimal deductionFee) {
		this.deductionFee = deductionFee;
	}

	public BigDecimal getActualFee() {
		return actualFee;
	}

	public void setActualFee(BigDecimal actualFee) {
		this.actualFee = actualFee;
	}

	public String getCouponNos() {
		return couponNos;
	}

	public void setCouponNos(String couponNos) {
		this.couponNos = couponNos;
	}

	public String getCheckRemark() {
		return checkRemark;
	}

	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}

	public String getErrorCheckCreator() {
		return errorCheckCreator;
	}

	public void setErrorCheckCreator(String errorCheckCreator) {
		this.errorCheckCreator = errorCheckCreator;
	}

	public Date getErrorTime() {
		return errorTime;
	}

	public void setErrorTime(Date errorTime) {
		this.errorTime = errorTime;
	}

	public String getCheckNo() {return checkNo;}

	public void setCheckNo(String checkNo) {this.checkNo = checkNo;}

	public String getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
	}

	public String getTreatmentMethod() {return treatmentMethod;}

	public void setTreatmentMethod(String treatmentMethod) {
		this.treatmentMethod = treatmentMethod;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public String getIsAddBill() {
		return isAddBill;
	}

	public void setIsAddBill(String isAddBill) {
		this.isAddBill = isAddBill;
	}

	public String getAcqAuthNo() {
		return acqAuthNo;
	}

	public void setAcqAuthNo(String acqAuthNo) {
		this.acqAuthNo = acqAuthNo;
	}

	public String getOrderReferenceNo() {
		return orderReferenceNo;
	}

	public void setOrderReferenceNo(String orderReferenceNo) {
		this.orderReferenceNo = orderReferenceNo;
	}

	public String getAcqOrderNo() {
		return acqOrderNo;
	}

	public void setAcqOrderNo(String acqOrderNo) {
		this.acqOrderNo = acqOrderNo;
	}

	public String getAcqTransOrderNo() {
		return acqTransOrderNo;
	}

	public void setAcqTransOrderNo(String acqTransOrderNo) {
		this.acqTransOrderNo = acqTransOrderNo;
	}

	public String getAcqTransType() {
		return acqTransType;
	}

	public void setAcqTransType(String acqTransType) {
		this.acqTransType = acqTransType;
	}

	public String getAcqMerchantOrderNo() {
		return acqMerchantOrderNo;
	}

	public void setAcqMerchantOrderNo(String acqMerchantOrderNo) {
		this.acqMerchantOrderNo = acqMerchantOrderNo;
	}

	public BigDecimal getTaskAmount() {
		return taskAmount;
	}

	public void setTaskAmount(BigDecimal taskAmount) {
		this.taskAmount = taskAmount;
	}

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public BigDecimal getPlateAgentShareAmount() {
		return plateAgentShareAmount;
	}

	public void setPlateAgentShareAmount(BigDecimal plateAgentShareAmount) {
		this.plateAgentShareAmount = plateAgentShareAmount;
	}

	public String getErrorHandleStatus() {
		return errorHandleStatus;
	}

	public void setErrorHandleStatus(String errorHandleStatus) {
		this.errorHandleStatus = errorHandleStatus;
	}

	public String getPlateTransSource() {
		return plateTransSource;
	}

	public void setPlateTransSource(String plateTransSource) {
		this.plateTransSource = plateTransSource;
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

	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}

	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}

	public String getAcqMerchantName() {
		return acqMerchantName;
	}

	public void setAcqMerchantName(String acqMerchantName) {
		this.acqMerchantName = acqMerchantName;
	}

	public String getAcqTerminalNo() {
		return acqTerminalNo;
	}

	public void setAcqTerminalNo(String acqTerminalNo) {
		this.acqTerminalNo = acqTerminalNo;
	}

	public String getAccessOrgNo() {
		return accessOrgNo;
	}

	public void setAccessOrgNo(String accessOrgNo) {
		this.accessOrgNo = accessOrgNo;
	}

	public String getAccessOrgName() {
		return accessOrgName;
	}

	public void setAccessOrgName(String accessOrgName) {
		this.accessOrgName = accessOrgName;
	}

	public String getAcqBatchNo() {
		return acqBatchNo;
	}

	public void setAcqBatchNo(String acqBatchNo) {
		this.acqBatchNo = acqBatchNo;
	}

	public String getAcqSerialNo() {
		return acqSerialNo;
	}

	public void setAcqSerialNo(String acqSerialNo) {
		this.acqSerialNo = acqSerialNo;
	}

	public String getAcqAccountNo() {
		return acqAccountNo;
	}

	public void setAcqAccountNo(String acqAccountNo) {
		this.acqAccountNo = acqAccountNo;
	}

	public String getAcqCardSequenceNo() {
		return acqCardSequenceNo;
	}

	public void setAcqCardSequenceNo(String acqCardSequenceNo) {
		this.acqCardSequenceNo = acqCardSequenceNo;
	}

	public String getAcqReferenceNo() {
		return acqReferenceNo;
	}

	public void setAcqReferenceNo(String acqReferenceNo) {
		this.acqReferenceNo = acqReferenceNo;
	}

	public String getAcqTransCode() {
		return acqTransCode;
	}

	public void setAcqTransCode(String acqTransCode) {
		this.acqTransCode = acqTransCode;
	}

	public String getAcqTransStatus() {
		return acqTransStatus;
	}

	public void setAcqTransStatus(String acqTransStatus) {
		this.acqTransStatus = acqTransStatus;
	}


	public String getAcqOriTransSerialNo() {
		return acqOriTransSerialNo;
	}

	public void setAcqOriTransSerialNo(String acqOriTransSerialNo) {
		this.acqOriTransSerialNo = acqOriTransSerialNo;
	}

	public Date getAcqTransTime() {
		return acqTransTime;
	}

	public void setAcqTransTime(Date acqTransTime) {
		this.acqTransTime = acqTransTime;
	}

	public Date getAcqSettleDate() {
		return acqSettleDate;
	}

	public void setAcqSettleDate(Date acqSettleDate) {
		this.acqSettleDate = acqSettleDate;
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

	public String getPlateAcqMerchantNo() {
		return plateAcqMerchantNo;
	}

	public void setPlateAcqMerchantNo(String plateAcqMerchantNo) {
		this.plateAcqMerchantNo = plateAcqMerchantNo;
	}

	public String getPlateAcqTerminalNo() {
		return plateAcqTerminalNo;
	}

	public void setPlateAcqTerminalNo(String plateAcqTerminalNo) {
		this.plateAcqTerminalNo = plateAcqTerminalNo;
	}

	public String getPlateMerchantNo() {
		return plateMerchantNo;
	}

	public void setPlateMerchantNo(String plateMerchantNo) {
		this.plateMerchantNo = plateMerchantNo;
	}

	public String getPlateTerminalNo() {
		return plateTerminalNo;
	}

	public void setPlateTerminalNo(String plateTerminalNo) {
		this.plateTerminalNo = plateTerminalNo;
	}

	public String getPlateAcqBatchNo() {
		return plateAcqBatchNo;
	}

	public void setPlateAcqBatchNo(String plateAcqBatchNo) {
		this.plateAcqBatchNo = plateAcqBatchNo;
	}

	public String getPlateAcqSerialNo() {
		return plateAcqSerialNo;
	}

	public void setPlateAcqSerialNo(String plateAcqSerialNo) {
		this.plateAcqSerialNo = plateAcqSerialNo;
	}

	public String getPlateBatchNo() {
		return plateBatchNo;
	}

	public void setPlateBatchNo(String plateBatchNo) {
		this.plateBatchNo = plateBatchNo;
	}

	public String getPlateSerialNo() {
		return plateSerialNo;
	}

	public void setPlateSerialNo(String plateSerialNo) {
		this.plateSerialNo = plateSerialNo;
	}

	public String getPlateAccountNo() {
		return plateAccountNo;
	}

	public void setPlateAccountNo(String plateAccountNo) {
		this.plateAccountNo = plateAccountNo;
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

	public String getPlateAcqMerchantRate() {
		return plateAcqMerchantRate;
	}

	public void setPlateAcqMerchantRate(String plateAcqMerchantRate) {
		this.plateAcqMerchantRate = plateAcqMerchantRate;
	}

	public String getPlateMerchantRate() {
		return plateMerchantRate;
	}

	public void setPlateMerchantRate(String plateMerchantRate) {
		this.plateMerchantRate = plateMerchantRate;
	}

	public String getPlateAcqReferenceNo() {
		return plateAcqReferenceNo;
	}

	public void setPlateAcqReferenceNo(String plateAcqReferenceNo) {
		this.plateAcqReferenceNo = plateAcqReferenceNo;
	}

	public Date getPlateAcqTransTime() {
		return plateAcqTransTime;
	}

	public void setPlateAcqTransTime(Date plateAcqTransTime) {
		this.plateAcqTransTime = plateAcqTransTime;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPlateMerchantSettleDate() {
		return plateMerchantSettleDate;
	}

	public void setPlateMerchantSettleDate(Date plateMerchantSettleDate) {
		this.plateMerchantSettleDate = plateMerchantSettleDate;
	}

	public String getMendBatchNo() {
		return mendBatchNo;
	}

	public void setMendBatchNo(String mendBatchNo) {
		this.mendBatchNo = mendBatchNo;
	}

	public BigDecimal getPlateAgentFee() {
		return plateAgentFee;
	}

	public void setPlateAgentFee(BigDecimal plateAgentFee) {
		this.plateAgentFee = plateAgentFee;
	}

	public String getPlateAgentNo() {
		return plateAgentNo;
	}

	public void setPlateAgentNo(String plateAgentNo) {
		this.plateAgentNo = plateAgentNo;
	}

	public String getMySettle() {
		return mySettle;
	}

	public void setMySettle(String mySettle) {
		this.mySettle = mySettle;
	}


	public Long getPlateTransId() {
		return plateTransId;
	}

	public void setPlateTransId(Long plateTransId) {
		this.plateTransId = plateTransId;
	}

	public String getBagSettle() {
		return bagSettle;
	}

	public void setBagSettle(String bagSettle) {
		this.bagSettle = bagSettle;
	}

	public String getPosType() {
		return posType;
	}

	public void setPosType(String posType) {
		this.posType = posType;
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

	public String getRiqieMatch() {
		return riqieMatch;
	}

	public void setRiqieMatch(String riqieMatch) {
		this.riqieMatch = riqieMatch;
	}

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public String getPlateAccountCardNo() {
		return plateAccountCardNo;
	}

	public void setPlateAccountCardNo(String plateAccountCardNo) {
		this.plateAccountCardNo = plateAccountCardNo;
	}

	public String getOutBillStatus() {
		return outBillStatus;
	}

	public void setOutBillStatus(String outBillStatus) {
		this.outBillStatus = outBillStatus;
	}

	public String getOutAccountBillMethod() {
		return outAccountBillMethod;
	}

	public void setOutAccountBillMethod(String outAccountBillMethod) {
		this.outAccountBillMethod = outAccountBillMethod;
	}

	public Date getPlateOrderTime() {
		return plateOrderTime;
	}

	public void setPlateOrderTime(Date plateOrderTime) {
		this.plateOrderTime = plateOrderTime;
	}

	public String getPlateOrderNo() {
		return plateOrderNo;
	}

	public void setPlateOrderNo(String plateOrderNo) {
		this.plateOrderNo = plateOrderNo;
	}

	public String getPlateCardNo() {
		return plateCardNo;
	}

	public void setPlateCardNo(String plateCardNo) {
		this.plateCardNo = plateCardNo;
	}

	public String getFreezeStatus() {
		return freezeStatus;
	}

	public void setFreezeStatus(String freezeStatus) {
		this.freezeStatus = freezeStatus;
	}

	public String getPlateMerchantEntryNo() {
		return plateMerchantEntryNo;
	}

	public void setPlateMerchantEntryNo(String plateMerchantEntryNo) {
		this.plateMerchantEntryNo = plateMerchantEntryNo;
	}

	public BigDecimal getQuickRate() {
		return quickRate;
	}

	public void setQuickRate(BigDecimal quickRate) {
		this.quickRate = quickRate;
	}

	public BigDecimal getQuickFee() {
		return quickFee;
	}

	public void setQuickFee(BigDecimal quickFee) {
		this.quickFee = quickFee;
	}

	public BigDecimal getMerchantPrice() {
		return merchantPrice;
	}

	public void setMerchantPrice(BigDecimal merchantPrice) {
		this.merchantPrice = merchantPrice;
	}

	public BigDecimal getDeductionMerFee() {
		return deductionMerFee;
	}

	public void setDeductionMerFee(BigDecimal deductionMerFee) {
		this.deductionMerFee = deductionMerFee;
	}

	public String getMerchantBlack() {
		return merchantBlack;
	}

	public void setMerchantBlack(String merchantBlack) {
		this.merchantBlack = merchantBlack;
	}

	@Override
	public String toString() {
		return "DuiAccountDetail [id=" + id + ", acqMerchantOrderNo=" + acqMerchantOrderNo + ", acqOrderNo="
				+ acqOrderNo + ", acqTransOrderNo=" + acqTransOrderNo + ", acqTransType=" + acqTransType
				+ ", orderReferenceNo=" + orderReferenceNo + ", plateAccountCardNo=" + plateAccountCardNo
				+ ", acqAuthNo=" + acqAuthNo + ", acqTransSerialNo=" + acqTransSerialNo + ", acqMerchantNo="
				+ acqMerchantNo + ", acqMerchantName=" + acqMerchantName + ", acqTerminalNo=" + acqTerminalNo
				+ ", accessOrgNo=" + accessOrgNo + ", accessOrgName=" + accessOrgName + ", acqBatchNo=" + acqBatchNo
				+ ", acqSerialNo=" + acqSerialNo + ", acqAccountNo=" + acqAccountNo + ", acqCardSequenceNo="
				+ acqCardSequenceNo + ", acqTransTime=" + acqTransTime + ", acqReferenceNo=" + acqReferenceNo
				+ ", acqSettleDate=" + acqSettleDate + ", acqTransCode=" + acqTransCode + ", acqTransStatus="
				+ acqTransStatus + ", acqTransAmount=" + acqTransAmount + ", acqRefundAmount=" + acqRefundAmount
				+ ", acqCheckDate=" + acqCheckDate + ", acqOriTransSerialNo=" + acqOriTransSerialNo + ", acqEnname="
				+ acqEnname + ", plateTransId=" + plateTransId + ", plateAcqMerchantNo=" + plateAcqMerchantNo
				+ ", plateAcqTerminalNo=" + plateAcqTerminalNo + ", plateMerchantNo=" + plateMerchantNo
				+ ", plateTerminalNo=" + plateTerminalNo + ", plateAcqBatchNo=" + plateAcqBatchNo
				+ ", plateAcqSerialNo=" + plateAcqSerialNo + ", plateBatchNo=" + plateBatchNo + ", plateSerialNo="
				+ plateSerialNo + ", plateAccountNo=" + plateAccountNo + ", plateTransAmount=" + plateTransAmount
				+ ", plateAcqMerchantFee=" + plateAcqMerchantFee + ", plateMerchantFee=" + plateMerchantFee
				+ ", plateAgentFee=" + plateAgentFee + ", plateAcqMerchantRate=" + plateAcqMerchantRate
				+ ", plateMerchantRate=" + plateMerchantRate + ", plateAcqReferenceNo=" + plateAcqReferenceNo
				+ ", plateAcqTransTime=" + plateAcqTransTime + ", plateMerchantSettleDate=" + plateMerchantSettleDate
				+ ", plateTransType=" + plateTransType + ", plateTransStatus=" + plateTransStatus
				+ ", checkAccountStatus=" + checkAccountStatus + ", checkBatchNo=" + checkBatchNo + ", mendBatchNo="
				+ mendBatchNo + ", description=" + description + ", createTime=" + createTime + ", plateAgentNo="
				+ plateAgentNo + ", plateTransSource=" + plateTransSource + ", mySettle=" + mySettle + ", bagSettle="
				+ bagSettle + ", posType=" + posType + ", errorHandleStatus=" + errorHandleStatus
				+ ", errorHandleCreator=" + errorHandleCreator + ", errorMsg=" + errorMsg + ", riqieMatch=" + riqieMatch
				+ ", recordStatus=" + recordStatus + ", plateAgentShareAmount=" + plateAgentShareAmount + ", remark="
				+ remark + ", settlementMethod=" + settlementMethod + ", settleStatus=" + settleStatus + ", account="
				+ account + ", isAddBill=" + isAddBill + ", outBillStatus=" + outBillStatus + ", taskAmount="
				+ taskAmount + ", outAccountBillMethod=" + outAccountBillMethod + ", plateOrderTime=" + plateOrderTime
				+ ", plateOrderNo=" + plateOrderNo + ", plateCardNo=" + plateCardNo + "]";
	}


	
}
