package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.qj.core.entity.Order;
import cn.qj.core.repository.OrderRepository;
import cn.qj.core.service.OrderService;

/**
 * 订单服务实现
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Override
	public Order getById(long orderId) {
		return orderRepository.findOne(orderId);
	}

}
