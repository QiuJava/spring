package cn.loan.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.loan.core.entity.Permission;

/**
 * 权限数据操作
 * 
 * @author qiujian
 *
 */
public interface PermissionDao extends JpaRepository<Permission, Long> {

}
