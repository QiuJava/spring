package framework.test;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.enums.ReverseStatus;
import cn.eeepay.framework.enums.SystemStatus;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtNobookedDetail;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideNobookedDetail;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.OutAccountTask;
import cn.eeepay.framework.model.bill.OutAccountTaskDetail;
import cn.eeepay.framework.model.bill.ShadowAccount;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SubjectInfo;
import cn.eeepay.framework.model.bill.SystemInfo;
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
import cn.eeepay.framework.service.bill.OutBillService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectInfoService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.bill.TransImportInfoService;
import cn.eeepay.framework.service.nposp.AcqOrgService;
import cn.eeepay.framework.service.nposp.OutAccountServiceService;
import cn.eeepay.framework.service.nposp.SysCalendarService;
import cn.eeepay.framework.util.DateUtil;
public class OutAccountTaskTest extends BaseTest {
	
	private static final Logger log = LoggerFactory.getLogger(OutAccountTaskTest.class);
	@Resource
	public SystemInfoService systemInfoService;
	@Resource
	public InsAccountService inputAccountService;
	@Resource
	public InsideNobookedDetailService insideNobookedDetailService;
	@Resource
	public InsideTransInfoService insideTransInfoService;
	@Resource
	private InsAccountHistoryBalanceService insAccountHistoryBalanceService;
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
	private OutBillService outBillService;
	//日切
		public void runCutOff() throws Exception {
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
		}
		//	日终修改余额账户类的余额处理-内部账
		@Test
		public void runAccoutUpdateAmount() throws Exception {
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
				
			}
	    	
	    	
		}
		
		//	日终修改余额账户类的余额处理-外部账
		@Test
		public void runExtAccoutUpdateAmount() throws Exception {
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
				
			}
	    	
	    	
		}
		/**
		 * 	入总账
		 */
		@Test
		public void runCheckInAllAccount() throws Exception {
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
					
			
		}
		/**
		 * 		平衡检查
		 */
		@Test
		public void runCheckBalance() throws Exception {
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
				
			}
			
		}
		/**
		 * 	备份交易流水之历史流水
		 */
		@Test
		public void runBakHistoryTrans() throws Exception {
			//查询系统状态信息
			SystemInfo sysInfo = systemInfoService.findSystemInfoByCurrentDate(DateUtil.getCurrentDate());
			//批量插入到内部账户历史余额表
			int i = insAccountHistoryBalanceService.insertInto(sysInfo.getParentTransDate());
			
			//批量插入到外部账户历史余额表
			int j = extAccountHistoryBalanceService.insertInto(sysInfo.getParentTransDate());
			
		}
		/**
		 * 追账
		 */
		@Test
		public void runAppend() throws Exception {
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
			
		}
		/**
		 * 	月初代理商补上月阶梯差
		 */
		@Test
		public void runBeginOfMonthAgentDiff() throws Exception {
			Date date = new Date();
			if (date.equals(DateUtil.getFirstDayOfMonth(date))) {
				
			}
			
		}
		/**
		 * 	月初上游通道补上月阶梯差
		 */
		@Test
		public void runBeginOfMonthUpChannelDiff() throws Exception {
			// TODO Auto-generated method stub
			
		}
		/**
		 * 	系统切回日间正常状态
		 */
		@Test
		public void runNormal() throws Exception {
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
			
		}
		
		/**
		 * 	计算商户，上游，代理商结算中金额
		 */
		@Test
		public void runCountSettlingAmount() throws Exception {
			boolean result = false;
			//判断当前日期是否为工作日
			boolean isHoliday = sysCalendarService.isHoliday(DateUtil.getCurrentDate());
			if (!isHoliday) {
				//计算商户结算中金额：  其他应付款-结算款_应付商户T1账户（科目号22410101）结算中金额=账户余额-冻结金额；
				result = extAccountService.updateSettlingAmount();
				
			} else {
				//Nothing to do
			}
			
			
		}

	@Test
	public void test1() throws Exception {
		//查询系统状态信息
		SystemInfo sysInfo = systemInfoService.findSystemInfoByCurrentDate(DateUtil.getCurrentDate());
		
		//生成出账任务之前，先将昨天的出账任务全部标记成关闭状态
		outAccountTaskService.updateToClosedByTransTime(DateUtil.getBeforeDate(sysInfo.getParentTransDate()));
		outBillService.updateToClosedByTransTime(DateUtil.getBeforeDate(sysInfo.getParentTransDate()));
		//1. 查询所有的机构组织
		List<OrgInfo> orgList = orgInfoService.findOrgInfo();
		//3. 查询所有收单机构
		List<AcqOrg> acqList = acqOrgService.findAllAcqOrg();
		
		String accountType = "Acq";
		String userId = "";  //收单机构id
		String acqEName = ""; //收单机构名称
		String accountOwner = "";  //机构组织id
		String cardNo = "";   //卡号
		String subjectNo = "122103";  //科目号122103
		String currencyNo = "RMB";
		ExtAccountInfo extAccountInfo = null;
		ExtAccount extAccount = null;
		BigDecimal money = new BigDecimal(0);
		BigDecimal transMoney = new BigDecimal(0);
		
		//统计金额信息
		BigDecimal upBalance = new BigDecimal(0);
		BigDecimal upTodayBalance = new BigDecimal(0);
		BigDecimal outAccountTaskAmount = new BigDecimal(0);
		BigDecimal transAmount = new BigDecimal(0);
		
		OutAccountTaskDetail taskDetail = null;
		
		int i=0,j=0;
		//币种号
		List<Currency> currencyList = currencyService.findCurrency();
		if (currencyList != null && currencyList.size() > 0) {
			currencyNo = currencyList.get(0).getCurrencyNo();
		}
		//2. 循环所有机构组织
		for (OrgInfo org : orgList) {
			//4. 循环所有收单机构
			for (AcqOrg acq : acqList) {
				taskDetail = new OutAccountTaskDetail();
				taskDetail.setCreateTime(sysInfo.getCurrentDate());
				taskDetail.setSysTime(sysInfo.getCurrentDate());
				
				userId = acq.getId().toString();
				acqEName = acq.getAcqEnname();
				accountOwner = org.getOrgNo();
				//5. 根据唯一索引：外部用户类型、外部用户ID，账户归属，卡号，内部科目编号，币种号   查询用户账户关系表
				extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, userId, accountOwner, cardNo, subjectNo, currencyNo);
				if (extAccountInfo == null) {
					continue;
				}
				//6. 根据上一步得到的外部用户账户账号查询外部用户账户表, 得到当前余额
				extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
				taskDetail.setAcqOrgNo(acqEName);
				if (extAccount == null || extAccount.getSettlingAmount() == null) {
					taskDetail.setUpBalance(new BigDecimal(0));
					taskDetail.setOutAccountAmount(new BigDecimal(0));
				} else {
					taskDetail.setUpBalance(extAccount.getSettlingAmount());
					taskDetail.setOutAccountAmount(extAccount.getSettlingAmount());
				}
				
				//7. 根据账号和上一个交易日期查询外部用户账户交易明细表， 计算：借方金额-贷方金额 = 交易金额
				money = extTransInfoService.countTransMoney(extAccount.getAccountNo(), sysInfo.getParentTransDate());
				taskDetail.setTodayBalance(money == null ? new BigDecimal(0) : money);
				
				//8. 根据收单机构id，冲销交易标志NORMAL，冲销状态正常NORMAL，上一个记账日期查询记账交易流水表
				//TODO:上一个记账日期需要到系统状态信息表中查询
				transMoney = transImportInfoService.countByParam(acqEName, ReverseFlag.NORMAL.toString(), ReverseStatus.NORMAL.toString(), sysInfo.getParentTransDate());
				taskDetail.setTodayAmount(transMoney == null ? new BigDecimal(0) : transMoney);
				
				upTodayBalance = upTodayBalance.add(money == null ? new BigDecimal(0) : money);  //上游当日余额
				transAmount = transAmount.add(transMoney == null ? new BigDecimal(0) : transMoney);  //累加交易金额
				if (extAccount == null || extAccount.getCurrBalance() == null) {
					outAccountTaskAmount = outAccountTaskAmount.add(new BigDecimal(0));
					upBalance = upBalance.add(new BigDecimal(0));  //历史余额
				} else {
					outAccountTaskAmount = outAccountTaskAmount.add(extAccount.getSettlingAmount());
					upBalance = upBalance.add(extAccount.getSettlingAmount());  //历史余额
				}
				
				//9. 创建出账任务
				if (taskDetail != null) {
					OutAccountTask task = new OutAccountTask();
					task.setCreateTime(sysInfo.getCurrentDate());
					task.setTransTime(sysInfo.getParentTransDate());
					task.setTransAmount(transAmount);
					task.setUpBalance(upBalance);
					task.setUpTodayBalance(upTodayBalance);
					task.setOutAccountTaskAmount(outAccountTaskAmount);
					task.setUpCompanyCount(1);
					task.setAcqEnname(acqEName);
					task.setSysTime(sysInfo.getCurrentDate());
					//i = outAccountTaskService.insert(task);
					i = outAccountTaskService.insertOrUpdate(task);
					taskDetail.setOutAccountTaskId(task.getId());
					//需要查询之前有没有该出账任务的明细，有则删除
					outAccountTaskDetailService.deleteByTaskId(task.getId());
				}
				//10. 创建出账任务详细
				j = outAccountTaskDetailService.insert(taskDetail);
				
				transAmount = BigDecimal.ZERO;
				upBalance = BigDecimal.ZERO;
				upTodayBalance = BigDecimal.ZERO;
				outAccountTaskAmount = BigDecimal.ZERO;
			}
		}
	}
	
	@Test
	public void test2() throws Exception {
		//内部账账户表查询-查询所有数据
		List<InsAccount> insAccountList = inputAccountService.findAll();
		//批量插入到内部账户历史余额表
		int i = insAccountHistoryBalanceService.insertBatch(insAccountList);
		
		//外部账账户表查询-查询所有数据
		List<ExtAccount> extAccountList = extAccountService.findAll();
		//批量插入到外部账户历史余额表
		int j = extAccountHistoryBalanceService.insertBatch(extAccountList);
	}

}
