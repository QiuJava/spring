package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 分润信息
 * 
 * @author tgh
 *
 */
public class SuperPushShare {

	private Integer id;
	private String orderNo;
	private String transAmount;
	private String transTime;
	private String merchantNo;
	private String mobile;
	private String agentNo;
	private String agentNode;
	private String shareType;
	private String shareNo;
	private String shareName;
	private String shareAmount;
	private String shareRate;
	private String shareStatus;
	private Date shareTime;
	private Date createTime;

	private String startCreateTime;
	private String endCreateTime;

	private String startShareTime;
	private String endShareTime;
	private String containSub;	// 是否包含下级

	public String getContainSub() {
		return containSub;
	}

	public void setContainSub(String containSub) {
		this.containSub = containSub;
	}

	public Integer getId() {
		return id;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}

	public String getTransTime() {
		return transTime;
	}

	public void setTransTime(String transTime) {
		this.transTime = transTime;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
	}

	public String getShareNo() {
		return shareNo;
	}

	public void setShareNo(String shareNo) {
		this.shareNo = shareNo;
	}

	public String getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(String shareAmount) {
		this.shareAmount = shareAmount;
	}

	public String getShareRate() {
		return shareRate;
	}

	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate != null ? shareRate.setScale(2).toString() + "%" : "";
	}

	public String getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(String shareStatus) {
		this.shareStatus = shareStatus;
	}

	public Date getShareTime() {
		return shareTime;
	}

	public void setShareTime(Date shareTime) {
		this.shareTime = shareTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	public String getStartShareTime() {
		return startShareTime;
	}

	public void setStartShareTime(String startShareTime) {
		this.startShareTime = startShareTime;
	}

	public String getEndShareTime() {
		return endShareTime;
	}

	public void setEndShareTime(String endShareTime) {
		this.endShareTime = endShareTime;
	}

//
//	private Integer id;
//	private String orderNo;
//	private BigDecimal amount;
//	private BigDecimal share;
//	private Date transTime;
//	private String merchantNo;
//	private String merchantName;
//	private String mobile;
//	private BigDecimal selfShare;
//	private BigDecimal oneShare;
//	private BigDecimal twoShare;
//	private BigDecimal threeShare;
//	private String selfRule;
//	private String oneRule;
//	private String twoRule;
//	private String threeRule;
//	private String sDate;
//	private String eDate;
//	private String mertId;
//	private String level;
//	private String rule;
//	public Integer getId() {
//		return id;
//	}
//	public void setId(Integer id) {
//		this.id = id;
//	}
//	public String getOrderNo() {
//		return orderNo;
//	}
//	public void setOrderNo(String orderNo) {
//		this.orderNo = orderNo;
//	}
//	public BigDecimal getAmount() {
//		return amount;
//	}
//	public void setAmount(BigDecimal amount) {
//		this.amount = amount;
//	}
//	public BigDecimal getShare() {
//		return share;
//	}
//	public void setShare(BigDecimal share) {
//		this.share = share;
//	}
//	public Date getTransTime() {
//		return transTime;
//	}
//	public void setTransTime(Date transTime) {
//		this.transTime = transTime;
//	}
//	public String getMerchantNo() {
//		return merchantNo;
//	}
//	public void setMerchantNo(String merchantNo) {
//		this.merchantNo = merchantNo;
//	}
//	public String getMerchantName() {
//		return merchantName;
//	}
//	public void setMerchantName(String merchantName) {
//		this.merchantName = merchantName;
//	}
//	public String getMobile() {
//		return mobile;
//	}
//	public void setMobile(String mobile) {
//		this.mobile = mobile;
//	}
//	public BigDecimal getSelfShare() {
//		return selfShare;
//	}
//	public void setSelfShare(BigDecimal selfShare) {
//		this.selfShare = selfShare;
//	}
//	public BigDecimal getOneShare() {
//		return oneShare;
//	}
//	public void setOneShare(BigDecimal oneShare) {
//		this.oneShare = oneShare;
//	}
//	public BigDecimal getTwoShare() {
//		return twoShare;
//	}
//	public void setTwoShare(BigDecimal twoShare) {
//		this.twoShare = twoShare;
//	}
//	public BigDecimal getThreeShare() {
//		return threeShare;
//	}
//	public void setThreeShare(BigDecimal threeShare) {
//		this.threeShare = threeShare;
//	}
//	public String getSelfRule() {
//		return selfRule;
//	}
//	public void setSelfRule(String selfRule) {
//		this.selfRule = selfRule;
//	}
//	public String getOneRule() {
//		return oneRule;
//	}
//	public void setOneRule(String oneRule) {
//		this.oneRule = oneRule;
//	}
//	public String getTwoRule() {
//		return twoRule;
//	}
//	public void setTwoRule(String twoRule) {
//		this.twoRule = twoRule;
//	}
//	public String getThreeRule() {
//		return threeRule;
//	}
//	public void setThreeRule(String threeRule) {
//		this.threeRule = threeRule;
//	}
//	public String getsDate() {
//		return sDate;
//	}
//	public void setsDate(String sDate) {
//		this.sDate = sDate;
//	}
//	public String geteDate() {
//		return eDate;
//	}
//	public void seteDate(String eDate) {
//		this.eDate = eDate;
//	}
//	public String getMertId() {
//		return mertId;
//	}
//	public void setMertId(String mertId) {
//		this.mertId = mertId;
//	}
//	public String getLevel() {
//		return level;
//	}
//	public void setLevel(String level) {
//		this.level = level;
//	}
//	public String getRule() {
//		return rule;
//	}
//	public void setRule(String rule) {
//		this.rule = rule;
//	}

}
