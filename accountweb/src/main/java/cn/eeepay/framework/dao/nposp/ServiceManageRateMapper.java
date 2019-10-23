package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.ServiceRate;

public interface ServiceManageRateMapper {
	
	//用于商户费率
	@Select("select * from service_manage_rate where "
			+ "service_id=#{srs.serviceId} and agent_no=#{srs.agentNo} "
			+ "and holidays_mark=#{srs.holidaysMark} and card_type=#{srs.cardType} and rate_type=#{srs.rateType}")
	@ResultType(ServiceRate.class)
	ServiceRate findServiceManageRate(@Param("srs")ServiceRate sr);
}
