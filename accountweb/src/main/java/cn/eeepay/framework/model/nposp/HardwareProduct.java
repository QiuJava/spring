package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.util.Date;

public class HardwareProduct implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long hpId;//'硬件产品ID',
	private String typeName; //'种类名称',
	private String model; //'型号',
	private String  versionNu; //'版本号',
	private Date saleStarttime; //'可销售起始日期',
	private Date  saleEndtime; //'可销售终止日期',
	private Date prodStarttime; //'可生产起始日期',
	private Date prodEndtime; //'可生产终止日期',
	private Date useStarttime; //'可使用起始日期',
	private Date useEndtime; //'可使用终止日期',
	private Date repaStarttime; //'可维修起始日期',
	private Date repaEndtime; //'可维修终止日期',
	private String oemMark; //'OEM标识',
	private String oemId; //'OEM ID',
	private String facturerCode; //'生产产商英文标识',
	private String manufacturer; //'生产产商',
	private String posType; //'设备类型',
	private String imageUrl; //'示例图片',
	public Long getHpId() {
		return hpId;
	}
	public void setHpId(Long hpId) {
		this.hpId = hpId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getVersionNu() {
		return versionNu;
	}
	public void setVersionNu(String versionNu) {
		this.versionNu = versionNu;
	}
	public Date getSaleStarttime() {
		return saleStarttime;
	}
	public void setSaleStarttime(Date saleStarttime) {
		this.saleStarttime = saleStarttime;
	}
	public Date getSaleEndtime() {
		return saleEndtime;
	}
	public void setSaleEndtime(Date saleEndtime) {
		this.saleEndtime = saleEndtime;
	}
	public Date getProdStarttime() {
		return prodStarttime;
	}
	public void setProdStarttime(Date prodStarttime) {
		this.prodStarttime = prodStarttime;
	}
	public Date getProdEndtime() {
		return prodEndtime;
	}
	public void setProdEndtime(Date prodEndtime) {
		this.prodEndtime = prodEndtime;
	}
	public Date getUseStarttime() {
		return useStarttime;
	}
	public void setUseStarttime(Date useStarttime) {
		this.useStarttime = useStarttime;
	}
	public Date getUseEndtime() {
		return useEndtime;
	}
	public void setUseEndtime(Date useEndtime) {
		this.useEndtime = useEndtime;
	}
	public Date getRepaStarttime() {
		return repaStarttime;
	}
	public void setRepaStarttime(Date repaStarttime) {
		this.repaStarttime = repaStarttime;
	}
	public Date getRepaEndtime() {
		return repaEndtime;
	}
	public void setRepaEndtime(Date repaEndtime) {
		this.repaEndtime = repaEndtime;
	}
	public String getOemMark() {
		return oemMark;
	}
	public void setOemMark(String oemMark) {
		this.oemMark = oemMark;
	}
	public String getOemId() {
		return oemId;
	}
	public void setOemId(String oemId) {
		this.oemId = oemId;
	}
	public String getFacturerCode() {
		return facturerCode;
	}
	public void setFacturerCode(String facturerCode) {
		this.facturerCode = facturerCode;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getPosType() {
		return posType;
	}
	public void setPosType(String posType) {
		this.posType = posType;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	
}
