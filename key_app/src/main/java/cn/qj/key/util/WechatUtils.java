package cn.qj.key.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties("wechat")
public class WechatUtils {
	private static final Logger LOG = LoggerFactory.getLogger(WechatUtils.class);
	public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static String accessToken;
	private static long expiresIn;

	@Autowired
	private RestTemplate restTemplate;

	private String appId;
	private String appsecret;

	public void getAccessToken() {
		if (accessToken == null || System.currentTimeMillis() > expiresIn) {
			String body = restTemplate
					.getForEntity(ACCESS_TOKEN_URL.replace("appid", appId).replace("secret", appsecret), String.class)
					.getBody();
			LOG.info("获取微信access_token返回:{}", body);
			JSONObject json = JSON.parseObject(body);
			accessToken = (String) json.get("access_token");
			Integer expires = (Integer) json.get("expires_in");
			expiresIn = System.currentTimeMillis() + expires;
		}
	}

}
