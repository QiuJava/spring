package cn.eeepay.framework.dao.bill;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Administrator
 *
 */
public interface AgentAccPreAdjustMapper {

	@SelectProvider( type=SqlProvider.class,method="findAgentAccPreAdjustList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentAccPreAdjustMapper.BaseResultMap")
	List<AgentAccPreAdjust> findAgentAccPreAdjustList(@Param("agentAccPreAdjust") AgentAccPreAdjust agentAccPreAdjust, @Param("params") Map<String, String> params, @Param("sort") Sort sort, Page<AgentAccPreAdjust> page);

	@SelectProvider( type=SqlProvider.class,method="findAgentAccPreAdjustList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentAccPreAdjustMapper.BaseResultMap")
	List<AgentAccPreAdjust> exportAgentAccPreAdjustList(@Param("agentAccPreAdjust") AgentAccPreAdjust agentAccPreAdjust);

	@SelectProvider(type=SqlProvider.class,method="findAgentAccPreAdjustListCollection")
	@Results(value = {
			@Result(property = "allAdjustAmount", column = "adjust_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL),
			@Result(property = "allActivitySubsidyBalance", column = "activity_subsidy_balance",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL),
			@Result(property = "allActivitySubsidyFreeze", column = "activity_subsidy_freeze",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL),
			@Result(property = "allActivitySubsidyAvailableBalance", column = "activity_subsidy_available_balance",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL)
	})
	Map<String,Object> findAdjustmentAmountCollection(@Param("agentAccPreAdjust") AgentAccPreAdjust agentAccPreAdjust);


    public class SqlProvider{

		public String findAgentAccPreAdjustListCollection(final Map<String, Object> parameter) {
			final AgentAccPreAdjust agentAccPreAdjust = (AgentAccPreAdjust) parameter.get("agentAccPreAdjust");
			return new SQL(){{
				SELECT("sum(acc.adjust_amount) as adjust_amount,"+
						"sum(bea.curr_balance) as activity_subsidy_balance," +
						"sum(bea.control_amount) as activity_subsidy_freeze," +
						"sum(bea.curr_balance - bea.settling_amount - bea.control_amount) as activity_subsidy_available_balance");
				FROM(" agent_acc_pre_adjust acc LEFT JOIN ext_account_info eai ON eai.user_id = acc.agent_no LEFT JOIN bill_ext_account bea ON bea.account_no = eai.account_no");
				WHERE("eai.account_type = 'A' AND bea.currency_no = '1' AND bea.subject_no = '224106'");
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentName())){
					WHERE(" acc.agent_name  = #{agentAccPreAdjust.agentName} ");
				}
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentNo())){
					WHERE(" acc.agent_no  = #{agentAccPreAdjust.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentLevel()) && !agentAccPreAdjust.getAgentLevel().equals("ALL")){
					WHERE(" acc.agent_level  = #{agentAccPreAdjust.agentLevel} ");
				}
				if(agentAccPreAdjust.getAdjustAmount1()!=null){
					WHERE(" acc.adjust_amount  >= #{agentAccPreAdjust.adjustAmount1} ");
				}
				if(agentAccPreAdjust.getAdjustAmount2()!=null){
					WHERE(" acc.adjust_amount  <= #{agentAccPreAdjust.adjustAmount2} ");
				}
				if(agentAccPreAdjust.getActivitySubsidyBalance1()!=null){
					WHERE(" bea.curr_balance  >= #{agentAccPreAdjust.activitySubsidyBalance1} ");
				}
				if(agentAccPreAdjust.getActivitySubsidyBalance2()!=null){
					WHERE(" bea.curr_balance  <= #{agentAccPreAdjust.activitySubsidyBalance2} ");
				}
//				if(StringUtils.isNotBlank(agentAccPreAdjust.getDate1())){
//					WHERE("apply_date >= #{agentAccPreAdjust.date1}");
//				}
//				if(StringUtils.isNotBlank(agentAccPreAdjust.getDate2())){
//					WHERE("apply_date <= #{agentAccPreAdjust.date2}");
//				}

			}}.toString();
		}

		public String findAgentAccPreAdjustList(final Map<String, Object> parameter) {
			final AgentAccPreAdjust agentAccPreAdjust = (AgentAccPreAdjust) parameter.get("agentAccPreAdjust");
			return new SQL(){{
				SELECT("acc.id,acc.agent_name,acc.agent_level,acc.agent_no,acc.adjust_amount," +
						"bea.curr_balance as activity_subsidy_balance," +
						"bea.control_amount as activity_subsidy_freeze," +
						"(bea.curr_balance - bea.settling_amount - bea.control_amount) as activity_subsidy_available_balance");
				FROM(" agent_acc_pre_adjust acc LEFT JOIN ext_account_info eai ON eai.user_id = acc.agent_no LEFT JOIN bill_ext_account bea ON bea.account_no = eai.account_no");
				WHERE("eai.account_type = 'A' AND bea.currency_no = '1' AND acc.agent_level = '1' AND bea.subject_no = '224106'");
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentName())){
					WHERE(" acc.agent_name  = #{agentAccPreAdjust.agentName} ");
				}
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentNo())){
					WHERE(" acc.agent_no  = #{agentAccPreAdjust.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentAccPreAdjust.getAgentLevel()) && !agentAccPreAdjust.getAgentLevel().equals("ALL")){
					WHERE(" acc.agent_level  = #{agentAccPreAdjust.agentLevel} ");
				}
				if(agentAccPreAdjust.getAdjustAmount1()!=null){
					WHERE(" acc.adjust_amount  >= #{agentAccPreAdjust.adjustAmount1} ");
				}
				if(agentAccPreAdjust.getAdjustAmount2()!=null){
					WHERE(" acc.adjust_amount  <= #{agentAccPreAdjust.adjustAmount2} ");
				}
				if(agentAccPreAdjust.getActivitySubsidyBalance1()!=null){
					WHERE(" bea.curr_balance  >= #{agentAccPreAdjust.activitySubsidyBalance1} ");
				}
				if(agentAccPreAdjust.getActivitySubsidyBalance2()!=null){
					WHERE(" bea.curr_balance  <= #{agentAccPreAdjust.activitySubsidyBalance2} ");
				}
//				if(StringUtils.isNotBlank(agentAccPreAdjust.getDate1())){
//					WHERE("apply_date >= #{agentAccPreAdjust.date1}");
//				}
//				if(StringUtils.isNotBlank(agentAccPreAdjust.getDate2())){
//					WHERE("apply_date <= #{agentAccPreAdjust.date2}");
//				}

				ORDER_BY("acc.adjust_time desc");
			}}.toString();
		}

	}

	
}
