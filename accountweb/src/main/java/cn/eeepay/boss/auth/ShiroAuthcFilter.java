//package cn.eeepay.boss.auth;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.shiro.authc.AuthenticationToken;
//import org.apache.shiro.subject.Subject;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.apache.shiro.web.util.WebUtils;
//import org.springframework.stereotype.Component;
//
//
///**
// * 表单认证过滤器，可以在这里记录登录成功后的日志记录等操作
// * 
// * @author dj
// * 
// */
//@Component("authc")
//public class ShiroAuthcFilter extends FormAuthenticationFilter {
//	// 登录成功操作,这里设置了代理商常用信息
//	@Override
//	protected boolean onLoginSuccess(AuthenticationToken token,
//			Subject subject, ServletRequest request, ServletResponse response)
//			throws Exception {
////		BossUser bossUser = (BossUser) SecurityUtils.getSubject().getPrincipal();
//		HttpSession session = WebUtils.toHttp(request).getSession(true);
//
//		Map<String, String> param = new HashMap<String, String>();
//		WebUtils.issueRedirect(request, response, getSuccessUrl(), param, true);
//		// save log
//		String ip = request.getRemoteAddr();
//		return false;
//	}
//}
