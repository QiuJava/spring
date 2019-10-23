package cn.eeepay.framework.model;

import java.util.Date;

/**
 * Mpos 机具
 */
public class MposMachines {

    private Long id;
    private String snNo;                //Sn号
    private String productType;        //硬件产品种类Id
    private String productTypeName;     //硬件产品种类名称
    private Long orgId;               //所属组织
    private Integer status;             //机具状态 1-已入库 2-已分配 3-已发货 4-已启用 5-已激活
    private String v2MerchantCode;      //V2商户编号
    private String v2MerchantName;      //V2商户名称
    private String v2MerchantPhone;     //V2商户手机号
    private String purchaserUserCode;   //采购者编号
    private String purchaserUserPhone;  //采购者手机号
    private Date shipDate;              //发货时间
    private Date enabledDate;           //启动时间
    private Date activeDate;            //激活时间
    private Long orderId;               //关联订单ID
    private String createBy;            //添加人
    private Date createDate;            //添加时间
    private String updateBy;            //修改人
    private Date updateDate;            //修改时间

    private String orgName;             //所属组织
    private String snStart;
    private String snEnd;
    private String enabledDateStart;
    private String enabledDateEnd;

    private String orderNo; //订单编号


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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



    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getPurchaserUserCode() {
        return purchaserUserCode;
    }

    public void setPurchaserUserCode(String purchaserUserCode) {
        this.purchaserUserCode = purchaserUserCode;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
    }

    public Date getEnabledDate() {
        return enabledDate;
    }

    public void setEnabledDate(Date enabledDate) {
        this.enabledDate = enabledDate;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public String getSnStart() {
        return snStart;
    }

    public void setSnStart(String snStart) {
        this.snStart = snStart;
    }

    public String getSnEnd() {
        return snEnd;
    }

    public void setSnEnd(String snEnd) {
        this.snEnd = snEnd;
    }

    public String getEnabledDateStart() {
        return enabledDateStart;
    }

    public void setEnabledDateStart(String enabledDateStart) {
        this.enabledDateStart = enabledDateStart;
    }

    public String getEnabledDateEnd() {
        return enabledDateEnd;
    }

    public void setEnabledDateEnd(String enabledDateEnd) {
        this.enabledDateEnd = enabledDateEnd;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getPurchaserUserPhone() {
        return purchaserUserPhone;
    }

    public void setPurchaserUserPhone(String purchaserUserPhone) {
        this.purchaserUserPhone = purchaserUserPhone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
