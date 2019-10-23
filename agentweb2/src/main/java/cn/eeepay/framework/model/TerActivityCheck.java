package cn.eeepay.framework.model;

import java.util.Date;

/**
 * @author tgh
 * @description 活动考核机具
 * @date 2019/9/9
 */
public class TerActivityCheck {

    private Long id;
    private String sn;
    private String dueDays;//考核剩余天数
    private String dueDaysMin;//考核剩余天数
    private String dueDaysMax;//考核剩余天数
    private String status;//激活状态
    private String standardStatus;//考核达标状态
    private Date updateTime;
    private Date checkTime;//考核日期
    private String checkTimeStart;
    private String checkTimeEnd;
    private String entityEnd;
    private String entityNode;
    private String snStart;
    private String snEnd;
    private String agentNodeTwo;//二级代理商节点
    private String agentNoTwo;//二级代理商编号
    private String agentNameTwo;//二级代理商名称
    private String agentNo;//所属代理商编号
    private String agentName;//所属代理商名称
    private String agentNode;//所属代理商节点
    private String type;//硬件产品种类
    private String bpId;//业务产品id
    private String productType;//业务产品
    private String bpName;//业务产品id
    private String activityTypeNo;//欢乐返子类型
    private String openStatus;//机具状态
    private String sort;//排序,按考核剩余天数
    private String hasChild;//是否包含下级,1包含,0不包含

    public String getDueDaysMin() {
        return dueDaysMin;
    }

    public void setDueDaysMin(String dueDaysMin) {
        this.dueDaysMin = dueDaysMin;
    }

    public String getDueDaysMax() {
        return dueDaysMax;
    }

    public void setDueDaysMax(String dueDaysMax) {
        this.dueDaysMax = dueDaysMax;
    }

    public String getHasChild() {
        return hasChild;
    }

    public void setHasChild(String hasChild) {
        this.hasChild = hasChild;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDueDays() {
        return dueDays;
    }

    public void setDueDays(String dueDays) {
        this.dueDays = dueDays;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStandardStatus() {
        return standardStatus;
    }

    public void setStandardStatus(String standardStatus) {
        this.standardStatus = standardStatus;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckTimeStart() {
        return checkTimeStart;
    }

    public void setCheckTimeStart(String checkTimeStart) {
        this.checkTimeStart = checkTimeStart;
    }

    public String getCheckTimeEnd() {
        return checkTimeEnd;
    }

    public void setCheckTimeEnd(String checkTimeEnd) {
        this.checkTimeEnd = checkTimeEnd;
    }

    public String getEntityEnd() {
        return entityEnd;
    }

    public void setEntityEnd(String entityEnd) {
        this.entityEnd = entityEnd;
    }

    public String getEntityNode() {
        return entityNode;
    }

    public void setEntityNode(String entityNode) {
        this.entityNode = entityNode;
    }

    public String getSnStart() {
        return snStart;
    }

    public void setSnStart(String snStart) {
        this.snStart = snStart;
    }

    public String getSnEnd() {
        return snEnd;
    }

    public void setSnEnd(String snEnd) {
        this.snEnd = snEnd;
    }

    public String getAgentNodeTwo() {
        return agentNodeTwo;
    }

    public void setAgentNodeTwo(String agentNodeTwo) {
        this.agentNodeTwo = agentNodeTwo;
    }

    public String getAgentNoTwo() {
        return agentNoTwo;
    }

    public void setAgentNoTwo(String agentNoTwo) {
        this.agentNoTwo = agentNoTwo;
    }

    public String getAgentNameTwo() {
        return agentNameTwo;
    }

    public void setAgentNameTwo(String agentNameTwo) {
        this.agentNameTwo = agentNameTwo;
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

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBpId() {
        return bpId;
    }

    public void setBpId(String bpId) {
        this.bpId = bpId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getActivityTypeNo() {
        return activityTypeNo;
    }

    public void setActivityTypeNo(String activityTypeNo) {
        this.activityTypeNo = activityTypeNo;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
