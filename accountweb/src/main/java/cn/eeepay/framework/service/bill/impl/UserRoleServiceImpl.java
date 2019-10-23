package cn.eeepay.framework.service.bill.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.UserRoleMapper;
import cn.eeepay.framework.model.bill.ShiroRole;
import cn.eeepay.framework.model.bill.UserRole;
import cn.eeepay.framework.service.bill.ShiroRoleService;
import cn.eeepay.framework.service.bill.UserRoleService;
@Service("userRoleService")
@Transactional
public class UserRoleServiceImpl implements UserRoleService {
	@Resource
	public UserRoleMapper userRoleMapper;
//	@Resource
//	public ShiroRigthService shiroRigthService;
	@Resource
	public ShiroRoleService shiroRoleService;
	@Override
	public int insertUserRole(Integer userId, Integer roleId) throws Exception {
		return userRoleMapper.insertUserRole(userId, roleId);
	}
	@Override
	public int deleteUserRole(Integer userId, Integer role_id) throws Exception {
		return userRoleMapper.deleteUserRole(userId, role_id);
	}
	@Override
	public List<UserRole> findUserRoleByUserId(Integer userId) throws Exception {
		return userRoleMapper.findUserRoleByUserId(userId);
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
		return userRoleMapper.deleteUserRoleByUserId(userId);
	}
	@Override
	public List<UserRole> findUserRoleByRoleId(Integer roleId) {
		return userRoleMapper.findUserRoleByRoleId(roleId);
	}
}
