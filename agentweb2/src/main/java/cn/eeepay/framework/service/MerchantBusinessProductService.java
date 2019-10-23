package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.boss.action.MerchantBusinessProductAction;
import cn.eeepay.boss.action.MerchantBusinessProductAction.SelectParams;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.BusinessProductDefine;
import cn.eeepay.framework.model.ExaminationsLog;
import cn.eeepay.framework.model.MerchantBusinessProduct;
import cn.eeepay.framework.model.MerchantCardInfo;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.MerchantRequireItem;
import cn.eeepay.framework.model.MerchantServiceQuota;
import cn.eeepay.framework.model.MerchantServiceRate;

public interface MerchantBusinessProductService {

	public MerchantBusinessProduct selectByPrimaryKey(Long id);

	public List<MerchantBusinessProduct> selectAllByStatusInfo(Page<MerchantBusinessProduct> page, String agentNo);

	public List<MerchantBusinessProduct> selectAllInfo(Page<MerchantBusinessProduct> page, String agentNo);

	public List<MerchantBusinessProduct> selectByParam(Page<MerchantBusinessProduct> page, SelectParams selectParams,
			String loginAgentNo);

	List<MerchantBusinessProduct> exportMerchantInfo(SelectParams selectParams, String loginAgentNo);

	public List<MerchantBusinessProduct> selectByStatusParam(Page<MerchantBusinessProduct> page,
			SelectParams selectParams, String agentNo);

	public int updateBymbpId(Long mbpId, String status, String auditorId);

	public int updateByItemAndMbpId(MerchantBusinessProduct record, MerchantInfo mis, List<MerchantRequireItem> mris,
			List<MerchantServiceRate> msr, List<MerchantServiceQuota> msq, int status);

	public Object updateMerBusiOProInfo(int num, String merbpId, String auditorId);

	public List<String> querySerivceId(String bpId);

	public boolean replaceBusinessProduct(String merchantNo, String oldBpId, String newBpId, String operationAgentNo);

	public Map<String, Object> findFunctionManage(String functionNumber);

	public Map<String, Object> findActivityIsSwitch(String oneAgentNo, String functionNumber);

	public List<BusinessProductDefine> selectGroupBpInfo(String agentNo, String bpId, String bpId2);

	public Map<String, Object> selectMerBpInfo(String merchantNo, String bpId);

	public List<Map<String, Object>> selectTerBpInfo(String merchantNo, String bpId);

	public Integer selectMechant(SelectParams selectParams, String agentNo);

	/**
	 * 查询商户成功的进件
	 * 
	 * @author: gaowei
	 * @date: 2017年4月19日 下午2:24:06
	 * @param merchantNo
	 * @return
	 */
	public List<MerchantBusinessProduct> selectMerPro(String merchantNo);

	/**
	 * 查询商户对应业务产品
	 * 
	 * @param merId
	 * @return
	 */
	List<MerchantBusinessProduct> selectByParam(String merId);

	boolean isOpenAgentUpdateBpSwitch(String agentNo);
	
}
