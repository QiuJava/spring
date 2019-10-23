package cn.eeepay.framework.model;

import java.util.Date;

public class AcqMerchantFileInfo {
	private Long id;

	private Date createTime;

	private String fileType;

	private String fileUrl;

	private Integer status;

	private String acqIntoNo;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAcqIntoNo() {
		return acqIntoNo;
	}

	public void setAcqIntoNo(String acqIntoNo) {
		this.acqIntoNo = acqIntoNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}