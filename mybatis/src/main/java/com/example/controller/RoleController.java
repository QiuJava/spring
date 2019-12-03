package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.common.LogicException;
import com.example.common.PageResult;
import com.example.common.Result;
import com.example.entity.Role;
import com.example.qo.RoleQo;
import com.example.service.MenuServiceImpl;
import com.example.service.RoleServiceImpl;
import com.example.util.StrUtil;
import com.example.vo.MenuTreeVo;
import com.github.pagehelper.Page;

import lombok.extern.slf4j.Slf4j;

/**
 * 角色控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class RoleController {

	@Autowired
	private RoleServiceImpl roleService;

	@Autowired
	private MenuServiceImpl menuService;

	@GetMapping("/role")
	public String role() {
		return "role_list";
	}

	@GetMapping("/role/menuTree")
	@ResponseBody
	public List<MenuTreeVo> menuTree(Long roleId) {
		return menuService.listMenuTreeVoByAll(roleId);
	}

	@GetMapping("/role/listByQo")
	@ResponseBody
	public PageResult<Role> listByQo(RoleQo roleQo) {
		Page<Role> page = roleService.listByQo(roleQo);
		return new PageResult<>(page.getTotal(), page.getResult());
	}

	@GetMapping("/role/form")
	public String roleForm(Model model, Long index) {
		model.addAttribute("index", index);
		return "role_form";
	}

	@PostMapping("/role/add")
	@ResponseBody
	public Result addRole(Role role) {
		Result verifyRoleName = this.verifyRoleName(role.getRoleName());
		if (verifyRoleName != null) {
			return verifyRoleName;
		}

		String intro = role.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(role.getIntro());
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		try {
			int save = roleService.save(role);
			if (save != 1) {
				return new Result(false, "添加失败");
			}
			return new Result(true, "添加成功", null, role);
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "添加失败");
		}

	}

	@GetMapping("/allotPermission")
	public Result allotPermission(Long roleId, Long[] permissionIdList) {
		Result verifyId = this.verifyId(roleId);
		if (verifyId != null) {
			return verifyId;
		}

		if (permissionIdList == null || permissionIdList.length == 0) {
			return new Result(false, "权限ID组不能为空");
		}

		try {
			roleService.allotPermission(roleId, permissionIdList);
			return new Result(true, "分配成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "分配失败");
		}

	}

	@PostMapping("/role/update")
	@ResponseBody
	public Result updateRole(Role role) {
		Result verifyId = this.verifyId(role.getId());
		if (verifyId != null) {
			return verifyId;
		}

		Result verifyRoleName = this.verifyRoleName(role.getRoleName());
		if (verifyRoleName != null) {
			return verifyRoleName;
		}

		String intro = role.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		try {
			int updateById = roleService.updateById(role);
			if (updateById != 1) {
				return new Result(false, "更新失败");
			}
			return new Result(true, "更新成功", null, role);
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "更新失败");
		}

	}

	@PostMapping("/role/delete")
	@ResponseBody
	public Result deleteRole(Long id) {
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}

		try {
			int deleteById = roleService.deleteById(id);
			if (deleteById != 1) {
				return new Result(false, "删除失败");
			}
			return new Result(true, "删除成功");
		} catch (LogicException e) {
			return new Result(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除失败");
		}
	}

	private Result verifyId(Long id) {
		if (id == null) {
			return new Result(false, "角色ID不能为空");
		} else if (id.toString().length() > 20) {
			return new Result(false, "角色ID过长");
		}
		return null;
	}

	private Result verifyRoleName(String roleName) {
		if (StrUtil.noText(roleName)) {
			return new Result(false, "角色名不能为空");
		} else if (roleName.length() > 20) {
			return new Result(false, "角色名过长");
		} else if (StrUtil.isContainSpecialChar(roleName)) {
			return new Result(false, "角色名不能包含特殊字符");
		}
		return null;
	}

	private Result verifyIntro(String intro) {
		if (intro.length() > 255) {
			return new Result(false, "角色描述过长");
		}
		if (StrUtil.isContainSpecialChar(intro)) {
			return new Result(false, "角色描述不能包含特殊字符");
		}
		return null;
	}
}
