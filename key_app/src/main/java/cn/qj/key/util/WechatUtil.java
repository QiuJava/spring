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
	public static final String TOKEN = "123456";

	/**
	 * 微信消息类型
	 */
	public static final String TEXT = "text";
	public static final String IMAGE = "image";
	public static final String EVENT = "event";
	public static final String NEWS = "news";

	/**
	 * 微信时间类型类型
	 */
	public static final String SUBSCRIBE = "subscribe";
	public static final String UNSUBSCRIBE = "unsubscribe";
	public static final String CLICK = "click";

	/**
	 * 测试号信息
	 */
	public static final String APPID = "wx9d44774f8d0c066f";
	public static final String APPSECRET = "d091cea7572b2c7e75218d3139bfee45";

	public static final String ACCESS_TOKEN = "access_token";

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

	public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&APPID=APPID&secret=APPSECRET";
	public static final String POST_CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	public static final String GET_DELETE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	public static final String POST_TEMPLATE_MSG = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";

	public static final String SEND_TEMPLATE_MSG_DATA = "{\r\n"
			+ "    \"touser\": \"oj1sP03WKuKxKq4Q10LkKwNNVCMI\",\r\n"
			+ "    \"template_id\": \"Bp72jjth0hjtXI5zw0zdOAWHMt42oTiY9cldVMyRiKI\",\r\n"
			+ "    \"url\": \"http://www.baidu.com\",\r\n" + "    \"data\": {\r\n" + "        \"first\": {\r\n"
			+ "            \"value\": \"恭喜你购买成功！\",\r\n" + "            \"color\": \"#173177\"\r\n" + "        },\r\n"
			+ "        \"goodsName\": {\r\n" + "            \"value\": \"巧克力\",\r\n"
			+ "            \"color\": \"#173177\"\r\n" + "        },\r\n" + "        \"payAmount\": {\r\n"
			+ "            \"value\": \"39.8元\",\r\n" + "            \"color\": \"#173177\"\r\n" + "        },\r\n"
			+ "        \"orderTime\": {\r\n" + "            \"value\": \"2014年9月22日\",\r\n"
			+ "            \"color\": \"#173177\"\r\n" + "        },\r\n" + "        \"remark\": {\r\n"
			+ "            \"value\": \"欢迎再次购买！\",\r\n" + "            \"color\": \"#173177\"\r\n" + "        }\r\n"
			+ "    }\r\n" + "}";

	/**
	 * 微信用户授权页面
	 */
	public static final String WECHAT_AUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?" + "APPID="
			+ APPID
			+ "&redirect_uri=http%3A%2F%2Fqjwxkf.nat300.top%2Fwechat%2FwechatUserInfo&response_type=code&scope=snsapi_userinfo#wechat_redirect";
	public static final String GET_WEB_ACCESSTOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?"
			+ "APPID=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";

	public static final String GET_WECHAT_USERINFO_CALLBACK_URL = "http://mdpd2k.natappfree.cc/wechat/getWechatUserInfo";

	public static final String GET_WECHAT_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?"
			+ "access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

	/**
	 * 获取页面使用JS SDK的凭据地址
	 */
	public static final String GET_TICHET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?"
			+ "access_token=ACCESS_TOKEN&type=jsapi";

}
