package cn.eeepay.boss.security;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.eeepay.framework.model.bill.SysMenu;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.RedisService;
import cn.eeepay.framework.service.bill.SysMenuService;
import cn.eeepay.framework.util.Constants;

public class SessionInterceptor implements HandlerInterceptor {

	@Resource
	public SysMenuService sysMenuService;
	@Resource
	private RedisService redisService;
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
			Object arg2) throws Exception {
		if(SecurityContextHolder.getContext().getAuthentication()== null ||
				SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
			String requestType = request.getHeader("X-Requested-With");  
			if (!StringUtils.isEmpty(requestType) && requestType.equalsIgnoreCase("XMLHttpRequest")) {  
				response.setHeader("optStatus", "expired");  
				response.sendError(518, "会话已过期.");  
			    return false;  
			}  
			response.sendRedirect(request.getContextPath()+"/login.do?expired");
			return false;
		}
		String reqUrl = request.getServletPath();
		String menuUrl = StringUtils.removeStart(reqUrl, "/");
		SysMenu sysMenu = sysMenuService.findSysMenuByMenuUrlWithMenu(menuUrl);
		if (sysMenu != null) {
			String menuCode = sysMenu.getMenuCode();
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				    .getAuthentication()
				    .getPrincipal();
			Integer userId = userInfo.getUserId();
			String key = Constants.user_last_menu_code + ":" + userId;
			redisService.insertString(key, menuCode);
		}
		return true;
	}



}