package cn.pay.core.service;

import cn.pay.core.domain.business.Recharge;
import cn.pay.core.obj.qo.RechargeQo;
import cn.pay.core.obj.vo.PageResult;

/**
 * 线下充值
 * 
 * @author Administrator
 *
 */
public interface RechargeService {

	/**
	 * 线下充值申请
	 * 
	 * @param recharge
	 */
	void apply(Recharge recharge);

	/**
	 * 充值信息列表用于页面分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult list(RechargeQo qo);

	/**
	 * 充值审核
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit(Long id, String remark, int state);

}
