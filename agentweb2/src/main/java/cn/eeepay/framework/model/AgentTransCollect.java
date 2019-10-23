package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class AgentTransCollect {
    private Long id;

    private Date transTime;

    private String agentNo;

    private String agentNode;

    private String bpId;

    private String serviceId;

    private BigDecimal transAmount;

    private Integer singleCount;

    private BigDecimal agentShareAmount;
    
    private Date calcTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Date getTransTime() {
		return transTime;
	}

	public void setTransTime(Date transTime) {
		this.transTime = transTime;
	}

	public Date getCalcTime() {
		return calcTime;
	}

	public void setCalcTime(Date calcTime) {
		this.calcTime = calcTime;
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

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId == null ? null : bpId.trim();
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId == null ? null : serviceId.trim();
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Integer getSingleCount() {
        return singleCount;
    }

    public void setSingleCount(Integer singleCount) {
        this.singleCount = singleCount;
    }

    public BigDecimal getAgentShareAmount() {
        return agentShareAmount;
    }

    public void setAgentShareAmount(BigDecimal agentShareAmount) {
        this.agentShareAmount = agentShareAmount;
    }
}