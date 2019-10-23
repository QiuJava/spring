package cn.eeepay.framework.model;

import java.util.Date;

public class AcqMerchantInfoLog {
	private Integer id;

	private Long acqMerchantInfoId;

	private Integer auditStatus;

	private String examinationOpinions;

	private String operator;

	private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getAcqMerchantInfoId() {
		return acqMerchantInfoId;
	}

	public void setAcqMerchantInfoId(Long acqMerchantInfoId) {
		this.acqMerchantInfoId = acqMerchantInfoId;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getExaminationOpinions() {
		return examinationOpinions;
	}

	public void setExaminationOpinions(String examinationOpinions) {
		this.examinationOpinions = examinationOpinions;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}