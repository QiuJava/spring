package cn.eeepay.framework.model.nposp;

import java.io.Serializable;
import java.util.Date;

public class BusinessProductDefine implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long bpId; // '业务产品ID',
	private String bpName;   //'名称',
	private String saleStarttime;//'可销售起始日期',
	private Date saleEndtime;//'可销售截止日期',
	private String proxy;    //'可否代理:1-可，0-否',
	private String bpType;    //'类型:1-个人，2-个体商户，3-企业商户',
	private String  isOem;    //'是否OEM:1-是，0-否',
	private String teamId;   //'TEAMINFO ID',
	private String ownBpId;   //'关联自营业务产品ID',
	private String  twoCode;    //'二维码',
  	private String  remark;    //'说明',
  	private String  bpImg;    //'宣传图片',
  	private String notCheck;    //'证件资料完整时无需人工审核',
  	private String  link;
  	private Integer relyHardware;    //'是否依赖硬件，1：是，0：否',
  	private String  linkProduct;   //'自动开通关联业务产品',
  	private String  allowWebItem;    //'是否允许web进件，0：否，1：是',
  	private String  allowIndividualApply;    //'允许单独申请，1：是，0：否',
	public Long getBpId() {
		return bpId;
	}
	public void setBpId(Long bpId) {
		this.bpId = bpId;
	}
	public String getBpName() {
		return bpName;
	}
	public void setBpName(String bpName) {
		this.bpName = bpName;
	}
	public String getSaleStarttime() {
		return saleStarttime;
	}
	public void setSaleStarttime(String saleStarttime) {
		this.saleStarttime = saleStarttime;
	}
	public Date getSaleEndtime() {
		return saleEndtime;
	}
	public void setSaleEndtime(Date saleEndtime) {
		this.saleEndtime = saleEndtime;
	}
	public String getProxy() {
		return proxy;
	}
	public void setProxy(String proxy) {
		this.proxy = proxy;
	}
	public String getBpType() {
		return bpType;
	}
	public void setBpType(String bpType) {
		this.bpType = bpType;
	}
	public String getIsOem() {
		return isOem;
	}
	public void setIsOem(String isOem) {
		this.isOem = isOem;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public String getOwnBpId() {
		return ownBpId;
	}
	public void setOwnBpId(String ownBpId) {
		this.ownBpId = ownBpId;
	}
	public String getTwoCode() {
		return twoCode;
	}
	public void setTwoCode(String twoCode) {
		this.twoCode = twoCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBpImg() {
		return bpImg;
	}
	public void setBpImg(String bpImg) {
		this.bpImg = bpImg;
	}
	public String getNotCheck() {
		return notCheck;
	}
	public void setNotCheck(String notCheck) {
		this.notCheck = notCheck;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public Integer getRelyHardware() {
		return relyHardware;
	}
	public void setRelyHardware(Integer relyHardware) {
		this.relyHardware = relyHardware;
	}
	public String getLinkProduct() {
		return linkProduct;
	}
	public void setLinkProduct(String linkProduct) {
		this.linkProduct = linkProduct;
	}
	public String getAllowWebItem() {
		return allowWebItem;
	}
	public void setAllowWebItem(String allowWebItem) {
		this.allowWebItem = allowWebItem;
	}
	public String getAllowIndividualApply() {
		return allowIndividualApply;
	}
	public void setAllowIndividualApply(String allowIndividualApply) {
		this.allowIndividualApply = allowIndividualApply;
	}
  	
  	
}
