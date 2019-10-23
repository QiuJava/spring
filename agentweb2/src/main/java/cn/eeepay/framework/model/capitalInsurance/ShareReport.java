package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/25/025.
 * @author  liuks
 * 月报
 * 对应表 zjx_share_report
 */
public class ShareReport {

    private Integer id;
    private String billMonth;//保单创建月份
    private String oneAgentNo;//一级代理商编号
    private BigDecimal totalAmount;//保费总额
    private Integer totalCount;//保单总数
    private BigDecimal shareRate;//代理商分润百分比
    private BigDecimal shareAmount;//代理商分润金额
    private Integer accountStatus;//入账状态： 1 入账成功、2 入账失败 0 未入账
    private Date accountTime;//入账时间
    private Date accountTimeBegin;
    private Date accountTimeEnd;

    private Date createTime;//创建时间

    private String oneAgentName;//一级代理商名称
    private  String loginAgentNo;//登入代理商

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBillMonth() {
        return billMonth;
    }

    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public BigDecimal getShareRate() {
        return shareRate;
    }

    public void setShareRate(BigDecimal shareRate) {
        this.shareRate = shareRate;
    }

    public BigDecimal getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(BigDecimal shareAmount) {
        this.shareAmount = shareAmount;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getAccountTime() {
        return accountTime;
    }

    public void setAccountTime(Date accountTime) {
        this.accountTime = accountTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
    }

    public String getLoginAgentNo() {
        return loginAgentNo;
    }

    public void setLoginAgentNo(String loginAgentNo) {
        this.loginAgentNo = loginAgentNo;
    }

    public Date getAccountTimeBegin() {
        return accountTimeBegin;
    }

    public void setAccountTimeBegin(Date accountTimeBegin) {
        this.accountTimeBegin = accountTimeBegin;
    }

    public Date getAccountTimeEnd() {
        return accountTimeEnd;
    }

    public void setAccountTimeEnd(Date accountTimeEnd) {
        this.accountTimeEnd = accountTimeEnd;
    }
}
