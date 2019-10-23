package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author tgh
 * @description 分润修改记录
 * @date 2019/6/14
 */
public class ProfitUpdateRecord {

    private Long id;

    private String shareId;//分润规则id

    private String costHistory;//修改前代理商成本

    private String cost;//修改后代理商成本

    private BigDecimal shareProfitPercentHistory;//修改前分润比例

    private BigDecimal shareProfitPercent;//修改后分润比例

    private Date efficientDate;//生效日期

    private String effectiveStatus;//是否生效:0-未生效,1-已生效

    private Date updateDate;//修改日期

    private String auther;//修改人

    private Integer shareTaskId;//对应agent_share_rule_task表id

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getCostHistory() {
        return costHistory;
    }

    public void setCostHistory(String costHistory) {
        this.costHistory = costHistory;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public BigDecimal getShareProfitPercentHistory() {
        return shareProfitPercentHistory;
    }

    public void setShareProfitPercentHistory(BigDecimal shareProfitPercentHistory) {
        this.shareProfitPercentHistory = shareProfitPercentHistory;
    }

    public BigDecimal getShareProfitPercent() {
        return shareProfitPercent;
    }

    public void setShareProfitPercent(BigDecimal shareProfitPercent) {
        this.shareProfitPercent = shareProfitPercent;
    }

    public Date getEfficientDate() {
        return efficientDate;
    }

    public void setEfficientDate(Date efficientDate) {
        this.efficientDate = efficientDate;
    }

    public String getEffectiveStatus() {
        return effectiveStatus;
    }

    public void setEffectiveStatus(String effectiveStatus) {
        this.effectiveStatus = effectiveStatus;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAuther() {
        return auther;
    }

    public void setAuther(String auther) {
        this.auther = auther;
    }

    public Integer getShareTaskId() {
        return shareTaskId;
    }

    public void setShareTaskId(Integer shareTaskId) {
        this.shareTaskId = shareTaskId;
    }
}
