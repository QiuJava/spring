package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;

public class OutChannelLadderRateRebalance implements Serializable{
	private static final long serialVersionUID = 1L;
	private Long id;
	private String  outAcqEnname; // '出款服务通道英文名',
	private String  outServiceId; // '出账服务编号',
	private String  reType; // '还差类型',
	private Integer  reYear; //'年份',
	private Integer  reMonth; // '月份',
	private BigDecimal  totalOutAmountMonth; //'月累计出款服务总额',
	private BigDecimal totalAvgDayOutAmountMonth; //'该月日均累计出款服务总额',
	private BigDecimal outAmountMonthFee; //'该月出款费用',
	private BigDecimal rebalance; //'应还差金额',
	private BigDecimal  realRebalance; //'实际还差金额',
	private Integer  recordStatus; // '记账状态：  1记账成功，0记账失败，2未记账(中间状态)）',
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOutAcqEnname() {
		return outAcqEnname;
	}
	public void setOutAcqEnname(String outAcqEnname) {
		this.outAcqEnname = outAcqEnname;
	}
	public String getOutServiceId() {
		return outServiceId;
	}
	public void setOutServiceId(String outServiceId) {
		this.outServiceId = outServiceId;
	}
	public String getReType() {
		return reType;
	}
	public void setReType(String reType) {
		this.reType = reType;
	}
	public Integer getReYear() {
		return reYear;
	}
	public void setReYear(Integer reYear) {
		this.reYear = reYear;
	}
	public Integer getReMonth() {
		return reMonth;
	}
	public void setReMonth(Integer reMonth) {
		this.reMonth = reMonth;
	}
	public BigDecimal getTotalOutAmountMonth() {
		return totalOutAmountMonth;
	}
	public void setTotalOutAmountMonth(BigDecimal totalOutAmountMonth) {
		this.totalOutAmountMonth = totalOutAmountMonth;
	}
	public BigDecimal getTotalAvgDayOutAmountMonth() {
		return totalAvgDayOutAmountMonth;
	}
	public void setTotalAvgDayOutAmountMonth(BigDecimal totalAvgDayOutAmountMonth) {
		this.totalAvgDayOutAmountMonth = totalAvgDayOutAmountMonth;
	}
	public BigDecimal getOutAmountMonthFee() {
		return outAmountMonthFee;
	}
	public void setOutAmountMonthFee(BigDecimal outAmountMonthFee) {
		this.outAmountMonthFee = outAmountMonthFee;
	}
	public BigDecimal getRebalance() {
		return rebalance;
	}
	public void setRebalance(BigDecimal rebalance) {
		this.rebalance = rebalance;
	}
	public BigDecimal getRealRebalance() {
		return realRebalance;
	}
	public void setRealRebalance(BigDecimal realRebalance) {
		this.realRebalance = realRebalance;
	}
	public Integer getRecordStatus() {
		return recordStatus;
	}
	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}
}
