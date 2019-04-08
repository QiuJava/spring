package cn.qj.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

/**
 * 安全配置
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/** Spring Security 放到session中的认证相关信息 */
	public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

	public static final String DEFAULT_CSRF_TOKEN_ATTR_NAME = HttpSessionCsrfTokenRepository.class.getName()
			.concat(".CSRF_TOKEN");

	@Autowired
	private AuthenticationProviderImpl authenticationProviderImpl;

	/**
	 * 自定义系统安全配置
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 添加认证供应器
		http.authenticationProvider(authenticationProviderImpl);

		// 授权请求
		http.authorizeRequests().antMatchers("/login", "/loginProcessing", "/home").permitAll();
		http.authorizeRequests().anyRequest().authenticated();

		// 配置登录
		http.formLogin().loginPage("/login").loginProcessingUrl("/loginProcessing").defaultSuccessUrl("/home");

		// 配置登出
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/login");

		// 配置crsf
		// http.csrf().csrfTokenRepository(csrfTokenRepository);
		 http.csrf().disable();
	}

}
