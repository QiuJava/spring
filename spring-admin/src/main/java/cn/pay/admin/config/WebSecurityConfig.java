package cn.pay.admin.config;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import cn.pay.core.security.AdminAuthenticationProvider;
import cn.pay.core.security.AdminFilterSecurityInterceptor;
import cn.pay.core.service.LoginInfoService;

/**
 * SpringSecurity 配置类
 * 
 * @author Qiujian
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AdminFilterSecurityInterceptor adminFilterSecurityInterceptor;

	@Autowired
	private LoginInfoService loginInfoService;

	@Resource
	private AdminAuthenticationProvider authenticationProvider;

	/**
	 * 认证关系构建
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(loginInfoService).passwordEncoder(new BCryptPasswordEncoder());
		auth.authenticationProvider(authenticationProvider);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();// 禁用csrf防护

		http.authorizeRequests()
				// 静态资源可以完全访问
				//.antMatchers("/css/**", "/images/**", "/js/**", "/tags/**", "/login.html").permitAll()
				// 登录登出url可以完全访问
				.antMatchers("/loginInfo/login.do", "/loginInfo/logout.do").permitAll()
				//.antMatchers("/index.do").access("hasRole('后台首页')")
				// 所有以.do 结尾的请求需要登录之后才能访问
				//.antMatchers("*。do").authenticated()

				.and()//
				// 登录配置
				.formLogin()
				// 登录页面
				.loginPage("/login.html")
				// 登录处理URL
				.loginProcessingUrl("/loginInfo/login.do")
				// 登录失败跳转的页面
				.failureForwardUrl("/login.html")
				// 登录成功之后跳转的url
				.defaultSuccessUrl("/index.do")

				.and()//
				// 登出配置
				.logout()
				// 登出的URL
				.logoutUrl("/loginInfo/logout.do")
				// 登出成功的URL
				.logoutSuccessUrl("/login.html");

		http.addFilterBefore(adminFilterSecurityInterceptor, FilterSecurityInterceptor.class);
	}

}
