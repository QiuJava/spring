package cn.eeepay.framework.service.nposp;

import java.util.List;

import cn.eeepay.framework.model.nposp.AcqService;

public interface AcqServiceService {
	AcqService getById(Integer id);
	
	List<AcqService> findByAcqEnnameAndServiceType(String acqEnname, String serviceType);
}
