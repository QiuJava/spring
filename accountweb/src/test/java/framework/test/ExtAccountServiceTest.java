package framework.test;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.dao.bill.ShiroUserMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.service.bill.AdjustAccountService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.ShiroUserService;
public class ExtAccountServiceTest extends BaseTest {
	@Resource
	public ShiroUserService shiroUserService;
	@Resource
	public AdjustAccountService adjustAccountService;
	@Resource 
	public ExtAccountService extAccountService;
	@Resource
	public ShiroUserMapper shiroUserMapper;
	
	
	@Test
	public void testaaa() throws Exception{
		ExtTransInfo extTransInfo = new ExtTransInfo();
		extTransInfo.setAccountNo("000001122103000000000488");
		Map<String, String> params = new HashMap<String, String>();
		Sort sort = new Sort();
		Page<Map<String,Object>> page = new Page<Map<String,Object>>();
		List<Map<String,Object>> list = extAccountService.findAllExtTransInfo(extTransInfo , params , sort, page);
		for (Map<String,Object> extTransInfo2 : list) {
			System.out.println(extTransInfo2.toString());
		}
		
	}
}
