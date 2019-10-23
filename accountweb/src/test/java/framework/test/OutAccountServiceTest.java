package framework.test;


import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.service.nposp.OutAccountServiceService;
public class OutAccountServiceTest extends BaseTest {
	@Resource
	public OutAccountServiceService outAccountServiceService;
	
	@Test
	public void testCreateKey() throws Exception{
		String acqEnname = "xmms";
        String outAccountId = "23";
        String agentRateType = "3";
        String costRateType = "1";
        BigDecimal transAmount = new BigDecimal("1000");
        Map<String, BigDecimal> acqOutServiceMoney = outAccountServiceService.acqOutServiceMoney(acqEnname, outAccountId, transAmount, agentRateType, costRateType);
        System.out.println("a");
	}
	
}
