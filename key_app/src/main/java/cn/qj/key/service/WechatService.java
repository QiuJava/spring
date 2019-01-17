package cn.qj.key.service;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.qj.key.bean.WechatVerify;
import cn.qj.key.entity.ArticlesItem;
import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.entity.WechatReplyMsg;
import cn.qj.key.util.WechatUtil;

/**
 * 微信业务处理
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Service
public class WechatService {

	private static Logger log = LoggerFactory.getLogger(WechatService.class);

	@Autowired
	private RestTemplate restTemplate;

	public String verify(WechatVerify wechatVerify) {
		// 1）将token、timestamp、nonce三个参数进行字典序排序 2）将三个参数字符串拼接成一个字符串进行sha1加密
		// 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		String[] strArr = new String[] { WechatUtil.WECHAT_TOKEN, wechatVerify.getTimestamp(),
				wechatVerify.getNonce() };
		Arrays.sort(strArr);
		StringBuilder sb = new StringBuilder();
		for (String str : strArr) {
			sb.append(str);
		}
		// sha1 加密
		String signature = DigestUtils.sha1Hex(sb.toString());
		if (signature.equals(wechatVerify.getSignature())) {
			log.info("微信接入成功");
			return wechatVerify.getEchostr();
		}
		log.info("微信接入失败");
		return null;
	}

	public WechatReplyMsg replyMsg(WechatAcceptMsg msg) {
		// 根据MsgId进行消息排重
		WechatReplyMsg replyMsg = new WechatReplyMsg();
		replyMsg.setToUserName(msg.getFromUserName());
		replyMsg.setFromUserName(msg.getToUserName());
		replyMsg.setCreateTime(System.currentTimeMillis());

		if (WechatUtil.WECHAT_MSG_TYPE_TEXT.equals(msg.getMsgType())) {
			replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_TEXT);

			// 模拟关键字回复
			String content = null;
			if (msg.getContent().contains("吃")) {
				content = "早餐喝粥\n中餐吃饭\n晚餐吃饭";
			} else if (msg.getContent().contains("喝")) {
				content = "早上喝牛奶\n中午喝益力多\n晚上喝水";
			} else {
				content = msg.getContent();
			}
			replyMsg.setContent(content);
		} else if (WechatUtil.WECHAT_MSG_TYPE_IMAGE.equals(msg.getMsgType())) {
			replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_IMAGE);
			replyMsg.setMediaId(new String[] { msg.getMediaId() });
		} else if (WechatUtil.WECHAT_MSG_TYPE_EVENT.equals(msg.getMsgType())) {
			if (WechatUtil.WECHAT_EVENT_TYPE_SUBSCRIBE.equals(msg.getEvent())) {
				// replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_TEXT);
				// replyMsg.setContent("感谢关注！[爱心]");
				replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_NEWS);
				replyMsg.setArticleCount(1);

				ArticlesItem item = new ArticlesItem();
				item.setPicUrl("http://www.wolfcode.cn/data/upload/20181122/5bf666c6b9b61.jpg");
				item.setTitle("学习资料");
				item.setDescription("学习资料");
				item.setUrl("http://www.baidu.com");
				replyMsg.setItem(new ArticlesItem[] { item });
			} else if (WechatUtil.WECHAT_EVENT_TYPE_UNSUBSCRIBE.equals(msg.getEvent())) {
				log.info("已取消关注！");
			} else if (WechatUtil.WECHAT_EVENT_TYPE_CLICK.equalsIgnoreCase(msg.getEvent())) {
				if ("classinfo".equals(msg.getEventKey())) {
					replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_TEXT);
					replyMsg.setContent("北京java一期\n上海java二期 \n深圳java三期");
				} else if ("address".equals(msg.getEventKey())) {
					replyMsg.setMsgType(WechatUtil.WECHAT_MSG_TYPE_TEXT);
					replyMsg.setContent("北京\n上海\n深圳");
				}
			}
		}
		return replyMsg;
	}

	public void getAccessToken() {
		if (WechatUtil.getAccessToken() == null || System.currentTimeMillis() > WechatUtil.getExpiresTime()) {
			String body = restTemplate.getForEntity(WechatUtil.GET_ACCESSTOKEN_URL, String.class).getBody();
			log.info("获取微信access_token返回:{}", body);
			JSONObject json = JSON.parseObject(body);
			WechatUtil.setAccessToken((String) json.get("access_token"));
			Integer expires = (Integer) json.get("expires_in");
			WechatUtil.setExpiresTime(System.currentTimeMillis() + (expires - 3600) * 1000);
		}
		System.out.println("accessToken:" + WechatUtil.getAccessToken());
	}

	public String createMenu() {
		return restTemplate.postForEntity(WechatUtil.CREATE_MENU_URL + WechatUtil.getAccessToken(),
				WechatUtil.MENU_JSON, String.class).getBody();
	}

	public void sendTemplateMsg(String data) {
		String result = restTemplate.postForObject(WechatUtil.SEND_TEMPLATE_MSG_URL + WechatUtil.getAccessToken(), data,
				String.class);
		System.out.println(result);
	}

	public void getWebAccessToken(String code) {
		if (WechatUtil.getWebAccessToken() == null || System.currentTimeMillis() > WechatUtil.getWebExpiresTime()) {
			String url = WechatUtil.GET_WEB_ACCESSTOKEN_URL.replace("APPID", WechatUtil.appID);
			url = url.replace("SECRET", WechatUtil.appsecret);
			url = url.replace("CODE", code);
			String body = restTemplate.getForEntity(url, String.class).getBody();
			log.info("获取微信web_access_token返回:{}", body);
			JSONObject json = JSON.parseObject(body);
			WechatUtil.setWebAccessToken((String) json.get("access_token"));
			Integer expires = (Integer) json.get("expires_in");
			WechatUtil.setWebExpiresTime(System.currentTimeMillis() + (expires - 3600) * 1000);
			WechatUtil.setWebRefreshToken((String) json.get("refresh_token"));
			WechatUtil.setWebOpenid((String) json.get("openid"));
		}
		System.out.println("webAccessToken:" + WechatUtil.getWebAccessToken());
	}

	public JSONObject getWechatUserInfo() {
		String url = WechatUtil.GET_WECHAT_USERINFO_URL.replace("ACCESS_TOKEN", WechatUtil.getWebAccessToken());
		url = url.replace("OPENID", WechatUtil.getWebOpenid());
		String result = restTemplate.getForEntity(url, String.class).getBody();
		return JSON.parseObject(result);
	}
	
	/**
	 * 获取调用微信js的临时票据
	 */
	public void getTicket() {
		if (WechatUtil.getTicket() == null || System.currentTimeMillis() > WechatUtil.getTicketExpiresTime()) {
			String url = WechatUtil.GET_TICHET_URL;
			url = url.replace("ACCESS_TOKEN", WechatUtil.getAccessToken());
			String body = restTemplate.getForEntity(url, String.class).getBody();
			log.info("获取微信ticket:{}", body);
			JSONObject json = JSON.parseObject(body);
			WechatUtil.setTicket((String) json.get("ticket"));
			Integer expires = (Integer) json.get("expires_in");
			WechatUtil.setTicketExpiresTime(System.currentTimeMillis() + (expires - 3600) * 1000);
		}
		System.out.println("ticket:" + WechatUtil.getTicket());

	}
	
	public void getJssdkSignature() {
		
	}
	
	/**
	 * 计算jssdk-config的签名
	 * 
	 * @param jsapi_ticket
	 * @param timestamp
	 * @param noncestr
	 * @param url
	 * @return
	 */
	public String getSignature(String jsapi_ticket, Long timestamp, String noncestr, String url) {
		// 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）
		Map<String, Object> map = new TreeMap<>();
		map.put("jsapi_ticket", jsapi_ticket);
		map.put("timestamp", timestamp);
		map.put("noncestr", noncestr);
		map.put("url", url);
		// 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串string1
		StringBuilder sb = new StringBuilder();
		Set<String> set = map.keySet();
		for (String key : set) {
			sb.append(key + "=" + map.get(key)).append("&");
		}
		// 去掉最后一个&符号
		String temp = sb.substring(0, sb.length() - 1);
		// 使用sha1加密
		String sha1Hex = DigestUtils.sha1Hex(temp);
		return sha1Hex;
	}

}
