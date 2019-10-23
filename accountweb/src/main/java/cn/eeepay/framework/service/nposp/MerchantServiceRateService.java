package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.MerchantServiceRate;

public interface MerchantServiceRateService {


	//用户商户费率修改查询
	public List<MerchantServiceRate> selectByMertId(MerchantServiceRate rate);
	
}
