package cn.eeepay.framework.service.nposp.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper;
import cn.eeepay.framework.model.nposp.OutAccountServiceRate;
import cn.eeepay.framework.service.nposp.OutAccountServiceRateService;

@Service("outAccountServiceRateService")
@Transactional("nposp")
public class OutAccountServiceRateServiceImpl implements OutAccountServiceRateService {

	@Resource
	private OutAccountServiceRateMapper outAccountServiceRateMapper;
	@Override
	public OutAccountServiceRate findOutAccountServiceRateBySrvIdAndArType(Map<String, Object> map) {
		return outAccountServiceRateMapper.findOutAccountServiceRateBySrvIdAndArType(map);
	}

	@Override
	public OutAccountServiceRate findOutAccountServiceRateBySrvIdAndCrType(Map<String, Object> map) {
		return outAccountServiceRateMapper.findOutAccountServiceRateBySrvIdAndCrType(map);
	}

	@Override
	public OutAccountServiceRate getByServiceId(Integer serviceId) {
		List<OutAccountServiceRate> list = outAccountServiceRateMapper.findByServiceId(serviceId);
		return list != null && list.size() > 0 ? list.get(0) : null;
	}
	
	@Override
	public List<OutAccountServiceRate>  getByServiceIdInfo(Integer serviceId) {
		return outAccountServiceRateMapper.findByServiceId(serviceId);
	}





}
