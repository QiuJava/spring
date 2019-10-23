package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级银行家红包明细
 * 
 * @author Administrator
 *
 */
public class RedEnvelopesDetails {
	private Integer id;
	private String type;// 账户类型,交易类型
	private Date createDate;// 记账时间,交易时间
	private String transType;// 变动类型 对应red_account_detail表交易类型type
	private String operationType;// 操作类型 :根据金额判断,正数增加,负数减少
	private BigDecimal transAmount;// 交易金额
	private String redOrderId;// 红包订单ID 对应红包ID
	private String startCreateDate;
	private String endCreateDate;
	private BigDecimal totalAmount;// 总金额统计
	private String withdOn;// 提现订单号

	private String redAccountId;// 所属账户id关联red_account_info表主键
	private String accountCode;// 红包账号
	private String remark;// 备注

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRedAccountId() {
		return redAccountId;
	}

	public void setRedAccountId(String redAccountId) {
		this.redAccountId = redAccountId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getWithdOn() {
		return withdOn;
	}

	public void setWithdOn(String withdOn) {
		this.withdOn = withdOn;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStartCreateDate() {
		return startCreateDate;
	}

	public void setStartCreateDate(String startCreateDate) {
		this.startCreateDate = startCreateDate;
	}

	public String getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(String endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public String getRedOrderId() {
		return redOrderId;
	}

	public void setRedOrderId(String redOrderId) {
		this.redOrderId = redOrderId;
	}

}
