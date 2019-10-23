package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.TeamInfoService;

@Controller
@RequestMapping(value = "/teamInfo")
public class TeamInfoAction {
	private static final Logger log = LoggerFactory.getLogger(TeamInfoAction.class);
	
	@Resource
	private TeamInfoService teamInfoService;
	
	@Resource
	private AgentInfoService agentInfoService;
	
	/**
	 * 查询所有组织的名称
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryTeamName.do")
	@ResponseBody
	public Map<String, Object> queryTeamName(){
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			List<TeamInfo> allTeam = teamInfoService.selectTeamName();
			msg.put("status", true);
			msg.put("teamInfo", allTeam);
		} catch (Exception e){
			log.error("查询所有组织名称失败!");
			msg.put("status", false);
			msg.put("msg", "查询所有组织名称失败!");
		}
		return msg;
	}
	
	/**
	 * 查询所有组织的名称和一级代理商
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/queryTeamAndOneAgent.do")
	@ResponseBody
	public Map<String, Object> queryTeamAndOneAgent(){
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			List<TeamInfo> allTeam = teamInfoService.selectTeamName();
			List<AgentInfo> allAgent = agentInfoService.selectByLevelOne();
			msg.put("status", true);
			msg.put("teamInfo", allTeam);
			msg.put("allAgent", allAgent);
		} catch (Exception e){
			log.error("查询所有组织名称失败!");
			msg.put("status", false);
			msg.put("msg", "查询所有组织名称失败!");
		}
		return msg;
	}
}
