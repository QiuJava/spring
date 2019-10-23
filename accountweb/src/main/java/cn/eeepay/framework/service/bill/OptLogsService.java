package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OptLogs;

public interface OptLogsService {
	
	List<OptLogs> findOperateLog(OptLogs operateLog,Map<String, String> params,Sort sort,Page<OptLogs> page) throws Exception;

	int insertOptLogs(OptLogs log);

}
