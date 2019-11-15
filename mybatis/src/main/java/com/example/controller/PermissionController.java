package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.LogicException;
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

		Date date = new Date();
		permission.setCreateTime(date);
		permission.setUpdateTime(date);
		try {
			boolean hasById = menuService.hasById(menuId);
			if (!hasById) {
				return new Result(false, "菜单ID不存在");
			}

			boolean hasByPermissionName = permissionService.hasByPermissionName(permissionName);
			if (hasByPermissionName) {
				return new Result(false, "权限名称已存在");
			}
			boolean hasByAuthority = permissionService.hasByAuthority(authority);
			if (hasByAuthority) {
				return new Result(false, "权限编码已存在");
			}
			if (StrUtil.hasText(url)) {
				boolean hasByUrl = permissionService.hasByUrl(url);
				if (hasByUrl) {
					return new Result(false, "权限路径已存在");
				}
			}
			int save = permissionService.save(permission);
			if (save != 1) {
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

		Date date = new Date();
		permission.setUpdateTime(date);
		try {
			Permission oldPermission = permissionService.getById(id);
			if (oldPermission == null) {
				return new Result(false, "权限ID不存在");
			}

			if (!permissionName.equals(oldPermission.getPermissionName())) {
				boolean hasByPermissionName = permissionService.hasByPermissionName(permissionName);
				if (hasByPermissionName) {
					return new Result(false, "权限名称已存在");
				}
			} else {
				// 不更新权限名称
				permission.setPermissionName(null);
			}

			if (!authority.equals(oldPermission.getAuthority())) {
				boolean hasByAuthority = permissionService.hasByAuthority(authority);
				if (hasByAuthority) {
					return new Result(false, "权限编码已存在");
				}
			} else {
				// 不更新权限编码
				permission.setAuthority(null);
			}

			if (StrUtil.hasText(url)) {
				if (!url.equals(oldPermission.getUrl())) {
					boolean hasByUrl = permissionService.hasByUrl(url);
					if (hasByUrl) {
						return new Result(false, "权限路径已存在");
					}
				} else {
					permission.setUrl(null);
				}
			}

			int update = permissionService.update(permission);
			if (update != 1) {
				return new Result(false, "更新失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
			return new Result(true, "更新成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "更新失败");
		}
	}

	@GetMapping("/deletePermission")
	public Result deletePermission(Long id) {
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}
		try {
			boolean hasById = permissionService.hasById(id);
			if (!hasById) {
				return new Result(false, "权限ID不存在");
			}

			int deleteById = permissionService.deleteById(id);
			if (deleteById != 1) {
				return new Result(false, "删除失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
			return new Result(true, "删除成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除失败");
		}
	}

	@GetMapping("/listByQo")
	public Result listByQo(PermissionQo qo) {
		Result verify = qo.verify();
		if (verify != null) {
			return verify;
		}

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

		String menuName = qo.getMenuName();
		if (StrUtil.hasText(menuName)) {
			if (menuName.length() > 20) {
				return new Result(false, "菜单名称过长");
			} else if (StrUtil.isContainSpecialChar(menuName)) {
				return new Result(false, "菜单名称不能包含特殊字符");
			}
		}

		try {
			return new Result(true, "查询成功", null, permissionService.listByQo(qo));
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "查询失败");
		}
	}

	@GetMapping("/hasByPermissionName")
	public Result hasByPermissionName(String permissionName) {
		Result verifyPermissionName = this.verifyPermissionName(permissionName);
		if (verifyPermissionName != null) {
			return verifyPermissionName;
		}
		try {
			return new Result(true, "获取成功", null, permissionService.hasByPermissionName(permissionName));
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}
	}

	@GetMapping("/hasByAuthority")
	public Result hasByAuthority(String authority) {
		Result verifyAuthority = this.verifyAuthority(authority);
		if (verifyAuthority != null) {
			return verifyAuthority;
		}
		try {
			return new Result(true, "获取成功", null, permissionService.hasByAuthority(authority));
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}
	}

	@GetMapping("/hasByUrl")
	public Result hasByUrl(String url) {
		Result verifyUrl = this.verifyUrl(url);
		if (verifyUrl != null) {
			return verifyUrl;
		}
		try {
			return new Result(true, "获取成功", null, permissionService.hasByUrl(url));
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
