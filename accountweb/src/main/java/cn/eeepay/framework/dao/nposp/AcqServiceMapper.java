package cn.eeepay.framework.dao.nposp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.AcqService;

public interface AcqServiceMapper {
	@Select("select * from acq_service where id=#{id}")
	@ResultType(AcqService.class)
	AcqService getById(Integer id);
	
	@Select("select * from acq_service where acq_enname=#{acqEnname} and service_type in (#{serviceType})")
	@ResultType(AcqService.class)
	List<AcqService> findByAcqEnnameAndServiceType(@Param("acqEnname")String acqEnname, @Param("serviceType")String serviceType);
}
