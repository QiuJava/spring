package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * superbank.red_account_detail
 */
public class RedAccountDetail {
    private Long id;

    private Long redAccountId;//所属账户id关联red_account_info表主键

    private String accountCode;//红包账号

    private Date createDate;//交易时间

    private String type;//交易类型(0发红包，1抢红包，2红包分润，3过期余额回收，4其他账户转入，5转出其他账户，6风控关闭红包，7风控打开关闭的红包)

    private BigDecimal transAmount;//交易金额（金额可正，可负）

    private Long redOrderId;//红包订单id关联red_orders的主键

    private String remark;//备注

    private String createDateStart;

    private String createDateEnd;

    private String busType;//业务类型

    private String transAmountStr;

    private String accountName;

    private String userType;

    private String createDateStr;

    private Integer selectType;//查询类型，1表示汇总

    private BigDecimal transAmountSum;//汇总金额

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRedAccountId() {
        return redAccountId;
    }

    public void setRedAccountId(Long redAccountId) {
        this.redAccountId = redAccountId;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode == null ? null : accountCode.trim();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Long getRedOrderId() {
        return redOrderId;
    }

    public void setRedOrderId(Long redOrderId) {
        this.redOrderId = redOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getCreateDateStart() {
        return createDateStart;
    }

    public void setCreateDateStart(String createDateStart) {
        this.createDateStart = createDateStart;
    }

    public String getCreateDateEnd() {
        return createDateEnd;
    }

    public void setCreateDateEnd(String createDateEnd) {
        this.createDateEnd = createDateEnd;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getTransAmountStr() {
        return transAmountStr;
    }

    public void setTransAmountStr(String transAmountStr) {
        this.transAmountStr = transAmountStr;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreateDateStr() {
        return createDateStr;
    }

    public void setCreateDateStr(String createDateStr) {
        this.createDateStr = createDateStr;
    }

    public Integer getSelectType() {
        return selectType;
    }

    public void setSelectType(Integer selectType) {
        this.selectType = selectType;
    }

    public BigDecimal getTransAmountSum() {
        return transAmountSum;
    }

    public void setTransAmountSum(BigDecimal transAmountSum) {
        this.transAmountSum = transAmountSum;
    }
}