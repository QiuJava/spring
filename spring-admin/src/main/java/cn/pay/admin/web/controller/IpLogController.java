package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.IpLog;
import cn.pay.core.obj.qo.IpLogQo;
import cn.pay.core.obj.vo.PageResult;
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

	@RequestMapping("/iplog/page")
	public String iplogPage(IpLogQo qo, Model model) {
		qo.setLike(true);
		Page<IpLog> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(),
				qo.getPageSize());
		model.addAttribute("page", pageResult);
		return "iplog/list_detail";
	}

	@RequestMapping("/iplog")
	public String iplog() {
		return "iplog/list";
	}
}
