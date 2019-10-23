package cn.eeepay.boss.action;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.eeepay.framework.dao.bill.FastCheckAccountDetailMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.DuiAccountStatus;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.OptLogs;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.bill.FastCheckAccountBatch;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.AcqService;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.service.bill.FastCheckAccBatchService;
import cn.eeepay.framework.service.bill.FastCheckAccDetailService;
import cn.eeepay.framework.service.bill.OptLogsService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.nposp.AcqOrgService;
import cn.eeepay.framework.service.nposp.AcqServiceService;
import cn.eeepay.framework.service.nposp.CollectionTransOrderService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.service.nposp.TransInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.ListDataExcelExport;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * 扫码对账(快捷对账)
 *
 */
@Controller
@RequestMapping(value = "/fastDuiAccount")
public class FastDuiAccountAction {
	private static final Logger log = LoggerFactory.getLogger(FastDuiAccountAction.class);

	@Autowired
	public FastCheckAccDetailService fastCheckAccDetailService;
	@Autowired
	public FastCheckAccBatchService fastCheckAccBatchService;
	@Autowired
	public CollectionTransOrderService collectionTransOrderService;
	@Autowired
	public FastCheckAccountDetailMapper duiAccountDetailDao;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public AcqOrgService acqOrgService;
	@Resource
	public TransInfoService transInfoService ;
	@Resource
	public MerchantInfoService merchantInfoService;
	@Resource
	private AcqServiceService acqServiceService;
	
	@Resource
	private OptLogsService optLogsService;

	@Value("${accountApi.http.secret}")  
	private String accountApiHttpSecret;
	@Value("${accountApi.http.url}")  
	private String accountApiHttpUrl;
	/**
	 * 解析国彩对账文件
	 * @return
	 */
	private void resolveGcFileTxt(List<FastCheckAccDetail> acqTrans,File file) throws Exception{
		log.info("==============开始解析对账文件==============");
		FileInputStream fis = new FileInputStream(file);
		BufferedReader reader = new BufferedReader(new InputStreamReader(fis,"GBK"));
		String line = reader.readLine();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		while (line != null&&!"".equals(line)){
			String[] detail = line.split("\\|");
			if(detail.length==12){
				FastCheckAccDetail checkDetail = new FastCheckAccDetail();
				checkDetail.setAcqTransOrderNo(detail[1]);
				checkDetail.setAcqOrderNo(detail[0].trim());
				checkDetail.setAcqOrderTime(sdf.parse(detail[3]));
				checkDetail.setAcqCheckDate(sdf.parse(detail[3]));
				checkDetail.setAcqTransType(detail[5]);
				checkDetail.setAcqTransAmount(new BigDecimal(detail[7]).movePointLeft(2));
				checkDetail.setAcqRefundAmount(new BigDecimal(detail[9]).movePointLeft(2));
				acqTrans.add(checkDetail);
			}
			line = reader.readLine();
		}
		reader.close();
		fis.close();
		log.info("==============解析文件结束==============");
	}

	/**
	 * 
	 * 功能：上传excel文件统计
	 *
	 * @param mf77
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('fastDuiAccountFileUpload:insert')")
	@ResponseBody
	@RequestMapping (value="/duiAccountFileUpload.do", method = RequestMethod.POST) 
	public Map<String,Object> duiAccountFileUpload
	(HttpServletRequest request,@RequestParam final Map<String, String> params) throws IOException {
		Map<String,Object> result=new HashMap<String, Object>();
		/*获取文件路径*/
		String strDirPath = request.getSession().getServletContext()
				.getRealPath("/");
		String temp1 = "fastTemp";
		String tmpPath = strDirPath + temp1;

		String acqEnname = params.get("acqOrg");
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		MultipartFile file=files.get(0);
		String fileName  = file.getOriginalFilename();
		File fileDz = new File(tmpPath + File.separator + fileName); 
		//保存模板路径
		File parentFile =fileDz.getParentFile();
		if(!parentFile.exists()){

			parentFile.mkdirs();
		}
		if(!fileDz.exists())
			fileDz.createNewFile();
		file.transferTo(fileDz);//接收文件
		/*获取文件路径*/

		switch (acqEnname) {
		case "TFB_API":
			break;
		default:
			result.put("statu",false);
			result.put("msg","收单机构未实现");
			log.info(result.toString());
			return result;
		}


		FileInputStream in = null;
		try {
			List<String> errorList=new ArrayList<String>();
			List<FastCheckAccDetail> acqTrans = new ArrayList<>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (fileDz.getName().indexOf(".xls") != -1 || fileDz.getName().indexOf(".xlsx") != -1) {
				//文件不是txt格式，报错
				result.put("statu", false);
				result.put("msg", "不支持EXCEL文件，请上传TXT格式的对账文件");
				log.info(result.toString());
				return result;
			}
			resolveGcFileTxt(acqTrans,fileDz);

			int len = fileDz.getName().lastIndexOf("_")+1;
			String fileTime = fileDz.getName().substring(len,len+8);
			map.put("fileName", fileDz.getName());
			map.put("checkFileDate", DateUtil.StrDateToFormatStr(fileTime, "yyyyMMdd", "yyyy-MM-dd"));
			map.put("checkAccountDetails", acqTrans);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			List<CollectiveTransOrder> transInfos = getCheckTransInfos(acqEnname, sdf.parse(fileTime));
			String duiResult = doCheckAccount(acqEnname,transInfos, map);	

			if(duiResult.equals("0")){
				result.put("statu",true);
				result.put("msg","对账完毕，请查询对账信息");
				log.info(result.toString());
			}else if(duiResult.equals("1")){
				result.put("statu",false);
				result.put("msg","该文件已对账完毕，请不要重复对账");
				log.info(result.toString());
			}
		} catch (Exception e) {
			result.put("statu",false);
			result.put("msg","导入对账异常！");
			log.info(result.toString());
			log.error("异常:",e);
		}finally{
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
//					e.printStackTrace();
					log.error("异常:",e);
				}
			}
			if (fileDz != null) {
				fileDz.delete();
			}
		}
		return result;
	}
	/**
	 * 判断该交易是否已经对账（避免由于时间差异造成的单笔记录）
	 * 
	 * */
	private boolean judgeCheckEd(CollectiveTransOrder transInfo){
		FastCheckAccDetail detail = null;
		try {
			detail = duiAccountDetailDao.findDuiAccountDetailByTransInfo(transInfo);
		} catch (Exception e) {
//			e.printStackTrace();
			log.error("异常:",e);
		}
		if(detail == null){
			return false;
		}
		return true;
	}


	/**
	 * 获取平台对账交易数据
	 * @throws Exception 
	 * 
	 * */
	private List<CollectiveTransOrder> getCheckTransInfos(String acqEnname, Date transDate) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transDate);
		int day = calendar.get(Calendar.DATE);				
		//		calendar.set(Calendar.DATE, day-1);
		Date date = calendar.getTime();

		String jhTimeStart;
		String jhTimeEnd;
		jhTimeStart = sdf.format(date)+" 00:00:00";
		jhTimeEnd = sdf.format(date)+" 23:59:59";

		return collectionTransOrderService.queryTransOrder(acqEnname,jhTimeStart,jhTimeEnd);

	}

	private String doCheckAccount(String acqEnname,
			List<CollectiveTransOrder> transInfos, Map<String, Object> map){
		List<FastCheckAccDetail> checkAccountDetails = (List<FastCheckAccDetail>) map.get("checkAccountDetails");
		String fileName = (String)map.get("fileName");
		String checkFileDate = (String)map.get("checkFileDate");
		FastCheckAccountBatch batch = fastCheckAccBatchService.queryBatchByFileName(fileName, acqEnname);
		if(batch != null){
			return "1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sdfL = new SimpleDateFormat("yyyy-MM-dd");
		FastCheckAccountBatch checkAccountBatch = new FastCheckAccountBatch();

		int random = (int) (Math.random() * 1000);
		String checkBatchNo =  acqEnname+ sdf.format(new Date()) + random;
		checkAccountBatch.setCheckBatchNo(checkBatchNo);
		try {
			checkAccountBatch.setCheckFileDate(sdfL.parse(checkFileDate));
		} catch (ParseException e) {
			log.error("异常:",e);
		}
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		//		SysDict acqOrg = sysDictService.findSysDictByKeyValue("fast_acq_org", acqEnname);
		checkAccountBatch.setCheckFileName(fileName);
		checkAccountBatch.setCheckTime(new Date());
		checkAccountBatch.setOperator(userInfo.getUsername());
		checkAccountBatch.setAcqEnname(acqEnname);
		BigDecimal acqTotalAmount = new BigDecimal("0.00");
		BigDecimal totalAmount = new BigDecimal("0.00");
		for(int i=0; i<checkAccountDetails.size();i++){
			acqTotalAmount = acqTotalAmount.add(checkAccountDetails.get(i).getAcqTransAmount());
		}

		checkAccountBatch.setAcqTotalAmount(acqTotalAmount);

		for(int i=0; i<transInfos.size();i++){
			totalAmount = totalAmount.add(transInfos.get(i).getTransAmount());
		}
		checkAccountBatch.setTotalAmount(totalAmount);

		checkAccountBatch.setAcqTotalItems(Long.parseLong(String.valueOf(checkAccountDetails.size())));
		checkAccountBatch.setTotalItems(Long.parseLong(String.valueOf(transInfos.size())));

		int acqTotalSuccessItems = 0;
		int acqTotalFailedItems = 0;
		int totalSuccessItems = 0;
		int totalFailedItems = 0;
		int count = 0;
		log.info("对账逻辑开始:"+count++);
		for(int i=0; i<checkAccountDetails.size(); i++){			
			FastCheckAccDetail detail = checkAccountDetails.get(i);
			detail.setAcqEnname(acqEnname);			
			detail.setCheckBatchNo(checkBatchNo);	
			detail.setErrorHandleStatus("pendingTreatment");
			detail.setErrorHandleCreator(userInfo.getUsername());
			detail.setRecordStatus(2); 
			for(int j=0; j<transInfos.size(); j++){
				CollectiveTransOrder info = transInfos.get(j);
				boolean flag = false;
				if( info.getOrderNo().trim().equals(detail.getAcqOrderNo().trim())){
					flag = true;
				}
				if(flag){
					installDetailByInfo(detail, info);
					BigDecimal transAmount = detail.getAcqTransAmount();
					if(info.getTransAmount().compareTo(transAmount) == 0){
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						detail.setRecordStatus(1);  //对账成功，则记账也成功
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalSuccessItems++;
						totalSuccessItems++;
					}else{
						detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalFailedItems++;
						totalFailedItems++;
					}
					fastCheckAccDetailService.saveFastCheckAccountDetail(detail);
					i--;
					j--;
					break;
				}
			}

			if(detail.getCheckAccountStatus() == null || detail.getCheckAccountStatus().equals("")){
				String acqOrderNo = detail.getAcqOrderNo();
				CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(acqOrderNo, acqEnname);
				if(transInfo != null){					
					if("SUCCESS".equals(transInfo.getTransStatus())){
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						detail.setRecordStatus(1);  //对账成功，则记账也成功
						installDetailByInfo(detail, transInfo);
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()+1);
						totalSuccessItems++;
						acqTotalSuccessItems++;
					}else{
						detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
						installDetailByInfo(detail, transInfo);						
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()+1);
						totalFailedItems++;
						acqTotalFailedItems++;
					}


				}else{
					detail.setSettleStatus(0);
					detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
					acqTotalFailedItems++;
				}

				detail.setCheckBatchNo(checkBatchNo);
				checkAccountDetails.remove(i);
				fastCheckAccDetailService.saveFastCheckAccountDetail(detail);
				i--;

			}		
		}

		if(transInfos.size() >= 0){
			for(int i=0; i<transInfos.size(); i++){			
				CollectiveTransOrder info = transInfos.get(i);
				/*if(judgeCheckEd(info)){
					checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems()-1);
					continue;
				}*/
				FastCheckAccDetail detail = new FastCheckAccDetail();
				installDetailByInfo(detail, info);
				detail.setAcqEnname(acqEnname);
				detail.setCheckBatchNo(checkBatchNo);
				detail.setCheckAccountStatus(DuiAccountStatus.PLATE_SINGLE.toString());
				detail.setErrorHandleStatus("pendingTreatment");
				detail.setErrorHandleCreator(userInfo.getUsername());
				detail.setRecordStatus(2);
				fastCheckAccDetailService.saveFastCheckAccountDetail(detail);
				totalFailedItems++;
			}
			//			totalFailedItems = totalFailedItems+transInfos.size();
		}


		checkAccountBatch.setAcqTotalSuccessItems(Long.parseLong(String.valueOf(acqTotalSuccessItems)));
		checkAccountBatch.setAcqTotalFailedItems(Long.parseLong(String.valueOf(acqTotalFailedItems)));
		checkAccountBatch.setTotalSuccessItems(Long.parseLong(String.valueOf(totalSuccessItems)));
		checkAccountBatch.setTotalFailedItems(Long.parseLong(String.valueOf(totalFailedItems)));

		if(acqTotalFailedItems>0 || totalFailedItems>0){
			checkAccountBatch.setCheckResult(DuiAccountStatus.FAILED.toString());
			checkAccountBatch.setRecordStatus(2);
		}else{
			checkAccountBatch.setCheckResult(DuiAccountStatus.SUCCESS.toString());
			checkAccountBatch.setRecordStatus(2);
		}


		fastCheckAccBatchService.saveFastCheckAccountBatch(checkAccountBatch);

		return "0";	
	}

	/**
	 * 通过平台交易数据赋值对账明细
	 * 
	 * */
	private void installDetailByInfo(FastCheckAccDetail detail, CollectiveTransOrder info){
		detail.setPlateMerchantNo(info.getMerchantNo());
		detail.setPlateTransAmount(info.getTransAmount());
		detail.setPlateMerchantFee(info.getMerchantFee());
		detail.setPlateAcqMerchantFee(info.getAcqMerchantFee());
		detail.setPlateTransType(info.getTransType());
		detail.setPlateTransStatus(info.getTransStatus());
		detail.setPlateOrderTime(info.getCreateTime());
		detail.setPlateOrderNo(info.getOrderNo());
		detail.setPlateCardNo(info.getAccountNo());
		detail.setSettlementMethod(info.getSettlementMethod());
		detail.setSettleStatus(info.getSettleStatus() == null ? 0 : info.getSettleStatus());
		detail.setAccount(info.getAccount());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			if(info.getTransTime()!=null){
				String merchantSettleDate = sdf.format(info.getTransTime());
				detail.setPlateMerchantSettleDate(sdf.parse(merchantSettleDate));
			}
		} catch (ParseException e) {
			log.error("异常:",e);
		}
	}

	@PreAuthorize("hasAuthority('fastDuiAccountFileUpload:query')")
	@RequestMapping(value = "/toDuiAccountFileUpload.do")
	public String toDuiAccountFileUpload(ModelMap model, @RequestParam Map<String, String> params) throws Exception{

		List<AcqOrg> acqList_ = acqOrgService.findAllAcqOrg();
		List<AcqOrg> acqList = new ArrayList<AcqOrg>();
		acqList.addAll(acqList_);
		//循环收单机构，查询出款服务
		List<AcqService> asList = null;
		for (AcqOrg acq : acqList_) {
			//判断该出款机构是否是批量代付的出款服务类型
			String serviceType = "9,10,11";
			asList = acqServiceService.findByAcqEnnameAndServiceType(acq.getAcqEnname(), serviceType);
			if (asList == null) {
				acqList.remove(acq);
			}
		}

		model.put("acqOrgList", acqList);

		return "fastAccount/duiAccountFileUpload";
	}

	/**
	 * 对账信息查询
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:query')")
	@RequestMapping(value = "/toDuiAccountQuery.do")
	public String toDuiAccountQuery(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
		model.put("recordStatusList", recordStatusList);
		return "fastAccount/duiAccountQuery";
	}
	/**
	 * 对账信息明细查询
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:detail')")
	@RequestMapping(value = "/toDuiAccountDetailQuery.do")
	public String toDuiAccountDetailQuery(ModelMap model, @RequestParam Map<String, String> params, @RequestParam(value = "forwardTo", required = false)Integer forwardTo) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		List<SysDict> checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status");
		model.put("checkAccountStatusList", checkAccountStatusList);
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
		model.put("recordStatusList", recordStatusList);
		List<SysDict> plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status");
		model.put("plateTransStatusList", plateTransStatusList);
		List<SysDict> plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type");
		model.put("plateTransTypeList", plateTransTypeList);
		List<SysDict> settlementMethodList = sysDictService.findSysDictGroup("settlement_method");
		model.put("settlementMethodList", settlementMethodList);
		List<SysDict> settleStatusList = sysDictService.findSysDictGroup("settle_status");
		model.put("settleStatusList", settleStatusList);
		List<SysDict> npospAccountList = sysDictService.findSysDictGroup("nposp_account");
		model.put("npospAccountList", npospAccountList);

		model.put("params", params);
		model.put("forwardTo", forwardTo);
		return "fastAccount/duiAccountDetailQuery";
	}
	/**
	 * 查询所有的对账信息
	 * @param duiAccountBatch
	 * @param sort
	 * @param page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:query')")
	@RequestMapping(value = "/queryDuiAccountList.do")
	@ResponseBody
	public Page<FastCheckAccountBatch> queryDuiAccountList(@RequestParam Map<String, String> params,
			@ModelAttribute("sort")Sort sort,
			@ModelAttribute("page")Page<FastCheckAccountBatch> page ){
		try {
			//String createTime = request.getParameter("createDate") ;
			List<FastCheckAccountBatch> list = fastCheckAccBatchService.queryDuiAccountList(params, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}

	/**
	 * 跳转到详情2页面
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:detail2')")
	@RequestMapping(value = "/toDuiAccountDetail2.do")
	public String duiAccountDetail2(ModelMap model, @RequestParam Map<String, String> params, 
			@RequestParam(value = "checkBatchNo", required = false) String checkBatchNo, 
			@RequestParam(value = "acqEnname", required = false) String acqEnname) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> acq = fastCheckAccBatchService.getAcqTransSumAndCount(checkBatchNo);
		Map<String, Object> plate = fastCheckAccBatchService.getPlateSumAndCount(checkBatchNo);
		Map<String, Object> acqSingle = fastCheckAccBatchService.getAcqTransAmountSumAndCount(checkBatchNo);
		Map<String, Object> plateSingle = fastCheckAccBatchService.getPlateTransAmountSumAndCount(checkBatchNo);
		Map<String, Object> plateMe = fastCheckAccBatchService.getPlateSumAndCountMe(checkBatchNo);

		BigDecimal acqTransAmountSum = (BigDecimal)acq.get("sum"); 			// 渠道金额总数
		Long acqTransAmountCount = (Long)acq.get("count"); 		  			// 渠道交易总个数
		BigDecimal acqRefundAmountSum = (BigDecimal)acq.get("refundSum"); 	// 渠道交易手续费

		BigDecimal plateTransAmountSum = (BigDecimal)plate.get("sum"); 		// 获取平台金额总数
		Long plateTransAmountCount = (Long)plate.get("count"); 				// 平台交易总个数

		BigDecimal plateTransAmountSumMe = (BigDecimal)plateMe.get("sum"); 		// 获取我方平台金额总数
		Long plateTransAmountCountMe = (Long)plateMe.get("count"); 				// 我方平台交易总个数

		BigDecimal longAcqTransAmountSum = (BigDecimal)acqSingle.get("sum"); 		// 获取长款金额总数
		Long longAcqTransAmountCount = (Long)acqSingle.get("count"); 				// 获取长款总个数

		BigDecimal shortPlateTransAmountSum = (BigDecimal)plateSingle.get("sum");	// 获取短款金额总数
		Long shortPlateTransAmountCount = (Long)plateSingle.get("count"); 			// 获取短款个数

		map.put("acqTransAmountSum", acqTransAmountSum);
		map.put("acqTransAmountCount", acqTransAmountCount);
		map.put("acqRefundAmountSum", acqRefundAmountSum);
		map.put("plateTransAmountSum", plateTransAmountSum);
		map.put("plateTransAmountCount", plateTransAmountCount);
		map.put("plateTransAmountSumMe", plateTransAmountSumMe);
		map.put("plateTransAmountCountMe", plateTransAmountCountMe);
		map.put("longAcqTransAmountSum", longAcqTransAmountSum);
		map.put("longAcqTransAmountCount", longAcqTransAmountCount);
		map.put("shortPlateTransAmountSum", shortPlateTransAmountSum);
		map.put("shortPlateTransAmountCount", shortPlateTransAmountCount);
		map.put("checkBatchNo", checkBatchNo);
		//map.put("acqCnname", new String(acqCnname.getBytes("ISO-8859-1"),"UTF-8")) ;
		map.put("acqCnname", acqEnname);

		model.put("map", map);

		return "fastAccount/duiAccountDetail2";
	}

	@PreAuthorize("hasAuthority('fastDuiAccount:delete')")
	@RequestMapping(value = "/deleteDuiAccount.do")
	@ResponseBody
	public Map<String, Object> deleteDuiAccount(@RequestParam(value = "id", required = true)Integer id){
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int re = fastCheckAccBatchService.deleteFastAccountBatch(id);

			if (re > 0) {
				data.put("state", true);
				data.put("msg", "删除对账信息成功");
			} else if (re == -2) {
				data.put("state", false);
				data.put("msg", "该对账批次已经记账，不允许删除");
			} else {
				data.put("state", false);
				data.put("msg", "删除对账信息失败");
			}
		} catch (Exception e) {
			data.put("state", false);
			data.put("msg", "删除对账信息异常!");
			log.error("异常:",e);
		}	
		log.info(data.toString());
		return data;
	}

	/**
	 * 确认记账
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:confirm')")
	@RequestMapping(value = "/confirmAccount.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> confirmAcc(@RequestParam(value = "checkBatchNo", required = true)String checkBatchNo,@RequestParam String acqEnname) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		FastCheckAccountBatch batch = fastCheckAccBatchService.getByCheckBatchNo(checkBatchNo);
		if (batch != null && batch.getRecordStatus().equals(1)) {
			msg.put("state", false);
			msg.put("msg", "该批次已记账，不能再次记账");
			log.info(msg.toString());
			return msg;
		}

		AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(acqEnname);

		if (acqOrg == null) {
			msg.put("state", false);
			msg.put("msg", "收单机构不存在");
			log.info(msg.toString());
		} else {
			msg = confirmAccount(checkBatchNo,acqOrg.getId().toString());

			if ((boolean)msg.get("state") == true) {
				fastCheckAccBatchService.updateRecordStatus(checkBatchNo, 1);
			} else {
				fastCheckAccBatchService.updateRecordStatus(checkBatchNo, 0);
			}
		}
		log.info(msg.toString());
		return msg;
	}

	public Map<String, Object> confirmAccount(String checkBatchNo, String orgId)
			throws Exception {
		Map<String, Object> accMsg = new HashMap<String, Object>();
		//查询所有未成功的对账数据
		List<FastCheckAccDetail> duiacc = fastCheckAccDetailService.findErrorDuiAccountDetailList(checkBatchNo);
		if(duiacc.size()>0){
			int successCount = 0;
			int failedCount = 0;
			FastCheckAccDetail errorDetail = null;
			for (int i = 0; i < duiacc.size(); i++) {
				errorDetail = duiacc.get(i);
				try{
					if("PLATE_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，平台单边记存疑
						CollectiveTransOrder transInfo = new CollectiveTransOrder();
						transInfo = collectionTransOrderService.queryByAcqFastTransInfo(errorDetail.getPlateOrderNo(), errorDetail.getAcqEnname());//对账的交易数据

						Map<String,Object> msg = this.platformSingleMarkSuspect(errorDetail ,transInfo, orgId) ;
						if((boolean)msg.get("state") == false){
							log.info("平台单边记存疑记账失败原因："+msg.get("msg").toString());
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");//记账失败
							errorDetail.setErrorMsg(errMsg);
							errorDetail.setRecordStatus(0);
							failedCount++;
						}else{
							errorDetail.setErrorHandleStatus("checkForzen");
							errorDetail.setErrorMsg("记账成功");
							errorDetail.setRecordStatus(1);
							successCount++;
						}

					}else if("FAILED".equals(errorDetail.getCheckAccountStatus()) || "ACQ_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，收单单边
						CollectiveTransOrder transInfo = new CollectiveTransOrder();
						//transInfo = collectionTransOrderService.queryByAcqFastTransInfo(errorDetail.getAcqOrderNo(), errorDetail.getAcqEnname());
						Map<String,Object> msg = this.acqSingleMarkSuspect(errorDetail ,transInfo,orgId) ;
						if((boolean)msg.get("state") == false){
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");//记账失败
							errorDetail.setErrorMsg(errMsg);
							errorDetail.setRecordStatus(0);
							failedCount++;
							log.info("收单单边记账失败原因："+msg.get("msg").toString());
						}else{
							errorDetail.setErrorHandleStatus("upstreamDoubt");
							errorDetail.setErrorMsg("记账成功");
							errorDetail.setRecordStatus(1);
							successCount++;
						}
					}else if (DuiAccountStatus.AMOUNT_FAILED.toString().equals(errorDetail.getCheckAccountStatus())) {
						//金额不符
						String errMsg = "商户"+errorDetail.getPlateMerchantNo()+"与上游订单"+errorDetail.getAcqOrderNo()+"金额不符";
						errorDetail.setErrorHandleStatus("accountFailed");//记账失败
						errorDetail.setErrorMsg(errMsg);
						errorDetail.setRecordStatus(0);
						failedCount++;
					}
				}catch(Exception e){
					errorDetail.setErrorHandleStatus("accountFailed");//记账失败
					errorDetail.setErrorMsg("记账过程异常");
					errorDetail.setRecordStatus(0);
					failedCount++;
				}

				fastCheckAccDetailService.updateDuiAccount(errorDetail) ;
			}
			if (failedCount != 0) {
				accMsg.put("state", false);
			} else {
				accMsg.put("state", true);
			}
			
			accMsg.put("msg", "记账成功"+successCount+"条，记账失败"+failedCount+"条");
			String msg = (String) accMsg.get("msg");
			String logType = "duiAccountLog";
			//插入财务操作日志
			insertOperateLog(msg, checkBatchNo, logType);
			
		}else{
			accMsg.put("state", false);
			accMsg.put("msg", "无差错数据需要处理！");
		}
		log.info(accMsg.toString());
		return accMsg;
	}
	
	/**
     * 插入财务操作日志
     * @param msg
     * @param checkBatchNo
     */
	private void insertOperateLog(String msg, String checkBatchNo,String logType) {
		//获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		OptLogs log = new OptLogs();
		log.setCheckBatchNo(checkBatchNo);
		log.setLogType(logType);
		log.setOperateContent(msg);
		log.setOperator(userInfo.getUsername());//创建者
		optLogsService.insertOptLogs(log);
	}

	public Map<String, Object> platformSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo,  String acqOrgId) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8平台单边记存疑
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		if(transInfo==null){
			msg.put("state",false);
			msg.put("msg","找不到交易源！");
			log.info(msg.toString());
			return msg;
		}
		try{
			MerchantInfo merInfo = null;
			merInfo = merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", sdf.format(transInfo.getTransTime()));
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ?null:transInfo.getTransAmount().toString()) ;//交易金额
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			claims.put("directAgentNo", merInfo.getAgentNo());
			claims.put("oneAgentNo", merInfo.getOneAgentNo());
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("transTypeCode", "000008");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("agentShareAmount", transInfo.getProfits1().toString());
			//claims.put("exp", exp);
			//claims.put("iat", iat);
			//claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/platformSingleMarkSuspect.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边记存疑返回结果："+response);

			if (response == null || "".equals(response)) {
				//平台单边确认是日切
				msg.put("state",false);
				msg.put("msg","平台单边记存疑返回为空！");
				log.info(msg.toString());
				return msg;
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					//平台单边确认是日切
					msg.put("state",false);
					msg.put("msg","平台单边记存疑失败，reason:"+resp.get("msg").toString());
					log.info(msg.toString());
					return msg;
				}
			}
			/*//3、修改对账详细信息中的    差错处理状态   为'系统自动冻结'
			duiAccountDetail.setErrorHandleStatus("checkForzen");
			duiAccountDetailDao.updateDuiAccountDetail(duiAccountDetail) ;*/
			msg.put("state",true);
			msg.put("msg","平台单边记存疑成功！");
			log.info(msg.toString());
		}catch(Exception e){
			log.info("平台单边赔付商户异常"+e.getMessage());
			msg.put("state",false);
			msg.put("msg","平台单边记存疑异常！");
			log.info(msg.toString());
			log.error("异常:",e);
		}
		return msg;
	}

	public Map<String, Object> acqSingleMarkSuspect(FastCheckAccDetail duiAccountDetail, CollectiveTransOrder transInfo,String acqOrgId) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8上游单边记存疑
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", duiAccountDetail.getAcqOrderTime() == null ?null:sdf.format(duiAccountDetail.getAcqOrderTime())) ;//交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString() : duiAccountDetail.getAcqTransAmount().toString()) ;//交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000004");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", duiAccountDetail.getAcqOrderNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/acqSingleMarkSuspect.do" ;
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边记存疑问返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response) || (boolean)resp.get("status") == false) {
				//上游单边，结算给商户失败
				msg.put("state",false);
				msg.put("msg","上游单边记存疑问失败,reason:"+resp.get("msg").toString());
				log.info(msg.toString());
				return msg;
			}
			/*	//3、修改对账详细信息中的    差错处理状态   为'上游单边记存疑问'
				duiAccountDetail.setErrorHandleStatus("upstreamDoubt");
				duiAccountDetailDao.updateDuiAccount(duiAccountDetail) ;*/
			msg.put("state",true);
			msg.put("msg","上游单边记存疑问成功！");
			log.info(msg.toString());
		}catch(Exception e){
			log.info("上游单边记存疑问异常");
			msg.put("state",false);
			msg.put("msg","上游单边记存疑问异常！");
			log.error("异常:",e);
			log.error(msg.toString());
		}
		log.info(msg.toString());
		return msg;
	}

	/**
	 * 对账差错处理
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:query')")
	@RequestMapping(value = "/toDuiAccountErrorHandle.do")
	public String toDuiAccountErrorHandle(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		List<SysDict> checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status");
		model.put("checkAccountStatusList", checkAccountStatusList);
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
		model.put("recordStatusList", recordStatusList);
		List<SysDict> errorHandleStatusList = sysDictService.findSysDictGroup("check_error_handle_status");
		model.put("errorHandleStatusList", errorHandleStatusList);
		List<SysDict> plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status");
		model.put("plateTransStatusList", plateTransStatusList);
		List<SysDict> plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type");
		model.put("plateTransTypeList", plateTransTypeList);

		return "fastAccount/duiAccountErrorHandle";
	}

	/**
	 * 查询对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('fastDuiAccount:detail')")
	@RequestMapping(value = "/findDuiAccountDetailList.do")
	@ResponseBody
	public Page<FastCheckAccDetail> findDuiAccountDetailList(@RequestParam Map<String,String> params ,@ModelAttribute FastCheckAccDetail duiAccountDetail,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<FastCheckAccDetail> page){
		try {
			String createTimeStart = params.get("createTimeStart") ;
			String createTimeEnd = params.get("createTimeEnd") ;

			fastCheckAccDetailService.findDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}

	/**
	 * 查询对账差错处理信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:query')")
	@RequestMapping(value = "/findErrorDuiAccountDetailList.do")
	@ResponseBody
	public Page<FastCheckAccDetail> findErrorDuiAccountDetailList(@RequestParam Map<String,String> params ,@ModelAttribute FastCheckAccDetail duiAccountDetail,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<FastCheckAccDetail> page){
		try {
			String createTimeStart = params.get("createTimeStart") ;
			String createTimeEnd = params.get("createTimeEnd") ;

			fastCheckAccDetailService.findErrorDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}

	/**
	 * 导出数据(对账详情)
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('fastDuiAccountDetail:export')")
	@RequestMapping(value = "exportDuiAccountDetail.do",method = RequestMethod.POST)
	public void exportDuiAccountDetail(@RequestParam Map<String,String> params ,@ModelAttribute FastCheckAccDetail duiAccountDetail, HttpServletResponse response,HttpServletRequest request) throws IOException {

		String createTimeStart = params.get("createTimeStart") ;
		String createTimeEnd = params.get("createTimeEnd") ; 
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		List<SysDict> errorHandleStatusList = new ArrayList<SysDict>() ;
		List<SysDict> checkAccountStatusList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransTypeList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransStatusList = new ArrayList<SysDict>() ;
		List<SysDict> recordStatusList = new ArrayList<SysDict>() ;
		List<SysDict> settlementMethodList = null;
		List<SysDict> settleStatusList = null;
		List<SysDict> npospAccountList = null;
		try {
			errorHandleStatusList = sysDictService.findSysDictGroup("check_error_handle_status");
			checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
			plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type") ;
			plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status") ;
			recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
			settlementMethodList = sysDictService.findSysDictGroup("settlement_method");
			settleStatusList = sysDictService.findSysDictGroup("settle_status");
			npospAccountList = sysDictService.findSysDictGroup("nposp_account");
		} catch (Exception e1) {
			log.error("异常:",e1);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "快捷对账详情"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<FastCheckAccDetail> list = new ArrayList<FastCheckAccDetail>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;

		System.out.println("duiAccountDetail-->"+duiAccountDetail);
		//从数据库中查询数据
		try {
			list = fastCheckAccDetailService.findExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		for(FastCheckAccDetail duiAcc:list){
			Map<String,String> map = new HashMap<String,String>() ;
			map.put("id", duiAcc.getId().toString());
			map.put("checkBatchNo", duiAcc.getCheckBatchNo()) ;
			map.put("acqOrderNo", duiAcc.getAcqOrderNo());
			map.put("acqTransOrderNo", duiAcc.getAcqTransOrderNo());
			map.put("acqTransAmount", duiAcc.getAcqTransAmount() == null ? "" : duiAcc.getAcqTransAmount().toString()) ;
			map.put("acqRefundAmount", duiAcc.getAcqRefundAmount() == null ? "" : duiAcc.getAcqRefundAmount().toString()) ;
			map.put("acqCheckDate", duiAcc.getAcqCheckDate()==null?"":df.format(duiAcc.getAcqCheckDate())) ;
			map.put("acqEnname", duiAcc.getAcqEnname()) ;
			map.put("plateOrderNo", duiAcc.getPlateOrderNo());
			map.put("plateMerchantNo", duiAcc.getPlateMerchantNo()) ;
			map.put("plateTransAmount", duiAcc.getPlateTransAmount()==null?"":duiAcc.getPlateTransAmount().toString()) ;
			map.put("plateAcqMerchantFee", duiAcc.getPlateAcqMerchantFee()==null?"":duiAcc.getPlateAcqMerchantFee().toString()) ;
			map.put("plateMerchantFee", duiAcc.getPlateMerchantFee()==null?"":duiAcc.getPlateMerchantFee().toString()) ;
			map.put("plateTransType", format.formatSysDict(duiAcc.getPlateTransType(), plateTransTypeList)) ;
			map.put("plateTransStatus", format.formatSysDict(duiAcc.getPlateTransStatus(), plateTransStatusList)) ;
			map.put("checkAccountStatus", format.formatSysDict(duiAcc.getCheckAccountStatus(), checkAccountStatusList)) ;
			map.put("recordStatus", format.formatSysDict(duiAcc.getRecordStatus() == null ? "" : duiAcc.getRecordStatus().toString(), recordStatusList));
			map.put("createTime", duiAcc.getCreateTime()==null?"":df.format(duiAcc.getCreateTime())) ;
			map.put("taskAmount", duiAcc.getTaskAmount() == null ? "" : duiAcc.getTaskAmount().toString()) ;
			map.put("settlementMethod", format.formatSysDict(duiAcc.getSettlementMethod() == null ? "" : duiAcc.getSettlementMethod().toString(), settlementMethodList));
			map.put("settleStatus", format.formatSysDict(duiAcc.getSettleStatus() == null ? "" : duiAcc.getSettleStatus().toString(), settleStatusList));
			map.put("account", format.formatSysDict(duiAcc.getAccount().toString(), npospAccountList));
			data.add(map) ;
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","checkBatchNo","acqOrderNo","acqTransOrderNo",
				"acqTransAmount","acqRefundAmount","acqCheckDate","acqEnname",
				"plateOrderNo","plateMerchantNo","plateTransAmount","plateAcqMerchantFee",
				"plateMerchantFee","plateTransType",
				"plateTransStatus","taskAmount","settlementMethod","settleStatus","account","checkAccountStatus","recordStatus","createTime"};
		String[] colsName = new String[]{"ID","对账批次号", "收单机构订单号", "收单机构交易订单号", 
				"收单机构交易金额",  "收单机构退货金额",  "收单机构对账日期", "收单机构英文名称", 
				"平台订单号","平台商户号", "平台交易金额",  "平台收单机构商户手续费",  
				"平台商户手续费", "平台交易类型",  "平台交易状态","出账任务金额","结算周期","结算状态","交易记账",  "对账状态", "记账状态",
		"创建时间"};
		export.export(cols, colsName, data, response.getOutputStream());

	}

	/**
	 * 导出数据(对账差错处理)
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:export')")
	@RequestMapping(value = "exportDuiAccount.do",method = RequestMethod.POST)
	public void exportDuiAccount(@RequestParam Map<String,String> params ,@ModelAttribute FastCheckAccDetail duiAccountDetail, HttpServletResponse response,HttpServletRequest request) throws IOException {
		String createTimeStart = params.get("createTimeStart") ;
		String createTimeEnd = params.get("createTimeEnd") ; 
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		List<SysDict> errorHandleStatusList = new ArrayList<SysDict>() ;
		List<SysDict> checkAccountStatusList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransTypeList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransStatusList = new ArrayList<SysDict>() ;
		List<SysDict> recordStatusList = new ArrayList<SysDict>() ;
		try {
			errorHandleStatusList = sysDictService.findSysDictGroup("check_error_handle_status");
			checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
			plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type") ;
			plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status") ;
			recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
		} catch (Exception e1) {
			log.error("异常:",e1);
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "快捷对账差错"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<FastCheckAccDetail> list = new ArrayList<FastCheckAccDetail>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;

		//从数据库中查询数据
		try {
			list = fastCheckAccDetailService.findErrorExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail) ;

			for(FastCheckAccDetail duiAcc:list){
				Map<String,String> map = new HashMap<String,String>() ;
				map.put("id", duiAcc.getId().toString());
				map.put("checkBatchNo", duiAcc.getCheckBatchNo()) ;
				map.put("acqOrderNo", duiAcc.getAcqOrderNo());
				map.put("acqTransOrderNo", duiAcc.getAcqTransOrderNo());
				map.put("acqTransAmount", duiAcc.getAcqTransAmount() == null ? "" : duiAcc.getAcqTransAmount().toString()) ;
				map.put("acqRefundAmount", duiAcc.getAcqRefundAmount() == null ? "" : duiAcc.getAcqRefundAmount().toString()) ;
				map.put("acqCheckDate", duiAcc.getAcqCheckDate()==null?"":df.format(duiAcc.getAcqCheckDate())) ;
				map.put("acqEnname", duiAcc.getAcqEnname()) ;
				map.put("plateOrderNo", duiAcc.getPlateOrderNo());
				map.put("plateMerchantNo", duiAcc.getPlateMerchantNo()) ;
				map.put("plateTransAmount", duiAcc.getPlateTransAmount()==null?"":duiAcc.getPlateTransAmount().toString()) ;
				map.put("plateAcqMerchantFee", duiAcc.getPlateAcqMerchantFee()==null?"":duiAcc.getPlateAcqMerchantFee().toString()) ;
				map.put("plateMerchantFee", duiAcc.getPlateMerchantFee()==null?"":duiAcc.getPlateMerchantFee().toString()) ;
				map.put("plateTransType", format.formatSysDict(duiAcc.getPlateTransType(), plateTransTypeList)) ;
				map.put("plateTransStatus", format.formatSysDict(duiAcc.getPlateTransStatus(), plateTransStatusList)) ;
				map.put("checkAccountStatus", format.formatSysDict(duiAcc.getCheckAccountStatus(), checkAccountStatusList)) ;
				map.put("recordStatus", format.formatSysDict(duiAcc.getRecordStatus().toString(), recordStatusList));
				map.put("createTime", duiAcc.getCreateTime()==null?"":df.format(duiAcc.getCreateTime())) ;
				map.put("errorHandleStatus",format.formatSysDict(duiAcc.getErrorHandleStatus(), errorHandleStatusList)) ;
				map.put("errorHandleCreator",duiAcc.getErrorHandleCreator()) ;
				data.add(map) ;
			}
			ListDataExcelExport export = new ListDataExcelExport();
			String[] cols = new String[]{"id","checkBatchNo","acqOrderNo","acqTransOrderNo",
					"acqTransAmount","acqRefundAmount","acqCheckDate","acqEnname",
					"plateOrderNo","plateMerchantNo","plateTransAmount","plateAcqMerchantFee",
					"plateMerchantFee","plateTransType",
					"plateTransStatus","checkAccountStatus","recordStatus","createTime","errorHandleStatus","errorHandleCreator"};
			String[] colsName = new String[]{"ID","对账批次号",  "收单机构订单号", "收单机构交易订单号",  
					"收单机构交易金额",  "收单机构退货金额",  "收单机构对账日期",  "收单机构英文名称",  
					"平台订单号","平台商户号",  "平台交易金额",  "平台收单机构商户手续费",  
					"平台商户手续费",  "平台交易类型",  "平台交易状态",  "对账状态", "记账状态",
					"创建时间","对账差错处理状态","差错处理人"};
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("异常:",e);
		}
	}

	/*==========================对账差错处理四个按钮具体功能开始============*/
	/**
	 * 平台单边解冻结算
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:platformSingleFreeze')")
	@RequestMapping(value="/platformThawSettle.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> platformThawSettle(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";
		boolean flag = true;
		boolean recordFlag = true;
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'平台单边'  
			if(!"PLATE_SINGLE".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","解冻失败，您勾选的第  【"+(i+1)+"】  条数据对账结果不为  '平台单边' ！");*/
			}
			if("thawSettle".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'对账冻结'
			if(!"checkForzen".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","解冻失败，您勾选的第  【"+(i+1)+"】  条数据状态不为  '对账冻结' ！");*/
			}
		}
		if(flag==false){
			msg.put("state",false);
			msg.put("msg","操作失败，请检查"+errMsg.substring(0,errMsg.length()-1)+"条数据是否符合条件！");
			log.info(msg.toString());
			return msg;
		}
		if (!recordFlag) {
			msg.put("state",false);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据中包含未记账的数据，不能进行差错处理！");
			log.info(msg.toString());
			return msg;
		}
		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					//System.out.println(params.get(paramsName));
					FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据findErrorTransInfoByDuiAccountDetail
					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getPlateOrderNo(), duiAccountDetail.getAcqEnname());
					MerchantInfo merInfo =  merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(transInfo.getAcqEnname());
					msg = fastCheckAccDetailService.platformSingleForDayCut(duiAccountDetail ,transInfo ,merInfo ,acqOrg) ;
					if(!(boolean)msg.get("state")){
						failedCount++;
					}else{
						succCount++;
					}
				}catch(Exception e){
					failedCount++;
//					e.printStackTrace();
					log.error("异常:",e);
				}
			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","平台单边解冻结算处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
		}
		log.info(msg.toString());
		return msg ;
	}


	/**
	 * 平台单边赔付商户（交易解冻）
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:platformSingleSettleToMerchant')")
	@RequestMapping(value="/platformPayment.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> platformPayment(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";
		boolean flag = true;
		boolean recordFlag = true;
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'平台单边'  
			if(!"PLATE_SINGLE".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","解冻失败，您勾选的第  【"+(i+1)+"】  条数据对账结果不为  '平台单边' ！");*/
			}
			if("platformPayment".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'对账冻结'
			if(!"checkForzen".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","解冻失败，您勾选的第  【"+(i+1)+"】  条数据状态不为  '对账冻结' ！");*/
			}
		}
		if(flag==false){
			msg.put("state",false);
			msg.put("msg","操作失败，请检查"+errMsg.substring(0,errMsg.length()-1)+"条数据是否符合条件！");
			log.info(msg.toString());
			return msg;
		}

		if (!recordFlag) {
			msg.put("state",false);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据中包含未记账的数据，不能进行差错处理！");
			log.info(msg.toString());
			return msg;
		}

		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					//System.out.println(params.get(paramsName));
					FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据
					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getPlateOrderNo(), duiAccountDetail.getAcqEnname());
					MerchantInfo merInfo =  merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(transInfo.getAcqEnname());
					msg = fastCheckAccDetailService.platformSingleSettleToMerchant(duiAccountDetail ,transInfo, merInfo, acqOrg) ;
					if(!(boolean)msg.get("state")){
						failedCount++;	
					}else{
						succCount++;
					}
				}catch(Exception e){
					failedCount++;
//					e.printStackTrace();
					log.error("异常:",e);
					/*msg.put("state",false);
					msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，平台单边赔付，解冻失败！原因是:"+e.getMessage());
					return msg ;*/
				}

			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}

			msg.put("msg","平台单边赔付商户处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
		}
		log.info(msg.toString());
		return msg;
	}

	@PreAuthorize("hasAuthority('fastErrorHandle:acqSingleSettleToMerchant')")
	@RequestMapping(value="/markToHandled.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> markToHandled(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		String paramsName ;
		int count = 0;
		String type = params.get("type");
		for(int i=0 ;i<params.size()-1 ;i++){

			paramsName = "selectId["+i+"]" ;
			if (params.get(paramsName) == null) {
				continue;
			}
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName));
			if ("accountFailed".equals(duiAccountDetail.getErrorHandleStatus())) {
				duiAccountDetail.setErrorMsg("上游单边，已标记为已处理");
				duiAccountDetail.setErrorHandleStatus(type);

				count += fastCheckAccDetailService.updateDuiAccount(duiAccountDetail);
			}
		}
		if (count > 0) {
			msg.put("state", true);
			msg.put("msg", "处理成功！");
			log.info(msg.toString());
		} else {
			msg.put("state", false);
			msg.put("msg", "处理失败！");
			log.info(msg.toString());
		}
		return msg;
	}



	/**
	 * 上游单边补记账结算商户
	 * @param m 	odel
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:acqSingleSettleToMerchant')")
	@RequestMapping(value="/acqSingleSettleToMerchant.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> acqSingleSettleToMerchant(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";
		boolean flag = true;
		boolean recordFlag = true;
		boolean accountFailedFlag = true;
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName));
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'上游单边'  或上游成功平台失败
			if(!"ACQ_SINGLE".equals(duiAccountDetail.getCheckAccountStatus()) && !"FAILED".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*	msg.put("state",false);
				msg.put("msg","上游单边补记账结算失败，您勾选的第  【"+(i+1)+"】  条数据对账结果不为  '上游单边' ！");*/
			}
			if("upstreamRecordAccount".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				//				return msg ;
			}
			if ("accountFailed".equals(duiAccountDetail.getErrorHandleStatus())) {
				//上游单边，平台没有这条记录，则提示错误
				errMsg += "【"+(i+1)+"】,";
				accountFailedFlag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'上游单边记存疑问'
			if(!"upstreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","上游单边补记账结算失败，您勾选的第  【"+(i+1)+"】  条数据交易处理标志不为  '上游单边记存疑问' ！");*/
			}
			if(duiAccountDetail.getPlateTransStatus()==null||!"FAILED".equals(duiAccountDetail.getPlateTransStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
		}
		if(flag==false){
			msg.put("state",false);
			msg.put("msg","操作失败，请检查"+errMsg.substring(0,errMsg.length()-1)+"条数据是否符合条件！");
			log.info(msg.toString());
			return msg;
		}
		if (!recordFlag) {
			msg.put("state",false);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据中包含未记账的数据，不能进行差错处理！");
			log.info(msg.toString());
			return msg;
		}
		if (!accountFailedFlag) {
			msg.put("state",false);
			msg.put("result", 1);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据上游单边，交易系统未查询到对应的交易，请确认是否标记为已处理！");
			log.info(msg.toString());
			return msg;
		}

		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					//System.out.println(params.get(paramsName));
					FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据
					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getAcqOrderNo(), duiAccountDetail.getAcqEnname());
					MerchantInfo merInfo =  merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(duiAccountDetail.getAcqEnname());
					msg = fastCheckAccDetailService.acqSingleSettleToMerchant(duiAccountDetail,transInfo,merInfo,acqOrg) ;

					if(!(boolean)msg.get("state")){
						failedCount++;	
						/*msg.put("state",false);
						msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边补记账结算失败");*/
					}else{
						succCount++;
					}

				}catch(Exception e){
					failedCount++;
//					e.printStackTrace();
					log.error("异常:",e);
					/*msg.put("state",false);
					msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边补记账结算失败");*/
				}

			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","上游单边补记账结算账户处理成功"+succCount+"条"+",处理失败"+failedCount+"条"+",请进行调账来扣减余额");
		}
		log.info(msg.toString());
		return msg ;
	}

	/**
	 * 上游单边退款给持卡人
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:acqSingleBackMoneyToOwner')")
	@RequestMapping(value="/acqSingleBackMoneyToOwner.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> acqSingleBackMoneyToOwner(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";
		boolean flag = true;
		boolean recordFlag = true;
		boolean accountFailedFlag = true;
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'上游单边'
			if(!"ACQ_SINGLE".equals(duiAccountDetail.getCheckAccountStatus()) && !"FAILED".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","上游单边退款给持卡人失败，您勾选的第  【"+(i+1)+"】  条数据对账结果不为  '上游单边' ！");*/
			}
			if("upstreamRefund".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","请勿重复操作，您勾选的第  【"+(i+1)+"】  条数据交易处理标志已为  '上游单边退款给持卡人' ！");*/
			}
			if ("accountFailed".equals(duiAccountDetail.getErrorHandleStatus())) {
				//上游单边，平台没有这条记录，则提示错误
				errMsg += "【"+(i+1)+"】,";
				accountFailedFlag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'上游单边记存疑问'
			if(!"upstreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","上游单边退款给持卡人失败，您勾选的第  【"+(i+1)+"】  条数据交易处理标志不为  '上游单边记存疑问' ！");*/
			}
		}
		if(flag==false){
			msg.put("state",false);
			msg.put("msg","操作失败，请检查"+errMsg.substring(0,errMsg.length()-1)+"条数据是否符合条件！");
			log.info(msg.toString());
			return msg;
		}
		if (!recordFlag) {
			msg.put("state",false);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据中包含未记账的数据，不能进行差错处理！");
			log.info(msg.toString());
			return msg;
		}
		if (!accountFailedFlag) {
			msg.put("state",false);
			msg.put("result", 1);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据上游单边，交易系统未查询到对应的交易，请确认是否标记为已处理！");
			log.info(msg.toString());
			return msg;
		}
		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					//System.out.println(params.get(paramsName));
					FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;


					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据
					//					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getAcqOrderNo(), duiAccountDetail.getAcqEnname());
					//					MerchantInfo merInfo =  merchantInfoService.findMerchantByUserId(transInfo.getMerchantNo());
					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(duiAccountDetail.getAcqEnname());
					msg = fastCheckAccDetailService.acqSingleBackMoneyToOwner(duiAccountDetail ,acqOrg) ;

					if(!(boolean)msg.get("state")){
						failedCount++;
						/*msg.put("state",false);
						msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边退款给持卡人失败");*/
					}else{
						succCount++;
					}

				}catch(Exception e){
					failedCount++;
//					e.printStackTrace();
					log.error("异常:",e);
					/*msg.put("state",false);
					msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边退款给持卡人失败");*/
				}
			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","上游单边退款给持卡人处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
		}
		log.info(msg.toString());
		return msg ;
	}

	/**
	 * 上游单边退款给持卡人
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('fastErrorHandle:acqSingleBackMoneyToOwner')")
	@RequestMapping(value="/acqSingleThaw.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> acqSingleThaw(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";
		boolean flag = true;
		boolean recordFlag = true;
		boolean accountFailedFlag = true;
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'上游单边'
			if(!"ACQ_SINGLE".equals(duiAccountDetail.getCheckAccountStatus()) && !"FAILED".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","上游单边退款给持卡人失败，您勾选的第  【"+(i+1)+"】  条数据对账结果不为  '上游单边' ！");*/
			}
			if("upstreamThaw".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","请勿重复操作，您勾选的第  【"+(i+1)+"】  条数据交易处理标志已为  '上游单边退款给持卡人' ！");*/
			}
			if ("accountFailed".equals(duiAccountDetail.getErrorHandleStatus())) {
				//上游单边，平台没有这条记录，则提示错误
				errMsg += "【"+(i+1)+"】,";
				accountFailedFlag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'上游单边记存疑问'
			if(!"upstreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
				/*msg.put("state",false);
				msg.put("msg","上游单边退款给持卡人失败，您勾选的第  【"+(i+1)+"】  条数据交易处理标志不为  '上游单边记存疑问' ！");*/
			}
		}
		if(flag==false){
			msg.put("state",false);
			msg.put("msg","操作失败，请检查"+errMsg.substring(0,errMsg.length()-1)+"条数据是否符合条件！");
			log.info(msg.toString());
			return msg;
		}
		if (!recordFlag) {
			msg.put("state",false);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据中包含未记账的数据，不能进行差错处理！");
			log.info(msg.toString());
			return msg;
		}
		if (!accountFailedFlag) {
			msg.put("state",false);
			msg.put("result", 1);
			msg.put("msg","操作失败，选择的"+errMsg.substring(0,errMsg.length()-1)+"条数据上游单边，交易系统未查询到对应的交易，请确认是否标记为已处理！");
			log.info(msg.toString());
			return msg;
		}
		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					//System.out.println(params.get(paramsName));
					FastCheckAccDetail duiAccountDetail = fastCheckAccDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(duiAccountDetail.getAcqEnname());
					msg = fastCheckAccDetailService.acqSingleThaw(duiAccountDetail ,acqOrg) ;

					if(!(boolean)msg.get("state")){
						failedCount++;
						/*msg.put("state",false);
						msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边退款给持卡人失败");*/
					}else{
						succCount++;
					}

				}catch(Exception e){
					failedCount++;
//					e.printStackTrace();
					log.error("异常:",e);
					/*msg.put("state",false);
					msg.put("msg","您勾选的第  【"+(i+1)+"】条数据，上游单边退款给持卡人失败");*/
				}
			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","上游单边退款给持卡人处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
		}
		log.info(msg.toString());
		return msg ;
	}

	/**
	 * 修改备注
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:query')")
	@RequestMapping(value = "/updateRemark.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> updateRemark(@ModelAttribute FastCheckAccDetail detail) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();

		int re = fastCheckAccDetailService.updateRemark(detail);
		if (re > 0) {
			msg.put("status", true);
			msg.put("msg", "修改备注成功");
		} else {
			msg.put("status", false);
			msg.put("msg", "修改备注失败");
		}
		log.info(msg.toString());
		return msg;
	}
}
