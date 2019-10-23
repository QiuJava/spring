package cn.eeepay.framework.model;


import java.math.BigDecimal;
import java.util.Date;

/**
 * mpos商户维度交易数据汇总
 */
public class MposMerchantTradeCount {

    private Long id;
    private String v2MerchantCode;      //V2商户编号
    private String v2MerchantName;      //V2商户名称
    private String v2MerchantPhone;     //V2商户手机号
    private String snNo;                 //sn号
    private String productType;          //硬件产品种类
    private Date registerDate;           //进件时间
    private String userCode;             //机具所属用户编码
    private Long orgId;                  //组织id
    private BigDecimal totalTradeAmount; //累计交易金额
    private BigDecimal monthTradeAmount; //本月交易金额
    private BigDecimal near30TradeAmount; //近30天交易金额
    private Date createDate;                 //创建时间

    private String registerDateStart; //进件开始时间
    private String registerDateEnd; //进件结束时间
    private String userName; //所属采购者姓名
    private String phone; //所属采购者手机号
    private String orgName; //组织名称
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public BigDecimal getTotalTradeAmount() {
        return totalTradeAmount;
    }

    public void setTotalTradeAmount(BigDecimal totalTradeAmount) {
        this.totalTradeAmount = totalTradeAmount;
    }

    public BigDecimal getMonthTradeAmount() {
        return monthTradeAmount;
    }

    public void setMonthTradeAmount(BigDecimal monthTradeAmount) {
        this.monthTradeAmount = monthTradeAmount;
    }

    public BigDecimal getNear30TradeAmount() {
        return near30TradeAmount;
    }

    public void setNear30TradeAmount(BigDecimal near30TradeAmount) {
        this.near30TradeAmount = near30TradeAmount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

}
