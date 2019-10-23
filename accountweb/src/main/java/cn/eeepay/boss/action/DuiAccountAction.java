package cn.eeepay.boss.action;


import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.boss.util.Constants;
import cn.eeepay.boss.util.FileNameUtil;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.TransInfoPreFreezeLog;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.bill.impl.ExtAccountServiceImpl;
import cn.eeepay.framework.service.nposp.*;
import cn.eeepay.framework.util.*;
import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpATTRS;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
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
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对账管理
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年5月18日16:49:01
 *
 */
@Controller
@RequestMapping(value = "/duiAccountAction")
public class DuiAccountAction {
	@Resource
	public SysDictService sysDictService;
	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	@Resource
	public DuiAccountBatchService duiAccountBatchService;
	@Resource
	public DuiAccountAssemblyOrParsing duiAccountAssemblyOrParsing;
	@Autowired
	public ScanCodeTransService scanCodeTransService;
	@Autowired
	public CollectionTransOrderService collectionTransOrderService;
	@Resource
	public ExtAccountService eaService ;
	@Resource
	public ExtAccountServiceImpl eaServiceImpl ;
	@Resource
	public TransInfoService transInfoService ;
	@Resource
	private AcqServiceService acqServiceService;
	@Resource
	public MerchantInfoService merchantInfoService;
	@Resource
	public AcqOrgService acqOrgService;
	
	@Resource
	private OptLogsService optLogsService;
	
	@Resource
	private DuiAccountService duiAccountService;

	@Resource
	private AgentInfoService agentInfoService;

    @Autowired
    private SysConfigService sysConfigService;

	
	@Value("${accountApi.http.url}")  
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")  
	private String accountApiHttpSecret;

	@Value("${ds.trans.download}")
	private String dsTransDownload;

	@Value("${merchant.numbers}")
	private String merchantNumbers;

	@Value("${zy.trans.download}")
	private String zyTransDownload;

	@Value("${zy.merchant.numbers}")
	private String zyMerchantNumbers;

	@Value("${zyld.merchant.numbers}")
	private String zyLdMerchantNumbers;

	private static final Logger log = LoggerFactory.getLogger(DuiAccountAction.class);


	@PreAuthorize("hasAuthority('duiAccountFileDown:query')")
	@RequestMapping(value = "/toDuiAccountFileDown.do")
	public String toDuiAccountFileDown(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		
		return "duiAccount/duiAccountFileDown";
	}
	@PreAuthorize("hasAuthority('duiAccountFileDown:query')")
	@RequestMapping(value = "findDuiAccountFileDownList.do")
	@ResponseBody
	public Page<MyFile> findDuiAccountFileDownList(@ModelAttribute MyFile myFile,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<MyFile> page){
		try {
			String acqOrg = params.get("acqOrg");
			final String fileName = params.get("fileName");
			
			SysDict sysDict = sysDictService.findSysDictByKeyValue("ftp", acqOrg + ":ftp");
			String ftp = sysDict.getSysName();
			final List<MyFile> list = new ArrayList<>();
			if (!StringUtils.isEmpty(ftp)) {

				String[] ftpInfo = ftp.split(",");
				if ("SFT_ZQ".equals(acqOrg)) {
					try {
						String dirName = "/download/statement";
						List<ChannelSftp.LsEntry> dirEntries = FtpUtil.connect(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3], dirName);
						for (ChannelSftp.LsEntry fileEntry : dirEntries) {
							MyFile file = new MyFile();
							if (!fileEntry.getFilename().endsWith(".")) {
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

					} catch (Exception e) {
						log.error("", e);
					}
				}else if("ZG_ZQ".equals(acqOrg)){
					try {
//						String format = sdf.format(new Date());
//						String dirName = "/"+format;
						String dirName = "/";
						List<ChannelSftp.LsEntry> dirEntries = FtpUtil.getZGAllFiles(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3], dirName);
						dirEntries.stream().filter(it->
								it.getFilename().endsWith("order.csv") ? true : false
						).forEach(it->{
							MyFile file = new MyFile();
							file.setName(it.getFilename());
							SftpATTRS sftpATTRS = it.getAttrs();
							long mTime = sftpATTRS.getMTime();
							Date mtime = new Date(mTime * 1000);
							long size = sftpATTRS.getSize();
							file.setCreateDate(mtime);
							file.setSize(size);
							list.add(file);
						});

					} catch (Exception e) {
						log.error("", e);
					}
				} else {
					FtpUtil fu = new FtpUtil(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
					try {
						if (fu.connect()) {
							List<MyFile> list1 = fu.getAllFileList("");
							list.addAll(list1);
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
	 * 对账文件下载
	 */
	@PreAuthorize("hasAuthority('duiAccountFileDown:download')")
	@RequestMapping(value = "duiAccountFileDown.do")
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
			}else if("ZG_ZQ".equals(acqOrg)){
				String s = fileName.split("_")[1];
				s = s.substring(0,s.length()-2);
				s = s.substring(0,s.length()-2) +"-"+ s.substring(s.length()-2);
				filePath = "/"+s+"/"+fileName;
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
	@PreAuthorize("hasAuthority('duiAccountFileUpload:insert')")
	@RequestMapping(value = "/toDuiAccountFileUpload.do")
	public String toDuiAccountFileUpload(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		
		return "duiAccount/duiAccountFileUpload";
	}
	//上传模板
	@PreAuthorize("hasAuthority('duiAccountFileUpload:insert')")
	@ResponseBody
	@RequestMapping (value="/duiAccountFileUpload.do", method = RequestMethod.POST)
	public Map<String,Object> duiAccountFileUpload(HttpServletRequest request,@RequestParam final Map<String, String> params) {
		Map<String,Object> result=new HashMap<String, Object>();

		String acqEnname = params.get("acqOrg");		//得仕快捷不需要上传对账文件

		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
				.getAuthentication()
				.getPrincipal();
		String uname=userInfo.getUsername();

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> files = multipartRequest.getFiles("fileupload");
		MultipartFile file=files.get(0);
		String fileName  = file.getOriginalFilename();
		//保存单据
		try {
			//保存模板路径
			File newFile=new File(Constants.ACCOUNT_TEMP+UUID.randomUUID().toString());
			File parentFile =newFile.getParentFile();
			if(!parentFile.exists()){
				parentFile.mkdirs();
			}
			if(!newFile.exists())
				newFile.createNewFile();
			file.transferTo(newFile);//接收文件

			switch (acqEnname) {
				case "KQ_ZQ":
					log.info(acqEnname+"对账开始！");
					log.info("收单机构名称　{} 文件名称　{} 用户名　{}", new Object[] {acqEnname, fileName, uname});
					result = kqzq(acqEnname,fileName,newFile,uname);
					log.info(acqEnname+"对账结束！");
					break;
				case "ZG_ZQ":
					log.info(acqEnname+"对账开始！");
					log.info("收单机构名称　{} 文件名称　{} 用户名　{}", new Object[] {acqEnname, fileName, uname});
					result = kqZg(acqEnname,fileName,newFile,uname);
					log.info(acqEnname+"对账结束！");
					break;
				default:
					result.put("statu",false);
					result.put("msg","收单机构未实现");
					log.info(result.toString());
					break;

			}
		}catch(Exception e){
			result.put("statu",false);
			result.put("msg","导入对账异常！");
			log.error(result.toString());
			log.error(e.getMessage());
		}
		return result;
	}

	private Map<String, Object> kqzq(String acqEnname, String fileName, File newFile, String uname) {
		Map<String,Object> result=new HashMap<String, Object>();
		List<DuiAccountDetail> acqTrans = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		if(!fileName.endsWith(".txt")){
			result.put("statu",false);
			result.put("msg","请上传正确的快钱对账文件");
			return result;
		}
		try{
			resolveKQZQFileTxt(newFile,acqTrans);
			String[] s = fileName.split("_");
			String fileTime = s[2].split("\\.")[0];
			map.put("fileName", fileName);
			fileTime = DateUtil.StrDateToFormatStr(fileTime, "yyyyMMdd", "yyyy-MM-dd");
			map.put("checkFileDate",fileTime );
			map.put("checkAccountDetails", acqTrans);
			map.put("uname",uname);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SysDict acqOrg = sysDictService.findSysDictByKeyName("acq_day_altered_time", acqEnname);
			List<DuiAccountDetail> dbDetails = getCheckDetailTransInfos(acqEnname,acqOrg.getSysValue(),sdf.parse(fileTime));
			String duiResult = duiAccountService.doDuiAccountForKqzq(acqEnname,dbDetails, map);
			if(duiResult.equals("0")){
				result.put("statu",true);
				result.put("msg","对账完毕，请查询对账信息");
			}else if(duiResult.equals("1")){
				result.put("statu",false);
				result.put("msg","该文件已对账完毕，请不要重复对账");
			}
		}catch (Exception e){
			log.error("",e);
			result.put("statu",false);
			result.put("msg","导入对账异常！");
			log.error(acqEnname+"对账异常:",e);
		}
		return result;
	}
	


	/**
	 * 解析快钱对账文件
	 * @param newFile
	 * @param acqTrans 
	 * @throws Exception
	 */
	private Map<String,Object> resolveKQZQFileTxt(File newFile, List<DuiAccountDetail> acqTrans) throws Exception {
		return duiAccountAssemblyOrParsing.resolveKQZQFileTxt(newFile,acqTrans);
	}
	

	private List<DuiAccountDetail> getCheckDetailTransInfos(String acqEnname,String dayAlteredTime, Date transDate) throws Exception{

		String date = DateUtil.getFormatDate("yyyy-MM-dd",transDate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String behDate = "";
		if(dayAlteredTime.equals("00:00:00")){			//如果日切时间是00:00:00 则不减一天
			behDate = DateUtil.subDay(transDate,0);
		}else{
			behDate = DateUtil.subDay(transDate,1);
		}
		String tomDate = DateUtil.subDay(transDate,-1);
		Date behD = sdf.parse(behDate);
		Date tomD = sdf.parse(tomDate);
		behDate = DateUtil.getFormatDate("yyyy-MM-dd",behD);
		tomDate = DateUtil.getFormatDate("yyyy-MM-dd",tomD);

		String dayTime = dayAlteredTime;

		String jhTimeStart =date + " " + dayTime;
		String jhTimeEnd =date + " "+ dayTime;
		if(dayTime.equals("00:00:00")){
			jhTimeEnd = tomDate +" "+dayTime;
		}else {
			jhTimeStart = behDate +" "+dayTime;
		}

		return duiAccountDetailService.getCheckDetailTransInfos(acqEnname,jhTimeStart,jhTimeEnd);

	}

	/**
	 * 快钱中钢对账
	 * @param acqEnname
	 * @param fileName
	 * @param newFile
	 * @param uname
	 * @return
	 */
	private Map<String, Object> kqZg(String acqEnname, String fileName, File newFile, String uname) {
		Map<String,Object> result=new HashMap<String, Object>();
		List<DuiAccountDetail> acqTrans = new ArrayList<>();
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			resolvKqZgFile(acqTrans,newFile);
			String[] s = fileName.split("_");
//			String fileTime = s[1];
			String fileTime = s[1].substring(0,s[1].indexOf("."));
			map.put("fileName", fileName);
			fileTime = DateUtil.StrDateToFormatStr(fileTime, "yyyyMMdd", "yyyy-MM-dd");
			map.put("checkFileDate",fileTime );
			map.put("checkAccountDetails", acqTrans);
			map.put("uname",uname);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			SysDict acqOrg = sysDictService.findSysDictByKeyName("acq_day_altered_time", acqEnname);
			List<DuiAccountDetail> dbDetails = getCheckDetailTransInfos(acqEnname,acqOrg.getSysValue(),sdf.parse(fileTime));
			String duiResult = duiAccountService.doDuiAccountForKqZg(acqEnname,dbDetails, map);
			if(duiResult.equals("0")){
				result.put("statu",true);
				result.put("msg","对账完毕，请查询对账信息");
			}else if(duiResult.equals("1")){
				result.put("statu",false);
				result.put("msg","该文件已对账完毕，请不要重复对账");
			}
		}catch (Exception e){
			log.error("",e);
			result.put("statu",false);
			result.put("msg","导入对账异常！");
			log.error(acqEnname+"对账异常:",e);
		}
		return result;
	}

	/**
	 * 解析快钱中钢对账文件
	 * @param acqTrans
	 * @param newFile
	 * @throws Exception
	 */
	private void resolvKqZgFile(List<DuiAccountDetail> acqTrans, File newFile) throws Exception {
		log.info("==============解析快钱中钢对账文件开始==============");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		InputStream inputStream = new FileInputStream(newFile);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
		String line = reader.readLine();
		line = reader.readLine();

		while (line != null && !"".equals(line)) {
			String[] detail = line.split(",");

			DuiAccountDetail checkDetail = new DuiAccountDetail();

			checkDetail.setAcqMerchantNo(detail[1]);
			checkDetail.setAcqMerchantName(detail[2]);
			checkDetail.setAcqOrderNo(detail[4]);				//订单号
			checkDetail.setAcqTransType(detail[3]);		//支付类型
			checkDetail.setAcqTransAmount(new BigDecimal(detail[6]));
			checkDetail.setAcqTransStatus(detail[7]);
//			String refundAmount = StringUtils.isBlank(temp.get(8)) ? "0" : temp.get(8);
			checkDetail.setAcqRefundAmount(new BigDecimal(detail[9]));
			checkDetail.setAcqMerchantOrderNo(detail[5]);
			checkDetail.setAcqTransTime(sdf.parse(detail[0]));

			checkDetail.setCreateTime(new Date());
			acqTrans.add(checkDetail);
			line = reader.readLine();
		}
		inputStream.close();
		log.info("==============解析快钱中钢对账文件结束==============");

	}

	/**
	 * 对账明细
	 *
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/viewDuiAccountDetail.do")
	public String viewDuiAccountDetail(ModelMap model, @RequestParam String id, @RequestParam Map<String, String> params) throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		DuiAccountDetail detail = new DuiAccountDetail();
		detail = duiAccountDetailService.findDuiAccountDetailById(id) ;


		data.put("id", detail.getId());//ID
		data.put("checkBatchNo", detail.getCheckBatchNo());//对账批次号
		data.put("orderReferenceNo", detail.getOrderReferenceNo());//订单参考号

		List<SysDict> checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
		for (SysDict sysDict : checkAccountStatusList) {
			if (sysDict.getSysValue().equals(detail.getCheckAccountStatus())) {
				data.put("checkAccountStatus", sysDict.getSysName());//对账状态
				break;
			}
		}
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");

		for (SysDict sysDict : recordStatusList) {
			if (sysDict.getSysValue().equals(detail.getRecordStatus()==null ? "": detail.getRecordStatus().toString())) {
				data.put("recordStatus", sysDict.getSysName());//存疑记账状态  原 记账状态
				break;
			}
		}
		data.put("createTime", DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getCreateTime()));//创建时间
		data.put("acqEnname", detail.getAcqEnname());//上游名称
		data.put("plateMerchantNo", detail.getPlateMerchantNo());//平台商户号
		data.put("plateTransAmount", detail.getPlateTransAmount());//平台交易金额
		data.put("plateAcqMerchantFee", detail.getPlateAcqMerchantFee());//平台收单机构商户手续费
		data.put("plateMerchantFee", detail.getPlateMerchantFee());//平台商户手续费
		data.put("plateAccountNo", detail.getPlateAccountNo());//平台交易账号

		List<SysDict> plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status") ;
		for (SysDict sysDict : plateTransStatusList) {
			if (sysDict.getSysValue().equals(detail.getPlateTransStatus())) {
				data.put("plateTransStatus", sysDict.getSysName());//平台交易状态
				break;
			}
		}

		List<SysDict> plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type") ;
		for (SysDict sysDict : plateTransTypeList) {
			if (sysDict.getSysValue().equals(detail.getPlateTransType())) {
				data.put("plateTransType", sysDict.getSysName());//平台交易类型
				break;
			}
		}
		List<SysDict> npospAccountList = sysDictService.findSysDictGroup("nposp_account");
		for (SysDict sysDict : npospAccountList) {
			if (sysDict.getSysValue().equals(detail.getAccount()==null ? "": detail.getAccount().toString())) {
				data.put("account", sysDict.getSysName());//交易记账
				break;
			}
		}
		data.put("plateAcqMerchantNo", detail.getPlateAcqMerchantNo());//平台收单机构商户号
		data.put("plateAcqTerminalNo", detail.getPlateAcqTerminalNo());//平台收单机构终端号
		data.put("plateTerminalNo", detail.getPlateTerminalNo());//平台终端号
		BigDecimal taskAmount = BigDecimal.ZERO;
		if(detail.getPlateTransAmount() != null && detail.getPlateMerchantFee() != null){
			taskAmount = detail.getPlateTransAmount().subtract(detail.getPlateMerchantFee());
		}
		data.put("taskAmount", taskAmount);//出账任务金额

		List<SysDict> settlementMethodList = sysDictService.findSysDictGroup("settlement_method");
		for (SysDict sysDict : settlementMethodList) {
			if (sysDict.getSysValue().equals(detail.getSettlementMethod()==null ? "": detail.getSettlementMethod())) {
				data.put("settlementMethod", sysDict.getSysName());//结算周期
				break;
			}
		}

		List<SysDict> settleStatusList = sysDictService.findSysDictGroup("settle_status");
		for (SysDict sysDict : settleStatusList) {
			if (sysDict.getSysValue().equals(detail.getSettleStatus()==null?"":detail.getSettleStatus().toString())) {
				data.put("settleStatus", sysDict.getSysName());//结算状态
				break;
			}
		}

		//1T0交易；2手工提现；3T1线上代付；4T1线下代付
		String settleType = "";
		settleType = detail.getSettleType()==null?"":detail.getSettleType();
		if(StringUtils.isNotBlank(settleType)){
			if("1".equals(detail.getSettleType())){
				settleType = "T0交易";
			}else if("2".equals(detail.getSettleType())){
				settleType = "手工提现";
			}else if("3".equals(detail.getSettleType())){
				settleType = "T1线上代付";
			}else{
				settleType = "T1线下代付";
			}
		}
		data.put("settleType", settleType);//出账方式
		data.put("plateOrderNo", detail.getPlateOrderNo());//平台订单号
		data.put("plateSerialNo", detail.getPlateSerialNo());//平台流水号
		data.put("plateBatchNo", detail.getPlateBatchNo());//平台批次号
		data.put("plateAcqBatchNo", detail.getPlateAcqBatchNo());//平台收单机构批次号
		data.put("plateAcqReferenceNo", detail.getPlateAcqReferenceNo());//平台收单机构参考号
		data.put("plateAcqMerchantRate", detail.getPlateAcqMerchantRate());//收单机构商户扣率
		data.put("plateMerchantRate", detail.getPlateMerchantRate());//商户扣率
		data.put("plateAcqSerialNo", detail.getPlateAcqSerialNo());//平台收单机构流水号
		data.put("mySettle", detail.getMySettle());//是否优质商户
		data.put("plateAcqTransTime",(detail.getPlateAcqTransTime()==null?"":DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getPlateAcqTransTime())));//平台收单机构交易时间
		data.put("plateMerchantSettleDate", (detail.getPlateMerchantSettleDate()==null?"":DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getPlateMerchantSettleDate())));//平台商户结算日期
		//交易方式 1 POS，2 支付宝，3 微信，4 快捷
		String payMethod = "";
		payMethod = detail.getPayMethod()==null?"":detail.getPayMethod();
		if(StringUtils.isNotBlank(payMethod)){
			if("1".equals(detail.getPayMethod())){
				payMethod = "POS";
			}else if("2".equals(detail.getPayMethod())){
				payMethod = "支付宝";
			}else if("3".equals(detail.getPayMethod())){
				payMethod = "微信";
			}else if("4".equals(detail.getPayMethod())){
				payMethod = "快捷";
			}else if("5".equals(detail.getPayMethod())){
				payMethod = "银联二维码";
			}
		}
		data.put("payMethod", payMethod);

		data.put("acqEnname", detail.getAcqEnname());//收单机构英文名称
		data.put("acqTransAmount", detail.getAcqTransAmount());//收单机构交易金额
		data.put("acqRefundAmount", detail.getAcqRefundAmount());//收单机构退货金额
		data.put("acqTransOrderNo", detail.getAcqTransOrderNo());//收单机构交易订单号

		data.put("acqTransStatus", detail.getAcqTransStatus());//收单机构交易状态


		data.put("acqAccountNo", detail.getAcqAccountNo());//收单机构卡号
		data.put("acqTransSerialNo", detail.getAcqTransSerialNo());//收单机构交易流水号
		data.put("acqSerialNo", detail.getAcqSerialNo());//收单机构流水号
		data.put("acqOrderNo", detail.getAcqOrderNo());//收单机构订单号
		data.put("acqMerchantNo", detail.getAcqMerchantNo());//收单机构商户号
		data.put("accessOrgNo", detail.getAccessOrgNo());//接入机构编号
		data.put("acqReferenceNo", detail.getAcqReferenceNo());//收单机构系统参考号
		data.put("acqMerchantName", detail.getAcqMerchantName());//收单机构商户名称
		data.put("acqOriTransSerialNo", detail.getAcqOriTransSerialNo());//收单机构原交易流水号
		data.put("acqTerminalNo", detail.getAcqTerminalNo());//收单机构终端号
		data.put("acqBatchNo", detail.getAcqBatchNo());//收单机构批次号
		data.put("acqCardSequenceNo", detail.getAcqCardSequenceNo());//收单机构序列号
		data.put("acqTransCode", "");//收单机构交易码
		data.put("acqTransTime", (detail.getAcqTransTime()==null?"":DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getAcqTransTime())));//收单机构交易时间
		data.put("acqCheckDate", (detail.getAcqCheckDate()==null?"":DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getAcqCheckDate())));//收单机构对账日期
		data.put("acqSettleDate", (detail.getAcqSettleDate()==null?"":DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", detail.getAcqSettleDate())));//收单机构入账日期

		model.put("data", data);
		model.put("params", params);
		return "duiAccount/duiAccountViewDetail";
	}
	/**
	 * 对账信息查询
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountMsg:query')")
	@RequestMapping(value = "/toDuiAccountQuery.do")
	public String toDuiAccountQuery(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
		List<SysDict> checkResultList = sysDictService.findSysDictGroup("sys_check_result");
		model.put("recordStatusList", recordStatusList);
		model.put("checkResultList", checkResultList);

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
		model.put("params", params);
		return "duiAccount/duiAccountQuery";
	}
	

	/**
	 * 对账信息明细查询
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountMsgDetail:query')")
	@RequestMapping(value = "/toDuiAccountDetailQuery.do")
	public String toDuiAccountDetailQuery(ModelMap model, @RequestParam Map<String, String> params, @RequestParam(value = "forwardTo", required = false)Integer forwardTo) throws Exception{
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

//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String currenDate = sdf.format(new Date());
//
//		String date1 = DateUtil.subDayFormatLong(new Date(),7);
//		model.put("date1",date1);
//		model.put("date2",currenDate);

		return "duiAccount/duiAccountDetailQuery";
	}


	/**
	 * 对账差错处理
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:query')")
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
		List<SysDict> settleStatusList = sysDictService.findSysDictGroup("settle_status");
		model.put("settleStatusList", settleStatusList);
		List<SysDict> npospAccountList = sysDictService.findSysDictGroup("nposp_account");
		model.put("npospAccountList", npospAccountList);

		return "duiAccount/duiAccountErrorHandle";
	}
	public static String convertDateString(String date) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat longSdf=new SimpleDateFormat("yyyyMMdd");
		Date dt=sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		Date   lastDate   =   cal.getTime();
		return longSdf.format(lastDate);
	}


	/**
	 * 查询所有的对账信息
	 * @param duiAccountBatch
	 * @param sort
	 * @param page
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAuthority('duiAccountMsg:query')")
	@RequestMapping(value = "/queryDuiAccountList.do")
	@ResponseBody
	public Page<DuiAccountBatch> queryDuiAccountList(@ModelAttribute DuiAccountBatch duiAccountBatch,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<DuiAccountBatch> page,HttpServletRequest request ){
		try {
			List<DuiAccountBatch> list = duiAccountBatchService.queryDuiAccountList(duiAccountBatch, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	@PreAuthorize("hasAuthority('duiAccountMsg:delete')")
	@RequestMapping(value = "/deleteDuiAccount.do")
	@ResponseBody
	public Map<String, Object> deleteDuiAccount(@RequestParam(value = "id", required = true)Integer id){
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			int re = duiAccountBatchService.deleteDuiAccountBatch(id);

			if (re > 0) {
				data.put("state", true);
				data.put("msg", "删除对账信息成功");
				log.info(data.toString());
			} else if (re == -2) {
				data.put("state", false);
				data.put("msg", "该对账批次已经记账，不允许删除");
				log.info(data.toString());
			} else {
				data.put("state", false);
				data.put("msg", "删除对账信息失败");
				log.info(data.toString());
			}
		} catch (Exception e) {
			data.put("state", false);
			data.put("msg", "删除对账信息异常!");
			log.info(data.toString());
			log.error("异常:",e);
		}
		return data;
	}

	/**
	 * 处理日期类型的转换问题
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * 跳转到详情2页面
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountMsg:query')")
	@RequestMapping(value = "/toDuiAccountDetail2.do")
	public String duiAccountDetail2(ModelMap model, @RequestParam Map<String, String> params,
									@RequestParam(value = "checkBatchNo", required = false) String checkBatchNo,
									@RequestParam(value = "acqEnname", required = false) String acqEnname) throws Exception{

		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> acq = duiAccountBatchService.getAcqTransSumAndCount(checkBatchNo);
		Map<String, Object> plate = duiAccountBatchService.getPlateSumAndCount(checkBatchNo);
		Map<String, Object> acqSingle = duiAccountBatchService.getAcqTransAmountSumAndCount(checkBatchNo);
		Map<String, Object> plateSingle = duiAccountBatchService.getPlateTransAmountSumAndCount(checkBatchNo);
		Map<String, Object> plateMe = duiAccountBatchService.getPlateSumAndCountMe(checkBatchNo);

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
		model.put("params", params);
		return "duiAccount/duiAccountDetail2";
	}

	/**
	 * 查询对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('duiAccountMsgDetail:query')")
	@RequestMapping(value = "/findDuiAccountDetailList.do")
	@ResponseBody
	public Page<DuiAccountDetail> findDuiAccountDetailList(@RequestParam Map<String,String> params ,@ModelAttribute DuiAccountDetail duiAccountDetail,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<DuiAccountDetail> page){
		try {
			String createTimeStart = params.get("createTimeStart") ;
			String createTimeEnd = params.get("createTimeEnd") ;
			duiAccountDetailService.findDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	/**
	 * 查询对账详细信息列表
	 * @param duiAccountDetail
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:query')")
	@RequestMapping(value = "/findErrorDuiAccountDetailList.do")
	@ResponseBody
	public Page<DuiAccountDetail> findErrorDuiAccountDetailList(@RequestParam Map<String,String> params ,@ModelAttribute DuiAccountDetail duiAccountDetail,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<DuiAccountDetail> page){
		try {
			String createTimeStart = params.get("createTimeStart") ;
			String createTimeEnd = params.get("createTimeEnd") ;
			duiAccountDetailService.findErrorDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail, sort, page);
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
	@PreAuthorize("hasAuthority('duiAccountMsgDetail:export')")
	@RequestMapping(value = "exportDuiAccountDetail.do",method = RequestMethod.POST)
	public void exportDuiAccountDetail(@RequestParam Map<String,String> params ,@ModelAttribute DuiAccountDetail duiAccountDetail, HttpServletResponse response,HttpServletRequest request) throws IOException {
		//表单提交解码
				duiAccountDetail.setAcqMerchantName(java.net.URLDecoder.decode(duiAccountDetail.getAcqMerchantName(),"UTF-8"));
		long countTime = System.currentTimeMillis();
				String createTimeStart = params.get("createTimeStart") ;
				String createTimeEnd = params.get("createTimeEnd") ;
				//用于对数据字典的数据进行格式化显示
				ExportFormat format = new ExportFormat() ;
				//List<SysDict> errorHandleStatusList = new ArrayList<SysDict>() ;
				List<SysDict> checkAccountStatusList = new ArrayList<SysDict>() ;
				//List<SysDict> plateTransTypeList = new ArrayList<SysDict>() ;
				List<SysDict> plateTransStatusList = new ArrayList<SysDict>() ;
				List<SysDict> recordStatusList = new ArrayList<SysDict>() ;
				List<SysDict> settlementMethodList = null;
				List<SysDict> settleStatusList = null;
				List<SysDict> npospAccountList = null;
				try {
					//errorHandleStatusList = sysDictService.findSysDictGroup("check_error_handle_status");
					checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
					//plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type") ;
					plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status") ;
					recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
					settlementMethodList = sysDictService.findSysDictGroup("settlement_method");
					settleStatusList = sysDictService.findSysDictGroup("settle_status");
					npospAccountList = sysDictService.findSysDictGroup("nposp_account");
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				  response.setContentType("application/vnd.ms-excel;charset=utf-8");
				  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
				  String fileName = "对账详情"+sdf.format(new Date())+".xlsx" ;
				  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
				  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
				  response.addHeader("Pragma", "no-cache");
				  response.addHeader("Cache-Control", "no-cache");
				  List<DuiAccountDetail> list = new ArrayList<DuiAccountDetail>();
				  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
				  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

//				  log.info("duiAccountDetail=" + ToStringBuilder.reflectionToString(duiAccountDetail, ToStringStyle.MULTI_LINE_STYLE));
				//从数据库中查询数据
				  try {
					list = duiAccountDetailService.findExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail) ;
				} catch (Exception e) {
					log.error("异常:",e);
				}
		for(DuiAccountDetail duiAcc:list){
					  Map<String,String> map = new HashMap<String,String>() ;
					  //1T0交易；2手工提现；3T1线上代付；4T1线下代付
						String settleType = "";
						if(StringUtils.isNotBlank(duiAcc.getSettleType())){
							if("1".equals(duiAcc.getSettleType())){
								settleType = "T0交易";
							}else if("2".equals(duiAcc.getSettleType())){
								settleType = "手工提现";
							}else if("3".equals(duiAcc.getSettleType())){
								settleType = "T1线上代付";
							}else{
								settleType = "T1线下代付";
							}
						}
						//冻结
						String freezeStatus = "";
						if(StringUtils.isNotBlank(duiAcc.getFreezeStatus())){
							if("0".equals(duiAcc.getFreezeStatus())){
								freezeStatus = "未冻结";
							}else if("1".equals(duiAcc.getFreezeStatus())){
								freezeStatus = "风控冻结";
							}else if("2".equals(duiAcc.getFreezeStatus())){
								freezeStatus = "活动冻结";
							}else if("3".equals(duiAcc.getFreezeStatus())){
								freezeStatus = "财务冻结";
							}
						}
                      String payMethod = "";
                      if(StringUtils.isNotBlank(duiAcc.getPayMethod())){
                          if("1".equals(duiAcc.getPayMethod())){
                              payMethod = "POS";
                          }else if("2".equals(duiAcc.getPayMethod())){
                              payMethod = "支付宝";
                          }else if("3".equals(duiAcc.getPayMethod())){
                              payMethod = "微信";
                          }else if("4".equals(duiAcc.getPayMethod())){
                              payMethod = "快捷";
                          }else if("5".equals(duiAcc.getPayMethod())){
							  payMethod = "银联二维码";
						  }
                      }

						//是否加入出账单
						String isAddBill = "";
						if(StringUtils.isNotBlank(duiAcc.getIsAddBill())){
							if("0".equals(duiAcc.getIsAddBill())){
								isAddBill = "否";
							}else if("1".equals(duiAcc.getIsAddBill())){
								isAddBill = "是";
							}
						}

			String orderReferenceNo =duiAcc.getOrderReferenceNo();
//			if(duiAcc.getCheckAccountStatus()!=null && duiAcc.getCheckAccountStatus().equals(DuiAccountStatus.ACQ_SINGLE.toString())){
//				if(orderReferenceNo==null){
//					switch (duiAcc.getAcqEnname()){
//						case "zm_xe":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "zm_pay":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "WF_ZQ":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "openPay":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "YS_ZQ":
//							orderReferenceNo = duiAcc.getAcqReferenceNo();
//							break;
//						case "ZF_ZQ":
//							orderReferenceNo = duiAcc.getAcqReferenceNo();
//							break;
//						case "HLB_KJ":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "RYAT_SM":
//							orderReferenceNo = duiAcc.getAcqMerchantOrderNo();
//							break;
//						case "HLJC":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "hljcLd":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "ZFYL_ZQ":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "ZY":
//						case "ZYLD":
//						case "ds_pay":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//						case "YBEI":
//							orderReferenceNo = duiAcc.getAcqOrderNo();
//							break;
//					}
//				}
//			}

					  map.put("id", duiAcc.getId().toString());
					  map.put("checkBatchNo", duiAcc.getCheckBatchNo()) ;
					  map.put("acqEnname", duiAcc.getAcqEnname()) ;
					  map.put("plateOrderNo", duiAcc.getPlateOrderNo()) ;
					  map.put("orderReferenceNo", orderReferenceNo) ;
					  map.put("acqReferenceNo", duiAcc.getAcqReferenceNo()) ;
					  map.put("checkAccountStatus", format.formatSysDict(duiAcc.getCheckAccountStatus(), checkAccountStatusList)) ;
					  map.put("recordStatus", format.formatSysDict(duiAcc.getRecordStatus() == null ? "" : duiAcc.getRecordStatus().toString(), recordStatusList));
					  map.put("isAddBill", isAddBill) ;
					  map.put("freezeStatus", freezeStatus) ;
					  map.put("plateMerchantNo", duiAcc.getPlateMerchantNo()) ;
					  map.put("plateMerchantRate", duiAcc.getPlateMerchantRate()) ;
					  map.put("plateAccountNo", duiAcc.getPlateAccountNo()) ;
					  map.put("plateTransAmount", duiAcc.getPlateTransAmount()==null?"":duiAcc.getPlateTransAmount().toString()) ;
					  map.put("plateAcqMerchantFee", duiAcc.getPlateAcqMerchantFee()==null?"":duiAcc.getPlateAcqMerchantFee().toString()) ;
					  map.put("merFee2", duiAcc.getMerFee2()==null?"":duiAcc.getMerFee2().toString()) ;
					  map.put("deductionFee", duiAcc.getDeductionFee()==null?"":duiAcc.getDeductionFee().toString()) ;
					  map.put("plateMerchantFee", duiAcc.getPlateMerchantFee()==null?"":duiAcc.getPlateMerchantFee().toString()) ;
                      map.put("couponNos", duiAcc.getCouponNos() == null ? "" : duiAcc.getCouponNos().toString()) ;
					  map.put("plateTransStatus", format.formatSysDict(duiAcc.getPlateTransStatus(), plateTransStatusList)) ;
                      map.put("payMethod", payMethod) ;
					  map.put("plateAcqTransTime", duiAcc.getPlateAcqTransTime()==null?"":df.format(duiAcc.getPlateAcqTransTime())) ;
					  map.put("taskAmount", duiAcc.getTaskAmount() == null ? "" : duiAcc.getTaskAmount().toString()) ;
					  map.put("settlementMethod", format.formatSysDict(duiAcc.getSettlementMethod() == null ? "" : duiAcc.getSettlementMethod().toString(), settlementMethodList));
					  map.put("settleStatus", format.formatSysDict(duiAcc.getSettleStatus() == null ? "" : duiAcc.getSettleStatus().toString(), settleStatusList));
					  map.put("settleType", settleType);
					  map.put("account", format.formatSysDict(duiAcc.getAccount() == null ? "":duiAcc.getAccount().toString(), npospAccountList));
					  map.put("acqAccountNo", duiAcc.getAcqAccountNo()) ;
					  map.put("acqTransAmount", duiAcc.getAcqTransAmount() == null ? "" : duiAcc.getAcqTransAmount().toString()) ;
					  map.put("acqRefundAmount", duiAcc.getAcqRefundAmount() == null ? "" : duiAcc.getAcqRefundAmount().toString()) ;
					  map.put("acqTransTime", duiAcc.getAcqTransTime()==null?"":df.format(duiAcc.getAcqTransTime())) ;
					  map.put("acqMerchantNo", duiAcc.getAcqMerchantNo()==null?"":duiAcc.getAcqMerchantNo()) ;
					  map.put("acqTerminalNo", duiAcc.getAcqTerminalNo()==null?"":duiAcc.getAcqTerminalNo()) ;
					  map.put("acqSerialNo", duiAcc.getAcqSerialNo()==null?"":duiAcc.getAcqSerialNo()) ;
					  map.put("createTime", duiAcc.getCreateTime()==null?"":df.format(duiAcc.getCreateTime())) ;
					  map.put("quickRate", duiAcc.getQuickRate()==null?"":duiAcc.getQuickRate().toString());
					  map.put("quickFee", duiAcc.getQuickFee()==null?"":duiAcc.getQuickFee().toString());
					  map.put("actualFee", duiAcc.getActualFee()==null?"":duiAcc.getActualFee().toString());
					String dbCutFlag = duiAcc.getDbCutFlag()==null?"None":duiAcc.getDbCutFlag();
					if(dbCutFlag.equals("None")){
						dbCutFlag = "无";
					}else if(dbCutFlag.equals("ShortCashAuto")){
						dbCutFlag = "短款自动匹配";
					}else{
						dbCutFlag = "长款自动匹配";
					}
					map.put("dbCutFlag", dbCutFlag);

					map.put("merchantPrice",duiAcc.getMerchantPrice().toString());
					map.put("deductionMerFee",duiAcc.getDeductionMerFee().toString());
					map.put("actualMerFee",duiAcc.getActualMerFee().toString());
					data.add(map) ;
				  }
				  NewListDataExcelExport export = new NewListDataExcelExport();

				  String[] cols = new String[]{
						  "id","checkBatchNo","acqEnname",
						  "orderReferenceNo","acqReferenceNo","plateOrderNo","checkAccountStatus",
						  "recordStatus","isAddBill","freezeStatus","dbCutFlag","plateMerchantNo","plateMerchantRate",
						  "plateAccountNo","plateTransAmount",
						  "plateAcqMerchantFee","merFee2","deductionFee","plateMerchantFee","couponNos",
						  "plateTransStatus","payMethod","plateAcqTransTime",
						  "taskAmount","settlementMethod",
						  "settleStatus","account","settleType",
						  "acqAccountNo","acqTransAmount",
						  "acqRefundAmount","acqTransTime",
						  "acqMerchantNo","acqTerminalNo","acqSerialNo","quickFee","quickRate","actualFee","merchantPrice","deductionMerFee","actualMerFee","createTime"};

				  String[] colsName = new String[]{
						  "ID","对账批次号","上游名称",
						  "订单参考号","收单机构参考号","平台订单号","对账状态",
						  "存疑记账状态","是否加入出账单","冻结状态","单边日切标识","平台商户号","商户扣率",
						  "平台交易卡号","平台交易金额",
						  "平台上游手续费","平台商户手续费","抵扣交易手续费","实际交易手续费","抵扣券编号",
						  "平台交易状态","平台交易方式","平台交易时间",
						  "出账任务金额","结算周期",
						  "结算状态","交易记账","出账方式",
						  "收单机构卡号","收单机构交易金额",
		        	      "收单机构退货金额","收单机构交易时间",
						  "收单机构商户号","终端号",
						  "流水号","云闪付手续费","云闪付活动费率","原始交易手续费","自选商户手续费","抵扣自选商户手续费","实际自选商户手续费","创建时间"};
		          OutputStream out = response.getOutputStream();
				  export.export(cols, colsName, data, out);
				  out.close();
	}

	
	/**
	 * 导出数据(对账差错处理)
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException 
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:export')")
	@RequestMapping(value = "exportDuiAccount.do",method = RequestMethod.POST)
	public void exportDuiAccount(@RequestParam Map<String,String> params ,@ModelAttribute DuiAccountDetail duiAccountDetail, HttpServletResponse response,HttpServletRequest request) throws IOException {
		//表单提交解码
		//duiAccountDetail.setAcqMerchantName(java.net.URLDecoder.decode(duiAccountDetail.getAcqMerchantName(),"UTF-8"));
		
		String createTimeStart = params.get("createTimeStart") ;
		String createTimeEnd = params.get("createTimeEnd") ;
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		List<SysDict> errorHandleStatusList = new ArrayList<SysDict>() ;
		List<SysDict> checkAccountStatusList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransTypeList = new ArrayList<SysDict>() ;
		List<SysDict> plateTransStatusList = new ArrayList<SysDict>() ;
		List<SysDict> recordStatusList = new ArrayList<SysDict>() ;
		List<SysDict> npospAccountList = new ArrayList<SysDict>() ;
		List<SysDict> settleStatusList = new ArrayList<SysDict>() ;
		try {
			errorHandleStatusList = sysDictService.findSysDictGroup("check_error_handle_status");
			checkAccountStatusList = sysDictService.findSysDictGroup("dui_account_status") ;
			plateTransTypeList = sysDictService.findSysDictGroup("plate_trans_type") ;
			plateTransStatusList = sysDictService.findSysDictGroup("plate_trans_status") ;
			recordStatusList = sysDictService.findSysDictGroup("sys_record_status");
			npospAccountList = sysDictService.findSysDictGroup("nposp_account");
			settleStatusList = sysDictService.findSysDictGroup("settle_status");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "对账差错"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<DuiAccountDetail> list = new ArrayList<DuiAccountDetail>();
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;

		//从数据库中查询数据
		try {
			list = duiAccountDetailService.findErrorExportDuiAccountDetailList(createTimeStart,createTimeEnd,duiAccountDetail) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		String treatment_method="";
		String check_status = "";
		String freeze_status = "";
		for(DuiAccountDetail duiAcc:list){
			if(StringUtils.isNotBlank(duiAcc.getTreatmentMethod())) {
				if (duiAcc.getTreatmentMethod().equals("0")) {
					treatment_method = "未处理";
				} else if (duiAcc.getTreatmentMethod().equals("1")) {
					treatment_method = "预冻结";
				} else if (duiAcc.getTreatmentMethod().equals("2")) {
					treatment_method = "冻结";
				} else {
					treatment_method = "标记";
				}
			}
			if(StringUtils.isNotBlank(duiAcc.getCheckStatus())) {
				if (duiAcc.getCheckStatus().equals("0")) {
					check_status = "待初审";
				} else if (duiAcc.getCheckStatus().equals("1")) {
					check_status = "已初审";
				}
			}

			if(StringUtils.isNotBlank(duiAcc.getFreezeStatus())) {
				if (duiAcc.getFreezeStatus().equals("0")) {
					freeze_status = "未冻结";
				} else if (duiAcc.getFreezeStatus().equals("1")) {
					freeze_status = "风控冻结";
				} else if (duiAcc.getFreezeStatus().equals("2")) {
					freeze_status = "活动冻结";
				} else {
					freeze_status = "财务冻结";
				}
			}

			Map<String,String> map = new HashMap<String,String>() ;
			map.put("id", duiAcc.getId().toString());
			map.put("checkBatchNo", duiAcc.getCheckBatchNo()) ;
			map.put("acqEnname", duiAcc.getAcqEnname()) ;
			map.put("orderReferenceNo", duiAcc.getOrderReferenceNo()) ;
			map.put("plateOrderNo", duiAcc.getPlateOrderNo()) ;
			map.put("acqReferenceNo", duiAcc.getAcqReferenceNo()) ;
			map.put("checkAccountStatus", format.formatSysDict(duiAcc.getCheckAccountStatus(), checkAccountStatusList)) ;
			map.put("recordStatus", format.formatSysDict(duiAcc.getRecordStatus()==null?"":duiAcc.getRecordStatus().toString(), recordStatusList));
			map.put("treatmentMethod", treatment_method);
			map.put("checkStatus", check_status);
			map.put("errorHandleStatus",format.formatSysDict(duiAcc.getErrorHandleStatus(), errorHandleStatusList)) ;
			map.put("plateMerchantNo", duiAcc.getPlateMerchantNo()) ;
			map.put("plateTransAmount", duiAcc.getPlateTransAmount()==null?"":duiAcc.getPlateTransAmount().toString()) ;
			map.put("plateTransStatus", format.formatSysDict(duiAcc.getPlateTransStatus(), plateTransStatusList)) ;
			map.put("account", format.formatSysDict(duiAcc.getAccount()==null?"":duiAcc.getAccount().toString(), npospAccountList));
			map.put("freezeStatus", freeze_status);
			map.put("settleStatus", format.formatSysDict(duiAcc.getSettleStatus()==null?"":duiAcc.getSettleStatus().toString(), settleStatusList));
			map.put("acqTransAmount", duiAcc.getAcqTransAmount() == null ? "" : duiAcc.getAcqTransAmount().toString()) ;
			map.put("acqTransTime", duiAcc.getAcqTransTime()==null?"":df.format(duiAcc.getAcqTransTime())) ;
			map.put("errorMsg", duiAcc.getErrorMsg()) ;
			map.put("checkNo", duiAcc.getCheckNo()) ;
			map.put("errorCheckCreator", duiAcc.getErrorCheckCreator()) ;
			map.put("errorHandleCreator", duiAcc.getErrorHandleCreator()) ;
			map.put("errorTime", duiAcc.getErrorTime()==null?"":df.format(duiAcc.getErrorTime())) ;//差错处理时间
			map.put("createTime", duiAcc.getCreateTime()==null?"":df.format(duiAcc.getCreateTime())) ;
			map.put("remark", duiAcc.getRemark()) ;


			data.add(map) ;
		}
		NewListDataExcelExport export = new NewListDataExcelExport();
		String[] cols = new String[]{"id","checkBatchNo","acqEnname","orderReferenceNo","plateOrderNo","acqReferenceNo","checkAccountStatus","recordStatus","treatmentMethod","checkStatus","errorHandleStatus",
				"plateMerchantNo","plateTransAmount","plateTransStatus","account","freezeStatus","settleStatus","acqTransAmount",
				"acqTransTime","errorMsg","checkNo","errorCheckCreator","errorHandleCreator","errorTime","createTime", "remark"};
		String[] colsName = new String[]{"ID","对账批次号","收单机构英文名称","订单参考号","平台订单号","收单机构参考号","对账状态","存疑记账状态" ,
				"处理方式","差错初审状态", "差错处理状态", "平台商户号","平台交易金额","平台交易状态","交易记账状态",
				"冻结状态","结算状态","收单机构交易金额","收单机构交易时间","原因","初审单号", "差错初审人","差错处理人",
				"差错处理时间","创建时间","备注"};
		ServletOutputStream out = response.getOutputStream();
		export.export(cols, colsName, data, out);
		out.close();
	}

	/*==========================对账差错处理四个按钮具体功能开始============*/
	/**
	 * 平台单边解冻结算
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
	@RequestMapping(value="/platformSingleForDayCut.do",method=RequestMethod.POST)
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
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
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
			if("thawSettle".equals(duiAccountDetail.getErrorHandleStatus())){//平台单边正常解冻结算
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'对账冻结'
			if(!"pedingStreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
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
					DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据findErrorTransInfoByDuiAccountDetail
					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getAcqMerchantOrderNo(), duiAccountDetail.getAcqEnname());
					if("3".equals(transInfo.getFreezeStatus())){//如果是财务冻结就去解冻交易
//						MerchantInfo merInfo =  merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
//						AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(transInfo.getAcqEnname());
//						msg = duiAccountDetailService.platformSingleForDayCut(duiAccountDetail ,transInfo ,merInfo ,acqOrg) ;
                        collectionTransOrderService.updateFreezeStatusByOrderNo(transInfo.getOrderNo(), "0");//解冻  0-未冻结

						/*if(!(boolean)msg.get("state")){
							failedCount++;
						}else{
							succCount++;
						}
						duiAccountDetail.setErrorMsg(msg.get("msg").toString());*/
						succCount++;
					}else{
						if("1".equals(duiAccountDetail.getTreatmentMethod())){
							BigDecimal preFrozenAmount = duiAccountDetail.getPlateTransAmount().subtract(duiAccountDetail.getPlateMerchantFee());
							TransInfoPreFreezeLog record = new TransInfoPreFreezeLog();
							record.setOperId("account");
							record.setOperName("account");
							record.setPreFreezeNote("平台单边正常解冻结算");
							record.setPreFreezeAmount(preFrozenAmount);
							record.setMerchantNo(transInfo.getMerchantNo());
							record.setOperTime(new Date());
							merchantInfoService.insertPreFreezeLog(record);
							duiAccountDetailService.updateDuiAccError(duiAccountDetail.getId().intValue(),"1",duiAccountDetail.getFreezeStatus());//处理方式标记为标记，冻结状态改为原本状态
						}
						succCount++;
					}
					duiAccountDetail.setErrorHandleStatus("thawSettle");//平台单边正常解冻结算
					duiAccountDetailService.updateDuiAccount(duiAccountDetail) ;
				}catch(Exception e){
					failedCount++;
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
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
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
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
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
			//2、查询对账详细信息中的    差错处理状态   是否为'平台单边记存疑问'
			if(!"pedingStreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
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
					DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					//通过对账信息中的上游信息去boss  trans_info表中查找对应数据
					CollectiveTransOrder transInfo = collectionTransOrderService.queryByAcqFastTransInfo(duiAccountDetail.getAcqMerchantOrderNo(), duiAccountDetail.getAcqEnname());

					if("3".equals(transInfo.getFreezeStatus())){//如果是财务冻结就去解冻交易
						/*MerchantInfo merInfo =  merchantInfoService.findMerchantInfoByUserId(transInfo.getMerchantNo());
						AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(transInfo.getAcqEnname());
//						msg = duiAccountDetailService.platformSingleSettleToMerchant(duiAccountDetail ,transInfo ,merInfo ,acqOrg) ;
						if(!(boolean)msg.get("state")){
							failedCount++;
						}else{
							succCount++;
						}
						duiAccountDetail.setErrorMsg(msg.get("msg").toString());*/
						collectionTransOrderService.updateFreezeStatusByOrderNo(transInfo.getOrderNo(), "0");//解冻  0-未冻结
						succCount++;
					}else{
						if("1".equals(duiAccountDetail.getTreatmentMethod())){
							BigDecimal preFrozenAmount = duiAccountDetail.getPlateTransAmount().subtract(duiAccountDetail.getPlateMerchantFee());
							TransInfoPreFreezeLog record = new TransInfoPreFreezeLog();
							record.setOperId("account");
							record.setOperName("account");
							record.setPreFreezeNote("平台单边赔付商户");
							record.setPreFreezeAmount(preFrozenAmount);
							record.setMerchantNo(transInfo.getMerchantNo());
							record.setOperTime(new Date());
							merchantInfoService.insertPreFreezeLog(record);
							duiAccountDetailService.updateDuiAccError(duiAccountDetail.getId().intValue(),"1",duiAccountDetail.getFreezeStatus());//处理方式标记为标记，冻结状态改为原本状态
						}
						succCount++;
					}
					duiAccountDetail.setErrorHandleStatus("platformPayment");//平台单边赔付商户
					duiAccountDetailService.updateDuiAccount(duiAccountDetail) ;

				}catch(Exception e){
					failedCount++;
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
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName));
			if ("accountFailed".equals(duiAccountDetail.getErrorHandleStatus())) {
				duiAccountDetail.setErrorMsg("上游单边，已标记为已处理");
				duiAccountDetail.setErrorHandleStatus(type);

				count += duiAccountDetailService.updateDuiAccount(duiAccountDetail);
			}
		}
		if (count > 0) {
			msg.put("state", true);
			msg.put("msg", "处理成功！");
		} else {
			msg.put("state", false);
			msg.put("msg", "处理失败！");
		}
		log.info(msg.toString());
		return msg;
	}

	/**
	 * 上游单边补记账结算商户
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
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
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			//System.out.println(params.get(paramsName));
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
			if (duiAccountDetail.getRecordStatus().equals(2)) {
				recordFlag = false;
				errMsg += "【"+(i+1)+"】,";
				continue;
			}
			//1、查询对账详细信息中的   对账结果  是否为'上游单边'  或上游成功平台失败
			if(!"ACQ_SINGLE".equals(duiAccountDetail.getCheckAccountStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			if("upstreamRecordAccount".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'上游单边记存疑问'
			if(!"upstreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
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
		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
					String acqEnname = duiAccountDetail.getAcqEnname();
					CollectiveTransOrder transInfo = null;
					if("neweptok".equals(acqEnname) || "ZF_ZQ".equals(acqEnname) || "YS_ZQ".equals(acqEnname)){
						//经过项目部讨论使用银联报备商户编号查询终端号
						Map<String,Object> acqTerMap = transInfoService.getAcqTerminalStore(duiAccountDetail.getAcqMerchantNo());
						String acqTerminalNo=duiAccountDetail.getAcqTerminalNo();
						if(acqTerMap!=null && acqTerMap.size()>0){
							acqTerminalNo=acqTerMap.get("terNo").toString();
						}
						duiAccountDetail.setAcqTerminalNo(acqTerminalNo);
						transInfo = transInfoService.getCollectiveTransDataForT1(duiAccountDetail.getAcqMerchantNo(),duiAccountDetail.getAcqEnname(),duiAccountDetail.getAcqSerialNo(),acqTerminalNo,duiAccountDetail.getAcqAccountNo(),duiAccountDetail.getAcqTransAmount());
//						log.info(acqEnname+"： 收单机构名称："+transInfo.getAcqEnname() +"订单号："+transInfo.getOrderNo());
					}else{
						String orderNo = "";
						switch (acqEnname){
							case "zm_xe":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "zm_pay":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "WF_ZQ":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "openPay":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "jc_pay":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "HLB_KJ":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "HLJC":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "hljcLd":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "ZFYL_ZQ":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "ZY":
							case "ZYLD":
							case "ds_pay":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							case "YBEI":
								orderNo = duiAccountDetail.getAcqOrderNo();
								break;
							default :
								orderNo = duiAccountDetail.getAcqMerchantOrderNo();
								break;
						}
						if(!orderNo.equals(""))
							transInfo = collectionTransOrderService.queryByAcqFastTransInfo(orderNo, duiAccountDetail.getAcqEnname());
					}
					if(transInfo!=null){
						log.info("上游单边补记账结算商户:"+duiAccountDetail.getId()+"--1");
						if("neweptok".equals(acqEnname) || "ZF_ZQ".equals(acqEnname) || "YS_ZQ".equals(acqEnname)){
							transInfoService.updateTransInfo(transInfo,duiAccountDetail);
							//补齐对账银联商户编号
							DuiAccountDetail detailT1 = new DuiAccountDetail();
							detailT1.setId(duiAccountDetail.getId());
							detailT1.setAcqMerchantNo(transInfo.getUnionpayMerNo());
							detailT1.setPlateMerchantEntryNo(transInfo.getMbpId());
							detailT1.setOrderReferenceNo(transInfo.getOrderNo());
							detailT1.setPlateAcqMerchantNo(transInfo.getUnionpayMerNo());
							detailT1.setPlateAgentNo(transInfo.getAgentNo());
							detailT1.setPlateMerchantNo(transInfo.getMerchantNo());
							detailT1.setPlateTerminalNo(transInfo.getTerminalNo());
							detailT1.setPlateAcqBatchNo(transInfo.getAcqBatchNo());
							detailT1.setPlateAcqSerialNo(transInfo.getAcqSerialNo());
							detailT1.setPlateAccountNo(transInfo.getAccountNo());
							detailT1.setPlateBatchNo(transInfo.getBatchNo());
							detailT1.setPlateSerialNo(transInfo.getSerialNo());
							detailT1.setPlateCardNo(transInfo.getAccountNo());
							detailT1.setPlateTransAmount(transInfo.getTransAmount());
							detailT1.setPlateAcqReferenceNo(transInfo.getAcqReferenceNo());
							detailT1.setPlateAcqTransTime(transInfo.getTransTime());
							detailT1.setPlateTransType(transInfo.getTransType());
							detailT1.setPlateTransStatus(transInfo.getTransStatus());
							detailT1.setPlateAcqMerchantFee(transInfo.getAcqMerchantFee());
							detailT1.setPlateMerchantFee(transInfo.getMerchantFee());
							detailT1.setPlateAcqMerchantRate(transInfo.getAcqMerchantRate());
							detailT1.setPlateMerchantRate(transInfo.getMerchantRate());
							detailT1.setPlateTransSource(transInfo.getTransSource());
							detailT1.setBagSettle(transInfo.getBagSettle());
							detailT1.setPosType(transInfo.getPosType());
							detailT1.setPlateOrderNo(transInfo.getOrderNo());
							detailT1.setPlateAgentShareAmount(transInfo.getSingleShareAmount());
							detailT1.setSettleStatus(transInfo.getSettleStatus());
							detailT1.setSettlementMethod(transInfo.getSettlementMethod());
							detailT1.setAccount(transInfo.getAccount());
							detailT1.setFreezeStatus(transInfo.getFreezeStatus());
							detailT1.setOutBillStatus("0");
							detailT1.setSettleType(transInfo.getSettleType());
//							detailT1.setPlateMerchantSettleDate(transInfo.getMerchantSettleDate());


							duiAccountDetailService.updateDuiAccountForT1(detailT1);

						}else{
							transInfoService.updateScanCodeTrans(transInfo);
						}
						log.info("上游单边补记账结算商户:"+duiAccountDetail.getId()+"--2");
						transInfoService.updateCollectiveTransOrder(transInfo);//修改交易状态为成功
						duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");//上游单边补记账结算商户
						duiAccountDetailService.updateDuiAccount(duiAccountDetail) ;
						succCount++;
					}else{
						log.info(duiAccountDetail.getId()+":未找到对应的交易记录");
					}

				}catch(Exception e){
					failedCount++;
					log.error("异常:",e);
				}

			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","上游单边补记账结算账户处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
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
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
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
		for(int i=0 ;i<params.size()-1 ;i++){
			paramsName = "selectId["+i+"]" ;
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
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
			}
			if("upstreamRefund".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
			}
			//2、查询对账详细信息中的    差错处理状态   是否为'上游单边记存疑问'
			if(!"upstreamDoubt".equals(duiAccountDetail.getErrorHandleStatus())){
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

		if(flag){
			for(int i=0 ;i<params.size()-1 ;i++){
				try{
					paramsName = "selectId["+i+"]" ;
					DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
					duiAccountDetail.setErrorHandleStatus("upstreamRefund");
					duiAccountDetailService.updateDuiAccount(duiAccountDetail);
					succCount++;

				}catch(Exception e){
					failedCount++;
					log.error("异常:",e);
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
	 * 上游单边确认是日切
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
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
			DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
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
			}
			if("upstreamThaw".equals(duiAccountDetail.getErrorHandleStatus())){
				errMsg += "【"+(i+1)+"】,";
				flag = false;
				continue;
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
					DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;

					AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(duiAccountDetail.getAcqEnname());
					msg = duiAccountDetailService.acqSingleThaw(duiAccountDetail,acqOrg) ;

					if(!(boolean)msg.get("state")){
						failedCount++;
					}else{
						succCount++;
					}

				}catch(Exception e){
					failedCount++;
					log.error("异常:",e);
				}
			}
			if(failedCount>0){
				msg.put("state",false);
			}else{
				msg.put("state",true);
			}
			msg.put("msg","上游单边确认是日切处理成功"+succCount+"条"+",处理失败"+failedCount+"条");
		}
		log.info(msg.toString());
		return msg ;
	}

	
	/**
	 * 确认记账
	 * @param checkBatchNo
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountMsg:confirmAcc')")
	@RequestMapping(value = "/confirmAccount.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> confirmAcc(@RequestParam(value = "checkBatchNo", required = true)String checkBatchNo,@RequestParam String acqEnname) throws Exception{
		Map<String, Object> msg = new HashMap<String,Object>();
		DuiAccountBatch batch = duiAccountBatchService.getByCheckBatchNo(checkBatchNo);
		if (batch != null && batch.getRecordStatus().equals(1)) {
			msg.put("state", false);
			msg.put("msg", "该批次已记账，不能再次记账");
			log.info(msg.toString());
			return msg;
		}
		AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(acqEnname);
		msg = confirmAccount(checkBatchNo,acqOrg.getId().toString());
		if ((boolean)msg.get("state") == true) {
			duiAccountBatchService.updateRecordStatus(checkBatchNo, 1);
		} else {
			duiAccountBatchService.updateRecordStatus(checkBatchNo, 0);
		}
		
		return msg;
	}
	public Map<String,Object> confirmAccount(String checkBatchNo,String orgId) throws Exception{
		Map<String, Object> accMsg = new HashMap<String, Object>();
		//查询所有未成功的对账数据
		List<DuiAccountDetail> duiacc = duiAccountDetailService.findErrorDuiAccountDetailList(checkBatchNo);
		if(duiacc.size()>0){
			int successCount = 0;
			int failedCount = 0;
			String acqEnname = "";
			CollectiveTransOrder transInfo = null;
			for (int i = 0; i < duiacc.size(); i++) {
				DuiAccountDetail errorDetail = duiacc.get(i);
				try{
					if("PLATE_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，平台单边记存疑
//						DuiAccountDetail ps = duiAccountDetailService.findError(errorDetail.getOrderReferenceNo(),errorDetail.getAcqEnname());
                        DuiAccountDetail ps =duiAccountDetailService.findAcq(errorDetail);//查找收单单边的对账数据
						if(ps!=null){
							duiAccountDetailService.updateDuiAccountDetailAuto(errorDetail,ps);
                            errorDetail.setRecordStatus(1);
                            successCount++;
						}else{
							if(errorDetail.getAccount()==1 && "0".equals(errorDetail.getFreezeStatus())
									&& (errorDetail.getSettleStatus()==0 || errorDetail.getSettleStatus()==3
									|| errorDetail.getSettleStatus()==4) ){//冻结
								acqEnname = errorDetail.getAcqEnname();
								/*if("neweptok".equals(acqEnname) || "ZF_ZQ".equals(acqEnname)){//平台单边可以使用订单号进行查询
									transInfo = transInfoService.findAcqTransInfoByOrderRefNoAndAcqName(errorDetail.getOrderReferenceNo(), errorDetail.getAcqEnname());
									log.info(acqEnname+": 收单机构名称："+transInfo.getAcqEnname() +"平台订单号："+transInfo.getOrderNo());
								}else{
								}*/
                              /*  Map<String,Object> msg = this.platformSingleMarkSuspect(errorDetail ,transInfo, orgId) ;//这个接口调用了冻结
                                Map<String,Object> msg = this.transForzen(transInfo,"3");
								if ((boolean)msg.get("state") == false){
                                    log.info("财务冻结失败原因："+msg.get("msg").toString());
									String errMsg = msg.get("msg").toString();
									errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
//									errorDetail.setErrorHandleStatus("accountFailed");//记账失败
									errorDetail.setErrorMsg(errMsg);
									errorDetail.setRecordStatus(0);
									failedCount++;
								}else {
								}*/
                                transInfo = collectionTransOrderService.queryByAcqFastTransInfo(errorDetail.getAcqMerchantOrderNo(), errorDetail.getAcqEnname());
                                collectionTransOrderService.updateFreezeStatusByOrderNo(transInfo.getOrderNo(), "3");//解冻  3-财务冻结
                                errorDetail.setRecordStatus(1);
                                errorDetail.setErrorHandleStatus("pedingStreamDoubt");//平台单边记存疑
                                successCount++;
                                duiAccountDetailService.updateDuiAccError(errorDetail.getId().intValue(), "2", "3");//处理方式标记为冻结，冻结状态改为财务冻结

							}else{
								BigDecimal preFrozenAmount = errorDetail.getPlateTransAmount().subtract(errorDetail.getPlateMerchantFee());
								TransInfoPreFreezeLog record = new TransInfoPreFreezeLog();
								record.setOperId("account");
								record.setOperName("sys");
								record.setPreFreezeNote("差错短款");
								record.setPreFreezeAmount(preFrozenAmount);
								record.setMerchantNo(errorDetail.getPlateMerchantNo());
								record.setOperTime(new Date());
								merchantInfoService.insertLogAndUpdateMerchantInfoAmount(record);
								duiAccountDetailService.updateDuiAccError(errorDetail.getId().intValue(),"1",errorDetail.getFreezeStatus());//标记为预冻结,冻结状态不做修改
                                errorDetail.setRecordStatus(1);
                                errorDetail.setErrorHandleStatus("pedingStreamDoubt");//平台单边记存疑
                                successCount++;
                            }
//								errorDetail.setErrorHandleStatus("checkForzen");
						}


					}else if("FAILED".equals(errorDetail.getCheckAccountStatus()) || "ACQ_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，收单单边
						acqEnname = errorDetail.getAcqEnname();
						DuiAccountDetail as =duiAccountDetailService.findPlate(errorDetail);//查找平台单边的对账数据
						if(as!=null){
							if("neweptok".equals(acqEnname) || "ZF_ZQ".equals(acqEnname) || "YS_ZQ".equals(acqEnname)){
								transInfo = transInfoService.getCollectiveTransData(errorDetail.getAcqMerchantNo(),errorDetail.getAcqEnname(),errorDetail.getAcqSerialNo(),errorDetail.getAcqTerminalNo(),errorDetail.getAcqAccountNo());
								log.info(acqEnname+"： 收单机构名称："+transInfo.getAcqEnname() +"订单号："+transInfo.getOrderNo());
							}else{
								String orderNo = "";
								switch (acqEnname){
									case "zm_xe":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "zm_pay":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "WF_ZQ":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "openPay":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "jc_pay":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "HLJC":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "hljcLd":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "ZFYL_ZQ":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "ZY":
									case "ZYLD":
									case "ds_pay":
										orderNo = errorDetail.getAcqOrderNo();
										break;
									case "YBEI":
										orderNo = errorDetail.getAcqOrderNo();
										break;
                                    default :
										orderNo = errorDetail.getAcqMerchantOrderNo();
										break;
								}
								if(!orderNo.equals(""))
									transInfo = collectionTransOrderService.queryByAcqFastTransInfo(orderNo, errorDetail.getAcqEnname());
							}
							if (transInfo == null) {
								log.info("参考号："+errorDetail.getAcqReferenceNo()+"找不到交易源");
								errorDetail.setErrorMsg("参考号："+errorDetail.getAcqReferenceNo()+"找不到交易源");
								errorDetail.setRecordStatus(0);
								failedCount++;
							}else {
                                if ("3".equals(transInfo.getFreezeStatus())) {//该笔交易为财务冻结
                                    collectionTransOrderService.updateFreezeStatusByOrderNo(transInfo.getOrderNo(), "0");//解冻  0-未冻结
                                    duiAccountDetailService.updateDuiAccError(errorDetail.getId().intValue(), "0", "0");//解冻标记为未冻结
                                } else {
                                    if ("1".equals(as.getTreatmentMethod())) {
                                        BigDecimal preFrozenAmount = as.getPlateTransAmount().subtract(as.getPlateMerchantFee());
                                        TransInfoPreFreezeLog record = new TransInfoPreFreezeLog();
                                        record.setOperId("account");
                                        record.setOperName("account");
                                        record.setPreFreezeNote("差错长款");
                                        record.setPreFreezeAmount(preFrozenAmount);
                                        record.setMerchantNo(transInfo.getMerchantNo());
                                        record.setOperTime(new Date());
                                        merchantInfoService.insertPreFreezeLog(record);
                                    }
                                }
                                duiAccountDetailService.updatePlateDuiAccountDetailAuto(errorDetail, as);
                                errorDetail.setErrorHandleStatus("upstreamDoubt");
                                errorDetail.setRecordStatus(1);
                                successCount++;
                            }
						}else{
							/*Map<String,Object> msg = this.acqSingleMarkSuspect(errorDetail ,transInfo,orgId) ;
//                            Map<String,Object> msg = this.transForzen(transInfo,"0");
							if((boolean)msg.get("state") == false){
                                log.info("上游单边记账失败原因："+msg.get("msg").toString());
								String errMsg = msg.get("msg").toString();
								errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
//								errorDetail.setErrorHandleStatus("accountFailed");//记账失败
								errorDetail.setErrorMsg(errMsg);
								errorDetail.setRecordStatus(0);
								failedCount++;
								log.info("上游单边记账失败原因："+msg.get("msg").toString());
							}else {
                                errorDetail.setErrorHandleStatus("upstreamDoubt");
                                errorDetail.setRecordStatus(1);
                                successCount++;
                            }*/
                            duiAccountDetailService.updateDuiAccError(errorDetail.getId().intValue(), "3", errorDetail.getFreezeStatus());//处理方式记为标记
                            errorDetail.setErrorHandleStatus("upstreamDoubt");
                            errorDetail.setRecordStatus(1);
                            successCount++;
                        }

					}
					/*else if (DuiAccountStatus.AMOUNT_FAILED.toString().equals(errorDetail.getCheckAccountStatus())) {
						//金额不符
						String errMsg = "商户"+errorDetail.getPlateMerchantNo()+"与上游商户"+errorDetail.getAcqMerchantNo()+"金额不符";
						errorDetail.setErrorHandleStatus("accountFailed");//记账失败
						errorDetail.setErrorMsg(errMsg);
						errorDetail.setRecordStatus(0);
						failedCount++;
					}*/
				}catch(Exception e){
//					errorDetail.setErrorHandleStatus("accountFailed");//记账失败
					errorDetail.setErrorMsg("记账过程异常");
					errorDetail.setRecordStatus(0);
					failedCount++;
					e.printStackTrace();
				}
				duiAccountDetailService.updateDuiAccount(errorDetail) ;
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
			accMsg.put("state", true);
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
	
	
	
	public Map<String, Object> platformSingleMarkSuspect(DuiAccountDetail duiAccountDetail, CollectiveTransOrder transInfo, String acqOrgId) throws Exception {
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
			claims.put("transOrderNo", transInfo.getOrderNo().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("agentShareAmount", transInfo.getProfits1().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/platformSingleMarkSuspect.do" ;
			log.info("平台单边记存疑url："+url);
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

	public Map<String, Object> acqSingleMarkSuspect(DuiAccountDetail duiAccountDetail, CollectiveTransOrder transInfo,String acqOrgId) throws Exception {
		Map<String,Object> msg=new HashMap<>();

		//调用6.4.8上游单边记存疑
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd") ;

		try{
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", duiAccountDetail.getAcqTransTime() == null ?null:sdf.format(duiAccountDetail.getAcqTransTime())) ;//交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString() : duiAccountDetail.getAcqTransAmount().toString()) ;//交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000004");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", duiAccountDetail.getId().toString());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/acqSingleMarkSuspect.do" ;
			log.info("上游单边记存疑问url："+url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边记存疑问返回结果："+response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response) || (boolean)resp.get("status") == false) {
				//上游单边，结算给商户失败
				msg.put("state",false);
				msg.put("msg","上游单边记存疑问失败,reason:"+(resp.get("msg")==null ? "null":resp.get("msg").toString()));
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
			log.info(msg.toString());
			log.error("异常:",e);
		}
		return msg;
	}


	/**
	 * 调用交易冻结
	 * @param transInfo
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> transForzen(CollectiveTransOrder transInfo,String freezeStatus) throws Exception {
		Map<String,Object> msg=new HashMap<>();

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
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("transOrderNo", transInfo.getOrderNo());
			claims.put("freezeStatus", freezeStatus);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/transInfoApiAction/freezeTrans.do" ;
			log.info("财务冻结url："+url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("财务冻结返回结果："+response);

			if (response == null || "".equals(response)) {
				msg.put("state",false);
				msg.put("msg","财务冻结返回为空！");
				log.info(msg.toString());
				return msg;
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					msg.put("state",false);
					msg.put("msg","财务冻结，reason:"+resp.get("msg").toString());
					log.info(msg.toString());
					return msg;
				}
			}
			msg.put("state",true);
			msg.put("msg","财务冻结成功！");
			log.info(msg.toString());
		}catch(Exception e){
			log.info("财务冻结异常"+e.getMessage());
			msg.put("state",false);
			msg.put("msg","财务冻结异常！");
			log.info(msg.toString());
			log.error("异常:",e);
		}
		return msg;
	}
	/**
	 * 修改备注
	 * @param detail
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:query')")
	@RequestMapping(value = "/updateRemark.do",method=RequestMethod.POST)
	@Logs(description="修改对账错误处理备注")
	@ResponseBody
	public Map<String, Object> updateRemark(@ModelAttribute DuiAccountDetail detail) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		
		int re = duiAccountDetailService.updateRemark(detail);
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

	/**
	 * 差错初审
	 * @param detail
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/updateErrorCheck.do",method=RequestMethod.POST)
	@Logs(description="差错初审")
	@ResponseBody
	public Map<String, Object> updateErrorCheck(@ModelAttribute DuiAccountDetail detail) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		int no = getBatchNo("error_serail");
		String checkNo = "CC"+FileNameUtil.getDateDic()+String.format("%05d", no);
		detail.setCheckNo(checkNo);
		detail.setErrorCheckCreator(userInfo.getUsername());
		int re = duiAccountDetailService.updateErrorCheck(detail);
		if (re > 0) {
			msg.put("status", true);
			msg.put("msg", "初审成功");
			String dateSerial = checkNo;
			SysConfig config = sysConfigService.getByKey("error_serail");
			config.setParamValue(FileNameUtil.getDateDic()+"_"+String.format("%05d", no));
			sysConfigService.update(config);
		} else {
			msg.put("status", false);
			msg.put("msg", "初审失败");
		}
		log.info(msg.toString());
		return msg;
	}
	@RequestMapping(value = "/queryCheck.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> query(ModelMap model,@ModelAttribute DuiAccountDetail detail) throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();

		DuiAccountDetail re = duiAccountDetailService.findDuiAccountDetailById(detail.getId().toString());
		msg.put("status", true);
		msg.put("duiq", re);
		model.put("data", re);

		return msg;

	}
	public int getBatchNo(String sysParams){
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

	/**
	 * 同步结算状态
	 * @return
	 * @throws Exception
	 */

	@PreAuthorize("hasAuthority('duiAccountMsgDetail:sysSettleStatus')")
	@RequestMapping(value = "/sysSettleStatus.do",method=RequestMethod.POST)
	@Logs(description="同步结算状态")
	@ResponseBody
	public Map<String, Object> sysSettleStatus(@ModelAttribute DuiAccountDetail detail) throws Exception{
		Map<String,Object> msg=new HashMap<>();
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
				msg.put("state",false);
				msg.put("msg","同步结算状态失败！");
				log.info(msg.toString());
				return msg;
			}else{
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if((boolean)resp.get("status") == false){
					msg.put("state",false);
					msg.put("msg",resp.get("msg"));
					log.info(msg.toString());
					return msg;
				}else{
					msg.put("state",true);
					msg.put("msg",resp.get("msg"));
				}
			}
			log.info(msg.toString());
		}catch(Exception e){
			log.info("同步结算状态异常"+e.getMessage());
			msg.put("state",false);
			msg.put("msg","同步结算状态异常！");
			log.info(msg.toString());
			log.error("异常:",e);
		}
		return msg;
	}


	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}



	/**
	 * 同步状态
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('duiAccountErrorHandler:errorHandler')")
	@RequestMapping(value="/syncOrderStatus.do",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> syncOrderStatus(ModelMap model ,@RequestParam Map<String, String> params) throws Exception{
		Map<String,Object> msg=new HashMap<>();

		String paramsName ;
		int succCount=0;
		int failedCount=0;
		String errMsg ="";


		for(int i=0 ;i<params.size()-1 ;i++){
			try{
				paramsName = "selectId["+i+"]" ;
				DuiAccountDetail duiAccountDetail = duiAccountDetailService.findDuiAccountDetailById(params.get(paramsName)) ;
				String acqEnname = duiAccountDetail.getAcqEnname();
				String orderNo = duiAccountDetail.getOrderReferenceNo();
				CollectiveTransOrder transInfo = null;
				transInfo = transInfoService.findOrderSomeStatus(orderNo);
				duiAccountDetail.setAccount(transInfo.getAccount());
				duiAccountDetail.setFreezeStatus(transInfo.getFreezeStatus());
				duiAccountDetail.setSettleStatus(transInfo.getSettleStatus());
				duiAccountDetail.setPlateTransStatus(transInfo.getTransStatus());
				duiAccountDetailService.updateDuiAccountStatus(duiAccountDetail);

			}catch(Exception e){
				failedCount++;
				log.error("异常:",e);
			}

		}
		if(failedCount>0){
			msg.put("state",false);
		}else{
			msg.put("state",true);
		}
		msg.put("msg","对账详情状态同步数据"+succCount+"条"+",处理失败"+failedCount+"条");

		log.info(msg.toString());
		return msg ;
	}
}
