package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class BusinessAccount implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String applicant; //'提交人',
	private Date applicantTime; //'提交时间',
	private String  approver; //'审批人',
	private Date approveTime; //'审批时间',
	private Integer  status; //'状态（0待提交，1待审批，2审批通过，3审批不通过，4已记账，5记账失败）',
	private String recordFailRemark; //'记账失败原因',
	private String remark; //'备注',
	private String filePath;  //上传excel模板路径
	private String approveRemark;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public Date getApplicantTime() {
		return applicantTime;
	}
	public void setApplicantTime(Date applicantTime) {
		this.applicantTime = applicantTime;
	}
	public String getApprover() {
		return approver;
	}
	public void setApprover(String approver) {
		this.approver = approver;
	}
	public Date getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRecordFailRemark() {
		return recordFailRemark;
	}
	public void setRecordFailRemark(String recordFailRemark) {
		this.recordFailRemark = recordFailRemark;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getApproveRemark() {
		return approveRemark;
	}
	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
	
}
