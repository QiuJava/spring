package cn.eeepay.boss.task;

import cn.eeepay.framework.dao.AgentTransCollectDao;
import cn.eeepay.framework.dao.CollectiveTransOrderDao;
import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.model.AgentTransCollect;
import cn.eeepay.framework.model.SysConfig;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class AgentTransCollectTask extends ScheduleJob {
	private static final String CONFIG_KEY_LAST_ID = "agent_trans_collect_job_collect_trans_order_last_id";
	private static final String CONFIG_KEY_LAST_TIME = "agent_trans_collect_job_last_time";

	private static final Lock lock = new ReentrantLock();
	private static final Log logger = LogFactory.getLog(AgentTransCollectTask.class);

	@Resource
	private AgentTransCollectDao agentTransCollectDao;
	@Resource
	private CollectiveTransOrderDao collectiveTransOrderDao;
	@Resource
	private SysConfigDao sysConfigDao;

	@Override
	protected void runTask(String runNo, Map<String, String> otherParam) {
		logger.info("start 分润计算, runNo : " +runNo + ", otherParam: " + otherParam);
		lock.lock();
		try {
			collectJob();
		}finally {
			lock.unlock();
		}
		logger.info("end 分润计算, runNo : " +runNo + ", otherParam: " + otherParam);
	}

	public void collectJob() {
		System.out.println("-----------定时任务---------"+new Date());
		logger.info("collectJob start at: " + new Date());
		try {
			doJob();
		} catch (Exception e) {
			logger.error("collectJob throws Exception", e);
		}
		logger.info("collectJob end at: " + new Date());
	}

	@SuppressWarnings("rawtypes")
	protected void doJob() {
		int id = 0;
		boolean hasConfig = false;
		List<SysConfig> configs = sysConfigDao.getByKey(CONFIG_KEY_LAST_ID);
		if (configs != null && !configs.isEmpty()) {
			hasConfig = true;
			try {
				final SysConfig sysConfig = configs.get(0);
				id = Integer.parseInt(sysConfig.getParamValue());
			} catch (Exception e) {
				logger.info("parse config Exception", e);
			}
		}
		
		String maxIdResult = collectiveTransOrderDao.selectMaxId();//取交易表最新MAXID
		if (StringUtils.isBlank(maxIdResult)) {
			logger.info("交易记录最大值取不到");
			return;
		}
		int maxId = Integer.parseInt(maxIdResult);
		List<Map> list = collectiveTransOrderDao.selectGroupAfterId(id + 1, maxId);
		if (list == null || list.isEmpty()) {
			logger.info("no data to collect");
			return;
		}
		logger.info(list.size() + " data to collect");
		AgentTransCollect record = new AgentTransCollect();
		for (Map item : list) {
			final String agentNode = (String) item.get("agent_node");
			if (StringUtils.isBlank(agentNode))
				continue;
			String[] agents = agentNode.split("-");
			if (agents.length <= 1)
				continue;
			String newAgentNode = "0-";
			record.setBpId(String.valueOf(item.get("business_product_id")));
			record.setServiceId(String.valueOf(item.get("service_id")));
			record.setCalcTime((Date)item.get("trans_time"));
			record.setTransTime((Date)item.get("trans_time"));
			record.setSingleCount(Integer.parseInt(item.get("total_count").toString()));
			record.setTransAmount((BigDecimal) item.get("trans_amount"));
			for (int i = 1; i < agents.length; i++) {
				newAgentNode = newAgentNode  + agents[i] + "-";
				BigDecimal sum = (BigDecimal) item.get("profits_" + i);
				if (sum == null || BigDecimal.ZERO.equals(sum))
					continue;
				record.setId(null);
				record.setAgentNo(agents[i]);
				record.setAgentNode(newAgentNode);
				record.setAgentShareAmount(sum);
				agentTransCollectDao.insert(record);
			}
		}
		SysConfig config = new SysConfig();
		config.setParamKey(CONFIG_KEY_LAST_ID);
		config.setParamValue(String.valueOf(maxId));
		config.setRemark("代理商分润统计job collective_trans_order最后的id");
		if (hasConfig) {
			sysConfigDao.updateByParamKey(config);
		} else {
			sysConfigDao.insert(config);
		}
		try {
			config.setParamKey(CONFIG_KEY_LAST_TIME);
			config.setParamValue(new Date().toString());
			config.setRemark("代理商分润统计job 最后的时间");
			configs = sysConfigDao.getByKey(CONFIG_KEY_LAST_TIME);
			if (configs != null && !configs.isEmpty()) {
				sysConfigDao.updateByParamKey(config);
			} else {
				sysConfigDao.insert(config);
			}
		} catch (Exception e) {
			logger.info("统计完成，更新最后时间失败", e);
		}
	}
	
}
