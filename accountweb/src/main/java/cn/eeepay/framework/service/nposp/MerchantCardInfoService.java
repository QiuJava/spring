package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.nposp.MerchantCardInfo;

public interface MerchantCardInfoService {
	/**
	 * 根据商户号查询商户银行卡信息
	 * @param merchantNo
	 * @return
	 */
	public MerchantCardInfo getByMerchantNo(String merchantNo);
	
	/**
	 * 根据手机号码查询商户银行卡信息
	 * @param mobile
	 * @return
	 */
	public MerchantCardInfo getByMobile(String mobile);
}
