package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.UserRoleDao;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.ShiroRole;
import cn.eeepay.framework.model.UserRole;
import cn.eeepay.framework.service.ShiroRoleService;
import cn.eeepay.framework.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	@Resource
	public UserRoleDao userRoleDao;
//	@Resource
//	public ShiroRigthService shiroRigthService;
	@Resource
	public ShiroRoleService shiroRoleService;
	
	@Autowired
	private AgentInfoDao agentInfoDao;
	@Override
	public int insertUserRole(Integer userId, Integer roleId) throws Exception {
		return userRoleDao.insertUserRole(userId, roleId);
	}
	@Override
	public int deleteUserRole(Integer userId, Integer role_id) throws Exception {
		return userRoleDao.deleteUserRole(userId, role_id);
	}
	@Override
	public List<UserRole> findUserRoleByUserId(Integer userId) throws Exception {
		return userRoleDao.findUserRoleByUserId(userId);
	}
	@Override
	public int saveUserRole(Integer userId, String[] roleIds) throws Exception {
		List<ShiroRole> selectCheckBoxs = new ArrayList<>();
		List<ShiroRole> shiroRoles= shiroRoleService.findAllShiroRole();
		int i = 0;
		this.deleteUserRoleByUserId(userId);//新增角色之前，先删除用户对应的角色
		
		for (ShiroRole shiroRole : shiroRoles) {
			if (roleIds != null) {
				for (int j = 0; j < roleIds.length; j++) {
					if (roleIds[j] !=null && roleIds[j].trim().length() > 0 ) {
						Integer _roleId = Integer.valueOf(roleIds[j]);
						if (shiroRole.getId().equals(_roleId)) {
							selectCheckBoxs.add(shiroRole);
							break;
						}
					}
					
				}
			}
		}
		for (ShiroRole sr : selectCheckBoxs) {
			i = this.insertUserRole(userId, sr.getId());
		}
		
		return i;
	}
	@Override
	public int deleteUserRoleByUserId(Integer userId) throws Exception {
		return userRoleDao.deleteUserRoleByUserId(userId);
	}
	@Override
	public List<UserRole> findUserRoleByRoleId(Integer roleId) throws Exception {
		return userRoleDao.findUserRoleByRoleId(roleId);
	}
	
	@Override
	public List<UserRole> findUserRoleByUserIdNew(String id,Integer userId) {
		List<UserRole> list = userRoleDao.findUserRoleByUserIdNew(id);
		for (UserRole userRole : list) {
			ShiroRole shiroRole = userRole.getShiroRole();
			userRole.setRoleId(shiroRole.getId());
			userRole.setUserId(Integer.valueOf(userId));
		}
		return list;
	}
	@Override
	public List<UserRole> findUserRoleByOem(String entityId, Integer userId) {
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		
		List<UserRole> list = userRoleDao.findUserRoleByOem(entityId);
		if (list.size()<1) {
			list = userRoleDao.findUserRoleByAgentType(entityId);
			String agentType = agentInfo.getAgentType();
			if ("101".equals(agentType) && list.size()>1 ) {
				list = new ArrayList<>();
			}
		}
		if (list.size()<1) {
			list = userRoleDao.findUserRoleByDefault(entityId);
		}
		for (UserRole userRole : list) {
			userRole.setUserId(Integer.valueOf(userId));
		}
		return list;
	}
	
}
