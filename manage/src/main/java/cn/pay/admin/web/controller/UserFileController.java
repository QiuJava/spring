package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.pay.core.domain.business.UserFile;
import cn.pay.core.pojo.qo.UserFileQo;
import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.UserFileService;

/**
 * 用户材料审核相关
 * 
 * @author Administrator
 *
 */
@Controller
public class UserFileController {

	@Autowired
	private UserFileService service;

	@RequestMapping("/userFile")
	public String page(@ModelAttribute("qo") UserFileQo qo, Model model) {
		Page<UserFile> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(),
				qo.getPageSize());
		model.addAttribute("pageResult", pageResult);
		return "userFileAuth/list";
	}

	@RequestMapping("/userFile/audit")
	@ResponseBody
	public AjaxResult audit(Long id, int state, int score, String remark) {
		AjaxResult result = new AjaxResult();
		service.audit(id, state, score, remark);
		result.setSuccess(true);
		return result;
	}
}
