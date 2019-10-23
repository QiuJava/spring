package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.AcqMerchant;

public interface AcqMerchantMapper {
	@Select("select * from acq_merchant where acq_merchant_no=#{acqMerchantNo}")
	@ResultType(AcqMerchant.class)
	public AcqMerchant getByAcqMerchantNo(String acqMerchantNo);
}
