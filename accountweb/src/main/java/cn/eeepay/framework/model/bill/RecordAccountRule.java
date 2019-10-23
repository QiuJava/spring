package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class RecordAccountRule implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ruleId;
	private String ruleNo;
	private String ruleName;
	private String program;
	private String remark;
    private String creator;
    private Date createTime;
    private String updator;
    private Date updateTime;
	
	public Integer getRuleId() {
		return ruleId;
	}
	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
	}
	public String getRuleNo() {
		return ruleNo;
	}
	public void setRuleNo(String ruleNo) {
		this.ruleNo = ruleNo;
	}
	public String getRuleName() {
		return ruleName;
	}
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	public String getProgram() {
		return program;
	}
	public void setProgram(String program) {
		this.program = program;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
//	@Override
//	public String toString() {
//		return "RecordAccountRule [ruleId=" + ruleId + ", ruleNo=" + ruleNo + ", ruleName=" + ruleName + ", program="
//				+ program + ", remark=" + remark + "]";
//	}
	@Override
	public String toString() {
		return "RecordAccountRule [ruleId=" + ruleId + ", ruleNo=" + ruleNo + ", ruleName=" + ruleName + ", program="
				+ program + ", remark=" + remark + ", creator=" + creator + ", createTime=" + createTime + ", updator="
				+ updator + ", updateTime=" + updateTime + "]";
	}
	
}
