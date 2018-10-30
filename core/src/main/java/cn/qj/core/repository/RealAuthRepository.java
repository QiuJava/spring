package cn.qj.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.qj.core.entity.RealAuth;

/**
 * 实名认证持久化相关
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface RealAuthRepository extends JpaRepository<RealAuth, Long>, JpaSpecificationExecutor<RealAuth> {

}
