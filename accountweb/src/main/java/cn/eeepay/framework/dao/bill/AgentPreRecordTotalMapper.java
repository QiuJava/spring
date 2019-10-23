package cn.eeepay.framework.dao.bill;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentAccPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * 代理商预记账累计表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月15日13:40:44
 *
 */
public interface AgentPreRecordTotalMapper {
	@Insert("insert into agent_pre_record_total(agent_no,agent_name,open_back_amount,rate_diff_amount,"
			+ " tui_cost_amount,risk_sub_amount,mer_mg_amount,"
			+ " other_amount,terminal_freeze_amount,other_freeze_amount,bail_sub_amount)"
			+ " values(#{agentPreRecordTotal.agentNo},#{agentPreRecordTotal.agentName},#{agentPreRecordTotal.openBackAmount},#{agentPreRecordTotal.rateDiffAmount},"
			+ "#{agentPreRecordTotal.tuiCostAmount},#{agentPreRecordTotal.riskSubAmount},#{agentPreRecordTotal.merMgAmount},"
			+ "#{agentPreRecordTotal.otherAmount},#{agentPreRecordTotal.terminalFreezeAmount},#{agentPreRecordTotal.otherFreezeAmount},#{agentPreRecordTotal.bailSubAmount})")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	int insertAgentPreRecordTotal(@Param("agentPreRecordTotal")AgentPreRecordTotal agentPreRecordTotal);
	
	
	@Insert("<script>"
			+" insert into agent_pre_record_total(agent_no,agent_name,open_back_amount,rate_diff_amount,"
			+ " tui_cost_amount,risk_sub_amount,mer_mg_amount,"
			+ " other_amount,terminal_freeze_amount,other_freeze_amount,bail_sub_amount)"
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ "( #{item.agentNo},#{item.agentName},#{item.openBackAmount},#{item.rateDiffAmount},"
			+ " #{item.tuiCostAmount},#{item.riskSubAmount},#{item.merMgAmount},"
			+ " #{item.otherAmount},#{item.terminalFreezeAmount},#{item.otherFreezeAmount},#{item.bailSubAmount})"
			+ " </foreach > "
            + " </script>")
	int insertAgentPreRecordTotalBatch(@Param("list")List<AgentPreRecordTotal> list);
	
	@Update("update agent_pre_record_total "
			+ " set agent_name=#{agentPreRecordTotal.agentName},"
			+ " open_back_amount=#{agentPreRecordTotal.openBackAmount},"
			+ " rate_diff_amount=#{agentPreRecordTotal.rateDiffAmount},"
			+ " tui_cost_amount=#{agentPreRecordTotal.tuiCostAmount},"
			+ " risk_sub_amount=#{agentPreRecordTotal.riskSubAmount},"
			+ " mer_mg_amount=#{agentPreRecordTotal.merMgAmount},"
			+ " other_amount=#{agentPreRecordTotal.otherAmount},"
			+ " terminal_freeze_amount=#{agentPreRecordTotal.terminalFreezeAmount},"
			+ " other_freeze_amount=#{agentPreRecordTotal.otherFreezeAmount},"
			+ " bail_sub_amount=#{agentPreRecordTotal.bailSubAmount}"
			+"   where agent_no=#{agentPreRecordTotal.agentNo}")
	int updateAgentPreRecordTotal(@Param("agentPreRecordTotal")AgentPreRecordTotal agentPreRecordTotal);
	
	
	@Update("<script>"
			+ " <foreach collection=\"list\" item=\"item\" index=\"index\" open=\"\" "
			+ "         close=\"\" separator=\";\"> "
			+ " update agent_pre_record_total "
			+ " set agent_name=#{item.agentName},"
			+ " open_back_amount=#{item.openBackAmount},"
			+ " rate_diff_amount=#{item.rateDiffAmount},"
			+ " tui_cost_amount=#{item.tuiCostAmount},"
			+ " risk_sub_amount=#{item.riskSubAmount},"
			+ " mer_mg_amount=#{item.merMgAmount},"
			+ " other_amount=#{item.otherAmount},"
			+ " terminal_freeze_amount=#{item.terminalFreezeAmount},"
			+ " other_freeze_amount=#{item.otherFreezeAmount},"
			+ " bail_sub_amount=#{item.bailSubAmount}"
			+ "   where agent_no=#{item.agentNo}"
			+ "     </foreach> "
            + " </script>")
	int updateAgentPreRecordTotalBatch(@Param("list")List<AgentPreRecordTotal> list);
	
	
	@Delete("delete from agent_pre_record_total where id = #{id}")
	int deleteAgentPreRecordTotal(@Param("id")Integer id);
	
	
	@Select({"<script>",
        	"SELECT * FROM agent_pre_record_total ",
        	"WHERE agent_no IN ", 
        	"<foreach item='item' index='index' collection='list'",
            	"open='(' separator=',' close=')'>",
            	"#{item}",
            "</foreach>",
        	"</script>"})
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	List<AgentPreRecordTotal> findAgentPreRecordTotalByAgentNos(@Param("agentNos") List<String> agentNos);
	
	
	@Select("select * from agent_pre_record_total ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	List<AgentPreRecordTotal> findAllAgentPreRecordTotal();
	
	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	List<AgentPreRecordTotal> findAgentPreRecordTotalList(@Param("agentPreRecordTotal")AgentPreRecordTotal agentPreRecordTotal,@Param("userNoStrs")String userNoStrs, @Param("sort")Sort sort,@Param("page")Page<AgentPreRecordTotal> page);

	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalByAgentNoAndSubjectNo")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(@Param("agentNo")String agentNo, @Param("subjectNo")String subjectNo);

//	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalByAgentAccPreAdjust")
//	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
//	AgentPreRecordTotal findAgentPreRecordTotalByAgentNoAndSubjectNo(@Param("agentNo")AgentAccPreAdjust agentNo, @Param("subjectNo")String subjectNo);


	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecord")
	@Results(value = {
			@Result(property = "preFreezeAmount",column = "pre_freeze_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "controlAmount",column = "control_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "controlAmount2",column = "control_amount2",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL)
	})
	Map<String, Object>  findAgentPreRecord(@Param("account")String account);


	@Select("select * from agent_pre_record_total where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	AgentPreRecordTotal findAgentPreRecordTotalById(@Param("id")Integer id);
	
	@Select("select * from agent_pre_record_total where agent_no = #{agentNo} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	AgentPreRecordTotal findAgentPreRecordTotalByAgentNo(@Param("agentNo")String agentNo);
	
	
	@Insert("insert into agent_pre_record_total"
			+ "("
			+ "agent_no,"
			+ "agent_name,"
			+ "open_back_amount,"
			+ "rate_diff_amount,"
			+ "tui_cost_amount,"
			+ "risk_sub_amount,"
			+ "bail_sub_amount,"
			+ "mer_mg_amount,"
			+ "other_amount,"
			+ "terminal_freeze_amount,"
			+ "other_freeze_amount"
			+ ")values("
			+ "#{agentPreAdjust.agentNo},"
			+ "#{agentPreAdjust.agentName},"
			+ "#{agentPreAdjust.openBackAmount},"
			+ "#{agentPreAdjust.rateDiffAmount},"
			+ "#{agentPreAdjust.tuiCostAmount},"
			+ "#{agentPreAdjust.riskSubAmount},"
			+ "#{agentPreAdjust.bailSubAmount},"
			+ "#{agentPreAdjust.merMgAmount},"
			+ "#{agentPreAdjust.otherAmount},"
			+ "#{agentPreAdjust.terminalFreezeAmount}"
			+ "#{agentPreAdjust.otherFreezeAmount})")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	int insertAgentPreRecordTotalByAgentPreAdjust(@Param("agentPreAdjust")AgentPreAdjust agentPreAdjust);

	
//	@Update("update agent_pre_record_total set "
//			+ " terminal_freeze_amount = terminal_freeze_amount + #{agentPreFreeze.terminalFreezeAmount},"
//			+ " other_freeze_amount = other_freeze_amount + #{agentPreFreeze.otherFreezeAmount}"
//			+"  where agent_no=#{agentPreFreeze.agentNo}")
//	int updateAgentPreRecordTotalByAgentPreFreeze(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze);
	
//	@Insert("insert into agent_pre_record_total"
//			+ "("
//			+ "agent_no,"
//			+ "agent_name,"
//			+ "terminal_freeze_amount,"
//			+ "other_freeze_amount"
//			+ ")values("
//			+ "#{agentPreFreeze.agentNo},"
//			+ "#{agentPreFreeze.agentName},"
//			+ "#{agentPreFreeze.terminalFreezeAmount},"
//			+ "#{agentPreFreeze.otherFreezeAmount})")
//	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
//	int insertAgentPreRecordTotalByAgentPreFreeze(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze);

	@SelectProvider(type=SqlProvider.class,method="exportAgentPreRecordTotalList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper.BaseResultMap")
	List<AgentPreRecordTotal> exportAgentPreRecordTotalList(@Param("agentPreRecordTotal")AgentPreRecordTotal agentPreRecordTotal,@Param("userNoStrs")String userNoStrs ,@Param("sort")Sort sort);

	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalListCollection")
	@Results(value = {
			@Result(property = "allOpenBackAmount", column = "all_open_back_amount",javaType=BigDecimal.class,jdbcType= JdbcType.DECIMAL),
			@Result(property = "allRateDiffAmount", column = "all_rate_diff_amount",javaType=Integer.class,jdbcType=JdbcType.INTEGER),
			@Result(property = "allTuiCostAmount", column = "all_tui_cost_amount",javaType=String.class,jdbcType=JdbcType.VARCHAR),
			@Result(property = "allTiskSubAmount", column = "all_risk_sub_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allMerMgAmount", column = "all_mer_mg_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allOtherAmount", column = "all_other_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allTerminalFreezeAmount", column = "all_terminal_freeze_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allOtherFreezeAmount", column = "all_other_freeze_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allBailSubAmount", column = "all_bail_sub_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allCurrBalance", column = "all_curr_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allControlAmount", column = "all_control_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allAvailBalance", column = "all_avail_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allCurrBalance2", column = "all_curr_balance2",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allControlAmount2", column = "all_control_amount2",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allAvailBalance2", column = "all_avail_balance2",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
	})
	Map<String, Object> findAgentPreRecordTotalListCollection(@Param("agentPreRecordTotal")AgentPreRecordTotal agentPreRecordTotal,@Param("userNoStrs")String userNoStrs);

	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalListCollectionByUserNoStrs")
	@Results(value = {
			@Result(property = "allCurrBalance", column = "all_curr_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allControlAmount", column = "all_control_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allAvailBalance", column = "all_avail_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
	})
	Map<String, Object> findAgentPreRecordTotalListCollectionByUserNoStrs(@Param("userNoStrs")String userNoStrs);

	@SelectProvider(type=SqlProvider.class,method="findAgentPreRecordTotalByAgentNoAndSubjectNoCollection")
	@Results(value = {
			@Result(property = "allCurrBalance", column = "all_curr_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allControlAmount", column = "all_control_amount",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
			@Result(property = "allAvailBalance", column = "all_avail_balance",javaType=BigDecimal.class,jdbcType=JdbcType.DECIMAL),
	})
	Map<String, Object> findAgentPreRecordTotalByAgentNoAndSubjectNoCollection(@Param("agentNo")String agentNo,@Param("subjectNo")String subjectNo);


	public class SqlProvider{
		public String findAgentPreRecordTotalList(final Map<String, Object> parameter){
			final AgentPreRecordTotal agentPreRecordTotal=(AgentPreRecordTotal)parameter.get("agentPreRecordTotal");
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" eai.user_id as agent_no ,"
						+" aprt.open_back_amount,"
						+" aprt.rate_diff_amount, "
						+" aprt.tui_cost_amount, "
						+" aprt.risk_sub_amount, "
						+" aprt.mer_mg_amount,  "
						+" aprt.other_amount,  "
						+" aprt.terminal_freeze_amount,"
						+" aprt.other_freeze_amount, "
						+" aprt.bail_sub_amount, "
						+" bea.curr_balance,"
						+" bea.control_amount, "
						+" (bea.curr_balance - bea.settling_amount - bea.control_amount) as avail_balance"
						+" from "
						+" bill_ext_account bea "
						+"LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				WHERE("  bea.subject_no = '224105' ");
				if(StringUtils.isNotBlank(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("agent_no desc ");
				}
			}}.toString();
		}

		public String findAgentPreRecordTotalByAgentNoAndSubjectNo(final Map<String, Object> parameter){
			final String agentNo = (String) parameter.get("agentNo") ;
			final String subjectNo = (String) parameter.get("subjectNo") ;
			return new SQL(){{
				SELECT(" eai.user_id as agent_no ,"
						+" aprt.open_back_amount,"
						+" aprt.rate_diff_amount, "
						+" aprt.tui_cost_amount, "
						+" aprt.risk_sub_amount, "
						+" aprt.mer_mg_amount,  "
						+" aprt.other_amount,  "
						+" aprt.terminal_freeze_amount,"
						+" aprt.other_freeze_amount, "
						+" aprt.bail_sub_amount, "
						+" bea.pre_freeze_amount,"
						+" bea.curr_balance,"
						+" bea.control_amount, "
						+" bea.subject_no, "
						+" (bea.curr_balance - bea.settling_amount - bea.control_amount) as avail_balance"
						+" from "
						+" bill_ext_account bea "
						+"LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				if(StringUtils.isNotBlank(subjectNo))
					WHERE(" bea.subject_no = '"+subjectNo+"' ") ;
				if(StringUtils.isNotBlank(agentNo))
					WHERE(" eai.user_id = '"+agentNo+"' ") ;
			}}.toString();
		}

		public String findAgentPreRecordTotalByAgentAccPreAdjust(final Map<String, Object> parameter){
			final AgentAccPreAdjust agentNo = (AgentAccPreAdjust) parameter.get("agentNo") ;
			final String subjectNo = (String) parameter.get("subjectNo") ;
			return new SQL(){{
				SELECT(" eai.user_id as agent_no ,"
						+" aprt.open_back_amount,"
						+" aprt.rate_diff_amount, "
						+" aprt.tui_cost_amount, "
						+" aprt.risk_sub_amount, "
						+" aprt.mer_mg_amount,  "
						+" aprt.other_amount,  "
						+" aprt.terminal_freeze_amount,"
						+" aprt.other_freeze_amount, "
						+" aprt.bail_sub_amount, "
						+" bea.pre_freeze_amount,"
						+" bea.curr_balance,"
						+" bea.control_amount, "
						+" bea.subject_no, "
						+" (bea.curr_balance - bea.settling_amount - bea.control_amount) as avail_balance"
						+" from "
						+" bill_ext_account bea "
						+"LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				if(StringUtils.isNotBlank(subjectNo))
					WHERE(" bea.subject_no = '"+subjectNo+"' ") ;
				if(StringUtils.isNotBlank(agentNo.getAgentNo()))
					WHERE(" eai.user_id = '"+agentNo.getAgentNo()+"' ") ;
				if(agentNo.getActivitySubsidyBalance1()!=null){
					WHERE("bea.curr_balance >= "+agentNo.getActivitySubsidyBalance1().toString());
				}

				if(agentNo.getActivitySubsidyBalance2()!=null){
					WHERE("bea.curr_balance <= "+agentNo.getActivitySubsidyBalance2().toString());
				}

			}}.toString();
		}


		public String findAgentPreRecordTotalByAgentNoAndSubjectNoCollection(final Map<String, Object> parameter){
			final String agentNo = (String) parameter.get("agentNo") ;
			final String subjectNo = (String) parameter.get("subjectNo") ;
			return new SQL(){{
				SELECT("  sum(bea.control_amount) as all_control_amount, "
						+" sum(bea.curr_balance - bea.settling_amount - bea.control_amount) as all_avail_balance,"
						+" sum(bea.curr_balance) as all_curr_balance"
						+" from "
						+" bill_ext_account bea "
						+"LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				if(StringUtils.isNotBlank(subjectNo))
					WHERE(" bea.subject_no = '"+subjectNo+"' ") ;
				if(StringUtils.isNotBlank(agentNo))
					WHERE(" eai.user_id = '"+agentNo+"' ") ;
			}}.toString();
		}


		public String findAgentPreRecord(final Map<String, Object> parameter){
			final String account = (String) parameter.get("account") ;
			return new SQL(){{
				SELECT(" bea.pre_freeze_amount,"
						+" bea.control_amount, "
						+" bea2.control_amount as control_amount2 "
						+" from "
						+" bill_ext_account bea "
						+" LEFT JOIN bill_ext_account bea2 on bea2.account_no = bea.account_no and bea2.subject_no = '224106' and bea2.currency_no = '1' ");
				WHERE("  bea.currency_no = '1' ");
				WHERE("  bea.subject_no = '224105' ");
				if(StringUtils.isNotBlank(account))
					WHERE(" bea.account_no = '"+account+"'") ;
			}}.toString();
		}

		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","agent_no","agent_name","open_back_amount","rate_diff_amount"};
		    final String[] columns={"id","agentNo","agentName","openBackAmount","rateDiffAmount"};
		    if(StringUtils.isNotBlank(name)){
		    	if(type==0){//属性查出字段名
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(propertys[i])){
		    				return columns[i];
		    			}
		    		}
		    	}else if(type==1){//字段名查出属性
		    		for(int i=0;i<propertys.length;i++){
		    			if(name.equalsIgnoreCase(columns[i])){
		    				return propertys[i];
		    			}
		    		}
		    	}
		    }
			return null;
		}

		public String exportAgentPreRecordTotalList(final Map<String, Object> parameter){
			final AgentPreRecordTotal agentPreRecordTotal=(AgentPreRecordTotal)parameter.get("agentPreRecordTotal");
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT(" eai.user_id as agent_no ,"
						+" aprt.open_back_amount,"
						+" aprt.rate_diff_amount, "
						+" aprt.tui_cost_amount, "
						+" aprt.risk_sub_amount, "
						+" aprt.mer_mg_amount,  "
						+" aprt.other_amount,  "
						+" aprt.terminal_freeze_amount,"
						+" aprt.other_freeze_amount, "
						+" aprt.bail_sub_amount, "
						+" bea.curr_balance,"
						+" bea.control_amount, "
						+" (bea.curr_balance - bea.settling_amount - bea.control_amount) as avail_balance"
						+" from "
						+" bill_ext_account bea "
						+" LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				WHERE("  bea.subject_no = '224105' ");
				if(StringUtils.isNotBlank(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("agent_no desc ");
				}
			}}.toString();
		}

		public String findAgentPreRecordTotalListCollection(final Map<String, Object> parameter){
			final AgentPreRecordTotal agentPreRecordTotal=(AgentPreRecordTotal)parameter.get("agentPreRecordTotal");
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			return new SQL(){{
				SELECT(" sum(aprt.open_back_amount) as all_open_back_amount,"
						+" sum(aprt.rate_diff_amount) as all_rate_diff_amount , "
						+" sum(aprt.tui_cost_amount) as all_tui_cost_amount, "
						+" sum(aprt.risk_sub_amount) as all_risk_sub_amount, "
						+" sum(aprt.mer_mg_amount) as all_mer_mg_amount,  "
						+" sum(aprt.other_amount) as all_other_amount,  "
						+" sum(aprt.terminal_freeze_amount) as all_terminal_freeze_amount,"
						+" sum(aprt.other_freeze_amount) as all_other_freeze_amount, "
						+" sum(aprt.bail_sub_amount) as all_bail_sub_amount, "
						+" sum(bea.curr_balance) as all_curr_balance,"
						+" sum(bea.control_amount) as all_control_amount, "
						+" sum(bea.curr_balance - bea.settling_amount - bea.control_amount) as all_avail_balance "
						+" from "
						+" bill_ext_account bea "
						+" LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				WHERE("  bea.subject_no = '224105' ");
				if(StringUtils.isNotBlank(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;
			}}.toString();
		}

		public String findAgentPreRecordTotalListCollectionByUserNoStrs(final Map<String, Object> parameter){
			final String userNoStrs = (String) parameter.get("userNoStrs") ;
			return new SQL(){{
				SELECT(" sum(bea.curr_balance) as all_curr_balance,"
						+" sum(bea.control_amount) as all_control_amount, "
						+" sum(bea.curr_balance - bea.settling_amount - bea.control_amount) as all_avail_balance "
						+" from "
						+" bill_ext_account bea "
						+" LEFT JOIN ext_account_info eai  on bea.account_no = eai.account_no "
						+" LEFT JOIN agent_pre_record_total aprt on eai.user_id = aprt.agent_no ");
				WHERE("  eai.account_type = 'A' ");
				WHERE("  bea.currency_no = '1' ");
				WHERE("  bea.subject_no = '224106' ");
				if(StringUtils.isNotBlank(userNoStrs))
					WHERE(" eai.user_id in ("+userNoStrs+")") ;
			}}.toString();
		}



	}







	
}
