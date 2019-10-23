package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级推活动
 *
 * @author tgh
 *
 */
public class SuperPush {

	private Integer id;
	private String userId;
	private String activityCode;
	private String agentNo;
	private String agentName;
	private String agentNode;
	private String merchantNo;
	private String merchantName;
	private String oneMerchantNo;
	private String twoMerchantNo;
	private String threeMerchantNo;
	private String oneMerchantName;
	private String twoMerchantName;
	private String threeMerchantName;
	private Date createTime;
	private String startCreateTime;
	private String endCreateTime;
	private String businessNo;// 推荐商户的业务编号
	private String recommendedSource;// 推荐来源：1是商户链接2是代理商链接
	private String recommendedUserId;// 推荐人ID
	private String mobilephone;
	private BigDecimal totalAmount;// 收益总额
	private BigDecimal balance;// 账户余额
	private String oneLevelId;// 一级代理商编号
	private String status; 		//商户状态
	private BigDecimal avaliBalance;//可用余额

	public BigDecimal getAvaliBalance() {
		return avaliBalance;
	}

	public void setAvaliBalance(BigDecimal avaliBalance) {
		this.avaliBalance = avaliBalance;
	}

	public String getOneLevelId() {
		return oneLevelId;
	}

	public void setOneLevelId(String oneLevelId) {
		this.oneLevelId = oneLevelId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(String activityCode) {
		this.activityCode = activityCode;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getOneMerchantNo() {
		return oneMerchantNo;
	}

	public void setOneMerchantNo(String oneMerchantNo) {
		this.oneMerchantNo = oneMerchantNo;
	}

	public String getTwoMerchantNo() {
		return twoMerchantNo;
	}

	public void setTwoMerchantNo(String twoMerchantNo) {
		this.twoMerchantNo = twoMerchantNo;
	}

	public String getThreeMerchantNo() {
		return threeMerchantNo;
	}

	public void setThreeMerchantNo(String threeMerchantNo) {
		this.threeMerchantNo = threeMerchantNo;
	}

	public String getOneMerchantName() {
		return oneMerchantName;
	}

	public void setOneMerchantName(String oneMerchantName) {
		this.oneMerchantName = oneMerchantName;
	}

	public String getTwoMerchantName() {
		return twoMerchantName;
	}

	public void setTwoMerchantName(String twoMerchantName) {
		this.twoMerchantName = twoMerchantName;
	}

	public String getThreeMerchantName() {
		return threeMerchantName;
	}

	public void setThreeMerchantName(String threeMerchantName) {
		this.threeMerchantName = threeMerchantName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBusinessNo() {
		return businessNo;
	}

	public void setBusinessNo(String businessNo) {
		this.businessNo = businessNo;
	}

	public String getRecommendedSource() {
		return recommendedSource;
	}

	public void setRecommendedSource(String recommendedSource) {
		this.recommendedSource = recommendedSource;
	}

	public String getRecommendedUserId() {
		return recommendedUserId;
	}

	public void setRecommendedUserId(String recommendedUserId) {
		this.recommendedUserId = recommendedUserId;
	}

	public String getMobilephone() {
		return mobilephone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
	}

	public String getStartCreateTime() {
		return startCreateTime;
	}

	public void setStartCreateTime(String startCreateTime) {
		this.startCreateTime = startCreateTime;
	}

	public String getEndCreateTime() {
		return endCreateTime;
	}

	public void setEndCreateTime(String endCreateTime) {
		this.endCreateTime = endCreateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
