package cn.loan.core.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.loan.core.common.BaseResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.CreditFile;
import cn.loan.core.entity.qo.CreditFileQo;
import cn.loan.core.service.CreditFileService;
import cn.loan.core.util.ServletContextUtil;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;
import cn.loan.core.util.UploadUtil;
import lombok.Setter;

/**
 * 信用材料控制
 * 
 * @author Qiujian
 * 
 */
@Controller
@ConfigurationProperties(prefix = StringUtil.UPLOAD)
public class CreditFileController {

	public static final String CREDIT_FILES_COMMIT = "creditFiles_commit";
	public static final String CREDIT_FILE_AUTH_LIST = "creditFileAuth_list";
	public static final String WEBSITE_CREDIT_FILE_MAPPING = "/website/creditFile";
	public static final String MANAGE_CREDIT_FILE_MAPPING = "/manage/creditFile";
	public static final String WEBSITE_CREDIT_FILE_UPDATETYPE_MAPPING = "/website/creditFile/updateType";
	public static final String WEBSITE_CREDIT_FILE_UPLOAD_MAPPING = "/website/creditFile/upload";
	public static final String REDIRECT_WEBSITE_CREDIT_FILE = "redirect:/website/creditFile";
	public static final String MANAGE_CREDITFILE_AUDIT_MAPPING = "/manage/creditFile/audit";

	@Autowired
	private CreditFileService creditFileService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@Setter
	private String image;

	@GetMapping(WEBSITE_CREDIT_FILE_MAPPING)
	public String creditFile(Model model) {
		List<CreditFile> files = creditFileService.listCurrentFilesTypeIsNull();
		if (files.size() == 0) {
			files = creditFileService.listCurrentFilesTypeIsNotNull();
			model.addAttribute(StringUtil.CREDIT_FILES, files);
			model.addAttribute(StringUtil.SESSIONID, ServletContextUtil.getCurrentHttpSessionId());
			return StringUtil.CREDIT_FILES;
		}
		model.addAttribute(StringUtil.FILE_TYPES,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.CREDIT_FILE_TYPE, systemDictionaryHashService));
		model.addAttribute(StringUtil.CREDIT_FILES, files);
		return CREDIT_FILES_COMMIT;
	}

	@PostMapping(WEBSITE_CREDIT_FILE_UPDATETYPE_MAPPING)
	public String updateType(Long[] id, Long[] fileType) {
		creditFileService.updateType(id, fileType);
		return REDIRECT_WEBSITE_CREDIT_FILE;
	}

	@PostMapping(WEBSITE_CREDIT_FILE_UPLOAD_MAPPING)
	@ResponseBody
	public void upload(MultipartHttpServletRequest request) throws IOException {
		List<MultipartFile> files = request.getFiles(StringUtil.FILE);
		for (MultipartFile file : files) {
			String fileName = UploadUtil.upload(file, image);
			creditFileService.save(fileName);
		}
	}

	@GetMapping(MANAGE_CREDIT_FILE_MAPPING)
	public String manageCreditFile(@ModelAttribute(StringUtil.QO) CreditFileQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, creditFileService.pageQuery(qo));
		return CREDIT_FILE_AUTH_LIST;
	}

	@PostMapping(MANAGE_CREDIT_FILE_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) CreditFileQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, creditFileService.pageQuery(qo));
		return CREDIT_FILE_AUTH_LIST;
	}

	@PostMapping(MANAGE_CREDITFILE_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult audit(Long id, Integer auditStatus, Integer score, String remark) {
		creditFileService.audit(id, auditStatus, score, remark);
		return BaseResult.ok(StringUtil.EMPTY);
	}
}
