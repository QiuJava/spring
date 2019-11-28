package com.example.controller;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.entity.Employee;
import com.example.entity.MenuTree;
import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.service.LoginLogServiceImpl;
import com.example.service.MenuServiceImpl;
import com.example.service.PermissionServiceImpl;
import com.example.util.SecurityContextUtil;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
public class HomeController {

	@Autowired
	private LoginLogServiceImpl loginLogService;
	@Autowired
	private MenuServiceImpl menuService;
	@Autowired
	private PermissionServiceImpl permissionService;

	@GetMapping("/")
	@SuppressWarnings("unchecked")
	public String home() {
		Employee currentEmployee = SecurityContextUtil.getCurrentEmployee();
		String username = currentEmployee.getUsername();
		List<Permission> authorities = (List<Permission>) currentEmployee.getAuthorities();

		Date newestLoginTime = loginLogService.getNewestLoginTimeByUsername(username);
		currentEmployee.setNewestLoginTime(newestLoginTime);

		// 登录用户的菜单
		List<MenuTree> menuTreeList = menuService.listMenuTreeByAll();
		if (currentEmployee.getSuperAdmin().equals(Employee.IS_NOT_ADMIN)) {
			this.menuTreeMatches(menuTreeList, authorities);
		}else {
			// 超级管理员拥有全部权限
			List<Permission> listByQo = permissionService.listByQo(new PermissionQo());
			currentEmployee.setAuthorities(listByQo);
		}
		currentEmployee.setMenuTreeList(menuTreeList);
		return "home";
	}

	private void menuTreeMatches(List<MenuTree> menuTreeList, List<Permission> authorities) {
		for (Iterator<MenuTree> iterator = menuTreeList.iterator(); iterator.hasNext();) {
			MenuTree menuTree = iterator.next();
			Long menuId = menuTree.getId();

			boolean contain = false;
			for (Permission permission : authorities) {
				if (permission.getMenuId().equals(menuId)) {
					contain = true;
					break;
				}
			}

			if (contain) {
				// 继续匹配下级菜单
				List<MenuTree> children = menuTree.getChildren();
				if (children != null && children.size() > 0) {
					this.menuTreeMatches(children, authorities);
				}
			} else {
				iterator.remove();
			}
		}
	}
}
