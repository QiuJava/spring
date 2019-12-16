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
import com.example.util.StrUtil;
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
	public Result addPermission(Permission permission) {
		Long menuId = permission.getMenuId();
		if (menuId == null) {
			return new Result(false, "菜单ID不能为空");
		} else if (menuId.toString().length() > 20) {
			return new Result(false, "菜单ID过长");
		}

		String permissionName = permission.getPermissionName();
		Result verifyPermissionName = this.verifyPermissionName(permissionName);
		if (verifyPermissionName != null) {
			return verifyPermissionName;
		}

		String authority = permission.getAuthority();
		Result verifyAuthority = this.verifyAuthority(authority);
		if (verifyAuthority != null) {
			return verifyAuthority;
		}

		String url = permission.getUrl();
		if (StrUtil.hasText(url)) {
			Result verifyUrl = this.verifyUrl(url);
			if (verifyUrl != null) {
				return verifyUrl;
			}
		}

		String intro = permission.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		try {
			permissionService.save(permission);
			permissionService.settingPermissionMap();
			return new Result(true, "添加成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "添加失败");
		}
	}

	@PostMapping("/permission/update")
	@ResponseBody
	public Result updatePermission(Permission permission) {
		Result verifyId = this.verifyId(permission.getId());
		if (verifyId != null) {
			return verifyId;
		}

		Result verifyPermissionName = this.verifyPermissionName(permission.getPermissionName());
		if (verifyPermissionName != null) {
			return verifyPermissionName;
		}

		Result verifyAuthority = this.verifyAuthority(permission.getAuthority());
		if (verifyAuthority != null) {
			return verifyAuthority;
		}

		String url = permission.getUrl();
		if (StrUtil.hasText(url)) {
			Result verifyUrl = this.verifyUrl(url);
			if (verifyUrl != null) {
				return verifyUrl;
			}
		}

		String intro = permission.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		try {

			int update = permissionService.update(permission);
			if (update != 1) {
				return new Result(false, "更新失败");
			}
			permissionService.settingPermissionMap();
			return new Result(true, "更新成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "更新失败");
		}
	}

	@PostMapping("/permission/remove")
	@ResponseBody
	public Result removePermission(Long id) {
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}
		try {
			permissionService.deleteById(id);
			permissionService.settingPermissionMap();
			return new Result(true, "删除成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除失败");
		}
	}

	@GetMapping("/permission/listByMenuId")
	@ResponseBody
	public List<PermissionCheckboxVo> listByMenuId(Long menuId, Long roleId) {
		return permissionService.listPermissionCheckboxVoByMenuId(menuId, roleId);
	}

	@GetMapping("/permission/listByQo")
	@ResponseBody
	public Result listByQo(PermissionQo qo) {

		String permissionName = qo.getPermissionName();
		if (StrUtil.hasText(permissionName)) {
			Result verifyPermissionName = this.verifyPermissionName(permissionName);
			if (verifyPermissionName != null) {
				return verifyPermissionName;
			}
		}

		String authority = qo.getAuthority();
		if (StrUtil.hasText(authority)) {
			Result verifyAuthority = this.verifyAuthority(authority);
			if (verifyAuthority != null) {
				return verifyAuthority;
			}
		}

		try {
			List<Permission> listByQo = permissionService.listByQo(qo);
			return new Result(true, "查询成功", listByQo);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "查询失败");
		}
	}

	private Result verifyPermissionName(String permissionName) {
		if (StrUtil.noText(permissionName)) {
			return new Result(false, "权限名称不能为空");
		} else if (permissionName.length() > 20) {
			return new Result(false, "权限名称过长");
		} else if (StrUtil.isContainSpecialChar(permissionName)) {
			return new Result(false, "权限名称不能包含特殊字符");
		}
		return null;
	}

	private Result verifyAuthority(String authority) {
		if (StrUtil.noText(authority)) {
			return new Result(false, "权限编码不能为空");
		} else if (authority.length() > 100) {
			return new Result(false, "权限编码过长");
		} else if (StrUtil.isContainSpecialChar(authority)) {
			return new Result(false, "权限编码不能包含特殊字符");
		}
		return null;
	}

	private Result verifyUrl(String url) {
		if (url.length() > 100) {
			return new Result(false, "权限路径编码过长");
		}
		return null;
	}

	private Result verifyId(Long id) {
		if (id == null) {
			return new Result(false, "权限ID不能为空");
		} else if (id.toString().length() > 20) {
			return new Result(false, "权限ID过长");
		}
		return null;
	}

	private Result verifyIntro(String intro) {
		if (intro.length() > 255) {
			return new Result(false, "权限描述过长");
		}
		if (StrUtil.isContainSpecialChar(intro)) {
			return new Result(false, "权限描述不能包含特殊字符");
		}
		return null;
	}

}
