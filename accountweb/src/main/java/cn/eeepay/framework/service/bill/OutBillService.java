package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTask;
import cn.eeepay.framework.model.bill.OutBill;
import cn.eeepay.framework.model.bill.UserInfo;

public interface OutBillService {
	
	int insertOutBill(OutBill outBill) throws Exception;
	int updateOutBillById(OutBill outBill) throws Exception;
	int deleteOutBillById(Integer id) throws Exception;
	int deleteOutBillByTaskId(Integer taskId) throws Exception;
	OutBill findOutBillByTaskId(Integer taskId);
	OutBill findOutBillById(Integer id);
	OutBill findOutBillTaskById(Integer id);
	List<OutBill> findOutBillList(String createTime1,String createTime2, Map<String, String> param, Sort sort, Page<OutBill> page) throws Exception;
	
	int updateOutBillStatus(Integer id, String uname);
	
	List<OutBill> findFailedOutBill();
	
	int updateToClosedByTransTime(Date transTime);
	
	
	int updateExportFileName(Integer outBillId, String fileName, String uname);
	
	Map<String, Object> confirmOut(OutBill outBill, UserInfo userInfo) throws Exception;
	
	
	void mergeSubOutBill(Integer id, String acqEnname) throws Exception;
	
	List<OutBill> findAllNoOutBillId();
	
	BigDecimal findCalAmountByAcqNameAndOutBillStatus(String acqEnname);
	
	Map<String, Object> judgeConfirmOut(Integer id, String acqEnname);

    OutBill findCreateTime(String acqEnname, String createTime);

	OutAccountTask findTransTime(String acqName, String tTime);
}
