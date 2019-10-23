package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class ServiceInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long serviceId; // '服务ID',
	private String  serviceName; // '服务名称',
	private String  serviceType; // '服务类型',
	private String  hardwareIs; // '是否与硬件相关:1-是，0-否',
	private String  bankCard; // '可用银行卡集合:1-信用卡，2-银行卡，0-不限',
	private String  exclusive; // '可否单独申请:1-可，0-否',
	private String  business; // '业务归属',
	private Date  saleStarttime; // '可销售起始日期',
	private Date  saleEndtime; // '可销售终止日期',
	private Date  useStarttime; // '可使用起始日期',
	private Date  useEndtime; // '可使用终止日期',
	private String  proxy; // '可否代理:1-可，0-否',
	private String  getcashId; // '提现服务ID',
	private String  rateCard; // '费率是否区分银行卡种类:1-是，0-否',
	private String  rateHolidays; // '费率是否区分节假日:1-是，0-否',
	private String  quotaHolidays; // '限额是否区分节假日:1-是，0-否',
	private String  quotaCard; // '限额是否区分银行卡种类:1-是，0-否',
	private String  oemId; // 'OEM ID',
	private String  remark; // '备注',
	private String  tFlag; // 'T0T1标志：0-不涉及，1-T0，2-T1, 3-T0和T1',
	private String  cashSubject; // '仅服务类型为账户提现，存储科目，账号',
	private Integer  fixedRate; // '费率固定标志:1-固定，0-不固定',
	private Integer  fixedQuota; // '额度固定标志:1-固定，0-不固定',
	private Integer  serviceStatus; // '服务状态：1开启，0关闭',
	private Date  tradStart; // '交易开始时间',
	private Date  tradEnd; // '交易截至时间',
	private String  linkService; // '关联提现服务',
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getHardwareIs() {
		return hardwareIs;
	}
	public void setHardwareIs(String hardwareIs) {
		this.hardwareIs = hardwareIs;
	}
	public String getBankCard() {
		return bankCard;
	}
	public void setBankCard(String bankCard) {
		this.bankCard = bankCard;
	}
	public String getExclusive() {
		return exclusive;
	}
	public void setExclusive(String exclusive) {
		this.exclusive = exclusive;
	}
	public String getBusiness() {
		return business;
	}
	public void setBusiness(String business) {
		this.business = business;
	}
	public Date getSaleStarttime() {
		return saleStarttime;
	}
	public void setSaleStarttime(Date saleStarttime) {
		this.saleStarttime = saleStarttime;
	}
	public Date getSaleEndtime() {
		return saleEndtime;
	}
	public void setSaleEndtime(Date saleEndtime) {
		this.saleEndtime = saleEndtime;
	}
	public Date getUseStarttime() {
		return useStarttime;
	}
	public void setUseStarttime(Date useStarttime) {
		this.useStarttime = useStarttime;
	}
	public Date getUseEndtime() {
		return useEndtime;
	}
	public void setUseEndtime(Date useEndtime) {
		this.useEndtime = useEndtime;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getGetcashId() {
		return getcashId;
	}
	public void setGetcashId(String getcashId) {
		this.getcashId = getcashId;
	}
	public String getRateCard() {
		return rateCard;
	}
	public void setRateCard(String rateCard) {
		this.rateCard = rateCard;
	}
	public String getRateHolidays() {
		return rateHolidays;
	}
	public void setRateHolidays(String rateHolidays) {
		this.rateHolidays = rateHolidays;
	}
	public String getQuotaHolidays() {
		return quotaHolidays;
	}
	public void setQuotaHolidays(String quotaHolidays) {
		this.quotaHolidays = quotaHolidays;
	}
	public String getQuotaCard() {
		return quotaCard;
	}
	public void setQuotaCard(String quotaCard) {
		this.quotaCard = quotaCard;
	}
	public String getOemId() {
		return oemId;
	}
	public void setOemId(String oemId) {
		this.oemId = oemId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String gettFlag() {
		return tFlag;
	}
	public void settFlag(String tFlag) {
		this.tFlag = tFlag;
	}
	public String getCashSubject() {
		return cashSubject;
	}
	public void setCashSubject(String cashSubject) {
		this.cashSubject = cashSubject;
	}
	public Integer getFixedRate() {
		return fixedRate;
	}
	public void setFixedRate(Integer fixedRate) {
		this.fixedRate = fixedRate;
	}
	public Integer getFixedQuota() {
		return fixedQuota;
	}
	public void setFixedQuota(Integer fixedQuota) {
		this.fixedQuota = fixedQuota;
	}
	public Integer getServiceStatus() {
		return serviceStatus;
	}
	public void setServiceStatus(Integer serviceStatus) {
		this.serviceStatus = serviceStatus;
	}
	public Date getTradStart() {
		return tradStart;
	}
	public void setTradStart(Date tradStart) {
		this.tradStart = tradStart;
	}
	public Date getTradEnd() {
		return tradEnd;
	}
	public void setTradEnd(Date tradEnd) {
		this.tradEnd = tradEnd;
	}
	public String getLinkService() {
		return linkService;
	}
	public void setLinkService(String linkService) {
		this.linkService = linkService;
	}
	
	
}
