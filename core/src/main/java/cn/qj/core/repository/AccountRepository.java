package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.Account;

/**
 * 用户账户持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface AccountRepository extends JpaRepository<Account, Long> {

}
