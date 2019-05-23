
package cn.qj.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
/**
 * 字典key配置
 * @author Qiujian
 * @date 2019年5月23日
 *
 */
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties("dict")
@Component
public class DictProperties {

	private String usernamePasswordErrMsg;
	private String credentialsExpiredErrMsg;
	private String accountExpiredErrMsg;
	private String lockedErrMsg;
	private String disabledErrMsg;
	private String loginSuccessMsg;
	private String loginMaxLoseNum;
	private String noRightsMsg;
	private String rememberMeSeconds;
}

