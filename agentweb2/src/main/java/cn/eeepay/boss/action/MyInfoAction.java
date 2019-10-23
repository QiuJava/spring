package cn.eeepay.boss.action;

import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.encryptor.rsa.RSAUtils;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.exception.RequestLimitException;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetailQo;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CheckNum;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetail;
import cn.eeepay.framework.model.HappyBackNotFullDeductDetailList;
import cn.eeepay.framework.model.ProfitDaySettleDetailBean;
import cn.eeepay.framework.model.ProfitDaySettleDetailBean.DataList;
import cn.eeepay.framework.model.ProfitDaySettleDetailParamBean;
import cn.eeepay.framework.model.RedEnvelopesDetails;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.poi.AccountTranInfoCreateRow;
import cn.eeepay.framework.poi.SharePreDayCreateRow;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.HappyBackNotFullDeductService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.service.CreditCardManagerService;
import cn.eeepay.framework.service.SuperPushService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.service.impl.SuperBankWithdrawCheck;
import cn.eeepay.framework.service.impl.redemActive.RedemActiveWithdrawCheck;
import cn.eeepay.framework.service.impl.redemActive.RedemWithdrawCheck;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.CodeUtil;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.CreateRow;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.ExcelUtils;
import cn.eeepay.framework.util.HttpUtils;
import cn.eeepay.framework.util.IpRequestLimitUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.MapTypeAdapter;
import cn.eeepay.framework.util.Result;
import cn.eeepay.framework.util.Sms;

@RestController
@RequestMapping(value = "/myInfo")
public class MyInfoAction extends BaseAction {

	private static final Logger log = LoggerFactory.getLogger(MyInfoAction.class);
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Resource
	private SuperBankWithdrawCheck superBankWithdrawCheck;
	@Resource
	private AgentInfoService agentInfoService;
	@Resource
	private ActivityDetailService activityDetailServiceNoTran;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private SuperPushService superPushService;
	@Resource
	private SharePreDayCreateRow sharePreDayCreateRow;
	@Resource
    private AccountTranInfoCreateRow accountTranInfoCreateRow;
	@Resource
	private CreditCardManagerService creditCardManagerService;
	@Resource
	private RedemActiveWithdrawCheck redemActiveWithdrawCheck;
	@Resource
	private RedemWithdrawCheck redemWithdrawCheck;
	@Resource
	private IpRequestLimitUtils ipRequestLimitUtils;
	@Resource
	private AccessService accessService;
	@Autowired
	private HappyBackNotFullDeductService happyBackNotFullDeductService;
	@Resource
	private RedisService redisService;
	@Resource
	private PosCardBinService posCardBinService;

	@RequestMapping("/info.do")
	public Object info() {
		JSONObject ret = new JSONObject();
		ret.put("success", true);
		ret.put("agent", agentInfoService.selectByPrincipal());
		return ret;
	}

	@RequestMapping(value = "/queryMyInfo")
	@ResponseBody
	public Map<String,Object> queryMyInfo(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		try {
//			 final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			JSONObject json=JSONObject.parseObject(param);
//			UserEntityInfo entity=principal.getUserEntityInfo();
//			String agentNo = entity.getEntityId();
			Integer teamId=json.getInteger("teamId");
			String agentNo = json.getString("agentNo");
			if (!accessService.canAccessTheAgent(agentNo, true)) {
				map.put("status", false);
				map.put("msg", "无权操作.");
				return map;
			}
			String oneAgentNo = "";
			if(StringUtils.isNotBlank(agentNo)){
				oneAgentNo = agentInfoService.getOneAgentNo(agentNo);
			}
			if(teamId!=null){
				map =agentInfoService.queryMyInfo(agentNo,oneAgentNo,teamId);
				map.put("status", true);
			}else{
				map.put("status", true);
				map.put("msg", "没有查询到代理商业务产品！");
			}
		} catch (Exception e) {
			log.error("代理商关联查询业务产品失败!",e);
			map.put("status", false);
			map.put("msg", "代理商关联查询业务产品失败！");
		}
		return map;
	}
	
	@RequestMapping("/account.do")
	public ResponseBean account() throws Exception{
		final Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> resultMap;
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			AgentInfo agent = agentInfoService.selectByagentNo(entityId);
			Integer isOpen = agentInfoService.selectIsOpen(entityId);
			agent.setIsOpen(isOpen);
			if (principalObj instanceof UserLoginInfo) {
				resultMap = agentInfoService.getMyAccount(((UserLoginInfo) principalObj).getUserEntityInfo().getEntityId());
			} else {
				return new ResponseBean("请登录");
			}
			
			BigDecimal retainAmount = new BigDecimal("0");
			String happyTixianSwitch = "0";
			String defaultStatus = "0";
			if (agent.getAgentLevel() == 1) {//留存金额只针对一级代理商
				happyTixianSwitch = sysDictService.SelectServiceId("HAPPY_TIXIAN_SWITCH");//欢乐返提现开关
				Map<String,Object> defaultMap = agentInfoService.selectDefaultStatus();
				defaultStatus = defaultMap.get("status").toString();//默认总开关的状态
				if ("1".equals(defaultStatus)) {
					Map<String,Object> map = agentInfoService.selectAccountStatus(entityId);
					if (map != null) {
						/**
						 * 若打开，留存金额值分以下情况展示：
						①　若可用余额大于设置的留存金额，则再此展示留存金额值为设置的留存金额，实际可提现金额=可用余额-留存金额
						若可用余额小于等于设置的留存金额，则再此展示留存金额值为可用余额的值，实际可提现金额=0	
						 */
						if ("1".equals(map.get("status").toString())) {
							retainAmount = new BigDecimal(map.get("retain_amount").toString());//留存金额
							if (!"11".equals(agent.getAgentType())) {
								String happyAccount = resultMap.get("happyAccount").toString();
								Gson gson = new Gson();
								@SuppressWarnings("unchecked")
								Map<String, Object> jsonMap = gson.fromJson(happyAccount, map.getClass());
								BigDecimal avaliBalance = new BigDecimal(jsonMap.get("avaliBalance").toString());//可用余额
								if (avaliBalance.compareTo(retainAmount) <= 0) {
									retainAmount = avaliBalance;
								}
							}
						}else{
							retainAmount = new BigDecimal("0");//开关为0,没有留存金额,不受控制
							defaultStatus = "0";//也不显
						}
					}else{
						retainAmount = new BigDecimal(defaultMap.get("retain_amount").toString());//留存金额,没查到就取默认的
					}
				}
			}
			resultMap.put("happyTixianSwitch", happyTixianSwitch);
			resultMap.put("defaultStatus", defaultStatus);
			resultMap.put("retainAmount", retainAmount);
			resultMap.put("cashBackSwitch", agent.getCashBackSwitch());
			
			resultMap.put("agentInfo", agent);
			resultMap.put("profitSwitch", agent.getProfitSwitch());
			String todaySuperPushAmount = superPushService.getTodaySuperPushAmount(principal.getUserEntityInfo().getEntityId());
			resultMap.put("todaySuperPushAmount", StringUtils.isBlank(todaySuperPushAmount) ? "0.00" : todaySuperPushAmount);
			String todayCreditAmount = creditCardManagerService.getTodayShareAmount(principal.getUserEntityInfo().getEntityId());
			resultMap.put("todayCreditAmount", StringUtils.isBlank(todayCreditAmount) ? "0.00" : todayCreditAmount);
			resultMap.put("status", true);
			return new ResponseBean(resultMap);
		} catch (Exception e) {
			log.error("服务异常!",e);
			return new ResponseBean("服务异常");
		}
	}
	
	//获取商户账户交易记录
	@RequestMapping(value="/getAccountTranInfo")
	@ResponseBody
	public Object getAccountTranInfo(@RequestParam("info") String param) throws Exception{
		Map<String, Object> maps=new HashMap<String, Object>();
		try{
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			AgentInfo agent = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//			if(agent.getHasAccount()==0){
//				maps.put("bols", false);
//				maps.put("msg", "代理商未开户");
//				return maps;
//			}
			JSONObject json=JSON.parseObject(param);
			Date sTime=json.getDate("sTime");
			Date eTime=json.getDate("eTime");
			String ioType=json.getString("ioType");
			Integer pageNo=json.getInteger("pageNo");
			Integer pageSize=json.getInteger("pageSize");
			String subjectNo = json.getString("subjectNo");//账户类型
			String transType = json.getString("transType");//交易类型
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			String st="";
			String et="";
			if(sTime!=null && !sTime.equals("")){
				st = sdf.format(sTime);  
			}
			if(eTime!=null && !eTime.equals("")){
				et = sdf.format(eTime);  
			}
			//账户交易记录
			String str1 = ClientInterface.selectAgentAccountTransInfoList(principal.getUserEntityInfo().getEntityId(), st, et, ioType, pageNo, pageSize,subjectNo,transType);
			log.info("账户接口返回: {} ======", str1);
			JSONObject jsons1 = JSON.parseObject(str1);
			JSONObject jsons2 = JSON.parseObject(jsons1.getString("data"));
			if (jsons2 != null && jsons2.getJSONArray("list") != null){
				List<Map> slist = JSON.parseArray(jsons2.getJSONArray("list").toJSONString(), Map.class);
				maps.put("bols", true);
				maps.put("list", slist);
				maps.put("total", jsons2.getString("total"));
			}else {
				maps.put("bols", true);
				maps.put("list", null);
				maps.put("total", jsons2.getString("total"));
			}
		}catch(Exception e){
			log.error("获取商户账户交易记录异常!",e);
			maps.put("bols", false);
			maps.put("msg", "取代理商账户交易记录异常");
		}
		return maps;
	}
	
	//判断是否显示提现按钮和补贴现金余额tgh330
	@RequestMapping(value = "/isShowButton")
	@ResponseBody
	public Object isShowButton(){
		Map<String, Object> msg = new HashMap<>();
		try {
			msg.put("flag", agentInfoService.findFunctionManage());
		} catch (Exception e) {
			log.error("系统异常",e);
		}
		return msg;
	}
	//提现手续费tgh331
	@RequestMapping(value = "/getSingleNumAmount")
	@ResponseBody
	public Object getSingleNumAmount(@RequestParam("money")String money) throws Exception {
		Map<String, Object> msg = new HashMap<>();
		UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			String serviceId = activityDetailServiceNoTran.getServiceId("002");
			String serviceId1 = sysDictService.SelectServiceId("ACCOUNT_FEE_5");//在数据字典中查出分润提现服务ID
			String serviceIdSuperBank = sysDictService.SelectServiceId("ACCOUNT_FEE_10");//超级银行家提现服务ID
			if (StringUtils.isNotBlank(serviceId)) {
				Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(serviceId);//根据代理商提现服务ID查询到费率
				if (rateMap!=null) {
					Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
					msg.put("singleNumAmount", map.get("single_num_amount"));
				}
			}
			if (StringUtils.isNotBlank(serviceId1)) {
				Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(serviceId1);
				if (rateMap!=null) {
					Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
					msg.put("singleNumAmount1", map.get("single_num_amount"));//账户余额提现手续费
				}
			}
			if (StringUtils.isNotBlank(serviceIdSuperBank)) {
				Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(serviceIdSuperBank);
				if (rateMap!=null) {
					Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
					msg.put("singleNumAmountSuperBank", map.get("single_num_amount"));//账户余额提现手续费
				}
			}
			if(Constants.PER_AGENT_TEAM_ID.equals(userInfo.getTeamId())){
				log.info("----------计算人人代理提现手续费-----------");
				String fee = sysDictService.getByKey("PER_AGENT_FEE").getSysValue();
				BigDecimal perAgentFee = new BigDecimal(fee).multiply(new BigDecimal(100));
				msg.put("perAgentFee", perAgentFee);//人人代理分润提现费率
				//分润
				String perAgentServiceId1 = sysDictService.SelectServiceId("ACCOUNT_FEE_5");//在数据字典中查出分润提现服务ID
				if (StringUtils.isNotBlank(perAgentServiceId1)) {
					Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(perAgentServiceId1);//根据代理商提现服务ID查询到费率
					if (rateMap!=null) {
						Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
						msg.put("perAgentProfitSingleAmount", map.get("single_num_amount"));
					}
				}
				BigDecimal perAgentProfitSingleAmount = (BigDecimal)msg.get("perAgentProfitSingleAmount");
				BigDecimal perAgentProfitTotalFee =
						perAgentProfitSingleAmount.add(new BigDecimal(money).multiply(new BigDecimal(fee)))
								.setScale(2, RoundingMode.HALF_UP);
				msg.put("perAgentProfitSingleAmount", perAgentProfitSingleAmount);//人人代理分润单笔提现手续费
				msg.put("perAgentProfitTotalFee", perAgentProfitTotalFee);//人人代理分润提现总手续费
				//活动
				String perAgentServiceId2 = activityDetailServiceNoTran.getServiceId("002");
				if (StringUtils.isNotBlank(perAgentServiceId2)) {
					Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(perAgentServiceId2);//根据代理商提现服务ID查询到费率
					if (rateMap!=null) {
						Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
						msg.put("perAgentActivitySingleAmount", map.get("single_num_amount"));
					}
				}
				BigDecimal perAgentActivitySingleAmount = (BigDecimal)msg.get("perAgentActivitySingleAmount");
				BigDecimal perAgentActivitylTotalFee =
						perAgentActivitySingleAmount.add(new BigDecimal(money).multiply(new BigDecimal(fee)))
								.setScale(2, RoundingMode.HALF_UP);
				msg.put("perAgentActivitySingleAmount", perAgentActivitySingleAmount);//人人代理分润单笔提现手续费
				msg.put("perAgentActivitylTotalFee", perAgentActivitylTotalFee);//人人代理分润提现总手续费
				//机具款项
				String perAgentServiceId3 = sysDictService.SelectServiceId("ACCOUNT_FEE_17");
				if (StringUtils.isNotBlank(perAgentServiceId3)) {
					Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(perAgentServiceId3);//根据代理商提现服务ID查询到费率
					if (rateMap!=null) {
						Map<String, Object> map = agentInfoService.getRateSingleNumAmount(money,rateMap);
						msg.put("perAgentTerminalSingleAmount", map.get("single_num_amount"));
					}
				}
				BigDecimal perAgentTerminalSingleAmount = (BigDecimal)msg.get("perAgentTerminalSingleAmount");
				msg.put("perAgentTerminalSingleAmount", perAgentTerminalSingleAmount);//人人代理机具单笔提现手续费
			}
		} catch (Exception e) {
			log.error("系统异常",e);
		}
		return msg;
	}
	@RequestMapping(value = "/selectSubjectNo")
	public @ResponseBody List<Map<String,String>> selectAllActivityType() throws Exception{
		List<Map<String,String>> map=null;
		try {
			map = agentInfoService.selectSubjectNo();
		} catch (Exception e) {
			log.error("查询账户类型失败",e);
		}
		return map;
	}
	
	//欢乐送提现确定提交提现tgh330
//	@RequestMapping(value="/withDrawCash")
//	@ResponseBody
//	public Object takeCash(@RequestParam("money") String money){
//		Map<String, Object> msg = new HashMap<>();
//		try {
//			//执行提现操作
//			msg = agentInfoService.UpdateWithDrawCash(money);
//		} catch (Exception e) {
//			log.error("欢乐送补贴提现提交异常",e);
//			msg.put("status", false);
//			msg.put("msg", "欢乐送补贴提现提交异常");
//		}
//		return msg;
//	}
	//活动补贴提现
	@SystemLog(description = "活动补贴提现")
	@RequestMapping(value="/withDrawCash")
	@ResponseBody
	public Object takeCash(@RequestParam("info") String params){
		Map<String, Object> msg = new HashMap<>();
		try {
			Map<String,String> paramsMap = JSONObject.parseObject(params, Map.class);
			if (needPassword(msg, paramsMap)) {
				return msg;
			}
			String money = paramsMap.get("money");
			//执行提现操作
			String happyTixianSwitch = sysDictService.SelectServiceId("HAPPY_TIXIAN_SWITCH");//欢乐返提现开关
			if ("1".equals(happyTixianSwitch)) {
				msg.put("status", false);
				msg.put("msg", "系统正在执行入账，请稍后再试！");
				return msg;
			}
			String serviceType = "10";//提现类型对应的出款服务类型
			upStreamLimit(money,serviceType);
			msg = agentInfoService.UpdateWithDrawCash(money);
		}catch (AgentWebException e){
			msg.put("status", false);
			msg.put("msg", e.getMessage());
		}catch (Exception e) {
			log.error("活动补贴提现提交异常",e);
			msg.put("status", false);
			msg.put("msg", "活动补贴提现提交异常");
		}
		return msg;
	}
	//账户余额提现
	@SystemLog(description = "账户余额提现")
	@RequestMapping(value="/takeBalance")
	@ResponseBody
	public Object takeBalance(@RequestParam("info") String params){
		Map<String, Object> msg = new HashMap<>();
		try {
			log.info("账户余额提现参数 {}",params);
			Map<String,String> paramsMap = JSONObject.parseObject(params, Map.class);
			UserLoginInfo userInfo = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (!(Constants.PER_AGENT_TEAM_ID.equals(userInfo.getTeamId())) && needPassword(msg, paramsMap)){
				return msg;
			}
			String money = paramsMap.get("money");
			//执行提现操作
			log.info("takeBalance 1");
			String serviceType = "8";//提现类型对应的出款服务类型
			upStreamLimit(money,serviceType);
			msg = agentInfoService.UpdateTakeBalance(money);
			log.info("takeBalance 2");
		}catch (AgentWebException e){
			log.info("takeBalance 3");
			msg.put("status", false);
			msg.put("msg", e.getMessage());
		} catch (Exception e) {
			log.info("takeBalance 4");
			log.error("账户余额提现提交异常",e);
			msg.put("status", false);
			msg.put("msg", "账户余额提现提交异常");
		}
		log.info("takeBalance 5");
		return msg;
	}

	/**
	 * 校验资金密码
	 * @param msg
	 * @param safePassword
	 * @return
	 */
	private boolean checkSafePassword(Map<String, Object> msg, String safePassword) {
		AgentInfo agentInfo = agentInfoService.selectByPrincipal();
		String oldSafePassword = agentInfo.getSafePassword();
		safePassword = safePassword.replaceAll(" ","+");//将空格转为+号
		// 私钥解密password
		safePassword = RSAUtils.decryptDataOnJava(safePassword, Constants.PRIVATE_KEY);
		safePassword = Md5.md5Str(safePassword + "{" + agentInfo.getAgentNo() + "}");
		if (!oldSafePassword.equals(safePassword)){
			msg.put("status", false);
			msg.put("msg", "资金密码不正确,请重新输入");
			return true;
		}
		return false;
	}

	/**
	 * 机具款项余额提现
	 * @param params
	 * @return
	 */
	@SystemLog(description = "机具款项余额提现")
	@RequestMapping(value="/takeTerminalBalance")
	@ResponseBody
	public Object takeTerminalBalance(@RequestParam("info") String params){
		Map<String, Object> msg = new HashMap<>();
		try {
			Map<String,String> paramsMap = JSONObject.parseObject(params, Map.class);
			if (needPassword(msg, paramsMap)){
				return msg;
			}
			String money = paramsMap.get("money");
//			String serviceType = "14";//提现类型对应的出款服务类型
//			upStreamLimit(money,serviceType);//需求要求机具款项上游不作校验
			msg = agentInfoService.takeTerminalBalance(money, "224124", Constants.PER_AGENT_SUB_TYPE);
		}catch (Exception e) {
			log.error("机具款项余额提现提交异常",e);
			msg.put("status", false);
			msg.put("msg",e.getMessage());
		}
		return msg;
	}

	/**
	 * 提现资金密码
	 * @param msg
	 * @param paramsMap
	 * @return
	 */
	private boolean needPassword(Map<String, Object> msg, Map<String, String> paramsMap) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal.getOemTypeEnum().getHasSafePassword()) {
			if (!hasSafePhone()) {
				msg.put("status", false);
				msg.put("needset", true);
				msg.put("msg", "请先设置安全手机");
				return true;
			}
			String safePassword = paramsMap.get("safePassword");
			if (StringUtils.isNotBlank(safePassword) && checkSafePassword(msg, safePassword)) {
				return true;
			}
		}
		return false;
	}

	@SystemLog(description = "超级兑提现")
	@RequestMapping(value="/takeRedemBalance")
	@ResponseBody
	public ResponseBean takeRedemBalance(@RequestParam("money") String money,
										 @RequestParam("safePassword") String safePassword){
		try {
			Map<String, Object> msg = new HashMap<>();
			ResponseBean responseBean = getResponseBean(safePassword, msg);
			if (responseBean != null){
				return responseBean;
			}
			return new ResponseBean(agentInfoService.commonWithdrawCash(money, "224120", "13",redemWithdrawCheck));
		} catch (Exception e) {
			log.error("积分兑换账户余额提现提交异常",e);
			return new ResponseBean(e);
		}
	}

	private ResponseBean getResponseBean(@RequestParam("safePassword") String safePassword, Map<String, Object> msg) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal.getOemTypeEnum().getHasSafePassword()){
			if(!hasSafePhone()){
				msg.put("needset",true);
				return new ResponseBean("请先设置安全手机",false);
			}
			if (StringUtils.isNotBlank(safePassword) && checkSafePassword(msg, safePassword)){
				return new ResponseBean(msg.get("msg"),false);
			}
		}
		return null;
	}

	/**
	 * 信用卡管家账户余额提现
	 * @param money
	 * @param safePassword
	 * @return
	 */
	@SystemLog(description = "信用卡管家账户余额提现")
	@RequestMapping(value="/takeCreditBalance")
	@ResponseBody
	public ResponseBean takeCreditBalance(@RequestParam("money") String money,
										  @RequestParam("safePassword") String safePassword){
		try {
			ResponseBean responseBean = getResponseBean(safePassword, new HashMap<String, Object>());
			if (responseBean != null){
				return responseBean;
			}
			// todo 修改subType类型
			String serviceType = "12";//提现类型对应的出款服务类型
			upStreamLimit(money,serviceType);
			return new ResponseBean(agentInfoService.commonWithdrawCash(money, "224121", "14"));
		} catch (Exception e) {
			log.error("信用卡管家余额提现提交异常",e);
			return new ResponseBean(e);
		}
	}

	@SystemLog(description = "超级兑-激活版提现")
	@RequestMapping(value="/takeRedemActiveBalance")
	@ResponseBody
	public ResponseBean takeRedemActiveBalance(@RequestParam("money") String money,
											   @RequestParam("safePassword") String safePassword){
		try {
			Map<String, Object> msg = new HashMap<>();
			ResponseBean responseBean = getResponseBean(safePassword, msg);
			if (responseBean != null){
				return responseBean;
			}
			return new ResponseBean(agentInfoService.commonWithdrawCash(money, "224123", "16", redemActiveWithdrawCheck));
		} catch (Exception e) {
			log.error("积分兑换激活版账户余额提现提交异常",e);
			return new ResponseBean(e);
		}
	}


	/**
	 * 超级还提现
	 * 信用卡余额提现
	 * @param money
	 * @return
	 */
	@SystemLog(description = "超级还提现")
	@RequestMapping(value="/takeReplayBalance")
	@ResponseBody
	public ResponseBean takeReplayBalance(@RequestParam("money") String money,
										  @RequestParam("safePassword") String safePassword){
		try {
			Map<String, Object> msg = new HashMap<>();
			ResponseBean responseBean = getResponseBean(safePassword, msg);
			if (responseBean != null){
				return responseBean;
			}
			String serviceType = "11";//提现类型对应的出款服务类型
			upStreamLimit(money,serviceType);
			return new ResponseBean(agentInfoService.commonWithdrawCash(money, "224114", "9"));
		} catch (Exception e) {
			log.error("信用卡余额提现提交异常",e);
			return new ResponseBean(e);
		}
	}
	/**
	 * 超级银行家分润提现
	 * @param money
	 * @return
	 */
	@SystemLog(description = "超级银行家分润提现")
	@RequestMapping(value="/takeReplayBalanceSuperBank")
	@ResponseBody
	public ResponseBean takeReplayBalanceSuperBank(@RequestParam("money") String money,
												   @RequestParam("safePassword") String safePassword){
		try {
			Map<String, Object> msg = new HashMap<>();
			ResponseBean responseBean = getResponseBean(safePassword, msg);
			if (responseBean != null){
				return responseBean;
			}
			return new ResponseBean(agentInfoService.commonWithdrawCash(money, "224116", "11", superBankWithdrawCheck));
		} catch (Exception e) {
			log.error("超级银行家余额提现提交异常",e);
			return new ResponseBean(e);
		}
	}
	/**
	 * 每日分润报表tgh415
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectAllShareList")
	@ResponseBody
	public Object selectAllShareList(@RequestParam("info") String param){
		log.info("=================>>进入查询每日分润报表!");
		Map<String, Object> maps = new HashMap<>();
		try{
			maps = agentInfoService.selectAllShareList(param);
		}catch(Exception e){
			log.error("查询每日分润报表异常!",e);
			maps.put("bols", false);
			maps.put("msg", "查询每日分润报表异常");
		}
		return maps;
	}

//	@RequestLimit
	@RequestMapping("/profitDaySettleDetailList")
	@ResponseBody
	public ProfitDaySettleDetailBean profitDaySettleDetailList(@RequestBody ProfitDaySettleDetailParamBean paramBean,HttpServletRequest request){

		boolean access = accessService.canQueryProfitSettleDetailList(paramBean.getAgentNo(), false);
		if(!access){
			ProfitDaySettleDetailBean result = new ProfitDaySettleDetailBean();
			result.setStatus(false);
			result.setMsg("非法操作");
			log.error("非法操作!");
			return result;
		}
		log.info("=================>>进入查询每日分润报表!");
		try{
			ipRequestLimitUtils.ipRequestLimit(request);
			if (StringUtils.isNotBlank(paramBean.getAgentNo())){
				AgentInfo agentInfo = agentInfoService.selectByagentNo(paramBean.getAgentNo());
				if (agentInfo != null){
					paramBean.setAgentNo(agentInfo.getAgentNode());
				}
			}
			log.info("调用交易分润接口");
			ProfitDaySettleDetailBean psdb = ClientInterface.profitDaySettleDetailList(paramBean);
			return psdb;
		}catch(RequestLimitException e){
			ProfitDaySettleDetailBean result = new ProfitDaySettleDetailBean();
			result.setStatus(false);
			result.setMsg(e.getMessage());
			log.error("请求操作过于频繁!",e);
			return result;
		}catch(Exception e){
			ProfitDaySettleDetailBean result = new ProfitDaySettleDetailBean();
			result.setStatus(false);
			result.setMsg("查询交易分润明细异常");
			log.error("查询交易分润明细异常!",e);
			return result;
		}
	}

	// 每日分润报表导出操作
	@SystemLog(description = "每日分润报表导出操作")
	@RequestMapping("/exportSharePreDay")
	@ResponseBody
	public void exportSharePreDay(@RequestParam Map<String,String> param, HttpServletResponse response){
		System.out.println(param);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String st =StringUtils.trimToEmpty(param.get("sTime"));
		String et =StringUtils.trimToEmpty(param.get("eTime"));
		String statu= StringUtils.trimToEmpty(param.get("statu"));
		Integer pageNo = 1;
		Integer pageSize = Integer.MAX_VALUE;
		String selectAgentNo= StringUtils.trimToEmpty(param.get("selectAgentNo"));
		try {
			String selectShareByDay = ClientInterface.selectShareByDay(principal.getUserEntityInfo().getEntityId(),selectAgentNo, st, et, statu, pageNo, pageSize);
			log.info("每日分润报表导出获取到的数据：" + selectShareByDay);
			List<Map<String, Object>> mapList = new ArrayList<>();
			if (StringUtils.isNotBlank(selectShareByDay)){
                Gson gson = MapTypeAdapter.newGson();
                Map<String, String> resultMap = gson.fromJson(selectShareByDay, new TypeToken<Map<String, String>>(){}.getType());
				String data = resultMap.get("data");
				if (StringUtils.isNotBlank(data)){
					Map<String, Object> dataMap = gson.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
					Object list = dataMap.get("list");
					if (list != null){
						mapList = (List<Map<String, Object>>) dataMap.get("list");
					}
				}
			}
			String[] columnNames = 	{"交易日期","代理商编号", "代理商名称", "交易总金额", "交易总笔数",
					"提现总笔数", "商户交易手续费", "商户提现手续费", "原交易分润", "原提现分润",
					"调账金额", "调整后交易分润", "调整后提现分润", "调整后总分润", "实际到账分润", "冻结金额",
					"入账时间", "入账状态",};
			Workbook workbook = ExcelUtils.createWorkBook("每日分润报表查询", mapList, columnNames, sharePreDayCreateRow);
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(("每日分润报表.xls").getBytes(), "ISO8859-1"));
			workbook.write(response.getOutputStream());
			response.flushBuffer();
		}catch (Exception e){
			log.error("每日分润报表导出异常", e);
		}
	}

	// 我的账户 账户明细导出
	@SystemLog(description = "账户明细导出")
	@RequestMapping("/exportAccountTranInfo")
	@ResponseBody
	public void exportAccountTranInfo(@RequestParam Map<String,String> param, HttpServletResponse response){
		System.out.println(param);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String st =StringUtils.trimToEmpty(param.get("sTime"));
		String et =StringUtils.trimToEmpty(param.get("eTime"));
		String ioType= StringUtils.trimToEmpty(param.get("ioType"));
		String subjectNo= StringUtils.trimToEmpty(param.get("subjectNo"));
		String transType= StringUtils.trimToEmpty(param.get("transType"));
		Integer pageNo = 1;
		Integer pageSize = Integer.MAX_VALUE;
		try {
			String selectAgentAccountTransInfoList = ClientInterface.selectAgentAccountTransInfoList(principal.getUserEntityInfo().getEntityId(), st, et, ioType, pageNo, pageSize,subjectNo,transType);
			log.info("账户明细导出导出获取到的数据：" + selectAgentAccountTransInfoList);
			List<Map<String, Object>> mapList = new ArrayList<>();
			if (StringUtils.isNotBlank(selectAgentAccountTransInfoList)){
                Gson gson = MapTypeAdapter.newGson();
                Map<String, String> resultMap = gson.fromJson(selectAgentAccountTransInfoList, new TypeToken<Map<String, String>>(){}.getType());
				String data = resultMap.get("data");
				if (StringUtils.isNotBlank(data)){
					Map<String, Object> dataMap = gson.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
					Object list = dataMap.get("list");
					if (list != null){
						mapList = (List<Map<String, Object>>) dataMap.get("list");
					}
				}
			}
			String[] columnNames = 	{"账户类别", "订单编号", "记账时间", "交易类型",
                    "操作", "金额", "账户余额", "可用余额", "摘要"};
			Workbook workbook = ExcelUtils.createWorkBook("账户明细导出查询", mapList, columnNames, accountTranInfoCreateRow);
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(("账户明细.xls").getBytes(), "ISO8859-1"));
			workbook.write(response.getOutputStream());
			response.flushBuffer();
		}catch (Exception e){
			log.error("账户明细导出异常", e);
		}
	}

//	@RequestLimit
	@SystemLog(description = "每日分润报表导出")
	@RequestMapping("/exportProfitDaySettleDetailList")
	@ResponseBody
	public void exportProfitDaySettleDetailList(@RequestParam("info") String info,HttpServletRequest request, HttpServletResponse response){
		log.info("=================>>进入查询每日分润报表!");
		String[] columnNames = {"交易订单号", "交易时间", "硬件产品种类", "业务产品", "卡类型", "商户编号", "商户名称", "代理商编号", "代理商名称", "交易金额", "交易商户扣率", "交易商户手续费", "交易代理商分润", "商户提现手续费", "抵扣提现手续费", "提现代理商分润"};
		String fileName = "交易分润明细查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			ipRequestLimitUtils.ipRequestLimit(request);
			ProfitDaySettleDetailParamBean paramBean = JSON.parseObject(info, ProfitDaySettleDetailParamBean.class);
			if (paramBean == null) {
				paramBean = new ProfitDaySettleDetailParamBean();
			}
			if (StringUtils.isBlank(paramBean.getAgentNo())) {
				paramBean.setAgentNo(principal.getUserEntityInfo().getEntityId());
			}
			List<ProfitDaySettleDetailBean.DataList> list;
			// 权限校验
			if (accessService.canAccessTheAgent(paramBean.getAgentNo(), false)) {
				list = agentInfoService.exportProfitDaySettleDetailList(paramBean);
			}else {
				list = new ArrayList<>();
			}
			Workbook workbook = ExcelUtils.createWorkBook("交易分润明细查询", list, columnNames, createProfitDaySettleDetailRow());
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
			workbook.write(response.getOutputStream());
			response.flushBuffer();
		} catch (RequestLimitException e) {
			List<ProfitDaySettleDetailBean.DataList> list = new ArrayList<>();
			DataList dataList = new ProfitDaySettleDetailBean.DataList();
			dataList.setMerchantNo("当前操作过于频繁");
			list.add(dataList);
			Workbook workbook = ExcelUtils.createWorkBook("交易分润明细查询", list, columnNames, createProfitDaySettleDetailRow());
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			try {
				response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
				workbook.write(response.getOutputStream());
				response.flushBuffer();		
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 预冻结明细查询
	 * @return
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 15:35:57
	 */
	@RequestMapping(value = "/preliminaryFreezeQuery")
	@ResponseBody
	public Object preliminaryFreezeQuery(@RequestParam("info") String param){
		log.info("=================>>进入预冻结明细查询!");
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			JSONObject json = JSON.parseObject(param);
			data = agentInfoService.preliminaryFreezeQuery(json);
		}catch(Exception e){
			log.error("预冻结明细查询异常!",e);
			data.put("bols", false);
			data.put("msg", "预冻结明细查询异常");
		}
		return data;
	}

	/**
	 * 预冻结明细导出
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 16:12:48
	 */
	@SystemLog(description = "预冻结明细导出")
	@RequestMapping(value="/preliminaryFreezeExport")
	@ResponseBody
	public void preliminaryFreezeExport(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		info = new String(info.getBytes("ISO-8859-1"),"UTF-8");
		JSONObject jsonObject = JSONObject.parseObject(info);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "预冻结记录"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		String dataStr = agentInfoService.preliminaryFreezeExport(jsonObject);
		str2List(data, dataStr);
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"agentNo","agentName","freezeTime","terminalFreezeAmount","otherFreezeAmount",
				"fenFreezeAmount","activityFreezeAmount","freezeAmount","remark"};
		String[] colsName = new String[]{"代理商编号","代理商名称","申请预冻结时间","机具款预冻结金额","其他冻结金额",
				"分润账户冻结金额","活动补贴账户冻结金额","预冻金额","备注"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

	/**
	 * 解冻明细查询
	 * @return
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 15:35:57
	 */
	@RequestMapping(value = "/unFreezeQuery")
	@ResponseBody
	public Object unFreezeQuery(@RequestParam("info") String param){
		log.info("=================>>进入解冻明细查询!");
		Map<String, Object> data = new HashMap<>();
		try{
			JSONObject json = JSON.parseObject(param);
			data = agentInfoService.unFreezeQuery(json);
		}catch(Exception e){
			log.error("解冻明细查询异常!",e);
			data.put("bols", false);
			data.put("msg", "解冻明细查询异常");
		}
		return data;
	}

	/**
	 * 解冻明细导出
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 16:12:48
	 */
	@SystemLog(description = "解冻明细导出")
	@RequestMapping(value="/unFreezeExport")
	@ResponseBody
	public void unFreezeExport(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		info = new String(info.getBytes("ISO-8859-1"),"UTF-8");
		JSONObject jsonObject = JSONObject.parseObject(info);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "解冻明细记录"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		String dataStr = agentInfoService.unFreezeExport(jsonObject);
		str2List(data, dataStr);
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"agentNo","agentName","unfreezeTime","amount",
				"terminalFreezeAmount","otherFreezeAmount","fenFreezeAmount","activityFreezeAmount","remark"};
		String[] colsName = new String[]{"代理商编号","代理商名称","解冻时间","解冻金额",
				"解冻机具预冻结款","解冻其他预冻结款","解冻分润账户冻结款","解冻活动补贴账户冻结款","备注"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

	/**
	 * 预调账明细查询
	 * @return
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 15:35:57
	 */
	@RequestMapping(value = "/preliminaryAdjustQuery")
	@ResponseBody
	public Object preliminaryAdjustQuery(@RequestParam("info") String param){
		log.info("=================>>进入预调账明细查询!");
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			JSONObject json = JSON.parseObject(param);
			data = agentInfoService.preliminaryAdjustQuery(json);
		}catch(Exception e){
			log.error("预调账明细查询异常!",e);
			data.put("bols", false);
			data.put("msg", "预调账明细查询异常");
		}
		return data;
	}

	/**
	 * 预调账明细导出
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 * @author ZengJA
	 * @date 2017-07-06 16:12:48
	 */
	@SystemLog(description = "预调账明细导出")
	@RequestMapping(value="/preliminaryAdjustExport")
	@ResponseBody
	public void preliminaryAdjustExport(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		info = new String(info.getBytes("ISO-8859-1"),"UTF-8");
		JSONObject jsonObject = JSONObject.parseObject(info);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "预调账记录"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		String dataStr = agentInfoService.preliminaryAdjustExport(jsonObject);
		str2List(data, dataStr);
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"agentNo","agentName","adjustTime","openBackAmount","rateDiffAmount","tuiCostAmount","riskSubAmount","merMgAmount","bailSubAmount","otherAmount",
				"activityAvailableAmount","activityFreezeAmount","generateAmount","remark"};
		String[] colsName = new String[]{"代理商编号","代理商名称","申请预调账时间","开通返现","费率差异","超级推成本","风控扣款","商户管理费","保证金扣除","其他",
				"账户可用余额调账金额","账户冻结余额调账金额","预调账金额","备注"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

	private void str2List(List<Map<String, String>> data, String dataStr) {
		if(StringUtils.isNotBlank(dataStr)){
			JSONArray jsonArray = JSONArray.parseArray(dataStr);
			Map<String, String> map = null;
			JSONObject jsObject = null;
			Set<String> keySet = null;
			for (Object obj:jsonArray) {
				map = new HashMap<String,String>();
				jsObject = JSONObject.parseObject(obj.toString());
				keySet = jsObject.keySet();
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				for (String key:keySet) {
					if(key.toLowerCase().contains("time")){
						map.put(key,simpleDateFormat.format(new Date(Long.valueOf(jsObject.getString(key)))));
					}else{
						map.put(key,String.valueOf(jsObject.get(key)));
					}
				}
				data.add(map);
			}
		}
	}

	private CreateRow<ProfitDaySettleDetailBean.DataList> createProfitDaySettleDetailRow;
	private CreateRow<ProfitDaySettleDetailBean.DataList> createProfitDaySettleDetailRow() {
		if (createProfitDaySettleDetailRow != null){
			return createProfitDaySettleDetailRow;
		}
		createProfitDaySettleDetailRow = new CreateRow<ProfitDaySettleDetailBean.DataList>() {
			@Override
			public void writeRow(Row row, ProfitDaySettleDetailBean.DataList dataList) {
				int index = 0;
				row.createCell(index ++).setCellValue(dataList.getPlateOrderNo());
				row.createCell(index ++).setCellValue(dataList.getTransTime());
				row.createCell(index ++).setCellValue(dataList.getHardwareProductName());
				row.createCell(index ++).setCellValue(dataList.getBusinessProductName());
				/*row.createCell(index ++).setCellValue(dataList.getSuperPush());
				row.createCell(index ++).setCellValue(dataList.getPayMethod());*/
				row.createCell(index ++).setCellValue(dataList.getCardType());
				row.createCell(index ++).setCellValue(dataList.getMerchantNo());
				row.createCell(index ++).setCellValue(dataList.getMerchantName());
				row.createCell(index ++).setCellValue(dataList.getAgentNo());
				row.createCell(index ++).setCellValue(dataList.getAgentName());
				row.createCell(index ++).setCellValue(dataList.getTransAmount());
				row.createCell(index ++).setCellValue(dataList.getMerchantRate());
				row.createCell(index ++).setCellValue(dataList.getMerchantFee());
				row.createCell(index ++).setCellValue(dataList.getAgentShareAmount());
				row.createCell(index ++).setCellValue(dataList.getMerCashFee());
				row.createCell(index ++).setCellValue(dataList.getDeductionFee());
				row.createCell(index ++).setCellValue(dataList.getCashAgentShareAmount());
//				row.createCell(index ++).setCellValue(dataList.getAgentProfitCollectionStatus());
//				row.createCell(index ++).setCellValue(dataList.getCollectionBatchNo());
			}
		};
		return createProfitDaySettleDetailRow;
	}

	@RequestMapping("/findAgentProfitCollection")
	@ResponseBody
	public ResponseBean findAgentProfitCollection(){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String, Object> result = agentInfoService.findAgentProfitCollection(principal.getUserEntityInfo().getEntityId());
			return new ResponseBean(result);
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}

	@SystemLog(description = "更换默认业务产品")
	@RequestMapping("/updateDefaultFlagSwitch")
	@ResponseBody
	public ResponseBean updateDefaultFlagSwitch(String bpId){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			return new ResponseBean("修改成功.", agentInfoService.updateDefaultFlagSwitch(bpId, loginAgentNo));
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}
	/**
	 * 银行家红包明细查询
	 * @return
	 */
	@RequestMapping("/selectRedEnvelopesDetails")
	@ResponseBody
	public Map<String,Object> selectRedEnvelopesDetails(@RequestParam("info") String param,@Param("page")Page<RedEnvelopesDetails> page){
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "红包收支明细查询异常!";
		try {
			RedEnvelopesDetails info = JSON.parseObject(param,RedEnvelopesDetails.class);
			map = agentInfoService.selectRedEnvelopesDetails(info,page);
			map.put("page",page);
			map.put("flag",true);
		}catch (Exception e){
			log.error(msg,e);
			map.put("flag",false);
			map.put("msg", msg);
		}
		return map;
	}
	/**
	 * 红包余额查询
	 * @return
	 */
	@RequestMapping("/selectBalance")
	@ResponseBody
	public Map<String,Object> selectBalance(){
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "红包余额查询异常!";
		try {
			map = agentInfoService.selectBalance();
		}catch (Exception e){
			log.error(msg,e);
			map.put("msg", msg);
		}
		return map;
	}
	/**
	 * 红包余额提现,默认全部提现
	 * @return
	 */
	@SystemLog(description = "红包余额提现")
	@RequestMapping("/withdrawRedBalance")
	@ResponseBody
	public Map<String,Object> withdrawRedBalance(@RequestParam("redBalance") String param){
		Map<String, Object> map = new HashMap<String, Object>();
		String msg = "提现异常!";
		try {
			String redBalance = JSON.parseObject(param,String.class);
			synchronized(this){
				map = agentInfoService.updateWithdrawRedBalance(redBalance);
			}
		}catch (Exception e){
			log.error(msg,e);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	/**
	 * 红包收支明细导出
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@SystemLog(description = "红包收支明细导出")
	@RequestMapping("/exportRedEnvelopesDetails")
	@ResponseBody
	public void exportRedEnvelopesDetails(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		RedEnvelopesDetails redEnvelopesDetails = JSON.parseObject(info,RedEnvelopesDetails.class);
		List<RedEnvelopesDetails> list = agentInfoService.exportRedEnvelopesDetails(redEnvelopesDetails);
		String fileName = "红包收支明细导出"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		List<Map<String, String>> data = new ArrayList<>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("type", null);
			maps.put("redOrderId", null);
			maps.put("createDate", null);
			maps.put("transType", null);
			maps.put("operationType", null);
			maps.put("transAmount", null);
			data.add(maps);
		}else{
			for (RedEnvelopesDetails order : list) {
				Map<String, String> maps = new HashMap<>();
				String type = order.getType();
				switch (type) {
				case "0":
					type = "平台";
					break;
				case "1":
					type = "品牌商";
					break;
				case "2":
					type = "个人";
					break;
				default:
					break;
				}
				maps.put("type", type);
				maps.put("redOrderId", order.getRedOrderId());
				maps.put("createDate", order.getCreateDate() == null ? "" : sdf.format(order.getCreateDate()));
				String transType = order.getTransType();
				switch (transType) {
				//0发红包，1抢红包，2红包分润，3过期余额回收，4其他账户转入，5转出其他账户，6风控关闭红包，7风控打开关闭的红包
				case "0":
					transType = "发红包";
					break;
				case "1":
					transType = "抢红包";
					break;
				case "2":
					transType = "红包分润";
					break;
				case "3":
					transType = "过期余额回收";
					break;
				case "4":
					transType = "其他账户转入";
					break;
				case "5":
					transType = "转出其他账户";
					break;
				case "6":
					transType = "风控关闭红包";
					break;
				case "7":
					transType = "风控打开关闭的红包";
					break;
				case "8":
					transType = "区域代理收益";
					break;
				case "9":
					transType = "买入区域";
					break;
				case "10":
					transType = "区域代理交易分润";
					break;
				case "11":
					transType = "转让区域代理收益";
					break;
				default:
					break;
				}
				maps.put("transType", transType);
				maps.put("operationType", order.getOperationType());
				maps.put("transAmount", order.getTransAmount() == null ? "0" : order.getTransAmount().toString());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"type","redOrderId","createDate","transType","operationType","transAmount"};
		String[] colsName = new String[]{"账户类别","对应红包ID","记账时间","变动类型", "操作","金额"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, ouputStream);
		ouputStream.close();
	}
	/**
	 *4：  os.service_type=10 order by os.level
			5：  os.service_type=8 order by os.level
			9：  os.service_type=11 order by os.level
			14：os.service_type=12 order by os.level
			
			李肖 2018/9/3 17:30:28
			out_account_service os
			加限制,上游余额大于等于设置的剩余提现额度值
	
			serviceType 出款服务类型
			balance     设置的剩余额度
	 */
	private void checkBalance(String serviceType,String balance) {
        List<Map<String, Object>> list = agentInfoService.selectByServiceType(serviceType);
        if (list.size() < 1) {
        	log.info("===可用出款通道条数: " + list.size() + " 条==所有开关为关闭状态======");
        	throw new AgentWebException("系统繁忙，请稍后重试");
		}
        for (int i = 0; i < list.size(); i++) {
        	log.info("===可用出款通道条数: " + list.size() + " 条========");
        	Map<String, Object> map = list.get(i);
        	BigDecimal userBalance = new BigDecimal(map.get("user_balance").toString());//user_balance 平台在上游余额
        	if (userBalance.compareTo(new BigDecimal(balance)) == -1 && i == list.size()-1) {//大于1,等于0,小于-1,而且是最后一个,就提示
        		log.info("====最后一条通道,上游可用余额为: " + userBalance + "元 小于 boss设置的剩余额度值:"+ balance + " =====");
        		//海涛,国栋,水育确认上游金额小于设置金额,自动关闭通道开关,然后账务手动开启
        		agentInfoService.updateWithdrawSwitch(Integer.valueOf(map.get("id").toString()));
				throw new AgentWebException("系统繁忙，请稍后重试");//若所有服务“上游账户余额”都低于设置的剩余额度值，则提示代理商：系统繁忙，请稍后重试
        	}else if(userBalance.compareTo(new BigDecimal(balance)) == -1){
        		//海涛,国栋,水育确认上游金额小于设置金额,自动关闭通道开关,然后账务手动开启
        		agentInfoService.updateWithdrawSwitch(Integer.valueOf(map.get("id").toString()));
        		log.info("====上游可用余额为: " + userBalance + "元 小于 boss设置的剩余额度值:"+ balance + " =====");
        		continue;
        	}else if(userBalance.compareTo(new BigDecimal(balance)) != -1){
        		log.info("====上游可用余额为: " + userBalance + "元 , boss设置的剩余额度值:"+ balance + ", ====执行正常提现=====");
        		break;
        	}
		}
	}

	/**
	 * 上游账户余额限制
	 * 
	 * 开关控制 
	 * 判断代理商限制开关是否打开，如未打开则正常提现，如已打开则进入下一条判断条件；
	 * 关闭:正常提现
	 * 打开:提现金额大于上游余额给出提示
	 */
	private void upStreamLimit(String money,String serviceType) {
		if ("1".equals(sysDictService.SelectServiceId("AGENT_WITHDRAW_SWITCH"))) {//0:关闭,1:开启
			String upStreamBalance = sysDictService.SelectServiceId("AGENT_WITHDRAW_BALANCE");//设置的剩余额度值 
			log.info("上游提现金额限制为===" + upStreamBalance + "元=======");
//			if (new BigDecimal(money).compareTo(new BigDecimal(upStreamBalance)) == 1) {
//				log.info("======提现金额大于代理商设置金额,数据字典AGENT_WITHDRAW_BALANCE=======");
//				throw new AgentWebException("系统繁忙,请稍后重试");
//			}
			checkBalance(serviceType,upStreamBalance);
		}
	}
//	@RequestMapping(value="/getAccountTranInfo")
//	@ResponseBody
//	public Object getAccountTranInfo(@RequestParam("info") String param) throws Exception{
//		Map<String, Object> maps=new HashMap<String, Object>();
//		try{
//			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			AgentInfo agent = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
////			if(agent.getHasAccount()==0){
////				maps.put("bols", false);
////				maps.put("msg", "代理商未开户");
////				return maps;
////			}
//			JSONObject json=JSON.parseObject(param);
//			Date sTime=json.getDate("sTime");
//			Date eTime=json.getDate("eTime");
//			String ioType=json.getString("ioType");
//			Integer pageNo=json.getInteger("pageNo");
//			Integer pageSize=json.getInteger("pageSize");
//			String subjectNo = json.getString("subjectNo");//账户类型
//			String transType = json.getString("transType");//交易类型
//			
//			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
//			String st="";
//			String et="";
//			if(sTime!=null && !sTime.equals("")){
//				st = sdf.format(sTime);  
//			}
//			if(eTime!=null && !eTime.equals("")){
//				et = sdf.format(eTime);  
//			}
//			//账户交易记录
//			String str1 = ClientInterface.selectAgentAccountTransInfoList(principal.getUserEntityInfo().getEntityId(), st, et, ioType, pageNo, pageSize,subjectNo,transType);
//			JSONObject jsons1=JSON.parseObject(str1);
//			JSONObject jsons2=JSON.parseObject(jsons1.getString("data"));
//			List<Map> slist = JSON.parseArray(jsons2.getJSONArray("list").toJSONString(), Map.class);
//			maps.put("bols", true);
//			maps.put("list", slist);
//			maps.put("total", jsons2.getString("total"));
//		}catch(Exception e){
//			log.error("获取商户账户交易记录异常!",e);
//			maps.put("bols", false);
//			maps.put("msg", "取代理商账户交易记录异常");
//		}
//		return maps;
//	}

	@RequestMapping("/selectTransType.do")
	public ResponseBean selectTransType() {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AgentInfo agent = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());

		final String secret = "zouruijin";
		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 60L; // expires claim. In this case the token
									// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("exp", exp);
		claims.put("iat", iat);
		claims.put("jti", jti);

		// 根据代理商类型和所属品牌获取交易分组
		String tradeGroup = sysDictService.getTradeGroupByAgentInfo(agent.getAgentType(),agent.getAgentOem());

		// 注明：以下参数均为 字符串类型
		claims.put("transGroup",tradeGroup);
		final String token = signer.sign(claims);
		String url = Constants.accountTransTypeByAgentType;
		String sendPost = HttpUtils.sendPost(url, "token="+token, "utf-8");
		log.info("请求账户交易类型返回:{}",sendPost);
		JSONObject json = JSON.parseObject(sendPost);
		List transTypeList = JSONObject.parseObject((String)json.get("data"), List.class);
		ResponseBean bean = new ResponseBean(true);
		bean.setData(transTypeList);
		return bean;
	}

	@RequestMapping("/getCode.do")
	public void getCode(HttpServletResponse resp,HttpServletRequest req) throws Exception{
		// 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute("imgcode", codeMap.get("code").toString());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);
        resp.setContentType("image/jpeg");
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	

	@RequestMapping("/getCodeLevel")
	public void getCodeLevel(HttpServletResponse resp,HttpServletRequest req) throws Exception{
		// 调用工具类生成的验证码和验证码图片
        Map<String, Object> codeMap = CodeUtil.generateCodeAndPic();
        // 将四位数字的验证码保存到Session中。
        HttpSession session = req.getSession();
        session.setAttribute("imgcodeLevel", codeMap.get("code").toString());
        // 禁止图像缓存。
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-cache");
        resp.setDateHeader("Expires", -1);
        resp.setContentType("image/jpeg");
        ServletOutputStream sos;
        try {
            sos = resp.getOutputStream();
            ImageIO.write((RenderedImage) codeMap.get("codePic"), "jpeg", sos);
            sos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	@RequestMapping(value = "/sendMsg")
	@ResponseBody
	public Map<String, Object> sendMsg(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			//校验手机
			if(!isPhone(info.getPhone())){
				map.put("msg", "手机号码格式不正确");
				return map;
			}
			//校验验证码
			if(!isImgNum(info.getImgNum())){
				map.put("msg", "验证码格式不正确");
				return map;
			}
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcode");
			
			//销毁验证码
			//session.removeAttribute("imgcode");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum())){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
	            Sms.sendMsg(info.getPhone(), String.format("验证码：%s。您正在设置安全手机号码，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("checkphone", info.getPhone());
	            session.setAttribute("phoneNum", phoneNum);
	            session.setAttribute("phoneNumTime", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@RequestMapping(value = "/sendOldMsg")
	@ResponseBody
	public Map<String, Object> sendOldMsg(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcode");
			//销毁验证码
			//session.removeAttribute("imgcode");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum())){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
		        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String agentNo = principal.getUserEntityInfo().getEntityId();
				String safePhone = agentInfoService.getSafePhone(agentNo);
		        
	            Sms.sendMsg(safePhone, String.format("验证码：%s。您正在修改安全手机号码，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("oldphoneNum", phoneNum);
	            session.setAttribute("oldphoneNumTime", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@RequestMapping(value = "/sendOldMsgLevel")
	@ResponseBody
	public Map<String, Object> sendOldMsgLevel(@RequestParam("checkNum") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String oldCode = (String)session.getAttribute("imgcodeLevel");
			//销毁验证码
			session.removeAttribute("imgcodeLevel");
			
			if(StringUtils.isNotBlank(oldCode) && oldCode.equalsIgnoreCase(info.getImgNum()) && StringUtils.isNotBlank(info.getAgentNo())  ){
				//4位数字验证码生成验证码
		        String phoneNum = "" + getRandom(4);
				//发送验证码
				String safePhone = agentInfoService.getSafePhone(info.getAgentNo());
		        
	            Sms.sendMsg(safePhone, String.format("验证码：%s。您正在修改安全手机号码，如非本人操作请及时登录后台查看。", phoneNum));
	            String phoneNumTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
				//存储session
	            session.setAttribute("oldphoneNumLevel", info.getAgentNo()+"-"+phoneNum);
	            session.setAttribute("oldphoneNumTimeLevel", phoneNumTime);
	            System.out.println("手机验证码为========================>:"+phoneNum);
				//响应
				map.put("status", true);
			}else{
				map.put("msg", "图形验证码有误");
			}
			
		} catch (Exception e) {
			log.error("获取验证码异常!",e);
		}
		return map;
	}
	
	@SystemLog(description = "校验旧安全手机")
	@RequestMapping(value = "/checkOldPhone")
	@ResponseBody
	public Map<String, Object> checkOldPhone(@RequestParam("info") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String phoneNum = (String)session.getAttribute("oldphoneNum");
			String phoneNumTime = (String) session.getAttribute("oldphoneNumTime");
			String imgcode = (String) session.getAttribute("imgcode");

			//校验验证码是否匹配
			if(StringUtils.isBlank(imgcode) || !imgcode.equals(info.getImgNum())){
				map.put("msg", "图形验证码不匹配");
				return map;
			}  
			
			if(StringUtils.isBlank(phoneNum) || !phoneNum.equals(info.getPhoneNum())){
				map.put("msg", "短信验证码不匹配");
				return map;
			}
			if(Long.valueOf(phoneNumTime)-System.currentTimeMillis()<0){
				map.put("msg", "验证码超过5分钟,请重新获取");
				return map;
			}
		
			map.put("status", true);	
			session.setAttribute("checkPhoneNumTrue", true);
            String checkPhoneNumTrueTime = "" + (System.currentTimeMillis() + 1000 * 60 * 5);
			session.setAttribute("checkPhoneNumTrueTime", checkPhoneNumTrueTime);
			
			//删除验证码
			session.removeAttribute("oldphoneNum");
			session.removeAttribute("oldphoneNumTime");
			session.removeAttribute("imgcode");

			
		} catch (Exception e) {
			log.error("修改安全手机异常!",e);
			map.put("msg", "修改异常");
		}
		return map;
	}
	
	
	@RequestMapping(value="/getPhone.do")
	@ResponseBody
	public Map<String, Object> getPhone() throws Exception{
		Map<String, Object> msg = new HashMap<String, Object>();
		Boolean hasSafePassword = false;
		try{
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();
			String safePhone = agentInfoService.getSafePhone(agentNo);
			if(StringUtils.isNotBlank(safePhone)){
				safePhone = safePhone.substring(0, 3) + "*****" + safePhone.substring(8, safePhone.length());
			}
			//判断是否设置资金密码
			AgentInfo enittyInfo = agentInfoService.selectByPrincipal();
			if (StringUtils.isNotBlank(enittyInfo.getSafePassword())){
				hasSafePassword = true;
			}
			msg.put("safephone", safePhone);
			msg.put("hasSafePassword", hasSafePassword);
		} catch (Exception e){
			log.error("查询安全手机失败!");
			msg.put("status", false);
		}
		return msg;
	}

	private boolean hasSafePhone(){
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String agentNo = principal.getUserEntityInfo().getEntityId();
		return  StringUtils.isNotBlank(agentInfoService.getSafePhone(agentNo));
	}
	
	
	private boolean isImgNum(String imgNum) {
		if(StringUtils.isBlank(imgNum) || imgNum.length()!=4){
			return false;
		}
		return true;
	}

	public static boolean isPhone(String str) {
		if(StringUtils.isBlank(str)){
			return false;
		}
		String regex = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$";
		return match(regex, str);
		}
	
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}
	
	@SystemLog(description = "保存安全手机")
	@RequestMapping(value = "/savePhone")
	@ResponseBody
	public Map<String, Object> savePhone(@RequestParam("info") String param,HttpServletRequest req) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			CheckNum info = JSONObject.parseObject(param, CheckNum.class);
			HttpSession session = req.getSession();
			String checkphone = (String)session.getAttribute("checkphone");
			String imgcode = (String)session.getAttribute("imgcode");
			String phoneNum = (String)session.getAttribute("phoneNum");
			String phoneNumTime = (String) session.getAttribute("phoneNumTime");
			
			if(StringUtils.isBlank(checkphone)){
				map.put("msg", "请先获取短信验证码");
				return map;
			}
			
			if(StringUtils.isBlank(imgcode) || !imgcode.equals(info.getImgNum())){
				map.put("msg", "图形验证码不匹配");
				return map;
			} 
			if(!checkphone.equals(info.getPhone())){
				map.put("msg", "前后手机不匹配");
				return map;
			}
			//校验手机
			if(!isPhone(info.getPhone())){
				map.put("msg", "手机号码格式不正确");
				return map;
			}
			if(StringUtils.isBlank(phoneNum) || !phoneNum.equals(info.getPhoneNum())){
				map.put("msg", "短信验证码不匹配");
				return map;
			}  
			//校验验证码是否匹配
			if(Long.valueOf(phoneNumTime)-System.currentTimeMillis()<0){
				map.put("msg", "验证码超过5分钟,请重新获取");
				return map;
			}
			//更新
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String agentNo = principal.getUserEntityInfo().getEntityId();
			
			String safePhone = agentInfoService.getSafePhone(agentNo);
			
			//如果存在旧手机
			if(StringUtils.isNotBlank(safePhone)){
				Boolean flag = (Boolean)session.getAttribute("checkPhoneNumTrue");
				String checkPhoneNumTrueTime = (String)session.getAttribute("checkPhoneNumTrueTime");
				
				if(null ==flag || flag !=true){
					map.put("msg", "请校验原手机");
					return map;
				}
				if(Long.valueOf(checkPhoneNumTrueTime)-System.currentTimeMillis()<0){
					map.put("msg", "验证码超过5分钟,请重新获取");
					return map;
				}
			}
			
			int  i = agentInfoService.updateSafePhoneByAgentNo(agentNo,checkphone);
			if(i>0){
				//销毁session
				session.removeAttribute("checkphone");
			    session.removeAttribute("phoneNum");
				session.removeAttribute("phoneNumTime");
				
				map.put("status", true);
				map.put("msg", "安全手机设置已完成");
			}
		} catch (Exception e) {
			log.error("保存安全手机异常!",e);
		}
		return map;
	}
	@SystemLog(description = "保存安全密码")
	@RequestMapping(value = "/updateSafePassword")
	@ResponseBody
	public Map<String, Object> updateSafePassword(@RequestParam("info") String param) {
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("status", true);
		try {
			log.info("保存安全密码所传参数===> {}",param);
			Map<String,String> paramsMap = JSONObject.parseObject(param, Map.class);
			String password = paramsMap.get("password");
			password = password.replaceAll(" ","+");//将空格转为+号
			// 私钥解密password
			password = RSAUtils.decryptDataOnJava(password, Constants.PRIVATE_KEY);
			Pattern pattern = Pattern.compile("\\d{6}");
			if (!pattern.matcher(password).matches()){
				log.info("=====设置安全密码参数长度不为6或包含非数字字符,非法操作=====");
				resultMap.put("msg", "请输入6位数字密码");
				resultMap.put("status", false);
				return resultMap;
			}
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			AgentInfo agentInfo = agentInfoService.selectByPrincipal();
			if (StringUtil.isBlank(agentInfo.getSafephone())){
				resultMap.put("msg", "请先设置安全手机号码");
				resultMap.put("status", false);
				return resultMap;
			}
			//查询是否设置原密码
			if (StringUtils.isNotBlank(agentInfo.getSafePassword())){
				String oldPassword = paramsMap.get("oldPassword");
				oldPassword = oldPassword.replaceAll(" ","+");//将空格转为+号
				// 私钥解密password
				oldPassword = RSAUtils.decryptDataOnJava(oldPassword, Constants.PRIVATE_KEY);
				//大佬要求改成代理商编号加密
				oldPassword = Md5.md5Str(oldPassword + "{" + principal.getUserEntityInfo().getEntityId() + "}");
				if (!(agentInfo.getSafePassword()).equals(oldPassword)){
					resultMap.put("msg", "原始密码不正确");
					resultMap.put("status", false);
					return resultMap;
				}
			}
			if (agentInfoService.updateSafePassword(password, principal) > 0){
				resultMap.put("msg", "安全密码保存成功");
				return resultMap;
			}
			resultMap.put("status", false);
			resultMap.put("msg", "密码保存出错");
			return resultMap;
		} catch (Exception e) {
			log.error("保存安全密码异常!",e);
			resultMap.put("msg", "保存安全密码异常");
			resultMap.put("status", false);
			return resultMap;
		}
	}




	@RequestMapping("/happyBackNotFullDeductDetail")
	@ResponseBody
	public Result happyBackNotFullDeductDetail(String info) {
		Result result = new Result();
		HappyBackNotFullDeductDetailQo qo = JSON.parseObject(info, HappyBackNotFullDeductDetailQo.class);
		try {
			HappyBackNotFullDeductDetail detail = happyBackNotFullDeductService.queryHappyBackNotFullDeductDetail(qo);
			result.setData(detail);
			result.setStatus(true);
		} catch (Exception e) {
			result.setStatus(false);
			log.error(e.getMessage());
		}
		return result;
	}
	
	@RequestMapping("/happyBackNotFullDeductDetailExport")
	public void happyBackNotFullDeductDetailExport(String info ,HttpServletResponse response) {
		HappyBackNotFullDeductDetailQo qo = JSON.parseObject(info, HappyBackNotFullDeductDetailQo.class);
		List<HappyBackNotFullDeductDetailList> list  =  happyBackNotFullDeductService.exportHappyBackNotFullDeductDetailQuery(qo);
		if (list == null) {
			list = new ArrayList<>();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		String fileName = "欢乐返不满扣明细"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = null;
		try {
			fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>();
		for (HappyBackNotFullDeductDetailList detail : list) {
			HashMap<String, String> map = new HashMap<>();
			map.put("shouldDebtAmount", detail.getShouldDebtAmount().toString());
			map.put("debtAmount", detail.getDebtAmount().toString());
			map.put("adjustAmount", detail.getAdjustAmount().toString());
			map.put("agentNo", detail.getAgentNo());
			map.put("agentName", detail.getAgentName());
			map.put("agentName", detail.getAgentName());
			map.put("debtTime", DateUtils.format(detail.getDebtTime(), DateUtils.DATATIMEFORMAT));
			map.put("orderNo", detail.getOrderNo());
			
			data.add(map);
			
		}
		BigExcel export = new BigExcel();
		String[] cols = new String[]{"shouldDebtAmount","debtAmount","adjustAmount","agentNo","agentName",
				"debtTime","orderNo"};
		String[] colsName = new String[]{"应扣款金额(元)","实际扣款金额(元)","累计待扣款金额(元)","扣款代理商编号","扣款代理商名称",
				"日期","欢乐返订单号"};
		OutputStream ouputStream;
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, ouputStream);
			ouputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SystemLog(description = "三要素验证")
	@RequestMapping(value = "/cardAuth3")
	@ResponseBody
	public Map<String, Object> cardAuth3(@RequestBody String param) throws Exception {
		Map<String,Object> map = new HashMap<>();
		map.put("status", false);
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String key = "card:auth:username"+principal.getUsername();
			JSONObject json = JSON.parseObject(param);
			AgentInfo info = JSONObject.parseObject(json.getString("info"), AgentInfo.class);
			String cardNum=info.getAccountNo();
			String userName=info.getAccountName();
			String idCard=info.getIdCardNo();
			Integer num = 0;
			//判断卡bin
			Map bank=posCardBinService.queryAcpBearBankByBankName(info.getBankName(),0);
			if(bank!=null){
				List<String> str=posCardBinService.queryAcpBearBankNameByType(1);
				map.put("isRepeatStatus", false);
				map.put("isRepeatStatus2", true);
				map.put("bankNames", StringUtils.join(str, ","));
				map.put("msg", "不支持该银行，请重新提交");
				return map;
			}
			if ("true".equals(RealAuthUtil.sendMsg(cardNum, userName, idCard, null))) {
				agentInfoService.updateAgentByIdCardNo(idCard,info.getAgentNo());
				List<String> keys = new ArrayList<String>();
				keys.add(key);
				redisService.delete(keys);
				map.put("isRepeatStatus", false);
				map.put("isRepeatStatus2", false);
				map.put("subSize", num);
				map.put("status", true);
				map.put("msg", "三要素认证成功");
			}else{
				String CARD_AUTH_TIME=sysDictService.getStringValueByKey("CARD_AUTH_TIME");
				if(redisService.exists(key)){
					num = Integer.valueOf(redisService.select(key).toString())+1;
				}else{
					num = num + 1;
				}
				redisService.insertString(key, num.toString(), Long.valueOf(CARD_AUTH_TIME));//7天
				if(num>=3){
					map.put("isRepeatStatus", true);
					map.put("isRepeatStatus2", false);
				}else{
					map.put("isRepeatStatus", false);
					map.put("isRepeatStatus2", false);
				}
				map.put("subSize", num);
				map.put("status", false);
				map.put("msg", "三要素认证失败");
			}
		} catch (Exception e) {
			log.error("三要素认证异常!",e);
			map.put("status", false);
			map.put("msg", "三要素认证异常");
		}
		return map;
	}

	@RequestMapping("/sendForgotSafePasswordSmsCode/{imageId}/{imageCode}")
	public ResponseBean sendForgotSafePasswordSmsCode(@PathVariable String imageId,
													  @PathVariable String imageCode) throws Exception {
		if (StringUtils.isBlank(imageCode)){
			return new ResponseBean("请输入图形验证码");
		}
		String redisKey = String.format("agentWeb2:imageCode:forgotSafePassword:%s", imageId);
		Object redisValue = redisService.select(redisKey);
		if (redisValue == null || !StringUtils.equalsIgnoreCase(Objects.toString(redisValue), imageCode)){
			return new ResponseBean("图形验证码输入有误.");
		}
		redisService.delete(Collections.singletonList(redisKey));
		AgentInfo agentInfo = agentInfoService.selectByPrincipal();
		if (agentInfo == null || StringUtils.isBlank(agentInfo.getSafephone())) {
			return new ResponseBean("请选择设置资金安全手机");
		}
		String smsCode = RandomStringUtils.random(6, "0123456789");
		Sms.sendMsg(agentInfo.getSafephone(), String.format("验证码：%s。您正在找回资金安全密码，如非本人操作请及时登录后台查看。", smsCode));
		log.info("找回资金安全密码验证码为 {}", smsCode);
        redisService.insertString(String.format("agentweb2:smsCode:forgotSafePassword:%s", agentInfo.getAgentNo()), smsCode, 600L);
        return new ResponseBean("短信验证码已经下发", true);
	}

    @SystemLog(description = "找回资金安全密码")
    @RequestMapping("/findBackForgotSafePassword/{smsCode}")
    public ResponseBean findBackForgotSafePassword(@PathVariable String smsCode, @RequestParam String password) throws Exception {
        if (StringUtils.isBlank(smsCode)) {
            return new ResponseBean("请输入短信验证码");
        }
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AgentInfo agentInfo = agentInfoService.selectByPrincipal();
        if (agentInfo == null || StringUtils.isBlank(agentInfo.getSafephone())) {
            return new ResponseBean("请选择设置资金安全手机");
        }
        String redisKey = String.format("agentweb2:smsCode:forgotSafePassword:%s", agentInfo.getAgentNo());
        Object redisValue = redisService.select(redisKey);
        if (redisValue == null || !StringUtils.equalsIgnoreCase(Objects.toString(redisValue), smsCode)) {
            return new ResponseBean("短信验证码输入有误.");
        }
        redisService.delete(Collections.singletonList(redisKey));
        password = password.replaceAll(" ","+");//将空格转为+号
        // 私钥解密password
        password = RSAUtils.decryptDataOnJava(password, Constants.PRIVATE_KEY);
        if (!password.matches("\\d{6}")) {
            return new ResponseBean("请输入6位数字密码");
        }
        boolean result = agentInfoService.updateSafePassword(password, principal) > 0;
        return new ResponseBean(result ? "修改成功" : "修改失败", result);
    }
}


