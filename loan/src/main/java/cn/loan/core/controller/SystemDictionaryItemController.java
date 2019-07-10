package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.qo.SystemDictionaryItemQo;
import cn.loan.core.service.SystemDictionaryItemService;
import cn.loan.core.service.SystemDictionaryService;
import cn.loan.core.util.StringUtil;

/**
 * 数据字典条目控制
 * 
 * @author qiujian
 *
 */
@Controller
public class SystemDictionaryItemController {

	public static final String SYSTEMDICTIONARY_SYSTEMDICTIONARYITEM_LIST = "systemDictionary/systemDictionaryItem_list";
	public static final String REDIRECT_MANAGE_SYSTEMDICTIONARY_ITEM = "redirect:/manage/systemDictionaryItem?systemDictionaryId=";
	public static final String MANAGE_SYSTEMDICTIONARY_ITEM_DELETE_MAPPING = "/manage/systemDictionaryItem/delete";
	public static final String MANAGE_SYSTEMDICTIONARY_ITEM_SAVE_MAPPING = "/manage/systemDictionaryItem/save";
	public static final String MANAGE_SYSTEMDICTIONARY_ITEM_MAPPING = "/manage/systemDictionaryItem";

	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;
	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@GetMapping(MANAGE_SYSTEMDICTIONARY_ITEM_DELETE_MAPPING)
	public String delete(SystemDictionaryItem item) {
		systemDictionaryItemService.delete(item.getId());
		return this.builderString(item.getSystemDictionary().getId());
	}

	private String builderString(Long id) {
		StringBuilder builder = new StringBuilder();
		builder.append(REDIRECT_MANAGE_SYSTEMDICTIONARY_ITEM).append(id);
		return builder.toString();
	}

	@PostMapping(MANAGE_SYSTEMDICTIONARY_ITEM_SAVE_MAPPING)
	public String save(SystemDictionaryItem item) {
		systemDictionaryItemService.save(item);
		return this.builderString(item.getSystemDictionary().getId());
	}

	@GetMapping(MANAGE_SYSTEMDICTIONARY_ITEM_MAPPING)
	public String systemDictionaryItem(@ModelAttribute(StringUtil.QO) SystemDictionaryItemQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, systemDictionaryItemService.pageQuery(qo));
		model.addAttribute(StringUtil.SYSTEM_DICTIONARY_LIST, systemDictionaryService.getAll());
		return SYSTEMDICTIONARY_SYSTEMDICTIONARYITEM_LIST;
	}

	@PostMapping(MANAGE_SYSTEMDICTIONARY_ITEM_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) SystemDictionaryItemQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, systemDictionaryItemService.pageQuery(qo));
		model.addAttribute(StringUtil.SYSTEM_DICTIONARY_LIST, systemDictionaryService.getAll());
		return SYSTEMDICTIONARY_SYSTEMDICTIONARYITEM_LIST;
	}

}
