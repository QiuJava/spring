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
import com.example.service.LoginLogServiceImpl;
import com.example.service.MenuServiceImpl;
import com.example.util.SecurityContextUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 首页控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class HomeController {

	@Autowired
	private LoginLogServiceImpl loginLogService;
	@Autowired
	private MenuServiceImpl menuService;

	@GetMapping("/")
	@SuppressWarnings("unchecked")
	public String home() {
		Employee currentEmployee = SecurityContextUtil.getCurrentEmployee();
		String username = currentEmployee.getUsername();
		List<Permission> authorities = (List<Permission>) currentEmployee.getAuthorities();
		try {
			
			Date newestLoginTime = loginLogService.getNewestLoginTimeByUsername(username);
			currentEmployee.setNewestLoginTime(newestLoginTime);
			
			List<MenuTree> menuTreeList = menuService.listMenuTreeByAll();
			if (currentEmployee.getSuperAdmin().equals(Employee.IS_NOT_ADMIN)) {
				this.menuTreeMatches(menuTreeList,authorities);
			}
			currentEmployee.setMenuTreeList(menuTreeList);
			return "home";
		} catch (Exception e) {
			log.error("系统异常", e);
			return "error";
		}

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
			}else {
				iterator.remove();
			}
		}
	}
}
