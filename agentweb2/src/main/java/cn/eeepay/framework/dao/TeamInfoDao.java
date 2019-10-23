package cn.eeepay.framework.dao;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.util.WriteReadDataSource;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.TeamInfo;
import org.springframework.web.bind.annotation.RequestParam;

@WriteReadDataSource
public interface TeamInfoDao {

	@Select("SELECT team_id,team_name FROM team_info")
	@ResultType(TeamInfo.class)
	List<TeamInfo> selectTeamName();
	
	@Select("SELECT team_id,team_name FROM team_info where team_id=#{teamId}")
	@ResultType(TeamInfo.class)
	TeamInfo selectByTeamId(String teamId);

	@Select("SELECT DISTINCT a_i.team_name AS text,a_i.team_id AS value " +
			"FROM app_info a_i " +
			"LEFT JOIN business_product_define b_p_d ON b_p_d.team_id = a_i.team_id " +
			"LEFT JOIN agent_business_product a_b_p ON a_b_p.bp_id = b_p_d.bp_id " +
			"WHERE a_b_p.agent_no = #{agentNo}")
	@ResultType(Map.class)
	List<Map<String,Object>> getTeams(@RequestParam("agentNo") String agentNo);
}
