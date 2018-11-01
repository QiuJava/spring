package cn.qj.core.service;

import java.math.BigDecimal;

import cn.qj.core.common.PageResult;
import cn.qj.core.pojo.qo.WithdrawQo;

/**
 * 提现服务
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
public interface WithdrawServcie {

	/**
	 * 获取提现列表用于分页查询
	 * 
	 * @param qo
	 * @return
	 */
	PageResult list(WithdrawQo qo);

	/**
	 * 提现审核
	 * 
	 * @param id
	 * @param remark
	 * @param state
	 */
	void audit(Long id, String remark, Integer state);

	/**
	 * 提现申请
	 * 
	 * @param moneyAmount
	 */
	void apply(BigDecimal moneyAmount);

}
