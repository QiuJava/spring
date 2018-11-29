package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.entity.IpLog;
import cn.qj.core.service.IpLogService;

/**
 * 首页控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
public class HomeController {

	@Autowired
	private IpLogService ipLogService;

	@RequestMapping("/home")
	public String home() {
		return "main";
	}

	@RequestMapping("/mybatis")
	@ResponseBody
	public String mybatis() {
		IpLog ipLog = ipLogService.getById(1L);
		System.out.println(ipLog);
		return "成功";
	}

}
