package cn.eeepay.boss.action;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.OptLogs;
import cn.eeepay.framework.service.bill.OptLogsService;

/**
 * 操作日志管理
 * 
 * by wangchangkuan
 * email wck@eeepay.cn@eeepay.cn
 * 2016年12月12日10:45:54
 *
 */
@Controller
@RequestMapping(value = "/optLogsAction")
public class OptLogsAction {
	
	private static final Logger log = LoggerFactory.getLogger(OptLogsAction.class);
	
	@Resource
	private OptLogsService optLogsService;
	
	//跳转到  对账记账日志查询 页面
	@RequestMapping(value = "/toDuiAccountOptLogs.do")
	public String toDuiAccountOperateLog(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		return "logs/duiAccountOptLogsQuery";
	}
	
	@RequestMapping(value = "/findDuiAccountOptLogsList.do")
	@ResponseBody
	public Page<OptLogs> findDuiAccountOperateLogList(
			@ModelAttribute("operateLog") OptLogs operateLog, 
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OptLogs> page) {
		try {
			operateLog.setLogType("duiAccountLog");
			optLogsService.findOperateLog(operateLog, params, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}

		return page;
	}
	
	//跳转到  确认出账日志查询 页面
	@RequestMapping(value = "/toConfirmAccountOptLogs.do")
	public String toConfirmAccountOperateLog(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		return "logs/confirmAccountOptLogs";
	}

	@RequestMapping(value = "/findConfirmAccountOptLogsList.do")
	@ResponseBody
	public Page<OptLogs> findConfirmDuiAccountOperateLogList(
			@ModelAttribute("operateLog") OptLogs operateLog, 
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OptLogs> page) {
		try {
			operateLog.setLogType("confirmAccountLog");
			optLogsService.findOperateLog(operateLog, params, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}

		return page;
	}
	
}
