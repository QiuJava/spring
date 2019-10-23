package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.model.FunctionManager;
import cn.eeepay.framework.model.IndustrySwitch;
import cn.eeepay.framework.model.IndustrySwitchInfo;
import cn.eeepay.framework.model.Result;
import cn.eeepay.framework.service.FunctionManagerService;
import cn.eeepay.framework.util.BossBaseException;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能控制总开关
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/functionManager")
public class FunctionManagerAction {

	public static  final String FLUSHFUNCTIONNUMBER="015";//充值返功能编码
	public static  final String FLUSHFUNCTIONNUMBER_BUYREWARD="023";//购买鼓励金功能编码
	@Resource
	private FunctionManagerService functionManagerService;

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectFunctionManagers.do")
	@ResponseBody
	public List<FunctionManager> selectFunctionManagers() {
		List<FunctionManager> list = new ArrayList<>();
		try {
			list = functionManagerService.selectFunctionManagers();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value="/updateFunctionSwitch.do")
	@ResponseBody
	@SystemLog(description = "功能控制功能开关",operCode="func.switchManager")
	public  Object updateFunctionSwitch(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		FunctionManager info = JSON.parseObject(param, FunctionManager.class);
		try {
			int i = functionManagerService.updateFunctionSwitch(info);
			if(i>0){
				FunctionManager fm=functionManagerService.getFunctionManager(info.getId());
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(1);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(1);
				}
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
	@RequestMapping(value="/updateAgentControl.do")
	@SystemLog(description = "是否开启代理商控制",operCode="func.agentManager")
	public @ResponseBody Object updateAgentControl(@RequestBody String param )throws Exception{
		Map<String, Object> msg = new HashMap<>();
		FunctionManager info = JSON.parseObject(param, FunctionManager.class);
		try {
			int i = functionManagerService.updateAgentControl(info);
			if(i>0){
				FunctionManager fm=functionManagerService.getFunctionManager(info.getId());
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(2);
				}
				if(FunctionManagerAction.FLUSHFUNCTIONNUMBER_BUYREWARD.equals(fm.getFunctionNumber())){
					ClientInterface.flushActivityCache(2);
				}
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
	
	@RequestMapping(value="/industrySwitch")
	@ResponseBody
	public Result industrySwitch() {
		Result result = new Result();
		try {
			IndustrySwitchInfo info = functionManagerService.getIndustrySwitchInfo();
			result.setStatus(true);
			result.setData(info);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}
	@RequestMapping(value="/industrySwitchSave")
	@ResponseBody
	@SystemLog(description = "保存行业切换配置",operCode="func.industrySwitchActivitySave")
	public Result industrySwitchSave(@RequestBody IndustrySwitch data) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchSave(data);
			result.setStatus(true);
		} catch (Exception e) {
			if (!( e instanceof BossBaseException)) {
				e.printStackTrace();
			}
			result.setStatus(false);
		}
		return result;
	}
	
	@RequestMapping(value="/industrySwitchDelete")
	@ResponseBody
	@SystemLog(description = "删除行业切换配置",operCode="func.industrySwitchActivityDelete")
	public Result industrySwitchDelete(@RequestBody IndustrySwitch data) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchDelete(data.getId());
			result.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
	}
	
	@RequestMapping(value="/industrySwitchUpdate")
	@ResponseBody
	@SystemLog(description = "修改行业切换开关",operCode="func.industrySwitchActivityUpdate")
	public Result industrySwitchUpdate(boolean industrySwitch) {
		Result result = new Result();
		try {
			functionManagerService.industrySwitchUpdate(industrySwitch?1:0);
			result.setStatus(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setStatus(false);
		}
		return result;
		
	}
}
