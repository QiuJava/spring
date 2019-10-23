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
import cn.eeepay.framework.model.bill.AgentUnfreeze;
/**
 * 代理商解冻表
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2017年5月26日14:59:14
 *
 */
public interface AgentUnfreezeMapper {
	@Insert({"insert into agent_unfreeze(agent_no,agent_name,unfreeze_time,operater,amount,remark,terminal_freeze_amount,other_freeze_amount,fen_freeze_amount,activity_freeze_amount)"
			+ " values(#{agentUnfreeze.agentNo},#{agentUnfreeze.agentName},#{agentUnfreeze.unfreezeTime},#{agentUnfreeze.operater},#{agentUnfreeze.amount},#{agentUnfreeze.remark},#{agentUnfreeze.terminalFreezeAmount},#{agentUnfreeze.otherFreezeAmount},#{agentUnfreeze.fenFreezeAmount},#{agentUnfreeze.activityFreezeAmount})"})
	@ResultMap("cn.eeepay.framework.dao.bill.AgentUnfreezeMapper.BaseResultMap")
	int insertAgentUnfreeze(@Param("agentUnfreeze")AgentUnfreeze agentUnfreeze);
	
	@Update("update agent_unfreeze set agent_no=#{agentUnfreeze.agentNo},"
			+ " agent_name=#{agentUnfreeze.agentName},"
			+ " unfreeze_time=#{agentUnfreeze.unfreezeTime},"
			+ " operater=#{agentUnfreeze.operater},"
			+ " amount=#{agentUnfreeze.amount},"
			+ " remark=#{agentUnfreeze.remark}"
			+"   where id=#{agentUnfreeze.id}")
	int updateAgentUnfreeze(@Param("agentUnfreeze")AgentUnfreeze agentUnfreeze);
	
	@Delete("delete from agent_unfreeze where id = #{id}")
	int deleteAgentUnfreeze(@Param("id")Integer id);
	
	@Select("select * from agent_unfreeze ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentUnfreezeMapper.BaseResultMap")
	List<AgentUnfreeze> findAllAgentUnfreeze();
	
	@SelectProvider(type=SqlProvider.class,method="findAgentUnfreezeList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentUnfreezeMapper.BaseResultMap")
	List<AgentUnfreeze> findAgentUnfreezeList(@Param("agentUnfreeze")AgentUnfreeze agentUnfreeze,@Param("params")Map<String, String> params,@Param("sort")Sort sort,Page<AgentUnfreeze> page);
	
	@Select("select * from agent_unfreeze where id = #{id} ")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentUnfreezeMapper.BaseResultMap")
	AgentUnfreeze findAgentUnfreezeById(@Param("id")Integer id);
	
	@SelectProvider(type=SqlProvider.class,method="exportAgentsProfitUnfreezeList")
	@ResultMap("cn.eeepay.framework.dao.bill.AgentUnfreezeMapper.BaseResultMap")
	List<AgentUnfreeze> exportAgentsProfitUnfreezeList(@Param("AgentUnfreeze")AgentUnfreeze agentUnfreeze,@Param("params")Map<String, String> params);
	
	public class SqlProvider{
		public String findAgentUnfreezeList(final Map<String, Object> parameter){
			final AgentUnfreeze agentUnfreeze=(AgentUnfreeze)parameter.get("agentUnfreeze");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String unfreezeTime1 = params.get("unfreezeTime1");
			final String unfreezeTime2 = params.get("unfreezeTime2");
			final Sort sord=(Sort)parameter.get("sort");
			return new SQL(){{
				SELECT("*");
				FROM("agent_unfreeze");
				if(StringUtils.isNotBlank(agentUnfreeze.getAgentNo()) && !"ALL".equals(agentUnfreeze.getAgentNo())){
					WHERE(" agent_no=#{agentUnfreeze.agentNo} ");
				}
				if (unfreezeTime1 != null && StringUtils.isNotBlank(unfreezeTime1))
					WHERE(" unfreeze_time >= #{params.unfreezeTime1} ");
				if (unfreezeTime2 != null && StringUtils.isNotBlank(unfreezeTime2))
					WHERE(" unfreeze_time <= #{params.unfreezeTime2} ");
				if(sord != null && StringUtils.isNotBlank(sord.getSidx())){
					ORDER_BY(propertyMapping(sord.getSidx(), 0)+" "+sord.getSord());
				} else {
					ORDER_BY("unfreeze_time desc ");
				}
			}}.toString();
		}
		public String exportAgentsProfitUnfreezeList(final Map<String, Object> parameter){
			final AgentUnfreeze agentUnfreeze=(AgentUnfreeze)parameter.get("agentUnfreeze");
			final Map<String, String> params = (Map<String, String>) parameter.get("params");
			final String unfreezeTime1 = params.get("unfreezeTime1");
			final String unfreezeTime2 = params.get("unfreezeTime2");
			return new SQL(){{
				SELECT("*");
				FROM("agent_unfreeze");
				if(StringUtils.isNotBlank(agentUnfreeze.getAgentNo())&& !"ALL".equals(agentUnfreeze.getAgentNo())){
					WHERE(" agent_no=#{agentUnfreeze.agentNo} ");
				}
				if (unfreezeTime1 != null && StringUtils.isNotBlank(unfreezeTime1))
					WHERE(" unfreeze_time >= #{params.unfreezeTime1} ");
				if (unfreezeTime2 != null && StringUtils.isNotBlank(unfreezeTime2))
					WHERE(" unfreeze_time <= #{params.unfreezeTime2} ");
			}}.toString();
		}
		
		public String propertyMapping(String name,int type){
			final String[] propertys={"id","agent_no","agent_name","operater","amount"};
		    final String[] columns={"id","agentNo","agentName","operater","amount"};
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
