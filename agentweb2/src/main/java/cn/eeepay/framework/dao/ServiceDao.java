package cn.eeepay.framework.dao;

import java.util.List;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;

@WriteReadDataSource
public interface ServiceDao {
	
	//用于业务产品默认路由集群
	@Select("select * from service_info")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> selectServiceInfo();
	
	@Select("select * from service_info where service_id = #{serviceId}")
	@ResultType(ServiceInfo.class)
	ServiceInfo queryServiceInfo(Long serviceId);
	
	//用于商户费率
	@Select("select * from service_manage_rate where "
			+ "service_id=#{srs.serviceId} and agent_no=#{srs.agentNo} "
			+ "and holidays_mark=#{srs.holidaysMark} and card_type=#{srs.cardType} and rate_type=#{srs.rateType}")
	@ResultType(ServiceRate.class)
	ServiceRate queryServiceRate(@Param("srs")ServiceRate sr);
	
	//用于商户限额
	@Select("select * from service_manage_quota where "
			+ "service_id=#{srs.serviceId} and agent_no=#{srs.agentNo} "
			+ "and holidays_mark=#{srs.holidaysMark} and card_type=#{srs.cardType}")
	@ResultType(ServiceRate.class)
	ServiceQuota queryServiceQuota(@Param("srs")ServiceQuota sq);
	
	@Select("select * from service_manage_rate where service_id=#{serviceId} and agent_no=#{agentNo}")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getServiceRate(@Param("serviceId") Long serviceId,@Param("agentNo") String agentNo);
	
	@Select("select * from service_manage_quota where service_id=#{serviceId} and agent_no=#{agentNo}")
	@ResultType(ServiceQuota.class)
	List<ServiceQuota> getServiceQuota(@Param("serviceId") Long serviceId,@Param("agentNo") String agentNo);
	
	@Select("SELECT service_id,service_name FROM service_info WHERE service_id=#{id}")
	@ResultType(ServiceInfo.class)
	ServiceInfo findServiceName(@Param("id")String id);

	@Select("SELECT service_id,service_name,remark FROM service_info")
	@ResultType(ServiceInfo.class)
	List<ServiceInfo> findAllServiceName();

	//用于商户
	@Select("select * from service_manage_rate where service_id=#{serviceId} and agent_no=#{agentId}")
	@ResultType(ServiceRate.class)
	List<ServiceRate> getServiceAllRate(@Param("serviceId") Long serviceId,@Param("agentId")String agentId);

	@Select("select smq.* from service_manage_quota smq,activity_config ac where ac.agent_service_id = smq.service_id and smq.agent_no = 0 ")
	@ResultType(ServiceQuota.class)
	ServiceQuota queryHlsServiceQuota();

	@Select("SELECT smq.* FROM service_manage_quota smq,activity_config ac WHERE ac.cash_service_id = smq.service_id AND smq.agent_no = 0 ")
	@ResultType(ServiceQuota.class)
	ServiceQuota queryCashServiceQuota();

	/**
	 * 超级银行家提现限额
	 * @param sysKey
	 * @return
	 */
	@Select("select * from service_manage_quota where service_id = (select sys_value from sys_dict where sys_key = #{sysKey}) ")
	@ResultType(ServiceQuota.class)
	ServiceQuota queryHlsServiceQuotaSuperBank(@Param("sysKey")String sysKey);


	@Select("select * from service_manage_quota where service_id=#{serviceId} and agent_no=#{agentNo} limit 1")
	@ResultType(ServiceQuota.class)
	ServiceQuota selectServiceQuota(@Param("serviceId") Long serviceId,@Param("agentNo") String agentNo);
}
