package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 新增代理商 欢乐返活动
 * 
 * @author Administrator
 *
 */
public class AgentActivity {
	private Long id;
	private String activityTypeNo;// 欢乐返子类型编号
	private String activityTypeName;// 欢乐返子类型名称
	private String activityCode;// 欢乐返类型
	private BigDecimal transAmount;// 交易金额
	private boolean status;//1-开启 0-关闭
	private String agentNo;
	private String agentNode;
	private BigDecimal cashBackAmount;// 返现金额
	private BigDecimal taxRate;// 税额百分比
	private Date createTime;// 创建时间
	private String remark;
	private Date currentTime;//返回当前时间
	private BigDecimal repeatRegisterAmount;
	private BigDecimal repeatRegisterRatio;
	private BigDecimal fullPrizeAmount;
	private BigDecimal notFullDeductAmount;
	private BigDecimal repeatFullPrizeAmount;
	private BigDecimal repeatNotFullDeductAmount;
	private boolean showFullPrizeAmount;
	private boolean showNotFullDeductAmount;
	private boolean select;//是否选中
	private boolean showStatusSwitch;//是否显示状态按钮
	private boolean disabled;//是否可选
	
	public Date getCurrentTime() {
		return new Date();
	}

	public void setCurrentTime(Date currentTime) {
		this.currentTime = currentTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public String getActivityTypeName() {
		return activityTypeName;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public BigDecimal getCashBackAmount() {
		return cashBackAmount;
	}

	public void setCashBackAmount(BigDecimal cashBackAmount) {
		this.cashBackAmount = cashBackAmount;
	}

	public BigDecimal getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		this.taxRate = taxRate;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public BigDecimal getRepeatRegisterAmount() {
		return repeatRegisterAmount;
	}

	public void setRepeatRegisterAmount(BigDecimal repeatRegisterAmount) {
		this.repeatRegisterAmount = repeatRegisterAmount;
	}

	public BigDecimal getRepeatRegisterRatio() {
		return repeatRegisterRatio;
	}

	public void setRepeatRegisterRatio(BigDecimal repeatRegisterRatio) {
		this.repeatRegisterRatio = repeatRegisterRatio;
	}

	public BigDecimal getFullPrizeAmount() {
		return fullPrizeAmount;
	}

	public void setFullPrizeAmount(BigDecimal fullPrizeAmount) {
		this.fullPrizeAmount = fullPrizeAmount;
	}

	public BigDecimal getNotFullDeductAmount() {
		return notFullDeductAmount;
	}

	public void setNotFullDeductAmount(BigDecimal notFullDeductAmount) {
		this.notFullDeductAmount = notFullDeductAmount;
	}

	public BigDecimal getRepeatFullPrizeAmount() {
		return repeatFullPrizeAmount;
	}

	public void setRepeatFullPrizeAmount(BigDecimal repeatFullPrizeAmount) {
		this.repeatFullPrizeAmount = repeatFullPrizeAmount;
	}

	public BigDecimal getRepeatNotFullDeductAmount() {
		return repeatNotFullDeductAmount;
	}

	public void setRepeatNotFullDeductAmount(BigDecimal repeatNotFullDeductAmount) {
		this.repeatNotFullDeductAmount = repeatNotFullDeductAmount;
	}

	public boolean isShowFullPrizeAmount() {
		return showFullPrizeAmount;
	}

	public void setShowFullPrizeAmount(boolean showFullPrizeAmount) {
		this.showFullPrizeAmount = showFullPrizeAmount;
	}

	public boolean isShowNotFullDeductAmount() {
		return showNotFullDeductAmount;
	}

	public void setShowNotFullDeductAmount(boolean showNotFullDeductAmount) {
		this.showNotFullDeductAmount = showNotFullDeductAmount;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public boolean isShowStatusSwitch() {
		return showStatusSwitch;
	}

	public void setShowStatusSwitch(boolean showStatusSwitch) {
		this.showStatusSwitch = showStatusSwitch;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
