package cn.eeepay.framework.model;

import java.math.BigDecimal;

/**
 * @author pc20160100
 * table service_manage_quota
 * desc 
 */
public class ServiceQuota {
    private Long id;

    private Long serviceId;
    
    private String serviceName;

    private String holidaysMark;

    private String cardType;

    private String quotaLevel;

    private String agentNo;

    private BigDecimal singleDayAmount;

    private BigDecimal serviceManageQuotacol;

    private BigDecimal singleCountAmount;

    private BigDecimal singleMinAmount;

    private BigDecimal singleDaycardAmount;

    private Integer singleDaycardCount;

    private String checkStatus;

    private String lockStatus;
    
    private Integer isGlobal;
    private int fixedQuota;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId ;
    }

    public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark == null ? null : holidaysMark.trim();
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getQuotaLevel() {
        return quotaLevel;
    }

    public void setQuotaLevel(String quotaLevel) {
        this.quotaLevel = quotaLevel == null ? null : quotaLevel.trim();
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo == null ? null : agentNo.trim();
    }

    public BigDecimal getSingleDayAmount() {
        return singleDayAmount;
    }

    public void setSingleDayAmount(BigDecimal singleDayAmount) {
        this.singleDayAmount = singleDayAmount;
    }

    public BigDecimal getServiceManageQuotacol() {
        return serviceManageQuotacol;
    }

    public void setServiceManageQuotacol(BigDecimal serviceManageQuotacol) {
        this.serviceManageQuotacol = serviceManageQuotacol;
    }

    public BigDecimal getSingleCountAmount() {
        return singleCountAmount;
    }

    public void setSingleCountAmount(BigDecimal singleCountAmount) {
        this.singleCountAmount = singleCountAmount;
    }

    public BigDecimal getSingleDaycardAmount() {
        return singleDaycardAmount;
    }

    public void setSingleDaycardAmount(BigDecimal singleDaycardAmount) {
        this.singleDaycardAmount = singleDaycardAmount;
    }

    public Integer getSingleDaycardCount() {
        return singleDaycardCount;
    }

    public void setSingleDaycardCount(Integer singleDaycardCount) {
        this.singleDaycardCount = singleDaycardCount;
    }

    public String getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus == null ? null : lockStatus.trim();
    }
    
    public Integer getIsGlobal() {
		return isGlobal;
	}

	public void setIsGlobal(Integer isGlobal) {
		this.isGlobal = isGlobal;
	}

	public int getFiexdQuota() {
		return fixedQuota;
	}

	public void setFiexdQuota(int fixedQuota) {
		this.fixedQuota = fixedQuota;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getFixedQuota() {
		return fixedQuota;
	}

	public void setFixedQuota(int fixedQuota) {
		this.fixedQuota = fixedQuota;
	}

	public BigDecimal getSingleMinAmount() {
		return singleMinAmount;
	}

	public void setSingleMinAmount(BigDecimal singleMinAmount) {
		this.singleMinAmount = singleMinAmount;
	}

}