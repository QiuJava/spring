package com.example.config.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.example.entity.Employee;
import com.example.entity.MenuTree;
import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.service.EmployeeServiceImpl;
import com.example.service.MenuServiceImpl;
import com.example.service.PermissionServiceImpl;
import com.example.util.DateTimeUtil;

/**
 * 用户明细服务实现
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private EmployeeServiceImpl employeeService;
	@Autowired
	private MenuServiceImpl menuService;
	@Autowired
	private PermissionServiceImpl permissionService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employee employee = employeeService.getByUsername(username);

		if (employee == null) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		// 判断用户是否锁定 锁定状态抛出异常
		if (Employee.LOCK_STATUS == employee.getStatus()) {
			Date lockTime = employee.getLockTime();
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				employee.setLockTime(null);
				employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				employee.setStatus(Employee.NORMAL_STATUS);
				employee.setUpdateTime(date);
				employeeService.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeById(employee);
			} else {
				long differ = date.getTime() - lockTime.getTime();
				StringBuilder builder = new StringBuilder();
				builder.append("账户已锁定，请").append((DateTimeUtil.LOCK_INTERVAL - differ) / 1000).append("秒后再试");
				throw new LockedException(builder.toString());
			}
		}

		@SuppressWarnings("unchecked")
		List<Permission> authorities = (List<Permission>) employee.getAuthorities();
		List<Long> menuIdList = new ArrayList<>();
		for (Permission permission : authorities) {
			menuIdList.add(permission.getMenuId());
		}

		// 登录用户的菜单
		List<MenuTree> menuTreeList = menuService.listMenuTreeByAll();
		if (employee.getSuperAdmin().equals(Employee.IS_NOT_ADMIN)) {
			this.menuTreeMatches(menuTreeList, menuIdList);
		} else {
			// 超级管理员拥有全部权限
			List<Permission> listByQo = permissionService.listByQo(new PermissionQo());
			employee.setAuthorities(listByQo);
		}
		employee.setMenuTreeList(menuTreeList);
		return employee;
	}

	private void menuTreeMatches(List<MenuTree> menuTreeList, List<Long> menuIdList) {
		for (Iterator<MenuTree> iterator = menuTreeList.iterator(); iterator.hasNext();) {
			MenuTree menuTree = iterator.next();
			Long menuId = menuTree.getId();

			boolean contain = false;
			if (menuIdList.contains(menuId)) {
				contain = true;
			}

			List<MenuTree> children = menuTree.getChildren();
			if (contain) {
				// 继续匹配下级菜单
				this.menuTreeMatches(children, menuIdList);
			} else {
				// 如果有下级菜单的权限则不删除
				boolean hasMenuPermission = this.hasMenuPermission(children, menuIdList);
				if (!hasMenuPermission) {
					iterator.remove();
				}else {
					// 继续匹配下级菜单
					this.menuTreeMatches(children, menuIdList);
				}
			}
		}
	}

	private boolean hasMenuPermission(List<MenuTree> children, List<Long> menuIdList) {
		for (MenuTree menuTree : children) {
			Long id = menuTree.getId();
			if (menuIdList.contains(id)) {
				return true;
			}else {
				return hasMenuPermission(menuTree.getChildren(),menuIdList);
			}
		}
		return false;
	}

}
