package cn.loan.core.util;

/**
 * 状态值工具
 * 
 * @author Qiujian
 * 
 */
public class UserInfoStatusUtil {
	private UserInfoStatusUtil() {
	}

	/** 定义绑定手机的状态码 */
	public static final long OP_BIND_PHONE = 1L;
	/** 定义绑定邮箱的状态 */
	public static final long OP_BIND_EMAIL = 2L << 0;
	/** 定义基本资料状态 */
	public static final long OP_BASIC_INFO = 2L << 1;
	/** 定义实名认证状态 */
	public static final long OP_REAL_AUTH = 2L << 2;
	/** 定义视频认证状态 */
	public static final long OP_VEDIO_AUTH = 2L << 3;
	/** 定义当前用户是否有借款在审核流程 */
	public static final long OP_BORROW_PROCESS = 2L << 4;
	/** 定义当前用户是否有绑定银行卡 */
	public static final long OP_BANK_CARD_BIND = 2L << 5;
	/** 定义当前用户是否有提现在审核流程中 */
	public static final long OP_WITHDRAW_PROCESS = 2L << 6;
	public static final long OP_SERVICE_FEE = 2L << 7;

	/**
	 * 判断是否有这个状态
	 * 
	 */
	public static boolean hasStatus(long statusValue, long status) {
		return (statusValue & status) != 0;
	}

	/**
	 * 添加状态的状态值
	 * 
	 */
	public static long addStatus(long statusValue, long status) {
		if (hasStatus(statusValue, status)) {
			return statusValue;
		}
		return (statusValue | status);
	}

	/**
	 * 删除状态
	 * 
	 */
	public static long removeStatus(long statusValue, long status) {
		if (!hasStatus(statusValue, status)) {
			return statusValue;
		}
		return statusValue ^ status;
	}

}
