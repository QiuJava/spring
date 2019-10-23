package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ShiroRole;

public interface ShiroRoleService {
	ShiroRole findShiroRoleByRoleCode(String roleCode)  throws Exception;
	ShiroRole findShiroRoleById(Integer id)  throws Exception;
	List<ShiroRole> findAllShiroRole()  throws Exception;
	int updateShiroRole(ShiroRole shiroRole);
	int deleteShiroRoleById(Integer id);
	int insertShiroRole(ShiroRole shiroRole);
	
	List<ShiroRole> findAllShiroRoleList(ShiroRole shiroRole, Sort sort, Page<ShiroRole> page)  throws Exception;
}
