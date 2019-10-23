package framework.test;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.enums.OutBillRecordStatus;
import cn.eeepay.framework.model.bill.OutBillDetail;
import cn.eeepay.framework.service.bill.OutBillDetailService;
public class OutBillDetailServiceTest extends BaseTest {
	private static final Logger log = LoggerFactory.getLogger(OutBillDetailServiceTest.class);
	@Resource
	public OutBillDetailService outBillDetailService;
	
	@Test
	public void test1() throws Exception{
		List<OutBillDetail> details =  new ArrayList<>();
		OutBillDetail detail1 = new OutBillDetail();
		detail1.setCreateTime(new Date());
		detail1.setOutBillId(55555);
		detail1.setMerchantNo("258121000001477");
		detail1.setMerchantBalance(new BigDecimal("500"));
		detail1.setOutAccountTaskAmount(new BigDecimal("1500"));
		detail1.setAcqOrgNo("neweptok");
		detail1.setExportStatus(0);
		detail1.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
		detail1.setOutBillStatus(0);
		details.add(detail1);
		
		OutBillDetail detail2 = new OutBillDetail();
		detail2.setCreateTime(new Date());
		detail2.setOutBillId(55555);
		detail2.setMerchantNo("258121000001593");
		detail2.setMerchantBalance(new BigDecimal("300"));
		detail2.setOutAccountTaskAmount(new BigDecimal("1300"));
		detail2.setAcqOrgNo("neweptok");
		detail2.setExportStatus(0);
		detail2.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
		detail2.setOutBillStatus(0);
		details.add(detail2);
		
		OutBillDetail detail3 = new OutBillDetail();
		detail3.setCreateTime(new Date());
		detail3.setOutBillId(55555);
		detail3.setMerchantNo("258121000001253");
		detail3.setMerchantBalance(new BigDecimal("200"));
		detail3.setOutAccountTaskAmount(new BigDecimal("1200"));
		detail3.setAcqOrgNo("neweptok");
		detail3.setExportStatus(0);
		detail3.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
		detail3.setOutBillStatus(0);
		details.add(detail3);
		
		outBillDetailService.insertTestBatch3(details);
		
		for (OutBillDetail outBillDetail : details) {
			log.info(outBillDetail.getId());
		}
		
		
	}
	
}
