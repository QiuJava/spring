package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AcqMerchantInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer merchantType;

	private String merchantName;

	private String legalPerson;

	private String legalPersonId;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date idValidStart;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date idValidEnd;

	private String province;

	private String city;

	private String district;

	private String address;

	private String oneScope;

	private String twoScope;

	private String charterName;

	private String charterNo;

	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date charterValidStart;
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date charterValidEnd;

	private Integer accountType;

	private String bankNo;

	private String accountName;

	private String accountBank;

	private String accountProvince;

	private String accountCity;

	private String accountDistrict;

	private String bankBranch;

	private String lineNumber;

	private String acqIntoNo;

	private String intoSource;

	private Integer auditStatus;

	private Date auditTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	private String agentNo;

	private String oneAgentNo;

	private String mcc;

	private String intoSourceName;

	private List<AcqMerchantFileInfo> fileList;

	private List<AcqMerchantInfoLog> auditList;

	private String scope;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date updateTime;

	public String getAddressInfo() {
		return province + city + district + address;
	}

	public String getAccountAddress() {
		return accountProvince + accountCity + accountDistrict;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(Integer merchantType) {
		this.merchantType = merchantType;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getLegalPersonId() {
		return legalPersonId;
	}

	public void setLegalPersonId(String legalPersonId) {
		this.legalPersonId = legalPersonId;
	}

	public Date getIdValidStart() {
		return idValidStart;
	}

	public void setIdValidStart(Date idValidStart) {
		this.idValidStart = idValidStart;
	}

	public Date getIdValidEnd() {
		return idValidEnd;
	}

	public void setIdValidEnd(Date idValidEnd) {
		this.idValidEnd = idValidEnd;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOneScope() {
		return oneScope;
	}

	public void setOneScope(String oneScope) {
		this.oneScope = oneScope;
	}

	public String getTwoScope() {
		return twoScope;
	}

	public void setTwoScope(String twoScope) {
		this.twoScope = twoScope;
	}

	public String getCharterName() {
		return charterName;
	}

	public void setCharterName(String charterName) {
		this.charterName = charterName;
	}

	public String getCharterNo() {
		return charterNo;
	}

	public void setCharterNo(String charterNo) {
		this.charterNo = charterNo;
	}

	public Date getCharterValidStart() {
		return charterValidStart;
	}

	public void setCharterValidStart(Date charterValidStart) {
		this.charterValidStart = charterValidStart;
	}

	public Date getCharterValidEnd() {
		return charterValidEnd;
	}

	public void setCharterValidEnd(Date charterValidEnd) {
		this.charterValidEnd = charterValidEnd;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountBank() {
		return accountBank;
	}

	public void setAccountBank(String accountBank) {
		this.accountBank = accountBank;
	}

	public String getAccountProvince() {
		return accountProvince;
	}

	public void setAccountProvince(String accountProvince) {
		this.accountProvince = accountProvince;
	}

	public String getAccountCity() {
		return accountCity;
	}

	public void setAccountCity(String accountCity) {
		this.accountCity = accountCity;
	}

	public String getAccountDistrict() {
		return accountDistrict;
	}

	public void setAccountDistrict(String accountDistrict) {
		this.accountDistrict = accountDistrict;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public String getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(String lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getAcqIntoNo() {
		return acqIntoNo;
	}

	public void setAcqIntoNo(String acqIntoNo) {
		this.acqIntoNo = acqIntoNo;
	}

	public String getIntoSource() {
		return intoSource;
	}

	public void setIntoSource(String intoSource) {
		this.intoSource = intoSource;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getIntoSourceName() {
		return intoSourceName;
	}

	public void setIntoSourceName(String intoSourceName) {
		this.intoSourceName = intoSourceName;
	}

	public List<AcqMerchantFileInfo> getFileList() {
		return fileList;
	}

	public void setFileList(List<AcqMerchantFileInfo> fileList) {
		this.fileList = fileList;
	}

	public List<AcqMerchantInfoLog> getAuditList() {
		return auditList;
	}

	public void setAuditList(List<AcqMerchantInfoLog> auditList) {
		this.auditList = auditList;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

}