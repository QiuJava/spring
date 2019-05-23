package cn.qj.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 常量key
 * 
 * @author Qiujian
 * @date 2019年5月23日
 *
 */
@Getter
@Setter
@Component
@ConfigurationProperties("const")
public class ConstProperties {

	private String admin;
	private String password;
	private String dictHash;
	private String permissionHash;
	private String persistentLoginsTable;

}
