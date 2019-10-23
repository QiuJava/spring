package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OutBill implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private Integer outAccountTaskId ;
	private BigDecimal outAccountTaskAmount ;
	private BigDecimal calcOutAmount ;
	private Integer balanceUpCount ;
	private Integer balanceMerchantCount ;
	private String outBillStatus;
	private Date sysTime ;
	private Date createTime ;//创建时间
	private String creator ;//创建人
	private String updator ;//修改人
	private Date updateTime ;//更新时间
	private String backOperator ;//回盘人
	private Date backTime ;//回盘时间
	private String accountOwner;
	private String acqEnname;
	private String fileName;
	///VO字段
	private Integer hasService;
	private Integer tranImport;
	private String dayPhase;
	//实际出账金额
	private BigDecimal outAmount;
	//按交易出账需添加字段
	private Integer outAccountBillMethod;//出账方式
	private String outBillRange;//出账范围

	public String getOutBillRange() {return outBillRange;}
	public void setOutBillRange(String outBillRange) {this.outBillRange = outBillRange;}
	public Integer getOutAccountBillMethod() {
		return outAccountBillMethod;
	}
	public void setOutAccountBillMethod(Integer outAccountBillMethod) {
		this.outAccountBillMethod = outAccountBillMethod;
	}
	public Integer getHasService() {
		return hasService;
	}
	public void setHasService(Integer hasService) {
		this.hasService = hasService;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getOutBillStatus() {
		return outBillStatus;
	}
	public void setOutBillStatus(String outBillStatus) {
		this.outBillStatus = outBillStatus;
	}
	public Integer getOutAccountTaskId() {
		return outAccountTaskId;
	}
	public void setOutAccountTaskId(Integer outAccountTaskId) {
		this.outAccountTaskId = outAccountTaskId;
	}
 
	public BigDecimal getOutAccountTaskAmount() {
		return outAccountTaskAmount;
	}
	public void setOutAccountTaskAmount(BigDecimal outAccountTaskAmount) {
		this.outAccountTaskAmount = outAccountTaskAmount;
	}
	public BigDecimal getCalcOutAmount() {
		return calcOutAmount;
	}
	public void setCalcOutAmount(BigDecimal calcOutAmount) {
		this.calcOutAmount = calcOutAmount;
	}
	public Integer getBalanceUpCount() {
		return balanceUpCount;
	}
	public void setBalanceUpCount(Integer balanceUpCount) {
		this.balanceUpCount = balanceUpCount;
	}
	public Integer getBalanceMerchantCount() {
		return balanceMerchantCount;
	}
	public void setBalanceMerchantCount(Integer balanceMerchantCount) {
		this.balanceMerchantCount = balanceMerchantCount;
	}
	public Date getSysTime() {
		return sysTime;
	}
	public void setSysTime(Date sysTime) {
		this.sysTime = sysTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUpdator() {
		return updator;
	}
	public void setUpdator(String updator) {
		this.updator = updator;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getBackOperator() {
		return backOperator;
	}
	public void setBackOperator(String backOperator) {
		this.backOperator = backOperator;
	}
	public Date getBackTime() {
		return backTime;
	}
	public void setBackTime(Date backTime) {
		this.backTime = backTime;
	}
	public String getAcqEnname() {
		return acqEnname;
	}
	public void setAcqEnname(String acqEnname) {
		this.acqEnname = acqEnname;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Integer getTranImport() {
		return tranImport;
	}
	public void setTranImport(Integer tranImport) {
		this.tranImport = tranImport;
	}
	public String getDayPhase() {
		return dayPhase;
	}
	public void setDayPhase(String dayPhase) {
		this.dayPhase = dayPhase;
	}
	public String getAccountOwner() {
		return accountOwner;
	}
	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}
	public BigDecimal getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	
}
