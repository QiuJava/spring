package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 排行榜记录
 */
public class RankingRecordInfo {
    private String id;
    private String rankingNo;       //排行榜编号
    private String batchNo;         //期号
    private String ruleNo;          //规则编号
    private String rankingName;    //排行榜名称
    private String rankingType;     //排行榜类型
    private String orgId;           //组织id
    private String orgName;         //组织名称
    private String pushNum;         //本期获奖人数
    private BigDecimal pushTotalAmount;//本期奖金
    private String pushRealNum;     //实发人数
    private BigDecimal pushRealAmount;//实发奖金
    private String status;          //榜单状态0未生成 1未发放  2已经发放
    private Date createDate;        //榜单生成时间
    private Date startDate;         //统计开始时间
    private Date endDate;           //统计结束时间
    private String dataType;		//统计数据
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRankingNo() {
        return rankingNo;
    }

    public void setRankingNo(String rankingNo) {
        this.rankingNo = rankingNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getRuleNo() {
        return ruleNo;
    }

    public void setRuleNo(String ruleNo) {
        this.ruleNo = ruleNo;
    }

    public String getRankingName() {
        return rankingName;
    }

    public void setRankingName(String rankingName) {
        this.rankingName = rankingName;
    }

    public String getRankingType() {
        return rankingType;
    }

    public void setRankingType(String rankingType) {
        this.rankingType = rankingType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPushNum() {
        return pushNum;
    }

    public void setPushNum(String pushNum) {
        this.pushNum = pushNum;
    }

    public BigDecimal getPushTotalAmount() {
        return pushTotalAmount;
    }

    public void setPushTotalAmount(BigDecimal pushTotalAmount) {
        this.pushTotalAmount = pushTotalAmount;
    }

    public String getPushRealNum() {
        return pushRealNum;
    }

    public void setPushRealNum(String pushRealNum) {
        this.pushRealNum = pushRealNum;
    }

    public BigDecimal getPushRealAmount() {
        return pushRealAmount;
    }

    public void setPushRealAmount(BigDecimal pushRealAmount) {
        this.pushRealAmount = pushRealAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
