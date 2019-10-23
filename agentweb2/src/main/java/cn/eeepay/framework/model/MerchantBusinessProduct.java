package cn.eeepay.framework.model;

import java.util.Date;


/**
 * table merchant_business_product
 * desc 商户业务产品表
 */
public class MerchantBusinessProduct {
    private Long id;
    
    private String activityType;
    private String recommendedSource;
    private int quickPayment;

    public String getActivityType() {
		return activityType;
	}

	public String getShowReplace() {
		return showReplace;
	}

	public void setShowReplace(String showReplace) {
		this.showReplace = showReplace;
	}

	public void setActivityType(String activityType) {
		this.activityType = activityType;
	}

	private String merchantNo;

    private String bpId;

    //申请时间
    private Date createTime;

    private String saleName;

    private String status;

    private String examinationOpinions;
    //审核人员ID
    private String auditorId;
    
//    private AgentInfo agentInfo;
//    
//    private BusinessProductDefine businessProductDefine;
//    
//    private MerchantInfo merchantInfo;
//    

    //商户名称
    private String merchantName;
    
    
    private Date merCreateTime;
    
    //商户手机号
    private String mobilePhone;
    
    //业务产品名称
    private String bpName;

    //代理商名称
    private String agentName;
    private String riskStatus;
    //组织ID
    private String  teamId;
    private String teamName;
    private String itemSource;
    
    
    private String agentNo;
    
    private String showReplace;
    private String oneAgentNo;
    
    private String merTeamId;
	private String merGroup;
	private String sn;
	
	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
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

	public String getMerTeamId() {
		return merTeamId;
	}

	public String getOneAgentNo() {
		return oneAgentNo;
	}

	public void setOneAgentNo(String oneAgentNo) {
		this.oneAgentNo = oneAgentNo;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public Long getId() {
    	
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName == null ? null : saleName.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getExaminationOpinions() {
        return examinationOpinions;
    }

    public void setExaminationOpinions(String examinationOpinions) {
        this.examinationOpinions = examinationOpinions == null ? null : examinationOpinions.trim();
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

	public String getBpName() {
		return bpName;
	}

	public void setBpName(String bpName) {
		this.bpName = bpName;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public Date getMerCreateTime() {
		return merCreateTime;
	}

	public void setMerCreateTime(Date merCreateTime) {
		this.merCreateTime = merCreateTime;
	}

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getItemSource() {
		return itemSource;
	}

	public void setItemSource(String itemSource) {
		this.itemSource = itemSource;
	}

	public String getRiskStatus() {
		return riskStatus;
	}

	public void setRiskStatus(String riskStatus) {
		this.riskStatus = riskStatus;
	}

	public String getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(String auditorId) {
		this.auditorId = auditorId;
	}

	public String getRecommendedSource() {
		return recommendedSource;
	}

	public void setRecommendedSource(String recommendedSource) {
		this.recommendedSource = recommendedSource;
	}

	public int getQuickPayment() {
		return quickPayment;
	}

	public MerchantBusinessProduct setQuickPayment(int quickPayment) {
		this.quickPayment = quickPayment;
		return this;
	}
}