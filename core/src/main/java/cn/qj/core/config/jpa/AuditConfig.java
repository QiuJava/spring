package cn.qj.core.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;

import cn.qj.core.entity.LoginInfo;
import cn.qj.core.util.HttpSessionUtil;

/**
 * 审计配置
 * 
 * @author qiujian@eeepay.cn
 *
 */
@Configuration
public class AuditConfig implements AuditorAware<String> {

	@Override
	public String getCurrentAuditor() {
		LoginInfo info = HttpSessionUtil.getCurrentLoginInfo();
		return info == null ? null : info.getUsername();
	}

}
