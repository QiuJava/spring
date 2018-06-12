package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long>,JpaSpecificationExecutor<Permission>{

}
