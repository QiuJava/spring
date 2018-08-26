package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.pojo.qo.IpLogQo;
import cn.pay.core.service.IpLogService;

/**
 * 登录日志相关器
 * 
 * @author Qiujian
 */
@Controller
public class IpLogController {

	@Autowired
	private IpLogService service;

	@RequestMapping("/iplog/pageQuery")
	public String pageQuery(IpLogQo qo, Model model) {
		qo.setIsLike(true);
		model.addAttribute("pageResult", service.pageQueryIpLog(qo));
		return "iplog/list_detail";
	}

	@RequestMapping("/iplog")
	public String iplog() {
		return "iplog/list";
	}
}
