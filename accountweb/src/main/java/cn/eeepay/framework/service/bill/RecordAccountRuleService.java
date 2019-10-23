package cn.eeepay.framework.service.bill;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.RecordAccountRule;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfig;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfigList;

public interface RecordAccountRuleService {
	
	Map<String,Object> insertRecordAccountRule(RecordAccountRule recordAccountRule, RecordAccountRuleConfigList recordAccountRuleConfigList) throws Exception;
	
	Map<String,Object> insertRecordAccountRuleList(List<RecordAccountRuleConfig> ruleConfigList) throws Exception;
	
	RecordAccountRule findRuleIsExist(RecordAccountRule recordAccountRule) ;
	
	List<RecordAccountRule> findRecordAccountRuleList(RecordAccountRule recordAccountRule ,Sort sort ,Page<RecordAccountRule> page) throws Exception ;
	
	RecordAccountRule findRuleByRuleNo(String ruleNo) ;
	
//	List<RecordAccountRuleConfig> findRuleConfigList(String ruleNo ,Sort sort ,Page<RecordAccountRuleConfig> page) throws Exception ;
//	List<RecordAccountRuleConfig> findRuleConfigListTow(String ruleNo) throws Exception ;
	
	List<RecordAccountRule> findSelectRule(RecordAccountRule recordAccountRule) ;
	
	int findRuleIdByNo(String ruleNo) ;
	
	int insertTransType(RecordAccountRuleTransType transType ) ;
	
//	RecordAccountRuleTransType findTransTypeIsExist(RecordAccountRuleTransType transType ) ;
//	
//	List<RecordAccountRuleTransType> findTransTypeList(RecordAccountRuleTransType transType ,Sort sort ,Page<RecordAccountRuleTransType> page) throws Exception ;
//	
//	RecordAccountRuleTransType findTransTypeById(String id) ;
	
//	int updateTransType(RecordAccountRuleTransType transType ) ;
//	
//	int deleteTransType(String id ) ;
	
	RecordAccountRule findRuleById(String ruleId) ;
	
//	RecordAccountRuleConfig findRuleConfigById(String ruleConfigId) ;

	boolean updateRecordAccountRule(RecordAccountRule rule, List<RecordAccountRuleConfig> ruleConfigList) throws Exception;
	

//	List<RecordAccountRuleTransType> findAllTransType() throws Exception ;
//	
//	RecordAccountRuleTransType getByTransCodeAndFromSystem(String transCode, String fromSystem);
//	
//	List<RecordAccountRuleConfig> findRuleConfigByRuleNo(String ruleNo);
}
