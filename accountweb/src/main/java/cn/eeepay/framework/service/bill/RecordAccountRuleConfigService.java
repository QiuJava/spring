package cn.eeepay.framework.service.bill;

import java.util.List;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfig;

public interface RecordAccountRuleConfigService {
	
//	Map<String,Object> insertRecordAccountRule(RecordAccountRule recordAccountRule, RecordAccountRuleConfigList recordAccountRuleConfigList) throws Exception ;
//	
//	RecordAccountRule findRuleIsExist(RecordAccountRule recordAccountRule) ;
//	
//	List<RecordAccountRule> findRecordAccountRuleList(RecordAccountRule recordAccountRule ,Sort sort ,Page<RecordAccountRule> page) throws Exception ;
//	
//	RecordAccountRule findRuleByRuleNo(String ruleNo) ;
	
	List<RecordAccountRuleConfig> findRuleConfigList(String ruleNo ,Sort sort ,Page<RecordAccountRuleConfig> page) throws Exception ;
	List<RecordAccountRuleConfig> findRuleConfigListTow(String ruleNo) throws Exception ;
	
//	List<RecordAccountRule> findSelectRule(RecordAccountRule recordAccountRule) ;
//	
//	int findRuleIdByNo(String ruleNo) ;
	
//	int insertTransType(RecordAccountRuleAndTransType transType ) ;
//	
//	RecordAccountRuleAndTransType findTransTypeIsExist(RecordAccountRuleAndTransType transType ) ;
//	
//	List<RecordAccountRuleAndTransType> findTransTypeList(RecordAccountRuleAndTransType transType ,Sort sort ,Page<RecordAccountRuleAndTransType> page) throws Exception ;
//	
//	RecordAccountRuleAndTransType findTransTypeById(String id) ;
//	
//	int updateTransType(RecordAccountRuleAndTransType transType ) ;
//	
//	int deleteTransType(String id ) ;
	
//	RecordAccountRule findRuleById(String ruleId) ;
	
	RecordAccountRuleConfig findRuleConfigById(String ruleConfigId) ;

//	boolean updateRecordAccountRule(RecordAccountRule rule, List<RecordAccountRuleConfig> ruleConfigList);
	

//	List<RecordAccountRuleAndTransType> findAllTransType() throws Exception ;
//	
//	RecordAccountRuleAndTransType getByTransCodeAndFromSystem(String transCode, String fromSystem);
	
	List<RecordAccountRuleConfig> findRuleConfigByRuleNo(String ruleNo);
}
