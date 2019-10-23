package cn.eeepay.boss.action;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
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
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.enums.ReverseStatus;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.Batches;
import cn.eeepay.framework.model.bill.BatchesDetail;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ExtAccountHistoryBalance;
import cn.eeepay.framework.model.bill.InsAccountHistoryBalance;
import cn.eeepay.framework.model.bill.OutChannelLadderRateRebalance;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.ServiceInfo;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.model.bill.TransImportInfo;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.nposp.AcqMerchant;
import cn.eeepay.framework.model.nposp.AcqService;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.model.nposp.BusinessProductDefine;
import cn.eeepay.framework.model.nposp.HardwareProduct;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.service.bill.BatchesDetailService;
import cn.eeepay.framework.service.bill.BatchesService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.OutChannelLadderRateRebalanceService;
import cn.eeepay.framework.service.bill.RecordAccountRuleService;
import cn.eeepay.framework.service.bill.RecordAccountRuleTransTypeService;
import cn.eeepay.framework.service.bill.ReportService;
import cn.eeepay.framework.service.bill.ShiroRigthService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.bill.UserRigthService;
import cn.eeepay.framework.service.nposp.AcqMerchantService;
import cn.eeepay.framework.service.nposp.AcqOrgService;
import cn.eeepay.framework.service.nposp.AcqServiceService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.service.nposp.BusinessProductDefineService;
import cn.eeepay.framework.service.nposp.HardwareProductService;
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.service.nposp.OutAccountServiceService;
import cn.eeepay.framework.service.nposp.ServiceInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.UrlUtil;

import com.auth0.jwt.JWTSigner;

/**
 * 报表管理
 * @author hj
 * 2016年7月25日  下午10:05:27
 */
@Controller
@RequestMapping(value = "/reportAction")
public class ReportAction {
	private static final Logger log = LoggerFactory.getLogger(ReportAction.class);
	@Resource
	public ShiroRigthService shiroRigthService;
	@Resource
	public UserRigthService userRigthService;
	@Resource
	public SysDictService sysDictService ;
	@Resource
	public CurrencyService currencyService ;
	@Resource
	public ReportService reportService ;

	@Resource
	private AcqOrgService acqOrgService ;
	@Resource
	private MerchantInfoService merchantInfoService ;
	@Resource
	private AcqMerchantService acqMerchantService;
	@Resource
	private AgentInfoService agentInfoService ;
	@Resource
	public RecordAccountRuleService recordAccountRuleService;
	@Resource
	public RecordAccountRuleTransTypeService recordAccountRuleTransTypeService;
	@Resource
	private BusinessProductDefineService businessProductDefineService;
	@Resource
	private HardwareProductService hardwareProductService;
	@Resource
	private ServiceInfoService serviceInfoService;
	@Resource
	private AcqServiceService acqServiceService;
	@Resource
	private OutAccountServiceService outAccountServiceService;
	@Resource
	private BatchesService batchesService;
	@Resource
	private BatchesDetailService batchesDetailService;
	@Resource
	private SystemInfoService systemInfoService;
	@Resource
	private OutChannelLadderRateRebalanceService outChannelLadderRateRebalanceService;
	
	@Value("${accountApi.http.url}")  
    private String accountApiHttpUrl;
	
	@Value("${accountApi.http.secret}")  
    private String accountApiHttpSecret;
	
	//跳转到  科目每日余额查询  页面
	@PreAuthorize("hasAuthority('subjectDayAmount:query')")
	@RequestMapping(value = "/toSubjectDayAmount.do")
	public String toSubjectDayAmount(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> subjectLevelList = sysDictService.findSysDictGroup("sys_subject_level");		//科目级别
		model.put("subjectLevelList", subjectLevelList);
		List<Currency> currencyList = currencyService.findCurrency() ;		//币种号
		model.put("currencyList", currencyList);
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");		//余额方向
		model.put("balanceFromList", balanceFromList);
		
		return "report/subjectDayAmount";
	}

	//跳转到  内部账户历史余额查询  页面
	@PreAuthorize("hasAuthority('inAccountHistoryAmount:query')")
	@RequestMapping(value = "/toInAccountHistoryAmount.do")
	public String toInAccountHistoryAmount(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
		model.put("accountStatusList", accountStatusList);
		List<Currency> currencyList = currencyService.findCurrency() ;
		model.put("currencyList", currencyList);
		
		return "report/inAccountHistoryAmount";
	}
	

	//跳转到  外部账户历史余额查询  页面
	@PreAuthorize("hasAuthority('extAccountHistoryAmount:query')")
	@RequestMapping(value = "/toExtAccountHistoryAmount.do")
	public String toExtAccountHistoryAmount(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> accountTypeList = null;
        accountTypeList = sysDictService.findSysDictGroup("sys_account_type");
		List<SysDict> accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
		model.put("accountStatusList", accountStatusList);
		List<Currency> currencyList = currencyService.findCurrency() ;
        String date = DateUtil.getFormatDate("yyyy-MM-dd",new Date());
        params.put("beginDate",date);
        params.put("endDate",date);
        model.put("params",params);
		model.put("currencyList", currencyList);
		model.put("accountTypeList", accountTypeList);
		return "report/extAccountHistoryAmount";
	}
	

	//跳转到 交易流水查询  页面
	@PreAuthorize("hasAuthority('transFlow:query')")
	@RequestMapping(value = "/toTransFlow.do")
	public String toTransFlow(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		
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
		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");		//来源系统
		List<RecordAccountRuleTransType> transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
		List<AcqOrg> acqOrgList = acqOrgService.findAllAcqOrg();
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");		//记账状态
		List<SysDict> reverseFlagList = sysDictService.findSysDictGroup("sys_reverse_flag");		//冲销标记
		List<SysDict> reverseStatusList = sysDictService.findSysDictGroup("sys_reverse_status");		//冲销标记


		String date = DateUtil.getFormatDate("yyyy-MM-dd",new Date());
		params.put("recordBeginDate",date);
		params.put("recordEndDate",date);

		params.put("transBeginDate",date);
		params.put("transEndDate",date);
		model.put("params",params);

		model.put("fromSystemList", fromSystemList);
		model.put("transTypeList", transTypeList);
		model.put("acqOrgList", acqOrgList);
		model.put("recordStatusList", recordStatusList);
		model.put("reverseFlagList", reverseFlagList);
		model.put("reverseStatusList", reverseStatusList);
		model.put("params", params);
		return "report/transFlow";
	}
	
	
	//跳转到 交易 详情  页面
	@RequestMapping(value = "/toTransFlowDetail.do")
	public String toTransFlowDetail(ModelMap model, @RequestParam Map<String, String> params, @RequestParam Integer id) throws Exception{
		TransImportInfo info = reportService.getById(id);
		Map<String, Object> data = new HashMap<String, Object>();
		
		//来源系统
		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");
		for (SysDict dict : fromSystemList) {
			if (dict.getSysValue().equals(info.getFromSystem())) {
				data.put("fromSystem", dict.getSysName());
				//原交易来源系统
				data.put("oldFromSystem", dict.getSysName());
				break;
			}
		}
		data.put("transDate", DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", info.getTransDate()));  //交易时间
		data.put("fromSerialNo", info.getFromSerialNo());  //来源系统流水号
		
		if (StringUtils.isNotBlank(info.getTransType())) {
			RecordAccountRuleTransType tt = recordAccountRuleTransTypeService.getByTransCodeAndFromSystem(info.getTransType(), info.getFromSystem());
			data.put("transType", info.getTransType()+tt.getTransTypeName());
		}
		
		//交易金额
		data.put("transAmount", info.getTransAmount());
		
		//冲销状态
		if ("1".equals(info.getReverseStatus()) || ReverseStatus.REVERSED.toString().equals(info.getReverseStatus()) ) {
			data.put("reverseStatus", "已冲销");
		} else {
			data.put("reverseStatus", "正常");
		}
		
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");		//记账状态
		for (SysDict sysDict : recordStatusList) {
			if (sysDict.getSysValue().equals(info.getRecordStatus())) {
				data.put("recordStatus", sysDict.getSysName());
				break;
			}
		}
		
		//记账标志
//		if ("1".equals(info.getRecordStatus())) {
//			data.put("recordStatus", "成功");
//		} else {
//			data.put("recordStatus", "失败");
//		}
		
		
		
		if ("1".equals(info.getReverseFlag()) || ReverseFlag.REVERSE.equals(info.getReverseFlag())) {
			//冲销交易
			data.put("isReverse", true);
			data.put("reverseFlag", "冲销交易");
			
			//原交易交易时间
			data.put("oldTransDate", DateUtil.getDefaultFormatDate(info.getTransDate()));
			//原交易来源系统流水号
			data.put("oldFromSerialNo", info.getReverseSerialNo());
		} else {
			//正常交易
			data.put("isReverse", false);
			data.put("reverseFlag", "正常交易");
			data.put("terminalNo", info.getTerminalNo());  //终端号
			data.put("deviceSn", info.getDeviceSn());  //机具号
			data.put("merchantSettleFee", info.getMerchantSettleFee()); //提现手续费
			data.put("merchantFee", info.getMerchantFee());  //交易手续费
			data.put("transOrderNo", info.getTransOrderNo());
			
			data.put("cardNo", info.getCardNo());
			List<SysDict> cardTypeList = sysDictService.findSysDictGroup("sys_card_type");		//记账状态
			for (SysDict sysDict : cardTypeList) {
				if (sysDict.getSysValue().equals(info.getCardType())) {
					data.put("cardType", sysDict.getSysName());
					break;
				}
			}
			
			//商户名称
			if (StringUtils.isNotBlank(info.getMerchantNo())) {
				MerchantInfo mi = merchantInfoService.findMerchantInfoByUserId(info.getMerchantNo());
				data.put("merchantName", mi != null ? mi.getMerchantName()+"("+info.getMerchantNo()+")" : "");
			}
			//业务产品名称
			if (StringUtils.isNotBlank(info.getBpId())){
				Integer bpId = Integer.parseInt(info.getBpId());
				BusinessProductDefine bpd = businessProductDefineService.getById(bpId);
				data.put("bpName", bpd != null ? bpd.getBpName() : "");
			}
			//硬件产品类型
			if (StringUtils.isNotBlank(info.getHpId())) {
				Long hpId = Long.parseLong(info.getHpId());
				HardwareProduct hp = hardwareProductService.getById(hpId);
				data.put("hpType", hp != null ? hp.getTypeName()+hp.getVersionNu() : "");
			}
			ServiceInfo si = null;
			if (StringUtils.isNotBlank(info.getServiceId())) {
				Long serviceId = Long.parseLong(info.getServiceId());
				si = serviceInfoService.getById(serviceId);
			}
			if (StringUtils.isNotBlank(info.getOutServiceId())) {
				//交易服务种类
				data.put("serviceType2", si != null ? si.getServiceName() : "");
				//交易服务费率
				data.put("merchantRate2", info.getMerchantRate());
			} else {
				//关联提现服务种类
				data.put("serviceType1", si != null ? si.getServiceName() : "");
				//关联提现服务费率
				data.put("merchantRate1", info.getMerchantRate());
			}
			
			//一级代理商编号
			data.put("oneAgentNo", info.getOneAgentNo());
			//一级代理商名称
			if (StringUtils.isNotBlank(info.getOneAgentNo())) {
				AgentInfo ai = agentInfoService.findEntityByAgentNo(info.getOneAgentNo());
				data.put("agentName", ai != null ? ai.getAgentName() : "");
			}
			//交易分润金额
			data.put("agentShareAmount", info.getAgentShareAmount());
			//提现分润金额
			data.put("agentSettleShareAmount", info.getAgentSettleShareAmount());
			
			//收单机构-机构名称
			if (StringUtils.isNotBlank(info.getAcqEnname())) {
				AcqOrg ao = acqOrgService.findAcqOrgByAcqEnname(info.getAcqEnname());
				data.put("acqOrgName", ao.getAcqName());
			}
			//收单商户名称
			if (StringUtils.isNotBlank(info.getAcqMerchantNo())) {
				AcqMerchant mi = acqMerchantService.getByAcqMerchantNo(info.getAcqMerchantNo());
				data.put("acqMerchantName", mi != null ? mi.getAcqMerchantName()+"("+info.getAcqMerchantNo()+")" : "");
			}
			//收单服务
			if (StringUtils.isNotBlank(info.getAcqServiceId())) {
				Integer acqServiceId = Integer.parseInt(info.getAcqServiceId());
				AcqService as = acqServiceService.getById(acqServiceId);
				data.put("acqServiceName", as != null ? as.getServiceName() : "");
			}
			//收单服务费1
			data.put("acqOrgFee1", info.getAcqOrgFee1());
			
			//出款通道------
			//机构名称
			if (StringUtils.isNotBlank(info.getOutAcqEnname())) {
				AcqOrg ao = acqOrgService.findAcqOrgByAcqEnname(info.getOutAcqEnname());
				data.put("outOrgName", ao.getAcqName());
			}
			//出款服务
			if (StringUtils.isNotBlank(info.getOutServiceId())) {
				Integer outServiceId = Integer.parseInt(info.getOutServiceId());
				OutAccountService oas = outAccountServiceService.getById(outServiceId);
				data.put("outServiceName", oas != null ? oas.getServiceName() : "");
			}
			outAccountServiceService.getById(id);
			//出款服务费1
			data.put("outOrgFee1", info.getOutRateFee1());
			//出款服务费2
			data.put("outOrgFee2", info.getOutRateFee2());
			//出款服务费率1
			data.put("outRate1", info.getOutRate1());
			//出款服务费率2
			data.put("outRate2", info.getOutRate2());
		}
		
		model.addAttribute("data", data);
		model.put("params", params);
		log.info(data.toString());
		return "report/transFlowDetail";
	}
	
	//跳转到 出款通道阶梯费率返差  页面
	@PreAuthorize("hasAuthority('findOutChannelRate:query')")
	@RequestMapping(value = "/toFindOutChannelRate.do")
	public String toFindOutChannelRate(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> recordStatusList = sysDictService.findSysDictGroup("sys_record_status");		//记账状态
		model.put("recordStatusList", recordStatusList);
		return "report/outChannelRate";
	}
	
	//出款通道阶梯费率返差查询
	@PreAuthorize("hasAuthority('findOutChannelRate:query')")
	@RequestMapping(value="/findOutChannelRate.do")
	@ResponseBody
	public Page<OutChannelLadderRateRebalance> findOutChannelRate(@ModelAttribute OutChannelLadderRateRebalance obj,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<OutChannelLadderRateRebalance> page){
		try {
			outChannelLadderRateRebalanceService.findOutChannelRate(obj, sort, page) ;
		} catch (Exception e) {
			log.error("出款通道阶梯费率返差查询出现异常"+e.getMessage());
			log.error("异常:",e);
//			e.printStackTrace();
		}
		return page;
	}
	
	@PreAuthorize("hasAuthority('findOutChannelRate:query')")
	@RequestMapping(value = "/saveRealRelalance.do")
	@ResponseBody
	public Map<String, Object> saveRealRelalance(ModelMap model,
			@RequestParam Map<String, String> params,
			@ModelAttribute OutChannelLadderRateRebalance obj)
					throws Exception {
		Map<String, Object> msg = new HashMap<>();
		try {
			if (obj.getRealRebalance() == null) {
				//输入的值小于0
				msg.put("status", false);
				msg.put("msg", "请填写实际返差金额!");
				return msg;
			}
			OutChannelLadderRateRebalance old = outChannelLadderRateRebalanceService.getById(obj.getId());
			if (old.getRecordStatus() == 1) {
				//已经记账
				msg.put("status", false);
				msg.put("msg", "请不要重复记账");
				return msg;
			}

			int i = outChannelLadderRateRebalanceService
					.updateRealRebalance(obj);

			if (i > 0) {
				msg.put("status", true);
				msg.put("msg", "修改成功!");
				log.info(msg.toString());
			} else {
				msg.put("status", false);
				msg.put("msg", "修改失败");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("异常:",e);
			msg.put("status", false);
			msg.put("msg", "修改实际返差金额失败！");
			log.error(msg.toString());
		}
		return msg;

	}
	
	//出款通道阶梯费率返差记账
	@PreAuthorize("hasAuthority('findOutChannelRate:query')")
	@RequestMapping(value = "/outChannelRateRecord.do")
	@ResponseBody
	public void outChannelRateRecord(@RequestParam(value = "id")Long id) {
		OutChannelLadderRateRebalance obj = outChannelLadderRateRebalanceService.getById(id);
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			//调用账户接口系统，见6.4.7上游批量代付进行记账
			final String secret = accountApiHttpSecret;

			final long iat = System.currentTimeMillis() / 1000l; // issued at claim 
			final long exp = iat + 60L; // expires claim. In this case the token expires in 60 seconds
			final String jti = UUID.randomUUID().toString();
			JWTSigner signer = new JWTSigner(secret);
			HashMap<String, Object> claims = new HashMap<String, Object>();
			claims.put("fromSystem", "accountWeb");
			claims.put("transTypeCode", "000019");
			claims.put("fromSerialNo", String.valueOf(System.currentTimeMillis()));
			claims.put("transAmount", obj.getRealRebalance().toString());
			claims.put("transDate", DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date()));
			claims.put("outServiceId", obj.getOutServiceId());
			claims.put("outAcqEnname", obj.getOutAcqEnname());
			AcqOrg acqOrg = acqOrgService.findAcqOrgByAcqEnname(obj.getOutAcqEnname());
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("iat", iat);
			claims.put("jti", jti);

			String token = signer.sign(claims);
			String url = accountApiHttpUrl+"/recordAccountController/settleOutServiceDianCostForMonth.do";
			String response = HttpConnectUtil.postHttp(url, "token", token);
			System.out.println("response:" + response);
			int result = 0;
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response) || (boolean)resp.get("status") == false) {
				//远程批处理出账失败
				data.put("success", false);
				data.put("msg", "记账失败!"+resp.get("msg"));
				log.info(data.toString());
				result = outChannelLadderRateRebalanceService.updateRecordStatus(id, 0);
			} else {
				result = outChannelLadderRateRebalanceService.updateRecordStatus(id, 1);
				if (result > 0) {
					data.put("success", true);
					data.put("msg", "记账成功");
					log.info(data.toString());
				} else {
					data.put("success", false);
					data.put("msg", "记账失败");
					log.info(data.toString());
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
			data.put("msg", "记账接口调用异常");
			data.put("success", false);
			log.error(data.toString());
//			e.printStackTrace();
		}
	}
	

	//跳转到 记账流水查询  页面
	@PreAuthorize("hasAuthority('recordAccountFlow:query')")
	@RequestMapping(value = "/toRecordAccountFlow.do")
	public String toRecordAccountFlow(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		List<SysDict> accountStatusList = sysDictService.findSysDictGroup("sys_account_status");		//账户状态
		List<Currency> currencyList = currencyService.findCurrency() ;		//币种号
		List<SysDict> accountFlagList = sysDictService.findSysDictGroup("sys_account_falg");		//内部账户、外部账户标志
		List<SysDict> accountTypeList = sysDictService.findSysDictGroup("sys_account_type");  //外部账户类型
		List<RecordAccountRuleTransType> transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
		//从交易流水表过来时，通过此id传到记账流水查询页面，然后查询此id的相关信息
		String importId = params.get("id");
		model.put("accountStatusList", accountStatusList);
		model.put("currencyList", currencyList);
		model.put("accountFlagList", accountFlagList);
		model.put("accountTypeList", accountTypeList);
		model.put("transTypeList", transTypeList);
		model.put("importId", importId);
		model.put("params", params);
		return "report/recordAccountFlow";
	}
	
	
	//科目每日余额查询
	@PreAuthorize("hasAuthority('subjectDayAmount:query')")
	@RequestMapping(value="/findSubjectDayAmount.do")
	@ResponseBody
	@Cacheable(value="reportAction:findSubjectDayAmount.do",
			   key="#subjectInfo.currencyNo+':'+#params['beginDate']+':'+#params['endDate']"
			+ "+':'+#subjectInfo.subjectNo+':'+#subjectInfo.subjectLevel+':'+#subjectInfo.orgNo+':'+#page.pageNo+':'+#page.pageSize"
			+ "+':'+#sort.sidx+':'+#sort.sord")
	public Page<Map<String, Object>> findSubjectDayAmount(@ModelAttribute SubjectInfo subjectInfo,@RequestParam Map<String, String> params,
			@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<Map<String, Object>> page){
		try {
			reportService.findSubjectDayAmountList(subjectInfo, params, sort, page) ;
		} catch (Exception e) {
			log.error("科目每日余额查询出现异常"+e.toString());
			log.error("异常:",e);
//			e.printStackTrace();
		}
		System.out.println(page);
		return page;
	}
	
	//导出科目每日余额查询
	@PreAuthorize("hasAuthority('subjectDayAmount:export')")
	@RequestMapping(value = "exportSubjectDayAmount.do")
	public void exportSubjectDayAmount(@ModelAttribute SubjectInfo subjectInfo, @RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<SysDict> balanceFromList = null;	//余额方向
		List<Currency> currencyList = null ;	//币种号
		List<SysDict> subjectLevelList = null ;	//科目级别号
		try {
			balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
			currencyList = currencyService.findCurrency() ;
			subjectLevelList = sysDictService.findSysDictGroup("sys_subject_level");
		} catch (Exception e1) {
			log.error(e1.toString());
//			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "科目每日余额"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd") ;
		  
		//从数据库中查询数据
		  try {
			list = reportService.exportSubjectDayAmountList(subjectInfo, params) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		  for(Map<String,Object> subjectInfoQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("orgNo", subjectInfoQ.get("orgNo")==null?"":subjectInfoQ.get("orgNo").toString()) ;
			  map.put("currencyNo", exportFormat.formatCurrency(subjectInfoQ.get("currencyNo")==null?"":subjectInfoQ.get("currencyNo").toString(), currencyList)) ;
			  map.put("subjectNo", subjectInfoQ.get("subjectNo")==null?"":subjectInfoQ.get("subjectNo").toString()) ;
			  map.put("subjectName", subjectInfoQ.get("subjectName")==null?"":subjectInfoQ.get("subjectName").toString()) ;
			  map.put("subjectLevel", exportFormat.formatSysDict(subjectInfoQ.get("subjectLevel")==null?"":subjectInfoQ.get("subjectLevel").toString(),subjectLevelList)) ;
			  //map.put("createDate", subjectInfoQ.getCreateDate()==null?"":df.format(subjectInfoQ.getCreateDate())) ;
			  map.put("createDate", subjectInfoQ.get("createDate")==null?"":subjectInfoQ.get("createDate").toString()) ;
			  map.put("balanceFrom", exportFormat.formatSysDict(subjectInfoQ.get("balanceFrom")==null?"":subjectInfoQ.get("balanceFrom").toString(), balanceFromList)) ;
			  map.put("yesterdayAmount", subjectInfoQ.get("yesterdayAmount")==null?"":subjectInfoQ.get("yesterdayAmount").toString()) ;
			  map.put("todayDebitAmount", subjectInfoQ.get("todayDebitAmount")==null?"":subjectInfoQ.get("todayDebitAmount").toString()) ;
			  map.put("todayCreditAmount", subjectInfoQ.get("todayCreditAmount")==null?"":subjectInfoQ.get("todayCreditAmount").toString()) ;
			  map.put("todayBalance", subjectInfoQ.get("todayBalance")==null?"":subjectInfoQ.get("todayBalance").toString()) ;
			  log.info(map.toString());
			  data.add(map) ;
		  }
		  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"orgNo","currencyNo","subjectNo","subjectName","subjectLevel","createDate","balanceFrom","yesterdayAmount",
				  "todayDebitAmount","todayCreditAmount","todayBalance"};
		  String[] colsName = new String[]{"机构号","币种号","科目编号","科目名称","科目级别","日期","余额方向","昨日日终余额","本日借方发生额","本日贷方发生额","本日日终余额"};
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}
	
	//内部账户历史余额查询
	@PreAuthorize("hasAuthority('inAccountHistoryAmount:query')")
	@RequestMapping(value="/inAccountHistoryAmount.do")
	@ResponseBody
	public Page<Map<String, Object>> inAccountHistoryAmount(@ModelAttribute InsAccountHistoryBalance iaHistoryBalance,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<Map<String, Object>> page){
		try {
			reportService.findInAccountHistoryAmount(iaHistoryBalance, params, sort, page) ;
		} catch (Exception e) {
			log.error("内部账户历史余额查询出现异常"+e.toString());
			log.error("异常:",e);
//			e.printStackTrace();
		}
			System.out.println(page);
			return page;
	}
	//导出    内部账户历史余额查询
	@PreAuthorize("hasAuthority('inAccountHistoryAmount:export')")
	@RequestMapping(value = "exportInAccountHistoryAmount.do")
	public void exportInAccountHistoryAmount(@ModelAttribute InsAccountHistoryBalance iaHistoryBalance,@RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<SysDict> accountStatusList = null;	//用户状态
		List<Currency> currencyList = null ;	//币种号
		try {
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
			currencyList = currencyService.findCurrency() ;
		} catch (Exception e1) {
			log.error(e1.toString());
//			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "内部账历史余额"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		  
		//从数据库中查询数据
		  try {
			list = reportService.exportInAccountHistoryAmount(iaHistoryBalance, params) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		  for(Map<String, Object> iaHistoryBalanceQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("billDate", iaHistoryBalanceQ.get("billDate")==null?"":iaHistoryBalanceQ.get("billDate").toString()) ;
			  map.put("accountNo", iaHistoryBalanceQ.get("accountNo")==null?"":iaHistoryBalanceQ.get("accountNo").toString()) ;
			  map.put("orgNo", iaHistoryBalanceQ.get("orgNo")==null?"":iaHistoryBalanceQ.get("orgNo").toString()) ;
			  map.put("orgName", iaHistoryBalanceQ.get("orgName")==null?"":iaHistoryBalanceQ.get("orgName").toString()) ;
			  map.put("currency", exportFormat.formatCurrency(iaHistoryBalanceQ.get("currencyNo")==null?"":iaHistoryBalanceQ.get("currencyNo").toString(), currencyList)) ;
			  map.put("subjectNo", iaHistoryBalanceQ.get("subjectNo")==null?"":iaHistoryBalanceQ.get("subjectNo").toString()) ;
			  map.put("accountName", iaHistoryBalanceQ.get("accountName")==null?"":iaHistoryBalanceQ.get("accountName").toString()) ;
			  map.put("currBalance", iaHistoryBalanceQ.get("currBalance")==null?"":iaHistoryBalanceQ.get("currBalance").toString()) ;
			  map.put("controlAmount", iaHistoryBalanceQ.get("controlAmount")==null?"":iaHistoryBalanceQ.get("controlAmount").toString()) ;
			  map.put("parentTransBalance", iaHistoryBalanceQ.get("parentTransBalance")==null?"":iaHistoryBalanceQ.get("parentTransBalance").toString()) ;
			  map.put("accountStatus", exportFormat.formatSysDict(iaHistoryBalanceQ.get("accountStatus")==null?"":iaHistoryBalanceQ.get("accountStatus").toString(), accountStatusList)) ;
			  log.info(map.toString());
			  data.add(map) ;
		  }
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"billDate","accountNo","orgNo","orgName","currency","subjectNo","accountName",
				  "currBalance","controlAmount","parentTransBalance","accountStatus"};
		  String[] colsName = new String[]{"记账日期","账号","机构号","机构名称","币种号","科目编号","账户名称","当前余额","控制金额","上一个记账日期余额","账户状态"};
		  export.export(cols, colsName, data, response.getOutputStream());
	}

	//外部账户历史余额查询
	@PreAuthorize("hasAuthority('extAccountHistoryAmount:query')")
	@RequestMapping(value="/extAccountHistoryAmount.do")
	@ResponseBody
	public Page<Map<String, Object>> extAccountHistoryAmount(@ModelAttribute ExtAccountHistoryBalance eaHistoryBalance,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<Map<String, Object>> page){
		List<Map<String, Object>> eaHistoryBalanceList = new ArrayList<Map<String, Object>>();
		

		String userNoStrs = "" ;

		try {
			if(StringUtils.isBlank(userNoStrs)){
				userNoStrs = "-12123" ;
			}
			
			eaHistoryBalanceList = reportService.findExtAccountHistoryAmount(eaHistoryBalance, params, sort, page, userNoStrs) ;
			
			for(Map<String,Object> eaHistoryBalanceQ:eaHistoryBalanceList){
				//为每条查询出来的数据匹配 userName 和 mobilephone（boss中拿）
				if("A".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					AgentInfo agentInfo = agentInfoService.findAgentByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(agentInfo != null){
						eaHistoryBalanceQ.put("userName", agentInfo.getAgentName());
						eaHistoryBalanceQ.put("mobilephone", agentInfo.getMobilephone());
					}
				}else if("M".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					MerchantInfo merchantInfo = merchantInfoService.findMerchantInfoByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(merchantInfo != null){
						eaHistoryBalanceQ.put("userName", merchantInfo.getMerchantName());
						eaHistoryBalanceQ.put("mobilephone", merchantInfo.getMobilephone());
					}
				}else if("Acq".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					AcqOrg acqOrg = acqOrgService.findAcqOrgByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(acqOrg != null){
						eaHistoryBalanceQ.put("userName", acqOrg.getAcqName());
						eaHistoryBalanceQ.put("mobilephone", acqOrg.getPhone());
					}
				}
				
			}
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		page.setResult(eaHistoryBalanceList);
		return page;
	}
	

	//导出    外部账户历史余额查询
	@PreAuthorize("hasAuthority('extAccountHistoryAmount:export')")
	@RequestMapping(value = "exportExtAccountHistoryAmount.do")
	public void exportExtAccountHistoryAmount(@ModelAttribute ExtAccountHistoryBalance eaHistoryBalance,@RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<SysDict> accountStatusList = null;	//用户状态
		List<Currency> currencyList = null ;	//币种号
		try {
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
			currencyList = currencyService.findCurrency() ;
		} catch (Exception e1) {
			log.error("异常:",e1);
//			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "外部账历史余额"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<Map<String, Object>> list = new ArrayList<Map<String, Object>>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  
		//从数据库中查询数据
		  try {
			list = this.exportExtAccountHistoryAmountQuery(eaHistoryBalance, params) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		  for(Map<String, Object> eaHistoryBalanceQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("userId", eaHistoryBalanceQ.get("userId")==null?"":eaHistoryBalanceQ.get("userId").toString()) ;
			  //map.put("userName", eaHistoryBalanceQ.getExtAccountInfo().getUserId()) ;
			  map.put("userName", eaHistoryBalanceQ.get("userName")==null?"":eaHistoryBalanceQ.get("userName").toString()) ;
			  map.put("billDate", eaHistoryBalanceQ.get("billDate")==null?"":eaHistoryBalanceQ.get("billDate").toString()) ;
			  map.put("accountNo", eaHistoryBalanceQ.get("accountNo")==null?"":eaHistoryBalanceQ.get("accountNo").toString()) ;
			  
			  map.put("accountType", eaHistoryBalanceQ.get("accountType")==null?"":eaHistoryBalanceQ.get("accountType").toString()) ;//用户类别
			  map.put("mobilephone", eaHistoryBalanceQ.get("mobilephone")==null?"":eaHistoryBalanceQ.get("mobilephone").toString()) ;//手機號
			  
			  map.put("orgNo", eaHistoryBalanceQ.get("orgNo")==null?"":eaHistoryBalanceQ.get("orgNo").toString()) ;
			  map.put("orgName", eaHistoryBalanceQ.get("orgName")==null?"":eaHistoryBalanceQ.get("orgName").toString()) ;
			  map.put("currency", exportFormat.formatCurrency(eaHistoryBalanceQ.get("currencyNo")==null?"":eaHistoryBalanceQ.get("currencyNo").toString(), currencyList)) ;
			  map.put("subjectNo", eaHistoryBalanceQ.get("subjectNo")==null?"":eaHistoryBalanceQ.get("subjectNo").toString()) ;
			  map.put("currBalance", eaHistoryBalanceQ.get("currBalance")==null?"":eaHistoryBalanceQ.get("currBalance").toString()) ;
			  map.put("controlAmount", eaHistoryBalanceQ.get("controlAmount")==null?"":eaHistoryBalanceQ.get("controlAmount").toString()) ;
			  map.put("parentTransBalance", eaHistoryBalanceQ.get("parentTransBalance")==null?"":eaHistoryBalanceQ.get("parentTransBalance").toString()) ;
			  map.put("accountStatus", exportFormat.formatSysDict(eaHistoryBalanceQ.get("accountStatus")==null?"":eaHistoryBalanceQ.get("accountStatus").toString(), accountStatusList)) ;
			  log.info(map.toString());
			  data.add(map) ;
		  }
		  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"userId","userName","billDate","accountNo","accountType","mobilephone","orgNo","orgName","currency","subjectNo",
				  "currBalance","controlAmount","parentTransBalance","accountStatus"};
		  String[] colsName = new String[]{"用户编号","商户/代理商等名称","记账日期","账号","用户类别","手机号","机构号","机构名称","币种号","科目编号","当前余额","控制金额","上一个记账日期余额","账户状态"};
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}

	/**
	 * 用于外部历史余额导出数据的查询，从boss拿出用户名称，拼接，返回
	 * @param eaHistoryBalance
	 * @param params
	 * @return
	 */
	private List<Map<String, Object>> exportExtAccountHistoryAmountQuery(@ModelAttribute ExtAccountHistoryBalance eaHistoryBalance,@RequestParam Map<String, String> params){
		List<Map<String, Object>> eaHistoryBalanceList = new ArrayList<Map<String, Object>>();
		
		List<String> agentInfoList = new ArrayList<String>() ;
		List<String> merchantInfoList = new ArrayList<String>() ;
		List<String> acqOrgList = new ArrayList<String>() ;
		String userNoStrs = "" ;
		
		if(StringUtils.isBlank(eaHistoryBalance.getExtAccountInfo().getUserName()) && StringUtils.isBlank(eaHistoryBalance.getExtAccountInfo().getMobilephone())){
			userNoStrs = "isBlank" ;
		}else{
			agentInfoList = agentInfoService.findAgentListByParams(eaHistoryBalance.getExtAccountInfo().getUserName(), eaHistoryBalance.getExtAccountInfo().getMobilephone()) ;
			merchantInfoList = merchantInfoService.findMerchantListByParams(eaHistoryBalance.getExtAccountInfo().getUserName(), eaHistoryBalance.getExtAccountInfo().getMobilephone()) ;
			acqOrgList = acqOrgService.findAcqOrgListByParams(eaHistoryBalance.getExtAccountInfo().getUserName(), eaHistoryBalance.getExtAccountInfo().getMobilephone()) ;
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
			
			
			eaHistoryBalanceList = reportService.exportExtAccountHistoryAmount(eaHistoryBalance, params, userNoStrs) ;
			
			for(Map<String,Object> eaHistoryBalanceQ:eaHistoryBalanceList){
				//为每条查询出来的数据匹配 userName 和 mobilephone（boss中拿）
				if("A".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					AgentInfo agentInfo = agentInfoService.findAgentByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(agentInfo != null){
						eaHistoryBalanceQ.put("userName", agentInfo.getAgentName());
						eaHistoryBalanceQ.put("mobilephone", agentInfo.getMobilephone());
					}
				}else if("M".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					MerchantInfo merchantInfo = merchantInfoService.findMerchantInfoByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(merchantInfo != null){
						eaHistoryBalanceQ.put("userName", merchantInfo.getMerchantName());
						eaHistoryBalanceQ.put("mobilephone", merchantInfo.getMobilephone());
					}
				}else if("Acq".equalsIgnoreCase(eaHistoryBalanceQ.get("accountType").toString())){
					AcqOrg acqOrg = acqOrgService.findAcqOrgByUserId(eaHistoryBalanceQ.get("userId").toString()) ;
					if(acqOrg != null){
						eaHistoryBalanceQ.put("userName", acqOrg.getAcqName());
						eaHistoryBalanceQ.put("mobilephone", acqOrg.getPhone());
					}
				}
				
			}
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		return eaHistoryBalanceList;
	}
	
	//交易流水查询
	@PreAuthorize("hasAuthority('transFlow:query')")
	@RequestMapping(value="/transFlow.do")
	@ResponseBody
	public Page<TransImportInfo> transFlow(@ModelAttribute TransImportInfo transImportInfo,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<TransImportInfo> page){
		try {
			reportService.findTransFlow(transImportInfo, params, sort, page) ;
		} catch (Exception e) {
			log.error("交易流水查询出现异常"+e.toString());
			log.error("异常:",e);
//			e.printStackTrace();
		}
			System.out.println(page);
			return page;
	}
	

	//导出    交易流水查询
	@PreAuthorize("hasAuthority('transFlow:export')")
	@RequestMapping(value = "exportTransFlow.do")
	public void exportTransFlow(@ModelAttribute TransImportInfo transImportInfo,@RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<SysDict> fromSystemList = null ;	//来源系统
		List<RecordAccountRuleTransType> transTypeList = null ;		//交易类型
		List<SysDict> recordStatusList = null;  //记账状态
		List<SysDict> reverseFlagList = null;  //冲销标记
		List<SysDict> reverseStatusList = null;  //冲销标记
		try {
			fromSystemList = sysDictService.findSysDictGroup("from_system");	
			transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
			recordStatusList = sysDictService.findSysDictGroup("sys_record_status");		
			reverseFlagList = sysDictService.findSysDictGroup("sys_reverse_flag");	
			reverseStatusList = sysDictService.findSysDictGroup("sys_reverse_status");		
		} catch (Exception e1) {
			log.error("异常:",e1);
//			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "交易流水"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<TransImportInfo> list = new ArrayList<TransImportInfo>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		  
		//从数据库中查询数据
		  try {
			list = reportService.exportTransFlow(transImportInfo, params) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}        
		  for(TransImportInfo transImportInfoQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("recordSerialNo",transImportInfoQ.getRecordSerialNo()) ;
			  map.put("recordDate", transImportInfoQ.getRecordDate()==null?"":df.format(transImportInfoQ.getRecordDate())) ;
			  map.put("transDate", transImportInfoQ.getTransDate()==null?"":df.format(transImportInfoQ.getTransDate())) ;
			  map.put("transAmount", transImportInfoQ.getTransAmount() == null ?"":transImportInfoQ.getTransAmount().toString()) ;
			  map.put("transType", exportFormat.formatTransType(transImportInfoQ.getTransType(), transTypeList)) ;
			  map.put("merchantNo", transImportInfoQ.getMerchantNo()) ;
			  map.put("directAgentNo", transImportInfoQ.getDirectAgentNo()) ;
			  map.put("cardNo", transImportInfoQ.getCardNo()) ;
			  map.put("acqEnname", transImportInfoQ.getAcqEnname()) ;
			  map.put("outAcqEnname", transImportInfoQ.getOutAcqEnname()) ;
			  map.put("reverseFlag", exportFormat.formatSysDict(transImportInfoQ.getReverseFlag(), reverseFlagList)) ;
			  map.put("reverseStatus", exportFormat.formatSysDict(transImportInfoQ.getReverseStatus(), reverseStatusList)) ;
			  map.put("recordStatus", exportFormat.formatSysDict(transImportInfoQ.getRecordStatus(), recordStatusList)) ;
			  map.put("fromSystem", exportFormat.formatSysDict(transImportInfoQ.getFromSystem(),fromSystemList)) ;
			  map.put("fromSerialNo", transImportInfoQ.getFromSerialNo());
			  map.put("transOrderNo", transImportInfoQ.getTransOrderNo());
			  log.info(map.toString());
			  data.add(map) ;
		  }
		  
		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"recordSerialNo","recordStatus","recordDate","transType","fromSystem","fromSerialNo","transOrderNo","transDate","transAmount","merchantNo","directAgentNo","cardNo","acqEnname","outAcqEnname",
				  "reverseFlag","reverseStatus"};
		  String[] colsName = new String[]{"记账流水号","记账标志","记账时间","交易类型","来源系统","来源系统流水号","交易订单号","交易日期","交易金额","商户编号","代理商编号","交易卡号","收单机构（交易通道）","收单机构（出款通道）","冲销标志","冲销状态"};
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}


	//记账流水查询
	@PreAuthorize("hasAuthority('recordAccountFlow:query')")
	@RequestMapping(value="/recordAccountFlow.do")
	@ResponseBody
	public Page<CoreTransInfo> recordAccountFlow(@ModelAttribute CoreTransInfo coreTransInfo,@RequestParam Map<String, String> params,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<CoreTransInfo> page){
		try {
			reportService.findRecordFlow(coreTransInfo, params, sort, page) ;
		} catch (Exception e) {
			log.error("记账流水查询出现异常"+e.toString());
			log.error("异常:",e);
//			e.printStackTrace();
		}
			System.out.println(page);
			return page;
	}
	

	//导出    记账流水查询
	@PreAuthorize("hasAuthority('recordAccountFlow:export')")
	@RequestMapping(value = "exportRecordAccountFlow.do")
	public void exportRecordAccountFlow(@ModelAttribute CoreTransInfo coreTransInfo,@RequestParam Map<String, String> params,HttpServletResponse response,HttpServletRequest request) throws IOException {

		//格式化导出列的值
		List<Currency> currencyList = null ;	//币种号
		List<SysDict> accountFlagList = null ;	//内外账户标识
		List<SysDict> accountTypeList = null;  //外部账户类型
		List<RecordAccountRuleTransType> transTypeList = null ;		//交易类型
		try {
			currencyList = currencyService.findCurrency() ;
			accountFlagList = sysDictService.findSysDictGroup("sys_account_falg");	
			accountTypeList = sysDictService.findSysDictGroup("sys_account_type");  
			transTypeList = recordAccountRuleTransTypeService.findAllTransType();		//交易类型
		} catch (Exception e1) {
			log.error(e1.toString());
//			e1.printStackTrace();
		}
		ExportFormat exportFormat = new ExportFormat() ;
		
		  response.setContentType("application/vnd.ms-excel;charset=utf-8");
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		  String fileName = "记账流水"+sdf.format(new Date())+".xls" ;
		  String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		  response.setHeader("Content-Disposition","attachment;filename="+fileNameFormat);
		  response.addHeader("Pragma", "no-cache");
		  response.addHeader("Cache-Control", "no-cache");
		  List<CoreTransInfo> list = new ArrayList<CoreTransInfo>() ;
		  List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		  DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		  
		//从数据库中查询数据
		  try {
			list = reportService.exportRecordFlow(coreTransInfo, params) ;
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		  for(CoreTransInfo coreTransInfoQ:list){
			  Map<String,String> map = new HashMap<String,String>() ;
			  map.put("transDate", coreTransInfoQ.getTransDate()==null?"":df.format(coreTransInfoQ.getTransDate())) ;
			  map.put("serialNo", coreTransInfoQ.getSerialNo()) ;
			  map.put("accountType", exportFormat.formatSysDict(coreTransInfoQ.getAccountType(), accountTypeList));
			  map.put("journalNo", coreTransInfoQ.getJournalNo()) ;
			  map.put("childSerialNo", coreTransInfoQ.getChildSerialNo()) ;
			  map.put("transAmount", coreTransInfoQ.getTransAmount()==null?"":coreTransInfoQ.getTransAmount().toString()) ;
			  map.put("debitCreditSide", "debit".equals(coreTransInfoQ.getDebitCreditSide()) ?"借方":"credit".equals(coreTransInfoQ.getDebitCreditSide())?"贷方":"" ) ;
			  map.put("accountFlag", exportFormat.formatSysDict(coreTransInfoQ.getAccountFlag(),accountFlagList)) ;
			  map.put("userId", coreTransInfoQ.getUserId()) ;
			  map.put("reverseFlag", "NORMAL".equals(coreTransInfoQ.getReverseFlag()) ?"正常交易":"REVERSED".equals(coreTransInfoQ.getReverseFlag())?"冲销交易":"") ;
			  map.put("accountNo", coreTransInfoQ.getAccountNo()) ;
			  map.put("subjectNo", coreTransInfoQ.getSubjectNo()) ;
			  map.put("currency", exportFormat.formatCurrency(coreTransInfoQ.getCurrencyNo(), currencyList)) ;
			  map.put("orgNo", coreTransInfoQ.getOrgNo()) ;
			  map.put("summaryInfo", coreTransInfoQ.getSummaryInfo()) ;
			  map.put("transType", exportFormat.formatTransType(coreTransInfoQ.getTransType(), transTypeList));
			  log.info(map.toString());
			  data.add(map) ;
		  }

		  ListDataExcelExport export = new ListDataExcelExport();
		  String[] cols = new String[]{"transDate","serialNo","transType","accountType","journalNo","childSerialNo","transAmount","debitCreditSide","accountFlag","userId","reverseFlag",
				  "accountNo","subjectNo","currency","orgNo","summaryInfo"};
		  String[] colsName = new String[]{"交易日期","记账流水号","交易类型","外部用户类型","分录号","记账子流水号","交易金额","借贷方向","内/外标志","外部用户编号","冲销标志","账号","科目内部编号","币种号","支付机构号","摘要"};
		  export.export(cols, colsName, data, response.getOutputStream());
		
	}
	
	
	//跳转到 批处理管理  页面
	@RequestMapping(value = "/toBatches.do")
	public String toBatches(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
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
		
		SystemInfo systemInfo = systemInfoService.findSystemInfoByCurrentDate(cn.eeepay.framework.util.DateUtil.getCurrentDate());
		if (systemInfo != null) {
			switch(systemInfo.getStatus().toUpperCase()) {
			case "N":
				model.addAttribute("systemStatus", "日间运行状态");
				break;
			case "C":
				model.addAttribute("systemStatus", "日切运行状态");
				break;
			case "A":
				model.addAttribute("systemStatus", "追帐运行状态");
				break;
			case "S":
				model.addAttribute("systemStatus", "系统关闭");
				break;
			}
		}
		
		model.put("params", params);
		return "report/batches";
	}
	
	//批处理查询
	@RequestMapping(value="/findBatchesList.do")
	@ResponseBody
	public Page<Batches> findBatchesList(@RequestParam Map<String, String> params,
			@ModelAttribute("sort")Sort sort,
			@ModelAttribute("page")Page<Batches> page){
		try {
			String start = params.get("start");
			String end = params.get("end");
			if (StringUtils.isNotBlank(start)) {
				start += " 00:00:00";
			}
			if (StringUtils.isNotBlank(end)) {
				end += " 23:59:59";
			}
			batchesService.findBatchesList(start, end, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
//			e.printStackTrace();
		}
		System.out.println(page);
		
		return page;
	}
	
	//跳转到 批处理详情  页面
	@RequestMapping(value = "/toBatchesDetail.do")
	public String toBatchesDetail(ModelMap model, @RequestParam Map<String, String> params, @RequestParam Integer batchesId) throws Exception{
		
		List<SysDict> modeDicts = sysDictService.findSysDictGroup("batches_execute_mode");
		List<SysDict> statusDicts = sysDictService.findSysDictGroup("batches_execute_status");
		
		List<BatchesDetail> list = batchesDetailService.findByBatchesId(batchesId);
		
		model.addAttribute("batchesId", batchesId);
		model.addAttribute("modeDicts", modeDicts);
		model.addAttribute("statusDicts", statusDicts);
		model.addAttribute("batchesDetailList", list);
		model.put("params", params);
		return "report/batchesDetail";
	}
	
	//批处理详情 ajax表格 页面
	@RequestMapping(value = "/findBatchesDetailList.do")
	public String findBatchesDetailList(ModelMap model, @RequestParam Integer batchesId) throws Exception{
		List<BatchesDetail> list = batchesDetailService.findByBatchesId(batchesId);
		model.addAttribute("batchesDetailList", list);
		return "report/ajax_batches_detail";
	}
	
	//批处理人工设置为成功
	@RequestMapping(value="/manualSetSuccess.do")
	@ResponseBody
	public Map<String, Object> manualSetSuccess(@RequestParam Integer id){
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
			    .getAuthentication()
			    .getPrincipal();
		
		Map<String, Object> result = new HashMap<String, Object>();
		Integer status = 1;
		int re = batchesDetailService.updateStatusById(status, id);
		String logMsg = "";
		if (re > 0) {
			result.put("state", true);
			result.put("msg", "人工处理成功");
			log.info(result.toString());
			//添加logMsg
			logMsg = "<br/>"+DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date())+","+userInfo.getRealName()+"人工处理成功!";
		} else {
			result.put("state", false);
			result.put("msg", "人工处理失败");
			log.info(result.toString());
			logMsg = "<br/>"+DateUtil.getFormatDate("yyyy-MM-dd HH:mm:ss", new Date())+","+userInfo.getRealName()+"人工处理失败!";
		}
		batchesDetailService.updateExecuteLog(id, logMsg);
		return result;
	}
	
	//查看日志
	@RequestMapping(value="/getBatchesDetailLog.do")
	@ResponseBody
	public Map<String, Object> getBatchesDetailLog(@RequestParam(value="id", required=true) Integer id){
		Map<String, Object> result = new HashMap<String, Object>();
		BatchesDetail bd = batchesDetailService.getExecuteLog(id);
		if (bd != null && StringUtils.isNotBlank(bd.getExecuteResult())) {
			result.put("state", true);
			result.put("msg", bd.getExecuteResult());
			log.info(result.toString());
		} else {
			result.put("state", false);
			result.put("msg", "查询日志失败");
			log.info(result.toString());
		}
		
		return result;
	}
}
