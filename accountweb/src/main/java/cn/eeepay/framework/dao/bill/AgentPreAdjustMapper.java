package cn.eeepay.framework.dao.bill;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreAdjust;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;
/**
 * 代理商预调账表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月15日13:40:44
 *
 */
public interface AgentPreAdjustMapper {
	@Insert("insert into agent_pre_adjust("
			+ "adjust_time,"
			+ "applicant,"
			+ "agent_no,"
			+ "agent_name,"
			+ "open_back_amount,"
			+ "rate_diff_amount,"
			+ "tui_cost_amount,"
			+ "risk_sub_amount,"
			+ "bail_sub_amount,"
			+ "mer_mg_amount,"
			+ "other_amount,"
			+ "adjust_reason,"
			+ "activity_available_amount,"
			+ "activity_freeze_amount,"
			+ "generate_amount,"
			+ "remark)"
			+ " values("
			+ "now(),"
			+ "#{agentPreAdjust.applicant},"
			+ "#{agentPreAdjust.agentNo},"
			+ "#{agentPreAdjust.agentName},"
			+ "#{agentPreAdjust.openBackAmount},"
			+ "#{agentPreAdjust.rateDiffAmount},"
			+ "#{agentPreAdjust.tuiCostAmount},"
			+ "#{agentPreAdjust.riskSubAmount},"
			+ "#{agentPreAdjust.bailSubAmount},"
			+ "#{agentPreAdjust.merMgAmount},"
			+ "#{agentPreAdjust.otherAmount},"
			+ "#{agentPreAdjust.adjustReason},"
			+ "#{agentPreAdjust.activityAvailableAmount},"
			+ "#{agentPreAdjust.activityFreezeAmount},"
			+ "#{agentPreAdjust.generateAmount},"
			+ "#{agentPreAdjust.remark})")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreAdjustMapper.BaseResultMap")
	int insertAgentPreAdjust(@Param("agentPreAdjust")AgentPreAdjust agentPreAdjust);
	
	@Update("update agent_pre_adjust set "
			+ " adjust_time=#{agentPreAdjust.adjustTime},"
			+ " applicant=#{agentPreAdjust.applicant},"
			+ " agent_no=#{agentPreAdjust.agentNo},"
			+ " agent_name=#{agentPreAdjust.agentName},"
			+ " remark=#{agentPreAdjust.remark},"
			+ " adjust_reason=#{agentPreAdjust.adjustReason},"
			+ " open_back_amount=#{agentPreAdjust.openBackAmount},"
			+ " rate_diff_amount=#{agentPreAdjust.rateDiffAmount},"
			+ " tui_cost_amount=#{agentPreAdjust.tuiCostAmount},"
			+ " risk_sub_amount=#{agentPreAdjust.riskSubAmount},"
			+ " bail_sub_amount=#{agentPreAdjust.bailSubAmount},"
			+ " mer_mg_amount=#{agentPreAdjust.merMgAmount},"
			+ " other_amount=#{agentPreAdjust.otherAmount}"
			+ " where id=#{agentPreAdjust.id}")
	int updateAgentPreAdjust(@Param("agentPreAdjust")AgentPreAdjust agentPreAdjust);
	
	@Delete("delete from agent_pre_adjust where id = #{id}")
	int deleteAgentPreAdjust(@Param("id")Integer id);
	
	@Select("select * from agent_pre_adjust ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreAdjustMapper.BaseResultMap")
	List<AgentPreAdjust> findAllAgentPreAdjust();
	
	@SelectProvider(type=SqlProvider.class,method="findAgentPreAdjustList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreAdjustMapper.BaseResultMap")
	List<AgentPreAdjust> findAgentPreAdjustList(@Param("agentPreAdjust")AgentPreAdjust agentPreAdjust,@Param("sort")Sort sort,@Param("page")Page<AgentPreAdjust> page);
	
	@SelectProvider(type=SqlProvider.class,method="exportAgentsProfitPreAdjustList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreAdjustMapper.BaseResultMap")
	List<AgentPreAdjust> exportAgentsProfitPreAdjustList(@Param("agentPreAdjust")AgentPreAdjust agentPreAdjust);
	
	@Select("select * from agent_pre_adjust where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreAdjustMapper.BaseResultMap")
	AgentPreAdjust findAgentPreAdjustById(@Param("id")Integer id);
	
	
	public class SqlProvider{

		public String findAgentPreAdjustList(final Map<String, Object> parameter){
			final AgentPreAdjust agentPreAdjust=(AgentPreAdjust)parameter.get("agentPreAdjust");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("agent_pre_adjust");
				if(StringUtils.isNotBlank(agentPreAdjust.getAgentNo()) && !"ALL".equals(agentPreAdjust.getAgentNo())){
					WHERE(" agent_no=#{agentPreAdjust.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustReason()) && !"ALL".equals(agentPreAdjust.getAdjustReason())){
					if("open_return".equals(agentPreAdjust.getAdjustReason())){//开通返现
						WHERE(" open_back_amount != 0 ");
					}else if("rate_variance".equals(agentPreAdjust.getAdjustReason())){//费率差异
						WHERE(" rate_diff_amount != 0 ");
					}else if("tui_cost_deduction".equals(agentPreAdjust.getTuiCostAmount())){//超级推成本
						WHERE(" tui_cost_amount != 0 ");
					}else if("risk_deduction".equals(agentPreAdjust.getAdjustReason())){//风控扣款
						WHERE(" risk_sub_amount != 0 ");
					}else if("merchant_management_fee".equals(agentPreAdjust.getAdjustReason())){//商户管理费
						WHERE(" mer_mg_amount != 0 ");
					}else if("margin_deduction".equals(agentPreAdjust.getAdjustReason())){//保证金扣除
						WHERE(" bail_sub_amount != 0 ");
					}else{//其他
						WHERE(" other_amount != 0 ");
					}
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustTime1()) ){
					WHERE(" adjust_time >= #{agentPreAdjust.adjustTime1} ");
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustTime2()) ){
					WHERE(" adjust_time <= #{agentPreAdjust.adjustTime2} ");
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("adjust_time desc ");
				}
			}}.toString();
		}
		
		public String exportAgentsProfitPreAdjustList(final Map<String, Object> parameter){
			final AgentPreAdjust agentPreAdjust=(AgentPreAdjust)parameter.get("agentPreAdjust");
			return new SQL(){{
				SELECT("*");
				FROM("agent_pre_adjust");
				if(StringUtils.isNotBlank(agentPreAdjust.getAgentNo()) && !"ALL".equals(agentPreAdjust.getAgentNo())){
					WHERE(" agent_no=#{agentPreAdjust.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustReason()) && !"ALL".equals(agentPreAdjust.getAdjustReason())){
					if("open_return".equals(agentPreAdjust.getAdjustReason())){//开通返现
						WHERE(" open_back_amount != 0 ");
					}else if("rate_variance".equals(agentPreAdjust.getAdjustReason())){//费率差异
						WHERE(" rate_diff_amount != 0 ");
					}else if("tui_cost_deduction".equals(agentPreAdjust.getTuiCostAmount())){//超级推成本
						WHERE(" tui_cost_amount != 0 ");
					}else if("risk_deduction".equals(agentPreAdjust.getAdjustReason())){//风控扣款
						WHERE(" risk_sub_amount != 0 ");
					}else if("merchant_management_fee".equals(agentPreAdjust.getAdjustReason())){//商户管理费
						WHERE(" mer_mg_amount != 0 ");
					}else if("margin_deduction".equals(agentPreAdjust.getAdjustReason())){//保证金扣除
						WHERE(" bail_sub_amount != 0 ");
					}else{//其他
						WHERE(" other_amount != 0 ");
					}
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustTime1()) ){
					WHERE(" adjust_time >= #{agentPreAdjust.adjustTime1} ");
				}
				if(StringUtils.isNotBlank(agentPreAdjust.getAdjustTime2()) ){
					WHERE(" adjust_time <= #{agentPreAdjust.adjustTime2} ");
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","adjust_time","applicant","agent_no","agent_name"};
		    final String[] columns={"id","adjustTime","applicant","agentNo","agentName"};
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
	}






}
