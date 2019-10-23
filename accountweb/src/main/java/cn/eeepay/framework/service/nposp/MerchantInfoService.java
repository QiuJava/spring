package cn.eeepay.framework.service.nposp;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.TransInfoPreFreezeLog;

public interface MerchantInfoService {
	/**
	 * 查询商户 根据 id
	 * @return
	 */
	public MerchantInfo findMerchantInfoByUserId(String userId);

	public Map<String,Object> findMerchantInfoByBpmIdAndChannelCode(String bpmId,String channelCode);

	/**
	 * 查询商户 
	 * @param merchantNo：商户编号
	 * @param merchantName：商户名称
	 * @param mobile：电话号码
	 * @return : 商户编号集合
	 */
	public List<String> findByNameAndMobile(String merchantNo, String merchantName, String mobile);
	

	/**
	 * 查询 商户编号  通过  用户名、手机号
	 * @param userName
	 * @param mobilephone
	 * @return
	 */
	public List<String> findMerchantListByParams(String userName ,String mobilephone);

	Map<String, Object> queryQrMerchantInfo(String acqMerchantNo);
	Map<String, Object> queryQrMerInfo(String merchantNo);
	public void insertLogAndUpdateMerchantInfoAmount(TransInfoPreFreezeLog record);
	public void insertPreFreezeLog(TransInfoPreFreezeLog record) ;
}
