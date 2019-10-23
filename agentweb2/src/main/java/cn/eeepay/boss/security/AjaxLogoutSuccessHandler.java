package cn.eeepay.boss.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler implements LogoutSuccessHandler {

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		String url;
		if (ClientTeamIdUtil.DEFAULT_TEAM_ID.equals(ClientTeamIdUtil.getClientTeamId(request))) {
			url = "/login.do?logout";
		} else {
			url = "/oemlogin.do?logout";
		}
		return url;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		super.handle(request, response, authentication);
	}
}
