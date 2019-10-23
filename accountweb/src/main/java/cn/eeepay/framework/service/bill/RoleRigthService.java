package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.bill.RoleRigth;

public interface RoleRigthService {
	int insertRoleRigth(Integer roleId,Integer rigthId) throws Exception;
	int deleteRoleRigth(Integer roleId,Integer rigthId) throws Exception;
	int saveAllRoleRigth(List<Map<String, String>> list) throws Exception;
	int saveRoleRigth(Integer rId,Integer parentId, String[] rigthCodeArray) throws Exception;
	List<RoleRigth> findRoleRigthByRoleId(Integer roleId) throws Exception;
}
