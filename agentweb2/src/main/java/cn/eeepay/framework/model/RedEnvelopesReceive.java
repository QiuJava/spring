package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/1/18/018.
 * 
 * @author liuks 红包领取实体 对应表red_orders_receive
 */
public class RedEnvelopesReceive {
	private Long id;// 红包领取id

	private Long redOrderId;// 红包订单id关联red_orders的主键

	private String status;// 领取状态(0待领取1已经领2平台回收3原路退回)

	private String pushUserCode;// 发红包用户编号

	private String pushUserName;// 发红包用户姓名

	private String pushUserPhone;// 发红包用户手机号

	private String getUserCode;// 领取用户编号

	private String getUserName;// 领取用户姓名

	private String getUserPhone;// 领取用户手机号

	private String pushType;// 发放人类型(0个人发红包、1平台发放红包、2组织发放红包)

	private String receiveType;// 接收类型(0单个定下、1群抢)

	private String busType;// 业务类型(0个人发红包1新用户红包、2信用卡奖励红包、3贷款奖励红包、4登录红包、5敲门红包)

	private Long orgId;// 发放人组织id

	private String orgName;// 发放人组织名称

	private Date createDate;// 红包生成时间

	private Date getDate;// 红包领取时间
	private Date getDateMin;
	private Date getDateMax;

	private String orderNo;// 关联业务订单号,对应订单表订单号,当没有值时填空字符串

	private Long confId;// 红包配置id(对应red_configure表主键)

	private BigDecimal amount;// 领取金额

	private Integer showNo;// 抢红包要匹配的幸运值

	private BigDecimal amountCount;// 选中记录的总金额

	private Integer sumState;// 是否是统计 0，查询 1统计

	private String nickName;// 微信昵称

	private String oldStatus;// 改变之前的状态

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

	public Long getRedOrderId() {
		return redOrderId;
	}

	public void setRedOrderId(Long redOrderId) {
		this.redOrderId = redOrderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPushUserCode() {
		return pushUserCode;
	}

	public void setPushUserCode(String pushUserCode) {
		this.pushUserCode = pushUserCode;
	}

	public String getPushUserName() {
		return pushUserName;
	}

	public void setPushUserName(String pushUserName) {
		this.pushUserName = pushUserName;
	}

	public String getPushUserPhone() {
		return pushUserPhone;
	}

	public void setPushUserPhone(String pushUserPhone) {
		this.pushUserPhone = pushUserPhone;
	}

	public String getGetUserCode() {
		return getUserCode;
	}

	public void setGetUserCode(String getUserCode) {
		this.getUserCode = getUserCode;
	}

	public String getGetUserName() {
		return getUserName;
	}

	public void setGetUserName(String getUserName) {
		this.getUserName = getUserName;
	}

	public String getGetUserPhone() {
		return getUserPhone;
	}

	public void setGetUserPhone(String getUserPhone) {
		this.getUserPhone = getUserPhone;
	}

	public String getPushType() {
		return pushType;
	}

	public void setPushType(String pushType) {
		this.pushType = pushType;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getGetDate() {
		return getDate;
	}

	public void setGetDate(Date getDate) {
		this.getDate = getDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getConfId() {
		return confId;
	}

	public void setConfId(Long confId) {
		this.confId = confId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getShowNo() {
		return showNo;
	}

	public void setShowNo(Integer showNo) {
		this.showNo = showNo;
	}

	public Date getGetDateMin() {
		return getDateMin;
	}

	public void setGetDateMin(Date getDateMin) {
		this.getDateMin = getDateMin;
	}

	public Date getGetDateMax() {
		return getDateMax;
	}

	public void setGetDateMax(Date getDateMax) {
		this.getDateMax = getDateMax;
	}

	public BigDecimal getAmountCount() {
		return amountCount;
	}

	public void setAmountCount(BigDecimal amountCount) {
		this.amountCount = amountCount;
	}

	public Integer getSumState() {
		return sumState;
	}

	public void setSumState(Integer sumState) {
		this.sumState = sumState;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(String oldStatus) {
		this.oldStatus = oldStatus;
	}
}
