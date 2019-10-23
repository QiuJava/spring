package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.nposp.RiskRoll;

public interface RiskRollService {

	public RiskRoll findMerchantBlackByMerchantNo(String merchantNo);

}
