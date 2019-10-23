package cn.eeepay.boss.action;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.boss.util.*;
import cn.eeepay.boss.util.Constants;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.enums.ReverseStatus;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.nposp.MerchantCardInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.*;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.*;
import cn.eeepay.framework.util.UrlUtil;
import com.auth0.jwt.JWTSigner;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import jxl.read.biff.BiffException;
import jxl.read.biff.WorkbookParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出账管理 by zouruijin 
 * email rjzou@qq.com zrj@eeepay.cn 
 * 2016年5月18日16:49:01
 *
 */
@Controller
@RequestMapping(value = "/chuAccountAction")
public class ChuAccountAction {
	@Resource
	public SysDictService sysDictService;
	@Autowired
	private SysConfigService sysConfigService;
	@Resource
	public SettleTransferFileService settleTransferFileService;
	@Resource
	public DuiAccountBatchService duiAccountBatchService;
	@Resource
	public SettleTransferService settleTransferService;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public ChuAccountService chuAccountService;
	@Resource
	public OutAccountTaskService outAccountTaskService;
	@Resource
	public OutAccountTaskDetailService outAccountTaskDetailService;
	@Resource
	public OutBillService outBillService;
	@Resource
	public OutBillDetailService outBillDetailService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	public AcqOutBillService acqOutBillService;
	@Resource
	private MerchantCardInfoService merchantCardInfoService;
	@Resource
	private AcqOrgService acqOrgService;
	@Resource
	private MerchantInfoService merchantInfoService;
	@Resource
	private OutAccountServiceService outAccountServiceService;
	@Resource
	private OutAccountServiceRateService outAccountServiceRateService;
	@Resource
	private CurrencyService currencyService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	private TransImportInfoService transImportInfoService;
	@Resource
	public ExtTransInfoService extTransInfoService;
	@Resource
	private SystemInfoService systemInfoService;
	@Resource
	private OptLogsService optLogsService;
	@Resource
	private CollectionTransOrderService collectionTransOrderService;
	
	@Resource
	private SubOutBillDetailService subOutBillDetailService;
	
	@Resource
	private SubOutBillDetailLogsService subOutBillDetailLogsService;
	
	@Resource
	private DuiAccountDetailService duiAccountDetailService;
	@Resource
	public ChuAccountAssemblyOrParsing chuAccountAssemblyOrParsing;

	
	

	@Value("${accountApi.http.url}")  
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")  
	private String accountApiHttpSecret;

	@Value("${core2.http.url}")
	private String core2ApiHttpUrl;
	@Value("${core2.http.secret}")  
	private String core2HttpSecret;


	private static final Logger log = LoggerFactory
			.getLogger(ChuAccountAction.class);

	//
	@PreAuthorize("hasAuthority('chuAccountTasksManage:query')")
	@RequestMapping(value = "/toChuAccountTasksManage.do")
	public String toChuAccountTasksManage(ModelMap model,@RequestParam Map<String, String> params) throws Exception {
		
		List<SysDict> acqOrgList = null;
		List<SysDict> billStatusList = null;
		List<SysDict> outBillRangeList = null;
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
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			billStatusList = sysDictService.findSysDictGroup("bill_status");
			outBillRangeList  = sysDictService.findSysDictGroup("sys_out_bill_range");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("acqOrgList", acqOrgList);
		model.put("billStatusList", billStatusList);
		model.put("outBillRangeList", outBillRangeList);
		model.put("params", params);
		return "chuAccount/chuAccountTasksManage";
	}

	@PreAuthorize("hasAuthority('settleTransferFileUpload:query')")
	@RequestMapping(value = "/toSettleTransferFileUpload.do")
	public String toSettleTransferFileUpload(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> settleBankList = sysDictService
				.findSysDictGroup("settle_transfer_bank");
		model.put("settleBankList", settleBankList);

		return "chuAccount/settleTransferFileUpload";
	}

	// 上传模板
	@PreAuthorize("hasAuthority('settleTransferFileUpload:insert')")
	@ResponseBody
	@RequestMapping(value = "/settleTransferFileUpload.do", method = RequestMethod.POST)
	public Map<String, Object> settleTransferFileUpload(
			HttpServletRequest request,
			@RequestParam final Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		String settleBank = params.get("settleBank");
		String summary = params.get("summary");
		MultipartFile file = files.get(0);
		String fileName = file.getOriginalFilename();
		Boolean isResult = false;
		List<SettleTransferFile> stfList = null;
		FileInputStream in = null;
		File temp = null;
		List<String> errorList = new ArrayList<String>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		try {
			stfList = settleTransferFileService.findFileByFileName(fileName);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (stfList != null && stfList.size() > 0) {
			result.put("state", false);
			result.put("msg", "上传文件名已存在，请重新核对");
			log.info(result.toString());
			isResult = true;
		}
		try {
			temp = File.createTempFile("settleTransfer", ".xls");
			file.transferTo(temp);
			in = new FileInputStream(temp);
			String fileMd5 = Md5.getMd5ByFile(temp);
			stfList = settleTransferFileService.findFileByFileMD5(fileMd5);
			request.getSession().setAttribute("fileMd5", fileMd5);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if (stfList != null && stfList.size() > 0) {
			result.put("state", false);
			result.put("msg", "上传文件已存在，请重新核对");
			log.info(result.toString());
			isResult = true;
		}

		if (!isResult) {

			try {
				// 适用于 结算中心、中信银行、腾付通、民生银行、深圳银商
				String[] head = null;
				if ("hy".equalsIgnoreCase(settleBank)) {
					head = new String[] { "seqNo:string", "amount:number",
							"inSettleBankNo:string", "inAccNo:string",
							"inAccName:string", "inBankNo:string",
							"inBankName:string", "bak1:string", "bak2:string" };
				} else if ("zy".equals(settleBank)) {
					head = new String[] { "seqNo:string", "amount:number",
							"inAccNo:string", "inAccName:string",
							"inBankNo:string", "inBankName:string",
							"bak1:string", "bak2:string" };
				} else {
					head = new String[] { "seqNo:string", "amount:number",
							"inSettleBankNo:string", "inAccNo:string",
					"inAccName:string" };
				}

				ExcelParseUtil.parserExcel(in, head, list, errorList);

				int totalNum = list.size();
				int maxListSize = 3000;
				if ("cmbc_xm".equalsIgnoreCase(settleBank)
						|| "cmbc_xm_pos".equalsIgnoreCase(settleBank)) {
					maxListSize = 4000;
				} else if ("chinaums".equalsIgnoreCase(settleBank)) {
					maxListSize = 5000;
				} else if ("ecitic_proxy".equalsIgnoreCase(settleBank)) {
					maxListSize = 4000;
				} else if ("yse_batch_pay".equalsIgnoreCase(settleBank)) {
					maxListSize = 1000;
				}
				if (totalNum > maxListSize) {
					result.put("state", false);
					result.put("msg", "有效记录不能超过" + maxListSize + "条");
					log.info(result.toString());
					return result;
				}

				BigDecimal totalBigDecimal = new BigDecimal("0");
				for (Map<String, String> map : list) {
					BigDecimal amountBigDecimal = new BigDecimal(
							map.get("amount"));
					totalBigDecimal = totalBigDecimal.add(amountBigDecimal);

				}

				// model.addAttribute("totalNum", ""+totalNum);
				// model.addAttribute("totalAmount",
				// totalBigDecimal.toPlainString());
				request.getSession().setAttribute("settleBank", settleBank);
				request.getSession().setAttribute("excelList", list);
				request.getSession().setAttribute("totalNum", "" + totalNum);
				request.getSession().setAttribute("totalAmount",
						totalBigDecimal.toPlainString());
				request.getSession().setAttribute("summary", summary);
				request.getSession().setAttribute("fileName", fileName);

				String status = "";
				String msg = "";
				if (totalNum == 0) {
					status = "error";
					msg = "没有解析到有效的数据";
				} else if (totalNum > 0) {
					status = "parserSuccess";
					msg = "共有" + totalNum + "条转账数据，总计"
							+ totalBigDecimal.toPlainString() + "元";
				}
				// model.addAttribute("status", status);
				// model.addAttribute("msg", msg);
				result.put("state", true);
				result.put("msg", msg);
				log.info(result.toString());
			} catch (Exception e) {
				result.put("state", false);
				result.put("msg", "导入对账异常！");
				log.error(e.getMessage());
			}
		}
		return result;
	}

	@PreAuthorize("hasAuthority('settleTransferFileUpload:insert')")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "saveSettleTransFileUpload.do")
	@ResponseBody
	public Map<String, Object> saveSettleTransFileUpload(ModelMap model,
			HttpServletRequest request,
			@RequestParam final Map<String, String> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, String>> list = (List<Map<String, String>>) request
				.getSession().getAttribute("excelList");
		String summary = (String) request.getSession().getAttribute("summary");
		String fileName = (String) request.getSession()
				.getAttribute("fileName");
		String fileMd5 = (String) request.getSession().getAttribute("fileMd5");
		String totalNum = (String) request.getSession()
				.getAttribute("totalNum");
		String totalAmount = (String) request.getSession().getAttribute(
				"totalAmount");
		String settleBank = (String) request.getSession().getAttribute(
				"settleBank");

		params.put("summary", summary);
		params.put("settleBank", settleBank);
		model.put("params", params);
		List<SysDict> settleBankList = sysDictService
				.findSysDictGroup("settle_transfer_bank");
		// model.put("settleBankList", settleBankList);
		model.put("bankList", settleBankList);

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();

		// Map<String, String> settleParams=new HashMap<String, String>();
		// settleParams.put("fileName", fileName);
		// settleParams.put("fileMd5", fileMd5);
		// settleParams.put("operatorId", ""+ userInfo.getUserId());
		// settleParams.put("operatorName", userInfo.getUsername());
		// settleParams.put("totalNum", totalNum);
		// settleParams.put("totalAmount", totalAmount);
		// settleParams.put("settleBank", settleBank);

		SettleTransferFile stf = new SettleTransferFile();
		stf.setFileName(fileName);
		stf.setFileMd5(fileMd5);
		stf.setOperatorId(userInfo.getUserId());
		stf.setOperatorName(userInfo.getUsername());
		stf.setTotalNum(Integer.valueOf(totalNum));
		stf.setTotalAmount(new BigDecimal(totalAmount));
		stf.setSettleBank(settleBank);

		if ("szfs".equals(settleBank)) {
			stf.setOutAccNo("626306516");
			stf.setOutAccName("深圳市移付宝科技有限公司");
			stf.setOutBankNo("");
			stf.setOutBankName("中国民生银行深圳彩田支行");
			stf.setOutSettleBankNo("305584000002");
		} else if ("ecitic".equals(settleBank)) {
			stf.setOutAccNo("7328310182600000238");
			stf.setOutAccName("深圳市移付宝科技有限公司");
			stf.setOutBankNo("");
			stf.setOutBankName("中信银行沙河支行");
			stf.setOutSettleBankNo("302100011000");
		} else if ("tft".equals(settleBank)) {
			stf.setOutAccNo("12345678910");
			stf.setOutAccName("XXXXXXXX");
			stf.setOutBankNo("tft");
			stf.setOutBankName("腾付通");
			stf.setOutSettleBankNo("tft");
		} else if ("cmbc".equals(settleBank)) {
			stf.setOutAccNo("695121588");
			stf.setOutAccName("深圳市移付宝科技有限公司");
			stf.setOutBankNo("");
			stf.setOutBankName("");
			stf.setOutSettleBankNo("305100000013");
		} else if ("chinaums".equals(settleBank)) {
			stf.setOutAccNo("898440348160193");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutSettleBankNo("银联商务有限公司");
		} else if ("zy".equals(settleBank)) {
			stf.setOutAccNo("ZY_EEEPAY");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutSettleBankNo("中意代付");
		} else if ("hy".equalsIgnoreCase(settleBank)) {
			stf.setOutAccNo("HY_EEEPAY");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutSettleBankNo("翰亿代付");
		} else if ("cmbc_xm".equalsIgnoreCase(settleBank)) {
			stf.setOutAccNo("CMBC_XM_EEEPAY");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutSettleBankNo("民生银行（厦门）代付");
		} else if ("cmbc_xm_api".equalsIgnoreCase(settleBank)) {
			stf.setOutAccNo("693568719");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutBankNo("");
			stf.setOutBankName("");
			stf.setOutSettleBankNo("305100000013");
		} else if ("ecitic_proxy".equalsIgnoreCase(settleBank)) {
			stf.setOutAccNo("8110301014000024562");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutBankNo("");
			stf.setOutBankName("中信银行深圳市民中心支行");
			stf.setOutSettleBankNo("302100011000");
		} else if ("cmbc_xm_pos".equalsIgnoreCase(settleBank)) {
			stf.setOutAccNo("CMBC_XM_EEEPAY");
			stf.setOutAccName("深圳移付宝科技有限公司");
			stf.setOutSettleBankNo("民生银行（厦门）代付");
		} else if ("yse_batch_pay".equalsIgnoreCase(settleBank)) {
			SysDict sysDict1 = sysDictService.findSysDictByKeyName(
					"settle_bank_out_acc", "out_acc_no");
			SysDict sysDict2 = sysDictService.findSysDictByKeyName(
					"settle_bank_out_acc", "out_acc_name");
			String outAccNo = sysDict1.getSysValue();
			String outAccName = sysDict2.getSysValue();
			stf.setOutAccNo(outAccNo);
			stf.setOutAccName(outAccName);
			String outSettleBankNo = outAccNo;
			stf.setOutSettleBankNo(outSettleBankNo);

			// settleParams.put("outAccNo",merchantNo );
			// settleParams.put("outAccName", merchantName);
			// settleParams.put("outSettleBankNo", merchantNo);
		}

		// settleParams.put("summary", summary);
		stf.setSummary(summary);
		stf.setCreateTime(new Date());
		stf.setStatus("0");
		stf.setErrCode("");
		stf.setErrMsg("");
		stf.setBak1("");
		stf.setBak2("");

		result = settleTransferService.saveBatchSettleTransfer(stf, list,
				fileName, totalNum);
		return result;
	}

	/**
	 * 判断上传文件类型
	 * 
	 * @return
	 */
	private Boolean checkFileType(MultipartFile file) {
		String filename = file.getOriginalFilename();
		String extName = filename.substring(filename.lastIndexOf("."))
				.toLowerCase();
		if (!(".xls".equals(extName))) {
			return false;
		}
		return true;
	}

	/**
	 * 对账信息查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDuiAccountQuery.do")
	public String toDuiAccountQuery(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> acqOrgList = sysDictService
				.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);

		return "duiAccount/duiAccountQuery";
	}

	/**
	 * 对账信息明细查询
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDuiAccountDetailQuery.do")
	public String toDuiAccountDetailQuery(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> acqOrgList = sysDictService
				.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);

		return "duiAccount/duiAccountDetailQuery";
	}

	/**
	 * 对账差错处理
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toDuiAccountErrorHandle.do")
	public String toDuiAccountErrorHandle(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> acqOrgList = sysDictService
				.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);

		return "duiAccount/duiAccountErrorHandle";
	}

	public static String convertDateString(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf = new SimpleDateFormat("yyyyMMdd");
		Date dt = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		Date lastDate = cal.getTime();
		return longSdf.format(lastDate);
	}

	/**
	 * 跳转到 出账单管理 页面
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "/toChuAccountBillManage.do")
	public String chuAccountBillManage(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		// 查询所有 组织机构
		/*
		 * List<OrgInfo> orgInfo = orgInfoService.findOrgInfo() ;
		 * model.put("orgInfos", orgInfo);
		 */
		List<SysDict> acqOrgList = null;
		List<OutAccountService> outAccountServiceList = null;
        List<SysDict> outBillList = null;//出账范围列表
		
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
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
            outBillList = sysDictService.findSysDictGroup("sys_out_bill_range");
			outAccountServiceList = outAccountServiceService.findAllOutAccountServiceList();
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("acqOrgList", acqOrgList);
		model.put("outAccountServiceList", outAccountServiceList);
		model.put("outBillList", outBillList);
		model.put("params", params);
		return "chuAccount/chuAccountBillManage";
	}

	/**
	 * 出账单导出-上传FTP
	 * @param params
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('chuAccountBillManage:fileExport')")
	@RequestMapping(value = "billExport.do")
	@ResponseBody
	public Map<String, Object> billExport(@RequestParam Map<String, String> params,
			HttpServletResponse response, HttpServletRequest request)
					throws Exception {
		//获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		String uname = userInfo.getUsername();
		Map<String, Object> map = new HashMap<String, Object>();
		boolean result = false;
		boolean flag = true;
		String settleBank = params.get("settleBank");
		String filePath = request.getSession().getServletContext()
				.getRealPath("/")
				+ Constants.OUT_ACCOUNT_BILL_YINSHENG_TEMPLATE;

		Integer outBillId = Integer.parseInt(params.get("outBillId"));

		// 根据选择的出款通道和出账单id查询数据
		List<OutBillDetail> list = outBillDetailService.findByOutBillIdAndBank(
				outBillId, settleBank);
		if (!StringUtils.isNotBlank(settleBank)) {
			map.put("success", false);
			map.put("msg", "请选择出款通道");
			log.info(map.toString());
		} else if (list != null && list.size() > 0) {
			if("ZF_ZQ".equalsIgnoreCase(settleBank)){

				List<Map<String, String>> data = new ArrayList<>();
                Map<String,String> out = null;
                MerchantCardInfo card = null;
                StringBuilder detailIds = new StringBuilder();  //参数用来拼接每个excel文件导出的出账单明细id
                for(OutBillDetail item : list){
                    detailIds.append(item.getId()+",");//拼接ID
                    card = merchantCardInfoService.getByMerchantNo(item.getMerchantNo());
//					Map<String,Object> zfMer = merchantInfoService.queryQrMerInfo(item.getMerchantNo());
                    out = new HashMap<String,String>() ;
                    out.put("id", item.getId().toString()) ;
                    out.put("merchantName", card.getMerchantName()) ;
                    out.put("merchantNo", item.getAcqMerchantNo()) ;
                    out.put("bankName", StringUtils.isNotBlank(card.getBankName()) ? card.getBankName() : "") ;
                    out.put("cnapsNo", StringUtils.isNotBlank(card.getCnapsNo()) ? card.getCnapsNo() : "") ;
                    out.put("bankhb", "00") ;
                    out.put("accountName", StringUtils.isNotBlank(card.getAccountName()) ? card.getAccountName() : "") ;
                    out.put("accountNo", StringUtils.isNotBlank(card.getAccountNo()) ? card.getAccountNo() : "") ;
                    out.put("outAmount", item.getOutAccountTaskAmount()==null?"0.00":item.getOutAccountTaskAmount().toString()) ;
                    data.add(out) ;
                }
                int batchNo = getBatchNo(settleBank,"zfzq_serial");
                String fileName =  FileNameUtil.exportOutBill(settleBank,batchNo)+ ".xls";
                String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
                response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

                ListDataExcelExport export = new ListDataExcelExport();
				String[] cols = new String[]{"id","merchantName","merchantNo","bankName","cnapsNo","bankhb","accountName","accountNo","outAmount"};
				String[] colsName = new String[]{"序号","商户名","商户号","银行名称","银行行号","银行行别","结算人名称","结算人账号","出款金额"};
				double[] cellWidth = {3000,6000,5000,10000,5000,4000,4000,6000,4000};
                String zfzqPath = request.getSession().getServletContext().getRealPath("/") +"template"+File.separator+fileName;
                File file2 = new File(zfzqPath);
                OutputStream ouputStream = new FileOutputStream(file2);
                export.export(cols, colsName,cellWidth, data, ouputStream);
                SysConfig config =  sysConfigService.getByKey("zfzq_serial");
                FileInputStream input = new FileInputStream(file2);
                String today = FileNameUtil.getDateDic();
                result = FTPUtil.uploadFile(ConfigUtil.getConfig(Constants.FTP_IP),
                        Integer.parseInt(ConfigUtil
                                .getConfig(Constants.FTP_PORT)), ConfigUtil
                                .getConfig(Constants.FTP_USERNAME), ConfigUtil
                                .getConfig(Constants.FTP_PASSWORD),ConfigUtil
								.getConfig("ftp.zfzq_uploadDir")+today, fileName, input);
				ouputStream.close();
                //进行判断：如果数据异常，则直接退出循环
                if (result) {
                    //上传FTP成功，需要将序列号存入字典表
                    String dateSerial = today + "_" + batchNo;
                    config.setParamValue(dateSerial);
                    sysConfigService.update(config);

                    //更新导出的出账单明细数据状态为成功,导出序列号为 number  eg:001
                   String number = String.format("%03d", batchNo);
                    //进行更新
                    outBillDetailService.updateExportStatusAndSerial(1, number, detailIds.substring(0, detailIds.length() - 1));
					map.put("success", true);
					map.put("msg", "上传成功");

                }
			}else if(Constants.NEWEPTOK.equals(settleBank)){
				SysConfig config = null;
				int index = 0;
				int no = 0;
				String curDate = "";
				String dateSerial = "";
				String number = "";
				StringBuilder detailIds = new StringBuilder();  //参数用来拼接每个excel文件导出的出账单明细id
				StringBuilder fileNames = new StringBuilder();  //存储导出的文件名
				do {
					// 传递模板地址和要操作的页签
					ExcelExport excel = new HssExcelExport(filePath, 0);
					// 创建页脚，打印excel时显示页数
					excel.createFooter();

					// 插入行
					int startRow = 1;// 起始行
					int rows = (list.size() - index) >= 1000 ? 1000
							: (list.size() - index);// 插入行数

					HSSFSheet sheet = excel.getHssSheet();

					MerchantCardInfo card = null;
					String merchantNo = "";
					HSSFRow row = null;
					BigDecimal outAmount = null;
					String remark = "";
					for (int i = index; i < list.size() && startRow <= rows; i++) {
						detailIds.append(list.get(i).getId()+",");
						row = sheet.createRow(startRow);
						row.createCell(0).setCellValue(list.get(i).getId());
						merchantNo = list.get(i).getMerchantNo();
						remark = list.get(i).getChangeRemark();
						outAmount = list.get(i).getOutAccountTaskAmount();

						if (merchantNo == null || "".equals(merchantNo)) {
							flag = false;  //如果商户为空，则数据有异常，不进行导出
							break;
						} else {
							card = merchantCardInfoService
									.getByMerchantNo(merchantNo);
							if (card != null) {
								row.createCell(1).setCellValue(card.getAccountProvince() != null ? card.getAccountProvince() : "");
								row.createCell(2).setCellValue(card.getAccountCity() != null ? card.getAccountCity() : "");
								row.createCell(3).setCellValue(
										StringUtils.isNotBlank(card.getBankName()) ? card
												.getBankName() : "");
								row.createCell(4).setCellValue(
										StringUtils.isNotBlank(card.getAccountNo()) ? card
												.getAccountNo() : "");
								row.createCell(5).setCellValue(
										StringUtils.isNotBlank(card.getAccountName()) ? card
												.getAccountName() : "");
								row.createCell(6).setCellValue(
										outAmount != null ? outAmount
												.doubleValue() : 0);
								row.createCell(7).setCellValue(
										StringUtils.isNotBlank(card.getAccountType()) ? card
												.getAccountType() : "");
								row.createCell(8).setCellValue(
										StringUtils.isNotBlank(remark) ? remark : "代付");
								row.createCell(9).setCellValue(
										StringUtils.isNotBlank(card.getMobilephone()) ? card
												.getMobilephone() : "");
							}
						}
						startRow++;
					}

					//进行判断：如果数据异常，则直接退出循环
					if (!flag) {
						break;
					}

					//文件名生成：根据  '_' 截取日期和序列号，不是当天则从1开始，否则累加
					config = sysConfigService.getByKey("neweptok_serial");
					dateSerial = config.getParamValue(); //eg:20160830_1
					curDate = dateSerial.substring(0, dateSerial.indexOf('_'));  //20160830
					no = Integer.parseInt(dateSerial.substring(dateSerial.indexOf('_')+1));  //1

					String path = FileNameUtil.getDateDic();  //获取当天日期 eg:20160830
					if (path.equals(curDate)) {
						//当天
						no++;
					} else {
						//不是当天
						no = 1;
					}
					String fileName = FileNameUtil.exportOutBill(settleBank, no)
							+ ".xls";
					fileNames.append(fileName+";");

					ByteArrayOutputStream os = new ByteArrayOutputStream();
					excel.getHssWb().write(os);
					ByteArrayInputStream ios = new ByteArrayInputStream(
							os.toByteArray());
					result = FTPUtil.uploadFile(ConfigUtil.getConfig(Constants.FTP_IP),
							Integer.parseInt(ConfigUtil
									.getConfig(Constants.FTP_PORT)), ConfigUtil
									.getConfig(Constants.FTP_USERNAME), ConfigUtil
									.getConfig(Constants.FTP_PASSWORD),
							ConfigUtil.getConfig(Constants.FTP_NEWEPTOK_UPLOAD_DIR)
									+ path, fileName, ios);

					index += 100;
					if (result) {
						//上传FTP成功，需要将序列号存入字典表
						dateSerial = path + "_" + no;
						config.setParamValue(dateSerial);
						sysConfigService.update(config);

						//更新导出的出账单明细数据状态为成功,导出序列号为 number  eg:001
						number = String.format("%03d", no);
						//进行更新
						outBillDetailService.updateExportStatusAndSerial(1, number, detailIds.substring(0, detailIds.length() - 1));

					}
					detailIds.setLength(0);  //清空stringbuilder
				} while (list.size() > index);
				fileNames.setLength(fileNames.length() - 1);
				outBillService.updateExportFileName(outBillId, fileNames.toString(), uname);
				if (result) {
					map.put("success", true);
					map.put("msg", "上传成功");
					log.info(map.toString());
				} else {
					map.put("success", false);
					map.put("msg", "上传失败");
					log.info(map.toString());
				}
			}else if("ZFYL_ZQ".equalsIgnoreCase(settleBank)){

				List<Map<String, String>> data = new ArrayList<>();
				Map<String,String> out = null;
				MerchantCardInfo card = null;
				StringBuilder detailIds = new StringBuilder();  //参数用来拼接每个excel文件导出的出账单明细id
				for(OutBillDetail item : list){
					detailIds.append(item.getId()+",");//拼接ID
					card = merchantCardInfoService.getByMerchantNo(item.getMerchantNo());
//					Map<String,Object> zfMer = merchantInfoService.queryQrMerInfo(item.getMerchantNo());
					out = new HashMap<String,String>() ;
					out.put("id", item.getId().toString()) ;
					out.put("merchantName", card.getMerchantName()) ;
					out.put("merchantNo", item.getAcqMerchantNo()) ;
					out.put("bankName", StringUtils.isNotBlank(card.getBankName()) ? card.getBankName() : "") ;
					out.put("cnapsNo", StringUtils.isNotBlank(card.getCnapsNo()) ? card.getCnapsNo() : "") ;
					out.put("bankhb", "00") ;
					out.put("accountName", StringUtils.isNotBlank(card.getAccountName()) ? card.getAccountName() : "") ;
					out.put("accountNo", StringUtils.isNotBlank(card.getAccountNo()) ? card.getAccountNo() : "") ;
					out.put("outAmount", item.getOutAccountTaskAmount()==null?"0.00":item.getOutAccountTaskAmount().toString()) ;
					data.add(out) ;
				}
				int batchNo = getBatchNo(settleBank,"zfylzq_serial");
				String fileName =  FileNameUtil.exportOutBill(settleBank,batchNo)+ ".xls";
				String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
				response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);

				ListDataExcelExport export = new ListDataExcelExport();
				String[] cols = new String[]{"id","merchantName","merchantNo","bankName","cnapsNo","bankhb","accountName","accountNo","outAmount"};
				String[] colsName = new String[]{"序号","商户名","商户号","银行名称","银行行号","银行行别","结算人名称","结算人账号","出款金额"};
				double[] cellWidth = {3000,6000,5000,10000,5000,4000,4000,6000,4000};
				String zfzqPath = request.getSession().getServletContext().getRealPath("/") +"template"+File.separator+fileName;
				File file2 = new File(zfzqPath);
				OutputStream ouputStream = new FileOutputStream(file2);
				export.export(cols, colsName,cellWidth, data, ouputStream);
				SysConfig config =  sysConfigService.getByKey("zfylzq_serial");
				FileInputStream input = new FileInputStream(file2);
				String today = FileNameUtil.getDateDic();
				result = FTPUtil.uploadFile(ConfigUtil.getConfig(Constants.FTP_IP),
						Integer.parseInt(ConfigUtil
								.getConfig(Constants.FTP_PORT)), ConfigUtil
								.getConfig(Constants.FTP_USERNAME), ConfigUtil
								.getConfig(Constants.FTP_PASSWORD),ConfigUtil
								.getConfig("ftp.zfylzq_uploadDir")+today, fileName, input);
				ouputStream.close();
				//进行判断：如果数据异常，则直接退出循环
				if (result) {
					//上传FTP成功，需要将序列号存入字典表
					String dateSerial = today + "_" + batchNo;
					config.setParamValue(dateSerial);
					sysConfigService.update(config);

					//更新导出的出账单明细数据状态为成功,导出序列号为 number  eg:001
					String number = String.format("%03d", batchNo);
					//进行更新
					outBillDetailService.updateExportStatusAndSerial(1, number, detailIds.substring(0, detailIds.length() - 1));
					map.put("success", true);
					map.put("msg", "上传成功");

				}
			}

		} else {
			map.put("success", false);
			map.put("msg", "无可用数据可以导出出账单，请确认是否已经导出或者记账是否成功");
			log.info(map.toString());
		}
		return map;
	}
    public int getBatchNo(String settleBank,String sysParams){
        //文件名生成：根据  '_' 截取日期和序列号，不是当天则从1开始，否则累加
        SysConfig config = sysConfigService.getByKey(sysParams);
        String  dateSerial = config.getParamValue(); //eg:20160830_1
        String curDate = dateSerial.substring(0, dateSerial.indexOf('_'));  //20160830
        int no = Integer.parseInt(dateSerial.substring(dateSerial.indexOf('_')+1));  //1
        String path = FileNameUtil.getDateDic();  //获取当天日期 eg:20160830
        if (path.equals(curDate)) {
            //当天
            no++;
        } else {
            //不是当天
            no = 1;
        }
        return no;

    }

	@RequestMapping(value = "/yltest.do")
	public void yltest() {
		String path = FileNameUtil.getDateDic();
		FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
				Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
				ConfigUtil.getConfig(Constants.FTP_USERNAME),
				ConfigUtil.getConfig(Constants.FTP_PASSWORD),
				ConfigUtil.getConfig(Constants.FTP_NEWEPTOK_UPLOAD_DIR) + path,
				"*", "D:/");
	}

	/**
	 * 跳转到 商户出账结果查询 页面
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('merchantChuAccountResult:query')")
	@RequestMapping(value = "/toMerchantChuAccountResult.do")
	public String merchantChuAccountResult(@ModelAttribute("subOutBillDetail")SubOutBillDetail subOutBillDetail,ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		// 查询所有 组织机构
		List<AcqOrg> acqOrgList = acqOrgService.findAllAcqOrg();
		model.put("acqOrgList", acqOrgList);

		List<SysDict> acqOrgs = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgs", acqOrgs);
		
		List<SysDict> outBillStatusList = sysDictService.findSysDictGroup("out_bill_status");
		model.put("outBillStatusList", outBillStatusList);

		List<SysDict> isAddBillList = sysDictService.findSysDictGroup("is_add_bill");
		model.put("isAddBillList", isAddBillList);
		
		//找出所有没有出账的出账单,供判断加入出账单所需要
		List<OutBill> allNoOutBillIdList =  outBillService.findAllNoOutBillId();
		model.put("allNoOutBillIdList", allNoOutBillIdList);
		model.put("selectIds", subOutBillDetail.getSelectIds());

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String currenDate = sdf.format(new Date());
		String date1 = DateUtil.subDayFormatLong(new Date(),7);
		model.put("date1",date1);
		model.put("date2",currenDate);

		return "chuAccount/merchantChuAccountResult";
	}


	
	/**
	 * 失败后再次加入出账单
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('merchantChuAccountResult:addOutBillToAlready')")
	@RequestMapping(value="/addOutBillToAlready.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addOutBillToAlready(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();
		String outBillId = params.get("outBillId");
		String subAcqOrg = params.get("subAcqOrg");//勾选的所有渠道
		OutBill outbill = outBillService.findOutBillTaskById(Integer.parseInt(outBillId));
		
		if(!StringUtil.isBlank(subAcqOrg)){
			if(!subAcqOrg.equals(outbill.getAcqEnname())){
				msg.put("status", false);
				msg.put("msg", "所选交易出款通道跟出账单通道不一致！");
				return msg;
			}
		}
		
		String range = outbill.getOutBillRange();//出账范围
		if("ZF_ZQ".equalsIgnoreCase(outbill.getAcqEnname()) || "YS_ZQ".equalsIgnoreCase(outbill.getAcqEnname())
				|| "SFT_ZQ".equalsIgnoreCase(outbill.getAcqEnname())){//如果选择的出账单是中付或者银盛直清，则只能中付或者银盛的才可以走
			if (subAcqOrg.equalsIgnoreCase("ZF_ZQ") || subAcqOrg.equalsIgnoreCase("YS_ZQ")
					|| subAcqOrg.equalsIgnoreCase("SFT_ZQ")){
				if(!outbill.getAcqEnname().equals(subAcqOrg)){
					msg.put("status", false);
					msg.put("msg", "出账单通道不符，请检查！");
					return msg;
				}
				for(int i=0 ;i<params.size() ;i++) {
					String paramsName = "selectId[" + i + "]";
					SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
					if(StringUtils.isNotBlank(params.get(paramsName))){
						subOutBillDetail.setId(params.get(paramsName));
						SubOutBillDetail dbsubOutBillDetail = subOutBillDetailService.queryOutBillDetailById(subOutBillDetail);
	                    Map<String,Object> zfMer = merchantInfoService.queryQrMerchantInfo(dbsubOutBillDetail.getAcqMerchantNo());
						if(!"1".equals(zfMer.get("sync_status"))){
	                        msg.put("status", false);
	                        msg.put("msg", "该商户未与上游同步状态："+dbsubOutBillDetail.getAcqMerchantNo()+"|"+subOutBillDetail.getMerchantNo());
	                        return msg;
	                    }
						
						if("ZF_ZQ".equalsIgnoreCase(outbill.getAcqEnname())){
							if("T1".equals(range)){
								if(!DateUtil.isZfTomorrow(dbsubOutBillDetail.getTransTime())){
									msg.put("status", false);
									msg.put("msg", "所选账单不符合T1出账条件："+paramsName);
									return msg;
								}
							}else if("Tn".equals(range)){
								if(DateUtil.isZfTomorrow(dbsubOutBillDetail.getTransTime())){
									msg.put("status", false);
									msg.put("msg", "所选账单不符合Tn出账条件："+paramsName);
									return msg;
								}
							}	
						}
						
					 }
				}
			}else{
				msg.put("status", false);
				msg.put("msg", "其他通道的交易不允许加入直清通道出款");
				return msg;
			}
		}else{
			if(subAcqOrg.contains("ZF_ZQ") ||subAcqOrg.contains("YS_ZQ") || subAcqOrg.contains("SFT_ZQ")){
				msg.put("status", false);
				msg.put("msg", "直清通道的交易不允许加入其他通道出款");
				return msg;
			}
		}




		Map<String,Object> judgemsg=new HashMap<>();
		List<String> subDetailIdList = new ArrayList<String>();
		BigDecimal subTotal = new BigDecimal("0.00");
		BigDecimal outAccountTaskAmount = new BigDecimal("0.00");
		BigDecimal upBalance = new BigDecimal("0.00");//结算中金额减去计划出账金额
		String paramsName = "";
		//先将所选择的出账单的交易金额汇总，然后去跟上游结算中金额去对比
		for(int i=0 ;i<params.size() ;i++){
			paramsName = "selectId["+i+"]" ;
			SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
			if(StringUtils.isNotBlank(params.get(paramsName))){
				subOutBillDetail.setId(params.get(paramsName));
		    	subDetailIdList.add(params.get(paramsName));
		    	SubOutBillDetail dbsubOutBillDetail = subOutBillDetailService.queryOutBillDetailById(subOutBillDetail);
		    	outAccountTaskAmount = dbsubOutBillDetail.getOutAccountTaskAmount();
		    	subTotal = subTotal.add(outAccountTaskAmount);
			}

		}

		List<AcqOutBill>  acqOutBillList = new ArrayList<AcqOutBill>();
		acqOutBillList = acqOutBillService.findByOutBillId(new Integer(outBillId));
		for (AcqOutBill acqOutBill : acqOutBillList) {
			upBalance = upBalance.add(acqOutBill.getUpBalance().subtract(acqOutBill.getCalcOutAmount()));
		}
		if (upBalance.compareTo(subTotal) >= 0) {//加入出账单之后需要更新  对象详情中  该交易的 状态
			try {
				judgemsg = subOutBillDetailService.judgeIsAddSubOutDetail(subDetailIdList, new Integer(outBillId));
				if((boolean) judgemsg.get("status")){
					subOutBillDetailService.updateSubOutDetaiAndCheckOutDetail(subDetailIdList, subTotal, new Integer(outBillId));
				}else{
					return judgemsg;
				}
			} catch (Exception e) {
				log.error("加入出账单异常：" + e);
				msg.put("status", false);
				msg.put("msg", "加入该出账单异常！");
			}
			msg.put("status", true);
			msg.put("msg", "加入该出账单成功！");
		}else{
			msg.put("status", false);
			msg.put("msg", "加入该出账单失败！该出账单余额不足！");
		}
		return msg;
		
	}

	
	/**
	 * 跳转到 商户出款失败记录查询 页面
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('errorMerchantChuAccountResult:query')")
	@RequestMapping(value = "/toErrorMerchantChuAccountResult.do")
	public String errorMerchantChuAccountResult(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		// 查询所有 组织机构
		List<AcqOrg> acqOrgList = acqOrgService.findAllAcqOrg();
		model.put("acqOrgList", acqOrgList);

		List<SysDict> acqOrgs = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgs", acqOrgs);
		
		List<SysDict> outBillStatusList = sysDictService.findSysDictGroup("out_bill_status");
		model.put("outBillStatusList", outBillStatusList);

		List<SysDict> isAddBillList = sysDictService.findSysDictGroup("is_add_bill");
		model.put("isAddBillList", isAddBillList);
		return "chuAccount/errorMerchantChuAccountResult";
	}
	
	
	/**
	 * 跳转到 结算转账查询 页面
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('settleTransferQuery:query')")
	@RequestMapping(value = "/toSettleTransferQuery.do")
	public String settleTransferQuery(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> settleBankList = sysDictService
				.findSysDictGroup("settle_transfer_bank");
		List<SysDict> settleStatusList = sysDictService
				.findSysDictGroup("settle_transfer_status");
		model.put("settleBankList", settleBankList);
		model.put("settleStatusList", settleStatusList);

		return "chuAccount/settleTransferQuery";
	}

	/**
	 * 结算转账 查询
	 * 
	 * @param settleTransferFile
	 * @param sort
	 * @param page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('settleTransferQuery:query')")
	@RequestMapping(value = "/findSettleTransferQueryList.do")
	@ResponseBody
	public Page<SettleTransferFile> findSettleTransferQueryList(
			@ModelAttribute SettleTransferFile settleTransferFile,
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<SettleTransferFile> page,
			HttpServletRequest request) {
		// settleTransferFile.getCreateTime() ;
		try {
			String createTime1 = params.get("start");
			String createTime2 = params.get("end");
			if (StringUtils.isNotBlank(createTime1)) {
				createTime1 += " 00:00:00";
				settleTransferFile.setCreateTime1(createTime1);
			}
			if (StringUtils.isNotBlank(createTime2)) {
				createTime2 += " 23:59:59";
				settleTransferFile.setCreateTime2(createTime2);
			}
			List<SettleTransferFile> list = chuAccountService
					.findSettleTransferFileList(settleTransferFile, sort, page);
			log.info(list.toString());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	/**
	 * 处理日期类型的转换问题
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(
				dateFormat, true));
	}

	/**
	 * 结算转账详情
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('settleTransferQuery:query')")
	@RequestMapping(value = "/toSettleTransferDetail.do")
	public String settleTransferDetail(ModelMap model,
			@RequestParam(value = "fileId", required = true)Integer id) throws Exception {
		SettleTransferFile settleTransferFile = chuAccountService
				.findSettleTransferFileById(id);
		model.put("settleTransferFile", settleTransferFile);

		List<SysDict> settleTransferStatusList = sysDictService
				.findSysDictGroup("settle_transfer_status");
		model.put("settleTransferStatusList", settleTransferStatusList);

		model.put("fileId", id);
		return "chuAccount/settleTransferDetail";
	}

	/**
	 * 查询所有 出账任务
	 * 
	 * @param sort
	 * @param page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('chuAccountTasksManage:query')")
	@RequestMapping(value = "/findOutAccountTaskList.do")
	@ResponseBody
	public Page<OutAccountTask> findOutAccountTaskList(
			@RequestParam Map<String, Object> param,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OutAccountTask> page,
			HttpServletRequest request) {;
		try {
			List<OutAccountTask> list = outAccountTaskService
					.findOutAccountTaskList(param, sort, page);
			List<OutAccountTaskDetail> outAccountTaskDetailList = null;
			String currencyNo = "";
			String accountType = "Acq";
			String userId = "";  //收单机构id
			String accountOwner = "";  //机构组织id
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();
			if (orgList != null && orgList.size() > 0) {
				accountOwner = orgList.get(0).getOrgNo();
			}
			
			for (OutAccountTask task : list) {
                String acqName = StringUtil.filterNull(task.getAcqEnname());
                String tTime = "2016-11-10";
                String dayP1 = "2016-11-10";
                if(task.getTransTime()!=null){
                    tTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",task.getTransTime());
                    dayP1 = DateUtil.getFormatDate("yyyy-MM-dd",task.getTransTime());
                }
                OutAccountTask ta = outBillService.findTransTime(acqName,tTime);
                String dayP2 = "2016-11-10";
                if(ta!=null&&ta.getTransTime()!=null){
                    dayP2 = DateUtil.getFormatDate("yyyy-MM-dd",ta.getTransTime());
                }
                task.setDayPhase(dayP2+"~"+dayP1);
				BigDecimal upBalance = BigDecimal.ZERO;
				if (task.getBillStatus() == 0) {
					outAccountTaskDetailList = outAccountTaskDetailService
							.findOutAccountTaskDetailByTaskId(task.getId());
					for (OutAccountTaskDetail detail : outAccountTaskDetailList) {
						AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
						userId = acqOrg.getId().toString();
						ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
						ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
						upBalance = upBalance.add(extAccount.getSettlingAmount());
					}
					task.setUpBalance(upBalance);
				}
			}
			page.setResult(list);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	/**
	 * 跳转到 详情
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('chuAccountTasksManage:query')")
	@RequestMapping(value = "/toOutAccountTaskDetail.do")
	public String outAccountTaskDetail(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		Integer id = Integer.valueOf(params.get("id"));
		OutAccountTask outAccountTask = outAccountTaskService
				.findOutAccountTaskById(id);
		List<OutAccountTaskDetail> outAccountTaskDetailList = outAccountTaskDetailService
				.findOutAccountTaskDetailByTaskId(id);

		BigDecimal upBalance = BigDecimal.ZERO;
		String currencyNo = "";
		String accountType = "Acq";
		String userId = "";  //收单机构id
		String accountOwner = "";  //机构组织id
		if (outAccountTask.getBillStatus() == 0) {
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();
			if (orgList != null && orgList.size() > 0) {
				accountOwner = orgList.get(0).getOrgNo();
			}
			for (OutAccountTaskDetail detail : outAccountTaskDetailList) {
				AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
				userId = acqOrg.getId().toString();
				ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
				ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
				detail.setUpBalance(extAccount.getSettlingAmount());
				upBalance = upBalance.add(extAccount.getSettlingAmount());
			}
			outAccountTask.setUpBalance(upBalance);
		}

		model.put("outAccountTask", outAccountTask);
		model.put("outAccountTaskDetailList", outAccountTaskDetailList);
		model.put("params", params);
		return "chuAccount/outAccountTaskDetail";
	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "/toOutAccountTaskUpdate.do")
	public String toOutAccountTaskUpdate(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		Integer id = Integer.valueOf(params.get("id"));

		//判断该出账单是否已经生成，bill_status为1 已经生成，则直接取out_account_task, 为0则查询当前上游的结算中金额
		OutAccountTask outAccountTask = outAccountTaskService
				.findOutAccountTaskById(id);

		List<OutAccountTaskDetail> outAccountTaskDetailList = outAccountTaskDetailService
				.findOutAccountTaskDetailByTaskId(id);

		BigDecimal upBalance = BigDecimal.ZERO;
		String currencyNo = "";
		String accountType = "Acq";
		String userId = "";  //收单机构id
		String accountOwner = "";  //机构组织id
		if (outAccountTask.getBillStatus() == 0) {
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();
			if (orgList != null && orgList.size() > 0) {
				accountOwner = orgList.get(0).getOrgNo();
			}
			for (OutAccountTaskDetail detail : outAccountTaskDetailList) {
				AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
				userId = acqOrg.getId().toString();
				ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
				ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
				detail.setUpBalance(extAccount.getSettlingAmount());
				upBalance = upBalance.add(extAccount.getSettlingAmount());
			}
			outAccountTask.setUpBalance(upBalance);
		}

		model.put("outAccountTask", outAccountTask);
		model.put("outAccountTaskDetailList", outAccountTaskDetailList);
		List<SysDict> acqOrgList = null;
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		} catch (Exception e) {

		}
		model.put("acqOrgList", acqOrgList);
		model.put("params", params);
		return "chuAccount/outAccountTaskUpdate";
	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "findOutAccountTaskUpdateList.do")
	@ResponseBody
	public Page<OutAccountTaskDetail> findOutAccountTaskUpdateList(
			@ModelAttribute OutAccountTaskDetail outAccountTaskDetail,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OutAccountTaskDetail> page) {
		try {
			List<OutAccountTaskDetail> outAccountTaskDetailList = outAccountTaskDetailService.findOutAccountTaskUpdateList(
					outAccountTaskDetail, sort, page);
			OutAccountTask outAccountTask = outAccountTaskService.findOutAccountTaskById(outAccountTaskDetail.getOutAccountTaskId());
			BigDecimal upBalance = BigDecimal.ZERO;
			String currencyNo = "";
			String accountType = "Acq";
			String userId = "";  //收单机构id
			String accountOwner = "";  //机构组织id
			if (outAccountTask.getBillStatus() == 0) {
				List<Currency> currencyList = currencyService.findCurrency();
				if (currencyList != null && currencyList.size() > 0) {
					currencyNo = currencyList.get(0).getCurrencyNo();
				}
				List<OrgInfo> orgList = orgInfoService.findOrgInfo();
				if (orgList != null && orgList.size() > 0) {
					accountOwner = orgList.get(0).getOrgNo();
				}
				for (OutAccountTaskDetail detail : outAccountTaskDetailList) {
					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
					userId = acqOrg.getId().toString();
					ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
					ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
					detail.setUpBalance(extAccount.getSettlingAmount());
					upBalance = upBalance.add(extAccount.getSettlingAmount());
				}
			}
			page.setResult(outAccountTaskDetailList);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "/toOutAccountTaskAdd.do")
	public String toOutAccountTaskAdd(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> acqOrgList = null;
		SystemInfo sysInfo = null;
		List<SysDict> outBillRangeList = null;
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			outBillRangeList = sysDictService.findSysDictGroup("sys_out_bill_range");
			sysInfo = systemInfoService.findSystemInfoByCurrentDate(DateUtil.getCurrentDate());
		} catch (Exception e) {

		}
		model.put("sysInfo", sysInfo);
		model.put("acqOrgList", acqOrgList);
		model.put("outBillRangeList", outBillRangeList);
		return "chuAccount/chuAccountTaskAdd";
	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "/calcSettlingAmountByParam.do")
	@ResponseBody
	public Map<String, Object> calcSettlingAmountByParam(@RequestParam String acqEnname, @RequestParam String transTime, @RequestParam String outBillRange) {
		Map<String, Object> data = new HashMap<>();
		//查询当天是否含有未出账的出账单
		List<OutAccountTask> taskList = outAccountTaskService.findByTransTimeAndAcqEnname(DateUtil.parseDateTime(transTime), acqEnname, outBillRange);
		if (taskList != null && !taskList.isEmpty()) {
			Integer taskId = -1;
			OutBill outBill = null;
			for (OutAccountTask task : taskList) {
				taskId = task.getId();
				outBill = outBillService.findOutBillByTaskId(taskId);
				if (outBill == null){
					data.put("success", false);
					data.put("msg", "该通道存在未生成出账单的任务，请先处理完成");
					log.info(data.toString());
					return data;
				}
			}
		}
		//查询该上游当前出账任务总金额
		BigDecimal outAccountTaskAmount = outAccountTaskService.calcOutAccountTaskAmountByAcqEnname(acqEnname, DateUtil.parseDateTime(transTime));
		outAccountTaskAmount = outAccountTaskAmount == null ? BigDecimal.ZERO : outAccountTaskAmount;
		BigDecimal upBalance = BigDecimal.ZERO;
		//计算上游当前结算中金额
		try {
			String currencyNo = "";
			String accountType = "Acq";
			String userId = "";  //收单机构id
			String accountOwner = "";  //机构组织id
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();
			if (orgList != null && orgList.size() > 0) {
				accountOwner = orgList.get(0).getOrgNo();
			}
			AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(acqEnname);
			userId = acqOrg.getId().toString();
			ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
			ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
			upBalance = upBalance.add(extAccount.getSettlingAmount());

			upBalance = upBalance.subtract(outAccountTaskAmount);
		} catch (Exception e) {
			data.put("success", false);
			data.put("msg", "计算上游结算中金额异常");
			log.info(data.toString());
			return data;
		}
		data.put("success", true);
		data.put("upBalance", upBalance);
		log.info(data.toString());
		return data;
	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "/confirmAddTask.do")
	@ResponseBody
	public Map<String, Object> confirmAddTask(@ModelAttribute OutAccountTask task) {
		Map<String, Object> data = new HashMap<>();
		if (task.getOutAccountTaskAmount().compareTo(BigDecimal.ZERO) <= 0) {
			data.put("success", false);
			data.put("msg", "出账任务金额必须大于零");
			log.info(data.toString());
			return data;
		}
		if (task.getOutAccountTaskAmount().compareTo(task.getUpBalance()) > 0) {
			data.put("success", false);
			data.put("msg", "出账任务金额不能大于剩余上游结算中金额");
			log.info(data.toString());
			return data;
		}
		synchronized(this){
		//查询当天是否含有未出账的出账单
		List<OutAccountTask> taskList = outAccountTaskService.findByTransTimeAndAcqEnname(task.getTransTime(), task.getAcqEnname(), task.getOutBillRange());
				if (taskList == null || taskList.isEmpty()) {
					data.put("success", false);
					data.put("msg", "该出账任务暂不支持新增");
					log.info(data.toString());
					return data;
				}
		try {
			
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();

			String accountType = "Acq";
			String userId = "";  //收单机构id
			String acqEName = ""; //收单机构名称
			String accountOwner = "";  //机构组织id
			String cardNo = "";   //卡号
			String subjectNo = "122103";  //科目号122103
			String currencyNo = "RMB";
			ExtAccountInfo extAccountInfo = null;
			ExtAccount extAccount = null;
			BigDecimal money = new BigDecimal(0);
			BigDecimal transMoney = new BigDecimal(0);

			//统计金额信息
			BigDecimal upTodayBalance = new BigDecimal(0);
			BigDecimal transAmount = new BigDecimal(0);

			OutAccountTaskDetail taskDetail = null;

			Date now = new Date();

			int i=0,j=0;
			//币种号
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			AcqOrg acq = acqOrgService.findAcqOrgByAcqEnname(task.getAcqEnname());
			userId = acq.getId().toString();
			acqEName = acq.getAcqEnname();
			accountOwner = orgList.get(0).getOrgNo();

			taskDetail = new OutAccountTaskDetail();
			taskDetail.setCreateTime(now);
			taskDetail.setSysTime(now);

			taskDetail.setAcqOrgNo(task.getAcqEnname());
			taskDetail.setUpBalance(task.getUpBalance());
			taskDetail.setOutAccountAmount(task.getOutAccountTaskAmount());

			extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, cardNo, subjectNo, currencyNo);
			extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
			money = extTransInfoService.countTransMoney(extAccount.getAccountNo(), DateUtil.getBeforeDate(task.getTransTime()));
			taskDetail.setTodayBalance(money == null ? new BigDecimal(0) : money);
			//8. 根据收单机构id，冲销交易标志NORMAL，冲销状态正常NORMAL，上一个记账日期查询记账交易流水表
			transMoney = transImportInfoService.countByParam(acqEName, ReverseFlag.NORMAL.toString(), ReverseStatus.NORMAL.toString(), DateUtil.getBeforeDate(task.getTransTime()));
			taskDetail.setTodayAmount(transMoney == null ? new BigDecimal(0) : transMoney);

			upTodayBalance = upTodayBalance.add(money == null ? new BigDecimal(0) : money);  //上游当日余额
			transAmount = transAmount.add(transMoney == null ? new BigDecimal(0) : transMoney);  //累加交易金额

			//9. 创建出账任务
			if (taskDetail != null) {
				//获取到登录者信息
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				task.setCreator(userInfo.getUsername());//创建者
				task.setCreateTime(now);
				task.setTransAmount(transAmount);
				task.setUpTodayBalance(upTodayBalance);
				task.setUpCompanyCount(1);
				task.setSysTime(now);
				i = outAccountTaskService.insertOrUpdate(task);
				taskDetail.setOutAccountTaskId(task.getId());
				//需要查询之前有没有该出账任务的明细，有则删除
				outAccountTaskDetailService.deleteByTaskId(task.getId());
			}
			//10. 创建出账任务详细
			j = outAccountTaskDetailService.insert(taskDetail);

			if (i > 0 && j > 0) {
				data.put("success", true);
				data.put("msg", "新增出账任务成功");
				log.info(data.toString());
			} else {
				data.put("success", false);
				data.put("msg", "新增出账任务失败");
				log.info(data.toString());
			}
		 
		} catch (Exception e) {
			data.put("success", false);
			data.put("msg", "新增出账任务异常");
			log.info(data.toString());
		}
		}
		return data;
	}


	@PreAuthorize("hasAuthority('chuAccountTasksManage:update')")
	@RequestMapping(value = "/saveOutAccountTaskUpdate.do")
	@ResponseBody
	public Map<String, Object> saveOutAccountTaskUpdate(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute OutAccountTaskDetail outAccountTaskDetail)
					throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (outAccountTaskDetail.getOutAccountAmount().compareTo(BigDecimal.ZERO) <= 0) {
				//上游余额不够
				msg.put("status", false);
				msg.put("msg", "出账任务金额必须大于零!");
				log.info(msg.toString());
				return msg;
			}
			
			OutAccountTaskDetail detail = outAccountTaskDetailService
					.findOutAccountTaskDetailById(outAccountTaskDetail.getId());
			OutAccountTask outAccountTask = outAccountTaskService
					.findOutAccountTaskById(detail.getOutAccountTaskId());
			if (outAccountTask.getBillStatus() == 1) {
				//已经生成出账单，不能修改出账任务金额
				msg.put("status", false);
				msg.put("msg", "已经生成出账单，不能修改出账金额!");
				log.info(msg.toString());
				return msg;
			}
			BigDecimal upBalance = BigDecimal.ZERO;   //上游结算中金额
			String currencyNo = "";
			String accountType = "Acq";
			String userId = "";  //收单机构id
			String accountOwner = "";  //机构组织id
			List<Currency> currencyList = currencyService.findCurrency();
			if (currencyList != null && currencyList.size() > 0) {
				currencyNo = currencyList.get(0).getCurrencyNo();
			}
			List<OrgInfo> orgList = orgInfoService.findOrgInfo();
			if (orgList != null && orgList.size() > 0) {
				accountOwner = orgList.get(0).getOrgNo();
			}
			AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
			userId = acqOrg.getId().toString();
			ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
			ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
			upBalance = upBalance.add(extAccount.getSettlingAmount());

			//查询当天该上游所有出账单还未出账的金额
			BigDecimal sumAmount = outBillDetailService.countOutBillAmount(detail.getAcqOrgNo());
			sumAmount = sumAmount == null ? BigDecimal.ZERO : sumAmount;
			if (outAccountTaskDetail.getOutAccountAmount().compareTo(upBalance.subtract(sumAmount)) > 0) {
				//上游余额不够
				msg.put("status", false);
				msg.put("msg", "当日上游结算中金额不足!");
				log.info(msg.toString());
				return msg;
			}
			//获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			BigDecimal i = outAccountTaskDetailService
					.updateOutAccountDetailAmount(outAccountTaskDetail,userInfo);

			if (i != null && i.compareTo(BigDecimal.ZERO) > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功!");
				msg.put("newAmount", i);
				log.info(msg.toString());
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "修改出账金额失败！");
			log.error(msg.toString());
			log.error("修改出账金额失败！", e);
		}
		return msg;

	}

	@PreAuthorize("hasAuthority('chuAccountTasksManage:createOutBill')")
	@RequestMapping(value = "/judgeCreateOutBill.do")
	@ResponseBody
	public Map<String, Object> judgeCreateOutBill(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute OutAccountTask outAccountTask) throws Exception {
		log.info("校验出账单信息 出账单Id:"+outAccountTask.getId()+" start.............................");
		Map<String, Object> msg = new HashMap<>();
		int returnNum = 0;
		Integer outAccountTaskId = outAccountTask.getId();
		returnNum = chuAccountService.judgeCreateOutBill(outAccountTaskId);
		if (returnNum > 0) {//说明含有未对账数据
			msg.put("status", false);
			msg.put("msg", "存在未对账的交易");
		}else{
			msg.put("status", true);
			msg.put("msg", "不存在未对账数据，可以生成出账单!");
		}
		log.info("校验出账单信息 出账单Id:"+outAccountTask.getId()+" end.............................");
		return msg;
	}
	
	
	@PreAuthorize("hasAuthority('chuAccountTasksManage:createOutBill')")
	@RequestMapping(value = "/createOutBill.do")
	@ResponseBody
	public Map<String, Object> createOutBill(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute OutAccountTask outAccountTask) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			synchronized (this){
				int i = -1;
				Integer outAccountTaskId = outAccountTask.getId();
				OutAccountTask task = outAccountTaskService.findOutAccountTaskById(outAccountTaskId);
				OutBill outBill = outBillService.findOutBillByTaskId(outAccountTaskId);
				if (outBill != null && "1".equals(outBill.getOutBillStatus())) {
					msg.put("status", false);
					msg.put("msg", "出账单已经出账,请不要重复操作!");
					log.info(msg.toString());
				} else {
					//判断出账任务金额和上游结算中金额是否匹配
					BigDecimal upBalance = BigDecimal.ZERO;   //上游结算中金额
					String currencyNo = "";
					String accountType = "Acq";
					String userId = "";  //收单机构id
					String accountOwner = "";  //机构组织id
					List<Currency> currencyList = currencyService.findCurrency();
					if (currencyList != null && currencyList.size() > 0) {
						currencyNo = currencyList.get(0).getCurrencyNo();
					}
					List<OrgInfo> orgList = orgInfoService.findOrgInfo();
					if (orgList != null && orgList.size() > 0) {
						accountOwner = orgList.get(0).getOrgNo();
					}
					List<OutAccountTaskDetail> outAccountTaskDetailList = outAccountTaskDetailService
							.findOutAccountTaskDetailByTaskId(outAccountTaskId);

					for (OutAccountTaskDetail detail : outAccountTaskDetailList) {
						AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(detail.getAcqOrgNo());
						userId = acqOrg.getId().toString();
						ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, "", "122103", currencyNo);
						ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
						upBalance = upBalance.add(extAccount.getSettlingAmount());
					}
					if (upBalance.compareTo(task.getOutAccountTaskAmount()) < 0) {
						msg.put("status", false);
						msg.put("msg", "生成出账单失败，出账任务金额大于剩余上游结算中金额!");
						log.info(msg.toString());
						return msg;
					}
					//获取到登录者信息
					UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					i = chuAccountService.createOutBill(outAccountTaskId,userInfo);
					if (i > 0) {
						msg.put("status", true);
						msg.put("msg", "生成出账单成功!");
						log.info(msg.toString());
					} else if (i == -2) {
						msg.put("status", false);
						msg.put("msg", "生成出账单失败，请检查是否含有满足出款条件的订单!");
						log.info(msg.toString());
					} else {
						msg.put("status", false);
						msg.put("msg", "生成出账单失败!");
						log.info(msg.toString());
					}
				}
			}
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "生成出账单异常！");
			log.info(msg.toString());
			log.error("生成出账单失败！", e);
		}
		return msg;
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "/findOutBillList.do")
	@ResponseBody
	public Page<OutBill> findOutBillList(@ModelAttribute OutBill outBill,
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OutBill> page,
			HttpServletRequest request) {
		String createTime1 = params.get("start");
		String createTime2 = params.get("end");
		String acqEnname = params.get("acqEnname");
		String outBillStatus = params.get("outBillStatus");
		if (StringUtils.isNotBlank(createTime1)) {
			createTime1 += " 00:00:00";
		}
		if (StringUtils.isNotBlank(createTime2)) {
			createTime2 += " 23:59:59";
		}
		try {
			List<OutBill> list = outBillService.findOutBillList(createTime1,
					createTime2, params, sort, page);
			String acqName = "";
			OutAccountService outAccountService = null;
			for (OutBill bill : list) {
				acqName = bill.getAcqEnname();
                String createTime = "2016-11-10";
                String dayP1 = "2016-11-10";
                if(bill.getCreateTime()!=null){
                    createTime = DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss",bill.getCreateTime());
                    dayP1 = DateUtil.getFormatDate("yyyy-MM-dd",bill.getCreateTime());
                }
                OutBill otBill = outBillService.findCreateTime(acqName,createTime);
                String dayP2 = "2016-11-10";
                if(otBill!=null&&otBill.getCreateTime()!=null){
                    dayP2 = DateUtil.getFormatDate("yyyy-MM-dd",otBill.getCreateTime());
                }
                bill.setDayPhase(dayP2+"~"+dayP1);
				Integer outBillMethod = bill.getOutAccountBillMethod();
//				OutAccountService outAccountService= outAccountServiceService.getById(outBillMethod);
				if (StringUtils.isNotBlank(acqName)) {
					outAccountService = outAccountServiceService.getById(outBillMethod);
					if (outAccountService == null) {
						bill.setHasService(0);
					} else if (outAccountService.getServiceType() == 5) {//T1线下批量结算
						bill.setHasService(1);
					}else if (outAccountService.getServiceType() == 6) {//Tn线下批量结算
						bill.setHasService(1);
					} else if (outAccountService.getServiceType() == 4) {
						bill.setHasService(0);
					} else {
						bill.setHasService(0);
					}
				} else {
					bill.setHasService(0);
				}
				//需要查询当前出账单是否已经回盘导入过
				List<OutBillDetail> dlist = outBillDetailService.findPartByOutBillId(bill.getId());
				if (dlist != null && !dlist.isEmpty()) {
					bill.setTranImport(1);
				} else {
					bill.setTranImport(0);
				}
			}
			page.setResult(list);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "/toOutAccountBillDetail.do")
	public String toOutAccountBillDetail(ModelMap model,
			@ModelAttribute AcqOutBill acqOutBill,
			@RequestParam Map<String, String> params) throws Exception {
		OutBill outBill = outBillService.findOutBillById(acqOutBill
				.getOutBillId());
		model.put("outBill", outBill);
		model.put("params", params);
		List<SysDict> acqOrgList = null;
		List<SysDict> verifyFlagList = null;
		List<SysDict> recordStatusList = null;
		List<SysDict> billStatusList = null;
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			verifyFlagList = sysDictService.findSysDictGroup("sys_verify_flag");
			recordStatusList = sysDictService.findSysDictGroup("out_bill_record_status");
			billStatusList = sysDictService.findSysDictGroup("out_bill_status");
		} catch (Exception e) {
           log.error("异常：" + e.getMessage());
		}
		model.put("outBillDetailId", acqOutBill.getOutBillDetailId()== null ?"":acqOutBill.getOutBillDetailId());
		model.put("acqOrgList", acqOrgList);
		model.put("verifyFlagList", verifyFlagList);
		model.put("recordStatusList", recordStatusList);
		model.put("billStatusList", billStatusList);
		model.put("params", params);
		return "chuAccount/outAccountBillDetail";
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:viewOut')")
	@RequestMapping(value = "/toOutAccountBillView.do")
	public String toOutAccountBillView(ModelMap model,
			@ModelAttribute AcqOutBill acqOutBill,
			@RequestParam Map<String, String> params) throws Exception {
		OutBill outBill = outBillService.findOutBillById(acqOutBill
				.getOutBillId());
		model.put("outBill", outBill);
		model.put("params", params);
		List<SysDict> acqOrgList = null;
		List<SysDict> verifyFlagList = null;
		List<SysDict> billStatusList = null;
		List<SysDict> recordStatusList = null;
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			verifyFlagList = sysDictService.findSysDictGroup("sys_verify_flag");
			billStatusList = sysDictService.findSysDictGroup("out_bill_status");
			recordStatusList = sysDictService.findSysDictGroup("out_bill_record_status");
		} catch (Exception e) {

		}
		
		model.put("outBillId", outBill.getId());
		model.put("acqOrgList", acqOrgList);
		model.put("verifyFlagList", verifyFlagList);
		model.put("billStatusList", billStatusList);
		model.put("recordStatusList", recordStatusList);

		return "chuAccount/outAccountBillViewDetail";
	}
	

	@PreAuthorize("hasAuthority('chuAccountBillManage:fileExport')")
	@RequestMapping(value = "/toOutAccountBillExport.do")
	public String toOutAccountBillExport(ModelMap model,
			@RequestParam(value = "outBillId") Integer outBillId)
					throws Exception {
		OutBill outBill = outBillService.findOutBillById(outBillId);
		model.put("outBill", outBill);
		List<SysDict> acqOrgList = null;
		List<AcqOutBill> acqOutBillList = null;
		List<SysDict> finalOrgList = new ArrayList<SysDict>();
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			Sort sort = new Sort();
			sort.setSord("asc");
			acqOutBillList = acqOutBillService.findByOutBillId(outBillId);

			for (SysDict item1 : acqOrgList) {
				for (AcqOutBill item2 : acqOutBillList) {
					if (item2.getAcqOrgNo().equals(item1.getSysValue())) {
						finalOrgList.add(item1);
					}
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("acqOrgList", finalOrgList);
		return "chuAccount/outAccountBillExport";
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:fileImport')")
	@RequestMapping(value = "/toTransactionImport.do")
	public String toTransactionImport(ModelMap model,
			@ModelAttribute AcqOutBill acqOutBill,
			@RequestParam Map<String, String> params) throws Exception {
		OutBill outBill = outBillService.findOutBillById(acqOutBill
				.getOutBillId());
		model.put("outBill", outBill);
		model.put("params", params);
		List<SysDict> acqOrgList = null;
		List<AcqOutBill> acqOutBillList = null;
		List<SysDict> finalOrgList = new ArrayList<SysDict>();
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			Sort sort = new Sort();
			sort.setSord("asc");
			acqOutBillList = acqOutBillService.findByOutBillId(acqOutBill
					.getOutBillId());

			for (SysDict item1 : acqOrgList) {
				for (AcqOutBill item2 : acqOutBillList) {
					if (item2.getAcqOrgNo().equals(item1.getSysValue())) {
						finalOrgList.add(item1);
					}
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("acqOrgList", finalOrgList);
		return "chuAccount/outAccountBillTransaction";
	}

	// 回盘导入
	@PreAuthorize("hasAuthority('chuAccountBillManage:fileImport')")
	@ResponseBody
	@RequestMapping(value = "/transactionFileUpload.do", method = RequestMethod.POST)
	public Map<String, Object> transactionFileUpload(
			HttpServletRequest request,
			@RequestParam final Map<String, String> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		String settleBank = params.get("settleBank");
		Integer outBillId = Integer.parseInt(params.get("outBillId"));
		String date = params.get("mdate");
		if (StringUtils.isBlank(settleBank)) {
			result.put("success", false);
			result.put("msg", "没有选择出款通道");
			log.info(result.toString());
			return result;
		} 
		/*if (!Constants.NEWEPTOK.equals(settleBank)) {
			result.put("success", false);
			result.put("msg", "回盘文件导入目前只支持新银盛");
			log.info(result.toString());
			return result;
		}*/
		if (StringUtil.isBlank(date)) {
			result.put("success", false);
			result.put("msg", "没有选择日期");
			log.info(result.toString());
			return result;
		}

//		OutAccountService outAccountService = outAccountServiceService.findEntityByAcqEnnameAndServiceType(settleBank,5);
		List<OutAccountService> outAccountService = outAccountServiceService.findOutAccSerListByAcqNname(settleBank,"2,5,6");
		if (outAccountService == null) {
			result.put("success", false);
			result.put("msg", "该出款通道不能进行回盘导入");
			log.info(result.toString());
			return result;
		}

		OutBill outBill = outBillService.findOutBillById(outBillId);
		if (outBill != null && "0".equals(outBill.getOutBillStatus())) {
			//还未出账
			result.put("success", false);
			result.put("msg", "出账单还未出账");
			log.info(result.toString());
			return result;
		}

		String path = date.replace("-", "");
		String dFileName = "";
		//1. 先查询未同步的出账单明细，查询条件：export_status=1, outBillId, settleBank, date
		//注意：返回结果是分组之后的数据，只含有exportFileSerial, exportDate
		List<OutBillDetail> tempList = outBillDetailService.findByOutBillIdAndBank(outBillId, settleBank, 1, date);

		String uploadHeadStr = "/ftp/temp/"+ path;
		File uploadFile = new File(request.getSession().getServletContext().getRealPath(uploadHeadStr));
		if (!uploadFile.exists()) {
			uploadFile.mkdirs();
		}
		String localPath = uploadFile + "";

		if (tempList != null && tempList.size() > 0) {
			List<Map<String, Object>> tlist = new ArrayList<Map<String, Object>>();  //失败的数据记录的信息集合
			List<String> fdetailIdList = new ArrayList<String>();  //回盘文件失败记录集合
			List<String> sdetailIdList = new ArrayList<String>();  //回盘文件成功记录集合
			List<String> adetailIdList = new ArrayList<String>();  //回盘文件所有记录集合
			//boolean hasOtherData = false;  //有其他出账单的数据
			OutBillDetail obd = null;
			Map<String, Object> map = null;
			try {
				for (OutBillDetail detail : tempList) {
					//循环文件，组装文件名=》下载FTP文件-》回盘导入
					switch(settleBank) {
					case Constants.NEWEPTOK:
						dFileName = "RES_F03_"+path+"_"+detail.getExportFileSerial()+".xls";
						log.info("文件名:"+dFileName);
						boolean flag = FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
								Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
								ConfigUtil.getConfig(Constants.FTP_USERNAME),
								ConfigUtil.getConfig(Constants.FTP_PASSWORD),
								ConfigUtil.getConfig(Constants.FTP_NEWEPTOK_DOWNLOAD_DIR) + path,
								dFileName, localPath);
						if (!flag) {
							//ftp上没有下载到该文件，跳过
							log.info("ftp上没有下载到该文件，跳过");
						} else {
							//2003 excel文件
							// 解析文件，目前只有银盛，其它的类型需要以后再处理。如果出款失败，将账户余额加上
							Workbook wb = null;
							try {
								wb = WorkbookFactory.create(new File(localPath + "/" + dFileName));

								//银盛的回盘文件处理
								//文件第一行为注意信息，忽略
								//第二行：出账单信息标题用来比较是否是当前出账单信息
								//第三行：批次号等信息，
								//第四行：出款明细标题
								//第五...N行：出款明细记录
								Sheet sheet = wb.getSheetAt(0);
								//获取总行数
								int rowNum = sheet.getLastRowNum() + 1;

								//从第5行开始逐条判断，该记录是否交易成功，失败则将用户余额重新加上
								for (int i = 4; i < rowNum; i++) {
									Row row = sheet.getRow(i);

									if (row == null) {
										//没有数据了
										break;
									}

									Cell cell0 = row.getCell(0); //序号
									Cell cell10 = row.getCell(10);  //交易状态

									String cellValue10 = getStringCell(cell10);
									String cellValue0 = getStringCell(cell0);

									if (StringUtils.isBlank(cellValue0) || "0".equals(cellValue0)) {
										continue;
									} else {
										obd = outBillDetailService.findOutBillDetailById(cellValue0);
										if (obd == null || !obd.getOutBillId().equals(outBillId)) {
											//该条数据不属于该出账单
											//hasOtherData = true;
											continue;
										}
										if (obd.getExportStatus() == 2 || obd.getExportStatus() == 3) {
											//已经进行过回盘导入，不计算进去
											continue;
										}
										adetailIdList.add(cellValue0);
										if (StringUtils.isBlank(cellValue10) || cellValue10 == null || !cellValue10.toString().equals("00")) {
											map = new HashMap<String, Object>();
											map.put("fromSerialNo", cellValue0);
											map.put("transDate", DateUtil.getDefaultFormatDate(detail.getExportDate()));
											fdetailIdList.add(cellValue0);
											tlist.add(map);
										} else {
											sdetailIdList.add(cellValue0);
											continue;
										}
									}
								}
							} catch (IOException e) {

								log.error("异常:", e);
								//回盘文件下载
								result.put("success", false);
								result.put("msg", "回盘文件下载失败");
								log.error(result.toString());
								return result;
							} catch (Exception e) {
								log.error("异常:", e);
								//回盘文件下载
								result.put("success", false);
								result.put("msg", "没有找到回盘文件:" + dFileName);
								log.error(result.toString());
								return result;
							} finally {
								wb.close();
							}
						}
						break;
                        case "ZF_ZQ":
                            dFileName = "RES_ZFZQ_"+path+"_"+detail.getExportFileSerial()+".xlsx";
                            log.info("文件名:"+dFileName);
                            boolean zfFlag = FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
                                    Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
                                    ConfigUtil.getConfig(Constants.FTP_USERNAME),
                                    ConfigUtil.getConfig(Constants.FTP_PASSWORD),
									ConfigUtil.getConfig("ftp.zfzq_downloadDir") + path,
                                    dFileName, localPath);
							if (!zfFlag) {
								log.info("ftp上没有下载到该文件[{}]，继续下载",dFileName);
								dFileName = "RES_ZFZQ_"+path+"_"+detail.getExportFileSerial()+".xls";
								log.info("文件名:"+dFileName);
								zfFlag = FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
										Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
										ConfigUtil.getConfig(Constants.FTP_USERNAME),
										ConfigUtil.getConfig(Constants.FTP_PASSWORD),
										ConfigUtil.getConfig("ftp.zfzq_downloadDir") + path,
										dFileName, localPath);
							}
							if (!zfFlag) {
								log.info("ftp上没有下载到该文件[{}]，跳过",dFileName);
							}else{
								List<Map<String, String>> bankTransferList = new ArrayList<Map<String, String>>();
								String[] zfzqBankFields = {"seqNo", "merNo", "merName", "amount", "accNo", "accName", "cnaspNo", "bankName", "outTime", "status", "errMsg"};
								resolve07BackFile(zfzqBankFields, bankTransferList, localPath + File.separator + dFileName, 1);
								for (Map<String, String> rs : bankTransferList) {
									String seqNo = rs.get("seqNo");
									String bankStatus = rs.get("status");
									String errMsg = rs.get("errMsg");
									String status = "";
									adetailIdList.add(seqNo);
									if ("出款成功".equals(bankStatus)) {
										sdetailIdList.add(seqNo);
									} else {
										map = new HashMap<String, Object>();
										map.put("fromSerialNo", seqNo);
										map.put("transDate", DateUtil.getDefaultFormatDate(detail.getExportDate()));
										fdetailIdList.add(seqNo);
										tlist.add(map);
									}
								}
							}
							break;
						case "ZFYL_ZQ":
							dFileName = "ZFYL_ZQ_"+path+"_"+detail.getExportFileSerial()+".xlsx";
							log.info("文件名:"+dFileName);
							boolean zfZqFlag = FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
									Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
									ConfigUtil.getConfig(Constants.FTP_USERNAME),
									ConfigUtil.getConfig(Constants.FTP_PASSWORD),
									ConfigUtil.getConfig("ftp.zfylzq_downloadDir") + path,
									dFileName, localPath);
							if (!zfZqFlag) {
								log.info("ftp上没有下载到该文件[{}]，继续下载",dFileName);
								dFileName = "ZFYL_ZQ_"+path+"_"+detail.getExportFileSerial()+".xls";
								log.info("文件名:"+dFileName);
								zfZqFlag = FTPUtil.downFile(ConfigUtil.getConfig(Constants.FTP_IP),
										Integer.parseInt(ConfigUtil.getConfig(Constants.FTP_PORT)),
										ConfigUtil.getConfig(Constants.FTP_USERNAME),
										ConfigUtil.getConfig(Constants.FTP_PASSWORD),
										ConfigUtil.getConfig("ftp.zfylzq_downloadDir") + path,
										dFileName, localPath);
							}
							if (!zfZqFlag) {
								log.info("ftp上没有下载到该文件[{}]，跳过",dFileName);
							}else{
								List<Map<String, String>> bankTransferList = new ArrayList<Map<String, String>>();
								String[] zfzqBankFields = {"seqNo", "merNo", "merName", "amount", "accNo", "accName", "cnaspNo", "bankName", "outTime", "status", "errMsg"};
								resolve07BackFile(zfzqBankFields, bankTransferList, localPath + File.separator + dFileName, 1);
								for (Map<String, String> rs : bankTransferList) {
									String seqNo = rs.get("seqNo");
									String bankStatus = rs.get("status");
									String errMsg = rs.get("errMsg");
									String status = "";
									adetailIdList.add(seqNo);
									if ("出款成功".equals(bankStatus)) {
										sdetailIdList.add(seqNo);
									} else {
										map = new HashMap<String, Object>();
										map.put("fromSerialNo", seqNo);
										map.put("transDate", DateUtil.getDefaultFormatDate(detail.getExportDate()));
										fdetailIdList.add(seqNo);
										tlist.add(map);
									}
								}
							}
							break;
					default:
						break;
					}
				}
				//回盘文件读取完了
				if (fdetailIdList.size() == 0 && sdetailIdList.size() == 0 && tlist.size() == 0) {
					//回盘文件没有找到
					result.put("success", false);
					result.put("msg", "没有找到符合条件的回盘文件");
					log.info(result.toString());
					return result;
				} else {
					
					//判断回盘文件中包含所有的出账单详情
					String detailIds = StringUtils.join(adetailIdList.toArray(), ",");
					for (String adetailId : adetailIdList) {
						log.info("adetailId="+adetailId);
					}
					
					List<OutBillDetail> lessList = outBillDetailService.findNotInDetailIds(outBillId, detailIds);
					if (lessList != null && lessList.size() > 0) {
						result.put("success", false);
						result.put("msg", "回盘失败，回盘条数与出账条数不一致");
						log.info(result.toString());
						return result;
					}
					//获取到登录者信息
					UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
					String uname = userInfo.getUsername();
					result = outBillDetailService.updateTransactionImport(fdetailIdList, sdetailIdList, tlist, outBill ,uname);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				result.put("success", false);
				result.put("msg", "操作失败");
				log.error(result.toString());
				log.error("异常:",ex);
			}
		} else {
			//需要查询当前出账单是否已经回盘导入过
			List<OutBillDetail> dlist = outBillDetailService.findPartByOutBillId(outBillId);
			if (!dlist.isEmpty() && date.equals(DateUtil.getDefaultFormatDate(dlist.get(0).getExportDate()))) {
				result.put("success", false);
				result.put("msg", "请不要重复进行回盘导入");
				log.error(result.toString());
			} else {
				result.put("success", false);
				result.put("msg", "选择的日期与出账日期不匹配");
				log.error(result.toString());
			}
		}
		return result;
	}
	/**
	 *
	 * 功能：解析回盘文件(03版)
	 *
	 * @throws BiffException
	 * @throws ParseException
	 */
	private static  Map<String,Object> resolveBackFile(String[] fields,List<Map<String, String>> list,String fileName,int initNm) throws IOException, BiffException, ParseException{
		Map<String,Object> map = new HashMap<String, Object>();
		FileInputStream in = new FileInputStream(fileName);
		try {
			jxl.Workbook  workbook= WorkbookParser.getWorkbook(in);
			jxl.Sheet sheet = workbook.getSheet(0);
			int rowCount=sheet.getRows();
			for (int i = initNm; i < rowCount; i++) {
				Map<String, String> tempMap = new HashMap<String, String>();
				jxl.Cell[] cells = sheet.getRow(i);
				for (int j = 0; j < cells.length; j++) {
					tempMap.put(fields[j], cells[j].getContents());
				}
				if(tempMap!=null){
					list.add(tempMap);
				}
			}
			in.close();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} catch (BiffException e) {
			e.printStackTrace();
			throw e;
		}

		return map;
	}

	/**
	 *
	 * 功能：解析回盘文件(07版)
	 *
	 * @throws BiffException
	 * @throws ParseException
	 */
	private static  Map<String,Object> resolve07BackFile(String[] fields,List<Map<String, String>> list,String fileName,int initNm) throws IOException, BiffException, ParseException{
		Map<String,Object> map = new HashMap<String, Object>();
		FileInputStream in = new FileInputStream(fileName);
		try {
			Workbook  workbook;
			if(fileName.endsWith(".xlsx")){
				workbook =new XSSFWorkbook(in);
			}else {
				workbook =new HSSFWorkbook(in);
			}
			Sheet sheet=workbook.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();//行

			for (int i = initNm; i < rowCount; i++) {
				Map<String, String> tempMap = new HashMap<String, String>();
				Row row = sheet.getRow(i);
				System.out.println(row.getFirstCellNum()+"----"+row.getLastCellNum());
				for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {
					tempMap.put(fields[j], row.getCell(j).toString());
				}
				if(tempMap!=null){
					list.add(tempMap);
				}
			}
			in.close();
			workbook.close();

		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

		return map;
	}
	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "findAcqOutBillList.do")
	@ResponseBody
	public Page<AcqOutBill> findAcqOutBillList(
			@ModelAttribute AcqOutBill acqOutBill,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<AcqOutBill> page) {
		try {
			acqOutBillService.findAcqOutBillList(acqOutBill, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "findSubOutBillDetailList.do")
	@ResponseBody
	public Page<SubOutBillDetail> findOutBillDetailList(
			@ModelAttribute SubOutBillDetail subOutBillDetail,
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<SubOutBillDetail> page) {

		String merchantNo = (String) params.get("merchantNo");
		String acqOrgNo = (String) params.get("acqOrg");
		String merchantBalance1 = (String) params.get("merchantBalance1");
		String merchantBalance2 = (String) params.get("merchantBalance2");
		String outAccountTaskAmount1 = (String) params
				.get("outAccountTaskAmount1");
		String outAccountTaskAmount2 = (String) params
				.get("outAccountTaskAmount2");
		String timeStart = params.get("start") ;
		String timeEnd = params.get("end") ;
		String isChangeRemark = (String) params.get("isChangeRemark");
		try {
			List<SubOutBillDetail> subOutBillDetailList = subOutBillDetailService.findSubOutBillDetailList(subOutBillDetail,
					merchantNo, acqOrgNo, 
					merchantBalance1, merchantBalance2, outAccountTaskAmount1,
					outAccountTaskAmount2, isChangeRemark, timeStart,timeEnd,sort, page);
			page.setResult(subOutBillDetailList);
			//未进行出账，则需要查询最新的商户结算中金额
			/*if (subOutBillDetailList != null && !subOutBillDetailList.isEmpty()) {
				ExtAccount ext = null;
				for (SubOutBillDetail item : subOutBillDetailList) {
					ext = extAccountService.findExtAccountByMerchantNo(item.getMerchantNo());
					if(ext != null){
						item.setMerchantBalance(ext.getSettlingAmount());
					}	
				}
				page.setResult(subOutBillDetailList);
			}*/
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}
	
	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "collectSubOutBillDetailAmount.do")
	@ResponseBody
	public Map<String, Object> collectSubOutBillDetailAmount(ModelMap model,
			@ModelAttribute SubOutBillDetail subOutBillDetail,
			@RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		String merchantNo = (String) params.get("merchantNo");
		String acqOrgNo = (String) params.get("acqOrg");
		String merchantBalance1 = (String) params.get("merchantBalance1");
		String merchantBalance2 = (String) params.get("merchantBalance2");
		String outAccountTaskAmount1 = (String) params
				.get("outAccountTaskAmount1");
		String outAccountTaskAmount2 = (String) params
				.get("outAccountTaskAmount2");
		String isChangeRemark = (String) params.get("isChangeRemark");
		
		String timeStart = params.get("start") ;
		String timeEnd = params.get("end") ;
		
		int outBillSuccess = 0;//出款成功笔数
    	BigDecimal outBillSuccessAmount = BigDecimal.ZERO;//出款成功金额总和
    	
    	int outBillFail = 0;//出款失败笔数
    	BigDecimal outBillFailAmount = BigDecimal.ZERO;//出款失败金额总和
    	
    	int outBillIng = 0;//出款中笔数
    	BigDecimal outBillIngAmount = BigDecimal.ZERO;//出款中金额总和
    	
    	int outBillNo = 0;//未出款笔数
    	BigDecimal outBillNoAmount = BigDecimal.ZERO;//未出款金额总和
    	
    	BigDecimal totalAmount = BigDecimal.ZERO;//出款金额总和
		try {
			List<SubOutBillDetail> subOutBillDetailList = subOutBillDetailService.exportSubOutBillDetailList(subOutBillDetail,
					merchantNo, acqOrgNo, 
					merchantBalance1, merchantBalance2, outAccountTaskAmount1,
					outAccountTaskAmount2, isChangeRemark, timeStart, timeEnd);
			for (SubOutBillDetail subOutBillDetail2 : subOutBillDetailList) {
				totalAmount = totalAmount.add(subOutBillDetail2.getOutAccountTaskAmount());
				String outBillStatus = subOutBillDetail2.getOutBillStatus() == null?"":subOutBillDetail2.getOutBillStatus().toString();
				BigDecimal outAccountTaskAmount = subOutBillDetail2.getOutAccountTaskAmount();
				switch (outBillStatus) {
				case "0":
					outBillNo++;
					outBillNoAmount = outBillNoAmount.add(outAccountTaskAmount);
					break;
				case "1":
					outBillSuccess++;
					outBillSuccessAmount = outBillSuccessAmount.add(outAccountTaskAmount);
					break;
				case "2":
					outBillFail++;
					outBillFailAmount = outBillFailAmount.add(outAccountTaskAmount);
					break;
				case "3":
					outBillIng++;
					outBillIngAmount = outBillIngAmount.add(outAccountTaskAmount);
					break;
				default:
					outBillNo++;
					outBillNoAmount = outBillNoAmount.add(outAccountTaskAmount);
					break;
				}
			}
			msg.put("outBillSuccess", outBillSuccess);//出款成功笔数
			msg.put("outBillSuccessAmount", outBillSuccessAmount);//出款成功金额总和
			msg.put("outBillFail", outBillFail);//出款失败笔数
			msg.put("outBillFailAmount", outBillFailAmount);//出款失败金额总和
			msg.put("outBillIng", outBillIng);//出款中笔数
			msg.put("outBillIngAmount", outBillIngAmount);//出款中金额总和
			msg.put("outBillNo", outBillNo);//未出款笔数
			msg.put("outBillNoAmount", outBillNoAmount);//未出款金额总和
			
			msg.put("totalCount", subOutBillDetailList.size());//出款总笔数
			msg.put("totalAmount", totalAmount);//出款金额总和
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return msg;
	}
	
	@PreAuthorize("hasAuthority('chuAccountBillManage:query')")
	@RequestMapping(value = "findOutBillDetailList.do")
	@ResponseBody
	public Page<OutBillDetail> findOutBillDetailList(
			@ModelAttribute OutBillDetail outBillDetail,
			@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<OutBillDetail> page) {
		String merchantNo = (String) params.get("merchantNo");
		String acqOrgNo = (String) params.get("acqOrg");
		String merchantBalance1 = (String) params.get("merchantBalance1");
		String merchantBalance2 = (String) params.get("merchantBalance2");
		String outAccountTaskAmount1 = (String) params
				.get("outAccountTaskAmount1");
		String outAccountTaskAmount2 = (String) params
				.get("outAccountTaskAmount2");
		String isChangeRemark = (String) params.get("isChangeRemark");
		try {
			List<OutBillDetail> list = outBillDetailService.findOutBillDetailList(outBillDetail,
					merchantNo, acqOrgNo, 
					merchantBalance1, merchantBalance2, outAccountTaskAmount1,
					outAccountTaskAmount2, isChangeRemark, sort, page);
			//未进行出账，则需要查询最新的商户结算中金额
			if (list != null && !list.isEmpty()) {
				ExtAccount ext = null;
				for (OutBillDetail item : list) {
					ext = extAccountService.findExtAccountByMerchantNo(item.getMerchantNo());
					item.setMerchantBalance(ext.getSettlingAmount());
				}
				page.setResult(list);
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}


/*	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "/saveOutAccountBillUpdate.do")
	@ResponseBody
	public Map<String, Object> saveOutAccountBillUpdate(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute OutBillDetail outBillDetail) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			BigDecimal merchantBalance = outBillDetail.getMerchantBalance();
			BigDecimal outAccountTaskAmount = outBillDetail.getOutAccountTaskAmount();
			if (merchantBalance.compareTo(outAccountTaskAmount) < 0) {
				msg.put("status", false);
				msg.put("msg", "出账任务金额超出商户余额！");
			} else {
				OutBillDetail outBillDetail2 = outBillDetailService
						.findOutBillDetailById(outBillDetail.getId());
				outBillDetail2.setOutAccountTaskAmount(outBillDetail
						.getOutAccountTaskAmount());
				outBillDetail2.setChangeRemark(outBillDetail.getChangeRemark());

				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal();
				outBillDetail2.setChangeOperatorId(userInfo.getUserId());
				outBillDetail2.setChangeOperatorName(userInfo.getUsername());
				BigDecimal i = outBillDetailService
						.updateOutAccountAmount(outBillDetail2);
				if (i != null && i.compareTo(BigDecimal.ZERO) > 0) {
					msg.put("status", true);
					msg.put("msg", "修改成功!");
					msg.put("newAmount", i);
				}
			}
		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改失败！");
		}
		return msg;

	}*/
	
	/**
	 * 更新财务调整备注
	 * @param model
	 * @param params
	 * @param subOutBillDetail
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "/saveSubOutAccountBillUpdate")
	@ResponseBody
	public Map<String, Object> saveSubOutAccountBillUpdate(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute SubOutBillDetail subOutBillDetail) throws Exception {
	
		Map<String, Object> msg = new HashMap<>();
		int i = 0;
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			subOutBillDetail.setChangeOperatorId(userInfo.getUserId());
			subOutBillDetail.setChangeOperatorName(userInfo.getUsername());
			i = subOutBillDetailService.updateOutBillDetailChangeRemark(subOutBillDetail);
			if ( i > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功!");
				msg.put("newAmount", i);
			}

		} catch (Exception e) {
			log.error("修改失败！", e);
			msg.put("status", false);
			msg.put("msg", "修改失败！");
		}
		return msg;

	}
	
	/**
	 * 删除子出账单
	 * @param model
	 * @param params
	 * @param subOutBillDetail
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/deleteSubOutAccountBillDetail")
	@ResponseBody
	public Map<String, Object> deleteSubOutAccountBillUpdate(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute SubOutBillDetail subOutBillDetail) throws Exception {
	
		Map<String, Object> msg = new HashMap<>();
		List<SubOutBillDetail> subOutBillDetailList =  new ArrayList<SubOutBillDetail>();
		int i = 0;
		try {
			SubOutBillDetail dbsubOutBillDetail = subOutBillDetailService.queryOutBillDetailById(subOutBillDetail);
			subOutBillDetailList.add(dbsubOutBillDetail);
			i = subOutBillDetailService.updateOutBillDetailById(subOutBillDetailList);//更新状态就好，移出该出账单
			if ( i > 0) {
				msg.put("status", true);
				msg.put("msg", "删除成功!");
				msg.put("newAmount", i);
			}else{
				msg.put("status", false);
				msg.put("msg", "删除失败！");
			}

		} catch (Exception e) {
			log.error("删除失败！", e);
			msg.put("status", false);
			msg.put("msg", "删除失败！");
		}
		return msg;

	}
	
	
	
	/**
	 * 批量删除
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bacthDeleteSubOutBillDetail.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bacthDeleteSubOutBillDetail(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		List<SubOutBillDetail> subOutBillDetailList =  new ArrayList<SubOutBillDetail>();
		Map<String,Object> msg=new HashMap<>();
		int returnNum = 0;
		//log.info("params  " +params);
		//log.info("params.size()  " +params.size());
		String paramsName = "";
		try {
			//先将所选择的出账单的交易金额汇总，然后去跟上游结算中金额去对比
			for(int i=0 ;i<params.size() ;i++){
				paramsName = "selectId["+i+"]" ;
				//log.info("paramsName  " +paramsName);
				SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
				//log.info("params.get(paramsName)  " +params.get(paramsName));
				if(StringUtils.isNotBlank(params.get(paramsName))){
					subOutBillDetail.setId(params.get(paramsName));
					SubOutBillDetail dbsubOutBillDetail = subOutBillDetailService.queryOutBillDetailById(subOutBillDetail);
					subOutBillDetailList.add(dbsubOutBillDetail);
				}
				
			}
			if(subOutBillDetailList != null && subOutBillDetailList.size() > 0 ){
				returnNum = subOutBillDetailService.updateOutBillDetailById(subOutBillDetailList);//更新状态就好，移出该出账单
				if ( returnNum > 0) {
					msg.put("status", true);
					msg.put("msg", "批量删除成功!");
					msg.put("newAmount", returnNum);
				}else{
					msg.put("status", false);
					msg.put("msg", "批量删除失败！");
				}
			}else{
				msg.put("status", false);
				msg.put("msg", "批量删除失败！");
			}
		} catch (Exception e) {
			log.error("批量删除失败！", e);
			msg.put("status", false);
			msg.put("msg", "批量删除失败！");
		}

		return msg;
	}
	
	
	/**
	 * 批量编辑
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/bacthEditSubOutBillDetail.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> bacthEditSubOutBillDetail(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		List<SubOutBillDetail> subOutBillDetailList =  new ArrayList<SubOutBillDetail>();
		Map<String,Object> msg=new HashMap<>();
		String paramsName = "";
		String changeRemark = "";
		try {
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			changeRemark = params.get("changeRemark");
			log.info("changeRemar = "+ changeRemark);
			for(int i=0 ;i<params.size() ;i++){
				paramsName = "selectId["+i+"]" ;
				SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
				if(StringUtils.isNotBlank(params.get(paramsName))){
					subOutBillDetail.setId(params.get(paramsName));
					SubOutBillDetail dbsubOutBillDetail = subOutBillDetailService.queryOutBillDetailById(subOutBillDetail);
					dbsubOutBillDetail.setChangeOperatorId(userInfo.getUserId());
					dbsubOutBillDetail.setChangeOperatorName(userInfo.getUsername());
					dbsubOutBillDetail.setChangeRemark(changeRemark);
					subOutBillDetailList.add(dbsubOutBillDetail);
				}
				
			}
			if(subOutBillDetailList != null && subOutBillDetailList.size() > 0 ){
				msg = subOutBillDetailService.updateRemarkBacthOutBillDetailById(subOutBillDetailList);//更新状态就好
			}else{
				msg.put("status", true);
				msg.put("msg", "没有修改任何数据");
			}
		} catch (Exception e) {
			log.error("批量编辑失败！", e);
			msg.put("status", false);
			msg.put("msg", "批量编辑失败！");
		}

		return msg;
	}
	

	@PreAuthorize("hasAuthority('chuAccountBillManage:confirmOut')")
	@RequestMapping(value = "/toOutAccountBillConfirm.do")
	public String toOutAccountBillConfirm(ModelMap model,
										  @ModelAttribute AcqOutBill acqOutBill,
										  @RequestParam Map<String, String> params, HttpSession session) throws Exception {
		OutBill outBill = outBillService.findOutBillTaskById(acqOutBill
				.getOutBillId());
		String randomVal = RandomStringUtils.randomNumeric(6);
		session.setAttribute("confirmRandom",randomVal);
		List<SysDict> acqOrgList = null;
		List<SysDict> verifyFlagList = null;
		List<SysDict> recordStatusList = null;
		List<SysDict> billStatusList = null;
		List<OutAccountService> outAccountServiceList = new ArrayList<OutAccountService>();
//		OutAccountService outAccountService = null;
//		Integer serviceType = 4;
		
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
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			verifyFlagList = sysDictService.findSysDictGroup("sys_verify_flag");
			recordStatusList = sysDictService.findSysDictGroup("out_bill_record_status");
			billStatusList = sysDictService.findSysDictGroup("out_bill_status");
            String serviceTypes = "4,5,6";
            if("T1".equals(outBill.getOutBillRange())){
                serviceTypes = "4,5";
            }else if("Tn".equals(outBill.getOutBillRange())){
                serviceTypes = "6";
            }
            outAccountServiceList = outAccountServiceService.findOutAccSerListByAcqNname(outBill.getAcqEnname(),serviceTypes);
//			outAccountServiceList.add(outAccountService);
		} catch (Exception e) {
            log.error("确认出账页面数据异常：" + e);
		}
		
		model.put("acqOrgList", acqOrgList);
		model.put("verifyFlagList", verifyFlagList);
		model.put("recordStatusList", recordStatusList);
		model.put("billStatusList", billStatusList);
		model.put("outAccountServiceList", outAccountServiceList);
		model.put("outBill", outBill);
		model.put("params", params);
		model.put("confirmRandom",randomVal);
		return "chuAccount/outAccountBillConfirm";
	}

	/**
	 * 结算转账 查询
	 * 
	 * @param settleTransfer
	 * @param sort
	 * @param page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('settleTransferQuery:query')")
	@RequestMapping(value = "/findSettleTransferList.do")
	@ResponseBody
	public Page<SettleTransfer> findSettleTransferList(
			@ModelAttribute SettleTransfer settleTransfer,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<SettleTransfer> page,
			HttpServletRequest request) {
		try {
			List<SettleTransfer> list = chuAccountService
					.findSettleTransferList(settleTransfer, sort, page);
			System.out.println(list);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('submitChuKuanChannel:query')")
	@RequestMapping(value = "/toSubmitChuKuanChannel.do")
	public String toSubmitChuKuanChannel(ModelMap model,
			@RequestParam Map<String, String> params) throws Exception {
		List<SysDict> settleTransferBankList = null;
		List<SysDict> settleTransferStatusList = null;
		try {
			settleTransferBankList = sysDictService.findSysDictGroup("settle_transfer_bank");
			settleTransferStatusList = sysDictService
					.findSysDictGroup("settle_transfer_bank");
		} catch (Exception e) {

		}
		model.put("settleTransferStatusList", settleTransferStatusList);
		model.put("settleTransferBankList", settleTransferBankList);
		model.put("params", params);
		return "chuAccount/submitChuKuanChannel";
	}

	@PreAuthorize("hasAuthority('submitChuKuanChannel:query')")
	@RequestMapping(value = "/findSubmitChuKuanChannelList.do")
	@ResponseBody
	public Page<SettleTransferFile> findSubmitChuKuanChannelList(
			@ModelAttribute SettleTransferFile settleTransferFile,
			@ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<SettleTransferFile> page,
			HttpServletRequest request) {
		try {
			String createDate1 = request.getParameter("createDate1");
			String createDate2 = request.getParameter("createDate2");
			if (StringUtils.isNotBlank(createDate1)) {
				createDate1 += " 00:00:00";
			}
			if (StringUtils.isNotBlank(createDate2)) {
				createDate2 += " 23:59:59";
			}
			List<SettleTransferFile> list = chuAccountService
					.findSubmitChuKuanChannelList(settleTransferFile,
							createDate1, createDate2, sort, page);
			log.info(list.toString());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:confirmOut')")
	@RequestMapping(value = "/judgeConfirmOut.do")
	@Logs(description="判断确认出账")
	@ResponseBody
	public Map<String, Object> judgeConfirmOut(@RequestParam(value = "id") Integer id ,@RequestParam(value = "outAccountBillMethod") String outAccountBillMethod) {
		log.info("判断确认出账开始：");
		Map<String, Object> data = new HashMap<String, Object>();
		Map<String, Object> resultMapData = new HashMap<String, Object>();
		boolean flag = false;
		try {
			// 判断该出账单出账状态是否为0，为0则可以确认出账，为1已经出账
			OutBill outBill = outBillService.findOutBillById(id);
			if ("0".equals(outBill.getOutBillStatus())) {
               int verFlagCount =  subOutBillDetailService.findVerifyFlag(outBill.getId());
                if(verFlagCount>0){
                    data.put("status", false);
                    data.put("msg", "存在校验失败的记录，请先进行处理");
                    return data;
                }
                //20171225修改为不去同步状态
//				flag = this.sysSettleStatus();
				flag = true;
				if(flag){
					resultMapData = outBillService.judgeConfirmOut(outBill.getId(), outBill.getAcqEnname());
					if(resultMapData.isEmpty() && resultMapData.size() == 0){
						//先判断是否合成 出账单明细
						List<OutBillDetail> dlist = outBillDetailService.findByOutBillId(outBill.getId());
						if(dlist.size() == 0 || dlist.isEmpty()){
							//先合成子出账单到出账单
							outBillService.mergeSubOutBill(outBill.getId(), outBill.getAcqEnname());
						}
						data.put("status", true);
					}else{
						data.put("status", false);
						data.put("resultMapData", resultMapData);
					}
				}else{
					data.put("status", false);
					data.put("msg", "调用同步状态接口异常!");
				}
			} else {
				data.put("status", false);
				data.put("msg", "出账单已经出账");
			}
		} catch (Exception e) {
			log.error("异常:", e);
			data.put("status", false);
			data.put("msg", "确认出账失败，调用同步状态接口异常或合成子出账单异常！");
		}
		log.info(data.toString());
		return data;
	}
	
	private boolean sysSettleStatus() {
		boolean flag = false;
		final String secret = accountApiHttpSecret;
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		try{
			claims.put("transDate", "");
			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/syncController/syncDuiBillDetailStatus.do" ;
			log.info("同步结算状态url："+url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("同步结算状态返回结果："+response);
			if (response == null || "".equals(response)) {
				//同步结算状态失败
				flag = false;
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					flag = false;
				}else{
					flag = true;
				}
			}
		}catch(Exception e){
			log.info("同步结算状态接口异常"+e.getMessage());
			flag = false;
		}
		return flag;
	}

	@PreAuthorize("hasAuthority('chuAccountBillManage:confirmOut')")
	@RequestMapping(value = "/confirmOut.do")
	@Logs(description="确认出账")
	@ResponseBody
	public Map<String, Object> confirmOut(@RequestParam(value = "id") Integer id ,@RequestParam(value = "outAccountBillMethod") Integer outAccountBillMethod,@RequestParam(value = "confirmRandom",defaultValue = "") String confirmRandom,HttpSession session) {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			log.info("确认出账，出账单id:"+id);
			String random = (String) session.getAttribute("confirmRandom");
			session.removeAttribute("confirmRandom");
			if(StringUtils.isBlank(random) || !confirmRandom.equals(random)){
				data.put("status", false);
				data.put("msg", "请返回出账列表，重新确认出账");
				log.info(data.toString());
				return data;
			}
			// 判断该出账单出账状态是否为0，为0则可以确认出账，为1已经出账
			OutBill outBill = outBillService.findOutBillById(id);
			if ("0".equals(outBill.getOutBillStatus())) {
				
//				//先判断是否合成 出账单明细
//				List<OutBillDetail> dlist = outBillDetailService.findByOutBillId(outBill.getId());
//				if(dlist.size() == 0 || dlist.isEmpty()){
//					//先合成子出账单到出账单
//					outBillService.mergeSubBill(outBill.getId(), outBill.getAcqEnname());
//				}
//				//先update出账方式
				outBill.setOutAccountBillMethod(outAccountBillMethod);
//				outBillService.updateOutBillById(outBill);
				//再计划出账
				Map<String, Object> result = outBillService.confirmOut(outBill, userInfo);
				String msg = (String) result.get("msg");
				String logType = "confirmAccountLog";
				//插入确认出账操作日志
				insertOperateLog(msg, outBill.getId(), logType);
				
				return result;
			} else {
				data.put("status", false);
				data.put("msg", "出账单已经出账");
				log.info(data.toString());
			}
		} catch (RuntimeException re) {
			log.error("异常:",re);
			data.put("status", false);
			data.put("msg", re.getMessage() == null ? "操作失败，系统异常" : re.getMessage());
			log.info(data.toString());
		} catch (Exception e) {
			log.error("异常:",e);
			data.put("status", false);
			data.put("msg", "操作失败，系统异常");
			log.info(data.toString());
		}
		return data;
	}
	/**
	 * 保存确认出账操作日志
	 * @param msg
	 * @param outBillID
	 * @param logType
	 */
	private void insertOperateLog(String msg, Integer outBillID, String logType) {
		// 获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		OptLogs log = new OptLogs();
		log.setOutBillId(outBillID);
		log.setLogType(logType);
		log.setOperateContent(msg);
		log.setOperator(userInfo.getUsername());// 创建者
		optLogsService.insertOptLogs(log);
	}

	/**
	 * 查询商户出账结果
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('errorMerchantChuAccountResult:query')")
	@RequestMapping(value = "/findErrorSubMerChuAccountList.do")
	@ResponseBody
	public Page<Map<String, Object>> findErrorSubMerChuAccountList(@ModelAttribute("subOutBillDetailLogs")SubOutBillDetailLogs subOutBillDetailLogs, 
			@ModelAttribute("sort")Sort sort,
			@ModelAttribute("page") Page<SubOutBillDetailLogs> page
			){
		Page<Map<String, Object>> tpage = new Page<Map<String, Object>>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		try {
			String merchantNo = (String) subOutBillDetailLogs.getMerchantNo();
			String merchantName = (String) subOutBillDetailLogs.getMerchantName();
			String mobile = (String) subOutBillDetailLogs.getMobile();
			List<String> merNoList = null;
			//先查询代理商数据库，根据条件查询商户list
			merNoList = merchantInfoService.findByNameAndMobile(merchantNo, merchantName, mobile);
			StringBuilder sb = new StringBuilder();
			if (merNoList != null && merNoList.size() > 0) {
				for (String s : merNoList) {
					sb.append("'");
					sb.append(s);
					sb.append("',");
				}
				subOutBillDetailLogs.setMerNos(sb.substring(0, sb.length() - 1));
			} else {
				subOutBillDetailLogs.setMerNos("-1");
			}
			Map<String, Object> tempMap = null;
			MerchantInfo merInfo = null;
			MerchantCardInfo merCardInfo = null;
			//查询出账单明细
			List<SubOutBillDetailLogs> list = subOutBillDetailLogsService.findOutBillDetailLogsList(subOutBillDetailLogs, sort, page);
			if (list != null && list.size() > 0) {
				for (SubOutBillDetailLogs detail : list) {
					tempMap = new HashMap<String, Object>();
					tempMap.put("subOutBillDetailId", detail.getSubOutBillDetailId());
					tempMap.put("outBillId", detail.getOutBillId());
					tempMap.put("outBillDetailId", detail.getOutBillDetailId());
					tempMap.put("merchantNo", detail.getMerchantNo());
					tempMap.put("outAccountTaskAmount", detail.getOutAccountTaskAmount());
					tempMap.put("outBillStatus", detail.getOutBillStatus());
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("settleTime", detail.getSettleTime());
					tempMap.put("orderReferenceNo", detail.getOrderReferenceNo());
					tempMap.put("transAmount", detail.getTransAmount());
					tempMap.put("transTime", detail.getTransTime());
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("merchantName", detail.getMerchantName());
					tempMap.put("mobile", detail.getMobile());
					tempMap.put("outAccountNote", detail.getOutAccountNote());
					merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					merCardInfo = merchantCardInfoService.getByMerchantNo(detail.getMerchantNo());
					if (merInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("merchantName", merInfo.getMerchantName());
						tempMap.put("mobile", merInfo.getMobilephone());
					}
					if (merCardInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("cardNo", merCardInfo.getAccountNo());
						tempMap.put("cardName", merCardInfo.getAccountName());
					}
					mapList.add(tempMap);
				}
			}
			tpage.setPageNo(page.getPageNo());
			tpage.setPageSize(page.getPageSize());
			tpage.setTotalCount(page.getTotalCount());
			tpage.setTotalPages(page.getTotalPages());
			tpage.setResult(mapList);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return tpage;
	}

	
	/**
	 * 查询商户出账结果
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('merchantChuAccountResult:query')")
	@RequestMapping(value = "/findMerChuAccountList.do")
	@ResponseBody
	public Page<Map<String, Object>> findSubMerChuAccountList(@ModelAttribute("subOutBillDetail")SubOutBillDetail subOutBillDetail, 
			@ModelAttribute("sort")Sort sort,
			@ModelAttribute("page") Page<SubOutBillDetail> page
			){
		Page<Map<String, Object>> tpage = new Page<Map<String, Object>>();
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		try {
			String merchantNo = subOutBillDetail.getMerchantNo();
			if(StringUtils.isNotBlank(merchantNo)){
				List<String> l = Arrays.asList(merchantNo.split(","));
				l = l.stream().filter( a -> StringUtils.isNotBlank(a)).map(a -> StringUtils.trim(a)).collect(Collectors.toList());
				merchantNo = "\'"+String.join("\',\'",l)+"\'";
			}

			String orderReferenceNo = subOutBillDetail.getOrderReferenceNo();
			if(StringUtils.isNotBlank(orderReferenceNo)){
				List<String> l = Arrays.asList(orderReferenceNo.split(","));
				l = l.stream().filter( a -> StringUtils.isNotBlank(a)).map(a -> StringUtils.trim(a)).collect(Collectors.toList());
				orderReferenceNo = "\'"+String.join("\',\'",l)+"\'";
				subOutBillDetail.setOrderReferenceNo(orderReferenceNo);
			}

			String acqMerchantNo = subOutBillDetail.getAcqMerchantNo();
			if(StringUtils.isNotBlank(acqMerchantNo)){
				List<String> l = Arrays.asList(acqMerchantNo.split(","));
				l = l.stream().filter( a -> StringUtils.isNotBlank(a)).map(a -> StringUtils.trim(a)).collect(Collectors.toList());
				acqMerchantNo = "\'"+String.join("\',\'",l)+"\'";
				subOutBillDetail.setAcqMerchantNo(acqMerchantNo);
			}


			String merchantName = (String) subOutBillDetail.getMerchantName();
			String mobile = (String) subOutBillDetail.getMobile();
			List<String> merNoList = null;
			//先查询代理商数据库，根据条件查询商户list
			if(StringUtils.isBlank(merchantNo)&&StringUtils.isBlank(merchantName)&&StringUtils.isBlank(mobile)){
				subOutBillDetail.setMerNos("-1");
			}else{
				merNoList = merchantInfoService.findByNameAndMobile(merchantNo, merchantName, mobile);
				StringBuilder sb = new StringBuilder();
				if (merNoList != null && merNoList.size() > 0) {
					for (String s : merNoList) {
						sb.append("'");
						sb.append(s);
						sb.append("',");
					}
					subOutBillDetail.setMerNos(sb.substring(0, sb.length() - 1));
				} else {
					subOutBillDetail.setMerNos("-1");
				}
			}

			StringBuilder selectId = new StringBuilder();
			String [] selectIds = {};
			if(StringUtils.isNotBlank(subOutBillDetail.getSelectIds())){
			   selectIds = subOutBillDetail.getSelectIds().split(",");
			   for (int i = 0; i < selectIds.length; i++) {
				   selectId.append("'");
				   selectId.append(selectIds[i]);
				   selectId.append("',");
			   }
			   subOutBillDetail.setSelectIds(selectId.substring(0, selectId.length() - 1));
			}else{
				subOutBillDetail.setSelectIds("-1");
			}
			
			Map<String, Object> tempMap = null;
			MerchantInfo merInfo = null;
			MerchantCardInfo merCardInfo = null;
			//查询出账单明细
			List<SubOutBillDetail> list = subOutBillDetailService.findSubMerChuAccountList(subOutBillDetail, sort, page);
			if (list != null && list.size() > 0) {
				for (SubOutBillDetail detail : list) {
					tempMap = new HashMap<String, Object>();
					tempMap.put("id", detail.getId());
					tempMap.put("outBillId", detail.getOutBillId());
					tempMap.put("outBillDetailId", detail.getOutBillDetailId());
					tempMap.put("merchantNo", detail.getMerchantNo());
					tempMap.put("plateMerchantEntryNo", detail.getPlateMerchantEntryNo());
					tempMap.put("acqMerchantNo", detail.getAcqMerchantNo());
					tempMap.put("outAccountTaskAmount", detail.getOutAccountTaskAmount());
					tempMap.put("outBillStatus", detail.getOutBillStatus());
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("isAddBill", detail.getIsAddBill());
					tempMap.put("orderReferenceNo", detail.getOrderReferenceNo());
					tempMap.put("transAmount", detail.getTransAmount());
					tempMap.put("settleTime", detail.getSettleTime());
					tempMap.put("transTime", detail.getTransTime());
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("merchantName", detail.getMerchantName());
					tempMap.put("mobile", detail.getMobile());
					tempMap.put("recordStatus", detail.getRecordStatus());
					tempMap.put("outAccountNote", detail.getOutAccountNote());
					merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					merCardInfo = merchantCardInfoService.getByMerchantNo(detail.getMerchantNo());
					if (merInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("merchantName", merInfo.getMerchantName());
						tempMap.put("mobile", merInfo.getMobilephone());
					}
					if (merCardInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("cardNo", merCardInfo.getAccountNo());
						tempMap.put("cardName", merCardInfo.getAccountName());
					}
					mapList.add(tempMap);
				}
			}
			tpage.setPageNo(page.getPageNo());
			tpage.setPageSize(page.getPageSize());
			tpage.setTotalCount(page.getTotalCount());
			tpage.setTotalPages(page.getTotalPages());
			tpage.setResult(mapList);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return tpage;
	}

	
/*	//导入出账单明细数据
		@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
		@RequestMapping(value = "importOutBillDetailFile.do")
		@ResponseBody
		public Map<String, Object> importOutBillDetailFile(
				HttpServletRequest request,
				MultipartFile file,
				@RequestParam Integer outBillId) {
			Map<String, Object> result = new HashMap<String, Object>();

			try {
				File temp = File.createTempFile(file.getOriginalFilename(), ".xls");
				file.transferTo(temp);
				FileInputStream in = new FileInputStream(temp);

				String[] head = new String[] {"id","merchantNo","merchantBalance","outAccountTaskAmount","acqOrgNo","changeRemark","verifyFlag","verifyMsg"};

				List<String> errorList = new ArrayList<String>();
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();

				ExcelParseUtil.parserExcel(in, head, list, errorList);
				if (list.size() == 0 && errorList.size() == 0) {
					result.put("status", false);
					result.put("msg", "文档中没有数据");
				} else if (list.size() > 0) {
					List<SysDict> acqOrgList = null;
					List<AcqOutBill> acqOutBillList = null;
					List<SysDict> finalOrgList = new ArrayList<SysDict>();
					try {
						acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
						acqOutBillList = acqOutBillService.findByOutBillId(outBillId);

						for (SysDict item1 : acqOrgList) {
							for (AcqOutBill item2 : acqOutBillList) {
								if (item2.getAcqOrgNo().equals(item1.getSysValue())) {
									finalOrgList.add(item1);
								}
							}
						}
					} catch (Exception e) {
						log.error("异常",e);
					}

					if (!finalOrgList.isEmpty()) {
						for (Map<String, String> map : list) {
							for (SysDict dict : finalOrgList) {
								if (dict.getSysName().equals(map.get("acqOrgNo"))) {
									map.put("acqOrgNo", dict.getSysValue());
								}
							}
						}
					}
					//校验导入的数据
					Map<String, Object> obj = null;
					String merchantNo = "";
					List<OutBillDetail> detailList = new ArrayList<OutBillDetail>();
					OutBillDetail detail = null;
					BigDecimal outTaskAmount = BigDecimal.ZERO;
					OutBill outBill = outBillService.findOutBillById(outBillId);
					MerchantCardInfo merchantCardInfo = null;
					
					for (int i = 0; i < list.size(); i++) {
						detail = new OutBillDetail();
						merchantNo = list.get(i).get("merchantNo");
						String changeRemark = list.get(i).get("changeRemark");
						if (merchantNo == null || merchantNo.isEmpty()) {
							list.remove(i);
							i--;
							continue;
						}
						obj = extAccountService.findByMerchantNo(merchantNo);
						merchantCardInfo = merchantCardInfoService.getByMerchantNo(merchantNo);
						if (obj == null) {
							detail.setVerifyFlag("2");
							detail.setVerifyMsg("没有找到对应的账户:"+merchantNo);
							detail.setMerchantBalance(BigDecimal.ZERO);
						} else if (!((String)obj.get("accountStatus")).equals("1")) {
							detail.setVerifyFlag("2");
							detail.setVerifyMsg("商户"+merchantNo+"已经锁定");
							detail.setMerchantBalance(JavaUtil.getBigDecimal(obj.get("settlingAmount")));
						} else if ( JavaUtil.getBigDecimal(obj.get("settlingAmount")).compareTo(JavaUtil.getBigDecimal(list.get(i).get("outAccountTaskAmount"))) < 0 ) {
							//出款金额大于账户可用余额
							detail.setVerifyFlag("2");
							detail.setVerifyMsg("商户"+merchantNo+"出款金额大于账户结算中余额");
							detail.setMerchantBalance(JavaUtil.getBigDecimal(obj.get("settlingAmount")));
						} else if (merchantCardInfo == null) {
							//商户没有商户卡信息
							detail.setVerifyFlag("2");
							detail.setVerifyMsg("商户"+merchantNo+"没有绑定银行卡");
							detail.setMerchantBalance(JavaUtil.getBigDecimal(obj.get("settlingAmount")));
						} else {
							detail.setVerifyFlag("1");
							detail.setVerifyMsg("校验成功");
							detail.setMerchantBalance(JavaUtil.getBigDecimal(obj.get("settlingAmount")));
						}
						detail.setOutBillId(outBillId);
						detail.setAcqOrgNo(list.get(i).get("acqOrgNo"));
						detail.setMerchantNo(merchantNo);
						detail.setChangeRemark(changeRemark);
						detail.setCreateTime(new Date());
						detail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
						detail.setExportStatus(0);
						detail.setOutBillStatus(0);
						detail.setOutAccountTaskAmount(JavaUtil.getBigDecimal(list.get(i).get("outAccountTaskAmount")));
						outTaskAmount = outTaskAmount.add(detail.getOutAccountTaskAmount());
						detailList.add(detail);
					}
					
					if (outTaskAmount.compareTo(outBill.getOutAccountTaskAmount()) > 0) {
						result.put("status", false);
						result.put("msg", "出账单文件中出账任务总金额超过"+outBill.getOutAccountTaskAmount());
						return result;
					}
					
					//获取到登录者信息
					UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
							.getAuthentication().getPrincipal();
					String uname = userInfo.getUsername();
					//调用批量更新
					BigDecimal re = outBillDetailService.updateBatch(outBillId, detailList,uname);
					if (re != null && re.compareTo(BigDecimal.ZERO) > 0) {
						result.put("status", true);
						result.put("msg", "导入完成，" + list.size() + "条成功，" + errorList.size() + "失败");
						result.put("newAmount", re);
					} else {
						result.put("status", false);
						result.put("msg", "导入失败");
					}
				} else {
					result.put("status", false);
					result.put("msg", "导入失败");
				}
			} catch (Exception e) {
				result.put("status", false);
				result.put("msg", "导入的文件格式有误");
			}

			return result;
		}
	*/
	
	//导入出账单明细数据  新
	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "importOutBillDetailFile.do")
	@ResponseBody
	public Map<String, Object> importOutBillDetailFiles(
			HttpServletRequest request,
			MultipartFile file,
			@RequestParam Integer outBillId) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		String fileName = file.getOriginalFilename();
		try {
			if(!fileName.endsWith(".xlsx")){
				result.put("statu",false);
				result.put("msg","请导入正确格式账单excel文件!");
				return result;
			}
			File temp = File.createTempFile(file.getName(), ".xls");
			file.transferTo(temp);
			map = resolveOutBillFile(temp);
			result = leadingInOutBillDetails(outBillId, map);
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("导入异常 " + e.getMessage());
			result.put("status", false);
			result.put("msg", "导入异常");
		}

		return result;
	}
	@SuppressWarnings("unchecked")
	private Map<String, Object> leadingInOutBillDetails(Integer outBillId, Map<String, Object> map)throws Exception {

		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> resultMapData = new HashMap<String, Object>();
		List<SubOutBillDetail> subOutBillDetailsExcel = new ArrayList<SubOutBillDetail>();
		subOutBillDetailsExcel = (List<SubOutBillDetail>) map.get("subOutBillDetails");
		BigDecimal oddAmount = BigDecimal.ZERO;
		String returnResult = "";
		Map<Integer, BigDecimal> tempData = new HashMap<Integer, BigDecimal>();  //用来临时存储出款通道的出账任务金额
		List<AcqOutBill> list = acqOutBillService.findByOutBillId(outBillId);
		OutBill outBill = outBillService.findOutBillTaskById(outBillId);
		for (AcqOutBill acqOutBill : list) {
			BigDecimal upBalance = BigDecimal.ZERO;
			upBalance = acqOutBill.getUpBalance().subtract(acqOutBill.getCalcOutAmount());
			tempData.put(outBillId, upBalance);
		}
		for (SubOutBillDetail subOutBillDetailExcel : subOutBillDetailsExcel) {
			DuiAccountDetail returnDuiAccountDetail =  new DuiAccountDetail();
			returnDuiAccountDetail = duiAccountDetailService.queryDuiAccountDetailByOrderRefNum(subOutBillDetailExcel);
			if(returnDuiAccountDetail == null){
				resultMapData.put(subOutBillDetailExcel.getOrderReferenceNo(), "该笔交易不满足出款条件或者不存在！");
			}else{
				if ("1".equals(returnDuiAccountDetail.getOutBillStatus())){
					resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易状态已经是：出款成功！");
				}else if("3".equals(returnDuiAccountDetail.getOutBillStatus())){
					resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易状态已经是：出款中！");
				}else{
					if("1".equals(returnDuiAccountDetail.getIsAddBill())){
						resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易已经在其他出账单存在！");
					}else{
						if("ZF_ZQ".equalsIgnoreCase(outBill.getAcqEnname()) || "YS_ZQ".equalsIgnoreCase(outBill.getAcqEnname())
								|| "SFT_ZQ".equalsIgnoreCase(outBill.getAcqEnname()) ){//出账单属于中付直清或者银盛直清

							if(!outBill.getAcqEnname().equals(returnDuiAccountDetail.getAcqEnname())){
								resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该交易不属于此直清通道，请检查！");
								continue;
							}

							if ("ZF_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname())  || "YS_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname())
									|| "SFT_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname())  ){
                                Map<String,Object> zfMer = merchantInfoService.queryQrMerchantInfo(returnDuiAccountDetail.getPlateAcqMerchantNo());
								if(!"1".equals(zfMer.get("sync_status"))){
                                    resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易对应的商户未与上游同步状态");
                                    continue;
                                }
	                               if("ZF_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname())){
	                            	   if("T1".equals(outBill.getOutBillRange())){
	                                       if(!DateUtil.isZfTomorrow(returnDuiAccountDetail.getPlateAcqTransTime())){
	                                           resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易应选择T1出账单！");
	                                           continue;
	                                       }
	                                   }else if("Tn".equals(outBill.getOutBillRange())){
	                                       if(DateUtil.isZfTomorrow(returnDuiAccountDetail.getPlateAcqTransTime())){
	                                           resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易应选择Tn出账单！");
	                                           continue;
	                                       }
	                                   }
	                               }
                                
							}else{
                                resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易不能加入直清通道！");
                                continue;
                            }
						}else{
                            if ("ZF_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname()) || "YS_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname())
									|| "SFT_ZQ".equalsIgnoreCase(returnDuiAccountDetail.getAcqEnname()) ){
                                resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易只能加入直清通道！");
                                continue;
                            }
                        }

						// 这里还要判断该通道  金额 是否足够，如足够才能导入
						oddAmount = tempData.get(outBillId);
						BigDecimal tranOutAmount = BigDecimal.ZERO;
						tranOutAmount = tranOutAmount.add(returnDuiAccountDetail.getPlateTransAmount().subtract(returnDuiAccountDetail.getPlateMerchantFee()));
						if (tranOutAmount.compareTo(oddAmount) <= 0) {
							//插入子出账单明细，更新计算出账金额，更新对账详情表中的  是否添加出账的 状态  这些需要在同一事物下进行
							returnResult = subOutBillDetailService.insertAndUpdateRecordStatus(returnDuiAccountDetail,outBillId);
							if("0".equals(returnResult)){
								resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "没有找到对应的账户:"+returnDuiAccountDetail.getPlateMerchantNo());
							}else if("-2".equals(returnResult)){
								resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "账户:"+returnDuiAccountDetail.getPlateMerchantNo() + "可用余额不足！");
							}else if("-3".equals(returnResult)){
								resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "账户:"+returnDuiAccountDetail.getPlateMerchantNo() + "不合法或者不存在！");
							}else{
								oddAmount = oddAmount.subtract(returnDuiAccountDetail.getPlateTransAmount());  //减去该商户的金额
								tempData.put(outBillId, oddAmount);  //存入临时一个map集合
								resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该笔交易导入成功！");
							}
						}else{
							resultMapData.put(returnDuiAccountDetail.getOrderReferenceNo(), "该出账单没有足够的金额，导入失败！");
						}
					}
				}
			}
		}
		result.put("status", true);
		result.put("msg", "操作成功，请查看结果列表！");
		result.put("resultMapData", resultMapData);
		return result;
   }

	private Map<String, Object> resolveOutBillFile(File temp) {
		return chuAccountAssemblyOrParsing.resolveOutBillFile(temp);
	}

/*	*//**
	 * 导出出账单详情数据
	 * @param params
	 * @param response
	 * @param request
	 * @throws Exception 
	 *//*
	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "exportOutBillDetail.do",method = RequestMethod.POST)
	public void exportOutBillDetail(@ModelAttribute OutBillDetail outBillDetail,
			@RequestParam Map<String, String> params,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {

		List<SysDict> acqOrgList = null;
		List<AcqOutBill> acqOutBillList = null;
		List<String> finalOrgList = new ArrayList<String>();
		List<SysDict> acqOrgList_ = new ArrayList<SysDict>();
		String[] textList = {};
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			Sort sort = new Sort();
			sort.setSord("asc");
			acqOutBillList = acqOutBillService.findByOutBillId(outBillDetail.getOutBillId());

			for (SysDict item1 : acqOrgList) {
				for (AcqOutBill item2 : acqOutBillList) {
					if (item2.getAcqOrgNo().equals(item1.getSysValue())) {
						finalOrgList.add(item1.getSysName());
						acqOrgList_.add(item1);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		textList = (String[]) finalOrgList.toArray(new String[finalOrgList.size()]);
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;

		String fileName = "出账单详情_"+outBillDetail.getOutBillId()+"_"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");

		List<OutBillDetail> list = new ArrayList<OutBillDetail>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		ExtAccount ext = null;

		//从数据库中查询数据
		try {
			list = outBillDetailService.findExportOutBillDetailList(params, outBillDetail) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		Map<String,String> map = null;
		for(OutBillDetail item : list){
			map = new HashMap<String,String>() ;
			map.put("id", item.getId().toString()) ;
			map.put("merchantNo", item.getMerchantNo()) ;
			ext = extAccountService.findExtAccountByMerchantNo(item.getMerchantNo());
			map.put("merchantBalance", ext.getSettlingAmount().toString()) ;
			map.put("outAccountTaskAmount", item.getOutAccountTaskAmount().toString()) ;
			map.put("acqOrgNo", format.formatSysDict(item.getAcqOrgNo(), acqOrgList_)) ;
			map.put("changeRemark", item.getChangeRemark()) ;
			map.put("verifyFlag", item.getVerifyFlag() != null ? ("0".equals(item.getVerifyFlag()) ? "校验失败" :"校验成功") : "未校验");
			map.put("verifyMsg", item.getVerifyMsg());
			data.add(map) ;
		}

		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","merchantNo","merchantBalance","outAccountTaskAmount","acqOrgNo","changeRemark","verifyFlag","verifyMsg"};
		String[] colsName = new String[]{"出账单明细",  "商户编号","商户结算中金额",  "商户出账金额",  "出款通道",  "财务调整备注", "校验标志", "校验结果"};
		
		export.setHSSFValidation(textList, 0, 10000, 4, 4);
		export.export(cols, colsName, data, response.getOutputStream());
	}*/

	/**
	 * 导出出账单详情数据
	 * @param params
	 * @param response
	 * @param request
	 * @throws Exception 
	 */
	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "exportOutBillDetail.do",method = RequestMethod.POST)
	public void exportOutBillDetail(@ModelAttribute SubOutBillDetail subOutBillDetail,
			@RequestParam Map<String, String> params,
			HttpServletResponse response,
			HttpServletRequest request) throws Exception {

		List<SysDict> acqOrgList = null;
		List<AcqOutBill> acqOutBillList = null;
		List<String> finalOrgList = new ArrayList<String>();
		List<SysDict> acqOrgList_ = new ArrayList<SysDict>();
		String[] textList = {};
		try {
			acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
			Sort sort = new Sort();
			sort.setSord("asc");
			acqOutBillList = acqOutBillService.findByOutBillId(subOutBillDetail.getOutBillId());

			for (SysDict item1 : acqOrgList) {
				for (AcqOutBill item2 : acqOutBillList) {
					if (item2.getAcqOrgNo().equals(item1.getSysValue())) {
						finalOrgList.add(item1.getSysName());
						acqOrgList_.add(item1);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		textList = (String[]) finalOrgList.toArray(new String[finalOrgList.size()]);
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "出账单详情_"+subOutBillDetail.getOutBillId()+"_"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");

		List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		ExtAccount ext = null;
		String merchantNo = (String) params.get("merchantNo");
		String acqOrgNo = (String) params.get("acqOrg");
		String merchantBalance1 = (String) params.get("merchantBalance1");
		String merchantBalance2 = (String) params.get("merchantBalance2");
		String outAccountTaskAmount1 = (String) params
				.get("outAccountTaskAmount1");
		String outAccountTaskAmount2 = (String) params
				.get("outAccountTaskAmount2");
		String timeStart = params.get("start") ;
		String timeEnd = params.get("end") ;

		String isChangeRemark = (String) params.get("isChangeRemark");
		
		List<SysDict> recordStatusList = new ArrayList<SysDict>() ;
		List<SysDict> outBillStatusList = new ArrayList<SysDict>() ;
		recordStatusList = sysDictService.findSysDictGroup("out_bill_record_status");
		outBillStatusList = sysDictService.findSysDictGroup("out_bill_status");
		
		
		try {
			        subOutBillDetailList = subOutBillDetailService.exportSubOutBillDetailList(subOutBillDetail,
					merchantNo, acqOrgNo, 
					merchantBalance1, merchantBalance2, outAccountTaskAmount1,
					outAccountTaskAmount2, isChangeRemark,timeStart,timeEnd);

			Map<String,String> map = null;
			for(SubOutBillDetail item : subOutBillDetailList){
				map = new HashMap<String,String>() ;
				map.put("id", item.getId().toString()) ;
				map.put("outBillDetailId", item.getOutBillDetailId()) ;
				map.put("merchantNo", item.getMerchantNo()) ;
				map.put("plateMerchantEntryNo", item.getPlateMerchantEntryNo()) ;
				map.put("acqMerchantNo", item.getAcqMerchantNo()) ;
				map.put("merchantBalance", item.getMerchantBalance() == null ? "" :item.getMerchantBalance().toString()) ;
				map.put("outAccountTaskAmount", item.getOutAccountTaskAmount().toString()) ;
				map.put("acqOrgNo", format.formatSysDict(item.getAcqOrgNo(), acqOrgList_)) ;
				map.put("outAccountTaskAmount1", item.getOutAccountTaskAmount().toString()) ;
				map.put("orderReferenceNo", item.getOrderReferenceNo()) ;
				map.put("transAmount", item.getTransAmount().toString()) ;
				map.put("transTime", item.getTransTime()==null?"":df.format(item.getTransTime())) ;
				map.put("verifyFlag", item.getVerifyFlag() != null ? ("0".equals(item.getVerifyFlag()) ? "校验失败" :"校验成功") : "未校验");
				map.put("verifyMsg", item.getVerifyMsg());
				map.put("changeRemark", item.getChangeRemark()) ;
				map.put("recordStatus", format.formatSysDict(item.getRecordStatus() == null ? "" : item.getRecordStatus().toString(), recordStatusList)) ;
				map.put("outBillResult", format.formatSysDict(item.getOutBillStatus() == null ? "" : item.getOutBillStatus().toString(), outBillStatusList)) ;
				data.add(map) ;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}

		ListDataExcelExport export = new ListDataExcelExport();

		String[] cols = new String[]{"id","outBillDetailId","merchantNo","plateMerchantEntryNo","acqMerchantNo","merchantBalance","outAccountTaskAmount",
				"acqOrgNo","outAccountTaskAmount1","orderReferenceNo"
				,"transAmount","transTime","verifyFlag","verifyMsg","changeRemark","recordStatus","outBillResult"};
		String[] colsName = new String[]{"子出账单明细","出账单明细",  "商户编号","商户进件编号","银联报备商户编号","商户结算中金额","出账任务金额","出款通道","商户出账金额","订单参考号","交易金额","交易时间","校验通过","校验错误信息","财务调整备注","记账状态","出账结果"};

		export.setHSSFValidation(textList, 0, 10000, 5, 5);
		export.export(cols, colsName, data, response.getOutputStream());
	}
	private static String getStringCell(Cell cell) {
		if (cell != null)
			cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell != null ? cell.getStringCellValue() : null;
	}



	@PreAuthorize("hasAuthority('chuAccountBillManage:update')")
	@RequestMapping(value = "/outBillDetailChuAmount.do")
	@ResponseBody
	public Map<String, Object> outBillDetailChuAmount(@RequestParam(value = "id")String id) {
		Map<String, Object> map = new HashMap<String, Object>();
		OutBillDetail detail = outBillDetailService.findOutBillDetailById(id);
		List<OutAccountService> outAccountServiceList = outAccountServiceService.findOutAccountServiceListByAcqEnname(detail.getAcqOrgNo());
		for (OutAccountService outAccountService : outAccountServiceList) {
		
			//OutAccountService outAccountService = 
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			String uname = userInfo.getUsername();
			JWTSigner signer = new JWTSigner(core2HttpSecret);
			ObjectMapper om = new ObjectMapper();
			try {
				if (outAccountService.getServiceType() == 4) {
					//线上单笔代付
					//继续调用T1线上结算接口
					String t1online = core2ApiHttpUrl+"/settle/commitT1UpSettleMoney";
					Map<String, Object> t1onparams = null;
	
					MerchantInfo merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					t1onparams = new HashMap<String, Object>();
					t1onparams.put("settleUserNo", detail.getMerchantNo());
					t1onparams.put("settleUserType", "M");
					t1onparams.put("settleType", "3");
					t1onparams.put("sourceSystem", "account");
					t1onparams.put("createUser", userInfo.getUsername());
					t1onparams.put("settleAmount", detail.getOutAccountTaskAmount().toString());
					if (merInfo == null) {
						map.put("status", false);
						map.put("msg", "商户"+detail.getMerchantNo()+"不存在");
						return map;
					} else {
						t1onparams.put("agentNode", merInfo.getAgentNo());
					}
					t1onparams.put("holidaysMark", "0");
					t1onparams.put("acqenname", detail.getAcqOrgNo());
					t1onparams.put("sourceOrderNo", detail.getId().toString());
					t1onparams.put("sourceBatchNo", detail.getOutBillId().toString());
					String token = signer.sign(t1onparams);
					String t1response = HttpConnectUtil.postHttp(t1online, "token", token);
	
					Map<String, Object> resp = om.readValue(t1response, Map.class);
					if (resp == null || "".equals(resp) || (boolean)resp.get("status") == false) {
						outBillDetailService.updateOutBillResultById("T1线上结算：出款失败",2, detail.getId()); //出账失败
						map.put("status", false);
						map.put("msg", "T1线上结算：出款失败");
					} else {
						outBillDetailService.updateOutBillResultById("T1线上结算：已提交出款系统",3, detail.getId());
						map.put("status", true);
						map.put("msg", "T1线上结算:已提交出款系统");
					}
				} else if (outAccountService.getServiceType() == 5){
					//继续调用T1线下结算接口
					String t1offline = core2ApiHttpUrl+"/settle/commitT1DownSettleMoney";
					Map<String, Object> t1offparams = null;
					//循环出账单明细，调用
					MerchantInfo merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					t1offparams = new HashMap<String, Object>();
					t1offparams.put("settleUserNo", detail.getMerchantNo());
					t1offparams.put("settleUserType", "M");
					t1offparams.put("settleType", "4");
					t1offparams.put("sourceSystem", "account");
					t1offparams.put("createUser", userInfo.getUsername());
					t1offparams.put("settleAmount", detail.getOutAccountTaskAmount().toString());
					if (merInfo == null) {
						map.put("status", false);
						map.put("msg", "商户"+detail.getMerchantNo()+"不存在");
						return map;
					} else {
						t1offparams.put("agentNode", merInfo.getAgentNo());
					}
					t1offparams.put("agentNode", merInfo.getAgentNo());
					t1offparams.put("acqenname", detail.getAcqOrgNo());
					t1offparams.put("sourceOrderNo", detail.getId().toString());
					t1offparams.put("sourceBatchNo", detail.getOutBillId().toString());
					t1offparams.put("outBillStatus", "");
					t1offparams.put("holidaysMark", "0");
	
					String token2 = signer.sign(t1offparams);
					String t1response = HttpConnectUtil.postHttp(t1offline, "token", token2);
					Map<String, Object> resp2 = om.readValue(t1response, Map.class);
					if (resp2 == null || "".equals(resp2) || (boolean)resp2.get("status") == false) {
						outBillDetailService.updateOutBillResultById("T1线下结算：出款失败",2, detail.getId()); //出账失败
						map.put("status", false);
						map.put("msg", "出款失败：T1线下结算执行失败");
						log.info(map.toString());
					} else {
						outBillDetailService.updateOutBillResultById("T1线下结算：出款成功",1, detail.getId()); //出账失败
						map.put("status", true);
						map.put("msg", "T1线下结算：出款成功");
						log.info(map.toString());
						//查询所有的出账单详情，判断是否已经全部出款，如果是，则修改出账单状态为1
						List<OutBillDetail> detailList = outBillDetailService.findByOutBillIdAndStatus(detail.getOutBillId(), 2);
						if (detailList == null || detailList.isEmpty()) {
							outBillService.updateOutBillStatus(detail.getOutBillId(),uname);
						}
					}
				} else {
					map.put("status", false);
					map.put("msg", "该出账单详情上游出款服务信息异常");
					log.info(map.toString());
				}
			
			} catch (Exception e) {
				map.put("status", false);
				map.put("msg", "出款操作异常");
				log.info(map.toString());
			} 
		}
		return map;
	}

	/**
	 * 导出数据(出款失败记录查询)
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('errorMerchantChuAccountResult:export')")
	@RequestMapping(value = "exportErrorMerchantChuAccountResult.do",method = RequestMethod.POST)
	public void exportErrorMerchantDetail(@RequestParam Map<String,String> params ,
			@ModelAttribute SubOutBillDetailLogs subOutBillDetailLogs, 
			HttpServletResponse response,HttpServletRequest request) throws IOException {
		
		
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "出款失败记录查询_"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss") ;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			String merchantNo = (String) subOutBillDetailLogs.getMerchantNo();
			String merchantName = (String) subOutBillDetailLogs.getMerchantName();
			String mobile = (String) subOutBillDetailLogs.getMobile();
			List<String> merNoList = null;
			//先查询代理商数据库，根据条件查询商户list
			if(StringUtils.isBlank(merchantNo)&&StringUtils.isBlank(merchantName)&&StringUtils.isBlank(mobile)){
				subOutBillDetailLogs.setMerNos("-1");
			}else{
				merNoList = merchantInfoService.findByNameAndMobile(merchantNo, merchantName, mobile);
				StringBuilder sb = new StringBuilder();
				if (merNoList != null && merNoList.size() > 0) {
					for (String s : merNoList) {
						sb.append("'");
						sb.append(s);
						sb.append("',");
					}
					subOutBillDetailLogs.setMerNos(sb.substring(0, sb.length() - 1));
				} else {
					subOutBillDetailLogs.setMerNos("-1");
				}
			}

			Map<String, String> tempMap = null;
			MerchantInfo merInfo = null;
			MerchantCardInfo merCardInfo = null;
			List<SysDict> outBillStatusList = sysDictService.findSysDictGroup("out_bill_status");
			//查询出账单明细
			List<SubOutBillDetailLogs> list = subOutBillDetailLogsService.exportOutBillDetailLogsList(subOutBillDetailLogs);
			if (list != null && list.size() > 0) {
				for (SubOutBillDetailLogs detail : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("subOutBillDetailId", detail.getSubOutBillDetailId());
					tempMap.put("outBillId", detail.getOutBillId() == null? "":detail.getOutBillId().toString());
					tempMap.put("outBillDetailId", detail.getOutBillDetailId());
					tempMap.put("merchantNo", detail.getMerchantNo());
					tempMap.put("outAccountTaskAmount", detail.getOutAccountTaskAmount() == null? "":detail.getOutAccountTaskAmount().toString());
					tempMap.put("outBillStatus", format.formatSysDict(detail.getOutBillStatus() == null ? "":detail.getOutBillStatus().toString(), outBillStatusList));
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("settleTime", detail.getSettleTime()==null?"":df.format(detail.getSettleTime()));
					tempMap.put("orderReferenceNo", detail.getOrderReferenceNo());
					tempMap.put("transAmount", detail.getTransAmount() == null? "":detail.getTransAmount().toString());
					tempMap.put("transTime", detail.getTransTime()==null?"":df.format(detail.getTransTime()));
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("merchantName", detail.getMerchantName());
					tempMap.put("mobile", detail.getMobile());
					tempMap.put("outAccountNote", detail.getOutAccountNote());
					merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					merCardInfo = merchantCardInfoService.getByMerchantNo(detail.getMerchantNo());
					if (merInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("merchantName", merInfo.getMerchantName());
						tempMap.put("mobile", merInfo.getMobilephone());
					}
					if (merCardInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("cardNo", merCardInfo.getAccountNo());
						tempMap.put("cardName", merCardInfo.getAccountName());
					}
					data.add(tempMap);
				}
			}
			} catch (Exception e) {
				log.error("异常:",e);
			}	
		
		
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{
				  "subOutBillDetailId","outBillId","outBillDetailId","merchantNo","cardNo"
				  ,"outAccountTaskAmount","outBillStatus","acqOrgNo","settleTime","orderReferenceNo"
				  ,"transAmount","transTime","acqOrgNo","merchantName","mobile","cardName","outAccountNote"};
		 
		  String[] colsName = new String[]{
				  "子出账单明细", "出账单ID","出账单明细", "商户编号","结算卡号", "商户出账金额",
					"出款状态", "出账通道", "结算时间","订单参考号", "交易金额",
					"交易时间", "交易通道", "商户名称","商户手机号", "银行户名", "出账备注" };
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}
	
	
	/**
	 * 导出数据(出款失败记录查询)test
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('merchantChuAccountResult:export')")
	@RequestMapping(value = "exportMerchantChuAccountResult.do",method = RequestMethod.POST)
	public void exportMerchantChuAccountResult(@RequestParam Map<String,String> params ,
			@ModelAttribute SubOutBillDetail subOutBillDetail, 
			HttpServletResponse response,HttpServletRequest request) throws IOException {
		
		
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "商户出账结果查询_"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		try {
			String merchantNo = (String) subOutBillDetail.getMerchantNo();
			String merchantName = (String) subOutBillDetail.getMerchantName();
			String mobile = (String) subOutBillDetail.getMobile();
			List<String> merNoList = null;
			//先查询代理商数据库，根据条件查询商户list
			if(StringUtils.isBlank(merchantNo)&&StringUtils.isBlank(merchantName)&&StringUtils.isBlank(mobile)){
				subOutBillDetail.setMerNos("-1");
			}else {
				merNoList = merchantInfoService.findByNameAndMobile(merchantNo, merchantName, mobile);
				StringBuilder sb = new StringBuilder();
				if (merNoList != null && merNoList.size() > 0) {
					for (String s : merNoList) {
						sb.append("'");
						sb.append(s);
						sb.append("',");
					}
					subOutBillDetail.setMerNos(sb.substring(0, sb.length() - 1));
				} else {
					subOutBillDetail.setMerNos("-1");
				}
			}
			Map<String, String> tempMap = null;
			MerchantInfo merInfo = null;
			MerchantCardInfo merCardInfo = null;
			List<SysDict> outBillStatusList = sysDictService.findSysDictGroup("out_bill_status");
			List<SysDict> isAddBillList = sysDictService.findSysDictGroup("is_add_bill");
			//查询出账单明细
			List<SubOutBillDetail> list = subOutBillDetailService.exportOutBillDetailList(subOutBillDetail);
			if (list != null && list.size() > 0) {
				for (SubOutBillDetail detail : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("id", detail.getId());
					tempMap.put("outBillId", detail.getOutBillId() == null? "":detail.getOutBillId().toString());
					tempMap.put("outBillDetailId", detail.getOutBillDetailId());
					tempMap.put("merchantNo", detail.getMerchantNo());
					tempMap.put("outAccountTaskAmount", detail.getOutAccountTaskAmount() == null? "":detail.getOutAccountTaskAmount().toString());
					tempMap.put("outBillStatus", format.formatSysDict(detail.getOutBillStatus() == null ? "":detail.getOutBillStatus().toString(), outBillStatusList));
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("isAddBill", format.formatSysDict(detail.getIsAddBill() == null ? "":detail.getIsAddBill().toString(), isAddBillList));
					tempMap.put("settleTime", detail.getSettleTime()==null?"":df.format(detail.getSettleTime()));
					tempMap.put("orderReferenceNo", detail.getOrderReferenceNo());
					tempMap.put("transAmount", detail.getTransAmount() == null? "":detail.getTransAmount().toString());
					tempMap.put("transTime", detail.getTransTime()==null?"":df.format(detail.getTransTime()));
					tempMap.put("acqOrgNo", detail.getAcqOrgNo());
					tempMap.put("merchantName", detail.getMerchantName());
					tempMap.put("mobile", detail.getMobile());
					tempMap.put("outAccountNote", detail.getOutAccountNote());
					tempMap.put("plateMerchantEntryNo", detail.getPlateMerchantEntryNo());
					tempMap.put("acqMerchantNo", detail.getAcqMerchantNo());
					merInfo = merchantInfoService.findMerchantInfoByUserId(detail.getMerchantNo());
					merCardInfo = merchantCardInfoService.getByMerchantNo(detail.getMerchantNo());
					if (merInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("merchantName", merInfo.getMerchantName());
						tempMap.put("mobile", merInfo.getMobilephone());
					}
					if (merCardInfo == null) {
						tempMap.put("cardNo", "");
						tempMap.put("cardName", "");
					} else {
						tempMap.put("cardNo", merCardInfo.getAccountNo());
						tempMap.put("cardName", merCardInfo.getAccountName());
					}
					data.add(tempMap);
				}
			}
			} catch (Exception e) {
				log.error("异常:",e);
			}	
		
		
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{
				  "id","outBillId","outBillDetailId","merchantNo","plateMerchantEntryNo","acqMerchantNo","isAddBill","cardNo"
				  ,"outAccountTaskAmount","outBillStatus","acqOrgNo","settleTime","orderReferenceNo"
				  ,"transAmount","transTime","acqOrgNo","merchantName","mobile","cardName","outAccountNote"};
		 
		  String[] colsName = new String[]{
				  "子出账单明细", "出账单ID","出账单明细", "商户编号","平台商户进件编号","银联报备商户编号","加入出账单","结算卡号", "商户出账金额",
					"出款状态", "出账通道", "结算时间","订单参考号", "交易金额",
					"交易时间", "交易通道", "商户名称","商户手机号", "银行户名", "出账备注" };
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}

	@PreAuthorize("hasAuthority('chuAccountFileDown:query')")
	@RequestMapping(value = "/toChuAccountFileDown.do")
	public String toDuiAccountFileDown(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);

		return "chuAccount/chuAccountFileDown";
	}

	@PreAuthorize("hasAuthority('chuAccountFileDown:query')")
	@RequestMapping(value = "findChuAccountFileDownList.do")
	@ResponseBody
	public Page<MyFile> findDuiAccountFileDownList(@ModelAttribute MyFile myFile,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<MyFile> page){
		try {
			String acqOrg = params.get("acqOrg");
			final String fileName = params.get("fileName");

			SysDict sysDict = sysDictService.findSysDictByKeyValue("ftp", acqOrg + ":ftp");
			String ftp = sysDict.getSysName();
			List<MyFile> list = new ArrayList<>();
			if (!StringUtils.isEmpty(ftp)) {

				String[] ftpInfo = ftp.split(",");
				if ("SFT_ZQ".equals(acqOrg)) {
					try {
						String dirName = "/download/statement";
						List<ChannelSftp.LsEntry> dirEntries = FtpUtil.connect(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3], dirName);
						for (ChannelSftp.LsEntry fileEntry : dirEntries) {
							MyFile file = new MyFile();
							if (!fileEntry.getFilename().endsWith(".")) {
								if(fileEntry.getFilename().contains("ACO")&&fileEntry.getFilename().contains("DF")){
									file.setName(fileEntry.getFilename());
									SftpATTRS sftpATTRS = fileEntry.getAttrs();
									long mTime = sftpATTRS.getMTime();
									Date mtime = new Date(mTime * 1000);
									long size = sftpATTRS.getSize();
									file.setCreateDate(mtime);
									file.setSize(size);
									list.add(file);
								}
							}
						}

					} catch (Exception e) {
						log.error("", e);
					}
				} else {
					FtpUtil fu = new FtpUtil(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
					try {
						if (fu.connect()) {
							list = fu.getDFFileList("");
							fu.disconnect();
						} else {
							log.info("无法连接到ftp");
						}

					} catch (Exception e) {
						log.info(acqOrg + "暂时无法获取ftp数据，请反馈稍后再试" + e.toString());
						log.error("异常:", e);
					}
				}

				Predicate condition = new Predicate() {
					@Override
					public boolean evaluate(Object obj) {
						return ((MyFile) obj).getName().contains(fileName);
					}
				};
				List<MyFile> resultList = (List<MyFile>) CollectionUtils.select(list, condition);

				Collections.sort(resultList);

				page.setTotalCount(resultList.size());
				int fromIndex = (page.getPageNo() - 1) * page.getPageSize();
				int toIndex = page.getPageNo() * page.getPageSize();
				if (page.getPageNo() >= page.getTotalPages()) {
					toIndex = page.getTotalCount();
				}
				page.setResult(resultList.subList(fromIndex, toIndex));

			}

		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}


	/**
	 * 出款对账文件下载
	 */
	@PreAuthorize("hasAuthority('chuAccountFileDown:download')")
	@RequestMapping(value = "chuAccountFileDown.do")
	public void DuiAccountFileDown(@RequestParam Map<String,String> params, HttpServletResponse response,HttpServletRequest request) {

		String fileName = params.get("fileName");
		String acqOrg = params.get("acqOrg");

		String tempurl = "/account.temp/";
		String filePath = tempurl + fileName;

		OutputStream os = null;
		InputStream is = null;
		File f = null;
		try {
			SysDict sysDict = sysDictService.findSysDictByKeyValue("ftp", acqOrg + ":ftp");
			String ftp = sysDict.getSysName();
			String[] ftpInfo = ftp.split(",");
			if("SFT_ZQ".equals(acqOrg)){
				filePath = "/download/statement/"+fileName;
				request.setCharacterEncoding("UTF-8");
				os = response.getOutputStream(); // 取得输出流
				response.reset(); // 清空输出流
				response.setHeader("Content-disposition", "attachment;filename="
						+ new String(fileName.getBytes("GBK"), "ISO8859-1"));
				response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
				FtpUtil.sftpDown(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3], filePath,os);
				os.close();
				return;
			}
			FtpUtil fu = new FtpUtil(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);

			fu.connect();
			boolean download = fu.download(fileName, filePath);
			if (download) {
				request.setCharacterEncoding("UTF-8");
				os = response.getOutputStream(); // 取得输出流
				response.reset(); // 清空输出流
				response.setHeader("Content-disposition", "attachment;filename="
						+ new String(fileName.getBytes("GBK"), "ISO8859-1"));
				response.setContentType("application/msexcel;charset=UTF-8");// 定义输出类型
				f = new  File(filePath);
				is =  new FileInputStream(f);
				byte[] b = new byte[is.available()];
				is.read(b);
				os.write(b);
			}
			fu.disconnect();
		} catch (IOException e) {
			log.error("异常:",e);
		} catch (Exception e) {
			log.error("异常:",e);
		} finally {
			if(os != null){
				try {
					os.close();
					if(is!=null){
						is.close();
					}
					String[] fileNames = FileUtil.getFiles(tempurl);
					for (String file : fileNames) {
						File fl = new File(file);
						if (fl.exists()) {
							fl.delete();
						}
					}
				} catch (IOException e) {
					log.error("异常:",e);
				}
			}
		}
	}

}
