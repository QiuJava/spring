package cn.pay.core.service;

import java.math.BigDecimal;

import cn.pay.core.pojo.qo.WithdrawQo;
import cn.pay.core.pojo.vo.PageResult;

/**
 * 提现服务
 * 
 * @author Administrator
 *
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
