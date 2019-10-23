package cn.eeepay.boss.action;

import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.SysDict;
import cn.eeepay.framework.service.SysDictService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
@Controller
@RequestMapping(value="/sysDict")
public class SysDictAction {

	private static final Logger log = LoggerFactory.getLogger(SysDictAction.class);
	
	@Resource
	private SysDictService sysDictService;

	@RequestMapping(value="findAgentWebSwitch.do")
	@ResponseBody
	public ResponseBean findAgentWebSwitch(){
		Map<String, Boolean> resultMap = new HashMap<>();
		try {

			boolean shareSwitch = false;
			boolean promotionSwitch = false;
			boolean cashBackSwitch = false;
			SysDict agentWebShareSwitch = sysDictService.findAgentWebShareSwitch();
			SysDict agentWebPromotionSwitch = sysDictService.findAgentWebPromotionSwitch();
			SysDict agentWebCashBackSwitch = sysDictService.findAgentWebCashBackSwitch();

			if (agentWebShareSwitch != null && !StringUtils.equals("0", agentWebShareSwitch.getSysValue())){
				shareSwitch = true;
			}
			if (agentWebPromotionSwitch != null && !StringUtils.equals("0", agentWebPromotionSwitch.getSysValue())){
				promotionSwitch = true;
			}
			if (agentWebCashBackSwitch != null && !StringUtils.equals("0", agentWebCashBackSwitch.getSysValue())){
				cashBackSwitch = true;
			}
			resultMap.put("agentWebShareSwitch", shareSwitch);
			resultMap.put("agentWebPromotionSwitch", promotionSwitch);
			resultMap.put("agentWebCashBackSwitch", cashBackSwitch);
		}catch (Exception e){
			resultMap.put("agentWebShareSwitch", false);
			resultMap.put("agentWebPromotionSwitch", false);
			resultMap.put("agentWebCashBackSwitch", false);
			log.error("异常信息: " , e);
		}
		return new ResponseBean(resultMap);
	}
	@RequestMapping(value="selectDictAndChildren.do")
	@ResponseBody
	public Object selectDictAndChildren() throws Exception{
		Map<String, Object> msg = null;
		try {
			msg = sysDictService.selectDictAndChildren();
		} catch (Exception e) {
			log.error("查询所有的数据字典失败!");
		}
		return msg;
	}

	@RequestMapping(value="listSysDictGroup.do")
	@ResponseBody
	public ResponseBean listSysDictGroup(String keyName){
		try {
			return new ResponseBean(sysDictService.listSysDictGroup(keyName));
		} catch (Exception e) {
			log.error("查询所有的数据字典失败!" + e.getMessage());
			return new ResponseBean(e);
		}
	}

	@RequestMapping(value = "getByKey.do")
	@ResponseBody
	public ResponseBean getByKey(String sysKey){
		try {
			String sysValue = sysDictService.SelectServiceId(sysKey);
			ResponseBean responseBean = new ResponseBean(true);
			responseBean.setData(sysValue);
			return responseBean;
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}
}
