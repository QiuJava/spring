package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * table
 * desc 代理商分润提现
 */

public class SettleOrderInfo implements Serializable{

	private static final long serialVersionUID = 1L;

	private Long settleOrder;

    private String agentNo;

    private String agentName;

    private BigDecimal settleAmount;

    private BigDecimal feeAmount;

    private String status;

    private String settleAccountName;

    private String settleAccountNo;

    private String settleUserNo;

    private Date createTime;

    private String settleMsg;

    private String startTime;

    private String endTime;

    private String settleStatus;

    private String inAccName;

    private String inAccNo;

    private String subType;


    public Long getSettleOrder() {
        return settleOrder;
    }

    public void setSettleOrder(Long settleOrder) {
        this.settleOrder = settleOrder;
    }

    public String getAgentNo() {
        return agentNo;
    }

    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public BigDecimal getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(BigDecimal settleAmount) {
        this.settleAmount = settleAmount;
    }

    public BigDecimal getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSettleAccountName() {
        return settleAccountName;
    }

    public void setSettleAccountName(String settleAccountName) {
        this.settleAccountName = settleAccountName;
    }

    public String getSettleAccountNo() {
        return settleAccountNo;
    }

    public void setSettleAccountNo(String settleAccountNo) {
        this.settleAccountNo = settleAccountNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSettleUserNo() {
        return settleUserNo;
    }

    public void setSettleUserNo(String settleUserNo) {
        this.settleUserNo = settleUserNo;
    }

    public String getSettleMsg() {
        return settleMsg;
    }

    public void setSettleMsg(String settleMsg) {
        this.settleMsg = settleMsg;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getInAccName() {
        return inAccName;
    }

    public void setInAccName(String inAccName) {
        this.inAccName = inAccName;
    }

    public String getInAccNo() {
        return inAccNo;
    }

    public void setInAccNo(String inAccNo) {
        this.inAccNo = inAccNo;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }
}