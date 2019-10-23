package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;


import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.AcqMerQo;
import cn.eeepay.framework.model.AcqMerchantInfo;
import cn.eeepay.framework.model.BusinessProductHardware;
import cn.eeepay.framework.model.MerchantInfo;
import cn.eeepay.framework.model.ServiceInfo;
import cn.eeepay.framework.model.ServiceQuota;
import cn.eeepay.framework.model.ServiceRate;
import cn.eeepay.framework.model.SysDict;

public interface MerchantInfoService {

	public MerchantInfo selectByPrimaryKey(Long id);

	public int updateByPrimaryKey(MerchantInfo record);
	    
	public List<MerchantInfo> selectAllInfo();
	
	public List<MerchantInfo> selectByMertId(String mertId);
	
	public int updateByMerId(MerchantInfo record);
	
	public MerchantInfo selectMn(String merchantNo);
	
	public MerchantInfo selectMp(String phone,String teamId);
	
	List<MerchantInfo> selectAllInfoByTermianl();
	/**
	 * gw
	 * @param agent_no
	 * @param pb_id
	 * @return
	 */
	List<ServiceRate> getServiceRatedByParams(String agent_no,String pb_id);
	
	/**
	 * gw
	 * @param agent_no
	 * @param pb_id
	 * @return
	 */
	List<ServiceQuota> getServiceQuotaByParams(String agent_no,String pb_id);
	
	List<ServiceInfo>  getServiceInfoByParams(String agent_no,String pb_id);

	public  String getMerchantNo(String mcc);
	
	public Map<String, Object> insertMerchantInfo(JSONObject json);

	List<MerchantInfo> selectByNameInfoByTermianl();
	
	MerchantInfo selectByNameInfo(String merName);
	
	List<MerchantInfo> selectByNameAllInfo(String merName);
	
	SysDict selectSysDictByKey(String key);
	
	List<SysDict> getMerTypeMcc(String syskey,String parentId);
	
	List<SysDict> getMerAllMcc(String syskey);
	List<SysDict> selectTwoInfoByParentId(String ParentId);
	
	public List<MerchantInfo> queryCardMerInfo(String cardNo,String teamId);
	
	public List<MerchantInfo> queryPhoneMerInfo(String phone,String teamId);
	List<BusinessProductHardware> queryHardWare(String bpId);
	List<SysDict> selectOneInfo();
	String findBlacklist(String rollNo,String rollType,String rollBelong);

    int countMerchantByIdCardAndTeamId(String idCardNo, String teamId);

	/**
	 * 检查商户手机号码是否已经注册
	 * @param mobilePhone	手机号码
	 * @param bpId			进件的业务产品
	 * @return	已经注册的手机号个数
	 */
	int countMerchantPhone(String mobilePhone, String bpId);

	public List<Map<String,Object>> getMerTeamsByAgentNo(String agentNo);

	public Map<String, Object> getMerGroupByTeamId(String merTeamId);

	public void saveAcqInfo(AcqMerchantInfo info, List<String> urlList);

	public void page(Page<AcqMerchantInfo> page, AcqMerQo qo);

	public AcqMerchantInfo getAcqMerInfoDetail(Long id);

	public void updateAcqInfo(AcqMerchantInfo info, List<String> urlList);

	Map<String, Object> getUserByMerNo(String merchantNo);

	List<Map<String, Object>> getBpHpByMerNo(String merchantNo);

	String selectRecommendedSource(String merchantNo);

	List<Map<String, String>> batchSelectRecommendedSource(List<String> merchantNos);

	public int getByMerchantName(String merchantName);

	void merToCjtMer(String sn, String merchantNo);
	/**
	 * create by: zhangcheng
	 * description: 查询商户下的直属商户
	 * create time: 2019/9/3 18:44
	 *
	 *[agentNo]
	 * @return java.lang.String
	 */
	String selectParentNodeByAgentNo(String agentNo);
}
