package cn.qj.loan.config.security;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.qj.core.consts.SysConst;

/**
 * SpringSecurity 配置类
 * 
 * @author Qiujian
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource
	private LoanAuthenticationProvider authenticationProvider;

	@Autowired
	private LoanLoginSuccessHandler successHandler;
	@Autowired
	private LoanLoginFailureHandler failureHandler;

	/**
	 * 认证关系构建
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.userDetailsService(userDetailsService).passwordEncoder(new
		// BCryptPasswordEncoder());
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();// 禁用csrf防护

		http.authorizeRequests()
				// 静态资源可以完全访问
				.antMatchers("/borrow.html", "/index.html", "/register.html").permitAll()
				// 登录登录url可以完全访问
				.antMatchers(SysConst.URL_LOGIN_INFO_AJAX, "/home", "/borrow/home", "/sms/send", "/email/verify",
						"/borrow_info", "/invest/list", "/loginInfo/register", "/loginInfo/isExist")
				.permitAll()
				// .antMatchers("/index").access("hasRole('后台首页')")
				// 所有以 结尾的请求需要登录之后才能访问
				// .antMatchers("*").
				.anyRequest().authenticated().and()//
				// 登录配置
				.formLogin()
				// 登录页面
				.loginPage(SysConst.URL_LOGIN_HTML)
				// 登录处理URL
				.loginProcessingUrl(SysConst.URL_LOGIN_INFO_LOGIN).permitAll()
				// 登录失败跳转的页面
				// .failureForwardUrl("/login.html")
				// 登录成功之后跳转的url
				// .defaultSuccessUrl("/index")
				.successHandler(successHandler).failureHandler(failureHandler)

				.and()//
				// 登出配置
				.logout()
				// 登出的URL
				.logoutUrl(SysConst.URL_LOGIN_INFO_LOGOUT).permitAll()
				// 登出成功的URL
				.logoutSuccessUrl(SysConst.URL_LOGIN_HTML)
				// 登出时让HttpSession无效
				.invalidateHttpSession(true);

	}

}
