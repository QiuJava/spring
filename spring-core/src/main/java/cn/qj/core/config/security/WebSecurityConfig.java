package cn.qj.core.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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

	@Autowired
	private AuthenticationProviderImpl authenticationProviderImpl;

	@Autowired
	private AccessDeniedHandlerImpl accessDeniedHandlerImpl;

	/**
	 * 自定义系统安全配置
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 添加认证供应器
		http.authenticationProvider(authenticationProviderImpl);

		// 授权请求
		http.authorizeRequests().antMatchers("/", "/home", "/about").permitAll()
				//
				.antMatchers("/admin/**").hasAnyRole("ADMIN")
				//
				.antMatchers("/user/**").hasAnyRole("USER");

		http.authorizeRequests().anyRequest().authenticated();

		// 配置登录
		http.formLogin().loginPage("/login").permitAll();
		// 配置登出
		http.logout().permitAll();

		http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);
		// 配置crsf
	}

}
