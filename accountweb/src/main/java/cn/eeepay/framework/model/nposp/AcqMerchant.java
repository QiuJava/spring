package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcqMerchant implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer   acqOrgId; // '收单机构ID',
	private Integer   acqServiceId; // '收单服务ID',
	private String  acqMerchantNo; // '收单机构商户号',
	private String  acqMerchantName; // '收单机构商户名称',
	private String  merchantNo; // '收单商户对应的普通商户',
	private String  agentNo; // '所属代理商',
	private String  mcc; // '行业信息码',
	private Integer   largeSmallFlag; // '大套小标志(是否A类)',
	private Integer   rateType; // '费率类型:1-每笔固定金额，2-扣率，3-扣率带保底封顶，4-扣率+固定金额,5-单笔阶梯 扣率 6-每月阶梯扣率',
	private BigDecimal singleAmount; // '单笔固定金额',
	private BigDecimal rate; // '单笔扣率',
	private BigDecimal capping; // '封顶金额',
	private BigDecimal ladderRate; // '单笔阶梯扣率',
	private BigDecimal ladderAmount; // '阶梯金额',
	private BigDecimal quota; // '限额',
	private Integer   quotaStatus; // '额度状态 1.是(已超额） 2.否',
	private Integer   locked; // '0正常,1锁定,2废弃',
	private String  lockedMsg; //
	private Date lockedTime; //
	private Integer  repPay; // '1.否  2.是',
	private Integer  overQuota; // '路由集群快钱和夏门民生是否已超额。1:已超额，0：未超额',
	private Date createTime; // '创建时间',
	private String createPerson; // '创建人',
	private Integer  acqStatus; // '状态  0关闭  1开通',
	private String  merchantServiceType;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAcqOrgId() {
		return acqOrgId;
	}
	public void setAcqOrgId(Integer acqOrgId) {
		this.acqOrgId = acqOrgId;
	}
	public Integer getAcqServiceId() {
		return acqServiceId;
	}
	public void setAcqServiceId(Integer acqServiceId) {
		this.acqServiceId = acqServiceId;
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
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getAgentNo() {
		return agentNo;
	}
	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	public String getMcc() {
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public Integer getLargeSmallFlag() {
		return largeSmallFlag;
	}
	public void setLargeSmallFlag(Integer largeSmallFlag) {
		this.largeSmallFlag = largeSmallFlag;
	}
	public Integer getRateType() {
		return rateType;
	}
	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}
	public BigDecimal getSingleAmount() {
		return singleAmount;
	}
	public void setSingleAmount(BigDecimal singleAmount) {
		this.singleAmount = singleAmount;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getCapping() {
		return capping;
	}
	public void setCapping(BigDecimal capping) {
		this.capping = capping;
	}
	public BigDecimal getLadderRate() {
		return ladderRate;
	}
	public void setLadderRate(BigDecimal ladderRate) {
		this.ladderRate = ladderRate;
	}
	public BigDecimal getLadderAmount() {
		return ladderAmount;
	}
	public void setLadderAmount(BigDecimal ladderAmount) {
		this.ladderAmount = ladderAmount;
	}
	public BigDecimal getQuota() {
		return quota;
	}
	public void setQuota(BigDecimal quota) {
		this.quota = quota;
	}
	public Integer getQuotaStatus() {
		return quotaStatus;
	}
	public void setQuotaStatus(Integer quotaStatus) {
		this.quotaStatus = quotaStatus;
	}
	public Integer getLocked() {
		return locked;
	}
	public void setLocked(Integer locked) {
		this.locked = locked;
	}
	public String getLockedMsg() {
		return lockedMsg;
	}
	public void setLockedMsg(String lockedMsg) {
		this.lockedMsg = lockedMsg;
	}
	public Date getLockedTime() {
		return lockedTime;
	}
	public void setLockedTime(Date lockedTime) {
		this.lockedTime = lockedTime;
	}
	public Integer getRepPay() {
		return repPay;
	}
	public void setRepPay(Integer repPay) {
		this.repPay = repPay;
	}
	public Integer getOverQuota() {
		return overQuota;
	}
	public void setOverQuota(Integer overQuota) {
		this.overQuota = overQuota;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public Integer getAcqStatus() {
		return acqStatus;
	}
	public void setAcqStatus(Integer acqStatus) {
		this.acqStatus = acqStatus;
	}
	public String getMerchantServiceType() {
		return merchantServiceType;
	}
	public void setMerchantServiceType(String merchantServiceType) {
		this.merchantServiceType = merchantServiceType;
	}
	
	
}
