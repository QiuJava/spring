package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * by zrj
 * zrj@eeepay.cn rjzou@qq.com
 */
public class SuperPushShareDaySettle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
    private String collectionBatchNo;
    private Date groupTime;
    private Date createTime;
    private String shareType;
    private String shareNo;
    private String shareName;
    private BigDecimal shareTotalAmount;
    private Integer shareTotalNum;
    private String enterAccountStatus;
    private Date enterAccountTime;
    private String enterAccountMessage;
    private String groupTime1;
    private String groupTime2;
    private String createTime1;
    private String createTime2;
    
    private String shareTotalAmount1;
    private String shareTotalAmount2;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCollectionBatchNo() {
		return collectionBatchNo;
	}
	public void setCollectionBatchNo(String collectionBatchNo) {
		this.collectionBatchNo = collectionBatchNo;
	}
	public Date getGroupTime() {
		return groupTime;
	}
	public void setGroupTime(Date groupTime) {
		this.groupTime = groupTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
	public String getShareName() {
		return shareName;
	}
	public void setShareName(String shareName) {
		this.shareName = shareName;
	}
	public BigDecimal getShareTotalAmount() {
		return shareTotalAmount;
	}
	public void setShareTotalAmount(BigDecimal shareTotalAmount) {
		this.shareTotalAmount = shareTotalAmount;
	}
	public Integer getShareTotalNum() {
		return shareTotalNum;
	}
	public void setShareTotalNum(Integer shareTotalNum) {
		this.shareTotalNum = shareTotalNum;
	}
	public String getEnterAccountStatus() {
		return enterAccountStatus;
	}
	public void setEnterAccountStatus(String enterAccountStatus) {
		this.enterAccountStatus = enterAccountStatus;
	}
	public Date getEnterAccountTime() {
		return enterAccountTime;
	}
	public void setEnterAccountTime(Date enterAccountTime) {
		this.enterAccountTime = enterAccountTime;
	}
	public String getEnterAccountMessage() {
		return enterAccountMessage;
	}
	public void setEnterAccountMessage(String enterAccountMessage) {
		this.enterAccountMessage = enterAccountMessage;
	}
	public String getGroupTime1() {
		return groupTime1;
	}
	public void setGroupTime1(String groupTime1) {
		this.groupTime1 = groupTime1;
	}
	public String getGroupTime2() {
		return groupTime2;
	}
	public void setGroupTime2(String groupTime2) {
		this.groupTime2 = groupTime2;
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
	public String getShareTotalAmount1() {
		return shareTotalAmount1;
	}
	public void setShareTotalAmount1(String shareTotalAmount1) {
		this.shareTotalAmount1 = shareTotalAmount1;
	}
	public String getShareTotalAmount2() {
		return shareTotalAmount2;
	}
	public void setShareTotalAmount2(String shareTotalAmount2) {
		this.shareTotalAmount2 = shareTotalAmount2;
	}
	
}
