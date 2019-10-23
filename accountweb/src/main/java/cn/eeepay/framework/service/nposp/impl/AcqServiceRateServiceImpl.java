package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.AcqServiceRateMapper;
import cn.eeepay.framework.model.nposp.AcqServiceRate;
import cn.eeepay.framework.service.nposp.AcqServiceRateService;

@Service("acqServiceRateService")
@Transactional("nposp")
public class AcqServiceRateServiceImpl implements AcqServiceRateService {

	@Resource
	private AcqServiceRateMapper acqServiceRateMapper;

	@Override
	public List<AcqServiceRate> findServiceRateByServiceId(Long acqServiceId) {
		return acqServiceRateMapper.findServiceRateByServiceId(acqServiceId);
	}
	

}
