package cn.qj.core.service;

import cn.qj.core.entity.OnlineRecharge;

/**
 * 线上充值服务
 * 
 * @author Qiujian
 * @date 2018/11/05
 */
public interface OnlineRechargeService {

	/**
	 * 保存线下充值信息
	 * 
	 * @param onlineRecharge
	 * @return
	 */
	OnlineRecharge save(OnlineRecharge onlineRecharge);

	/**
	 * 获取线下充值信息
	 * 
	 * @param id
	 * @return
	 */
	OnlineRecharge get(Long id);

	/**
	 * 进行支付
	 * 
	 * @param recharge
	 */
	void pay(OnlineRecharge recharge);

}
