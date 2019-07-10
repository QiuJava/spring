package cn.loan.core.controller;

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

import cn.loan.core.common.BaseResult;
import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.RealAuth;
import cn.loan.core.entity.SystemDictionaryItem;
import cn.loan.core.entity.UserInfo;
import cn.loan.core.entity.qo.RealAuthQo;
import cn.loan.core.service.RealAuthService;
import cn.loan.core.service.UserInfoService;
import cn.loan.core.util.StringUtil;
import cn.loan.core.util.SystemDictionaryUtil;
import cn.loan.core.util.UploadUtil;
import lombok.Setter;

/**
 * 实名认证控制器
 * 
 * @author Qiujian
 * 
 */
@Controller
@ConfigurationProperties(prefix = StringUtil.UPLOAD)
public class RealAuthController {

	public static final String WEBSITE_REAL_AUTH_MAPPING = "/website/realAuth";
	public static final String MANAGE_REAL_AUTH_MAPPING = "/manage/realAuth";
	public static final String WEBSITE_REAL_AUTH_UPLOAD_MAPPING = "/website/realAuth/upload";
	public static final String WEBSITE_REAL_AUTH_SAVE_MAPPING = "/website/realAuth/save";
	public static final String MANAGE_REAL_AUTH_AUDIT_MAPPING = "/manage/realAuth/audit";
	public static final String REALAUTH_RESULT = "realAuth_result";
	public static final String REALAUTH_LIST = "realAuth/list";

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RealAuthService realAuthService;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	@Setter
	private String image;

	@GetMapping(WEBSITE_REAL_AUTH_MAPPING)
	public String realAuth(Model model) {
		UserInfo userInfo = userInfoService.getCurrent();
		Long realAuthId = userInfo.getRealAuthId();
		// 如果用户已经实名认证直接跳到结果
		if (userInfo.isRealAuth()) {
			List<SystemDictionaryItem> genders = SystemDictionaryUtil.getItems(SystemDictionaryUtil.GENDER,
					systemDictionaryHashService);
			RealAuth realAuth = realAuthService.get(realAuthId);
			for (SystemDictionaryItem item : genders) {
				if (realAuth.getGender().equals(Integer.valueOf(item.getItemValue()))) {
					realAuth.setDisplayGender(item.getItemName());
				}
			}
			model.addAttribute(StringUtil.REAL_AUTH, realAuth);
			model.addAttribute(StringUtil.USER_INFO, userInfo);
			return REALAUTH_RESULT;
		}
		if (realAuthId != null) {
			model.addAttribute(StringUtil.USER_INFO, userInfo);
			return REALAUTH_RESULT;
		}
		model.addAttribute(StringUtil.ITEMS,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.GENDER, systemDictionaryHashService));
		// 如果用户没有进行实名认证 则跳到实名认证界面
		return StringUtil.REAL_AUTH;
	}

	@PostMapping(WEBSITE_REAL_AUTH_UPLOAD_MAPPING)
	@ResponseBody
	public String upload(MultipartFile file) {
		return UploadUtil.upload(file, image);
	}

	@PostMapping(WEBSITE_REAL_AUTH_SAVE_MAPPING)
	@ResponseBody
	public BaseResult save(RealAuth realAuth) {
		realAuthService.save(realAuth);
		return BaseResult.ok(StringUtil.EMPTY);
	}

	@GetMapping(MANAGE_REAL_AUTH_MAPPING)
	public String realAuth(@ModelAttribute(StringUtil.QO) RealAuthQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, realAuthService.pageQuery(qo));
		model.addAttribute(StringUtil.ITEMS,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT, systemDictionaryHashService));
		return REALAUTH_LIST;
	}

	@PostMapping(MANAGE_REAL_AUTH_MAPPING)
	public String pageQuery(@ModelAttribute(StringUtil.QO) RealAuthQo qo, Model model) {
		model.addAttribute(StringUtil.PAGE_RESULT, realAuthService.pageQuery(qo));
		model.addAttribute(StringUtil.ITEMS,
				SystemDictionaryUtil.getItems(SystemDictionaryUtil.AUDIT, systemDictionaryHashService));
		return REALAUTH_LIST;
	}

	@PostMapping(MANAGE_REAL_AUTH_AUDIT_MAPPING)
	@ResponseBody
	public BaseResult audit(Long id, Integer auditStatus, String remark) {
		realAuthService.autid(id, auditStatus, remark);
		return BaseResult.ok(StringUtil.EMPTY);
	}
}
