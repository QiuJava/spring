package cn.qj.key.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import cn.qj.key.bean.WechatVerify;
import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.entity.WechatReplyMsg;
import cn.qj.key.service.WechatService;

/**
 * 微信接入控制器
 * 
 * @author Qiujian
 * @date 2018/12/19
 */
@RestController
public class WechatController {

	@Autowired
	private WechatService wechatService;

	@PostMapping("/wechat/getAccessToken")
	public String getAccessToken() {
		wechatService.getAccessToken();
		return "获取成功";
	}

	@PostMapping("/wechat/createMenu")
	public String createMenu() {
		return wechatService.createMenu();
	}

	/**
	 * 微信接入校验
	 * 
	 * @return
	 */
	@GetMapping("/wechat")
	public String wechat(WechatVerify wechatVerify) {
		return wechatService.verify(wechatVerify);
	}

	/**
	 * 接收普通消息
	 * 
	 * @return
	 */
	@PostMapping(name = "/wechat", produces = { "application/xml;charset=UTF-8" })
	public WechatReplyMsg accept(@RequestBody WechatAcceptMsg msg) {
		return wechatService.replyMsg(msg);
	}

}
