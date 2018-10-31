package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.Order;

/**
 * 订单数据操作
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

}
