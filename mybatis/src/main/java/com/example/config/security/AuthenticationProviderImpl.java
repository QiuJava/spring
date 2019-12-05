package com.example.config.security;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.entity.Employee;
import com.example.entity.MenuTree;
import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.service.MenuServiceImpl;
import com.example.service.PermissionServiceImpl;

/**
 * 认证供应实现
 * 
 * @author Qiu Jian
 *
 */
@Configuration
public class AuthenticationProviderImpl implements AuthenticationProvider {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private MenuServiceImpl menuService;
	@Autowired
	private PermissionServiceImpl permissionService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UserDetails userDetails = userDetailsService.loadUserByUsername(authentication.getName());

		if (userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException("用户名或密码错误");
		}

		String credentials = authentication.getCredentials().toString();
		if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
			throw new BadCredentialsException("用户名或密码错误");
		}

		this.setMenuTreeAndPermission(userDetails);

		return new UsernamePasswordAuthenticationToken(userDetails, credentials, userDetails.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
	
	
	private void setMenuTreeAndPermission(UserDetails userDetails) {
		Employee employee = (Employee) userDetails;

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
				} else {
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
			} else {
				return hasMenuPermission(menuTree.getChildren(), menuIdList);
			}
		}
		return false;
	}

}
