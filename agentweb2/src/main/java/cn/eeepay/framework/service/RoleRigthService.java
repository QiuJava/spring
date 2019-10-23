package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.RoleRigth;

public interface RoleRigthService {
	int insertRoleRigth(Integer roleId,Integer rigthId) throws Exception;
	int deleteRoleRigth(Integer roleId,Integer rigthId) throws Exception;
	int saveRoleRigth(Integer rId,Integer parentId, String[] rigthCodeArray) throws Exception;
	List<RoleRigth> findRoleRigthByRoleId(Integer roleId) throws Exception;
}
