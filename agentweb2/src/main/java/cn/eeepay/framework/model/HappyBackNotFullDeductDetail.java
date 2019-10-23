package cn.eeepay.framework.model;

import java.math.BigDecimal;
import java.util.List;

public class HappyBackNotFullDeductDetail {
	private List<HappyBackNotFullDeductDetailList> list;

	private BigDecimal totalDebtAmount;
	private BigDecimal shouldDebtAmount;
	private BigDecimal debtAmount;
	private Integer total;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<HappyBackNotFullDeductDetailList> getList() {
		return list;
	}

	public void setList(List<HappyBackNotFullDeductDetailList> list) {
		this.list = list;
	}

	public BigDecimal getTotalDebtAmount() {
		return totalDebtAmount;
	}

	public void setTotalDebtAmount(BigDecimal totalDebtAmount) {
		this.totalDebtAmount = totalDebtAmount;
	}

	public BigDecimal getShouldDebtAmount() {
		return shouldDebtAmount;
	}

	public void setShouldDebtAmount(BigDecimal shouldDebtAmount) {
		this.shouldDebtAmount = shouldDebtAmount;
	}

	public BigDecimal getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(BigDecimal debtAmount) {
		this.debtAmount = debtAmount;
	}

}
