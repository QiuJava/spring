package cn.eeepay.framework.enums;
/**
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
public enum TransStatus {

	/**
	 * 成功
	 * 
	 * */
	SUCCESS,

	/**
	 * 失败
	 * 
	 * */
	FAILED,

	/**
	 * 初始化
	 * 
	 * */
	INIT,

	/**
	 * 已冲正
	 * 
	 * */
	REVERSED,

	/**
	 * 已撤销
	 * 
	 * */
	REVOKED,

	/**
	 * 已结算
	 * 
	 * */
	SETTLE,

	/**
	 * 超额
	 * */
	OVERLIMIT,

	/**
	 * 已退款
	 * 
	 * */
	REFUND
	/**
	 * 已完成
	 */
	, COMPLETE;

}
