package cn.eeepay.framework.model;

import java.util.Date;
import java.util.List;

/**
 * table terminal_info desc 机具信息表
 */
public class TerminalInfo {
	private Long id;
	private String sn;

	private String snStart;
	private String snEnd;

	private boolean belongAgent;// 判断机具是否是当前登录代理商的
	private String callbackLock;



	private String activityType;

	public String getActivityType() {
		return activityType;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	private String terminalId;

	private String merchantNo;

	private String psamNo;

	private String agentNo;

	private String agentNode;

	private String openStatus;

	private String type;

	private String allotBatch;

	private String model;

	private String tmk;

	private String tmkTpk;

	private String tmkTak;

	private Date startTime;

	private Date createTime;

	private Integer posType;

	private Byte needCheck;

	private Date lastCheckInTime;

	private String cashierNo;

	private String serialNo;

	private String batchNo;

	private String bpId;
	private String bpName;
	// 商户名称
	private String merchantName;

	private String userCode;
	private String realName;

	// 代理商级别
	private String agentLevel;

	// 代理商名称
	private String agentName;

	// 硬件产品
	private String typeName;
	private String versionNu;

	private String activityTypeValues;

	private String agentType;

	private Integer status;// 用来控制人人代理二级代理商下发机具功能
	private Integer sendLock;// 机具锁定状态 0-未锁定 1-锁定 不可以再下发
	
	private String activityTypeNo;//活动子类型编号
	private String activityTypeNoName;//活动子类型名称
	private String teamName;//所属组织
	private String startReceiptdate;
	private String endReceiptdate;
	private String startDowndate;
	private String endDowndate;
	private Date  downDate;
	private Date receiptDate;
	
	private String teamEntryId;


	@Override
	public String toString() {
		return "TerminalInfo{" +
				"id=" + id +
				", sn='" + sn + '\'' +
				", snStart='" + snStart + '\'' +
				", snEnd='" + snEnd + '\'' +
				", belongAgent=" + belongAgent +
				", callbackLock='" + callbackLock + '\'' +
				", activityType='" + activityType + '\'' +
				", terminalId='" + terminalId + '\'' +
				", merchantNo='" + merchantNo + '\'' +
				", psamNo='" + psamNo + '\'' +
				", agentNo='" + agentNo + '\'' +
				", agentNode='" + agentNode + '\'' +
				", openStatus='" + openStatus + '\'' +
				", type='" + type + '\'' +
				", allotBatch='" + allotBatch + '\'' +
				", model='" + model + '\'' +
				", tmk='" + tmk + '\'' +
				", tmkTpk='" + tmkTpk + '\'' +
				", tmkTak='" + tmkTak + '\'' +
				", startTime=" + startTime +
				", createTime=" + createTime +
				", posType=" + posType +
				", needCheck=" + needCheck +
				", lastCheckInTime=" + lastCheckInTime +
				", cashierNo='" + cashierNo + '\'' +
				", serialNo='" + serialNo + '\'' +
				", batchNo='" + batchNo + '\'' +
				", bpId='" + bpId + '\'' +
				", bpName='" + bpName + '\'' +
				", merchantName='" + merchantName + '\'' +
				", userCode='" + userCode + '\'' +
				", realName='" + realName + '\'' +
				", agentLevel='" + agentLevel + '\'' +
				", agentName='" + agentName + '\'' +
				", typeName='" + typeName + '\'' +
				", versionNu='" + versionNu + '\'' +
				", activityTypeValues='" + activityTypeValues + '\'' +
				", agentType='" + agentType + '\'' +
				", status=" + status +
				", sendLock=" + sendLock +
				", activityTypeNo='" + activityTypeNo + '\'' +
				", activityTypeNoName='" + activityTypeNoName + '\'' +
				", teamName='" + teamName + '\'' +
				", updateAgentStatus=" + updateAgentStatus +
				", receiptDate=" + receiptDate +
				", downDate=" + downDate +
				", psamNo1='" + psamNo1 + '\'' +
				", bool='" + bool + '\'' +
				", collectionCode='" + collectionCode + '\'' +
				",startReceiptdate='" + startReceiptdate + '\'' +
				",endReceiptdate='" + endReceiptdate + '\'' +
				",startDowndate='" + startDowndate + '\'' +
				",endDowndate='" + endDowndate + '\'' +
				'}';
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getCallbackLock() {
		return callbackLock;
	}

	public void setCallbackLock(String callbackLock) {
		this.callbackLock = callbackLock;
	}

	public boolean isBelongAgent() {
		return belongAgent;
	}

	public void setBelongAgent(boolean belongAgent) {
		this.belongAgent = belongAgent;
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
	public Date getReceiptDate() {
		return receiptDate;
	}

	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	public Date getDownDate() {
		return downDate;
	}

	public void setDownDate(Date downDate) {
		this.downDate = downDate;
	}

	private Integer updateAgentStatus;
	private String merTeamId;//商户组织id
	private String merGroup;//商户所属组织

	private String superPush;//是否为超级推机具 1-是 0-不是

	public Integer getSendLock() {
		return sendLock;
	}

	public void setSendLock(Integer sendLock) {
		this.sendLock = sendLock;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getActivityTypeValues() {
		return activityTypeValues;
	}

	public void setActivityTypeValues(String activityTypeValues) {
		this.activityTypeValues = activityTypeValues;
	}

	public String getCollectionCode() {
		return collectionCode;
	}

	public void setCollectionCode(String collectionCode) {
		this.collectionCode = collectionCode;
	}

	// 查询专用
	private String psamNo1;
	private String bool;

	private String collectionCode;// 收款码

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn == null ? null : sn.trim();
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId == null ? null : terminalId.trim();
	}

	public String getPsamNo() {
		return psamNo;
	}

	public void setPsamNo(String psamNo) {
		this.psamNo = psamNo == null ? null : psamNo.trim();
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo == null ? null : agentNo.trim();
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode == null ? null : agentNode.trim();
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus == null ? null : openStatus.trim();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type == null ? null : type.trim();
	}

	public String getAllotBatch() {
		return allotBatch;
	}

	public void setAllotBatch(String allotBatch) {
		this.allotBatch = allotBatch == null ? null : allotBatch.trim();
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model == null ? null : model.trim();
	}

	public String getTmk() {
		return tmk;
	}

	public void setTmk(String tmk) {
		this.tmk = tmk == null ? null : tmk.trim();
	}

	public String getTmkTpk() {
		return tmkTpk;
	}

	public void setTmkTpk(String tmkTpk) {
		this.tmkTpk = tmkTpk == null ? null : tmkTpk.trim();
	}

	public String getTmkTak() {
		return tmkTak;
	}

	public void setTmkTak(String tmkTak) {
		this.tmkTak = tmkTak == null ? null : tmkTak.trim();
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getPosType() {
		return posType;
	}

	public void setPosType(Integer posType) {
		this.posType = posType;
	}

	public Byte getNeedCheck() {
		return needCheck;
	}

	public void setNeedCheck(Byte needCheck) {
		this.needCheck = needCheck;
	}

	public Date getLastCheckInTime() {
		return lastCheckInTime;
	}

	public void setLastCheckInTime(Date lastCheckInTime) {
		this.lastCheckInTime = lastCheckInTime;
	}

	public String getCashierNo() {
		return cashierNo;
	}

	public void setCashierNo(String cashierNo) {
		this.cashierNo = cashierNo == null ? null : cashierNo.trim();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo == null ? null : serialNo.trim();
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo == null ? null : batchNo.trim();
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(String agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getPsamNo1() {
		return psamNo1;
	}

	public void setPsamNo1(String psamNo1) {
		this.psamNo1 = psamNo1;
	}

	public String getBool() {
		return bool;
	}

	public void setBool(String bool) {
		this.bool = bool;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getVersionNu() {
		return versionNu;
	}

	public void setVersionNu(String versionNu) {
		this.versionNu = versionNu;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getBpId() {
		return bpId;
	}

	public void setBpId(String bpId) {
		this.bpId = bpId;
	}

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
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

	public String getSuperPush() {
		return superPush;
	}

	public void setSuperPush(String superPush) {
		this.superPush = superPush;
	}


	public String getActivityTypeNo() {
		return activityTypeNo;
	}

	public void setActivityTypeNo(String activityTypeNo) {
		this.activityTypeNo = activityTypeNo;
	}

	public Integer getUpdateAgentStatus() {
		return updateAgentStatus;
	}

	public void setUpdateAgentStatus(Integer updateAgentStatus) {
		this.updateAgentStatus = updateAgentStatus;
	}

	public String getActivityTypeNoName() {
		return activityTypeNoName;
	}

	public void setActivityTypeNoName(String activityTypeNoName) {
		this.activityTypeNoName = activityTypeNoName;
	}

	public String getMerTeamId() {
		return merTeamId;
	}

	public void setMerTeamId(String merTeamId) {
		this.merTeamId = merTeamId;
	}

	public String getMerGroup() {
		return merGroup;
	}

	public void setMerGroup(String merGroup) {
		this.merGroup = merGroup;
	}

	public String getStartReceiptdate() {
		return startReceiptdate;
	}

	public void setStartReceiptdate(String startReceiptdate) {
		this.startReceiptdate = startReceiptdate;
	}

	public String getEndReceiptdate() {
		return endReceiptdate;
	}

	public void setEndReceiptdate(String endReceiptdate) {
		this.endReceiptdate = endReceiptdate;
	}

	public String getStartDowndate() {
		return startDowndate;
	}

	public void setStartDowndate(String startDowndate) {
		this.startDowndate = startDowndate;
	}

	public String getEndDowndate() {
		return endDowndate;
	}

	public void setEndDowndate(String endDowndate) {
		this.endDowndate = endDowndate;
	}

	public String getTeamEntryId() {
		return teamEntryId;
	}

	public void setTeamEntryId(String teamEntryId) {
		this.teamEntryId = teamEntryId;
	}
}