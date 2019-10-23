package cn.eeepay.framework.service.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.TransInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface DuiAccountDetailService {
	//对账信息改为从  check_account_detail 中获取
	List<DuiAccountDetail> getCheckDetailTransInfos(String acqEnname,String beginTime,String endtime) throws Exception;
	DuiAccountDetail queryByAcqDbDetailInfo(String acqOrderNo, String acqEnname);
	DuiAccountDetail queryByAcqDbDetailInfoNoChecked(String acqOrderNo, String acqEnname);
	DuiAccountDetail queryByAcqDbDetail(String acqReferenceNo,String acqMerchantNo, String acqEnname, String acqSerNo, String acqTerNo);
	List<DuiAccountDetail> getYinShengCheckDetailTransInfos(String acqEnname, Date parse) throws Exception;
	int updateDuiAccountDetailByOrderReNum(SubOutBillDetail subOutBillDetail, String  isAddBill);

	DuiAccountDetail queryByAcqDbDetailInfo1(String acqReferenceNo, String acqEnname);

	int insertDuiAccountDetail(DuiAccountDetail dui) throws Exception;
	int updateDuiAccountDetail(DuiAccountDetail dui) throws Exception;

	/**
	 * 通过平台订单号批量更新对账明细表
	 * @param list
	 * @return
	 */
	int updateDuiAccountDetailBatchByPlateOrderNo(List<DuiAccountDetail> list);
	
	/**
	 * 查询所有的对账明细表数据
	 * @return
	 */
	List<DuiAccountDetail> findAllDuiAccountDetailList();
	
	int deleteByCheckBatchNo(String checkBatchNo);
	int deleteDuiAccountDetail(Integer id) throws Exception;
	int deleteDuiAccountDetailByParams(String acqMerchantNo,String acqTerminalNo,String acqBatchNo,String acqSerialNo,String acqAccountNo) throws Exception;
	List<DuiAccountDetail> findDuiAccountDetailListByStartEndTransTime(String startTime,String endTime) throws Exception;
	int saveDuiAccountDetail(DuiAccountDetail dui) throws Exception;
	String doDuiAccount(String acqEnname,List<TransInfo> transInfos, Map<String,Object> map) throws Exception;
	DuiAccountDetail findDuiAccountDetailByTransInfo(TransInfo transInfo)  throws Exception;
	List<TransInfo> findDuiTransInfos(String acqEnname, Date transDate)  throws Exception;
	
	/**
	 * 查询对账信息详情列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<DuiAccountDetail> findDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,DuiAccountDetail duiAccountDetail,Sort sort,Page<DuiAccountDetail> page) throws Exception;
	/**
	 * 查询对账信息详情列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<DuiAccountDetail> findErrorDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,DuiAccountDetail duiAccountDetail,Sort sort,Page<DuiAccountDetail> page) throws Exception;
	
	/**
	 * 查询对账信息详情列表(用来导出)
	 * @return
	 * @throws Exception
	 */
	List<DuiAccountDetail> findExportDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,DuiAccountDetail duiAccountDetail) throws Exception;

	/**
	 * 查询对账信息详情列表(差错帐用来导出)
	 * @return
	 * @throws Exception
	 */
	List<DuiAccountDetail> findErrorExportDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,DuiAccountDetail duiAccountDetail) throws Exception;
	
	DuiAccountDetail findDuiAccountDetailById(String id)  throws Exception;
	Map<String, Object> platformSingleMarkSuspect(DuiAccountDetail duiAccountDetail, TransInfo transInfo, MerchantInfo merInfo, String acqOrgId) throws Exception;
	Map<String, Object> acqSingleMarkSuspect(DuiAccountDetail duiAccountDetail, TransInfo transInfo,String acqOrgId) throws Exception;
	Map<String, Object> acqSingleSettleToMerchant(DuiAccountDetail duiAccountDetail,AcqOrg acqOrg) throws Exception;
	Map<String, Object> acqSingleBackMoneyToOwner(DuiAccountDetail duiAccountDetail,AcqOrg acqOrg) throws Exception;
	Map<String, Object> acqSingleThaw(DuiAccountDetail duiAccountDetail,AcqOrg acqOrg) throws Exception;
	Map<String, Object> platformSingleForDayCut(DuiAccountDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception;
	Map<String, Object> platformSingleSettleToMerchant(DuiAccountDetail duiAccountDetail,CollectiveTransOrder transInfo,MerchantInfo merInfo, AcqOrg acqOrg) throws Exception;
	
	
	public boolean duiAccountFileDown(Map<String,String> params);
	public Map<String,Object> confirmAccount(String checkBatchNo,String orgId) throws Exception;
	
	List<DuiAccountDetail> findErrorDuiAccountDetailList(String checkBatchNo);

	DuiAccountDetail findError(String orderReferenceNo,String acqEnname)  throws Exception;
	DuiAccountDetail findAcq(DuiAccountDetail duiAccountDetail);
	DuiAccountDetail findPlate(DuiAccountDetail duiAccountDetail);
	int updateDuiAccount(DuiAccountDetail dui);

	int updateDuiAccountDetailAuto(DuiAccountDetail dbDetail,DuiAccountDetail detail)throws Exception ;
	int updatePlateDuiAccountDetailAuto(DuiAccountDetail dbDetail,DuiAccountDetail detail)throws Exception ;
	/**
	 * 修改备注
	 * @return
	 */
	int updateRemark(DuiAccountDetail detail);
	int updateErrorCheck(DuiAccountDetail detail);
	List<DuiAccountDetail> findAllTranByAcqNameAndDate(String acqEnname, Date transTime, String startTime, String endTime);
	DuiAccountDetail queryDuiAccountDetailByOrderRefNum(SubOutBillDetail subOutBillDetailExcel);
	void installKqzqDetailByInfo(TransInfo transInfo1, String orderNo, DuiAccountDetail detail, String acqEnname);
	
	
	List<DuiAccountDetail> findDuiAccountDetailListByTransTime(String transTime);
	
	List<DuiAccountDetail> findNoCheckedDuiAccountDetailListByTransTime(String transTime);

	public int updateDuiAccError(Integer id,String treatmentMethod,String freezeStatus) throws Exception;
	DuiAccountDetail findDuiAccountDetailByOrderReferenceNo(String orderNo);

	DuiAccountDetail findDuiAccountDetailByAcqOrderNoAndDate(String orderNo,String date1,String date2);


	DuiAccountDetail findDuiAccountDetailByAcqReferenceNoAndDate(String plateAcqReferenceNo, String s, String s1);

	DuiAccountDetail findDuiAccountDetailByAcqMerchantOrderNoAndDate(String plateAcqReferenceNo, String s, String s1);

	int updateDuiAccountForT1(DuiAccountDetail dui);

	int updateDuiAccountStatus(DuiAccountDetail dui);


}
