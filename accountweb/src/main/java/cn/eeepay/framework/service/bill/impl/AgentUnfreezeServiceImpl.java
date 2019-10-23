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

import cn.eeepay.framework.dao.bill.AgentUnfreezeMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AgentPreFreeze;
import cn.eeepay.framework.model.bill.AgentPreRecordTotal;
import cn.eeepay.framework.model.bill.AgentUnfreeze;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.service.bill.AgentPreRecordTotalService;
import cn.eeepay.framework.service.bill.AgentShareDaySettleService;
import cn.eeepay.framework.service.bill.AgentUnfreezeService;
import cn.eeepay.framework.service.bill.ExtAccountService;



@Service("agentUnfreezeService")
@Transactional
public class AgentUnfreezeServiceImpl implements AgentUnfreezeService{
	
	private static final Logger log = LoggerFactory.getLogger(AgentUnfreezeServiceImpl.class);
	@Resource
	private AgentUnfreezeMapper agentUnfreezeMapper;
	@Resource
	private AgentPreRecordTotalService agentPreRecordTotalService;
	@Resource
	private ExtAccountService extAccountService;
	@Resource
	private AgentShareDaySettleService agentShareDaySettleService;
	
	@Override
	public int insertAgentUnfreeze(AgentUnfreeze agentUnfreeze) {
		return agentUnfreezeMapper.insertAgentUnfreeze(agentUnfreeze);
	}
	@Override
	public int updateAgentUnfreeze(AgentUnfreeze agentUnfreeze) {
		return agentUnfreezeMapper.updateAgentUnfreeze(agentUnfreeze);
	}
	@Override
	public int deleteAgentUnfreeze(Integer id) {
		return agentUnfreezeMapper.deleteAgentUnfreeze(id);
	}
	@Override
	public List<AgentUnfreeze> findAllAgentUnfreeze() {
		return agentUnfreezeMapper.findAllAgentUnfreeze();
	}
	@Override
	public List<AgentUnfreeze> findAgentUnfreezeList(AgentUnfreeze agentUnfreeze, Map<String, String> params, Sort sort,
			Page<AgentUnfreeze> page) {
		String unfreezeTime1 = params.get("unfreezeTime1");
		String unfreezeTime2 = params.get("unfreezeTime2");
		
		if (StringUtils.isNotBlank(unfreezeTime1)) {
			unfreezeTime1 = unfreezeTime1 + " 00:00:00";
			params.put("unfreezeTime1", unfreezeTime1);
		}
		if (StringUtils.isNotBlank(unfreezeTime2)) {
			unfreezeTime2 = unfreezeTime2 + " 23:59:59";
			params.put("unfreezeTime2", unfreezeTime2);
		}
		
		return agentUnfreezeMapper.findAgentUnfreezeList(agentUnfreeze, params, sort, page);
	}
	@Override
	public AgentUnfreeze findAgentUnfreezeById(Integer id) {
		return agentUnfreezeMapper.findAgentUnfreezeById(id);
	}
	@Override
	public List<AgentUnfreeze> exportAgentsProfitUnfreezeList(AgentUnfreeze agentUnfreeze,
			Map<String, String> params) {
		return agentUnfreezeMapper.exportAgentsProfitUnfreezeList(agentUnfreeze, params);
	}

	//方法作废，事务有问题
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> saveBatchUnfreezeExcelDetails(Map<String, Object> map) throws Exception {
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> resultMapData = new HashMap<String, Object>();
		List<AgentUnfreeze> agentUnfreezeExcel = new ArrayList<AgentUnfreeze>();
		agentUnfreezeExcel = (List<AgentUnfreeze>) map.get("agentUnfreezes");
		for (AgentUnfreeze agentUnfreeze : agentUnfreezeExcel) {
			log.info("批量解冻代理商编号：" + agentUnfreeze.getAgentNo());
			result = this.saveAgentsProfitUnfreeze(agentUnfreeze);				//事务有问题作废，跟预冻结一样的，除非迁移到另外一个类
			boolean resultStatus = (boolean) result.get("status");
			String resultMsg = (String) result.get("msg");
			if (resultStatus) {
				resultMapData.put("status", true);
				resultMapData.put("msg", "导入成功");
				log.info(resultMapData.toString());
			}
			else{
				resultMapData.put("status", true);
				resultMapData.put("msg", resultMsg);
				log.info(resultMapData.toString());
			}
		}
		result.put("resultMapData",resultMapData);
		return result;
	}
	
	@Override
	public Map<String, Object> saveAgentsProfitUnfreeze(AgentUnfreeze agentUnfreeze) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		String agentNo = agentUnfreeze.getAgentNo();
		String agentName = agentUnfreeze.getAgentName();
		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal unfreezeAmount = agentUnfreeze.getAmount(); 
		if (unfreezeAmount == null) {
			msg.put("msg","解冻金额不能为空");
			msg.put("status",false);
			return msg;
		}
		else if (unfreezeAmount.compareTo(BigDecimal.ZERO) <= 0) {
			msg.put("msg","解冻金额必须大于0");
			msg.put("status",false);
			return msg;
		}
		
		log.info("agentNo --> " + agentNo);
		AgentPreRecordTotal agentPreRecordTotal = agentPreRecordTotalService.findAgentPreRecordTotalByAgentNo(agentNo);
		
		BigDecimal terminalFreezeAmount =  BigDecimal.ZERO;		//机具冻结款
		BigDecimal otherFreezeAmount =  BigDecimal.ZERO;		//其他冻结款
		if (agentPreRecordTotal != null) {
			terminalFreezeAmount =  agentPreRecordTotal.getTerminalFreezeAmount();
			otherFreezeAmount =  agentPreRecordTotal.getOtherFreezeAmount();
		}
		
		amount = unfreezeAmount;		//需要解冻的金额
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
		ExtAccount extAccount= extAccountService.getExtAccount(accountNo);		//获取外部用户账户
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
		BigDecimal activityControlAmount = BigDecimal.ZERO;
		ExtAccountInfo activityExtAccountInfo= extAccountService.findExtAccountInfoByManyParams(activityAccountType, activityUserId, activityAccountOwner, activitySubjectNo, activityCurrencyNo);
		if(activityExtAccountInfo != null){		//如果存在活动补贴账户
			String activityAccountNo = activityExtAccountInfo.getAccountNo();
			ExtAccount activityExtAccount= extAccountService.getExtAccount(activityAccountNo);		//获取获取活动补贴外部用户账户
			if(activityExtAccount == null){
				String reponseMsg = String.format("没找到外部账号,科目%s代理商编号%s", activitySubjectNo,activityUserId);
				msg.put("status", false);
				msg.put("msg", reponseMsg);
				msg.put("timestamp", String.valueOf(new Date().getTime()));
				log.error(msg.toString());
				throw new RuntimeException(reponseMsg);
			}
			activityControlAmount = activityExtAccount.getControlAmount();			//活动补贴已冻结金额
		}

		//开始计算如何扣减
		BigDecimal controlAmount = extAccount.getControlAmount();		//分润账户已冻结金额
		BigDecimal sumUnfreezeAmount = terminalFreezeAmount.add(otherFreezeAmount).add(controlAmount).add(activityControlAmount);
		if (amount.compareTo(sumUnfreezeAmount) > 0) {
			//代理商编号XXX，代理商名称XX，解冻金额XX元，大于总冻结金额XX元
			String reponseMsg = String.format("代理商编号%s，代理商名称%s，解冻金额%s元，大于总冻结金额%s元",agentNo,agentName,unfreezeAmount,sumUnfreezeAmount);
			throw new RuntimeException(reponseMsg);
		}
		//4个变量保存每次抵扣的明细，做记录
		BigDecimal amount1 = BigDecimal.ZERO;		//扣减的机具冻结款
		BigDecimal amount2 = BigDecimal.ZERO;		//扣减的其他冻结款
		BigDecimal amount3 = BigDecimal.ZERO;		//扣减的分润账户冻结款
		BigDecimal amount4 = BigDecimal.ZERO;		//扣减的活动补贴账户冻结款
		if (agentPreRecordTotal != null) {
			if (terminalFreezeAmount.compareTo(amount) >= 0) {		//如果机具冻结款足够，则直接从机具冻结款扣减
				amount1 = amount;
				amount = terminalFreezeAmount.subtract(amount);
				agentPreRecordTotal.setTerminalFreezeAmount(amount);
			}else{							//机具冻结款不够
				amount1 = terminalFreezeAmount;
				agentPreRecordTotal.setTerminalFreezeAmount(BigDecimal.ZERO);
				amount = amount.subtract(terminalFreezeAmount);			//先扣减机具冻结款
				if (otherFreezeAmount.compareTo(amount) >= 0) {			//判断其他冻结款，如果足够
					amount2 = amount;
					amount = otherFreezeAmount.subtract(amount);
					agentPreRecordTotal.setOtherFreezeAmount(amount);
				}else{			//机具冻结款+其他冻结款也不够
					amount2 = otherFreezeAmount;
					agentPreRecordTotal.setOtherFreezeAmount(BigDecimal.ZERO);
					amount = amount.subtract(otherFreezeAmount);
					//机具冻结款+其他冻结款都不够，开始扣减活动补贴账户冻结款
					//下面连续的调用接口，可以先计算要扣减的金额，然后在代码最下面做接口调用这样会好很多，懒得写了！
					if (activityControlAmount.compareTo(amount) >= 0) {
						amount3 = amount;
						//够扣,调用解冻接口
						if(amount.compareTo(BigDecimal.ZERO)>0){
							Map<String,Object> result = extAccountService.agentUnFreezeAmount(agentNo,activitySubjectNo, amount);
							boolean resultStatus = (boolean) result.get("status");
							String resultMsg = (String) result.get("msg");
							if (resultStatus) {
								msg.put("status", true);
								msg.put("msg", "调用解冻接口解冻成功");
								log.info(msg.toString());
							}
							else{
								msg.put("status", false);
								msg.put("msg", resultMsg);
								log.info(msg.toString());
								throw new RuntimeException(resultMsg);
							}
						}
					}else{
						//活动补贴账户的也不够扣
						amount3 = activityControlAmount;
						agentPreRecordTotal.setOtherFreezeAmount(BigDecimal.ZERO);
						amount = amount.subtract(activityControlAmount);
						//先扣减活动补贴冻结款再去扣减分润账户冻结款
						Map<String,Object> result = new HashMap<>();
						String resultMsg = "";
						boolean resultStatus = false;
						if(activityControlAmount.compareTo(BigDecimal.ZERO)>0){
							result = extAccountService.agentUnFreezeAmount(agentNo,activitySubjectNo, activityControlAmount);
							resultStatus = (boolean) result.get("status");
							resultMsg = (String) result.get("msg");
							if (resultStatus) {
								msg.put("status", true);
								msg.put("msg", "调用解冻接口解冻成功");
								log.info(msg.toString());
							}else{
								msg.put("status", false);
								msg.put("msg", resultMsg);
								log.info("第一步扣减活动补贴账户冻结款失败，"+msg.toString());
								throw new RuntimeException(resultMsg);
							}
						}

						//开始扣减分润账户冻结款
						if(controlAmount.compareTo(amount) >= 0){
							amount4 = amount;
							//够扣,调用解冻接口
							if(amount.compareTo(BigDecimal.ZERO)>0){
								result = extAccountService.agentUnFreezeAmount(agentNo,subjectNo, amount);
								resultStatus = (boolean) result.get("status");
								resultMsg = (String) result.get("msg");
								if (resultStatus) {
									msg.put("status", true);
									msg.put("msg", "调用解冻接口解冻成功");
									msg.put("fenrun", true);
									log.info(msg.toString());
								}
								else{
									msg.put("status", false);
									msg.put("msg", resultMsg);
									log.info("第二步扣减分润账户冻结款失败，"+msg.toString());
									throw new RuntimeException(resultMsg);
								}
							}
						}
					}
				}
			}
			agentPreRecordTotalService.updateAgentPreRecordTotal(agentPreRecordTotal);
		}else{
			if (activityControlAmount.compareTo(amount) >= 0) {
				amount3 = amount;
				//够扣,调用解冻接口
				if(amount.compareTo(BigDecimal.ZERO)>0){
					Map<String,Object> result = extAccountService.agentUnFreezeAmount(agentNo,activitySubjectNo, amount);
					boolean resultStatus = (boolean) result.get("status");
					String resultMsg = (String) result.get("msg");
					if (resultStatus) {
						msg.put("status", true);
						msg.put("msg", "调用解冻接口解冻成功");
						log.info(msg.toString());
					}
					else{
						msg.put("status", false);
						msg.put("msg", resultMsg);
						log.info(msg.toString());
						throw new RuntimeException(resultMsg);
					}
				}
			}else{
				//活动补贴账户不够扣
				amount3 = activityControlAmount;
				amount = amount.subtract(activityControlAmount);
				//先扣减活动补贴冻结款再去扣减分润账户冻结款
				if(activityControlAmount.compareTo(BigDecimal.ZERO) > 0){		//有钱的话，才去调用接口
					Map<String,Object> result = extAccountService.agentUnFreezeAmount(agentNo,activitySubjectNo, activityControlAmount);
					boolean resultStatus = (boolean) result.get("status");
					String resultMsg = (String) result.get("msg");
					if (resultStatus) {
						msg.put("status", true);
						msg.put("msg", "调用解冻接口解冻成功");
						log.info(msg.toString());
					}
					else{
						msg.put("status", false);
						msg.put("msg", resultMsg);
						log.info("第一步扣减活动补贴账户冻结款失败，"+msg.toString());
						throw new RuntimeException(resultMsg);
					}
				}else{
					log.info("活动补贴账户冻结款没钱，不用调用接口;");
				}


				//开始扣减分润账户冻结款
				if(controlAmount.compareTo(amount) >= 0) {
					amount4 = amount;
					//够扣,调用解冻接口
					if(amount.compareTo(BigDecimal.ZERO)>0){
						Map<String,Object> result = extAccountService.agentUnFreezeAmount(agentNo, subjectNo, amount);
						boolean resultStatus = (boolean) result.get("status");
						String resultMsg = (String) result.get("msg");
						if (resultStatus) {
							msg.put("status", true);
							msg.put("msg", "调用解冻接口解冻成功");
							msg.put("fenrun", true);
							log.info(msg.toString());
						} else {
							msg.put("status", false);
							msg.put("msg", resultMsg);
							log.info("第二步扣减分润账户冻结款失败，" + msg.toString());
							throw new RuntimeException(resultMsg);
						}
					}
				}
			}
		}
		agentUnfreeze.setTerminalFreezeAmount(amount1);
		agentUnfreeze.setOtherFreezeAmount(amount2);
		agentUnfreeze.setActivityFreezeAmount(amount3);
		agentUnfreeze.setFenFreezeAmount(amount4);
		agentUnfreezeMapper.insertAgentUnfreeze(agentUnfreeze);

		msg.put("status", true);
		msg.put("msg", "执行成功");
		return msg;
	}
	@Override
	public Map<String, Object> saveAgentsProfitBatchUnfreeze(List<AgentUnfreeze> agentUnfreezeList) throws Exception {
		Map<String,Object> msg=new HashMap<>();
		for (AgentUnfreeze agentUnfreeze : agentUnfreezeList) {
			saveAgentsProfitUnfreeze(agentUnfreeze);
		}
		msg.put("status", true);
		msg.put("msg", "执行成功");
		return msg;
	}
}
