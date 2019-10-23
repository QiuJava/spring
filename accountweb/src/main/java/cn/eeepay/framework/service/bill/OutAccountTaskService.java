package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutAccountTask;

public interface OutAccountTaskService {
	
	List<OutAccountTask> findOutAccountTaskList(Map<String, Object> param, Sort sort,Page<OutAccountTask> page) throws Exception;
	OutAccountTask findOutAccountTaskById(Integer id) ;

	int insert(OutAccountTask task);
	
	int update(OutAccountTask task);
	
	/**
	 * 插入或者更新任务，根据系统时间查询
	 * @param task
	 * @return
	 */
	int insertOrUpdate(OutAccountTask task);
	
	/**
	 * 该上游出账任务总金额
	 * @param acqEnname
	 * @return
	 */
	BigDecimal calcOutAccountTaskAmountByAcqEnname(String acqEnname, Date transTime);
	
	List<OutAccountTask> findByTransTimeAndAcqEnname(Date transTime, String acqEnname, String outBillRange);
	
	List<OutAccountTask> findOutAccountTaskByTransTime(Date transTime);
	
	/**
	 * 关闭昨天未生成出账单的出账任务
	 * @param transTime
	 * @return
	 */
	int updateToClosedByTransTime(Date transTime);
}
