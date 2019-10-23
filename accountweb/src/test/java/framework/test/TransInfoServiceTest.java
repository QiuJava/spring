package framework.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.nposp.TransInfoService;

public class TransInfoServiceTest  extends BaseTest {
	@Resource
	public TransInfoService transInfoService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	
	//@Test
	public void test() throws Exception {
		String acqEnname = null;
		String transTimeBegin = null;
		String transTimeEnd = null;
		List<TransInfo> transInfoList= transInfoService.findCheckData(acqEnname, transTimeBegin, transTimeEnd);
		for (TransInfo transInfo : transInfoList) {
			System.out.println(transInfo.getAcqEnname());
		}
	}
	@Test
	public void test1() throws Exception {
		List<TransInfo> tansInfoList = transInfoService.findCheckData(null, null, null);
		for (TransInfo transInfo : tansInfoList) {
			System.out.println(transInfo.toString());
		}
	}
}
