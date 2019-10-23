package cn.eeepay.framework.service;


import cn.eeepay.framework.model.MerchantCardInfo;

public interface MerchantCardInfoService {

	public int insert(MerchantCardInfo record);

	public MerchantCardInfo selectByMertId(String mertId);
	
	int updateById(MerchantCardInfo record);
}
