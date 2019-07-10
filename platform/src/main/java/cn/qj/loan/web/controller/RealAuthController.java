package cn.qj.loan.web.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.qj.core.common.BaseResult;
import cn.qj.core.entity.RealAuth;
import cn.qj.core.entity.UserInfo;
import cn.qj.core.service.RealAuthService;
import cn.qj.core.service.UserInfoService;
import cn.qj.core.util.HttpSessionUtil;
import cn.qj.core.util.UploadUtil;
import lombok.Setter;

/**
 * 实名认证控制器
 * 
 * @author Qiujian
 * @date 2018/11/01
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
		UserInfo userInfo = userInfoService.get(HttpSessionUtil.getCurrentLoginInfo().getId());
		// 如果用户已经实名认证直接跳到结果
		if (userInfo.getIsRealAuth()) {
			// 查询出实名认证的信息并放到Model中
			RealAuth realAuth = service.get(userInfo.getRealAuthId());
			model.addAttribute("realAuth", realAuth);
			return "realAuth_result";
		} else {
			if (userInfo.getRealAuthId() > 0) {
				model.addAttribute("auditing", true);
				return "realAuth_result";
			}
		}
		// 如果用户没有进行实名认证 则跳到实名认证界面
		return "realAuth";
	}

	/**
	 * 身份证正反面上传
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
	@RequestMapping("/realAuth/save")
	@ResponseBody
	public BaseResult save(RealAuth realAuth) {
		service.save(realAuth);
		return BaseResult.ok("实名认证信息保存成功");
	}
}
