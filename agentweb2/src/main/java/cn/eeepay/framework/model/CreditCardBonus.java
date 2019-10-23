package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 信用卡奖金配置表
 * 
 * @author Administrator
 *
 */
public class CreditCardBonus {

	private Long id;

	private Long orgId;// 组织ID

	private Long sourceId;// 信用卡银行ID

	private String orgCost;// 品牌组织成本

	private String orgPushCost;// 品牌组织发放奖金

	private String updateBy;

	private Date updateDate;

	private String orgName;// 组织名称

	private String bankName;// 银行名称

	private String bankBonus;// 总奖金 (配置在银行表)

	private String isOnlyone;// 是否首次办卡奖励

	private String entityId;

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getOrgCost() {
		return orgCost;
	}

	public void setOrgCost(String orgCost) {
		this.orgCost = orgCost;
	}

	public String getOrgPushCost() {
		return orgPushCost;
	}

	public void setOrgPushCost(String orgPushCost) {
		this.orgPushCost = orgPushCost;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankBonus() {
		return bankBonus;
	}

	public void setBankBonus(String bankBonus) {
		this.bankBonus = bankBonus;
	}

	public String getIsOnlyone() {
		return isOnlyone;
	}

	public void setIsOnlyone(String isOnlyone) {
		this.isOnlyone = isOnlyone;
	}

}
