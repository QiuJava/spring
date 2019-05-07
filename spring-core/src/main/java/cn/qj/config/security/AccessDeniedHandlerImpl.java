package cn.qj.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

/**
 * 没权限处理
 * 
 * @author Qiujian
 * @date 2019年4月9日
 *
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

	private static final Logger logger = LoggerFactory.getLogger(AccessDeniedHandlerImpl.class);

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			logger.warn(auth.getName() + "没有权限访问:" + request.getRequestURI());
		}
		response.sendRedirect(request.getContextPath() + "/403");
	}

}
