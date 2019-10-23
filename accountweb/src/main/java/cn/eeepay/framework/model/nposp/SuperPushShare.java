package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * table  agent_info
 * desc 代理商信息表
 */
/**
 * @author zrj
 * zrj@eeepay.cn rjzou@qq.com
 * 
 * 超级推商户分润记录
 *
 */
public class SuperPushShare implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String orderNo;

    private BigDecimal transAmount;

    private Date transTime;

    private String merchantNo;

    private String mobile;

    private String agentNode;

    private String shareType;

    private String shareNo;
    
    private String shareName;

    private BigDecimal shareAmount;

    private BigDecimal shareRate;
    
    private String shareRateStr;

    private String shareStatus;

    private Date shareTime;

    private Date createTime;
    
    
    private String createTime1;
	private String createTime2;
	
    private String shareTime1;
	private String shareTime2;
	
	private String checkAccountStatus;
	private String collectionStatus;
	private String collectionBatchNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getTransAmount() {
		return transAmount;
	}

	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
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

	public BigDecimal getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}

	public BigDecimal getShareRate() {
		return shareRate;
	}

	public void setShareRate(BigDecimal shareRate) {
		this.shareRate = shareRate;
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

	public String getCreateTime1() {
		return createTime1;
	}

	public void setCreateTime1(String createTime1) {
		this.createTime1 = createTime1;
	}

	public String getCreateTime2() {
		return createTime2;
	}

	public void setCreateTime2(String createTime2) {
		this.createTime2 = createTime2;
	}

	public String getShareTime1() {
		return shareTime1;
	}

	public void setShareTime1(String shareTime1) {
		this.shareTime1 = shareTime1;
	}

	public String getShareTime2() {
		return shareTime2;
	}

	public void setShareTime2(String shareTime2) {
		this.shareTime2 = shareTime2;
	}

	public String getCheckAccountStatus() {
		return checkAccountStatus;
	}

	public void setCheckAccountStatus(String checkAccountStatus) {
		this.checkAccountStatus = checkAccountStatus;
	}

	public String getCollectionStatus() {
		return collectionStatus;
	}

	public void setCollectionStatus(String collectionStatus) {
		this.collectionStatus = collectionStatus;
	}

	public String getCollectionBatchNo() {
		return collectionBatchNo;
	}

	public void setCollectionBatchNo(String collectionBatchNo) {
		this.collectionBatchNo = collectionBatchNo;
	}

	public String getShareName() {
		return shareName;
	}

	public void setShareName(String shareName) {
		this.shareName = shareName;
	}

	public String getShareRateStr() {
		return shareRateStr;
	}

	public void setShareRateStr(String shareRateStr) {
		this.shareRateStr = shareRateStr;
	}
	
	
	

}