package cn.eeepay.boss.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.SuperPushShareDaySettle;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.SuperPushShare;
import cn.eeepay.framework.service.bill.AgentPreAdjustService;
import cn.eeepay.framework.service.bill.AgentPreFreezeService;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.bill.AgentUnfreezeService;
import cn.eeepay.framework.service.bill.AgentsProfitAssemblyOrParsing;
import cn.eeepay.framework.service.bill.DuiAccountDetailService;
import cn.eeepay.framework.service.bill.SuperPushEnterAccountService;
import cn.eeepay.framework.service.bill.SuperPushShareDaySettleService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.TransShortInfoService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.service.nposp.SuperPushShareService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

/**
 * 超级推
 * zouruijin 
 * rjzou@qq.com zrj@eeepay.cn
 */
@Controller
@RequestMapping(value = "/superPushAction")
public class SuperPushAction {

	private static final Logger log = LoggerFactory.getLogger(SuperPushAction.class);
	@Resource
	public SysDictService sysDictService;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public AgentPreFreezeService agentPreFreezeService;
	@Resource
	public AgentPreAdjustService agentPreAdjustService;
	@Resource
	public SuperPushShareDaySettleService superPushShareDaySettleService;
	@Resource
	public TransShortInfoService transShortInfoService;
	@Resource
	public AgentsProfitAssemblyOrParsing agentsProfitAssemblyOrParsing;
	@Resource
	public AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	public AgentUnfreezeService agentUnfreezeService;
	@Resource
	public SuperPushShareService superPushShareService;
	@Resource
	public SuperPushEnterAccountService superPushEnterAccountService;
	@Resource
	public MerchantInfoService merchantInfoService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;

	@RequestMapping(value = "queryAgentName.do")
	@ResponseBody
	public List<Map<String, String>> queryAgentName(String q) throws Exception {
		q = URLDecoder.decode(q, "UTF-8");
		log.info("queryAgentName:" + q);
		AgentInfo agentInfo = new AgentInfo();
		agentInfo.setAgentNo(q);
		agentInfo.setAgentName(q);
		List<AgentInfo> agentInfoList = null;
		List<Map<String, String>> maps = new ArrayList<>();
		int limit = 50;
		try {
			agentInfoList = agentInfoService.findSelectAgentInfo(agentInfo,limit);
		} catch (Exception e) {
			log.error("异常:", e);
		}
		Map<String, String> thenMap = null;
		for (AgentInfo s : agentInfoList) {
			thenMap = new HashMap<String, String>();
			thenMap.put("id", s.getAgentNo());
			thenMap.put("text", s.getAgentName());
			maps.add(thenMap);
		}
		return maps;
	}

	/**
	 * 超级推分润日结报表
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:query')")
	@RequestMapping(value = "/toSuperPushShareDaySettle.do")
	public String toSuperPushShareDaySettle(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		log.info("进入超级推分润日结报表");
		// 入账状态
		List<SysDict> enterAccountStatusList = null;
		List<SysDict> superDaySettleTypeList = null;
		List<AgentInfo> agentInfoList = null;
		SysDict sysDict = null;
		
		String sysKey = "sys_agents_profit";
		String sysName = "enter_scale";
		
		try {
			sysDict = sysDictService.findSysDictByKeyName(sysKey, sysName);
			enterAccountStatusList = sysDictService.findSysDictGroup("enter_account_status");
			superDaySettleTypeList = sysDictService.findSysDictGroup("super_push_day_settle_type");
			agentInfoList = agentInfoService.findAllOneAgentInfoList();
		} catch (Exception e) {
			log.error("进入超级推分润日结报表异常:", e);
		}
		params.put("enterScale", sysDict.getSysValue());
		model.put("enterAccountStatusList", enterAccountStatusList);
		model.put("superDaySettleTypeList", superDaySettleTypeList);
		model.put("agentInfoList", agentInfoList);
		model.put("params", params);
		return "superPush/superPushShareDaySettle";
	}

	/**
	 * 查询朝记忆分润日结
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:query')")
	@RequestMapping(value = "/findSuperPushDaySettleList.do")
	@ResponseBody
	public Page<SuperPushShareDaySettle> findSuperPushDaySettleList(
			@ModelAttribute("superPushShareDaySettle") SuperPushShareDaySettle superPushShareDaySettle, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<SuperPushShareDaySettle> page) {
		log.info("进入查询操击推分润日结报表grid--->");
		try {
			superPushShareDaySettleService.findSuperPushShareDaySettleList(superPushShareDaySettle, sort, page);
		} catch (Exception e) {
			log.error("进入查询操击推分润日结报表grid异常:",e);
		}	
		return page;
	}
	
	
	
	/**
	 * 超级推收益明细报表 汇总内容
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareList:collectionData')")
	@RequestMapping(value = "/findSuperPushShareListCollection.do")
	@ResponseBody
	public Map<String, Object> findSuperPushShareListCollection(
			ModelMap model,@ModelAttribute("superPushShareDaySettle") SuperPushShare superPushShare, @RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String,Object> map = superPushShareService.findSuperPushShareCollection(superPushShare);
			
			Map<String,Object> map2 = superPushShareService.findSuperPushShareCollectionTotalAmount(superPushShare);
			Object allShareTotalAmount = "0";
			Object allNoEnterShareTotalAmount =  "0";
			Object allAccountedShareTotalAmount =  "0";
			Object allTransTotalAmount =  "0";
			Object allTransTotalNum = "0";
			if (map != null) {
				allShareTotalAmount = map.get("allShareTotalAmount");
				allNoEnterShareTotalAmount = map.get("allNoEnterShareTotalAmount");
				allAccountedShareTotalAmount = map.get("allAccountedShareTotalAmount");
				//allTransTotalAmount = map.get("allTransTotalAmount");
				allTransTotalNum = map.get("allTransTotalNum");
			}
			if (map2 != null){
				allTransTotalAmount = map2.get("allTransTotalAmount");
			}
			msg.put("allShareTotalAmount", allShareTotalAmount);//累计分润总金额
			msg.put("allNoEnterShareTotalAmount", allNoEnterShareTotalAmount);//已入账
			msg.put("allAccountedShareTotalAmount", allAccountedShareTotalAmount);//未入账
			msg.put("allTransTotalAmount", allTransTotalAmount);//累计交易总金额
			msg.put("allTransTotalNum", allTransTotalNum);//交易笔数
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		log.info("超级推收益明细报表 汇总内容 ---> " + msg.toString());
		return msg;
	}
	
	
	/**
	 * 超级推收益日结报表 汇总内容
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:collectionData')")
	@RequestMapping(value = "/findSuperPushShareDaySettleListCollection.do")
	@ResponseBody
	public Map<String, Object> findSuperPushShareDaySettleListCollection(
			ModelMap model,@ModelAttribute("superPushShareDaySettle") SuperPushShareDaySettle superPushShareDaySettle, @RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String,Object> map = superPushShareDaySettleService.findSuperPushShareDaySettleCollection(superPushShareDaySettle);
			Object allShareTotalAmount = "0";
			Object allNoEnterShareTotalAmount =  "0";
			Object allAccountedShareTotalAmount =  "0";
			if (map != null) {
				allShareTotalAmount = map.get("allShareTotalAmount");
				allNoEnterShareTotalAmount = map.get("allNoEnterShareTotalAmount");
				allAccountedShareTotalAmount = map.get("allAccountedShareTotalAmount");
			}
			msg.put("allShareTotalAmount", allShareTotalAmount);//累计分润总金额
			msg.put("allNoEnterShareTotalAmount", allNoEnterShareTotalAmount);//已入账
			msg.put("allAccountedShareTotalAmount", allAccountedShareTotalAmount);//未入账
			
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		log.info("超级推收益日结报表 汇总内容 ---> " + msg.toString());
		return msg;
	}
	
	/**
	 * 超级推收益明细
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('superPushShareList:query')")
	@RequestMapping(value = "/toSuperPushShareList.do")
	public String toSuperPushShareList(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		log.info("进入超级推收益明细页面---->");
		
		List<SysDict> superPushShareStatusList = null;
		String agentNo = params.get("agentNo");
		AgentInfo agentInfo = null;
		if (agentNo != null) {
			agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			params.put("agentName", agentInfo.getAgentName());
		}
		try {
			superPushShareStatusList = sysDictService.findSysDictGroup("super_push_share_status");
		} catch (Exception e) {
			log.info("进入超级推收益明细页面异常----> " + e.toString());
		}
		params.put("agentNo", agentNo);
		model.put("superPushShareStatusList", superPushShareStatusList);
		model.put("params", params);
		return "superPush/superPushShareList";
	}

	/**
	 * 查询超级推分润明细
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareList:query')")
	@RequestMapping(value = "/findSuperPushShareList.do")
	@ResponseBody
	public Page<SuperPushShare> findSuperPushShareList(
			@ModelAttribute("superPushShare") SuperPushShare superPushShare,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<SuperPushShare> page) {
		log.info("进入查询超级推分润明细grid--->");
		try {
			superPushShareService.findSuperPushShareList(superPushShare, sort, page);
		} catch (Exception e) {
			log.error("查询超级推分润明细grid异常:", e);
		}
		return page;
	}

	/**
	 * 导出数据(超级推收益明细)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('superPushShareList:export')")
	@RequestMapping(value = "exportSuperPushShareList.do", method = RequestMethod.POST)
	public void exportSuperPushShareList(@RequestParam Map<String, String> params,
			@ModelAttribute SuperPushShare superPushShare, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		//用于对数据字典的数据进行格式化显示
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "超级推收益明细导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		try {
			Map<String, String> tempMap = null;
			// 查询代理商分润交易明细
			List<SuperPushShare> list = superPushShareService.exportSuperPushShareList(superPushShare);
			for (SuperPushShare sp : list) {
				sp.setShareRateStr(sp.getShareRate()==null?"":String.valueOf(sp.getShareRate().setScale(2, BigDecimal.ROUND_HALF_UP))+"%");
				if (sp.getShareType().equals("0") || sp.getShareType().equals("1")) {
					//代理商
					String agentNo = sp.getShareNo();
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
					if (agentInfo != null) {
						sp.setShareName(agentInfo.getAgentName());
					}
				}else {
					//商户
					String merchantNo = sp.getShareNo();
					MerchantInfo merchantInfo= merchantInfoService.findMerchantInfoByUserId(merchantNo);
					if (merchantInfo != null) {
						sp.setShareName(merchantInfo.getMerchantName());
					}
				}
				//对账状态
//				DuiAccountDetail detail = duiAccountDetailService.findDuiAccountDetailByOrderReferenceNo(sp.getOrderNo());
//				if(detail != null ){
//					sp.setCheckAccountStatus(detail.getCheckAccountStatus());
//				}
			}
			
			
			if (list != null && list.size() > 0) {
				for (SuperPushShare info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("createTime", DateUtil.getLongFormatDate(info.getCreateTime()));
					tempMap.put("shareAmount", String.valueOf(info.getShareAmount()));
					
					//商户类别
					String shareType = "";
					if(StringUtils.isNotBlank(info.getShareType())){
						if("0".equals(info.getShareType())){
							shareType = "一级代理商分润";
						}else if("1".equals(info.getShareType())){
							shareType = "直属代理商分润";
						}else if("2".equals(info.getShareType())){
							shareType = "上一级商户";
						}else if("3".equals(info.getShareType())){
							shareType = "上二级商户";
						}else if("4".equals(info.getShareType())){
							shareType = "上三级商户";
						}
					}
					tempMap.put("shareType", shareType);
					
					tempMap.put("shareRate", info.getShareRateStr()==null?"":String.valueOf(info.getShareRateStr()));
					tempMap.put("shareName", info.getShareName());
					tempMap.put("shareNo", info.getShareNo());
					tempMap.put("transAmount", String.valueOf(info.getTransAmount()));
					tempMap.put("orderNo", info.getOrderNo());
					tempMap.put("merchantNo", info.getMerchantNo());
					
					//对账状态
//					String checkAccountStatus = "";
//					
//					if(StringUtils.isNotBlank(info.getCheckAccountStatus())){
//						if("SUCCESS".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "核对成功";
//						}else if("FAILED".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "核对有误";
//						}else if("ACQ_SINGLE".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "上游单边";
//						}else if("PLATE_SINGLE".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "平台单边";
//						}else if("AMOUNT_FAILED".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "金额不符";
//						}else if("NO_CHECKED".equals(info.getCheckAccountStatus())){
//							checkAccountStatus = "未核对";
//						}
//					}
//					tempMap.put("checkAccountStatus", checkAccountStatus);
					
					//汇总状态
					String collecttionStatus = "";
					if(StringUtils.isNotBlank(info.getCollectionStatus())){
						if("NOCOLLECTION".equals(info.getCollectionStatus())){
							collecttionStatus = "未汇总";
						}else{
							collecttionStatus = "已汇总";
						}
					}
					tempMap.put("collectionStatus", collecttionStatus);

					tempMap.put("collectionBatchNo", info.getCollectionBatchNo());
					String shareStatus = info.getShareStatus();
					SysDict shareStatusSysDict = sysDictService.findSysDictByKeyValue("super_push_share_status", shareStatus);
					tempMap.put("shareStatus", shareStatusSysDict.getSysName());
					String shareTime = null;
					if (info.getShareTime() != null) {
						shareTime = DateUtil.getLongFormatDate(info.getShareTime());
					}
					tempMap.put("shareTime", shareTime);
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("导出数据(超级推收益明细)异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {  
				"createTime", "shareAmount", 
				"shareType", "shareRate", 
				"shareName", "shareNo",
				"transAmount", "orderNo",
				"merchantNo", 
				"collectionStatus", "collectionBatchNo",
				"shareStatus", "shareTime"};
        //"checkAccountStatus","对账状态",
		String[] colsName = new String[] {
				"分润创建时间","分润金额",
				"分润级别","分润百分比",
				"商户/代理商名称","商户/代理商编号",
				"交易金额","交易订单号",
				"交易商户编号",
				"汇总状态","汇总批次号",
				"入账状态","入账时间"};
		export.export(cols, colsName, data, response.getOutputStream());

	}
	
	
	/**
	 * 导出数据(超级推日结)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:export')")
	@RequestMapping(value = "exportSuperPushShareDaySettleList.do", method = RequestMethod.POST)
	public void exportSuperPushShareDaySettleList(@RequestParam Map<String, String> params,
			@ModelAttribute SuperPushShareDaySettle superPushShareDaySettle, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		//用于对数据字典的数据进行格式化显示
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "超级推分润日结报表导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		try {
			Map<String, String> tempMap = null;
			// 查询代理商分润交易明细
			List<SuperPushShareDaySettle> list = superPushShareDaySettleService.exportSuperPushShareDaySettleList(superPushShareDaySettle);
			for (SuperPushShareDaySettle sp : list) {
				String shareType = sp.getShareType();
				if (shareType.equals("0") ) {
					//代理商
					String agentNo = sp.getShareNo();
					AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
					if(agentInfo != null){
						sp.setShareName(agentInfo.getAgentName());	
					}
					
				}
				if (shareType.equals("1")) {
					//商户
					String merchantNo = sp.getShareNo();
					MerchantInfo merchantInfo = merchantInfoService.findMerchantInfoByUserId(merchantNo);
					if(merchantInfo!= null){
						sp.setShareName(merchantInfo.getMerchantName());
					}
				}
			}
			
			if (list != null && list.size() > 0) {
				for (SuperPushShareDaySettle info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("id", info.getId().toString());
					tempMap.put("collectionBatchNo", info.getCollectionBatchNo());
					tempMap.put("groupTime", info.getGroupTime()==null?"":DateUtil.getDefaultFormatDate(info.getGroupTime()));
					tempMap.put("createTime", info.getCreateTime()==null?"":DateUtil.getLongFormatDate(info.getCreateTime()));
					//商户类别
					String shareType = "";
					if(StringUtils.isNotBlank(info.getShareType())){
						if("0".equals(info.getShareType())){
							shareType = "代理商";
						}else if("1".equals(info.getShareType())){
							shareType = "商户";
//						}else if("2".equals(info.getShareType())){
//							shareType = "上一级商户";
//						}else if("3".equals(info.getShareType())){
//							shareType = "上二级商户";
//						}else if("4".equals(info.getShareType())){
//							shareType = "上三级商户";
						}
					}
					tempMap.put("shareType", shareType);
					tempMap.put("shareName", info.getShareName());
					tempMap.put("shareNo", info.getShareNo());
					tempMap.put("shareTotalAmount", info.getShareTotalAmount()==null?"":String.valueOf(info.getShareTotalAmount()));
					tempMap.put("shareTotalNum", info.getShareTotalNum() !=null?info.getShareTotalNum().toString():"0");
					String enterAccountStatus = "";
					if(StringUtils.isNotBlank(info.getEnterAccountStatus())){
						if("ENTERACCOUNTED".equals(info.getEnterAccountStatus())){
							enterAccountStatus = "入账";
						}else if("NOENTERACCOUNT".equals(info.getEnterAccountStatus())){
							enterAccountStatus = "未入账";
						}
					}
					tempMap.put("enterAccountStatus", enterAccountStatus);
					String enterAccountTime = null;
					if (info.getEnterAccountTime() != null) {
						enterAccountTime = DateUtil.getLongFormatDate(info.getEnterAccountTime());
					}
					tempMap.put("enterAccountTime", enterAccountTime);
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("导出数据(超级推日结)异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {  
				"id", "collectionBatchNo", 
				"groupTime", "createTime", 
				"shareType", "shareName",
				"shareNo", "shareTotalAmount",
				"shareTotalNum", "enterAccountStatus",
				"enterAccountTime"};

		String[] colsName = new String[] {
				"序号","汇总批次号",
				"汇总时间","分润创建日期",
				"用户类别","商户/代理商名称",
				"商户/代理商编号","分润总金额",
				"分润笔数","入账状态",
				"入账时间"};
		export.export(cols, colsName, data, response.getOutputStream());

	}
	
	
	
	/**
	 * 汇总
	 * @param createDate
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:collection')")
	@RequestMapping(value = "superPushShareCollection.do")
	@Logs(description="超级推收益汇总")
	@ResponseBody
	public Map<String,Object> superPushShareCollection(String createDate) throws Exception{
		log.info("进入超级推汇总------> ");
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(createDate)) {
			msg.put("msg","创建日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			// 获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			try {
				String username = userInfo.getUsername();
				Map<String,Object> result = superPushShareDaySettleService.superPushShareCollection(createDate,username);
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg",resultMsg);
					msg.put("status",true);
					log.info(msg.toString());
				}
				else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.toString());
				log.error("超级推汇总异常:",e);
			}	
		}
		
		return msg;
	}
	
	/**
	 * 批量入账
	 * @param selectEnterId
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:enterAccount')")
	@RequestMapping(value = "superPushShareEnterAccount.do")
	@Logs(description="超级推分润入账")
	@ResponseBody
	public Map<String,Object> superPushShareEnterAccount(String selectEnterId) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(selectEnterId)) {
			msg.put("msg","你还没有选择任何内容");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
					result = superPushShareDaySettleService.superPushBatchEnterAccount(selectEnterId);
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
					msg.put("status",true);
					log.info(msg.toString());
					return msg;
				}
				else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	
	/**
	 * 今日汇总全部预判断 金额 数量
	 * @param model
	 * @param superPushShare
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:todayEnterAccount')")
	@RequestMapping(value = "/judgeSuperPushShareEnterTodayAccount.do")
	@ResponseBody
	public Map<String, Object> judgeSuperPushShareEnterTodayAccount(
			ModelMap model,@ModelAttribute("superPushShareDaySettle") SuperPushShare superPushShare, @RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String,Object> map = superPushShareDaySettleService.judgeSuperPushShareEnterTodayAccount(DateUtil.getCurrentDate());
			Object allEnterAmount = "0";
			Object allEnterNum =  "0";
			if (map != null) {
				allEnterAmount = map.get("allEnterAmount");
				allEnterNum = map.get("allEnterNum");
			}
			msg.put("allEnterAmount", allEnterAmount);//金额
			msg.put("allEnterNum", allEnterNum);//笔数
			
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return msg;
	}
	
	/**
	 * 今日全部入账
	 * @param selectEnterId
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:todayEnterAccount')")
	@RequestMapping(value = "superPushShareEnterTodayAccount.do")
	@Logs(description="超级推分润入账")
	@ResponseBody
	public Map<String,Object> superPushShareEnterTodayAccount() throws Exception{
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
					result = superPushShareDaySettleService.superPushEnterTodayAccount(DateUtil.getCurrentDate());
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
					msg.put("status",true);
					log.info(msg.toString());
					return msg;
				}
				else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	/**
	 * 单个入账
	 * @param superPushShareDaySettle
	 * @return
	 */
	@PreAuthorize("hasAuthority('superPushShareDaySettle:singleEnterAccount')")
	@RequestMapping(value = "superPushSingleEnterAccount.do")
	@Logs(description="单个代理商分润入账")
	@ResponseBody
	public Map<String,Object> agentProfitSingleEnterAccount(@ModelAttribute("agentShareDaySettle") SuperPushShareDaySettle superPushShareDaySettle) {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if(superPushShareDaySettle != null){
			if (superPushShareDaySettle.getId()==null) {
				msg.put("msg","入账错误");
				msg.put("status",false);
				isReturn = true;
			}
		}else{
			msg.put("msg","入账错误");
			msg.put("status",false);
			isReturn = true;
		}
		SuperPushShareDaySettle shareDaySettleDB = superPushShareDaySettleService.findSuperPushShareDaySettleById(superPushShareDaySettle.getId());
		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
					result = superPushEnterAccountService.superPushEnterAccount(shareDaySettleDB);
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
					msg.put("status",true);
					log.info(msg.toString());
					return msg;
				}
				else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
}
