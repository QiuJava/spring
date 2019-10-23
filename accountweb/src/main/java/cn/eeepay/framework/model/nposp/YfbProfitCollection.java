package cn.eeepay.framework.model.nposp;

import java.math.BigDecimal;
import java.util.Date;

public class YfbProfitCollection {

    private Integer id;
    private String collectionNo;                //分润汇总编号
    private String collectionBatchNo;           //汇总批次号
    private String merType;                     //商户类型 A：代理商  M:商户
    private String merNo;                       //商户号/代理商编号
    private String agentNode;                   //代理商节点
    private BigDecimal serviceCostRate;         //服务商成本费率
    private BigDecimal serviceCostSingleFee;    //服务商单笔代付成本
    private BigDecimal profitAmount;            //分润金额
    private String operator;                    //汇总人
    private Integer incomeStatus;                //入账状态  0否 1是
    private Date incomeTime;                    //入账时间
    private Date createTime;                    //创建时间
    private Date lastUpdateTime;                //最后更新时间
    private String TallyTime1;      //汇总时间
    private String TallyTime2;
    private String agentName;      //服务商名称
    private String fenMoney1;       //分润金额1
    private String fenMoney2;       //分润金额2
    private Date collectionTime;        //汇总时间
    private String agentLevel;          //代理商级别
    private String profitType;
    private String allowIncome;         //是否需要入账

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCollectionNo() {
        return collectionNo;
    }

    public void setCollectionNo(String collectionNo) {
        this.collectionNo = collectionNo;
    }

    public String getCollectionBatchNo() {
        return collectionBatchNo;
    }

    public void setCollectionBatchNo(String collectionBatchNo) {
        this.collectionBatchNo = collectionBatchNo;
    }

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType;
    }

    public String getMerNo() {
        return merNo;
    }

    public void setMerNo(String merNo) {
        this.merNo = merNo;
    }

    public String getAgentNode() {
        return agentNode;
    }

    public void setAgentNode(String agentNode) {
        this.agentNode = agentNode;
    }

    public BigDecimal getServiceCostRate() {
        return serviceCostRate;
    }

    public void setServiceCostRate(BigDecimal serviceCostRate) {
        this.serviceCostRate = serviceCostRate;
    }

    public BigDecimal getServiceCostSingleFee() {
        return serviceCostSingleFee;
    }

    public void setServiceCostSingleFee(BigDecimal serviceCostSingleFee) {
        this.serviceCostSingleFee = serviceCostSingleFee;
    }

    public BigDecimal getProfitAmount() {
        return profitAmount;
    }

    public void setProfitAmount(BigDecimal profitAmount) {
        this.profitAmount = profitAmount;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Integer getIncomeStatus() {
        return incomeStatus;
    }

    public void setIncomeStatus(Integer incomeStatus) {
        this.incomeStatus = incomeStatus;
    }

    public Date getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(Date incomeTime) {
        this.incomeTime = incomeTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getTallyTime1() {
        return TallyTime1;
    }

    public void setTallyTime1(String tallyTime1) {
        TallyTime1 = tallyTime1;
    }

    public String getTallyTime2() {
        return TallyTime2;
    }

    public void setTallyTime2(String tallyTime2) {
        TallyTime2 = tallyTime2;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getFenMoney1() {
        return fenMoney1;
    }

    public void setFenMoney1(String fenMoney1) {
        this.fenMoney1 = fenMoney1;
    }

    public String getFenMoney2() {
        return fenMoney2;
    }

    public void setFenMoney2(String fenMoney2) {
        this.fenMoney2 = fenMoney2;
    }

    public Date getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(Date collectionTime) {
        this.collectionTime = collectionTime;
    }

    public String getAgentLevel() {
        return agentLevel;
    }

    public void setAgentLevel(String agentLevel) {
        this.agentLevel = agentLevel;
    }

    public String getProfitType() {
        return profitType;
    }

    public void setProfitType(String profitType) {
        this.profitType = profitType;
    }

    public String getAllowIncome() {
        return allowIncome;
    }

    public void setAllowIncome(String allowIncome) {
        this.allowIncome = allowIncome;
    }
}
