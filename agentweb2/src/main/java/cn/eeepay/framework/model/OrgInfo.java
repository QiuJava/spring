package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super.org_info desc:超级银行家组织表
 * 
 * @author tans
 * @date 2017-11-29
 */
public class OrgInfo {
	private Long orgId;// 组织ID

	private String orgName;// 组织名称

	private String orgLogo;// 组织图标

	private String orgLogoUrl;// 组织图标地址

	private String weixinSign;// 微信公众号

	private String superOrgcode;// 超级还组织编号

	private String v2AgentNumber;// V2代理商编号

	private String v2Orgcode;// V2组织编号

	private Long agentPrice;// 代理价格

	private String agentCost;// 产品代理成本

	private String agentPushCost;// 产品代理发放成本

	private String receiveCost;// 产品收款成本

	private String receivePushCost;// 产品收款发放成本

	private String repaymentCost;// 产品超级还款成本

	private String repaymentPushCost;// 产品超级还款发放成本

	private Integer upManagerNum;// 产品超级还款发放成本

	private Integer upManagerCardnum;// 升经理需发展卡数

	private Integer upBankerNum;// 升银行家需发展个数

	private Integer upBankerCardnum;// 升银行家需发展卡数

	private Long updateBy;// 修改人

	private Date updateDate;// 修改时间

	private Date createDate;// 创建时间

	private String remark;// 备注

	private String authorizedUnitSeal;// 授权单位印章
	private String memberCenterLogo;// 会员中心LOGO
	private String startPage;// 启动页
	private String appLogo;// APP LOGO
	private String publicQrCode;// 公众号二维码
	private String shareMessageLogo;// 分享消息LOGO
	private String shareTemplateImage1;// 分享模板图片1
	private String shareTemplateImage1Url;// 分享模板图片1
	private String shareTemplateImage2;// 分享模板图片2
	private String shareTemplateImage2Url;// 分享模板图片2
	private String shareTemplateImage3;// 分享模板图片3
	private String shareTemplateImage3Url;// 分享模板图片3
	private String authorizedUnitSealUrl;// 授权单位印章url
	private String memberCenterLogoUrl;// 会员中心LOGO
	private String startPageUrl;// 启动页
	private String appLogoUrl;// APP LOGO
	private String publicQrCodeUrl;// 公众号二维码
	private String shareMessageLogoUrl;// 分享消息LOGO
	private Double tradeFeeRate;// 超级还交易费率
	private Double tradeSingleFee;// 超级还交易单笔手续费
	private Double withdrawFeeRate;// 超级还提现费率
	private Double withdrawSingleFee;// 超级还提现单笔手续费
	private String tradeFeeRateStr;// 超级还交易费率
	private String tradeSingleFeeStr;// 超级还交易单笔手续费
	private String withdrawFeeRateStr;// 超级还提现费率
	private String withdrawSingleFeeStr;// 超级还提现单笔手续费

	private String publicAccount;// 公众号
	private String servicePhone;// 客服电话
	private String authorizedUnit;// 授权单位
	private String companyNo;// 公司编号
	private String companyName;// 公司名称
	private String publicAccountName;// 公众号名称
	private String appid;// 公众号appid
	private String secret;// 公众号secret
	private String encodingAesKey;// 公众号密文
	private String businessEmail;// 商务邮箱
	private String uiStatus;// 平台UI风格，0超级银行家(自用) 1金色 2橙色 3黄色
	private String isSupportPay;// 是否支持支付 0-否 1-是
	private BigDecimal openRepayPrice;// 开通超级还售价
	private String openRepayCost;// 开通超级还代理成本
	private String openRepayPushCost;// 开通超级还发放成本
	private String receiveKjCost;// 产品收款快捷成本
	private String dayStart;// 当月day_start号到day_end号
	private String dayEnd;// 当月day_start号到day_end号
	private String monthCardNum;// 当月day_start号到day_end号，直推办理month_card_num张卡
	private String yearCardNum;// 当年累计直推办理多少张卡
	private BigDecimal redMoneyMin;// 红包随机金额，最小
	private BigDecimal redMoneyMax;// 红包随机金额，最大
	private BigDecimal withdrawMoneyMin;// 超过多少元的才能提现

	private String notifyUrl;// 订单结果通知地址,授权回调地址
	private String batchOrderNotifyUrl;// 批量订单结果通知地址
	private String openAppUrl;// 外放平台APP首页地址
	private String openAppName;// 外放组织应用名称
	private String openAppIntro;// 外放组织应用简介
	private String openMerchantKey;// 商户应用密钥
	private String keyVersion;// 商户应用密钥版本

	private String firstPage;// 银行家功能首页
	private String creditCard;// 办理信用卡
	private String handleLoan;// 办理贷款
	private String queryProgress;// 查询办卡进度
	private String queryOrder;// 订单查询
	private String peccancy;// 违章代缴
	private String repayCard;// 信用卡还款
	private String merchantReceivables;// 商户收款
	private String personalInfomation;// 个人信息
	private String isEnable;// 功能是否开通
	private Integer isEnable4;// 功能是否开通
	private Integer isEnable6;// 功能是否开通
	private Integer isEnable7;// 功能是否开通
	private Integer isEnable8;// 功能是否开通
	private Integer isEnableFirstPage;// 功能是否开通
	private Integer isEnableCreditCard;// 功能是否开通
	private Integer isEnableQueryOrder;// 功能是否开通
	private Integer isEnablePersonalInfomation;// 功能是否开通
	private Integer isEnableQueryProgress;// 功能是否开通
	private String getUserInfoUrl;// 用户信息查询地址

	public String getGetUserInfoUrl() {
		return getUserInfoUrl;
	}

	public void setGetUserInfoUrl(String getUserInfoUrl) {
		this.getUserInfoUrl = getUserInfoUrl;
	}

	public Integer getIsEnableQueryProgress() {
		return isEnableQueryProgress;
	}

	public void setIsEnableQueryProgress(Integer isEnableQueryProgress) {
		this.isEnableQueryProgress = isEnableQueryProgress;
	}

	public Integer getIsEnableFirstPage() {
		return isEnableFirstPage;
	}

	public void setIsEnableFirstPage(Integer isEnableFirstPage) {
		this.isEnableFirstPage = isEnableFirstPage;
	}

	public Integer getIsEnableCreditCard() {
		return isEnableCreditCard;
	}

	public void setIsEnableCreditCard(Integer isEnableCreditCard) {
		this.isEnableCreditCard = isEnableCreditCard;
	}

	public Integer getIsEnableQueryOrder() {
		return isEnableQueryOrder;
	}

	public void setIsEnableQueryOrder(Integer isEnableQueryOrder) {
		this.isEnableQueryOrder = isEnableQueryOrder;
	}

	public Integer getIsEnablePersonalInfomation() {
		return isEnablePersonalInfomation;
	}

	public void setIsEnablePersonalInfomation(Integer isEnablePersonalInfomation) {
		this.isEnablePersonalInfomation = isEnablePersonalInfomation;
	}

	public String getPersonalInfomation() {
		return personalInfomation;
	}

	public void setPersonalInfomation(String personalInfomation) {
		this.personalInfomation = personalInfomation;
	}

	public Integer getIsEnable4() {
		return isEnable4;
	}

	public void setIsEnable4(Integer isEnable4) {
		this.isEnable4 = isEnable4;
	}

	public Integer getIsEnable6() {
		return isEnable6;
	}

	public void setIsEnable6(Integer isEnable6) {
		this.isEnable6 = isEnable6;
	}

	public Integer getIsEnable7() {
		return isEnable7;
	}

	public void setIsEnable7(Integer isEnable7) {
		this.isEnable7 = isEnable7;
	}

	public Integer getIsEnable8() {
		return isEnable8;
	}

	public void setIsEnable8(Integer isEnable8) {
		this.isEnable8 = isEnable8;
	}

	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}

	public String getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(String firstPage) {
		this.firstPage = firstPage;
	}

	public String getCreditCard() {
		return creditCard;
	}

	public void setCreditCard(String creditCard) {
		this.creditCard = creditCard;
	}

	public String getQueryProgress() {
		return queryProgress;
	}

	public void setQueryProgress(String queryProgress) {
		this.queryProgress = queryProgress;
	}

	public String getHandleLoan() {
		return handleLoan;
	}

	public void setHandleLoan(String handleLoan) {
		this.handleLoan = handleLoan;
	}

	public String getQueryOrder() {
		return queryOrder;
	}

	public void setQueryOrder(String queryOrder) {
		this.queryOrder = queryOrder;
	}

	public String getPeccancy() {
		return peccancy;
	}

	public void setPeccancy(String peccancy) {
		this.peccancy = peccancy;
	}

	public String getRepayCard() {
		return repayCard;
	}

	public void setRepayCard(String repayCard) {
		this.repayCard = repayCard;
	}

	public String getMerchantReceivables() {
		return merchantReceivables;
	}

	public void setMerchantReceivables(String merchantReceivables) {
		this.merchantReceivables = merchantReceivables;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getBatchOrderNotifyUrl() {
		return batchOrderNotifyUrl;
	}

	public void setBatchOrderNotifyUrl(String batchOrderNotifyUrl) {
		this.batchOrderNotifyUrl = batchOrderNotifyUrl;
	}

	public String getOpenAppUrl() {
		return openAppUrl;
	}

	public void setOpenAppUrl(String openAppUrl) {
		this.openAppUrl = openAppUrl;
	}

	public String getOpenAppName() {
		return openAppName;
	}

	public void setOpenAppName(String openAppName) {
		this.openAppName = openAppName;
	}

	public String getOpenAppIntro() {
		return openAppIntro;
	}

	public void setOpenAppIntro(String openAppIntro) {
		this.openAppIntro = openAppIntro;
	}

	public String getOpenMerchantKey() {
		return openMerchantKey;
	}

	public void setOpenMerchantKey(String openMerchantKey) {
		this.openMerchantKey = openMerchantKey;
	}

	public String getKeyVersion() {
		return keyVersion;
	}

	public void setKeyVersion(String keyVersion) {
		this.keyVersion = keyVersion;
	}

	public String getDayStart() {
		return dayStart;
	}

	public void setDayStart(String dayStart) {
		this.dayStart = dayStart;
	}

	public String getDayEnd() {
		return dayEnd;
	}

	public void setDayEnd(String dayEnd) {
		this.dayEnd = dayEnd;
	}

	public String getMonthCardNum() {
		return monthCardNum;
	}

	public void setMonthCardNum(String monthCardNum) {
		this.monthCardNum = monthCardNum;
	}

	public String getYearCardNum() {
		return yearCardNum;
	}

	public void setYearCardNum(String yearCardNum) {
		this.yearCardNum = yearCardNum;
	}

	public BigDecimal getRedMoneyMin() {
		return redMoneyMin;
	}

	public void setRedMoneyMin(BigDecimal redMoneyMin) {
		this.redMoneyMin = redMoneyMin;
	}

	public BigDecimal getRedMoneyMax() {
		return redMoneyMax;
	}

	public void setRedMoneyMax(BigDecimal redMoneyMax) {
		this.redMoneyMax = redMoneyMax;
	}

	public BigDecimal getWithdrawMoneyMin() {
		return withdrawMoneyMin;
	}

	public void setWithdrawMoneyMin(BigDecimal withdrawMoneyMin) {
		this.withdrawMoneyMin = withdrawMoneyMin;
	}

	public String getPublicAccount() {
		return publicAccount;
	}

	public void setPublicAccount(String publicAccount) {
		this.publicAccount = publicAccount;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getAuthorizedUnit() {
		return authorizedUnit;
	}

	public void setAuthorizedUnit(String authorizedUnit) {
		this.authorizedUnit = authorizedUnit;
	}

	public String getCompanyNo() {
		return companyNo;
	}

	public void setCompanyNo(String companyNo) {
		this.companyNo = companyNo;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getPublicAccountName() {
		return publicAccountName;
	}

	public void setPublicAccountName(String publicAccountName) {
		this.publicAccountName = publicAccountName;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getEncodingAesKey() {
		return encodingAesKey;
	}

	public void setEncodingAesKey(String encodingAesKey) {
		this.encodingAesKey = encodingAesKey;
	}

	public String getBusinessEmail() {
		return businessEmail;
	}

	public void setBusinessEmail(String businessEmail) {
		this.businessEmail = businessEmail;
	}

	public String getUiStatus() {
		return uiStatus;
	}

	public void setUiStatus(String uiStatus) {
		this.uiStatus = uiStatus;
	}

	public String getIsSupportPay() {
		return isSupportPay;
	}

	public void setIsSupportPay(String isSupportPay) {
		this.isSupportPay = isSupportPay;
	}

	public BigDecimal getOpenRepayPrice() {
		return openRepayPrice;
	}

	public void setOpenRepayPrice(BigDecimal openRepayPrice) {
		this.openRepayPrice = openRepayPrice;
	}

	public String getOpenRepayCost() {
		return openRepayCost;
	}

	public void setOpenRepayCost(String openRepayCost) {
		this.openRepayCost = openRepayCost;
	}

	public String getOpenRepayPushCost() {
		return openRepayPushCost;
	}

	public void setOpenRepayPushCost(String openRepayPushCost) {
		this.openRepayPushCost = openRepayPushCost;
	}

	public String getReceiveKjCost() {
		return receiveKjCost;
	}

	public void setReceiveKjCost(String receiveKjCost) {
		this.receiveKjCost = receiveKjCost;
	}

	public String getTradeFeeRateStr() {
		return tradeFeeRateStr;
	}

	public void setTradeFeeRateStr(String tradeFeeRateStr) {
		this.tradeFeeRateStr = tradeFeeRateStr;
	}

	public String getTradeSingleFeeStr() {
		return tradeSingleFeeStr;
	}

	public void setTradeSingleFeeStr(String tradeSingleFeeStr) {
		this.tradeSingleFeeStr = tradeSingleFeeStr;
	}

	public String getWithdrawFeeRateStr() {
		return withdrawFeeRateStr;
	}

	public void setWithdrawFeeRateStr(String withdrawFeeRateStr) {
		this.withdrawFeeRateStr = withdrawFeeRateStr;
	}

	public String getWithdrawSingleFeeStr() {
		return withdrawSingleFeeStr;
	}

	public void setWithdrawSingleFeeStr(String withdrawSingleFeeStr) {
		this.withdrawSingleFeeStr = withdrawSingleFeeStr;
	}

	public String getAuthorizedUnitSeal() {
		return authorizedUnitSeal;
	}

	public void setAuthorizedUnitSeal(String authorizedUnitSeal) {
		this.authorizedUnitSeal = authorizedUnitSeal;
	}

	public String getMemberCenterLogo() {
		return memberCenterLogo;
	}

	public void setMemberCenterLogo(String memberCenterLogo) {
		this.memberCenterLogo = memberCenterLogo;
	}

	public String getStartPage() {
		return startPage;
	}

	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	public String getAppLogo() {
		return appLogo;
	}

	public void setAppLogo(String appLogo) {
		this.appLogo = appLogo;
	}

	public String getPublicQrCode() {
		return publicQrCode;
	}

	public void setPublicQrCode(String publicQrCode) {
		this.publicQrCode = publicQrCode;
	}

	public String getShareMessageLogo() {
		return shareMessageLogo;
	}

	public void setShareMessageLogo(String shareMessageLogo) {
		this.shareMessageLogo = shareMessageLogo;
	}

	public String getShareTemplateImage1() {
		return shareTemplateImage1;
	}

	public void setShareTemplateImage1(String shareTemplateImage1) {
		this.shareTemplateImage1 = shareTemplateImage1;
	}

	public String getShareTemplateImage1Url() {
		return shareTemplateImage1Url;
	}

	public void setShareTemplateImage1Url(String shareTemplateImage1Url) {
		this.shareTemplateImage1Url = shareTemplateImage1Url;
	}

	public String getShareTemplateImage2() {
		return shareTemplateImage2;
	}

	public void setShareTemplateImage2(String shareTemplateImage2) {
		this.shareTemplateImage2 = shareTemplateImage2;
	}

	public String getShareTemplateImage2Url() {
		return shareTemplateImage2Url;
	}

	public void setShareTemplateImage2Url(String shareTemplateImage2Url) {
		this.shareTemplateImage2Url = shareTemplateImage2Url;
	}

	public String getShareTemplateImage3() {
		return shareTemplateImage3;
	}

	public void setShareTemplateImage3(String shareTemplateImage3) {
		this.shareTemplateImage3 = shareTemplateImage3;
	}

	public String getShareTemplateImage3Url() {
		return shareTemplateImage3Url;
	}

	public void setShareTemplateImage3Url(String shareTemplateImage3Url) {
		this.shareTemplateImage3Url = shareTemplateImage3Url;
	}

	public String getAuthorizedUnitSealUrl() {
		return authorizedUnitSealUrl;
	}

	public void setAuthorizedUnitSealUrl(String authorizedUnitSealUrl) {
		this.authorizedUnitSealUrl = authorizedUnitSealUrl;
	}

	public String getMemberCenterLogoUrl() {
		return memberCenterLogoUrl;
	}

	public void setMemberCenterLogoUrl(String memberCenterLogoUrl) {
		this.memberCenterLogoUrl = memberCenterLogoUrl;
	}

	public String getStartPageUrl() {
		return startPageUrl;
	}

	public void setStartPageUrl(String startPageUrl) {
		this.startPageUrl = startPageUrl;
	}

	public String getAppLogoUrl() {
		return appLogoUrl;
	}

	public void setAppLogoUrl(String appLogoUrl) {
		this.appLogoUrl = appLogoUrl;
	}

	public String getPublicQrCodeUrl() {
		return publicQrCodeUrl;
	}

	public void setPublicQrCodeUrl(String publicQrCodeUrl) {
		this.publicQrCodeUrl = publicQrCodeUrl;
	}

	public String getShareMessageLogoUrl() {
		return shareMessageLogoUrl;
	}

	public void setShareMessageLogoUrl(String shareMessageLogoUrl) {
		this.shareMessageLogoUrl = shareMessageLogoUrl;
	}

	public Double getTradeFeeRate() {
		return tradeFeeRate;
	}

	public void setTradeFeeRate(Double tradeFeeRate) {
		this.tradeFeeRate = tradeFeeRate;
	}

	public Double getTradeSingleFee() {
		return tradeSingleFee;
	}

	public void setTradeSingleFee(Double tradeSingleFee) {
		this.tradeSingleFee = tradeSingleFee;
	}

	public Double getWithdrawFeeRate() {
		return withdrawFeeRate;
	}

	public void setWithdrawFeeRate(Double withdrawFeeRate) {
		this.withdrawFeeRate = withdrawFeeRate;
	}

	public Double getWithdrawSingleFee() {
		return withdrawSingleFee;
	}

	public void setWithdrawSingleFee(Double withdrawSingleFee) {
		this.withdrawSingleFee = withdrawSingleFee;
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
		this.orgName = orgName == null ? null : orgName.trim();
	}

	public String getOrgLogo() {
		return orgLogo;
	}

	public void setOrgLogo(String orgLogo) {
		this.orgLogo = orgLogo == null ? null : orgLogo.trim();
	}

	public String getWeixinSign() {
		return weixinSign;
	}

	public void setWeixinSign(String weixinSign) {
		this.weixinSign = weixinSign == null ? null : weixinSign.trim();
	}

	public String getSuperOrgcode() {
		return superOrgcode;
	}

	public void setSuperOrgcode(String superOrgcode) {
		this.superOrgcode = superOrgcode == null ? null : superOrgcode.trim();
	}

	public String getV2AgentNumber() {
		return v2AgentNumber;
	}

	public void setV2AgentNumber(String v2AgentNumber) {
		this.v2AgentNumber = v2AgentNumber == null ? null : v2AgentNumber.trim();
	}

	public String getV2Orgcode() {
		return v2Orgcode;
	}

	public void setV2Orgcode(String v2Orgcode) {
		this.v2Orgcode = v2Orgcode == null ? null : v2Orgcode.trim();
	}

	public Long getAgentPrice() {
		return agentPrice;
	}

	public void setAgentPrice(Long agentPrice) {
		this.agentPrice = agentPrice;
	}

	public String getAgentCost() {
		return agentCost;
	}

	public void setAgentCost(String agentCost) {
		this.agentCost = agentCost == null ? null : agentCost.trim();
	}

	public String getAgentPushCost() {
		return agentPushCost;
	}

	public void setAgentPushCost(String agentPushCost) {
		this.agentPushCost = agentPushCost;
	}

	public String getReceiveCost() {
		return receiveCost;
	}

	public void setReceiveCost(String receiveCost) {
		this.receiveCost = receiveCost == null ? null : receiveCost.trim();
	}

	public String getReceivePushCost() {
		return receivePushCost;
	}

	public void setReceivePushCost(String receivePushCost) {
		this.receivePushCost = receivePushCost == null ? null : receivePushCost.trim();
	}

	public String getRepaymentCost() {
		return repaymentCost;
	}

	public void setRepaymentCost(String repaymentCost) {
		this.repaymentCost = repaymentCost == null ? null : repaymentCost.trim();
	}

	public String getRepaymentPushCost() {
		return repaymentPushCost;
	}

	public void setRepaymentPushCost(String repaymentPushCost) {
		this.repaymentPushCost = repaymentPushCost == null ? null : repaymentPushCost.trim();
	}

	public Integer getUpManagerNum() {
		return upManagerNum;
	}

	public void setUpManagerNum(Integer upManagerNum) {
		this.upManagerNum = upManagerNum;
	}

	public Integer getUpManagerCardnum() {
		return upManagerCardnum;
	}

	public void setUpManagerCardnum(Integer upManagerCardnum) {
		this.upManagerCardnum = upManagerCardnum;
	}

	public Integer getUpBankerNum() {
		return upBankerNum;
	}

	public void setUpBankerNum(Integer upBankerNum) {
		this.upBankerNum = upBankerNum;
	}

	public Integer getUpBankerCardnum() {
		return upBankerCardnum;
	}

	public void setUpBankerCardnum(Integer upBankerCardnum) {
		this.upBankerCardnum = upBankerCardnum;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOrgLogoUrl() {
		return orgLogoUrl;
	}

	public void setOrgLogoUrl(String orgLogoUrl) {
		this.orgLogoUrl = orgLogoUrl;
	}
}