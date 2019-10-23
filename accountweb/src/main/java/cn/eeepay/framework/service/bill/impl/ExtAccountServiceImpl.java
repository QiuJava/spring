package cn.eeepay.framework.service.bill.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.AccountStatus;
import cn.eeepay.framework.enums.DebitCreditSide;
import cn.eeepay.framework.enums.ReverseFlag;
import cn.eeepay.framework.enums.ReverseStatus;
import cn.eeepay.framework.enums.SystemStatus;
import cn.eeepay.framework.model.bill.CoreTransInfo;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtAccountInfo;
import cn.eeepay.framework.model.bill.ExtAccountOpRecord;
import cn.eeepay.framework.model.bill.ExtNobookedDetail;
import cn.eeepay.framework.model.bill.ExtTransInfo;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;
import cn.eeepay.framework.model.bill.MsgEntity;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.ShadowAccount;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.SystemInfo;
import cn.eeepay.framework.service.bill.CommonService;
import cn.eeepay.framework.service.bill.CoreTransInfoService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.ExtAccountOpRecordService;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.ExtNobookedDetailService;
import cn.eeepay.framework.service.bill.ExtUserTypeSubjectService;
import cn.eeepay.framework.service.bill.GenericTableService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.ShadowAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.bill.SystemInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.HttpConnectUtil;
@Service("extAccountService")
@Transactional
public class ExtAccountServiceImpl  implements ExtAccountService{
	private static final Logger log = LoggerFactory.getLogger(ExtAccountServiceImpl.class);
	@Resource
	public ExtAccountMapper extAccountMapper;
	@Resource
	public ShadowAccountService shadowAccountService;
	@Resource
	public SystemInfoService systemInfoService;
	
	@Resource
	public CurrencyService  currencyService;
	@Resource
	public SubjectService  subjectService;
	@Resource
	public CoreTransInfoService  coreTransInfoService;
	@Resource
	public OrgInfoService orgInfoService;
	@Resource
	public  GenericTableService  genericTableService;
	@Resource
	public CommonService commonService;
	@Resource
	public ExtAccountOpRecordService extAccountOpRecordService;
	@Resource
	public ExtNobookedDetailService extNobookedDetailService ;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public ExtUserTypeSubjectService extUserTypeSubjectService;
	
	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;
	
	@Override
	public boolean createOutAccount(Map<String, String> params)  throws Exception {
		
		String p_account_type = params.get("p_account_type");
//		String p_user_id = params.get("p_user_id");
		String p_account_owner = params.get("p_account_owner");
		String p_card_no = params.get("p_card_no");
		String p_subject_no = params.get("p_subject_no");
		String p_currency_no = params.get("p_currency_no");
		String p_org_no = params.get("p_org_no");
		
		//accountNo = 6位机构号+14位科目号+12位顺序号
		String accountNo = commonService.getExtAccountNo(p_org_no,p_subject_no);
		ExtAccount extAccount = new ExtAccount();
		Date now = new Date();
		
		extAccount.setCurrBalance(new BigDecimal(0));
		extAccount.setControlAmount(new BigDecimal(0));
		extAccount.setParentTransBalance(new BigDecimal(0));
		extAccount.setAccountStatus("1");
		Subject subject = subjectService.getSubject(extAccount.getSubjectNo());
		extAccount.setBalanceAddFrom(subject.getAddBalanceFrom());
		extAccount.setBalanceFrom(subject.getBalanceFrom());
		extAccount.setAccountNo(accountNo);
		extAccount.setAccountName(subject.getSubjectName()); //账号名称  = 科目名称
		extAccount.setCreateTime(now);
		
		ExtAccountInfo extAccountInfo = new ExtAccountInfo();
		extAccountInfo.setAccountNo(extAccount.getAccountNo());
		extAccountInfo.setAccountOwner(extAccount.getOrgNo());

		extAccountInfo.setCardNo(p_card_no);
		extAccountInfo.setCurrencyNo(extAccount.getCurrencyNo());
		extAccountInfo.setSubjectNo(extAccount.getSubjectNo());
//		extAccountInfo.setUserId(p_user_id);
	
		int i = this.insertExtAccount(extAccount,extAccountInfo);
		if (i>0) {
			return true;
		}
		return false;
	}
	
	@Override
	public List<ExtAccount> findExtAccountByDayBalFlag(String dayBalFlag) {
		return extAccountMapper.findExtAccountByDayBalFlag(dayBalFlag);
	}
	
	@Override
	public boolean recordExtAccountApi(Map<String, String> params) throws Exception {
		
		String p_select_type = params.get("p_select_type");
		String p_account_no = params.get("p_account_no");
		String p_user_id = params.get("p_user_id");
		String p_account_owner = params.get("p_account_owner");
		String p_card_no = params.get("p_card_no");
		String p_subject_no = params.get("p_subject_no");
		String p_currency_no = params.get("p_currency_no");
		BigDecimal p_trans_amount = new BigDecimal(params.get("p_trans_amount"));
		String p_debit_credit_side = params.get("p_debit_credit_side");//交易借贷方向
		String p_reverse_flag = params.get("p_reverse_flag");//冲销标志
		String p_summary_info = params.get("p_summary_info");
		String p_account_type = params.get("p_account_type");
		String p_trans_order_no = params.get("p_trans_order_no") ;	//交易订单号
		String accountFlag = "0";
		
		if (p_select_type.equals("1")) {//账号查找方式
			
		}else if (p_select_type.equals("2")) {//多添加查找方式
			try{
				ExtAccountInfo eat = this.findExtAccountInfoByParams(p_account_type,p_user_id,p_account_owner,p_card_no,p_subject_no,p_currency_no);
				p_account_no = eat.getAccountNo();
			}catch(NullPointerException e){
				log.info("查找外部用户账户关系表，没找到相应的账号");
				log.error("异常:",e);
				return false ;
			}
		}
		
		ExtAccount oa = null ;
		try{
			oa = this.findExtAccountByAccountNo(p_account_no);
			oa.getAccountNo() ;
		}catch(NullPointerException e){
			log.error("异常:",e);
			log.info("操作的外部账户不存在！");
			return false ;
		}
		if (oa != null && oa.getAccountStatus() != null && oa.getAccountStatus().equals("1")) {
			//判断冲销标志是否为     冲销
			if (ReverseFlag.REVERSE.toString().equals(p_reverse_flag)) {
				p_trans_amount = p_trans_amount.multiply(new BigDecimal("-1"));
			}
			updateOutAccountBalanceApi(oa, p_debit_credit_side, p_trans_amount, p_summary_info);
			ExtTransInfo extTransInfo = new ExtTransInfo();
			extTransInfo.setAccountNo(p_account_no);
			extTransInfo.setRecordAmount(p_trans_amount);
			extTransInfo.setBalance(oa.getCurrBalance());
			//可用余额  = 账户余额 - 冻结金额 - 结算中金额
			extTransInfo.setAvaliBalance((oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()));
			extTransInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
			extTransInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String record_date = formatter1.format(new Date());
			Date recordDate = formatter1.parse(record_date);
			Time recordTime = new Time(recordDate.getTime());
			extTransInfo.setRecordDate(recordDate);
			extTransInfo.setRecordTime(recordTime);
			extTransInfo.setDebitCreditSide(p_debit_credit_side);
			extTransInfo.setSummaryInfo(p_summary_info);
			extTransInfo.setTransOrderNo(p_trans_order_no);//交易订单号
			//往交易明细表插入记录
			this.insertExtTransInfo(extTransInfo);
			CoreTransInfo transInfo = new CoreTransInfo();
			transInfo.setAccountNo(p_account_no);
			transInfo.setAccountFlag(accountFlag);
			transInfo.setTransAmount(p_trans_amount);
			transInfo.setCurrencyNo(oa.getCurrencyNo());
			transInfo.setSubjectNo(oa.getSubjectNo());
			//transInfo.setReverseFlag(ReverseFlag.REVERSED.toString());		//冲销标志    ??
			transInfo.setReverseFlag(p_reverse_flag);		//冲销标志    ??
			transInfo.setDebitCreditFlag("1"); 			//借贷平衡检查标志:0-不平衡，1-平衡         ??
			transInfo.setDebitCreditSide(p_debit_credit_side);
			transInfo.setSummaryInfo(p_summary_info);
			transInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
			transInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));//以后调整   ??
			transInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));//以后调整   ??
			transInfo.setTransDate(recordDate);
			transInfo.setTransTime(recordTime);
			//记账流水表插入记录
			coreTransInfoService.insertTransInfo(transInfo);
		}
		else{
			return false;
		}
		return true;
	}
	@Override
	public int insertExtAccount(ExtAccount extAccount,ExtAccountInfo extAccountInfo) throws Exception {
		extAccountMapper.insertExtAccount(extAccount);
		extAccountMapper.insertExtAccountInfo(extAccountInfo);
		return 1;
	}
	@Override
	public ExtAccountInfo exsitsExtAccountInfo(ExtAccountInfo extAccountInfo) throws Exception {
		return extAccountMapper.exsitsExtAccountInfo(extAccountInfo) ;
	}

	@Override
	public List<ExtAccount> findAllAccountInfo(ExtAccount extAccount, Sort sort, Page<ExtAccount> page,String userNoStrs) throws Exception {
		return extAccountMapper.findAllAccountInfo(extAccount,sort,page,userNoStrs); 
	}

	@Override
	public List<Map<String,Object>> findAllExtTransInfo(ExtTransInfo extTransInfo,Map<String, String> params, Sort sort, Page<Map<String,Object>> page)
			throws Exception {
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
		List<Map<String,Object>> extTransInfoList = extAccountMapper.getAllExtTransInfo(extTransInfo, params, sort, page) ;
		for(Map<String, Object> etInfo:extTransInfoList){
			String debitCreditSideText = "" ;
			String debitCreditSide = etInfo.get("debitCreditSide").toString() ;
			for(SysDict balanceFrom:balanceFromList){
				if(balanceFrom.getSysValue().equals(etInfo.get("debitCreditSide"))){
					debitCreditSideText = balanceFrom.getSysName() ;
				}
			}
			if (debitCreditSide.equals(DebitCreditSide.FREEZE.toString())) {
				debitCreditSideText += "(-)";
			}
			else if (debitCreditSide.equals(DebitCreditSide.UNFREEZE.toString())) {
				debitCreditSideText += "(+)";
			}
			else if(debitCreditSide.equals(etInfo.get("balanceAddFrom").toString())){
				debitCreditSideText += "(+)";
			}else{
				debitCreditSideText += "(-)";
			}
			
			etInfo.put("debitCreditSide", debitCreditSideText);
		}
		return extTransInfoList;
		//return extAccountDao.getAllExtTransInfo(extTransInfo,params, sort, page);
	}
	
	
	@Override
	public List<ExtTransInfo> exportAllExtTransInfo(ExtTransInfo extTransInfo, Map<String, String> params) throws Exception {
		List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
		List<ExtTransInfo> extTransInfoList = extAccountMapper.exportAllExtTransInfo(extTransInfo, params) ;
		for(ExtTransInfo etInfo:extTransInfoList){
			String debitCreditSideText = "" ;
			String debitCreditSide = etInfo.getDebitCreditSide().toString() ;
			for(SysDict balanceFrom:balanceFromList){
				if(balanceFrom.getSysValue().equals(etInfo.getDebitCreditSide().toString())){
					debitCreditSideText = balanceFrom.getSysName() ;
				}
			}
			if (debitCreditSide.equals(DebitCreditSide.FREEZE.toString())) {
				debitCreditSideText += "(-)";
			}
			else if (debitCreditSide.equals(DebitCreditSide.UNFREEZE.toString())) {
				debitCreditSideText += "(+)";
			}
			else if(debitCreditSide.equals(etInfo.getBalanceAddFrom().toString())){
				debitCreditSideText += "(+)";
			}else{
				debitCreditSideText += "(-)";
			}
			
			etInfo.setDebitCreditSide(debitCreditSideText);
		}
		return extTransInfoList;
	}
	
	
	

	@Override
	public List<ExtAccount> findAllAccountStatusUpdateInfo(ExtAccount extAccount, Sort sort, Page<ExtAccount> page)
			throws Exception {
		return extAccountMapper.findAllAccountStatusUpdateInfo(extAccount,sort,page);
	}

	@Override
	public int updateExtAccountStatus(ExtAccount extAccount) throws Exception {
		return extAccountMapper.updateExtAccountStatus(extAccount);
	}

	@Override
	public int updateExtAccountSettlingHoldAmount(ExtAccount extAccount) {
		return extAccountMapper.updateExtAccountSettlingHoldAmount(extAccount);
	}
	@Override
	public ExtAccount getExtAccount(String accountNo) throws Exception {
		return extAccountMapper.getExtAccount(accountNo);
	}

	@Override
	public int updateExtAccount(ExtAccount extAccount) throws Exception {
		return extAccountMapper.updateExtAccount(extAccount);
	}
	@Override
	public ExtAccountInfo findExtAccountInfoByParams(String accountType, String userId, String accountOwner,
			String cardNo, String subjectNo, String currencyNo) throws Exception {
		return extAccountMapper.findExtAccountInfoByParams(accountType, userId, accountOwner, cardNo, subjectNo, currencyNo);
	}
	@Override
	public ExtAccountInfo getExtAccountInfoByParams(ExtAccountInfo extAccountInfo) throws Exception {
		return extAccountMapper.getExtAccountInfoByParams2(extAccountInfo);
	}
	/**
	 * 外部账修改余额函数
	 * @param oa
	 * @param reverse_flag		交易借贷方向
	 * @param trans_amount		交易金额
	 * @param summary_info		摘要
	 * @return
	 */
	public MsgEntity updateOutAccountBalanceApi(ExtAccount oa,String debit_credit_side,BigDecimal trans_amount,String summary_info) throws Exception {

		MsgEntity msg = new MsgEntity() ;
		
		String account_no = "";
		try{
			account_no = oa.getAccountNo();
		}catch(Exception e){
			log.error("异常:",e);
			log.info("操作的外部账户不存在！");
			msg.setStatus("false");
			msg.setMsg("操作的外部账户不存在！");
			return msg ;
		}
		String account_flag = "0";//账号方向 0 外部账号，1 内部账号
		SystemInfo systemInfo =  new SystemInfo();
		systemInfo.setCurrentDate(new Date());

		//记录账户可用余额 = 账户余额 - 账户控制中金额 - 账户结算中金额
		BigDecimal avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record_date = formatter1.format(new Date());
		Date recordDate = formatter1.parse(record_date);
		Time recordTime = new Time(recordDate.getTime());
		
		try {
			//1、账号属性为日间修改余额？
			if("0".equals(oa.getDayBalFlag())){
				
				//1Y1、读取系统状态
				systemInfo = systemInfoService.findSystemInfoById(1);
				//OutAccount oa= outAccountService.getOutAccount(p_account_no);
				String status = "n".equalsIgnoreCase(systemInfo.getStatus())?"NORMAL":"c".equalsIgnoreCase(systemInfo.getStatus())?"CUTOFF":
					"a".equalsIgnoreCase(systemInfo.getStatus())?"APPEND":"s".equalsIgnoreCase(systemInfo.getStatus())?"SHUTDOWN":"" ;
				//1Y2、判断系统状态
				switch (SystemStatus.valueOf(status)) {
				case NORMAL://日间运行状态
					//账号增加借贷方向与发生额方向是否一致？
					if(oa.getBalanceAddFrom().equals(debit_credit_side)){
						//账户预冻结金额>0？
						if(oa.getPreFreezeAmount().compareTo(new BigDecimal(0)) == 1){
							//账户余额+=发生额
							oa.setCurrBalance(oa.getCurrBalance().add(trans_amount));
							//冻结中金额 += min（预冻结金额，发生额）
							oa.setControlAmount(oa.getControlAmount().add((oa.getPreFreezeAmount().compareTo(trans_amount))==-1?oa.getPreFreezeAmount():trans_amount));
							//预冻结金额 = （预冻结金额 - 发生额）>0？（预冻结金额 - 发生额）：0
							oa.setPreFreezeAmount((oa.getPreFreezeAmount().subtract(trans_amount).compareTo(new BigDecimal(0))==1?oa.getPreFreezeAmount().subtract(trans_amount):new BigDecimal(0)));
						}else{
							//账户余额+=发生额
							oa.setCurrBalance(oa.getCurrBalance().add(trans_amount));
						}
					}else {
						//账户余额-=发生额
						oa.setCurrBalance(oa.getCurrBalance().subtract(trans_amount));
					}
					
					//账户可用余额 = 账户余额 - 控制金额 - 结算中金额
					avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
					//账户可用余额 < 0 ?
					if(avaliBalance.compareTo(new BigDecimal(0)) == -1){
						//报错退出
						throw new RuntimeException("账户可用余额 < 0 ，抛出个运行时异常");
					}else{
						//修改账户信息表中余额
						msg.setBalance(oa.getCurrBalance().toString());
						msg.setAvaliBalance(avaliBalance.toString());
						this.updateExtAccount(oa);
					}
					
					break;
				case CUTOFF://日切运行状态
					ShadowAccount sa = shadowAccountService.getShadowAccount(account_no, account_flag, DateUtil.getCurrentDate());
					if (sa != null && sa.getBookedFlag() != null && sa.getBookedFlag().equals("0")) {//未入账
						BigDecimal sa_amount = new BigDecimal("0");
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							sa_amount = sa.getDebitAmount();
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							sa_amount = sa.getCreditAmount();
						}
						//影子账户未入账发生额  += 本次交易发生额
						sa_amount = sa_amount.add(trans_amount);
						//账户余额 += 影子账户未入账累计发生额
						oa.setCurrBalance(oa.getCurrBalance().add(sa_amount));
						
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							sa.setDebitAmount(sa_amount);
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							sa.setCreditAmount(sa_amount);
						}

						avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
						//账户可用余额  < 0 ？ 
						if(avaliBalance.compareTo(new BigDecimal(0)) == -1){
							throw new RuntimeException("账户可用余额 < 0 ，抛出个运行时异常");
						}else{
							//更新影子账户信息表
							shadowAccountService.updateShadowAccount(sa);
						}
					}else {//sa 为空或者没有未入账的
						sa = new ShadowAccount();
						sa.setAccountNo(oa.getAccountNo());
						sa.setAccountFlag(account_flag);
						//sa.setTransDate(systemInfo.getCurrentDate());
						sa.setTransDate(new Date());
						sa.setDebitAmount(new BigDecimal("0"));
						sa.setCreditAmount(new BigDecimal("0"));
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							sa.setDebitAmount(trans_amount);
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							sa.setCreditAmount(trans_amount);
						}
						sa.setBookedFlag("0");
						//账户当前余额 += 影子账户未入账发生额
						oa.setCurrBalance(oa.getCurrBalance().add(trans_amount));
						shadowAccountService.insertShadowAccount(sa);
					}

					avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
					msg.setBalance(oa.getCurrBalance().toString());
					msg.setAvaliBalance(avaliBalance.toString());
					break;
				case APPEND://追帐运行状态
					ShadowAccount a_sa = shadowAccountService.getShadowAccount(account_no, account_flag, DateUtil.getCurrentDate());
					if (a_sa != null && a_sa.getBookedFlag() != null && a_sa.getBookedFlag().equals("0")) {//未入账
						BigDecimal a_sa_amount = new BigDecimal("0");
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							a_sa_amount = a_sa.getDebitAmount();
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							a_sa_amount = a_sa.getCreditAmount();
						}
						a_sa_amount = a_sa_amount.add(trans_amount);
						oa.setCurrBalance(oa.getCurrBalance().add(a_sa_amount));
						
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							a_sa.setDebitAmount(a_sa_amount);
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							a_sa.setCreditAmount(a_sa_amount);
						}
						
						avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
						//账户可用余额  < 0 ？ 
						if(avaliBalance.compareTo(new BigDecimal(0)) == -1){
							throw new RuntimeException("账户可用余额 < 0 ，抛出个运行时异常");
						}else{
							msg.setBalance(oa.getCurrBalance().toString());
							msg.setAvaliBalance(avaliBalance.toString());
							//更新影子账户信息表
							a_sa.setBookedFlag("1");	//修改为已入账
							shadowAccountService.updateShadowAccount(a_sa);
							this.updateExtAccount(oa);
						}
						
					}else {//sa 为空或者没有未入账的
						sa = new ShadowAccount();
						sa.setAccountNo(oa.getAccountNo());
						sa.setAccountFlag(account_flag);
						//sa.setTransDate(systemInfo.getCurrentDate());
						sa.setTransDate(new Date());
						sa.setDebitAmount(new BigDecimal("0"));
						sa.setCreditAmount(new BigDecimal("0"));
						if (debit_credit_side.toLowerCase().equals("debit")) {//借方
							sa.setDebitAmount(trans_amount);
						}
						if (debit_credit_side.toLowerCase().equals("credit")) {//贷方
							sa.setCreditAmount(trans_amount);
						}
						sa.setBookedFlag("0");
						
						//直接加上交易金额，进行入账
						oa.setCurrBalance(oa.getCurrBalance().add(trans_amount));
						//修改状态为  “已入账”
						sa.setBookedFlag("1");
						shadowAccountService.insertShadowAccount(sa);
						this.updateExtAccount(oa);
					}

					avaliBalance = (oa.getCurrBalance().subtract(oa.getControlAmount())).subtract(oa.getSettlingAmount()) ;
					msg.setBalance(oa.getCurrBalance().toString());
					msg.setAvaliBalance(avaliBalance.toString());
					break;
				case SHUTDOWN://系统关闭
					throw new RuntimeException("系统状态为      系统关闭   ，抛出个运行时异常");
				default:
					throw new RuntimeException("系统状态出错，抛出个运行时异常");
				}
				
			}else{
				//1N1、写入外部未入账流水表
				ExtNobookedDetail extNobookedDetail = new ExtNobookedDetail() ;
				extNobookedDetail.setAccountNo(account_no);
				extNobookedDetail.setSerialNo(String.valueOf(System.currentTimeMillis()));
				extNobookedDetail.setChildSerialNo(String.valueOf(System.currentTimeMillis()));
				extNobookedDetail.setRecordDate(recordDate);
				extNobookedDetail.setBookedFlag("0");
				extNobookedDetail.setDebitCreditSide(debit_credit_side);
				extNobookedDetail.setRecordAmount(trans_amount);
				extNobookedDetail.setSummaryInfo(summary_info);
				extNobookedDetailService.insertExtNobookedDetail(extNobookedDetail) ;
			}
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		
		return msg;
		
	}
	@Override
	public int insertExtTransInfo(ExtTransInfo extTransInfo) throws Exception {
		return extAccountMapper.insertExtTransInfo(extTransInfo);
	}

	@Override
	public ExtAccountInfo getExtAccountInfoByAccountNo(String accountNo) {
		return extAccountMapper.getExtAccountInfoByAccountNo(accountNo);
	}

	/**
	 * 外部账户         冻结(从可用去扣，加入控制；从结算去扣，加入控制；最后没得扣，剩余冻结金额加入预冻结。)
	 * @param params 账户查找方式（1账号，2关系查找）,账号，外部用户ID（消费者ID或商户ID或代理商ID等支付机构外部用户ID），账户归属（支付机构号或商户号），
	 * 				卡号（预付卡账户则填卡号，否则填空），科目内部编号，币种号，操作类型（冻结，解冻），冻结金额，摘要，冲销标志（正常，冲销），交易订单号，账户类型(外部账用户类型)
	 * @return
	 * @throws Exception
	 */
	@Override
	public MsgEntity extAccountForzenAmountApi(Map<String, String> params) throws Exception {
		String p_select_type = params.get("p_select_type");//账户查找方式
		String p_account_no = params.get("p_account_no");//外部账号
		String p_user_id = params.get("p_user_id");//外部用户id 
		String p_account_owner = params.get("p_account_owner");//账号归属
		String p_card_no = params.get("p_card_no");//卡号
		String p_subject_no = params.get("p_subject_no");//科目内部编号
		String p_currency_no = params.get("p_currency_no");//币种号
		BigDecimal p_forzen_amount = new BigDecimal(params.get("p_forzen_amount"));//冻结金额
		String p_summary_info = params.get("p_summary_info");//摘要
		String p_trans_order_no = params.get("p_trans_order_no");//交易订单号
		String p_account_type = params.get("p_account_type");//账户类型
		String p_reverse_flag = params.get("p_reverse_flag");//冲销标志
		String account_flag = "0";//账号方向 0 外部账号，1 内部账号
		
		BigDecimal avali_balance = new BigDecimal("0");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record_date = formatter1.format(new Date());
		Date recordDate = formatter1.parse(record_date);
		Time recordTime = new Time(recordDate.getTime());
		
		MsgEntity msg = new MsgEntity();
		//1、判断冲销标志
		if (p_reverse_flag.equals(ReverseFlag.REVERSE.toString())) {
			p_forzen_amount = p_forzen_amount.multiply(new BigDecimal("-1"));
		}
		//2、账户查找方式是否为账号？
		if (p_select_type.equals("1")) {//账号查找方式
			
		}else if (p_select_type.equals("2")) {//多添加查找方式
				//2N1、读取外部用户账号关系表，找到账号
				ExtAccountInfo extAccountInfo = this.findExtAccountInfoByParams(p_account_type,p_user_id,p_account_owner,p_card_no,p_subject_no,p_currency_no);
				if(extAccountInfo == null){
					msg.setStatus("false");
					msg.setMsg("查找外部用户账户关系表，没找到相应的账号");
					return msg ;
				}
				p_account_no = extAccountInfo.getAccountNo();
		}

		ExtAccount extAccount = null ;
			//1、读取外部用户账户表中记录
			extAccount = this.findExtAccountByAccountNo(p_account_no);
			if(extAccount == null){
				msg.setStatus("false");
				msg.setMsg("外部账户不存在！");
				return msg ;
			}
		
		//2、判断系统当前运行状态是否为    日间   ？
		SystemInfo systemInfo =  new SystemInfo();
		systemInfo.setCurrentDate(new Date());
		systemInfo = systemInfoService.findSystemInfoById(1);
		if (!SystemStatus.NORMAL.toString().equals(systemInfo.getStatus())) {//系统bu为日间
			//2N1、从影子账户中读取未入账累计交易发生额
			ShadowAccount sa = shadowAccountService.getShadowAccount(p_account_no, account_flag, DateUtil.getCurrentDate());
			BigDecimal curr_balance = extAccount.getCurrBalance();
			String balance_from  = extAccount.getBalanceFrom();
			if (sa != null && sa.getBookedFlag() != null && sa.getBookedFlag().equals("0")) {//未入账
				if (balance_from.toLowerCase().equals("debit")) {//借方
					curr_balance = curr_balance.add(sa.getDebitAmount());
					//借：账户余额 = 账户余额+借方累加金额-贷方累加金额
					//curr_balance = curr_balance.add(sa.getDebitAmount()).subtract(sa.getCreditAmount()) ;
				}
				if (balance_from.toLowerCase().equals("credit")) {//贷方
					curr_balance = curr_balance.add(sa.getCreditAmount());
					//贷：账户余额 = 账户余额+贷方累加金额-借方累加金额
					//curr_balance = curr_balance.add(sa.getCreditAmount()).subtract(sa.getDebitAmount()) ;
				}
			}
			//2N2、账户余额 += 累计未入账发生额
			extAccount.setCurrBalance(curr_balance);
		}
		
		//3、账户原可用余额 = 账号余额 - 账号控制金额 	??
		//BigDecimal original_amount = extAccount.getCurrBalance().subtract(extAccount.getControlAmount()) ;
		//修改hj  3、账户原可用余额 = 账号余额 - 账号控制金额 -账号结算中金额 	??
		BigDecimal original_amount = (extAccount.getCurrBalance().subtract(extAccount.getControlAmount())).subtract(extAccount.getSettlingAmount()) ;
		
		//4、剩余冻结金额 = 冻结金额
		//BigDecimal surplus_amount = p_forzen_amount ;
		//修改hj 4、剩余冻结金额 = 冻结金额 +账户预冻结金额		??
		BigDecimal surplus_amount = p_forzen_amount.add(extAccount.getPreFreezeAmount()) ;
		
		//5、剩余冻结金额 < 账户原可用余额   ？
		if(surplus_amount.compareTo(original_amount) == -1){
			//5Y1、账户控制中金额 += 剩余冻结金额
			extAccount.setControlAmount(extAccount.getControlAmount().add(surplus_amount));
		}else{
			//5N1、账户控制中金额 += 账户原可用余额
			extAccount.setControlAmount(extAccount.getControlAmount().add(original_amount));
			
			//5N2、剩余冻结金额 -= 账户原可用余额
			surplus_amount = surplus_amount.subtract(original_amount) ;
			
			//5N3、剩余冻结金额 < 账户结算中金额   ？
			if(surplus_amount.compareTo(extAccount.getSettlingAmount()) == -1){
				//5N3Y1、账户结算中金额 -= 剩余冻结金额
				extAccount.setSettlingAmount(extAccount.getSettlingAmount().subtract(surplus_amount));
				
				//5N3Y2、账户控制中金额 += 剩余冻结金额
				extAccount.setControlAmount(extAccount.getControlAmount().add(surplus_amount));
			}else{
				//5N3N1、账户控制中金额 += 账户结算中金额
				extAccount.setControlAmount(extAccount.getControlAmount().add(extAccount.getSettlingAmount()));
				
				//5N3N2、账户结算中金额 = 0
				extAccount.setSettlingAmount(new BigDecimal(0));
				
				//5N3N3、剩余冻结金额 -= 账户结算中金额
				surplus_amount = surplus_amount.subtract(extAccount.getSettlingAmount()) ;
				
				//5N3N4、预冻结金额 = 剩余冻结金额
				extAccount.setPreFreezeAmount(surplus_amount);
				
			}
		}
		
		//6、账户可用余额 = 账户余额 - 账号控制金额（冻结金额） - 账号结算中金额 ；更新账户表记录中的相应字段
		avali_balance = extAccount.getCurrBalance().subtract(extAccount.getControlAmount()).subtract(extAccount.getSettlingAmount());
		msg.setBalance(extAccount.getCurrBalance().toString());
		msg.setAvaliBalance(avali_balance.toString());
		this.updateExtAccount(extAccount) ;
		
		//7、登记外部账户冻结解冻记录表
		ExtAccountOpRecord opRecord = new ExtAccountOpRecord() ;
		opRecord.setAccountNo(p_account_no);			//账号
		opRecord.setRecordDate(recordDate);			//记账日期
		opRecord.setSerialNo(String.valueOf(System.currentTimeMillis()));			//记账流水号
		opRecord.setOperationBalance(p_forzen_amount);			//冻结金额
		opRecord.setOperationType("1");			//操作类型：0解冻  ，1冻结  
		//opRecord.setSummaryInfo("对账冻结");			//摘要
		opRecord.setSummaryInfo(p_summary_info);			//摘要
		opRecord.setTransOrderNo(p_trans_order_no);			//交易订单号
		extAccountOpRecordService.insertExtAccountOpRecord(opRecord) ;
		
		//8、往外部账户明细表中添加记录
		ExtTransInfo extTransInfo = new ExtTransInfo() ;
		extTransInfo.setAccountNo(p_account_no);//外部账号
		extTransInfo.setRecordAmount(p_forzen_amount);//记账金额
		extTransInfo.setBalance(extAccount.getCurrBalance());//余额
		extTransInfo.setAvaliBalance(avali_balance);//可用余额
		extTransInfo.setSerialNo(opRecord.getSerialNo());//记账流水号
		extTransInfo.setChildSerialNo("");//子交易流水号         ???????
		extTransInfo.setRecordDate(recordDate);//记账日期
		extTransInfo.setRecordTime(recordTime);//记账时间
		extTransInfo.setDebitCreditSide(extAccount.getBalanceFrom());//借贷方向:debit-借方,credit-贷方
		extTransInfo.setSummaryInfo(p_summary_info) ;//摘要
		extTransInfo.setTransOrderNo(p_trans_order_no);//交易订单号
		this.insertExtTransInfo(extTransInfo) ;
		
		
		/*//9、往交易流水表中添加记录
		
		CoreTransInfo transInfo = new CoreTransInfo();
		transInfo.setAccountNo(p_account_no);
		transInfo.setAccountFlag(account_flag);
		transInfo.setTransAmount(extAccount.getCurrBalance());
		transInfo.setCurrencyNo(extAccount.getCurrencyNo());
		transInfo.setSubjectNo(extAccount.getSubjectNo());
		transInfo.setReverseFlag(ReverseFlag.REVERSED.toString());
		transInfo.setDebitCreditSide(extAccount.getBalanceAddFrom());
		transInfo.setSummaryInfo(p_summary_info);
		//TODO SerialNo 需要调整
		transInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
		//TODO ChildSerialNo 需要调整
		transInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setTransDate(recordDate);
		transInfo.setTransTime(recordTime);
		coreTransInfoService.insertTransInfo(transInfo);*/
		
		
		msg.setStatus("200");
		msg.setMsg("成功!");
		
		return msg;
	}
	
	/**
	 * 外部账户      解冻
	 * @param params 账户查找方式（1账号，2关系查找）,账号，外部用户ID（消费者ID或商户ID或代理商ID等支付机构外部用户ID），账户归属（支付机构号或商户号），
	 * 				卡号（预付卡账户则填卡号，否则填空），科目内部编号，币种号，操作类型（冻结，解冻），冻结金额，摘要，冲销标志（正常，冲销），交易订单号，账户类型
	 * @return
	 * @throws Exception
	 */
	@Override
	public MsgEntity extAccountThawAmountApi(Map<String, String> params) throws Exception {
		String p_select_type = params.get("p_select_type");//账户查找方式
		String p_account_no = params.get("p_account_no");//外部账号
		String p_user_id = params.get("p_user_id");//外部用户id 
		String p_account_owner = params.get("p_account_owner");//账号归属
		String p_card_no = params.get("p_card_no");//卡号
		String p_subject_no = params.get("p_subject_no");//科目内部编号
		String p_currency_no = params.get("p_currency_no");//币种号
		BigDecimal p_thaw_amount = new BigDecimal(params.get("p_thaw_amount"));//解冻金额
		String p_summary_info = params.get("p_summary_info");//摘要
		String p_trans_order_no = params.get("p_trans_order_no");//交易订单号
		String p_account_type = params.get("p_account_type");//账户类型
		String p_reverse_flag = params.get("p_reverse_flag");//冲销标志
		String account_flag = "0";//账号方向 0 外部账号，1 内部账号
		
		BigDecimal avali_balance = new BigDecimal("0");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record_date = formatter1.format(new Date());
		Date recordDate = formatter1.parse(record_date);
		Time recordTime = new Time(recordDate.getTime());
		
		MsgEntity msg = new MsgEntity();
		//1、判断冲销标志
		if (p_reverse_flag.equals(ReverseFlag.REVERSE.toString())) {
			p_thaw_amount = p_thaw_amount.multiply(new BigDecimal("-1"));
		}
		//2、账户查找方式是否为账号？
		if (p_select_type.equals("1")) {//账号查找方式
			
		}else if (p_select_type.equals("2")) {//多添加查找方式
				//2N1、读取外部用户账号关系表，找到账号
				ExtAccountInfo extAccountInfo = this.findExtAccountInfoByParams(p_account_type,p_user_id,p_account_owner,p_card_no,p_subject_no,p_currency_no);
				if(extAccountInfo == null){
					msg.setStatus("false");
					msg.setMsg("查找外部用户账户关系表，没找到相应的账号");
					return msg ;
				}
				p_account_no = extAccountInfo.getAccountNo();
			
		}
		
		ExtAccount extAccount = null ;
		//1、读取外部用户账户表中记录
		extAccount = this.findExtAccountByAccountNo(p_account_no);
		if(extAccount == null){
			msg.setStatus("false");
			msg.setMsg("外部账户不存在！");
			return msg ;
		}
		
		//2、判断系统当前运行状态是否为    日间   ？
		SystemInfo systemInfo =  new SystemInfo();
		systemInfo.setCurrentDate(new Date());
		systemInfo = systemInfoService.findSystemInfoById(1);
		if (!SystemStatus.NORMAL.toString().equals(systemInfo.getStatus())) {//系统 bu 为日间
			//2N1、从影子账户中读取未入账累计交易发生额
			ShadowAccount sa = shadowAccountService.getShadowAccount(p_account_no, account_flag, DateUtil.getCurrentDate());
			BigDecimal curr_balance = extAccount.getCurrBalance();
			String balance_from  = extAccount.getBalanceFrom();
			if (sa != null && sa.getBookedFlag() != null && sa.getBookedFlag().equals("0")) {//未入账
				if (balance_from.toLowerCase().equals("debit")) {//借方
					curr_balance = curr_balance.add(sa.getDebitAmount());
					//借：账户余额 = 账户余额+借方累加金额-贷方累加金额
					//curr_balance = curr_balance.add(sa.getDebitAmount()).subtract(sa.getCreditAmount()) ;
				}
				if (balance_from.toLowerCase().equals("credit")) {//贷方
					curr_balance = curr_balance.add(sa.getCreditAmount());
					//贷：账户余额 = 账户余额+贷方累加金额-借方累加金额
					//curr_balance = curr_balance.add(sa.getCreditAmount()).subtract(sa.getDebitAmount()) ;
				}
			}
			//2N2、账户余额 += 累计未入账发生额
			extAccount.setCurrBalance(curr_balance);
		}
		
		//3、剩余解冻金额 = 解冻金额
		BigDecimal surplus_amount = p_thaw_amount ;
		
		//4、剩余解冻金额 < 账户预冻结余额   ？
		if(surplus_amount.compareTo(extAccount.getPreFreezeAmount()) == -1){
			//4Y1、账户预冻结金额 -= 剩余解冻金额
			extAccount.setPreFreezeAmount(extAccount.getPreFreezeAmount().subtract(surplus_amount));
		}else{
			//4N1、剩余解冻金额 -= 账户预冻结金额
			surplus_amount = surplus_amount.subtract(extAccount.getPreFreezeAmount()) ;
			
			//4N2、账号预冻结金额 = 0
			extAccount.setPreFreezeAmount(new BigDecimal(0));
			
			//4N3、剩余解冻金额 < 账户控制中金额   ？
			if(surplus_amount.compareTo(extAccount.getControlAmount()) == -1){
				//4N3Y1、账户控制中金额 -= 剩余解冻金额
				extAccount.setControlAmount(extAccount.getControlAmount().subtract(surplus_amount));
			}else{
				throw new RuntimeException("解冻金额错误，大于冻结金额，超出账户总额 !");
			}
		}
		
		//5、账户可用余额 = 账户余额 - 账号控制金额（冻结金额） - 账号结算中金额 ；更新账户表记录中的相应字段
		avali_balance = extAccount.getCurrBalance().subtract(extAccount.getControlAmount()).subtract(extAccount.getSettlingAmount());
		msg.setBalance(extAccount.getCurrBalance().toString());
		msg.setAvaliBalance(avali_balance.toString());
		this.updateExtAccount(extAccount) ;
		
		//7、登记外部账户冻结解冻记录表
		ExtAccountOpRecord opRecord = new ExtAccountOpRecord() ;
		opRecord.setAccountNo(p_account_no);			//账号
		opRecord.setRecordDate(recordDate);			//记账日期
		opRecord.setSerialNo(String.valueOf(System.currentTimeMillis()));			//记账流水号
		opRecord.setOperationBalance(p_thaw_amount);			//解冻金额           ????
		opRecord.setOperationType("0");			//操作类型：0解冻  ，1冻结
		//opRecord.setSummaryInfo("解冻结算");			//摘要
		opRecord.setSummaryInfo(p_summary_info);			//摘要
		opRecord.setTransOrderNo(p_trans_order_no);			//交易订单号
		extAccountOpRecordService.insertExtAccountOpRecord(opRecord) ;
		
		//8、往外部账户明细表中添加记录
		ExtTransInfo extTransInfo = new ExtTransInfo() ;
		extTransInfo.setAccountNo(p_account_no);//外部账号
		extTransInfo.setRecordAmount(p_thaw_amount);//记账金额
		extTransInfo.setBalance(extAccount.getCurrBalance());//余额
		extTransInfo.setAvaliBalance(avali_balance);//可用余额
		extTransInfo.setSerialNo(opRecord.getSerialNo());//记账流水号
		extTransInfo.setChildSerialNo("");//子交易流水号         ???????
		extTransInfo.setRecordDate(recordDate);//记账日期
		extTransInfo.setRecordTime(recordTime);//记账时间
		extTransInfo.setDebitCreditSide(extAccount.getBalanceFrom());//借贷方向:debit-借方,credit-贷方
		extTransInfo.setSummaryInfo(p_summary_info) ;//摘要
		extTransInfo.setTransOrderNo(p_trans_order_no);//交易订单号
		this.insertExtTransInfo(extTransInfo) ;
		
		
		/*//9、往交易流水表中添加记录
		
		CoreTransInfo transInfo = new CoreTransInfo();
		transInfo.setAccountNo(p_account_no);
		transInfo.setAccountFlag(account_flag);
		transInfo.setTransAmount(extAccount.getCurrBalance());
		transInfo.setCurrencyNo(extAccount.getCurrencyNo());
		transInfo.setSubjectNo(extAccount.getSubjectNo());
		transInfo.setReverseFlag(ReverseFlag.REVERSED.toString());
		transInfo.setDebitCreditSide(extAccount.getBalanceAddFrom());
		transInfo.setSummaryInfo(p_summary_info);
		//TODO SerialNo 需要调整
		transInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
		//TODO ChildSerialNo 需要调整
		transInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setTransDate(recordDate);
		transInfo.setTransTime(recordTime);
		coreTransInfoService.insertTransInfo(transInfo);*/
		
		
		msg.setStatus("200");
		msg.setMsg("成功!");
		
		return msg;
	}
	
	@Override
	public MsgEntity extAccountLessAmountApi(Map<String, String> params) throws Exception {
		String p_select_type = params.get("p_select_type");
		String p_account_no = params.get("p_account_no");
		String p_user_id = params.get("p_user_id");
		String p_account_owner = params.get("p_account_owner");
		String p_card_no = params.get("p_card_no");
		String p_subject_no = params.get("p_subject_no");
		String p_currency_no = params.get("p_currency_no");
		BigDecimal p_trans_amount = new BigDecimal(params.get("p_trans_amount"));
		String p_reverse_flag = params.get("p_reverse_flag");//冲销标志
		String p_summary_info = params.get("p_summary_info");
		String p_account_type = params.get("p_account_type");
		String account_flag = "0";
		BigDecimal avali_balance = new BigDecimal("0");
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String record_date = formatter1.format(new Date());
		Date recordDate = formatter1.parse(record_date);
		Time recordTime = new Time(recordDate.getTime());
		
		MsgEntity msg = new MsgEntity();
		if (p_reverse_flag.equals(ReverseFlag.REVERSE)) {
			p_trans_amount = p_trans_amount.multiply(new BigDecimal("-1"));
		}
		if (p_select_type.equals("1")) {//账号查找方式
			
		}else if (p_select_type.equals("2")) {//多添加查找方式
			ExtAccountInfo eat = this.findExtAccountInfoByParams(p_account_type,p_user_id,p_account_owner,p_card_no,p_subject_no,p_currency_no);
			p_account_no = eat.getAccountNo();
		}
		ExtAccount oa = this.getExtAccount(p_account_no);
		if (oa.getAccountStatus().equals(AccountStatus.FREEZE_ONLY_IN_DENY_OUT)) {//冻结只进不出
			BigDecimal controlAmount = oa.getControlAmount();
			oa.setControlAmount(controlAmount.add(p_trans_amount));
		}
		else{
			oa.setControlAmount(p_trans_amount);
		}
		
		SystemInfo systemInfo =  new SystemInfo();
		systemInfo.setCurrentDate(new Date());
		systemInfo = systemInfoService.findSystemInfoById(1);
		if (SystemStatus.NORMAL.equals(systemInfo.getStatus())) {//系统为日间
			
		}
		else{
			ShadowAccount a_sa = shadowAccountService.getShadowAccount(p_account_no, account_flag, DateUtil.getCurrentDate());
			
			if (a_sa != null && a_sa.getBookedFlag() != null && a_sa.getBookedFlag().equals("0")) {//未入账
				//借：账户余额 = 账户余额+借方累加金额-贷方累加金额
				//贷：账户余额 = 账户余额+贷方累加金额-借方累加金额
				String balance_from  = oa.getBalanceFrom();
				BigDecimal curr_balance = oa.getCurrBalance();
				ShadowAccount sa = shadowAccountService.getShadowAccount(p_account_no, account_flag, DateUtil.getCurrentDate());
				
				if (balance_from.toLowerCase().equals("debit")) {
					curr_balance = curr_balance.add(sa.getDebitAmount()).subtract(sa.getCreditAmount()) ;
				}
				if (balance_from.toLowerCase().equals("credit")) {
					curr_balance = curr_balance.add(sa.getCreditAmount()).subtract(sa.getDebitAmount()) ;
				}
				oa.setCurrBalance(curr_balance);
			}
			avali_balance = oa.getCurrBalance().subtract(oa.getControlAmount());
		}
		msg.setBalance(oa.getCurrBalance().toString());
		msg.setAvaliBalance(avali_balance.toString());
		
		//登记外部账户冻结解冻记录表
		ExtAccountOpRecord extAccountOpRecord = new ExtAccountOpRecord();
		extAccountOpRecord.setAccountNo(p_account_no);
		extAccountOpRecord.setRecordDate(recordDate);
		extAccountOpRecord.setOperationType("0");
		extAccountOpRecord.setOperationBalance(p_trans_amount);
		//TODO SerialNo 需要调整
		extAccountOpRecord.setSerialNo(String.valueOf(System.currentTimeMillis()));
		extAccountOpRecordService.insertExtAccountOpRecord(extAccountOpRecord);
		
		//往交易流水表中添加记录
		
		CoreTransInfo transInfo = new CoreTransInfo();
		transInfo.setAccountNo(p_account_no);
		transInfo.setAccountFlag(account_flag);
		transInfo.setTransAmount(oa.getCurrBalance());
		transInfo.setCurrencyNo(oa.getCurrencyNo());
		transInfo.setSubjectNo(oa.getSubjectNo());
		transInfo.setReverseFlag(ReverseStatus.REVERSED.toString());
		transInfo.setDebitCreditSide(oa.getBalanceAddFrom());
		transInfo.setSummaryInfo(p_summary_info);
		//TODO SerialNo 需要调整
		transInfo.setSerialNo(String.valueOf(System.currentTimeMillis()));
		//TODO ChildSerialNo 需要调整
		transInfo.setChildSerialNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setJournalNo(String.valueOf(System.currentTimeMillis()));//以后调整
		transInfo.setTransDate(recordDate);
		transInfo.setTransTime(recordTime);
		coreTransInfoService.insertTransInfo(transInfo);
		
		this.updateExtAccount(oa);
		
		msg.setStatus("200");
		msg.setMsg("成功!");
		
		return msg;
	}

	@Override
	public List<ExtAccount> findExtAccountBySubjectNo(String subjectNo) {
		return extAccountMapper.findExtAccountBySubjectNo(subjectNo);
	}

	@Override
	public boolean createDefaultExtAccount(Map<String, String> params) throws Exception {
		String accountType = params.get("accountType");
		String userId = params.get("userId");
		int i = 0;
		try {
			List<OrgInfo> orgInfoList = orgInfoService.findOrgInfo();
			List<ExtUserTypeSubject> extUserTypeSubjectList= extUserTypeSubjectService.findExtUserTypeSubjectByUserType(accountType);
			List<Currency> currencyList = currencyService.findCurrency();
			for (Currency currency : currencyList) {
				for (OrgInfo orgInfo : orgInfoList) {
					for (ExtUserTypeSubject extUserTypeSubject : extUserTypeSubjectList) {
						//accountNo = 6位机构号+14位科目号+12位顺序号
						String subjectNo = extUserTypeSubject.getSubjectNo();
						String orgNo = orgInfo.getOrgNo();
						String currencyNo = currency.getCurrencyNo();
						String accountNo = commonService.getExtAccountNo(orgNo,subjectNo);
						ExtAccount outAccount = new ExtAccount();
						Date now = new Date();
						
						outAccount.setCurrBalance(new BigDecimal(0));
						outAccount.setControlAmount(new BigDecimal(0));
						outAccount.setSettlingAmount(new BigDecimal(0));
						outAccount.setPreFreezeAmount(new BigDecimal(0));
						outAccount.setParentTransBalance(new BigDecimal(0));
						outAccount.setAccountStatus("1");
						Subject subject = subjectService.getSubject(subjectNo);
						outAccount.setBalanceAddFrom(subject.getAddBalanceFrom());
						outAccount.setBalanceFrom(subject.getBalanceFrom());
						outAccount.setAccountNo(accountNo);
						outAccount.setOrgNo(orgNo);
						outAccount.setCurrencyNo(currencyNo);
						outAccount.setAccountName(subject.getSubjectName()); //账号名称  = 科目名称
						outAccount.setCreateTime(now);
						
						ExtAccountInfo extAccountInfo = new ExtAccountInfo();
						extAccountInfo.setAccountNo(outAccount.getAccountNo());
						extAccountInfo.setAccountOwner(outAccount.getOrgNo());
						extAccountInfo.setAccountType(accountType);
				//		extAccountInfo.setCardNo(p_card_no);
						extAccountInfo.setCurrencyNo(outAccount.getCurrencyNo());
						extAccountInfo.setSubjectNo(outAccount.getSubjectNo());
						extAccountInfo.setUserId(userId);
					
						i = this.insertExtAccount(outAccount,extAccountInfo);
				
					}
				}
			}
		} catch (Exception e) {
			log.error("异常:",e);
		}
		if (i>0) {
			return true;
		}
		return false;
	}

	@Override
	public List<ExtAccount> findExtAccountByAccountType(String accountType, Date transTime) throws Exception {
		return extAccountMapper.findExtAccountByAccountType(accountType, transTime);
	}

	@Override
	public List<ExtAccountInfo> findExtAccountInfoByAccountTypeAndUserId(String accountType, String userId) throws Exception {
		return extAccountMapper.findExtAccountInfoByAccountTypeAndUserId(accountType,userId);
	}

	@Override
	public ExtAccount findExtAccountByAccountNo(String accountNo) throws Exception {
		return extAccountMapper.findExtAccountByAccountNo(accountNo);
	}

	@Override
	public boolean updateSettlingAmount() throws Exception {
		List<SysDict> mdict = sysDictService.findSysDictGroup("subject_M");
		List<SysDict> adict = sysDictService.findSysDictGroup("subject_A");
		List<SysDict> cdict = sysDictService.findSysDictGroup("subject_C");
		
		if (mdict != null && adict != null && cdict != null) {
			//商户
			int rm = extAccountMapper.updateExtAccountByMAndA(mdict.get(0).getSysValue());
			//代理商
			int ra = extAccountMapper.updateExtAccountByMAndA(adict.get(0).getSysValue());
			
			int rc = extAccountMapper.updateExtAccountByMAndA(cdict.get(0).getSysValue());
			return rm > 0 && ra > 0 && rc > 0;
		}
		
		return false;
	}

	@Override
	public List<ExtAccount> findAll() {
		return extAccountMapper.findAll();
	}

	@Override
	public Map<String,Object> findByMerchantNo(String merchantNo) {
		return extAccountMapper.findByMerchantNo(merchantNo);
	}

	@Override
	public ExtAccountInfo getByUserId(String userId) {
		return extAccountMapper.getByUserId(userId);
	}

	@Override
	public ExtAccount findExtAccountByMerchantNo(String merchantNo) {
		return extAccountMapper.findExtAccountByMerchantNo(merchantNo);
	}

	@Override
	public int updateAddSettlingAmount(BigDecimal outAccountTaskAmount, String merchantNo) {
		return extAccountMapper.updateAddSettlingAmount(outAccountTaskAmount, merchantNo);
	}

	@Override
	public ExtAccountInfo findExtAccountInfoByManyParams(String accountType, String userId, String accountOwner,
			 String subjectNo, String currencyNo) throws Exception {
		return extAccountMapper.findExtAccountInfoByManyParams(accountType, userId, accountOwner, subjectNo, currencyNo);
	}

	@Override
	public Map<String, Object> agentFreezeAmount(String agentNo,String subjectNo, BigDecimal amount)
			throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", false);
			msg.put("msg", "冻结金额都为0,不需要冻结");
			return msg;
		}
		
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("accountOwner", "000001");
		claims.put("cardNo", "");
		claims.put("subjectNo", subjectNo);//"224105"
		claims.put("currencyNo", "1");
		claims.put("opt", "freeze");
		claims.put("amount", amount.toString()); // 交易解冻
		claims.put("summaryInfo", "");
		claims.put("reverseFlag", ReverseFlag.NORMAL.toString());
//		claims.put("transOrderNo", transInfo.getAccountNo());
		claims.put("transDate", DateUtil.getCurrentDate());

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/extAccountController/extAccountFreezePartAmount.do";
		log.info("冻结 url：" + url);
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("冻结返回结果：" + response);

		ObjectMapper om = new ObjectMapper();
		Map<String, Object> resp = om.readValue(response, Map.class);
		if (response == null || "".equals(response)) {
			// 平台单边确认是日切
			String errorMsg = "冻结 返回为空";
			msg.put("status", false);
			msg.put("msg", errorMsg);
			return msg;
		} else {
			if ((boolean) resp.get("status") == false) {
				String errMsg = "";
				if (resp.get("msg") == null || resp.get("msg") == "") {
					errMsg += "冻结message:返回为空";
				} else {
					errMsg = "冻结记账失败:" + resp.get("msg").toString();
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
				}
				msg.put("status", false);
				msg.put("msg", errMsg);
				return msg;
			} else {
				msg.put("status", true);
				msg.put("msg", "冻结成功");
				return msg;
			}
		}
	}

	@Override
	public Map<String, Object> agentUnFreezeAmount(String agentNo,String subjectNo, BigDecimal amount)
			throws Exception, JsonMappingException, IOException {
		Map<String,Object> msg=new HashMap<>();
		
		if (amount.compareTo(BigDecimal.ZERO) == 0) {
			msg.put("status", false);
			msg.put("msg", "解冻金额都为0,不需要解冻");
			return msg;
		}
		
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		claims.put("selectType", "2");
		claims.put("accountType", "A");
		claims.put("userId", agentNo);
		claims.put("accountOwner", "000001");
		claims.put("cardNo", "");
		claims.put("subjectNo", subjectNo);
		claims.put("currencyNo", "1");
		claims.put("opt", "unfreeze");
		claims.put("amount", amount.toString()); // 交易解冻
		claims.put("summaryInfo", "");
		claims.put("reverseFlag", ReverseFlag.NORMAL.toString());
//		claims.put("transOrderNo", transInfo.getAccountNo());
		claims.put("transDate", DateUtil.getCurrentDate());

		final String token = signer.sign(claims);
		String url = accountApiHttpUrl + "/extAccountController/extAccountFreezePartAmount.do";
		log.info("解冻 url：" + url);
		String response = HttpConnectUtil.postHttp(url, "token", token);
		log.info("解冻返回结果：" + response);

		ObjectMapper om = new ObjectMapper();
		Map<String, Object> resp = om.readValue(response, Map.class);
		if (response == null || "".equals(response)) {
			// 平台单边确认是日切
			String errorMsg = "解冻 返回为空";
			msg.put("status", false);
			msg.put("msg", errorMsg);
			return msg;
		} else {
			if ((boolean) resp.get("status") == false) {
				String errMsg = "";
				if (resp.get("msg") == null || resp.get("msg") == "") {
					errMsg += "解冻message:返回为空";
				} else {
					errMsg = "解冻记账失败:" + resp.get("msg").toString();
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
				}
				msg.put("status", false);
				msg.put("msg", errMsg);
				return msg;
			} else {
				msg.put("status", true);
				msg.put("msg", "解冻成功");
				return msg;
			}
		}
	}


}
