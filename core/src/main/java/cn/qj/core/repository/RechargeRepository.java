package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.Recharge;

/**
 * 充值持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface RechargeRepository extends JpaRepository<Recharge, Long>, JpaSpecificationExecutor<Recharge> {

}
