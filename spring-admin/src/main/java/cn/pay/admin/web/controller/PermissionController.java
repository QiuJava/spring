package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.domain.sys.Permission;
import cn.pay.core.service.PermissionService;

/**
 * 权限相关控制器
 * 
 * @author Qiujian
 *
 */
@Controller
public class PermissionController {

	@Autowired
	private PermissionService service;

	@RequestMapping("/permission/add")
	@ResponseBody
	public void addPermission() {
		Permission permission = new Permission();
		permission.setUrl("/permission/update.do");
		permission.setName("ROLE_修改权限信息");
		service.save(permission);
	}
}
