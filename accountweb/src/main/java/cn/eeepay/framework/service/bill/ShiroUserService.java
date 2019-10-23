package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.User;

public interface ShiroUserService {
	List<ShiroUser> findAllShiroUser()  throws Exception;
	
	List<ShiroUser> findShiroUserNameByParams(ShiroUser user)  throws Exception;
	
	int insertUser(ShiroUser user, String[] roleIds) throws Exception;
	int deleteUserById(Integer id) throws Exception;
	List<ShiroUser> findUsers(ShiroUser user,Sort sort,Page<ShiroUser> page) throws Exception;
	List<ShiroUser> findUsersWithRole2() throws Exception;
	ShiroUser findUserWithRolesByUserName(String userName);
	ShiroUser findUserByUserName(String userName) throws Exception;
	ShiroUser findUserById(Integer id) throws Exception;
	int updateUser(ShiroUser shiroUser, String[] roleIds) throws Exception;
	int updateUserPwd(Integer id,String password) throws Exception;
	boolean isBlocked(String ip) ;
	List<ShiroUser> findAllUsers() throws Exception;
	int insertTestUser(User user) throws Exception;
}
