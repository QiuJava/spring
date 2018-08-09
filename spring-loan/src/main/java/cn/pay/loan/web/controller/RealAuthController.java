package cn.pay.loan.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.pay.core.domain.business.RealAuth;
import cn.pay.core.domain.business.UserInfo;
import cn.pay.core.pojo.vo.AjaxResult;
import cn.pay.core.service.RealAuthService;
import cn.pay.core.service.UserInfoService;
import cn.pay.core.util.HttpServletContext;
import cn.pay.core.util.UploadUtil;
import lombok.Setter;

/**
 * 实名认证相关
 * 
 * @author Administrator
 *
 */
@Controller
@ConfigurationProperties(prefix = "upload.path")
public class RealAuthController {
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private RealAuthService service;

	@Setter
	private String img;

	/**
	 * 跳转到认证界面 往网页面中注入需要的数据
	 */
	@RequestMapping("/realAuth")
	public String realAuth(Model model) {
		// 得到当前用户信息
		UserInfo userInfo = userInfoService.get(HttpServletContext.getCurrentLoginInfo().getId());
		// 如果用户已经实名认证直接跳到结果
		if (userInfo.getIsRealAuth()) {
			// 查询出实名认证的信息并放到Model中
			RealAuth realAuth = service.get(userInfo.getRealAuthId());
			model.addAttribute("realAuth", realAuth);
			return "realAuth_result";
		} else {
			if (userInfo.getRealAuthId() != null) {
				model.addAttribute("auditing", true);
				return "realAuth_result";
			}
		}
		// 如果用户没有进行实名认证 则跳到实名认证界面
		return "realAuth";
	}

	/**
	 * 处理文件上传
	 */
	@RequestMapping("/realAuth/upload")
	@ResponseBody
	public String upload(MultipartFile file) {
		String fileName = "";
		try {
			fileName = UploadUtil.upload(file, img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * 保存实名认证信息
	 */
	@RequestMapping("realAuth/save")
	@ResponseBody
	public AjaxResult save(RealAuth realAuth) {
		service.save(realAuth);
		AjaxResult result = new AjaxResult();
		result.setSuccess(true);
		return result;
	}
}
