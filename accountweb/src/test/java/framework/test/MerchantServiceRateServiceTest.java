package framework.test;


import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.nposp.MerchantServiceRate;
import cn.eeepay.framework.service.nposp.MerchantServiceRateService;
public class MerchantServiceRateServiceTest extends BaseTest {
	@Resource
	public MerchantServiceRateService merchantServiceRateService;
	
	@Test
	public void test() throws InterruptedException {
		MerchantServiceRate rate = new MerchantServiceRate();
		rate.setMerchantNo("25541100000000000131");
		List<MerchantServiceRate> list = merchantServiceRateService.selectByMertId(rate);
		System.out.println(list);
		Thread.sleep(20000);

	}
}
