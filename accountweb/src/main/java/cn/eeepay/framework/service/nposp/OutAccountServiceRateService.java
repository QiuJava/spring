package cn.eeepay.framework.service.nposp;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.nposp.OutAccountServiceRate;


/**
 * 出款服务费率
 * @author zouruijin
 * 2016年8月4日11:36:08
 *
 */
public interface OutAccountServiceRateService {


	public OutAccountServiceRate findOutAccountServiceRateBySrvIdAndArType(Map<String, Object> map);
	public OutAccountServiceRate findOutAccountServiceRateBySrvIdAndCrType(Map<String, Object> map);
	
	OutAccountServiceRate getByServiceId(Integer serviceId);
	List<OutAccountServiceRate>  getByServiceIdInfo(Integer serviceId);
}
