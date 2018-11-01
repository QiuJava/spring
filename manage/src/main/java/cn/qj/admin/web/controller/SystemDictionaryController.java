package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qj.core.entity.SystemDictionary;
import cn.qj.core.pojo.qo.SystemDictionaryQo;
import cn.qj.core.service.SystemDictionaryService;

/**
 * 系统字典控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
@RequestMapping("/systemDictionary")
public class SystemDictionaryController {

	@Autowired
	private SystemDictionaryService service;

	@RequestMapping("/delete")
	public String delete(Long id) {
		service.deleteById(id);
		return "redirect:/systemDictionary/pageQuery";
	}

	@RequestMapping("/update")
	public String update(SystemDictionary systemDictionary) {
		Long id = systemDictionary.getId();

		if (id != null) {
			service.updateSystemDictionary(systemDictionary);
		} else {
			service.saveSystemDictionary(systemDictionary);
		}
		return "redirect:/systemDictionary/pageQuery";
	}

	@RequestMapping("/pageQuery")
	public String page(@ModelAttribute("qo") SystemDictionaryQo qo, Model model) {
		model.addAttribute("pageResult", service.pageQuerySystemDictionary(qo));
		return "systemDictionary/systemDictionary_list";
	}
}
