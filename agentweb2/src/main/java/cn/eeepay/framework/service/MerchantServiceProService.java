package cn.eeepay.framework.service;

import java.util.List;


import cn.eeepay.framework.model.MerchantService;

public interface MerchantServiceProService {

	public int updateByPrimaryKey(MerchantService record);
    
	public List<MerchantService> selectByMerId(String merId);
	
	List<String> selectServiceTypeByMerId(String merId);
}
