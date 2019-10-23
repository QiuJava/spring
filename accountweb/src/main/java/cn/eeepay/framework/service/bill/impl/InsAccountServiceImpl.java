package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.InsAccountMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.enums.SystemStatus;
import cn.eeepay.framework.model.bill.BankAccount;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.InsideNobookedDetail;
import cn.eeepay.framework.model.bill.InsideTransInfo;
import cn.eeepay.framework.model.bill.ShadowAccount;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.BankAccountService;
import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.InsAccountService;
import cn.eeepay.framework.service.bill.InsideNobookedDetailService;
import cn.eeepay.framework.service.bill.InsideTransInfoService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.service.nposp.TransInfoService;
import cn.eeepay.framework.util.DateUtil;

@Service("insAccountService")
@Transactional
public class InsAccountServiceImpl implements InsAccountService {
	private static final Logger log = LoggerFactory.getLogger(InsAccountServiceImpl.class);
	@Resource
	public InsAccountMapper insAccountMapper;
	@Resource
	public CurrencyService currencyService;
	@Resource
	public SubjectService subjectService;
	@Resource
	public ShadowAccountService shadowAccountService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public InsideNobookedDetailService insideNobookedDetailService;
	@Resource
	public SystemInfoService systemInfoService;
	@Resource
	public TransInfoService transInfoService;
	@Resource
	public InsideTransInfoService insideTransInfoService;
	@Resource
	public CommonService commonService;
	@Resource
	public CoreTransInfoService coreTransInfoService;
	@Resource
	public BankAccountService bankAccountService;

	/**
	 * 内部账修改余额函数
	 * 
	 * @param ia
	 * @param reverse_flag
	 * @param trans_amount
	 * @param summary_info
	 * @return
	 */
	public boolean updateInAccountBalanceApi(InsAccount ia, String p_debit_credit_side, BigDecimal trans_amount,
			String summary_info) {
		boolean result = false;
		String account_no = "";
		try {
			account_no = ia.getAccountNo();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("异常 " + e.getMessage());
			log.info("操作的内部账户不存在！");
			return false;
		}

		String account_flag = "1";// 账号方向 0 外部账号，1 内部账号
		String day_bal_flag = ia.getDayBalFlag();// 日终修改余额标志:0-日间，1-日终

		try {
			// 1、账户属性是否为 日间更改余额
			if (day_bal_flag.equals("0")) {
				SystemInfo systemInfo = new SystemInfo();
				systemInfo.setCurrentDate(new Date());
				// 1Y1、读取系统状态
				systemInfo = systemInfoService.findSystemInfoById(1);
				String status = "n".equalsIgnoreCase(systemInfo.getStatus()) ? "NORMAL"
						: "c".equalsIgnoreCase(systemInfo.getStatus()) ? "CUTOFF"
								: "a".equalsIgnoreCase(systemInfo.getStatus()) ? "APPEND"
										: "s".equalsIgnoreCase(systemInfo.getStatus()) ? "SHUTDOWN" : "";
				// 1Y2、系统运行状态
				switch (SystemStatus.valueOf(status)) {
				case NORMAL:// 日间运行状态
					String balance_add_from = ia.getBalanceAddFrom();
					BigDecimal curr_balance = ia.getCurrBalance();
					// 账户增加借贷方向 与 发生额借贷方向 是否一致
					if (balance_add_from.equals(p_debit_credit_side)) {
						// 账户当前余额 += 交易金额
						curr_balance = curr_balance.add(trans_amount);
					} else {
						// 账户当前余额 -= 交易金额
						curr_balance = curr_balance.subtract(trans_amount);
					}
					// 修改账户信息表中的余额
					ia.setCurrBalance(curr_balance);
					this.updateInsAccount(ia);
					result = true;
					break;
				case CUTOFF:// 日切运行状态
					ShadowAccount sa = shadowAccountService.getShadowAccount(account_no, account_flag,
							DateUtil.getCurrentDate());
					// 影子账户未入账发生额
					if (sa != null && sa.getBookedFlag() != null && sa.getBookedFlag().equals("0")) {// 未入账
						BigDecimal sa_amount = new BigDecimal("0");
						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							sa_amount = sa.getDebitAmount();
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							sa_amount = sa.getCreditAmount();
						}
						// 影子账户未入账发生额 += 交易金额
						sa_amount = sa_amount.add(trans_amount);
						// 账户当前余额 += 影子账户未入账发生额
						ia.setCurrBalance(ia.getCurrBalance().add(sa_amount));

						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							sa.setDebitAmount(sa_amount);
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							sa.setCreditAmount(sa_amount);
						}
						// 更新影子账号信息表
						shadowAccountService.updateShadowAccount(sa);
					} else {// sa 为空或者没有未入账的
						sa = new ShadowAccount();
						sa.setAccountNo(ia.getAccountNo());
						sa.setAccountFlag(account_flag);
						// sa.setTransDate(systemInfo.getCurrentDate());
						sa.setTransDate(new Date());
						sa.setDebitAmount(new BigDecimal("0"));
						sa.setCreditAmount(new BigDecimal("0"));
						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							sa.setDebitAmount(trans_amount);
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							sa.setCreditAmount(trans_amount);
						}
						sa.setBookedFlag("0");
						// 账户当前余额 += 影子账户未入账发生额
						ia.setCurrBalance(ia.getCurrBalance().add(trans_amount));
						shadowAccountService.insertShadowAccount(sa);
					}
					result = true;
					break;
				case APPEND:// 追帐运行状态
					ShadowAccount a_sa = shadowAccountService.getShadowAccount(account_no, account_flag,
							DateUtil.getCurrentDate());
					// 影子账户未入账发生额
					if (a_sa != null && a_sa.getBookedFlag() != null && a_sa.getBookedFlag().equals("0")) {// 未入账
						BigDecimal a_sa_amount = new BigDecimal("0");
						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							a_sa_amount = a_sa.getDebitAmount();
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							a_sa_amount = a_sa.getCreditAmount();
						}
						// 影子账户未入账发生额 += 交易金额
						a_sa_amount = a_sa_amount.add(trans_amount);
						// 账户当前余额 += 影子账户未入账发生额
						ia.setCurrBalance(ia.getCurrBalance().add(a_sa_amount));

						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							a_sa.setDebitAmount(a_sa_amount);
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							a_sa.setCreditAmount(a_sa_amount);
						}
						// 修改（更新）账户信息表中的信息
						this.updateInsAccount(ia);
						// 影子账户‘未入账0’ 改为 ‘已入账1’,并更新影子账户表
						a_sa.setBookedFlag("1");
						shadowAccountService.updateShadowAccount(a_sa);

					} else {// sa 为空或者没有未入账的
						sa = new ShadowAccount();
						sa.setAccountNo(ia.getAccountNo());
						sa.setAccountFlag(account_flag);
						// sa.setTransDate(systemInfo.getCurrentDate());
						sa.setTransDate(new Date());
						sa.setDebitAmount(new BigDecimal("0"));
						sa.setCreditAmount(new BigDecimal("0"));
						if (p_debit_credit_side.toLowerCase().equals("debit")) {// 借方
							sa.setDebitAmount(trans_amount);
						}
						if (p_debit_credit_side.toLowerCase().equals("credit")) {// 贷方
							sa.setCreditAmount(trans_amount);
						}

						// 直接加上交易金额，进行入账
						ia.setCurrBalance(ia.getCurrBalance().add(trans_amount));
						// 修改状态为 “已入账”
						sa.setBookedFlag("1");
						shadowAccountService.insertShadowAccount(sa);
						this.updateInsAccount(ia);
					}
					result = true;
					break;
				case SHUTDOWN:// 系统关闭
					result = false;
					break;
				default:
					result = false;
					break;
				}
			} else {
				// 写内部账未入账流水表
				InsideNobookedDetail insideNobookedDetail = new InsideNobookedDetail();
				insideNobookedDetail.setAccountNo(account_no);
				insideNobookedDetail.setBookedFlag("0");
				insideNobookedDetail.setDebitCreditSide(ia.getBalanceAddFrom());
				insideNobookedDetail.setTransDate(new Date());
				insideNobookedDetail.setTransAmount(trans_amount);
				insideNobookedDetail.setSerialNo(String.valueOf(System.currentTimeMillis()));// 以后调整
				insideNobookedDetail.setChildSerialNo(String.valueOf(System.currentTimeMillis()));// 以后调整
				insideNobookedDetail.setSummaryInfo(summary_info);
				insideNobookedDetailService.insertInsideNobookedDetail(insideNobookedDetail);
				result = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public InsAccount exsitsInputAccount(String currencyNo, String subjectNo, String org_no) {
		return insAccountMapper.exsitsSubject(currencyNo, subjectNo, org_no);
	}

	@Override
	public int insertInputAccount(InsAccount inputAccount) throws Exception {
		return insAccountMapper.insertInsAccount(inputAccount);
	}

	@Override
	public Map<String, Object> createInsAccount(String orgNo,UserInfo userInfo) throws Exception {
		Map<String, Object> msg = new HashMap<>();
//		boolean saveFlag = false;
//		boolean returnFlag = false;
//		 currencyList = null;
//		 subjectList = null;
		int i = 0;
		List<Subject> subjectList = subjectService.findSubjectList();
		List<Currency> currencyList = currencyService.findCurrency();
		for (Currency currencyInfo : currencyList) {
			for (Subject subjectInfo : subjectList) {
				if (subjectInfo.getIsInnerAccount().equals("1")) {
					String currencyNo = currencyInfo.getCurrencyNo();
					String subjectNo = subjectInfo.getSubjectNo();
					Subject subject = subjectService.getSubject(subjectNo);
//					List<Subject> childrenSubjectList = subjectService.findChildrenSubjectListByParentSubjectNo(subjectNo);
					InsAccount inputAccoutInfo = exsitsInputAccount(currencyNo, subjectNo, orgNo);
					if (inputAccoutInfo == null) {
//						System.out.println(childrenSubjectList.size());
//						if (childrenSubjectList.size() > 0) {
//							continue;// 代表没有子级科目
//						}
						InsAccount insAccount = new InsAccount();
						StringBuffer sb = new StringBuffer(orgNo);
						String serialNum = genericTableService.createKey();
						log.info(serialNum);
						sb.append(subjectNo);
						sb.append(serialNum);
						insAccount.setCurrencyNo(currencyNo);
						insAccount.setSubjectNo(subjectNo);
						insAccount.setOrgNo(orgNo);
						insAccount.setAccountNo(sb.toString());
						if (subject != null) {
							insAccount.setAccountName(subject.getSubjectName());
							insAccount.setBalanceAddFrom(subject.getAddBalanceFrom());
							insAccount.setBalanceFrom(subject.getBalanceFrom());
							insAccount.setDayBalFlag(subject.getInnerDayBalFlag());
							insAccount.setSumFlag(subject.getInnerSumFlag());
						}
						SystemInfo systemInfo = systemInfoService.findSystemInfoById(1);
						if (systemInfo != null) {
							insAccount.setParentTransDay(systemInfo.getParentTransDate());
						}
						insAccount.setAccountStatus("1");
						insAccount.setCurrBalance(BigDecimal.ZERO);
						insAccount.setControlAmount(BigDecimal.ZERO);
						insAccount.setParentTransBalance(BigDecimal.ZERO);
						insAccount.setCreateTime(new Date());
						insAccount.setCreator(userInfo.getUsername());
						int result = insertInputAccount(insAccount);
						if (result > 0) {
							i++;
						}
					}
				}
			}
		}
		if (i > 0) {
			msg.put("status",true);
			msg.put("msg","开通内部账号成功,共开通" + i + "个");
		}
		else{
			msg.put("status",false);
			msg.put("msg","没有需要开通的内部账号");
		}
		return msg;
	}

	@Override
	public List<InsAccount> findInsAccountListInfo(InsAccount inputAccount, Sort sort, Page<InsAccount> page)
			throws Exception {
		return insAccountMapper.findInsAccountListInfo(inputAccount, sort, page);
	}

	@Override
	public List<InsideTransInfo> findInsideTransList(InsideTransInfo insideTransInfo, Map<String, String> params,
			Sort sort, Page<InsideTransInfo> page) throws Exception {
		return insAccountMapper.findInsideTransList(insideTransInfo, params, sort, page);
	}

	@Override
	public int updateInsAccount(InsAccount inputAccount) throws Exception {
		return insAccountMapper.updateInsAccount(inputAccount);
	}

	@Override
	public boolean recordInAccountApi(Map<String, String> params) throws Exception {
		boolean result = false;
		String p_select_type = params.get("p_select_type");
		String p_account_no = params.get("p_account_no");
		String p_org_no = params.get("p_org_no");
		String p_subject_no = params.get("p_subject_no");
		String p_currency_no = params.get("p_currency_no");
		BigDecimal p_trans_amount = new BigDecimal(params.get("p_trans_amount"));
		String p_debit_credit_side = params.get("p_debit_credit_side");// 交易借贷方向
		String p_reverse_flag = params.get("p_reverse_flag");// 冲销标志
		String p_summary_info = params.get("p_summary_info");
		// String p_trans_order_no = params.get("P_trans_order_no") ; //交易订单号
		String accountFlag = "0";

		InsAccount ia = null;
		if (p_select_type.equals("1")) {// 账号查找方式
			ia = this.getInputAccountByAccountNo(p_account_no);
		} else if (p_select_type.equals("2")) {// 多添加查找方式
			ia = this.getInputAccountByParams(p_org_no, p_subject_no, p_currency_no);
		}
		if (ia != null && ia.getAccountStatus() != null && ia.getAccountStatus().equals("1")) {
			// 判断冲销标志 ???
			if (p_reverse_flag.equals(ReverseFlag.REVERSE)) {
				p_trans_amount = p_trans_amount.multiply(new BigDecimal("-1"));
			}
			result = updateInAccountBalanceApi(ia, p_debit_credit_side, p_trans_amount, p_summary_info);
			// 修改账户余额失败，直接返回false
			if (!result) {
				return result;
			}
			String day_bal_flag = ia.getDayBalFlag();
			String sum_flag = ia.getSumFlag();

			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String record_date = formatter1.format(new Date());
			Date recordDate = formatter1.parse(record_date);
			Time recordTime = new Time(recordDate.getTime());

			if (day_bal_flag.equals("0") && sum_flag.equals("0")) {// 为日间 &&
																	// 日间单笔
				InsideTransInfo insideTransInfo = new InsideTransInfo();
				insideTransInfo.setAccountNo(p_account_no);
				insideTransInfo.setRecordAmount(p_trans_amount);
				insideTransInfo.setBalance(ia.getCurrBalance());
				insideTransInfo.setAvaliBalance(ia.getControlAmount());
				insideTransInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
				insideTransInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));

				insideTransInfo.setRecordDate(recordDate);
				insideTransInfo.setRecordTime(recordTime);
				insideTransInfo.setDebitCreditSide(p_debit_credit_side);
				insideTransInfo.setSummaryInfo(p_summary_info);
				insideTransInfoService.insertInsideTransInfo(insideTransInfo);
			}

			CoreTransInfo transInfo = new CoreTransInfo();
			transInfo.setAccountNo(p_account_no);
			transInfo.setAccountFlag(accountFlag);
			transInfo.setTransAmount(p_trans_amount);
			transInfo.setCurrencyNo(ia.getCurrencyNo());
			transInfo.setSubjectNo(ia.getSubjectNo());
			// transInfo.setReverseFlag(ReverseFlag.REVERSED.toString());
			transInfo.setReverseFlag(p_reverse_flag); // 冲销标志 ？？
			transInfo.setDebitCreditSide(ia.getBalanceAddFrom());
			transInfo.setSummaryInfo(p_summary_info);
			transInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
			transInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));// 以后调整
			transInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));// 以后调整
			transInfo.setTransDate(recordDate);
			transInfo.setTransTime(recordTime);
			transInfo.setDebitCreditFlag("1"); // 借贷平衡 值该怎么设 ???
			coreTransInfoService.insertTransInfo(transInfo);
		} else {
			return false;
		}
		return true;
	}

	@Override
	public InsAccount getInputAccountByAccountNo(String accountNo) {
		return insAccountMapper.getInsAccountByAccountNo(accountNo);
	}

	@Override
	public InsAccount getInputAccountByParams(String orgNo, String subjectNo, String currencyNo) {
		return insAccountMapper.getInsAccountByParams(orgNo, subjectNo, currencyNo);
	}

	@Override
	public List<InsAccount> findInputAccountByDayBalFlag(String dayBalFlag) {
		return insAccountMapper.findInsAccountByDayBalFlag(dayBalFlag);
	}

	@Override
	public List<InsAccount> findInputAccountBySubjectNo(String subjectNo) {
		return insAccountMapper.findInsAccountBySubjectNo(subjectNo);
	}

	@Override
	public boolean createInputAccount(Map<String, String> params) throws Exception {
		String p_account_type = params.get("p_account_type");
		String p_user_id = params.get("p_user_id");
		String p_account_owner = params.get("p_account_owner");
		String p_card_no = params.get("p_card_no");
		String p_subject_no = params.get("p_subject_no");
		String p_currency_no = params.get("p_currency_no");
		String p_org_no = params.get("p_org_no");
		BankAccount bankAccount = new BankAccount(params.get("bankName"), params.get("accountName"),
				params.get("accountNo"), p_org_no, p_currency_no, params.get("accountType"), p_subject_no,
				params.get("cnapsNo"));

		// accountNo = 6位机构号+14位科目号+12位顺序号
		String accountNo = commonService.getInsAccountNo(p_org_no, p_subject_no);
		bankAccount.setInsAccountNo(accountNo); // 内部账号
		InsAccount inputAccount = new InsAccount();
		Date now = new Date();
		Time time = new Time(now.getTime());
		inputAccount.setCurrBalance(new BigDecimal(0));
		inputAccount.setControlAmount(new BigDecimal(0));
		inputAccount.setParentTransBalance(new BigDecimal(0));
		inputAccount.setAccountStatus("1");
		Subject subject = subjectService.getSubject(bankAccount.getSubjectNo());
		inputAccount.setBalanceAddFrom(subject.getAddBalanceFrom());
		inputAccount.setBalanceFrom(subject.getBalanceFrom());
		inputAccount.setAccountNo(accountNo);
		inputAccount.setAccountName(subject.getSubjectName()); // 账号名称 = 科目名称
		inputAccount.setCreateTime(now);
		inputAccount.setSubjectNo(p_subject_no);
		SystemInfo systemInfo = systemInfoService.findSystemInfoById(1);
		if (systemInfo != null) {
			inputAccount.setParentTransDay(systemInfo.getParentTransDate());
		}
		CoreTransInfo coreTransInfo = new CoreTransInfo();
		coreTransInfo.setAccountNo(accountNo);
		coreTransInfo.setAccountFlag("1");
		coreTransInfo.setTransAmount(new BigDecimal(0));
		coreTransInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
		coreTransInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));// 以后调整
		coreTransInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));// 以后调整
		coreTransInfo.setSubjectNo(p_subject_no);
		coreTransInfo.setCurrencyNo(p_currency_no);
		coreTransInfo.setTransDate(now);
		coreTransInfo.setReverseFlag(ReverseFlag.NORMAL.toString());
		coreTransInfo.setDebitCreditFlag("1");
		coreTransInfo.setDebitCreditSide(subject.getBalanceFrom());
		coreTransInfo.setTransTime(time);
		int i = 0;
		i = this.insertInputAccount(inputAccount);
		coreTransInfoService.insertTransInfo(coreTransInfo);
		// 判断银行账户是否已经存在
		BankAccount bankAccountQuery = bankAccountService.existBankAccount(bankAccount.getAccountNo());
		if (bankAccountQuery != null) {
			throw new RuntimeException("bankAccountExisted");
		}
		// 插入银行账户
		bankAccountService.insertBankAccount(bankAccount);

		if (i > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<InsideTransInfo> findExportInsideTransList(InsideTransInfo insideTransInfo,
			Map<String, String> params) {
		return insAccountMapper.findExportInsideTransList(insideTransInfo, params);
	}

	@Override
	public List<InsAccount> findExportInsAccountList(InsAccount insAccount) {
		return insAccountMapper.findExportInsAccountList(insAccount);
	}

	@Override
	public List<InsAccount> findAll() {
		return insAccountMapper.findAll();
	}
}
