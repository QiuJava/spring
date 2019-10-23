package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.ServiceInfoMapper;
import cn.eeepay.framework.model.bill.ServiceInfo;
import cn.eeepay.framework.service.nposp.ServiceInfoService;

@Service("serviceInfoService")
@Transactional("nposp")
public class ServiceInfoServiceImpl implements ServiceInfoService {
	@Resource
	private ServiceInfoMapper serviceInfoMapper;
	
	@Override
	public ServiceInfo getById(Long id) {
		return serviceInfoMapper.getById(id);
	}

}
