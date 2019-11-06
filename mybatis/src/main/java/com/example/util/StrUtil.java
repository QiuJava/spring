package com.example.util;

/**
 * 字符串工具
 *
 * @author Qiu Jian
 *
 */
public class StrUtil {
	private StrUtil() {

	}

	/**
	 * 空字符串
	 */
	public static final String EMPTY_TEXT = "";

	/**
	 * 手机号码正则
	 */
	public static final String PHONE_REGEX = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	/**
	 * 邮箱正则
	 */
	public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	/**
	 * 密码正则
	 */
	public static final String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
	/**
	 * 数字正则
	 */
	public static final String NUMBER_REGEX = "[0-9]*";
	/**
	 * 字母正则
	 */
	public static final String LETTER_REGEX = "^[A-Za-z]+$";
	/**
	 * 中文正则
	 */
	public static final String CHINESE_REGEX = "[\\u4e00-\\u9fa5]*";
	/**
	 * 特殊字符正则
	 */
	public static final String SPECIAL_CHAR_REGEX = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";

	/**
	 * 是否包含特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isContainSpecialChar(String str) {
		char[] charArray = str.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			char c = charArray[i];
			boolean isSpecialChar = new String(new char[] { c }).matches(StrUtil.SPECIAL_CHAR_REGEX);
			if (isSpecialChar) {
				return isSpecialChar;
			}
		}
		return false;
	}
	
	/**
	 * 判断是否是身份证号码
	 * 
	 * @param idCardNo
	 * @return
	 */
	public static boolean isIdCardNo(String idCardNo) {
		if (StrUtil.noText(idCardNo)) {
			return false;
		}
		String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|"
				+ "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

		boolean matches = idCardNo.matches(regularExpression);

		// 判断第18位校验值
		if (matches) {
			if (idCardNo.length() == 18) {
				char[] charArray = idCardNo.toCharArray();
				// 前十七位加权因子
				int[] idCardWi = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
				// 这是除以11后，可能产生的11位余数对应的验证码
				String[] idCardY = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
				int sum = 0;
				for (int i = 0; i < idCardWi.length; i++) {
					int current = Integer.parseInt(String.valueOf(charArray[i]));
					int count = current * idCardWi[i];
					sum += count;
				}
				char idCardLast = charArray[17];
				int idCardMod = sum % 11;
				if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
					return true;
				} else {
					return false;
				}
			}

		}
		return matches;
	}

	/**
	 * 判断字符串对象有文本| "" =false, " " = false, null = false, " 1 " = true
	 * 
	 * @param str
	 * @return
	 */
	public static boolean hasText(String str) {
		return str != null && str.trim().length() > 0;
	}

	/**
	 * 判断字符串对象没有文本
	 * 
	 * @param str
	 * @return
	 */
	public static boolean noText(String str) {
		return str == null || str.trim().length() < 1;
	}

}
