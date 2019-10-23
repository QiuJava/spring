package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class PaCashBackDetail {
	private Long id;
	private String userCode;// 盟主编号 激活返现盟主编号
	private BigDecimal cashBackAmount;// 返现金额
	private String cashBackSwitch;// 返现开关(1打开)
	private String entryStatus;// 入帐状态(0未入账)
	private Date entryTime;// 入账时间
	private Date createTime;// 创建时间
	private String remark;// 备注
	private String activeOrder;// 激活订单号
	private String accUserAgent;// 入账用户编号
	private String realName;// 激活返现盟主名称
	private String entryTimeStart;
	private String entryTimeEnd;
	private BigDecimal transAmount;// 交易金额
	private String activityTypeNo;// 欢乐返子类型
	private String activityCode;// 欢乐返类型
	private String loginUserCode;
	private Integer agentLevel;
	private String loginUserNode;

	public String getLoginUserNode() {
		return loginUserNode;
	}

	public void setLoginUserNode(String loginUserNode) {
		this.loginUserNode = loginUserNode;
	}

	public String getLoginUserCode() {
		return loginUserCode;
	}

	public void setLoginUserCode(String loginUserCode) {
		this.loginUserCode = loginUserCode;
	}

	public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getEntryTimeStart() {
		return entryTimeStart;
	}

	public void setEntryTimeStart(String entryTimeStart) {
		this.entryTimeStart = entryTimeStart;
	}

	public String getEntryTimeEnd() {
		return entryTimeEnd;
	}

	public void setEntryTimeEnd(String entryTimeEnd) {
		this.entryTimeEnd = entryTimeEnd;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public BigDecimal getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(BigDecimal cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public String getCashBackSwitch() {
		return cashBackSwitch;
	}

	public void setCashBackSwitch(String cashBackSwitch) {
		this.cashBackSwitch = cashBackSwitch;
	}

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getActiveOrder() {
		return activeOrder;
	}

	public void setActiveOrder(String activeOrder) {
		this.activeOrder = activeOrder;
	}

	public String getAccUserAgent() {
		return accUserAgent;
	}

	public void setAccUserAgent(String accUserAgent) {
		this.accUserAgent = accUserAgent;
	}
}
