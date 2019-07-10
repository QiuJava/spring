package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.Recharge;

/**
 * 充值数据操作
 * 
 * @author qiujian
 *
 */
public interface RechargeDao extends JpaRepository<Recharge, Long>, JpaSpecificationExecutor<Recharge> {

}
