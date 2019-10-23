package framework.test;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.util.ListUtil;
public class DuiAccountDetailServiceTest extends BaseTest {
	@Resource
	public DuiAccountDetailService duiAccountDetailService;

	@Test
	public void testUpdateDuiAccountDetailBatchByPlateOrderNo() throws Exception{
		
		int batchCount = 500;
		List<DuiAccountDetail> list = new ArrayList<>();
		
		List<DuiAccountDetail> duiAccountDetailList = duiAccountDetailService.findAllDuiAccountDetailList();
		
		for (DuiAccountDetail duiAccountDetail : duiAccountDetailList) {
			duiAccountDetail.setRemark("hello world");
		}
		List<List<?>> splitList = ListUtil.batchList(duiAccountDetailList, batchCount);
		for (List<?> clist : splitList) {
			for (Object object : clist) {
				DuiAccountDetail duiAccountDetail = (DuiAccountDetail) object;
				if (duiAccountDetail !=null) {
					list.add(duiAccountDetail);
				}
			}
			if (list.size() > 0) {
				duiAccountDetailService.updateDuiAccountDetailBatchByPlateOrderNo(list);
				if (!list.isEmpty()) {
					list.clear();
				}
			}
		}
		
	}
}
