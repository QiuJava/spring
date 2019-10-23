package framework.test;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.bill.ShiroRigth;
import cn.eeepay.framework.model.bill.ShiroRole;
import cn.eeepay.framework.model.bill.ShiroUser;
import cn.eeepay.framework.model.bill.UserRole;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.ShiroRoleService;
import cn.eeepay.framework.service.bill.ShiroUserService;
import cn.eeepay.framework.service.bill.UserRoleService;
public class ShiroUserServiceTest extends BaseTest {
	@Resource
	public ShiroUserService shiroUserService;
	@Resource
	public UserRoleService userRoleService;
	@Resource
	public ShiroRoleService shiroRoleService;
	@Resource 
	public ShiroRigthService shiroRigthService;
//	@Resource
//	public UserDao userDao;
//	//@Test
//	public void test() {
//		//添加
////		int i=0;
////		while(i<20){
////		ShiroUser shiroUser = new ShiroUser();
////		String str=RandomStringUtils.randomAlphanumeric(10);
////		shiroUser.setUserName(str);
////		shiroUser.setCreateTime(new Date());
////		userService.insertUser(shiroUser);
////		i++;
////		}
//		
//		Page<ShiroUser> page=new Page<>();
//		page.setPageSize(10);
//		page.setPageNo(1);
//		Sort sort=new Sort();
//		//查询
//		List<ShiroUser> list=userService.findUsers(new ShiroUser(), sort,page);
//		for(ShiroUser u :list){
//			System.out.println(u.getUserName());
//		}
//	}
//	
//	// @Test
//	public void testManyToMany(){
//		List<ShiroUser> list=userDao.findUsersWithRole();
//		for(ShiroUser u:list){
//			System.out.println(u.getUserName());
//			for(ShiroRole role:u.getRoles()){
//				System.out.println("\t"+role.getRoleName());
//			}
//		}
//	}
//	
//	// @Test
//	public void testCreateKey() throws Exception{
//		System.out.println(genericTableService.createKey());
//	}
//	
////	@Test
//	public void testaaa(){
//		User user = new User();
//		user.setName("rjzouweb");
//		user.setAge(30);
//		user.setPassword("123456");
//		user.setCreatTime(new Date());
//		userService.insertTestUser(user);
//	}
	@Test
	public void test1() throws Exception{
		List<ShiroUser> shiroUsers = shiroUserService.findUsersWithRole2();
		for (ShiroUser shiroUser : shiroUsers) {
			System.out.println(shiroUser.getRealName());
		}
	}
	
//	@Test
	public void test2() throws Exception{
		List<UserRole> userRoles = userRoleService.findUserRoleByUserId(1);
		for (UserRole userRole : userRoles) {
			System.out.println(userRole.getRoleId());
		}
	}
//	@Test
	public void test3() throws Exception{
		List<ShiroRole> userRoles= shiroRoleService.findAllShiroRole();
		for (ShiroRole shiroRole : userRoles) {
			System.out.println(shiroRole.getRoleName());
		}
	}
	//@Test
	public void test4() throws Exception{
		ShiroRigth shiroRigth= shiroRigthService.findShiroRigthById(1008);
		System.out.println(shiroRigth.getRigthName());
	}
}
