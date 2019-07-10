package cn.loan.core.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.loan.core.entity.bo.PerMonth;

/**
 * 精确小数工具
 * 
 * @author qiujian
 *
 */
public class BigDecimalUtil {
	private BigDecimalUtil() {
	}

	public static final Integer CALC_SCALE = 8;
	public static final Integer SAVE_SCALE = 2;
	public static final BigDecimal ZERO = new BigDecimal("0.00");
	public static final BigDecimal ONE = new BigDecimal("1.00");
	public static final BigDecimal TWELVE = new BigDecimal("12.00");
	public static final BigDecimal HUNDRED = new BigDecimal("100.00");

	// ==========================等额本息-按月到期===============================================================================
	/**
	 * 等额本息计算 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
	 * 
	 */
	public static BigDecimal getPerMonthPrincipalInterest(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		BigDecimal monthRate = rate.divide(HUNDRED).divide(TWELVE, CALC_SCALE, BigDecimal.ROUND_HALF_UP);
		return borrowAmount
				.multiply(new BigDecimal(
						monthRate.doubleValue() * Math.pow(1 + monthRate.doubleValue(), repaymentMonths)))
				.divide(new BigDecimal(Math.pow(1 + monthRate.doubleValue(), repaymentMonths) - 1), 2,
						BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 等额本息计算 公式：每月偿还利息=贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
	 * 
	 */
	public static List<PerMonth> getPerMonthInterest(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		List<PerMonth> list = new ArrayList<>();
		BigDecimal monthRate = rate.divide(HUNDRED).divide(TWELVE, CALC_SCALE, BigDecimal.ROUND_HALF_UP);
		BigDecimal monthInterest;
		for (int i = 1; i < repaymentMonths + 1; i++) {
			BigDecimal monthAmount = borrowAmount.multiply(monthRate);
			BigDecimal sub = new BigDecimal(Math.pow(1 + monthRate.doubleValue(), repaymentMonths))
					.subtract(new BigDecimal(Math.pow(1 + monthRate.doubleValue(), i - 1)));
			monthInterest = monthAmount.multiply(sub).divide(
					new BigDecimal(Math.pow(1 + monthRate.doubleValue(), repaymentMonths) - 1), CALC_SCALE,
					BigDecimal.ROUND_HALF_UP);
			monthInterest = monthInterest.setScale(SAVE_SCALE, BigDecimal.ROUND_HALF_UP);

			PerMonth perMonth = new PerMonth();
			perMonth.setMonth(i);
			perMonth.setAmount(monthInterest);
			list.add(perMonth);
		}
		return list;
	}

	public static List<PerMonth> getPerMonthPrincipal(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		BigDecimal perMonthPrincipalInterest = getPerMonthPrincipalInterest(borrowAmount, rate, repaymentMonths);
		List<PerMonth> perMonthInterest = getPerMonthInterest(borrowAmount, rate, repaymentMonths);
		List<PerMonth> list = new ArrayList<>();
		for (PerMonth perMonth : perMonthInterest) {
			PerMonth month = new PerMonth();
			month.setMonth(perMonth.getMonth());
			month.setAmount(perMonthPrincipalInterest.subtract(perMonth.getAmount()));
			list.add(month);
		}
		return list;
	}

	public static BigDecimal getTotalInterest(BigDecimal borrowAmount, BigDecimal rate, Integer repaymentMonths) {
		List<PerMonth> perMonthInterest = getPerMonthInterest(borrowAmount, rate, repaymentMonths);
		BigDecimal totalInterest = ZERO;
		for (PerMonth perMonth : perMonthInterest) {
			totalInterest = totalInterest.add(perMonth.getAmount());
		}
		return totalInterest;
	}

	public static BigDecimal getTotalPrincipal(BigDecimal borrowAmount, BigDecimal rate, Integer repaymentMonths) {
		BigDecimal totalPrincipal = ZERO;
		List<PerMonth> perMonthPrincipal = getPerMonthPrincipal(borrowAmount, rate, repaymentMonths);
		for (PerMonth perMonth : perMonthPrincipal) {
			totalPrincipal = totalPrincipal.add(perMonth.getAmount());
		}
		return totalPrincipal;
	}

	// ===================================================================================================================

	// ===================================按月到期============================================================================
	/**
	 * 按月付息，到期还本计算获取还款方式为按月付息，到期还本的每月偿还利息，最后一个月归还利息+本金
	 * 
	 */
	public static List<PerMonth> getPerMonthPrincipalInterests(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		List<PerMonth> perMonths = getPerMonthInterests(borrowAmount, rate, repaymentMonths);
		BigDecimal principal = borrowAmount.divide(TWELVE, SAVE_SCALE, BigDecimal.ROUND_HALF_UP);
		for (PerMonth perMonth : perMonths) {
			perMonth.setAmount(perMonth.getAmount().add(principal));
		}
		return perMonths;
	}

	public static List<PerMonth> getPerMonthPrincipals(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		List<PerMonth> perMonths = new ArrayList<>();
		for (int i = 1; i <= repaymentMonths; i++) {
			PerMonth perMonth = new PerMonth();
			perMonth.setMonth(i);
			perMonths.add(perMonth);
			if (i == repaymentMonths) {
				perMonth.setAmount(borrowAmount);
			} else {
				perMonth.setAmount(BigDecimal.ZERO);
			}
		}
		return perMonths;
	}

	public static List<PerMonth> getPerMonthInterests(BigDecimal borrowAmount, BigDecimal rate,
			Integer repaymentMonths) {
		BigDecimal monthRate = rate.divide(HUNDRED).divide(TWELVE, CALC_SCALE, BigDecimal.ROUND_HALF_UP);
		BigDecimal monthInterest = borrowAmount.multiply(monthRate);
		List<PerMonth> list = new ArrayList<>();
		for (int i = 1; i <= repaymentMonths; i++) {
			PerMonth perMonth = new PerMonth();
			perMonth.setAmount(monthInterest);
			perMonth.setMonth(i);
			list.add(perMonth);
		}
		return list;
	}

	public static BigDecimal getTotalInterests(BigDecimal borrowAmount, BigDecimal rate, Integer repaymentMonths) {
		List<PerMonth> perMonthInterests = getPerMonthInterests(borrowAmount, rate, repaymentMonths);
		BigDecimal totalInterests = BigDecimalUtil.ZERO;
		for (PerMonth perMonth : perMonthInterests) {
			totalInterests = totalInterests.add(perMonth.getAmount());
		}
		return totalInterests.setScale(SAVE_SCALE, BigDecimal.ROUND_HALF_UP);
	}

}
