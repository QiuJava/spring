package cn.eeepay.boss.security;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * 客户端组织ID工具类。将组织ID写到客户端，以避免session过期后组织ID信息丢失
 * 
 * @author xyf1
 *
 */
public class ClientTeamIdUtil {
	/**
	 * session中的组织ID字段名
	 */
	public static final String SESSION_ATTRIBUTE_TEAM_ID = "teamId";
	/**
	 * 缺省组织ID
	 */
	public static final String DEFAULT_TEAM_ID = "100010";
	/**
	 * 客户端cookie的组织ID字段名
	 */
	public static final String CLIENT_FIELD_TEAM_ID = "clientTeamId";
	public static final String CLIENT_FIELD_OEM_TYPE = "oemType";

	/**
	 * 获取客户端的组织ID
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientTeamId(HttpServletRequest request) {
		for (Cookie cookie : request.getCookies()) {
			if (CLIENT_FIELD_TEAM_ID.equals(cookie.getName()))
				return cookie.getValue();
		}
		return DEFAULT_TEAM_ID;
	}

	/**
	 * 判断客户端是否缺省组织ID
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isClientDefaultTeam(HttpServletRequest request) {
		return DEFAULT_TEAM_ID.equals(getClientTeamId(request));
	}

	/**
	 * 将组织ID写入客户端cookie
	 * 
	 * @param session
	 *            将从session中获取组织ID
	 * @param response
	 */
	public static void saveTeamIdToCookie(HttpSession session, HttpServletResponse response) {
		final Cookie cookie = new Cookie(ClientTeamIdUtil.CLIENT_FIELD_TEAM_ID,
				(String) session.getAttribute(SESSION_ATTRIBUTE_TEAM_ID));
		// 客户端需要获取“clientTeamId”这个cookie，设置httpOnly=false
		cookie.setHttpOnly(false);
		cookie.setSecure(true);
		response.addCookie(cookie);
	}

	/**
	 * 从session和客户端中获取组织ID
	 * 
	 * @param request
	 * @param session
	 */
	public static void findTeamIdFromSessionAndClient(HttpServletRequest request, HttpSession session) {
		// 为了解决session失效或丢失后，可能无法通过session获取teamId，导致oem的变为直营的，将teamId保存在cookie中。
		if (StringUtils.isBlank((String) session.getAttribute(SESSION_ATTRIBUTE_TEAM_ID))) {
			for (Cookie item : request.getCookies()) {
				if (CLIENT_FIELD_TEAM_ID.equals(item.getName())) {
					final String val = item.getValue();
					if (StringUtils.isNotBlank(val)) {
						session.setAttribute(SESSION_ATTRIBUTE_TEAM_ID, val);
					}
					break;
				}
			}
			if (StringUtils.isBlank((String) session.getAttribute(SESSION_ATTRIBUTE_TEAM_ID)))
				session.setAttribute(SESSION_ATTRIBUTE_TEAM_ID, ClientTeamIdUtil.DEFAULT_TEAM_ID);
		}
	}

	/**
	 * 设置session中的组织ID
	 * 
	 * @param session
	 * @param teamId
	 */
	public static void setSessionTeamId(HttpSession session, String teamId) {
		session.setAttribute(SESSION_ATTRIBUTE_TEAM_ID, teamId);
	}
}
