package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.model.bill.SystemInfo;

public interface SystemInfoService {
	SystemInfo findSystemInfoByCurrentDate(String currentDate) throws Exception;
	SystemInfo findSystemInfoById(Integer Id) throws Exception;
	int updateSystemInfo(SystemInfo systemInfo) throws Exception;
}
