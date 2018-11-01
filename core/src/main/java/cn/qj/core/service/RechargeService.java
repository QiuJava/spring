package cn.qj.core.service;

import cn.qj.core.common.PageResult;
import cn.qj.core.entity.Recharge;
import cn.qj.core.pojo.qo.RechargeQo;

/**
 * 充值服务
 * 
 * @author Qiujian
 * @date 2018/11/01
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
	void audit(Long id, String remark, Integer state);

}
