package cn.qj.core.util;

/**
 * 业务状态工具
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public class BidStateUtil {
	private BidStateUtil() {
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
	public static final long OP_USERBANKINFO_BIND = 2L << 5;
	/** 定义当前用户是否有提现在审核流程中 */
	public static final long OP_WITHDRAW_PROCESS = 2L << 6;

	/**
	 * 判断是否有这个状态
	 * 
	 * @param states
	 *            所有状态值
	 * @param value
	 *            需要判断状态值
	 * @return 是否存在
	 */
	public static boolean hasState(long states, long value) {
		return (states & value) != 0;
	}

	/**
	 * 添加状态的状态值
	 * 
	 * @param states
	 *            已有状态值
	 * @param value
	 *            需要添加状态值
	 * @return 新的状态值
	 */
	public static long addState(long states, long value) {
		if (hasState(states, value)) {
			return states;
		}
		return (states | value);
	}

	/**
	 * 删除状态
	 * 
	 * @param states
	 *            已有状态值
	 * @param value
	 *            需要删除状态值
	 * @return 新的状态值
	 */
	public static long removeState(long states, long value) {
		if (!hasState(states, value)) {
			return states;
		}
		return states ^ value;
	}

}
