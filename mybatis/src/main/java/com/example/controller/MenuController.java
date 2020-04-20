package com.example.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.common.LogicException;
import com.example.common.Result;
import com.example.entity.Menu;
import com.example.qo.MenuQo;
import com.example.service.MenuServiceImpl;
import com.example.util.StrUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单控制器
 * 
 * @author Qiu Jian
 *
 */
@Controller
@Slf4j
public class MenuController {

	@Autowired
	private MenuServiceImpl menuService;

	@GetMapping("/menu")
	public String menu() {
		return "menu_list";
	}

	@GetMapping("/menu/listByQo")
	@ResponseBody
	public List<Menu> listByQo(MenuQo qo) {
		try {
			List<Menu> listByQo = menuService.listByQo(qo);
			return listByQo;
		} catch (Exception e) {
			log.error("系统异常", e);
			return new ArrayList<>();
		}
	}

	@PostMapping("/menu/add")
	@ResponseBody
	public Result<?> addMenu(Menu menu) {
		Result<?> verifyMenuName = this.verifyMenuName(menu.getMenuName());
		if (verifyMenuName != null) {
			return verifyMenuName;
		}

		Result<?> verifyUrl = this.verifyUrl(menu.getUrl());
		if (verifyUrl != null) {
			return verifyUrl;
		}

		try {
			menuService.save(menu);
			return new Result<>(true, "添加成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "添加失败");
		}
	}

	@PostMapping("/menu/update")
	@ResponseBody
	public Result<?> updateMenu(Menu menu) {

		Result<?> verifyMenuName = this.verifyMenuName(menu.getMenuName());
		if (verifyMenuName != null) {
			return verifyMenuName;
		}

		Result<?> verifyUrl = this.verifyUrl(menu.getUrl());
		if (verifyUrl != null) {
			return verifyUrl;
		}

		try {
			int update = menuService.update(menu);
			if (update != 1) {
				return new Result<>(false, "更新失败");
			}
			return new Result<>(true, "更新成功");
		} catch (LogicException e) {
			return new Result<>(false, e.getMessage());
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "更新失败");
		}
	}

	@PostMapping("/menu/remove")
	@ResponseBody
	public Result<?> removeMenu(Long id) {
		try {
			menuService.deleteById(id);
			return new Result<>(true, "删除成功");
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result<>(false, "删除失败");
		}
	}

	private Result<?> verifyMenuName(String menuName) {
		if (StrUtil.noText(menuName)) {
			return new Result<>(false, "菜单名称不能为空");
		} else if (menuName.length() > 20) {
			return new Result<>(false, "菜单名称过长");
		} else if (StrUtil.isContainSpecialChar(menuName)) {
			return new Result<>(false, "菜单名称不能包含特殊字符");
		}
		return null;
	}

	private Result<?> verifyUrl(String url) {
		if (StrUtil.noText(url)) {
			return new Result<>(false, "菜单链接不能为空");
		} else if (url.length() > 100) {
			return new Result<>(false, "菜单链接过长");
		}
		return null;
	}
}
