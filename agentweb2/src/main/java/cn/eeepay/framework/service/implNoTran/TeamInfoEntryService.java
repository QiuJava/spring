package cn.eeepay.framework.service.implNoTran;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.eeepay.framework.dao.TeamInfoEntryMapper;
import cn.eeepay.framework.model.TeamInfoEntry;

/**
 * 子组织服务
 *
 * @author Qiujian
 *
 */
@Service
public class TeamInfoEntryService {

	@Autowired
	private TeamInfoEntryMapper teamInfoEntryMapper;

	public List<TeamInfoEntry> listByTeamId(String merTeamId) {
		return teamInfoEntryMapper.findTeamInfoEntryByTeamId(merTeamId);
	}

}
