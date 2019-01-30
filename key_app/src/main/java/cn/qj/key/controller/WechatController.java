package cn.qj.key.controller;

import java.util.Arrays;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.entity.WechatReplyMsg;
import cn.qj.key.service.WechatService;
import cn.qj.key.util.BaseResult;
import cn.qj.key.util.StrUtil;
import cn.qj.key.util.WechatUtil;
import cn.qj.key.util.XmlUtil;

/**
 * 微信业务控制器
 * 
 * @author Qiujian
 * @date 2018/12/19
 */
@RestController
public class WechatController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WechatService wechatService;

	/**
	 * 创建公众号菜单
	 * 
	 * @return
	 */
	@PostMapping("/wechat/createMenu")
	public void createMenu() {
		wechatService.createMenu();
	}

	/**
	 * 微信接入校验
	 * 
	 * @return
	 */
	@GetMapping("/wechat")
	public String wechat(String signature, String timestamp, String nonce, String echostr) {
		if (StrUtil.isEmpty(signature) || StrUtil.isEmpty(timestamp) || StrUtil.isEmpty(nonce)
				|| StrUtil.isEmpty(echostr)) {
			return "";
		}
		String[] strArr = new String[] { WechatUtil.TOKEN, timestamp, nonce };
		Arrays.sort(strArr);
		StringBuilder sb = new StringBuilder();
		for (String str : strArr) {
			sb.append(str);
		}
		// sha1 加密
		String signatureReuslt = DigestUtils.sha1Hex(sb.toString());
		if (signature.equals(signatureReuslt)) {
			log.info("微信接入成功");
			return echostr;
		}
		log.info("微信接入失败");
		return "";
	}

	/**
	 * 接收普通消息
	 * 
	 * @return
	 */
	@PostMapping("/wechat")
	public String accept(@RequestBody WechatAcceptMsg msg) {
		WechatReplyMsg replyMsg = wechatService.replyMsg(msg);
		return XmlUtil.toXml(replyMsg);

	}

	/**
	 * 发送模板消息
	 * 
	 * @param data
	 * @return
	 */
	@PostMapping("/wechat/sendTemplateMsg")
	public String sendTemplateMsg(String data) {
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
		JSONObject json = wechatService.getWechatUserInfo();
		return BaseResult.ok("获取用户成功", json);
	}

	/**
	 * 获取JS SDK 签名
	 * 
	 * @return
	 */
	@GetMapping("/wechat/jssdkSignature")
	public void jssdkSignature() {
		wechatService.getTicket();
	}

}
