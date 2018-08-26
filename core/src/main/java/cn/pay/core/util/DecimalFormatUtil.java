package cn.pay.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cn.pay.core.consts.BidConst;

/**
 * 金额格式化工具类
 * 
 * @author Qiujian
 *
 */
public class DecimalFormatUtil {
	private DecimalFormatUtil() {
	}

	public static BigDecimal amountFormat(BigDecimal number) {
		number = number.setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP);
		return number;
	}

	public static BigDecimal rateFormat(BigDecimal number) {
		number = number.setScale(BidConst.STORE_SCALE, RoundingMode.HALF_UP);
		return number;
	}

	/**
	 * 利率 * 100
	 */
	public static BigDecimal decimalRateFormat(BigDecimal number) {
		return number.multiply(BigDecimal.valueOf(100));
	}

	/**
	 * 月利率格式化
	 */
	public static BigDecimal monthRateFormat(BigDecimal number) {
		return number.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), BidConst.CAL_SCALE,
				RoundingMode.HALF_UP);
	}

	/**
	 * 数额格式化
	 */
	public static BigDecimal formatBigDecimal(BigDecimal data, int scal) {
		if (null == data) {
			return new BigDecimal(0.00);
		}
		return data.setScale(scal, BigDecimal.ROUND_HALF_UP);
	}
}
