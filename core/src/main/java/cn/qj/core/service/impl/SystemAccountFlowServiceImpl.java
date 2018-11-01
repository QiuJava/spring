package cn.qj.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.qj.core.entity.SystemAccountFlow;
import cn.qj.core.repository.SystemAccountFlowRepository;
import cn.qj.core.service.SystemAccountFlowService;

/**
 * 系统账户流水服务实现
 * 
 * @author Qiujian
 * @date 2018/8/10
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
