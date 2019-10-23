package cn.eeepay.framework.model;

import cn.eeepay.framework.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * table:super.order_main desc:订单表
 * 
 * @author tans
 * @date 2017-11-30
 */
public class OrderMain {
	private Long id;// ID

	private String orderNo;// 订单编号

	private String userCode;// 订单用户编码

	private Long orgId;// 组织id

	private String orgName;// 组织名称

	private String orderType;// 订单类型:1代理授权；2信用卡申请 3收款 4信用卡还款

	private String status;// 订单状态:1已创建；2待支付；3:待审核 4已授权 5订单成功 6订单失败 9已关闭

	private Date createDate;// 创建时间

	private BigDecimal totalBonus;// 品牌发放总奖金

	private String shareUserCode;// 分享链接用户

	private String oneUserCode;// 1级收益用户编号

	private String oneUserType;// 1级身份

	private BigDecimal oneUserProfit;// 1级分润

	private BigDecimal orgProfit;// 品牌商(组织)分润; =品牌的固定分润 + 分润剩余

	private BigDecimal orgLeftProfit;// 用户奖金分配后的剩余收益归组织

	private BigDecimal plateProfit;// 平台分润值; =银行或贷款返还-组织成本

	private String accountStatus;// 记账状态;0未记账；1已记账

	private Date payDate;// 支付时间

	private String payMethod;// 支付方式

	private String payChannel;// 付款通道：V2,WEIXIN

	private String payOrderNo;// 上游的支付订单号

	private String updateBy;// 操作人

	private Date updateDate;// 操作时间

	private BigDecimal price;// 售价

	private Long bankSourceId;// 银行id

	private String bankName;// 银行名称

	private String orderName;// 订单名称

	private String orderPhone;// 订单手机号

	private String orderIdNo;// 订单证件号

	private Long loanSourceId;// 放款机构id

	private String loanName;// 放款机构名称

	private BigDecimal loanAmount;// 贷款金额

	private String loanPushPro;// 发放比例

	private String receiveAgentId;// 收款商户id

	private BigDecimal receiveAmount;// 收款金额,目标还款金额

	private String repaymentAgentId;// 还款商户id

	private String remark;// 备注

	private String twoUserCode;// 2级收益用户编号

	private String twoUserType;// 2级身份

	private BigDecimal twoUserProfit;// 2级分润

	private String thrUserCode;// 3级收益用户编号

	private String thrUserType;// 3级身份

	private BigDecimal thrUserProfit;// 3级分润

	private String fouUserCode;// 4级收益用户编号

	private String fouUserType;// 4级身份

	private BigDecimal fouUserProfit;// 4级分润

	private String userName;// 贡献人名称

	private BigDecimal creditcardBankBonus;// 信用卡办理银行发放总奖金
	private String loanBankRate;// 贷款机构奖金发放扣率(金额或者百分比)
	private String loanOrgRate;// 贷款:品牌代理成本扣率
	private String loanOrgBonus;// 贷款:品牌发放总奖金扣率
	private BigDecimal repaymentAmount;// 实际还款金额
	private String transRate;// 收款还款等交易的费率(%)
	private String[] orderTypeList;// 订单类型的集合

	private String createDateStart;
	private String createDateEnd;
	private String payDateStart;
	private String payDateEnd;
	private String createDateStr;
	private String payDateStr;
	private String payDate2Str;
	private String shareUserName;// 贡献人名称
	private String oneUserName;// 一级贡献人名称
	private String twoUserName;// 二级贡献人名称
	private String thrUserName;// 三级贡献人名称
	private String fouUserName;// 四级贡献人名称
	private String entityId;
	private String loanToBrankRate;// 贷款机构给品牌奖金扣率＝贷款机构奖金发放扣率loanBankRate－品牌代理成本扣率loanOrgRate
	private String loanToBrankTotalAmount;// 贷款机构给品牌总奖金＝贷款金额loanAmount*贷款机构给品牌奖金扣率loanToBrankRate
	private BigDecimal bankTotalAmount;// 办信用卡银行给品牌总奖金=银行发放总奖金creditcardBankBonus-品牌代理成本totalBonus
	private String refundStatus;// 是否已达标退款 0否1是
	private String refundMsg;// 退款原由
	private String secondUserNode;// 二级代理节点
	private Date refundDate;// 退款时间
	private String startRefundDate;
	private String endRefundDate;
	private String refundDateStr;

	private String openProvince;// 开通省份
	private String openCity;// 开通城市
	private String openRegion;// 开通地区
	private String address;
	private String incomeType;// 退款订单号
	private String sharePhone;// 贡献人手机号
	private String payChannelNo;// 收款通道商户号
	private String repayTransStatus;// 还款状态:3成功，4失败，6终止
	private Date completeDate;// 订单完成时间
	private String completeDateStart;
	private String completeDateEnd;
	private BigDecimal repayTransfee;// 还款状态
	private BigDecimal repayTransfeeAdd;// 还款手续费额外(金额)

	private String creditcardBankBonus2;// 信用卡办理银行发放总奖金(首刷)
	private BigDecimal totalBonus2;// 品牌总发放奖金(首刷)
	// private String oneUserCode2;// 一级分润用户(首刷)
	private BigDecimal oneUserProfit2;// 一级分润(首刷)
	// private String twoUserCode2;// 二级分润用户(首刷)
	private BigDecimal twoUserProfit2;// 二级分润(首刷)
	// private String thrUserCode2;// 三级分润用户(首刷)
	private BigDecimal thrUserProfit2;// 三级分润(首刷)
	// private String fouUserCode2;// 四级分润用户(首刷)
	private BigDecimal fouUserProfit2;// 四级分润(首刷)
	private BigDecimal orgProfit2;// 品牌商(组织)分润(首刷)
	private BigDecimal orgLeftProfit2;// 用户奖金分配后的剩余收益归组织(首刷)
	private BigDecimal plateProfit2;// 平台分润值(首刷)'
	private String accountStatus2;// (首刷)记账状态;0待入账；1已记账；2记账失败
	private String profitStatus2;// (首刷)计算分润状态2 0为计算失败 1为计算成功
	private Date payDate2;// 首刷分润时间
	private String payDate2Start;
	private String payDate2End;

	private String loanType;// 奖励要求  1:有效注册，2有效借款，3授信成功
	private String profitType;// 奖金模式  1-固定奖金，2-按比例发放
	private String orgBonusConf;// 品牌截留扣率
	
	private String companyBonusConf;//(配置)公司截留奖金
	private String completeDateStr;


	private String productName;		//保险产品名称
	private String productType;		//保险产品种类
	private String companyNickName;	//保险公司别称
	private Long companyId;			//保险公司Id
	private String insuranceName;	//被保人姓名
	private String insurancePhone;	//被保人手机号
	private String insuranceIdNo;	//被保人证件
	private String bonusSettleTime;	//奖金结算时间
	
	public String getCompleteDateStr() {
		return completeDate == null ? "" : DateUtil.getLongFormatDate(completeDate);
	}

	public void setCompleteDateStr(String completeDateStr) {
		this.completeDateStr = completeDateStr;
	}

	public String getCompanyBonusConf() {
		return companyBonusConf;
	}

	public void setCompanyBonusConf(String companyBonusConf) {
		this.companyBonusConf = companyBonusConf;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getProfitType() {
		return profitType;
	}

	public void setProfitType(String profitType) {
		this.profitType = profitType;
	}

	public String getOrgBonusConf() {
		return orgBonusConf;
	}

	public void setOrgBonusConf(String orgBonusConf) {
		this.orgBonusConf = orgBonusConf;
	}

	public String getCreditcardBankBonus2() {
		return creditcardBankBonus2;
	}

	public void setCreditcardBankBonus2(String creditcardBankBonus2) {
		this.creditcardBankBonus2 = creditcardBankBonus2;
	}

	public BigDecimal getTotalBonus2() {
		return totalBonus2;
	}

	public void setTotalBonus2(BigDecimal totalBonus2) {
		this.totalBonus2 = totalBonus2;
	}

	public BigDecimal getOneUserProfit2() {
		return oneUserProfit2;
	}

	public void setOneUserProfit2(BigDecimal oneUserProfit2) {
		this.oneUserProfit2 = oneUserProfit2;
	}

	public BigDecimal getTwoUserProfit2() {
		return twoUserProfit2;
	}

	public void setTwoUserProfit2(BigDecimal twoUserProfit2) {
		this.twoUserProfit2 = twoUserProfit2;
	}

	public BigDecimal getThrUserProfit2() {
		return thrUserProfit2;
	}

	public void setThrUserProfit2(BigDecimal thrUserProfit2) {
		this.thrUserProfit2 = thrUserProfit2;
	}

	public BigDecimal getFouUserProfit2() {
		return fouUserProfit2;
	}

	public void setFouUserProfit2(BigDecimal fouUserProfit2) {
		this.fouUserProfit2 = fouUserProfit2;
	}

	public BigDecimal getOrgProfit2() {
		return orgProfit2;
	}

	public void setOrgProfit2(BigDecimal orgProfit2) {
		this.orgProfit2 = orgProfit2;
	}

	public BigDecimal getOrgLeftProfit2() {
		return orgLeftProfit2;
	}

	public void setOrgLeftProfit2(BigDecimal orgLeftProfit2) {
		this.orgLeftProfit2 = orgLeftProfit2;
	}

	public BigDecimal getPlateProfit2() {
		return plateProfit2;
	}

	public void setPlateProfit2(BigDecimal plateProfit2) {
		this.plateProfit2 = plateProfit2;
	}

	public String getAccountStatus2() {
		return accountStatus2;
	}

	public void setAccountStatus2(String accountStatus2) {
		this.accountStatus2 = accountStatus2;
	}

	public String getProfitStatus2() {
		return profitStatus2;
	}

	public void setProfitStatus2(String profitStatus2) {
		this.profitStatus2 = profitStatus2;
	}

	public Date getPayDate2() {
		return payDate2;
	}

	public void setPayDate2(Date payDate2) {
		this.payDate2 = payDate2;
	}

	public BigDecimal getRepayTransfeeAdd() {
		return repayTransfeeAdd;
	}

	public void setRepayTransfeeAdd(BigDecimal repayTransfeeAdd) {
		this.repayTransfeeAdd = repayTransfeeAdd;
	}

	public BigDecimal getRepayTransfee() {
		return repayTransfee;
	}

	public void setRepayTransfee(BigDecimal repayTransfee) {
		this.repayTransfee = repayTransfee;
	}

	public Date getCompleteDate() {
		return completeDate;
	}

	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}

	public String getCompleteDateStart() {
		return completeDateStart;
	}

	public void setCompleteDateStart(String completeDateStart) {
		this.completeDateStart = completeDateStart;
	}

	public String getCompleteDateEnd() {
		return completeDateEnd;
	}

	public void setCompleteDateEnd(String completeDateEnd) {
		this.completeDateEnd = completeDateEnd;
	}

	public String getRepayTransStatus() {
		return repayTransStatus;
	}

	public void setRepayTransStatus(String repayTransStatus) {
		this.repayTransStatus = repayTransStatus;
	}

	public String getPayChannelNo() {
		return payChannelNo;
	}

	public void setPayChannelNo(String payChannelNo) {
		this.payChannelNo = payChannelNo;
	}

	public String getSharePhone() {
		return sharePhone;
	}

	public void setSharePhone(String sharePhone) {
		this.sharePhone = sharePhone;
	}

	public String getIncomeType() {
		return incomeType;
	}

	public void setIncomeType(String incomeType) {
		this.incomeType = incomeType;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRefundDateStr() {
		return refundDate == null ? "" : DateUtil.getLongFormatDate(refundDate);
	}

	public void setRefundDateStr(String refundDateStr) {
		this.refundDateStr = refundDateStr;
	}

	public String getStartRefundDate() {
		return startRefundDate;
	}

	public void setStartRefundDate(String startRefundDate) {
		this.startRefundDate = startRefundDate;
	}

	public String getEndRefundDate() {
		return endRefundDate;
	}

	public void setEndRefundDate(String endRefundDate) {
		this.endRefundDate = endRefundDate;
	}

	public String getSecondUserNode() {
		return secondUserNode;
	}

	public void setSecondUserNode(String secondUserNode) {
		this.secondUserNode = secondUserNode;
	}

	public String getRefundMsg() {
		return refundMsg;
	}

	public void setRefundMsg(String refundMsg) {
		this.refundMsg = refundMsg;
	}

	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public BigDecimal getBankTotalAmount() {
		return bankTotalAmount;
	}

	public void setBankTotalAmount(BigDecimal bankTotalAmount) {
		this.bankTotalAmount = bankTotalAmount;
	}

	public String getLoanToBrankRate() {
		return loanToBrankRate;
	}

	public void setLoanToBrankRate(String loanToBrankRate) {
		this.loanToBrankRate = loanToBrankRate;
	}

	public String getLoanToBrankTotalAmount() {
		return loanToBrankTotalAmount;
	}

	public void setLoanToBrankTotalAmount(String loanToBrankTotalAmount) {
		this.loanToBrankTotalAmount = loanToBrankTotalAmount;
	}

	public String[] getOrderTypeList() {
		return orderTypeList;
	}

	public void setOrderTypeList(String[] orderTypeList) {
		this.orderTypeList = orderTypeList;
	}

	public BigDecimal getRepaymentAmount() {
		return repaymentAmount;
	}

	public void setRepaymentAmount(BigDecimal repaymentAmount) {
		this.repaymentAmount = repaymentAmount;
	}

	public String getTransRate() {
		return transRate;
	}

	public void setTransRate(String transRate) {
		this.transRate = transRate;
	}

	public String getLoanBankRate() {
		return loanBankRate;
	}

	public void setLoanBankRate(String loanBankRate) {
		this.loanBankRate = loanBankRate;
	}

	public String getLoanOrgRate() {
		return loanOrgRate;
	}

	public void setLoanOrgRate(String loanOrgRate) {
		this.loanOrgRate = loanOrgRate;
	}

	public String getLoanOrgBonus() {
		return loanOrgBonus;
	}

	public void setLoanOrgBonus(String loanOrgBonus) {
		this.loanOrgBonus = loanOrgBonus;
	}

	public BigDecimal getCreditcardBankBonus() {
		return creditcardBankBonus != null ? creditcardBankBonus : BigDecimal.ZERO;
	}

	public void setCreditcardBankBonus(BigDecimal creditcardBankBonus) {
		this.creditcardBankBonus = creditcardBankBonus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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
		this.orderNo = orderNo == null ? null : orderNo.trim();
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode == null ? null : userCode.trim();
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

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType == null ? null : orderType.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public BigDecimal getTotalBonus() {
		return totalBonus;
	}

	public void setTotalBonus(BigDecimal totalBonus) {
		this.totalBonus = totalBonus;
	}

	public String getShareUserCode() {
		return shareUserCode;
	}

	public void setShareUserCode(String shareUserCode) {
		this.shareUserCode = shareUserCode == null ? null : shareUserCode.trim();
	}

	public BigDecimal getOrgProfit() {
		return orgProfit;
	}

	public void setOrgProfit(BigDecimal orgProfit) {
		this.orgProfit = orgProfit;
	}

	public BigDecimal getOrgLeftProfit() {
		return orgLeftProfit;
	}

	public void setOrgLeftProfit(BigDecimal orgLeftProfit) {
		this.orgLeftProfit = orgLeftProfit;
	}

	public BigDecimal getPlateProfit() {
		return plateProfit;
	}

	public void setPlateProfit(BigDecimal plateProfit) {
		this.plateProfit = plateProfit;
	}

	public String getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(String accountStatus) {
		this.accountStatus = accountStatus == null ? null : accountStatus.trim();
	}

	public Date getPayDate() {
		return payDate;
	}

	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel == null ? null : payChannel.trim();
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo == null ? null : payOrderNo.trim();
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy == null ? null : updateBy.trim();
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Long getBankSourceId() {
		return bankSourceId;
	}

	public void setBankSourceId(Long bankSourceId) {
		this.bankSourceId = bankSourceId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName == null ? null : bankName.trim();
	}

	public String getOrderPhone() {
		return orderPhone;
	}

	public void setOrderPhone(String orderPhone) {
		this.orderPhone = orderPhone == null ? null : orderPhone.trim();
	}

	public String getOrderIdNo() {
		return orderIdNo;
	}

	public void setOrderIdNo(String orderIdNo) {
		this.orderIdNo = orderIdNo == null ? null : orderIdNo.trim();
	}

	public Long getLoanSourceId() {
		return loanSourceId;
	}

	public void setLoanSourceId(Long loanSourceId) {
		this.loanSourceId = loanSourceId;
	}

	public String getLoanName() {
		return loanName;
	}

	public void setLoanName(String loanName) {
		this.loanName = loanName == null ? null : loanName.trim();
	}

	public BigDecimal getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(BigDecimal loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanPushPro() {
		return loanPushPro;
	}

	public void setLoanPushPro(String loanPushPro) {
		this.loanPushPro = loanPushPro == null ? null : loanPushPro.trim();
	}

	public String getReceiveAgentId() {
		return receiveAgentId;
	}

	public void setReceiveAgentId(String receiveAgentId) {
		this.receiveAgentId = receiveAgentId == null ? null : receiveAgentId.trim();
	}

	public BigDecimal getReceiveAmount() {
		return receiveAmount;
	}

	public void setReceiveAmount(BigDecimal receiveAmount) {
		this.receiveAmount = receiveAmount;
	}

	public String getRepaymentAgentId() {
		return repaymentAgentId;
	}

	public void setRepaymentAgentId(String repaymentAgentId) {
		this.repaymentAgentId = repaymentAgentId == null ? null : repaymentAgentId.trim();
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public String getOneUserCode() {
		return oneUserCode;
	}

	public void setOneUserCode(String oneUserCode) {
		this.oneUserCode = oneUserCode == null ? null : oneUserCode.trim();
	}

	public String getOneUserType() {
		return oneUserType;
	}

	public void setOneUserType(String oneUserType) {
		this.oneUserType = oneUserType == null ? null : oneUserType.trim();
	}

	public BigDecimal getOneUserProfit() {
		return oneUserProfit;
	}

	public void setOneUserProfit(BigDecimal oneUserProfit) {
		this.oneUserProfit = oneUserProfit;
	}

	public String getTwoUserCode() {
		return twoUserCode;
	}

	public void setTwoUserCode(String twoUserCode) {
		this.twoUserCode = twoUserCode == null ? null : twoUserCode.trim();
	}

	public String getTwoUserType() {
		return twoUserType;
	}

	public void setTwoUserType(String twoUserType) {
		this.twoUserType = twoUserType == null ? null : twoUserType.trim();
	}

	public BigDecimal getTwoUserProfit() {
		return twoUserProfit;
	}

	public void setTwoUserProfit(BigDecimal twoUserProfit) {
		this.twoUserProfit = twoUserProfit;
	}

	public String getThrUserCode() {
		return thrUserCode;
	}

	public void setThrUserCode(String thrUserCode) {
		this.thrUserCode = thrUserCode == null ? null : thrUserCode.trim();
	}

	public String getThrUserType() {
		return thrUserType;
	}

	public void setThrUserType(String thrUserType) {
		this.thrUserType = thrUserType == null ? null : thrUserType.trim();
	}

	public BigDecimal getThrUserProfit() {
		return thrUserProfit;
	}

	public void setThrUserProfit(BigDecimal thrUserProfit) {
		this.thrUserProfit = thrUserProfit;
	}

	public String getFouUserCode() {
		return fouUserCode;
	}

	public void setFouUserCode(String fouUserCode) {
		this.fouUserCode = fouUserCode;
	}

	public String getFouUserType() {
		return fouUserType;
	}

	public void setFouUserType(String fouUserType) {
		this.fouUserType = fouUserType;
	}

	public BigDecimal getFouUserProfit() {
		return fouUserProfit;
	}

	public void setFouUserProfit(BigDecimal fouUserProfit) {
		this.fouUserProfit = fouUserProfit;
	}

	public String getPayMethod() {
		return payMethod;
	}

	public void setPayMethod(String payMethod) {
		this.payMethod = payMethod;
	}

	public String getCreateDateStart() {
		return createDateStart;
	}

	public void setCreateDateStart(String createDateStart) {
		this.createDateStart = createDateStart;
	}

	public String getCreateDateEnd() {
		return createDateEnd;
	}

	public void setCreateDateEnd(String createDateEnd) {
		this.createDateEnd = createDateEnd;
	}

	public String getPayDateStart() {
		return payDateStart;
	}

	public void setPayDateStart(String payDateStart) {
		this.payDateStart = payDateStart;
	}

	public String getPayDate2Start() {
		return payDate2Start;
	}

	public void setPayDate2Start(String payDate2Start) {
		this.payDate2Start = payDate2Start;
	}

	public String getPayDate2End() {
		return payDate2End;
	}

	public void setPayDate2End(String payDate2End) {
		this.payDate2End = payDate2End;
	}

	public String getPayDateEnd() {
		return payDateEnd;
	}

	public void setPayDateEnd(String payDateEnd) {
		this.payDateEnd = payDateEnd;
	}

	public String getShareUserName() {
		return shareUserName;
	}

	public void setShareUserName(String shareUserName) {
		this.shareUserName = shareUserName;
	}

	public String getOneUserName() {
		return oneUserName;
	}

	public void setOneUserName(String oneUserName) {
		this.oneUserName = oneUserName;
	}

	public String getTwoUserName() {
		return twoUserName;
	}

	public void setTwoUserName(String twoUserName) {
		this.twoUserName = twoUserName;
	}

	public String getThrUserName() {
		return thrUserName;
	}

	public void setThrUserName(String thrUserName) {
		this.thrUserName = thrUserName;
	}

	public String getFouUserName() {
		return fouUserName;
	}

	public void setFouUserName(String fouUserName) {
		this.fouUserName = fouUserName;
	}

	public String getCreateDateStr() {
		return createDate == null ? "" : DateUtil.getLongFormatDate(createDate);
	}

	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}

	public String getPayDateStr() {
		return payDate == null ? "" : DateUtil.getLongFormatDate(payDate);
	}

	public void setPayDateStr(String payDateStr) {
		this.payDateStr = payDateStr;
	}

	public String getPayDate2Str() {
		return payDate2 == null ? "" : DateUtil.getLongFormatDate(payDate2);
	}

	public void setPayDate2Str(String payDate2Str) {
		this.payDate2Str = payDate2Str;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getCompanyNickName() {
		return companyNickName;
	}

	public void setCompanyNickName(String companyNickName) {
		this.companyNickName = companyNickName;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public String getInsurancePhone() {
		return insurancePhone;
	}

	public void setInsurancePhone(String insurancePhone) {
		this.insurancePhone = insurancePhone;
	}

	public String getInsuranceIdNo() {
		return insuranceIdNo;
	}

	public void setInsuranceIdNo(String insuranceIdNo) {
		this.insuranceIdNo = insuranceIdNo;
	}


	public String getBonusSettleTime() {
		return bonusSettleTime;
	}

	public void setBonusSettleTime(String bonusSettleTime) {
		this.bonusSettleTime = bonusSettleTime;
	}
}