package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.SupertuiRule;
import cn.eeepay.framework.service.PushService;

/**
 * 超级推控制器
 * 
 * @author junhu
 *
 */
@Controller
@RequestMapping(value = "/push")
public class PushAction {

	private static final Logger log = LoggerFactory.getLogger(PushAction.class);

	@Resource
	private PushService pushService;

	@RequestMapping(value = "/addSupertuiRule.do", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> addSupertuiRule(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			SupertuiRule supertuiRule = json.getObject("info", SupertuiRule.class);
			int num = pushService.insertSupertuiRule(supertuiRule);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "添加规则成功！");
			}
		} catch (Exception e) {
			log.error("添加规则失败！", e);
			msg.put("status", false);
			msg.put("msg", "添加规则失败！");
		}
		return msg;
	}

	@RequestMapping(value = "/querySupertuiRuleList.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<SupertuiRule> querySupertuiRuleList(@RequestParam("info") String param,
			@ModelAttribute("page") Page<SupertuiRule> page) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			pushService.getSupertuiRule(jsonMap, page);
		} catch (Exception e) {
			log.error("查询规则列表失败！", e);
		}
		return page;
	}

	@RequestMapping(value = "/getSupertuiRule.do", method = RequestMethod.POST)
	@ResponseBody
	public SupertuiRule getSupertuiRule(@RequestBody String param) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			return pushService.getSupertuiRule(jsonMap);
		} catch (Exception e) {
			log.error("获取规则失败！", e);
		}
		return null;
	}

	@RequestMapping(value = "/updateSupertuiRule.do", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateSupertuiRule(@RequestBody String param) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			SupertuiRule supertuiRule = json.getObject("newInfo", SupertuiRule.class);
			int num = pushService.updateSupertuiRule(supertuiRule);
			if (num > 0) {
				msg.put("status", true);
				msg.put("msg", "修改规则成功！");
			}
		} catch (Exception e) {
			log.error("修改规则失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改规则失败！");
		}
		return msg;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/querySupertuiOrderList.do", method = RequestMethod.POST)
	@ResponseBody
	public Page<Map> querySupertuiOrderList(@RequestParam("info") String param,
			@ModelAttribute("page") Page<Map> page) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			pushService.getSupertuiOrder(jsonMap, page);
		} catch (Exception e) {
			log.error("查询订单列表失败！", e);
		}
		return page;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getSupertuiOrderInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public Map getSupertuiOrderInfo(@RequestBody String param) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = JSON.parseObject(param, HashMap.class);
			return pushService.getSupertuiOrderInfo(jsonMap);
		} catch (Exception e) {
			log.error("获取订单失败！", e);
		}
		return null;
	}
}
