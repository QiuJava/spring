package cn.eeepay.framework.model.capitalInsurance;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2018/7/23/023.
 * @author  liuks
 * 保险订单
 * 对应表 zjx_trans_order
 * settle_transfer 出款表
 * collective_trans_order 交易表
 */
public class SafeOrder {

    private  Integer id;//id
    private  String bxOrderNo;//保险订单号
    private  String orderNo;//交易订单号(外键交易表)
    private  String thirdOrderNo;//保单号(合作方的订单号)
    private  String merchantNo;//商户号(外键商户表)
    private  String oneAgentNo;//一级代理商编号
    private  String bxUnit;//承保单位(目前只有前海财险)
    private  String prodNo;//产品编码(前海财险提供的)
    private  BigDecimal nAmt;//保额(元，保留两位小数)
    private  BigDecimal nPrm;//保费-售价(元，保留两位小数)
    private  String bxType;//投保状态:SUCCESS：成功,FAILED：失败,INIT：初始化,OVERLIMIT：已退保

    private Date tTime;//投保时间
    private Date tTimeBegin;
    private Date tTimeEnd;
    private Date tBeginTime;//保险起期
    private Date tEndTime;//保险止期
    private Date createTime;//创建时间


    private  String settlementMethod;//结算方式 0 t0,1 t1
    private BigDecimal transAmount;//交易金额

    private Integer lowerAgent;//是否包含下级状态

    private  String oneAgentName;//一级代理商名称
    private  String agentNo;//所属代理商编号
    private  String agentName;//所属代理商编号

    private  String loginAgentNo;//登入代理商

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBxOrderNo() {
        return bxOrderNo;
    }

    public void setBxOrderNo(String bxOrderNo) {
        this.bxOrderNo = bxOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getThirdOrderNo() {
        return thirdOrderNo;
    }

    public void setThirdOrderNo(String thirdOrderNo) {
        this.thirdOrderNo = thirdOrderNo;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getOneAgentNo() {
        return oneAgentNo;
    }

    public void setOneAgentNo(String oneAgentNo) {
        this.oneAgentNo = oneAgentNo;
    }

    public String getBxUnit() {
        return bxUnit;
    }

    public void setBxUnit(String bxUnit) {
        this.bxUnit = bxUnit;
    }

    public String getProdNo() {
        return prodNo;
    }

    public void setProdNo(String prodNo) {
        this.prodNo = prodNo;
    }

    public BigDecimal getnAmt() {
        return nAmt;
    }

    public void setnAmt(BigDecimal nAmt) {
        this.nAmt = nAmt;
    }

    public BigDecimal getnPrm() {
        return nPrm;
    }

    public void setnPrm(BigDecimal nPrm) {
        this.nPrm = nPrm;
    }

    public String getBxType() {
        return bxType;
    }

    public void setBxType(String bxType) {
        this.bxType = bxType;
    }

    public Date gettTime() {
        return tTime;
    }

    public void settTime(Date tTime) {
        this.tTime = tTime;
    }

    public Date gettTimeBegin() {
        return tTimeBegin;
    }

    public void settTimeBegin(Date tTimeBegin) {
        this.tTimeBegin = tTimeBegin;
    }

    public Date gettTimeEnd() {
        return tTimeEnd;
    }

    public void settTimeEnd(Date tTimeEnd) {
        this.tTimeEnd = tTimeEnd;
    }

    public Date gettBeginTime() {
        return tBeginTime;
    }

    public void settBeginTime(Date tBeginTime) {
        this.tBeginTime = tBeginTime;
    }

    public Date gettEndTime() {
        return tEndTime;
    }

    public void settEndTime(Date tEndTime) {
        this.tEndTime = tEndTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod;
    }

    public BigDecimal getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(BigDecimal transAmount) {
        this.transAmount = transAmount;
    }

    public Integer getLowerAgent() {
        return lowerAgent;
    }

    public void setLowerAgent(Integer lowerAgent) {
        this.lowerAgent = lowerAgent;
    }

    public String getOneAgentName() {
        return oneAgentName;
    }

    public void setOneAgentName(String oneAgentName) {
        this.oneAgentName = oneAgentName;
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

    public String getLoginAgentNo() {
        return loginAgentNo;
    }

    public void setLoginAgentNo(String loginAgentNo) {
        this.loginAgentNo = loginAgentNo;
    }
}
