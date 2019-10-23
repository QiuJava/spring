package cn.eeepay.framework.service.nposp.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.ServiceManageRateMapper;
import cn.eeepay.framework.model.bill.ServiceRate;
import cn.eeepay.framework.service.nposp.ServiceManageRateService;


@Service("serviceManageRateService")
@Transactional("nposp")
public class ServiceManageRateServiceImpl implements ServiceManageRateService {
	@Resource
	private ServiceManageRateMapper serviceManageRateMapper;

	@Override
	public ServiceRate findServiceManageRate(ServiceRate sr) {
		return serviceManageRateMapper.findServiceManageRate(sr);
	}
}
