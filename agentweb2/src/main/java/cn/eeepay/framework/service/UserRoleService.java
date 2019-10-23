package cn.eeepay.framework.service;

import java.util.List;

import cn.eeepay.framework.model.UserRole;

public interface UserRoleService {
	
	int insertUserRole(Integer userId,Integer roleId) throws Exception;
	int deleteUserRole(Integer userId,Integer role_id) throws Exception;
	int deleteUserRoleByUserId(Integer userId) throws Exception;
	List<UserRole> findUserRoleByUserId(Integer userId) throws Exception;
	List<UserRole> findUserRoleByRoleId(Integer roleId) throws Exception;
	int saveUserRole(Integer userId,String[] roleId) throws Exception;
	List<UserRole> findUserRoleByUserIdNew(String id,Integer userId);
	List<UserRole> findUserRoleByOem(String entityId, Integer id);
	
}
