package cn.eeepay.boss.action;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.boss.util.Constants;
import cn.eeepay.boss.util.DownloadUtil;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.exception.FreezeException;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.SettleOrderInfo;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.AgentProfitTransferService;
import cn.eeepay.framework.service.nposp.SysDictNpospService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.NewListDataExcelExport;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代理商分润管理 2017年04月10日16:49:01
 * zouruijin 
 * rjzou@qq.com zrj@eeepay.cn
 */
@Controller
@RequestMapping(value = "/agentsProfitAction")
public class AgentsProfitAction {

	private static final Logger log = LoggerFactory.getLogger(AgentsProfitAction.class);
	@Resource
	public SysDictService sysDictService;
	@Resource
	public AgentInfoService agentInfoService;
	@Resource
	public AgentPreFreezeService agentPreFreezeService;
	@Resource
	public AgentPreAdjustService agentPreAdjustService;
	@Resource
	public AgentShareDaySettleService agentShareDaySettleService;
	@Resource
	public TransShortInfoService transShortInfoService;
	@Resource
	public AgentsProfitAssemblyOrParsing agentsProfitAssemblyOrParsing;
	@Resource
	public AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	public AgentUnfreezeService agentUnfreezeService;

	@Resource
	public SysDictNpospService sysDictNpospService;

	@Resource
	public AgentProfitTransferService agentProfitTransferService;



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
	 * 代理商分润日结
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:query')")
	@RequestMapping(value = "/toAgentsProfitDaySettle.do")
	public String toAgentsProfitDaySettle(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商分润日结");
		// 入账状态
		List<SysDict> enterAccountStatusList = null;
		List<AgentInfo> agentInfoList = null;
		SysDict sysDict = null;
		
		String sysKey = "sys_agents_profit";
		String sysName = "enter_scale";
		
		try {
			sysDict = sysDictService.findSysDictByKeyName(sysKey, sysName);
			enterAccountStatusList = sysDictService.findSysDictGroup("enter_account_status");
			agentInfoList = agentInfoService.findAllOneAgentInfoList();
			//代理商批量入账开关状态
			cn.eeepay.framework.model.nposp.SysDict shareAcc = sysDictNpospService.findAccountantShareAccounting();
			params.put("accountantShareAccounting", shareAcc.getSysValue());
		} catch (Exception e) {
			log.error("异常:", e);
		}
		params.put("enterScale", sysDict.getSysValue());
		model.put("enterAccountStatusList", enterAccountStatusList);
		model.put("agentInfoList", agentInfoList);
		model.put("params", params);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currenDate = sdf.format(new Date());

		String date1 = DateUtil.subDayFormatLong(new Date(),7);
		model.put("date1",date1);
		model.put("date2",currenDate);
		return "agentsProfit/agentsProfitDaySettle";
	}

	/**
	 * 查询代理商分润日结
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:query')")
	@RequestMapping(value = "/findAgentsProfitDaySettleList.do")
	@ResponseBody
	public Page<AgentShareDaySettle> findAgentsProfitDayList(
			@ModelAttribute("agentShareDaySettle") AgentShareDaySettle agentShareDaySettle, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<AgentShareDaySettle> page) {
		try {
			agentShareDaySettleService.findAgentShareDaySettleList(agentShareDaySettle, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}
	
	/**
	 * 代理商分润日结 汇总内容
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:collectionData')")
	@RequestMapping(value = "/findAgentsProfitDaySettleListCollection.do")
	@ResponseBody
	public Map<String, Object> findAgentsProfitDaySettleListCollection(
			ModelMap model,@ModelAttribute("agentShareDaySettle") AgentShareDaySettle agentShareDaySettle, @RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String,Object> map = agentShareDaySettleService.findAgentShareDaySettleListCollection(agentShareDaySettle);
			Object allTransTotalAmount = "0";
			Object allCashTotalNum =  "0";
			Object allAdjustTotalShareAmount =  "0";
			Object allPreTransShareAmount =  "0";
			Object allPreTransCashAmount =  "0";
			Object allAdjustAmount =  "0";
			if (map != null) {
				allTransTotalAmount = map.get("allTransTotalAmount");
				allCashTotalNum = map.get("allCashTotalNum");
				allAdjustTotalShareAmount = map.get("allAdjustTotalShareAmount");
				allPreTransShareAmount = map.get("allPreTransShareAmount");
				allPreTransCashAmount = map.get("allPreTransCashAmount");
				allAdjustAmount = map.get("allAdjustAmount");
			}
			msg.put("allTransTotalAmount", allTransTotalAmount);//交易总金额
			msg.put("allCashTotalNum", allCashTotalNum);//提现总笔数
			msg.put("allAdjustTotalShareAmount", allAdjustTotalShareAmount);//调整后总分润
			msg.put("allPreTransShareAmount", allPreTransShareAmount);//原交易分润
			msg.put("allPreTransCashAmount", allPreTransCashAmount);//原提现分润
			msg.put("allAdjustAmount", allAdjustAmount);//调账金额

		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return msg;
	}
	
	
	/**
	 * 修改代理商分润入账比例
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:updateEnterScale')")
	@RequestMapping(value = "/updateAgentsProfitEnterScale.do")
	@ResponseBody
	public Map<String, Object> updateAgentsProfitEnterScale(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("enterScale") String enterScale) {
		Map<String, Object> msg = new HashMap<String, Object>();
		boolean isReturn = false;
		if (StringUtils.isBlank(enterScale)) {
			msg.put("msg","分润入账比例不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if (!StringUtils.isNumeric(enterScale)) {
			msg.put("msg","分润入账比例只能是数字");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try {
				String sysKey = "sys_agents_profit";
				String sysName = "enter_scale";
				SysDict oldSysDict = sysDictService.findSysDictByKeyName(sysKey, sysName);
				SysDict newSysDict = (SysDict)BeanUtils.cloneBean(oldSysDict);
				newSysDict.setSysValue(enterScale);
				int i = sysDictService.updateSysDict(oldSysDict, newSysDict);
				if (i > 0) {
					msg.put("status", true);
					msg.put("msg", "修改成功");
					log.info(msg.toString());
				}
				else{
					msg.put("status", true);
					msg.put("msg", "没有操作任何数据");
					log.info(msg.toString());
				}
				
			} catch (Exception e) {
				log.error("异常:",e);
			}	
		}
		return msg;
	}
	
	/**
	 * 保存代理商冻结
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsAccountQuery:unfreeze')")
	@RequestMapping(value = "/saveAgentsProfitUnfreeze.do")
	@ResponseBody
	public Map<String, Object> saveAgentsProfitUnfreeze(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("agentUnfreeze") AgentUnfreeze agentUnfreeze) {
		Map<String, Object> msg = new HashMap<String, Object>();
		boolean isReturn = false;
		if (agentUnfreeze.getAmount() == null) {
			msg.put("msg","解冻金额不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try {
				String agentNo = params.get("unfreezeAgentNo");
				String agentName = params.get("unfreezeAgentName");
				
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
					    .getAuthentication()
					    .getPrincipal();
				agentUnfreeze.setAgentNo(agentNo);
				agentUnfreeze.setAgentName(agentName);
				agentUnfreeze.setOperater(userInfo.getUsername());
				agentUnfreeze.setUnfreezeTime(new Date());
				
				Map<String,Object> result = agentUnfreezeService.saveAgentsProfitUnfreeze(agentUnfreeze);
				Boolean fenrun = (Boolean) result.get("fenrun");
				if(fenrun != null){		//操作了分润账户
					log.info("解冻操作了分润账户冻结金额，开始判断是否清零分润日结冻结金额!");
					agentShareDaySettleService.clearUnfreezeAmount(agentNo);
					result.remove("fenrun");
				}
				boolean resultStatus = (boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("status", true);
					msg.put("msg", "保存成功");
					log.info(msg.toString());
				}
				else{
					msg.put("status", true);
					msg.put("msg", resultMsg);
					log.info(msg.toString());
				}
				
			} catch (Exception e) {
				msg.put("status", false);
				msg.put("msg", e.getMessage());
				log.error(msg.toString());
				log.error("异常:",e);
			}	
		}
		return msg;
	}

	/**
	 * 代理商分润明细
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitDetail:query')")
	@RequestMapping(value = "/toAgentsProfitDetail.do")
	public String toAgentsProfitDetail(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商分润明细");
//		List<AgentInfo> agentInfoList = null;
//		try {
//			agentInfoList = agentInfoService.findAllOneAgentInfoList();
//		} catch (Exception e) {
//			log.error("异常:", e);
//		}
//		model.put("agentInfoList", agentInfoList);
		String agentNo = params.get("agentNo");
		AgentInfo agentInfo = null;
		if (agentNo != null) {
			agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			params.put("agentName", agentInfo.getAgentName());
		}
//		if(!params.containsKey("agentNo")){
//			params.put("agentNo", "\"ALL\"");
//		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currenDate = sdf.format(new Date());

		String date1 = DateUtil.subDayFormatLong(new Date(),7);
		model.put("date1",date1);
		model.put("date2",currenDate);

		params.put("agentNo", agentNo);
		model.put("params", params);
		return "agentsProfit/agentsProfitDetail";
	}

	/**
	 * 查询代理商分润明细
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitDetail:query')")
	@RequestMapping(value = "/findAgentsProfitDetailList.do")
	@ResponseBody
	public Page<TransShortInfo> findAgentsProfitDetailList(
			@ModelAttribute("TransShortInfo") TransShortInfo transShortInfo,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<TransShortInfo> page) {
		try {
			List<TransShortInfo> list = transShortInfoService.findTransShortInfoList(transShortInfo, sort, page);
			page.setResult(list);
		} catch (Exception e) {
			log.error("异常:", e);
		}
		return page;
	}

	/**
	 * 代理商分润预调账记录查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:query')")
	@RequestMapping(value = "/agentsProfitPreAdjustmentQuery.do")
	public String agentsProfitPreAdjustmentQuery(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商分润调账记录查询");
		// 预调账原因下拉
		List<AgentInfo> agentInfoList = null;
		List<SysDict> ajustmentReasonList = null;
		try {
			ajustmentReasonList = sysDictService.findSysDictGroup("ajustment_reason");
			agentInfoList = agentInfoService.findAllOneAgentInfoList();
		} catch (Exception e) {
			log.error("异常:", e);
		}
		model.put("agentInfoList", agentInfoList) ;
		model.put("ajustmentReasonList", ajustmentReasonList);
		return "agentsProfit/agentsProfitPreAdjustmentQuery";
	}

	/**
	 * 代理商分润预调账记录查询
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:query')")
	@RequestMapping(value = "/findAgentsProfitPreAdjustList.do")
	@ResponseBody
	public Page<AgentPreAdjust> findAgentsProfitPreAdjustList(
			@ModelAttribute("AgentPreAdjust") AgentPreAdjust agentPreAdjust, @RequestParam Map<String, Object> param,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<AgentPreAdjust> page) {
		try {
			List<AgentPreAdjust> list = agentPreAdjustService.findAgentsProfitPreAdjustList(agentPreAdjust, param, sort,
					page);
			page.setResult(list);
		} catch (Exception e) {
			log.error("异常:", e);
		}
		return page;
	}

	/**
	 * 代理商分润预调账
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:preAdjustment')")
	@RequestMapping(value = "/agentsProfitPreAdjustment.do")
	public String agentsProfitPreAdjustment(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商分润预调账");
		// 预调账原因
		List<SysDict> ajustmentReasonList = null;
		try {
			ajustmentReasonList = sysDictService.findSysDictGroup("ajustment_reason");
		} catch (Exception e) {
			log.error("异常:", e);
		}
		model.put("ajustmentReasonList", ajustmentReasonList);
		return "agentsProfit/agentsProfitPreAdjustment";
	}

	/**
	 * 代理商分润预调账保存
	 * 
	 * @param agentPreAdjust
	 * @return
	 */
	@RequestMapping(value = "/saveAdjustInfo.do")
	@Logs(description = "代理商分润预调账保存")
	@ResponseBody
	public Map<String, Object> saveAdjustInfo(@ModelAttribute AgentPreAdjust agentPreAdjust) {
		Map<String, Object> msg = new HashMap<>();
		int i = 0;
		try {
			// 获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			agentPreAdjust.setApplicant(userInfo.getUsername());// 创建者
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreAdjust.getAgentNo());
			agentPreAdjust.setAgentName(agentInfo.getAgentName());
			i = agentPreAdjustService.insertAgentPreAdjustAndUpdateAccount(agentPreAdjust,agentInfo);
			if (i > 0) {
				msg.put("status", true);
				msg.put("msg", "保存成功");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "保存失败:"+e.getMessage());
			log.error("提交失败！", e);
			log.error(msg.toString());
		}

		return msg;
	}

	/**
	 * 代理商分润批量预调账
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:batchPreAdjustment')")
	@RequestMapping(value = "/agentsProfitBatchPreAdjustment.do")
	public String agentsProfitBatchPreAdjustment(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商分润批量预调账");
		return "agentsProfit/agentsProfitBatchPreAdjustment";
	}

	// 上传模板,批量预调账
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:upload')")
	@ResponseBody
	@RequestMapping(value = "/batchPreAdjustFileUpload.do", method = RequestMethod.POST)
	public Map<String, Object> batchPreAdjustFileUpload(HttpServletRequest request,@RequestParam final Map<String, String> params) {
		
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> result=new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		MultipartFile file=files.get(0);
		String fileName  = file.getOriginalFilename();
		if(!fileName.endsWith(".xls")){
			result.put("statu",false);
			result.put("msg","请导入正确格式批量预调账excel文件!");
			return result;
		}
		//保存单据
		try {
			File temp = File.createTempFile(file.getName(), ".xls");
			file.transferTo(temp);
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uname=userInfo.getUsername();
			map = resolvebatchPreAdjustFile(temp,uname);
			result = leadingInBatchPreAdjustDetails(map);
			result.put("statu",true);
			result.put("msg","操作成功！请查看结果列表！");
		} catch (Exception e) {
			log.error("批量预调账导入异常 :" + e);
			result.put("status", false);
			result.put("msg", "导入失败,请检查excel格式");
//			e.printStackTrace();
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> leadingInBatchPreAdjustDetails(Map<String, Object> map) throws Exception {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> resultMapData = new HashMap<String, Object>();
		List<AgentPreAdjust> agentPreAdjustsExcel = new ArrayList<AgentPreAdjust>();
		agentPreAdjustsExcel = (List<AgentPreAdjust>) map.get("agentPreAdjusts");
		for (AgentPreAdjust agentPreAdjust : agentPreAdjustsExcel) {
			//查询代理商编号是否存在
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreAdjust.getAgentNo());
			String agentNo = agentPreAdjust.getAgentNo();
			if(agentInfo == null ){

				throw new RuntimeException("代理商"+agentNo+"不存在,请检查后重新提交");
			}else{
				agentPreAdjust.setAgentName(agentInfo.getAgentName());
				try {
					agentPreAdjustService.insertAgentPreAdjustAndUpdateAccount(agentPreAdjust,agentInfo);
				} catch (Exception e) {
					throw new RuntimeException("代理商"+agentNo+"导入异常,"+e.getMessage());
				}
				resultMapData.put(agentPreAdjust.getAgentNo(), "导入成功！");
			}
//			if(agentInfo == null ){
//				resultMapData.put( agentPreAdjust.getAgentNo(), "该代理商不存在！");
//			}else{
//				agentPreAdjust.setAgentName(agentInfo.getAgentName());
//				agentPreAdjustService.insertAgentPreAdjustAndUpdateAccount(agentPreAdjust);
//				resultMapData.put(agentPreAdjust.getAgentNo(), "导入成功！");
//			}
		}
		
//		resultMapData = agentPreAdjustService.insertAgentPreAdjustAndUpdateAccountExcel(agentPreAdjustsExcel);
		result.put("resultMapData",resultMapData);
		return result;
	}

	private Map<String, Object> resolvebatchPreAdjustFile(File temp, String uname) throws Exception {
		return agentsProfitAssemblyOrParsing.resolvebatchPreAdjustFile(temp,uname);
	}

	
	
	
	
	// 上传模板,批量预冻结
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:upload')")
	@ResponseBody
	@RequestMapping(value = "/batchPreFreezeFileUpload.do", method = RequestMethod.POST)
	public Map<String, Object> batchPreFreezeFileUpload(HttpServletRequest request,@RequestParam final Map<String, String> params) {
		
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> result=new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		MultipartFile file=files.get(0);
		String fileName  = file.getOriginalFilename();
		if(!fileName.endsWith(".xls")){
			result.put("statu",false);
			result.put("msg","请导入正确格式批量预调账excel文件!");
			return result;
		}
		//保存单据
		try {
			File temp = File.createTempFile(file.getName(), ".xls");
			file.transferTo(temp);
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uname = userInfo.getUsername();
			map = resolvebatchPreFreezeFile(temp,uname);
			result = leadingInBatchPreFreezeDetails(map);
			result.put("status",true);
			result.put("msg","操作成功！请查看结果列表！");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("批量预冻结导入异常 " + e.getMessage());
			result.put("status", false);
			result.put("msg", "批量预冻结导入异常 " + e.getMessage());
		}

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Object> leadingInBatchPreFreezeDetails(Map<String, Object> map) throws Exception {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> resultMapData = new HashMap<String, Object>();
		List<AgentPreFreeze> agentPreFreezeExcel = new ArrayList<AgentPreFreeze>();
		agentPreFreezeExcel = (List<AgentPreFreeze>) map.get("agentPreFreezes");
//		for (AgentPreFreeze agentPreFreeze : agentPreFreezeExcel) {
//			//查询代理商编号是否存在
//			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentPreFreeze.getAgentNo());
//			if(agentInfo == null ){
//				//resultMapData.put( agentPreFreeze.getOneAgentNo(), "该代理商不存在！");
//			}else{
//				agentPreFreeze.setAgentName(agentInfo.getAgentName());
//				agentPreFreezeService.insertAgentPreFreezeAndUpdateAccount(agentPreFreeze);
//				resultMapData.put( agentPreFreeze.getAgentNo(), "导入成功！");
//			}

			for (AgentPreFreeze agentPreFreeze : agentPreFreezeExcel) {
				String freezeReason = "terminal";
				if (agentPreFreeze.getOtherFreezeAmount() != null
						&& agentPreFreeze.getOtherFreezeAmount().compareTo(BigDecimal.ZERO) > 0) {
					freezeReason = "other";
				}
				if (agentPreFreeze.getFreezeTime() == null) {
					agentPreFreeze.setFreezeTime(new Date());
				}
				if (agentPreFreeze.getTerminalFreezeAmount() == null) {
					agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
				}
				if (agentPreFreeze.getOtherFreezeAmount() == null) {
					agentPreFreeze.setOtherFreezeAmount(BigDecimal.ZERO);
				}
				agentPreFreezeService.saveAgentsProfitPreFreeze(agentPreFreeze, freezeReason);
			}

			resultMapData.put("status", true);
			resultMapData.put("msg", "导入成功！");
//		}
		result.put("resultMapData",resultMapData);
		return result;
	}
	
//	@SuppressWarnings("unchecked")
//	private Map<String, Object> leadingInBatchUnfreezeDetails(Map<String, Object> map) throws Exception {
//		Map<String,Object> result = new HashMap<String, Object>();
//		Map<String,Object> resultMapData = new HashMap<String, Object>();
//		List<AgentUnfreeze> agentUnfreezeExcel = new ArrayList<AgentUnfreeze>();
//		agentUnfreezeExcel = (List<AgentUnfreeze>) map.get("agentUnfreezes");
//		for (AgentUnfreeze agentUnfreeze : agentUnfreezeExcel) {
//			log.info("批量解冻代理商编号：" + agentUnfreeze.getAgentNo());
//			result = agentUnfreezeService.saveAgentsProfitUnfreeze(agentUnfreeze);
//			boolean resultStatus = (boolean) result.get("status");
//			String resultMsg = (String) result.get("msg");
//			if (resultStatus) {
//				resultMapData.put("status", true);
//				resultMapData.put("msg", "导入成功");
//				log.info(resultMapData.toString());
//			}
//			else{
//				resultMapData.put("status", true);
//				resultMapData.put("msg", resultMsg);
//				log.info(resultMapData.toString());
//			}
//		}
//		result.put("resultMapData",resultMapData);
//		return result;
//	}

	private Map<String, Object> resolvebatchPreFreezeFile(File temp, String uname) throws Exception{
		return agentsProfitAssemblyOrParsing.resolvebatchPreFreezeFile(temp,uname);
	}
	
	private Map<String, Object> resolvebatchUnfreezeFile(File temp, String uname) throws Exception{
		return agentsProfitAssemblyOrParsing.resolvebatchUnfreezeFile(temp,uname);
	}
	

	/**
	 * 代理商分润预冻结记录查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:query')")
	@RequestMapping(value = "/toAgentsProfitPreFreezeQuery.do")
	public String toAgentsProfitPreFreezeQuery(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商分润预冻结记录查询");
		// 预冻结原因
		List<SysDict> freezeReasonList = null;
		List<AgentInfo> agentInfoList = null;
		try {
			agentInfoList = agentInfoService.findAllOneAgentInfoList();
			freezeReasonList = sysDictService.findSysDictGroup("freeze_reason");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("freezeReasonList", freezeReasonList);
		model.put("agentInfoList", agentInfoList) ;
		return "agentsProfit/agentsProfitPreFreezeQuery";
	}
	
	
	/**
	 * 代理商解冻查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitUnfreezeQuery:query')")
	@RequestMapping(value = "/toAgentsProfitUnfreezeQuery.do")
	public String toAgentsProfitUnFreezeQuery(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商解冻查询");
//		// 预冻结原因
//		List<SysDict> freezeReasonList = null;
//		List<AgentInfo> agentInfoList = null;
//		try {
//			agentInfoList = agentInfoService.findAllOneAgentInfoList();
//			freezeReasonList = sysDictService.findSysDictGroup("freeze_reason");
//		} catch (Exception e) {
//			log.error("异常:",e);
//		}
//		model.put("freezeReasonList", freezeReasonList);
//		model.put("agentInfoList", agentInfoList) ;
		return "agentsProfit/agentsProfitUnfreezeQuery";
	}
	
	/**
	 * 代理商批量解冻
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitBatchUnfreeze:query')")
	@RequestMapping(value = "/toAgentsProfitBatchUnfreeze.do")
	public String toAgentsProfitBatchUnfreeze(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商批量解冻");
		return "agentsProfit/agentsProfitBatchUnfreeze";
	}

	
	// 上传模板,批量解冻
	@PreAuthorize("hasAuthority('agentsProfitBatchUnfreeze:upload')")
	@ResponseBody
	@RequestMapping(value = "/batchUnfreezeFileUpload.do", method = RequestMethod.POST)
	public Map<String, Object> batchUnfreezeFileUpload(HttpServletRequest request,@RequestParam final Map<String, String> params) {
		
		Map<String,Object> map=new HashMap<String, Object>();
		Map<String,Object> result=new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		MultipartFile file=files.get(0);
		String fileName  = file.getOriginalFilename();
		if(!fileName.endsWith(".xls")){
			result.put("status",false);
			result.put("msg","请导入正确格式批量解冻excel文件!");
			return result;
		}
		//保存单据
		try {
			File temp = File.createTempFile(file.getName(), ".xls");
			file.transferTo(temp);
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String uname=userInfo.getUsername();
			map = resolvebatchUnfreezeFile(temp,uname);
			Map<String,Object> resultMapData = new HashMap<String, Object>();
			List<AgentUnfreeze> agentUnfreezeExcel = new ArrayList<AgentUnfreeze>();
			agentUnfreezeExcel = (List<AgentUnfreeze>) map.get("agentUnfreezes");
			for (AgentUnfreeze agentUnfreeze : agentUnfreezeExcel) {
				log.info("批量解冻代理商编号：" + agentUnfreeze.getAgentNo());
				result = agentUnfreezeService.saveAgentsProfitUnfreeze(agentUnfreeze);
				Boolean fenrun = (Boolean) result.get("fenrun");
				if(fenrun != null){		//操作了分润账户
					log.info("解冻操作了分润账户冻结金额，开始判断是否清零分润日结冻结金额!");
					agentShareDaySettleService.clearUnfreezeAmount(agentUnfreeze.getAgentNo());
					result.remove("fenrun");
				}
				boolean resultStatus = (boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					resultMapData.put("status", true);
					resultMapData.put("msg", "导入成功");
					log.info(resultMapData.toString());
				}
				else{
					resultMapData.put("status", true);
					resultMapData.put("msg", resultMsg);
					log.info(resultMapData.toString());
				}
			}
			result.put("resultMapData",resultMapData);

			result.put("status",true);
			result.put("msg","操作成功！请查看结果列表！");
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("批量解冻导入异常 " + e.getMessage());
			result.put("status", false);
			result.put("msg", "批量解冻导入异常 "+e.getMessage());
			log.error(result.toString());
		}
		return result;
	}
	
	/**
	 * 代理商解冻记录查询
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitUnfreezeQuery:query')")
	@RequestMapping(value = "/findAgentsProfitUnfreezeList.do")
	@ResponseBody
	public Page<AgentUnfreeze> findAgentsProfitUnfreezeList(
			@ModelAttribute("agentUnfreeze") AgentUnfreeze agentUnfreeze,@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<AgentUnfreeze> page) {
		try {
			agentUnfreezeService.findAgentUnfreezeList(agentUnfreeze, params, sort, page);
			List<AgentUnfreeze> list = page.getResult();
			if(list!=null){
				for(AgentUnfreeze auf : list){
					//agentPreFreezeService.find
					auf.getAgentNo();
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
			// e.printStackTrace();
		}
		return page;
	}
	
	/**
	 * 代理商分润预冻结
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:preFreeze')")
	@RequestMapping(value = "/toAgentsProfitPreFreeze.do")
	public String toAgentsProfitPreFreeze(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商分润预冻结");
		// 预冻结原因
		List<SysDict> freezeReasonList = null;
		try {
			freezeReasonList = sysDictService.findSysDictGroup("freeze_reason");
		} catch (Exception e) {
			log.error("异常:", e);
		}
		model.put("freezeReasonList", freezeReasonList);
		return "agentsProfit/agentsProfitPreFreeze";
	}

	/**
	 * 保存代理商分润预冻结
	 * 
	 * @param subject
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:preFreeze')")
	@RequestMapping(value = "/saveAgentsProfitPreFreeze.do")
	@Logs(description = "保存代理商分润预冻结")
	@ResponseBody
	public Map<String, Object> saveAgentsProfitPreFreeze(@ModelAttribute AgentPreFreeze agentPreFreeze,
			@RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<>();
		boolean isReturn = false;
		String freezeAmount = params.get("freezeAmount");
		
		if (StringUtils.isBlank(freezeAmount)) {
			msg.put("msg","预冻结金额不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		BigDecimal amount = new BigDecimal(freezeAmount);
		if (amount.compareTo(BigDecimal.ZERO) <=0) {
			msg.put("msg","预冻结金额必须大于0");
			msg.put("status",false);
			isReturn = true;
		}
		if (isReturn) {
			return msg;
		}
		// 获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		agentPreFreeze.setOperater(userInfo.getUsername());// 操作人
		String agentNo = agentPreFreeze.getAgentNo();
		AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
		agentPreFreeze.setAgentName(agentInfo.getAgentName());
		//判断冻结原因
		if ("terminal".equals(agentPreFreeze.getFreezeReason())) {// 机具款
			BigDecimal terminalFreezeAmount = new BigDecimal(freezeAmount);
			agentPreFreeze.setTerminalFreezeAmount(terminalFreezeAmount);
			agentPreFreeze.setOtherFreezeAmount(BigDecimal.ZERO);
		} else if ("other".equals(agentPreFreeze.getFreezeReason())) {// 其他
			BigDecimal otherFreezeAmount = new BigDecimal(freezeAmount);
			agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
			agentPreFreeze.setOtherFreezeAmount(otherFreezeAmount);
		}
		agentPreFreeze.setFreezeTime(new Date());
		try {
			Map<String, Object> result = agentPreFreezeService.saveAgentsProfitPreFreeze(agentPreFreeze,agentPreFreeze.getFreezeReason());
			Boolean resultStatus = (Boolean) result.get("status");
			String resultMsg = (String) result.get("msg");
			if (resultStatus) {
				msg.put("status", true);
				msg.put("msg", "保存成功");
				log.info(msg.toString());
			}
			else {
				msg.put("status",false);
				msg.put("msg",resultMsg);
				log.info(msg.toString());
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "保存失败:"+e.getMessage());
			log.error("保存失败", e);
			log.error(msg.toString());
		}
		return msg;
	}
	
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:collection')")
	@RequestMapping(value = "validateAgentProfitCollection.do")
	@Logs(description="代理商分润汇总验证")
	@ResponseBody
	public Map<String,Object> validateAgentProfitCollection(String transDate) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(transDate)) {
			msg.put("msg","交易日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try {
				Map<String,Object> result = agentShareDaySettleService.validateAgentProfitCollection(transDate);
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","验证成功");
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
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:collection')")
	@RequestMapping(value = "agentProfitCollection.do")
	@Logs(description="代理商分润汇总")
	@ResponseBody
	public Map<String,Object> agentProfitCollection(String transDate) throws Exception {
		
//		Thread.sleep(300000);
		
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(transDate)) {
			msg.put("msg","交易日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			// 获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			try {
				String username = userInfo.getUsername();
				Map<String,Object> result = agentShareDaySettleService.agentProfitCollection(transDate,username);
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
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
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:tryCalculation')")
	@RequestMapping(value = "agentProfitTryCalculation.do")
	@Logs(description="代理商分润试算")
	@ResponseBody
	public Map<String,Object> agentProfitTryCalculation(String transDate) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(transDate)) {
			msg.put("msg","交易日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
					result = agentShareDaySettleService.agentProfitTryCalculation(transDate);
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
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
				log.error("异常:",e);
			}	
		}
		return msg;
	}
	
	
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:enterAccount')")
	@RequestMapping(value = "agentProfitEnterAccount.do")
	@Logs(description="代理商分润入账")
	@ResponseBody
	public Map<String,Object> agentProfitEnterAccount(String transDate) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(transDate)) {
			msg.put("msg","交易日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		// 获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				//关闭提款状态开关
				cn.eeepay.framework.model.nposp.SysDict sysNpospDict = new cn.eeepay.framework.model.nposp.SysDict();
				sysNpospDict.setSysKey("ACCOUNTANT_SHARE_ACCOUNTING");
				sysNpospDict.setSysValue("1");
				sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);
				synchronized(this){
					result = agentShareDaySettleService.agentProfitEnterAccount(transDate);
				}
				//开启提款状态开关
				sysNpospDict.setSysValue("0");
				sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);

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
				cn.eeepay.framework.model.nposp.SysDict shareAcc = sysDictNpospService.findAccountantShareAccounting();
				if("1".equals(shareAcc.getSysValue())){
					cn.eeepay.framework.model.nposp.SysDict sysNpospDict = new cn.eeepay.framework.model.nposp.SysDict();
					sysNpospDict.setSysKey("ACCOUNTANT_SHARE_ACCOUNTING");
					sysNpospDict.setSysValue("0");
					sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);
				}
				msg.put("status",false);
				msg.put("msg",e.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}

	
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:enterAccount')")
	@RequestMapping(value = "agentProfitSingleEnterAccount.do")
	@Logs(description="单个代理商分润入账")
	@ResponseBody
	public Map<String,Object> agentProfitSingleEnterAccount(@ModelAttribute("agentShareDaySettle") AgentShareDaySettle agentShareDaySettle) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if(agentShareDaySettle != null){
			if (agentShareDaySettle.getId()==null) {
				msg.put("msg","入账错误");
				msg.put("status",false);
				isReturn = true;
			}
		}else{
			msg.put("msg","入账错误");
			msg.put("status",false);
			isReturn = true;
		}
		
		if (!isReturn) {
			try {
				Map<String, Object> result = new HashMap<>();
				//关闭提款状态开关
				cn.eeepay.framework.model.nposp.SysDict sysNpospDict = new cn.eeepay.framework.model.nposp.SysDict();
				sysNpospDict.setSysKey("ACCOUNTANT_SHARE_ACCOUNTING");
				sysNpospDict.setSysValue("1");
				sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);
				synchronized(this){
					result = agentShareDaySettleService.agentProfitSingleEnterAccount(agentShareDaySettle);
				}
				//开启提款状态开关
				sysNpospDict.setSysValue("0");
				sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);

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
				cn.eeepay.framework.model.nposp.SysDict shareAcc = sysDictNpospService.findAccountantShareAccounting();
				if("1".equals(shareAcc.getSysValue())){
					cn.eeepay.framework.model.nposp.SysDict sysNpospDict = new cn.eeepay.framework.model.nposp.SysDict();
					sysNpospDict.setSysKey("ACCOUNTANT_SHARE_ACCOUNTING");
					sysNpospDict.setSysValue("0");
					sysDictNpospService.updateSysDictShareAccounting(sysNpospDict);
				}
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	/**
	 * 代理商分润预预冻结记录查询
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:query')")
	@RequestMapping(value = "/findAgentsProfitPreFreezeList.do")
	@ResponseBody
	public Page<AgentPreFreeze> findAgentsProfitPreFreezeList(
			@ModelAttribute("agentPreFreeze") AgentPreFreeze agentPreFreeze,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<AgentPreFreeze> page) {
		try {
			agentPreFreezeService.findAgentPreFreezeList(agentPreFreeze, sort, page);
//			List<AgentPreFreeze> list = page.getResult();
//			if (list != null){
//				for(AgentPreFreeze apf : list){
//					AgentPreRecordTotal agentPreRecordTotal1 = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNoAndSubjectNo(apf.getAgentNo(), "224105");
//					AgentPreRecordTotal agentPreRecordTotal2 = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNoAndSubjectNo(apf.getAgentNo(), "224106");
//					BigDecimal preFreezeAmount1 = new BigDecimal(0);
//					BigDecimal preFreezeAmount2 = new BigDecimal(0);
//					if(agentPreRecordTotal1!=null){
//						preFreezeAmount1 = agentPreRecordTotal1.getPreFreezeAmount()==null? new BigDecimal(0) : agentPreRecordTotal1.getPreFreezeAmount();	//预冻金额
//						//apf.setPreFreezeAmount(agentPreRecordTotal1.getPreFreezeAmount()==null? "0" : agentPreRecordTotal1.getPreFreezeAmount().toString());	//预冻金额
//						apf.setControlAmount(agentPreRecordTotal1.getControlAmount()==null? "0" : agentPreRecordTotal1.getControlAmount().toString());		//分润账户冻结金额
//					}
//					if(agentPreRecordTotal2!=null){
//						preFreezeAmount2 = agentPreRecordTotal2.getPreFreezeAmount()==null? new BigDecimal(0) : agentPreRecordTotal2.getPreFreezeAmount();	//预冻金额
//						apf.setControlAmount(agentPreRecordTotal2.getControlAmount()==null? "0" : agentPreRecordTotal2.getControlAmount().toString());		//活动补贴账户冻结金额
//					}
//					apf.setPreFreezeAmount(preFreezeAmount1.add(preFreezeAmount2).toString());		//这是错误的
//				}
//			}
		} catch (Exception e) {
			log.error("异常:", e);
			// e.printStackTrace();
		}
		return page;
	}

	/**
	 * 代理商分润批量预冻结
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:batchPreFreeze')")
	@RequestMapping(value = "/toAgentsProfitBatchPreFreeze.do")
	public String toAgentsProfitBatchPreFreeze(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		log.info("代理商分润批量预冻结");
		
		return "agentsProfit/agentsProfitBatchPreFreeze";
	}

	/**
	 * 代理商账户查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsAccountQuery:query')")
	@RequestMapping(value = "/agentsAccountQuery.do")
	public String agentsAccountQuery(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商账户查询");
		List<AgentInfo> agentInfoList = null;
		try {
			agentInfoList = agentInfoService.findAllOneAgentInfoList();
		} catch (Exception e) {
			log.error("异常:", e);
		}
		model.put("agentInfoList", agentInfoList) ;
		return "agentsProfit/agentsAccountQuery";
	}

	/**
	 * 代理商账户查询
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsAccountQuery:query')")
	@RequestMapping(value = "/findAgentsProfitAccountList.do")
	@ResponseBody
	public Page<AgentPreRecordTotal> findAgentsProfitAccountList(
			@ModelAttribute("agentPreRecordTotal") AgentPreRecordTotal agentPreRecordTotal, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<AgentPreRecordTotal> page) {
		    List<String> agentInfoList = new ArrayList<String>() ;
		    String userNoStrs = "" ;
		try {

			AgentInfo agent = new AgentInfo();
			agent.setAgentNo(agentPreRecordTotal.getAgentNo());
			agent.setLevel(agentPreRecordTotal.getAgentLevel());
			agentInfoList = agentInfoService.findAgentList(agent) ;
			if(agentInfoList.size()<1){
				return page;
			}
			userNoStrs = StringUtils.join(agentInfoList, ",") ;

			List<AgentPreRecordTotal> list = agentPreRecordTotalService.findAgentPreRecordTotalList(agentPreRecordTotal, userNoStrs, sort, page);
			for (AgentPreRecordTotal agentPreRecordTotal2 : list) {
				AgentInfo agentInfo = agentInfoService.findAgentByUserId(agentPreRecordTotal2.getAgentNo()) ;
				if(agentInfo != null){
					agentPreRecordTotal2.setAgentNo(agentInfo.getAgentNo());
					agentPreRecordTotal2.setAgentName(agentInfo.getAgentName());
					agentPreRecordTotal2.setAgentLevel(agentInfo.getAgentLevel()+"");
				}
				AgentPreRecordTotal agentPreRecordTotal3 = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNoAndSubjectNo(agentPreRecordTotal2.getAgentNo(),"224106");
				if(agentPreRecordTotal3!=null){			//如果是活动补贴账户
					agentPreRecordTotal2.setActivitySubsidyFreeze(agentPreRecordTotal3.getControlAmount());		//活动补贴账户已冻结金额
					agentPreRecordTotal2.setActivitySubsidyBalance(agentPreRecordTotal3.getCurrBalance());		//活动补贴账户余额
					agentPreRecordTotal2.setActivitySubsidyAvailableBalance(agentPreRecordTotal3.getAvailBalance());		//活动补贴账户可用余额
				}
			}
			page.setResult(list);
		} catch (Exception e) {
			 log.error("异常:", e);
		}
		return page;
	}

	/**
	 *  导出数据（代理商账户）
	 *
	 */
	@PreAuthorize("hasAuthority('agentsAccountQuery:export')")
	@RequestMapping(value = "/agentsAccountExport.do")
	@ResponseBody
	public void agentsAccountExport(@ModelAttribute("agentPreRecordTotal") AgentPreRecordTotal agentPreRecordTotal,
		@ModelAttribute("sort") Sort sort ,HttpServletResponse response, HttpServletRequest request)  throws IOException  {

		// 用于对数据字典的数据进行格式化显示
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商分润账户查询导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		//查询表格数据
		List<String> agentInfoList = new ArrayList<String>() ;
		String userNoStrs = "" ;
		try {
			Map<String, String> tempMap = null;

			AgentInfo agent = new AgentInfo();
			agent.setAgentNo(agentPreRecordTotal.getAgentNo());
			agent.setLevel(agentPreRecordTotal.getAgentLevel());
			agentInfoList = agentInfoService.findAgentList(agent) ;
			userNoStrs = StringUtils.join(agentInfoList, ",") ;
			if(StringUtils.isBlank(userNoStrs)){
				return ;
			}

			List<AgentPreRecordTotal> list = agentPreRecordTotalService.exportAgentPreRecordTotalList(agentPreRecordTotal, userNoStrs,sort);
			for (AgentPreRecordTotal agentPreRecordTotal2 : list) {
				AgentInfo agentInfo = agentInfoService.findAgentByUserId(agentPreRecordTotal2.getAgentNo()) ;
				if(agentInfo != null){
					agentPreRecordTotal2.setAgentNo(agentInfo.getAgentNo());
					agentPreRecordTotal2.setAgentName(agentInfo.getAgentName());
					agentPreRecordTotal2.setAgentLevel(agentInfo.getAgentLevel()+"");
				}
				AgentPreRecordTotal agentPreRecordTotal3 = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNoAndSubjectNo(agentPreRecordTotal2.getAgentNo(),"224106");
				if(agentPreRecordTotal3!=null){			//如果是活动补贴账户
					agentPreRecordTotal2.setActivitySubsidyFreeze(agentPreRecordTotal3.getControlAmount());		//活动补贴账户已冻结金额
					agentPreRecordTotal2.setActivitySubsidyBalance(agentPreRecordTotal3.getCurrBalance());		//活动补贴账户余额
					agentPreRecordTotal2.setActivitySubsidyAvailableBalance(agentPreRecordTotal3.getAvailBalance());		//活动补贴账户可用余额
				}
				tempMap = new HashMap<String, String>();
				tempMap.put("agentName",agentPreRecordTotal2.getAgentName() == null ? "" : agentPreRecordTotal2.getAgentName());
				tempMap.put("agentNo",agentPreRecordTotal2.getAgentNo() == null ? "" : agentPreRecordTotal2.getAgentNo());
				tempMap.put("agentLevel",agentPreRecordTotal2.getAgentLevel() == null ? "" : agentPreRecordTotal2.getAgentLevel());
				tempMap.put("openBackAmount",agentPreRecordTotal2.getOpenBackAmount() == null ? "0.00" : agentPreRecordTotal2.getOpenBackAmount().toString());
				tempMap.put("rateDiffAmount",agentPreRecordTotal2.getRateDiffAmount() == null ? "0.00" : agentPreRecordTotal2.getRateDiffAmount().toString());
				tempMap.put("tuiCostAmount",agentPreRecordTotal2.getTuiCostAmount() == null ? "0.00" : agentPreRecordTotal2.getTuiCostAmount().toString());
				tempMap.put("riskSubAmount",agentPreRecordTotal2.getRiskSubAmount() == null ? "0.00" : agentPreRecordTotal2.getRiskSubAmount().toString());
				tempMap.put("merMgAmount",agentPreRecordTotal2.getMerMgAmount() == null ? "0.00" : agentPreRecordTotal2.getMerMgAmount().toString());
				tempMap.put("bailSubAmount",agentPreRecordTotal2.getBailSubAmount() == null ? "0.00" : agentPreRecordTotal2.getBailSubAmount().toString());
				tempMap.put("otherAmount",agentPreRecordTotal2.getOtherAmount() == null ? "0.00" : agentPreRecordTotal2.getOtherAmount().toString());
				tempMap.put("terminalFreezeAmount",agentPreRecordTotal2.getTerminalFreezeAmount() == null ? "0.00" : agentPreRecordTotal2.getTerminalFreezeAmount().toString());
				tempMap.put("otherFreezeAmount",agentPreRecordTotal2.getOtherFreezeAmount() == null ? "0.00" : agentPreRecordTotal2.getOtherFreezeAmount().toString());
				tempMap.put("controlAmount",agentPreRecordTotal2.getControlAmount() == null ? "0.00" : agentPreRecordTotal2.getControlAmount().toString());
				tempMap.put("currBalance",agentPreRecordTotal2.getCurrBalance() == null ? "0.00" : agentPreRecordTotal2.getCurrBalance().toString());
				tempMap.put("availBalance",agentPreRecordTotal2.getAvailBalance() == null ? "0.00" : agentPreRecordTotal2.getAvailBalance().toString());

				tempMap.put("activitySubsidyFreeze",agentPreRecordTotal2.getActivitySubsidyFreeze() == null ? "0.00" : agentPreRecordTotal2.getActivitySubsidyFreeze().toString());
				tempMap.put("activitySubsidyBalance",agentPreRecordTotal2.getActivitySubsidyBalance() == null ? "0.00" : agentPreRecordTotal2.getActivitySubsidyBalance().toString());
				tempMap.put("activitySubsidyAvailableBalance",agentPreRecordTotal2.getActivitySubsidyAvailableBalance() == null ? "0.00" : agentPreRecordTotal2.getActivitySubsidyAvailableBalance().toString());
				data.add(tempMap);
			}

		} catch (Exception e) {
			log.error("异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {
				"agentName",
				"agentNo",
				"agentLevel",
				"openBackAmount",
				"rateDiffAmount",
				"tuiCostAmount",
				"riskSubAmount",
				"merMgAmount",
				"bailSubAmount",
				"otherAmount",
				"terminalFreezeAmount",
				"otherFreezeAmount",
				"controlAmount",
				"currBalance",
				"availBalance",
				"activitySubsidyFreeze",
				"activitySubsidyBalance",
				"activitySubsidyAvailableBalance"};

		String[] colsName = new String[] { "代理商名称","代理商编号","代理商级别","开通返现 ","费率差异 ","超级推成本 ","风控扣款预扣款 ",
				"商户管理费预扣款 ","保证金预扣款 ","其他预扣款 ",
				"机具款预冻结 ","其他预冻结 ","分润账户已冻结金额 ","分润账户余额","分润账户可用余额","活动补贴账户已冻结金额","活动补贴账户余额","活动补贴账户可用余额"};
		export.export(cols, colsName, data, response.getOutputStream());

	}


	/**
	 * 代理商分润日结 汇总内容
	 *
	 *
	 * @return
	 */
//	@PreAuthorize("hasAuthority('agentsProfitDaySettle:collectionData')")
	@RequestMapping(value = "/findAgentsAccountCollection.do")
	@ResponseBody
	public Map<String, Object> findAgentsAccountCollection(
			@ModelAttribute("agentPreRecordTotal") AgentPreRecordTotal agentPreRecordTotal,
			@ModelAttribute("sort") Sort sort ,HttpServletResponse response ) {


		Map<String, Object> msg = new HashMap<String, Object>();
		try {

			//查询表格数据
			List<String> agentInfoList = new ArrayList<String>();
			List<String> agentLevelList = new ArrayList<String>();
			List<AgentInfo> agentInfoLevel = new ArrayList<AgentInfo>();
			String userNoStrs = "";

			Map<String, String> tempMap = null;
			agentInfoList = agentInfoService.findAgentListByAgentNo(agentPreRecordTotal.getAgentNo());
			if (StringUtils.isBlank(agentPreRecordTotal.getAgentNo())) {
					if (!"ALL".equals(agentPreRecordTotal.getAgentLevel())) {
						agentInfoLevel = agentInfoService.findEntityByLevel(agentPreRecordTotal.getAgentLevel());
						for (AgentInfo agentInfo : agentInfoLevel) {
							agentLevelList.add(agentInfo.getAgentNo());
						}
						userNoStrs = StringUtils.join(agentLevelList, ",");
					} else {
						userNoStrs = StringUtils.join(agentInfoList, ",");
					}
			} else {
					userNoStrs = StringUtils.join(agentInfoList, ",");
			}
			if (StringUtils.isBlank(userNoStrs)) {
				return msg;
			}

			Map<String,Object> map = agentPreRecordTotalService.findAgentPreRecordTotalListCollection(agentPreRecordTotal, userNoStrs);

			Map<String,Object> map2 = agentPreRecordTotalService.findAgentPreRecordTotalListCollectionByUserNoStrs(userNoStrs);

			Object allOpenBackAmount = "0";
			Object allRateDiffAmount =  "0";
			Object allTuiCostAmount =  "0";
			Object allTiskSubAmount =  "0";
			Object allMerMgAmount =  "0";
			Object allOtherAmount =  "0";
			Object allTerminalFreezeAmount =  "0";
			Object allOtherFreezeAmount =  "0";
			Object allBailSubAmount =  "0";
			Object allCurrBalance =  "0";
			Object allControlAmount =  "0";
			Object allAvailBalance =  "0";
			Object allCurrBalance2 =  "0";
			Object allControlAmount2 =  "0";
			Object allAvailBalance2 =  "0";

			if (map != null) {
				allOpenBackAmount = map.get("allOpenBackAmount")== null ? "0.00" :map.get("allOpenBackAmount").toString();
				allRateDiffAmount = map.get("allRateDiffAmount")== null ? "0.00" :map.get("allRateDiffAmount").toString();
				allTuiCostAmount = map.get("allTuiCostAmount")== null ? "0.00" :map.get("allTuiCostAmount").toString();
				allTiskSubAmount = map.get("allTiskSubAmount")== null ? "0.00" :map.get("allTiskSubAmount").toString();
				allMerMgAmount = map.get("allMerMgAmount")== null ? "0.00" :map.get("allMerMgAmount").toString();
				allOtherAmount = map.get("allOtherAmount")== null ? "0.00" :map.get("allOtherAmount").toString();
				allTerminalFreezeAmount = map.get("allTerminalFreezeAmount")== null ? "0.00" :map.get("allTerminalFreezeAmount").toString();
				allOtherFreezeAmount = map.get("allOtherFreezeAmount")== null ? "0.00" :map.get("allOtherFreezeAmount").toString();
				allBailSubAmount = map.get("allBailSubAmount")== null ? "0.00" :map.get("allBailSubAmount").toString();
				allCurrBalance = map.get("allCurrBalance")== null ? "0.00" :map.get("allCurrBalance").toString();
				allControlAmount = map.get("allControlAmount")== null ? "0.00" :map.get("allControlAmount").toString();
				allAvailBalance = map.get("allAvailBalance")== null ? "0.00" :map.get("allAvailBalance").toString();
			}
			if(map2 !=null){
				allCurrBalance2 = map2.get("allCurrBalance")== null ? "0.00" :map2.get("allCurrBalance").toString();
				allControlAmount2 = map2.get("allControlAmount")== null ? "0.00" :map2.get("allControlAmount").toString();
				allAvailBalance2 = map2.get("allAvailBalance")== null ? "0.00" :map2.get("allAvailBalance").toString();
			}
			msg.put("allOpenBackAmount", allOpenBackAmount);//交易总金额
			msg.put("allRateDiffAmount", allRateDiffAmount);//提现总笔数
			msg.put("allTuiCostAmount", allTuiCostAmount);//调整后总分润
			msg.put("allTiskSubAmount", allTiskSubAmount);//原交易分润
			msg.put("allMerMgAmount", allMerMgAmount);//原提现分润
			msg.put("allOtherAmount", allOtherAmount);//调账金额
			msg.put("allTerminalFreezeAmount", allTerminalFreezeAmount);//调账金额
			msg.put("allOtherFreezeAmount", allOtherFreezeAmount);//调账金额
			msg.put("allBailSubAmount", allBailSubAmount);//调账金额
			msg.put("allCurrBalance", allCurrBalance);//调账金额
			msg.put("allControlAmount", allControlAmount);//调账金额
			msg.put("allAvailBalance", allAvailBalance);//调账金额
			msg.put("allCurrBalance2", allCurrBalance2);//活动补贴账户余额
			msg.put("allControlAmount2", allControlAmount2);//活动补贴账户已冻结金额
			msg.put("allAvailBalance2", allAvailBalance2);//活动补贴账户可用余额

		} catch (Exception e) {
			log.error("异常:",e);
		}
		return msg;
	}



	/**
	 * 导出数据(代理商分润预调账查询)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:export')")
	@RequestMapping(value = "exportPreAdjustResult.do", method = RequestMethod.POST)
	public void exportPreAdjustResult(@RequestParam Map<String, String> params,
			@ModelAttribute AgentPreAdjust agentPreAdjust, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		// 用于对数据字典的数据进行格式化显示
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商分润预调账记录导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			Map<String, String> tempMap = null;
			// 查询代理商分润预调账明细
			List<AgentPreAdjust> list = agentPreAdjustService.exportAgentsProfitPreAdjustList(agentPreAdjust);
			if (list != null && list.size() > 0) {
				for (AgentPreAdjust adjustDetail : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("adjustTime", adjustDetail.getAdjustTime() == null ? "" : df.format(adjustDetail.getAdjustTime()));
					tempMap.put("applicant", adjustDetail.getApplicant());
					tempMap.put("agentNo", adjustDetail.getAgentNo());
					tempMap.put("agentName", adjustDetail.getAgentName());
					tempMap.put("openBackAmount",adjustDetail.getOpenBackAmount() == null ? "" : adjustDetail.getOpenBackAmount().toString());
					tempMap.put("rateDiffAmount", adjustDetail.getRateDiffAmount() == null ? "" : adjustDetail.getRateDiffAmount().toString());
					tempMap.put("riskSubAmount", adjustDetail.getRiskSubAmount() == null ? "" : adjustDetail.getRiskSubAmount().toString());
					tempMap.put("merMgAmount",adjustDetail.getMerMgAmount() == null ? "" : adjustDetail.getMerMgAmount().toString());
					tempMap.put("bailSubAmount", adjustDetail.getBailSubAmount() == null ? "" : adjustDetail.getBailSubAmount().toString());
					tempMap.put("otherAmount", adjustDetail.getOtherAmount() == null ? "" : adjustDetail.getOtherAmount().toString());
					tempMap.put("activityAvailableAmount", adjustDetail.getActivityAvailableAmount() == null ? "" : adjustDetail.getActivityAvailableAmount().toString());
					tempMap.put("activityFreezeAmount", adjustDetail.getActivityFreezeAmount() == null ? "" : adjustDetail.getActivityFreezeAmount().toString());
					tempMap.put("generateAmount", adjustDetail.getGenerateAmount() == null ? "" : adjustDetail.getGenerateAmount().toString());
					tempMap.put("remark", adjustDetail.getRemark());
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] { "adjustTime", "applicant", "agentNo", "agentName", "openBackAmount", "rateDiffAmount", 
				"riskSubAmount", "merMgAmount","bailSubAmount",  "otherAmount", "activityAvailableAmount","activityFreezeAmount","generateAmount","remark"};

		String[] colsName = new String[] { "申请调账时间","申请人","代理商编号","代理商名称","开通返现","费率差异","风控扣款","商户管理费","保证金扣除","其他","账户可用余额调账金额","账户冻结余额调账金额","预调账金额","备注"};
		export.export(cols, colsName, data, response.getOutputStream());

	}
	
	
	/**
	 * 导出数据(代理商分润预冻结查询)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:export')")
	@RequestMapping(value = "exportPreFreezeResult.do", method = RequestMethod.POST)
	public void exportPreFreezeResult(@RequestParam Map<String, String> params,
			@ModelAttribute AgentPreFreeze agentPreFreeze, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		// 用于对数据字典的数据进行格式化显示
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商分润预冻结记录导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			Map<String, String> tempMap = null;
			// 查询代理商分润预冻结明细
			List<AgentPreFreeze> list = agentPreFreezeService.exportAgentsProfitPreFreezeList(agentPreFreeze);
			if (list != null && list.size() > 0) {
				for (AgentPreFreeze freezeDetail : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("agentNo", freezeDetail.getAgentNo());
					tempMap.put("agentName", freezeDetail.getAgentName());
					tempMap.put("freezeTime", freezeDetail.getFreezeTime() == null ? "" : df.format(freezeDetail.getFreezeTime()));
					tempMap.put("freezeReason", freezeDetail.getFreezeReason());
					tempMap.put("terminalFreezeAmount", freezeDetail.getTerminalFreezeAmount() == null ? "" : freezeDetail.getTerminalFreezeAmount().toString());
					tempMap.put("otherFreezeAmount", freezeDetail.getOtherFreezeAmount() == null ? "" : freezeDetail.getOtherFreezeAmount().toString());
					tempMap.put("operater", freezeDetail.getOperater());
					tempMap.put("remark", freezeDetail.getRemark());
					tempMap.put("fenFreezeAmount", freezeDetail.getFenFreezeAmount() == null ? "" : freezeDetail.getFenFreezeAmount().toString());
					tempMap.put("activityFreezeAmount", freezeDetail.getActivityFreezeAmount() == null ? "" : freezeDetail.getActivityFreezeAmount().toString());
					tempMap.put("freezeAmount", freezeDetail.getFreezeAmount() == null ? "" : freezeDetail.getFreezeAmount().toString());
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {  "agentNo", "agentName", "freezeTime", "freezeReason", 
				"terminalFreezeAmount", "otherFreezeAmount", "fenFreezeAmount","activityFreezeAmount","freezeAmount","operater", "remark"};

		String[] colsName = new String[] { "代理商编号","代理商名称","冻结时间","冻结原因","机具款预冻结金额","其他预冻结金额","分润账户冻结金额","活动补贴冻结金额","预冻金额","操作人","备注"};
		export.export(cols, colsName, data, response.getOutputStream());

	}
	
	/**
	 * 导出数据(代理商分润交易明细)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('agentsProfitDetail:tranDeatilExport')")
	@RequestMapping(value = "tranDeatilExport.do", method = RequestMethod.POST)
	public void tranDeatilExport(@RequestParam Map<String, String> params,
			@ModelAttribute TransShortInfo transShortInfo, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商交易分润明细导出_" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		
		List<SysDict> checkAccountStatusList = new ArrayList<SysDict>() ;
		try {
			checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			Map<String, String> tempMap = null;
			if(transShortInfo != null){
				if(StringUtils.isNotEmpty(transShortInfo.getMerchantName())){
					transShortInfo.setMerchantName(URLDecoder.decode(transShortInfo.getMerchantName(), "UTF-8"));
				}
			}
			// 查询代理商分润交易明细
			List<TransShortInfo> list = transShortInfoService.exportAgentsProfitTransShortInfoList(transShortInfo);
			if (list != null && list.size() > 0) {
				for (TransShortInfo info : list) {
					
					//0 不限，1 贷记卡，2 借记卡
					String cardType = "";
					if(StringUtils.isNotBlank(info.getCardType())){
						if("0".equals(info.getCardType())){
							cardType = "不限";
						}else if("1".equals(info.getCardType())){
							cardType = "贷记卡";
						}else if("2".equals(info.getCardType())){
							cardType = "借记卡";
						}else{
							cardType = " ";
						}
					}
					
					//NOCOLLECT 未汇总，COLLECTED 汇总
					String agentProfitCollectionStatus = "";
					if(StringUtils.isNotBlank(info.getAgentProfitCollectionStatus())){
						if("NOCOLLECT".equals(info.getAgentProfitCollectionStatus())){
							agentProfitCollectionStatus = "未汇总";
						}else if("COLLECTED".equals(info.getAgentProfitCollectionStatus())){
							agentProfitCollectionStatus = "已汇总";
						}else{
							agentProfitCollectionStatus = " ";
						}
					}
					
					tempMap = new HashMap<String, String>();
					tempMap.put("plateOrderNo", info.getPlateOrderNo());
					tempMap.put("transTime", info.getTransTime() == null ? "" : df.format(info.getTransTime()));
					tempMap.put("hardwareProductName", info.getHardwareProductName());
					tempMap.put("businessProductName", info.getBusinessProductName());
					tempMap.put("serviceName", info.getServiceName());
					tempMap.put("cardType", cardType);
					tempMap.put("merchantNo", info.getMerchantNo());
					tempMap.put("merchantName", info.getMerchantName());
					tempMap.put("oneAgentNo", info.getOneAgentNo());
					tempMap.put("oneAgentName", info.getOneAgentName());
					tempMap.put("agentNo", info.getAgentNo());
					tempMap.put("agentName", info.getAgentName());
					tempMap.put("agentLevel", info.getAgentLevel());
					tempMap.put("transAmount", info.getTransAmount()== null ? "" : info.getTransAmount().toString());
					tempMap.put("merchantRate", info.getMerchantRate());
					tempMap.put("merchantFee", info.getMerchantFee()== null ? "" : info.getMerchantFee().toString());
					
					tempMap.put("transDeductionFee", info.getTransDeductionFee()== null ? "" : info.getTransDeductionFee().toString());//抵扣交易商户手续费
					tempMap.put("actualFee", info.getActualFee()== null ? "" : info.getActualFee().toString());//实际交易商户手续费
					tempMap.put("merchantPrice", info.getMerchantPrice()== null ? "" : info.getMerchantPrice().toString());//自选商户手续费
					tempMap.put("deductionMerFee", info.getDeductionMerFee()== null ? "" : info.getDeductionMerFee().toString());//抵扣自选商户手续费
					tempMap.put("actualOptionalFee", info.getActualOptionalFee()== null ? "" : info.getActualOptionalFee().toString());//实际自选商户手续费
					
					tempMap.put("acqEnname", info.getAcqEnname());
					tempMap.put("plateAcqMerchantRate", info.getPlateAcqMerchantRate());
					tempMap.put("acqOutCost", info.getAcqOutCost()== null ? "" : info.getAcqOutCost().toString());
					tempMap.put("agentShareAmount", info.getAgentShareAmount()== null ? "" : info.getAgentShareAmount().toString());
					tempMap.put("merCashFee", info.getMerCashFee()== null ? "" : info.getMerCashFee().toString());
					tempMap.put("deductionFee", info.getDeductionFee()== null ? "" : info.getDeductionFee().toString());
					tempMap.put("cashAgentShareAmount", info.getCashAgentShareAmount()== null ? "" : info.getCashAgentShareAmount().toString());
					tempMap.put("acqEnname", info.getAcqEnname());
					tempMap.put("daiCost", info.getDaiCost()== null ? "" : info.getDaiCost().toString());
					tempMap.put("dianCost", info.getDianCost()== null ? "" : info.getDianCost().toString());
					tempMap.put("checkAccountStatus", format.formatSysDict(info.getCheckAccountStatus(), checkAccountStatusList));
					tempMap.put("agentProfitCollectionStatus", agentProfitCollectionStatus);
					tempMap.put("collectionBatchNo", info.getCollectionBatchNo());
					tempMap.put("agentProfitGroupTime", info.getAgentProfitGroupTime() == null ? "" : df.format(info.getAgentProfitGroupTime()));

					tempMap.put("oneAgentShareAmount", info.getOneAgentShareAmount()== null ? "0.0" : info.getOneAgentShareAmount().toString());
					tempMap.put("oneCashAgentShareAmount", info.getOneCashAgentShareAmount()== null ? "0.0" : info.getOneCashAgentShareAmount().toString());
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
		}

		NewListDataExcelExport export = new NewListDataExcelExport();
		String[] cols = new String[] {  
				"plateOrderNo", "transTime", 
				"hardwareProductName", "businessProductName", 
				"serviceName", "cardType",
				"merchantNo", "merchantName",
				"oneAgentNo", "oneAgentName","agentNo", "agentName","agentLevel",
				"transAmount", "merchantRate",
				"merchantFee", "transDeductionFee",
				"actualFee","merchantPrice",
				"deductionMerFee","actualOptionalFee",
				"acqEnname","plateAcqMerchantRate",
				"acqOutCost", "oneAgentShareAmount","agentShareAmount",			//一级代理商交易分润
				"merCashFee","deductionFee", "oneCashAgentShareAmount","cashAgentShareAmount",		//一级代理商提现分润
				"acqEnname", "daiCost",
				"dianCost", "checkAccountStatus",
				"agentProfitCollectionStatus", "collectionBatchNo",
				"agentProfitGroupTime"};

		String[] colsName = new String[] {
				"交易订单号","订单时间",
				"硬件产品种类","业务产品",
				"服务类型","卡类型",
				"商户编号","商户名称",
				"一级代理商编号","一级代理商名称","代理商编号","代理商名称","代理商级别",
				"交易金额","交易商户扣率",
				"交易商户手续费",
				"抵扣交易商户手续费","实际交易商户手续费","自选商户手续费","抵扣自选商户手续费","实际自选商户手续费",
				"收单机构","收单扣率",
				"收单服务成本","一级代理商交易分润","交易代理商分润",
				"商户提现手续费","抵扣商户提现手续费","一级代理商提现分润","提现代理商分润",
				"出款通道","代付成本",
				"垫资成本","对账状态",
				"代理商分润汇总",
				"汇总批次","汇总时间"};
		OutputStream out = response.getOutputStream();
		export.export(cols, colsName, data, out);
		out.close();

	}
	
	
	/**
	 * 导出数据(代理商分润日结)
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('agentsProfitDaySettle:export')")
	@RequestMapping(value = "exportAgentsProfitDaySettleList.do", method = RequestMethod.POST)
	public void agentsProfitDaySettleExport(@RequestParam Map<String, String> params,
			@ModelAttribute("agentShareDaySettle") AgentShareDaySettle agentShareDaySettle, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		//用于对数据字典的数据进行格式化显示
		// 入账状态
		List<SysDict> enterAccountStatusList = null;
		try {
			enterAccountStatusList = sysDictService.findSysDictGroup("enter_account_status");
		} catch (Exception e) {
			log.error("异常:", e);
		}
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商分润日结导出_" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		try {
			Map<String, String> tempMap = null;
			// 查询代理商分润日结
			List<AgentShareDaySettle> list = agentShareDaySettleService.exportAgentShareDaySettleList(agentShareDaySettle);
			if (list != null && list.size() > 0) {
				for (AgentShareDaySettle info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("collectionBatchNo", info.getCollectionBatchNo());
					tempMap.put("groupTime", info.getGroupTime() == null ? "" : df.format(info.getGroupTime()));
					tempMap.put("transDate", info.getTransDate() == null ? "" : df1.format(info.getTransDate()));
					tempMap.put("agentNo", info.getAgentNo());
					tempMap.put("agentName", info.getAgentName());
					tempMap.put("agentLevel", info.getAgentLevel());
					String parentAgentNo = "";
					String parentAgentName = "";
					
					if (!info.getParentAgentNo().equals("0")) {
						parentAgentNo = info.getParentAgentNo();
						AgentInfo parentAgentInfo = agentInfoService.findEntityByAgentNo(parentAgentNo);
						if (parentAgentInfo != null) {
							parentAgentName = parentAgentInfo.getAgentName();
						}
						else {
							log.info("找不到代理商编号" + parentAgentNo);
						}
					}
					
					tempMap.put("parentAgentNo", parentAgentNo);
					tempMap.put("parentAgentName", parentAgentName);
					tempMap.put("saleName", info.getSaleName());
					tempMap.put("transTotalAmount", info.getTransTotalAmount()== null ? "" : info.getTransTotalAmount().toString());
					tempMap.put("transTotalNum", info.getTransTotalNum()== null ? "" : info.getTransTotalNum().toString());
					tempMap.put("duiSuccTransTotalNum", info.getDuiSuccTransTotalNum()== null ? "" : info.getDuiSuccTransTotalNum().toString());
					tempMap.put("duiSuccTransTotalAmount", info.getDuiSuccTransTotalAmount()== null ? "" : info.getDuiSuccTransTotalAmount().toString());
					tempMap.put("cashTotalNum", info.getCashTotalNum()== null ? "" : info.getCashTotalNum().toString());
					tempMap.put("merFee", info.getMerFee()== null ? "" : info.getMerFee().toString());
					
					tempMap.put("transDeductionFee", info.getTransDeductionFee()== null ? "" : info.getTransDeductionFee().toString());//抵扣交易商户手续费
					tempMap.put("actualFee", info.getActualFee()== null ? "" : info.getActualFee().toString());//实际交易商户手续费
					tempMap.put("merchantPrice", info.getMerchantPrice()== null ? "" : info.getMerchantPrice().toString());//自选商户手续费
					tempMap.put("deductionMerFee", info.getDeductionMerFee()== null ? "" : info.getDeductionMerFee().toString());//抵扣自选商户手续费
					tempMap.put("actualOptionalFee", info.getActualOptionalFee()== null ? "" : info.getActualOptionalFee().toString());//实际自选商户手续费
					
					tempMap.put("merCashFee", info.getMerCashFee()== null ? "" : info.getMerCashFee().toString());
					tempMap.put("deductionFee", info.getDeductionFee()== null ? "" : info.getDeductionFee().toString());
					tempMap.put("acqOutCost", info.getAcqOutCost()== null ? "" : info.getAcqOutCost().toString());
					tempMap.put("acqOutProfit", info.getAcqOutProfit()== null ? "" : info.getAcqOutProfit().toString());
					tempMap.put("daiCost", info.getDaiCost()== null ? "" : info.getDaiCost().toString());
					tempMap.put("dianCost", info.getDianCost()== null ? "" : info.getDianCost().toString());
					tempMap.put("preTransShareAmount", info.getPreTransShareAmount()== null ? "" : info.getPreTransShareAmount().toString());
					tempMap.put("preTransCashAmount", info.getPreTransCashAmount()== null ? "" : info.getPreTransCashAmount().toString());
					tempMap.put("openBackAmount", info.getOpenBackAmount()== null ? "" : info.getOpenBackAmount().toString());
					tempMap.put("rateDiffAmount", info.getRateDiffAmount()== null ? "" : info.getRateDiffAmount().toString());
					tempMap.put("riskSubAmount", info.getRiskSubAmount()== null ? "" : info.getRiskSubAmount().toString());
					tempMap.put("merMgAmount", info.getMerMgAmount()== null ? "" : info.getMerMgAmount().toString());
					tempMap.put("bailSubAmount", info.getBailSubAmount()== null ? "" : info.getBailSubAmount().toString());
					tempMap.put("otherAmount", info.getOtherAmount()== null ? "" : info.getOtherAmount().toString());
					tempMap.put("adjustTransShareAmount", info.getAdjustTransShareAmount()== null ? "" : info.getAdjustTransShareAmount().toString());
					tempMap.put("adjustTransCashAmount", info.getAdjustTransCashAmount()== null ? "" : info.getAdjustTransCashAmount().toString());
					tempMap.put("adjustTotalShareAmount", info.getAdjustTotalShareAmount()== null ? "" : info.getAdjustTotalShareAmount().toString());
					tempMap.put("terminalFreezeAmount", info.getTerminalFreezeAmount()== null ? "" : info.getTerminalFreezeAmount().toString());
					tempMap.put("otherFreezeAmount", info.getOtherFreezeAmount()== null ? "" : info.getOtherFreezeAmount().toString());
					tempMap.put("enterAccountStatus", format.formatSysDict(info.getEnterAccountStatus(), enterAccountStatusList));
					tempMap.put("enterAccountTime", info.getEnterAccountTime()== null ? "" : df.format(info.getEnterAccountTime()));
					tempMap.put("operator", info.getOperator()== null ? "" : info.getOperator().toString());
					tempMap.put("realEnterShareAmount", info.getRealEnterShareAmount()== null ? "" : info.getRealEnterShareAmount().toString());
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
		}

		NewListDataExcelExport export = new NewListDataExcelExport();
		String[] cols = new String[] {  
				 "collectionBatchNo",
				 "groupTime",
				 "transDate", 
				 "agentNo", 
				 "agentName",
				 "agentLevel",
				 "parentAgentNo", 
				 "parentAgentName",
				 "saleName",
				 "transTotalAmount",
				 "transTotalNum",
				 "duiSuccTransTotalNum",
				 "duiSuccTransTotalAmount",
				 "cashTotalNum",
				 "merFee",
				 "transDeductionFee",
				 "actualFee",
				 "merchantPrice",
				 "deductionMerFee",
				 "actualOptionalFee",
				 "merCashFee",
				 "deductionFee",
				 "acqOutCost",
				 "acqOutProfit",
				 "daiCost",
				 "dianCost",
				 "preTransShareAmount",
				 "preTransCashAmount",
				 "openBackAmount",
				 "rateDiffAmount",
				 "riskSubAmount",
				 "merMgAmount",
				 "bailSubAmount",
				 "otherAmount",
				 "adjustTransShareAmount",
				 "adjustTransCashAmount",
				 "adjustTotalShareAmount",
				 "realEnterShareAmount",
				 "terminalFreezeAmount",
				 "otherFreezeAmount",
				 "enterAccountStatus",
				 "enterAccountTime",
				 "operator"};

		String[] colsName = new String[] {
				"汇总批次号","汇总时间", "交易日期", "代理商编号", "代理商名称", "代理商级别", "上级代理商编号", "上级代理商名称", "所属销售", "交易总金额", "交易总笔数", "对账成功交易总笔数",
				"对账成功交易总金额", "提现总笔数", "商户交易手续费", "抵扣交易商户手续费","实际交易商户手续费","自选商户手续费","抵扣自选商户手续费","实际自选商户手续费",
				"商户提现手续费","抵扣商户提现手续费", "收单成本", "收单收益", "代付成本", "垫资成本",
				"原交易分润 ", "原提现分润 ", "开通返现 ", "费率差异 ", "风控扣款 ", "商户管理费 ","保证金扣除 ",  "其他 ", "调整后交易分润 ",
				"调整后提现分润 ", "调整后总分润 ","实际到账分润 ", "机具款冻结 ", "其他冻结 ", "入账状态","入账时间","操作人"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();

	}
	
	//代理商预调账 下载模板
	@PreAuthorize("hasAuthority('agentsProfitAdjustmentQuery:downLoad')")
	@RequestMapping(value="/DownloadBacthAdjustTemplate.do")
	public String downloadBacthAdjustTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.BACTH_ADJUST_TEMPLATE;
		DownloadUtil.download(response, filePath,"代理商批量预调账明细模板.xls");
		return null;
	}
	
	//代理商预冻结 下载模板
	@PreAuthorize("hasAuthority('agentsProfitPreFreezeQuery:downLoad')")
	@RequestMapping(value="/DownloadBacthFreezeTemplate.do")
	public String downloadBacthFreezeTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.BACTH_FREEZE_TEMPLATE;
		DownloadUtil.download(response, filePath,"代理商批量预冻结调账明细模板.xls");
		return null;
	}
	
	//代理商批量解冻 下载模板
	@PreAuthorize("hasAuthority('agentsProfitBatchUnfreeze:downLoad')")
	@RequestMapping(value="/DownloadBacthUnfreezeTemplate.do")
	public String DownloadBacthUnfreezeTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.BATCH_UNFREEZE_TEMPLATE;
		DownloadUtil.download(response, filePath,"代理商批量解冻明细模板.xls");
		return null;
	}

	/**
	 * 代理商分润提现记录
	 *
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('agentsProfitToCash:query')")
	@RequestMapping(value = "/toAgentsProfitToCash.do")
	public String toAgentsProfitToCash(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		log.info("代理商分润提现记录");
		String agentNo = params.get("agentNo");
		AgentInfo agentInfo = null;
		if (agentNo != null) {
			agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			params.put("agentName", agentInfo.getAgentName());
		}

		Date yesDay = DateUtil.getPreviousDate(new Date());
		String yesDayStr = DateUtil.getFormatDate("yyyy-MM-dd",yesDay);
		params.put("startTime",yesDayStr);
		params.put("endTime",yesDayStr);
		params.put("agentNo", agentNo);
		model.put("params", params);
		return "agentsProfit/agentsProfitToCash";
	}


	/**
	 * 代理商分润提现记录查询
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('agentsProfitToCash:query')")
	@RequestMapping(value = "/findAgentsProfitToCashList.do")
	@ResponseBody
	public Page<SettleOrderInfo> findAgentsProfitToCashList(@ModelAttribute("settleOrderInfo")SettleOrderInfo settleOrderInfo,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<SettleOrderInfo> page) {
		try {

			List<SettleOrderInfo> list = agentProfitTransferService.findSettleOrderInfoList(settleOrderInfo, sort, page);
			page.setResult(list);

		} catch (Exception e) {
			log.error("异常:", e);
		}
		return page;
	}

	/**
	 * 导出数据(代理商分润提现)
	 *
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('agentsProfitToCash:export')")
	@RequestMapping(value = "exportAgentsProfitToCashList.do", method = RequestMethod.POST)
	public void exportAgentsProfitToCashList(@ModelAttribute("settleOrderInfo")SettleOrderInfo settleOrderInfo
			, HttpServletResponse response, HttpServletRequest request)
			throws IOException {


		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "代理商分润提现导出_" + sdf.format(new Date()) + ".xls";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();

		try {
			String status ="";
			String settleStatus="";
			String subType="";
			Map<String, String> tempMap = null;
			// 查询代理商分润提现
			List<SettleOrderInfo> list = agentProfitTransferService.exportSettleOrderInfoList(settleOrderInfo);
			if (list != null && list.size() > 0) {
				for (SettleOrderInfo info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("createTime", info.getCreateTime() == null ? "" : df.format(info.getCreateTime()));
					tempMap.put("settleUserNo", info.getSettleUserNo());
					tempMap.put("agentName", info.getAgentName());
					tempMap.put("settleAmount", info.getSettleAmount()== null ? "" : info.getSettleAmount().toString());
					tempMap.put("feeAmount", info.getFeeAmount()== null ? "" : info.getFeeAmount().toString());
					tempMap.put("status", info.getStatus());
					tempMap.put("inAccName", info.getInAccName());
					tempMap.put("inAccNo", info.getInAccNo());
					tempMap.put("settleMsg", info.getSettleMsg());
					tempMap.put("subType", info.getSubType());
					if("0".equals(info.getStatus())){
						status="未提交";
					}else if("1".equals(info.getStatus())){
						status="已提交";
					}else if("2".equals(info.getStatus())){
						status= "提交失败";
					}else if("3".equals(info.getStatus())){
						status= "超时";
					}else if("4".equals(info.getStatus())){
						status= "交易成功";
					}else if("5".equals(info.getStatus())){
						status= "交易失败";
					}else if("6".equals(info.getStatus())){
						status= "未知";
					}
					if("0".equals(info.getSettleStatus())){
						settleStatus="未结算";
					}else if("1".equals(info.getSettleStatus())){
						status="已结算";
					}else if("2".equals(info.getSettleStatus())){
						settleStatus= "结算中";
					}else if("3".equals(info.getSettleStatus())){
						settleStatus= "结算失败";
					}else if("4".equals(info.getSettleStatus())){
						settleStatus= "转T1结算";
					}else if("5".equals(info.getSettleStatus())){
						settleStatus= "不结算";
					}else if("6".equals(info.getSettleStatus())){
						settleStatus= "已返鼓励金";
					}
					if("4".equals(info.getSubType())){
						subType="活动补贴出款";

					}else if("5".equals(info.getSubType())){
						subType="代理商分润";
					}else{
						subType=info.getSubType();
					}
					tempMap.put("status", status);
					tempMap.put("subType", subType);
					tempMap.put("settleStatus", settleStatus);
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("异常:", e);
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[] {
				"createTime",
				"settleUserNo",
				"agentName",
				"settleAmount",
				"feeAmount",
				"subType",
				"settleStatus",
				"status",
				"inAccName",
				"inAccNo",
				"settleMsg"};

		String[] colsName = new String[] {
				"创建时间","代理商编号", "代理商名称", "结算金额", "手续费","出款子类型", "结算状态","出款状态", "开户名", "银行账号", "备注"};
		export.export(cols, colsName, data, response.getOutputStream());

	}
}
