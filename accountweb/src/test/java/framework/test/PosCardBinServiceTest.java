package framework.test;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.nposp.PosCardBin;
import cn.eeepay.framework.service.nposp.PosCardBinService;

public class PosCardBinServiceTest  extends BaseTest {
	@Resource
	public PosCardBinService posCardBinService;
	@Test
	public void test1() throws Exception {
		String cardNo = "6225886555116998";
		PosCardBin posCardBin = posCardBinService.findPosCardBinByCardNo(cardNo);
		System.out.println(posCardBin.getCardName());
	}
}
