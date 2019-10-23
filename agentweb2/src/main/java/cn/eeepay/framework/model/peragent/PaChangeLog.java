package cn.eeepay.framework.model.peragent;

import java.sql.Timestamp;

/**
 * @author MXG
 * create 2018/11/19
 */
public class PaChangeLog {
    private int id;
    private String changePre;
    private String changeAfter;
    private String remark;
    private Timestamp createTime;
    private String operater;
    private String operMethod;

    public PaChangeLog() {
    }

    public PaChangeLog(String changePre, String changeAfter, String remark, String operater, String operMethod) {
        this.changePre = changePre;
        this.changeAfter = changeAfter;
        this.remark = remark;
        this.operater = operater;
        this.operMethod = operMethod;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChangePre() {
        return changePre;
    }

    public void setChangePre(String changePre) {
        this.changePre = changePre;
    }

    public String getChangeAfter() {
        return changeAfter;
    }

    public void setChangeAfter(String changeAfter) {
        this.changeAfter = changeAfter;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getOperMethod() {
        return operMethod;
    }

    public void setOperMethod(String operMethod) {
        this.operMethod = operMethod;
    }
}
