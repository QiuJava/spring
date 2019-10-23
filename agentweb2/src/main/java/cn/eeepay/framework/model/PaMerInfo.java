package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 盟主商户
 * @author RPC
 * create 2018/09/11
 */
public class PaMerInfo {
	private String merchantNo;// 商户编号
	private String merchantName;// 商户名称
	private String mobilePhone;// 商户手机号码
	private String userCode;// 所属盟主编号

	private String realName;// 所属盟主编号
	private String mobile;// 所属盟主手机

	private String loginUserType;// '用户类型 1：机构，2：大盟主，3：盟主'

	private Date createTime;// 注册时间-创建时间
	private String agentNode;
	private String agentNo;
	private String loginUserCode;
	private String loginUserNode;// 所属盟主编号节点
	private String nickName;// 盟主昵称
	private String merType;// 商户类型
	private Date actTime;// 激活时间
	private String startCreateTime;
	private String endCreateTime;
	private String startActTime;
	private String endActTime;
	private String status;// 是否已认证
	private String isAct;// 是否激活 0 未激活 1 已激活\r\n',
	private String isMerUser;// 是否商户成为盟主
	private String bindTer;// 1已绑定过0未绑定

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getIsAct() {
		return isAct;
	}

	public void setIsAct(String isAct) {
		this.isAct = isAct;
	}

	public String getIsMerUser() {
		return isMerUser;
	}

	public void setIsMerUser(String isMerUser) {
		this.isMerUser = isMerUser;
	}

	public String getBindTer() {
		return bindTer;
	}

	public void setBindTer(String bindTer) {
		this.bindTer = bindTer;
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

	public String getStartActTime() {
		return startActTime;
	}

	public void setStartActTime(String startActTime) {
		this.startActTime = startActTime;
	}

	public String getEndActTime() {
		return endActTime;
	}

	public void setEndActTime(String endActTime) {
		this.endActTime = endActTime;
	}

	public Date getActTime() {
		return actTime;
	}

	public void setActTime(Date actTime) {
		this.actTime = actTime;
	}

	public String getMerType() {
		return merType;
	}

	public void setMerType(String merType) {
		this.merType = merType;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getLoginUserType() {
		return loginUserType;
	}

	public void setLoginUserType(String loginUserType) {
		this.loginUserType = loginUserType;
	}

	public String getLoginUserNode() {
		return loginUserNode;
	}

	public void setLoginUserNode(String loginUserNode) {
		this.loginUserNode = loginUserNode;
	}

	public String getLoginUserCode() {
		return loginUserCode;
	}

	public void setLoginUserCode(String loginUserCode) {
		this.loginUserCode = loginUserCode;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
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

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "PaMerInfo{" + "merchantNo='" + merchantNo + '\'' + ", merchantName='" + merchantName + '\''
				+ ", mobilePhone='" + mobilePhone + '\'' + ", userCode='" + userCode + '\'' + ", realName='" + realName
				+ '\'' + ", mobile='" + mobile + '\'' + ", createTime=" + createTime + ", agentNode='" + agentNode
				+ '\'' + ", loginUserCode='" + loginUserCode + '\'' + '}';
	}
}
