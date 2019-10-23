package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.AcqServiceMapper;
import cn.eeepay.framework.model.nposp.AcqService;
import cn.eeepay.framework.service.nposp.AcqServiceService;

@Service("acqServiceService")
@Transactional("nposp")
public class AcqServiceServiceImpl implements AcqServiceService{
	@Resource
	private AcqServiceMapper acqServiceMapper;
	
	@Override
	public AcqService getById(Integer id) {
		return acqServiceMapper.getById(id);
	}

	@Override
	public List<AcqService> findByAcqEnnameAndServiceType(String acqEnname,
			String serviceType) {
		return acqServiceMapper.findByAcqEnnameAndServiceType(acqEnname, serviceType);
	}

}
