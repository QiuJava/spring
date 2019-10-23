package cn.eeepay.framework.model.peragent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class PaShareDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Integer id;
	private String userCode;//用户编号
	private String userNode;//用户节点
	private String grade;//身份
	private Integer userLevel; //用户级别
	private String shareType;//分润类型 1 交易分润 2 管理津贴 3 皇冠奖金  4 钻石奖金
	
	private BigDecimal shareAmount; //分润金额
	private BigDecimal transAmount; //交易金额(交易分润下有效)
	private String TransAmountStr;
	private BigDecimal goldAmount; //黄金盟友奖励金(钻石奖金下有效)
	
	private Integer countGold; //团队黄金盟友个数(钻石奖金下有效)
	private Integer countTeamUser; //团队盟友个数
	
	private BigDecimal teamTotalAmount; //团队总交易额
	private BigDecimal totalAmount; //用户总交易额
	
	private String shareMonth;//分润月份
	private String accStatus;//入账状态 NOENTERACCOUNT未入账,ENTERACCOUNTED已入账
	private String accMessage;//入账信息
	private String accOperator;//入账操作人
	
	private Date accTime;//入账时间
	private Date createTime; //创建时间
	
	private String brandCode;//所属品牌
	private String userType; //用户类别
	private String realName;//用户名称
	
	private String accUserAgent;//实际入账账号
	
	private String oneUserCode;//所属机构
	
	private String twoUserCode;//所属大盟主

	private String accTime1;//入账时间
	private String accTime2;//入账时间
	private String createTime1; //创建时间
	private String createTime2; //创建时间
	private String transNo;
	private Integer shareLevel;
	private String shareLevelStr;
	private BigDecimal shareRatio;
	private String shareRatioStr;
	
	private String honourShareRatioStr;
	private String setterNo;
	private String setterAmountStr;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getUserNode() {
		return userNode;
	}
	public void setUserNode(String userNode) {
		this.userNode = userNode;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public String getShareType() {
		return shareType;
	}
	public void setShareType(String shareType) {
		this.shareType = shareType;
	}
	public BigDecimal getShareAmount() {
		return shareAmount;
	}
	public void setShareAmount(BigDecimal shareAmount) {
		this.shareAmount = shareAmount;
	}
	public BigDecimal getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(BigDecimal transAmount) {
		this.transAmount = transAmount;
	}
	public BigDecimal getGoldAmount() {
		return goldAmount;
	}
	public void setGoldAmount(BigDecimal goldAmount) {
		this.goldAmount = goldAmount;
	}
	public Integer getCountGold() {
		return countGold;
	}
	public void setCountGold(Integer countGold) {
		this.countGold = countGold;
	}
	public Integer getCountTeamUser() {
		return countTeamUser;
	}
	public void setCountTeamUser(Integer countTeamUser) {
		this.countTeamUser = countTeamUser;
	}
	public BigDecimal getTeamTotalAmount() {
		return teamTotalAmount;
	}
	public void setTotalTeamAmount(BigDecimal teamTotalAmount) {
		this.teamTotalAmount = teamTotalAmount;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getShareMonth() {
		return shareMonth;
	}
	public void setShareMonth(String shareMonth) {
		this.shareMonth = shareMonth;
	}
	public String getAccStatus() {
		return accStatus;
	}
	public void setAccStatus(String accStatus) {
		this.accStatus = accStatus;
	}
	public String getAccMessage() {
		return accMessage;
	}
	public void setAccMessage(String accMessage) {
		this.accMessage = accMessage;
	}
	public String getAccOperator() {
		return accOperator;
	}
	public void setAccOperator(String accOperator) {
		this.accOperator = accOperator;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getAccTime() {
		return accTime;
	}
	public void setAccTime(Date accTime) {
		this.accTime = accTime;
	}
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getAccTime1() {
		return accTime1;
	}
	public void setAccTime1(String accTime1) {
		this.accTime1 = accTime1;
	}
	public String getAccTime2() {
		return accTime2;
	}
	public void setAccTime2(String accTime2) {
		this.accTime2 = accTime2;
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
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getAccUserAgent() {
		return accUserAgent;
	}
	public void setAccUserAgent(String accUserAgent) {
		this.accUserAgent = accUserAgent;
	}
	public void setTeamTotalAmount(BigDecimal teamTotalAmount) {
		this.teamTotalAmount = teamTotalAmount;
	}
	public String getOneUserCode() {
		return oneUserCode;
	}
	public void setOneUserCode(String oneUserCode) {
		this.oneUserCode = oneUserCode;
	}
	public String getTwoUserCode() {
		return twoUserCode;
	}
	public void setTwoUserCode(String twoUserCode) {
		this.twoUserCode = twoUserCode;
	}
	public String getTransNo() {
		return transNo;
	}
	public void setTransNo(String transNo) {
		this.transNo = transNo;
	}
	public Integer getShareLevel() {
		return shareLevel;
	}
	public void setShareLevel(Integer shareLevel) {
		this.shareLevel = shareLevel;
	}
	public String getShareLevelStr() {
		return shareLevelStr;
	}
	public void setShareLevelStr(String shareLevelStr) {
		this.shareLevelStr = shareLevelStr;
	}
	public BigDecimal getShareRatio() {
		return shareRatio;
	}
	public void setShareRatio(BigDecimal shareRatio) {
		this.shareRatio = shareRatio;
	}
	public String getShareRatioStr() {
		return shareRatioStr;
	}
	public void setShareRatioStr(String shareRatioStr) {
		this.shareRatioStr = shareRatioStr;
	}
	public String getTransAmountStr() {
		return TransAmountStr;
	}
	public void setTransAmountStr(String transAmountStr) {
		TransAmountStr = transAmountStr;
	}
	public String getSetterNo() {
		return setterNo;
	}
	public void setSetterNo(String setterNo) {
		this.setterNo = setterNo;
	}
	public String getSetterAmountStr() {
		return setterAmountStr;
	}
	public void setSetterAmountStr(String setterAmountStr) {
		this.setterAmountStr = setterAmountStr;
	}
	public String getHonourShareRatioStr() {
		return honourShareRatioStr;
	}
	public void setHonourShareRatioStr(String honourShareRatioStr) {
		this.honourShareRatioStr = honourShareRatioStr;
	}
    

}
