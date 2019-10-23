package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.model.nposp.RiskRoll;
import org.apache.ibatis.annotations.Select;

public interface RiskRollMapper {

	@Select("select * from risk_roll where roll_status = 1 and roll_belong = 2 and roll_no = #{merchantNo}")
	public RiskRoll findMerchantBlackByMerchantNo(String merchantNo);
	
}
