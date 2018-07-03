package cn.pay.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.domain.sys.Role;

public interface RoleRepository extends JpaRepository<Role, Long>,JpaSpecificationExecutor<Role>{

}
