package cn.eeepay.framework.dao.nposp;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.OutAccountServiceRate;

/**
 * 出款服务费率
 * @author zouruijin
 * @date 2016年8月16日17:19:42
 *
 */
public interface OutAccountServiceRateMapper {
	
	@Select("select * from out_account_service_rate  where out_account_service_id = #{serviceId}  and agent_rate_type=#{agentRateType}")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper.BaseResultMap")
	OutAccountServiceRate findOutAccountServiceRateBySrvIdAndArType(Map<String, Object> map);
	
	@Select("select * from out_account_service_rate where out_account_service_id = #{serviceId}  and cost_rate_type=#{costRateType}")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper.BaseResultMap")
	OutAccountServiceRate findOutAccountServiceRateBySrvIdAndCrType(Map<String, Object> map);
	
	@Select("select * from out_account_service_rate where out_account_service_id = #{serviceId}")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper.BaseResultMap")
	List<OutAccountServiceRate> findByServiceId(@Param("serviceId")Integer serviceId);
	
	
	@Select("select * from out_account_service_rate where out_account_service_id = #{serviceId} and agent_rate_type is not null")
	@ResultMap("cn.eeepay.framework.dao.nposp.OutAccountServiceRateMapper.BaseResultMap")
	OutAccountServiceRate findServiceRateByServiceIdAndAgentRateTypeNotNull(@Param("serviceId")Integer serviceId);
	
	
}
