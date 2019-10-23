package cn.eeepay.framework.model;

import java.util.Date;

/**
 * SN回拨记录
 * 
 * @author Administrator
 *
 */
public class PaSnBack {

	private Long id;
	private String orderNo;// 回拨单号
	private Integer backCount;// 回拨数量
	private String userCode;// 回拨盟主编号
	private String userNode;
	private String receiveUserCode;// 接收盟主编号
	private String receiveUserType;// 接收盟主类型
	private String beLongUserCode;// 所属机构编号
	private String status;// 回拨状态
	private Date createTime;// 回拨日期
	private Date lastUpdateTime;// 接收日期
	private String createTimeStart;
	private String createTimeEnd;
	private String lastUpdateTimeStart;
	private String lastUpdateTimeEnd;
	private Integer entityLevel;// 当前登录级别
	private String userCodeStatus;// 用于区分自己是接收人或申请人0:申请人,1:接收人
	private String parentId;

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getUserCodeStatus() {
		return userCodeStatus;
	}

	public void setUserCodeStatus(String userCodeStatus) {
		this.userCodeStatus = userCodeStatus;
	}

	public Integer getEntityLevel() {
		return entityLevel;
	}

	public void setEntityLevel(Integer entityLevel) {
		this.entityLevel = entityLevel;
	}

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

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

	public Integer getBackCount() {
		return backCount;
	}

	public void setBackCount(Integer backCount) {
		this.backCount = backCount;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getReceiveUserCode() {
		return receiveUserCode;
	}

	public void setReceiveUserCode(String receiveUserCode) {
		this.receiveUserCode = receiveUserCode;
	}

	public String getReceiveUserType() {
		return receiveUserType;
	}

	public void setReceiveUserType(String receiveUserType) {
		this.receiveUserType = receiveUserType;
	}

	public String getBeLongUserCode() {
		return beLongUserCode;
	}

	public void setBeLongUserCode(String beLongUserCode) {
		this.beLongUserCode = beLongUserCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) {
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getLastUpdateTimeStart() {
		return lastUpdateTimeStart;
	}

	public void setLastUpdateTimeStart(String lastUpdateTimeStart) {
		this.lastUpdateTimeStart = lastUpdateTimeStart;
	}

	public String getLastUpdateTimeEnd() {
		return lastUpdateTimeEnd;
	}

	public void setLastUpdateTimeEnd(String lastUpdateTimeEnd) {
		this.lastUpdateTimeEnd = lastUpdateTimeEnd;
	}

}
