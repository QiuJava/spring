package cn.qj.key.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.entity.SysUser;
import cn.qj.key.service.SysUserService;
import cn.qj.key.util.BaseResult;

/**
 * 用户数据返回
 * 
 * @author Qiujian
 * @date 2018/12/17
 */
@RestController
public class SysUserController {

	@Autowired
	private SysUserService sysUserService;

	@PostMapping("/sysUser")
	public BaseResult save(SysUser sysUser) {
		int row = sysUserService.save(sysUser);
		BaseResult result = new BaseResult(true, "保存成功");
		if (row != 1) {
			result.setSuccess(false);
			result.setMsg("保存失败");
		}
		return result;
	}

	@GetMapping("/sysUser/{id}")
	public BaseResult get(@PathVariable("id")Long id) {
		SysUser user = sysUserService.getById(id);
		return new BaseResult(true, "查询成功", user);
	}

}
