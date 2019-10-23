package cn.eeepay.framework.dao.bill;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
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
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;

/**
 * 交易类型
 * @author Administrator
 *
 */
public interface RecordAccountRuleTransTypeMapper {
	
	@Insert("INSERT INTO record_account_rule_trans_type(trans_type_name,from_system,rule_id,remark,trans_type_code,creator,create_time,updator,update_time,trans_group) "
			+ " VALUES(#{transType.transTypeName},#{transType.fromSystem},#{transType.ruleId},#{transType.remark},#{transType.transTypeCode},"
			+ " #{transType.creator},#{transType.createTime},#{transType.updator},#{transType.updateTime},#{transType.transGroup})")
	int insertTransType(@Param("transType")RecordAccountRuleTransType transType ) ;
	
	@Select("SELECT * FROM record_account_rule_trans_type WHERE trans_type_name = #{transType.transTypeName} AND from_system = #{transType.fromSystem}")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleTransTypeMapper.BaseResultMap")
	RecordAccountRuleTransType findTransTypeIsExist(@Param("transType")RecordAccountRuleTransType transType ) ;
	
	@SelectProvider( type=SqlProvider.class,method="findTransTypeList")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleTransTypeMapper.OneToOneResultMap")
	List<RecordAccountRuleTransType> findTransTypeList(@Param("transType")RecordAccountRuleTransType transType ,@Param("sort")Sort sort ,Page<RecordAccountRuleTransType> page) ;
	
	@Select("SELECT rtt.trans_type_name,rtt.from_system,rtt.remark,rtt.trans_group,r.rule_no,r.rule_name,rtt.trans_type_code "
			+ "FROM record_account_rule_trans_type AS rtt,record_account_rule AS r "
			+ "WHERE rtt.rule_id = r.rule_id AND id = #{id}")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleTransTypeMapper.OneToOneResultMap")
	RecordAccountRuleTransType findTransTypeById(@Param("id")String id);
	
	@Update("UPDATE record_account_rule_trans_type "
			+ "SET trans_type_name = #{transType.transTypeName},rule_id = #{transType.ruleId},from_system = #{transType.fromSystem},remark = #{transType.remark},"
			+ "trans_type_code = #{transType.transTypeCode},updator = #{transType.updator},update_time = #{transType.updateTime},trans_group = #{transType.transGroup} "
			+ "WHERE id = #{transType.id}")
	int updateTransType(@Param("transType")RecordAccountRuleTransType transType);
	
	@Delete("DELETE FROM record_account_rule_trans_type WHERE id = #{id}")
	int deleteTransType(@Param("id")String id) ;
	
	
	@Select("SELECT ra.trans_type_code,ra.trans_type_name,ra.trans_group,(select sd.sys_name from sys_dict sd where sd.sys_key='from_system' and sys_value=ra.from_system) as from_system, "
			+ " creator,create_time,updator,update_time "
			+ " FROM record_account_rule_trans_type ra")
	@ResultMap("cn.eeepay.framework.dao.bill.RecordAccountRuleTransTypeMapper.BaseResultMap")
	List<RecordAccountRuleTransType> findAllTransType();
	
	@Select("SELECT * from record_account_rule_trans_type where trans_type_code=#{transCode} and from_system=#{fromSystem}")
	@ResultType(RecordAccountRuleTransType.class)
	RecordAccountRuleTransType getByTransCodeAndFromSystem(@Param("transCode")String transCode, @Param("fromSystem")String fromSystem);
	
	public class SqlProvider{
//		public String findSelectRule(final Map<String,Object> parameter){
//			final RecordAccountRule rule = (RecordAccountRule)parameter.get("rule") ;
//			return new SQL(){{
//				SELECT(" rule_no,rule_name ");
//				FROM(" record_account_rule");
//				/*if (rule.getRuleNo()!=null || rule.getRuleNo()!="")
//					WHERE(" rule_no like  \"%\"#{rule.ruleNo}\"%\" or rule_name like  \"%\"#{rule.ruleNo}\"%\" ");*/
//				if (!StringUtils.isBlank(rule.getRuleNo()))
//					WHERE(" rule_no like  \"%\"#{rule.ruleNo}\"%\" or rule_name like  \"%\"#{rule.ruleNo}\"%\" ");
//			}}.toString() ;
//		}
		
		public String findTransTypeList(final Map<String,Object> parameter){
			final RecordAccountRuleTransType transType = (RecordAccountRuleTransType)parameter.get("transType") ;
			return new SQL(){{
				SELECT(" rtt.id,rtt.trans_type_name,rtt.from_system,r.rule_no,r.rule_name,rtt.trans_type_code,"
						+ " rtt.creator,rtt.create_time,rtt.updator,rtt.update_time, rtt.trans_group");
				FROM(" record_account_rule_trans_type as rtt,record_account_rule as r ");
				WHERE(" rtt.rule_id = r.rule_id ") ;
				if (!StringUtils.isBlank(transType.getTransTypeCode()) )
					WHERE(" rtt.trans_type_code like  \"%\"#{transType.transTypeCode}\"%\" ");
				if (!StringUtils.isBlank(transType.getTransTypeName()) )
					WHERE(" rtt.trans_type_name like  \"%\"#{transType.transTypeName}\"%\" ");
				if (!StringUtils.isBlank(transType.getFromSystem()) && !transType.getFromSystem().equals("ALL"))
					WHERE(" rtt.from_system like  \"%\"#{transType.fromSystem}\"%\" ");
			}}.toString() ;
		}
		
	}

	
	
}
