package cn.pay.core.service;

import cn.pay.core.entity.business.Borrow;
import cn.pay.core.entity.business.PaymentPlan;
import cn.pay.core.entity.business.RealAuth;
import cn.pay.core.entity.business.Recharge;
import cn.pay.core.entity.business.RepaymentSchedule;
import cn.pay.core.entity.business.Withdraw;

/**
 * 发送短信服务
 * 
 * @author Administrator
 *
 */
public interface SendSmsService {

	/**
	 * 发送验证码相关信息
	 * 
	 * @param phoneNumber
	 */
	void verifyCode(String phoneNumber);

	/**
	 * 发送借款成功相关信息
	 * 
	 * @param eventObj
	 */
	void borrowSuccess(Borrow eventObj);

	/**
	 * 发送收款成功相关信息
	 * 
	 * @param eventObj
	 */
	void paymentSuccess(PaymentPlan eventObj);

	/**
	 * 发送实名认证相关信息
	 * 
	 * @param eventObj
	 */
	void realAuthSuccess(RealAuth eventObj);

	/**
	 * 发送充值成功相关信息
	 * 
	 * @param eventObj
	 */
	void rechargeSuccess(Recharge eventObj);

	/**
	 * 发送提现成功相关信息
	 * 
	 * @param eventObj
	 */
	void withdrawSuccess(Withdraw eventObj);

	/**
	 * 发送还款提醒 充值还款
	 * 
	 * @param repaymentSchedule
	 */
	void repayWarn(RepaymentSchedule repaymentSchedule);

}
