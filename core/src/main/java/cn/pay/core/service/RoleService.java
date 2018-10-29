package cn.pay.core.service;

import java.util.List;

import cn.pay.core.entity.sys.Role;

/**
 * 角色服务
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
public interface RoleService {

	/**
	 * 获取所有角色
	 * 
	 * @return
	 */
	List<Role> listAll();

	/**
	 * 保存角色
	 * 
	 * @param role
	 * @return
	 */
	Role saveRole(Role role);

	/**
	 * 删除所有角色
	 * 
	 * @param roleListAll
	 */
	void deleteAll(List<Role> roleListAll);

	/**
	 * 更新角色
	 * 
	 * @param role
	 * @return
	 */
	Role updateRole(Role role);

}
