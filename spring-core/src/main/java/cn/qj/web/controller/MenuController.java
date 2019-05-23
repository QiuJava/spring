package cn.qj.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.qj.common.BaseResult;
import cn.qj.config.properties.ConstProperties;
import cn.qj.entity.Permission;
import cn.qj.entity.vo.MenuListVo;
import cn.qj.entity.vo.MenuVo;
import cn.qj.service.PermissionService;

/**
 * 菜单控制器
 * 
 * @author Qiujian
 * @date 2019年5月8日
 *
 */
@Controller
public class MenuController {

	private static final Logger log = LoggerFactory.getLogger(MenuController.class);

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private HashOperations<String,String, Object> hashOperations;
	
	@Autowired
	private ConstProperties constProperties;

	@PostMapping("/menu/tree")
	@ResponseBody
	public BaseResult menu(Long id) {
		try {
			List<MenuVo> menuList = permissionService.getchildrenMenu(id);
			return BaseResult.ok("获取成功", menuList);
		} catch (Exception e) {
			log.error("系统异常", e);
			return BaseResult.err500();
		}
	}

	@GetMapping("/menu")
	public String menuList() {
		return "menu/list";
	}

	@PostMapping("/menu")
	@ResponseBody
	public BaseResult menuQuery() {
		try {
			List<Object> permissions = hashOperations.values(constProperties.getPermissionHash());
			MenuListVo menuListVo = new MenuListVo();
			menuListVo.setTotal(permissions.size());
			menuListVo.setRows(permissions);
			return BaseResult.ok("查询成功", menuListVo);
		} catch (Exception e) {
			log.error("系统异常", e);
			return BaseResult.err500();
		}
	}

	@PostMapping("/menu/save")
	@ResponseBody
	public String saveMenu(Permission permission) {
		try {
			permissionService.save(permission);
			return JSON.toJSONString(BaseResult.ok("保存成功", ""));
		} catch (Exception e) {
			log.error("系统异常", e);
			return JSON.toJSONString(BaseResult.err500());
		}
	}

	@PostMapping("/menu/delete")
	@ResponseBody
	public String deleteMenu(Permission permission) {
		try {
			permissionService.delete(permission);
			return JSON.toJSONString(BaseResult.ok("删除成功", ""));
		} catch (Exception e) {
			log.error("系统异常", e);
			return JSON.toJSONString(BaseResult.err500());
		}
	}

}
