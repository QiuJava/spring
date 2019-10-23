package cn.eeepay.framework.service;

import java.util.List;
import java.util.Map;

import cn.eeepay.framework.model.TeamInfo;

public interface TeamInfoService {

	List<TeamInfo> selectTeamName();
	
	TeamInfo selectByTeamId(String teamId);

	List<Map<String,Object>> getTeams(String agentNo);
}
