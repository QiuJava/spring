package cn.pay.admin.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pay.core.domain.sys.SystemDictionaryItem;
import cn.pay.core.obj.qo.SystemDictionaryQo;
import cn.pay.core.obj.vo.PageResult;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.SystemDictionaryService;

/**
 * 系统字典明细控制器
 * 
 * @author Qiujian
 *
 */
@Controller
@RequestMapping("/systemDictionaryItem")
public class SystemDictionaryItemController {
	
	@Autowired
	private SystemDictionaryItemService service;
	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@RequestMapping("/delete")
	public String delete(Long itemId) {
		Long systemDictionaryId = -1L;
		if (itemId != null) {
			systemDictionaryId = service.getSystemDictionaryId(itemId);
			service.delete(itemId);
		}
		return "redirect:/systemDictionaryItem/list.do?qo.systemDictionaryId=" + systemDictionaryId;
	}

	@RequestMapping("/update")
	public String update(SystemDictionaryItem item) {
		service.update(item);
		return "redirect:/systemDictionaryItem/list.do?qo.systemDictionaryId=" + item.getSystemDictionaryId();
	}

	@RequestMapping("/list")
	public String list(@ModelAttribute("qo") SystemDictionaryQo qo, Model model) {
		Page<SystemDictionaryItem> page = service.page(qo);
		PageResult pageResult = new PageResult(page.getContent(), page.getTotalPages(), qo.getCurrentPage(), qo.getPageSize());
		model.addAttribute("page", pageResult);
		model.addAttribute("systemDictionaryList", systemDictionaryService.list());
		return "dictionary/systemDictionaryItem_list";
	}
}
