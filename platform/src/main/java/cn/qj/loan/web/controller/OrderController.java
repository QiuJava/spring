package cn.qj.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import cn.qj.core.common.LogicException;
import cn.qj.core.entity.Order;
import cn.qj.core.service.OrderService;

/**
 * 订单控制器
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Controller
public class OrderController {

	@Autowired
	private OrderService orderService;

	/**
	 * 订单支付发起
	 * 
	 * @param orderId
	 * @param returnUrl
	 * @return
	 */
	@PostMapping("/order")
	public String create(long orderId, int transChannel) {

		// 根据订单id查询订单,数据库查询
		Order order = orderService.getById(orderId);
		if (order == null) {
			// 订单不存在抛出错误
			throw new LogicException("订单号不存在");
		}
		return null;
	}

}
