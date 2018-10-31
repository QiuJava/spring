package cn.qj.core.service;

import cn.qj.core.entity.Order;

/**
 * 订单服务
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
public interface OrderService {

	/**
	 * 根据订单ID获取订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	Order getById(long orderId);

}
