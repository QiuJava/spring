package cn.loan.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.loan.core.entity.SystemAccount;
import cn.loan.core.repository.SystemAccountDao;

/**
 * 系统账户服务
 * 
 * @author qiujian
 *
 */
@Service
public class SystemAccountService {

	@Autowired
	private SystemAccountDao systemAccountDao;

	public boolean existAccount() {
		return systemAccountDao.count() > 0;
	}
	
	@Transactional(rollbackFor = RuntimeException.class)
	public void save(SystemAccount systemAccount) {
		systemAccountDao.save(systemAccount);
	}

	public SystemAccount getCurrent() {
		return systemAccountDao.findAll().get(0);
	}

}
