package cn.eeepay.framework.model;

import java.util.Date;

public class RankingRecordDetailInfo {

    private Long id;
    private String recordId;        //统计记录id
    private String userName;        //用户姓名
    private String nickName;        //昵称
    private String deNickName;      //转码后的昵称
    private String userCode;        //用户编号
    private String userTotalAmount; //统计的数据
    private String isRank;          //是否获奖0未获奖 1获奖
    private String rankingLevel;    //获奖等级
    private String rankingAmount;   //获奖奖金
    private String status;          //发放状态0未发放 1已发放
    private Date removeTime;        //移除时间
    private Date pushTime;          //发放时间
    private String remark;          //说明
    private String rankingIndex;        //排名

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserTotalAmount() {
        return userTotalAmount;
    }

    public void setUserTotalAmount(String userTotalAmount) {
        this.userTotalAmount = userTotalAmount;
    }

    public String getIsRank() {
        return isRank;
    }

    public void setIsRank(String isRank) {
        this.isRank = isRank;
    }

    public String getRankingLevel() {
        return rankingLevel;
    }

    public void setRankingLevel(String rankingLevel) {
        this.rankingLevel = rankingLevel;
    }

    public String getRankingAmount() {
        return rankingAmount;
    }

    public void setRankingAmount(String rankingAmount) {
        this.rankingAmount = rankingAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getRemoveTime() {
        return removeTime;
    }

    public void setRemoveTime(Date removeTime) {
        this.removeTime = removeTime;
    }

    public Date getPushTime() {
        return pushTime;
    }

    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRankingIndex() {
        return rankingIndex;
    }

    public void setRankingIndex(String rankingIndex) {
        this.rankingIndex = rankingIndex;
    }

    public String getDeNickName() {
        return deNickName;
    }

    public void setDeNickName(String deNickName) {
        this.deNickName = deNickName;
    }
}
