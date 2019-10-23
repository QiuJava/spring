package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.RoleRigthDao;
import cn.eeepay.framework.model.RoleRigth;
import cn.eeepay.framework.model.ShiroRigth;
import cn.eeepay.framework.service.RoleRigthService;
import cn.eeepay.framework.service.ShiroRigthService;
import cn.eeepay.framework.service.UserRigthService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service("roleRigthService")
public class RoleRigthServiceImpl implements RoleRigthService {
	@Resource
	public RoleRigthDao roleRigthDao;
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Override
	public int insertRoleRigth(Integer roleId, Integer rigthId) throws Exception {
		return roleRigthDao.insertRoleRigth(roleId, rigthId);
	}
	@Override
	public int deleteRoleRigth(Integer roleId, Integer rigthId) throws Exception {
		return roleRigthDao.deleteRoleRigth(roleId, rigthId);
	}
	@Override
	public List<RoleRigth> findRoleRigthByRoleId(Integer roleId) throws Exception {
		return roleRigthDao.findRoleRigthByRoleId(roleId);
	}
	@Override
	public int saveRoleRigth(Integer rId, Integer menuId, String[] rigthCodeArray) throws Exception {
		List<ShiroRigth> selectCheckBoxs = new ArrayList<>();
		List<ShiroRigth> shiroRigths = shiroRigthService.findShiroRigthByParentId(menuId);
		int i = 0;
		for (ShiroRigth shiroRigth : shiroRigths) {//清空原有的
			int n = this.deleteRoleRigth(rId, shiroRigth.getId());
			i += n;
		}
		for (ShiroRigth shiroRigth : shiroRigths) {
			for (int j = 0; j < rigthCodeArray.length; j++) {
				String _rigthCode = rigthCodeArray[j];
				if (shiroRigth.getRigthCode().equals(_rigthCode)) {
					selectCheckBoxs.add(shiroRigth);
					break;
				}
			}
		}
		for (ShiroRigth shiroRigth : selectCheckBoxs) {
			int n = this.insertRoleRigth(rId, shiroRigth.getId());
			int m = userRigthService.deleteUserRigthByRigthId(shiroRigth.getId());//同时删除权限对应(user_rigth)表的所有用户(userId)对应的权限(rigthId)
			
			i += n;
			i += m;
		}
		return i;
	}

	



	
}
