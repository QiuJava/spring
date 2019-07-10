package cn.loan.core.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import cn.loan.core.entity.LoginUser;
import cn.loan.core.util.SecurityContextUtil;

/**
 * 审计自定义
 * 
 * @author qiujian
 *
 */
@Configuration
@EnableJpaAuditing
public class AuditCustom implements AuditorAware<Long> {

	@Override
	public Long getCurrentAuditor() {
		LoginUser currentUser = SecurityContextUtil.getCurrentUser();
		return currentUser == null ? null : currentUser.getId();
	}

}
