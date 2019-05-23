package cn.qj.config.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import cn.qj.config.properties.ConstProperties;
import cn.qj.service.LoginUserServiceImpl;

/**
 * 安全配置
 * 
 * @author Qiujian
 * @date 2019年3月26日
 *
 */
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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

	@Autowired
	private ConstProperties constProperties;

	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		String persistentLoginsTable = constProperties.getPersistentLoginsTable();
		if (valueOperations.get(persistentLoginsTable) == null) {
			tokenRepository.setCreateTableOnStartup(true);
			valueOperations.set(persistentLoginsTable, persistentLoginsTable);
		}
		return tokenRepository;
	}

	/**
	 * 自定义系统安全配置
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		// 授权请求
		http.authorizeRequests().antMatchers("/jquery-easyui-1.7.0/**").permitAll();
		http.authorizeRequests().anyRequest().authenticated();

		// 配置登录
		http.formLogin().loginPage("/login").permitAll();
		// 记住我配置
		http.rememberMe().tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(securityProperty.getRememberMeSeconds()).userDetailsService(loginUserServiceImpl);

		http.exceptionHandling().accessDeniedHandler(accessDeniedHandlerImpl);

		// X-Frame-Options 设置可以在frame 中展示
		http.headers().frameOptions().disable();
	}

}
