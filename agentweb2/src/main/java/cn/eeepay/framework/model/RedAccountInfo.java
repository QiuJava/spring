package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * red_account_info 红包收支账户余额表
 * 
 * @author Administrator
 *
 */
public class RedAccountInfo {

	private Long id;
	private String type;
	private String accountCode;
	private String relationId;
	private String userCode;
	private BigDecimal totalAmount;
	private String status;
	private String receviceMax;
	private String luckyValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getRelationId() {
		return relationId;
	}

	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getReceviceMax() {
		return receviceMax;
	}

	public void setReceviceMax(String receviceMax) {
		this.receviceMax = receviceMax;
	}

	public String getLuckyValue() {
		return luckyValue;
	}

	public void setLuckyValue(String luckyValue) {
		this.luckyValue = luckyValue;
	}

}
