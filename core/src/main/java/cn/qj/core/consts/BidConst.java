package cn.qj.core.consts;

import java.math.BigDecimal;

/**
 * 业务常量类
 * 
 * @author Qiujian
 *
 */
public class BidConst {
	private BidConst() {
	}

	/** 显示的精度为2位 */
	public static final Integer DISP_SCALE = 2;
	/** 保存到数据库精度为4位 */
	public static final Integer STORE_SCALE = 4;
	/** 计算精度为8位 */
	public static final Integer CAL_SCALE = 8;

	public static final BigDecimal ZERO = BigDecimal.ZERO;
	public static final BigDecimal INIT_BORROW_LIMIT = new BigDecimal("50000");

	/** 借款需要的信用分 */
	public static final Integer BORROW_CREDIT_SCORE = 30;

	public static final BigDecimal MIN_BORROW_AMOUNT = new BigDecimal("500");
	public static final BigDecimal MIN_BID_AMOUNT = new BigDecimal("1000");
	public static final BigDecimal MIN_BORROW_RETE = new BigDecimal("5");
	public static final BigDecimal MAX_BORROW_RETE = new BigDecimal("15");
	public static final BigDecimal MIN_WITHDRAW_AMOUNT = new BigDecimal("100");

	/** 待发布：借款刚提交 */
	public static final int BORROW_STATE_PUBLISH_PENDING = 0;
	/** 招标：发标审核通过 */
	public static final int BORROW_STATE_BIDDING = 1;
	/** 已撤销：在发标过程中，如果借款用户撤销了借款或者后台管理员撤销了借款 */
	public static final int BORROW_STATE_UNDO = 2;
	/** 过期：在招标时间内，标的没有投满 */
	public static final int BORROW_STATE_BIDDING_OVERDUE = 3;
	/** 满标1审：满标后进入满标一审（待审） */
	public static final int BORROW_STATE_APPROVE_PENDING_1 = 4;
	/** 满标2审：通过满标一审，进入满标二审（待审） */
	public static final int BORROW_STATE_APPROVE_PENDING_2 = 5;
	/** 满标审核被拒绝：满标一审或者满标二审失败 */
	public static final int BORROW_STATE_REJECTED = 6;
	/** 还款中：通过满标二审，进入还款状态 */
	public static final int BORROW_STATE_PAYING_BACK = 7;
	/** 已还清：所有还款全部还清 */
	public static final int BORROW_STATE_COMPLETE_PAY_BACK = 8;
	/** 逾期：如果某一期还款逾期，整个标都进入逾期状态 */
	public static final int BORROW_STATE_PAY_BACK_OVERDUE = 9;
	/** 初审拒绝状态：发标前审核失败 */
	public static final int BORROW_STATE_PUBLISH_REFUSE = 10;

	/** 按月分期 */
	public static final Integer RETURN_TYPE_MONTH_INTEREST_PRINCIPAL = 0;
	/** 按月到期 */
	public static final Integer RETURN_TYPE_MONTH_INTEREST = 1;

	/** 系统服务费收取比例 */
	public static final BigDecimal ACCOUNT_MANAGER_CHARGE_RATE = new BigDecimal("3");

	/** 资金流水类别：线下充值 */
	public final static Integer ACCOUNT_ACTIONTYPE_DEPOSIT_OFFLINE_LOCAL = 0;
	/** 资金流水类别：提现 */
	public final static Integer ACCOUNT_ACTIONTYPE_WITHDRAW = 1;
	/** 资金流水类别：成功借款 */
	public final static Integer ACCOUNT_ACTIONTYPE_BORROW_SUCCESSFUL = 2;
	/** 资金流水类别：成功投标 */
	public final static Integer ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL = 3;
	/** 资金流水类别：还款 */
	public final static Integer ACCOUNT_ACTIONTYPE_RETURN_MONEY = 4;
	/** 资金流水类别：回款 */
	public final static Integer ACCOUNT_ACTIONTYPE_CALLBACK_MONEY = 5;
	/** 资金流水类别：平台管理费 */
	public final static Integer ACCOUNT_ACTIONTYPE_CHARGE = 6;
	/** 资金流水类别：利息管理费 */
	public final static Integer ACCOUNT_ACTIONTYPE_INTEREST_SHARE = 7;
	/** 资金流水类别：提现手续费 */
	public final static Integer ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 8;
	/** 资金流水类别：充值手续费 */
	public final static Integer ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE = 9;
	/** 资金流水类别：投标冻结金额 */
	public final static Integer ACCOUNT_ACTIONTYPE_BID_FREEZED = 10;
	/** 资金流水类别：取消投标冻结金额 */
	public final static Integer ACCOUNT_ACTIONTYPE_BID_UNFREEZED = 11;
	/** 资金流水类别：提现申请冻结金额 */
	public final static Integer ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED = 12;
	/** 资金流水类别:提现申请失败取消冻结金额 */
	public final static Integer ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED = 13;

	/** 信用认证标 */
	public final static Integer BORROW_TYPE_CREDIT = 0;
}
