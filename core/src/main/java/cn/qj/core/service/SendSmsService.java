package cn.qj.core.service;

import cn.qj.core.entity.Borrow;
import cn.qj.core.entity.PaymentPlan;
import cn.qj.core.entity.RealAuth;
import cn.qj.core.entity.Recharge;
import cn.qj.core.entity.RepaymentSchedule;
import cn.qj.core.entity.Withdraw;

/**
 * 发送短信服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
