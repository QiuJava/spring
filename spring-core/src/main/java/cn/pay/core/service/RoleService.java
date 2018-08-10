package cn.pay.core.service;

import java.util.List;

import cn.pay.core.domain.sys.Role;

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
	List<Role> getAll();

	/**
	 * 保存角色
	 * 
	 * @param p
	 */
	void save(Role p);

}
