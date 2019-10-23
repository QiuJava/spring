package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.ShiroRigth;

public interface ShiroRigthService {
	int insertShiroRigth(ShiroRigth shiroRigth)  throws Exception;
	int updateShiroRigth(ShiroRigth shiroRigth) throws Exception;
	int deleteShiroRigthByRigthCode(String rigthCode)  throws Exception;
	ShiroRigth findShiroRigthById(Integer id)  throws Exception;
	List<ShiroRigth> findShiroRigthByRigthCode(String rigthCode)  throws Exception;
	List<ShiroRigth> findAllShiroRigth()  throws Exception;
	List<ShiroRigth> findUserRoleRigthByUserId(Integer userId)  throws Exception;
	List<ShiroRigth> findUserWithRolesPrivilegeRigthByParentId(Integer userId,Integer parentId)   throws Exception;
	List<ShiroRigth> findShiroRigthByParentId(Integer parentId)   throws Exception;
	List<ShiroRigth> findRolePrivilegeRigthByParentId(Integer roleId,Integer parentId)   throws Exception;
}
