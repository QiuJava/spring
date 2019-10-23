package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OutBill;
import cn.eeepay.framework.model.bill.OutBillDetail;

public interface OutBillDetailService {
	BigDecimal updateOutAccountAmount(OutBillDetail detail);
	
	int insertOutBillDetail(OutBillDetail outBillDetail) throws Exception;
	int updateOutBillDetailById(OutBillDetail outBillDetail) throws Exception;
	int deleteOutBillDetailById(Integer id) throws Exception;
	int deleteOutBillDetailByOutBillId(Integer outBillId) throws Exception;
	List<OutBillDetail> findOutBillDetailList(OutBillDetail outBillDetail,			
			String merchantNo,
			String acqOrgNo,
			String merchantBalance1,
			String merchantBalance2,
			String outAccountTaskAmount1,
			String outAccountTaskAmount2,
			String isChangeRemark,
			Sort sort,
			Page<OutBillDetail> page) throws Exception;
	List<OutBillDetail> findExportOutBillDetailList(Map<String, String> params, OutBillDetail detail);
	
	OutBillDetail findOutBillDetailById(String id);
	
	/**
	 * 查询出账明细
	 * @param outBillDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	List<OutBillDetail> findByParams(OutBillDetail outBillDetail, Sort sort, Page<OutBillDetail> page);
	
	/**
	 * 回盘文件导入
	 * @param fdetailIdList：回盘文件失败记录集合
	 * @param sdetailIdList：回盘文件成功记录集合
	 * @param tlist: 失败的数据记录的信息集合
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateTransactionImport(List<String> fdetailIdList, List<String> sdetailIdList, List<Map<String, Object>> tlist, OutBill outBill, String uname) throws Exception;
	
	/**
	 * 根据出账单id和出款通道查询list
	 * @param outBillId
	 * @param bank
	 * @return
	 */
	List<OutBillDetail> findByOutBillIdAndBank(Integer outBillId, String bank);
	
	/**
	 * 根据出账单id和出款通道查询list
	 * @param outBillId
	 * @param bank
	 * @param status： 导入状态
	 * @return
	 */
	List<OutBillDetail> findByOutBillIdAndBank(Integer outBillId, String bank, Integer status, String date);
	
	List<OutBillDetail> findTransactionFileByParam(Integer outBillId, String acqEnname);
	
	/**
	 * 更新出账单明细导出状态和序列号
	 * @param status
	 * @param serial
	 * @param detailIds
	 * @return
	 */
	int updateExportStatusAndSerial(Integer status, String serial, String detailIds);
	
	
	List<OutBillDetail> findPartByOutBillId(Integer id);
	
	List<OutBillDetail> findPartByOutBillIdAndAcq(Integer id, String acqEnname);
	
	List<OutBillDetail> findAcqOrgByOutBillId(Integer id);
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	int insertTestBatch(List<OutBillDetail> list);
	
	int insertTestBatch2(List<OutBillDetail> list);
	
	
	int insertTestBatch3(List<OutBillDetail> list);
	
	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	BigDecimal updateBatch(Integer outBillId, List<OutBillDetail> detailList,String uname);
	
	int updateOutBillResultByBillId(String outBillResult, Integer outBillStatus, Integer outBillId);
	
	int updateOutBillResultById(String outBillResult, Integer outBillStatus, String id);
	
	/**
	 * 更新记账状态
	 */
	int updateRecordStatusById(String recordStatus, Integer id);
	
	/**
	 * 查询出款失败的出账明细
	 * @return
	 */
	List<OutBillDetail> findByOutBillStatusAndAcqEnname(Integer outBillStatus, String acqEnname);
	
	/**
	 * 查询出款失败的收单机构
	 * @return
	 */
	List<OutBillDetail> findFailedAcqOrg();
	

	/**
	 * 计算当天收单机构未出账的金额
	 * @return
	 */
	BigDecimal countOutBillAmount(String acqEnname);
	
	/**
	 * 更新回盘导入状态
	 * @param status
	 * @param detailIds
	 * @return
	 */
	int updateByOutBillDetailIds(Integer status, String detailIds);
	
	List<OutBillDetail> findByOutBillIdAndStatus(Integer outBillId, Integer status);
	
	List<OutBillDetail> findNotInDetailIds(Integer outBillid, String detailIds);
	
	int syncOutBillDetailStatus();

	List<OutBillDetail> findByOutBillId(Integer id);
}
