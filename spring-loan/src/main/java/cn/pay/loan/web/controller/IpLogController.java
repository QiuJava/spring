package cn.pay.loan.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.pojo.qo.IpLogQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.IpLogService;
import cn.pay.core.util.HttpServletContext;

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

	@RequestMapping("/page")
	public String page(@ModelAttribute("qo") IpLogQo qo, Model model) {
		qo.setUsername(HttpServletContext.getCurrentLoginInfo().getUsername());
		Page<IpLog> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(),
				qo.getPageSize());
		model.addAttribute("page", pageResult);
		return "iplog_list";
	}
}
