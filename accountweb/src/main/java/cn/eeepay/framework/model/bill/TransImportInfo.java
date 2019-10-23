package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class TransImportInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String fromSerialNo;  //'来源系统流水号:钱包交易流水或刷卡交易流水',
	private Date recordDate; //'账户账务系统记账日期',
	private Date transDate; //'V1系统日期时间',
	private String recordSerialNo;  // '记账流水号',
	private BigDecimal transAmount;// '交易金额',
	private String transType; // '交易类型',
	private String merchantNo; // '商户号',
	private String cardNo; // '交易卡号',
	private String terminalNo; // '终端号',
	private String cardType; //  '卡片类型',
	private String acqMerchantNo;  //  '收单商户号',
	private String reverseFlag; // '冲销交易标志:0-正常交易,1-冲销交易',
	private String reverseStatus; //'冲销状态：0正常, 1:已冲销',
	private String directAgentNo;  //  '商户直属代理商',
	private String deviceSn; //  '机具号',
	private String recordStatus; // '已记账标（已记账，未记账）',
    private BigDecimal merchantFee; //  '商户服务手续费',
	private String bpId; //  '业务产品ID',
	private BigDecimal agentShareAmount; //  '各级代理商分润金额（代理商节点－分润金额）',
	private String fromSystem;  //  '来源系统',
	private Date fromDate; //  '来源系统日期',
	private String merchantRateType; //  '商户费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯扣率 6-每月阶梯扣率',
	private String merchantRate; //  '商户扣率',
	private BigDecimal acqServiceRate; //  '收单服务扣率',
	private BigDecimal acqOrgFee1; //  '收单机构手续费1',
	private String holidaysMark; //  '节假日标志:1-只工作日，2-只节假日，0-不限',
	private String serviceId; //  '服务ID',
	private String hpId; //  '硬件产品ID',
	private String acqOrgId; //  '收单机构ID',
	private String acqServiceId; //  '收单（出款）服务ID',
	private String acqRateType; //  '收单机构服务费率类型1（费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯扣率6-每月阶梯扣率）',
	private String acqRateType2; //  '收单机构服务费率类型2',
	private BigDecimal acqOrgFee2; //  '收单机构手续费2',
	
	private String acqEnname ; //收单机构（交易通道）
	private String outAcqEnname ; //收单机构（出款通道）
	private String outRateType1 ;//'出款服务费率类型1'
	private BigDecimal outRateFee1 ; //'出款服务费1',
	private String outRateType2 ; //'出款服务费率类型2',
	private BigDecimal outRateFee2 ;// '出款服务费2',
	private String outRate1;  //出款服务费率1
	private String outRate2;  //出款服务费率2
	private BigDecimal merchantSettleFee;
	private String oneAgentNo;
	private BigDecimal agentSettleShareAmount;
	private String outServiceId;
	private String reverseTransType;
	private String reverseSerialNo;
	private String transOrderNo;
	private String recordResult;//记账结果
	private BigDecimal money;
	private BigDecimal days;
	private ExtAccountInfo extAccountInfo ;
	
	public ExtAccountInfo getExtAccountInfo() {
		return extAccountInfo;
	}
	public void setExtAccountInfo(ExtAccountInfo extAccountInfo) {
		this.extAccountInfo = extAccountInfo;
	}
	
	public String getReverseSerialNo() {
		return reverseSerialNo;
	}
	public void setReverseSerialNo(String reverseSerialNo) {
		this.reverseSerialNo = reverseSerialNo;
	}
	public String getReverseTransType() {
		return reverseTransType;
	}
	public void setReverseTransType(String reverseTransType) {
		this.reverseTransType = reverseTransType;
	}
	public String getOutServiceId() {
		return outServiceId;
	}
	public void setOutServiceId(String outServiceId) {
		this.outServiceId = outServiceId;
	}
	public BigDecimal getAgentSettleShareAmount() {
		return agentSettleShareAmount;
	}
	public void setAgentSettleShareAmount(BigDecimal agentSettleShareAmount) {
		this.agentSettleShareAmount = agentSettleShareAmount;
	}
	public String getOneAgentNo() {
		return oneAgentNo;
	}
	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}
	public BigDecimal getMerchantSettleFee() {
		return merchantSettleFee;
	}
	public void setMerchantSettleFee(BigDecimal merchantSettleFee) {
		this.merchantSettleFee = merchantSettleFee;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getOutAcqEnname() {
		return outAcqEnname;
	}
	public void setOutAcqEnname(String outAcqEnname) {
		this.outAcqEnname = outAcqEnname;
	}
	public String getOutRateType1() {
		return outRateType1;
	}
	public void setOutRateType1(String outRateType1) {
		this.outRateType1 = outRateType1;
	}

	public String getOutRateType2() {
		return outRateType2;
	}
	public void setOutRateType2(String outRateType2) {
		this.outRateType2 = outRateType2;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFromSerialNo() {
		return fromSerialNo;
	}
	public void setFromSerialNo(String fromSerialNo) {
		this.fromSerialNo = fromSerialNo;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public Date getTransDate() {
		return transDate;
	}
	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}
	public String getRecordSerialNo() {
		return recordSerialNo;
	}
	public void setRecordSerialNo(String recordSerialNo) {
		this.recordSerialNo = recordSerialNo;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getTerminalNo() {
		return terminalNo;
	}
	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}
	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}
	public String getReverseFlag() {
		return reverseFlag;
	}
	public void setReverseFlag(String reverseFlag) {
		this.reverseFlag = reverseFlag;
	}
	public String getDirectAgentNo() {
		return directAgentNo;
	}
	public void setDirectAgentNo(String directAgentNo) {
		this.directAgentNo = directAgentNo;
	}
	public String getDeviceSn() {
		return deviceSn;
	}
	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}
	public String getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	public BigDecimal getMerchantFee() {
		return merchantFee;
	}
	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}
	public String getBpId() {
		return bpId;
	}
	public void setBpId(String bpId) {
		this.bpId = bpId;
	}
	public BigDecimal getAgentShareAmount() {
		return agentShareAmount;
	}
	public void setAgentShareAmount(BigDecimal agentShareAmount) {
		this.agentShareAmount = agentShareAmount;
	}
	public String getFromSystem() {
		return fromSystem;
	}
	public void setFromSystem(String fromSystem) {
		this.fromSystem = fromSystem;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public String getMerchantRateType() {
		return merchantRateType;
	}
	public void setMerchantRateType(String merchantRateType) {
		this.merchantRateType = merchantRateType;
	}
	public String getMerchantRate() {
		return merchantRate;
	}
	public void setMerchantRate(String merchantRate) {
		this.merchantRate = merchantRate;
	}
	public BigDecimal getAcqServiceRate() {
		return acqServiceRate;
	}
	public void setAcqServiceRate(BigDecimal acqServiceRate) {
		this.acqServiceRate = acqServiceRate;
	}
	public BigDecimal getAcqOrgFee1() {
		return acqOrgFee1;
	}
	public void setAcqOrgFee1(BigDecimal acqOrgFee1) {
		this.acqOrgFee1 = acqOrgFee1;
	}
	public String getHolidaysMark() {
		return holidaysMark;
	}
	public void setHolidaysMark(String holidaysMark) {
		this.holidaysMark = holidaysMark;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getHpId() {
		return hpId;
	}
	public void setHpId(String hpId) {
		this.hpId = hpId;
	}
	public String getAcqOrgId() {
		return acqOrgId;
	}
	public void setAcqOrgId(String acqOrgId) {
		this.acqOrgId = acqOrgId;
	}
	public String getAcqServiceId() {
		return acqServiceId;
	}
	public void setAcqServiceId(String acqServiceId) {
		this.acqServiceId = acqServiceId;
	}
	public String getAcqRateType() {
		return acqRateType;
	}
	public void setAcqRateType(String acqRateType) {
		this.acqRateType = acqRateType;
	}
	public String getAcqRateType2() {
		return acqRateType2;
	}
	public void setAcqRateType2(String acqRateType2) {
		this.acqRateType2 = acqRateType2;
	}
	public BigDecimal getAcqOrgFee2() {
		return acqOrgFee2;
	}
	public void setAcqOrgFee2(BigDecimal acqOrgFee2) {
		this.acqOrgFee2 = acqOrgFee2;
	}
	public String getReverseStatus() {
		return reverseStatus;
	}
	public void setReverseStatus(String reverseStatus) {
		this.reverseStatus = reverseStatus;
	}
	public BigDecimal getOutRateFee1() {
		return outRateFee1;
	}
	public void setOutRateFee1(BigDecimal outRateFee1) {
		this.outRateFee1 = outRateFee1;
	}
	public BigDecimal getOutRateFee2() {
		return outRateFee2;
	}
	public void setOutRateFee2(BigDecimal outRateFee2) {
		this.outRateFee2 = outRateFee2;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getDays() {
		return days;
	}
	public void setDays(BigDecimal days) {
		this.days = days;
	}
	public String getOutRate1() {
		return outRate1;
	}
	public void setOutRate1(String outRate1) {
		this.outRate1 = outRate1;
	}
	public String getOutRate2() {
		return outRate2;
	}
	public void setOutRate2(String outRate2) {
		this.outRate2 = outRate2;
	}
	public String getTransOrderNo() {
		return transOrderNo;
	}
	public void setTransOrderNo(String transOrderNo) {
		this.transOrderNo = transOrderNo;
	}
	public String getRecordResult() {
		return recordResult;
	}
	public void setRecordResult(String recordResult) {
		this.recordResult = recordResult;
	}
}
