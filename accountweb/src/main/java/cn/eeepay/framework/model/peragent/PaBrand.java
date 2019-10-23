package cn.eeepay.framework.model.peragent;

import java.io.Serializable;
import java.util.Date;

public class PaBrand implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String brandCode;// 品牌编码
	private String brandName;// 品牌名称
	private String cost;// 商户提现成本
	private Date createTime; // 创建时间

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
