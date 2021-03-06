package cn.qj.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

import cn.qj.core.consts.BidConst;

/**
 * 数字格式化工具
 * 
 * @author Qiujian
 * @date 2018/11/01
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
		return number.multiply(BigDecimal.valueOf(100)).divide(BigDecimal.valueOf(12), BidConst.CALC_SCALE,
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
