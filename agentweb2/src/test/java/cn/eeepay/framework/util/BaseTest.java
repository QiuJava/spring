package cn.eeepay.framework.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.eeepay.framework.dao.AgentTransCollectDao;
import cn.eeepay.framework.dao.CollectiveTransOrderDao;
import cn.eeepay.framework.dao.SysConfigDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.model.AgentTransCollect;
import cn.eeepay.framework.model.SysConfig;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.AgentInfoService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContext.xml" })
public class BaseTest extends AbstractJUnit4SpringContextTests {

	private static final String CONFIG_KEY_LAST_ID = "agent_trans_collect_job_collect_trans_order_last_id";
	private static final String CONFIG_KEY_LAST_TIME = "agent_trans_collect_job_last_time";

	private final Log logger = LogFactory.getLog(getClass());

	@Resource
	private AgentTransCollectDao agentTransCollectDao;
	@Resource
	private CollectiveTransOrderDao collectiveTransOrderDao;
	@Resource
	private SysConfigDao sysConfigDao;
	@Resource
	private SysDictDao sysDictDao;
	
	@Autowired
	private AgentInfoService agentInfoService;

	
	@Test
	public void testAgentNo() {
		agentInfoService.getConfigInfo("4929");
	}
	
	@Test
	public void test(){
		String str="0-1452-";
		String[] agents = str.split("-");
		
	}
	@Test
	public void testYL() throws ParseException {
		List<SysDict> dictList = sysDictDao.findSysDictGroup("AGENT_SHARE_STATE");
		if (!dictList.isEmpty()) {
			SysDict state = dictList.get(0);
			if ("STOP".equalsIgnoreCase(state.getSysValue())) {
				//修改状态为running
				state.setSysValue("running");
				sysDictDao.update(state);

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
				List<Map> list = collectiveTransOrderDao.selectGroupAfterId(id,1);
				if (list == null || list.isEmpty()) {
					// if(logger.isDebugEnabled()){
					logger.debug("no data to collect");
					// }
					return;
				}
				// if(logger.isDebugEnabled()){
				logger.debug(list.size() + " data to collect");
				// }

				AgentTransCollect record = new AgentTransCollect();
				for (Map item : list) {
					Integer id2 = (Integer) item.get("id");
					id = Math.max(id, id2);
					final String agentNode = (String) item.get("agent_node");
					if (StringUtils.isBlank(agentNode))
						continue;
					String[] agents = agentNode.split("-");
					if (agents.length < 1)
						continue;
					String newAgentNode = "0";
					record.setBpId(String.valueOf(item.get("business_product_id")));
					record.setServiceId(String.valueOf(item.get("service_id")));
					record.setCalcTime((Date)item.get("trans_time"));
					record.setTransTime((Date)item.get("trans_time"));
					record.setTransAmount((BigDecimal) item.get("trans_amount"));
					for (int i = 1; i < agents.length; i++) {
						newAgentNode = newAgentNode + "-" + agents[i] + "-";
						BigDecimal sum = (BigDecimal) item.get("profits_" + i);
						if (sum == null || BigDecimal.ZERO.equals(sum))
							continue;
						record.setId(null);
						Integer singleCount = ((Number) item.get("count_" + i)).intValue();
						record.setSingleCount(singleCount);
						record.setAgentNo(agents[i]);
						record.setAgentNode(newAgentNode);
						record.setAgentShareAmount(sum);
						agentTransCollectDao.insert(record);
					}
				}

				SysConfig config = new SysConfig();
				config.setParamKey(CONFIG_KEY_LAST_ID);
				config.setParamValue(String.valueOf(id));
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

				//执行完之后修改字典值为stop
				state.setSysValue("stop");
				sysDictDao.update(state);
			}
		}
	}
	
}
