package cn.pay;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cn.qj.core.consts.RequestConst;
import cn.qj.core.entity.Role;
import cn.qj.core.service.RoleService;
import lombok.Setter;

/**
 * 
 * @author Qiujian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ConfigurationProperties(prefix = "test.sms")
public class ManageApplicationTest {

	@Setter
	private String url;

	@Autowired
	private ApplicationContext ac;

	@Autowired
	private RoleService roleService;

	@Test
	public void contextLoads() {
		roleService.deleteAll(roleService.listAll());
		RequestMappingHandlerMapping mapping = ac.getBean(RequestMappingHandlerMapping.class);
		// 获取url与类和方法的对应信息
		Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();
		for (RequestMappingInfo info : map.keySet()) {
			// 获取url的Set集合，一个方法可能对应多个url
			Set<String> patterns = info.getPatternsCondition().getPatterns();
			for (String url : patterns) {
				if (RequestConst.LOGIN_INFO_AJAX.equals(url)) {
					break;
				}
				if ("/error".equals(url)) {
					break;
				}
				Role role = new Role();
				role.setUrl(url);
				role.setName("ROLE_" + url);
				role.setDescritpion("角色");
				// 保存权限
				roleService.saveRole(role);
			}
		}

	}

}
