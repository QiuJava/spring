package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AcqOutBill implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id ;
	private Integer outBillId;
	private String acqOrgNo;
	private BigDecimal todayAmount;
	private BigDecimal todayHistoryBalance;
	private BigDecimal todayBalance;
	private BigDecimal outAccountTaskAmount;
	private BigDecimal upBalance;
	private BigDecimal calcOutAmount;//计算出账金额(也叫出账单金额)
	private BigDecimal outAmount;
	private String outBillResult;
	private String acqReference;
	private Date createTime;
	
	//用做查询参数
	private String outBillDetailId;//出账详细id
	
	
	public String getOutBillDetailId() {
		return outBillDetailId;
	}
	public void setOutBillDetailId(String outBillDetailId) {
		this.outBillDetailId = outBillDetailId;
	}
	public BigDecimal getUpBalance() {
		return upBalance;
	}
	public void setUpBalance(BigDecimal upBalance) {
		this.upBalance = upBalance;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getOutBillId() {
		return outBillId;
	}
	public void setOutBillId(Integer outBillId) {
		this.outBillId = outBillId;
	}
 
	public String getAcqOrgNo() {
		return acqOrgNo;
	}
	public void setAcqOrgNo(String acqOrgNo) {
		this.acqOrgNo = acqOrgNo;
	}
	public BigDecimal getTodayAmount() {
		return todayAmount;
	}
	public void setTodayAmount(BigDecimal todayAmount) {
		this.todayAmount = todayAmount;
	}

	public BigDecimal getTodayHistoryBalance() {
		return todayHistoryBalance;
	}
	public void setTodayHistoryBalance(BigDecimal todayHistoryBalance) {
		this.todayHistoryBalance = todayHistoryBalance;
	}
	public BigDecimal getTodayBalance() {
		return todayBalance;
	}
	public void setTodayBalance(BigDecimal todayBalance) {
		this.todayBalance = todayBalance;
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
	public BigDecimal getOutAmount() {
		return outAmount;
	}
	public void setOutAmount(BigDecimal outAmount) {
		this.outAmount = outAmount;
	}
	public String getOutBillResult() {
		return outBillResult;
	}
	public void setOutBillResult(String outBillResult) {
		this.outBillResult = outBillResult;
	}
	public String getAcqReference() {
		return acqReference;
	}
	public void setAcqReference(String acqReference) {
		this.acqReference = acqReference;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

		
}
