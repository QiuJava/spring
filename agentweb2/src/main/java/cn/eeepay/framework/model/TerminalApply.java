package cn.eeepay.framework.model;

import java.util.Date;

public class TerminalApply {

	private String sn;
	private String hasSn;
	private String id;
	private String merchantNo;
	private String status;
	private String productType;
	private Date createTime;
	private String address;
	private String mobilephone;
	private String merchantName;
	private String agentName;
	private String oneAgentName;
	private String merAccount;
	private String sTime;
	private String eTime;
	private String hpName;
	private String teamId;
	private String remark;
	private Date updateTime;
	private boolean isBind;
	private String isBindParam;
	private String agentNo;
	private String agentNode;
	private String oneLevelId;

	public String getHasSn() {
		return hasSn;
	}

	public void setHasSn(String hasSn) {
		this.hasSn = hasSn;
	}

	public String getAgentNode() {
		return agentNode;
	}

	public void setAgentNode(String agentNode) {
		this.agentNode = agentNode;
	}

	public String getOneAgentName() {
		return oneAgentName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public void setOneAgentName(String oneAgentName) {
		this.oneAgentName = oneAgentName;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getOneLevelId() {
		return oneLevelId;
	}

	public void setOneLevelId(String oneLevelId) {
		this.oneLevelId = oneLevelId;
	}

	public boolean isBind() {
		return isBind;
	}

	public void setBind(boolean bind) {
		isBind = bind;
	}

	public String getIsBindParam() {
		return isBindParam;
	}

	public void setIsBindParam(String isBindParam) {
		this.isBindParam = isBindParam;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMerchantNo() {
		return merchantNo;
	}
	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getMobilephone() {
		return mobilephone;
	}
	public void setMobilephone(String mobilephone) {
		this.mobilephone = mobilephone;
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
	public String getMerAccount() {
		return merAccount;
	}
	public void setMerAccount(String merAccount) {
		this.merAccount = merAccount;
	}

	public String getHpName() {
		return hpName;
	}
	public void setHpName(String hpName) {
		this.hpName = hpName;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }
}
