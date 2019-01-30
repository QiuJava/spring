package cn.qj.key.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.qj.key.entity.WechatAcceptMsg;
import cn.qj.key.entity.WechatArticle;
import cn.qj.key.entity.WechatReplyMsg;
import cn.qj.key.entity.WechatUser;
import cn.qj.key.util.DateTimeUtil;
import cn.qj.key.util.FileUtil;
import cn.qj.key.util.LogicException;
import cn.qj.key.util.StrUtil;
import cn.qj.key.util.WechatResult;
import cn.qj.key.util.WechatUtil;

/**
 * 微信业务处理
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
@Service
public class WechatService {

	private static final Logger log = LoggerFactory.getLogger(WechatService.class);

	@Autowired
	private WechatUserService wechantUserService;

	@Autowired
	private DictService dictService;

	@Autowired
	private WechatArticleService wechatArticleService;

	@Autowired
	private WechatAcceptMsgService wechatAcceptMsgService;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Value("classpath:wechat_menu.json")
	private Resource wecahtMenu;

	public WechatReplyMsg replyMsg(WechatAcceptMsg msg) {
		msg.setCreateDateTime(DateTimeUtil.getDate(msg.getCreateTime() * 1000));

		// 根据MsgId进行消息排重
		if (WechatUtil.EVENT.equals(msg.getMsgType())) {
			msg.setMsgId(msg.getFromUserName() + msg.getCreateTime());
		}
		long count = wechatAcceptMsgService.countByMsgId(msg.getMsgId());
		if (count > 0) {
			return new WechatReplyMsg();
		}

		wechatAcceptMsgService.save(msg);
		WechatReplyMsg replyMsg = new WechatReplyMsg();

		replyMsg.setToUserName(msg.getFromUserName());
		replyMsg.setFromUserName(msg.getToUserName());
		replyMsg.setCreateTime(System.currentTimeMillis());

		if (WechatUtil.TEXT.equals(msg.getMsgType())) {
			replyMsg.setMsgType(WechatUtil.TEXT);
			// 模拟关键字回复
			String content = dictService.getByDictName(msg.getContent());
			if (StrUtil.isEmpty(content)) {
				content = msg.getContent();
			}
			replyMsg.setContent(content);
		} else if (WechatUtil.IMAGE.equals(msg.getMsgType())) {
			replyMsg.setMsgType(WechatUtil.TEXT);
			replyMsg.setContent("[上传成功]");
		} else if (WechatUtil.EVENT.equals(msg.getMsgType())) {

			if (WechatUtil.SUBSCRIBE.equals(msg.getEvent())) {
				List<WechatArticle> articles = wechatArticleService.getByStatus(WechatArticle.VALID);
				replyMsg.setMsgType(WechatUtil.NEWS);
				replyMsg.setArticleCount(articles.size());
				replyMsg.setItem(articles.toArray(new WechatArticle[articles.size()]));

				// 保存关注用户
				String wechatUserOpenId = msg.getFromUserName();
				WechatUser wechatUser = wechantUserService.getByOpenId(wechatUserOpenId);
				if (wechatUser == null) {
					wechatUser = new WechatUser();
					wechatUser.setOpenId(wechatUserOpenId);
					wechatUser.setCreateTime(DateTimeUtil.getDate());
				}
				wechatUser.setSubscribeType(WechatUser.SUBSCRIBE);
				wechatUser.setUpdateTime(DateTimeUtil.getDate());
				wechantUserService.save(wechatUser);

			} else if (WechatUtil.UNSUBSCRIBE.equals(msg.getEvent())) {
				String wechatUserOpenId = msg.getFromUserName();
				WechatUser wechatUser = wechantUserService.getByOpenId(wechatUserOpenId);
				wechatUser.setSubscribeType(WechatUser.UN_SUBSCRIBE);
				wechatUser.setUpdateTime(DateTimeUtil.getDate());
				wechantUserService.save(wechatUser);

			} else if (WechatUtil.CLICK.equalsIgnoreCase(msg.getEvent())) {
				String eventKey = msg.getEventKey();
				String byDictName = dictService.getByDictName(eventKey);
				replyMsg.setMsgType(WechatUtil.TEXT);
				if (StrUtil.isEmpty(byDictName)) {
					replyMsg.setContent("没有");
				} else {
					replyMsg.setContent(byDictName);
				}
			}
		}

		return replyMsg;
	}

	public void createMenu() {
		String jsonRead = "";
		try {
			jsonRead = FileUtil.jsonRead(wecahtMenu.getFile());
		} catch (IOException e) {
			throw new LogicException("Io 异常");
		}

		String token = (String) valueOperations.get(WechatUtil.ACCESS_TOKEN);
		String body = restTemplate
				.postForEntity(WechatUtil.POST_CREATE_MENU.replace("ACCESS_TOKEN", token), jsonRead, String.class)
				.getBody();
		WechatResult wechatResult = JSON.parseObject(body, WechatResult.class);
		if (!WechatResult.OK.equals(wechatResult.getErrcode())) {
			throw new LogicException(wechatResult.getErrmsg());
		}
	}

	public void sendTemplateMsg(String data) {
		String token = (String) valueOperations.get(WechatUtil.ACCESS_TOKEN);
		String postForObject = restTemplate.postForObject(WechatUtil.POST_TEMPLATE_MSG.replace("ACCESS_TOKEN", token),
				data, String.class);
		System.out.println(postForObject);
	}

	public void getWebAccessToken(String code) {
		if (WechatUtil.getWebAccessToken() == null || System.currentTimeMillis() > WechatUtil.getWebExpiresTime()) {
			String url = WechatUtil.GET_WEB_ACCESSTOKEN_URL.replace("APPID", WechatUtil.APPID);
			url = url.replace("SECRET", WechatUtil.APPSECRET);
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
		String token = (String) valueOperations.get(WechatUtil.ACCESS_TOKEN);
		if (WechatUtil.getTicket() == null || System.currentTimeMillis() > WechatUtil.getTicketExpiresTime()) {
			String url = WechatUtil.GET_TICHET_URL;
			url = url.replace("ACCESS_TOKEN", token);
			String body = restTemplate.getForEntity(url, String.class).getBody();
			log.info("获取微信ticket:{}", body);
			JSONObject json = JSON.parseObject(body);
			WechatUtil.setTicket((String) json.get("ticket"));
			Integer expires = (Integer) json.get("expires_in");
			WechatUtil.setTicketExpiresTime(System.currentTimeMillis() + (expires - 3600) * 1000);
		}
		System.out.println("ticket:" + WechatUtil.getTicket());

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
	public String getSignature(String jsapiTicket, Long timestamp, String noncestr, String url) {
		// 对所有待签名参数按照字段名的ASCII 码从小到大排序（字典序）
		Map<String, Object> map = new TreeMap<>();
		map.put("jsapi_ticket", jsapiTicket);
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
