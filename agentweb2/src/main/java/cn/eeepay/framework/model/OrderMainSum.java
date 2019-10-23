package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * desc:订单表汇总数据
 * 
 * @author tans
 * @date 2017-12-1
 */
public class OrderMainSum {

	private BigDecimal totalBonusSum;// 奖励总金额

	private BigDecimal plateProfitSum;// 平台分润汇总

	private BigDecimal orgProfitSum;// 发卡品牌分润汇总

	private BigDecimal orgProfitSum2;// 首刷品牌分润汇总

	private BigDecimal profitSum;// 收益汇总

	private BigDecimal obtainAmountSum;// 结算金额汇总

	private BigDecimal priceSum;// 售价汇总

	private BigDecimal receiveAmountSum;// 目标还款金额汇总

	private BigDecimal repaymentAmountSum;// 实际还款金额汇总

	public BigDecimal getOrgProfitSum2() {
		return orgProfitSum2 == null ? new BigDecimal("0.00") : orgProfitSum2;
	}

	public void setOrgProfitSum2(BigDecimal orgProfitSum2) {
		this.orgProfitSum2 = orgProfitSum2;
	}

	public BigDecimal getReceiveAmountSum() {
		return receiveAmountSum;
	}

	public void setReceiveAmountSum(BigDecimal receiveAmountSum) {
		this.receiveAmountSum = receiveAmountSum;
	}

	public BigDecimal getRepaymentAmountSum() {
		return repaymentAmountSum;
	}

	public void setRepaymentAmountSum(BigDecimal repaymentAmountSum) {
		this.repaymentAmountSum = repaymentAmountSum;
	}

	public BigDecimal getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(BigDecimal priceSum) {
		this.priceSum = priceSum;
	}

	public BigDecimal getObtainAmountSum() {
		return obtainAmountSum != null ? obtainAmountSum : new BigDecimal(0);
	}

	public void setObtainAmountSum(BigDecimal obtainAmountSum) {
		this.obtainAmountSum = obtainAmountSum;
	}

	public BigDecimal getProfitSum() {
		return profitSum != null ? profitSum : new BigDecimal(0);
	}

	public void setProfitSum(BigDecimal profitSum) {
		this.profitSum = profitSum;
	}

	public BigDecimal getTotalBonusSum() {
		return totalBonusSum != null ? totalBonusSum : new BigDecimal(0);
	}

	public void setTotalBonusSum(BigDecimal totalBonusSum) {
		this.totalBonusSum = totalBonusSum;
	}

	public BigDecimal getPlateProfitSum() {
		return plateProfitSum != null ? plateProfitSum : new BigDecimal(0);
	}

	public void setPlateProfitSum(BigDecimal plateProfitSum) {
		this.plateProfitSum = plateProfitSum;
	}

	public BigDecimal getOrgProfitSum() {
		return orgProfitSum != null ? orgProfitSum : new BigDecimal(0);
	}

	public void setOrgProfitSum(BigDecimal orgProfitSum) {
		this.orgProfitSum = orgProfitSum;
	}
}