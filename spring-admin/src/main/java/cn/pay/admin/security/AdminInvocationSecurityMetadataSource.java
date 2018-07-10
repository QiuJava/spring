package cn.pay.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import cn.pay.core.domain.sys.Role;
import cn.pay.core.redis.service.ConfigAttributeRedisService;
import cn.pay.core.service.RoleService;

/**
 * 获取权限资源
 * 
 * @author Qiujian
 *
 */
@Component
public class AdminInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	private static Logger log = LoggerFactory.getLogger(AdminInvocationSecurityMetadataSource.class);

	@Autowired
	private RoleService roleService;
	@Autowired
	private ConfigAttributeRedisService redisService;

	/** 储存所有系统权限 */
	// public static Map<String, Collection<ConfigAttribute>> map = null;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		List<Collection<ConfigAttribute>> list = redisService.getAll();
		if (list.size() == 0) {
			/**
			 * 加载资源，初始化资源变量
			 */
			// map = new HashMap<>();
			Collection<ConfigAttribute> con;
			ConfigAttribute config;
			List<Role> roleList = roleService.getAll();
			for (Role role : roleList) {
				con = new ArrayList<>();
				config = new SecurityConfig(role.getName());
				con.add(config);
				redisService.put(role.getUrl(), con, -1);
			}
			log.info("系统安全信息加载成功!!");
		}
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		AntPathRequestMatcher matcher;
		Set<String> urls = redisService.getKeys();
		for (String url : urls) {
			matcher = new AntPathRequestMatcher(url);
			if (matcher.matches(request)) {
				return redisService.get(url);
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		Collection<ConfigAttribute> con = new ArrayList<>();
		ConfigAttribute config;
		List<Role> roleList = roleService.getAll();
		for (Role role : roleList) {
			config = new SecurityConfig(role.getName());
			con.add(config);
		}
		
		log.info("所有系统安全信息给到SpringSecurity");
		return con;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
