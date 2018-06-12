package cn.pay.admin.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import cn.pay.core.domain.sys.Permission;
import cn.pay.core.service.PermissionService;

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
	private PermissionService permissionService;
	
	/** 储存所有系统权限 */
	private Map<String, Collection<ConfigAttribute>> map = null;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (map == null) {
			/**
			 * 加载资源，初始化资源变量
			 */
			map = new HashMap<>();
			Collection<ConfigAttribute> con;
			ConfigAttribute config;
			List<Permission> permissions = permissionService.getAll();
			for (Permission permission : permissions) {
				con = new ArrayList<>();
				config = new SecurityConfig(permission.getName());
				con.add(config);
				map.put(permission.getUrl(), con);
			}
			log.info("security info load success!!");
		}
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		AntPathRequestMatcher matcher;
		String requestUrl;
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();) {
			requestUrl = iter.next();
			matcher = new AntPathRequestMatcher(requestUrl);
			if (matcher.matches(request)) {
				return map.get(requestUrl);
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
