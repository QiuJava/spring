package cn.qj.config.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

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
		if (RequestMethod.POST.name().endsWith(method)) {
			request.setAttribute("msg", accessDeniedException.getMessage());
			request.getRequestDispatcher("/noPermission").forward(request, response);
		} else {
			response.sendRedirect(request.getContextPath() + "/403");
		}
	}

}
