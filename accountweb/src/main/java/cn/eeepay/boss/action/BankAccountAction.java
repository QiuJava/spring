package cn.eeepay.boss.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.BankAccount;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.service.bill.BankAccountService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.InsAccountService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.SysDictService;

/**
 * 银行账户管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/bankAccountAction")
public class BankAccountAction {
	@Resource
	public BankAccountService bankAccountService;
	@Resource
	public OrgInfoService orgInfoService ;
	@Resource
	public CurrencyService currencyService ;
	@Resource
	public InsAccountService inputAccountService ;
	@Resource
	public SysDictService sysDictService ;
	
	private static final Logger log = LoggerFactory.getLogger(BankAccountAction.class);
	
	/**
	 * 跳转  新增银行账户页面
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('bankAccountAdd:query')")
	@RequestMapping(value = "/toBankAccountAdd.do")
	public String bankAccountAdd(ModelMap model,@RequestParam Map<String,String> params) throws Exception{
		//查询所有  组织机构
		List<OrgInfo> orgInfo = orgInfoService.findOrgInfo() ;
		model.put("orgInfos", orgInfo);
		//查询所有  货币种类
		List<Currency> currencyInfo = currencyService.findCurrency() ;
		model.put("currencyInfos", currencyInfo);
		//查询所有  账户类型
		List<SysDict> accountTypeInfo = sysDictService.findSysDictGroup("accountType") ;
		model.put("accountTypeInfos", accountTypeInfo) ;
		
		return "bankAccount/bankAccountAdd" ;  
	}
	
	
	/**
	 * 跳转   银行账户管理
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('bankAccountManage:query')")
	@RequestMapping(value = "/bankAccountManage.do")
	public String bankAccountManage(ModelMap model,@RequestParam Map<String,String> params) throws Exception{
		//查询所有  组织机构
		List<OrgInfo> orgInfo = orgInfoService.findOrgInfo() ;
		model.put("orgInfos", orgInfo);
		//查询所有  货币种类
		List<Currency> currencyInfo = currencyService.findCurrency() ;
		model.put("currencyInfos", currencyInfo);
		//查询所有  账户状态
		List<SysDict> accountStatusInfo = sysDictService.findSysDictGroup("sys_account_status") ;
		model.put("accountStatusInfos", accountStatusInfo) ;
		//查询所有  账户类型
		List<SysDict> accountTypeInfo = sysDictService.findSysDictGroup("accountType") ;
		model.put("accountTypeInfos", accountTypeInfo) ;
				
		return "bankAccount/bankAccountManage" ;
	}
	

	/**
	 * 保存银行账户
	 * @param bankAccount
	 * @return
	 */
	@RequestMapping(value = "/saveBankAccount.do")
	@Logs(description="保存银行账户")
	@ResponseBody
	public Map<String,Object> saveBankAccount(@ModelAttribute BankAccount bankAccount){		
		Map<String,Object> msg=new HashMap<>();
		Map<String, String> params = new HashMap<>();
		//开启一个新的内部（虚拟）账户
		try{
			params.put("accountName", bankAccount.getAccountName()) ;
			params.put("accountNo", bankAccount.getAccountNo()) ;
			params.put("accountType", bankAccount.getAccountType()) ;
			params.put("bankName", bankAccount.getBankName()) ;
			params.put("cnapsNo", bankAccount.getCnapsNo()) ;
			params.put("p_subject_no", bankAccount.getSubjectNo()) ;
			params.put("p_currency_no", bankAccount.getCurrencyNo()) ;
			params.put("p_org_no", bankAccount.getOrgNo()) ;
			inputAccountService.createInputAccount(params) ;
		}catch(Exception e){
			log.error("异常:",e);
			if("bankAccountExisted".equals(e.getMessage())){
				msg.put("state",false);
				msg.put("msg","开启银行账户失败！该银行账号已存在");
				log.info(msg.toString());
				log.error("开启银行账户失败！该银行账号已存在",e);
				return msg ;
			}else{
				msg.put("state",false);
				msg.put("msg","开启银行账户失败！");
				log.info(msg.toString());
				log.error("开启银行账户失败！",e);
				return msg ;
			}
		}
		msg.put("state",true);
		msg.put("msg","开启银行账户成功！");
		log.info(msg.toString());
		return msg;
	}
	
	
	/**
	 * 查询银行账户列表
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('bankAccountManage:query')")
	@RequestMapping(value = "/findBankAccountList.do")
	@ResponseBody
	public Page<BankAccount> findBankAccountList(@ModelAttribute BankAccount bankAccount,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<BankAccount> page){
		try {
			//System.out.println("page:"+page);
			bankAccountService.findBankAccountList(bankAccount, sort, page) ;
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}
	
	/**
	 * 通过  id  查询银行账户
	 * @param model
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('bankAccountManage:detail')")
	@RequestMapping(value="/toFindBankAccountById.do")
	public String findBankAccountById(ModelMap model,@RequestParam(value="id") String id){
		//查询所有  组织机构
		List<OrgInfo> orgInfo = null;
		//查询所有的币种号
		List<Currency> currencyInfo = null;
		List<SysDict> accountTypeInfo = null ;		//查询账户类型
		List<SysDict> accountStatusInfo = null ;		//查询账户状态
		try {
			orgInfo = orgInfoService.findOrgInfo();
			currencyInfo = currencyService.findCurrency() ;
			accountTypeInfo = sysDictService.findSysDictGroup("accountType") ;
			accountStatusInfo = sysDictService.findSysDictGroup("sys_account_status") ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		BankAccount bankAccount = bankAccountService.findBankAccountById(id) ;
		//格式化传入页面的值
		for(OrgInfo org:orgInfo){
			if(org.getOrgNo().equals(bankAccount.getOrgNo())){
				bankAccount.setOrgNo(org.getOrgName());break ;
			}
		}
		for(Currency currency:currencyInfo){
			if(currency.getCurrencyNo().equals(bankAccount.getCurrencyNo())){
				bankAccount.setCurrencyNo(currency.getCurrencyName());break ;
			}
		}
		for(SysDict accountType:accountTypeInfo){
			if(accountType.getSysValue().equals(bankAccount.getAccountType())){
				bankAccount.setAccountType(accountType.getSysName());;break ;
			}
		}
		for(SysDict accountStatus:accountStatusInfo){
			if(accountStatus.getSysValue().equals(bankAccount.getInsAccount().getAccountStatus())){
				bankAccount.getInsAccount().setAccountStatus(accountStatus.getSysName());;break ;
			}
		}
		
		
		//bankAccount
		model.put("bankAccount", bankAccount) ;
		return "bankAccount/bankAccountDetail" ;
	}
	
	/**
	 * 跳转到  修改  银行账户页面
	 * @param model
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('bankAccountManage:update')")
	@RequestMapping(value="/toBankAccountUpdatePage.do")
	public String bankAccountUpdatePage(ModelMap model,@RequestParam(value="id") String id){
		//查询所有  组织机构
		List<OrgInfo> orgInfo = null;
		//查询所有的币种号
		List<Currency> currencyInfo = null;
		List<SysDict> accountStatusList = null ;
		List<SysDict> accountTypeInfo = null ;		//查询账户类型
		try {
			orgInfo = orgInfoService.findOrgInfo();
			currencyInfo = currencyService.findCurrency() ;
			accountTypeInfo = sysDictService.findSysDictGroup("accountType") ;
			accountStatusList = sysDictService.findSysDictGroup("sys_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("orgInfos", orgInfo);
		model.put("currencyInfos", currencyInfo);
		model.put("accountTypeInfos", accountTypeInfo) ;
		model.put("accountStatusList", accountStatusList);
		
		BankAccount bankAccount = bankAccountService.findBankAccountById(id) ;
		model.put("bankAccount", bankAccount) ;
		model.put("id", id) ;
		return "bankAccount/bankAccountUpdate" ;
	}
	
	@PreAuthorize("hasAuthority('bankAccountManage:update')")
	@ResponseBody
	@RequestMapping(value="/bankAccountUpdate.do")
	public Map<String,Object> bankAccountUpdate(ModelMap model, @RequestParam Map<String, String> params,@ModelAttribute BankAccount bankAccount) throws Exception{
		
		Map<String,Object> msg=new HashMap<>();
		String id = bankAccount.getId()+"" ;
		
		try{
			BankAccount bankAccountQuery = bankAccountService.findBankAccountById(id) ; 
			bankAccountQuery.setId(bankAccount.getId());
			bankAccountQuery.getInsAccount().setAccountStatus(params.get("accountStatus"));
			bankAccountQuery.setBankName(bankAccount.getBankName());
			bankAccountQuery.setAccountName(bankAccount.getAccountName());
			bankAccountQuery.setAccountNo(bankAccount.getAccountNo());
			bankAccountQuery.setCnapsNo(bankAccount.getCnapsNo());
			bankAccountQuery.setCurrencyNo(bankAccount.getCurrencyNo());
			bankAccountQuery.setOrgNo(bankAccount.getOrgNo());
			bankAccountQuery.setAccountType(bankAccount.getAccountType());
			bankAccountQuery.setSubjectNo(bankAccount.getSubjectNo());
			 int i =bankAccountService.updateBankAccount(bankAccountQuery);
			 if(i>0){
				 msg.put("state",true);
				 msg.put("msg", "修改成功!");
				 log.info(msg.toString());
			 }
		}catch(Exception e){
			 msg.put("msg","修改银行账户失败！");
			 log.error(msg.toString());
			 log.error("修改银行账户失败！",e);
		}
		//System.out.println(msg) ;
		return msg;

	}
		
}
