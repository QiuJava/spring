package cn.eeepay.framework.model.bill;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/5/18
 * Time: 10:28
 * Description: 类注释
 */
public class BeforeAdjustApply {

    private Integer id;
    private String agentNo;
    private String agentName;
    private String applicant;
    private BigDecimal freezeAmount;
    private BigDecimal activityAvailableAmount;
    private BigDecimal activityFreezeAmount;
    private BigDecimal generateAmount;
    private Date applyDate;
    private String remark;
    private Integer isApply;

    private Integer isDetail;


    private String date1;
    private String date2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public BigDecimal getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(BigDecimal freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public BigDecimal getActivityAvailableAmount() {
        return activityAvailableAmount;
    }

    public void setActivityAvailableAmount(BigDecimal activityAvailableAmount) {
        this.activityAvailableAmount = activityAvailableAmount;
    }

    public BigDecimal getActivityFreezeAmount() {
        return activityFreezeAmount;
    }

    public void setActivityFreezeAmount(BigDecimal activityFreezeAmount) {
        this.activityFreezeAmount = activityFreezeAmount;
    }

    public BigDecimal getGenerateAmount() {
        return generateAmount;
    }

    public void setGenerateAmount(BigDecimal generateAmount) {
        this.generateAmount = generateAmount;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public Integer getIsApply() {
        return isApply;
    }

    public void setIsApply(Integer isApply) {
        this.isApply = isApply;
    }

    public Integer getIsDetail() {
        return isDetail;
    }

    public void setIsDetail(Integer isDetail) {
        this.isDetail = isDetail;
    }
}
