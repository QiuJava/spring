package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * table  agent_info
 * desc 代理商信息表
 */
/**
 * @author pc20160100
 * 代理商信息表
 *
 */
public class AgentInfo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String agentNo;

    private String agentNode;

    private String agentName;

    private Integer agentLevel;

    private String parentId;

    private String oneLevelId;

    private Integer isOem;

    private Integer teamId;

    private String email;

    private String phone;

    private String cluster;

    private Integer invest;

    private String agentArea;

    private String mobilephone;

    private String linkName;

    private BigDecimal investAmount;

    private String address;

    private String accountName;

    private Integer accountType;

    private String accountNo;

    private String bankName;

    private String cnapsNo;

    private String saleName;

    private String creator;

    private String mender;

    private Date lastUpdateTime;

    private String status;

    private Date createDate;
    
    private String publicQrcode;
    
    private String managerLogo;
    private String managerLogoLink;
    
    private String logoRemark;
    
    private String clientLogo;
    private String clientLogoLink;
    
    private int isApprove;
    
    private int countLevel;
    
    private String customTel;
    
    private String parentAgentName;
    
    private String province;
    
    private String city;
    
    private String area;
    
    private String profitSwitch;

    private String level;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName == null ? null : agentName.trim();
    }

    public Integer getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getOneLevelId() {
        return oneLevelId;
    }

    public void setOneLevelId(String oneLevelId) {
        this.oneLevelId = oneLevelId == null ? null : oneLevelId.trim();
    }

    public Integer getIsOem() {
        return isOem;
    }

    public void setIsOem(Integer isOem) {
        this.isOem = isOem;
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster == null ? null : cluster.trim();
    }

    public Integer getInvest() {
        return invest;
    }

    public void setInvest(Integer invest) {
        this.invest = invest;
    }

    public String getAgentArea() {
        return agentArea;
    }

    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea == null ? null : agentArea.trim();
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone == null ? null : mobilephone.trim();
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName == null ? null : linkName.trim();
    }

    public BigDecimal getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(BigDecimal investAmount) {
        this.investAmount = investAmount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo == null ? null : accountNo.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getCnapsNo() {
        return cnapsNo;
    }

    public void setCnapsNo(String cnapsNo) {
        this.cnapsNo = cnapsNo == null ? null : cnapsNo.trim();
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName == null ? null : saleName.trim();
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getMender() {
        return mender;
    }

    public void setMender(String mender) {
        this.mender = mender == null ? null : mender.trim();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

	public String getPublicQrcode() {
		return publicQrcode;
	}

	public void setPublicQrcode(String publicQrcode) {
		this.publicQrcode = publicQrcode;
	}

	public String getManagerLogo() {
		return managerLogo;
	}

	public void setManagerLogo(String managerLogo) {
		this.managerLogo = managerLogo;
	}

	public String getManagerLogoLink() {
		return managerLogoLink;
	}

	public void setManagerLogoLink(String managerLogoLink) {
		this.managerLogoLink = managerLogoLink;
	}


	public String getLogoRemark() {
		return logoRemark;
	}

	public void setLogoRemark(String logoRemark) {
		this.logoRemark = logoRemark;
	}

	public String getClientLogo() {
		return clientLogo;
	}

	public void setClientLogo(String clientLogo) {
		this.clientLogo = clientLogo;
	}
	
	public String getClientLogoLink() {
		return clientLogoLink;
	}
	
	public void setClientLogoLink(String clientLogoLink) {
		this.clientLogoLink = clientLogoLink;
	}

	public int getIsApprove() {
		return isApprove;
	}

	public void setIsApprove(int isApprove) {
		this.isApprove = isApprove;
	}

	public int getCountLevel() {
		return countLevel;
	}

	public void setCountLevel(int countLevel) {
		this.countLevel = countLevel;
	}

	public String getCustomTel() {
		return customTel;
	}

	public void setCustomTel(String customTel) {
		this.customTel = customTel;
	}

	public String getParentAgentName() {
		return parentAgentName;
	}

	public void setParentAgentName(String parentAgentName) {
		this.parentAgentName = parentAgentName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProfitSwitch() {
		return profitSwitch;
	}

	public void setProfitSwitch(String profitSwitch) {
		this.profitSwitch = profitSwitch;
	}
	
	
	
}