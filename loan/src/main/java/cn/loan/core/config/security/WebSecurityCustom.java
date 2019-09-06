package cn.loan.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;

import cn.loan.core.config.filter.XssFilter;
import cn.loan.core.controller.BorrowController;
import cn.loan.core.controller.LoginUserController;

/**
 * Web安全自定义
 * 
 * @author Qiujian
 * 
 */
@EnableWebSecurity
public class WebSecurityCustom {

	public static final XssFilter XSS = new XssFilter();

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Configuration
	@Order(1)
	public static class WebsiteWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private AuthenticationSuccessHandlerCustom authenticationSuccessHandlerCustom;
		@Autowired
		private AuthenticationFailureHandlerWebsite authenticationFailureHandlerWebsite;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/website/**").authorizeRequests()
					.antMatchers(LoginUserController.WEBSITE_MAPPING, LoginUserController.WEBSITE_REGISTER_MAPPING,
							LoginUserController.WEBSITE_LOGINUSER_USERNAMEEXIST_MAPPING,
							LoginUserController.WEBSITE_LOGINUSER_REGISTER_MAPPING,
							LoginUserController.WEBSITE_LOGINUSER_LOGINRESULT_MAPPING,
							BorrowController.WEBSITE_BORROW_MAPPING)
					.permitAll().anyRequest().authenticated().and().formLogin()
					.loginPage(LoginUserController.WEBSITE_LOGIN_MAPPING).loginProcessingUrl("/website/loginUser/login")
					.permitAll().successHandler(authenticationSuccessHandlerCustom)
					.failureHandler(authenticationFailureHandlerWebsite).and().logout().logoutUrl("/website/logout")
					.permitAll().and().addFilterBefore(XSS, CsrfFilter.class);

		}
	}

	@Configuration
	@Order(2)
	public static class ManageWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private AuthenticationSuccessHandlerCustom authenticationSuccessHandlerCustom;
		@Autowired
		private AuthenticationFailureHandlerManage authenticationFailureHandlerCustom;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.antMatcher("/manage/**").authorizeRequests()
					.antMatchers(LoginUserController.MANAGE_MAPPING,
							LoginUserController.MANAGE_LOGINUSER_LOGINRESULT_MAPPING)
					.permitAll().anyRequest().authenticated().and().formLogin()
					.loginPage(LoginUserController.MANAGE_LOGIN_MAPPING).loginProcessingUrl("/manage/loginUser/login")
					.permitAll().successHandler(authenticationSuccessHandlerCustom)
					.failureHandler(authenticationFailureHandlerCustom).and().logout().logoutUrl("/manage/logout")
					.permitAll().and().addFilterBefore(XSS, CsrfFilter.class);
		}
	}

	@Configuration
	@Order(3)
	public static class ApiWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.antMatcher("/api/**").authorizeRequests().anyRequest().anonymous().and().addFilterBefore(XSS,
					CsrfFilter.class);
		}
	}

}
