package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 售后订单
 * 
 * @author Administrator
 *
 */
public class PaAfterSale {

	private Long id;
	private String userCode;
	private String orderNo;
	private String payOrder;
	private String saleType;
	private String applyDesc;
	private String applyImg;
	private String applyImg1;
	private String applyImg2;
	private String applyImg3;
	private Date applyTime;
	private String dealDesc;
	private String dealImg;
	private String dealImg1;
	private String dealImg2;
	private String dealImg3;
	private Date dealTime;
	private String status;
	private Date createTime;
	private String handler;
	private String applyTimeBegin;
	private String applyTimeEnd;
	private String dealTimeBegin;
	private String dealTimeEnd;
	private String userNode;

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

	public String getApplyImg1() {
		return applyImg1;
	}

	public void setApplyImg1(String applyImg1) {
		this.applyImg1 = applyImg1;
	}

	public String getApplyImg2() {
		return applyImg2;
	}

	public void setApplyImg2(String applyImg2) {
		this.applyImg2 = applyImg2;
	}

	public String getApplyImg3() {
		return applyImg3;
	}

	public void setApplyImg3(String applyImg3) {
		this.applyImg3 = applyImg3;
	}

	public String getDealImg1() {
		return dealImg1;
	}

	public void setDealImg1(String dealImg1) {
		this.dealImg1 = dealImg1;
	}

	public String getDealImg2() {
		return dealImg2;
	}

	public void setDealImg2(String dealImg2) {
		this.dealImg2 = dealImg2;
	}

	public String getDealImg3() {
		return dealImg3;
	}

	public void setDealImg3(String dealImg3) {
		this.dealImg3 = dealImg3;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(String payOrder) {
		this.payOrder = payOrder;
	}

	public String getSaleType() {
		return saleType;
	}

	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}

	public String getApplyDesc() {
		return applyDesc;
	}

	public void setApplyDesc(String applyDesc) {
		this.applyDesc = applyDesc;
	}

	public String getApplyImg() {
		return applyImg;
	}

	public void setApplyImg(String applyImg) {
		this.applyImg = applyImg;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getDealDesc() {
		return dealDesc;
	}

	public void setDealDesc(String dealDesc) {
		this.dealDesc = dealDesc;
	}

	public String getDealImg() {
		return dealImg;
	}

	public void setDealImg(String dealImg) {
		this.dealImg = dealImg;
	}

	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
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

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getApplyTimeBegin() {
		return applyTimeBegin;
	}

	public void setApplyTimeBegin(String applyTimeBegin) {
		this.applyTimeBegin = applyTimeBegin;
	}

	public String getApplyTimeEnd() {
		return applyTimeEnd;
	}

	public void setApplyTimeEnd(String applyTimeEnd) {
		this.applyTimeEnd = applyTimeEnd;
	}

	public String getDealTimeBegin() {
		return dealTimeBegin;
	}

	public void setDealTimeBegin(String dealTimeBegin) {
		this.dealTimeBegin = dealTimeBegin;
	}

	public String getDealTimeEnd() {
		return dealTimeEnd;
	}

	public void setDealTimeEnd(String dealTimeEnd) {
		this.dealTimeEnd = dealTimeEnd;
	}

}
