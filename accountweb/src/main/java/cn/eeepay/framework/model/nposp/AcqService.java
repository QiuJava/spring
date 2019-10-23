package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.util.Date;

public class AcqService implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id; //'收单服务ID',
	private Integer acqId; //'收单机构ID',
	private String acqEnname; //'收单机构英文名',
	private String serviceType; //'服务类型 1-POS刷卡，2-扫码支付，3-快捷支付，4-账户提现',
	private String serviceName; //'服务名称',
	private Integer feeIsCard; //'费率区分银行卡 1.是 2.否',
	private Integer quotaIsCard; //'限额区分银行卡 1.是 2.否',
	private Integer bankCardType; //'可用银行卡 0.全部 1.仅信用卡 2.仅借记卡',
	private Date allowTransStartTime; //'每日允许交易开始时间',
	private Date allowTransEndTime; //'每日允许交易结束时间',
	private String serviceRemark; //'备注',
	private Integer serviceStatus; //'服务状态 1.开启 0.关闭',
	private Date createTime; //'创建时间',
	private String createPerson; //'创建人',
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getAcqId() {
		return acqId;
	}
	public void setAcqId(Integer acqId) {
		this.acqId = acqId;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Integer getFeeIsCard() {
		return feeIsCard;
	}
	public void setFeeIsCard(Integer feeIsCard) {
		this.feeIsCard = feeIsCard;
	}
	public Integer getQuotaIsCard() {
		return quotaIsCard;
	}
	public void setQuotaIsCard(Integer quotaIsCard) {
		this.quotaIsCard = quotaIsCard;
	}
	public Integer getBankCardType() {
		return bankCardType;
	}
	public void setBankCardType(Integer bankCardType) {
		this.bankCardType = bankCardType;
	}
	public Date getAllowTransStartTime() {
		return allowTransStartTime;
	}
	public void setAllowTransStartTime(Date allowTransStartTime) {
		this.allowTransStartTime = allowTransStartTime;
	}
	public Date getAllowTransEndTime() {
		return allowTransEndTime;
	}
	public void setAllowTransEndTime(Date allowTransEndTime) {
		this.allowTransEndTime = allowTransEndTime;
	}
	public String getServiceRemark() {
		return serviceRemark;
	}
	public void setServiceRemark(String serviceRemark) {
		this.serviceRemark = serviceRemark;
	}
	public Integer getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(Integer serviceStatus) {
		this.serviceStatus = serviceStatus;
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
	
}
