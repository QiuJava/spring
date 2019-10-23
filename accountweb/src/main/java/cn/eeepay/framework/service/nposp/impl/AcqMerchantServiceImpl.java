package cn.eeepay.framework.service.nposp.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.AcqMerchantMapper;
import cn.eeepay.framework.model.nposp.AcqMerchant;
import cn.eeepay.framework.service.nposp.AcqMerchantService;

@Service("acqMerchantService")
@Transactional("nposp")
public class AcqMerchantServiceImpl implements AcqMerchantService {
	@Autowired
	private AcqMerchantMapper acqMerchantMapper;

	@Override
	public AcqMerchant getByAcqMerchantNo(String acqMerchantNo) {
		return acqMerchantMapper.getByAcqMerchantNo(acqMerchantNo);
	}
	
	
}
