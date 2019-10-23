package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.model.bill.BatchesDetail;

public interface BatchesDetailService {
	List<BatchesDetail> findByBatchesId(Integer batchesId);
	
	/**
	 * 人工设置批处理运行状态
	 * @param status
	 * @param id
	 * @return
	 */
	int updateStatusById(Integer status, Integer id);
	
	/**
	 * 获取执行log
	 * @param id
	 * @return
	 */
	BatchesDetail getExecuteLog(Integer id);
	
	/**
	 * 人工执行成功添加log
	 * @param log
	 * @return
	 */
	int updateExecuteLog(Integer id, String log);
}
