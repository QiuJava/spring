package cn.qj.core.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import cn.qj.core.service.LoginUserServiceImpl;

/**
 * 安全配置
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String CREATE_TABLEON_START_UP = "CREATE_TABLEON_START_UP";

	@Autowired
	private AccessDeniedHandlerImpl accessDeniedHandlerImpl;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private LoginUserServiceImpl loginUserServiceImpl;

	@Autowired
	private ValueOperations<String, Object> valueOperations;

	@Autowired
	private SecurityProperty securityProperty;

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		if (valueOperations.get(CREATE_TABLEON_START_UP) == null) {
			tokenRepository.setCreateTableOnStartup(true);
			valueOperations.set(CREATE_TABLEON_START_UP, "false");
		}
		return tokenRepository;
	}

	/**
	 * 自定义系统安全配置
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 授权请求
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("/api/*").hasRole("USER");

		http.authorizeRequests().anyRequest().authenticated();

		// 配置登录
		http.formLogin().loginPage("/login").permitAll();
		// 记住我配置
		http.rememberMe().tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperty.getRememberMeSeconds()).userDetailsService(loginUserServiceImpl);
		http.logout().permitAll();

		http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);
	}

}
