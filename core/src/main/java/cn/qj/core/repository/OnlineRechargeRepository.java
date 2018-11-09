package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.OnlineRecharge;

/**
 * 线上充值数据操作
 * 
 * @author Qiujian
 * @date 2018/11/05
 */
public interface OnlineRechargeRepository extends JpaRepository<OnlineRecharge, Long> {

}