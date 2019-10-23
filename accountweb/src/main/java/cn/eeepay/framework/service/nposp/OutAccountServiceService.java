package cn.eeepay.framework.service.nposp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.nposp.OutAccountService;

public interface OutAccountServiceService {
//	public OutAccountService getByAcqEnnameAndServiceType(String acqEnname, Integer serviceType);

	public Map<String, BigDecimal> acqOutServiceMoney(String acqEnname,
			String outAccountId, BigDecimal transAmount, String agentRateType,
			String costRateType) throws Exception;
	
	public OutAccountService getById(Integer id);
	
//	OutAccountService getByAcqEnname(String acqEnname);
	public List<OutAccountService> findAllOutAccountServiceByType();
	
	public OutAccountService findEntityByAcqEnnameAndServiceType(String acqEnname, Integer serviceType);

	public List<OutAccountService> findEntity(String acqEnname);

	public List<OutAccountService> findOutAccountServiceListByAcqEnname(String acqEnname);

	public List<OutAccountService> findOutAccSerListByAcqNname(String acqEnname,String serviceTypes);
	
	public List<OutAccountService> findAllOutAccountServiceList();
}
