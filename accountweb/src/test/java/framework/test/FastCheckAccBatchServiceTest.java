package framework.test;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper;
import cn.eeepay.framework.service.bill.FastCheckAccBatchService;
import cn.eeepay.framework.service.bill.FastCheckAccDetailService;

public class FastCheckAccBatchServiceTest extends BaseTest {
	@Resource
	public FastCheckAccBatchService fastCheckAccBatchService;
	@Resource
	public FastCheckAccDetailService fastCheckAccDetailService;
	
	@Resource
	public FastCheckAccountDetailMapper fastCheckAccountDetailDao;
	@Test
	public void testaaa() throws Exception{
//		int i = 0;
//		i = fastCheckAccBatchService.deleteFastAccountBatch1(38);
//		System.out.println(i);
		
//		List<FastCheckAccDetail> list = fastCheckAccountDetailDao.findByCheckBatchNoAndErrorHandleSatus("TFB_API20161104632", "pendingTreatment");
//		for (FastCheckAccDetail fastCheckAccDetail : list) {
//			System.out.println(fastCheckAccDetail.getAcqEnname());
//		}
		
		fastCheckAccBatchService.test();
	}
}
