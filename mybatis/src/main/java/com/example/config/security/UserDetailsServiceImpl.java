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
import com.example.entity.Menu;
import com.example.entity.Permission;
import com.example.service.EmployeeServiceImpl;
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
	private ValueOperations<String, Object> valueOpertions;

	@Transactional(rollbackFor = RuntimeException.class)
	@Override
	@SuppressWarnings("unchecked")
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		boolean hasEmployee = employeeService.hasEmployeeByUsername(username);
		if (!hasEmployee) {
			throw new UsernameNotFoundException("用户名或密码错误");
		}
		Employee employee = employeeService.getContainAuthoritiesByUsername(username);
		int status = employee.getStatus();
		Date lockTime = employee.getLockTime();

		// 判断用户是否锁定 锁定状态抛出异常
		if (Employee.LOCK_STATUS == status) {
			// 过了锁定区间 进行解锁操作
			Date date = new Date();
			if (date.getTime() > lockTime.getTime() + DateTimeUtil.LOCK_INTERVAL) {
				employee.setLockTime(null);
				employee.setPasswordErrors(Employee.PASSWORD_ERRORS_INIT);
				employee.setStatus(Employee.NORMAL_STATUS);
				employee.setUpdateTime(date);
				employeeService.updatePasswordErrorsAndStatusAndLockTimeAndUpdateTimeByPrimaryKey(employee);
			} else {
				long differ = date.getTime() - lockTime.getTime();
				StringBuilder builder = new StringBuilder();
				builder.append("账户已锁定，请").append((DateTimeUtil.LOCK_INTERVAL - differ) / 1000).append("秒后再试");
				throw new LockedException(builder.toString());
			}
		}

		List<Menu> menuList = (List<Menu>) valueOpertions.get(ContextStartListener.ALL_MENU_KEY);
		if (menuList != null && menuList.size() > 0) {
			this.menuTreeMatches(menuList, (List<Permission>) employee.getAuthorities());
			employee.setMenuList(menuList);
		}
		return employee;
	}

	private void menuTreeMatches(List<Menu> menuList, List<Permission> authorities) {
		for (Iterator<Menu> iterator = menuList.iterator(); iterator.hasNext();) {
			Menu menu = (Menu) iterator.next();
			List<Permission> pemissionList = menu.getPemissionList();
			int mismatches = 0;
			for (Permission permission : pemissionList) {
				if (!authorities.contains(permission)) {
					mismatches++;
				}

			}
			if (mismatches == pemissionList.size()) {
				iterator.remove();
			}
			// 开始找下级菜单
			List<Menu> children = menu.getChildren();
			if (children != null && children.size() > 0) {
				this.menuTreeMatches(children, authorities);
			}
		}
	}

}
