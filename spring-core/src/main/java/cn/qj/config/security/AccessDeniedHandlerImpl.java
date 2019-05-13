package cn.qj.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
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

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String method = request.getMethod();
		if ("GET".endsWith(method)) {
			response.sendRedirect(request.getContextPath() + "/403");
		} else {
			request.setAttribute("msg", accessDeniedException.getMessage());
			request.getRequestDispatcher("/noPermission").forward(request, response);
		}
	}

}