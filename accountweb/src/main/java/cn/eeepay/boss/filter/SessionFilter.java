//package cn.eeepay.boss.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class SessionFilter implements Filter{
//
//	@Override
//	public void destroy() {
//		// TODO Auto-generated method stub
//	}
//
//	@Override
//	public void doFilter(ServletRequest arg0, ServletResponse arg1,
//			FilterChain arg2) throws IOException, ServletException {
//		System.out.println("myFilter");
//		HttpServletRequest request=(HttpServletRequest)arg0;  
//        HttpServletResponse response=(HttpServletResponse)arg1;
//		if(SecurityContextHolder.getContext().getAuthentication()== null ||
//				SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")){
//			response.sendRedirect(request.getContextPath()+"/login.do?expired");
//		}
//		else{
//			arg2.doFilter(arg0, arg1);
//		}
//		
//	}
//
//	@Override
//	public void init(FilterConfig arg0) throws ServletException {
//		
//	}
//
//}