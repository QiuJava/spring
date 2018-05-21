package cn.pay.loan.web.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.pay.core.domain.business.UserFile;
import cn.pay.core.service.SystemDictionaryItemService;
import cn.pay.core.service.UserFileService;
import cn.pay.core.util.HttpSessionContext;
import cn.pay.core.util.UploadUtil;
import lombok.Setter;

/**
 * 用户风控材料相关
 * 
 * @author Administrator
 *
 */
@Controller
@ConfigurationProperties(prefix = "upload.path")
public class UserFileController {
	@Autowired
	private UserFileService service;
	@Autowired
	private SystemDictionaryItemService systemDictionaryItemService;

	@Setter
	private String img;

	@RequestMapping("/userFile")
	public String userFile(Model model, HttpSession session) {
		// 判断没有选择类型的userFile对象
		Long id = HttpSessionContext.getCurrentLoginInfo().getId();
		List<UserFile> files = service.listByUser(id, false);
		if (files.size() == 0) {
			// 放到model中 跳转到提交材料对象界面
			files = service.listByUser(id, true);
			model.addAttribute("userFiles", files);
			model.addAttribute("sessionid", session.getId());
			return "userFiles";
		}
		// 在model中添加材料类型
		model.addAttribute("fileTypes", systemDictionaryItemService.getBySn("userFileType"));
		model.addAttribute("userFiles", files);
		// 否者跳到添加上传材料和显示有类型的userFile对象
		return "userFiles_commit";
	}

	/**
	 * 绑定userFile对象的类型
	 * 
	 * @param ids
	 *            材料对象的id数组
	 * @param typeIds
	 *            材料类型id的数组
	 * @return
	 */
	@RequestMapping("/userFile/updateType")
	public String updateType(Long[] id, Long[] fileType) {
		service.updateType(id, fileType);
		return "redirect:/userFile.do";
	}

	@RequestMapping("/userFile/upload")
	@ResponseBody
	public void upload(MultipartFile file) throws IOException {
		String fileName = "";
		fileName = UploadUtil.upload(file, img);
		service.apply(fileName);

	}
}
