package cn.qj.admin.web.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import cn.qj.core.consts.RequestConst;
import cn.qj.core.entity.Role;
import cn.qj.core.service.AccountService;
import cn.qj.core.service.RoleService;

/**
 * 逻辑服务
 * 
 * @author Qiujian
 * @date 2018/10/30
 */
@Controller
public class LogicService {

	@Autowired
	private ApplicationContext ac;

	@Autowired
	private RoleService roleService;

	@Autowired
	private AccountService accountService;

	@RequestMapping("/account/flush")
	public String flush() {
		accountService.flushAccountVerify();
		return "main";
	}

	@RequestMapping("/role/flush")
	@ResponseBody
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
