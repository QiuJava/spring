package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author MXG create 2018/07/14
 */
public class PaOrder {
	private String agentNo;
	private String agentNode;// 用户所属代理商节点
	private String orderNo;// 订单号
	private String userCode;// 申购盟主编号
	private String userName;// 申购盟主姓名
	private Integer userType;// 用户类型 如果为盟主：表示发货给盟主 如果为盟主，表示发货给二级代理商
	private String gName;// 商品名称
	private String img;// 商品图片地址
	private Integer num;
	private BigDecimal totalAmount;// 订单金额
	private String transChannel;// 支付方式
	private String orderStatus;// 订单状态
	private String receiver;// 收件人
	private String receiverMobile;// 收件人手机号
	private String receiverAddress;// 收件人地址
	private Date sendTime;// 发货日期
	private Date createTime;// 创建时间
	private Date receiptDate;// 确认收货日期
	private Date transTime;// 支付日期
	private Date accTime;// 机具分润入账日期
	private Date entryTime;// 机具款项入账日期
	private String createTimeBegin;
	private String createTimeEnd;
	private String transTimeBegin;
	private String transTimeEnd;
	private String sendTimeBegin;
	private String sendTimeEnd;
	private String receiptDateBegin;
	private String receiptDateEnd;
	private String transportCompany;// 快递公司
	private String postNo;// 物流单号
	private String sn;// sn字符串，sn号之间以逗号隔开
	private String snStart;
	private String snEnd;
	private String errorMsg;// 错误信息
	private String sendType;// 订单类型
	private String isPlatform;// 发货方
	private BigDecimal goodsTotal;// 机具款项金额
	private String entryStatus;// 机具款项入账状态
	private BigDecimal shareAmount;// 机具分润金额
	private String accStatus;// 机具分润入账状态
	private String entryTimeBegin;
	private String entryTimeEnd;
	private String accTimeBegin;
	private String accTimeEnd;
	private String size;
	private String color;
	private BigDecimal price;// 销售单价
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getEntryTimeBegin() {
		return entryTimeBegin;
	}

	public void setEntryTimeBegin(String entryTimeBegin) {
		this.entryTimeBegin = entryTimeBegin;
	}

	public String getEntryTimeEnd() {
		return entryTimeEnd;
	}

	public void setEntryTimeEnd(String entryTimeEnd) {
		this.entryTimeEnd = entryTimeEnd;
	}

	public String getAccTimeBegin() {
		return accTimeBegin;
	}

	public void setAccTimeBegin(String accTimeBegin) {
		this.accTimeBegin = accTimeBegin;
	}

	public String getAccTimeEnd() {
		return accTimeEnd;
	}

	public void setAccTimeEnd(String accTimeEnd) {
		this.accTimeEnd = accTimeEnd;
	}

	public PaOrder() {
	}

	public PaOrder(String orderNo, String transportCompany, String postNo, String snStart, String snEnd) {
		this.orderNo = orderNo;
		this.transportCompany = transportCompany;
		this.postNo = postNo;
		this.snStart = snStart;
		this.snEnd = snEnd;
	}

	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public String getIsPlatform() {
		return isPlatform;
	}

	public void setIsPlatform(String isPlatform) {
		this.isPlatform = isPlatform;
	}

	public BigDecimal getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(BigDecimal goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public String getEntryStatus() {
		return entryStatus;
	}

	public void setEntryStatus(String entryStatus) {
		this.entryStatus = entryStatus;
	}

	public BigDecimal getShareAmount() {
		return shareAmount;
	}

	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}

	public String getAccStatus() {
		return accStatus;
	}

	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getTransChannel() {
		return transChannel;
	}

	public void setTransChannel(String transChannel) {
		this.transChannel = transChannel;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(String createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public String getTransTimeBegin() {
		return transTimeBegin;
	}

	public void setTransTimeBegin(String transTimeBegin) {
		this.transTimeBegin = transTimeBegin;
	}

	public String getTransTimeEnd() {
		return transTimeEnd;
	}

	public Date getAccTime() {
		return accTime;
	}

	public void setAccTime(Date accTime) {
		this.accTime = accTime;
	}

	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public void setTransTimeEnd(String transTimeEnd) {
		this.transTimeEnd = transTimeEnd;
	}

	public String getSendTimeBegin() {
		return sendTimeBegin;
	}

	public void setSendTimeBegin(String sendTimeBegin) {
		this.sendTimeBegin = sendTimeBegin;
	}

	public String getSendTimeEnd() {
		return sendTimeEnd;
	}

	public void setSendTimeEnd(String sendTimeEnd) {
		this.sendTimeEnd = sendTimeEnd;
	}

	public String getReceiptDateBegin() {
		return receiptDateBegin;
	}

	public void setReceiptDateBegin(String receiptDateBegin) {
		this.receiptDateBegin = receiptDateBegin;
	}

	public String getReceiptDateEnd() {
		return receiptDateEnd;
	}

	public void setReceiptDateEnd(String receiptDateEnd) {
		this.receiptDateEnd = receiptDateEnd;
	}

	public String getTransportCompany() {
		return transportCompany;
	}

	public void setTransportCompany(String transportCompany) {
		this.transportCompany = transportCompany;
	}

	public String getPostNo() {
		return postNo;
	}

	public void setPostNo(String postNo) {
		this.postNo = postNo;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public String getSnStart() {
		return snStart;
	}

	public void setSnStart(String snStart) {
		this.snStart = snStart;
	}

	public String getSnEnd() {
		return snEnd;
	}

	public void setSnEnd(String snEnd) {
		this.snEnd = snEnd;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
