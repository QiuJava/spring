package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Mpos订单
 */
public class MposOrder {


    private Long id;
    private String orderNo;        //订单编号
    private String userCode;       //下单人用户编码
    private String userName;        //下单人名称
    private String userPhone;       //下单人手机号
    private String userType;        //下单人身份 身份类型;10:普通用户； 20专员；30经理；40银行家
    private String userRemark;      //下单人备注
    private String payChannelName;          //付款通道名称
    private String status;          //订单状态:1待付款 2待发货 3待收获 4已收货 9已关闭
    private String payStatus;      //支付状态:1未支付 2已支付
    private Long orgId;            //组织id
    private Long goodId;           //商品id
    private String goodTitle;       //商品标题
    private Integer buyNum;        //购买数量
    private BigDecimal goodSinglePrice;    //商品单价
    private BigDecimal goodTotalPrice;    //商品总价
    private Integer shipWay;               //配送方式 1-快递配送 2-线下自提
    private Integer needShipFee;          //是否包邮 1-是 2-否
    private BigDecimal shipFee;            //运费
    private BigDecimal totalPrice;         //订单总金额
    private Integer shipper;                //发货方 1-平台发货 2-组织发货
    private String receiverName;           //收货人
    private String receiverPhone;          //收货手机号
    private String receiverAddr;           //收货人地址
    private String payMethod;              //支付方式：1 微信，2 支付宝，3 快捷，4红包账户，5分润账户
    private Date payDate;                  //支付时间
    private String payChannel;             //付款通道：V2,WEIXIN
    private String payOrderNo;            //关联支付流水号
    private String payChannelNo;          //收款通道商户号
    private Date completeDate;             //订单完成时间
    private BigDecimal toOrgAmount;       //转组织货款金额
    private Integer toOrgStatus;          //货款转账状态 1-未转账 2-已转账
    private Date toOrgDate;               //货款转账时间
    private Integer receiveType;           //收货人类型 1-用户确认收货 2-系统自动收货
    private Date receiveDate;              //收货时间
    private String shipExpress;            //快递公司
    private String shipExpressNo;         //快递单号
    private Date shipDate;                 //发货时间
    private BigDecimal totalBonus;         //总发放奖金(采购机具奖金包)
    private BigDecimal plateProfit;        //平台分润
    private BigDecimal orgProfit;          //组织分润
    private String oneUserCode;           //1级收益用户编号
    private String oneUserName;            //1级名称
    private String oneUserType;           //1级身份
    private BigDecimal oneUserProfit;     //1级分润
    private String twoUserCode;           //2级收益用户编号
    private String twoUserName;             //2级名称
    private String twoUserType;           //2级身份
    private BigDecimal twoUserProfit;     //2级分润
    private String thrUserCode;           //3级收益用户编号
    private String thrUserName;             //3级名称
    private String thrUserType;           //3级身份
    private BigDecimal thrUserProfit;     //3级分润
    private String fouUserCode;           //4级收益用户编号
    private String fouUserName;             //4级名称
    private String fouUserType;           //4级身份
    private BigDecimal fouUserProfit;     //4级分润
    private String profitStatus;           //计算分润状态 0为计算失败 1为计算成功
    private String accountStatus;          //记账状态;0待入账；1已记账；2记账失败
    private String application;             //应用 1-公众号，2-APP
    private String secondUserNode;        //二级代理节点
    private String provinceName;           //下单人省份
    private String cityName;               //下单人城市
    private String districtName;           //下单人区县
    private String remark;                  //备注
    private String createBy;               //添加人
    private Date createDate;               //添加时间
    private String updateBy;               //修改人
    private Date updateDate;               //修改时间

    private String goodNo;      //商品编号

    private String goodStatus;  //商品状态

    private String imgUrl;      //商品图片

    private String typeName;    //商品分类名称
    private String productType; //设备类型

    private String orgName;     //组织名称

    private String shipOrg; //发货组织
    private String goodTypeId;  //商品类型ID


    private String createDateStart; //创建时间
    private String createDateEnd;

    private String shipDateStart;   //发货时间
    private String shipDateEnd;

    private String payDateStart;    //支付时间
    private String payDateEnd;

    private String receiveDateStart;    //收货时间
    private String receiveDateEnd;

    private String machinesSnNo;  //发货机具sn号

    private List<MposMachines> mposMachines;

    private String consignRemark;   //委托备注

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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getGoodId() {
        return goodId;
    }

    public void setGoodId(Long goodId) {
        this.goodId = goodId;
    }

    public Integer getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(Integer buyNum) {
        this.buyNum = buyNum;
    }

    public BigDecimal getGoodTotalPrice() {
        return goodTotalPrice;
    }

    public void setGoodTotalPrice(BigDecimal goodTotalPrice) {
        this.goodTotalPrice = goodTotalPrice;
    }

    public Integer getShipWay() {
        return shipWay;
    }

    public void setShipWay(Integer shipWay) {
        this.shipWay = shipWay;
    }

    public Integer getNeedShipFee() {
        return needShipFee;
    }

    public void setNeedShipFee(Integer needShipFee) {
        this.needShipFee = needShipFee;
    }

    public BigDecimal getShipFee() {
        return shipFee;
    }

    public void setShipFee(BigDecimal shipFee) {
        this.shipFee = shipFee;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getShipper() {
        return shipper;
    }

    public void setShipper(Integer shipper) {
        this.shipper = shipper;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayChannelNo() {
        return payChannelNo;
    }

    public void setPayChannelNo(String payChannelNo) {
        this.payChannelNo = payChannelNo;
    }

    public Date getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(Date completeDate) {
        this.completeDate = completeDate;
    }

    public BigDecimal getToOrgAmount() {
        return toOrgAmount;
    }

    public void setToOrgAmount(BigDecimal toOrgAmount) {
        this.toOrgAmount = toOrgAmount;
    }

    public Integer getToOrgStatus() {
        return toOrgStatus;
    }

    public void setToOrgStatus(Integer toOrgStatus) {
        this.toOrgStatus = toOrgStatus;
    }

    public Date getToOrgDate() {
        return toOrgDate;
    }

    public void setToOrgDate(Date toOrgDate) {
        this.toOrgDate = toOrgDate;
    }

    public Integer getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(Integer receiveType) {
        this.receiveType = receiveType;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }

    public String getShipExpress() {
        return shipExpress;
    }

    public void setShipExpress(String shipExpress) {
        this.shipExpress = shipExpress;
    }

    public String getShipExpressNo() {
        return shipExpressNo;
    }

    public void setShipExpressNo(String shipExpressNo) {
        this.shipExpressNo = shipExpressNo;
    }

    public Date getShipDate() {
        return shipDate;
    }

    public void setShipDate(Date shipDate) {
        this.shipDate = shipDate;
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

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSecondUserNode() {
        return secondUserNode;
    }

    public void setSecondUserNode(String secondUserNode) {
        this.secondUserNode = secondUserNode;
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

    public String getGoodNo() {
        return goodNo;
    }

    public void setGoodNo(String goodNo) {
        this.goodNo = goodNo;
    }

    public String getGoodTitle() {
        return goodTitle;
    }

    public void setGoodTitle(String goodTitle) {
        this.goodTitle = goodTitle;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getGoodStatus() {
        return goodStatus;
    }

    public void setGoodStatus(String goodStatus) {
        this.goodStatus = goodStatus;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
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

    public String getShipDateStart() {
        return shipDateStart;
    }

    public void setShipDateStart(String shipDateStart) {
        this.shipDateStart = shipDateStart;
    }

    public String getShipDateEnd() {
        return shipDateEnd;
    }

    public void setShipDateEnd(String shipDateEnd) {
        this.shipDateEnd = shipDateEnd;
    }

    public String getPayDateStart() {
        return payDateStart;
    }

    public void setPayDateStart(String payDateStart) {
        this.payDateStart = payDateStart;
    }

    public String getPayDateEnd() {
        return payDateEnd;
    }

    public void setPayDateEnd(String payDateEnd) {
        this.payDateEnd = payDateEnd;
    }

    public String getReceiveDateStart() {
        return receiveDateStart;
    }

    public void setReceiveDateStart(String receiveDateStart) {
        this.receiveDateStart = receiveDateStart;
    }

    public String getReceiveDateEnd() {
        return receiveDateEnd;
    }

    public void setReceiveDateEnd(String receiveDateEnd) {
        this.receiveDateEnd = receiveDateEnd;
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

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public BigDecimal getGoodSinglePrice() {
        return goodSinglePrice;
    }

    public void setGoodSinglePrice(BigDecimal goodSinglePrice) {
        this.goodSinglePrice = goodSinglePrice;
    }

    public String getMachinesSnNo() {
        return machinesSnNo;
    }

    public void setMachinesSnNo(String machinesSnNo) {
        this.machinesSnNo = machinesSnNo;
    }

    public List<MposMachines> getMposMachines() {
        return mposMachines;
    }

    public void setMposMachines(List<MposMachines> mposMachines) {
        this.mposMachines = mposMachines;
    }

    public String getShipOrg() {
        return shipOrg;
    }

    public void setShipOrg(String shipOrg) {
        this.shipOrg = shipOrg;
    }

    public String getConsignRemark() {
        return consignRemark;
    }

    public void setConsignRemark(String consignRemark) {
        this.consignRemark = consignRemark;
    }

    public String getUserRemark() {
        return userRemark;
    }

    public void setUserRemark(String userRemark) {
        this.userRemark = userRemark;
    }

    public String getPayChannelName() {
        return payChannelName;
    }

    public void setPayChannelName(String payChannelName) {
        this.payChannelName = payChannelName;
    }

    public String getGoodTypeId() {
        return goodTypeId;
    }

    public void setGoodTypeId(String goodTypeId) {
        this.goodTypeId = goodTypeId;
    }
}
