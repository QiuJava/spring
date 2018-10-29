package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.entity.business.SystemAccountFlow;
import cn.pay.core.repository.SystemAccountFlowRepository;
import cn.pay.core.service.SystemAccountFlowService;

/**
 * 系统账户流水服务实现
 * 
 * @author Qiujian
 * @date 2018年8月10日
 */
@Service
public class SystemAccountFlowServiceImpl implements SystemAccountFlowService {

	@Autowired
	private SystemAccountFlowRepository repository;

	@Override
	@Transactional(rollbackFor = { RuntimeException.class })
	public void saveAndUpdate(SystemAccountFlow flow) {
		repository.saveAndFlush(flow);
	}

}
