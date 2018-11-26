package cn.qj.core.consts;

/**
 * 状态常量
 * 
 * @author Qiujian
 * @date 2018/11/26
 */
public class StatusConst {
	private StatusConst() {
	}

	/** 审核中 */
	public static final int AUTH_NORMAL = 0;
	/** 通过 */
	public static final int AUTH_PASS = 1;
	/** 拒绝 */
	public static final int AUTH_REJECT = 2;

	public static final int SUCCEED = 0;
	public static final int FAILD = 1;

	/** 发标审核 */
	public static final int PUSH_AUDIT = 0;
	/** 满标一审 */
	public static final int FULL_AUDIT1 = 1;
	/** 满标二审 */
	public static final int FULL_AUDIT2 = 2;

	public static final int LOGIN_SUCCESS = 0;
	public static final int LOGIN_FAIL = 1;

	public static final int NORMAL = 0;
	public static final int LOCK = 1;
	public static final int DEL = 2;
	public static final int USER_PLATFORM = 0;
	public static final int MANAGER = 1;

	public static final int TRANS_IN = 0;
	public static final int TRANS_SUCCESS = 1;
	public static final int TRANS_FAILURE = 2;

	public static final int MAN = 0;
	public static final int WOMAN = 1;

	/** 正常待还 */
	// public static final int NORMAL = 0;
	/** 已还 */
	public static final int PAYBACK = 1;
	/** 逾期 */
	public static final int OVERDUE = 2;

	public static final int PAUSE = 1;
	// public static final int NORMAL = 0;
}
