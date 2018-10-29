package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.entity.sys.Role;
import cn.pay.core.service.RoleService;

/**
 * 权限相关控制器
 * 
 * @author Qiujian
 *
 */
@Controller
public class RoleController {

	@Autowired
	private RoleService service;

	@RequestMapping("/role/add")
	@ResponseBody
	public void addRole() {
		Role role = new Role();
		role.setUrl("/systemDictionary/page.do");
		role.setName("ROLE_系统字典列表查询");
		service.saveRole(role);
	}
}
