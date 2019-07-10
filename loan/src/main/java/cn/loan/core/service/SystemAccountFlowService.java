package cn.loan.core.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.loan.core.config.cache.SystemDictionaryHashService;
import cn.loan.core.entity.SystemAccount;
import cn.loan.core.entity.SystemAccountFlow;
import cn.loan.core.repository.SystemAccountFlowDao;
import cn.loan.core.util.DateUtil;
import cn.loan.core.util.SystemDictionaryUtil;

/**
 * 系统账户流水服务
 * 
 * @author qiujian
 *
 */
@Service
public class SystemAccountFlowService {

	@Autowired
	private SystemAccountFlowDao systemAccountFlowDao;
	@Autowired
	private SystemDictionaryHashService systemDictionaryHashService;

	public void serviceFeeFlow(SystemAccount account, BigDecimal serviceFee, Long userId) {
		SystemAccountFlow flow = new SystemAccountFlow();
		flow.setSystemAccountId(account.getId());
		flow.setTargetUserId(userId);
		flow.setFreezedAmount(account.getFreezedAmount());
		flow.setUsableBalance(account.getUsableBalance());
		flow.setActionAmount(serviceFee);
		flow.setCreateDate(DateUtil.getNewDate());
		flow.setActionType(SystemDictionaryUtil.getItemValue(SystemDictionaryUtil.SYSTEM_ACCOUNT_FLOW_TYPE,
				SystemDictionaryUtil.BORROW_SERVICE_FEE_ACTION, systemDictionaryHashService));
		systemAccountFlowDao.save(flow);
	}

}
