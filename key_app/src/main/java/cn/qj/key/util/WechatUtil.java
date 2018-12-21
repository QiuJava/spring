package cn.qj.key.util;

import lombok.Getter;
import lombok.Setter;

/**
 * 微信工具类
 * 
 * @author Qiujian
 * @date 2018/12/20
 */
public class WechatUtil {
	private WechatUtil() {
	}

	public static final String TOKEN = "123456";
	public static final String WECHAT_MSG_TYPE_TEXT = "text";
	public static final String WECHAT_MSG_TYPE_IMAGE = "image";
	public static final String WECHAT_MSG_TYPE_EVENT = "event";
	public static final String WECHAT_MSG_TYPE_NEWS = "news";
	
	public static final String WECHAT_EVENT_TYPE_SUBSCRIBE = "subscribe";
	public static final String WECHAT_EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public static final String WECHAT_EVENT_TYPE_CLICK = "click";

	public static String appID = "wx9d44774f8d0c066f";
	public static String appsecret = "d091cea7572b2c7e75218d3139bfee45";

	// 缓存的access_token
	@Getter
	@Setter
	private static String accessToken;
	// access_token的失效时间
	@Getter
	@Setter
	private static long expiresTime;

	public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
			+ appID + "&secret=" + appsecret;
	public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";
	
	public static final String MENU_JSON = "{" + 
			"     \"button\":[" + 
			"     {    " + 
			"          \"type\":\"click\"," + 
			"          \"name\":\"开班信息\"," + 
			"          \"key\":\"classinfo\"" + 
			"      }," + 
			"     {    " + 
			"          \"type\":\"click\"," + 
			"          \"name\":\"校区地址\"," + 
			"          \"key\":\"address\"" + 
			"      }," + 
			"      {" + 
			"           \"name\":\"学科介绍\"," + 
			"           \"sub_button\":[" + 
			"           {    " + 
			"               \"type\":\"view\"," + 
			"               \"name\":\"Java课程\"," + 
			"               \"url\":\"http://www.wolfcode.cn/zt/java/index.html\"" + 
			"            }," + 
			"           {    " + 
			"               \"type\":\"view\"," + 
			"               \"name\":\"Python课程\"," + 
			"               \"url\":\"http://www.wolfcode.cn/zt/python/index.html\"" + 
			"            }]" + 
			"       }]" + 
			" }";

}
