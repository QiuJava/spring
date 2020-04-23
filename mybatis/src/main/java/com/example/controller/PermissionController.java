package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.common.LogicException;
import com.example.common.Result;
import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.service.PermissionServiceImpl;
import com.example.vo.PermissionCheckboxVo;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class PermissionController {

	@Autowired
	private PermissionServiceImpl permissionService;

	@PostMapping("/permission/add")
	@ResponseBody
	public Result<?> addPermission(Permission permission) {

		try {
			permissionService.save(permission);
			permissionService.settingPermissionMap();
			return new Result<>(true, "添加成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "添加失败");
		}
	}

	@PostMapping("/permission/update")
	@ResponseBody
	public Result<?> updatePermission(Permission permission) {
		try {
			int update = permissionService.update(permission);
			if (update != 1) {
				return new Result<>(false, "更新失败");
			}
			permissionService.settingPermissionMap();
			return new Result<>(true, "更新成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "更新失败");
		}
	}

	@PostMapping("/permission/remove")
	@ResponseBody
	public Result<?> removePermission(Long id) {
		try {
			permissionService.deleteById(id);
			permissionService.settingPermissionMap();
			return new Result<>(true, "删除成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "删除失败");
		}
	}

	@GetMapping("/permission/listByMenuId")
	@ResponseBody
	public List<PermissionCheckboxVo> listByMenuId(Long menuId, Long roleId) {
		return permissionService.listPermissionCheckboxVoByMenuId(menuId, roleId);
	}

	@GetMapping("/permission/listByQo")
	@ResponseBody
	public Result<?> listByQo(PermissionQo qo) {

		try {
			List<Permission> listByQo = permissionService.listByQo(qo);
			return new Result<>(true, "查询成功", listByQo);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "查询失败");
		}
	}

}
