package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import cn.eeepay.framework.enums.SystemStatus;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtNobookedDetail;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideNobookedDetail;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.OutChannelLadderRateRebalance;
import cn.eeepay.framework.model.bill.ShadowAccount;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.model.bill.TransImportInfo;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.service.bill.BatchService;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.ExtAccountHistoryBalanceService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.ExtNobookedDetailService;
import cn.eeepay.framework.service.bill.ExtTransInfoService;
import cn.eeepay.framework.service.bill.InsAccountHistoryBalanceService;
import cn.eeepay.framework.service.bill.InsAccountService;
import cn.eeepay.framework.service.bill.InsideNobookedDetailService;
import cn.eeepay.framework.service.bill.InsideTransInfoService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.OutAccountTaskDetailService;
import cn.eeepay.framework.service.bill.OutAccountTaskService;
import cn.eeepay.framework.service.bill.OutChannelLadderRateRebalanceService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectInfoService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.bill.TransImportInfoService;
import cn.eeepay.framework.service.nposp.AcqOrgService;
import cn.eeepay.framework.service.nposp.OutAccountServiceService;
import cn.eeepay.framework.service.nposp.SysCalendarService;
import cn.eeepay.framework.util.DateUtil;
@Service("batchService")
@Transactional
public class BatchServiceImpl implements BatchService {
	private static final Logger log = LoggerFactory.getLogger(BatchServiceImpl.class);
	@Resource
	public SystemInfoService systemInfoService;
	@Resource
	public InsAccountService inputAccountService;
	@Resource
	private InsAccountHistoryBalanceService insAccountHistoryBalanceService;
	@Resource
	public InsideNobookedDetailService insideNobookedDetailService;
	@Resource
	public InsideTransInfoService insideTransInfoService;
	@Resource
	public SubjectService subjectService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	public CurrencyService currencyService;
	@Resource
	public SubjectInfoService subjectInfoService;
	@Resource
	public CoreTransInfoService coreTransInfoService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	private ExtAccountHistoryBalanceService extAccountHistoryBalanceService;
	@Resource
	public ExtNobookedDetailService extNobookedDetailService;
	@Resource
	public ExtTransInfoService extTransInfoService;
	@Resource
	public ShadowAccountService shadowAccountService;
	@Resource
	private AcqOrgService acqOrgService;
	@Resource
	private TransImportInfoService transImportInfoService;
	@Resource
	private OutAccountTaskService outAccountTaskService;
	@Resource
	private OutAccountTaskDetailService outAccountTaskDetailService;
	@Resource
	private SysCalendarService sysCalendarService;
	@Resource
	private OutAccountServiceService outAccountServiceService;
	@Resource
	private OutChannelLadderRateRebalanceService outChannelLadderRateRebalanceService;
	
	//日切
	@Override
	public boolean runCutOff() throws Exception {
		SystemInfo systemInfo = new SystemInfo();
		Date d = new Date();
		systemInfo.setId(1);
		systemInfo.setStatus(SystemStatus.CUTOFF.toString());
		systemInfo.setCurrentDate(new Date());
		systemInfo.setNextTransDate(DateUtil.getAfterDate(d));
		systemInfo.setParentTransDate(DateUtil.getBeforeDate(d));
		boolean result = false;
		int i=0;
		try {
			i = systemInfoService.updateSystemInfo(systemInfo);
			if (i > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}
	//	日终修改余额账户类的余额处理-内部账
	@Override
	public boolean runAccoutUpdateAmount() throws Exception {
		String day_bal_flag = "1";
    	try {
	    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String record_date = formatter1.format(new Date());
			Date recordDate = formatter1.parse(record_date);
			Time recordTime = new Time(recordDate.getTime());
    	
    		List<InsAccount> inputAccounts = inputAccountService.findInputAccountByDayBalFlag(day_bal_flag);
	    	for (InsAccount inputAccount : inputAccounts) {
	    		//1-日终单笔
				if ("1".equals(inputAccount.getSumFlag())) {
					String serialNo = String.valueOf(System.currentTimeMillis());
					List<InsideNobookedDetail> insideNobookedDetails= insideNobookedDetailService.findInsideNobookedDetailByParams(inputAccount.getAccountNo(), inputAccount.getParentTransDay(), "0");
					int i = 0;
					BigDecimal balance = inputAccount.getCurrBalance();
					for (InsideNobookedDetail insideNobookedDetail : insideNobookedDetails) {
						InsideTransInfo insideTransInfo = new InsideTransInfo();
						insideTransInfo.setAccountNo(insideNobookedDetail.getAccountNo());
						insideTransInfo.setRecordAmount(insideNobookedDetail.getTransAmount());
						
						if (inputAccount.getBalanceAddFrom().equals(insideNobookedDetail.getDebitCreditSide())) {
							balance = balance.add(insideNobookedDetail.getTransAmount());
						}
						else{
							balance = balance.subtract(insideNobookedDetail.getTransAmount());
						}
						
						insideTransInfo.setBalance(balance);
						insideTransInfo.setAvaliBalance(balance.subtract(inputAccount.getControlAmount()));		// 当前余额-控制金额 （-结算中金额）??
						insideTransInfo.setSerialNo(serialNo);
						i++;
						String childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
						insideTransInfo.setChildSerialNo(childSerialNo);
						insideTransInfo.setRecordDate(recordDate);
						insideTransInfo.setRecordTime(recordTime);
						insideTransInfo.setDebitCreditSide(insideNobookedDetail.getDebitCreditSide());
						insideTransInfo.setSummaryInfo(insideNobookedDetail.getSummaryInfo());
						insideTransInfoService.insertInsideTransInfo(insideTransInfo);
					}
					inputAccount.setCurrBalance(balance);
					
					inputAccountService.updateInsAccount(inputAccount);
					
				}
				//2-日终汇总
				else if ("2".equals(inputAccount.getSumFlag())) { 
					String serialNo = String.valueOf(System.currentTimeMillis());
					List<InsideNobookedDetail> insideNobookedDetails= insideNobookedDetailService.findInsideNobookedDetailByParams(inputAccount.getAccountNo(), inputAccount.getParentTransDay(), "0");
					int i = 0;
					BigDecimal balance = inputAccount.getCurrBalance();
					BigDecimal debit_balance = new BigDecimal("0");//借
					BigDecimal credit_balance = new BigDecimal("0");//贷
					for (InsideNobookedDetail insideNobookedDetail : insideNobookedDetails) {
						if (inputAccount.getBalanceAddFrom().toLowerCase().equals("debit")) {
							debit_balance = debit_balance.add(insideNobookedDetail.getTransAmount());
						}
						else{
							credit_balance = credit_balance.add(insideNobookedDetail.getTransAmount());
						}
					}
					
					if (inputAccount.getBalanceAddFrom().toLowerCase().equals("debit")) {
						balance = balance.add(debit_balance);
					}
					else{
						balance = balance.subtract(credit_balance);
					}
					
					InsideTransInfo insideTransInfo = new InsideTransInfo();
					insideTransInfo.setAccountNo(inputAccount.getAccountNo());
					insideTransInfo.setRecordAmount(debit_balance);
					insideTransInfo.setBalance(balance);
					insideTransInfo.setAvaliBalance(balance.subtract(inputAccount.getControlAmount()));		//  同上？？
					insideTransInfo.setSerialNo(serialNo);
					i++;
					String childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
					insideTransInfo.setChildSerialNo(childSerialNo);
					insideTransInfo.setRecordDate(recordDate);
					insideTransInfo.setRecordTime(recordTime);
					insideTransInfo.setDebitCreditSide("debit");
					
					insideTransInfoService.insertInsideTransInfo(insideTransInfo);		//两条插入语句  ??
					
					insideTransInfo.setAccountNo(inputAccount.getAccountNo());
					insideTransInfo.setRecordAmount(credit_balance);
					insideTransInfo.setBalance(balance);
					insideTransInfo.setAvaliBalance(balance.subtract(inputAccount.getControlAmount()));
					insideTransInfo.setSerialNo(serialNo);
					i++;
					childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
					insideTransInfo.setChildSerialNo(childSerialNo);
					insideTransInfo.setRecordDate(recordDate);
					insideTransInfo.setRecordTime(recordTime);
					insideTransInfo.setDebitCreditSide("credit");
					
					insideTransInfoService.insertInsideTransInfo(insideTransInfo);		//两条插入语句  ??
					
					inputAccount.setCurrBalance(balance);
					
					inputAccountService.updateInsAccount(inputAccount);
					
				}
			}
    	} catch (Exception e) {
			//log.error("异常:",e);
			return false;
		}
    	
    	return true;
	}
	
	//日终修改余额账户类的余额处理-外部账
	@Override
	public boolean runExtAccoutUpdateAmount() throws Exception {
		String day_bal_flag = "1";
    	try {
	    	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String record_date = formatter1.format(new Date());
			Date recordDate = formatter1.parse(record_date);
			Time recordTime = new Time(recordDate.getTime());
    	
    		List<ExtAccount> extAccounts = extAccountService.findExtAccountByDayBalFlag(day_bal_flag);
	    	for (ExtAccount extAccount : extAccounts) {
	    		//1-日终单笔
				if ("1".equals(extAccount.getSumFlag())) {
					String serialNo = String.valueOf(System.currentTimeMillis());
					List<ExtNobookedDetail> extNobookedDetails= extNobookedDetailService.findExtNobookedDetailByParams(extAccount.getAccountNo(), extAccount.getParentTransDay(), "0");
					int i = 0;
					BigDecimal balance = extAccount.getCurrBalance();
					for (ExtNobookedDetail extNobookedDetail : extNobookedDetails) {
						ExtTransInfo extTransInfo = new ExtTransInfo();
						extTransInfo.setAccountNo(extNobookedDetail.getAccountNo());
						extTransInfo.setRecordAmount(extNobookedDetail.getRecordAmount());
						
						if (extAccount.getBalanceAddFrom().equals(extNobookedDetail.getDebitCreditSide())) {
							balance = balance.add(extNobookedDetail.getRecordAmount());
						}
						else{
							balance = balance.subtract(extNobookedDetail.getRecordAmount());
						}
						
						extTransInfo.setBalance(balance);
						extTransInfo.setAvaliBalance(balance.subtract(extAccount.getControlAmount()));		// 当前余额-控制金额 （-结算中金额）??
						extTransInfo.setSerialNo(serialNo);
						i++;
						String childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
						extTransInfo.setChildSerialNo(childSerialNo);
						extTransInfo.setRecordDate(recordDate);
						extTransInfo.setRecordTime(recordTime);
						extTransInfo.setDebitCreditSide(extNobookedDetail.getDebitCreditSide());
						extTransInfo.setSummaryInfo(extNobookedDetail.getSummaryInfo());
						extTransInfoService.insertExtTransInfo(extTransInfo);
					}
					extAccount.setCurrBalance(balance);
					
					extAccountService.updateExtAccount(extAccount);
					
				}
				//2-日终汇总
				else if ("2".equals(extAccount.getSumFlag())) { 
					String serialNo = String.valueOf(System.currentTimeMillis());
					List<ExtNobookedDetail> extNobookedDetails= extNobookedDetailService.findExtNobookedDetailByParams(extAccount.getAccountNo(), extAccount.getParentTransDay(), "0");
					int i = 0;
					BigDecimal balance = extAccount.getCurrBalance();
					BigDecimal debit_balance = new BigDecimal("0");//借
					BigDecimal credit_balance = new BigDecimal("0");//贷
					for (ExtNobookedDetail extNobookedDetail : extNobookedDetails) {
						if (extAccount.getBalanceAddFrom().toLowerCase().equals("debit")) {
							debit_balance = debit_balance.add(extNobookedDetail.getRecordAmount());
						}
						else{
							credit_balance = credit_balance.add(extNobookedDetail.getRecordAmount());
						}
					}
					
					if (extAccount.getBalanceAddFrom().toLowerCase().equals("debit")) {
						balance = balance.add(debit_balance);
					}
					else{
						balance = balance.subtract(credit_balance);
					}
					
					ExtTransInfo extTransInfo = new ExtTransInfo();
					extTransInfo.setAccountNo(extAccount.getAccountNo());
					extTransInfo.setRecordAmount(debit_balance);
					extTransInfo.setBalance(balance);
					extTransInfo.setAvaliBalance(balance.subtract(extAccount.getControlAmount()));		//  同上？？
					extTransInfo.setSerialNo(serialNo);
					i++;
					String childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
					extTransInfo.setChildSerialNo(childSerialNo);
					extTransInfo.setRecordDate(recordDate);
					extTransInfo.setRecordTime(recordTime);
					extTransInfo.setDebitCreditSide("debit");
					
					extTransInfoService.insertExtTransInfo(extTransInfo);		//两条插入语句  ??
					
					extTransInfo.setAccountNo(extAccount.getAccountNo());
					extTransInfo.setRecordAmount(credit_balance);
					extTransInfo.setBalance(balance);
					extTransInfo.setAvaliBalance(balance.subtract(extAccount.getControlAmount()));
					extTransInfo.setSerialNo(serialNo);
					i++;
					childSerialNo = StringUtils.leftPad(String.valueOf(i), 3, "0");
					extTransInfo.setChildSerialNo(childSerialNo);
					extTransInfo.setRecordDate(recordDate);
					extTransInfo.setRecordTime(recordTime);
					extTransInfo.setDebitCreditSide("credit");
					
					extTransInfoService.insertExtTransInfo(extTransInfo);		//两条插入语句  ??
					
					extAccount.setCurrBalance(balance);
					
					extAccountService.updateExtAccount(extAccount);
					
				}
			}
    	} catch (Exception e) {
			//log.error("异常:",e);
			return false;
		}
    	
    	return true;
	}
	/**
	 * 	入总账
	 */
	@Override
	public boolean runCheckInAllAccount() throws Exception {
		int max_subject_level = subjectService.findMaxSubjectLevel();
		int current_subject_level = 0;
		List<OrgInfo> orgInfos = orgInfoService.findOrgInfo();
		Date before_date2 = DateUtil.getBeforeDate2(new Date()); 
		Date before_trans_time = DateUtil.getBeforeDate(new Date()); 
		for (OrgInfo orgInfo : orgInfos) {
			List<Currency> currencies = currencyService.findCurrency();
			for (Currency currency : currencies) {
				current_subject_level = max_subject_level;
				List<Subject> subjects = subjectService.findSubjectListBySubjectLevel(current_subject_level);
				for (Subject subject : subjects) {
					SubjectInfo subjectInfo = subjectInfoService.findSubjectInfoByParams(subject.getSubjectNo(), orgInfo.getOrgNo(), currency.getCurrencyNo(), before_date2);
					SubjectInfo beforeSubjectInfo = subjectInfoService.findSubjectInfoByParams(subject.getSubjectNo(), orgInfo.getOrgNo(), currency.getCurrencyNo(), before_trans_time);
					List<Subject> subjectChilds = subjectService.getChildSubjectList(subject.getSubjectNo());
					SubjectInfo subjectInfoNew = new SubjectInfo();
					subjectInfoNew.setSubjectNo(subject.getSubjectNo());
					subjectInfoNew.setOrgNo(orgInfo.getOrgNo());
					subjectInfoNew.setCurrencyNo(currency.getCurrencyNo());
					subjectInfoNew.setCreateDate(new Date());
					subjectInfoNew.setSubjectLevel(subject.getSubjectLevel());
					subjectInfoNew.setBalanceFrom(subject.getBalanceFrom());
						
					if (current_subject_level == max_subject_level) {
						
					}
					else{
						 
					}
					CoreTransInfo debitTransInfo = coreTransInfoService.findSubjectAllTransAmount(subject.getSubjectNo(), "debit",before_trans_time);
					CoreTransInfo creditTransInfo = coreTransInfoService.findSubjectAllTransAmount(subject.getSubjectNo(), "credit",before_trans_time);
					BigDecimal debitTransAmount = new BigDecimal("0");
					BigDecimal creditTransAmount = new BigDecimal("0");
					if (debitTransInfo != null) {
						debitTransAmount = debitTransInfo.getTransAmount();
					}
					if (creditTransInfo != null) {
						creditTransAmount = creditTransInfo.getTransAmount();
					}
					subjectInfoNew.setTodayDebitAmount(debitTransAmount);
					subjectInfoNew.setTodayCreditAmount(creditTransAmount);
					if (subjectInfo !=null && "debit".equals(subjectInfo.getBalanceFrom())) {
						subjectInfoNew.setYesterdayAmount(subjectInfo.getTodayBalance().add(debitTransInfo.getTransAmount()).subtract(creditTransInfo.getTransAmount()));
					}
					else if (subjectInfo !=null && "credit".equals(subjectInfo.getBalanceFrom())) {
						subjectInfoNew.setYesterdayAmount(subjectInfo.getTodayBalance().add(creditTransInfo.getTransAmount()).subtract(debitTransInfo.getTransAmount()));
					}
					List<ExtAccount> allOutAccounts = new ArrayList<>();
					for (Subject subjectChild : subjectChilds) {
						List<ExtAccount> outAccounts = extAccountService.findExtAccountBySubjectNo(subjectChild.getSubjectNo());
						allOutAccounts.addAll(outAccounts);
					}
					BigDecimal allOutBalance = new BigDecimal("0");
					for (ExtAccount outAccount : allOutAccounts) {
						allOutBalance.add(outAccount.getCurrBalance());
					}
					
					List<InsAccount> allInputAccounts = new ArrayList<>();
					for (Subject subjectChild : subjectChilds) {
						List<InsAccount> inAccounts = inputAccountService.findInputAccountBySubjectNo(subjectChild.getSubjectNo());
						allInputAccounts.addAll(inAccounts);
					}
					BigDecimal allInputBalance = new BigDecimal("0");
					for (InsAccount inputAccount : allInputAccounts) {
						allInputBalance.add(inputAccount.getCurrBalance());
					}
					BigDecimal allBalance = allOutBalance.add(allInputBalance);
							
					if (beforeSubjectInfo!= null && beforeSubjectInfo.getYesterdayAmount()!=null && beforeSubjectInfo.getYesterdayAmount().equals(allBalance)) {
						subjectInfoService.insertSubjectInfo(subjectInfoNew);
					}
				}
			}
		}
				
		return true;
	}
	/**
	 * 		平衡检查
	 */
	@Override
	public boolean runCheckBalance() throws Exception {
		Date before_trans_time = DateUtil.getBeforeDate(new Date()); 
		List<SubjectInfo> subjectInfos = subjectInfoService.findSubjectInfoByDate(before_trans_time);
		BigDecimal debitTransAmount = new BigDecimal("0");
		BigDecimal creditTransAmount = new BigDecimal("0");
		for (SubjectInfo subjectInfo : subjectInfos) {
			if (subjectInfo.getSubjectLevel() == 0) {
				if (subjectInfo.getBalanceFrom().equals("debit")) {
					debitTransAmount.add(subjectInfo.getTodayDebitAmount());
				}
				if (subjectInfo.getBalanceFrom().equals("credit")) {
					creditTransAmount.add(subjectInfo.getTodayCreditAmount());
				}
			}
		}
		if (debitTransAmount.equals(creditTransAmount)) {
			return true;
		}
		return false;
	}
	/**
	 * 	备份交易流水之历史流水
	 */
	@Override
	public boolean runBakHistoryTrans() throws Exception {
		//查询系统状态信息
		SystemInfo sysInfo = systemInfoService.findSystemInfoByCurrentDate(DateUtil.getCurrentDate());
		
		//批量插入到内部账户历史余额表
		int i = insAccountHistoryBalanceService.insertInto(sysInfo.getParentTransDate());
				
		//批量插入到外部账户历史余额表
		int j = extAccountHistoryBalanceService.insertInto(sysInfo.getParentTransDate());
		
		return i > 0 && j > 0;
	}
	/**
	 * 追账
	 */
	@Override
	public boolean runAppend() throws Exception {
		SystemInfo systemInfo = new SystemInfo();
		Date d = new Date();
		systemInfo.setId(1);
		systemInfo.setStatus(SystemStatus.APPEND.toString());
		systemInfo.setCurrentDate(new Date());
		systemInfo.setNextTransDate(DateUtil.getAfterDate(d));
		systemInfo.setParentTransDate(DateUtil.getBeforeDate(d));
		boolean result = false;
		int i=0;
		try {
			i = systemInfoService.updateSystemInfo(systemInfo);
			
			List<ShadowAccount> shadowAccounts = shadowAccountService.findShadowAccountByAccountFlag("0");	//0-外部账号，1-内部账号'    ???这里获取到的全部是外部账户
			for (ShadowAccount shadowAccount : shadowAccounts) {
				if (shadowAccount.getAccountFlag() !=null && shadowAccount.getAccountFlag().equals("1")) {
					InsAccount inputAccount = inputAccountService.getInputAccountByAccountNo(shadowAccount.getAccountNo());
					BigDecimal currBalance = inputAccount.getCurrBalance();
					if (inputAccount.getBalanceFrom().equals("debit")) {
						currBalance = currBalance.add(shadowAccount.getDebitAmount());
					}
					else{
						currBalance = currBalance.add(shadowAccount.getCreditAmount());
					}
					inputAccount.setCurrBalance(currBalance);
					inputAccountService.updateInsAccount(inputAccount);
					
				}
				if (shadowAccount.getAccountFlag() != null && shadowAccount.getAccountFlag().equals("0")) {
					ExtAccount outAccount = extAccountService.getExtAccount(shadowAccount.getAccountNo());
					BigDecimal currBalance = outAccount.getCurrBalance();
					if (outAccount.getBalanceFrom().equals("debit")) {
						currBalance = currBalance.add(shadowAccount.getDebitAmount());
					}
					else{
						currBalance = currBalance.add(shadowAccount.getCreditAmount());
					}
					outAccount.setCurrBalance(currBalance);
					extAccountService.updateExtAccount(outAccount);
				}
				shadowAccount.setBookedFlag("1");
				shadowAccountService.updateShadowAccount(shadowAccount);
			}
			if (i > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}
	/**
	 * 	月初代理商补上月阶梯差
	 */
	@Override
	public boolean runBeginOfMonthAgentDiff() throws Exception {
		Date date = new Date();
		if (date.equals(DateUtil.getFirstDayOfMonth(date))) {
			
		}
		return false;
	}
	/**
	 * 	月初上游通道补上月阶梯差
	 */
	@Override
	public boolean runBeginOfMonthUpChannelDiff() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 	月处出款通道补上月阶梯差
	 */
	@Override
	public Map<String,Object> outBeginOfMonthUpChannelDiff() throws Exception {
		Map<String,Object> msg=new HashMap<>();
		msg.put("name","月初上游通道补上月阶梯差");
		Date date = new Date();
		try {
//			int y=d.getYear()+1900;
//			int y = DateUtil.getYear(date);
//			int m = date.getMonth();
			Calendar cal = Calendar.getInstance();
	        int y = cal.get(Calendar.YEAR);//获取年份
	        int m = cal.get(Calendar.MONTH);//获取上月份 
//			Calendar cl=Calendar.getInstance();//实例化一个日历对象
//			cl.set(Calendar.YEAR,d.getYear()+1900);//年设置为2016年
//			cl.set(Calendar.MONTH,d.getMonth()-1);//7月的id是6   
//			int days=cl.getActualMaximum(Calendar.DATE);//这个月有多少天
//			String d1=String.valueOf(y)+"-"+String.valueOf(m)+"-"+"01";
//			String d2=String.valueOf(y)+"-"+String.valueOf(m)+"-"+String.valueOf(days);
			int days = DateUtil.getDays(date);
			Date firstDayOfMonth = DateUtil.getFirstDayOfMonth(date);
			String d1 = DateUtil.getDefaultFormatDate(firstDayOfMonth);
			Date lastDayOfMonth = DateUtil.getLastDayOfMonth(date);
			String d2 = DateUtil.getDefaultFormatDate(lastDayOfMonth);
			
			List<OutAccountService> osr = outAccountServiceService.findAllOutAccountServiceByType();
			for (int i = 0; i < osr.size(); i++) {
//				for (int k = i+1; k < osr.size(); k++) {
//					if(osr.get(i).getId().equals(osr.get(k).getId())){
//						osr.remove(k);
//					}
//				}
				//服务费率
				TransImportInfo tii1 = transImportInfoService.findServiceFeeByMonth(d1, d2, osr.get(i).getId().toString(), days);
				//垫资费率
				TransImportInfo tii2 = transImportInfoService.findDianFeeByMonth(d1, d2, osr.get(i).getId().toString(), days);
				if(tii1!=null){
					OutChannelLadderRateRebalance oclrr1=new OutChannelLadderRateRebalance();
					oclrr1.setOutAcqEnname(osr.get(i).getAcqEnname());
					BigDecimal outMoney = transImportInfoService.countOutFeel(1,osr.get(i).getId(),days,tii1.getDays());//计算每月出款费用
					oclrr1.setOutAmountMonthFee(outMoney);
					oclrr1.setOutServiceId(osr.get(i).getId().toString());
					oclrr1.setRealRebalance(BigDecimal.ZERO);//实际还差金额
					oclrr1.setRebalance(BigDecimal.ZERO);//应还差金额
					oclrr1.setReMonth(m);
					oclrr1.setReType("代付服务费");
					oclrr1.setReYear(y);
					oclrr1.setTotalOutAmountMonth(tii1.getMoney());
					oclrr1.setTotalAvgDayOutAmountMonth(tii1.getDays());
					oclrr1.setRecordStatus(2);
					int num = outChannelLadderRateRebalanceService.insertOutChannelLadderRateRebalance(oclrr1);
					if(num<=0){
						msg.put("status",false);
						msg.put("msg","执行失败");
				    	return msg;
					}
				} 
				if(tii2!=null){
					OutChannelLadderRateRebalance oclrr2=new OutChannelLadderRateRebalance();
					oclrr2.setOutAcqEnname(osr.get(i).getAcqEnname());
					BigDecimal outMoney = transImportInfoService.countOutFeel(2,osr.get(i).getId(),days,tii2.getDays());//计算每月出款费用
					oclrr2.setOutAmountMonthFee(outMoney);
					oclrr2.setOutServiceId(osr.get(i).getId().toString());
					oclrr2.setRealRebalance(BigDecimal.ZERO);//实际还差金额
					oclrr2.setRebalance(BigDecimal.ZERO);//应还差金额
					oclrr2.setReMonth(m);
					oclrr2.setReType("垫资成本");
					oclrr2.setReYear(y);
					oclrr2.setTotalAvgDayOutAmountMonth(tii2.getDays());
					oclrr2.setTotalOutAmountMonth(tii2.getMoney());
					oclrr2.setRecordStatus(2);
					int num1 = outChannelLadderRateRebalanceService.insertOutChannelLadderRateRebalance(oclrr2);
					if(num1<=0){
						msg.put("status",false);
						msg.put("msg","执行失败");
				    	return msg;
					}
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		msg.put("status",true);
		msg.put("msg","执行成功");
    	return msg;
	}
	
	/**
	 * 	系统切回日间正常状态
	 */
	@Override
	public boolean runNormal() throws Exception {
		SystemInfo systemInfo = new SystemInfo();
		Date d = new Date();
		systemInfo.setId(1);
		systemInfo.setStatus(SystemStatus.NORMAL.toString());
		systemInfo.setCurrentDate(new Date());
		systemInfo.setNextTransDate(DateUtil.getAfterDate(d));
		systemInfo.setParentTransDate(DateUtil.getBeforeDate(d));
		boolean result = false;
		int i=0;
		try {
			i = systemInfoService.updateSystemInfo(systemInfo);
			if (i > 0) {
				result = true;
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		return result;
	}
	
	/**
	 * 	计算商户，上游，代理商结算中金额
	 */
	@Override
	public boolean runCountSettlingAmount() throws Exception {
		boolean result = false;
		//判断当前日期是否为工作日
		boolean isHoliday = sysCalendarService.isHoliday(DateUtil.getCurrentDate());
		if (!isHoliday) {
			//计算商户结算中金额：  其他应付款-结算款_应付商户T1账户（科目号22410101）结算中金额=账户余额-冻结金额；
			result = extAccountService.updateSettlingAmount();
			
		} else {
			//Nothing to do
		}
		
		return result;
	}
}
