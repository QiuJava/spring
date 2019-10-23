package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.MerchantInfo;


public interface FastCheckAccDetailService {

	String doCheckAccount(String acqEnname,List<CollectiveTransOrder> transOrder, Map<String,Object> map) throws Exception;	
	public Map<String,Object> confirmAccount(String checkBatchNo,String orgId) throws Exception;
	
	Map<String, Object> acqSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo,String acqOrgId) throws Exception;
	Map<String, Object> platformSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, String acqOrgId) throws Exception;
	FastCheckAccDetail findDuiAccountDetailById(String id)  throws Exception;
	/**
	 * 查询对账信息详情列表(用来导出)
	 * @return
	 * @throws Exception
	 */
	List<FastCheckAccDetail> findExportDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,FastCheckAccDetail duiAccountDetail) throws Exception;
	
	/**
	 * 查询对账信息差错详情列表(用来导出)
	 * @return
	 * @throws Exception
	 */
	List<FastCheckAccDetail> findErrorExportDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,FastCheckAccDetail duiAccountDetail) throws Exception;
	
	/**
	 * 查询对账信息详情列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<FastCheckAccDetail> findDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,FastCheckAccDetail duiAccountDetail,Sort sort,Page<FastCheckAccDetail> page) throws Exception;
	
	/**
	 * 查询差错对账信息详情列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<FastCheckAccDetail> findErrorDuiAccountDetailList(String createTimeStart ,String createTimeEnd ,FastCheckAccDetail duiAccountDetail,Sort sort,Page<FastCheckAccDetail> page) throws Exception;
	
	Map<String, Object> acqSingleSettleToMerchant(FastCheckAccDetail duiAccountDetail,CollectiveTransOrder transInfo,MerchantInfo merInfo,AcqOrg acqOrg) throws Exception;
	Map<String, Object> acqSingleBackMoneyToOwner(FastCheckAccDetail duiAccountDetail, AcqOrg acqOrg) throws Exception;
	Map<String, Object> acqSingleThaw(FastCheckAccDetail duiAccountDetail, AcqOrg acqOrg) throws Exception;
	Map<String, Object> platformSingleForDayCut(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception;
	Map<String, Object> platformSingleSettleToMerchant(FastCheckAccDetail duiAccountDetail,CollectiveTransOrder transInfo,MerchantInfo merInfo, AcqOrg acqOrg) throws Exception;
	
	int saveFastCheckAccountDetail(FastCheckAccDetail dui);
	
	
	List<FastCheckAccDetail> findErrorDuiAccountDetailList(String checkBatchNo);
	
	int updateDuiAccount(FastCheckAccDetail dui);
	
	/**
	 * 修改备注
	 * @return
	 */
	int updateRemark(FastCheckAccDetail detail);
}
