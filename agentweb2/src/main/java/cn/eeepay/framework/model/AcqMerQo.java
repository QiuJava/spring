package cn.eeepay.framework.model;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 收单商户列表查询
 * 
 * @author Qiujian
 * @date 2019/02/19
 */
public class AcqMerQo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String acqIntoNo;
	private String merchantName;
	private String legalPerson;
	private int auditState;
	private int merchantType;
	private String intoSource;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date intoStartTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date intoEndTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date auditStartTime;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date auditEndTime;
	private String agentNo;

	public String getAcqIntoNo() {
		return acqIntoNo;
	}

	public void setAcqIntoNo(String acqIntoNo) {
		this.acqIntoNo = acqIntoNo;
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

	public int getAuditState() {
		return auditState;
	}

	public void setAuditState(int auditState) {
		this.auditState = auditState;
	}

	public int getMerchantType() {
		return merchantType;
	}

	public void setMerchantType(int merchantType) {
		this.merchantType = merchantType;
	}

	public String getIntoSource() {
		return intoSource;
	}

	public void setIntoSource(String intoSource) {
		this.intoSource = intoSource;
	}

	public Date getIntoStartTime() {
		return intoStartTime;
	}

	public void setIntoStartTime(Date intoStartTime) {
		this.intoStartTime = intoStartTime;
	}

	public Date getIntoEndTime() {
		return intoEndTime;
	}

	public void setIntoEndTime(Date intoEndTime) {
		this.intoEndTime = intoEndTime;
	}

	public Date getAuditStartTime() {
		return auditStartTime;
	}

	public void setAuditStartTime(Date auditStartTime) {
		this.auditStartTime = auditStartTime;
	}
	
	public Date getAuditEndTime() {
		return auditEndTime;
	}

	public void setAuditEndTime(Date auditEndTime) {
		this.auditEndTime = auditEndTime;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	
}
