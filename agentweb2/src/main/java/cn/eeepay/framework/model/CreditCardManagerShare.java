package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

public class CreditCardManagerShare {
    private Long id;
    private Date createDate;    //分润创建日期
    private BigDecimal shareCash;//分润金额
    private Integer sharePercentage;    //分润百分比
    private Integer enterStatus;        //入账状态  1-未入账  2-已入账  3-入账失败
    private String shareAgentName;      //分润代理商名称
    private String shareAgentNo;        //分润代理商编号
    private String relatedOrderNo;       //关联订单号
    private BigDecimal orderCash;       //订单金额
    private Integer orderType;          //订单类型  1-会员服务费  2-其他
    private String userId;              //产生分润的用户ID
    private String belongAgentName;         //所属代理商名称
    private String belongAgentNo;           //所属代理商编号
    private Date enterDate;                 //入账日期


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public BigDecimal getShareCash() {
        return shareCash;
    }

    public void setShareCash(BigDecimal shareCash) {
        this.shareCash = shareCash;
    }

    public Integer getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(Integer sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public Integer getEnterStatus() {
        return enterStatus;
    }

    public void setEnterStatus(Integer enterStatus) {
        this.enterStatus = enterStatus;
    }

    public String getShareAgentName() {
        return shareAgentName;
    }

    public void setShareAgentName(String shareAgentName) {
        this.shareAgentName = shareAgentName;
    }

    public String getShareAgentNo() {
        return shareAgentNo;
    }

    public void setShareAgentNo(String shareAgentNo) {
        this.shareAgentNo = shareAgentNo;
    }

    public String getRelatedOrderNo() {
        return relatedOrderNo;
    }

    public void setRelatedOrderNo(String relatedOrderNo) {
        this.relatedOrderNo = relatedOrderNo;
    }

    public BigDecimal getOrderCash() {
        return orderCash;
    }

    public void setOrderCash(BigDecimal orderCash) {
        this.orderCash = orderCash;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBelongAgentName() {
        return belongAgentName;
    }

    public void setBelongAgentName(String belongAgentName) {
        this.belongAgentName = belongAgentName;
    }

    public String getBelongAgentNo() {
        return belongAgentNo;
    }

    public void setBelongAgentNo(String belongAgentNo) {
        this.belongAgentNo = belongAgentNo;
    }

    public Date getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(Date enterDate) {
        this.enterDate = enterDate;
    }
}
