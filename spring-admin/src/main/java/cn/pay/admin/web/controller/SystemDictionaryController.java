package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.SystemDictionary;
import cn.pay.core.pojo.qo.SystemDictionaryQo;
import cn.pay.core.pojo.vo.PageResult;
import cn.pay.core.service.SystemDictionaryService;

/**
 * 系统字典相关
 * 
 * @author Qiujian
 *
 */
@Controller
@RequestMapping("/systemDictionary")
public class SystemDictionaryController {
	
	@Autowired
	private SystemDictionaryService service;

	@RequestMapping("/delete")
	public String delete(Long id) {
		service.delete(id);
		return "redirect:/systemDictionary/page.do";
	}

	@RequestMapping("/update")
	public String update(SystemDictionary systemDictionary) {
		service.save(systemDictionary);
		return "redirect:/systemDictionary/page.do";
	}

	@RequestMapping("/page")
	public String page(@ModelAttribute("qo") SystemDictionaryQo qo, Model model) {
		Page<SystemDictionary> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(),
				qo.getPageSize());
		model.addAttribute("page", pageResult);
		return "dictionary/systemDictionary_list";
	}
}
