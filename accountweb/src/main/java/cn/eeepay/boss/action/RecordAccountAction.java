package cn.eeepay.boss.action;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
import cn.eeepay.framework.enums.DebitCreditSide;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.RecordAccountRule;
import cn.eeepay.framework.model.bill.RecordAccountRuleTransType;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfig;
import cn.eeepay.framework.model.bill.RecordAccountRuleConfigList;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.RecordAccountRuleConfigService;
import cn.eeepay.framework.service.bill.RecordAccountRuleService;
import cn.eeepay.framework.service.bill.RecordAccountRuleTransTypeService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.nposp.SysDictNpospService;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.StringUtil;
import cn.eeepay.framework.util.UrlUtil;


/**
 * 记账处理 by zouruijin 
 * email rjzou@qq.com zrj@eeepay.cn 
 * 2016年5月18日16:49:01
 *
 */
@Controller
@RequestMapping(value = "/recordAccountAction")
public class RecordAccountAction {
	@Resource
	public SysDictService sysDictService;
	
	@Resource
	public SysDictNpospService sysDictNpospService;
	@Resource
	public RecordAccountRuleService recordAccountRuleService;
	@Resource
	public RecordAccountRuleConfigService recordAccountRuleConfigService;
	@Resource
	public RecordAccountRuleTransTypeService recordAccountRuleTransTypeService;
	@Resource
	public SubjectService subjectService;
	@Resource
	public CurrencyService currencyService;

	private static final Logger log = LoggerFactory.getLogger(RecordAccountAction.class);

	// 跳转到 新增记账规则 页面
	@PreAuthorize("hasAuthority('recordAccountRule:query')")
	@RequestMapping(value = "/toRecordAccountRule.do")
	public String toRecordAccountRule(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		List<SysDict> acqOrgList = sysDictService.findSysDictGroup("sys_acq_org");
		model.put("acqOrgList", acqOrgList);
		List<SysDict> accountTypeList = sysDictService.findSysDictGroup("sys_account_type"); // 外部账用户类型
		model.put("accountTypeList", accountTypeList);
		List<SysDict> accountFlagList = sysDictService.findSysDictGroup("sys_account_falg"); // 内部账户、外部账户标志
		model.put("accountFlagList", accountFlagList);
		List<Currency> currencyList = currencyService.findCurrency(); // 币种号
		model.put("currencyList", currencyList);
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from"); // 借贷标识
		model.put("balanceFromList", balanceFromList);

		return "recordAccount/recordAccountRuleAdd";
	}

	// 跳转到 记账规则查询 页面
	@PreAuthorize("hasAuthority('recordAccountRuleListQuery:query')")
	@RequestMapping(value = "/toRecordAccountRuleListQuery.do")
	public String toRecordAccountRuleListQuery(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {
		
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
		
		List<SysDict> settleBankList = sysDictService.findSysDictGroup("settle_transfer_bank");
		model.put("settleBankList", settleBankList);
		model.put("params", params);
		return "recordAccount/recordAccountRuleListQuery";
	}

	// 跳转到 记账规则查询 详情 页面
	@PreAuthorize("hasAuthority('recordAccountRuleListQuery:detail')")
	@RequestMapping(value = "/toRuleListQueryDetail.do")
	public String toRuleListQueryDetail(ModelMap model, @RequestParam Map<String, String> params) throws Exception {

		List<SysDict> accountTypeList = sysDictService.findSysDictGroup("sys_account_type"); // 外部账用户类型
		model.put("accountTypeList", accountTypeList);
		List<SysDict> accountFlagList = sysDictService.findSysDictGroup("sys_account_falg"); // 内部账户、外部账户标志
		model.put("accountFlagList", accountFlagList);
		List<Currency> currencyList = currencyService.findCurrency(); // 币种号
		model.put("currencyList", currencyList);
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from"); // 借贷标识
		model.put("balanceFromList", balanceFromList);

		String ruleNo = params.get("ruleNo");
		RecordAccountRule rule = new RecordAccountRule();
		List<Subject> subjectList = new ArrayList<Subject>();
		subjectList = subjectService.findSubjectList();

		try {
			rule = recordAccountRuleService.findRuleByRuleNo(ruleNo);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("rule", rule);
		model.put("subjectList", subjectList);
		model.put("params", params);
		return "recordAccount/ruleListQueryDetail";
	}

	// 跳转到 记账规则查询 编辑 页面
	@PreAuthorize("hasAuthority('recordAccountRule:update')")
	@RequestMapping(value = "/toRuleListQueryUpdate.do")
	public String toRuleListQueryUpdate(ModelMap model, @RequestParam Map<String, String> params) throws Exception {

		List<SysDict> accountTypeList = sysDictService.findSysDictGroup("sys_account_type"); // 外部账用户类型
		model.put("accountTypeList", accountTypeList);
		List<SysDict> accountFlagList = sysDictService.findSysDictGroup("sys_account_falg"); // 内部账户、外部账户标志
		model.put("accountFlagList", accountFlagList);
		List<Currency> currencyList = currencyService.findCurrency(); // 币种号
		model.put("currencyList", currencyList);
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from"); // 借贷标识
		model.put("balanceFromList", balanceFromList);

		String ruleNo = params.get("ruleNo");
		RecordAccountRule rule = new RecordAccountRule();
		List<Subject> subjectList = new ArrayList<Subject>();
		subjectList = subjectService.findSubjectList();
		List<RecordAccountRuleConfig> ruleConfigList = new ArrayList<RecordAccountRuleConfig>();

		try {
			rule = recordAccountRuleService.findRuleByRuleNo(ruleNo);
			ruleConfigList = recordAccountRuleConfigService.findRuleConfigListTow(ruleNo);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("rule", rule);
		model.put("subjectList", subjectList);
		model.put("ruleConfigList", ruleConfigList);
		model.put("params", params);
		return "recordAccount/ruleListQueryUpdate";
	}

	// 跳转到 新增 交易类型 页面
	@PreAuthorize("hasAuthority('transTypeAdd:query')")
	@RequestMapping(value = "/toTransTypeAdd.do")
	public String toTransTypeAdd(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");
		
		List<cn.eeepay.framework.model.nposp.SysDict> transGroupList = sysDictNpospService.findSysDictGroup("TRADE_GROUP");
		
		model.put("fromSystemList", fromSystemList);
		model.put("transGroupList", transGroupList);
		
		return "recordAccount/transTypeAdd";
	}

	// 跳转到 交易类型查询 页面
	@PreAuthorize("hasAuthority('transTypeListQuery:query')")
	@RequestMapping(value = "/toTransTypeListQuery.do")
	public String toTransTypeListQuery(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");
		
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
		
		model.put("fromSystemList", fromSystemList);
		model.put("params", params);
		return "recordAccount/transTypeListQuery";
	}

	// 跳转到 交易类型查询详情 页面
	@PreAuthorize("hasAuthority('transTypeListQuery:detail')")
	@RequestMapping(value = "/toTransTypeDetail.do")
	public String toTransTypeDetail(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		String id;
		id = params.get("id");

		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");
		model.put("fromSystemList", fromSystemList);
		List<cn.eeepay.framework.model.nposp.SysDict> transGroupList = sysDictNpospService.findSysDictGroup("TRADE_GROUP");
		model.put("transGroupList", transGroupList);
		RecordAccountRuleTransType transType = new RecordAccountRuleTransType();
		transType = recordAccountRuleTransTypeService.findTransTypeById(id);
		String[] transGroup = new String[]{};
		if(!StringUtil.isBlank(transType.getTransGroup())){
			if(transType.getTransGroup().contains(",")){
				transGroup = transType.getTransGroup().split(",");
			}else{
				transGroup = new String[]{transType.getTransGroup()};
			}
		}
		StringBuffer transGroupCn = new StringBuffer("");
		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		for (String transGroupDetail : transGroup) {
			transGroupCn.append(format.formatNpsopSysDict(transGroupDetail, transGroupList));
			transGroupCn.append(",");
		}
		//去掉最后一个逗号
		if(transGroupCn.toString().contains(",")){
			transType.setTransGroup(transGroupCn.substring(0, transGroupCn.length()-1).toString());
		}
		model.put("transType", transType);
		model.put("params", params);
		return "recordAccount/transTypeDetail";
	}

	// 跳转到 交易类型修改 页面
	@PreAuthorize("hasAuthority('transTypeListQuery:update')")
	@RequestMapping(value = "/toTransTypeUpdate.do")
	public String toTransTypeUpdate(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		List<SysDict> fromSystemList = sysDictService.findSysDictGroup("from_system");
		model.put("fromSystemList", fromSystemList);
		List<cn.eeepay.framework.model.nposp.SysDict> transGroupList = sysDictNpospService.findSysDictGroup("TRADE_GROUP");
		model.put("transGroupList", transGroupList);
		String id = params.get("id");
		RecordAccountRuleTransType transType = new RecordAccountRuleTransType();
		transType = recordAccountRuleTransTypeService.findTransTypeById(id);
		transType.setId(Integer.parseInt(id));
		model.put("transType", transType);
		model.put("params", params);
		return "recordAccount/transTypeUpdate";
	}

	/**
	 * 新增 记账规则以及记账规则配置
	 * 
	 * @param model
	 * @param params
	 * @param recordAccountRule
	 * @param recordAccountRuleConfig
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('recordAccountRule:insert')")
	@RequestMapping(value = "/recordAccountRuleAdd.do", method = RequestMethod.POST)
	@Logs(description="新增记账规则以及记账规则配置")
	@ResponseBody
	public Map<String, Object> recordAccountRuleAdd(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("recordAccountRule") RecordAccountRule recordAccountRule,
			@ModelAttribute("recordAccountRuleConfig") RecordAccountRuleConfigList recordAccountRuleConfig)
			throws Exception {

		Map<String, Object> msg = new HashMap<>();
		int i = 0;

		// 自动生成一个 记账规则号
		recordAccountRule.setRuleNo(System.currentTimeMillis() + "");// System.out.println(System.currentTimeMillis()+"");

		// i = ruleService.insertRecordAccountRule(recordAccountRule,
		// recordAccountRuleConfig) ;
		// 查询提交的表单数据 对账规则 是否已存在,存在直接跳出
		if (recordAccountRuleService.findRuleIsExist(recordAccountRule) != null) {//
			msg.put("state", false);
			msg.put("msg", "该条 对账规则 已存在！");
			log.info(msg.toString());
			return msg;
		}
		List<RecordAccountRuleConfig> ruleConfigList = recordAccountRuleConfig.getRecordAccountRuleConfig();
		List<RecordAccountRuleConfig> uRuleConfigList = null;
		String debitCreditSide = "";
		boolean dbpassed = false;
		boolean fpassed = true;
		if (!ruleConfigList.isEmpty()) {
			for (RecordAccountRuleConfig item : ruleConfigList) {
				debitCreditSide = item.getDebitCreditSide();
				uRuleConfigList = new ArrayList<RecordAccountRuleConfig>();
				uRuleConfigList.addAll(ruleConfigList);
				uRuleConfigList.remove(item);
				//有借必须有贷，解冻和冻结只能有一条记录
				switch (debitCreditSide) {
					case "debit":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo())) {
								if (item2.getDebitCreditSide().equals(DebitCreditSide.CREDIT.toString())) {
									dbpassed = true;
									break;
								}
							}
						}
						break;
					case "credit":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo())) {
								if (item2.getDebitCreditSide().equals(DebitCreditSide.DEBIT.toString())) {
									dbpassed = true;
									break;
								}
							}
						}
						break;
					case "freeze":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo()) ) {
								fpassed = false;
								break;
							}
						}
						break;
					case "unFreeze":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo()) ) {
								fpassed = false;
								break;
							}
						}
						break;
					default:
							break;
				}
				if (!fpassed) {
					msg.put("state", false);
					msg.put("msg", "分录号为"+item.getJournalNo()+"的记账规则借贷标识为解冻或冻结只能存在一条记录");
					log.info(msg.toString());
					return msg;
				} else if (!dbpassed) {
					msg.put("state", false);
					msg.put("msg", "分录号为"+item.getJournalNo()+"的记账规则借贷标识有借必须有贷");
					log.info(msg.toString());
					return msg;
				}
			}
		}
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		recordAccountRule.setCreator(userInfo.getUsername());
		recordAccountRule.setCreateTime(new Date());
		try {
			msg = recordAccountRuleService.insertRecordAccountRule(recordAccountRule, recordAccountRuleConfig);
			if ((int) msg.get("i") < 0) {
				msg.put("state", false);
				msg.put("msg", "新增失败！");
				log.info(msg.toString());
				return msg;
			}
		} catch (Exception e) {
			log.error("新增失败！" + e.getMessage(), e);
			msg.put("state", false);
			msg.put("msg", "新增失败！" + e.getMessage());
			log.error(msg.toString());
			return msg;
		}
		// System.out.println(msg) ;

		msg.put("state", true);
		msg.put("msg", "新增成功!");
		log.info(msg.toString());
		return msg;

	}

	/**
	 * 查询所有的 记账规则
	 * 
	 * @param recordAccountRule
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('recordAccountRuleListQuery:query')")
	@RequestMapping(value = "/findRecordAccountRuleList.do")
	@ResponseBody
	public Page<RecordAccountRule> findRecordAccountRuleList(
			@ModelAttribute("recordAccountRule") RecordAccountRule recordAccountRule, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<RecordAccountRule> page) {
		try {
			recordAccountRuleService.findRecordAccountRuleList(recordAccountRule, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return page;
	}

	/**
	 * 记账规则配置 全部查询
	 * 
	 * @param params
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('recordAccountRuleListQuery:query')")
	@RequestMapping(value = "/findRuleConfigList.do")
	@ResponseBody
	public Page<RecordAccountRuleConfig> findRuleConfigList(@RequestParam Map<String, String> params,
			@ModelAttribute("sort") Sort sort, @ModelAttribute("page") Page<RecordAccountRuleConfig> page) {
		String ruleNo = params.get("ruleNo");

		try {
			recordAccountRuleConfigService.findRuleConfigList(ruleNo, sort, page);
		} catch (Exception e) {
			log.error("异常:",e);
		}

		return page;
	}

	/**
	 * 查询所有的记账规则名字及编号（用于select2）
	 * 
	 * @param q
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/querySelectRule.do")
	@ResponseBody
	public List<Map<String, String>> querySelectRule(String q) throws Exception {
		System.out.println("q-->" + q);
		q = URLDecoder.decode(q, "UTF-8");
		log.info("querySelectRule:" + q);
		RecordAccountRule rule = new RecordAccountRule();
		rule.setRuleNo(q);
		List<RecordAccountRule> ruleList = null;
		List<Map<String, String>> maps = new ArrayList<>();
		try {
			ruleList = recordAccountRuleService.findSelectRule(rule);
		} catch (Exception e) {
			log.error("异常:",e);
		}
		Map<String, String> thenMap = null;
		for (RecordAccountRule ruleq : ruleList) {
			thenMap = new HashMap<String, String>();
			thenMap.put("id", ruleq.getRuleNo());
			thenMap.put("text", ruleq.getRuleName());
			maps.add(thenMap);
		}

		return maps;
	}

	/**
	 * 新增 交易类型
	 * 
	 * @param model
	 * @param params
	 * @param transType
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('transTypeAdd:insert')")
	@RequestMapping(value = "/transTypeAdd.do", method = RequestMethod.POST)
	@Logs(description="新增交易类型")
	@ResponseBody
	public Map<String, Object> transTypeAdd(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("transType") RecordAccountRuleTransType transType) throws Exception {

		Map<String, Object> msg = new HashMap<>();
		int i = 0;
		int ruleId;

		if (transType.getRecordAccountRule() == null) {
			msg.put("msg", "请选择一条记账规则！");
			log.info(msg.toString());
			return msg;
		}
		
//		if (StringUtil.isBlank(transType.getTransGroup())) {
//			msg.put("msg", "请选择一条交易分组！");
//			log.info(msg.toString());
//			return msg;
//		}

		ruleId = recordAccountRuleService.findRuleIdByNo(transType.getRecordAccountRule().getRuleNo());
		transType.setRuleId(ruleId);

		if (recordAccountRuleTransTypeService.findTransTypeIsExist(transType) != null) {
			msg.put("state", false);
			msg.put("msg", "该条交易类型数据已存在,新增交易类型失败！");
			log.info(msg.toString());
			return msg;
		}
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		transType.setCreator(userInfo.getUsername());
		transType.setCreateTime(new Date());
		try {
			i = recordAccountRuleService.insertTransType(transType);
			if (i > 0) {
				msg.put("state", true);
				msg.put("msg", "新增交易类型成功!");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			log.error("新增交易类型失败！", e);
			msg.put("state", false);
			msg.put("msg", "新增交易类型失败！");
			log.error(msg.toString());
		}
		// System.out.println(msg) ;

		return msg;

	}

	/**
	 * 查询所有的 交易类型
	 * 
	 * @param transType
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('transTypeAdd:query')")
	@RequestMapping(value = "/findTransTypeList.do")
	@ResponseBody
	public Page<RecordAccountRuleTransType> findTransTypeList(
			@ModelAttribute("transType") RecordAccountRuleTransType transType, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<RecordAccountRuleTransType> page) {

		try {
			List<cn.eeepay.framework.model.nposp.SysDict> transGroupList = sysDictNpospService.findSysDictGroup("TRADE_GROUP");
			List<RecordAccountRuleTransType> list = recordAccountRuleTransTypeService.findTransTypeList(transType, sort, page);
			for (RecordAccountRuleTransType recordAccountRuleTransType : list) {
				String[] transGroup = new String[]{};
				if(!StringUtil.isBlank(recordAccountRuleTransType.getTransGroup())){
					if(recordAccountRuleTransType.getTransGroup().contains(",")){
						transGroup = recordAccountRuleTransType.getTransGroup().split(",");
					}else{
						transGroup = new String[]{recordAccountRuleTransType.getTransGroup()};
					}
				}
				StringBuffer transGroupCn = new StringBuffer("");
				//用于对数据字典的数据进行格式化显示
				ExportFormat format = new ExportFormat() ;
				for (String transGroupDetail : transGroup) {
					transGroupCn.append(format.formatNpsopSysDict(transGroupDetail, transGroupList));
					transGroupCn.append(",");
				}
				//去掉最后一个逗号
				if(transGroupCn.toString().contains(",")){
					recordAccountRuleTransType.setTransGroup(transGroupCn.substring(0, transGroupCn.length()-1).toString());
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}

		return page;
	}

	/**
	 * 修改 交易类型
	 * 
	 * @param model
	 * @param params
	 * @param transType
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('transTypeAdd:update')")
	@RequestMapping(value = "/transTypeUpdate.do", method = RequestMethod.POST)
	@Logs(description="修改交易类型")
	@ResponseBody
	public Map<String, Object> transTypeUpdate(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("transType") RecordAccountRuleTransType transType) throws Exception {
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> msg = new HashMap<>();
		
		if (transType.getRecordAccountRule() == null) {
			msg.put("msg", "请选择一条记账规则！");
			log.info(msg.toString());
			return msg;
		}
//		if (StringUtil.isBlank(transType.getTransGroup())) {
//			msg.put("msg", "请选择一条交易分组！");
//			log.info(msg.toString());
//			return msg;
//		}
		
		// 通过提交过来的ruleNo得到ruleId
		int ruleId = recordAccountRuleService.findRuleIdByNo(transType.getRecordAccountRule().getRuleNo());
		RecordAccountRuleTransType transTypeQ = recordAccountRuleTransTypeService.findTransTypeById(transType.getId().toString());
		transTypeQ.setTransGroup(transType.getTransGroup());
		transTypeQ.setId(transType.getId());
		transTypeQ.setTransTypeCode(transType.getTransTypeCode());
		transTypeQ.setTransTypeName(transType.getTransTypeName());
		transTypeQ.setRuleId(ruleId);
		transTypeQ.setFromSystem(transType.getFromSystem());
		transTypeQ.setRemark(transType.getRemark());
		transTypeQ.setUpdator(userInfo.getUsername());
		transTypeQ.setUpdateTime(new Date());
		if (recordAccountRuleTransTypeService.updateTransType(transTypeQ) > 0) {
			msg.put("state", true);
			msg.put("msg", "修改成功！");
			log.info(msg.toString());
			return msg;
		}
		// System.out.println(msg) ;
		msg.put("state", false);
		msg.put("msg", "修改失败！");
		log.info(msg.toString());
		return msg;
	}

	/**
	 * 删除
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('transTypeAdd:delete')")
	@RequestMapping(value = "/transTypeDelete.do", method = RequestMethod.POST)
	@Logs(description="删除交易类型")
	@ResponseBody
	public Map<String, Object> transTypeDelete(ModelMap model, @RequestParam Map<String, String> params)
			throws Exception {

		Map<String, Object> msg = new HashMap<>();

		if (recordAccountRuleTransTypeService.deleteTransType(params.get("id")) > 0) {
			msg.put("state", true);
			msg.put("msg", "删除成功！");
			log.info(msg.toString());
			return msg;
		}
		// System.out.println(msg) ;
		msg.put("state", false);
		msg.put("msg", "删除失败！");
		log.info(msg.toString());
		return msg;

	}
	@PreAuthorize("hasAuthority('recordAccountRule:update')")
	@RequestMapping(value = "/recordAccountRuleUpdate.do", method = RequestMethod.POST)
	@Logs(description="修改记账规则")
	@ResponseBody
	public Map<String, Object> recordAccountRuleUpdate(ModelMap model, @RequestParam Map<String, String> params,
			@ModelAttribute("recordAccountRule") RecordAccountRule recordAccountRule,
			@ModelAttribute("recordAccountRuleConfig") RecordAccountRuleConfigList recordAccountRuleConfig)
			throws Exception {

		Map<String, Object> msg = new HashMap<>();
		List<RecordAccountRuleConfig> ruleConfigListParams = recordAccountRuleConfig.getRecordAccountRuleConfig();
		List<RecordAccountRuleConfig> ruleConfigList = new ArrayList<RecordAccountRuleConfig>();
		List<RecordAccountRuleConfig> ruleConfigListParamsAfter = new ArrayList<RecordAccountRuleConfig>();
		if(ruleConfigListParams != null && ruleConfigListParams.size() > 0){
        	for (int i = 0; i < ruleConfigListParams.size(); i++) {
        		RecordAccountRuleConfig rarc = new RecordAccountRuleConfig();
        		rarc = ruleConfigListParams.get(i);
        		if(StringUtils.isNotBlank(rarc.getJournalNo())){
        			ruleConfigListParamsAfter.add(ruleConfigListParams.get(i));
				}
			}
        	ruleConfigList.addAll(ruleConfigListParamsAfter);
        }
		//移除无效的
		if (ruleConfigList != null && ruleConfigList.size() > 0) {
			for (int i = 0; i < ruleConfigList.size(); i++) {
				if (StringUtils.isBlank(ruleConfigList.get(i).getJournalNo())
						|| "no".equals(ruleConfigList.get(i).getDebitCreditFlag())) {
					ruleConfigList.remove(i);
					i--;
				}
			}
		}
		List<RecordAccountRuleConfig> uRuleConfigList = null;
		String debitCreditSide = "";
		boolean dbpassed = false;
		boolean fpassed = true;
		if (!ruleConfigList.isEmpty()) {
			for (RecordAccountRuleConfig item : ruleConfigList) {
				debitCreditSide = item.getDebitCreditSide();
				uRuleConfigList = new ArrayList<RecordAccountRuleConfig>();
				uRuleConfigList.addAll(ruleConfigList);
				uRuleConfigList.remove(item);
				//有借必须有贷，解冻和冻结只能有一条记录
				switch (debitCreditSide) {
					case "debit":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo())) {
								if (item2.getDebitCreditSide().equals(DebitCreditSide.CREDIT.toString())) {
									dbpassed = true;
									break;
								}
							}
						}
						break;
					case "credit":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo())) {
								if (item2.getDebitCreditSide().equals(DebitCreditSide.DEBIT.toString())) {
									dbpassed = true;
									break;
								}
							}
						}
						break;
					case "freeze":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo()) ) {
								fpassed = false;
								break;
							}
						}
						break;
					case "unFreeze":
						for (RecordAccountRuleConfig item2 : uRuleConfigList) {
							if (item2.getJournalNo().equals(item.getJournalNo()) ) {
								fpassed = false;
								break;
							}
						}
						break;
					default:
							break;
				}
				
				if (!fpassed) {
					msg.put("state", false);
					msg.put("msg", "分录号为"+item.getJournalNo()+"的记账规则借贷标识为解冻或冻结只能存在一条记录");
					log.info(msg.toString());
					return msg;
				} else if (!dbpassed) {
					msg.put("state", false);
					msg.put("msg", "分录号为"+item.getJournalNo()+"的记账规则借贷标识有借必须有贷");
					log.info(msg.toString());
					return msg;
				}
			}
		}
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		recordAccountRule.setUpdator(userInfo.getUsername());
		recordAccountRule.setUpdateTime(new Date());
		try {
			if (recordAccountRuleService.updateRecordAccountRule(recordAccountRule, ruleConfigListParamsAfter)) {
				msg.put("state", true);
				msg.put("msg", "修改成功！");
				log.info(msg.toString());
				return msg;
			}
		} catch (Exception e) {
			msg.put("state", false);
			msg.put("msg", "修改失败！");
			log.error(msg.toString());
			log.error("异常:",e);
		}

		// System.out.println(msg) ;
		msg.put("state", false);
		msg.put("msg", "修改失败！");
		log.info(msg.toString());
		return msg;

	}

}
