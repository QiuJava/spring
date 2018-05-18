package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.business.RealAuth;

public interface RealAuthRepository extends JpaRepository<RealAuth, Long>, JpaSpecificationExecutor<RealAuth> {

}
