package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.Result;
import com.example.entity.Menu;
import com.example.service.MenuServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 菜单控制器
 * 
 * @author Qiu Jian
 *
 */
@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {

	@Autowired
	private MenuServiceImpl menuService;

	@GetMapping("/listAll")
	public Result listAll() {
		Result result = new Result(true, "查询成功");
		try {
			List<Menu> list = menuService.listAll();
			result.setData(list);
		} catch (Exception e) {
			result.setSucceed(false);
			result.setMsg("查询失败");
			log.debug("系统异常", e);
		}
		return result;

	}
}
