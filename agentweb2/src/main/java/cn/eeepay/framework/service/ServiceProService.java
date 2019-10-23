package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;

public interface ServiceProService {
	List<ServiceInfo> selectServiceInfo();

	String profitExpression(ServiceRate rule);

	ServiceRate queryServiceRate(ServiceRate sr);
	
	ServiceInfo queryServiceInfo(Long serviceId);
	
	ServiceQuota queryServiceQuota(ServiceQuota sq);

	List<ServiceQuota> getServiceAllQuota(Long serviceId,String agentId);

	List<ServiceRate> getServiceAllRate(Long serviceId, String oneAgentNo);
}
