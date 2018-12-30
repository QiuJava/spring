package cn.qj.key.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.qj.key.bean.WechatVerify;
import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.entity.WechatReplyMsg;
import cn.qj.key.service.WechatService;
import cn.qj.key.util.BaseResult;
import cn.qj.key.util.WechatUtil;

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

	/**
	 * 创建公众号菜单
	 * 
	 * @return
	 */
	@PostMapping("/wechat/createMenu")
	public String createMenu() {
		wechatService.getAccessToken();
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

	/**
	 * 发送模板消息
	 * 
	 * @param data
	 * @return
	 */
	@PostMapping("/wechat/sendTemplateMsg")
	public String sendTemplateMsg(String data) {
		wechatService.getAccessToken();
		data = WechatUtil.SEND_TEMPLATE_MSG_DATA;
		wechatService.sendTemplateMsg(data);
		return "发送成功";
	}

	/**
	 * 获取微信用户基本信息
	 * 
	 * @return
	 */
	@GetMapping("/wechat/wechatUserInfo")
	public BaseResult wechatUserInfo(String code, String state) {
		System.out.println("code:" + code);
		// 根据code获取webaccess_token
		wechatService.getWebAccessToken(code);

		// 获取用户信息
		BaseResult result = new BaseResult();
		JSONObject json = wechatService.getWechatUserInfo();
		result.setData(json);
		result.setSuccess(true);
		return result;
	}

	/**
	 * 获取JS SDK 签名
	 * 
	 * @return
	 */
	@GetMapping("/wechat/jssdkSignature")
	public void jssdkSignature() {
		wechatService.getAccessToken();
		wechatService.getTicket();
		wechatService.getJssdkSignature();
	}

}
