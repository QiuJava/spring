package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.DuiAccountBatch;

public interface DuiAccountBatchService {
	int insertDuiAccountBatch(DuiAccountBatch dui) throws Exception;
	int updateDuiAccountBatch(DuiAccountBatch dui) throws Exception;
	/**
	 * 删除对账信息，只有当该对账批次中所有明细的所有差次处理状态都为待处理时，才允许删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	int deleteDuiAccountBatch(Integer id) throws Exception;
	DuiAccountBatch findDuiAccountBatchByFileNameAndAcqEnname(String fileName,String acqEnname) throws Exception;
	/**
	 * 查询所有的对账信息
	 * @param duiAccountBatch
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<DuiAccountBatch> queryDuiAccountList(DuiAccountBatch duiAccountBatch, Sort sort, Page<DuiAccountBatch> page) throws Exception;
	
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
	 * 通过 组织机构编号 获取到 组织机构名称
	 * @param AcqEnname
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAcqCnnameByAcqEnname(String acqEnname) throws Exception ;

	/**
	 * 更新记账状态
	 * @param checkBatchNo
	 * @param recordStatus
	 * @return
	 */
	int updateRecordStatus(String checkBatchNo, Integer recordStatus);
	DuiAccountBatch getByCheckBatchNo(String checkBatchNo);
	
}
