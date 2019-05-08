package cn.qj.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.Role;

/**
 * 角色数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
