package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.AcqServiceRate;

public interface AcqServiceRateService {
	List<AcqServiceRate> findServiceRateByServiceId(Long acqServiceId);
}
