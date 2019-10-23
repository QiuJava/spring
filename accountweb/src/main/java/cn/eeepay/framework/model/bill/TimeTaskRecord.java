package cn.eeepay.framework.model.bill;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 长沙张学友
 * Date: 2018/11/6
 * Time: 11:18
 * Description: 类注释
 */
public class TimeTaskRecord {

    private Long id;
    private String runningNo;
    private String runningStatus;
    private Date createTime;
    private Date lastUpdateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRunningNo() {
        return runningNo;
    }

    public void setRunningNo(String runningNo) {
        this.runningNo = runningNo;
    }

    public String getRunningStatus() {
        return runningStatus;
    }

    public void setRunningStatus(String runningStatus) {
        this.runningStatus = runningStatus;
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
}
