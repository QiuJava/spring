package com.example.config.security;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.example.config.listener.ContextStartListener;
import com.example.entity.Employee;
import com.example.service.EmployeeServiceImpl;
import com.example.service.MenuServiceImpl;
import com.example.util.DateTimeUtil;
import com.example.vo.EmployeeVo;
import com.example.vo.MenuTreeVo;
import com.example.vo.PermissionVo;

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
	private ValueOperations<String, Object> valueOpertions;
	@Autowired
	private MenuServiceImpl menuService;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		boolean hasEmployee = employeeService.hasEmployeeByUsername(username);
		if (!hasEmployee) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}

		EmployeeVo employeeVo = employeeService.getEmployeeVoByUsername(username);

		// 判断用户是否锁定 锁定状态抛出异常
		if (Employee.LOCK_STATUS == employeeVo.getStatus()) {
			Date lockTime = employeeVo.getLockTime();
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				Employee employee = new Employee();
				employee.setId(employeeVo.getId());
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

		// 登录用户的菜单
		List<MenuTreeVo> menuTreeVoList = menuService.listAll(); //(List<MenuTreeVo>) valueOpertions.get(ContextStartListener.ALL_MENU_TREE);
		List<PermissionVo> authorities = (List<PermissionVo>) employeeVo.getAuthorities();
		if (menuTreeVoList != null && authorities != null) {
			this.menuTreeMatches(menuTreeVoList, authorities);
			employeeVo.setMenuTreeVoList(menuTreeVoList);
		}

		return employeeVo;
	}

	private void menuTreeMatches(List<MenuTreeVo> menuTreeVoList, List<PermissionVo> authorities) {
		for (Iterator<MenuTreeVo> iterator = menuTreeVoList.iterator(); iterator.hasNext();) {
			MenuTreeVo menuTreeVo = (MenuTreeVo) iterator.next();
			List<PermissionVo> pemissionVoList = menuTreeVo.getPermissionVoList();
			if (pemissionVoList != null) {
				// 不匹配次数
				int mismatches = 0;
				for (PermissionVo permissionVo : pemissionVoList) {
					if (!authorities.contains(permissionVo)) {
						mismatches++;
					}
				}
				// 没有该菜单的权限去掉该菜单
				if (mismatches == pemissionVoList.size()) {
					iterator.remove();
				}

				// 继续匹配下级菜单
				List<MenuTreeVo> children = menuTreeVo.getChildren();
				if (children != null && children.size() > 0) {
					this.menuTreeMatches(children, authorities);
				}
			}
		}
	}

}
