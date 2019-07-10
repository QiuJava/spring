package cn.loan.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import cn.loan.core.entity.SystemDictionary;
import cn.loan.core.entity.qo.SystemDictionaryQo;
import cn.loan.core.service.SystemDictionaryService;
import cn.loan.core.util.StringUtil;

/**
 * 数据字典控制
 * 
 * @author qiujian
 *
 */
@Controller
public class SystemDictionaryController {

	public static final String MANAGE_SYSTEMDICTIONARY_MAPPING = "/manage/systemDictionary";
	public static final String MANAGE_SYSTEMDICTIONARY_DELETE_MAPPING = "/manage/systemDictionary/delete";
	public static final String MANAGE_SYSTEMDICTIONARY_SAVE_MAPPING = "/manage/systemDictionary/save";
	public static final String REDIRECT_MANAGE_SYSTEMDICTIONARY = "redirect:/manage/systemDictionary";
	public static final String SYSTEMDICTIONARY_SYSTEMDICTIONARY_LIST = "systemDictionary/systemDictionary_list";

	@Autowired
	private SystemDictionaryService systemDictionaryService;

	@GetMapping(MANAGE_SYSTEMDICTIONARY_MAPPING)
	public String systemDictionary(Model model, @ModelAttribute(StringUtil.QO) SystemDictionaryQo qo) {
		model.addAttribute(StringUtil.PAGE_RESULT, systemDictionaryService.pageQuery(qo));
		return SYSTEMDICTIONARY_SYSTEMDICTIONARY_LIST;
	}

	@GetMapping(MANAGE_SYSTEMDICTIONARY_DELETE_MAPPING)
	public String delete(SystemDictionary systemDictionary) {
		systemDictionaryService.delete(systemDictionary);
		return REDIRECT_MANAGE_SYSTEMDICTIONARY;
	}

	@PostMapping(MANAGE_SYSTEMDICTIONARY_SAVE_MAPPING)
	public String save(SystemDictionary systemDictionary) {
		systemDictionaryService.save(systemDictionary);
		return REDIRECT_MANAGE_SYSTEMDICTIONARY;
	}
}
