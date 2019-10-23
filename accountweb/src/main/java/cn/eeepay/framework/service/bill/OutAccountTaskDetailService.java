package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTaskDetail;
import cn.eeepay.framework.model.bill.UserInfo;

public interface OutAccountTaskDetailService {
	
	List<OutAccountTaskDetail> findOutAccountTaskDetailList(OutAccountTaskDetail outAccountTaskDetail,Sort sort, Page<OutAccountTaskDetail> page);
	OutAccountTaskDetail findOutAccountTaskDetailById(Integer id);
	List<OutAccountTaskDetail> findOutAccountTaskDetailByTaskId(Integer outAccountTaskId) ;
	List<OutAccountTaskDetail> findOutAccountTaskUpdateList(OutAccountTaskDetail outAccountTaskDetail, Sort sort, Page<OutAccountTaskDetail> page) throws Exception;
	BigDecimal updateOutAccountDetailAmount(OutAccountTaskDetail outAccountTaskDetail,UserInfo userInfo);

	int insertBatch(List<OutAccountTaskDetail> list);
	
	int deleteByTaskId(Integer taskId);
	
	int insert(OutAccountTaskDetail outAccountTaskDetail);
}
