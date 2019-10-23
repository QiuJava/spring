package cn.eeepay.framework.dao.bill;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.RecordAccountRule;

/**
 * 记账规则
 * @author zouruijin
 * zrj@eeepay.cn rjzou@qq.com
 * 2016年12月3日14:04:59
 *
 */
public interface RecordAccountRuleMapper {
	
	@Insert("INSERT INTO record_account_rule(rule_no,rule_name,program,remark,creator,create_time,updator,update_time) "
			+ "VALUES(#{rule.ruleNo},#{rule.ruleName},#{rule.program},#{rule.remark},#{rule.creator},#{rule.createTime},#{rule.updator},#{rule.updateTime})")
	int insertRecordAccountRule(@Param("rule")RecordAccountRule rule ) ;
	
//	@Insert("INSERT INTO record_account_rule_config(rule_no,journal_no,child_trans_no,account_flag,debit_credit_side,debit_credit_flag,subject_no,remark,account_type,currency_no,amount) "
//			+ "VALUES(#{ruleConfig.ruleNo},#{ruleConfig.journalNo},#{ruleConfig.childTransNo},#{ruleConfig.accountFlag},#{ruleConfig.debitCreditSide},"
//			+ "#{ruleConfig.debitCreditFlag},#{ruleConfig.subjectNo},#{ruleConfig.remark},#{ruleConfig.accountType},#{ruleConfig.currencyNo},#{ruleConfig.amount})")
//	int insertRuleConfig(@Param("ruleConfig")RecordAccountRuleConfig ruleConfig ) ;
	
	
	@SelectProvider( type=SqlProvider.class,method="findSelectRule")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleMapper.BaseResultMap")
	List<RecordAccountRule> findSelectRule(@Param("rule")RecordAccountRule rule) ;

	@Select("SELECT rule_id FROM record_account_rule WHERE rule_no = #{ruleNo}")
	@ResultType(Integer.class)
	int findRuleIdByNo(@Param("ruleNo")String ruleNo) ;
	
	@Select("SELECT * from record_account_rule where rule_name = #{rule.ruleName} AND program = #{rule.program}")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleMapper.BaseResultMap")
	RecordAccountRule findRuleIsExist(@Param("rule")RecordAccountRule rule ) ;
	
	@SelectProvider( type=SqlProvider.class,method="findRecordAccountRuleList")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleMapper.BaseResultMap")
	List<RecordAccountRule> findRecordAccountRuleList(@Param("rule")RecordAccountRule rule ,@Param("sort")Sort sort ,Page<RecordAccountRule> page) ;
	
	@Select("SELECT * from record_account_rule where rule_no = #{ruleNo}")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleMapper.BaseResultMap")
	RecordAccountRule findRuleByRuleNo(@Param("ruleNo")String ruleNo ) ;
	
//	@Select("SELECT * from record_account_rule_config where rule_no = #{ruleNo}")
//	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper.BaseResultMap")
//	List<RecordAccountRuleConfig> findRuleConfigList(@Param("ruleNo")String ruleNo ,@Param("sort")Sort sort ,Page<RecordAccountRuleConfig> page) ;
	
//	@Select("SELECT rc.*,sub.subject_name from record_account_rule_config rc LEFT OUTER JOIN bill_subject sub ON rc.subject_no = sub.subject_no "
//			+ "where rc.rule_no = #{ruleNo}")
//	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper.BaseResultMap")
//	List<RecordAccountRuleConfig> findRuleConfigListTow(@Param("ruleNo")String ruleNo ) ;
	
	@Select("SELECT * from record_account_rule where rule_id = #{ruleId}")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleMapper.BaseResultMap")
	RecordAccountRule findRuleById(@Param("ruleId")String ruleId ) ;
	
//	@Select("SELECT * from record_account_rule_config where rule_config_id = #{ruleConfigId}")
//	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper.BaseResultMap")
//	RecordAccountRuleConfig findRuleConfigById(@Param("ruleConfigId")String ruleConfigId ) ;
	
//	@Select("SELECT * from record_account_rule_config where rule_no = #{ruleNo}")
//	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper.BaseResultMap")
//	List<RecordAccountRuleConfig> findRuleConfigByRuleNo(@Param("ruleNo")String ruleNo);
	
	@Update("UPDATE record_account_rule "
			+ " SET rule_name = #{rule.ruleName},program = #{rule.program},remark = #{rule.remark},"
			+ " updator = #{rule.updator},update_time = #{rule.updateTime} "
			+ "WHERE rule_id = #{rule.ruleId}")
	int updateRule(@Param("rule")RecordAccountRule rule) ;
	
//	@Update("UPDATE record_account_rule_config "
//			+ "SET child_trans_no = #{ruleConfig.childTransNo},account_flag = #{ruleConfig.accountFlag},debit_credit_side = #{ruleConfig.debitCreditSide}"
//			+ ",debit_credit_flag = #{ruleConfig.debitCreditFlag},subject_no = #{ruleConfig.subjectNo},remark = #{ruleConfig.remark}"
//			+ ",journal_no = #{ruleConfig.journalNo},account_type = #{ruleConfig.accountType},currency_no = #{ruleConfig.currencyNo},amount = #{ruleConfig.amount} "
//			+ "WHERE rule_config_id = #{ruleConfig.ruleConfigId}")
//	int updateRuleConfig(@Param("ruleConfig")RecordAccountRuleConfig ruleConfig) ;
	
//	@Select("SELECT rule_config_id FROM record_account_rule_config WHERE child_trans_no = #{childTransNo} AND rule_no = #{ruleNo}")
//	Integer findRuleConfigId(@Param("childTransNo")String childTransNo ,@Param("ruleNo")String ruleNo) ;
	
//	@Delete("DELETE FROM record_account_rule_config WHERE rule_config_id = #{ruleConfigId}")
//	int deleteRuleConfig(@Param("ruleConfigId")String ruleConfigId) ;

//	@Select("SELECT * from record_account_rule_config where child_trans_no = #{childTransNo} and journal_no = #{journalNo} and rule_no = #{ruleNo}")
//	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleConfigMapper.BaseResultMap")
//	RecordAccountRuleConfig findRuleConfigExist(@Param("ruleNo")String ruleNo, @Param("childTransNo")String childTransNo, @Param("journalNo")String journalNo ) ;
	
	
	public class SqlProvider{
		public String findSelectRule(final Map<String,Object> parameter){
		final RecordAccountRule rule = (RecordAccountRule)parameter.get("rule") ;
		return new SQL(){{
			SELECT(" rule_no,rule_name ");
			FROM(" record_account_rule");
			/*if (rule.getRuleNo()!=null || rule.getRuleNo()!="")
				WHERE(" rule_no like  \"%\"#{rule.ruleNo}\"%\" or rule_name like  \"%\"#{rule.ruleNo}\"%\" ");*/
			if (!StringUtils.isBlank(rule.getRuleNo()))
				WHERE(" rule_no like  \"%\"#{rule.ruleNo}\"%\" or rule_name like  \"%\"#{rule.ruleNo}\"%\" ");
		}}.toString() ;
	}
		public String findRecordAccountRuleList(final Map<String,Object> parameter){
			final RecordAccountRule rule = (RecordAccountRule)parameter.get("rule") ;
			return new SQL(){{
				SELECT("*");
				FROM(" record_account_rule");
				if (!StringUtils.isBlank(rule.getRuleName()) )
					WHERE(" rule_name like  \"%\"#{rule.ruleName}\"%\" ");
				if (!StringUtils.isBlank(rule.getProgram()))
					WHERE(" program like  \"%\"#{rule.program}\"%\" ");
			}}.toString() ;
		}
		
	}
	
}
