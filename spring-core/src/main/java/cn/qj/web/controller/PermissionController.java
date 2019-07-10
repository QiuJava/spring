package cn.qj.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.common.BaseResult;
import cn.qj.util.StringUtil;

/**
 * 权限控制器
 * 
 * @author Qiujian
 * @date 2019年5月11日
 *
 */
@RestController
public class PermissionController {

	@RequestMapping("/noPermission")
	public BaseResult noPermission(HttpServletRequest request) {
		return BaseResult.err(request.getAttribute("msg").toString(), StringUtil.EMPTY);
	}
}
