package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.FastCheckAccountBatch;



public interface FastCheckAccBatchService {
	FastCheckAccountBatch getByCheckBatchNo(String checkBatchNo);
	public FastCheckAccountBatch queryBatchByFileName(String fileName, String acqEnname);
	void saveFastCheckAccountBatch(FastCheckAccountBatch fb);
	List<FastCheckAccountBatch> queryDuiAccountList(Map<String, String> params, Sort sort, Page<FastCheckAccountBatch> page) throws Exception;
	int deleteFastAccountBatch(Integer id) throws Exception;
	
	/**
	 * 获取长款总数，金额总数
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAcqTransAmountSumAndCount(String checkBatchNo) throws Exception;
	
	/**
	 * 获取短款总数，金额总数
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPlateTransAmountSumAndCount(String checkBatchNo) throws Exception ;
	
	/**
	 * 获取渠道交易总数，总手续费, 金额总数
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAcqTransSumAndCount(String checkBatchNo) throws Exception ;
	
	/**
	 *  获取平台交易总数，金额总数
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPlateSumAndCount(String checkBatchNo) throws Exception ;
	
	/**
	 *  获取我方平台交易总数，金额总数
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPlateSumAndCountMe(String checkBatchNo) throws Exception ;
	
	/**
	 * 更新记账状态
	 * @param checkBatchNo
	 * @param recordStatus
	 * @return
	 */
	int updateRecordStatus(String checkBatchNo, Integer recordStatus);
	
	void test() throws Exception; 
}
