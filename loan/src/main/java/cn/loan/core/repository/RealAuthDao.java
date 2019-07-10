package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.loan.core.entity.RealAuth;

/**
 * 实名认证数据操作
 * 
 * @author Qiujian
 * 
 */
public interface RealAuthDao extends JpaRepository<RealAuth, Long>, JpaSpecificationExecutor<RealAuth> {

}
