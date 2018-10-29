package cn.pay.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.pay.core.entity.sys.Role;

/**
 * 系统角色相关持久化
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

}
