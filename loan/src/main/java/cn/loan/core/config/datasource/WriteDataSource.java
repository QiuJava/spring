package cn.loan.core.config.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * 写数据源属性
 * 
 * @author Qiujian
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("mysql.write")
public class WriteDataSource {
	
	private String driverClassName;
	private String url;
	private String username;
	private String password;
	
}
