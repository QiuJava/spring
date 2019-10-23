package cn.eeepay.framework.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.annotation.Resource;

import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.MerchantInfoDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.model.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.SuperPushDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.SuperPushService;

@Service("superPushService")
public class SuperPushServiceImpl implements SuperPushService {
	private static final Logger log = LoggerFactory.getLogger(SuperPushServiceImpl.class);

	@Resource
	private SuperPushDao superPushDao;
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private MerchantInfoDao merchantInfoDao;
	@Resource
	private SysDictDao sysDictDao;

	@Override
	public List<SuperPush> listSuperPushMerchant(SuperPush superPush, String loginAgentNo, Page<SuperPush> page) {
		AgentInfo loginAgentInfo = agentInfoDao.selectByAgentNo(loginAgentNo);
		if (StringUtils.isNotBlank(superPush.getAgentNo())){
			AgentInfo searchAgentInfo = agentInfoDao.selectByAgentNo(superPush.getAgentNo());
			if (searchAgentInfo != null){
				superPush.setAgentNode(searchAgentInfo.getAgentNode());
			}
		}
		String bpId = sysDictDao.getSuperPushBpId();
		return superPushDao.listSuperPushMerchant(superPush, loginAgentInfo, bpId, page);
	}

	@Override
	public Map<String,Object> getSuperPushMerchantDetail(String userId) {
		Map<String,Object> map = new HashMap<>();
		SuperPush superPush = superPushDao.getSuperPushUser(userId);
		if (superPush == null){
			return null;
		}
		map.put("superPush", superPush);
		if (StringUtils.isNotBlank(superPush.getOneLevelId())){
			map.put("oneLevelAgent", agentInfoDao.selectByAgentNo(superPush.getOneLevelId()));
		}
		String merchantNo = superPush.getMerchantNo();
		if (StringUtils.isBlank(merchantNo)){
			return map;
		}
		MerchantInfo merchantInfo = superPushDao.selectMerchantDetail(merchantNo);
		if (merchantInfo == null){
			return map;
		}
		if (StringUtils.isNotBlank(merchantInfo.getIdCardNo()) &&
				merchantInfo.getIdCardNo().length() >= 18){
			merchantInfo.setIdCardNo("**************" + merchantInfo.getIdCardNo().substring(14,18));
		}
		map.put("merchantInfo", merchantInfo);//商户信息
		map.put("terminalInfo", superPushDao.selectFromTerminalInfo(merchantNo));
		MerchantCardInfo merchantCardInfo = superPushDao.selectMerchantCardInfo(merchantNo);
		if (merchantCardInfo != null){
			String accountNo = merchantCardInfo.getAccountNo();
			merchantCardInfo.setAccountNo("***************" + accountNo.substring(accountNo.length()-4,accountNo.length()));
			map.put("merchantCardInfo", merchantCardInfo);
		}
		return map;
	}

	@Override
	public List<SuperPushShare> listSuperPushShare(SuperPushShare superPushShare, String loginAgentNo, Page<SuperPushShare> page) {
//		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
//		if (StringUtils.isNotBlank(superPushShare.getAgentNo())){
//			AgentInfo searchAgent = agentInfoDao.selectByAgentNo(superPushShare.getAgentNo());
//			if (searchAgent != null){
//				superPushShare.setAgentNode(searchAgent.getAgentNode());
//			}
//		}
		return superPushDao.listSuperPushShare(superPushShare, loginAgentNo, page);
	}

	@Override
	public List<SuperPushShare> exportSuperPushShare(SuperPushShare superPushShare, String loginAgentNo) {
//		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
//		if (StringUtils.isNotBlank(superPushShare.getAgentNo())){
//			AgentInfo searchAgent = agentInfoDao.selectByAgentNo(superPushShare.getAgentNo());
//			if (searchAgent != null){
//				superPushShare.setAgentNode(searchAgent.getAgentNode());
//			}
//		}
		return superPushDao.exportSuperPushShare(superPushShare, loginAgentNo);
	}

	@Override
	public Map<String, Object> countSuperPushShare(SuperPushShare superPushShare, String loginAgentNo) {
//		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
//		if (StringUtils.isNotBlank(superPushShare.getAgentNo())){
//			AgentInfo searchAgent = agentInfoDao.selectByAgentNo(superPushShare.getAgentNo());
//			if (searchAgent != null){
//				superPushShare.setAgentNode(searchAgent.getAgentNode());
//			}
//		}
		Map<String, Object> resultMap = superPushDao.countSuperPushShare(superPushShare, loginAgentNo);
        BigDecimal transAmount = superPushDao.countSuperPushTransAmount(superPushShare, loginAgentNo);
        if(resultMap != null){
			resultMap.put("transAmount", transAmount == null ? "0" : transAmount.setScale(2).toString());
		}
		return resultMap;
	}

    @Override
    public String getTodaySuperPushAmount(String loginAgentNo) {
        return superPushDao.getTodaySuperPushAmount(loginAgentNo);
    }
//	@Resource
//	private SuperPushDao superPushDao;
//
//	@Override
//	public List<SuperPush> selectSuperPush(Page<SuperPush> page,String phone,SuperPush superPush) {
//		List<SuperPush> list = superPushDao.selectSuperPush(page,phone,superPush);
//		for (SuperPush superPush2 : list) {
//			String params = ClientInterface.getSuperPushUserBalance(superPush2.getMerchantNo());
//			JSONObject json = JSON.parseObject(params);
//			if (json.getBoolean("status")) {
//				superPush2.setBalance(json.getBigDecimal("avaliBalance"));
//			}else{
//				log.error(json.getString("msg") + "商户号" + superPush2.getMerchantNo());
//			}
//		}
//		return list;
//	}
//
//	@Override
//	public Map<String, Object> selectMerchantDetail(String mertId) {
//		Map<String,Object> map = new HashMap<String, Object>();
//		MerchantInfo merchantInfo = superPushDao.selectMerchantDetail(mertId);
//		merchantInfo.setIdCardNo("**************" + merchantInfo.getIdCardNo().substring(14,18));
//		map.put("merchantInfo", merchantInfo);//商户信息
//		map.put("superPush", superPushDao.selectFromSuperPushUser(mertId));
//		map.put("terminalInfo", superPushDao.selectFromTerminalInfo(mertId));
//		MerchantCardInfo merchantCardInfo = superPushDao.selectMerchantCardInfo(mertId);
//		String accountNo = merchantCardInfo.getAccountNo();
//		merchantCardInfo.setAccountNo("***************" + accountNo.substring(accountNo.length()-4,accountNo.length()));
//		map.put("merchantCardInfo", merchantCardInfo);
//		return map;
//	}
//
//	@Override
//	public Map<String, Object> selectCashDetail(String mertId) {
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("cashDetail", superPushDao.selectCashDetail(mertId));
//		return map;
//	}
//
//	@Override
//	public List<SuperPushShare> selectShareDetail(Page<SuperPush> page, SuperPushShare info) {
//		return superPushDao.selectShareDetail(page,info);
//	}
//
//	@Override
//	public List<SuperPushShare> exportTransInfo(SuperPushShare info){
//		return superPushDao.exportTransInfo(info);
//	}
//
//	@Override
//	public SuperPush getCashMerchantDetail(String merchantNo) {
//		return superPushDao.getCashMerchantDetail(merchantNo);
//	}
//
//	@Override
//	public BigDecimal getTotalAmount(String merchantNo) {
//		return superPushDao.getTotalAmount(merchantNo);
//	}
//
//	/**
//	 * 获取商户账号余额
//	 * @param merNo
//	 * @return
//	 */
//	@Override
//	public BigDecimal getSuperPushUserBalance(String merNo) {
//		BigDecimal avaliBalance = null;
//		String str = ClientInterface.getSuperPushUserBalance(merNo);
//		log.info("查询超级推商户余额，商户号：{}，返回结果[{}]",merNo,str);
//		JSONObject json = JSON.parseObject(str);
//		if ((boolean) json.get("status")) {// 返回成功
//			AccountInfo ainfo = JSON.parseObject(str, AccountInfo.class);
//			avaliBalance = ainfo.getAvaliBalance();
//		}
//		return avaliBalance;
//	}
//	@Override
//	public List<SettleOrderInfo> getCashPage(SettleOrderInfo settleOrderInfo, Page<SuperPush> page) {
//		return superPushDao.getCashPage(settleOrderInfo, page);
//	}
	
}
