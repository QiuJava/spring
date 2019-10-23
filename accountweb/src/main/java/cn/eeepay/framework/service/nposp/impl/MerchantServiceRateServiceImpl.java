package cn.eeepay.framework.service.nposp.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.nposp.MerchantServiceRateMapper;
import cn.eeepay.framework.model.nposp.MerchantServiceRate;
import cn.eeepay.framework.service.nposp.MerchantServiceRateService;

@Service("merchantServiceRateService")
@Transactional("nposp")
public class MerchantServiceRateServiceImpl implements MerchantServiceRateService {
	
	@Resource
	private MerchantServiceRateMapper merchantServiceRateMapper;

	@Override
	public List<MerchantServiceRate> selectByMertId(MerchantServiceRate rate) {
		return merchantServiceRateMapper.selectByMertId(rate);
	}

}
