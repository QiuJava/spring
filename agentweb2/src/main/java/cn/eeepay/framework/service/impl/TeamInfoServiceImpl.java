package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.TeamInfoDao;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.TeamInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("teamInfoService")
public class TeamInfoServiceImpl implements TeamInfoService {

	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Override
	public List<TeamInfo> selectTeamName() {
		return teamInfoDao.selectTeamName();
	}

	@Override
	public TeamInfo selectByTeamId(String teamId) {
		return teamInfoDao.selectByTeamId(teamId);
	}

	@Override
	public List<Map<String, Object>> getTeams(String agentNo) {
		return teamInfoDao.getTeams(agentNo);
	}
}
