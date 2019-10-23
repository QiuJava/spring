package cn.eeepay.framework.service.impl;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.AutoCheckRoute;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.AutoCheckRuleDao;
import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;
import cn.eeepay.framework.service.AutoCheckRuleService;

@Service("autoCheckRuleService")
@Transactional
public class AutoCheckRuleServiceImpl implements AutoCheckRuleService {

	@Resource
	private AutoCheckRuleDao autoCheckRuleDao;

	
	@Override
	public AutoCheckRoute selectByChannelCode(String channelCode) {
		return autoCheckRuleDao.selectByChannelCode(channelCode);
		
	}

	@Override
	public SysConfigAutoCheck selectByParamKey(String paramKey) {
		return autoCheckRuleDao.selectByParamKey(paramKey);

	}
	
	@Override
	public List<AutoCheckRule> selectAll() {
		
		return autoCheckRuleDao.selectAll();
	}
	
	@Override
	public int updateState(AutoCheckRule autoCheckRule) {
		return autoCheckRuleDao.updateState(autoCheckRule);
	}

	@Override
	public int updateValue(Map<String, Object> param) {
		int i=0;
		i = autoCheckRuleDao.updateValue("phoho_comp_prop",param.get("phohoCompProp").toString());
		i++;
		i = autoCheckRuleDao.updateValue("bank_card_ocr",param.get("bankCardOcr").toString());
		i++;
		i = autoCheckRuleDao.updateValue("id_card_ocr",param.get("idCardOcr").toString());
		i++;
		i = autoCheckRuleDao.updateValue("single_merch_times",param.get("singleMerchTimes").toString());
		i++;
		i = autoCheckRuleDao.updateValue("age_limit",param.get("minAge").toString()+"_"+param.get("maxAge").toString());
		i++;
		i = autoCheckRuleDao.updateValue("age_limit",param.get("minAge").toString()+"_"+param.get("maxAge").toString());
		i++;
		// 更新通道配置
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("livingJskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("livingXskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("livingXinLian"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("ocrJskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("ocrXskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("ocrXinLian"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("ocrYlsw"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("realJskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("realXskj"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("realXinLian"));
		i++;
		i = autoCheckRuleDao.updateRoutePercent((Map)param.get("realYlsw"));
		i++;
		return i;
	}

	@Override
	public int updateIsOpen(AutoCheckRule info) {
		return autoCheckRuleDao.updateIsOpen(info);
	}
	@Override
	public int updateIsPass(AutoCheckRule info) {
		return autoCheckRuleDao.updateIsPass(info);
	}

	@Override
	public List<AutoCheckRoute> listByRouteType(int i) {
		return autoCheckRuleDao.findByRouteType(i);
	}

}
