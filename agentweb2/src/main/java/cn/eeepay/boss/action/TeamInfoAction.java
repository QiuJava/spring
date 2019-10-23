package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.framework.model.UserLoginInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.TeamInfo;
import cn.eeepay.framework.service.TeamInfoService;

@Controller
@RequestMapping(value = "/teamInfo")
public class TeamInfoAction {
	private static final Logger log = LoggerFactory.getLogger(TeamInfoAction.class);
	
	@Resource
	private TeamInfoService teamInfoService;
	
	/**
	 * 查询所有组织的名称
	 */
	@RequestMapping(value="/queryTeamName.do")
	@ResponseBody
	public Map<String, Object> queryTeamName() throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			List<TeamInfo> list = teamInfoService.selectTeamName();
			msg.put("status", true);
			msg.put("teamInfo", list);
		} catch (Exception e){
			log.error("查询所有组织名称失败!");
			msg.put("status", false);
			msg.put("msg", "查询所有组织名称失败!");
		}
		return msg;
	}

	@RequestMapping("/getTeams")
	@ResponseBody
	public List<Map<String,Object>> getTeams(String agentNo){
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!org.springframework.util.StringUtils.hasLength(agentNo)) {
			agentNo = userInfo.getUserEntityInfo().getEntityId();
		}
		try {
			List<Map<String,Object>> result = teamInfoService.getTeams(agentNo);
			return result;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
}
