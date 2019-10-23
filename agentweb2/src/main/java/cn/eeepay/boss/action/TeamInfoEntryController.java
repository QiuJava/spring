package cn.eeepay.boss.action;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.model.TeamInfoEntry;
import cn.eeepay.framework.service.implNoTran.TeamInfoEntryService;
import cn.eeepay.framework.util.Result;

/**
 * 子组织控制器
 *
 * @author Qiujian
 *
 */
@Controller
@RequestMapping("/teamInfoEntry")
public class TeamInfoEntryController {
	
	private static final Logger log = LoggerFactory.getLogger(TeamInfoEntryController.class);
	
	@Autowired
	private TeamInfoEntryService teamInfoEntryService;
	
	@RequestMapping("/getTeamEntryIdList")
	@ResponseBody
	public Result getTeamEntryIdList(String merTeamId) {
		Result result = new Result();
		try {
			List<TeamInfoEntry> list = teamInfoEntryService.listByTeamId(merTeamId);
			result.setData(list);
			result.setStatus(true);
		} catch (Exception e) {
			log.error("查询异常", e);
			result.setStatus(false);
		}
		return result;
	}
}
