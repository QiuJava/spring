package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.common.PageResult;
import com.example.common.Result;
import com.example.entity.Role;
import com.example.qo.RoleQo;
import com.example.service.MenuServiceImpl;
import com.example.service.RoleServiceImpl;
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
		try {
			List<MenuTreeVo> listMenuTreeVoByAll = menuService.listMenuTreeVoByAll(roleId);
			return listMenuTreeVoByAll;
		} catch (Exception e) {
			log.error("系统异常", e);
			return new ArrayList<>();
		}
	}

	@GetMapping("/role/listByQo")
	@ResponseBody
	public PageResult<Role> listByQo(RoleQo roleQo) {
		try {
			Page<Role> page = roleService.listByQo(roleQo);
			return new PageResult<>(page.getTotal(), page.getResult());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new PageResult<>(0L, new ArrayList<>());
		}
	}

	@GetMapping("/role/form")
	public String roleForm(Model model, Long index, Long id) {
		model.addAttribute("index", index);
		model.addAttribute("id", id);
		return "role_form";
	}

	@PostMapping("/role/add")
	@ResponseBody
	public Result<Role> addRole(Role role) {
		try {
			roleService.save(role);
			return new Result<>(true, "添加成功", role);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "添加失败");
		}

	}

	@PostMapping("/role/allotPermission")
	@ResponseBody
	public Result<?> allotPermission(Long roleId, Long[] permissionIdList, Long menuId) {
		try {
			roleService.allotPermission(roleId, permissionIdList, menuId);
			return new Result<>(true, "分配成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "分配失败");
		}

	}

	@PostMapping("/role/update")
	@ResponseBody
	public Result<?> updateRole(Role role) {
		try {
			int updateById = roleService.updateById(role);
			if (updateById != 1) {
				return new Result<>(false, "更新失败");
			}
			return new Result<>(true, "更新成功", role);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "更新失败");
		}

	}

	@PostMapping("/role/verifyRoleName")
	@ResponseBody
	public boolean verifyRoleName(String roleName, Long id) {
		try {
			if (id != 0) {
				String oldRoleName = roleService.getRoleNameById(id);
				if (oldRoleName.equals(roleName)) {
					return true;
				}
			}
			boolean hasByRoleName = roleService.hasByRoleName(roleName);
			return !hasByRoleName;
		} catch (Exception e) {
			log.error("系统异常", e);
			return false;
		}
	}

	@PostMapping("/role/delete")
	@ResponseBody
	public Result<?> deleteRole(Long id) {
		try {
			roleService.deleteById(id);
			return new Result<>(true, "删除成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "删除失败");
		}
	}

}
