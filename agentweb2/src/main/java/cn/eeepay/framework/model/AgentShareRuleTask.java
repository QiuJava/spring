package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class AgentShareRuleTask {
    private Integer id;

    private Long shareId;
//    @JSONField(format = "yyyy-MM-dd")
    private Date efficientDate;

    private Integer effectiveStatus;

    private Integer profitType;

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

	private BigDecimal ladder1Rate;

	private BigDecimal ladder1Max;

	private BigDecimal ladder2Rate;

	private BigDecimal ladder2Max;

	private BigDecimal ladder3Rate;

	private BigDecimal ladder3Max;

	private BigDecimal ladder4Rate;

	private BigDecimal ladder4Max;

	private String income;

	private String cost;
	
	private String ladderRate;
	
	private Integer checkStatus;
	private String serviceType;
	private String serviceId;
    private String cardType;
    private String holidaysMark;

    private BigDecimal shareProfitPercentHistory;//修改前分润比例

    private BigDecimal costHistory;//修改前代理商成本

    public BigDecimal getShareProfitPercentHistory() {
        return shareProfitPercentHistory;
    }

    public void setShareProfitPercentHistory(BigDecimal shareProfitPercentHistory) {
        this.shareProfitPercentHistory = shareProfitPercentHistory;
    }

    public BigDecimal getCostHistory() {
        return costHistory;
    }

    public void setCostHistory(BigDecimal costHistory) {
        this.costHistory = costHistory;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getHolidaysMark() {
        return holidaysMark;
    }

    public void setHolidaysMark(String holidaysMark) {
        this.holidaysMark = holidaysMark;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    public Date getEfficientDate() {
        return efficientDate;
    }

    public void setEfficientDate(Date efficientDate) {
        this.efficientDate = efficientDate;
    }

    public Integer getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(Integer effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public Integer getProfitType() {
        return profitType;
    }

    public void setProfitType(Integer profitType) {
        this.profitType = profitType ;
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


    public BigDecimal getLadder1Rate() {
        return ladder1Rate;
    }

    public void setLadder1Rate(BigDecimal ladder1Rate) {
        this.ladder1Rate = ladder1Rate;
    }

    public BigDecimal getLadder1Max() {
        return ladder1Max;
    }

    public void setLadder1Max(BigDecimal ladder1Max) {
        this.ladder1Max = ladder1Max;
    }

    public BigDecimal getLadder2Rate() {
        return ladder2Rate;
    }

    public void setLadder2Rate(BigDecimal ladder2Rate) {
        this.ladder2Rate = ladder2Rate;
    }

    public BigDecimal getLadder2Max() {
        return ladder2Max;
    }

    public void setLadder2Max(BigDecimal ladder2Max) {
        this.ladder2Max = ladder2Max;
    }

    public BigDecimal getLadder3Rate() {
        return ladder3Rate;
    }

    public void setLadder3Rate(BigDecimal ladder3Rate) {
        this.ladder3Rate = ladder3Rate;
    }

    public BigDecimal getLadder3Max() {
        return ladder3Max;
    }

    public void setLadder3Max(BigDecimal ladder3Max) {
        this.ladder3Max = ladder3Max;
    }

    public BigDecimal getLadder4Rate() {
        return ladder4Rate;
    }

    public void setLadder4Rate(BigDecimal ladder4Rate) {
        this.ladder4Rate = ladder4Rate;
    }

    public BigDecimal getLadder4Max() {
        return ladder4Max;
    }

    public void setLadder4Max(BigDecimal ladder4Max) {
        this.ladder4Max = ladder4Max;
    }

	public String getIncome() {
		return income;
	}

	public String getCost() {
		return cost;
	}

	public String getLadderRate() {
		return ladderRate;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public void setLadderRate(String ladderRate) {
		this.ladderRate = ladderRate;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}
    
	
}