package cn.eeepay.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.TeamInfoDao;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.TeamInfoService;

@Service("teamInfoService")
@Transactional
public class TeamInfoServiceImpl implements TeamInfoService {

	@Resource
	private TeamInfoDao teamInfoDao;
	
	@Override
	public List<TeamInfo> selectTeamName() {
		return teamInfoDao.selectTeamName();
	}

}
