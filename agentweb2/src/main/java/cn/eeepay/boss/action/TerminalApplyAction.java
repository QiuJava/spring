package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.service.AccessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.TerminalApply;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.TerminalApplyService;

@Controller
@RequestMapping(value="/terminalApplyAction")
public class TerminalApplyAction {
	private static final Logger log = LoggerFactory.getLogger(TerminalApplyAction.class);
	
	@Resource
	private TerminalApplyService terminalApplyService;
	@Resource
	private AccessService accessService;
	
	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public ResponseBean selectAllInfo(@RequestBody TerminalApply terminalApply,
									  @RequestParam(defaultValue = "1") int pageNo,
									  @RequestParam(defaultValue = "10") int pageSize)throws Exception{

		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			Page<TerminalApply> page = new Page<>(pageNo, pageSize);
			List<TerminalApply> terminalApplies = terminalApplyService.queryAllInfo(page, terminalApply, loginAgentNo);
			return new ResponseBean(terminalApplies, page.getTotalCount());
		} catch (Exception e) {
			log.error("机具申请查询报错",e);
			return new ResponseBean(e);
		}
	}	
	
	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/selectDetail")
	@ResponseBody
	public Object selectDetail(@RequestParam("ids") String ids)throws Exception{
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			TerminalApply ta = terminalApplyService.queryInfoDetail(ids);
			jsonMap.put("result", ta);
			jsonMap.put("bols", true);
		} catch (Exception e) {
			log.error("机具申请详情查询报错",e);
			System.out.println(e.getMessage());
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改状态
	 * @return
	 * @throws Exception
	 */
	@SystemLog(description = "处理机具申请")
	@RequestMapping(value="/dealWithTerminalApply")
	@ResponseBody
	public ResponseBean dealWithTerminalApply(@RequestBody TerminalApply terminalApply){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			terminalApplyService.dealWithTerminalApply(terminalApply,loginAgentNo);
			return new ResponseBean("操作成功", true);
		} catch (Exception e) {
			log.error("机具申请详情查询报错",e);
			return new ResponseBean(e);
		}
	}
}
