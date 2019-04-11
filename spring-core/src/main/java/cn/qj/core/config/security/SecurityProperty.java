package cn.qj.core.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * 安全属性
 * 
 * @author Qiujian
 * @date 2019年4月11日
 *
 */
@Component
@ConfigurationProperties("spring.security.property")
@Getter
@Setter
public class SecurityProperty {

	private int rememberMeSeconds;

}
