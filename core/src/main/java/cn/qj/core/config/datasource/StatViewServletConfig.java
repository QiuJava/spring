package cn.qj.core.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * Druid视图Servlet初始化参数
 * 
 * @author Qiujian
 * @date 2018/9/25
 */
@Getter
@Setter
@ConfigurationProperties("statViewServlet.initParameter")
@Configuration
public class StatViewServletConfig {
	private String allow;
	private String deny;
	private String loginUsername;
	private String loginPassword;
	private Boolean resetEnable;
}
