package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class ScanCodeTrans implements Serializable{
	private static final long serialVersionUID = 1L;


	private String tradeNo; //'主键 订单号',
	private String tradeType;//'交易类型(alipay:支付宝；weixin:微信)',
	private String authCode; // '授权码、二维码信息（生成二维码支付此列为空）',
	private Integer resultCode; // '请求通信标识,是否创建此订单(0表示成功,非0表示失败)',
	private String tradeState; // '交易状态（INIT—初始化;SUCCESS—支付成功;REFUND—转入退款;NOTPAY—未支付;CLOSED—已关闭;REVERSE—已冲正;REVOK—已撤销;FAILED:交易失败）',
	private String merchantNo; // '商户号，外键商户表',
	private String  acqEnname; // '收单机构',
	private String acqTransId; // '收单机构返回的订单ID',
	private String acqMerchantNo; // '收单机构商户编号',
	private BigDecimal totalFee; // '支付金额',
	private BigDecimal merchantFee; // '商户手续费',
	private BigDecimal acqFee; // '收单机构手续费',
	private BigDecimal refundFee; // '退款金额（交易状态为退款是有值）',
	private String refundChannel; // '退款渠道（交易状态为退款是有值）ORIGINAL-原路退款，BALANCE-余额，默认ORIGINAL',
	private String mchCreateIp; // '客户端IP',
	private String nonceStr; // '随机字符串，不长于 32 位',
	private String  message; // '返回信息',
	private String codeUrl; // '二维码链接',
	private String codeImgUrl; // '二维码图片',
	private String openid; // '用户支付宝的账户名，或者微信支付银行订单号',
	private String clientType; //'手机系统类型（android/ios）',
	private String jpushId; // '推送Id',
	private Integer isT0; //'结算方式 0 t0 ,1 t1',
	private Date timeStart; // '订单生成时间',
	private Date timeExpire; // '订单失效时间',
	private Date timeEnd; // '支付完成时间',
	private Date timeLast; // '最后操作时间(结算时间)',
	private String settleStatus; // '结算状态',
	public String getTradeNo() {
		return tradeNo;
	}
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	public String getTradeType() {
		return tradeType;
	}
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public Integer getResultCode() {
		return resultCode;
	}
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	public String getTradeState() {
		return tradeState;
	}
	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getAcqTransId() {
		return acqTransId;
	}
	public void setAcqTransId(String acqTransId) {
		this.acqTransId = acqTransId;
	}
	public String getAcqMerchantNo() {
		return acqMerchantNo;
	}
	public void setAcqMerchantNo(String acqMerchantNo) {
		this.acqMerchantNo = acqMerchantNo;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	public BigDecimal getMerchantFee() {
		return merchantFee;
	}
	public void setMerchantFee(BigDecimal merchantFee) {
		this.merchantFee = merchantFee;
	}
	public BigDecimal getAcqFee() {
		return acqFee;
	}
	public void setAcqFee(BigDecimal acqFee) {
		this.acqFee = acqFee;
	}
	public BigDecimal getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}
	public String getRefundChannel() {
		return refundChannel;
	}
	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}
	public String getMchCreateIp() {
		return mchCreateIp;
	}
	public void setMchCreateIp(String mchCreateIp) {
		this.mchCreateIp = mchCreateIp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCodeUrl() {
		return codeUrl;
	}
	public void setCodeUrl(String codeUrl) {
		this.codeUrl = codeUrl;
	}
	public String getCodeImgUrl() {
		return codeImgUrl;
	}
	public void setCodeImgUrl(String codeImgUrl) {
		this.codeImgUrl = codeImgUrl;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getClientType() {
		return clientType;
	}
	public void setClientType(String clientType) {
		this.clientType = clientType;
	}
	public String getJpushId() {
		return jpushId;
	}
	public void setJpushId(String jpushId) {
		this.jpushId = jpushId;
	}
	public Integer getIsT0() {
		return isT0;
	}
	public void setIsT0(Integer isT0) {
		this.isT0 = isT0;
	}
	public Date getTimeStart() {
		return timeStart;
	}
	public void setTimeStart(Date timeStart) {
		this.timeStart = timeStart;
	}
	public Date getTimeExpire() {
		return timeExpire;
	}
	public void setTimeExpire(Date timeExpire) {
		this.timeExpire = timeExpire;
	}
	public Date getTimeEnd() {
		return timeEnd;
	}
	public void setTimeEnd(Date timeEnd) {
		this.timeEnd = timeEnd;
	}
	public Date getTimeLast() {
		return timeLast;
	}
	public void setTimeLast(Date timeLast) {
		this.timeLast = timeLast;
	}
	public String getSettleStatus() {
		return settleStatus;
	}
	public void setSettleStatus(String settleStatus) {
		this.settleStatus = settleStatus;
	}
	
	
}
