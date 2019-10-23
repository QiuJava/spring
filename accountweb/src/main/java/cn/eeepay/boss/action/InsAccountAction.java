package cn.eeepay.boss.action;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.InsAccountService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.UrlUtil;
/**
 * 内部账
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/insAccountAction")
public class InsAccountAction {
	
	@Resource
	public SysDictService sysDictService;
	
	@Resource
	public CurrencyService  currencyService;
	
	@Resource
	public SubjectService  subjectService;
	
	@Resource
	public InsAccountService  insAccountService;
	
	@Resource
	public OrgInfoService orgInfoService;
	
	private static final Logger log = LoggerFactory.getLogger(InsAccountAction.class);

	@PreAuthorize("hasAuthority('createInsAccount:query')")
	@RequestMapping(value="/toCreateInsAccount.do")
	public String toCreateInsAccount(ModelMap model, @RequestParam Map<String, String> params){
		log.info("进入内部账户创建页面控制层");
		List<OrgInfo> orgInfoList=null;
		try {
			 orgInfoList = orgInfoService.findOrgInfo();
		} catch (Exception e) {
			log.error("异常:",e);
		}		
		model.put("orgInfoList", orgInfoList);
		return "insAccount/createInsAccount";
	}
	@PreAuthorize("hasAuthority('insAccountListInfo:query')")
	@RequestMapping(value="/insAccountListInfo.do")
	public String insAccountListInfo(ModelMap model, @RequestParam Map<String, String> params){
		log.info("进入内部账户页面显示列表控制层");
		List<OrgInfo> orgInfoList = null;
		List<SysDict> accountStatusList = null;
		List<SysDict> balanceFromList = null;
		List<Currency> currencyList = null;
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
			  currencyList = currencyService.findCurrency();
			  orgInfoList = orgInfoService.findOrgInfo();
			  accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
			  balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
			  subjects = subjectService.findSubjectList();
		} catch (Exception e) {
			log.error("进入内部账户页面显示列表失败");
			log.error("异常:",e);
		}
		model.put("currencyList", currencyList);
		model.put("orgInfoList", orgInfoList);
		model.put("accountStatusList", accountStatusList);
		model.put("balanceFromList", balanceFromList);
		model.put("subjectList", subjects) ;
		model.put("params", params);
		return "insAccount/insAccountListInfo";
	}
	@PreAuthorize("hasAuthority('insAccountDetailQuery:query')")
	@RequestMapping(value="insAccountDetailQuery.do")
	public String insAccountDetailQuery(ModelMap model,@RequestParam Map<String, String> params,@RequestParam(value = "forwardTo", required = false)Integer forwardTo){
		log.info("进入内部账户显示详情控制层");
		List<SysDict> balanceFromList = null;
		String accountNo = params.get("accountNo");
		model.put("accountNo", accountNo);
		try {
			balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
		} catch (Exception e) {
//			log.error("异常:",e);
			log.error("进入内部账户显示详情失败");
			log.error("异常:",e);
		}
		model.put("balanceFromList", balanceFromList);
		model.put("params", params);
		model.put("forwardTo", forwardTo);
		return "insAccount/insAccountDetailQuery";
	}

	
	/**
	 * 创建内部账户
	 * @return
	 */
	@PreAuthorize("hasAuthority('createInsAccount:insert')")
	@RequestMapping(value="/createInsAccount.do")
	@ResponseBody
	public Map<String,Object> createAccountNo(ModelMap model,@ModelAttribute Subject subject,@ModelAttribute Currency currency,@RequestParam Map<String, String> params){
		log.info("进入创建内部账户控制层");
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String,Object> msg=new HashMap<>();
		String orgNo = params.get("orgNo");
		try {
			Map<String,Object> result = insAccountService.createInsAccount(orgNo,userInfo);
			boolean resultStatus = (boolean) result.get("status");
			String resultMsg =  (String) result.get("msg");
			msg.put("msg",resultMsg);
			msg.put("status", resultStatus) ;
			log.info(msg.toString());
		} catch (Exception e) {
			msg.put("msg","开立账号异常:" + e.getMessage());
			msg.put("status", false) ;
			log.error(msg.toString());
			log.error("异常:",e);
		}
		return msg;		
	}
	
	
	@PreAuthorize("hasAuthority('insAccountListInfo:query')")
	@RequestMapping(value = "findInsAccountListInfo.do")
	@ResponseBody
	public Page<InsAccount> findInsAccountListInfo(ModelMap model,@ModelAttribute InsAccount insAccount,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<InsAccount> page){
			log.info("进入内部账户分页查询控制层");
		try {
			insAccountService.findInsAccountListInfo(insAccount,sort,page);
		} catch (Exception e) {
			log.error("进入内部账户分页查询出现异常");
			log.error("异常:",e);
		}	
			System.out.println(page);
			return page;
	}
	
	
	/**
	 * 交易明细跳转账户明细
	 * @param model
	 * @param params
	 * @return
	 */
	
	@RequestMapping(value="/insideTransInfo.do")
	@ResponseBody
	public Page<InsideTransInfo> insideTransInfo(@ModelAttribute InsideTransInfo insideTransInfo,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<InsideTransInfo> page){
		try {
			log.info("内部交易明细分页查询控制层");
			insAccountService.findInsideTransList(insideTransInfo,params,sort,page);
		} catch (Exception e) {
			log.error("部交易明细分页查询出现异常");
			log.error("异常:",e);
		}
			System.out.println(page);
			return page;
	}
	
	/**
	 * 导出内部账户交易明细
	 * @param insideTransInfo
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('extAccountDetailQuery:export')")
	@RequestMapping(value = "exportInsAccountTrans.do")
	public void exportInsAccountTrans(@ModelAttribute InsideTransInfo insideTransInfo, @RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<SysDict> balanceFromList = null;
		try {
			balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "内部交易"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<InsideTransInfo> list = new ArrayList<InsideTransInfo>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		  DateFormat tf = new SimpleDateFormat("HH:mm:ss") ;
		  
		//从数据库中查询数据
		  try {
			list = insAccountService.findExportInsideTransList(insideTransInfo,params) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		  for(InsideTransInfo insTrans:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("accountNo", insTrans.getAccountNo());
			  map.put("recordDate", insTrans.getRecordDate()==null?"":df.format(insTrans.getRecordDate())) ;
			  map.put("recordTime", insTrans.getRecordTime()==null?"":tf.format(insTrans.getRecordTime())) ;
			  map.put("serialNo", insTrans.getSerialNo()) ;
			  map.put("childSerialNo", insTrans.getChildSerialNo()) ;
			  map.put("recordAmount", insTrans.getRecordAmount().toString()) ;
			  map.put("balance", insTrans.getBalance().toString()) ;
			  map.put("avaliBalance", insTrans.getAvaliBalance().toString()) ;
			  map.put("debitCreditSide", exportFormat.formatSysDict(insTrans.getDebitCreditSide(), balanceFromList)) ;
			  map.put("summaryInfo", insTrans.getSummaryInfo()) ;
			  
			  data.add(map) ;
		  }
		  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"accountNo","recordDate","recordTime","serialNo","childSerialNo","recordAmount","balance","avaliBalance",
				  "debitCreditSide","summaryInfo"};
		  String[] colsName = new String[]{"账号","记账日期","记账时间","记账流水号","记账子交易流水号","记账金额","余额","可用余额","借贷方向","摘要"};
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}
	@PreAuthorize("hasAuthority('insAccountListInfo:export')")
	@RequestMapping(value = "exportInsAccount.do")
	public void exportInsAccount(@ModelAttribute InsAccount insAccount, @RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {
		//表单提交解码
		insAccount.setAccountName(java.net.URLDecoder.decode(insAccount.getAccountName() , "UTF-8"));
		
		//格式化导出列的值
		List<SysDict> balanceFromList = null;
		List<SysDict> accountStatusList = null;
		List<Currency> currencyList = null;
		try {
			balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
			currencyList = currencyService.findCurrency();
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
	    String fileName = "内部账户"+sdf.format(new Date())+".xls" ;
	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
	    response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
	    response.addHeader("Pragma", "no-cache");
	    response.addHeader("Cache-Control", "no-cache");
	    List<InsAccount> list = new ArrayList<InsAccount>() ;
	    List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
	    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
	    //从数据库中查询数据
	    try {
			list = insAccountService.findExportInsAccountList(insAccount) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		for(InsAccount insAccountQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("accountNo", insAccountQ.getAccountNo()) ;
			  map.put("subjectNo", insAccountQ.getSubjectNo()) ;
			  map.put("subjectName", insAccountQ.getSubject().getSubjectName()) ;
			  map.put("currencyNo",  exportFormat.formatCurrency(insAccountQ.getCurrencyNo(), currencyList)) ;
			  map.put("accountName", insAccountQ.getAccountName()) ;
			  map.put("currBalance", insAccountQ.getCurrBalance().toString()) ;
			  map.put("availBalance", insAccountQ.getAvailBalance().toString()) ;
			  map.put("balanceFrom",exportFormat.formatSysDict(insAccountQ.getBalanceFrom(), balanceFromList)) ;
			  map.put("accountStatus", exportFormat.formatSysDict(insAccountQ.getAccountStatus(), accountStatusList)) ;
			  map.put("createTime", sdf2.format(insAccountQ.getCreateTime())) ;
			  map.put("creator", insAccountQ.getCreator()) ;
			  
			  data.add(map) ;
		  }
		  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"accountNo","subjectNo","subjectName","currencyNo","accountName","currBalance","availBalance",
				  "balanceFrom","accountStatus","createTime","creator"};
		  String[] colsName = new String[]{"账号","科目编号","科目名称","币种号","账户名称","余额","可用余额","余额借贷方向","账户状态","开户时间","创建人"};
		  double[] cellWidth = { 8000, 4000, 8000, 3000, 8000, 3000, 3500, 4500, 3500, 5000, 3000  };
		  export.export(cols, colsName,cellWidth, data, response.getOutputStream());
//		
//		//传递模板地址和要操作的页签
//        ExcelExp excel = new XssExcelExp("d:/test1.xlsx", 0);
//
//        //创建页脚，打印excel时显示页数
//        excel.createFooter();
//
//        //插入行
//        int startRow = 1;//起始行
//        int rows = 3;//插入行数
//        excel.insertRows(startRow, rows);
//
//      //在插入的行中写入数据
//        wirteXssExcel(excel);
//
//      //为模板中变量赋值
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("xlsx_a", "2016-06-07 12:00:00");
//        map.put("xlsx_b", "测试");
//        map.put("xlsx_c", "12345");
//        excel.replaceExcelData(map);
//
//        //导出，此处只封装了浏览器下载方式
//        //调用downloadExcel，返回输出流给客户端
//        String fileName = "export1.xlsx";
//        excel.downloadExcel(response, fileName);
		
	}
//	public void wirteXssExcel(ExcelExp excel){
//        XSSFSheet sheet = excel.getXssSheet();
//        List list = new ArrayList();
//        for (int i = 0; i < 3; i++) {
//        	XSSFRow row = sheet.createRow(i+1);
//    		row.createCell(0).setCellValue(String.valueOf(i + 1));
//    		row.createCell(1).setCellValue("111");
//    		row.createCell(2).setCellValue("222");
//    		row.createCell(3).setCellValue("333");
//        }
//    }
//    
//    public void wirteHssExcel(ExcelExp excel){
//        HSSFSheet sheet = excel.getHssSheet();
//        List list = new ArrayList();
//        for (int i = 0; i < list.size(); i++) {
//        HSSFRow row = sheet.createRow(i+1);
//    		row.createCell(0).setCellValue(String.valueOf(i + 1));
//    		row.createCell(1).setCellValue("111");
//    		row.createCell(2).setCellValue("222");
//    		row.createCell(3).setCellValue("333");
//        }
//    }
	

}
