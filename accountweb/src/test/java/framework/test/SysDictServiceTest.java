package framework.test;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.service.bill.SysDictService;
public class SysDictServiceTest extends BaseTest {
	@Resource
	public SysDictService sysDictService;

	@Test
	public void testbbb() throws Exception{
		List<SysDict> sysDicts = sysDictService.findAllSysDict();
		for (SysDict sysDict : sysDicts) {
			System.out.println(sysDict.getSysName());
		}
		
	}
}
