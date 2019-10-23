package cn.eeepay.framework.model.bill;

import java.io.Serializable;
import java.math.BigDecimal;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年5月8日08:21:14
 * 代理商分润试算结果
 *
 */
public class TryCalculationResult implements Serializable {
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean status;
	private BigDecimal amount;
	private BigDecimal adjustTransCashAmount;
	private BigDecimal adjustTransShareAmount;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAdjustTransCashAmount() {
		return adjustTransCashAmount;
	}
	public void setAdjustTransCashAmount(BigDecimal adjustTransCashAmount) {
		this.adjustTransCashAmount = adjustTransCashAmount;
	}
	public BigDecimal getAdjustTransShareAmount() {
		return adjustTransShareAmount;
	}
	public void setAdjustTransShareAmount(BigDecimal adjustTransShareAmount) {
		this.adjustTransShareAmount = adjustTransShareAmount;
	}
	 
	
	
	 
}
