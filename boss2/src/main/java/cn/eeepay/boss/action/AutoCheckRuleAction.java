package cn.eeepay.boss.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.framework.model.AutoCheckRoute;
import cn.eeepay.framework.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.AutoCheckRule;
import cn.eeepay.framework.model.SysConfigAutoCheck;
import cn.eeepay.framework.service.AutoCheckRuleService;

@Controller
@RequestMapping(value = "/autoCheckRule")
public class AutoCheckRuleAction {
	
	@Resource
	private AutoCheckRuleService autoCheckRuleService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/autoCheckRule.do")
	@SuppressWarnings("all")
	public @ResponseBody Object selectByParamKey(String paramKey) throws Exception {
		List list = new ArrayList<>();
		try {
			SysConfigAutoCheck  singleMerchTimes = autoCheckRuleService.selectByParamKey("single_merch_times");
			SysConfigAutoCheck phohoCompProp = autoCheckRuleService.selectByParamKey("phoho_comp_prop");
			SysConfigAutoCheck bankCardOcr = autoCheckRuleService.selectByParamKey("bank_card_ocr");
			SysConfigAutoCheck idCardOcr = autoCheckRuleService.selectByParamKey("id_card_ocr");
			SysConfigAutoCheck age = autoCheckRuleService.selectByParamKey("age_limit");
			SysConfigAutoCheck minAge = age;
			
			
			list.add(singleMerchTimes);
			list.add(phohoCompProp);
			list.add(bankCardOcr);
			list.add(idCardOcr);
			list.add(minAge);
			List<AutoCheckRoute> livingList  = autoCheckRuleService.listByRouteType(1);
			list.addAll(livingList);
			List<AutoCheckRoute> ocrList  = autoCheckRuleService.listByRouteType(2);
			list.addAll(ocrList);
			List<AutoCheckRoute> realList  = autoCheckRuleService.listByRouteType(3);
			list.addAll(realList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 规则查询
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/autoCheckRuleAll.do")
	@ResponseBody
	public List<AutoCheckRule> selectAll() throws Exception {
		List<AutoCheckRule> list=new ArrayList<>();
		try {
			
			 list = autoCheckRuleService.selectAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}
	
	@RequestMapping(value="/updateIsOpen.do")
	@SystemLog(description = "自动审件是否打开",operCode="func.switch")
	public @ResponseBody Object updateIsOpen(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		AutoCheckRule info = JSON.parseObject(param, AutoCheckRule.class);
		try {
			int i = autoCheckRuleService.updateIsOpen(info);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	@RequestMapping(value="/updateIsPass.do")
	@SystemLog(description = "自动审件是否必过",operCode="func.must")
	public @ResponseBody Object updateIsPass(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		AutoCheckRule info = JSON.parseObject(param, AutoCheckRule.class);
		try {
			int i = autoCheckRuleService.updateIsPass(info);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "设置成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "设置失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	
	/**
	 * 保存修改自动审件控制
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/updateValues.do")
	@SystemLog(description = "保存修改自动审件控制",operCode="func.save")
	public @ResponseBody Object updateValue(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		@SuppressWarnings("unchecked")
		Map<String, Object> sysConfigAutoCheck = JSON.parseObject(param, Map.class);
		try {
			int i = autoCheckRuleService.updateValue(sysConfigAutoCheck);
			if(i>0){
				msg.put("status", true);
				msg.put("msg", "修改成功");
			} else {
				msg.put("status", false);
				msg.put("msg", "修改失败");
			}
		} catch (Exception e) {
			msg.put("msg", "服务异常");
			e.printStackTrace();
			msg.put("status", false);
		}
		return msg;
	}
	
}
