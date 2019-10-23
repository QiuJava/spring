package cn.eeepay.framework.dao.nposp;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.nposp.AcqServiceRate;

public interface AcqServiceRateMapper {
    
	@Select("select * from acq_service_rate where acq_service_id=#{acqServiceId}")
	@ResultType(AcqServiceRate.class)
	List<AcqServiceRate> findServiceRateByServiceId(@Param("acqServiceId")Long acqServiceId);
}