package cn.qj.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.qj.core.entity.SystemDictionaryItem;
import cn.qj.core.pojo.qo.SystemDictionaryItemQo;
import cn.qj.core.service.SystemDictionaryItemService;
import cn.qj.core.service.SystemDictionaryService;

/**
 * 系统字典明细控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
 */
@Controller
@RequestMapping("/systemDictionaryItem")
public class SystemDictionaryItemController {

	@Autowired
	private SystemDictionaryItemService service;
	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@RequestMapping("/delete")
	public String delete(Long itemId, Long sysDictId) {
		service.deleteById(itemId);
		return "redirect:/systemDictionaryItem/pageQuery?systemDictionaryId=" + sysDictId;
	}

	@RequestMapping("/saveOrupdate")
	public String saveOrupdate(SystemDictionaryItem item) {
		service.update(item);
		return "redirect:/systemDictionaryItem/pageQuery?systemDictionaryId=";
	}

	@RequestMapping("/pageQuery")
	public String pageQuery(@ModelAttribute("qo") SystemDictionaryItemQo qo, Model model) {
		model.addAttribute("pageResult", service.pageQuery(qo));
		model.addAttribute("systemDictionaryList", systemDictionaryService.listAll());
		return "systemDictionary/systemDictionaryItem_list";
	}
}
