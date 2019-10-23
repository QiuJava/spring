package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.*;
import cn.eeepay.framework.daoPerAgent.PerAgentDao;
import cn.eeepay.framework.daoSuperBank.OrgInfoDao;
import cn.eeepay.framework.daoSuperBank.RedEnvelopesDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.encryptor.md5.Md5;
import cn.eeepay.framework.exception.AgentWebException;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.model.peragent.PaChangeLog;
import cn.eeepay.framework.service.*;
import cn.eeepay.framework.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWTSigner;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("agentInfoService")
public class AgentInfoServiceImpl implements AgentInfoService {
	private static final Logger log = LoggerFactory.getLogger(AgentInfoServiceImpl.class);
	private static final Pattern pattern = Pattern.compile("^(\\d+(\\.\\d+)?)~(\\d+(\\.\\d+)?)%~(\\d+(\\.\\d+)?)$");
	private static final DecimalFormat format = new DecimalFormat("0.00");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Resource
	private AgentInfoDao agentInfoDao;
	@Resource
	private ServiceDao serviceDao;
	@Resource
	private OrgInfoDao orgInfoDao;
	@Resource
	private RedEnvelopesDao redEnvelopesDao;
	@Resource
	private AgentBusinessProductDao agentBusinessProductDao;
	@Resource
	private BusinessProductDefineDao businessProductDefineDao;
	@Resource
	private BusinessProductDefineService businessProductDefineService;
	@Resource
	private ServiceProService serviceProService;
	@Resource
	private UserInfoDao userInfoDao;
	@Resource
	private SeqService seqService;
	@Resource
	private PaChangeLogService paChangeLogService;
	@Resource
	private SysDictDao sysDictDao;
	@Resource
	private ProviderDao providerDao;
	@Resource
	private PerAgentDao perAgentDao;
	@Resource
	private AgentInfoServiceImpl agentInfoService;
	@Resource
	private SysDictService sysDictService;
	@Resource
	private TerminalInfoDao terminalInfoDao;

	@Resource
	private OpenPlatformService openPlatformService;

	@Override
	public AgentInfo selectByName(String name) {
		return agentInfoDao.selectByName(name);
	}

	@Override
	public List<AgentInfo> selectAllInfo(String agentNo) {
		return agentInfoDao.selectAllInfo(agentNo);
	}

	@Override
	public AgentInfo selectByagentNo(String agentNo) {
		AgentInfo agent = agentInfoDao.selectByAgentNo(agentNo);
		if (agent != null) {
			if (StringUtils.isNotBlank(agent.getClientLogo())) {
				String logo = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, agent.getClientLogo(),
						new Date(new Date().getTime() + 3600000));
				agent.setClientLogoLink(logo);
			}
			if (StringUtils.isNotBlank(agent.getManagerLogo())) {
				String logo = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, agent.getManagerLogo(),
						new Date(new Date().getTime() + 3600000));
				agent.setManagerLogo(logo);
			}
		}
		return agent;
	}

	@Override
	public Map<String, Object> getAgentServices(Map<String, Object> json) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String agentId = (String) json.get("agentId");
			@SuppressWarnings("unchecked")
			List<Integer> ids = (List<Integer>) json.get("bpIds");
			if (StringUtils.isNotBlank(agentId)) {
				result.put("rates", this.getServiceRate(ids, agentId));
			}
		} catch (Exception e) {
			log.error("查询代理商的代理业务产品对应的所有的服务费率和服务额度异常！", e);
		}
		return result;
	}

	/**
	 * 查询出代理商的费率
	 */
	@Override
	public List<ServiceRate> getServiceRate(List<Integer> bpIds, String agentId) {
		if (bpIds == null || bpIds.isEmpty()) {
			return null;
		}
		List<ServiceRate> list = agentInfoDao.getServiceRate(bpIds, agentId);

		for (ServiceRate r : list) {
			r.setMerRate(serviceProService.profitExpression(r));
		}
		return list;
	}

	/**
	 * 保存下级代理商
	 */
	@Override
	public void saveAgentInfo(JSONObject json) {
		if (json.getJSONArray("bpData").isEmpty()) {
			throw new AgentWebException("请选择业务产品！");
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		// mxg
		AgentInfo agent = JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
		if (agent.getAccountType() == 2) {
			Map<String, String> maps = openPlatformService.doAuthen(agent.getAccountNo(), agent.getAccountName(),
					agent.getIdCardNo(), null);
			boolean flag = "00".equalsIgnoreCase(maps.get("errCode"));
			if (!flag) {
				log.info("身份证验证失败");
				throw new AgentWebException("开户名、身份证、银行卡号不匹配!");
			}
		}
		AgentInfo parentAgent = agentInfoDao.selectByAgentNo(entityId);
		String agentNo = seqService.createKey("agent_no");
		String userCode = agent.getUserCode();
		PaUserInfo paUserInfo = perAgentDao.selectByUserCode(userCode);
		PaUserInfo entityPaUserInfo = perAgentDao.selectUserByAgentNo(entityId);
		// 保存代理商基本信息
		saveAgentBaseInfo(principal, parentAgent, agent, agentNo);
		// 保存代理商业务产品信息
		List<Integer> bps = saveAgentBpIds(json, agent, agentNo);
		// 保存代理商分润信息
		saveAgentShareRule(json, parentAgent, agent, agentNo, bps);
		// 创建代理商管理员
		saveAgentUserInfo(agent, parentAgent, paUserInfo);
		// 开设代理商账户
		openAgentAccount(agentNo);

		// 升级成大盟主,代理商编号取盟主代理商编号
		if ("11".equals(parentAgent.getAgentType()) && StringUtils.isNotBlank(userCode)) {// 才是升级大盟主的操作
			/**
			 *
			 * pa_agent_user 新增一条 代理商 盟主对应关系记录 超级盟主 pa_mer_info 代理商编号 节点 pa_trans_info 代理商编号
			 * 代理商节点 pa_order 表 发货方 is_platform 字段 pa_user_info 代理商编号 节点 pa_user_card
			 * 判断是否有数据,如果没有,则插入一条 pa_user_upgrade 插入一条数据
			 * 
			 * agent_info 代理商表 新增一条 V2系统 merchant_info 商户表 collective_trans_order 订单表
			 * activity_detail 活动表 terminal_info 机具表 pa_ter_info 代理商编号,节点
			 */
			String agentNode = parentAgent.getAgentNode() + agentNo + "-";
			String userNode = paUserInfo.getUserNode();
			log.info("======== 代理商编号  agentNo = " + agentNo + "; 用户编号  userCode = " + userCode + " ========");
			log.info("pa_agent_user   表插入 " + perAgentDao.insertPaAgentUser(userCode, agentNo, agentNode) + " 条数据===");
			log.info("pa_mer_info     表更新 " + perAgentDao.updatePaMerInfo(userNode, agentNo, agentNode) + " 条数据===");
			PaChangeLog paChangeLog = new PaChangeLog("", "agent_no=" + agentNo + ",agent_node=" + agentNode,
					"升级成大盟主时更新pa_mer_info表", "代理商系统，代理商编号：" + parentAgent.getAgentNo(), "saveAgentInfo");
			paChangeLogService.insert(paChangeLog);
			log.info("pa_trans_info   表更新 " + perAgentDao.updatePaTransInfo(userNode, agentNo, agentNode) + " 条数据===");
			log.info("pa_order        表更新 " + perAgentDao.updatePaOrder(userCode) + " 条数据===");
			log.info("pa_user_info    表更新 "
					+ perAgentDao.updatePaUserInfoByUserNode(userNode, agentNo, agentNode, userCode) + " 条数据===");
			log.info("pa_user_info    表更新 " + perAgentDao.updatePaUserInfoByUserCode(userCode)
					+ " 条数据===更新所升级的状态为大盟主===");
			log.info("pa_user_upgrade 表插入 " + perAgentDao.insertPaUserUpgrade(userCode, entityPaUserInfo.getUserCode())
					+ " 条数据===");

			List<PaMerInfo> paMerInfoList = perAgentDao.selectPaMerInfo(userNode);
			log.info("pa_mer_info  查询到  " + paMerInfoList.size() + " 条商户信息===");
			Integer i = 0;
			Integer j = 0;
			Integer k = 0;
			Integer h = 0;
			for (PaMerInfo paMerInfo : paMerInfoList) {
				String merchantNo = paMerInfo.getMerchantNo();
				i += agentInfoDao.updateMerchantInfo(agentNo, agentNode, merchantNo);
				j += agentInfoDao.updateCollectiveTransOrder(agentNode, merchantNo);
				k += agentInfoDao.updateActivityDetail(agentNode, merchantNo);
				h += terminalInfoDao.updateByMerchantNo(agentNo, agentNode, merchantNo);
			}
			log.info("merchant_info 表更新 " + i + " 条数据 === collective_trans_order 表更新 " + j + " 条数据 === "
					+ "activity_detail 表更新 " + k + " 条数据=== terminal_info 表更新" + h + "条数据====");
			List<PaUserInfo> list = perAgentDao.selectByUserNode(userNode);
			Integer num = 0;
			for (PaUserInfo info : list) {
				int result = agentInfoDao.updatePaTerInfoAndTerminalInfo(info.getUserCode(), agentNo, agentNode);
				num += result;
				/*
				 * if(result > 0){ PaChangeLog changeLog = new
				 * PaChangeLog("","agent_no="+agentNo,
				 * "升级大盟主时更改pa_ter_info",parentAgent.getAgentNo(),
				 * "updatePaTerInfoAndTerminalInfo"); paChangeLogService.insert(changeLog); }
				 */
			}
			log.info("pa_agent_user 表更新 " + num + " 条数据===");// 两张机具表一起更新

			String mobilephone = agent.getMobilephone();
			String idCardNo = agent.getIdCardNo();
			String realName = agent.getAccountName();
			int countInsert = 0;
			int countUpdate = 0;
			if (perAgentDao.selectPaUserCardByUserCode(userCode) == null) {
				// 保存数据到pa_user_card,根据user_code修改pa_user_info表
				PeragentCard cardInfo = new PeragentCard();
				cardInfo.setUserCode(userCode);
				cardInfo.setBankName(agent.getBankName());
				cardInfo.setBankBranchName(agent.getBankName());
				cardInfo.setAccount(agent.getAccountNo());
				cardInfo.setMobile(mobilephone);
				cardInfo.setCnaps(agent.getCnapsNo().toString());
				cardInfo.setAddress(agent.getCity() + agent.getProvince() + agent.getArea());
				cardInfo.setAddress(agent.getAddress());
				cardInfo.setCardType("借记卡");
				cardInfo.setIsSettle(1);
				cardInfo.setIdCardNo(idCardNo);
				cardInfo.setRealName(realName);
				countInsert = perAgentDao.insertIdCardNo(cardInfo);
				log.info("====插入pa_user_card 表== " + countInsert + " ==条数据=====");
			} else {
				countInsert = perAgentDao.updatePaUserCard(agent);
				log.info("====更新 pa_user_card 表== " + countInsert + " ==条数据=====");
			}
			if (!mobilephone.equals(paUserInfo.getMobile())) {
				String pwd = new Md5PasswordEncoder().encodePassword(mobilephone.substring(5, 11), mobilephone);
				countUpdate = perAgentDao.updatePaUserInfo(realName, idCardNo, mobilephone, pwd, userCode);
			} else {
				countUpdate = perAgentDao.updatePaUserInfoForInsert(realName, idCardNo, userCode);
			}
			log.info("====更新pa_user_info表== " + countUpdate + " ==条数据=====");
			if (countInsert < 1 || countUpdate < 1) {
				throw new AgentWebException("新增失败!");
			}
		} else {
			// 保存人人代理信息,登录代理商是人人代理用户才需要保存
			if ("11".equals(parentAgent.getAgentType())) {
				savePerAgent(parentAgent, agent, agentNo);
			} else {
				// 保存欢乐返活动配置t 非人人代理才需要保存
				String stringList = json.getString("happyBackList");
				if (StringUtils.isNotBlank(stringList)) {
					List<AgentActivity> happyBackList = JSONArray.parseArray(stringList, AgentActivity.class);
					if (happyBackList.size() > 0) {// 为空不保存
						saveHappyBack(happyBackList, parentAgent, agentNo);
					}
				}
			}
		}
	}

	/**
	 * 保存人人代理相关信息,调用perAgent系统接口 tgh
	 *
	 * @param parentAgent 当前登录代理商
	 * @param agent       页面传过来的新增代理商信息
	 * @param agentNo     生成的新增代理商编号
	 */
	private void savePerAgent(AgentInfo parentAgent, AgentInfo agent, String agentNo) {
		log.info("=====进入保存人人代理相关信息==================");
		String idCardNo = agent.getIdCardNo();
		/*
		 * Integer count = perAgentDao.selectByIdCardNo(idCardNo); if (count > 0) {
		 * throw new AgentWebException("填写的身份证号码已经存在，一个身份证号码只能开通一个账号"); }
		 */
		Map<String, String> map = perAgentDao.selectByAgentNo(parentAgent.getAgentNo());
		if (map == null) {
			log.info("根据当前登录代理商编号在人人代理pa_agent_user表中没查询到数据");
			throw new AgentWebException("数据异常,请联系客服");
		}
		String parentUserCode = map.get("user_code");
		log.info("根据当前登录代理商编号查出人人代理parentUserCode = " + parentUserCode);
		parentAgent.setUserCode(parentUserCode);

		String resultMsg = ClientInterface.savePerAgent(parentAgent, agent, agentNo);
		log.info("=====调注册人人代理接口返回:" + resultMsg + "======");
		if (StringUtils.isNotBlank(resultMsg)) {
			JSONObject jsonResult = JSON.parseObject(resultMsg);
			if (!"200".equals(jsonResult.getString("status"))) {
				String msg = jsonResult.getString("msg");
				log.info("=====调注册人人代理接口返回==" + msg + "状态不等于200,等于:" + jsonResult.getString("status") + "===");
				throw new AgentWebException(msg);
			}
			// 保存数据到pa_user_card,根据user_code修改pa_user_info表
			PeragentCard cardInfo = jsonResult.getObject("data", PeragentCard.class);
			String userCode = cardInfo.getUserCode();
			cardInfo.setUserCode(userCode);
			cardInfo.setBankName(agent.getBankName());
			cardInfo.setBankBranchName(agent.getBankName());
			cardInfo.setAccount(agent.getAccountNo());
			cardInfo.setMobile(agent.getMobilephone());
			cardInfo.setCnaps(agent.getCnapsNo().toString());
			cardInfo.setAddress(agent.getCity() + agent.getProvince() + agent.getArea());
			cardInfo.setAddress(agent.getAddress());
			cardInfo.setCardType("借记卡");
			cardInfo.setIsSettle(1);
			cardInfo.setIdCardNo(idCardNo);
			cardInfo.setRealName(agent.getAccountName());
			int i = perAgentDao.insertIdCardNo(cardInfo);
			log.info("====插入pa_user_card表==" + i + "==条数据=====");
			int j = perAgentDao.updatePaUserInfoForInsert(cardInfo.getRealName(), idCardNo, userCode);
			log.info("====更新pa_user_info表==" + j + "==条数据=====");
			if (i < 1 || j < 1) {
				throw new AgentWebException("新增失败!");
			}
		} else {
			log.info("=====接口返回 null =====");
			throw new AgentWebException("提交失败!");
		}
	}

	private int fullPrizeNotFullDeductAmount(List<AgentActivity> happyBackList) {
		int isNull = 0;
		int isNotNull = 0;
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
			BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
			BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
			BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
			if (fullPrizeAmount == null && notFullDeductAmount == null && repeatFullPrizeAmount == null
					&& repeatNotFullDeductAmount == null) {
				isNull++;

			}
			if (fullPrizeAmount != null && notFullDeductAmount != null && repeatFullPrizeAmount != null
					&& repeatNotFullDeductAmount != null) {
				isNotNull++;
			}
		}
		if (isNull != happyBackList.size() && isNotNull != happyBackList.size()) {
			throw new AgentWebException("请正确的配置满奖不满扣金额");
		}
		return isNull;
	}

	private int fullPrizeAmount(List<AgentActivity> happyBackList) {
		int isNull = 0;
		int isNotNull = 0;
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
			BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
			if (fullPrizeAmount == null && repeatFullPrizeAmount == null) {
				isNull++;

			}
			if (fullPrizeAmount != null && repeatFullPrizeAmount != null) {
				isNotNull++;
			}
		}
		if (isNull != happyBackList.size() && isNotNull != happyBackList.size()) {
			throw new AgentWebException("请正确的配置满奖金额");
		}
		return isNull;
	}

	private int notFullDeductAmount(List<AgentActivity> happyBackList) {
		int isNull = 0;
		int isNotNull = 0;
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
			BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
			if (notFullDeductAmount == null && repeatNotFullDeductAmount == null) {
				isNull++;

			}
			if (notFullDeductAmount != null && repeatNotFullDeductAmount != null) {
				isNotNull++;
			}
		}
		if (isNull != happyBackList.size() && isNotNull != happyBackList.size()) {
			throw new AgentWebException("请正确的配置不满扣金额");
		}
		return isNull;
	}

	private int saveHappyBack(List<AgentActivity> happyBackList, AgentInfo parentAgent, String agentNo) {
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
			if (fullPrizeAmount == null) {
				agentActivity.setFullPrizeAmount(BigDecimal.ZERO);
			}
			BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
			if (notFullDeductAmount == null ) {
				agentActivity.setNotFullDeductAmount(BigDecimal.ZERO);
			}
			BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
			if (repeatFullPrizeAmount == null) {
				agentActivity.setRepeatFullPrizeAmount(BigDecimal.ZERO);
			}
			BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
			if (repeatNotFullDeductAmount == null ) {
				agentActivity.setRepeatNotFullDeductAmount(BigDecimal.ZERO);
			}
		}
		AgentInfo curInfo = this.getCurAgentInfo();
		// 获取当前代理商支持的层级
		SupportRankDto supportRank = this.getSupportRank(curInfo.getAgentOem());
		Integer fullPrizeLevel = supportRank.getFullPrizeLevel();
		Integer notFullDeductLevel = supportRank.getNotFullDeductLevel();
		Integer agentLevel = curInfo.getAgentLevel();

		if (fullPrizeLevel >= agentLevel && notFullDeductLevel >= agentLevel) {
			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				this.fullPrizeNotFullDeductAmount(happyBackList);
			} else if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 0) {
				this.fullPrizeAmount(happyBackList);
			} else if (curInfo.getFullPrizeSwitch() == 0 && curInfo.getNotFullDeductSwitch() == 1) {
				this.notFullDeductAmount(happyBackList);
			}
		} else if (fullPrizeLevel >= agentLevel && notFullDeductLevel < agentLevel) {// 只支持满奖

			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				this.fullPrizeAmount(happyBackList);
			} else if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 0) {
				this.fullPrizeAmount(happyBackList);
			}
		} else if (fullPrizeLevel < agentLevel && notFullDeductLevel >= agentLevel) {// 只支持不满扣
			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				this.notFullDeductAmount(happyBackList);
			} else if (curInfo.getFullPrizeSwitch() == 0 && curInfo.getNotFullDeductSwitch() == 1) {
				this.notFullDeductAmount(happyBackList);
			}
		}
		// 保存到agent_activity
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal taxRate = agentActivity.getTaxRate();
			BigDecimal cashBackAmount = agentActivity.getCashBackAmount();
			BigDecimal repeatRegisterAmount = agentActivity.getRepeatRegisterAmount();
			BigDecimal repeatRegisterRatio = agentActivity.getRepeatRegisterRatio();
			if (cashBackAmount == null || taxRate == null) {
				throw new AgentWebException("返现金额和返现比例不能为空！");
			}
			if (repeatRegisterAmount == null || repeatRegisterRatio == null) {
				throw new AgentWebException("重复注册返现金额和重复注册返现比例不能为空！");
			}
			String activityTypeNo = agentActivity.getActivityTypeNo();
			Map<String, Object> mapParentAgent = selectByActivityTypeNo(activityTypeNo);
			if (mapParentAgent == null) {
				mapParentAgent = selectDefaultStatus();
			}
			BigDecimal cashBackAmountParentAgent = mapParentAgent.get("cash_back_amount") == null ? new BigDecimal("0")
					: new BigDecimal(mapParentAgent.get("cash_back_amount").toString());
			BigDecimal taxRateParentAgent = mapParentAgent.get("tax_rate") == null ? new BigDecimal("1")
					: new BigDecimal(mapParentAgent.get("tax_rate").toString());// 需求要求如果一级代理商没有设置,默认为100%
			BigDecimal repeatRegisterAmountParentAgent = mapParentAgent.get("repeat_register_amount") == null
					? new BigDecimal("0")
					: new BigDecimal(mapParentAgent.get("repeat_register_amount").toString());
			BigDecimal repeatRegisterRatioParentAgent = mapParentAgent.get("repeat_register_ratio") == null
					? new BigDecimal("1")
					: new BigDecimal(mapParentAgent.get("repeat_register_ratio").toString());// 需求要求如果一级代理商没有设置,默认为100%
			if ((cashBackAmount.multiply(taxRate).divide(new BigDecimal("100")))
					.compareTo(cashBackAmountParentAgent.multiply(taxRateParentAgent)) == 1) {
				throw new AgentWebException("下级代理商的返现不得高于上级代理商的返现!");
			}
			if ((repeatRegisterAmount.multiply(repeatRegisterRatio).divide(new BigDecimal("100")))
					.compareTo(repeatRegisterAmountParentAgent.multiply(repeatRegisterRatioParentAgent)) == 1) {
				throw new AgentWebException("下级代理商的重复返现不得高于上级代理商的重复返现!");
			}
//			if (agentActivity.getTaxRate() == null) {//一级代理商的没有设置税额百分比，默认为100%
//				agentActivity.setTaxRate(new BigDecimal("100"));
//			}
			agentActivity.setRepeatRegisterRatio(repeatRegisterRatio.divide(new BigDecimal("100")));
			agentActivity.setTaxRate(taxRate.divide(new BigDecimal("100")));
			agentActivity.setAgentNo(agentNo);
			agentActivity.setAgentNode(parentAgent.getAgentNode() + agentNo + "-");

			// 拿到上级的满奖不满扣金额
			AgentActivity parent = agentInfoDao.findAgentActivityByParentAndType(parentAgent.getAgentNo(),
					activityTypeNo);
			if (parent == null ) {
				continue;
			}
			BigDecimal parentFullPrizeAmount = parent.getFullPrizeAmount();
			BigDecimal parentNotFullDeductAmount = parent.getNotFullDeductAmount();
			BigDecimal parentRepeatFullPrizeAmount = parent.getRepeatFullPrizeAmount();
			BigDecimal parentRepeatNotFullDeductAmount = parent.getRepeatNotFullDeductAmount();
			BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
			BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
			BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
			BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
			if (fullPrizeAmount != null && parentFullPrizeAmount != null && fullPrizeAmount.compareTo(parentFullPrizeAmount) == 1) {
				throw new AgentWebException("首次注册满奖金额需 ≤ " + parentFullPrizeAmount + "元");
			}
			if (notFullDeductAmount != null && parentNotFullDeductAmount != null  && notFullDeductAmount.compareTo(parentNotFullDeductAmount) == 1) {
				throw new AgentWebException("首次注册不满扣金额需 ≤ " + parentNotFullDeductAmount + "元");
			}
			if (repeatFullPrizeAmount != null && parentRepeatFullPrizeAmount != null && repeatFullPrizeAmount.compareTo(parentRepeatFullPrizeAmount) == 1) {
				throw new AgentWebException("重复注册满奖金额需 ≤ " + parentRepeatFullPrizeAmount + "元");
			}
			if (repeatNotFullDeductAmount != null && parentRepeatNotFullDeductAmount != null
					&& repeatNotFullDeductAmount.compareTo(parentRepeatNotFullDeductAmount) == 1) {
				throw new AgentWebException("重复注册不满扣金额需 ≤ " + parentRepeatNotFullDeductAmount + "元");
			}
			if (parentFullPrizeAmount == null ) {
				agentActivity.setFullPrizeAmount(null);
			}
			if(parentNotFullDeductAmount == null) {
				agentActivity.setNotFullDeductAmount(null);
			}
			if (parentRepeatFullPrizeAmount == null ) {
				agentActivity.setRepeatFullPrizeAmount(null);
			}
			if (parentRepeatNotFullDeductAmount == null ) {
				agentActivity.setRepeatNotFullDeductAmount(null);
			}

		}
		return agentInfoDao.insertAgentActivity(happyBackList);
	}

	/**
	 * 开代理商默认账户
	 */
	private void openAgentAccount(final String agentNo) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(50);
					// 不需要传科目号
					String acc = ClientInterface.createAgentAccount(agentNo, "224105");
					log.info("开立代理商账户(224105) --> " + acc);
//					String acc2 = ClientInterface.createAgentAccount(agentNo, "224106");
//                    log.info("开立代理商账户(224106) --> " + acc2);
					agentInfoDao.updateAgentAccount(agentNo, 1);
				} catch (Exception e) {
					log.error("开立代理商账户异常", e);
				}
			}
		}).start();
	}

	/**
	 * 保存代理用户信息
	 */
	private void saveAgentUserInfo(AgentInfo agent, AgentInfo entityInfo, PaUserInfo paUserInfo) {
		AgentUserInfo agentUser = agentInfoDao.selectAgentUser(agent.getMobilephone(), agent.getTeamId().toString());
		if (agentUser == null) {
			agentUser = new AgentUserInfo();
			agentUser.setUserName(agent.getAgentName());
			String userId = seqService.createKey(Constants.AGENT_USER_SEQ, new BigInteger("1000000000000000000"));
			agentUser.setUserId(userId);
			agentUser.setTeamId(agent.getTeamId().toString());
			String mobilephoneParam = agent.getMobilephone();
			if ("11".equals(entityInfo.getAgentType())) {
				String mobile = "";
				if (paUserInfo != null) {
					mobile = paUserInfo.getMobile();
				}
				if (mobilephoneParam.equals(mobile)) {
					agentUser.setMobilephone(paUserInfo.getMobile());
					agentUser.setPassword(paUserInfo.getPwd());
				} else {
					agentUser.setMobilephone(mobilephoneParam);
					agentUser.setPassword(new Md5PasswordEncoder().encodePassword(mobilephoneParam.substring(5, 11),
							mobilephoneParam));
				}
			} else {
				agentUser.setMobilephone(mobilephoneParam);
				agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456", mobilephoneParam));
			}
			agentUser.setEmail(agent.getEmail());
			agentInfoDao.insertAgentUser(agentUser);
		}
		AgentUserEntity entity = agentInfoDao.selectAgentUserEntity(agentUser.getUserId(), agent.getAgentNo());
		if (entity == null) {
			entity = new AgentUserEntity();
			entity.setEntityId(agent.getAgentNo());
			entity.setUserId(agentUser.getUserId());
			entity.setIsAgent("1");
			agentInfoDao.insertAgentEntity(entity);
			// agentInfoDao.insertAgentRole(entity.getId());
		} else {
			throw new AgentWebException("代理商手机号已注册");
		}
	}

	/**
	 * 保存代理商分润信息
	 */
	private void saveAgentShareRule(JSONObject json, AgentInfo parentAgent, AgentInfo agent, String agentNo,
			List<Integer> bps) {
		List<AgentShareRule> shareList = JSONArray.parseArray(json.getString("shareData"), AgentShareRule.class);
		if (shareList == null || shareList.isEmpty()) { // 302tgh xy
			return;
		}
		List<AgentShareRule> totalShareList = new ArrayList<>();

		for (AgentShareRule rule : shareList) {
			// 不是提现服务的，代理商成本需要加上%
			if (rule.getServiceType() != null && rule.getServiceType() != 10000 && rule.getServiceType() != 10001) {
				if (rule.getCost() != null && !rule.getCost().contains("%")) {
					rule.setCost(rule.getCost() + "%");
				}
			}
			setShareRuleAdd(rule);
			rule.setAgentNo(agentNo);
			rule.setCheckStatus(1);
			rule.setLockStatus(0);
			AgentShareRule parentAgentShareRule = agentInfoDao.getSameTypeParentAgentShare(rule);
			if (parentAgentShareRule == null) {
				log.warn(rule.toString());
				throw new AgentWebException("代理商(" + agent.getParentId() + ")" + rule.getServiceName() + "("
						+ rule.getServiceId() + ")的分润规则没配置");
			}
			isChildrenRuleLessThanParent(parentAgentShareRule, rule);
			compareServiceRate(rule, parentAgent.getOneLevelId(), parentAgent.getAgentNo(), agentNo);

			totalShareList.add(rule);
		}
		log.info("新增代理商时,需要保存的全部分润数据:{}", JSONObject.toJSONString(totalShareList));
		agentInfoDao.insertAgentShareList(totalShareList);
		List<Map<String, Object>> leaderAndMember = agentInfoDao.getLeaderAndMember(agentNo);
		// 如果不为空,说明有对应需要添加.
		if (!CollectionUtils.isEmpty(leaderAndMember)) {
			for (Map<String, Object> temp : leaderAndMember) {
				int leader = Integer.valueOf(StringUtil.filterNull(temp.get("leader")));
				int member = Integer.valueOf(StringUtil.filterNull(temp.get("member")));
				// 此次更新的分润信息如果有包含队长的分润信息,队员才进行修改
				if (bps.contains(leader) && bps.contains(member)) {
                    List<AgentShareRule> agentShareRules = agentInfoDao.listMemberAgentShareByLeader(leader + "", member + "", agentNo);
                    agentInfoDao.insertMemberAgentShare(agentShareRules);
				}
			}
		}

	}

	/**
	 * 保存代理业务产品
	 */
	private List<Integer> saveAgentBpIds(JSONObject json, AgentInfo agent, String agentNo) {
		if (json.getJSONArray("bpData").isEmpty()) {
			throw new AgentWebException("请选择业务产品！");
		}
		List<Integer> bps = JSONArray.parseArray(json.getString("bpData"), Integer.class);
		List<JoinTable> bpList = new ArrayList<>();
		for (Integer id : bps) {
			JoinTable product = new JoinTable();
			product.setKey1(id);
			product.setKey2(1);
			product.setKey3(agentNo);
			bpList.add(product);
		}
		agentInfoDao.insertAgentProductList(bpList);
		agentInfoDao.updateDefaultFlagLeader2On(bps, agent.getAgentNo());
		return bps;
	}

	/**
	 * 新增代理商基本信息
	 */
	private String saveAgentBaseInfo(UserLoginInfo principal, AgentInfo parentAgent, AgentInfo agent, String agentNo) {
//		agent.setTeamId(Integer.valueOf(Constants.TEAM_ID));
		agent.setTeamId(parentAgent.getTeamId());
		if ("11".equals(parentAgent.getAgentType())) {
			checkFourInfomation(principal, agent);
		}
		if (agentInfoDao.existAgentByMobilephoneAndTeamId(agent) > 0) {
			throw new AgentWebException("该组织下的手机号码或者邮箱代理商名称已存在!");
		}
		if (agentInfoDao.existUserByMobilephoneAndTeamId(agent) > 0) {
			log.info("该组织下的手机号码在User_info表已存在!");
			throw new AgentWebException("该组织下的手机号码已被使用!");
		}
		if (StringUtils.isNotBlank(agent.getEmail()) && agentInfoDao.existUserByEmailAndTeamId(agent) > 0) {
			log.info("该组织下的邮箱在User_info表已存在!");
			throw new AgentWebException("该组织下的邮箱已被使用!");
		}

		agent.setAgentNo(agentNo);
		// 节点后面加上“-”
		agent.setAgentNode(parentAgent.getAgentNode() + agentNo + "-");
		agent.setAgentLevel(parentAgent.getAgentLevel() + 1);
		agent.setParentId(principal.getUserEntityInfo().getEntityId());
		agent.setOneLevelId(parentAgent.getOneLevelId());
		agent.setTeamId(parentAgent.getTeamId());
		agent.setIsOem(parentAgent.getIsOem());
		agent.setCountLevel(parentAgent.getCountLevel());
		agent.setIsApprove(parentAgent.getIsApprove());
		agent.setCreator(principal.getUserEntityInfo().getUserId());
		agent.setStatus("1");
		agent.setAgentOem(parentAgent.getAgentOem());
		agent.setAgentType(parentAgent.getAgentType());
		agent.setAgentShareLevel(parentAgent.getAgentShareLevel());
		if ("11".equals(parentAgent.getAgentType())) {// 展策要求人人代理默认代理区域为全国
			agent.setAgentArea("全国");
		}
		if (agentInfoDao.insertAgentInfo(agent) < 1) {
			throw new AgentWebException("保存代理商失败！");
		}
		return agentNo;
	}

	/**
	 * 放开一个身份证可以注册多个,四要素验证
	 *
	 * @param principal
	 * @param agent
	 */
	private void checkFourInfomation(UserLoginInfo principal, AgentInfo agent) {
		// 开放一个身份证号码可以注册多个盟主账号(包含大盟主)，注册的数量由数据字典配置，注册数量的限制不区分品牌,V2也要做
		// checkIdCardNo(agent,parentAgent);
		SysDict dict = sysDictDao.selectDictByKey("PER_REGIST_NUM_" + principal.getTeamId());
		if (dict == null) {// 取默认的
			dict = sysDictDao.selectDictByKey("PER_REGIST_NUM");
		}
		String idCardNo = agent.getIdCardNo();
		AgentInfo info = agentInfoService.selectByagentNo(agent.getAgentNo());
		if ((info != null
				&& (!idCardNo.equals(info.getIdCardNo()) || (!agent.getAccountNo().equals(info.getAccountNo()))
						|| (!agent.getAccountName().equals(info.getAccountName()))))
				|| info == null) {// 修改时,修改了身份证,银行卡号,开户名就作校验,新增时作校验
			// 三要素认证
			if (!"true".equals(RealAuthUtil.sendMsg(agent.getAccountNo(), agent.getAccountName(), idCardNo, null))) {
				throw new AgentWebException("三要素认证失败!");
			}
			// 作校验
			Integer IdRegistTimes = Integer.valueOf(dict.getSysValue()); // 身份证允许注册的次数
			log.info("数据字典配置了同一个身份证可以注册 " + IdRegistTimes + "次=====");
			if (StringUtils.isBlank(idCardNo)) {
				throw new AgentWebException("身份证号不能为空!");
			}
			Integer count = agentInfoDao.selectByIdCardNo(idCardNo);
			Integer countPerAgent = perAgentDao.selectByIdCardNo(idCardNo);
			if (info == null || (!idCardNo.equals(info.getIdCardNo()))) {
				if ("998".equals(principal.getTeamId())) {
					log.info("V2实际已注册 " + count + "次=====超级盟主实际已注册 " + countPerAgent + "次=====");
					if (count >= IdRegistTimes || countPerAgent >= IdRegistTimes) {
						throw new AgentWebException("该身份证号已达注册上限，暂时无法完成认证!");
					}
				} else {
					log.info("V2实际已注册 " + count + "次=====");
					if (count >= IdRegistTimes) {
						throw new AgentWebException("该身份证号已达注册上限，暂时无法完成认证!");
					}
				}
			}
		}
	}

	/**
	 * 校验身份证唯一
	 * 
	 * @param agent
	 */
	/*
	 * private void checkIdCardNo(AgentInfo agent,AgentInfo entityInfo) {
	 * //人人代理需要填身份证号,才需要校验 if ("11".equals(entityInfo.getAgentType())) { if
	 * (StringUtils.isBlank(agent.getIdCardNo())) { throw new
	 * AgentWebException("身份证号不能为空"); }else{ Integer count =
	 * agentInfoDao.selectByIdCardNo(agent.getIdCardNo()); if (count > 0) { throw
	 * new AgentWebException("该身份证号码已存在"); } } } }
	 */

	/**
	 * 分润规则信息跟商户的服务费率进行比较, 分利信息成本不能高于服务费率
	 *
	 * @param rule       分润规则
	 * @param oneLevelId 一级代理商编号
	 * @param agentNo    当前修改的代理商编号
	 */
	public void compareServiceRate(AgentShareRule rule, String oneLevelId, String parentAgentNo, String agentNo) {
		Map<String, Object> serviceRateMap = agentInfoDao.getSameTypeRootAgentMinServiceRate(rule, oneLevelId, agentNo);
		if (serviceRateMap == null || serviceRateMap.size() == 0) {
			log.warn("rule:" + rule);
			log.warn("oneLevelId:" + oneLevelId + ", agentNo:" + agentNo);
			throw new AgentWebException("该代理商的服务费率没有配置,或服务产品已失效");
		}
		String serviceName = MapUtils.getString(serviceRateMap, "service_name");
		String bpName = MapUtils.getString(serviceRateMap, "bp_name");
		BigDecimal rate = new BigDecimal(MapUtils.getString(serviceRateMap, "rate"));
		BigDecimal singleNumAmount = new BigDecimal(MapUtils.getString(serviceRateMap, "single_num_amount"));
		String isTx = MapUtils.getString(serviceRateMap, "isTx");
		if (StringUtils.equals(isTx, "1")) {
			if (rule.getPerFixCost().compareTo(singleNumAmount) > 0) {
				throw new AgentWebException("代理商(" + bpName + "-" + serviceName + ")的分润成本"
						+ rule.getPerFixCost().setScale(4) + " 元高于服务费率 " + singleNumAmount.setScale(4) + "元");
			}

		} else {
			if (rule.getCostRate().compareTo(rate) > 0) {
				throw new AgentWebException("代理商(" + bpName + "-" + serviceName + ")的分润成本"
						+ rule.getCostRate().setScale(4) + "%高于服务费率 " + rate.setScale(4) + "%");
			}

		}
		compareServiceRateNew(rule, oneLevelId, parentAgentNo, agentNo);
	}

	public void compareServiceRateNew(AgentShareRule rule, String oneLevelId, String parentAgentNo, String agentNo) {
		Map<String, Object> serviceRateMap = agentInfoDao.getSameTypeRootAgentMaxServiceRate(rule, oneLevelId, agentNo);
		if (serviceRateMap == null || serviceRateMap.size() == 0) {
			log.warn("rule:" + rule);
			log.warn("oneLevelId:" + oneLevelId + ", agentNo:" + agentNo);
			throw new AgentWebException("该代理商的服务费率没有配置,或服务产品已失效");
		}
		String cardType = MapUtils.getString(serviceRateMap, "card_type");
        String holidaysMark = MapUtils.getString(serviceRateMap, "holidays_mark");
		String isTx = MapUtils.getString(serviceRateMap, "isTx");
		String bpStr = "(" + rule.getBpName() + "-" + rule.getServiceName() + ")";
		BigDecimal rate = new BigDecimal(MapUtils.getString(serviceRateMap, "rate"));
		BigDecimal singleNumAmount = new BigDecimal(MapUtils.getString(serviceRateMap, "single_num_amount"));
		if (StringUtils.equals(isTx, "1")) {
			Map<String, Object> agentShareRuleMap = agentInfoDao.selectAgentShareRule(parentAgentNo,
					rule.getServiceId(), cardType, holidaysMark);
			if (agentShareRuleMap == null) {
				throw new AgentWebException(bpStr + "上级代理商没有配置此产品");
			}
			if (agentShareRuleMap.get("per_fix_cost") == null
					|| agentShareRuleMap.get("share_profit_percent") == null) {
				throw new AgentWebException(bpStr + "上级代理商成本扣率或者分润百分比没有配置");
			}
			BigDecimal aResult = (singleNumAmount.subtract(rule.getPerFixCost()))
					.multiply(rule.getShareProfitPercent());
			BigDecimal parCostRate = new BigDecimal(StringUtil.filterNull(agentShareRuleMap.get("per_fix_cost")));
			BigDecimal parShareProfitPercent = new BigDecimal(
					StringUtil.filterNull(agentShareRuleMap.get("share_profit_percent")));
			BigDecimal bResult = (singleNumAmount.subtract(parCostRate).multiply(parShareProfitPercent));
			if (aResult.compareTo(bResult) > 0) {
				throw new AgentWebException(bpStr + "下级提现分润大于上级，请重新设置");
			}
		} else {
			// 在原判断逻辑上，在修改代理商固定分润百分比时，
			// 被修改的代理商(商户签约扣率 - 代理商成本扣率) * 代理商分润百分比
			// 不能高于上级的(商户签约扣率 - 代理商成本扣率) * 代理商分润百分比，
			// 如：1级代理商设置2级代理商的分润百分比时，设置的不能高于1级的，以此类推
			Map<String, Object> agentShareRuleMap = agentInfoDao.selectAgentShareRule(parentAgentNo,
					rule.getServiceId(), cardType, holidaysMark);
			String parentAgentShareCostRate = StringUtil.filterNull(agentShareRuleMap.get("cost_rate"));
			String parentAgentShareProfit = StringUtil.filterNull(agentShareRuleMap.get("share_profit_percent"));
			if (agentShareRuleMap == null) {
				throw new AgentWebException(bpStr + "上级代理商没有配置此产品");
			}
			if (agentShareRuleMap.get("cost_rate") == null || agentShareRuleMap.get("share_profit_percent") == null) {
				throw new AgentWebException(bpStr + "上级代理商成本扣率或者分润百分比没有配置");
			}
			BigDecimal aResult = (rate.subtract(new BigDecimal(parentAgentShareCostRate)))
					.multiply(new BigDecimal(parentAgentShareProfit));
			BigDecimal myCostRate = rule.getCostRate();
			BigDecimal myShareProfitPercent = rule.getShareProfitPercent();
			BigDecimal bResult = (rate.subtract(myCostRate).multiply(myShareProfitPercent));
			if (aResult.compareTo(bResult) < 0) {
				throw new AgentWebException(bpStr + "下级交易分润大于上级，请重新设置");
			}
		}
	}

	@Override
	public Map<String, Object> queryAgentInfoList(Map<String, Object> params, Page<AgentInfo> page) {
		Map<String, Object> msg = new HashMap<>();
		String agentNo = (String) params.get("agentNo");
		AgentInfo parentAgent = this.getCurAgentInfo();
		msg.put("agentLevel", parentAgent.getAgentLevel());
		params.put("oneLevelId", parentAgent.getOneLevelId());
		params.put("agentNode", parentAgent.getAgentNode());
		params.put("agentLevel", parentAgent.getAgentLevel());
		params.put("agentNo", agentNo);
		params.put("parentId", parentAgent.getAgentNo());
		List<AgentInfo> list = agentInfoDao.queryAgentInfoList(params, page);
		// 获取当前代理商支持的层级
		SupportRankDto supportRank = this.getSupportRank(parentAgent.getAgentOem());
		parentAgent.setShowFullPrizeSwitch((parentAgent.getFullPrizeSwitch() == 1 || parentAgent.getAgentLevel() == 1)
				&& supportRank.getFullPrizeLevel() >= parentAgent.getAgentLevel()
				&& !"11".equals(parentAgent.getAgentType()));
		parentAgent.setShowNotFullDeductSwitch(
				(parentAgent.getNotFullDeductSwitch() == 1 || parentAgent.getAgentLevel() == 1)
						&& supportRank.getNotFullDeductLevel() >= parentAgent.getAgentLevel()
						&& !"11".equals(parentAgent.getAgentType()));
		msg.put("curAgentInfo", parentAgent);

		for (AgentInfo agent : list) {
			if (parentAgent.getAgentLevel().equals(agent.getAgentLevel() - 1)) {
				agent.setModifyFlag(true);
			} else {
				agent.setModifyFlag(false);
			}
			agent.setShowFullPrizeSwitch((parentAgent.getFullPrizeSwitch() == 1 || parentAgent.getAgentLevel() == 1)
					&& supportRank.getFullPrizeLevel() >= parentAgent.getAgentLevel()
					&& !"11".equals(parentAgent.getAgentType()));
			agent.setShowNotFullDeductSwitch(
					(parentAgent.getNotFullDeductSwitch() == 1 || parentAgent.getAgentLevel() == 1)
							&& supportRank.getNotFullDeductLevel() >= parentAgent.getAgentLevel()
							&& !"11".equals(parentAgent.getAgentType()));

		}
		msg.put("page", page);
		return msg;
	}

	@Override
	public List<BusinessProductDefine> queryAgentProduct(String agentNo) {
		return agentBusinessProductDao.queryAgentProduct(agentNo);
	}

	@Override
	public Map<String, Object> queryAgentProducts(String agentNo, Integer teamId) {
		AgentInfo info = this.selectByagentNo(agentNo);
		Map<String, Object> map = new HashMap<>();
		if (info != null) {
			if (info.getAgentLevel() == 1) {
				// 一级代理商
				map.put("parentProducts", this.selectProductByTeamId(info.getTeamId().intValue()));
			} else {
				// 查询父级代理商业务产品
				map.put("parentProducts", businessProductDefineDao.getAgentProducts(info.getParentId()));
			}
			if (info.getTeamId().compareTo(teamId) == 0)// 组织是否变更
				map.put("agentProducts", businessProductDefineDao.getAgentProducts(info.getAgentNo()));
		}
		return map;
	}

	@Override
	public Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId) {
		Map<String, Object> map = new HashMap<>();
		String entityId = this.getCurAgentNo(); // 当前登陆代理商
		if (!agentInfoDao.isSubordinate(entityId, agentNo)) {
			throw new AgentWebException(String.format("代理商 %s 不是 %s 的下属代理商,无权查询该代理商的信息", agentNo, entityId));
		}
		AgentInfo curInfo = this.getCurAgentInfo();
		AgentInfo info = this.selectByagentNo(agentNo);
		if (info != null) {
			map.put("agentInfo", info);
			// 添加两个人人代理的字段
			if ("11".equals(info.getAgentType())) {
				Map<String, String> resultMap = perAgentDao.selectShareLevel(agentNo);
				String grade = "";
				if (resultMap != null) {
					grade = resultMap.get("grade");// '用户身份:0:普通盟主 1：黄金盟主，2：铂金盟主，3：黑金盟主，4：钻石盟主',
					switch (grade) {
					case "0":
						grade = "普通盟主";
						break;
					case "1":
						grade = "黄金盟主";
						break;
					case "2":
						grade = "铂金盟主";
						break;
					case "3":
						grade = "黑金盟主";
						break;
					case "4":
						grade = "钻石盟主";
						break;
					default:
						break;
					}
				}
				info.setGrade(grade);
				info.setShareLevel(resultMap.get("shareLevel"));
			}

//			查询业务产品
			map.put("agentProducts", businessProductDefineDao.getAgentProducts(info.getAgentNo()));
			map.put("agentTeamInfo", businessProductDefineService.selectTeamByAgentAndBp(info.getAgentNo()));
//			查询代理商的分润信息the1stActivityTypeNoTeamId
			map.put("agentShare", this.getAgentShareInfos(agentNo));
			// 查询代理商欢乐返返现配置
			List<AgentActivity> list = agentInfoDao.selectHappyBackList(agentNo);
			// 获取当前代理商支持的层级
			SupportRankDto supportRank = this.getSupportRank(curInfo.getAgentOem());
			for (AgentActivity agentActivity : list) {
				this.setShowPrizeDeductAmount(agentActivity, curInfo, supportRank);
			}
			map.put("happyBackList", list);
			map.put("activityTypeNoAndTeamId", this.getActivityTypeNoAndTeamIdMap(info.getAgentNo()));
//			查询代理商的费率
//			List<Integer> bpIds=new ArrayList<>();
//			for(JoinTable t:bps){
//				bpIds.add(t.getId());
//			}
//			map.put("agentRate",this.getServiceRate(bpIds, agentNo));
//			查询代理商的额度
//			map.put("agentQouta", agentInfoDao.getServiceQuota(bpIds, agentNo));

		}
		return map;
	}

	@Override
	public AgentInfo selectByPrincipal() {
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();
			if (StringUtils.equalsIgnoreCase(entityId, "ALL")) {
				AgentInfo agentInfo = new AgentInfo();
				agentInfo.setAgentNo("0");
				agentInfo.setAgentNode("0-");
				agentInfo.setAgentName("超级管理员");
				agentInfo.setOemType("0");
				return agentInfo;
			}
			AgentInfo loginAgent = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
			String oemType = agentInfoDao.selectOneAgentOemType(loginAgent.getOneLevelId());
			if (StringUtils.isBlank(oemType)) {
				oemType = "0";
			}
			loginAgent.setOemType(oemType);
			return loginAgent;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public AgentInfo selectByPrincipalApi(String entityId) {
		try {
			if (StringUtils.equalsIgnoreCase(entityId, "ALL")) {
				AgentInfo agentInfo = new AgentInfo();
				agentInfo.setAgentNo("0");
				agentInfo.setAgentNode("0-");
				agentInfo.setAgentName("超级管理员");
				agentInfo.setOemType("0");
				return agentInfo;
			}
			AgentInfo loginAgent = agentInfoDao.selectByAgentNo(entityId);
			String oemType = agentInfoDao.selectOneAgentOemType(loginAgent.getOneLevelId());
			if (StringUtils.isBlank(oemType)) {
				oemType = "0";
			}
			loginAgent.setOemType(oemType);
			return loginAgent;
		} catch (Exception e) {
			log.error("查询当前登录代理商信息异常", e);
			return null;
		}
	}

	/**
	 * 通过所有的代理商查询出所有分润信息
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cn.eeepay.framework.service.AgentInfoService#getAgentShareInfos(java.lang.
	 * String)
	 */
	@Override
	public List<AgentShareRule> getAgentShareInfos(String agentNo) {
		List<String> learderOrIndividualBpIdBySelf = agentInfoDao.getLearderOrIndividualBpIdBySelf(agentNo);
		if (CollectionUtils.isEmpty(learderOrIndividualBpIdBySelf)) {
			return null;
		}
		List<AgentShareRule> list = agentInfoDao.getAgentShareInfos(learderOrIndividualBpIdBySelf, agentNo);
		for (AgentShareRule r : list)
			this.profitExpression(r);
		return list;
	}

	@Override
	public void setShareRule(AgentShareRule share) {
		String temp;
		switch (share.getProfitType()) {
		case 1:
			temp = share.getIncome();
			if (StringUtils.isBlank(temp) || !temp.matches("\\d+(\\.\\d+)?"))
				throw new RuntimeException("分润信息中类型为【每笔固定收益金额】：填写不合法！");
			share.setPerFixIncome(new BigDecimal(temp));
			break;
		case 2:
			temp = share.getIncome();
			if (StringUtils.isBlank(temp) || !temp.matches("\\d+(\\.\\d+)?%"))
				throw new RuntimeException("分润信息中类型为【每笔收益率】：填写不合法！");
			share.setPerFixInrate(new BigDecimal(temp.substring(0, temp.indexOf("%"))));
			break;
		case 3:
			temp = share.getIncome();
			Matcher m = pattern.matcher(temp);
			if (StringUtils.isBlank(temp) || !m.matches())
				throw new RuntimeException("分润信息中类型为【每笔收益率带保底封顶】：填写不合法！");
			m.reset();
			while (m.find()) {
				share.setSafeLine(new BigDecimal(m.group(1)));
				share.setPerFixInrate(new BigDecimal(m.group(3)));
				share.setCapping(new BigDecimal(m.group(5)));
				if (share.getSafeLine().compareTo(share.getCapping()) > 0) {
					throw new RuntimeException("分润信息中类型为【每笔收益率带保底封顶】：保底不能大于封顶！");
				}
			}
			break;
		case 4:
			temp = share.getIncome();
			if (StringUtils.isBlank(temp) || !temp.matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				throw new RuntimeException("分润信息中类型为【每笔收益率+每笔固定收益金额】：填写不合法！");
			String[] arr = temp.split("%\\+");
			share.setPerFixInrate(new BigDecimal(arr[0]));
			share.setPerFixIncome(new BigDecimal(arr[1]));
			break;
		case 5:
			temp = setShareCose(share);
			if (share.getShareProfitPercent() == null || StringUtils.isNotBlank(temp))
				throw new RuntimeException("分润信息中类型为【商户签约费率与代理商成本费率差额百分比分润】商户成本填写不合法！");
			break;
		case 6:
			temp = setShareCose(share);
			if (StringUtils.isNotBlank(temp))
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】商户成本填写不合法！");
			temp = share.getLadderRate();
			if (!temp.matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%){3}"))
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例：填写不合法！");
			String[] strs = temp.split("<");
			share.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			share.setLadder1Max(new BigDecimal(strs[1]));
			share.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			share.setLadder2Max(new BigDecimal(strs[3]));
			share.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
			share.setLadder3Max(new BigDecimal(strs[5]));
			share.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
			if (share.getLadder2Max().compareTo(share.getLadder1Max()) <= 0)
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第二组上限额度小于第一组上限额度！");
			if (share.getLadder3Max().compareTo(share.getLadder2Max()) <= 0)
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第三组上限额度小于第二组上限额度！");
			break;
		}
	}

	public void setShareRuleAdd(AgentShareRule share) {
		String temp;
		switch (share.getProfitType()) {
		case 5:
			temp = setShareCoseAdd(share);
			if (share.getShareProfitPercent() == null || StringUtils.isNotBlank(temp))
				throw new RuntimeException("分润信息中类型为【商户签约费率与代理商成本费率差额百分比分润】商户成本填写不合法！");
			break;
		case 6:
			temp = setShareCoseAdd(share);
			if (StringUtils.isNotBlank(temp))
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】商户成本填写不合法！");
			temp = share.getLadderRate();
			if (!temp.matches("^(\\d+(\\.\\d+)?)%(<(\\d+(\\.\\d+)?)<(\\d+(\\.\\d+)?)%){3}"))
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例：填写不合法！");
			String[] strs = temp.split("<");
			share.setLadder1Rate(new BigDecimal(strs[0].substring(0, strs[0].lastIndexOf("%"))));
			share.setLadder1Max(new BigDecimal(strs[1]));
			share.setLadder2Rate(new BigDecimal(strs[2].substring(0, strs[2].lastIndexOf("%"))));
			share.setLadder2Max(new BigDecimal(strs[3]));
			share.setLadder3Rate(new BigDecimal(strs[4].substring(0, strs[4].lastIndexOf("%"))));
			share.setLadder3Max(new BigDecimal(strs[5]));
			share.setLadder4Rate(new BigDecimal(strs[6].substring(0, strs[6].lastIndexOf("%"))));
			if (share.getLadder2Max().compareTo(share.getLadder1Max()) <= 0)
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第二组上限额度要大于第一组上限额度！");
			if (share.getLadder3Max().compareTo(share.getLadder2Max()) <= 0)
				throw new RuntimeException("分润信息中类型为【商户费率与代理商成本差额阶梯分润】分润比例第三组上限额度要大于第二组上限额度！");
			break;
		default:
			throw new RuntimeException("分润方式错误，请刷新页面后重试");
		}
	}

	/**
	 * 设置成本
	 *
	 * @param share
	 * @return
	 */
	private String setShareCose(AgentShareRule share) {
		String temp = share.getCost();
		if (temp.indexOf("+") != -1) {
			if (!temp.matches("^(\\d+(\\.\\d+)?)%\\+(\\d+(\\.\\d+)?)"))
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			String[] arr = temp.split("%+");
			share.setCostRate(new BigDecimal(arr[0]));
			share.setPerFixCost(new BigDecimal(arr[1]));
			share.setCostRateType("4");
		} else if (temp.indexOf("~") != -1) {
			Matcher m = pattern.matcher(temp);
			while (m.find()) {
				share.setCostSafeline(new BigDecimal(m.group(1)));
				share.setCostRate(new BigDecimal(m.group(3)));
				share.setCostCapping(new BigDecimal(m.group(5)));
			}
			if (share.getCostCapping() == null || share.getCostCapping().compareTo(share.getCostSafeline()) < 0)
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			share.setCostRateType("3");
		} else if (temp.indexOf("%") != -1) {
			String str_ = temp.substring(0, temp.indexOf("%"));
			if (str_.matches("\\d+(\\.\\d+)?")) {
				share.setCostRate(new BigDecimal(str_));
			} else {
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("2");
		} else {
			if (temp.matches("\\d+(\\.\\d+)?")) {
				share.setPerFixCost(new BigDecimal(temp));
			} else {
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("1");
		}
		return null;
	}

	/**
	 * 新增代理商的时候，设置成本，此时成本只有两种1-每笔固定金额，2-扣率
	 *
	 * @param share
	 * @return
	 */
	private String setShareCoseAdd(AgentShareRule share) {
		String temp = share.getCost();
		if (temp.indexOf("%") != -1) {
			String str_ = temp.substring(0, temp.indexOf("%"));
			if (str_.matches("\\d+(\\.\\d+)?")) {
				share.setCostRate(new BigDecimal(str_));
			} else {
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("2");
		} else {
			if (temp.matches("\\d+(\\.\\d+)?")) {
				share.setPerFixCost(new BigDecimal(temp));
			} else {
				return "分润信息中类型为【商户费率与代理商成本差额百分比分润】";
			}
			share.setCostRateType("1");
		}
		return null;
	}

	@Override
	public List<BusinessProductDefine> queryMerProduct(String agentNo, String Product) {
		return agentBusinessProductDao.queryMerProduct(agentNo, Product);
	}

	@Override
	public String delAgent(String agentNo) {
		// 1.判断一级代理商是否有下级代理商和直接商户
		int count = agentInfoDao.getAgentAndMerchantCount(agentNo);
		if (count > 0) {
			return "当前代理商已经存在下级代理商或商户，不能删除！";
		}
//		AgentInfo agent=this.selectByagentNo(agentNo);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//		Map<String,Object> map=agentInfoDao.getAgentEntity(agentNo);
		Map<String, Object> map = agentInfoDao.getAgentEntity(agentNo, info.getTeamId());
		String userId = (String) map.get("user_id");
		// 删除代理商角色
		agentInfoDao.delAgentRole(userId);
		// 删除代理商用户
		agentInfoDao.delAgentUser(userId);
		// 删除代理商实体
		agentInfoDao.delAgentEntity(userId);
		// 删除分润task
		agentInfoDao.deleteAgentShareTasks(agentNo);
		// 删除分润
		agentInfoDao.deleteAgentShares(agentNo);
		// 删除业务产品
		agentInfoDao.deleteAgentProducts(agentNo);
		// 删除代理商
		agentInfoDao.deleteAgent(agentNo);
		return "删除代理商成功！";
	}

	@Override
	public Map<String, Object> queryAgentInfoDetail(String agentNo, Integer teamId, boolean isAllProducts) {
		Map<String, Object> map = new HashMap<>();
		String entityId = this.getCurAgentNo(); // 当前登陆代理商
		if (!agentInfoDao.isDirectSubordinate(entityId, agentNo)) {
			throw new AgentWebException(String.format("代理商 %s 不是 %s 的直接下级代理商,无权修改该代理商的信息", agentNo, entityId));
		}
		AgentInfo info = this.selectByagentNo(agentNo);
		AgentInfo curInfo = this.selectByPrincipal();
		if (info != null) {
			map.put("agentInfo", info);
//			查询业务产品
			map.put("agentProducts", businessProductDefineDao.getAgentProducts(info.getAgentNo()));
			map.put("agentTeamInfo", businessProductDefineService.selectTeamByAgentAndBp(info.getAgentNo()));
//			查询上级的所有的业务产品
			if (isAllProducts){
				map.put("parentProducts", businessProductDefineDao.getAgentProducts(entityId));
				map.put("parentTeamInfo", businessProductDefineService.selectTeamByAgentAndBp(entityId));
			}
//			查询代理商的分润信息
			map.put("agentShare", this.getAgentShareInfos(agentNo));
			// 查询欢乐返返现配置
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			List<AgentActivity> list = agentInfoDao.selectHappyBackList(agentNo);
			List<AgentActivity> activities = agentInfoDao.selectHappyBackListEclude(loginAgent.getAgentNo(), agentNo);

			// 超级盛POS才显示开关
			List<String> typeNos = null;
			SysDict sysDict  = sysDictDao.selectDictByKey("SUP_POS_ACTIVITY");
			String sysValue = sysDict.getSysValue().replace("，", ",");
			String[] typeNosStr = sysValue.split(",");
			if(null != typeNosStr && typeNosStr.length > 0){
				typeNos = Arrays.asList(typeNosStr);
			}

			for (AgentActivity agentActivity : list) {
				agentActivity.setSelect(true);
				agentActivity.setDisabled(true);
				if(null != typeNos && typeNos.contains(agentActivity.getActivityTypeNo())){
					agentActivity.setShowStatusSwitch(true);
				}else {
					agentActivity.setShowStatusSwitch(false);
				}
			}

			// 查询当前代理商所有可配置的欢乐返子类型
			if(activities.size() > 0){
				for (AgentActivity activity : activities) {
					activity.setSelect(false);
					activity.setShowStatusSwitch(false);
					activity.setDisabled(false);
					activity.setCashBackAmount(null);
					activity.setTaxRate(null);
					activity.setRepeatRegisterAmount(null);
					activity.setRepeatRegisterRatio(null);
					activity.setFullPrizeAmount(null);
					activity.setNotFullDeductAmount(null);
					activity.setRepeatFullPrizeAmount(null);
					activity.setRepeatNotFullDeductAmount(null);
				}
				list.addAll(activities);
			}

			// 获取当前代理商支持的层级
			SupportRankDto supportRank = this.getSupportRank(curInfo.getAgentOem());
			for (AgentActivity agentActivity : list) {
				this.setShowPrizeDeductAmount(agentActivity, curInfo, supportRank);
				if(null != agentActivity.getTaxRate()){
					agentActivity.setTaxRate(agentActivity.getTaxRate().multiply(new BigDecimal("100")));
				}
				if(null != agentActivity.getRepeatRegisterRatio()){
					agentActivity
							.setRepeatRegisterRatio(agentActivity.getRepeatRegisterRatio().multiply(new BigDecimal("100")));
				}
			}
			map.put("happyBackList", list);
			map.put("parentHappyBackList", agentInfoDao.selectHappyBackList(loginAgent.getAgentNo()));
			map.put("activityTypeNoAndTeamId", this.getActivityTypeNoAndTeamIdMap(loginAgent.getAgentNo()));
			//是否展示允许更改活动栏
			boolean showStatusSwitchColumn = false;
			for (AgentActivity activity : list) {
				if(activity.isShowStatusSwitch()){
					showStatusSwitchColumn = true;
					break;
				}
			}
			map.put("showStatusSwitchColumn", showStatusSwitchColumn);
		}
		return map;
	}

	// 设置成本
	@Override
	public void profitExpression(AgentShareRule rule) {
		if (rule.getProfitType() == null)
			return;
		switch (rule.getProfitType()) {
		case 1:
			rule.setIncome(rule.getPerFixIncome().setScale(2, RoundingMode.HALF_UP).toString());
			rule.setShareProfitPercent(null);
			break;
		case 2:
			rule.setIncome(rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString() + "%");
			rule.setShareProfitPercent(null);
			break;
		case 3:
			rule.setIncome(rule.getSafeLine().setScale(2, RoundingMode.HALF_UP).toString() + "~"
					+ rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString() + "%~"
					+ rule.getCapping().setScale(2, RoundingMode.HALF_UP).toString());
			rule.setShareProfitPercent(null);
			break;
		case 4:
			rule.setIncome(rule.getPerFixInrate().setScale(3, RoundingMode.HALF_UP).toString() + "%+"
					+ rule.getPerFixIncome().setScale(2, RoundingMode.HALF_UP).toString());
			rule.setShareProfitPercent(null);
			break;
		case 5:
			switch (rule.getCostRateType()) {
			case "1":
				rule.setCost(rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			case "2":
				rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%");
				break;
			case "3":
				rule.setCost(rule.getCostSafeline().setScale(2, RoundingMode.HALF_UP).toString() + "~"
						+ rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%~"
						+ rule.getCostCapping().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			case "4":
				rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%+"
						+ rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			}
			break;
		case 6:
			StringBuffer sb = new StringBuffer();
			sb.append(rule.getLadder1Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<")
					.append(rule.getLadder1Max().setScale(2, RoundingMode.HALF_UP).toString()).append("<")
					.append(rule.getLadder2Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<")
					.append(rule.getLadder2Max().setScale(2, RoundingMode.HALF_UP).toString()).append("<")
					.append(rule.getLadder3Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%<")
					.append(rule.getLadder3Max().setScale(2, RoundingMode.HALF_UP).toString()).append("<")
					.append(rule.getLadder4Rate().setScale(3, RoundingMode.HALF_UP).toString()).append("%");
			rule.setLadderRate(sb.toString());
			switch (rule.getCostRateType()) {
			case "1":
				rule.setCost(rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			case "2":
				rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%");
				break;
			case "3":
				rule.setCost(rule.getCostSafeline().setScale(2, RoundingMode.HALF_UP).toString() + "~"
						+ rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%~"
						+ rule.getCostCapping().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			case "4":
				rule.setCost(rule.getCostRate().setScale(3, RoundingMode.HALF_UP).toString() + "%+"
						+ rule.getPerFixCost().setScale(2, RoundingMode.HALF_UP).toString());
				break;
			}

		}
	}

	@Override
	public String updateAgent(String data) {
		JSONObject json = JSONObject.parseObject(data);
		AgentInfo agentParam = JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
        String updateType = Objects.toString(json.get("updateType"), "default");
		// 更新代理商系统的用户信息
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
		Map<String, Object> map = agentInfoDao.getAgentEntity(agentParam.getAgentNo(), info.getTeamId());
		if (map == null) {
			throw new AgentWebException("未查询到用户信息");
		}
        // 更新代理商基本信息
		AgentInfo agent;
        if (StringUtils.equalsIgnoreCase(updateType, "noSafePhone")) {
            agent = updateAgentBaseInfoNoSafePhone(json);
        }else {
            agent = updateAgentBaseInfo(json);
        }
		// 更新代理商业务产品信息和分润信息
		updateAgentProductList(json, agent);
		// 更新代理商欢乐返返现设置,非超级盟主的才需要修改欢乐返设置,超级盟主没有
		if (!"11".equals(info.getAgentType())) {
			List<AgentActivity> happyBackList = JSONArray.parseArray(json.getString("happyBackList"),
					AgentActivity.class);
			if (happyBackList.size() > 0) {
				updateHappyBackList(happyBackList, agentParam.getAgentNo());
			}
		}

		String userId = (String) map.get("user_id");
		AgentUserInfo agentUser = new AgentUserInfo();
		agentUser.setUserName(agent.getAgentName());
		agentUser.setMobilephone(agent.getMobilephone());
		agentUser.setEmail(agent.getEmail());
		agentUser.setUserId(userId);
		UserInfo userInfo = userInfoDao.findUserInfoByUserId(userId);
		if (!userInfo.getMobilephone().equals(agent.getMobilephone())) {
			if ("11".equals(info.getAgentType())) {
				agentUser.setPassword(new Md5PasswordEncoder().encodePassword(agent.getMobilephone().substring(5, 11),
						agent.getMobilephone()));
			} else {
				agentUser.setPassword(new Md5PasswordEncoder().encodePassword("123456", agent.getMobilephone()));
			}
		}
		agentInfoDao.updateAgentUser(agentUser);

		// 更新人人代理
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			// 更新人人代理结算卡信息
			Map<String, String> AgentUserMap = perAgentDao.selectByAgentNo(agent.getAgentNo());
			agent.setUserCode(AgentUserMap.get("user_code"));
			int i = perAgentDao.updatePaUserCard(agent);

			// 更新人人代理手机号,唯一t
			String mobilephone = agentParam.getMobilephone();
			Map<String, String> mapResult = perAgentDao.selectByAgentNo(agentParam.getAgentNo());
			String userCode = mapResult.get("user_code");
			log.info("根据页面传过来的代理商编号在pa_agent_user表查询数据 ==>" + mapResult + "<====");
			if (perAgentDao.selectByPhone(mobilephone, userCode) > 0) {
				throw new AgentWebException("手机号已存在");
			}
			String password = new Md5PasswordEncoder().encodePassword(mobilephone.substring(5, 11), mobilephone);
			int j = perAgentDao.updatePaUserInfo(agentParam.getAccountName(), agentParam.getIdCardNo(), mobilephone,
					password, userCode);
			log.info("====代理商修改,更新pa_user_info表==" + j + "==条数据=====");
			if (j < 1) {
				throw new AgentWebException("修改失败!");
			}
		}

		return "修改代理商成功";
	}

	private void updateHappyBackList(List<AgentActivity> happyBackList, String agentNo) {
		for (AgentActivity agentActivity : happyBackList) {
			BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
			if (fullPrizeAmount == null) {
				agentActivity.setFullPrizeAmount(BigDecimal.ZERO);
			}
			BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
			if (notFullDeductAmount == null ) {
				agentActivity.setNotFullDeductAmount(BigDecimal.ZERO);
			}
			BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
			if (repeatFullPrizeAmount == null) {
				agentActivity.setRepeatFullPrizeAmount(BigDecimal.ZERO);
			}
			BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
			if (repeatNotFullDeductAmount == null ) {
				agentActivity.setRepeatNotFullDeductAmount(BigDecimal.ZERO);
			}
		}

		AgentInfo curInfo = this.getCurAgentInfo();
		String no = curInfo.getAgentNo();
		// 获取当前代理商支持的层级
		SupportRankDto supportRank = this.getSupportRank(curInfo.getAgentOem());
		Integer fullPrizeLevel = supportRank.getFullPrizeLevel();
		Integer notFullDeductLevel = supportRank.getNotFullDeductLevel();
		Integer agentLevel = curInfo.getAgentLevel();

		if (fullPrizeLevel >= agentLevel && notFullDeductLevel >= agentLevel) {
			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				int i = this.fullPrizeNotFullDeductAmount(happyBackList);
				// 满奖不满扣参数全部设置为空 关闭所有下级满奖不满扣功能开关
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenFullSwitch(agentNo);
				}
			} else if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 0) {
				int i = this.fullPrizeAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenFullPrizeSwitch(agentNo);
				}
			} else if (curInfo.getFullPrizeSwitch() == 0 && curInfo.getNotFullDeductSwitch() == 1) {
				int i = this.notFullDeductAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenNotFullDeductSwitch(agentNo);
				}
			}
		} else if (fullPrizeLevel >= agentLevel && notFullDeductLevel < agentLevel) {// 只支持满奖

			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				int i = this.fullPrizeAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenFullPrizeSwitch(agentNo);
				}
			} else if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 0) {
				int i = this.fullPrizeAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenFullPrizeSwitch(agentNo);
				}
			}
		} else if (fullPrizeLevel < agentLevel && notFullDeductLevel >= agentLevel) {// 只支持不满扣
			if (curInfo.getFullPrizeSwitch() == 1 && curInfo.getNotFullDeductSwitch() == 1) {
				int i = this.notFullDeductAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenNotFullDeductSwitch(agentNo);
				}
			} else if (curInfo.getFullPrizeSwitch() == 0 && curInfo.getNotFullDeductSwitch() == 1) {
				int i = this.notFullDeductAmount(happyBackList);
				if (i == happyBackList.size()) {
					agentInfoDao.updateChildrenNotFullDeductSwitch(agentNo);
				}
			}
		}
		// 查询被修改代理商原本具有的欢乐返子类型
		List<String> activityTypeNos = agentInfoDao.selectActivityTypeNos(agentNo);

		// 更新表agent_activity返现金额和税额百分比
		int updateCount = 0;
		int addCount = 0;
		List<AgentActivity> addAgentActivities = new ArrayList<>();
		for (AgentActivity agentActivity : happyBackList) {
			// 修改
			if(activityTypeNos.contains(agentActivity.getActivityTypeNo())){
				Map<String, Object> mapParentAgent = selectByActivityTypeNo(agentActivity.getActivityTypeNo());
				if (mapParentAgent == null) {
					mapParentAgent = selectDefaultStatus();
				}
				BigDecimal cashBackAmountParentAgent = mapParentAgent.get("cash_back_amount") == null ? new BigDecimal("0")
						: new BigDecimal(mapParentAgent.get("cash_back_amount").toString());
				BigDecimal taxRateParentAgent = mapParentAgent.get("tax_rate") == null ? new BigDecimal("1")
						: new BigDecimal(mapParentAgent.get("tax_rate").toString());// 需求要求如果一级代理商没有设置,默认为100%
				BigDecimal repeatRegisterAmountParentAgent = mapParentAgent.get("repeat_register_amount") == null
						? new BigDecimal("0")
						: new BigDecimal(mapParentAgent.get("repeat_register_amount").toString());
				BigDecimal repeatRegisterRatioParentAgent = mapParentAgent.get("repeat_register_ratio") == null
						? new BigDecimal("1")
						: new BigDecimal(mapParentAgent.get("repeat_register_ratio").toString());// 需求要求如果一级代理商没有设置,默认为100%
				BigDecimal cashBackAmount = agentActivity.getCashBackAmount();
				BigDecimal taxRate = agentActivity.getTaxRate();
				BigDecimal repeatRegisterAmount = agentActivity.getRepeatRegisterAmount();
				BigDecimal repeatRegisterRatio = agentActivity.getRepeatRegisterRatio();
				if (cashBackAmount == null || taxRate == null) {
					throw new AgentWebException("返现金额和返现比例不能为空!");
				}
				if (repeatRegisterAmount == null || repeatRegisterRatio == null) {
					throw new AgentWebException("重复注册返现金额和重复注册返现比例不能为空！");
				}
				if ((cashBackAmount.multiply(taxRate).divide(new BigDecimal("100")))
						.compareTo(cashBackAmountParentAgent.multiply(taxRateParentAgent)) == 1) {
					throw new AgentWebException("下级代理商的返现不得高于上级代理商的返现!");
				}
				if ((repeatRegisterAmount.multiply(repeatRegisterRatio).divide(new BigDecimal("100")))
						.compareTo(repeatRegisterAmountParentAgent.multiply(repeatRegisterRatioParentAgent)) == 1) {
					throw new AgentWebException("下级代理商的重复返现不得高于上级代理商的重复返现!");
				}
				AgentInfo info = agentInfoDao.selectByAgentNo(agentActivity.getAgentNo());
				if (!(info.getParentId()).equals(no)) {
					throw new AgentWebException("非法操作,所要修改的代理商不是你的直属下级!");
				}
				agentActivity.setCashBackAmount(cashBackAmount);
				agentActivity.setTaxRate(taxRate.divide(new BigDecimal("100")));
				agentActivity.setRepeatRegisterAmount(repeatRegisterAmount);
				agentActivity.setRepeatRegisterRatio(repeatRegisterRatio.divide(new BigDecimal("100")));
				// 拿到上级的满奖不满扣金额
				AgentActivity parent = agentInfoDao.findAgentActivityByParentAndType(no, agentActivity.getActivityTypeNo());
				if (parent != null) {
					BigDecimal parentFullPrizeAmount = parent.getFullPrizeAmount();
					BigDecimal parentNotFullDeductAmount = parent.getNotFullDeductAmount();
					BigDecimal parentRepeatFullPrizeAmount = parent.getRepeatFullPrizeAmount();
					BigDecimal parentRepeatNotFullDeductAmount = parent.getRepeatNotFullDeductAmount();
					BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
					BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
					BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();
					BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
					if (fullPrizeAmount != null && parentFullPrizeAmount != null && fullPrizeAmount.compareTo(parentFullPrizeAmount) == 1) {
						throw new AgentWebException("首次注册满奖金额需 ≤ " + parentFullPrizeAmount + "元");
					}
					if (notFullDeductAmount != null && parentNotFullDeductAmount != null  && notFullDeductAmount.compareTo(parentNotFullDeductAmount) == 1) {
						throw new AgentWebException("首次注册不满扣金额需 ≤ " + parentNotFullDeductAmount + "元");
					}
					if (repeatFullPrizeAmount != null && parentRepeatFullPrizeAmount != null && repeatFullPrizeAmount.compareTo(parentRepeatFullPrizeAmount) == 1) {
						throw new AgentWebException("重复注册满奖金额需 ≤ " + parentRepeatFullPrizeAmount + "元");
					}
					if (repeatNotFullDeductAmount != null && parentRepeatNotFullDeductAmount != null
							&& repeatNotFullDeductAmount.compareTo(parentRepeatNotFullDeductAmount) == 1) {
						throw new AgentWebException("重复注册不满扣金额需 ≤ " + parentRepeatNotFullDeductAmount + "元");
					}
					if (parentFullPrizeAmount == null ) {
						agentActivity.setFullPrizeAmount(null);
					}
					if(parentNotFullDeductAmount == null) {
						agentActivity.setNotFullDeductAmount(null);
					}
					if (parentRepeatFullPrizeAmount == null ) {
						agentActivity.setRepeatFullPrizeAmount(null);
					}
					if (parentRepeatNotFullDeductAmount == null ) {
						agentActivity.setRepeatNotFullDeductAmount(null);
					}
				}

				updateCount += agentInfoDao.updateHappyBackList(agentActivity);

			}else {//新增
				addAgentActivities.add(agentActivity);
			}
		}

		if(addAgentActivities.size() > 0){
			addCount = saveHappyBack(addAgentActivities, this.getCurAgentInfo(), agentNo);
		}

		if(addCount != addAgentActivities.size()){
			log.info("修改代理商新增欢乐返活动子类型失败！" + "新增条数addCount=" + addCount + ",需要新增的条数：" + addAgentActivities.size());
			throw new AgentWebException("修改代理商失败!");
		}
		if(updateCount != (happyBackList.size() - addAgentActivities.size())){
			log.info("修改代理商时更新欢乐返返现金额和税额百分比失败!" + "更新条数updateCount=" + updateCount + ",需要更新的条数：" + (happyBackList.size() - addAgentActivities.size()));
			throw new AgentWebException("修改代理商失败!");
		}

	}

	/**
	 * 更新业务产品和分润信息
	 *
	 * @param json  页面传递过来的数据
	 * @param agent 此次更新代理商信息
	 */
	private void updateAgentProductList(JSONObject json, AgentInfo agent) {
		// 如果页面没有传分润信息过来,则不需要更新分润信息
		List<Integer> bps;
		if (json.getString("bpData") == null || json.getJSONArray("bpData").isEmpty()) {
			bps = new ArrayList<>();
		} else {
			bps = JSONArray.parseArray(json.getString("bpData"), Integer.class);
		}
		// 更新业务产品信息
		updateAgentProductBusiness(bps, agent.getAgentNo());
		// 更新分润信息
		updateAgentShareData(json, bps, agent);
	}

	/**
	 * 更新分润信息
	 *
	 * @param json  页面传递过来的数据
	 * @param bps   此次修改更新的业务产品id集合
	 * @param agent 此次更新代理商信息
	 */
	private void updateAgentShareData(JSONObject json, List<Integer> bps, AgentInfo agent) {
//		List<ServiceRate> list = agentInfoDao.getRate(bps, agent.getOneLevelId(), agent.getAgentNo());
//		if (CollectionUtils.isEmpty(list)) {
//			return;
//		}
		// 更新页面输入的分润信息
		updatePageShareData(json, agent);
		// 如果此次修改没有新增业务产品,则不需要更新队员的分润信息
		if (CollectionUtils.isEmpty(bps)) {
			return;
		}
		// 查询更新的代理商队长与队员的对应集合 key-value(队长-队员)
		List<Map<String, Object>> leaderAndMember = agentInfoDao.getLeaderAndMember(agent.getAgentNo());
		// 更新在页面没有输入,但是已经代理队长却刚代理的队员的分润信息
		updateMemberShareData(json, bps, agent, leaderAndMember);
		// 如果队长是第一次代理或者被修改的话,则要相应的修改队员的分润信息
		if (!CollectionUtils.isEmpty(leaderAndMember)) {
			for (Map<String, Object> temp : leaderAndMember) {
				int leader = Integer.valueOf(StringUtil.filterNull(temp.get("leader")));
				int member = Integer.valueOf(StringUtil.filterNull(temp.get("member")));
				// 此次更新的分润信息如果有包含队长,则队员相应进行更新
				if (bps.contains(leader)) {
                    List<AgentShareRule> agentShareRules = agentInfoDao.listMemberAgentShareByLeader(leader + "", member + "", agent.getAgentNo());
                    agentInfoDao.insertMemberAgentShare(agentShareRules);
				}
			}
		}
	}

	/**
	 * 更新组员的分润信息 一般情况下,是代理商先代理队长的信息 后来又想代理队员的分润信息 所以在页面是没有输入队员的分润信息,
	 * 都是通过该队员的队长的分润信息自动生成的
	 */
	private void updateMemberShareData(JSONObject json, List<Integer> bps, AgentInfo agent,
			List<Map<String, Object>> leaderAndMember) {
		// 获取在页面没有输入但是已经代理队长却刚代理的队员的分润信息
		List<AgentShareRule> memberAgentShareList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(leaderAndMember)) {
			for (Map<String, Object> temp : leaderAndMember) {
				int leader = Integer.valueOf(StringUtil.filterNull(temp.get("leader")));
				int member = Integer.valueOf(StringUtil.filterNull(temp.get("member")));
				// 此次更新业务产品没有包含队长,但却包含了队员的业务产品
				if (!bps.contains(leader) && bps.contains(member)) {
					List<AgentShareRule> agentShareRules = agentInfoDao.listMemberAgentShareByLeader(leader + "",
							member + "", agent.getAgentNo());
					if (!CollectionUtils.isEmpty(agentShareRules)) {
						// 如果队长有未生效的分润规则,则不允许代理该队员
						if (agentInfoDao.countHasNotEfficientRule(leader, agent.getAgentNo()) > 0) {
							throw new AgentWebException(
									String.format("业务产品(%d)拥有未生效的分润规则,不允许添加自定义业务产品(%d)", leader, member));
						}
						memberAgentShareList.addAll(agentShareRules);
					}
				}
			}
		}
		if (CollectionUtils.isEmpty(memberAgentShareList)) {
			return;
		}
		for (AgentShareRule rule : memberAgentShareList) {
			AgentShareRule parentAgentShareRule = agentInfoDao.getSameTypeParentAgentShare(rule);
			if (parentAgentShareRule == null) {
				log.warn(rule.toString());
				throw new AgentWebException("上级代理商 " + rule.getServiceId() + " 的分润规则没配置,请联系上级代理商配置");
			}
			isChildrenRuleLessThanParent(parentAgentShareRule, rule);
			compareServiceRate(rule, agent.getOneLevelId(), agent.getParentId(), agent.getAgentNo());
		}
		agentInfoDao.insertAgentShareList(memberAgentShareList);
	}

	/**
	 * 更新页面输入的分润信息 (一般是队长的分润信息或没有组的分润信息)
	 */
	private void updatePageShareData(JSONObject json, AgentInfo agent) {
		List<AgentShareRule> shareList = null;
		if (!json.getString("shareData").isEmpty() && json.getJSONArray("shareData").size() >= 1) {
			shareList = JSONArray.parseArray(json.getString("shareData"), AgentShareRule.class);
		}
		if (CollectionUtils.isEmpty(shareList)) {
			return;
		}
		List<AgentShareRule> listRules = new ArrayList<>();
		for (AgentShareRule rule : shareList) {
			if (rule == null) {
				continue;
			}
			if (!NumberUtils.isNumber(rule.getCost())) {
				throw new AgentWebException("代理商 " + rule.getServiceName() + " 的分润规则配置有误,请重新配置.");
			}
			rule.setProfitType(5); // 现在只剩下第5种分润类型
			// 如果是提现业务
			if (StringUtils.equalsIgnoreCase(rule.getServiceType() + "", "10001")
					|| StringUtils.equalsIgnoreCase(rule.getServiceType() + "", "10000")) {
				rule.setCostRateType("1");
				rule.setPerFixCost(new BigDecimal(rule.getCost()));
			} else {
				rule.setCostRateType("2");
				rule.setCostRate(new BigDecimal(rule.getCost()));
			}
			rule.setAgentNo(agent.getAgentNo());
			rule.setCheckStatus(1);
			rule.setLockStatus(0);
			AgentShareRule parentAgentShareRule = agentInfoDao.getSameTypeParentAgentShare(rule);
			if (parentAgentShareRule == null) {
				log.warn(rule.toString());
				throw new AgentWebException("上级代理商 " + rule.getServiceName() + " 的分润规则没配置,请联系上级代理商配置");
			}
			// 跟上级同类型的分润信息进行比较,不能比上级的分润成本低
			isChildrenRuleLessThanParent(parentAgentShareRule, rule);
			// 跟同类型的服务费率比较,不能高于同类型服务的服务费率
			compareServiceRate(rule, agent.getOneLevelId(), agent.getParentId(), agent.getAgentNo());
			listRules.add(rule);
		}
		agentInfoDao.insertAgentShareList(listRules);
	}

	/**
	 * 更新业务产品信息
	 */
	private void updateAgentProductBusiness(List<Integer> allBpIds, String agentNo) {
		// 如果没有新增业务产品,则不需要操作,直接跳出
		if (CollectionUtils.isEmpty(allBpIds)) {
			return;
		}
		List<JoinTable> bpList = new ArrayList<>();
		List<Integer> notProxyBpIds = new ArrayList<>();
		List<Integer> selectBpIdByAgentNo = agentInfoDao.selectBpIdByAgentNo(agentNo);// 查询所有业务产品编号314
		for (Integer id : allBpIds) {
			JoinTable product = new JoinTable();
			product.setKey1(id);
			product.setKey2(1);
			product.setKey3(agentNo);
			if (!selectBpIdByAgentNo.contains(id)) {
				bpList.add(product);
				notProxyBpIds.add(id);
			}
		}
		if (bpList.size() > 0) {
			agentInfoDao.insertAgentProductList(bpList);
			agentInfoDao.updateDefaultFlagLeader2On(notProxyBpIds, agentNo);
		}

	}

	// 更新代理商基本信息
	private AgentInfo updateAgentBaseInfo(JSONObject json) {
		AgentInfo agent = JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//		agent.setTeamId(Integer.valueOf(Constants.TEAM_ID));
		agent.setTeamId(info.getTeamId());
		if (agent.getAccountType() == 2) {
			AgentInfo agentInfo = agentInfoService.selectByagentNo(agent.getAgentNo());
			if ((! StringUtils.equals(agent.getIdCardNo(), agentInfo.getIdCardNo()) ||
                    (! StringUtils.equals(agent.getAccountNo(), agentInfo.getAccountNo())) ||
                    (! StringUtils.equals(agent.getAccountName(), agentInfo.getAccountName())))) {//修改时,修改了身份证,银行卡号,开户名就作校验,新增时作校验
				Map<String, String> maps=openPlatformService.doAuthen(agent.getAccountNo(),agent.getAccountName(),agent.getIdCardNo(),null);
				boolean flag = "00".equalsIgnoreCase(maps.get("errCode"));
				if (!flag) {
					log.info("身份证验证失败");
					throw new AgentWebException("开户名、身份证、银行卡号不匹配!");
				}
			}
		}
		if (agent.getId() == null || StringUtils.isBlank(agent.getAgentNo())) {
			throw new AgentWebException("代理商信息无效，请刷新重新填写！");
		}
		if (agentInfoDao.existAgentByMobilephoneAndTeamId(agent) > 0) {
			throw new AgentWebException("该组织下的手机号码或者邮箱或者代理商名称已存在!");
		}
		// 人人代理,更新身份证号,唯一t
		if ("11".equals(info.getAgentType())) {
			/*
			 * AgentInfo oldAgentInfo = agentInfoDao.selectByAgentNo(agent.getAgentNo());
			 * String idCardNo = agent.getIdCardNo(); if (StringUtils.isBlank(idCardNo)) {
			 * throw new AgentWebException("身份证号不能为空"); } if
			 * (!idCardNo.equals(oldAgentInfo.getIdCardNo())) { if
			 * (agentInfoDao.selectByIdCardNo(idCardNo) > 0 ||
			 * perAgentDao.selectByIdCardNo(idCardNo) > 0) { throw new
			 * AgentWebException("该身份证号码已存在"); } }
			 */
			checkFourInfomation(principal, agent);
		}
		if (agentInfoDao.updateAgent(agent) < 1) {
			throw new AgentWebException("代理商信息更新失败，请刷新重新填写");
		}
		agent.setParentId(info.getAgentNo());
		return agent;
	}

	/**
	 * 下级代理商的分润信息不得低于上级代理商的分润信息
	 *
	 * @param parentAgentShareRule 上级代理商的分润信息
	 * @param rule                 下级代理商的分润信息
	 */
	private void isChildrenRuleLessThanParent(AgentShareRule parentAgentShareRule, AgentShareRule rule) {
		// 1. 判断上下级的分润类型是否一致(大类),profitType必须是5
		log.info("parentAgentShareRule: " + parentAgentShareRule);
		log.info("rule: " + rule);
		if (parentAgentShareRule.getProfitType() != 5) {
			throw new AgentWebException("代理商(" + parentAgentShareRule.getAgentNo() + ")服务("
					+ parentAgentShareRule.getServiceId() + ")的分润类型不是固定成本类型");
		}
		// 2. 判断上下级的分润类型是否一致(小类),必须都是固定金额,或者必须都是固定扣率
		if (!StringUtils.equals(parentAgentShareRule.getCostRateType(), rule.getCostRateType())) {
			throw new AgentWebException("代理商(" + parentAgentShareRule.getAgentNo() + ")服务("
					+ parentAgentShareRule.getServiceId() + ")与下级代理商(" + rule.getAgentNo() + ")分润类型不一致");
		}
		// 3. 如果都是固定金额,则进行比较
		if (StringUtils.equals(parentAgentShareRule.getCostRateType(), "1")) { // 1-每笔固定金额，
			if (rule.getPerFixCost().compareTo(parentAgentShareRule.getPerFixCost()) < 0) {
				throw new AgentWebException("代理商(" + rule.getAgentNo() + ")" + rule.getServiceName() + "的分润成本 "
						+ rule.getPerFixCost() + "元比上级代理商(" + parentAgentShareRule.getAgentNo() + ")的成本"
						+ parentAgentShareRule.getPerFixCost().setScale(4) + " 元低");
			}
		} else if (StringUtils.equals(parentAgentShareRule.getCostRateType(), "2")) { // 2 扣率
			// 4. 否则都是固定扣率,则进行比较
			if (rule.getCostRate().compareTo(parentAgentShareRule.getCostRate()) < 0) {
				throw new AgentWebException("代理商(" + rule.getAgentNo() + ")" + rule.getServiceName() + "的分润成本 "
						+ rule.getCostRate() + "%比上级代理商(" + parentAgentShareRule.getAgentNo() + ")的成本"
						+ parentAgentShareRule.getCostRate().setScale(4) + " %低");
			}
		} else { // 其他异常
			// 5. 不是固定金额,也不是固定扣率,则抛出异常
			throw new AgentWebException("代理商(" + rule.getAgentNo() + ")" + rule.getServiceName() + "的分润成本与上级代理商("
					+ parentAgentShareRule.getAgentNo() + ")的类型不一致");
		}
	}

	@Override
	public Map<String, Object> getNewAgentServices(Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String agentId = (String) map.get("agentNo");
			@SuppressWarnings("unchecked")
			List<Integer> ids = (List<Integer>) map.get("bpIds");
			if (StringUtils.isNotBlank(agentId)) {
				List<ServiceRate> list = agentInfoDao.getNewServiceRate(ids, agentId);
				for (ServiceRate r : list) {
					r.setMerRate(serviceProService.profitExpression(r));
				}
				result.put("rates", list);
			}
		} catch (Exception e) {
			log.error("查询代理商的代理业务产品对应的所有的服务费率和服务额度异常！", e);
		}
		return result;
	}

	private AgentInfo updateAgentBaseInfoNoSafePhone(JSONObject json) {
		AgentInfo agent = JSONObject.parseObject(json.getString("agentInfo"), AgentInfo.class);
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//		agent.setTeamId(Integer.valueOf(Constants.TEAM_ID));
		agent.setTeamId(info.getTeamId());
		if (agent.getId() == null || StringUtils.isBlank(agent.getAgentNo())) {
			throw new AgentWebException("代理商信息无效，请刷新重新填写！");
		}
		if (agentInfoDao.existAgentByMobilephoneAndTeamId(agent) > 0) {
			throw new AgentWebException("该组织下的手机号码或者邮箱或者代理商名称已存在!");
		}
		// 人人代理,更新身份证号,唯一t
		if ("11".equals(info.getAgentType())) {
			/*
			 * AgentInfo oldAgentInfo = agentInfoDao.selectByAgentNo(agent.getAgentNo());
			 * String idCardNo = agent.getIdCardNo(); if (StringUtils.isBlank(idCardNo)) {
			 * throw new AgentWebException("身份证号不能为空"); } if
			 * (!idCardNo.equals(oldAgentInfo.getIdCardNo())) { if
			 * (agentInfoDao.selectByIdCardNo(idCardNo) > 0 ||
			 * perAgentDao.selectByIdCardNo(idCardNo) > 0) { throw new
			 * AgentWebException("该身份证号码已存在"); } }
			 */
			checkFourInfomation(principal, agent);
		}
		if (agentInfoDao.updateAgentNoSafePhone(agent) < 1) {
			throw new AgentWebException("代理商信息更新失败，请刷新重新填写");
		}
		agent.setParentId(info.getAgentNo());
		return agent;
	}

	/**
	 * 通过组织过滤出业务产品
	 */
	@Override
	public List<JoinTable> selectProductByTeamId(Integer id) {
		Map<String, Object> map = new HashMap<>();
		map.put("teamId", id);
		return businessProductDefineDao.getProducesByCondition(map);
	}

	@Override
	public List<AgentInfo> selectChildAgentByAgentNode(String agentNode, Integer agentLevel) {
		return agentInfoDao.selectChildAgentByAgentNode(agentNode, agentLevel);
	}

	@Override
	public List<AgentInfo> selectAllInfoByTeamId(String teamId) {
		return agentInfoDao.selectAllInfoByTeamId(teamId);
	}

	@Override
	public Map<String, Object> queryMyInfo(String agentNo, String oneAgentNo, Integer teamId) {
		Map<String, Object> map = new HashMap<>();
		AgentInfo curInfo = this.getCurAgentInfo();
		AgentInfo info = this.selectByagentNo(agentNo);
		if (info != null) {
			map.put("agentInfo", info);
//			查询业务产品
			List<JoinTable> bps = businessProductDefineDao.getAgentProducts(info.getAgentNo());
			map.put("agentProducts", bps);
//			查询代理商的分润信息
			map.put("agentShare", this.getAgentShareInfos(agentNo));
//			查询代理商的费率
			List<Integer> bpIds = new ArrayList<>();
			for (JoinTable t : bps) {
				bpIds.add(t.getKey1());
			}
			map.put("agentRate", this.getServiceRate(bpIds, oneAgentNo));
//			查询代理商的额度
			// 欢乐返活动
			List<AgentActivity> list = agentInfoDao.selectHappyBackActivity(agentNo);
			// 获取当前代理商支持的层级
			SupportRankDto supportRank = this.getSupportRank(curInfo.getAgentOem());
			for (AgentActivity agentActivity : list) {
				agentActivity.setShowFullPrizeAmount((info.getFullPrizeSwitch() == 1 || info.getAgentLevel() == 1)
						&& info.getAgentLevel() <= supportRank.getFullPrizeLevel() + 1
						&& !"11".equals(info.getAgentType()));
				agentActivity
						.setShowNotFullDeductAmount((info.getNotFullDeductSwitch() == 1 || info.getAgentLevel() == 1)
								&& info.getAgentLevel() <= supportRank.getNotFullDeductLevel() + 1
								&& !"11".equals(info.getAgentType()));
			}
			map.put("happyBackList", list);
			if (bpIds != null && !bpIds.isEmpty()) {
				map.put("agentQouta", agentInfoDao.getServiceQuota(bpIds, oneAgentNo));
			}
		}
		return map;
	}

	@Override
	public String getOneAgentNo(String agentNo) {
		return agentInfoDao.getOneAgentNo(agentNo);
	}

	@Override
	public AgentInfo selectByParentId(String parentId) {
		return agentInfoDao.selectByParentId(parentId);
	}

	@Override
	public Map<String, Object> getMyAccount(String entityId) {
		Map<String, Object> resultMap = new HashMap<>();
		String profitAmount = agentInfoDao.selectProfitToday(entityId);
		resultMap.put("profitAmount", profitAmount);

		// 代理商余额查询
		resultMap.put("account", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224105")));
		// 欢乐返账户余额查询
		resultMap.put("happyAccount", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224106")));
		// 代理商余额查询
		resultMap.put("creditAccount", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224121")));
		ServiceQuota serviceQuota = serviceDao.queryHlsServiceQuota();
		if (serviceQuota != null && serviceQuota.getServiceId() != null) {
			resultMap.put("accountService", serviceDao.queryServiceInfo(serviceQuota.getServiceId()));
		}
		// 人人代理 收款分润账户余额相关
		String perAgentServiceId1 = sysDictService.SelectServiceId("ACCOUNT_FEE_5");
		if (StringUtils.isNotBlank(perAgentServiceId1)) {
			resultMap.put("perAgentAccountService", serviceDao.queryServiceInfo(Long.parseLong(perAgentServiceId1)));
		}
		// 人人代理 机具款项账户余额相关
		String perAgentServiceId3 = sysDictService.SelectServiceId("ACCOUNT_FEE_17");
		if (StringUtils.isNotBlank(perAgentServiceId3)) {
			resultMap.put("terminalAccountService", serviceDao.queryServiceInfo(Long.parseLong(perAgentServiceId3)));
		}

		// 积分兑换余额查询
		resultMap.put("redemBalance", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224120")));
		// 积分兑换激活版余额查询
		resultMap.put("redemActiveBalance", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224123")));
		// 机具款项余额查询
		resultMap.put("terminalAccount", JSON.parse(ClientInterface.getAgentAccountBalance(entityId, "224124")));

		ServiceQuota cashServiceQuota = serviceDao.queryCashServiceQuota();
		if (cashServiceQuota != null && cashServiceQuota.getServiceId() != null) {
			resultMap.put("account2Service", serviceDao.queryServiceInfo(serviceQuota.getServiceId()));
		}
		ProviderBean providerBean = providerDao.queryRepayServiceCost(entityId);
		if (providerBean != null) {
			resultMap.put("hasReplayAccount", true);
			String agentAccountBalance = ClientInterface.getAgentAccountBalance(entityId, "224114");
			resultMap.put("replayAccount", JSON.parse(agentAccountBalance));
		} else {
			resultMap.put("hasReplayAccount", false);
		}
		String superBankShareId = sysDictDao.SelectServiceId("SUPER_BANK_SHARE_ID");
		log.info("超级银行家分润提现subjectNo:" + superBankShareId);
		String replayAccountSuperBank = ClientInterface.getAgentAccountBalance(entityId, superBankShareId);
		log.info("超级银行家分润提现返回:" + replayAccountSuperBank);
		resultMap.put("replayAccountSuperBank", JSON.parse(replayAccountSuperBank));
		return resultMap;
	}

	@Override
	public AgentInfo selectLevelOne(Integer userId) {
		return agentInfoDao.selectLevelOne(userId);
	}

	@Override
	public int updateRestPwd(String mobilephone, String teamId) {
		UserInfo user = agentInfoDao.selectByCons(mobilephone, teamId);
		if (user != null) {
			user.setUpdatePwdTime(new Date());
			user.setTeamId(teamId);
			Md5PasswordEncoder passEnc = new Md5PasswordEncoder();
			user.setPassword(passEnc.encodePassword(sysDictDao.selectRestPwd().getSysValue(), user.getMobilephone()));
		} else {
			return 0;
		}
		return agentInfoDao.updateRestPwd(user);
	}

	// @Override
//	public boolean findFunctionManage(String functionNumber) {
//		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String agentNo = principal.getUserEntityInfo().getEntityId();
//		Map<String, Object> functionMap = agentInfoDao.findFunctionManage(functionNumber);
//		String functionSwitch = (String) functionMap.get("function_switch");
//		String agentControl = (String) functionMap.get("agent_control");
//		if ("1".equals(functionSwitch)&&"1".equals(agentControl)) {
//			Map<String, Object> agentFunctionManage = this.findActivityIsSwitch(functionNumber,agentNo);
//			if (agentFunctionManage != null) {
//				return true;
//			}
//		}else if ("1".equals(functionSwitch)&&"0".equals(agentControl)) {
//			return true;
//		}
//		return false;
//	}
	@Override
	public boolean findFunctionManage() {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
		if (agentInfo.getAgentLevel() == 1) {
			return true;
		}
		return false;
	}

	@Override
	public Map<String, Object> findActivityIsSwitch(String functionNumber, String agentNo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo info = agentInfoService.selectByagentNo(principal.getUserEntityInfo().getEntityId());
		return agentInfoDao.findActivityIsSwitch(functionNumber, agentNo, info.getTeamId());
	}

	@Override
	public List<Map<String, String>> selectSubjectNo() {
		return agentInfoDao.selectSubjectNo();
	}

	@Override
	public Map<String, Object> UpdateWithDrawCash(String money) {
		String subType = "4";// 提现类型
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo eitityAgentInfo = agentInfoDao.selectByAgentNo(entityId);
		BigDecimal moneyNum = new BigDecimal(money).setScale(2, RoundingMode.HALF_UP);
		// 查询配置提现服务限额条件
		ServiceQuota serviceQuota = serviceDao.queryCashServiceQuota();
		if (serviceQuota == null) {
			log.info("提现服务限额为空");
			throw new AgentWebException("提现服务限额为空");
		}
		if (moneyNum.compareTo(serviceQuota.getSingleMinAmount()) == -1) {
			log.info("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
			throw new AgentWebException("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
		}

		// 判断该代理商是否存在已经提交提现的
		int count = agentInfoDao.findWithDrawCash(entityId, subType);
		if (count != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		String subjectNo = "224106";
		String accout = ClientInterface.getAgentAccountBalance(entityId, subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout != null) {
			JSONObject json = JSON.parseObject(accout);
			String retainAmount = limitRetainAmount(entityId, eitityAgentInfo);// 留存金额控制
			if (!json.getBooleanValue("status") || moneyNum
					.compareTo(json.getBigDecimal("avaliBalance").subtract(new BigDecimal(retainAmount))) == 1) {
				log.info("欢乐送补贴,调用记账接口返回false,错误信息:" + json.getString("msg"));
				throw new AgentWebException("输入金额超过可提现金额，请重新输入");
			}
		} else {
			log.info("=======>>>>查询余额接口返回空");
			throw new AgentWebException("提现提交失败");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',peragent人人代理
		map.put("createUser", principal.getUserEntityInfo().getUserId());// 创建人
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			map.put("sourceSystem", "peragent");
		}
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", entityId);// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", money);// 金额
		map.put("agentNode", agentInfo.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商)
		// 如果为人人代理，需要保存人人代理税费
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			SysDict sysDict = sysDictDao.getByKey("PER_AGENT_FEE");
			BigDecimal fee = new BigDecimal(sysDict.getSysValue());
			BigDecimal perAgentFee = moneyNum.multiply(fee).setScale(2, RoundingMode.HALF_UP);
			map.put("perAgentFee", perAgentFee);
		} else {
			map.put("perAgentFee", null);
		}
		int i = agentInfoDao.insertWithDrawCash(map);
		if (i > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String xiaom = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + xiaom);
				}
			}).start();
			Map<String, Object> msg = new HashMap<>();
			msg.put("msg", "提现提交成功");
			msg.put("status", true);
			log.info("欢乐送补贴提现提交成功,提现ID:" + map.get("settle_order"));
			return msg;
		} else {
			log.info("=========================>插入出款订单失败");
			throw new AgentWebException("提现提交失败");
		}

	}

	private String limitRetainAmount(String entityId, AgentInfo eitityAgentInfo) {
		String retainAmount = "0";
		if (eitityAgentInfo.getAgentLevel() == 1) {
			Map<String, Object> defaultMap = selectDefaultStatus();
			String defaultStatus = defaultMap.get("status").toString();// 默认总开关的状态
			if ("1".equals(defaultStatus)) {
				Map<String, Object> map = this.selectAccountStatus(entityId);// 可提现金额 = 账户可用余额 - 留存金额
				if (map != null) {
					if ("1".equals(map.get("status").toString())) {
						retainAmount = map.get("retain_amount") == null ? "0" : map.get("retain_amount").toString();
					} else {
						retainAmount = "0";
					}
				} else {
					retainAmount = defaultMap.get("retain_amount") == null ? "0"
							: defaultMap.get("retain_amount").toString();
				}
			}
		}
		return retainAmount;
	}

	@Override
	public Map<String, Object> UpdateTakeBalance(String money) {
		String subType = "5";// 提现类型
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		BigDecimal moneyNum = new BigDecimal(money).setScale(2, RoundingMode.HALF_UP);
		// 查询配置提现服务限额条件
		ServiceQuota serviceQuota = serviceDao.queryHlsServiceQuota();
		if (serviceQuota == null) {
			log.info("提现服务限额为空");
			throw new AgentWebException("提现服务限额为空");
		}
		if (moneyNum.compareTo(serviceQuota.getSingleMinAmount()) == -1) {
			log.info("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
			throw new AgentWebException("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
		}

		// 判断该代理商是否存在已经提交提现的
		int count = agentInfoDao.findWithDrawCash(entityId, subType);
		if (count != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		String subjectNo = "224105";// 账户余额提现
		String accout = ClientInterface.getAgentAccountBalance(entityId, subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout != null) {
			JSONObject json = JSON.parseObject(accout);
//			if(!json.getBooleanValue("status")||moneyNum.compareTo(json.getBigDecimal("avaliBalance"))==1){
			AgentInfo eitityAgentInfo = agentInfoDao.selectByAgentNo(entityId);
			if (!"11".equals(eitityAgentInfo.getAgentType())) {
				log.info("subjectNo = 224105 , 提现金额为===>" + moneyNum + ",可用余额为 ===> "
						+ json.getBigDecimal("avaliBalance"));
				if (!json.getBooleanValue("status") || moneyNum.compareTo(json.getBigDecimal("avaliBalance")) == 1) {
					log.info("账户余额提现,调用记账接口返回" + json.getBooleanValue("status") + ",错误信息:" + json.getString("msg"));
					throw new AgentWebException("提现提交失败");
				}
			} else {// 超级盟主分润提现
				// 提现手续费计算
				String fee = sysDictService.getByKey("PER_AGENT_FEE").getSysValue();
				String perAgentServiceId1 = sysDictService.SelectServiceId("ACCOUNT_FEE_5");// 在数据字典中查出分润提现服务ID
				Map<String, Object> map = new HashMap<>();
				map.put("single_num_amount", new BigDecimal("0"));
				if (StringUtils.isNotBlank(perAgentServiceId1)) {
					Map<String, Object> rateMap = agentInfoService.getSingleNumAmount(perAgentServiceId1);// 根据代理商提现服务ID查询到费率
					log.info("根据提现服务ID " + perAgentServiceId1 + " 查询到费率 ==>" + rateMap);
					if (rateMap != null) {
						map = agentInfoService.getRateSingleNumAmount(money, rateMap);
					}
				}
				BigDecimal perAgentProfitSingleAmount = (BigDecimal) map.get("single_num_amount");
				BigDecimal perAgentProfitTotalFee = perAgentProfitSingleAmount
						.add(new BigDecimal(money).multiply(new BigDecimal(fee))).setScale(2, RoundingMode.HALF_UP);
				String retainAmount = limitRetainAmount(entityId, eitityAgentInfo);// 留存金额控制
				log.info("subjectNo = 224105 留存金额为===>" + retainAmount + ", 提现金额为===>" + moneyNum + ",可用余额为 ===> "
						+ json.getBigDecimal("avaliBalance") + ",提现手续费为====>" + perAgentProfitTotalFee);
				if (!json.getBooleanValue("status") || moneyNum
						.compareTo(json.getBigDecimal("avaliBalance").subtract(new BigDecimal(retainAmount))) == 1) {
					log.info("账户余额提现,调用记账接口返回" + json.getBooleanValue("status") + ",错误信息:" + json.getString("msg"));
					throw new AgentWebException("提现提交失败,提现金额大于可用余额");
				}
				if (moneyNum.compareTo(perAgentProfitTotalFee) < 1) {
					throw new AgentWebException("提现提交失败,提现金额不能小于提现手续费");
				}
			}
		} else {
			log.info("=======>>>>查询余额接口返回空");
			throw new AgentWebException("提现提交失败");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',
		map.put("createUser", principal.getUserEntityInfo().getUserId());// 创建人
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			map.put("sourceSystem", "peragent");
		}
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", entityId);// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", money);// 金额
		map.put("agentNode", agentInfo.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商,5:账户余额提现)
		// 如果为人人代理，需要保存人人代理税费
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			SysDict sysDict = sysDictDao.getByKey("PER_AGENT_FEE");
			BigDecimal fee = new BigDecimal(sysDict.getSysValue());
			BigDecimal perAgentFee = moneyNum.multiply(fee).setScale(2, RoundingMode.HALF_UP);
			map.put("perAgentFee", perAgentFee);
		} else {
			map.put("perAgentFee", null);
		}
		int i = agentInfoDao.insertWithDrawCash(map);
		if (i > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(501);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String xiaom = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + xiaom);
				}
			}).start();
			Map<String, Object> msg = new HashMap<>();
			msg.put("msg", "提现提交成功");
			msg.put("status", true);
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			return msg;
		} else {
			log.info("=========================>账户余额提现-插入出款订单失败");
			throw new AgentWebException("提现提交失败");
		}
	}

	/**
	 * 机具款项账户余额提现
	 *
	 * @return
	 */
	@Override
	public Map<String, Object> takeTerminalBalance(String moneyStr, String subjectNo, String subType) {
		Map<String, Object> msg = new HashMap<>();
		// 判断财务时候在入账
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}

		// 校验输入的提现金额时候合法
		if (StringUtils.isBlank(moneyStr) || !moneyStr.matches("^\\d+(\\.\\d+)?$")) {
			throw new AgentWebException("请输入正确的提现金额");
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		BigDecimal moneyDecimal = new BigDecimal(moneyStr).setScale(2, RoundingMode.HALF_UP);

		// 获取配置的提现服务id
		SysDict serviceDict = sysDictDao.getByKey("ACCOUNT_FEE_" + subType);
		if (serviceDict == null || StringUtils.isBlank(serviceDict.getSysValue())) {
			log.error(String.format("提现服务%s没有配置", "ACCOUNT_FEE_" + subType));
			throw new AgentWebException("提现服务没有配置");
		}
		long serviceId;
		try {
			serviceId = Long.valueOf(serviceDict.getSysValue());
		} catch (Exception e) {
			log.error(String.format("提现服务%s配置有误%s", "ACCOUNT_FEE_" + subType, serviceDict.getSysValue()));
			throw new AgentWebException("提现服务配置有误");
		}

		// 获取提现服务限额
		ServiceQuota serviceQuota = serviceDao.selectServiceQuota(serviceId, "0");
		serviceQuate(moneyDecimal, serviceQuota);

		// 判断该代理商是否存在已经提交提现的
		if (agentInfoDao.findWithDrawCash(entityId, subType) != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		// 判断(提现金额加手续费)是否大于账户系统的可用余额
		// 机具款项
		String perAgentServiceId3 = sysDictService.SelectServiceId("ACCOUNT_FEE_17");
		Map<String, Object> singleAmountMap = new HashMap<>();
		if (StringUtils.isNotBlank(perAgentServiceId3)) {
			Map<String, Object> rateMap = getSingleNumAmount(perAgentServiceId3);// 根据代理商提现服务ID查询到费率
			if (rateMap != null) {
				singleAmountMap = getRateSingleNumAmount(moneyDecimal.toString(), rateMap);
			}
		}
		String accout = ClientInterface.getAgentAccountBalance(entityId, subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout == null) {
			throw new AgentWebException("提现提交失败");
		}
		JSONObject json = JSON.parseObject(accout);
		boolean booleanValue = json.getBooleanValue("status");
		BigDecimal perAgentTerminalSingleAmount = new BigDecimal("0");
		if (singleAmountMap != null) {
			perAgentTerminalSingleAmount = new BigDecimal(singleAmountMap.get("single_num_amount") == null ? "0"
					: singleAmountMap.get("single_num_amount").toString());
		}
		if (!booleanValue || moneyDecimal.compareTo(json.getBigDecimal("avaliBalance")) == 1) {
			log.info("账户余额提现,调用记账接口返回" + booleanValue + ",错误信息:" + json.getString("msg") + ",提现金额为 ==>" + moneyDecimal
					+ ",提现手续费为===>" + perAgentTerminalSingleAmount);
			throw new AgentWebException("提现提交失败,提现金额大于可用余额");
		}
		if (moneyDecimal.compareTo(perAgentTerminalSingleAmount) < 1) {
			throw new AgentWebException("提现提交失败,提现金额不能小于提现手续费");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);

		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',
		if (Constants.PER_AGENT_TEAM_ID.equals(principal.getTeamId())) {
			map.put("sourceSystem", "peragent");
		}
		map.put("createUser", principal.getUserEntityInfo().getUserId());// 创建人
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", entityId);// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", moneyStr);// 金额
		map.put("agentNode", agentInfo.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商,5:账户余额提现,13 积分兑换提现)
		if (agentInfoDao.insertWithDrawCash(map) > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(502);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String result = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + result);
				}
			}).start();
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			msg.put("msg", "提现提交成功");
			msg.put("status", true);
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			return msg;
		} else {
			log.info("=========================>账户余额提现-插入出款订单失败");
			throw new AgentWebException("提现操作失败");
		}
	}

	@Override
	public boolean takeRedemBalance(String moneyStr) {
		return commonWithdrawCash(moneyStr, "224120", "13");
	}

	@Override
	public boolean takeRedemActiveBalance(String moneyStr) {
		return commonWithdrawCash(moneyStr, "224123", "16");
	}

	/**
	 * 通用提现服务
	 *
	 * @param moneyStr  提现金额
	 * @param subjectNo 账户科目编号
	 * @param subType   出款子类型
	 */
	@Override
	public boolean commonWithdrawCash(String moneyStr, String subjectNo, String subType) {
		// 超级还提现即信用卡余额提现和卡管家提现加上上游提现金额限制
		return commonWithdrawCash(moneyStr, subjectNo, subType, null);
	}

	@Override
	public boolean commonWithdrawCash(String moneyStr, String subjectNo, String subType, WithDrawExtraCheck check) {
		// 判断财务时候在入账
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}

		// 校验输入的提现金额时候合法
		if (StringUtils.isBlank(moneyStr) || !moneyStr.matches("^\\d+(\\.\\d+)?$")) {
			throw new AgentWebException("请输入正确的提现金额");
		}
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		BigDecimal moneyDecimal = new BigDecimal(moneyStr).setScale(2, RoundingMode.HALF_UP);

		// 获取配置的提现服务id
		SysDict serviceDict = sysDictDao.getByKey("ACCOUNT_FEE_" + subType);
		if (serviceDict == null || StringUtils.isBlank(serviceDict.getSysValue())) {
			log.error(String.format("提现服务%s没有配置", "ACCOUNT_FEE_" + subType));
			throw new AgentWebException("提现服务没有配置");
		}
		long serviceId;
		try {
			serviceId = Long.valueOf(serviceDict.getSysValue());
		} catch (Exception e) {
			log.error(String.format("提现服务%s配置有误%s", "ACCOUNT_FEE_" + subType, serviceDict.getSysValue()));
			throw new AgentWebException("提现服务配置有误");
		}

		// 获取提现服务限额
		ServiceQuota serviceQuota = serviceDao.selectServiceQuota(serviceId, "0");
		serviceQuate(moneyDecimal, serviceQuota);

		// 判断该代理商是否存在已经提交提现的
		if (agentInfoDao.findWithDrawCash(entityId, subType) != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		// 判断提现金额时候大于账户系统的可用余额
		String accout = ClientInterface.getAgentAccountBalance(entityId, subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout == null) {
			throw new AgentWebException("提现提交失败");
		}
		JSONObject json = JSON.parseObject(accout);
		if (!json.getBooleanValue("status") || moneyDecimal.compareTo(json.getBigDecimal("avaliBalance")) > 0) {
			log.info("账户余额提现,调用记账接口返回false,错误信息:" + json.getString("msg"));
			throw new AgentWebException("提现失败,提现金额大于可用余额");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);

		if (check != null) {
			check.verification(moneyDecimal, json.getBigDecimal("avaliBalance"), agentInfo);
		}

		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',
		map.put("createUser", principal.getUserEntityInfo().getUserId());// 创建人
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", entityId);// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", moneyStr);// 金额
		map.put("agentNode", agentInfo.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商,5:账户余额提现,13 积分兑换提现)
		if (agentInfoDao.insertWithDrawCash(map) > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(502);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String result = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + result);
				}
			}).start();
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			return true;
		} else {
			log.info("=========================>账户余额提现-插入出款订单失败");
			throw new AgentWebException("提现操作失败");
		}
	}

	@Override
	public boolean commonWithdrawCashApi(String moneyStr, String subjectNo, String subType, String entityId,
			String userId, WithDrawExtraCheck check) {
		// 判断财务时候在入账
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}

		// 校验输入的提现金额时候合法
		if (StringUtils.isBlank(moneyStr) || !moneyStr.matches("^\\d+(\\.\\d+)?$")) {
			throw new AgentWebException("请输入正确的提现金额");
		}
		BigDecimal moneyDecimal = new BigDecimal(moneyStr).setScale(2, RoundingMode.HALF_UP);

		// 获取配置的提现服务id
		SysDict serviceDict = sysDictDao.getByKey("ACCOUNT_FEE_" + subType);
		if (serviceDict == null || StringUtils.isBlank(serviceDict.getSysValue())) {
			log.error(String.format("提现服务%s没有配置", "ACCOUNT_FEE_" + subType));
			throw new AgentWebException("提现服务没有配置");
		}
		long serviceId;
		try {
			serviceId = Long.valueOf(serviceDict.getSysValue());
		} catch (Exception e) {
			log.error(String.format("提现服务%s配置有误%s", "ACCOUNT_FEE_" + subType, serviceDict.getSysValue()));
			throw new AgentWebException("提现服务配置有误");
		}

		// 获取提现服务限额
		ServiceQuota serviceQuota = serviceDao.selectServiceQuota(serviceId, "0");
		serviceQuate(moneyDecimal, serviceQuota);

		// 判断该代理商是否存在已经提交提现的
		if (agentInfoDao.findWithDrawCash(entityId, subType) != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		// 判断提现金额时候大于账户系统的可用余额
		String accout = ClientInterface.getAgentAccountBalance(entityId, subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout == null) {
			throw new AgentWebException("提现提交失败");
		}
		JSONObject json = JSON.parseObject(accout);
		if (!json.getBooleanValue("status") || moneyDecimal.compareTo(json.getBigDecimal("avaliBalance")) > 0) {
			log.info("账户余额提现,调用记账接口返回false,错误信息:" + json.getString("msg"));
			throw new AgentWebException("提现失败,提现金额大于可用余额");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);

		if (check != null) {
			check.verificationApi(moneyDecimal, json.getBigDecimal("avaliBalance"), agentInfo);
		}

		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',
		map.put("createUser", userId);// 创建人
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", entityId);// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", moneyStr);// 金额
		map.put("agentNode", agentInfo.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商,5:账户余额提现,13 积分兑换提现)
		if (agentInfoDao.insertWithDrawCash(map) > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(502);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String result = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + result);
				}
			}).start();
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			return true;
		} else {
			log.info("=========================>账户余额提现-插入出款订单失败");
			throw new AgentWebException("提现操作失败");
		}
	}

	@Override
	public Map<String, Object> UpdateTakeReplayBalance(String money, String type) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo loginAgent = this.selectByPrincipal();
		BigDecimal moneyNum = new BigDecimal(money).setScale(2, RoundingMode.HALF_UP);
		String accountantShareAccounting = sysDictDao.getAccountantShareAccounting();
		String withdrawMoneyMin = "0";
		if (!StringUtils.equals(accountantShareAccounting, "0")) {
			throw new AgentWebException("财务正在进行分润入账，请稍后再来提现");
		}
		String subType = "";// 提现类型
		String subjectNo = "";// 账户余额提现
		ServiceQuota serviceQuota = new ServiceQuota();
		if ("takeReplayBalance".equals(type)) {
			subType = "9";
			subjectNo = "224114";
			// 查询配置提现服务限额条件
			serviceQuota = serviceDao.queryHlsServiceQuota();
			serviceQuate(moneyNum, serviceQuota);
		} else if ("takeReplayBalanceSuperBank".equals(type)) {// 超级银行家分润提现
			withdrawMoneyMin = orgInfoDao.selectWithdrawMoneyMinByOrgId(principal.getUserEntityInfo().getEntityId());
			subType = "11";
			subjectNo = sysDictDao.SelectServiceId("SUPER_BANK_SHARE_ID");
			// 查询配置提现服务限额条件
			serviceQuota = serviceDao.queryHlsServiceQuotaSuperBank("ACCOUNT_FEE_11");
			serviceQuate(moneyNum, serviceQuota);
		}
		// 判断该代理商是否存在已经提交提现的
		int count = agentInfoDao.findWithDrawCash(loginAgent.getAgentNo(), subType);
		if (count != 0) {
			log.info("存在已经提交提现的订单");
			throw new AgentWebException("存在已提交提现订单");
		}
		String accout = ClientInterface.getAgentAccountBalance(loginAgent.getAgentNo(), subjectNo);
		log.info("===============>调用查余额接口:" + accout);
		if (accout != null) {
			JSONObject json = JSON.parseObject(accout);
			BigDecimal avaliBalance = json.getBigDecimal("avaliBalance");
			if (((avaliBalance.subtract(new BigDecimal(withdrawMoneyMin))).subtract(moneyNum)).doubleValue() < 0) {
				log.info("由于退代理费和由于普通用户办理信用卡办理贷款红包，都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
				throw new AgentWebException(
						"由于退代理费和由于普通用户办理信用卡办理贷款红包，" + "都要从OEM分润账户中扣钱，本账户只能提走余额超过" + withdrawMoneyMin + "元的部分");
			}
			if (!json.getBooleanValue("status") || moneyNum.compareTo(avaliBalance) == 1) {
				log.info("信用卡账户余额提现,调用记账接口返回false,错误信息:" + json.getString("msg"));
				throw new AgentWebException("信用卡提现提交失败");
			}
		} else {
			log.info("=======>>>>查询余额接口返回空");
			throw new AgentWebException("提现提交失败");
		}
		final Map<String, Object> map = new HashMap<>();
		map.put("createTime", new Date());// 创建时间
		map.put("settleType", "2");// 结算类型 1T0交易；2手工提现；3T1线上代付；4T1线下代付',
		map.put("sourceSystem", "agentweb");// 来源系统 agentweb交易系统 account账户系统 boss运营系统',
		map.put("createUser", principal.getUserEntityInfo().getUserId());// 创建人
		map.put("settleUserType", "A");// 用户类型M代表商户 A代表代理商
		map.put("settleUserNo", loginAgent.getAgentNo());// 用户号
		map.put("settleStatus", "0");
		map.put("synStatus", "1");
		map.put("settleOrderStatus", "1");
		map.put("settleAmount", money);// 金额
		map.put("agentNode", loginAgent.getAgentNode());// 代理商节点
		map.put("holidaysMark", "0");// 假日标志:1-只工作日，2-只节假日，0-不限
		map.put("acqenname", "neweptok");// 出款通道,银盛
		map.put("subType", subType);// 提现类型(1:手刷,2:实体商户,3：欢乐送商户,4:欢乐送代理商,5:账户余额提现)
		int i = agentInfoDao.insertWithDrawCash(map);
		if (i > 0) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(502);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 调用晓明出款接口
					String xiaom = ClientInterface.transfer(String.valueOf(map.get("settle_order")), "2", "2");
					log.info("===========>调用出款接口:" + xiaom);
				}
			}).start();
			Map<String, Object> msg = new HashMap<>();
			msg.put("msg", "信用卡提现提交成功");
			msg.put("status", true);
			log.info("账户余额提现提交成功,提现ID:" + map.get("settle_order"));
			return msg;
		} else {
			log.info("=========================>账户余额提现-插入出款订单失败");
			throw new AgentWebException("信用卡提现提交失败");
		}
	}

	/**
	 * 超级银行家提现限额
	 *
	 * @param moneyNum
	 * @param serviceQuota
	 */
	private void serviceQuate(BigDecimal moneyNum, ServiceQuota serviceQuota) {

		if (serviceQuota == null) {
			log.info("提现服务限额为空");
			throw new AgentWebException("提现服务限额为空");
		}
		log.info("当前提现服务id为:" + serviceQuota.getServiceId());
		if (moneyNum.compareTo(serviceQuota.getSingleMinAmount()) < 0) {
			throw new AgentWebException("最小提现金额不能小于" + serviceQuota.getSingleMinAmount() + "元.");
		}
		if (moneyNum.compareTo(serviceQuota.getSingleCountAmount()) > 0) {
			throw new AgentWebException("输入的提现金额不能大于最大交易金额 " + serviceQuota.getSingleCountAmount() + "元,请重新输入!");
		}

		if (moneyNum.compareTo(new BigDecimal("0")) <= 0) {
			throw new AgentWebException("提现金额不能低于0元.");
		}
//        if (moneyNum.compareTo(new BigDecimal(withdrawCashInfo.getAvaliBalance())) > 0){
//            throw new AgentWebException("提现余额大于可用余额.");
//        }
		if (!agentInfoDao.canWithdrawCash(serviceQuota.getServiceId())) {
			throw new AgentWebException("当前时段不能进行提现操作.");
		}
		if (moneyNum.compareTo(serviceQuota.getSingleMinAmount()) == -1) {
			log.info("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
			throw new AgentWebException("提现金额不能低于最小限额:" + serviceQuota.getSingleMinAmount());
		}
	}

	@Override
	public Map<String, Object> getSingleNumAmount(String serviceId) {
		return agentInfoDao.getSingleNumAmount(serviceId);
	}

	@Override
	public Map<String, Object> selectAllShareList(String param) {
		Map<String, Object> maps = new HashMap<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//		if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//			maps.put("bols", false);
//			maps.put("msg", "代理商未开户");
//			return maps;
//		}
		JSONObject json = JSON.parseObject(param);
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String statu = json.getString("statu");
		Integer pageNo = json.getInteger("pageNo");
		Integer pageSize = json.getInteger("pageSize");
		String selectAgentNo = json.getString("selectAgentNo");

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String st = sTime != null && !sTime.equals("") ? sdf.format(sTime) : "";
		String et = eTime != null && !eTime.equals("") ? sdf.format(eTime) : "";
		log.info("=================>>查询每日分润报表调用查询接口!");
		// 每日分润报表
		String selectShareByDay = ClientInterface.selectShareByDay(principal.getUserEntityInfo().getEntityId(),
				selectAgentNo, st, et, statu, pageNo, pageSize);
		String selectShareByDayCollection = ClientInterface.selectShareByDayCollection(
				principal.getUserEntityInfo().getEntityId(), selectAgentNo, st, et, statu, pageNo, pageSize);
		if (StringUtils.isNotBlank(selectShareByDay)) {
			JSONObject jsons1 = JSON.parseObject(selectShareByDay);
			JSONObject jsons2 = JSON.parseObject(jsons1.getString("data"));
			JSONArray list = jsons2.getJSONArray("list");
			List<Map> slist = new ArrayList<>();
			if (list != null) {
				slist = JSON.parseArray(list.toJSONString(), Map.class);
			}
			maps.put("bols", true);
			maps.put("list", slist);
			log.info("=================>>每日分润报表,调用接口返回" + slist.size() + "条数据");
			maps.put("total", jsons2.getString("total"));
			if (StringUtils.isNotBlank(selectShareByDayCollection)) {
				JSONObject resultJson = JSON.parseObject(selectShareByDayCollection);
				JSONObject dataJson = JSON.parseObject(resultJson.getString("data"));
				maps.put("collection", dataJson);
			}
		} else {
			maps.put("bols", false);
			maps.put("msg", "查询不到数据");
		}
		return maps;
	}

	@Override
	public Map<String, Object> preliminaryFreezeQuery(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//		if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//			data.put("bols", false);
//			data.put("msg", "代理商未开户");
//			return data;
//		}

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("freezeReason");
		Integer pageNo = json.getInteger("pageNo");
		Integer pageSize = json.getInteger("pageSize");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("freezeTime1", st);
		claims.put("freezeTime2", et);
		claims.put("freezeReason", freezeReason);
		log.info("=================>>代理商预冻结明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商预冻结明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.PRELIMINARY_FREEZE_QUERY_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		putData(data, dataStr);
		return data;
	}

	@Override
	public Map<String, Object> profitAdvanceQuery(String url,JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());


		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		//String freezeReason = json.getString("freezeReason");
		Integer pageNo = json.getInteger("pageNo");
		Integer pageSize = json.getInteger("pageSize");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("adjustTime1", st);
		claims.put("adjustTime2", et);
		claims.put("subjectNo", "224114");
		log.info("=================>>分润预调账明细查询,接口[url]:" + url);
		log.info("=================>>分润预调账明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>分润预调账明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(url, params).postRequest();
		log.info("接口返回:{}", dataStr);

		putData(data, dataStr);
		return data;
	}

	@Override
	public String preliminaryFreezeExport(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//        if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//            data.put("bols", false);
//            data.put("msg", "代理商未开户");
//            return null;
//        }

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("freezeReason");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("freezeTime1", st);
		claims.put("freezeTime2", et);
		claims.put("freezeReason", freezeReason);
		log.info("=================>>代理商预冻结明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		// params.put("page", String.valueOf(pageNo));
		// params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商预冻结明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.PRELIMINARY_FREEZE_EXPORT_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		dataStr = data2Str(dataStr);
		return dataStr;
	}


	@Override
	public String profitAdvanceExport(String url,JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());


		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("adjustTime1", st);
		claims.put("adjustTime2", et);
		claims.put("subjectNo", "224114");
		log.info("=================>>分润预调账明细明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		// params.put("page", String.valueOf(pageNo));
		// params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>分润预调账明细明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(url, params).postRequest();
		log.info("接口返回:{}", dataStr);

		dataStr = data2Str(dataStr);
		return dataStr;
	}

	@Override
	public Map<String, Object> unFreezeQuery(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//        if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//            data.put("bols", false);
//            data.put("msg", "代理商未开户");
//            return data;
//        }

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("freezeReason");
		Integer pageNo = json.getInteger("pageNo");
		Integer pageSize = json.getInteger("pageSize");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("unfreezeTime1", st);
		claims.put("unfreezeTime2", et);
		claims.put("freezeReason", freezeReason);
		log.info("=================>>代理商解冻明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商解冻明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.UN_FREEZE_QUERY_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		putData(data, dataStr);
		return data;
	}

	@Override
	public Map<String, Object> preliminaryAdjustQuery(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//        if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//            data.put("bols", false);
//            data.put("msg", "代理商未开户");
//            return data;
//        }

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("adjustReason");
		Integer pageNo = json.getInteger("pageNo");
		Integer pageSize = json.getInteger("pageSize");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("adjustTime1", st);
		claims.put("adjustTime2", et);
		claims.put("adjustReason", freezeReason);
		log.info("=================>>代理商预冻结明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		params.put("page", String.valueOf(pageNo));
		params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商预冻结明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.PRELIMINARY_ADJUST_QUERY_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		putData(data, dataStr);
		return data;
	}

	@Override
	public String preliminaryAdjustExport(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//        if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//            data.put("bols", false);
//            data.put("msg", "代理商未开户");
//            return null;
//        }

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("adjustReason");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("adjustTime1", st);
		claims.put("adjustTime2", et);
		claims.put("adjustReason", freezeReason);
		log.info("=================>>代理商预冻结明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		// params.put("page", String.valueOf(pageNo));
		// params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商预冻结明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.PRELIMINARY_ADJUST_EXPORT_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		dataStr = data2Str(dataStr);
		return dataStr;
	}

	private void putData(Map<String, Object> data, String dataStr) {
		if (StringUtils.isNotBlank(dataStr)) {
			data.put("bols", true);
			JSONObject resJson = JSON.parseObject(dataStr);
			dataStr = resJson.getString("data");
			resJson = JSON.parseObject(dataStr);
			data.put("collection", resJson);
		} else {
			data.put("bols", false);
			data.put("msg", "查询不到数据");
		}
	}

	@Override
	public String unFreezeExport(JSONObject json) {
		Map<String, Object> data = new HashMap<>();

		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		AgentInfo agent = this.selectByagentNo(principal.getUserEntityInfo().getEntityId());
//        if(agent.getHasAccount()==0){//是否已有账号：1已开，0否开
//            data.put("bols", false);
//            data.put("msg", "代理商未开户");
//            return null;
//        }

		String agent_no = agent.getAgentNo();
		Date sTime = json.getDate("sTime");
		Date eTime = json.getDate("eTime");
		String freezeReason = json.getString("freezeReason");

		String st = sdf.format(sTime == null ? new Date() : sTime);
		String et = sdf.format(eTime == null ? new Date() : eTime);

		final HashMap<String, Object> claims = ClientInterface.getClaims();
		claims.put("agentNo", agent_no);
		claims.put("unfreezeTime1", st);
		claims.put("unfreezeTime2", et);
		claims.put("freezeReason", freezeReason);
		log.info("=================>>代理商解冻明细查询,参数[claims]:" + claims);

		Map<String, String> params = new HashMap<>();
		params.put("token", new JWTSigner(Constants.ACCOUNT_API_SECURITY).sign(claims));
		// params.put("page", String.valueOf(pageNo));
		// params.put("pageSize", String.valueOf(pageSize));
		log.info("=================>>代理商解冻明细查询,参数:[params]" + params);

		String dataStr = new ClientInterface(Constants.UN_FREEZE_EXPORT_URL, params).postRequest();
		log.info("瑞金接口返回:{}", dataStr);

		dataStr = data2Str(dataStr);
		return dataStr;
	}

	private String data2Str(String dataStr) {
		if (StringUtils.isNotBlank(dataStr)) {
			JSONObject resJson = JSON.parseObject(dataStr);
			dataStr = resJson.getString("data");
			resJson = JSON.parseObject(dataStr);
			dataStr = resJson.getString("list");
			return dataStr;
		}
		return null;
	}

	@Override
	public Map<String, Object> getRateSingleNumAmount(String money, Map<String, Object> map) {
		BigDecimal trans_amount = new BigDecimal(money).setScale(2, RoundingMode.HALF_UP);
		Map<String, Object> msg = new HashMap<>();
		BigDecimal feeAmount = new BigDecimal("0.00"); // 手续费
		String rateType = map.get("rate_type").toString();
		if ("1".equals(rateType)) {
			feeAmount = new BigDecimal(map.get("single_num_amount").toString());
		} else if ("2".equals(rateType)) {
			feeAmount = trans_amount.multiply((new BigDecimal(map.get("rate").toString()).divide(new BigDecimal(100))));
		} else if ("4".equals(rateType)) {
			feeAmount = trans_amount.multiply((new BigDecimal(map.get("rate").toString()).divide(new BigDecimal(100))));
			feeAmount = feeAmount.add(new BigDecimal(map.get("single_num_amount").toString()));
			if (feeAmount.compareTo(trans_amount) > 0) {
				msg.put("falg", false);
				msg.put("errorMsg", "结算金额小于手续费");
				log.info("结算金额小于手续费");
				feeAmount = new BigDecimal("0");
			}
		} else if ("3".equals(rateType)) {
			feeAmount = trans_amount.multiply((new BigDecimal(map.get("rate").toString()).divide(new BigDecimal(100))));
			if (feeAmount.compareTo(new BigDecimal(map.get("capping").toString())) >= 0) {
				feeAmount = new BigDecimal(map.get("capping").toString());
			} else if (feeAmount.compareTo(new BigDecimal(map.get("safe_line").toString())) != -1) {
				feeAmount = new BigDecimal(map.get("safe_line").toString());
			}
		} else if ("5".equals(rateType)) {
			if (trans_amount.compareTo(new BigDecimal(map.get("ladder1_max").toString())) > -1) {
				feeAmount = trans_amount
						.multiply((new BigDecimal(map.get("ladder1_rate").toString()).divide(new BigDecimal(100))));
			} else {
				feeAmount = trans_amount
						.multiply((new BigDecimal(map.get("ladder2_rate").toString()).divide(new BigDecimal(100))));
			}
		}
		msg.put("single_num_amount", feeAmount);
		return msg;
	}

	@Override
	public boolean findSuperPush(String functionNumber) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String agentNo = principal.getUserEntityInfo().getEntityId();
		Map<String, Object> functionMap = agentInfoDao.findFunctionManage(functionNumber);
		String functionSwitch = (String) functionMap.get("function_switch");
		String agentControl = (String) functionMap.get("agent_control");
		String oneAgentNo = agentInfoDao.getOneAgentNo(agentNo);
		// 不受代理商控制
		boolean isNotAgentControl = StringUtils.equals("1", functionSwitch) && StringUtils.equals("0", agentControl);

		// 受代理控制,则需要判断一级代理商是否有开通微创业功能
		boolean isAgentControl = StringUtils.equals("1", functionSwitch) && StringUtils.equals("1", agentControl)
				&& this.findActivityIsSwitch(functionNumber, oneAgentNo) != null;
		return isNotAgentControl || isAgentControl;
	}

	@Override
	public void updateProfitSwitch(String parentAgentNo, String childrenAgentNo, int profitSwitch) {
		if (StringUtils.isBlank(parentAgentNo) || StringUtils.isBlank(childrenAgentNo)) {
			throw new AgentWebException("更新分润日结功能失败.");
		}
		if (StringUtils.equalsIgnoreCase(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("不能修改自己的分润日结功能.");
		}
		if (!agentInfoDao.isDirectSubordinate(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("只能修改直接下级代理商的分润日结功能.");
		}
		if (!agentInfoDao.profitSwithIsOpen(parentAgentNo)) {
			throw new AgentWebException("上级或本级代理商未打开分润日结功能，不能给下级打开分润日结功能.");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(childrenAgentNo);
		if (agentInfo == null || StringUtils.isBlank(agentInfo.getAgentNode())) {
			throw new AgentWebException("代理商No:" + childrenAgentNo + " 不存在.");
		}
		// 开->关 被修改的代理商的所有下级都将被关闭
		if (profitSwitch == 0) {
			agentInfoDao.updateChildAndSelfProfitSwitch2off(agentInfo.getAgentNode() + "%");
		} else { // 关 -> 开, 只改变当前代理商的分润日结功能
			agentInfoDao.updateProfitSwitch2on(childrenAgentNo);
		}
	}

	@Override
	public void updatePromotionSwitch(String parentAgentNo, String childrenAgentNo, int promotionSwitch) {
		if (StringUtils.isBlank(parentAgentNo) || StringUtils.isBlank(childrenAgentNo)) {
			throw new AgentWebException("更新代理商推广功能失败.");
		}
		if (StringUtils.equalsIgnoreCase(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("不能修改自己的推广功能.");
		}
		if (!agentInfoDao.isDirectSubordinate(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("只能修改直接下级代理商的推广功能.");
		}
		if (!agentInfoDao.promotionSwitchIsOpen(parentAgentNo)) {
			throw new AgentWebException("上级或本级代理商未打开推广功能，不能给下级打开推广功能.");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(childrenAgentNo);
		if (agentInfo == null || StringUtils.isBlank(agentInfo.getAgentNode())) {
			throw new AgentWebException("代理商No:" + childrenAgentNo + " 不存在.");
		}
		// 开->关 被修改的代理商的所有下级都将被关闭
		if (promotionSwitch == 0) {
			agentInfoDao.updateChildAndSelfPromotionSwitch2off(agentInfo.getAgentNode() + "%");
		} else { // 关 -> 开, 只改变当前代理商的分润日结功能
			agentInfoDao.updatePromotionSwitch2on(childrenAgentNo);
		}
	}

	@Override
	public void updateCashBackSwitch(String parentAgentNo, String childrenAgentNo, int cashBackSwitch) {
		if (StringUtils.isBlank(parentAgentNo) || StringUtils.isBlank(childrenAgentNo)) {
			throw new AgentWebException("更新代理商欢乐返返现功能失败.");
		}
		if (StringUtils.equalsIgnoreCase(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("不能修改自己的欢乐返返现功能.");
		}
		if (!agentInfoDao.isDirectSubordinate(parentAgentNo, childrenAgentNo)) {
			throw new AgentWebException("只能修改直接下级代理商的欢乐返返现功能.");
		}
		if (!agentInfoDao.cashBackSwitchIsOpen(parentAgentNo)) {
			throw new AgentWebException("上级或本级代理商未打开欢乐返返现功能，不能给下级打开欢乐返返现功能.");
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(childrenAgentNo);
		if (agentInfo == null || StringUtils.isBlank(agentInfo.getAgentNode())) {
			throw new AgentWebException("代理商No:" + childrenAgentNo + " 不存在.");
		}
		// 开->关 被修改的代理商的所有下级都将被关闭
		if (cashBackSwitch == 0) {
			agentInfoDao.updateChildAndSelfCashBackSwitch2off(agentInfo.getAgentNode() + "%");
		} else { // 关 -> 开, 只改变当前代理商的欢乐返返现功能
			agentInfoDao.updateCashBackSwitch2on(childrenAgentNo);
		}
	}

	@Override
	public void updateFullPrizeSwitch(String parentAgentNo, String childrenAgentNo, int fullPrizeSwitch) {
		boolean oneAgent = isOneAgent(parentAgentNo);
		// 2、如开启则直属下级支持满奖或不满扣开启，如关闭则直属下级及整个链条均不支持
		if (!agentInfoDao.parentFullPrizeSwitch(parentAgentNo) && !oneAgent) {
			throw new AgentWebException("暂不支持开启该功能");
		}

		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(childrenAgentNo);

		// 开->关 被修改的代理商的所有下级都将被关闭
		if (fullPrizeSwitch == 0) {
			agentInfoDao.updateChildAndSelfFullPrizeSwitch(agentInfo.getAgentNode() + "%", fullPrizeSwitch);
		} else { // 关 -> 开, 只改变当前代理商的欢乐返返现功能
			// 获取该代理商所有欢乐返子类型
			List<AgentActivity> list = agentInfoDao.selectHappyBackList(childrenAgentNo);
			for (AgentActivity agentActivity : list) {
				// 拿到上级的满奖不满扣金额
				AgentActivity parent = agentInfoDao.findAgentActivityByParentAndType(parentAgentNo,
						agentActivity.getActivityTypeNo());
				if (parent == null) {
					throw new AgentWebException("活动配置有误，请核实后再打开！");
				}
				BigDecimal parentFullPrizeAmount = parent.getFullPrizeAmount();
				if (parentFullPrizeAmount == null) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				BigDecimal parentRepeatFullPrizeAmount = parent.getRepeatFullPrizeAmount();
				if (parentRepeatFullPrizeAmount == null) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				BigDecimal fullPrizeAmount = agentActivity.getFullPrizeAmount();
				BigDecimal repeatFullPrizeAmount = agentActivity.getRepeatFullPrizeAmount();

				if (parentFullPrizeAmount == null
						|| (fullPrizeAmount != null && fullPrizeAmount.compareTo(parentFullPrizeAmount) == 1)) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				if (parentRepeatFullPrizeAmount == null || (repeatFullPrizeAmount != null
						&& repeatFullPrizeAmount.compareTo(parentRepeatFullPrizeAmount) == 1)) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
			}
			// 3、如满奖或不满扣成本比例参数没有配置时，开关为关闭状态，打开开关提示：请先配置该代理商的满奖不满扣比例
			// 获取上级代理商满奖不满扣成本是否配置
			long count = agentInfoDao.countFullPrizeAmountByAgentNo(childrenAgentNo);
			if (count > 0) {
				throw new AgentWebException("请先配置该代理商的满奖金额");
			}
			agentInfoDao.updateFullPrizeSwitch(childrenAgentNo, fullPrizeSwitch);
		}

	}

	@Override
	public void updateNotFullDeductSwitch(String parentAgentNo, String childrenAgentNo, int notFullDeductSwitch) {
		boolean oneAgent = isOneAgent(parentAgentNo);
		// 2、如开启则直属下级支持满奖或不满扣开启，如关闭则直属下级及整个链条均不支持
		if (!agentInfoDao.parentNotFullDeductSwitch(parentAgentNo) && !oneAgent) {
			throw new AgentWebException("暂不支持开启该功能");
		}
		
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(childrenAgentNo);

		// 开->关 被修改的代理商的所有下级都将被关闭
		if (notFullDeductSwitch == 0) {
			agentInfoDao.updateChildAndSelfNotFullDeductSwitch(agentInfo.getAgentNode() + "%", notFullDeductSwitch);
		} else { // 关 -> 开, 只改变当前代理商的欢乐返返现功能
			// 3、如满奖或不满扣成本比例参数没有配置时，开关为关闭状态，打开开关提示：请先配置该代理商的满奖不满扣比例
			// 获取上级代理商满奖不满扣成本是否配置
			// 获取该代理商所有欢乐返子类型
			List<AgentActivity> list = agentInfoDao.selectHappyBackList(childrenAgentNo);
			for (AgentActivity agentActivity : list) {
				// 拿到上级的满奖不满扣金额
				AgentActivity parent = agentInfoDao.findAgentActivityByParentAndType(parentAgentNo,
						agentActivity.getActivityTypeNo());
				if (parent == null) {
					throw new AgentWebException("活动配置有误，请核实后再打开！");
				}
				BigDecimal parentNotFullDeductAmount = parent.getNotFullDeductAmount();
				if (parentNotFullDeductAmount == null) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				BigDecimal parentRepeatNotFullDeductAmount = parent.getRepeatNotFullDeductAmount();
				if (parentRepeatNotFullDeductAmount == null) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				BigDecimal notFullDeductAmount = agentActivity.getNotFullDeductAmount();
				BigDecimal repeatNotFullDeductAmount = agentActivity.getRepeatNotFullDeductAmount();
				if (parentNotFullDeductAmount == null
						|| (notFullDeductAmount != null && notFullDeductAmount.compareTo(parentNotFullDeductAmount) == 1)) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
				if (parentRepeatNotFullDeductAmount == null || (repeatNotFullDeductAmount != null
						&& repeatNotFullDeductAmount.compareTo(parentRepeatNotFullDeductAmount) == 1)) {
					throw new AgentWebException("满奖不满扣金额配置有误，请修改后开启");
				}
			}
			long count = agentInfoDao.countFullAmountByAgentNo(childrenAgentNo);
			if (count > 0) {
				throw new AgentWebException("请先配置该代理商的不满扣金额");
			}
			agentInfoDao.updateNotFullDeductSwitch(childrenAgentNo, notFullDeductSwitch);
		}

	}

	@Override
	public void batchUpdateProfitSwitch(String parentAgentNo, List<String> childrenAgentNos, int profitSwitch) {
		if (childrenAgentNos == null || childrenAgentNos.isEmpty()) {
			throw new AgentWebException("代理商No参数为空.");
		}
		if (childrenAgentNos.contains(parentAgentNo)) {
			throw new AgentWebException("不能修改自己的分润日结功能.");
		}
		// 先检查
		for (String agentNo : childrenAgentNos) {
			if (!agentInfoDao.isDirectSubordinate(parentAgentNo, agentNo)) {
				throw new AgentWebException("只能修改直接下级代理商的分润日结功能.");
			}
		}
		// 再更新状态
		for (String agentNo : childrenAgentNos) {
			updateProfitSwitch(parentAgentNo, agentNo, profitSwitch);
		}
	}

	@Override
	public void batchUpdatePromotionSwitch(String parentAgentNo, List<String> childrenAgentNos, int promotionSwitch) {
		if (childrenAgentNos == null || childrenAgentNos.isEmpty()) {
			throw new AgentWebException("代理商No参数为空.");
		}
		if (childrenAgentNos.contains(parentAgentNo)) {
			throw new AgentWebException("不能修改自己的代理商推广功能.");
		}
		// 先检查
		for (String agentNo : childrenAgentNos) {
			if (!agentInfoDao.isDirectSubordinate(parentAgentNo, agentNo)) {
				throw new AgentWebException("只能修改直接下级代理商的推广功能.");
			}
		}
		// 再更新状态
		for (String agentNo : childrenAgentNos) {
			updatePromotionSwitch(parentAgentNo, agentNo, promotionSwitch);
		}
	}

	@Override
	public void batchUpdateCashBackSwitch(String parentAgentNo, List<String> childrenAgentNos, int cashBackSwitch) {
		if (childrenAgentNos == null || childrenAgentNos.isEmpty()) {
			throw new AgentWebException("代理商No参数为空.");
		}
		if (childrenAgentNos.contains(parentAgentNo)) {
			throw new AgentWebException("不能修改自己的代理商欢乐返返现功能.");
		}
		// 先检查
		for (String agentNo : childrenAgentNos) {
			if (!agentInfoDao.isDirectSubordinate(parentAgentNo, agentNo)) {
				throw new AgentWebException("只能修改直接下级代理商的欢乐返返现功能.");
			}
		}
		// 再更新状态
		for (String agentNo : childrenAgentNos) {
			updateCashBackSwitch(parentAgentNo, agentNo, cashBackSwitch);
		}
	}

	@Override
	public List<ServiceRate> getNewAgentServicesByBpId(List<String> bpIds, String parentAgentNo) {
		List<String> learderOrIndividualBpId = agentInfoDao.getLearderOrIndividualBpId(bpIds, parentAgentNo);
		if (CollectionUtils.isEmpty(learderOrIndividualBpId)) {
			return null;
		}
		return agentInfoDao.getNewAgentServicesByBpId(learderOrIndividualBpId);
	}

	@Override
	public ProfitDaySettleDetailBean profitDaySettleDetailList(ProfitDaySettleDetailParamBean paramBean) {
		if (paramBean != null && StringUtils.isNotBlank(paramBean.getAgentNo())) {
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(paramBean.getAgentNo());
			if (agentInfo != null) {
				paramBean.setAgentNo(agentInfo.getAgentNode());
			}
		}
		return ClientInterface.profitDaySettleDetailList(paramBean);
	}

	@Override
	public List<ProfitDaySettleDetailBean.DataList> exportProfitDaySettleDetailList(
			ProfitDaySettleDetailParamBean paramBean) {
		if (paramBean != null && StringUtils.isNotBlank(paramBean.getAgentNo())) {
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(paramBean.getAgentNo());
			if (agentInfo != null) {
				paramBean.setAgentNo(agentInfo.getAgentNode());
			}
		}else {
			return new ArrayList<>();
		}
		return ClientInterface.exportProfitDaySettleDetailList(paramBean);
	}

	@Override
	public List<AgentInfo> selectSelfAndDirectChildren(String entityId) {
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		List<AgentInfo> children = agentInfoDao.selectAgentByParentId(entityId);
		List<AgentInfo> result = new ArrayList<>();
		if (agentInfo != null) {
			result.add(agentInfo);
		}
		if (!CollectionUtils.isEmpty(children)) {
			result.addAll(children);
		}
		return result;
	}

	@Override
	public List<AgentInfo> selectDirectChildren(String entityId) {
		return agentInfoDao.selectAgentByParentId(entityId);
	}

	@Override
	public boolean updateAgentProStatus(String loginAgentNo, String agentNo, String bpId, String status) {
		log.info("agentNo : " + agentNo + ", bpId" + bpId + ", status : " + status);
		// 1. 校验输入参数
		if (StringUtils.isBlank(agentNo) || StringUtils.isBlank(bpId)) {
			throw new AgentWebException("更新代理商的业务状态失败.");
		}

		// 2.判读该业务产品是否为自定义的业务产品,必须是有组的,且不能单独申请的业务产品
		if (agentInfoDao.countUserDefinedBusinessProduct(bpId) == 0) {
			return updateAgenttOtherBusinessProduct(loginAgentNo, agentNo, bpId, status);
		} else {
			return updateAgenttUserDefinedBusinessProduct(loginAgentNo, agentNo, bpId, status);
		}
	}

	/**
	 * 更新队员业务产品的状态
	 *
	 * @param loginAgentNo 登陆代理商编号
	 * @param agentNo      待修改的代理商编号
	 * @param bpId         业务产品id
	 * @param status       更新状态
	 * @return
	 */
	private boolean updateAgenttUserDefinedBusinessProduct(String loginAgentNo, String agentNo, String bpId,
			String status) {

		// 判断是否是要关闭,如果是关闭的话,直接关闭,不需要做校验(级联关闭)
		if (StringUtils.equals("0", status)) {
			AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
			return agentInfoDao.closeAgentProStatus(agentInfo.getAgentNode(), bpId) > 0;
		} else {
			// 判断登陆代理商的业务产品是否关闭的,如关闭则不允许打开
			JoinTable agentPro = agentInfoDao.getAgentPro(loginAgentNo, Integer.parseInt(bpId));
			if (agentPro != null && agentPro.getKey2() == 0) {
				throw new AgentWebException(
						"代理商" + loginAgentNo + "的业务产品" + bpId + "是关闭的,不能更改代理商" + agentNo + "业务产品的状态");
			}
			// 需要校验是否存在倒挂,即分润成本不能大于商户费率
			if (agentInfoDao.countShareDateMoreThanServiceRate(agentNo, bpId) > 0) {
				throw new AgentWebException("不能打开业务产品" + bpId + "的状态,否则将出现分润成本大于商户费率的情况.");
			}
			// 判断子表是否存在未生效的分润
			if (agentInfoDao.countHasNotEfficientRule(Integer.parseInt(bpId), agentNo) > 0) {
				throw new AgentWebException("因子表存在未生效的分润,不能打开业务产品" + bpId + "的状态");
			}
			return agentInfoDao.openAgentProStatus(agentNo, bpId) > 0;
		}
	}

	/**
	 * 更新普通业务产品和队长业务产品的状态
	 *
	 * @param loginAgentNo 登陆代理商编号
	 * @param agentNo      待修改的代理商编号
	 * @param bpId         业务产品id
	 * @param status       更新状态
	 * @return
	 */
	private boolean updateAgenttOtherBusinessProduct(String loginAgentNo, String agentNo, String bpId, String status) {
		// 如果是打开状态
		if (!StringUtils.equals("0", status)) {
			JoinTable agentPro = agentInfoDao.getAgentPro(loginAgentNo, Integer.parseInt(bpId));
			// 判断登陆代理商关联的业务产品是否关闭的,如关闭则不能打开
			if (agentPro != null && agentPro.getKey2() == 0) {
				throw new AgentWebException(
						"代理商" + loginAgentNo + "的业务产品" + bpId + "是关闭的,不能更改代理商" + agentNo + "业务产品的状态");
			}
			return agentInfoDao.openAgentProStatus(agentNo, bpId) > 0;
		} else {
			throw new AgentWebException("只能更新自定义业务产品的状态.");
		}
	}

//	@Override
//	public int updateAgentProStatus(Map<String, Object> map) {
//		Integer status=(Integer) map.get("status");
////		status=status==1?0:1;
//		map.put("status", status);
//		JoinTable agentPro=agentInfoDao.getAgentPro(map.get("agentNo").toString(), (Integer)map.get("bpId"));
//		if(status==agentPro.getKey1())
//			return 0;
//		if(status==0){
//			AgentInfo info = this.selectByagentNo(map.get("agentNo").toString());
//			map.put("agentNode",info.getAgentNode());
//		} else if(status==1){
//			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			JoinTable parentAgentPro=agentInfoDao.getAgentPro(principal.getUserEntityInfo().getEntityId(), (Integer)map.get("bpId"));
//			if(parentAgentPro.getKey1()==0){
//				throw new RuntimeException("该业务产品状态无法开启");
//			}
//		}
//		return agentInfoDao.updateAgentProStatus(map);
//	}

	@Override
	public Map<String, Object> findAgentProfitCollection(String agentNo) {
		Map<String, Object> agentProfitFreezeCollection = ClientInterface.findAgentProfitCollection(agentNo, true);
		Map<String, Object> agentProfitAdjustCollection = ClientInterface.findAgentProfitCollection(agentNo, false);
		Map<String, Object> result = new HashedMap();
		if (agentProfitFreezeCollection != null) {
			result.put("freeze", agentProfitFreezeCollection);
		}
		if (agentProfitFreezeCollection != null) {
			result.put("adjust", agentProfitAdjustCollection);
		}
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(agentNo);
		result.put("agent", agentInfo);
		return result;
	}

	@Override
	public boolean merchantIsBelongToAgent(String merchantBpId, String loginAgentNo) {
		return agentInfoDao.merchantIsBelongToAgent(merchantBpId, loginAgentNo);
	}

	@Override
	public boolean merchantIsDirectBelongToAgent(String merchantBpId, String loginAgentNo) {
		return agentInfoDao.merchantIsDirectBelongToAgent(merchantBpId, loginAgentNo);
	}

	@Override
	public boolean terminalIsBelongToAgent(String terminalId, String loginAgentNo) {
		return agentInfoDao.terminalIsBelongToAgent(terminalId, loginAgentNo);
	}

	@Override
	public boolean updateDefaultFlagSwitch(String bpId, String loginAgentNo) {
		if (StringUtils.isBlank(bpId) || StringUtils.isBlank(loginAgentNo)) {
			return false;
		}
		agentInfoDao.updateDefaultFlagGroup2Off(bpId, loginAgentNo);
		agentInfoDao.updateDefaultFlag2On(bpId, loginAgentNo);
		return true;
	}

	@Override
	public Map<String, Object> selectRedEnvelopesDetails(RedEnvelopesDetails info,
			@Param("page") Page<RedEnvelopesDetails> page) {
		Map<String, Object> map = new HashMap<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		List<RedEnvelopesDetails> list = new ArrayList<>();
		Integer totalAmount = 0;
		if ("1".equals(principal.getUserEntityInfo().getIsAgent())) {// 代理商
			list = redEnvelopesDao.selectRedEnvelopesDetailsByAgent(info, entityId, page);
		} else if ("0".equals(principal.getUserEntityInfo().getIsAgent())) {// 用户,暂时没用
			list = redEnvelopesDao.selectRedEnvelopesDetailsByUser(info, entityId, page);
		}
		totalAmount = redEnvelopesDao.selectTotalAmount(info, entityId);
		map.put("list", list);
		map.put("totalAmount", totalAmount);
		return map;
	}

	@Override
	public List<RedEnvelopesDetails> exportRedEnvelopesDetails(RedEnvelopesDetails info) {
		List<RedEnvelopesDetails> list = new ArrayList<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		if ("1".equals(principal.getUserEntityInfo().getIsAgent())) {// 代理商
			list = redEnvelopesDao.exportSelectRedEnvelopesDetailsByAgent(info, entityId);
		} else if ("0".equals(principal.getUserEntityInfo().getIsAgent())) {// 用户,暂时没用
			list = redEnvelopesDao.exportSelectRedEnvelopesDetailsByUser(info, entityId);
		}
		return list;
	}

	@Override
	public Map<String, Object> selectBalance() {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		Map<String, Object> map = new HashMap<>();
		String redBalance = "";
		if ("1".equals(principal.getUserEntityInfo().getIsAgent())) {// 代理商
			redBalance = redEnvelopesDao.selectTotalAmountByAgent(entityId);
		} else if ("0".equals(principal.getUserEntityInfo().getIsAgent())) {// 用户,暂时没用
			redBalance = redEnvelopesDao.selectTotalAmountByUser(entityId);
		}
		map.put("redBalance", redBalance == null ? "0" : redBalance);
		return map;
	}

	@Override
	public Map<String, Object> updateWithdrawRedBalance(String redBalance) {
		Map<String, Object> map = new HashMap<>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		String balance = "";
		if ("1".equals(principal.getUserEntityInfo().getIsAgent())) {// 代理商
			balance = redEnvelopesDao.selectTotalAmountByAgent(entityId);
		} else if ("0".equals(principal.getUserEntityInfo().getIsAgent())) {// 用户,暂时没用
			balance = redEnvelopesDao.selectTotalAmountByUser(entityId);
		}
		if (new BigDecimal(redBalance).compareTo(new BigDecimal(balance)) == 1
				|| new BigDecimal(redBalance).compareTo(new BigDecimal("0")) == 0) {
			throw new AgentWebException("余额不足或已提交红包提现");
		}
		log.info("====================进入调用账户接口红包余额提现");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		String withdOn = "AGENT" + sf2.format(date);// 提现订单号
		// 插入红包明细,接口返回失败则回滚
		Integer id = redEnvelopesDao.selectId() + 1;
		RedAccountInfo info = redEnvelopesDao.selectRedAccountInfo(entityId);
		log.info("======根据当前登录代理商查询到红包账户余额信息:[" + info + "]");
		log.info("========账户返回成功,开始插入红包明细==========");
		RedEnvelopesDetails details = new RedEnvelopesDetails();
		details.setId(id);
		details.setRedAccountId(info.getId().toString());
		details.setAccountCode(info.getAccountCode());
		details.setType("5");// 交易类型:5转出其他账户
		details.setTransAmount(new BigDecimal(redBalance));
		details.setRedOrderId("0");// 默认设置为0
		details.setWithdOn(withdOn);
		Integer i = redEnvelopesDao.insertRedEnvelopesDetail(details);
		log.info("========插入红包明细,插入i = " + i + "条数据========");
		// 提现成功,更新红包账户余额为0
		Integer num = 0;
		if ("1".equals(principal.getUserEntityInfo().getIsAgent())) {
			num = redEnvelopesDao.updateRedBalanceByAgent(entityId);
		} else if ("0".equals(principal.getUserEntityInfo().getIsAgent())) {// 用户,暂时没用
			num = redEnvelopesDao.updateRedBalanceByUser(entityId);
		}
		log.info("========提现成功,更新红包账户余额为0,更新num = " + num + "条数据========");

		// 调账户接口
		String result = ClientInterface.updateWithdrawRedBalance(entityId, redBalance, date);
		log.info("账户红包提现接口返回============>" + result);
		if (StringUtils.isNotBlank(result)) {
			JSONObject json = JSON.parseObject(result);
			String msg = json.getString("msg");
			if (json.getBooleanValue("status")) {
				map.put("msg", "红包账户余额已全部提现到 [超级银行家分润账户余额]");
			} else {
				log.info("===========>红包余额提现接口返回false,错误信息:" + msg);
				map.put("msg", msg == null ? "提现失败" : msg);
				throw new AgentWebException("提现失败");
			}
		} else {
			log.info("=======>>>>账户红包余额接口返回空");
			throw new AgentWebException("提现失败");
		}
		return map;
	}

	@Override
	public Integer selectIsOpen(String entityId) {
		return orgInfoDao.selectIsOpen(entityId);
	}

	@Override
	public List<AgentActivity> selectHappyBackList() {
		AgentInfo info = this.getCurAgentInfo();
		//List<AgentActivity> list = agentInfoDao.selectHappyBackListWithStatus(info.getAgentNo(), true);
		List<AgentActivity> list = agentInfoDao.selectHappyBackList(info.getAgentNo());
		// 获取当前代理商支持的层级
		SupportRankDto supportRank = this.getSupportRank(info.getAgentOem());
		for (AgentActivity agentActivity : list) {
			this.setShowPrizeDeductAmount(agentActivity, info, supportRank);
//			agentActivity.setTaxRate(null);
//			agentActivity.setCashBackAmount(null);
//			agentActivity.setRepeatRegisterRatio(null);
//			agentActivity.setRepeatRegisterAmount(null);
//			agentActivity.setFullPrizeAmount(null);
//			agentActivity.setNotFullDeductAmount(null);
//			agentActivity.setRepeatFullPrizeAmount(null);
//			agentActivity.setRepeatNotFullDeductAmount(null);
			agentActivity.setSelect(true);
		}
		return list;
	}

	@Override
	public Map<String, Object> selectByActivityTypeNo(String activityTypeNo) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		return agentInfoDao.selectByActivityTypeNo(entityId, activityTypeNo);
	}

	@Override
	public Map<String, Object> selectDefaultStatus() {
		return agentInfoDao.selectDefaultStatus();
	}

	@Override
	public Map<String, Object> selectAccountStatus(String entityId) {
		return agentInfoDao.selectAccountStatus(entityId);
	}

	@Override
	public AgentInfo selectByMobilephoneAndTeamId(String mobilephone, String teamId) {
		return agentInfoDao.selectByMobilephone(mobilephone, teamId);
	}

	@Override
	public Integer updateWithdrawSwitch(Integer id) {
		return agentInfoDao.updateWithdrawSwitch(id);

	}

	@Override
	public List<Map<String, Object>> selectByServiceType(String serviceType) {
		return agentInfoDao.selectByServiceType(serviceType);
	}

	@Override
	public List<Map<String, Object>> selectAllInfoBelong(String entityId) {
		return agentInfoDao.selectAllInfoBelong(entityId);
	}

	@Override
	public List<AgentInfo> getConfigInfo(String agentNo) {
		Set<String> agentNoSet = agentInfoDao.findConfigAgentNo(agentNo);
		Set<String> agentNoSetData = new HashSet<>();
		agentNoSetData.add(agentNo);
		this.getAgentNoList(agentNoSet, agentNoSetData);
		// 根据代理商编号查询开关为开的 代理商 编号和姓名 进行返回
		if (agentNoSetData.size() == 0) {
			return null;
		}
		return agentInfoDao.findAgentInfoListByAgentNoSet(agentNoSetData);
	}

	public void getAgentNoList(Set<String> agentNoSet, Set<String> agentNoSetData) {
		agentNoSetData.addAll(agentNoSet);
		for (String agentNo : agentNoSet) {
			Set<String> set = agentInfoDao.findConfigAgentNo(agentNo);
			if (set.size() > 0) {
				for (String string : agentNoSetData) {
					if (set.contains(string)) {
						set.remove(string);
					}
				}
			}

			getAgentNoList(set, agentNoSetData);
		}

	}

	@Autowired
	private TradeSumInfoService tradeSumInfoService;

	@Override
	public boolean isAuth(String currAgentNo, String selectAgentNo) {
		List<String> agentList = new ArrayList<>();
		agentList.add(currAgentNo);
		tradeSumInfoService.getAllLookAgentNo(agentList, currAgentNo);
		return agentList.contains(selectAgentNo);
	}

	@Override
	public boolean isOneAgent(String agentNo) {
		return agentInfoDao.countOneAgentByUserName(agentNo) > 0;
	}

	@Override
	public AgentInfo getOneAgentByAgentNo(String entityId) {
		return agentInfoDao.findAgentInfoByUserAgentNo(entityId);
	}

	@Override
	public int updateSafePhoneByAgentNo(String agentNo, String checkphone) {
		return agentInfoDao.updateSafePhoneByAgentNo(agentNo, checkphone);
	}

	@Override
	public String getSafePhone(String agentNo) {
		return agentInfoDao.getSafePhone(agentNo);
	}

	private SupportRankDto getSupportRank(String oem) {
		SupportRankDto supportRankDto = new SupportRankDto();
		String value = sysDictService.getStringValueByKey(SysDict.AGENT_OEM_PRIZE_BUCKLE_RANK + oem);
		if (!StringUtils.isEmpty(value)) {
			int indexOf = value.indexOf("-");
			supportRankDto.setFullPrizeLevel(Integer.valueOf(value.substring(0, indexOf)));
			supportRankDto.setNotFullDeductLevel(Integer.valueOf(value.substring(indexOf + 1, value.length())));
		}
		return supportRankDto;
	}

	private void setShowPrizeDeductAmount(AgentActivity agentActivity, AgentInfo info, SupportRankDto dto) {
		agentActivity.setShowFullPrizeAmount((info.getFullPrizeSwitch() == 1 || info.getAgentLevel() == 1)
				&& info.getAgentLevel() <= dto.getFullPrizeLevel() && !"11".equals(info.getAgentType()));
		agentActivity.setShowNotFullDeductAmount((info.getNotFullDeductSwitch() == 1 || info.getAgentLevel() == 1)
				&& info.getAgentLevel() <= dto.getNotFullDeductLevel() && !"11".equals(info.getAgentType()));
	}

	@Override
	public Integer updateSafePassword(String password, UserLoginInfo principal) {
		String entityId = principal.getUserEntityInfo().getEntityId();
		String safePassword = Md5.md5Str(password + "{" + entityId + "}");
		return agentInfoDao.updateSafePassword(safePassword,entityId);
	}

	@Override
	public AgentInfo getCurAgentInfo() {
		return agentInfoDao.selectByAgentNo(this.getCurAgentNo());
	}

	@Override
	public String getCurAgentNo() {
		UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return principal.getUserEntityInfo().getEntityId();
	}

	@Override
	public Integer updateAgentByIdCardNo(String idCardNo, String agentNo) {
		return agentInfoDao.updateAgentByIdCardNo(idCardNo, agentNo);
	}

	@Override
	public List<AgentActivity> getAgentActivity() {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(principal.getUserEntityInfo().getEntityId());
		return agentInfoDao.getAgentActivity(agentInfo.getOneLevelId());
	}

	@Override
    public boolean getFunctionSwitch(AgentInfo agentInfo,String functionNumber) {
		boolean isSwitch=true;
		Map<String, Object> functionMap = agentInfoDao.findFunctionManage(functionNumber);
		if(!CollectionUtils.isEmpty(functionMap)) {
			String functionSwitch = (String) functionMap.get("function_switch");
			String agentControl = (String) functionMap.get("agent_control");
			String oneAgentNo = agentInfoDao.getOneAgentNo(agentInfo.getAgentNo());
			// 不受代理商控制
			if ("1".equals(functionSwitch)) {
				//开启代理商就只能对应的代理商才显示
				if ("1".equals(agentControl)) {
					if (this.findActivityIsSwitch(functionNumber, oneAgentNo) == null) {
						isSwitch=false;
					}
				}
			} else {
				isSwitch=false;
			}
		}
		// 判断该代理商是否允许进件 黑名单优先级最高
		if(functionNumber.equals("051")){
			// 是否黑名单 不包含下级
			long blacklistNotContains = agentInfoDao.countBlacklistNotContains(agentInfo.getAgentNo());
			if (blacklistNotContains > 0) {
				isSwitch=false;
			}
			// 是否黑名单 包含下级
			long blacklistContains = agentInfoDao.countBlacklistContains(agentInfo.getAgentNode());
			if (blacklistContains > 0) {
				isSwitch=false;
			}
		}
		return isSwitch;
    }


	@Override
	public int updateAgentActivityStatusByAgentNode(String agentNode, boolean status, String activityTypeNo) {
		return agentInfoDao.updateAgentActivityStatusByAgentNode(agentNode, status, activityTypeNo);
	}

	@Override
	public int updateAgentActivityStatus(Long id, Boolean status) {
		return agentInfoDao.updateAgentActivityStatus(id, status);
	}

	@Override
	public AgentActivity selectByAgentNoAndActivityType(String agentNo, String activityTypeNo) {
		return agentInfoDao.selectByAgentNoAndActivityType(agentNo, activityTypeNo);
	}

	@Override
	public Map<String, Object> selectHappyBackDefaultParam(String activityTypeNo) {
		return agentInfoDao.selectHappyBackDefaultParam(activityTypeNo);
	}

	@Override
	public int insertAgentActivity(List<AgentActivity> happyBackList) {
		return agentInfoDao.insertAgentActivity(happyBackList);
	}

	@Override
	public int selectUpdateAgentStatusByActivityTypeNo(String activityTypeNo) {
		return agentInfoDao.selectUpdateAgentStatusByActivityTypeNo(activityTypeNo);
	}

	@Override
	public AgentActivity selectAgentActivityByAgentNoAndTypeNo(String agentNo, String activityTypeNo) {
		return agentInfoDao.selectAgentActivityByAgentNoAndTypeNo(agentNo, activityTypeNo);
	}

    @Override
    public Map<String, List<String>> getActivityTypeNoAndTeamIdMap(String agentNo) {
		// 欢乐返子类型(1)对应的组织id(n)
        List<Map<String, Object>> mapList = agentInfoDao.getActivityTypeNoAndTeamId(agentNo);
        // 登陆代理商根据代理商的业务产品所对应的组织id
		Map<String, String> agentTeamMap = businessProductDefineService.selectTeamByAgentAndBp(agentNo);
		Map<String, List<String>> result = new HashMap<>();
        if (mapList == null || mapList.size() == 0) {
            return result;
        }
        for (Map<String, Object> temp : mapList) {
            String activityTypeNo = Objects.toString(temp.get("activity_type_no"), "");
            String orgId = Objects.toString(temp.get("org_id"), "");
            // 如果组织为空,或者当前代理商没有代理这个组织的业务产品,则不统计
            if (StringUtils.isBlank(orgId)|| StringUtils.isBlank(agentTeamMap.get(orgId))) {
                continue;
            }
            List<String> list = result.get(activityTypeNo);
            if (list == null) {
                list = new ArrayList<>();
                result.put(activityTypeNo, list);
            }
            list.add(orgId);
        }
        return result;
    }
}
