package cn.qj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.Permission;

/**
 * 权限数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface PermissionRepository extends JpaRepository<Permission, Long> {

	/**
	 * 根据上级权限id和类型获取下级菜单
	 * 
	 * @param parentId
	 * @param type
	 * @return
	 */
	List<Permission> findByParentIdAndType(Long parentId, Integer type);

	/**
	 * 根据上级权限id和类型统计下级权限个数
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	long countByParentIdAndType(Long id, Integer type);

	/**
	 * 根据上级权限id获取下级菜单
	 * 
	 * @param parentId
	 * @return
	 */
	List<Permission> findByParentId(Long parentId);
	
}
