package cn.eeepay.framework.dao.nposp;

import cn.eeepay.framework.model.nposp.AgentInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

public interface AgentInfoMapper {
	@Select("select agent_no,agent_name,mobilephone ,agent_level from agent_info where agent_no = #{userId}")
	@ResultType(AgentInfo.class)
	AgentInfo findAgentInfoByUserId(@Param("userId") String userId);

	@SelectProvider(type = SqlProvider.class, method = "findAgentListByParams")
	@ResultType(String.class)
	List<String> findAgentListByParams(@Param("userName") String userName, @Param("mobilephone") String mobilephone);

	@Select("select * from agent_info where agent_no=#{agentNo}")
	@ResultType(AgentInfo.class)
	AgentInfo findEntityByAgentNo(@Param("agentNo")String agentNo);
	
	@Select("select * from agent_info where id=#{id}")
	@ResultType(AgentInfo.class)
	AgentInfo findEntityById(@Param("id")String id);
	
	
	@Select("select * from agent_info where agent_level=#{level}")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findEntityByLevel(@Param("level")String level);
	
	
	@Select("select * from agent_info where agent_level=#{level} and profit_switch=1 ")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findEntityByLevelSwitch(@Param("level")Integer level);
	
	
	@Select("select * from agent_info where parent_id=#{parentAgentNo} and profit_switch=1 ")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findOpenDirectEntityByParentAgentNo(@Param("parentAgentNo")String parentAgentNo);
	
	
	@Select("select * from agent_info ")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findAllAgentInfoList();
	
	@Select("select * from agent_info where agent_level = 1 ")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findAllOneAgentInfoList();

	@SelectProvider(type = SqlProvider.class, method = "findSelectAgentInfo")
	@ResultType(AgentInfo.class)
	List<AgentInfo> findSelectAgentInfo(@Param("agentInfo") AgentInfo agentInfo,@Param("limit") Integer limit);
	
	@SelectProvider(type = SqlProvider.class, method = "findAgentListByAgentNo")
	@ResultType(String.class)
	List<String> findAgentListByAgentNo(@Param("agentNo")String agentNo);


	@SelectProvider(type = SqlProvider.class, method = "findAgentList")
	@ResultType(String.class)
	List<String>   findAgentList(@Param("agentInfo")AgentInfo agentInfo);
	
	public class SqlProvider {

		public String findAgentListByParams(final Map<String, Object> parameter) {
			final String mobilephone = parameter.get("mobilephone").toString();
			final String userName = parameter.get("userName").toString();

			return new SQL() {
				{
					SELECT(" agent_no ");
					FROM(" agent_info ");
					if (!StringUtils.isBlank(mobilephone))
						WHERE(" mobilephone like  \"%\"#{mobilephone}\"%\" ");
					if (!StringUtils.isBlank(userName))
						WHERE(" agent_name like  \"%\"#{userName}\"%\" ");
				}
			}.toString();
		}
		
		public String findSelectAgentInfo(final Map<String, Object> parameter) {
			final AgentInfo agentInfo = (AgentInfo) parameter.get("agentInfo");
			final Integer limit = (Integer) parameter.get("limit");
			return new SQL(){{
				SELECT(" agent_no,agent_name ");
				FROM(" agent_info ");
				if (!StringUtils.isBlank(agentInfo.getAgentNo()))
					WHERE(" agent_no like  \"%\"#{agentInfo.agentNo}\"%\" or agent_name like  \"%\"#{agentInfo.agentName}\"%\" ");
				ORDER_BY(" agent_no  limit "+limit);
			}}.toString();
		}
		
		public String findAgentListByAgentNo(final Map<String, Object> parameter) {
			final String agentNo = (String) parameter.get("agentNo");
			return new SQL(){{
				SELECT(" agent_no");
				FROM(" agent_info ");
				//WHERE(" agent_level = 1 ");
				if (!StringUtils.isBlank(agentNo) && !"ALL".equals(agentNo))
					WHERE(" agent_no = #{agentNo} ");
			}}.toString();
		}

		public String findAgentList(final Map<String, Object> parameter) {
			AgentInfo agentInfo = (AgentInfo)parameter.get("agentInfo");
			String agentNo = agentInfo.getAgentNo();
			String agentLevel = agentInfo.getLevel();

			return new SQL(){{
				SELECT(" agent_no,agent_name , agent_level ");
				FROM(" agent_info ");
				if (!StringUtils.isBlank(agentNo))
					WHERE(" agent_no = #{agentInfo.agentNo} ");
				if (!StringUtils.isBlank(agentLevel)&&!"ALL".equals(agentLevel))
					WHERE(" agent_level = #{agentInfo.level} ");
				ORDER_BY(" agent_no ");
			}}.toString();
		}
	}


}
