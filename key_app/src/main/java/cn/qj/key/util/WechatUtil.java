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

	/**
	 * 微信接入时的凭据
	 */
	public static final String WECHAT_TOKEN = "123456";

	/**
	 * 微信消息类型
	 */
	public static final String WECHAT_MSG_TYPE_TEXT = "text";
	public static final String WECHAT_MSG_TYPE_IMAGE = "image";
	public static final String WECHAT_MSG_TYPE_EVENT = "event";
	public static final String WECHAT_MSG_TYPE_NEWS = "news";

	/**
	 * 微信时间类型
	 */
	public static final String WECHAT_EVENT_TYPE_SUBSCRIBE = "subscribe";
	public static final String WECHAT_EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	public static final String WECHAT_EVENT_TYPE_CLICK = "click";

	/**
	 * 测试号信息
	 */
	public static String appID = "wx9d44774f8d0c066f";
	public static String appsecret = "d091cea7572b2c7e75218d3139bfee45";

	/**
	 * 缓存 基本 access_token
	 */
	@Getter
	@Setter
	private static String accessToken;
	/**
	 * 缓存基本access_token的失效时间
	 */
	@Getter
	@Setter
	private static long expiresTime;

	@Getter
	@Setter
	private static String webAccessToken;
	@Getter
	@Setter
	private static long webExpiresTime;
	@Getter
	@Setter
	private static String webRefreshToken;
	@Getter
	@Setter
	private static String webOpenid;

	@Getter
	@Setter
	private static String ticket;
	@Getter
	@Setter
	private static long ticketExpiresTime;

	public static final String CREATE_MENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?" + "access_token=";
	public static final String SEND_TEMPLATE_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?"
			+ "access_token=";

	public static final String GET_ACCESSTOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?"
			+ "grant_type=client_credential" + "&appid=" + appID + "&secret=" + appsecret;

	/**
	 * 微信用户授权页面
	 */
	public static final String WECHAT_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "appid="
			+ appID + "&redirect_uri=http%3A%2F%2Fqjwxkf.nat300.top%2Fwechat%2FwechatUserInfo"
			+ "&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	public static final String GET_WEB_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
			+ "appid=APPID" + "&secret=SECRET" + "&code=CODE" + "&grant_type=authorization_code";

	public static final String GET_WECHAT_USERINFO_CALLBACK_URL = "http://mdpd2k.natappfree.cc/wechat/getWechatUserInfo";

	public static final String GET_WECHAT_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?"
			+ "access_token=ACCESS_TOKEN" + "&openid=OPENID" + "&lang=zh_CN";

	/**
	 * 获取页面使用JS SDK的凭据地址
	 */
	public static final String GET_TICHET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
			+ "access_token=ACCESS_TOKEN" + "&type=jsapi";

	public static final String MENU_JSON = "{" + "    \"button\": [" + "        {" + "            \"type\": \"click\","
			+ "            \"name\": \"开班信息\"," + "            \"key\": \"classinfo\"" + "        }," + "        {"
			+ "            \"type\": \"click\"," + "            \"name\": \"校区地址\","
			+ "            \"key\": \"address\"" + "        }," + "        {" + "            \"name\": \"学科介绍\","
			+ "            \"sub_button\": [" + "                {" + "                    \"type\": \"view\","
			+ "                    \"name\": \"Java课程\","
			+ "                    \"url\": \"http://www.wolfcode.cn/zt/java/index.html\"" + "                },"
			+ "                {" + "                    \"type\": \"view\","
			+ "                    \"name\": \"Python课程\","
			+ "                    \"url\": \"http://www.wolfcode.cn/zt/python/index.html\"" + "                }"
			+ "            ]" + "        }" + "    ]" + "";

	public static final String SEND_TEMPLATE_MSG_DATA = "{" + "    \"touser\": \"oj1sP08P3mSnZ0qsyCkaq7nVQZ6M\","
			+ "    \"template_id\": \"Bp72jjth0hjtXI5zw0zdOAWHMt42oTiY9cldVMyRiKI\","
			+ "    \"url\": \"http://www.baidu.com\"," + "    \"data\": {" + "        \"first\": {"
			+ "            \"value\": \"恭喜你购买成功！\"," + "            \"color\": \"#173177\"" + "        },"
			+ "        \"goodsName\": {" + "            \"value\": \"巧克力\"," + "            \"color\": \"#173177\""
			+ "        }," + "        \"payAmount\": {" + "            \"value\": \"39.8元\","
			+ "            \"color\": \"#173177\"" + "        }," + "        \"orderTime\": {"
			+ "            \"value\": \"2014年9月22日\"," + "            \"color\": \"#173177\"" + "        },"
			+ "        \"remark\": {" + "            \"value\": \"欢迎再次购买！\"," + "            \"color\": \"#173177\""
			+ "        }" + "    }" + "}";

}
