package cn.eeepay.framework.model;

import java.util.Date;

/**
 * 盟主
 * 
 * @author MXG create 2018/07/11
 */
public class PaUserInfo {
	private String userCode;// 盟主编号
	private String userNode;// 盟主节点
	private String realName;// 盟主姓名
	private String mobile;// 注册手机
	private String grade;// 盟主称号
	private String parentId;// 上级编号
	private String parentName;// 上级名称
	private String parentType;// 上级类型
	private String agentNo;// 机构编号
	private String agentName;// 机构姓名
	private String agentNode;// 代理商节点
	private String shareLevel;// 分润级别
	private String levelShow;// 节点展示
	private Date createTime;// 注册时间-创建时间
	private String createTimeBegin;
	private String createTimeEnd;
	private Long merTotal;// 直营商户数量
	private Long allyTotal;// 直营盟主数量
	private String isBindCard;// 是否认证（绑卡表示认证） 0-未认证 1-已认证
	private String isDirect;// 是否为登录账户的直属下级
	private String loginUserCode;// 登录账号的userCode
	private String userType;
	private String pwd;// 登录密码
	private String canProfitChange;// 是否允许分润调账 0 不允许 1 允许
	private Integer allMerTotal;// 盟友商户激活数,包括下级所有
	private String idCardNo;
	private String bankName;
	private String agentStatus;
	private String nickName;
	private String vipLevelShow;// vip分润比例

	public String getVipLevelShow() {
		return vipLevelShow;
	}

	public void setVipLevelShow(String vipLevelShow) {
		this.vipLevelShow = vipLevelShow;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAgentStatus() {
		return agentStatus;
	}

	public void setAgentStatus(String agentStatus) {
		this.agentStatus = agentStatus;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public Integer getAllMerTotal() {
		return allMerTotal;
	}

	public void setAllMerTotal(Integer allMerTotal) {
		this.allMerTotal = allMerTotal;
	}

	public String getCanProfitChange() {
		return canProfitChange;
	}

	public void setCanProfitChange(String canProfitChange) {
		this.canProfitChange = canProfitChange;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLevelShow() {
		return levelShow;
	}

	public void setLevelShow(String levelShow) {
		this.levelShow = levelShow;
	}

	public String getLoginUserCode() {
		return loginUserCode;
	}

	public void setLoginUserCode(String loginUserCode) {
		this.loginUserCode = loginUserCode;
	}

	public String getUserNode() {
		return userNode;
	}

	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}

	public String getIsDirect() {
		return isDirect;
	}

	public void setIsDirect(String isDirect) {
		this.isDirect = isDirect;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

	public String getIsBindCard() {
		return isBindCard;
	}

	public void setIsBindCard(String isBindCard) {
		this.isBindCard = isBindCard;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	/*
	 * public String getStatus() { return status; }
	 * 
	 * public void setStatus(String status) { this.status = status; }
	 */

	public String getShareLevel() {
		return shareLevel;
	}

	public void setShareLevel(String shareLevel) {
		this.shareLevel = shareLevel;
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

	public Long getMerTotal() {
		return merTotal;
	}

	public void setMerTotal(Long merTotal) {
		this.merTotal = merTotal;
	}

	public Long getAllyTotal() {
		return allyTotal;
	}

	public void setAllyTotal(Long allyTotal) {
		this.allyTotal = allyTotal;
	}
}
