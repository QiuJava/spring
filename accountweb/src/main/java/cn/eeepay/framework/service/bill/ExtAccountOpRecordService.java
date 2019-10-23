package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.model.bill.ExtAccountOpRecord;


public interface ExtAccountOpRecordService {
	int insertExtAccountOpRecord(ExtAccountOpRecord extAccountOpRecord)  throws Exception;
	int updateExtAccountOpRecord(ExtAccountOpRecord extAccountOpRecord) throws Exception;
}
