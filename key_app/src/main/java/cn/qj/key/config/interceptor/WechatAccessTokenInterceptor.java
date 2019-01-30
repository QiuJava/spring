package cn.qj.key.config.interceptor;

import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.qj.key.util.WechatUtil;

/**
 * 微信认证token检查拦截
 * 
 * @author Qiujian
 * @date 2019/01/30
 */
@Component
public class WechatAccessTokenInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ValueOperations<String, Object> valueOperations;
	@Autowired
	private RestTemplate restTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String token = (String) valueOperations.get(WechatUtil.ACCESS_TOKEN);
		if (token == null) {
			String body = restTemplate.getForEntity(WechatUtil.GET_ACCESS_TOKEN.replace("APPID", WechatUtil.APPID)
					.replace("APPSECRET", WechatUtil.APPSECRET), String.class).getBody();
			JSONObject json = JSON.parseObject(body);
			valueOperations.set(WechatUtil.ACCESS_TOKEN, json.get(WechatUtil.ACCESS_TOKEN),
					(Integer) json.get("expires_in"), TimeUnit.SECONDS);
		}
		return true;
	}
	
}
