package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * mpos进件激活
 */
public class MposActiveOrder {

    private Long id;
    private String orderNo;         //订单编号
    private String userCode;        //机具所属用户编码
    private String status;          //活动状态:1未激活 2已激活 3已返补贴
    private Long orgId;             //组织id
    private String v2MerchantCode;     //V2商户编号
    private String v2MerchantName;     //V2商户名称
    private String v2MerchantPhone;     //V2商户手机号
    private String snNo;            //sn号
    private String productType;     //硬件产品种类
    private Date registerDate;     //进件时间
    private Date activeDate;     //激活时间
    private Integer activeNum;     //激活次数
    private BigDecimal activeReturnBonus;     //激活返现金额
    private Date activeReturnDate; // 激活返现发放时间
    private BigDecimal totalBonus;     //总发放奖金(激活奖金包)
    private BigDecimal plateProfit;     //平台分润
    private BigDecimal orgProfit;     //组织分润
    private String oneUserCode;     //1级收益用户编号
    private String oneUserType;    //1级身份
    private BigDecimal oneUserProfit;     //1级分润
    private String twoUserCode;     //2级收益用户编号
    private String twoUserType;     //2级身份
    private BigDecimal twoUserProfit;     //2级分润
    private String thrUserCode;     //3级收益用户编号
    private String thrUserType;     //3级身份
    private BigDecimal thrUserProfit;     //3级分润
    private String fouUserCode;     //4级收益用户编号
    private String fouUserType;     //4级身份
    private BigDecimal fouUserProfit;     //4级分润
    private String profitStatus;     //计算分润状态 0为计算失败 1为计算成功
    private String accountStatus;     //记账状态;0待入账；1已记账；2记账失败
    private String provinceName;     //下单人省份
    private String cityName;     //下单人城市
    private String districtName;     //下单人区县
    private String remark;     //备注
    private Date completeDate; // 完成时间
    private String createBy;     //添加人
    private Date createDate;     //添加时间
    private String updateBy;     //修改人
    private Date updateDate;     //修改时间

    private String registerDateStart; //进件开始时间
    private String registerDateEnd; //进件结束时间
    private String userName; //所属采购者姓名
    private String phone; //所属采购者手机号
    private String orgName; //组织名称
    private String oneUserName; // 一级名称
    private String twoUserName; // 二级名称
    private String thrUserName; // 三级名称
    private String fouUserName; // 四级名称
    private String typeName; // 设备类型名称

    /**统计信息汇总 开始*/
    private BigDecimal v2MerchantSum; // 进件商户总数
    private BigDecimal activeSum; // 已激活
    private BigDecimal inActiveSum; // 未激活
    private BigDecimal activeReturnBonusSum; // 激活返现金额
    private BigDecimal totalBonusSum; // 激活奖励总金额
    private BigDecimal totalSubsidy; // 激活总补贴
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public Integer getActiveNum() {
        return activeNum;
    }

    public void setActiveNum(Integer activeNum) {
        this.activeNum = activeNum;
    }

    public BigDecimal getActiveReturnBonus() {
        return activeReturnBonus;
    }

    public void setActiveReturnBonus(BigDecimal activeReturnBonus) {
        this.activeReturnBonus = activeReturnBonus;
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

    public BigDecimal getOrgProfit() {
        return orgProfit;
    }

    public void setOrgProfit(BigDecimal orgProfit) {
        this.orgProfit = orgProfit;
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

    public String getRegisterDateStart() {
        return registerDateStart;
    }

    public void setRegisterDateStart(String registerDateStart) {
        this.registerDateStart = registerDateStart;
    }

    public String getRegisterDateEnd() {
        return registerDateEnd;
    }

    public void setRegisterDateEnd(String registerDateEnd) {
        this.registerDateEnd = registerDateEnd;
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

    public BigDecimal getV2MerchantSum() {
        return v2MerchantSum;
    }

    public void setV2MerchantSum(BigDecimal v2MerchantSum) {
        this.v2MerchantSum = v2MerchantSum;
    }

    public BigDecimal getActiveSum() {
        return activeSum;
    }

    public void setActiveSum(BigDecimal activeSum) {
        this.activeSum = activeSum;
    }

    public BigDecimal getInActiveSum() {
        return inActiveSum;
    }

    public void setInActiveSum(BigDecimal inActiveSum) {
        this.inActiveSum = inActiveSum;
    }

    public BigDecimal getActiveReturnBonusSum() {
        return activeReturnBonusSum;
    }

    public void setActiveReturnBonusSum(BigDecimal activeReturnBonusSum) {
        this.activeReturnBonusSum = activeReturnBonusSum;
    }

    public BigDecimal getTotalBonusSum() {
        return totalBonusSum;
    }

    public void setTotalBonusSum(BigDecimal totalBonusSum) {
        this.totalBonusSum = totalBonusSum;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public BigDecimal getTotalSubsidy() {
        return totalSubsidy;
    }

    public void setTotalSubsidy(BigDecimal totalSubsidy) {
        this.totalSubsidy = totalSubsidy;
    }

    public Date getActiveReturnDate() {
        return activeReturnDate;
    }

    public void setActiveReturnDate(Date activeReturnDate) {
        this.activeReturnDate = activeReturnDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
