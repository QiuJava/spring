package cn.qj.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qj.core.pojo.qo.IpLogQo;
import cn.qj.core.service.IpLogService;
import cn.qj.core.util.HttpServletContext;

/**
 * 用户登录日志相关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/ipLog")
public class IpLogController {

	@Autowired
	private IpLogService service;

	@RequestMapping("/pageQuery")
	public String pageQueryList(IpLogQo ipLogQo, Model model) {
		ipLogQo.setIsLike(false);
		ipLogQo.setUsername(HttpServletContext.getCurrentLoginInfo().getUsername());
		model.addAttribute("pageResult", service.pageQueryIpLog(ipLogQo));
		model.addAttribute("ipLogQo", ipLogQo);
		return "ipLog_list";
	}
}
