package cn.pay.admin.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	private Map<String, Collection<ConfigAttribute>> collectionConfigAttrMap = null;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (collectionConfigAttrMap == null) {
			collectionConfigAttrMap = new HashMap<>();
			/**
			 * 加载资源，初始化资源变量
			 */
			Collection<ConfigAttribute> con;
			ConfigAttribute config;
			List<Role> roleList = roleService.getAll();
			for (Role role : roleList) {
				con = new ArrayList<>();
				config = new SecurityConfig(role.getName());
				con.add(config);
				collectionConfigAttrMap.put(role.getUrl(), con);
			}
			log.info("系统安全信息加载成功!!");
		}
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

		AntPathRequestMatcher matcher = null;
		Set<String> urls = collectionConfigAttrMap.keySet();
		for (String url : urls) {
			matcher = new AntPathRequestMatcher(url);
			if (matcher.matches(request)) {
				return collectionConfigAttrMap.get(url);
			}
		}
		return null;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<>();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
