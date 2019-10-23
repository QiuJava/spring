package cn.eeepay.boss.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
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
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.MerchantCardInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.RecordAccountRuleTransTypeService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.nposp.AcqOrgService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.UrlUtil;

/**
 * 外部账
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年4月12日13:45:54
 *
 */
@Controller
@RequestMapping(value = "/extAccountAction")
public class ExtAccountAction {
	@Resource
	public CurrencyService currencyService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	public SubjectService subjectService;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public CommonService commonService;
	@Resource
	public SystemInfoService systemInfoService;
	@Resource
	private AcqOrgService acqOrgService;
	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	public RecordAccountRuleTransTypeService recordAccountRuleTransTypeService;
	
	
	private static final Logger log = LoggerFactory.getLogger(ExtAccountAction.class);
	
	@PreAuthorize("hasAuthority('createExtAccount:query')")
	@RequestMapping(value = "/toCreateExtAccount.do")
	public String  createOutAccount(ModelMap model, @RequestParam Map<String, String> params){
		List<OrgInfo> orgInfoList=null;
		List<Currency> currencyList=null;
		List<SysDict> accountType = null;
		
		try {
			orgInfoList = orgInfoService.findOrgInfo();
			currencyList = currencyService.findCurrency();
			accountType = sysDictService.findSysDictGroup("sys_account_type");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("accountTypes", accountType);
		model.put("orgInfoList", orgInfoList);
		model.put("currencyList", currencyList);
		return  "extAccount/createExtAccount";
	}
	/**
	 * 开立外部账户
	 * @param outAccount
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('createExtAccount:insert')")
	@RequestMapping(value = "/saveExtAccountInfo.do")
	@Logs(description="开立外部账户")
	@ResponseBody
	public Map<String,Object> SaveExtAccountInfo(@ModelAttribute ExtAccount outAccount, @RequestParam Map<String, String> params){		
		Map<String,Object> msg=new HashMap<>();
		String cardNo = params.get("cardNo");
		String userId = params.get("userId");
		String accountType = params.get("accountType");

		if (StringUtils.isBlank(outAccount.getSubjectNo())) {
			 log.error("科目编号不能为空！");
			 msg.put("msg","科目编号不能为空！");
			 log.info(msg.toString());
			 return msg;
		}
		if (StringUtils.isBlank(userId)) {
			 log.error("商户/代理商编号不能为空！");
			 msg.put("msg","商户/代理商编号不能为空！");
			 log.info(msg.toString());
			 return msg;
		}
		try{
			//accountNo = 6位机构号+14位科目号+12位顺序号
			String accountNo = commonService.getExtAccountNo(outAccount.getOrgNo(),outAccount.getSubjectNo());
			Date now = new Date();
			outAccount.setCurrBalance(new BigDecimal(0));
			outAccount.setControlAmount(new BigDecimal(0));
			outAccount.setAvailBalance(new BigDecimal(0));
			outAccount.setSettlingAmount(new BigDecimal(0));
			outAccount.setPreFreezeAmount(new BigDecimal(0));
			outAccount.setParentTransBalance(new BigDecimal(0));
			outAccount.setAccountStatus("1");
			Subject subject = subjectService.getSubject(outAccount.getSubjectNo());
			outAccount.setBalanceAddFrom(subject.getAddBalanceFrom());
			outAccount.setBalanceFrom(subject.getBalanceFrom());
			outAccount.setAccountNo(accountNo);
			outAccount.setAccountName(subject.getSubjectName()); //账号名称  = 科目名称
			
			//获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			outAccount.setCreator(userInfo.getUsername());//创建者
			
			outAccount.setCreateTime(now);
			outAccount.setSumFlag(subject.getInnerSumFlag());//汇总入明细标志
			outAccount.setDayBalFlag(subject.getInnerDayBalFlag());//修改余额标志
			//SystemInfo systemInfo =  new SystemInfo();
//			SystemInfo systemInfo = systemInfoService.findSystemInfoById(1);
			//上一个交易日（新建账户这里应该为空？）
			//outAccount.setParentTransDay(systemInfo.getParentTransDate());
			ExtAccountInfo extAccountInfo = new ExtAccountInfo();
			extAccountInfo.setAccountNo(outAccount.getAccountNo());
			extAccountInfo.setAccountOwner(outAccount.getOrgNo());
			extAccountInfo.setAccountType(accountType);
			extAccountInfo.setCardNo(cardNo);
			extAccountInfo.setCurrencyNo(outAccount.getCurrencyNo());
			extAccountInfo.setSubjectNo(outAccount.getSubjectNo());
			extAccountInfo.setUserId(userId);
			ExtAccountInfo extAccountInfoQ = extAccountService.exsitsExtAccountInfo(extAccountInfo) ;
			if(extAccountInfoQ != null){
				log.error("外部账户已存在！");
				msg.put("state", false) ;
				msg.put("msg", "外部账户已存在！") ;
				log.info(msg.toString());
				return msg ;
			}
			int i =extAccountService.insertExtAccount(outAccount,extAccountInfo);
			if(i>0){
				msg.put("state",true);
				msg.put("msg", "保存成功!");
				log.info(msg.toString());
			}
		}catch(Exception e){
			 msg.put("state",false);
			 msg.put("msg","开通外部账号失败！");
			 log.error(msg.toString());
		}
		return msg;
	}
	@PreAuthorize("hasAuthority('queryAllAccountList:query')")
	@RequestMapping(value = "/queryAllAccountList.do")
	public String  queryAllAccountList(ModelMap model, @RequestParam Map<String, String> params){
		List<Currency> currencyList = null;
		List<OrgInfo> orgInfoList=null;
		List<SysDict> accountTypeList = null;
		List<SysDict> accountStatusList = null;  
		List<Subject> subjects  = null;
		
		params.put("pageNo", "1");
		params.put("pageSize", "10");
		params.put("sortname", "");
		params.put("sortorder", "");
		
		String queryParams = params.get("queryParams");
		if (StringUtils.isNotBlank(queryParams)) {
			log.info(queryParams);
		    byte[] b = Base64.decode(queryParams.getBytes());
		    String s=new String(b);
		    log.info(s);
		    Map<String, Object> queryParamsMap= UrlUtil.getUrlParams(s);
	    	for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
				log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
				params.put(entry.getKey(), entry.getValue().toString());
			}
		}
	    
		try {
			accountTypeList = sysDictService.findSysDictGroup("sys_account_type");
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
			orgInfoList = orgInfoService.findOrgInfo();
			currencyList = currencyService.findCurrency();
			subjects = subjectService.findSubjectList();
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("accountTypeList", accountTypeList);
		model.put("accountStatusList", accountStatusList);
		model.put("currencyList", currencyList);
		model.put("orgInfoList", orgInfoList);
		model.put("subjectList", subjects) ;
		model.put("params", params);
		return  "extAccount/queryAllAccountList";
	}
	@PreAuthorize("hasAuthority('queryAllAccountList:query')")
	@RequestMapping(value = "findAllAccountInfo.do")
	@ResponseBody
	public Page<Map<String, Object>> findAllAccountInfo(@ModelAttribute("extAccount")ExtAccount extAccount,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ExtAccount> page){
		
		Page<Map<String, Object>> tpage = new Page<Map<String, Object>>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		
		List<ExtAccount> extAccountList = new ArrayList<ExtAccount>();

		List<String> agentInfoList = new ArrayList<String>() ;
		List<String> merchantInfoList = new ArrayList<String>() ;
		List<String> acqOrgList = new ArrayList<String>() ;
		String userNoStrs = "" ;
		
		if(StringUtils.isBlank(extAccount.getExtAccountInfo().getUserName()) && StringUtils.isBlank(extAccount.getExtAccountInfo().getMobilephone())){
			userNoStrs = "isBlank" ;
		}else{
			agentInfoList = agentInfoService.findAgentListByParams(extAccount.getExtAccountInfo().getUserName(), extAccount.getExtAccountInfo().getMobilephone()) ;
			merchantInfoList = merchantInfoService.findMerchantListByParams(extAccount.getExtAccountInfo().getUserName(), extAccount.getExtAccountInfo().getMobilephone()) ;
			acqOrgList = acqOrgService.findAcqOrgListByParams(extAccount.getExtAccountInfo().getUserName(), extAccount.getExtAccountInfo().getMobilephone()) ;
			userNoStrs = StringUtils.join(agentInfoList, ",") ;
			//拼接数据库查询IN条件里面的值
			if(agentInfoList.size() == 0){
				if(merchantInfoList.size() != 0){
					userNoStrs = userNoStrs.concat(StringUtils.join(merchantInfoList, ",")) ;
					if(acqOrgList.size() != 0){
						userNoStrs = userNoStrs.concat(",").concat(StringUtils.join(acqOrgList, ",")) ;
					}
				}else{
					userNoStrs = userNoStrs.concat(StringUtils.join(acqOrgList, ",")) ;
				}
			}else{
				if(merchantInfoList.size() != 0){
					userNoStrs = userNoStrs.concat(",").concat(StringUtils.join(merchantInfoList, ",")) ;
					if(acqOrgList.size() != 0){
						userNoStrs = userNoStrs.concat(",").concat(StringUtils.join(acqOrgList, ",")) ;
					}
				}else{
					if(acqOrgList.size() != 0){
						userNoStrs = userNoStrs.concat(",").concat(StringUtils.join(acqOrgList, ",")) ;
					}else{
						userNoStrs = userNoStrs.concat(StringUtils.join(acqOrgList, ",")) ;
					}
				}
			}
		}
		try {
			if(StringUtils.isBlank(userNoStrs)){
				userNoStrs = "-12123" ;
			}
			extAccountList = extAccountService.findAllAccountInfo(extAccount,sort,page ,userNoStrs);
			Map<String, Object> tempMap = null;
			for(ExtAccount ext:extAccountList){
				
				tempMap = new HashMap<String, Object>();
				tempMap.put("accountNo", ext.getAccountNo());
				tempMap.put("accountName", ext.getAccountName());
				tempMap.put("accountStatus", ext.getAccountStatus());
				tempMap.put("accountType", ext.getAccountType());
				tempMap.put("subjectNo", ext.getSubjectNo());
				tempMap.put("currencyNo", ext.getCurrencyNo());
				tempMap.put("orgNo", ext.getOrgNo());
				tempMap.put("currBalance", ext.getCurrBalance());
				tempMap.put("availBalance", ext.getAvailBalance());
				tempMap.put("settlingAmount", ext.getSettlingAmount());
				tempMap.put("controlAmount", ext.getControlAmount());
				tempMap.put("preFreezeAmount", ext.getPreFreezeAmount());
				tempMap.put("settlingHoldAmount", ext.getSettlingHoldAmount());
				tempMap.put("createTime", ext.getCreateTime());
				tempMap.put("creator", ext.getCreator());
				tempMap.put("userId", ext.getUserId());
				tempMap.put("subjectName", ext.getSubjectName());
				
				//为每条查询出来的数据匹配 userName 和 mobilephone（boss中拿）
				if("A".equalsIgnoreCase(ext.getAccountType())){
					AgentInfo agentInfo = agentInfoService.findAgentByUserId(ext.getUserId()) ;
					if(agentInfo != null){
						//ext.getExtAccountInfo().setUserName(agentInfo.getAgentName());
						//ext.getExtAccountInfo().setMobilephone(agentInfo.getMobilephone());
						tempMap.put("userName", agentInfo.getAgentName());
						tempMap.put("mobilephone", agentInfo.getMobilephone());
						
					}
				}else if("M".equalsIgnoreCase(ext.getAccountType())){
					MerchantInfo merchantInfo = merchantInfoService.findMerchantInfoByUserId(ext.getUserId()) ;
					if(merchantInfo != null){
						//ext.getExtAccountInfo().setUserName(merchantInfo.getMerchantName());
						//ext.getExtAccountInfo().setMobilephone(merchantInfo.getMobilephone());
						tempMap.put("userName", merchantInfo.getMerchantName());
						tempMap.put("mobilephone", merchantInfo.getMobilephone());
						
					}
				}else if("Acq".equalsIgnoreCase(ext.getAccountType())){
					AcqOrg acqOrg = acqOrgService.findAcqOrgByUserId(ext.getUserId()) ;
					if(acqOrg != null){
						tempMap.put("userName", acqOrg.getAcqName());
						tempMap.put("mobilephone", acqOrg.getPhone());
						//ext.getExtAccountInfo().setUserName(acqOrg.getAcqName());
						//ext.getExtAccountInfo().setMobilephone(acqOrg.getPhone());	
					}
				}
				mapList.add(tempMap);
				
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}

		tpage.setPageNo(page.getPageNo());
		tpage.setPageSize(page.getPageSize());
		tpage.setTotalCount(page.getTotalCount());
		tpage.setTotalPages(page.getTotalPages());
		tpage.setResult(mapList);
		//page.setResult(extAccountList);
		return tpage;
	}
	@PreAuthorize("hasAuthority('extAccountDetailQuery:query')")
	@RequestMapping(value = "/extAccountDetailQuery.do")
	public String  extAccountDetailQuery(ModelMap model, @RequestParam Map<String, String> params, @RequestParam(value = "forwardTo", required = false)Integer forwardTo) {
		List<OrgInfo> orgInfoList=null;
		List<Currency> currencyList = null;
		List<SysDict> balanceFromList = null;
		List<RecordAccountRuleTransType> transTypeList = null;
		try {
			orgInfoList = orgInfoService.findOrgInfo();
			currencyList = currencyService.findCurrency();
			balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
			transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
		} catch (Exception e) {
			log.error("异常:",e);
		}
		String date = DateUtil.getFormatDate("yyyy-MM-dd",new Date());
		params.put("recordDate1",date);
		params.put("recordDate2",date);
		model.put("params",params);
		model.put("orgInfoList", orgInfoList);
		model.put("currencyList", currencyList);
		model.put("balanceFromList", balanceFromList);
		model.put("transTypeList", transTypeList);
		model.put("params", params);
		model.put("forwardTo", forwardTo);
		return  "extAccount/extAccountDetailQuery";
	}
	@PreAuthorize("hasAuthority('extAccountDetailQuery:query')")
	@RequestMapping(value = "findAllExtTransDetailInfo.do")
	@ResponseBody
	public Page<Map<String,Object>> findAllExtTransDetailInfo(@ModelAttribute ExtTransInfo extTransInfo, @RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<Map<String,Object>> page){
		try {
			extAccountService.findAllExtTransInfo(extTransInfo , params , sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}
	
	/**
	 * 客户账明细查询 导出
	 * @param params
	 * @param subOutBillDetail
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value = "exportAllExtTransDetailInfo.do",method = RequestMethod.POST)
	public void exportAllExtTransDetailInfo(@RequestParam Map<String,String> params ,
			@ModelAttribute ExtTransInfo extTransInfo,
			HttpServletResponse response,HttpServletRequest request) throws IOException {
		
		
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "客户账明细查询_"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat dfDate = new SimpleDateFormat("yyyy-MM-dd") ;
		DateFormat dfTime = new SimpleDateFormat("HH:mm:ss") ;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		Map<String, String> tempMap = null;
		List<RecordAccountRuleTransType> transTypeList = null;
		try {
			transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
			//查询出账单明细
			List<ExtTransInfo> list = extAccountService.exportAllExtTransInfo(extTransInfo , params );
			if (list != null && list.size() > 0) {
				for (ExtTransInfo info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("accountNo", info.getAccountNo());
					tempMap.put("serialNo", info.getSerialNo());
					tempMap.put("childSerialNo", info.getChildSerialNo());
					tempMap.put("transType", format.formatTransType(info.getTransType() == null ? "":info.getTransType().toString(), transTypeList));
					tempMap.put("recordDate", info.getRecordDate()==null?"":dfDate.format(info.getRecordDate()));
					tempMap.put("recordTime", info.getRecordTime()==null?"":dfTime.format(info.getRecordTime()));
					tempMap.put("debitCreditSide", info.getDebitCreditSide());
					tempMap.put("recordAmount", info.getRecordAmount()==null?"":info.getRecordAmount().toString());
					tempMap.put("balance", info.getBalance()==null?"":info.getBalance().toString());
					tempMap.put("avaliBalance", info.getAvaliBalance()==null?"":info.getAvaliBalance().toString());
					tempMap.put("controlAmount", info.getControlAmount()==null?"":info.getControlAmount().toString());
					tempMap.put("settlingAmount", info.getSettlingAmount()==null?"":info.getSettlingAmount().toString());
					tempMap.put("preFreezeAmount", info.getPreFreezeAmount()==null?"":info.getPreFreezeAmount().toString());
					tempMap.put("summaryInfo", info.getSummaryInfo());
					data.add(tempMap);
				}
			}
			} catch (Exception e) {
				log.error("异常:",e);
			}


		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{
				  "accountNo","serialNo","childSerialNo","transType","recordDate","recordTime"
				  ,"debitCreditSide","recordAmount","balance","avaliBalance","controlAmount"
				  ,"settlingAmount","preFreezeAmount","summaryInfo"};
		 
		  String[] colsName = new String[]{
				  "账号","记账流水号","子记账流水号","交易类型","记账日期","记账时间","借贷方向","记账金额","余额","可用余额","控制金额","结算中金额","预冻结金额","摘要" };
		  export.export(cols, colsName, data, response.getOutputStream());

	}
	
	
	
	@PreAuthorize("hasAuthority('queryAllAccountList:update')")
	@RequestMapping(value = "/toExtAccountStatusUpdate.do")
	public String  toExtAccountStatusUpdate(ModelMap model, @RequestParam Map<String, String> params){
		List<OrgInfo> orgInfoList=null;
		List<Currency> currencyList = null;
		List<SysDict> accountStatusList = null;
		try {
			orgInfoList = orgInfoService.findOrgInfo();
			currencyList = currencyService.findCurrency();
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("orgInfoList", orgInfoList);
		model.put("currencyList", currencyList);
		model.put("accountStatusList", accountStatusList);
		return  "extAccount/extAccountStatusUpdate";
	}
	
	@PreAuthorize("hasAuthority('queryAllAccountList:update')")
	@RequestMapping(value = "/extAccountStatusUpdate.do")
	@ResponseBody
	public Map<String,Object> extAccountStatusUpdate(ModelMap model, @RequestParam Map<String, String> params,@ModelAttribute ExtAccount extAccount) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if(StringUtils.isBlank(extAccount.getAccountStatus()) ){
			msg.put("msg","账户状态不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(extAccount.getAccountNo()) ){
			msg.put("msg","账号不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try{
				 int i = extAccountService.updateExtAccountStatus(extAccount);
				 if(i>0){
					 msg.put("status",true);
					 msg.put("msg", "修改成功!");
				 }
			}catch(Exception e){
				 msg.put("status", false) ;
				 msg.put("msg","修改状态失败！");
				 log.error("修改状态失败",e);
			}
		}
		log.info(msg.toString());
		return msg;
	}
	@PreAuthorize("hasAuthority('queryAllAccountList:updateSettlingHoldAmount')")
	@RequestMapping(value = "/extAccountSettlingHoldAmountUpdate.do")
	@ResponseBody
	public Map<String,Object> extAccountSettlingHoldAmountUpdate(ModelMap model, @RequestParam Map<String, String> params,@ModelAttribute ExtAccount extAccount) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if(extAccount.getSettlingHoldAmount() == null ){
			msg.put("msg","结算保留金额不能为空");
			msg.put("status",false);
			isReturn = true;
		}else if(StringUtils.isBlank(extAccount.getAccountNo()) ){
			msg.put("msg","账号不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			try{
				 int i = extAccountService.updateExtAccountSettlingHoldAmount(extAccount);
				 if(i>0){
					 msg.put("status",true);
					 msg.put("msg", "修改成功!");
				 }
			}catch(Exception e){
				 msg.put("status", false) ;
				 msg.put("msg","修改状态失败！");
				 log.error("修改状态失败",e);
			}
		}
		log.info(msg.toString());
		return msg;
	}
//	@RequestMapping(value = "findAllAccountStatusUpdateInfo.do")
//	@ResponseBody
//	public Page<ExtAccount> findAllAccountStatusUpdateInfo(@ModelAttribute ExtAccount outAccount,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ExtAccount> page){
//		try {
//			extAccountService.findAllAccountStatusUpdateInfo(outAccount,sort,page);
//		} catch (Exception e) {
//			log.error("异常:",e);
//		}	
//		return page;
//	}
}
