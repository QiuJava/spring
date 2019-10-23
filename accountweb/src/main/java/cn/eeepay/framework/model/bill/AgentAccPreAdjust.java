package cn.eeepay.framework.model.bill;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/23
 * Time: 14:21
 * Description: 类注释
 */
public class AgentAccPreAdjust {

    private long id;
    private Date adjustTime;
    private String applicant;
    private String agentNo;
    private String agentName;
    private String agentLevel;
    private BigDecimal adjustAmount;
    private String adjustReason;
    private String subjectNo;
    private String remark;


    private BigDecimal activitySubsidyFreeze;		//活动补贴账户已冻结金额
    private BigDecimal activitySubsidyBalance;		//活动补贴账户余额
    private BigDecimal activitySubsidyAvailableBalance;		//活动补贴账户可用余额

    private BigDecimal adjustAmount1;
    private BigDecimal adjustAmount2;

    private BigDecimal activitySubsidyBalance1;
    private BigDecimal activitySubsidyBalance2;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(Date adjustTime) {
        this.adjustTime = adjustTime;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
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

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public BigDecimal getAdjustAmount() {
        return adjustAmount;
    }

    public void setAdjustAmount(BigDecimal adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    public String getAdjustReason() {
        return adjustReason;
    }

    public void setAdjustReason(String adjustReason) {
        this.adjustReason = adjustReason;
    }

    public String getSubjectNo() {
        return subjectNo;
    }

    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public BigDecimal getActivitySubsidyFreeze() {
        return activitySubsidyFreeze;
    }

    public void setActivitySubsidyFreeze(BigDecimal activitySubsidyFreeze) {
        this.activitySubsidyFreeze = activitySubsidyFreeze;
    }

    public BigDecimal getActivitySubsidyBalance() {
        return activitySubsidyBalance;
    }

    public void setActivitySubsidyBalance(BigDecimal activitySubsidyBalance) {
        this.activitySubsidyBalance = activitySubsidyBalance;
    }

    public BigDecimal getActivitySubsidyAvailableBalance() {
        return activitySubsidyAvailableBalance;
    }

    public void setActivitySubsidyAvailableBalance(BigDecimal activitySubsidyAvailableBalance) {
        this.activitySubsidyAvailableBalance = activitySubsidyAvailableBalance;
    }

    public BigDecimal getAdjustAmount1() {
        return adjustAmount1;
    }

    public void setAdjustAmount1(BigDecimal adjustAmount1) {
        this.adjustAmount1 = adjustAmount1;
    }

    public BigDecimal getAdjustAmount2() {
        return adjustAmount2;
    }

    public void setAdjustAmount2(BigDecimal adjustAmount2) {
        this.adjustAmount2 = adjustAmount2;
    }

    public BigDecimal getActivitySubsidyBalance1() {
        return activitySubsidyBalance1;
    }

    public void setActivitySubsidyBalance1(BigDecimal activitySubsidyBalance1) {
        this.activitySubsidyBalance1 = activitySubsidyBalance1;
    }

    public BigDecimal getActivitySubsidyBalance2() {
        return activitySubsidyBalance2;
    }

    public void setActivitySubsidyBalance2(BigDecimal activitySubsidyBalance2) {
        this.activitySubsidyBalance2 = activitySubsidyBalance2;
    }
}
