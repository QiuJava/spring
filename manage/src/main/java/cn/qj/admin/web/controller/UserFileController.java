package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.qj.core.common.AjaxResult;
import cn.qj.core.common.PageResult;
import cn.qj.core.entity.UserFile;
import cn.qj.core.pojo.qo.UserFileQo;
import cn.qj.core.service.UserFileService;

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
