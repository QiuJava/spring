package cn.eeepay.framework.dao.nposp;

import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.bill.ServiceInfo;

public interface ServiceInfoMapper {
	@Select("select * from service_info where service_id=#{id}")
	ServiceInfo getById(Long id);
}
