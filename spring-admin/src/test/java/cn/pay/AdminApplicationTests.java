package cn.pay;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.Role;
import cn.pay.core.service.RoleService;

/**
 * 使用SpringBoot测试，再多profile下需要在设置环境变量 spring.profile.active=dev
 * 
 * @author Qiujian
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
// @ConfigurationProperties(prefix = "test.sms")
public class AdminApplicationTests {

	/*
	 * @Setter private String url;
	 */

	@Autowired
	private ApplicationContext ac;

	@Autowired
	private RoleService roleService;

	@Test
	public void contextLoads() {
		this.setRole(Controller.class);
	}

	private void setRole(Class<? extends Annotation> clz) {
		Map<String, Object> map = ac.getBeansWithAnnotation(clz);

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			Object value = entry.getValue();
			String[] classMappingStrs = getRequestMappingValue(value);
			for (Method m : value.getClass().getMethods()) {
				String[] methodStrs = getReqMappingValuesFromMethod(m);
				if (this.isNotEmpty(methodStrs)) {
					Role role = new Role();
					StringBuilder sb = new StringBuilder();
					if (this.isNotEmpty(classMappingStrs)) {
						sb.append(classMappingStrs[0]);
					}
					sb.append(methodStrs[0]);
					sb.append(".do");
					role.setUrl(sb.toString());
					role.setName("ROLE_" + sb.toString());
					// 保存权限
					roleService.save(role);
				}
			}
		}
	}

	private String[] getRequestMappingValue(Object obj) {
		return obj.getClass().getAnnotation(RequestMapping.class) == null ? null
				: obj.getClass().getAnnotation(RequestMapping.class).value();
	}

	private String[] getReqMappingValuesFromMethod(Method method) {
		return method.getAnnotation(RequestMapping.class) == null ? null
				: method.getAnnotation(RequestMapping.class).value();
	}

	private boolean isNotEmpty(String[] strs) {
		return strs != null && strs.length > 0;
	}

}
