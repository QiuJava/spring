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
import com.example.entity.Menu;
import com.example.qo.MenuQo;
import com.example.service.MenuServiceImpl;
import com.example.util.StrUtil;
import com.example.vo.MenuListVo;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@RequestMapping("/menu")
@Slf4j
public class MenuController {

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private MenuServiceImpl menuService;

	@GetMapping("/menuTree")
	public Result menuTree() {
		try {
			return new Result(true, "获取成功", null, valueOperations.get(ContextStartListener.ALL_MENU_TREE));
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "获取失败");
		}
	}

	@GetMapping("/addMenu")
	public Result addMenu(Menu menu) {
		Long parentId = menu.getParentId();
		if (parentId != null && parentId.toString().length() > 20) {
			return new Result(false, "上级ID过长");
		}

		String menuName = menu.getMenuName();
		Result verifyMenuName = this.verifyMenuName(menuName);
		if (verifyMenuName != null) {
			return verifyMenuName;
		}

		String intro = menu.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		Date date = new Date();
		menu.setCreateTime(date);
		menu.setUpdateTime(date);
		try {
			if (parentId != null) {
				boolean hasById = menuService.hasById(parentId);
				if (!hasById) {
					return new Result(false, "上级菜单ID不存在");
				}
			}

			// 菜单名称不能重复
			boolean hasByMenuName = menuService.hasByMenuName(menuName);
			if (hasByMenuName) {
				return new Result(false, "菜单名称已存在");
			}
			int save = menuService.save(menu);
			if (save != 1) {
				return new Result(false, "添加失败");
			}
			// 重新设置菜单缓存
			valueOperations.set(ContextStartListener.ALL_MENU_TREE, menuService.listAll());
			return new Result(true, "添加成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "添加失败");
		}
	}

	@GetMapping("/updateMenu")
	public Result updateMenu(Menu menu) {
		Long id = menu.getId();
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}

		String menuName = menu.getMenuName();
		Result verifyMenuName = this.verifyMenuName(menuName);
		if (verifyMenuName != null) {
			return verifyMenuName;
		}

		String intro = menu.getIntro();
		if (StrUtil.hasText(intro)) {
			Result verifyIntro = this.verifyIntro(intro);
			if (verifyIntro != null) {
				return verifyIntro;
			}
		}

		Date date = new Date();
		menu.setUpdateTime(date);
		try {
			String oldMenuName = menuService.getMenuNameById(id);
			if (StrUtil.noText(oldMenuName)) {
				return new Result(false, "菜单ID不存在");
			}

			if (!menuName.equals(oldMenuName)) {
				// 菜单名称不能重复
				boolean hasMenuName = menuService.hasByMenuName(menuName);
				if (hasMenuName) {
					return new Result(false, "菜单名称已存在");
				}
			}
			int update = menuService.update(menu);
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

	@GetMapping("/deleteMenu")
	public Result deleteMenu(Long id) {
		Result verifyId = this.verifyId(id);
		if (verifyId != null) {
			return verifyId;
		}

		try {
			boolean hasById = menuService.hasById(id);
			if (!hasById) {
				return new Result(false, "菜单ID不存在");
			}

			int deleteById = menuService.deleteById(id);
			if (deleteById != 1) {
				return new Result(false, "删除失败");
			}
			return new Result(true, "删除成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "删除失败");
		}
	}

	@GetMapping("/listByQo")
	public Result listByQo(MenuQo qo) {
		Result verify = qo.verify();
		if (verify != null) {
			return verify;
		}

		String menuName = qo.getMenuName();
		if (StrUtil.hasText(menuName) && menuName.length() > 20) {
			return new Result(false, "菜单名称过长");
		}

		String parentMenuName = qo.getParentMenuName();
		if (StrUtil.hasText(parentMenuName) && parentMenuName.length() > 20) {
			return new Result(false, "上级菜单名称过长");
		}

		try {
			PageResult<MenuListVo> pageResult = menuService.listByQo(qo);
			return new Result(true, "查询成功", null, pageResult);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "查询失败");
		}
	}

	@GetMapping("/hasByMenuName")
	public Result hasByMenuName(String menuName) {
		Result verifyMenuName = this.verifyMenuName(menuName);
		if (verifyMenuName != null) {
			return verifyMenuName;
		}
		try {
			boolean hasByMenuName = menuService.hasByMenuName(menuName);
			return new Result(true, "获取成功", null, hasByMenuName);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "系统异常");
		}
	}

	private Result verifyMenuName(String menuName) {
		if (StrUtil.noText(menuName)) {
			return new Result(false, "菜单名称不能为空");
		} else if (menuName.length() > 20) {
			return new Result(false, "菜单名称过长");
		} else if (StrUtil.isContainSpecialChar(menuName)) {
			return new Result(false, "菜单名称不能包含特殊字符");
		}
		return null;
	}

	private Result verifyIntro(String intro) {
		if (intro.length() > 255) {
			return new Result(false, "菜单描述过长");
		}
		if (StrUtil.isContainSpecialChar(intro)) {
			return new Result(false, "菜单描述不能包含特殊字符");
		}
		return null;
	}

	private Result verifyId(Long id) {
		if (id == null) {
			return new Result(false, "菜单ID不能为空");
		} else if (id.toString().length() > 20) {
			return new Result(false, "菜单ID过长");
		}
		return null;
	}
}
