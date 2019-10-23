package cn.eeepay.boss.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AjaxAuthenticationFailureHandler extends AbstractAuthenticationTargetUrlRequestHandler  implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		handle(request, response, null);
	}
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		String url;
		Object error = request.getAttribute("error");
		Object lockTime = request.getAttribute("lockTime");
		String lock = "";
		if (lockTime != null) {
			lock = "&lock=" + lockTime;
		}
		if (ClientTeamIdUtil.DEFAULT_TEAM_ID.equals(ClientTeamIdUtil.getClientTeamId(request))) {
			url = "/oemlogin.do?error=" + ObjectUtils.toString(error) + lock;
		} else {
			url = "/oemlogin.do?error=" + ObjectUtils.toString(error) + lock;
		}
		logger.info("determineTargetUrl = " +  url);
		return url;
	}

}
