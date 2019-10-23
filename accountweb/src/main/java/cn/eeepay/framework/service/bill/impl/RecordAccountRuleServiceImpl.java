package cn.eeepay.framework.service.bill.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.RecordAccountRuleMapper;
import cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper;
import cn.eeepay.framework.dao.bill.RecordAccountRuleTransTypeMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.RecordAccountRule;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfig;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfigList;
import cn.eeepay.framework.service.bill.RecordAccountRuleService;



@Service("recordAccountRuleService")
@Transactional
public class RecordAccountRuleServiceImpl implements RecordAccountRuleService{
	
	@Resource
	public RecordAccountRuleConfigMapper recordAccountRuleConfigMapper ;
	@Resource
	public RecordAccountRuleMapper recordAccountRuleMapper;
	@Resource
	public RecordAccountRuleTransTypeMapper recordAccountRuleTransTypeMapper ;

	@Override
	public Map<String,Object> insertRecordAccountRule(RecordAccountRule recordAccountRule,
			RecordAccountRuleConfigList recordAccountRuleConfigList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0 ;
		int n = 0 ;
		List<RecordAccountRuleConfig> ruleConfigList = recordAccountRuleConfigList.getRecordAccountRuleConfig() ;
		
		if(ruleConfigList != null){
			for(RecordAccountRuleConfig ruleConfig:ruleConfigList){
				if(ruleConfig.getJournalNo() == null || ruleConfig.getChildTransNo() == null){
					n++ ;
					continue ;
				}
				//查询记账规则  是否存在(数据唯一性)
				RecordAccountRuleConfig ruleConfigQ = recordAccountRuleConfigMapper.findRuleConfigExist(recordAccountRule.getRuleNo(),ruleConfig.getChildTransNo(),ruleConfig.getJournalNo()) ;
				if(ruleConfigQ != null){
					throw new RuntimeException("分录号: "+ruleConfig.getJournalNo()+"、子交易标识号： "+ruleConfig.getChildTransNo()+"已存在!") ;
				}
				//设置  ruleNo 的值
				ruleConfig.setRuleNo(recordAccountRule.getRuleNo());
				//insert 记账规则配置
				n += recordAccountRuleConfigMapper.insertRuleConfig(ruleConfig) ;
			}
			

			//insert记账规则
			i = recordAccountRuleMapper.insertRecordAccountRule(recordAccountRule) ;

			if(n+i==ruleConfigList.size()+i){
				msg.put("i",n+i);
				return msg ;
			}else{
				msg.put("i",-1);
				return msg ;
			}
		}else{
			//insert记账规则
			i = recordAccountRuleMapper.insertRecordAccountRule(recordAccountRule) ;
			msg.put("i",i);
			return msg ;
		}
		
		
	}


	@Override
	public RecordAccountRule findRuleIsExist(RecordAccountRule recordAccountRule) {
		return recordAccountRuleMapper.findRuleIsExist(recordAccountRule);
	}

	@Override
	public List<RecordAccountRule> findRecordAccountRuleList(RecordAccountRule recordAccountRule, Sort sort,
			Page<RecordAccountRule> page) throws Exception {
		return recordAccountRuleMapper.findRecordAccountRuleList(recordAccountRule, sort, page);
	}

	@Override
	public RecordAccountRule findRuleByRuleNo(String ruleNo) {
		return recordAccountRuleMapper.findRuleByRuleNo(ruleNo);
	}

//	@Override
//	public List<RecordAccountRuleConfig> findRuleConfigList(String ruleNo , Sort sort,
//			Page<RecordAccountRuleConfig> page) throws Exception {
//		return recordAccountRuleConfigMapper.findRuleConfigList(ruleNo, sort, page);
//	}
//	@Override
//	public List<RecordAccountRuleConfig> findRuleConfigListTow(String ruleNo) throws Exception {
//		return recordAccountRuleConfigMapper.findRuleConfigListTow(ruleNo);
//	}

	@Override
	public List<RecordAccountRule> findSelectRule(RecordAccountRule recordAccountRule) {
		return recordAccountRuleMapper.findSelectRule(recordAccountRule);
	}

	@Override
	public int findRuleIdByNo(String ruleNo) {
		return recordAccountRuleMapper.findRuleIdByNo(ruleNo);
	}

	@Override
	public int insertTransType(RecordAccountRuleTransType transType) {
		return recordAccountRuleTransTypeMapper.insertTransType(transType);
	}

//	@Override
//	public RecordAccountRuleTransType findTransTypeIsExist(RecordAccountRuleTransType transType) {
//		return recordAccountRuleTransTypeMapper.findTransTypeIsExist(transType);
//	}
//
//	@Override
//	public List<RecordAccountRuleTransType> findTransTypeList(RecordAccountRuleTransType transType, Sort sort,
//			Page<RecordAccountRuleTransType> page) throws Exception {
//		return recordAccountRuleTransTypeMapper.findTransTypeList(transType, sort, page);
//	}
//
//	@Override
//	public RecordAccountRuleTransType findTransTypeById(String id) {
//		return recordAccountRuleTransTypeMapper.findTransTypeById(id);
//	}
//
//	@Override
//	public int updateTransType(RecordAccountRuleTransType transType) {
//		return recordAccountRuleTransTypeMapper.updateTransType(transType);
//	}

//	@Override
//	public int deleteTransType(String id) {
//		return recordAccountRuleTransTypeMapper.deleteTransType(id);
//	}

	@Override
	public RecordAccountRule findRuleById(String ruleId) {
		return recordAccountRuleMapper.findRuleById(ruleId);
	}

//	@Override
//	public RecordAccountRuleConfig findRuleConfigById(String ruleConfigId) {
//		return recordAccountRuleConfigMapper.findRuleConfigById(ruleConfigId);
//	}

	@Override
	public boolean updateRecordAccountRule(RecordAccountRule rule, List<RecordAccountRuleConfig> ruleConfigList) throws Exception {
		//记账规则处理
		RecordAccountRule ruleQ = recordAccountRuleMapper.findRuleById(rule.getRuleId().toString()) ;
		String ruleNo = ruleQ.getRuleNo();
		ruleQ.setRuleName(rule.getRuleName());
		ruleQ.setProgram(rule.getProgram());
		ruleQ.setRemark(rule.getRemark());
		ruleQ.setUpdator(rule.getUpdator());
		ruleQ.setUpdateTime(rule.getUpdateTime());
		//更新记账规则
		recordAccountRuleMapper.updateRule(ruleQ);
		
		recordAccountRuleConfigMapper.deleteRuleConfigByRuleNo(ruleNo);
		
		if (ruleConfigList != null && ruleConfigList.size() > 0) {
			for (RecordAccountRuleConfig item : ruleConfigList) {
				item.setRuleNo(ruleNo);
			}
			insertRecordAccountRuleList(ruleConfigList);
		}
		return true;
	}


	@Override
	public Map<String, Object> insertRecordAccountRuleList(
			List<RecordAccountRuleConfig> ruleConfigList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int n = 0 ;
		for(RecordAccountRuleConfig ruleConfig:ruleConfigList){
			//insert 记账规则配置
			n += recordAccountRuleConfigMapper.insertRuleConfig(ruleConfig) ;
		}
		if (n > 0) {
			msg.put("status", true);
			msg.put("msg", "保存成功");
			return msg;
		}
		else{
			msg.put("status", false);
			msg.put("msg", "保存失败");
			return msg;
		}
	}


//	@Override
//	public List<RecordAccountRuleTransType> findAllTransType() throws Exception {
//		return recordAccountRuleTransTypeMapper.findAllTransType();
//	}
//
//
//	@Override
//	public RecordAccountRuleTransType getByTransCodeAndFromSystem(
//			String transCode, String fromSystem) {
//		return recordAccountRuleTransTypeMapper.getByTransCodeAndFromSystem(transCode, fromSystem);
//	}
//
//
//	@Override
//	public List<RecordAccountRuleConfig> findRuleConfigByRuleNo(String ruleNo) {
//		return recordAccountRuleConfigMapper.findRuleConfigByRuleNo(ruleNo);
//	}

}
