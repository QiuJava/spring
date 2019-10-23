package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.model.bill.SysConfig;

public interface SysConfigService {
	SysConfig getByKey(String key);
	
	int update(SysConfig config);
}
