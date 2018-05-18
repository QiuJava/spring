package cn.pay.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pay.core.dao.SystemAccountFlowRepository;
import cn.pay.core.domain.business.SystemAccountFlow;
import cn.pay.core.service.SystemAccountFlowService;

@Service
public class SystemAccountFlowServiceImpl implements SystemAccountFlowService {

	@Autowired
	private SystemAccountFlowRepository repository;

	@Override
	@Transactional
	public void saveAndUpdate(SystemAccountFlow flow) {
		repository.saveAndFlush(flow);
	}

}
