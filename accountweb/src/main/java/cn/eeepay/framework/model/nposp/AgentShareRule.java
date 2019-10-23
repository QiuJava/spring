package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AgentShareRule implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private String agentId;

    private String serviceId;
    
    private String serviceName;

    private String cardType;

    private String holidaysMark;

    private Date efficientDate;

    private Date disabledDate;

    private String profitType;

    private BigDecimal perFixIncome;

    private BigDecimal perFixInrate;

    private BigDecimal safeLine;

    private BigDecimal capping;

    private BigDecimal shareProfitPercent;

    private String ladder;

    private String costRateType;

    private BigDecimal perFixCost;

    private BigDecimal costRate;

    private BigDecimal costCapping;

    private BigDecimal costSafeline;

    private String checkStatus;

    private String lockStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId == null ? null : agentId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType == null ? null : cardType.trim();
    }

    public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark == null ? null : holidaysMark.trim();
    }

    public Date getEfficientDate() {
        return efficientDate;
    }

    public void setEfficientDate(Date efficientDate) {
        this.efficientDate = efficientDate;
    }

    public Date getDisabledDate() {
        return disabledDate;
    }

    public void setDisabledDate(Date disabledDate) {
        this.disabledDate = disabledDate;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType == null ? null : profitType.trim();
    }

    public BigDecimal getPerFixIncome() {
        return perFixIncome;
    }

    public void setPerFixIncome(BigDecimal perFixIncome) {
        this.perFixIncome = perFixIncome;
    }

    public BigDecimal getPerFixInrate() {
        return perFixInrate;
    }

    public void setPerFixInrate(BigDecimal perFixInrate) {
        this.perFixInrate = perFixInrate;
    }

    public BigDecimal getSafeLine() {
        return safeLine;
    }

    public void setSafeLine(BigDecimal safeLine) {
        this.safeLine = safeLine;
    }

    public BigDecimal getCapping() {
        return capping;
    }

    public void setCapping(BigDecimal capping) {
        this.capping = capping;
    }

    public BigDecimal getShareProfitPercent() {
        return shareProfitPercent;
    }

    public void setShareProfitPercent(BigDecimal shareProfitPercent) {
        this.shareProfitPercent = shareProfitPercent;
    }

    public String getLadder() {
        return ladder;
    }

    public void setLadder(String ladder) {
        this.ladder = ladder == null ? null : ladder.trim();
    }

    public String getCostRateType() {
        return costRateType;
    }

    public void setCostRateType(String costRateType) {
        this.costRateType = costRateType == null ? null : costRateType.trim();
    }

    public BigDecimal getPerFixCost() {
        return perFixCost;
    }

    public void setPerFixCost(BigDecimal perFixCost) {
        this.perFixCost = perFixCost;
    }

    public BigDecimal getCostRate() {
        return costRate;
    }

    public void setCostRate(BigDecimal costRate) {
        this.costRate = costRate;
    }

    public BigDecimal getCostCapping() {
        return costCapping;
    }

    public void setCostCapping(BigDecimal costCapping) {
        this.costCapping = costCapping;
    }

    public BigDecimal getCostSafeline() {
        return costSafeline;
    }

    public void setCostSafeline(BigDecimal costSafeline) {
        this.costSafeline = costSafeline;
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
    
}