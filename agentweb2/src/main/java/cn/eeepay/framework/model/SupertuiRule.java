package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 超级推规则表
 * 
 * @author junhu
 *
 */
public class SupertuiRule {
    private Long id;

    private String bpId;

    private BigDecimal recommendedAmount;

    private String rewardValidNode;

    private BigDecimal orderAgentFee;

    private String feeValidNode;

    private Date efficientDate;

    private Date disabledDate;

    private BigDecimal orderThreshold;

    private BigDecimal grabOrderTime;

    private BigDecimal feedbackTime;

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
        this.bpId = bpId;
    }

    public BigDecimal getRecommendedAmount() {
        return recommendedAmount;
    }

    public void setRecommendedAmount(BigDecimal recommendedAmount) {
        this.recommendedAmount = recommendedAmount;
    }

    public String getRewardValidNode() {
        return rewardValidNode;
    }

    public void setRewardValidNode(String rewardValidNode) {
        this.rewardValidNode = rewardValidNode;
    }

    public BigDecimal getOrderAgentFee() {
        return orderAgentFee;
    }

    public void setOrderAgentFee(BigDecimal orderAgentFee) {
        this.orderAgentFee = orderAgentFee;
    }

    public String getFeeValidNode() {
        return feeValidNode;
    }

    public void setFeeValidNode(String feeValidNode) {
        this.feeValidNode = feeValidNode;
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

    public BigDecimal getOrderThreshold() {
        return orderThreshold;
    }

    public void setOrderThreshold(BigDecimal orderThreshold) {
        this.orderThreshold = orderThreshold;
    }

    public BigDecimal getGrabOrderTime() {
        return grabOrderTime;
    }

    public void setGrabOrderTime(BigDecimal grabOrderTime) {
        this.grabOrderTime = grabOrderTime;
    }

    public BigDecimal getFeedbackTime() {
        return feedbackTime;
    }

    public void setFeedbackTime(BigDecimal feedbackTime) {
        this.feedbackTime = feedbackTime;
    }
}