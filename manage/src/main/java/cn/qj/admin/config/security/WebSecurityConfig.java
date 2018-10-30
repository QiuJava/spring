package cn.qj.admin.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import cn.qj.core.consts.SysConst;

/**
 * 安全配置
 * 
 * @author Qiujian
 * @date 2018年8月13日
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AdminAuthenticationProvider authenticationProvider;
	@Autowired
	private AdminLoginSuccessHandler successHandler;
	@Autowired
	private AdminLoginFailureHandler failureHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// 禁用csrf防护
		http.csrf().disable();

		http.authorizeRequests().and()
				// 登录配置
				.formLogin()
				// 登录页面
				.loginPage(SysConst.URL_LOGIN_HTML)
				// 登录处理URL
				.loginProcessingUrl(SysConst.URL_LOGIN_INFO_LOGIN).permitAll()
				// 登录成功处理
				.successHandler(successHandler)
				// 登录失败处理
				.failureHandler(failureHandler)

				.and()
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
