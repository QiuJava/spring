package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.core.entity.SystemAccountFlow;

/**
 * 系统账户流水相关持久化
 * 
 * @author Qiujian
 * @date 2018/8/10
 */
public interface SystemAccountFlowRepository extends JpaRepository<SystemAccountFlow, Long> {

}
