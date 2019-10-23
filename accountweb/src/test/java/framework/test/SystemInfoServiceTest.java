package framework.test;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.service.bill.SystemInfoService;

public class SystemInfoServiceTest  extends BaseTest {
	@Resource
	public SystemInfoService systemInfoService;
	
	@Test
	public void test1() throws Exception {
		SystemInfo systemInfo = systemInfoService.findSystemInfoById(1);
		System.out.println(systemInfo.getNextTransDate());
	}
}
