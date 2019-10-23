package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.util.Date;

public class RecordAccountRuleTransType implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private String fromSystem ;
	private String transTypeName ;
	private Integer ruleId ;
	private String remark ;
    private String creator;
    private Date createTime;
    private String updator;
    private Date updateTime;
	private String transTypeCode ;
	private String transGroup;
	
	private RecordAccountRule recordAccountRule ;


	public String getTransTypeCode() {
		return transTypeCode;
	}

	public void setTransTypeCode(String transTypeCode) {
		this.transTypeCode = transTypeCode;
	}

	public RecordAccountRule getRecordAccountRule() {
		return recordAccountRule;
	}

	public void setRecordAccountRule(RecordAccountRule recordAccountRule) {
		this.recordAccountRule = recordAccountRule;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFromSystem() {
		return fromSystem;
	}

	public void setFromSystem(String fromSystem) {
		this.fromSystem = fromSystem;
	}

	public String getTransTypeName() {
		return transTypeName;
	}

	public void setTransTypeName(String transTypeName) {
		this.transTypeName = transTypeName;
	}

	public Integer getRuleId() {
		return ruleId;
	}

	public void setRuleId(Integer ruleId) {
		this.ruleId = ruleId;
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

	public String getTransGroup() {
		return transGroup;
	}

	public void setTransGroup(String transGroup) {
		this.transGroup = transGroup;
	}

	@Override
	public String toString() {
		return "RecordAccountRuleTransType [id=" + id + ", fromSystem=" + fromSystem + ", transTypeName="
				+ transTypeName + ", ruleId=" + ruleId + ", remark=" + remark + ", creator=" + creator + ", createTime="
				+ createTime + ", updator=" + updator + ", updateTime=" + updateTime + ", transTypeCode="
				+ transTypeCode + ", recordAccountRule=" + recordAccountRule + ", transGroup=" + transGroup + "]";
	}

//	@Override
//	public String toString() {
//		return "RecordAccountRuleAndTransType [id=" + id + ", fromSystem=" + fromSystem + ", transTypeName="
//				+ transTypeName + ", ruleId=" + ruleId + ", remark=" + remark + ", recordAccountRule="
//				+ recordAccountRule + "]";
//	}
	
}
