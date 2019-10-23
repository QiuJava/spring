package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * mpos交易订单
 */
public class MposTradeOrder {

    private Long id;      //ID
    private String orderNo;      //订单编号
    private String v2OrderNo;   //v2订单编号
    private String userCode;     //机具所属用户编码
    private String status;    //订单状态:1已完成
    private String transStatus; // 交易状态:0.初始化,1.成功,2.失败
    private String transType; // 交易类型：（1刷卡支付 2微信支付 3支付宝 4云闪付 5取现 6银联二维码 7快捷支付 8测试交易）
    private Long orgId;     //组织id
    private String v2MerchantCode;     //V2商户编号
    private String v2MerchantName;     //V2商户名称
    private String v2MerchantPhone;     //V2商户手机号
    private String snNo;         //sn号
    private String productType;      //硬件产品种类
    private String productTypeName; //硬件产品种类名称
    private BigDecimal tradeAmount;  //交易金额
    private Date tradeDate;  //交易时间
    private Integer isActiveTrade;  //是否激活交易 1-是 0-否
    private Integer settleCycle;     //结算周期 1-T0 2-T1
    private Integer receiveType;     //收款类型 1-标准类 2-vip类
    private BigDecimal platformRate;     //平台费率
    private BigDecimal merchantRate;     //商户费率
    private BigDecimal merchantPaymentFee;  //商户出款手续费
    private BigDecimal platformPaymentFee;  //平台出款成本费
    private BigDecimal merchantTradeFee;    //商户交易手续费
    private BigDecimal totalBonusConf;  //交易总奖金包配置
    private BigDecimal totalBonus;   //总发放奖金
    private BigDecimal plateProfit;  //平台总分润
    private BigDecimal plateTradeProfit;    //平台交易分润
    private BigDecimal platePaymentProfit;  //平台出款分润
    private BigDecimal orgProfit;    //组织总分润
    private BigDecimal orgTradeProfitConf;     //组织交易分润配置
    private BigDecimal orgTradeProfit;  //组织交易分润
    private BigDecimal orgPaymentProfit;    //组织出款分润
    private String oneUserCode;     //1级收益用户编号
    private String oneUserType;     //1级身份
    private BigDecimal oneUserProfit;   //1级分润
    private String twoUserCode;     //2级收益用户编号
    private String twoUserType;     //2级身份
    private BigDecimal twoUserProfit;   //2级分润
    private String thrUserCode;     //3级收益用户编号
    private String thrUserType;     //3级身份
    private BigDecimal thrUserProfit;   //3级分润
    private String fouUserCode;     //4级收益用户编号
    private String fouUserType;  //4级身份
    private BigDecimal fouUserProfit;   //4级分润
    private String profitStatus;     //计算分润状态 0为计算失败 1为计算成功
    private String accountStatus;    //记账状态;0待入账；1已记账；2记账失败
    private String provinceName;     //下单人省份
    private String cityName;     //下单人城市
    private String districtName;     //下单人区县
    private String remark;        //备注
    private String createBy;     //添加人
    private Date createDate;     //添加时间
    private String updateBy;     //修改人
    private Date updateDate;     //修改时间
    private Date completeDate; // 完成时间
    private BigDecimal actualPaymentFee; //实际出款手续费
    private String settleStatus; //结算状态


    private String tradeDateStart; //交易开始时间
    private String tradeDateEnd; //交易结束时间
    private String userName; //所属采购者姓名
    private String phone; //所属采购者手机号
    private String orgName; //组织名称
    private String oneUserName; // 一级名称
    private String twoUserName; // 二级名称
    private String thrUserName; // 三级名称
    private String fouUserName; // 四级名称
    private BigDecimal basicBonusAmount;//领地基准分红

    /**统计信息汇总 开始*/
    private BigDecimal orderCount; // 交易总笔数
    private BigDecimal tradeFeeSum; // 交易总手续费
    private BigDecimal paymentFeeSum; // 出款总手续费
    private BigDecimal plateProfitSum; // 平台实际分润汇总
    private BigDecimal plateTradeProfitSum; // 交易-平台实际分润
    private BigDecimal platePaymentProfitSum; // 出款-平台实际分润
    private BigDecimal tradeAmountSum; // 交易总金额
    /**统计信息汇总 结束*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getV2OrderNo() {
        return v2OrderNo;
    }

    public void setV2OrderNo(String v2OrderNo) {
        this.v2OrderNo = v2OrderNo;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getV2MerchantCode() {
        return v2MerchantCode;
    }

    public void setV2MerchantCode(String v2MerchantCode) {
        this.v2MerchantCode = v2MerchantCode;
    }

    public String getV2MerchantName() {
        return v2MerchantName;
    }

    public void setV2MerchantName(String v2MerchantName) {
        this.v2MerchantName = v2MerchantName;
    }

    public String getV2MerchantPhone() {
        return v2MerchantPhone;
    }

    public void setV2MerchantPhone(String v2MerchantPhone) {
        this.v2MerchantPhone = v2MerchantPhone;
    }

    public String getSnNo() {
        return snNo;
    }

    public void setSnNo(String snNo) {
        this.snNo = snNo;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(BigDecimal tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public Date getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(Date tradeDate) {
        this.tradeDate = tradeDate;
    }

    public Integer getIsActiveTrade() {
        return isActiveTrade;
    }

    public void setIsActiveTrade(Integer isActiveTrade) {
        this.isActiveTrade = isActiveTrade;
    }

    public Integer getSettleCycle() {
        return settleCycle;
    }

    public void setSettleCycle(Integer settleCycle) {
        this.settleCycle = settleCycle;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
    }

    public BigDecimal getPlatformRate() {
        return platformRate;
    }

    public void setPlatformRate(BigDecimal platformRate) {
        this.platformRate = platformRate;
    }

    public BigDecimal getMerchantRate() {
        return merchantRate;
    }

    public void setMerchantRate(BigDecimal merchantRate) {
        this.merchantRate = merchantRate;
    }

    public BigDecimal getMerchantPaymentFee() {
        return merchantPaymentFee;
    }

    public void setMerchantPaymentFee(BigDecimal merchantPaymentFee) {
        this.merchantPaymentFee = merchantPaymentFee;
    }

    public BigDecimal getPlatformPaymentFee() {
        return platformPaymentFee;
    }

    public void setPlatformPaymentFee(BigDecimal platformPaymentFee) {
        this.platformPaymentFee = platformPaymentFee;
    }

    public BigDecimal getMerchantTradeFee() {
        return merchantTradeFee;
    }

    public void setMerchantTradeFee(BigDecimal merchantTradeFee) {
        this.merchantTradeFee = merchantTradeFee;
    }

    public BigDecimal getTotalBonusConf() {
        return totalBonusConf;
    }

    public void setTotalBonusConf(BigDecimal totalBonusConf) {
        this.totalBonusConf = totalBonusConf;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public BigDecimal getPlateProfit() {
        return plateProfit;
    }

    public void setPlateProfit(BigDecimal plateProfit) {
        this.plateProfit = plateProfit;
    }

    public BigDecimal getPlateTradeProfit() {
        return plateTradeProfit;
    }

    public void setPlateTradeProfit(BigDecimal plateTradeProfit) {
        this.plateTradeProfit = plateTradeProfit;
    }

    public BigDecimal getPlatePaymentProfit() {
        return platePaymentProfit;
    }

    public void setPlatePaymentProfit(BigDecimal platePaymentProfit) {
        this.platePaymentProfit = platePaymentProfit;
    }

    public BigDecimal getOrgProfit() {
        return orgProfit;
    }

    public void setOrgProfit(BigDecimal orgProfit) {
        this.orgProfit = orgProfit;
    }

    public BigDecimal getOrgTradeProfitConf() {
        return orgTradeProfitConf;
    }

    public void setOrgTradeProfitConf(BigDecimal orgTradeProfitConf) {
        this.orgTradeProfitConf = orgTradeProfitConf;
    }

    public BigDecimal getOrgTradeProfit() {
        return orgTradeProfit;
    }

    public void setOrgTradeProfit(BigDecimal orgTradeProfit) {
        this.orgTradeProfit = orgTradeProfit;
    }

    public BigDecimal getOrgPaymentProfit() {
        return orgPaymentProfit;
    }

    public void setOrgPaymentProfit(BigDecimal orgPaymentProfit) {
        this.orgPaymentProfit = orgPaymentProfit;
    }

    public String getOneUserCode() {
        return oneUserCode;
    }

    public void setOneUserCode(String oneUserCode) {
        this.oneUserCode = oneUserCode;
    }

    public String getOneUserType() {
        return oneUserType;
    }

    public void setOneUserType(String oneUserType) {
        this.oneUserType = oneUserType;
    }

    public BigDecimal getOneUserProfit() {
        return oneUserProfit;
    }

    public void setOneUserProfit(BigDecimal oneUserProfit) {
        this.oneUserProfit = oneUserProfit;
    }

    public String getTwoUserCode() {
        return twoUserCode;
    }

    public void setTwoUserCode(String twoUserCode) {
        this.twoUserCode = twoUserCode;
    }

    public String getTwoUserType() {
        return twoUserType;
    }

    public void setTwoUserType(String twoUserType) {
        this.twoUserType = twoUserType;
    }

    public BigDecimal getTwoUserProfit() {
        return twoUserProfit;
    }

    public void setTwoUserProfit(BigDecimal twoUserProfit) {
        this.twoUserProfit = twoUserProfit;
    }

    public String getThrUserCode() {
        return thrUserCode;
    }

    public void setThrUserCode(String thrUserCode) {
        this.thrUserCode = thrUserCode;
    }

    public String getThrUserType() {
        return thrUserType;
    }

    public void setThrUserType(String thrUserType) {
        this.thrUserType = thrUserType;
    }

    public BigDecimal getThrUserProfit() {
        return thrUserProfit;
    }

    public void setThrUserProfit(BigDecimal thrUserProfit) {
        this.thrUserProfit = thrUserProfit;
    }

    public String getFouUserCode() {
        return fouUserCode;
    }

    public void setFouUserCode(String fouUserCode) {
        this.fouUserCode = fouUserCode;
    }

    public String getFouUserType() {
        return fouUserType;
    }

    public void setFouUserType(String fouUserType) {
        this.fouUserType = fouUserType;
    }

    public BigDecimal getFouUserProfit() {
        return fouUserProfit;
    }

    public void setFouUserProfit(BigDecimal fouUserProfit) {
        this.fouUserProfit = fouUserProfit;
    }

    public String getProfitStatus() {
        return profitStatus;
    }

    public void setProfitStatus(String profitStatus) {
        this.profitStatus = profitStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getTradeDateStart() {
        return tradeDateStart;
    }

    public void setTradeDateStart(String tradeDateStart) {
        this.tradeDateStart = tradeDateStart;
    }

    public String getTradeDateEnd() {
        return tradeDateEnd;
    }

    public void setTradeDateEnd(String tradeDateEnd) {
        this.tradeDateEnd = tradeDateEnd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOneUserName() {
        return oneUserName;
    }

    public void setOneUserName(String oneUserName) {
        this.oneUserName = oneUserName;
    }

    public String getTwoUserName() {
        return twoUserName;
    }

    public void setTwoUserName(String twoUserName) {
        this.twoUserName = twoUserName;
    }

    public String getThrUserName() {
        return thrUserName;
    }

    public void setThrUserName(String thrUserName) {
        this.thrUserName = thrUserName;
    }

    public String getFouUserName() {
        return fouUserName;
    }

    public void setFouUserName(String fouUserName) {
        this.fouUserName = fouUserName;
    }

    public BigDecimal getBasicBonusAmount() {
        return basicBonusAmount;
    }

    public void setBasicBonusAmount(BigDecimal basicBonusAmount) {
        this.basicBonusAmount = basicBonusAmount;
    }

    public BigDecimal getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(BigDecimal orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getTradeFeeSum() {
        return tradeFeeSum;
    }

    public void setTradeFeeSum(BigDecimal tradeFeeSum) {
        this.tradeFeeSum = tradeFeeSum;
    }

    public BigDecimal getPaymentFeeSum() {
        return paymentFeeSum;
    }

    public void setPaymentFeeSum(BigDecimal paymentFeeSum) {
        this.paymentFeeSum = paymentFeeSum;
    }

    public BigDecimal getPlateProfitSum() {
        return plateProfitSum;
    }

    public void setPlateProfitSum(BigDecimal plateProfitSum) {
        this.plateProfitSum = plateProfitSum;
    }

    public BigDecimal getPlateTradeProfitSum() {
        return plateTradeProfitSum;
    }

    public void setPlateTradeProfitSum(BigDecimal plateTradeProfitSum) {
        this.plateTradeProfitSum = plateTradeProfitSum;
    }

    public BigDecimal getPlatePaymentProfitSum() {
        return platePaymentProfitSum;
    }

    public void setPlatePaymentProfitSum(BigDecimal platePaymentProfitSum) {
        this.platePaymentProfitSum = platePaymentProfitSum;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public BigDecimal getTradeAmountSum() {
        return tradeAmountSum;
    }

    public void setTradeAmountSum(BigDecimal tradeAmountSum) {
        this.tradeAmountSum = tradeAmountSum;
    }

    public BigDecimal getActualPaymentFee() {
        return actualPaymentFee;
    }

    public void setActualPaymentFee(BigDecimal actualPaymentFee) {
        this.actualPaymentFee = actualPaymentFee;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
