package cn.qj.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.common.BaseResult;
import cn.qj.entity.vo.MenuVo;
import cn.qj.service.AuthorityService;

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
	private AuthorityService authorityService;

	@PostMapping("/menu")
	@ResponseBody
	public BaseResult menu(Long id) {
		try {
			List<MenuVo> menuList = authorityService.getchildrenMenu(id);
			return BaseResult.ok("获取成功", menuList);
		} catch (Exception e) {
			log.error("系统异常", e);
			return BaseResult.err500();
		}
	}

}
