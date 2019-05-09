package cn.qj.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cn.qj.entity.Authority;

/**
 * 权限数据操作
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
public interface AuthorityRepository extends JpaRepository<Authority, Long> {

	/**
	 * 根据上级权限id获取下级权限
	 * 
	 * @param parentId
	 * @return
	 */
	List<Authority> findByParentId(Long parentId);

	/**
	 * 根据上级权限id统计下级权限个数
	 * 
	 * @param id
	 * @return
	 */
	long countByParentId(Long id);

}
