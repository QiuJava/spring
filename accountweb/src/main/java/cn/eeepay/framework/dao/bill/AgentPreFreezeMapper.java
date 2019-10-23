package cn.eeepay.framework.dao.bill;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.jdbc.SQL;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
/**
 * 代理商预冻结表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年4月15日13:40:44
 *
 */
public interface AgentPreFreezeMapper {
	@Insert({"insert into agent_pre_freeze(agent_no,agent_name,freeze_time,operater,terminal_freeze_amount,other_freeze_amount,freeze_reason,remark,fen_freeze_amount,activity_freeze_amount,freeze_amount)"
			+ " values(#{agentPreFreeze.agentNo},#{agentPreFreeze.agentName},#{agentPreFreeze.freezeTime},#{agentPreFreeze.operater},#{agentPreFreeze.terminalFreezeAmount},#{agentPreFreeze.otherFreezeAmount},#{agentPreFreeze.freezeReason},#{agentPreFreeze.remark},#{agentPreFreeze.fenFreezeAmount},#{agentPreFreeze.activityFreezeAmount},#{agentPreFreeze.freezeAmount})"})
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreFreezeMapper.BaseResultMap")
	int insertAgentPreFreeze(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze);
	
	
	@Insert("<script>"
			+" insert into agent_pre_freeze(agent_no,agent_name,freeze_time,operater,terminal_freeze_amount,other_freeze_amount,freeze_reason,remark)"
            + " values "
            + " <foreach collection =\"list\" item=\"item\" index= \"index\" separator =\",\"> "
			+ " (#{item.agentNo},#{item.agentName},#{item.freezeTime},#{item.operater},#{item.terminalFreezeAmount},#{item.otherFreezeAmount},#{item.freezeReason},#{item.remark})"
            + " </foreach > "
            + " </script>")
	int insertAgentPreFreezeBatch(@Param("list")List<AgentPreFreeze> list);
	
	@Update("update agent_pre_freeze set agent_no=#{agentPreFreeze.agentNo},"
			+ " agent_name=#{agentPreFreeze.agentName},"
			+ " freeze_time=#{agentPreFreeze.freezeTime},"
			+ " operater=#{agentPreFreeze.operater},"
			+ " terminal_freeze_amount=#{agentPreFreeze.terminalFreezeAmount},"
			+ " other_freeze_amount=#{agentPreFreeze.otherFreezeAmount},"
			+ " remark=#{agentPreFreeze.remark}"
			+"   where id=#{agentPreFreeze.id}")
	int updateAgentPreFreeze(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze);
	
	@Delete("delete from agent_pre_freeze where id = #{id}")
	int deleteAgentPreFreeze(@Param("id")Integer id);
	
	@Select("select * from agent_pre_freeze ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreFreezeMapper.BaseResultMap")
	List<AgentPreFreeze> findAllAgentPreFreeze();
	
	@SelectProvider(type=SqlProvider.class,method="findAgentPreFreezeList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreFreezeMapper.BaseResultMap")
	List<AgentPreFreeze> findAgentPreFreezeList(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze,@Param("sort")Sort sort,Page<AgentPreFreeze> page);
	
	@Select("select * from agent_pre_freeze where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreFreezeMapper.BaseResultMap")
	AgentPreFreeze findAgentPreFreezeById(@Param("id")Integer id);
	
	@SelectProvider(type=SqlProvider.class,method="exportAgentsProfitPreFreezeList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentPreFreezeMapper.BaseResultMap")
	List<AgentPreFreeze> exportAgentsProfitPreFreezeList(@Param("agentPreFreeze")AgentPreFreeze agentPreFreeze);
	
	public class SqlProvider{
		public String findAgentPreFreezeList(final Map<String, Object> parameter){
			final AgentPreFreeze agentPreFreeze=(AgentPreFreeze)parameter.get("agentPreFreeze");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("agent_pre_freeze");
				if(StringUtils.isNotBlank(agentPreFreeze.getAgentNo()) && !"ALL".equals(agentPreFreeze.getAgentNo())){
					WHERE(" agent_no=#{agentPreFreeze.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeTime1()) ){
					WHERE(" freeze_time >= #{agentPreFreeze.freezeTime1} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeTime2()) ){
					WHERE(" freeze_time <= #{agentPreFreeze.freezeTime2} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeReason()) && !"ALL".equals(agentPreFreeze.getFreezeReason())){
					if("terminal".equals(agentPreFreeze.getFreezeReason())){//机具预冻结
						WHERE(" terminal_freeze_amount != 0 ");
					}else if("other".equals(agentPreFreeze.getFreezeReason())){//其他
						WHERE(" other_freeze_amount != 0 ");
					}
				}
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("freeze_time desc ");
				}
			}}.toString();
		}
		public String exportAgentsProfitPreFreezeList(final Map<String, Object> parameter){
			final AgentPreFreeze agentPreFreeze=(AgentPreFreeze)parameter.get("agentPreFreeze");
			return new SQL(){{
				SELECT("*");
				FROM("agent_pre_freeze");
				if(StringUtils.isNotBlank(agentPreFreeze.getAgentNo())&& !"ALL".equals(agentPreFreeze.getAgentNo())){
					WHERE(" agent_no=#{agentPreFreeze.agentNo} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeTime1()) ){
					WHERE(" freeze_time >= #{agentPreFreeze.freezeTime1} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeTime2()) ){
					WHERE(" freeze_time <= #{agentPreFreeze.freezeTime2} ");
				}
				if(StringUtils.isNotBlank(agentPreFreeze.getFreezeReason()) && !"ALL".equals(agentPreFreeze.getFreezeReason())){
					if("terminal".equals(agentPreFreeze.getFreezeReason())){//机具预冻结
						WHERE(" terminal_freeze_amount != 0 ");
					}else if("other".equals(agentPreFreeze.getFreezeReason())){//其他
						WHERE(" other_freeze_amount != 0 ");
					}
				}
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","group_time","trans_date","agent_no","agent_name","sale_name","trans_total_amount"};
		    final String[] columns={"id","groupTime","transDate","agentNo","agentName","saleName","transTotalAmount"};
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
