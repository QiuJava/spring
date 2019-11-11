package com.example.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.config.listener.ContextStartListener;
import com.example.entity.Menu;
import com.example.service.MenuServiceImpl;
import com.example.util.StrUtil;

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
			return new Result(true, "获取成功", null, valueOperations.get(ContextStartListener.ALL_MENU_KEY));
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "系统异常");
		}
	}

	@GetMapping("/addMenu")
	public Result addMenu(Menu menu) {
		Long parentId = menu.getParentId();
		if (parentId != null && parentId.toString().length() > 20) {
			return new Result(false, "上级ID过长");
		}

		String menuName = menu.getMenuName();
		if (StrUtil.noText(menuName)) {
			return new Result(false, "菜单名称不能为空");
		} else if (menuName.length() > 20) {
			return new Result(false, "菜单名称过长");
		} else if (StrUtil.isContainSpecialChar(menuName)) {
			return new Result(false, "菜单名称不能包含特殊字符");
		}

		String intro = menu.getIntro();
		if (StrUtil.hasText(intro)) {
			if (intro.length() > 255) {
				return new Result(false, "菜单描述过长");
			}
			if (StrUtil.isContainSpecialChar(intro)) {
				return new Result(false, "菜单描述不能包含特殊字符");
			}
		}

		Date date = new Date();
		menu.setCreateTime(date);
		menu.setUpdateTime(date);
		try {
			menuService.save(menu);
		} catch (Exception e) {
			log.error("系统异常", e);
			return new Result(false, "系统异常");
		}
		return new Result(true, "添加成功");
	}
}
