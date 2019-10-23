package cn.eeepay.framework.service.bill;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BusinessAccount;
import cn.eeepay.framework.model.bill.BusinessAccountDetail;

public interface BusinessAccountService {
	/**
	 * 业务调账记录多条列表分页查询
	 * @param adjustAccount
	 * @param params
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<BusinessAccount> findBusinessAccount(BusinessAccount account,Map<String, String> params,Sort sort,Page<BusinessAccount> page) throws Exception;

	/**
	 * 插入业务调账
	 * @param account
	 * @param file
	 * @return
	 */
	String insertBusinessAccount(BusinessAccount account, File file);
	
	/**
	 * 更新业务调账
	 * @param adjustAccount
	 * @param file
	 * @return
	 * @throws Exception
	 */
	String updateBusiness(BusinessAccount adjustAccount, File file);
	
	/**
	 * 根据id查询调账信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	BusinessAccount getBusinessAccount(Integer id) throws Exception;
	
	/**
	 * 只修改 AdjustAccount（即点击提交修改按钮而没有上传调账文件时）
	 * @param adjustAccount
	 * @return
	 */
	String updateBusinessAccount(BusinessAccount adjustAccount);
	
	/**
	 * 查询业务调账详情
	 * @param adjustDetail
	 * @param params
	 * @param sort
	 * @param page
	 * @return
	 * @throws Exception
	 */
	List<BusinessAccountDetail> findBusinessAccountDetail(BusinessAccountDetail adjustDetail,Map<String, String> params,Sort sort,Page<BusinessAccountDetail> page);
	
	/**
	 * 提交审核
	 * @param adjustAccount
	 * @return
	 * @throws Exception
	 */
	int updateBusinessExamineDate(BusinessAccount adjustAccount);
	
	/**
	 * 审核
	 * @param adjustAccount
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> updateBusinessExamine(BusinessAccount adjustAccount);
}
