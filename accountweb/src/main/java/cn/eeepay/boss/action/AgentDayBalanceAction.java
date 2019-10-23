package cn.eeepay.boss.action;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.service.bill.impl.AgentDayBalanceServiceImpl;
import cn.eeepay.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 汇总代理商分润活动账户数据
 *
 */
@Controller
@RequestMapping(value = "/agentDayBalanceAction")
public class AgentDayBalanceAction {


	@Resource
	public AgentDayBalanceServiceImpl agentDayBalanceService;
	
	
	private static final Logger log = LoggerFactory.getLogger(AgentDayBalanceAction.class);



	@RequestMapping(value = "/agentAccountDayCollection")
	@Logs(description="代理商账户金额汇总")
	@ResponseBody
	public Object agentAccountDayCollection(@RequestParam Map<String,String> params) throws Exception {

		Map<String,String> map = new HashMap<>();
		try {
			log.info("=======代理商每日账户汇总开始============");
			String nowDate = params.get("nowDate");
			if(nowDate==null || "".equals(nowDate) ){
				nowDate = DateUtil.getCurrentDate();
			}

			agentDayBalanceService.getAgentDayBalance(nowDate);

			log.info("=======代理商每日账户汇总完成============");
			map.put("result","代理商账户金额汇总");

		}catch (Exception e) {

			log.error("异常:", e.getMessage());
		}
		return map;
	}

	

}
