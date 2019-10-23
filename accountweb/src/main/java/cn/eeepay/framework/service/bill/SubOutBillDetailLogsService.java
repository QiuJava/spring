package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetailLogs;

public interface SubOutBillDetailLogsService {

	int insertOutBillDetailLogs(SubOutBillDetail subOutBillDetail) throws Exception;

	List<SubOutBillDetailLogs> findOutBillDetailLogsList(SubOutBillDetailLogs subOutBillDetailLogs, Sort sort,
			Page<SubOutBillDetailLogs> page);

	List<SubOutBillDetailLogs> exportOutBillDetailLogsList(SubOutBillDetailLogs subOutBillDetailLogs);
	
}
