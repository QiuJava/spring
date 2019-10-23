package cn.eeepay.boss.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.framework.db.pagination.Page;

@Controller
@RequestMapping(value = "/merchantInfo")
public class MerchantInfoAction {
	private static final Logger log = LoggerFactory.getLogger(MerchantInfoAction.class);
	// 商户信息
	@Resource
	private MerchantInfoService merchantInfoService;

	@Resource
	private BusinessProductDefineService businessProductDefineService;

	// 机具信息
	@Resource
	private TerminalInfoService terminalInfoService;

	// 进件项信息
	@Resource
	private AddRequireItemService addRequireItemService;

	// 代理商
	@Resource
	private AgentInfoService agentInfoService;

	@Resource
	private PosCardBinService posCardBinService;

	@Resource
	private PyIdentificationService pyIdentificationService;

	@Resource
	private PosCnapsService posCnapsService;
	@Resource
	private OpenPlatformService openPlatformService;

	@Autowired
	private SysDictService sysDictService;

	/*@RequestMapping("/testCJT")
	public void testCJT(){
		try {
			String url = "http://192.168.3.42:8666/core2/cjt/merToCjtMer";
			String signKey = "YLZF$CORE!666";
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put("merchantNo", "258121000031459");
			requestMap.put("sn", "B101224565400002");
			String signData = CommonUtil.sortASCIISign(requestMap, signKey);
			requestMap.put("signData", signData);

			String str = HttpUtils.sendPostRequest(url, requestMap);
			System.out.println("超级推进件处理结果：" + str);
		} catch (Exception e) {
			System.out.println("超级推机具处理异常"+ e);
		}
	}*/

	// 商户初始化
	@RequestMapping(value = "/selectAllInfo.do")
	@ResponseBody
	public Object selectAllInfo() throws Exception {
		List<MerchantInfo> listMer = null;
		Page<MerchantInfo> page = new Page<MerchantInfo>();
		try {
			listMer = merchantInfoService.selectByNameInfoByTermianl();
		} catch (Exception e) {
			log.error("商户初始化失败----", e);
		}
		return listMer;
	}

	// 经营范围下拉框
	@RequestMapping(value = "/selectSysName")
	@ResponseBody
	public Object selectSysName() throws Exception {
		List<SysDict> listDic = null;
		try {
			listDic = merchantInfoService.selectOneInfo();
		} catch (Exception e) {
			log.error("经营范围下拉框失败", e);
		}
		return listDic;
	}

	// 支行查询
	@RequestMapping(value = "/selectCnaps")
	@ResponseBody
	public Object selectCnaps(@RequestBody String param) throws Exception {
		Map<String, Object> mapJson = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			String cityName = json.getString("cityName");// 市
			String pris = json.getString("pris");// 省
			if (cityName == null || cityName.equals("")) {
				mapJson.put("bols", false);
				mapJson.put("msg", "参数有误");
				return mapJson;
			}
			if (pris == null || pris.equals("")) {
				mapJson.put("bols", false);
				mapJson.put("msg", "参数有误");
				return mapJson;
			}
			String backName = "%" + json.getString("backName") + "%";
			if (pris.equals("北京")) {
				List<PosCnaps> list = posCnapsService.query(backName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("上海")) {
				List<PosCnaps> list = posCnapsService.query(backName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("天津")) {
				List<PosCnaps> list = posCnapsService.query(backName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			if (pris.equals("重庆")) {
				List<PosCnaps> list = posCnapsService.query(backName, "%" + pris + "%");
				if (list.size() == 0) {
					mapJson.put("bols", false);
					mapJson.put("msg", "数据为空");
					return mapJson;
				}
				mapJson.put("bols", true);
				mapJson.put("list", list);
				return mapJson;
			}
			// tgh316处理市问题
			if (cityName.endsWith("州")) {
				cityName = cityName.substring(0, cityName.length() - 1);
			}
			String city = cityName.contains("市") ? "%" + cityName.substring(0, cityName.indexOf("市")) + "%"
					: "%" + cityName + "%";

			List<PosCnaps> list = posCnapsService.query(backName, city);
			if (list.size() == 0) {
				mapJson.put("bols", false);
				mapJson.put("msg", "数据为空");
			} else {
				mapJson.put("bols", true);
				mapJson.put("list", list);
			}
		} catch (Exception e) {
			log.error("支行查询报错", e);
			mapJson.put("bols", false);
			mapJson.put("msg", "支行查询报错");
		}
		return mapJson;
	}

	// 银行查询
	@RequestMapping(value = "/getBackName")
	@ResponseBody
	public Object getBackName(@RequestBody String param) throws Exception {
		Map<String, Object> mapJson = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			String accountNo = json.getString("accountNo");
			List<PosCardBin> cards = posCardBinService.queryAllInfo(accountNo);
//			PosCardBin cards=posCardBinService.queryInfo(accountNo);
			if (cards.size() != 0) {
				mapJson.put("lists", cards);
				mapJson.put("bols", true);
			} else {
				mapJson.put("msg", "没有查到对应的银行，请检查开户账号是否正确");
				mapJson.put("bols", false);
			}
		} catch (Exception e) {
			log.error("支行查询报错", e);
			mapJson.put("bols", false);
			mapJson.put("msg", "支行查询报错");
		}
		return mapJson;
	}

	// 查询代理商业务产品
	@RequestMapping(value = "/selectBpdInfoByAgentNo")
	@ResponseBody
	public Object selectBpdInfoByAgentNo(@RequestParam("ids") String param) throws Exception {
		List<BusinessProductDefine> listDic = null;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String merType = JSON.parseObject(param, String.class);
			listDic = agentInfoService.queryMerProduct(principal.getUserEntityInfo().getEntityId(), merType);
		} catch (Exception e) {
			log.error("查询代理商业务产品失败", e);
		}
		return listDic;
	}

	// 查询代理商所拥有的指费率与扣率
	@RequestMapping(value = "/queryAgentQuotaRate")
	@ResponseBody
	public Object queryAgentQuotaRate(@RequestParam("bpId") String bpIds) throws Exception {
		Map<String, Object> maps = new HashMap<String, Object>();
		List<ServiceInfo> listService = null;
		List<ServiceQuota> listmsq = null;
		List<ServiceRate> listmr = null;
		List<AddRequireItem> listItem = null;
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String agentNo = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId()).getOneLevelId();
		String bpId = JSON.parseObject(bpIds, String.class);
		try {
			// 判断当前代理商是否是一级
			String oneAgentNo = agentNo;
			AgentInfo ais = agentInfoService.selectByagentNo(agentNo);
			if (ais.getParentId().equals("0")) {
				oneAgentNo = ais.getAgentNo();
			}
			listService = merchantInfoService.getServiceInfoByParams(agentNo, bpId); // 查询服务
			if (listService.size() < 1) {
				maps.put("msg", Constants.MER_ADD_NOSERVICE);
				maps.put("bols", false);
				return maps;
			}
//			for (ServiceInfo ser : listService) {
//				if(ser.getFixedRate()==0){//不固定取当前代理商的费率
//					listmr = merchantInfoService.getServiceRatedByParams(agentNo, bpId);
//				}else{//固定取一级代理商的费率
			listmr = merchantInfoService.getServiceRatedByParams(oneAgentNo, bpId);
//				}
//				if(ser.getFixedQuota()==0){//不固定取当前代理商的限额
//					listmsq = merchantInfoService.getServiceQuotaByParams(agentNo, bpId);
//				}else{
			listmsq = merchantInfoService.getServiceQuotaByParams(oneAgentNo, bpId);
//				}
//			}
			listItem = addRequireItemService.getRequireItemByParams(agentNo, bpId);
			if (listmsq.size() < 1) {
				maps.put("msg", Constants.MER_ADD_NOQUOTA);
				maps.put("bols", false);
				return maps;
			} else if (listmr.size() < 1) {
				maps.put("msg", Constants.MER_ADD_NORATE);
				maps.put("bols", false);
				return maps;
			} else {
				maps.put("bols", true);
			}
		} catch (Exception e) {
			log.error("查询代理商所代理业务产品异常！");
			maps.put("bols", false);
			maps.put("msg", "查询代理商所代理业务产品异常");
		}
		maps.put("listItem", listItem);
		maps.put("listmr", listmr);
		maps.put("listmsq", listmsq);
		maps.put("listService", listService);
		return maps;
	}

	/**
	 * 1 个体收单商户 2 企业收单商户
	 * 
	 * @param merchantType
	 * @return
	 */
	@RequestMapping(value = "/getIntoList")
	@ResponseBody
	public List<AddRequireItem> getIntoList(String merchantType) {
		List<AddRequireItem> listItem = null;
		try {
			listItem = addRequireItemService.getRequireItemByType(merchantType);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("收单商户进件项查询异常");
		}
		return listItem;
	}

	@SystemLog(description = "收单商户进件")
	@RequestMapping("/saveAcqMerchantInfo")
	@ResponseBody
	public Result saveAcqMerchantInfo(@RequestBody JSONObject json) {
		Result result = new Result();
		try {
			List<String> urlList = json.getObject("content", List.class);
			AcqMerchantInfo info = json.getObject("info", AcqMerchantInfo.class);
			if (info.getId()!=null) {
				merchantInfoService.updateAcqInfo(info,urlList);
			}else {
				merchantInfoService.saveAcqInfo(info, urlList);
			}
			result.setStatus(true);
			result.setMsg("提交收单商户进件成功");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("收单商户进件异常");
			result.setStatus(false);
			result.setMsg("提交收单商户进件失败");
		}
		return result;
	}

	@RequestMapping(value = "/queryMerType")
	@ResponseBody
	public List<SysDict> queryMerType() throws Exception {
		List<SysDict> merTypeList = null;
		try {
			merTypeList = merchantInfoService.getMerTypeMcc("sys_mcc", "-1");
		} catch (Exception e) {
			log.error("查询商户类型失败！");
		}
		return merTypeList;
	}

	@RequestMapping(value = "/queryMerMcc")
	@ResponseBody
	public List<SysDict> queryMerMcc(@RequestParam("ids") String ids) throws Exception {
		List<SysDict> merMccList = null;
		try {
			merMccList = merchantInfoService.getMerTypeMcc("sys_mcc", ids);
		} catch (Exception e) {
			log.error("查询商户MCC失败！");
		}
		return merMccList;
	}

	/**
	 * 校验是否可以注册进件
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkMerchantInfo")
	@ResponseBody
	public Object checkMerchantInfo(@RequestParam("infos") String param) throws Exception {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try {
			JSONObject json = JSON.parseObject(param);
			MerchantInfo info = json.getObject("info", MerchantInfo.class);
			List<TerminalInfo> terminalInfos = JSON.parseArray(json.getJSONArray("shuzu").toJSONString(),
					TerminalInfo.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			String mobilePhone = info.getMobilephone();
			String bpId = json.getString("bpId");
			
			
			if (StringUtils.isBlank(mobilePhone)) {
				throw new AgentWebException("请输入手机号");
			}
			if (StringUtils.isNotBlank(merchantInfoService.findBlacklist(info.getIdCardNo(), "2", "2"))) {
				throw new AgentWebException("该身份证号录入了黑名单不能进件");
			}
			if (StringUtils.isNotBlank(merchantInfoService.findBlacklist(json.getString("card"), "3", "2"))) {
				throw new AgentWebException("该银行卡号录入了黑名单不能进件");
			}
			if (merchantInfoService.countMerchantPhone(mobilePhone, bpId) > 0) {
				throw new AgentWebException("手机号码已注册");
			}
			
			if(StringUtils.isNotBlank(info.getMerchantName())){
				//校验敏感词
				int i =merchantInfoService.getByMerchantName(info.getMerchantName());
				if(i>0){
					throw new AgentWebException("商户名称存在非法词汇，请重新输入");
				}
			}else{
				throw new AgentWebException("商户名称不能为空");
			}
			
			
			
			for (TerminalInfo terminal : terminalInfos) {
				String sn = terminal.getSn();
				if (StringUtils.isBlank(sn)) {
					throw new AgentWebException("请输入机具号");
				}
				TerminalInfo terInfo = terminalInfoService.querySn(sn);
				if (terInfo == null) {
					throw new AgentWebException("机具不存在");
				}

				if (!checkBpIdIsRight4Terminal(bpId, terInfo)) {
					throw new AgentWebException("您的机具不支持所选择的业务产品");
				}

				if (terminalInfoService.checkAgentSn(loginAgentNo, sn) == null) {
					throw new AgentWebException("对不起,机具未分配给您");
				}

				if (terminalInfoService.checkSn(loginAgentNo, sn, "2") != null) {
					throw new AgentWebException("机具已被使用");
				}

				if (terminalInfoService.checkSn(loginAgentNo, sn, "1") == null) {
					throw new AgentWebException("机具不存在");
				}
			}
			jsonMap.put("bols", true);
		} catch (AgentWebException e) {
			log.error("校验是否可以注册进件报错！", e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", e.getMessage());
		} catch (Exception e) {
			log.error("校验是否可以注册进件报错！", e);
			jsonMap.put("bols", false);
			jsonMap.put("msg", "校验是否可以注册进件报错");
		}
		jsonMap.put("bols", true);
		return jsonMap;
	}

	/**
	 * 检查业务产品是否符合该机具
	 * 
	 * @param bpId    业务产品
	 * @param terInfo 机具信息
	 * @return 是否符合
	 */
	private boolean checkBpIdIsRight4Terminal(String bpId, TerminalInfo terInfo) {
		String hpId = terInfo.getType();
		List<BusinessProductHardware> hardWareList = merchantInfoService.queryHardWare(bpId);
		for (BusinessProductHardware map : hardWareList) {
			String id = map.getHpId();
			if (StringUtils.equals("0", id) || StringUtils.equals(hpId, id)) {
				return true;
			}
		}
		return false;
	}

	@SystemLog(description = "商户进件")
	@RequestMapping(value = "/insertMerchantInfo")
	@ResponseBody
	public Object insertMerchantInfo(@RequestBody JSONObject json) throws Exception {
		Map<String, Object> jsonMap1 = new HashMap<String, Object>();
		try {
			MerchantInfo merInfo = json.getObject("infos", MerchantInfo.class);
			// 根据业务产品查询到teamId tgh412
			String bpId = json.getJSONObject("mbp").getString("bpId");
			String teamId = businessProductDefineService.selectTeamIdByBpId(bpId);
//				if(merchantInfoService.queryPhoneMerInfo(merInfo.getMobilephone(),merInfo.getTeamId()).size()>0){

			if (merInfo == null) {
				jsonMap1.put("msg", "商户信息填写不完整.");
				jsonMap1.put("bols", false);
				return jsonMap1;
			}
			if (merchantInfoService.countMerchantByIdCardAndTeamId(merInfo.getIdCardNo(), teamId) >= 2) {
				jsonMap1.put("msg", "该身份证已经进件过2次,不能重复进件.");
				jsonMap1.put("bols", false);
				return jsonMap1;
			}
			if (merchantInfoService.queryPhoneMerInfo(merInfo.getMobilephone(), teamId).size() > 0) {
				jsonMap1.put("msg", "商户已存在");
				jsonMap1.put("bols", false);
			} else {
				jsonMap1 = merchantInfoService.insertMerchantInfo(json);
			}
			// 如果为超级推机具调用高伟接口
			if((Boolean) jsonMap1.get("superPush")){
				merchantInfoService.merToCjtMer((String)jsonMap1.get("sn"), (String)jsonMap1.get("merchantNo"));
			}
		} catch (Exception ex) {
			jsonMap1.put("bols", false);
			String str = ex.getMessage();
			if (ex.getMessage() == null) {
				jsonMap1.put("msg", "商户进件失败");
				return jsonMap1;
			}
			if (str.contains("\r\n") || str.contains("\n")) {
				jsonMap1.put("msg", "商户进件失败");
			} else {
				jsonMap1.put("msg", str);
			}
			log.error("================>商户进件失败", ex);

		}
		return jsonMap1;
	}

	// 经营范围二级下拉框
	@RequestMapping(value = "/selectTwoSysName")
	@ResponseBody
	public Object selectTwoSysName(@RequestParam("key") String param) throws Exception {
		List<SysDict> listDic = null;
		try {
			String key = JSON.parseObject(param, String.class);
			listDic = merchantInfoService.selectTwoInfoByParentId(key);
		} catch (Exception e) {
			log.error("经营范围二级下拉框失败----", e);
		}
		return listDic;
	}

	/**
	 * 实名认证
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/checkBankNameIDCard")
	@ResponseBody
	public Object checkBankNameIDCard(@RequestBody String param) throws Exception {
		Map<String, String> maps = new HashMap<String, String>();
		Map<String, String> params = new HashMap<String, String>();
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			JSONObject json = JSON.parseObject(param);
			String accountNo = (String) json.get("accountNo");
			String name = (String) json.get("name");
			String card = (String) json.get("card");
			String cnapsNo = (String) json.get("cnapsNo");
			PyIdentification pyp = new PyIdentification();
			pyp.setCreatePerson(principal.getRealName());
			pyp.setBySystem(1);
			pyp.setAccountNo(accountNo);
			pyp.setIdCard(card);
			pyp.setIdentName(name);
			if (StringUtils.isEmpty(accountNo) || StringUtils.isEmpty(name) || StringUtils.isEmpty(card)) {
				maps.put("msg", "必要数据为空，请检查数据");
				maps.put("bols", "f");
			} else {
				params.put("accountNo", accountNo);
				params.put("name", name);
				params.put("card", card);
				params.put("cnapsNo", cnapsNo);
				PyIdentification ppp = pyIdentificationService.queryByCheckInfo(name, card, accountNo);
				if (ppp == null) {// 去走检查
					maps = openPlatformService.doAuthen(accountNo, name, card, null);
					String errCode = maps.get("errCode");
					String errMsg_ = maps.get("errMsg");
					boolean flag = "00".equalsIgnoreCase(errCode);
//					log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new Object[]
//							{flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
					// 如果身份验证失败，刚不再自动审核，按旧有的注册流程走。

					pyp.setErrorMsg(errMsg_);
					if (!flag) {
						log.info("身份证验证失败");
						maps.put("msg", "开户名、身份证、银行卡号不匹配");
						maps.put("bols", "f");
					} else {
						maps.put("msg", "验证通过");
						maps.put("bols", "t");
					}
				} else {// 查看是否通过
					if (ppp.getIdentiStatus() == 1) {
						maps.put("msg", "验证通过");
						maps.put("bols", "t");
					} else {// 去走检查
						maps = openPlatformService.doAuthen(accountNo, name, card, null);
						String errCode = maps.get("errCode");
						String errMsg_ = maps.get("errMsg");
						boolean flag = "00".equalsIgnoreCase(errCode);
//						log.info("身份证验证结果：是否成功:{};开户名:{};银行卡号:{};身份证:{};验证结果:{};错误信息:{};异常信息:{};",new Object[]
//								{flag,account_name,account_no,id_card_no,errCode,errMsg_,exceptionMsg});
						// 如果身份验证失败，刚不再自动审核，按旧有的注册流程走。

						pyp.setErrorMsg(errMsg_);
						if (!flag) {
							log.info("身份证验证失败");
							maps.put("msg", "开户名、身份证、银行卡号不匹配");
							maps.put("bols", "f");
						} else {
							maps.put("msg", "验证通过");
							maps.put("bols", "t");
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("实名认证报错", e);
			maps.put("msg", "实名认证报错");
			maps.put("bols", "f");
		}
		return maps;
	}

//	public Map<String, String> checkBank_Name_IDCard(Map<String, String> params) {
//		log.info("验证身份证、开户名、银行卡：" + params);
//		String ip ="http://www.yfbpay.cn/boss/api/checkMain";
//		//logger.info("数据库中配置的身份证、开户名、银行卡验证接口地址："+ip);
//		ip = (ip==null ? "http://www.yfbpay.cn/boss/api/checkMain" : ip);
//		StringBuffer url = new StringBuffer(ip);
//		url.append("?");
//		String responseBody = null;
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("errCode", "faild");
//		map.put("errMsg", "开户名+账号+证件号码,校验失败");
//		map.put("exceptionMsg", "--");
//		try {
//			//(1:验证账号+户名  2：验证账号+户名+证件号)
//			String verifyType = "2";
//			verifyType = (StringUtils.isEmpty(verifyType)) ? "2" : verifyType;
//			url.append("verifyType=").append(verifyType);
//
//			//标识为AGENT 代理商系统访问接口
//			String channel = params.get("channel");
//			channel = (StringUtils.isEmpty(channel)) ? "AGENT" : channel;
//			url.append("&channel=").append(channel);
//
//			//身份证号码
//			String identityId = params.get("card");
//			url.append("&identityId=").append(identityId);
//
//			//银行卡号
//			String accNo = params.get("accountNo");
//			url.append("&accNo=").append(URLEncoder.encode(accNo, "UTF-8"));
//
//			//开户名称
//			String accName = params.get("name");
//			url.append("&accName=").append(URLEncoder.encode(accName, "UTF-8"));
//
//			//清算联行号
//			String cnapsNo = params.get("cnapsNo");
//			if(StringUtils.isEmpty(cnapsNo)){ //如果没有传入清算联行号，则从 CardBin 中取
//				try {
//					String bankNoTemp=posCardBinService.queryBankNo(accNo);
//					if(bankNoTemp.equals("0")||bankNoTemp.equals("1")){
//						cnapsNo = null;
//					}else{
//						cnapsNo = bankNoTemp;
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//			url.append("&bankNo=").append(cnapsNo);
//			String finalUrl = url.toString();
//			log.info("验证身份证、开户名、银行卡，最终URL："+finalUrl);
////			HttpClient client = new HttpClient();
////
////			//设置连接超时时间
////			client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
////			//设置读取超时时间
////			client.getHttpConnectionManager().getParams().setSoTimeout(20000);
////
////			// 使用 GET 方法 ，如果服务器需要通过 HTTPS 连接，那只需要将下面 URL 中的 http 换成 https
////			HttpMethod method = new GetMethod(finalUrl);
////			// 使用POST方法
////			// HttpMethod method = new PostMethod(finalUrl);
////			client.executeMethod(method);
////			String statusLine = method.getStatusLine().toString();
//
//			responseBody = ClientInterface.postRequest(finalUrl);
//			String errCode = responseBody.substring(responseBody.indexOf("<errCode>")+9, responseBody.indexOf("</errCode>"));
//			String errMsg = responseBody.substring(responseBody.indexOf("<errMsg>")+8, responseBody.indexOf("</errMsg>"));
//			map.put("errCode", errCode);
//			map.put("errMsg", errMsg);
//			// response.sendRedirect(url.toString());
////	    } catch (ConnectTimeoutException cte) {
////	    	log.info("验证身份证、开户名、银行卡：连接超时<ConnectTimeoutException>");
////	        map.put("exceptionMsg", "连接超时");
////	        cte.printStackTrace();
////	    } catch (SocketTimeoutException ste) {
////	    	log.info("验证身份证、开户名、银行卡：读取超时<SocketTimeoutException>");
////	        map.put("exceptionMsg", "读取超时");
////	        ste.printStackTrace();
//		} catch (IOException e) {
//			log.info("验证身份证、开户名、银行卡：其他异常<IOException>");
//			e.printStackTrace();
//		}
//		return map;
//	}

	public static class items {
		private String accountName;
		private String accountType;
		private String accountHangName;
		private String accountNo;
		private String unionAccountNo;
		private String accountAddress;

		public String getAccountName() {
			return accountName;
		}

		public void setAccountName(String accountName) {
			this.accountName = accountName;
		}

		public String getAccountType() {
			return accountType;
		}

		public void setAccountType(String accountType) {
			this.accountType = accountType;
		}

		public String getAccountHangName() {
			return accountHangName;
		}

		public void setAccountHangName(String accountHangName) {
			this.accountHangName = accountHangName;
		}

		public String getAccountNo() {
			return accountNo;
		}

		public void setAccountNo(String accountNo) {
			this.accountNo = accountNo;
		}

		public String getUnionAccountNo() {
			return unionAccountNo;
		}

		public void setUnionAccountNo(String unionAccountNo) {
			this.unionAccountNo = unionAccountNo;
		}

		public String getAccountAddress() {
			return accountAddress;
		}

		public void setAccountAddress(String accountAddress) {
			this.accountAddress = accountAddress;
		}

	}

	@RequestMapping("/merTeams")
	@ResponseBody
	public List<Map<String, Object>> merTeams(String agentNo) {
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!org.springframework.util.StringUtils.hasLength(agentNo)) {
			agentNo = userInfo.getUserEntityInfo().getEntityId();
		}
		try {
			return merchantInfoService.getMerTeamsByAgentNo(agentNo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@RequestMapping("/intos")
	@ResponseBody
	public List<SysDict> intos() {
		String sysKey = "ACQ_MER_INTO_SOURCE";
		try {
			return sysDictService.findSysDictGroup(sysKey); 
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
	
	@RequestMapping("/acqMerInfoList")
	@ResponseBody
	public Page<AcqMerchantInfo> acqMerInfoList(@RequestBody AcqMerQo qo, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "10") int pageSize) {
		Page<AcqMerchantInfo> page = new Page<>(pageNo, pageSize);
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		qo.setAgentNo(userInfo.getUserEntityInfo().getEntityId());
		try {
			merchantInfoService.page(page,qo);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return page;
	}
	
	
	@RequestMapping("/acqMerInfoDetail")
	@ResponseBody
	public Result acqMerInfoDetail(Long id) {
		Result result = new Result();
		try {
			AcqMerchantInfo info = merchantInfoService.getAcqMerInfoDetail(id);
			result.setStatus(true);
			result.setData(info);
			result.setMsg("查询成功");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			result.setStatus(false);
			result.setMsg("系统繁忙，请稍后再试");
		}
		return result;
	}
	 
}
