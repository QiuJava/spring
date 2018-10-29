package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.pay.core.entity.business.SystemAccountFlow;

/**
 * 系统账户流水相关持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface SystemAccountFlowRepository extends JpaRepository<SystemAccountFlow, Long> {

}
