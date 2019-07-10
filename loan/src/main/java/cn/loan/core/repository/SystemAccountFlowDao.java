package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.SystemAccountFlow;

/**
 * 系统账户流水数据操作
 * 
 * @author qiujian
 *
 */
public interface SystemAccountFlowDao extends JpaRepository<SystemAccountFlow, Long> {

}
