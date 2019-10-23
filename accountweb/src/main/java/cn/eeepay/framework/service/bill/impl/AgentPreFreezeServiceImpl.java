package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.AgentPreFreezeMapper;
import cn.eeepay.framework.dao.bill.AgentPreRecordTotalMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.service.bill.AgentPreFreezeService;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.nposp.AgentInfoService;
import cn.eeepay.framework.util.ListUtil;



@Service("agentPreFreezeService")
@Transactional
public class AgentPreFreezeServiceImpl implements AgentPreFreezeService{
	
	private static final Logger log = LoggerFactory.getLogger(AgentPreFreezeServiceImpl.class);
	
	@Resource
	private AgentPreFreezeMapper agentPreFreezeMapper;
	@Resource
	private AgentPreRecordTotalMapper agentPreRecordTotalMapper;
	@Resource
	private ExtAccountService extAccountService;
	@Resource
	private AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	private AgentInfoService agentInfoService;

	@Override
	public int insertAgentPreFreezeAndUpdateAccount(AgentPreFreeze agentPreFreeze) throws Exception {
		String agentNo = agentPreFreeze.getAgentNo();
		String agentName = agentPreFreeze.getAgentName();
		
		AgentPreRecordTotal  agentPreRecordTotal = agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNo(agentNo);
		agentPreFreeze.setFreezeTime(new Date());
		try {
			if (agentPreFreeze.getTerminalFreezeAmount() == null) {
				agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
			}
			if (agentPreFreeze.getOtherFreezeAmount() == null) {
				agentPreFreeze.setOtherFreezeAmount(BigDecimal.ZERO);
			}

			if(agentPreRecordTotal != null){
				agentPreFreezeMapper.insertAgentPreFreeze(agentPreFreeze);
				AgentPreRecordTotal aprt= agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNo(agentNo);
				BigDecimal terminalFreezeAmount = aprt.getTerminalFreezeAmount();
				if (terminalFreezeAmount == null) {
					terminalFreezeAmount = BigDecimal.ZERO;
				}
				aprt.setTerminalFreezeAmount(terminalFreezeAmount.add(agentPreFreeze.getTerminalFreezeAmount()));

				BigDecimal otherFreezeAmount = aprt.getOtherFreezeAmount();
				if (otherFreezeAmount == null) {
					otherFreezeAmount = BigDecimal.ZERO;
				}
				aprt.setOtherFreezeAmount(otherFreezeAmount.add(agentPreFreeze.getOtherFreezeAmount()));
				
				agentPreRecordTotalMapper.updateAgentPreRecordTotal(aprt);
			}else{
				agentPreFreezeMapper.insertAgentPreFreeze(agentPreFreeze);
				AgentPreRecordTotal aprt= new AgentPreRecordTotal();
				aprt.setAgentNo(agentNo);
				aprt.setAgentName(agentName);
				aprt.setOpenBackAmount(BigDecimal.ZERO);
				aprt.setRateDiffAmount(BigDecimal.ZERO);
				aprt.setTuiCostAmount(BigDecimal.ZERO);
				aprt.setRiskSubAmount(BigDecimal.ZERO);
				aprt.setMerMgAmount(BigDecimal.ZERO);
				aprt.setOtherAmount(BigDecimal.ZERO);
				aprt.setBailSubAmount(BigDecimal.ZERO);
				aprt.setTerminalFreezeAmount(agentPreFreeze.getTerminalFreezeAmount());
				aprt.setOtherFreezeAmount(agentPreFreeze.getOtherFreezeAmount());
				agentPreRecordTotalMapper.insertAgentPreRecordTotal(agentPreRecordTotal);
			}
			return 1;
		} catch (Exception e) {
			throw new RuntimeException("预冻结出现异常！回滚！" +e.getMessage());
		}
	}

	@Override
	public int updateAgentPreFreeze(AgentPreFreeze agentPreFreeze) throws Exception {
		return agentPreFreezeMapper.updateAgentPreFreeze(agentPreFreeze);
	}

	@Override
	public int deleteAgentPreFreeze(Integer id) throws Exception {
		return agentPreFreezeMapper.deleteAgentPreFreeze(id);
	}

	@Override
	public List<AgentPreFreeze> findAllAgentPreFreeze() throws Exception {
		return agentPreFreezeMapper.findAllAgentPreFreeze();
	}

	@Override
	public List<AgentPreFreeze> findAgentPreFreezeList(AgentPreFreeze agentPreFreeze, Sort sort,
			Page<AgentPreFreeze> page) throws Exception {
		if (StringUtils.isNotBlank(agentPreFreeze.getFreezeTime1())) {
			agentPreFreeze.setFreezeTime1(agentPreFreeze.getFreezeTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentPreFreeze.getFreezeTime2())) {
			agentPreFreeze.setFreezeTime2(agentPreFreeze.getFreezeTime2() + " 23:59:59");
		}
		return agentPreFreezeMapper.findAgentPreFreezeList(agentPreFreeze, sort, page);
	}

	@Override
	public AgentPreFreeze findAgentPreFreezeById(Integer id) throws Exception {
		return agentPreFreezeMapper.findAgentPreFreezeById(id);
	}

	@Override
	public List<AgentPreFreeze> exportAgentsProfitPreFreezeList(AgentPreFreeze agentPreFreeze) {
		if (StringUtils.isNotBlank(agentPreFreeze.getFreezeTime1())) {
			agentPreFreeze.setFreezeTime1(agentPreFreeze.getFreezeTime1() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(agentPreFreeze.getFreezeTime2())) {
			agentPreFreeze.setFreezeTime2(agentPreFreeze.getFreezeTime2() + " 23:59:59");
		}
		return agentPreFreezeMapper.exportAgentsProfitPreFreezeList(agentPreFreeze);
	}

	@Override
	public Map<String, Object> saveAgentsProfitPreFreeze(AgentPreFreeze agentPreFreeze,String freezeReason) throws Exception {
		
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentPreFreeze.getAgentNo();
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal freezeAmount = BigDecimal.ZERO;			//要冻结的金额
		if ("terminal".equals(freezeReason)) {
			freezeAmount = agentPreFreeze.getTerminalFreezeAmount();
		}
		else if ("other".equals(freezeReason)) {
			freezeAmount = agentPreFreeze.getOtherFreezeAmount();
		}
		String accountType = "A";
		String userId = agentNo;
		String accountOwner = "000001";
		String subjectNo = "224105";
		String currencyNo = "1";
		ExtAccountInfo extAccountInfo= extAccountService.findExtAccountInfoByManyParams(accountType, userId, accountOwner, subjectNo, currencyNo);
		if (extAccountInfo == null) {
			String reponseMsg = String.format("没找到外部账号,科目%s代理商编号%s", subjectNo,userId);
			msg.put("status", false);
			msg.put("msg", reponseMsg);
			msg.put("timestamp", String.valueOf(new Date().getTime()));
			log.error(msg.toString());
			throw new RuntimeException(reponseMsg);
		}
		String accountNo = extAccountInfo.getAccountNo();
		ExtAccount extAccount= extAccountService.getExtAccount(accountNo);
		if (extAccount == null) {
			String reponseMsg = String.format("没找到外部账号,科目%s代理商编号%s", subjectNo,userId);
			msg.put("status", false);
			msg.put("msg", reponseMsg);
			msg.put("timestamp", String.valueOf(new Date().getTime()));
			log.error(msg.toString());
			throw new RuntimeException(reponseMsg);
		}

		//获取活动补贴账户
		String activityAccountType = "A";
		String activityUserId = agentNo;
		String activityAccountOwner = "000001";
		String activitySubjectNo = "224106";
		String activityCurrencyNo = "1";
		ExtAccountInfo activityExtAccountInfo= extAccountService.findExtAccountInfoByManyParams(activityAccountType, activityUserId, activityAccountOwner, activitySubjectNo, activityCurrencyNo);
		ExtAccount activityExtAccount = null;
		if(activityExtAccountInfo != null){		//如果存在活动补贴账户
			String activityAccountNo = activityExtAccountInfo.getAccountNo();
			activityExtAccount= extAccountService.getExtAccount(activityAccountNo);		//获取获取活动补贴外部用户账户
			//存在科目号，但不存在活动补贴账户
			if(activityExtAccount == null){
				String reponseMsg = String.format("没找到外部账号,科目%s代理商编号%s", activitySubjectNo,activityUserId);
				msg.put("status", false);
				msg.put("msg", reponseMsg);
				msg.put("timestamp", String.valueOf(new Date().getTime()));
				log.error(msg.toString());
				throw new RuntimeException(reponseMsg);
			}
		}

		BigDecimal currBalance = extAccount.getCurrBalance();		//当前余额
		BigDecimal controlAmount = extAccount.getControlAmount();	//冻结金额
		BigDecimal settlingAmount = extAccount.getSettlingAmount();	//结算中金额
		//分润账户可用余额 = 账户余额 - 控制金额 - 结算中金额
		BigDecimal avaliBalance = (currBalance.subtract(controlAmount)).subtract(settlingAmount);

		BigDecimal activityBalance = BigDecimal.ZERO;		//活动补贴账户被扣减的金额

		if (freezeAmount.compareTo(avaliBalance) > 0) {
			//分润账户可用余额不够冻结
			amount = freezeAmount.subtract(avaliBalance);		//剩余需要冻结的金额
			if(activityExtAccount != null){		//活动补贴账户不为空，从活动补贴账户中补
				BigDecimal activityCurrBalance = activityExtAccount.getCurrBalance();		//当前余额
				BigDecimal activityControlAmount = activityExtAccount.getControlAmount();	//冻结金额
				BigDecimal activitySettlingAmount = activityExtAccount.getSettlingAmount();	//结算中金额
				//活动补贴账户可用余额 = 账户余额 - 控制金额 - 结算中金额
				BigDecimal activityAvaliBalance = (activityCurrBalance.subtract(activityControlAmount)).subtract(activitySettlingAmount);
				//活动补贴账户可用余额不够冻结
				if(amount.compareTo(activityAvaliBalance) > 0){
					amount = amount.subtract(activityAvaliBalance);
					activityBalance = activityAvaliBalance;
				}else{
					activityBalance = amount;
					amount = BigDecimal.ZERO;		//冻结金额足够，预冻结为0
				}
			}
		}else{
			avaliBalance = freezeAmount;
			amount = BigDecimal.ZERO;			//冻结金额足够，预冻结为0
		}
		if (avaliBalance.compareTo(BigDecimal.ZERO) > 0 ) {
			Map<String,Object> freezeResult = extAccountService.agentFreezeAmount(agentNo,subjectNo, avaliBalance);
			Boolean freezeResultStatus = (Boolean) freezeResult.get("status");
			if (!freezeResultStatus) {
				String s = String.format("从账户实时冻结失败 代理商%s 科目 %s 冻结金额 %s,请检查后重新冻结", agentNo ,subjectNo, avaliBalance);
				throw new RuntimeException(s);
			}
		}

		if(activityBalance.compareTo(BigDecimal.ZERO) > 0){
			Map<String,Object> freezeResult = extAccountService.agentFreezeAmount(agentNo,activitySubjectNo, activityBalance);
			Boolean freezeResultStatus = (Boolean) freezeResult.get("status");
			if (!freezeResultStatus) {
				String s = String.format("从账户实时冻结失败 代理商%s 科目 %s 冻结金额 %s,请检查后重新冻结", agentNo ,activitySubjectNo, activityBalance);
				throw new RuntimeException(s);
			}
		}

		agentPreFreeze.setFenFreezeAmount(avaliBalance);
		agentPreFreeze.setActivityFreezeAmount(activityBalance);
		agentPreFreeze.setFreezeAmount(amount);
		String remark = agentPreFreeze.getRemark() + String.format(";从分润账户实时冻结金额%s，活动补贴账户实时冻结金额%s 计入预冻结金额 %s", avaliBalance,activityBalance ,amount);
		agentPreFreeze.setRemark(remark);
		this.insertAgentPreFreeze(agentPreFreeze);
//		if ("terminal".equals(freezeReason)) {
//			agentPreFreeze.setTerminalFreezeAmount(amount);
//		}
//		else if ("other".equals(freezeReason)) {
//			agentPreFreeze.setOtherFreezeAmount(amount);
//		}
		AgentPreRecordTotal  agentPreRecordTotal = agentPreRecordTotalMapper.findAgentPreRecordTotalByAgentNo(agentNo);
		
		if(agentPreRecordTotal != null){
			if ("terminal".equals(freezeReason)) {
				BigDecimal terminalFreezeAmount = agentPreRecordTotal.getTerminalFreezeAmount();
				if (terminalFreezeAmount == null) {
					terminalFreezeAmount = BigDecimal.ZERO;
				}
				agentPreRecordTotal.setTerminalFreezeAmount(terminalFreezeAmount.add(amount));
			}
			else if ("other".equals(freezeReason)) {
				BigDecimal otherFreezeAmount = agentPreRecordTotal.getOtherFreezeAmount();
				if (otherFreezeAmount == null) {
					otherFreezeAmount = BigDecimal.ZERO;
				}
				agentPreRecordTotal.setOtherFreezeAmount(otherFreezeAmount.add(amount));
			}
			agentPreRecordTotalService.updateAgentPreRecordTotal(agentPreRecordTotal);
		}else{
			agentPreRecordTotal = new AgentPreRecordTotal();
			if ("terminal".equals(freezeReason)) {
				agentPreRecordTotal.setTerminalFreezeAmount(amount);
			}
			else if ("other".equals(freezeReason)) {
				agentPreRecordTotal.setOtherFreezeAmount(amount);
			}
			agentPreRecordTotal.setAgentNo(agentNo);
					
			AgentInfo agentInfo = agentInfoService.findEntityByAgentNo(agentNo);
			String agentName = agentInfo.getAgentName();
			agentPreRecordTotal.setAgentName(agentName);
			agentPreRecordTotalService.insertAgentPreRecordTotal(agentPreRecordTotal);
		}
		msg.put("status", true);
		msg.put("msg", "执行成功");
		return msg;
	}

	@Override
	public int insertAgentPreFreeze(AgentPreFreeze agentPreFreeze) throws Exception {
		return agentPreFreezeMapper.insertAgentPreFreeze(agentPreFreeze);
	}

	//循环调用本类，事务有问题，方法作废
	@Override
	public Map<String, Object> saveAgentsProfitBatchPreFreeze(List<AgentPreFreeze> agentPreFreezeList)
			throws Exception {
		Map<String,Object> msg=new HashMap<>();
		for (AgentPreFreeze agentPreFreeze : agentPreFreezeList) {
//			String freezeReason = "terminal";
//			if (agentPreFreeze.getOtherFreezeAmount() != null
//					&& agentPreFreeze.getOtherFreezeAmount().compareTo(BigDecimal.ZERO) > 0) {
//				freezeReason = "other";
//			}
//			if (agentPreFreeze.getFreezeTime() == null) {
//				agentPreFreeze.setFreezeTime(new Date());
//			}
//			if (agentPreFreeze.getTerminalFreezeAmount() == null) {
//				agentPreFreeze.setTerminalFreezeAmount(BigDecimal.ZERO);
//			}
//			if (agentPreFreeze.getOtherFreezeAmount() == null) {
//				agentPreFreeze.setOtherFreezeAmount(BigDecimal.ZERO);
//			}
//			this.saveAgentsProfitPreFreeze(agentPreFreeze, freezeReason);			//这里由于是在事务中，循环调用本类事务会有问题，所以循环遍历直接放在action中，或者该方法迁移到另外一个service中
		}
		msg.put("status", true);
		msg.put("msg", "执行成功");
		return msg;
	}
	@Override
	public Map<String,Object> insertAgentPreFreezeSplitBatch(List<AgentPreFreeze> agentPreFreezeList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 200;
		List<AgentPreFreeze> asdList = new ArrayList<>();
		List<List<?>> agentPreFreezeSplitList = ListUtil.batchList(agentPreFreezeList, batchCount);
		for (List<?> clist : agentPreFreezeSplitList) {
			for (Object object : clist) {
				AgentPreFreeze asd = (AgentPreFreeze) object;
				if (asd.getTerminalFreezeAmount() == null) {
					asd.setTerminalFreezeAmount(BigDecimal.ZERO);
				}
				if (asd.getOtherFreezeAmount() == null) {
					asd.setOtherFreezeAmount(BigDecimal.ZERO);
				}
				asdList.add(asd);
			}
			if (asdList.size() > 0) {
				log.info("插入代理商预冻结表{}条",asdList.size());
				int j = agentPreFreezeMapper.insertAgentPreFreezeBatch(asdList);
				if (j > 0) {
					i = i + j;
				}
				if (!asdList.isEmpty()) {
					asdList.clear();
				}
			}
		}
		if (i > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
		}
		else{
			msg.put("status", true);
			msg.put("msg", "没有任何数据插入");
		}
		return msg;
	}
	@Override
	public int insertAgentPreFreezeBatch(List<AgentPreFreeze> list) throws Exception {
		return agentPreFreezeMapper.insertAgentPreFreezeBatch(list);
	}
	
}
