package cn.eeepay.framework.model;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;

/**
 * 超级银行家user_info表
 * 
 * @author Administrator
 *
 */
public class UserInfoSuperBank {

	private Long userId;
	private String userCode;// 用户编号,代理编号
	private String userName;
	private String nickName;
	private String phone;
	private String idCardNo;
	private String weixinCode;
	private String userType;// 代理身份1:普通用户； 2专员；3经理；4银行家'
	private String userLogo;// 微信头像
	private String qrCode;// 微信二维码
	private Long orgId;// 组织id
	private String status;
	private String statusAgent;
	private String statusRepayment;
	private String statusReceive;
	private BigDecimal totalProfit;// 总收益
	private String createBy;
	private Date createDate;// 入驻时间
	private Date toagentDate;// 成为专员时间
	private Date toManagerDate;// 成为经理时间
	private Date toBankerDate;// 成为银行家时间
	private String topOneCode;
	private String topTwoCode;
	private String topThreeCode;
	private String repaymentUserNo;// 超级还用户编号
	private String receiveUserNo;// 收款商户编号
	private String userNode;
	private Date updateDate;
	private String wxOpenId;// 微信唯一标识OPENID
	private Date startTime;
	private Date endTime;
	private String payBack;// 是否退款 1 已退款,0未退款
	private String hasAccount;// 是否开户
	private String orgName;// 所属组织名称
	private String topOneUserName;
	private String topOneUserType;
	private String topOneNickName;
	private String topTwoUserName;
	private String topTwoUserType;
	private String topTwoNickName;
	private String topThreeUserName;
	private String topThreeUserType;
	private String topThreeNickName;
	private String itemId;// 进件ID
	private String accountStatus;// 开户状态，0 未开户 1 已开户
	private String secondUserNode;// 二级代理节点
	private String oneAgentPhone;// 一级代理手机号
	private String twoAgentPhone;// 二级代理手机号
	private String threeAgentPhone;// 三级代理手机号
	private String oemDefaultUser;// 是否OEM默认用户 0-否 1-是

	private int ordinaryUserNum;// 普通用户总数量
	private int agentUserNum;// 代理数量
	private int commissionerUserNum;// 专员数量
	private int managerUserNum;// 经理数量
	private int BankerUserNum;// 银行家数量
	private String isAgent;// 当前登录账号是否是代理商

	private String isPay;// 是否缴费
	private String remark;// 备注
	private String openProvince;// 开通省份
	private String openCity;// 开通城市
	private String openRegion;// 开通地区
	private String address;

	private Date startPayDate;
	private Date endPayDate;

	public Date getToManagerDate() {
		return toManagerDate;
	}

	public void setToManagerDate(Date toManagerDate) {
		this.toManagerDate = toManagerDate;
	}

	public Date getToBankerDate() {
		return toBankerDate;
	}

	public void setToBankerDate(Date toBankerDate) {
		this.toBankerDate = toBankerDate;
	}

	public Date getStartPayDate() {
		return startPayDate;
	}

	public void setStartPayDate(Date startPayDate) {
		this.startPayDate = startPayDate;
	}

	public Date getEndPayDate() {
		return endPayDate;
	}

	public void setEndPayDate(Date endPayDate) {
		this.endPayDate = endPayDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getIsPay() {
		return isPay;
	}

	public void setIsPay(String isPay) {
		this.isPay = isPay;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOpenProvince() {
		return openProvince;
	}

	public void setOpenProvince(String openProvince) {
		this.openProvince = openProvince;
	}

	public String getOpenCity() {
		return openCity;
	}

	public void setOpenCity(String openCity) {
		this.openCity = openCity;
	}

	public String getOpenRegion() {
		return openRegion;
	}

	public void setOpenRegion(String openRegion) {
		this.openRegion = openRegion;
	}

	public String getIsAgent() {
		return isAgent;
	}

	public void setIsAgent(String isAgent) {
		this.isAgent = isAgent;
	}

	public String getOemDefaultUser() {
		return oemDefaultUser;
	}

	public void setOemDefaultUser(String oemDefaultUser) {
		this.oemDefaultUser = oemDefaultUser;
	}

	public void setOrdinaryUserNum(int ordinaryUserNum) {
		this.ordinaryUserNum = ordinaryUserNum;
	}

	public void setAgentUserNum(int agentUserNum) {
		this.agentUserNum = agentUserNum;
	}

	public void setCommissionerUserNum(int commissionerUserNum) {
		this.commissionerUserNum = commissionerUserNum;
	}

	public void setManagerUserNum(int managerUserNum) {
		this.managerUserNum = managerUserNum;
	}

	public void setBankerUserNum(int bankerUserNum) {
		BankerUserNum = bankerUserNum;
	}

	public Integer getOrdinaryUserNum() {
		return ordinaryUserNum;
	}

	public void setOrdinaryUserNum(Integer ordinaryUserNum) {
		this.ordinaryUserNum = ordinaryUserNum;
	}

	public Integer getAgentUserNum() {
		return agentUserNum;
	}

	public void setAgentUserNum(Integer agentUserNum) {
		this.agentUserNum = agentUserNum;
	}

	public Integer getCommissionerUserNum() {
		return commissionerUserNum;
	}

	public void setCommissionerUserNum(Integer commissionerUserNum) {
		this.commissionerUserNum = commissionerUserNum;
	}

	public Integer getManagerUserNum() {
		return managerUserNum;
	}

	public void setManagerUserNum(Integer managerUserNum) {
		this.managerUserNum = managerUserNum;
	}

	public Integer getBankerUserNum() {
		return BankerUserNum;
	}

	public void setBankerUserNum(Integer bankerUserNum) {
		BankerUserNum = bankerUserNum;
	}

	public String getOneAgentPhone() {
		return oneAgentPhone;
	}

	public void setOneAgentPhone(String oneAgentPhone) {
		this.oneAgentPhone = oneAgentPhone;
	}

	public String getTwoAgentPhone() {
		return twoAgentPhone;
	}

	public void setTwoAgentPhone(String twoAgentPhone) {
		this.twoAgentPhone = twoAgentPhone;
	}

	public String getThreeAgentPhone() {
		return threeAgentPhone;
	}

	public void setThreeAgentPhone(String threeAgentPhone) {
		this.threeAgentPhone = threeAgentPhone;
	}

	public String getSecondUserNode() {
		return secondUserNode;
	}

	public void setSecondUserNode(String secondUserNode) {
		this.secondUserNode = secondUserNode;
	}

	private BigDecimal totalAmount;// 总收益，取account_info

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getNickName() {
		if (StringUtils.isNotBlank(nickName)) {
			try {
				return URLDecoder.decode(nickName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		;
		return "";
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getWeixinCode() {
		return weixinCode;
	}

	public void setWeixinCode(String weixinCode) {
		this.weixinCode = weixinCode;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public String getQrCode() {
		return qrCode;
	}

	public void setQrCode(String qrCode) {
		this.qrCode = qrCode;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusAgent() {
		return statusAgent;
	}

	public void setStatusAgent(String statusAgent) {
		this.statusAgent = statusAgent;
	}

	public String getStatusRepayment() {
		return statusRepayment;
	}

	public void setStatusRepayment(String statusRepayment) {
		this.statusRepayment = statusRepayment;
	}

	public String getStatusReceive() {
		return statusReceive;
	}

	public void setStatusReceive(String statusReceive) {
		this.statusReceive = statusReceive;
	}

	public BigDecimal getTotalProfit() {
		return totalProfit;
	}

	public void setTotalProfit(BigDecimal totalProfit) {
		this.totalProfit = totalProfit;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getToagentDate() {
		return toagentDate;
	}

	public void setToagentDate(Date toagentDate) {
		this.toagentDate = toagentDate;
	}

	public String getTopOneCode() {
		return topOneCode;
	}

	public void setTopOneCode(String topOneCode) {
		this.topOneCode = topOneCode;
	}

	public String getTopTwoCode() {
		return topTwoCode;
	}

	public void setTopTwoCode(String topTwoCode) {
		this.topTwoCode = topTwoCode;
	}

	public String getTopThreeCode() {
		return topThreeCode;
	}

	public void setTopThreeCode(String topThreeCode) {
		this.topThreeCode = topThreeCode;
	}

	public String getRepaymentUserNo() {
		return repaymentUserNo;
	}

	public void setRepaymentUserNo(String repaymentUserNo) {
		this.repaymentUserNo = repaymentUserNo;
	}

	public String getReceiveUserNo() {
		return receiveUserNo;
	}

	public void setReceiveUserNo(String receiveUserNo) {
		this.receiveUserNo = receiveUserNo;
	}

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getWxOpenId() {
		return wxOpenId;
	}

	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getPayBack() {
		return payBack;
	}

	public void setPayBack(String payBack) {
		this.payBack = payBack;
	}

	public String getHasAccount() {
		return hasAccount;
	}

	public void setHasAccount(String hasAccount) {
		this.hasAccount = hasAccount;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getTopOneUserName() {
		return topOneUserName;
	}

	public void setTopOneUserName(String topOneUserName) {
		this.topOneUserName = topOneUserName;
	}

	public String getTopOneUserType() {
		return topOneUserType;
	}

	public void setTopOneUserType(String topOneUserType) {
		this.topOneUserType = topOneUserType;
	}

	public String getTopTwoUserName() {
		return topTwoUserName;
	}

	public void setTopTwoUserName(String topTwoUserName) {
		this.topTwoUserName = topTwoUserName;
	}

	public String getTopTwoUserType() {
		return topTwoUserType;
	}

	public void setTopTwoUserType(String topTwoUserType) {
		this.topTwoUserType = topTwoUserType;
	}

	public String getTopThreeUserName() {
		return topThreeUserName;
	}

	public void setTopThreeUserName(String topThreeUserName) {
		this.topThreeUserName = topThreeUserName;
	}

	public String getTopThreeUserType() {
		return topThreeUserType;
	}

	public void setTopThreeUserType(String topThreeUserType) {
		this.topThreeUserType = topThreeUserType;
	}

	public String getTopOneNickName() {
		if (StringUtils.isNotBlank(topOneNickName)) {
			try {
				return URLDecoder.decode(topOneNickName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		;
		return topOneNickName;
	}

	public void setTopOneNickName(String topOneNickName) {
		this.topOneNickName = topOneNickName;
	}

	public String getTopTwoNickName() {
		if (StringUtils.isNotBlank(topTwoNickName)) {
			try {
				return URLDecoder.decode(topTwoNickName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		;
		return "";
	}

	public void setTopTwoNickName(String topTwoNickName) {
		this.topTwoNickName = topTwoNickName;
	}

	public String getTopThreeNickName() {
		if (StringUtils.isNotBlank(topThreeNickName)) {
			try {
				return URLDecoder.decode(topThreeNickName, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		;
		return "";
	}

	public void setTopThreeNickName(String topThreeNickName) {
		this.topThreeNickName = topThreeNickName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus;
	}
}
