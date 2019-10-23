package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.AgentDayBalanceMapper;
import cn.eeepay.framework.dao.nposp.AgentInfoMapper;
import cn.eeepay.framework.model.bill.AgentAccountDay;
import cn.eeepay.framework.model.bill.AgentEverydayBalance;
import cn.eeepay.framework.model.nposp.AgentInfo;
import cn.eeepay.framework.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("agentDayBalanceService")
public class AgentDayBalanceServiceImpl {
	private static final Logger log = LoggerFactory.getLogger(AgentDayBalanceServiceImpl.class);
	@Resource
	public AgentDayBalanceMapper agentDayBalanceMapper;

	@Resource
	private AgentInfoMapper agentInfoMapper;





	/**
	 *
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAgentDayBalance(String nowDate) throws Exception {
		Map<String,Object> msg=new HashMap<>();


		// 查询汇总当天余额
		String transTypeIn ="credit";   //入账
		String transTypeOut ="debit";  //出账

//		String nowDate = "2017-10-19";
		Date recordDate = DateUtil.parseDateTime(nowDate);
		List<AgentAccountDay> shareList =  agentDayBalanceMapper.findAgentShareAccountList(transTypeIn,nowDate,"224105");
		List<AgentAccountDay> shareListOut =  agentDayBalanceMapper.findAgentShareAccountList(transTypeOut,nowDate,"224105");
		//活动补贴账户数据插入
		List<AgentAccountDay> actList =  agentDayBalanceMapper.findAgentShareAccountList(transTypeIn,nowDate,"224106");
		List<AgentAccountDay> actListOut =  agentDayBalanceMapper.findAgentShareAccountList(transTypeOut,nowDate,"224106");
		// 查询代理商信息
		List<AgentInfo> agentInfoList = agentInfoMapper.findAllAgentInfoList();

		//入账汇总表数据
		List<AgentEverydayBalance> agentEverydayBalanceList = new ArrayList<AgentEverydayBalance>();
		//获取批次号
		String batchNo = this.randomBatchNum();
		for (AgentInfo agent:agentInfoList) {
			AgentEverydayBalance agentEverydayBalance = new AgentEverydayBalance();
			agentEverydayBalance.setAgentNo(agent.getAgentNo());
			agentEverydayBalance.setAgentLevel(agent.getAgentLevel()+"");
			agentEverydayBalance.setAgentNode(agent.getAgentNode());
			agentEverydayBalance.setAgentName(agent.getAgentName());
			agentEverydayBalance.setSubjectNo("224105");
			agentEverydayBalance.setCreateTime(new Date());
			agentEverydayBalance.setBatchNo(batchNo);
			agentEverydayBalance.setRecordDate(recordDate);
			for (AgentAccountDay agentAcount:shareList) {
				if(agentAcount.getAgentNo().equals(agent.getAgentNo())){
//					agentEverydayBalance.setAgentNo(agentAcount.getAgentNo());
//					agentEverydayBalance.setAgentLevel(agent.getAgentLevel()+"");
//					agentEverydayBalance.setAgentNode(agent.getAgentNode());
//					agentEverydayBalance.setAgentName(agent.getAgentName());
//					agentEverydayBalance.setSubjectNo(agentAcount.getSubjectNo());
//					agentEverydayBalance.setCreateTime(new Date());
//					agentEverydayBalance.setBatchNo(batchNo);
//					agentEverydayBalance.setRecordDate(recordDate);
					agentEverydayBalance.setAccountNo(agentAcount.getAccountNo());
					agentEverydayBalance.setTransInAmount(agentAcount.getRecordAuountSum());
					agentEverydayBalance.setAccountStatus(agentAcount.getAccountStatus());
					agentEverydayBalance.setBalance(agentAcount.getBalance());
					agentEverydayBalance.setFreezeAmount(agentAcount.getControlAmount());
				}
			}
			for (AgentAccountDay agentAcountOut:shareListOut) {
				if(agentAcountOut.getAgentNo().equals(agent.getAgentNo())){
					agentEverydayBalance.setTransOutAmount(agentAcountOut.getRecordAuountSum());
					agentEverydayBalance.setAccountNo(agentAcountOut.getAccountNo());
					agentEverydayBalance.setAccountStatus(agentAcountOut.getAccountStatus());
					agentEverydayBalance.setBalance(agentAcountOut.getBalance());
					agentEverydayBalance.setFreezeAmount(agentAcountOut.getControlAmount());
				}
			}

			agentEverydayBalanceList.add(agentEverydayBalance);

			//活动账户
			AgentEverydayBalance agentEverydayBalanceAct = new AgentEverydayBalance();
			agentEverydayBalanceAct.setAgentNo(agent.getAgentNo());
			agentEverydayBalanceAct.setAgentLevel(agent.getAgentLevel()+"");
			agentEverydayBalanceAct.setAgentNode(agent.getAgentNode());
			agentEverydayBalanceAct.setAgentName(agent.getAgentName());
			agentEverydayBalanceAct.setSubjectNo("224106");
			agentEverydayBalanceAct.setCreateTime(new Date());
			agentEverydayBalanceAct.setBatchNo(batchNo);
			agentEverydayBalanceAct.setRecordDate(recordDate);

			for (AgentAccountDay agentActAcount:actList) {
				if(agentActAcount.getAgentNo().equals(agent.getAgentNo())){
					agentEverydayBalanceAct.setAccountNo(agentActAcount.getAccountNo());
					agentEverydayBalanceAct.setTransInAmount(agentActAcount.getRecordAuountSum());
					agentEverydayBalanceAct.setAccountStatus(agentActAcount.getAccountStatus());
					agentEverydayBalanceAct.setFreezeAmount(agentActAcount.getControlAmount());
					agentEverydayBalanceAct.setBalance(agentActAcount.getBalance());
				}
			}
			for (AgentAccountDay agentAcountOut:actListOut) {
				if(agentAcountOut.getAgentNo().equals(agent.getAgentNo())){
					agentEverydayBalanceAct.setAccountNo(agentAcountOut.getAccountNo());
					agentEverydayBalanceAct.setTransOutAmount(agentAcountOut.getRecordAuountSum());
					agentEverydayBalanceAct.setAccountStatus(agentAcountOut.getAccountStatus());
					agentEverydayBalanceAct.setBalance(agentAcountOut.getBalance());
					agentEverydayBalanceAct.setFreezeAmount(agentAcountOut.getControlAmount());
				}

			}
			agentEverydayBalanceList.add(agentEverydayBalanceAct);

		}

		//补充初始金额
		List<AgentEverydayBalance> list= this.setAccountInitBalance(agentEverydayBalanceList,nowDate);
		System.out.println("11111111111111111111");
		agentDayBalanceMapper.insertAgentDayBalanceBatch(list);

//
//		//出账汇总表数据
//		List<AgentEverydayBalance> agentEverydayBalanceListOut = new ArrayList<AgentEverydayBalance>();
//		for (AgentAccountDay agentAcount:shareListOut) {
//			for (AgentInfo agent:agentInfoList) {
//				if(agentAcount.getAgentNo().equals(agent.getAgentNo())){
//					AgentEverydayBalance agentEverydayBalance = new AgentEverydayBalance();
//					agentEverydayBalance.setAccountNo(agentAcount.getAccountNo());
//					agentEverydayBalance.setAgentNo(agentAcount.getAgentNo());
//					agentEverydayBalance.setAgentLevel(agent.getAgentLevel()+"");
//					agentEverydayBalance.setAgentNode(agent.getAgentNode());
//					agentEverydayBalance.setAgentName(agent.getAgentName());
//					agentEverydayBalance.setTransOutAmount(agentAcount.getRecordAuountSum());
//					agentEverydayBalance.setSubjectNo(agentAcount.getSubjectNo());
//					agentEverydayBalance.setAccountStatus(agentAcount.getAccountStatus());
//					agentEverydayBalance.setCreateTime(new Date());
//					agentEverydayBalance.setBalance(agentAcount.getBalance());
//					agentEverydayBalance.setBatchNo(batchNo);
//					agentEverydayBalance.setFreezeAmount(agentAcount.getControlAmount());
//					agentEverydayBalance.setRecordDate(recordDate);
//					agentEverydayBalanceListOut.add(agentEverydayBalance);
//				}
//			}
//		}
//		if(agentEverydayBalanceListOut.size()>0) {
//			//补充初始金额
//			List<AgentEverydayBalance> list= this.setAccountInitBalance(agentEverydayBalanceListOut,nowDate);
//			agentDayBalanceMapper.insertAgentDayBalanceBatch(list);
//		}
//
//
//
//		//活动补贴入账汇总表数据
//		List<AgentEverydayBalance> agentActEverydayBalanceList = new ArrayList<AgentEverydayBalance>();
//		for (AgentAccountDay agentActAcount:actList) {
//
//			for (AgentInfo agent:agentInfoList) {
//				if(agentActAcount.getAgentNo().equals(agent.getAgentNo())){
//					AgentEverydayBalance agentEverydayBalance = new AgentEverydayBalance();
//					agentEverydayBalance.setAccountNo(agentActAcount.getAccountNo());
//					agentEverydayBalance.setAgentNo(agentActAcount.getAgentNo());
//					agentEverydayBalance.setAgentLevel(agent.getAgentLevel()+"");
//					agentEverydayBalance.setAgentNode(agent.getAgentNode());
//					agentEverydayBalance.setAgentName(agent.getAgentName());
//					agentEverydayBalance.setTransInAmount(agentActAcount.getRecordAuountSum());
//					agentEverydayBalance.setSubjectNo(agentActAcount.getSubjectNo());
//					agentEverydayBalance.setCreateTime(new Date());
//					agentEverydayBalance.setAccountStatus(agentActAcount.getAccountStatus());
//					agentEverydayBalance.setBalance(agentActAcount.getBalance());
//					agentEverydayBalance.setBatchNo(batchNo);
//					agentEverydayBalance.setFreezeAmount(agentActAcount.getControlAmount());
//					agentEverydayBalance.setRecordDate(recordDate);
//					agentActEverydayBalanceList.add(agentEverydayBalance);
//
//				}
//			}
//		}
//		if(agentActEverydayBalanceList.size()>0){
//			//补充初始金额
//			List<AgentEverydayBalance> list= this.setAccountInitBalance(agentActEverydayBalanceList,nowDate);
//			agentDayBalanceMapper.insertAgentDayBalanceBatch(list);
//		}
//
//		//活动补贴出账汇总表数据
//		List<AgentEverydayBalance> agentActEverydayBalanceListOut = new ArrayList<AgentEverydayBalance>();
//		for (AgentAccountDay agentAcount:actListOut) {
//			for (AgentInfo agent:agentInfoList) {
//				if(agentAcount.getAgentNo().equals(agent.getAgentNo())){
//					AgentEverydayBalance agentEverydayBalance = new AgentEverydayBalance();
//					agentEverydayBalance.setAccountNo(agentAcount.getAccountNo());
//					agentEverydayBalance.setAgentNo(agentAcount.getAgentNo());
//					agentEverydayBalance.setAgentLevel(agent.getAgentLevel()+"");
//					agentEverydayBalance.setAgentNode(agent.getAgentNode());
//					agentEverydayBalance.setAgentName(agent.getAgentName());
//					agentEverydayBalance.setCreateTime(new Date());
//					agentEverydayBalance.setTransOutAmount(agentAcount.getRecordAuountSum());
//					agentEverydayBalance.setSubjectNo(agentAcount.getSubjectNo());
//					agentEverydayBalance.setAccountStatus(agentAcount.getAccountStatus());
//					agentEverydayBalance.setBalance(agentAcount.getBalance());
//					agentEverydayBalance.setBatchNo(batchNo);
//					agentEverydayBalance.setFreezeAmount(agentAcount.getControlAmount());
//					agentEverydayBalance.setRecordDate(recordDate);
//					agentActEverydayBalanceListOut.add(agentEverydayBalance);
//				}
//			}
//		}
//		if(agentActEverydayBalanceListOut.size()>0) {
//			//补充初始金额
//			List<AgentEverydayBalance> list= this.setAccountInitBalance(agentActEverydayBalanceListOut,nowDate);
//			agentDayBalanceMapper.insertAgentDayBalanceBatch(list);
//		}

		msg.put("status", true);
		msg.put("msg", "代理商账户汇总完成");
		log.info(msg.toString());
		return msg;
	}

	private String randomBatchNum() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String format = dateFormat.format(new Date());
		int max=10;
		int min=3;
		Random random = new Random();
		int s = random.nextInt(max)%(max-min+1) + min;
		StringBuffer buffer =new StringBuffer();
		for(int i=0;i<s;i++){
			Integer val = (int)(Math.random()*9+1);
			buffer.append(val.toString());
		}
		return format+buffer.toString();
	}

	//填充初始余额
	private List<AgentEverydayBalance>  setAccountInitBalance(List<AgentEverydayBalance> agentEverydayBalanceList,String nowDate) {

		for (AgentEverydayBalance agentEverydayBalance:agentEverydayBalanceList) {
			if(agentEverydayBalance.getAccountNo()!=null && !"".equals(agentEverydayBalance.getAccountNo())){
				AgentAccountDay accountNow = agentDayBalanceMapper.findAccountNowBalanceByDate(agentEverydayBalance.getAccountNo(),nowDate);
				if(accountNow!=null && !"".equals(accountNow.getAccountNo())){
					agentEverydayBalance.setInitBalance(accountNow.getBefBalance());
					agentEverydayBalance.setBalance(accountNow.getNowBalance());
				}
			}else {
				//

//				AgentAccountDay userAccount =  agentDayBalanceMapper.findAccountByUserId(agentEverydayBalance.getAgentNo(),agentEverydayBalance.getSubjectNo());
//				if(userAccount!=null && !"".equals(userAccount.getAccountNo())){
//					AgentAccountDay accountNow2 = agentDayBalanceMapper.findAccountNowBalanceByDate(userAccount.getAccountNo(),nowDate);
//					if(accountNow2!=null && !"".equals(accountNow2.getAccountNo())){
//						agentEverydayBalance.setInitBalance(accountNow2.getBefBalance());
//						agentEverydayBalance.setBalance(accountNow2.getNowBalance());
//					}
//					agentEverydayBalance.setAccountNo(userAccount.getAccountNo());
//					agentEverydayBalance.setFreezeAmount(userAccount.getControlAmount());
//					agentEverydayBalance.setAccountStatus(userAccount.getAccountStatus());
//				}

			}
		}
		//查询所有的账户号

		//查询所有账户前一天的初始余额

		//查询所有账户当天余额

		return  agentEverydayBalanceList;
	}
}
