package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.PageResult;
import com.example.common.Result;
import com.example.config.listener.ContextStartListener;
import com.example.entity.Permission;
import com.example.qo.PermissionQo;
import com.example.service.MenuServiceImpl;
import com.example.service.PermissionServiceImpl;
import com.example.util.StrUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 权限控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@RequestMapping("/permission")
@Slf4j
public class PermissionController {

	@Autowired
	private PermissionServiceImpl permissionService;
	@Autowired
	private MenuServiceImpl menuService;
	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@GetMapping("/addPermission")
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
		Result verifyUrl = this.verifyUrl(url);
		if (verifyUrl != null) {
			return verifyUrl;
		}

		String intro = permission.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		Date date = new Date();
		permission.setCreateTime(date);
		permission.setUpdateTime(date);
		try {
			boolean hasPermissionName = permissionService.hasPermissionName(permissionName);
			if (hasPermissionName) {
				return new Result(false, "权限名称已存在");
			}
			boolean hasAuthority = permissionService.hasAuthority(authority);
			if (hasAuthority) {
				return new Result(false, "权限编码已存在");
			}
			if (StrUtil.hasText(url)) {
				boolean hasUrl = permissionService.hasUrl(url);
				if (hasUrl) {
					return new Result(false, "权限路径已存在");
				}
			}
			int save = permissionService.save(permission);
			if (save < 1) {
				return new Result(false, "添加失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "添加失败");
		}
		return new Result(true, "添加成功");
	}

	@GetMapping("/updatePermission")
	public Result updatePermission(Permission permission) {
		Long id = permission.getId();
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
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
		Result verifyUrl = this.verifyUrl(url);
		if (verifyUrl != null) {
			return verifyUrl;
		}

		String intro = permission.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		Date date = new Date();
		permission.setUpdateTime(date);
		try {
			Permission oldPermission = permissionService.getById(id);
			if (!permissionName.equals(oldPermission.getPermissionName())) {
				boolean hasPermissionName = permissionService.hasPermissionName(permissionName);
				if (hasPermissionName) {
					return new Result(false, "权限名称已存在");
				}
			}

			if (!authority.equals(oldPermission.getAuthority())) {
				boolean hasAuthority = permissionService.hasAuthority(authority);
				if (hasAuthority) {
					return new Result(false, "权限编码已存在");
				}
			}

			if (StrUtil.hasText(url)) {
				if (!url.equals(oldPermission.getUrl())) {
					boolean hasUrl = permissionService.hasUrl(url);
					if (hasUrl) {
						return new Result(false, "权限路径已存在");
					}
				}
			}
			int update = permissionService.update(permission);
			if (update < 1) {
				return new Result(false, "更新失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "更新失败");
		}
		return new Result(true, "更新成功");
	}

	@GetMapping("/deletePermission")
	public Result deletePermission(Long id) {
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}
		try {
			int deleteById = permissionService.deleteById(id);
			if (deleteById < 1) {
				return new Result(false, "删除失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除失败");
		}
		return new Result(true, "删除成功");
	}

	@GetMapping("/listByQo")
	public Result listByQo(PermissionQo qo) {
		Result verify = qo.verify();
		if (verify != null) {
			return verify;
		}

		String permissionName = qo.getPermissionName();
		if (StrUtil.hasText(permissionName) && permissionName.length() > 20) {
			return new Result(false, "权限名称过长");
		}

		String authority = qo.getAuthority();
		if (StrUtil.hasText(authority) && authority.length() > 100) {
			return new Result(false, "权限编码过长");
		}

		try {
			PageResult<Permission> pageResult = permissionService.listByQo(qo);
			return new Result(true, "查询成功", null, pageResult);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "查询失败");
		}
	}

	@GetMapping("/hasPermissionName")
	public Result hasPermissionName(String permissionName) {
		Result verifyPermissionName = this.verifyPermissionName(permissionName);
		if (verifyPermissionName != null) {
			return verifyPermissionName;
		}
		try {
			boolean hasPermissionName = permissionService.hasPermissionName(permissionName);
			return new Result(true, "获取成功", null, hasPermissionName);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}
	}

	@GetMapping("/hasAuthority")
	public Result hasAuthority(String authority) {
		Result verifyAuthority = this.verifyAuthority(authority);
		if (verifyAuthority != null) {
			return verifyAuthority;
		}
		try {
			boolean hasAuthority = permissionService.hasAuthority(authority);
			return new Result(true, "获取成功", null, hasAuthority);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}
	}

	@GetMapping("/hasUrl")
	public Result hasUrl(String url) {
		Result verifyUrl = this.verifyUrl(url);
		if (verifyUrl != null) {
			return verifyUrl;
		}
		try {
			boolean hasUrl = permissionService.hasUrl(url);
			return new Result(true, "获取成功", null, hasUrl);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
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
		} else if (!authority.startsWith("ROLE_")) {
			return new Result(false, "权限编码格式不正确");
		}
		return null;
	}

	private Result verifyUrl(String url) {
		if (StrUtil.hasText(url) && url.length() > 100) {
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
