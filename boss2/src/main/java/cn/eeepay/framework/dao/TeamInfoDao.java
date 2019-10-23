package cn.eeepay.framework.dao;

import java.util.List;

import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import cn.eeepay.framework.model.TeamInfo;

public interface TeamInfoDao {

	@Select("SELECT team_id,team_name FROM team_info")
	@ResultType(TeamInfo.class)
	List<TeamInfo> selectTeamName();

	@Select("select * from team_info where team_id = #{teamId}")
	@ResultType(TeamInfo.class)
	TeamInfo selectTeamInfo(Integer teamId);
}
