package cn.pay.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.entity.business.Withdraw;

/**
 * 提现持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface WithdrawRepository extends JpaRepository<Withdraw, Long>, JpaSpecificationExecutor<Withdraw> {

}
