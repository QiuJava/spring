package cn.eeepay.boss.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.eeepay.boss.util.Constants;
import cn.eeepay.boss.util.DownloadUtil;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.AdjustAccount;
import cn.eeepay.framework.model.bill.AdjustDetail;
import cn.eeepay.framework.model.bill.Currency;
import cn.eeepay.framework.model.bill.OrgInfo;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.AdjustAccountService;
import cn.eeepay.framework.service.bill.CurrencyService;
import cn.eeepay.framework.service.bill.OrgInfoService;
import cn.eeepay.framework.service.bill.SysDictService;

/**
 * 调账管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/adjustAction")
public class AdjustAction {	
	@Resource
	public AdjustAccountService adjustAccountService;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public OrgInfoService orgInfoService ;
	@Resource
	public CurrencyService currencyService ;
	
	private static final Logger log = LoggerFactory.getLogger(AdjustAction.class);
	
	//记账导入界面adjustAdd:query
	@PreAuthorize("hasAuthority('adjustAdd:query')")
	@RequestMapping(value="/adjustAdd.do")
	public String adjustAdd(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountTypeList = null;
		try {
			adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
			
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		return "adjust/adjustAdd";
	}
	@PreAuthorize("hasAuthority('adjustRecordListQuery:update')")
	@RequestMapping(value="/adjustUpdate.do")
	public String adjustUpdate(@ModelAttribute AdjustAccount adjustAccount,ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountTypeList = null;
		try {
			adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
			
			adjustAccount = adjustAccountService.getAdjustAccount(adjustAccount.getId());
			
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		model.put("adjustAccount", adjustAccount);
		return "adjust/adjustUpdate";
	}
	
	/**
	 * 从调账记账页面提交审核
	 * @param model
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('adjustRecordListQuery:submitExamine')")
	@RequestMapping(value="/adjustRecordExamine.do")
	public String adjustRecordExamine(ModelMap model,@RequestParam Map<String, String> params){
		AdjustAccount adjustAccount =new AdjustAccount();
		String id = params.get("id");
		List<SysDict> balanceFromList = null;
		List<SysDict> adjustAccountTypeList = null;
		List<SysDict> adjustAccountStatusList = null;
		List<SysDict> accountTypeList = null;//外部账用户类型
		List<SysDict> accountFlagList = null;//账号方向：1:内部账户，0：外部账户
		List<OrgInfo> orgInfoList = null ;//账户归属
		List<Currency> currencyList =null ;//币种号
		try {
			 balanceFromList = sysDictService.findSysDictGroup("adjust_amount_from");
			 adjustAccount = adjustAccountService.getAdjustAccount(Integer.parseInt(id));
			 adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
			 adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
			 accountTypeList = sysDictService.findSysDictGroup("sys_account_type");
			 accountFlagList = sysDictService.findSysDictGroup("sys_account_falg");
			 orgInfoList = orgInfoService.findOrgInfo() ;
			 currencyList = currencyService.findCurrency() ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("balanceFromList", balanceFromList);
		model.put("adjustAccount", adjustAccount);
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		model.put("accountTypeList", accountTypeList);
		model.put("accountFlagList", accountFlagList);
		model.put("orgInfoList", orgInfoList);
		model.put("currencyList", currencyList);
		return "adjust/adjustRecordExamine";		
	}
	
	@PreAuthorize("hasAuthority('adjustExamine:query')")
	@RequestMapping(value="/examineList.do")
	public String examineList(ModelMap model,@RequestParam Map<String, String> params){
		AdjustAccount adjustAccount =new AdjustAccount();
		String id = params.get("adjustId");
		List<SysDict> balanceFromList = null;
		List<SysDict> sysAccountFalgList = null;
		List<SysDict> adjustAccountStatusList = null;
		try {
			 balanceFromList = sysDictService.findSysDictGroup("adjust_amount_from");
			 sysAccountFalgList = sysDictService.findSysDictGroup("sys_account_falg");
			 adjustAccount = adjustAccountService.getAdjustAccount(Integer.parseInt(id));
			 adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("balanceFromList", balanceFromList);
		model.put("sysAccountFalgList", sysAccountFalgList);
		model.put("adjustAccount", adjustAccount);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "adjust/examineList";		
	}
	//调账列表
	@PreAuthorize("hasAuthority('adjustExamine:query')")
	@RequestMapping(value="/adjustRecordListQuery.do")
	public String adjustRecordListQuery(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountTypeList = null;
		List<SysDict> adjustAccountStatusList = null;
		try {
			adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		//获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		model.put("userName", userInfo.getUsername()) ;
		return "adjust/adjustRecordListQuery";
	}
	@PreAuthorize("hasAuthority('adjustExamine:examine')")
	@RequestMapping(value="/adjustRecordDetail.do")
	public String adjustRecordDetail(@ModelAttribute AdjustAccount adjustAccount,ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountTypeList = null;//调账类型：1--长款，2--短款，3--其他
		List<SysDict> adjustAccountStatusList = null;//状态：0--待提交，1--待审核，2--审核通过，-1--审核不通过，3--已记账，4--记账失败
		List<SysDict> balanceFromList = null;//借代方向：debit-借方,credit-贷方
		List<SysDict> accountTypeList = null;//外部账用户类型
		List<SysDict> accountFlagList = null;//账号方向：1:内部账户，0：外部账户
		List<OrgInfo> orgInfoList = null ;//账户归属
		List<Currency> currencyList =null ;//币种号
		try {
			adjustAccount = adjustAccountService.getAdjustAccount(adjustAccount.getId());
			adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
			balanceFromList = sysDictService.findSysDictGroup("adjust_amount_from");
			accountTypeList = sysDictService.findSysDictGroup("sys_account_type");
			accountFlagList = sysDictService.findSysDictGroup("sys_account_falg");
			orgInfoList = orgInfoService.findOrgInfo() ;
			currencyList = currencyService.findCurrency() ;
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccount", adjustAccount);
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		model.put("balanceFromList", balanceFromList);
		model.put("accountTypeList", accountTypeList);
		model.put("accountFlagList", accountFlagList);
		model.put("orgInfoList", orgInfoList);
		model.put("currencyList", currencyList);
		return "adjust/adjustRecordDetail";
	}
	
	//调账明细
	@PreAuthorize("hasAuthority('adjustExamine:query')")
	@RequestMapping(value="/adjustExamine.do")
	public String adjustExamine(ModelMap model){
		List<SysDict> adjustAccountStatusList = null;
		try {
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}

		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "adjust/adjustExamine";
	}
	//下载模板
	@PreAuthorize("hasAuthority('adjustAdd:downloadTpl')")
	@RequestMapping(value="/downloadAdjustAccTemplate.do")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.ADJUST_ACCOUNT_TEMPLATE;
		DownloadUtil.download(response, filePath,"调账明细模板.xls");
		return null;
	}
	//下载模板
	@PreAuthorize("hasAuthority('adjustRecordListQuery:download')")
	@RequestMapping(value="/downloadAdjustAccFile.do")
	public String downloadAdjustAccFile(@ModelAttribute AdjustAccount adjustAccount,HttpServletRequest request, HttpServletResponse response){
		try {
			adjustAccount = adjustAccountService.getAdjustAccount(adjustAccount.getId());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		String filePath = adjustAccount.getFilePath();
		DownloadUtil.download(response, filePath,"调账明细.xls");
		return null;
	}
	
	@PreAuthorize("hasAuthority('adjustAdd:insert')")
	@ResponseBody
	@RequestMapping (value="/insertAdjust.do", method = RequestMethod.POST) 
	public Map<String,Object> insertAdjust(HttpServletRequest request,@ModelAttribute AdjustAccount adjustAccount) {
			Map<String,Object> result=new HashMap<String, Object>();
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("fileupload");
			MultipartFile file=files.get(0);
			if(adjustAccount.getApprover() == null || "".equals(adjustAccount.getApprover())){
				result.put("statu",false);
				result.put("msg","请选择审核人");
				log.info(result.toString());
				return result;
			}
			
			//保存单据
			try {
				//保存模板路径
				File newFile=new File(Constants.UPLOAD_TEMPLATE+UUID.randomUUID().toString()+".xls");
				File parentFile =newFile.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
				}
				if(!newFile.exists())
					newFile.createNewFile();
				file.transferTo(newFile);//接收文件
				if(!checkFileType(file)){
					result.put("statu",false);
					result.put("msg","调账文件类型不正确！请上传.xls类型文件！");
					log.info(result.toString());
					return result;
				}
				//获取到登录者信息
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext()
					    .getAuthentication()
					    .getPrincipal();
				adjustAccount.setFilePath(newFile.getAbsolutePath());
				adjustAccount.setStatus(0);			//状态：0待提交，1待审批，2审批通过，3审批不通过，4已记账，5记账失败
				adjustAccount.setApplicant(userInfo.getUsername());		//系统登录者名称
				adjustAccount.setApplicantTime(new Date());
				String msg = adjustAccountService.insertAdjust(adjustAccount, newFile);
				if(msg == null){
					result.put("statu",true);
					result.put("msg","保存成功");
					log.info(result.toString());
				}else{
					result.put("statu",false);
					result.put("msg",msg);
					log.info(result.toString());
				}
				
			}catch(Exception e){
				result.put("statu",false);
				result.put("msg",e.getMessage());
				log.error(result.toString());
				log.error(e.getMessage());
			}
		return result;
	}		
	
	@PreAuthorize("hasAuthority('adjustRecordListQuery:update')")
	@ResponseBody
	@RequestMapping (value="/updateAdjust.do", method = RequestMethod.POST) 
	public Map<String,Object> updateAdjust(HttpServletRequest request,@ModelAttribute AdjustAccount adjustAccount) {
			Map<String,Object> result=new HashMap<String, Object>();
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			List<MultipartFile> files = multipartRequest.getFiles("fileupload");
			MultipartFile file=files.get(0);
			if(adjustAccount.getApprover() == null || "".equals(adjustAccount.getApprover())){
				result.put("statu",false);
				result.put("msg","请指定审核人！");
				log.info(result.toString());
				return result;
			}
			
			//保存单据
			try {
				//保存模板路径
				File newFile=new File(Constants.UPLOAD_TEMPLATE+UUID.randomUUID().toString()+".xls");
				File parentFile =newFile.getParentFile();
				if(!parentFile.exists()){
					parentFile.mkdirs();
				}
				if(!newFile.exists())
					newFile.createNewFile();
				file.transferTo(newFile);//接收文件
				if(!checkFileType(file)){
					result.put("statu",false);
					result.put("msg","调账文件类型不正确！请上传.xls类型文件！");
					log.info(result.toString());
					return result;
				}
				adjustAccount.setFilePath(newFile.getAbsolutePath());
				String msg = adjustAccountService.updateAdjust(adjustAccount, newFile);
				if(msg == null){
					result.put("statu",true);
					result.put("msg","修改成功");
					log.info(result.toString());
				}else{
					result.put("statu",false);
					result.put("msg",msg);
					log.info(result.toString());
				}
				
			}catch(Exception e){
				result.put("statu",false);
				result.put("msg",e.getMessage());
				log.error(result.toString());
				log.error(e.getMessage());
			}
		return result;
	}	
	
	/**
	 * 判断上传文件类型
	 * @param items
	 * @return 
	 */
	private Boolean checkFileType(MultipartFile file){
		String filename = file.getOriginalFilename();  
		String extName = filename.substring(filename.lastIndexOf(".")).toLowerCase(); 
		if (!(".xls".equals(extName))) {
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "findAdjustAccount.do")
	@ResponseBody
	public Page<AdjustAccount> findAdjustAccount(@ModelAttribute AdjustAccount adjustAccount,@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<AdjustAccount> page){
		//获取到登录者信息
		UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			adjustAccount.setStatus(1);
			adjustAccount.setApprover(userInfo.getUsername());
			adjustAccountService.findAdjustAccountApprove(adjustAccount,params,sort,page);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		System.out.println(page);
		return page;
	}
	
	@RequestMapping(value = "findAdjustRecord.do")
	@ResponseBody
	public Page<AdjustAccount> findAdjustRecord(@ModelAttribute("adjustAccount")AdjustAccount adjustAccount,@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<AdjustAccount> page){
		try {
			adjustAccountService.findAdjustAccount(adjustAccount,params,sort,page);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}
	
	@RequestMapping(value = "findAdjustDetail.do")
	@ResponseBody
	public Page<AdjustDetail> findAdjustDetail(@ModelAttribute AdjustDetail adjustDetail,@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<AdjustDetail> page){
		try {
			log.info("审核时进入控制层");
			//String id = params.get("id");
			//adjustDetail.setAdjustId(Integer.parseInt(id));
			adjustAccountService.findAdjustDetail(adjustDetail,params,sort,page);
		} catch (Exception e) {
			log.error("异常:",e); 
		}	
		System.out.println(page);
		return page;
	}
	
	
	//提交审核
	@RequestMapping(value= "/updateRecordExamine.do")
	@ResponseBody
	public  Map<String,Object> updateRecordExamine(@ModelAttribute AdjustAccount adjustAccount,@RequestParam Map<String, String> params){
			log.info("进入审核通过不通过控制层");
			Map<String,Object> msg=new HashMap<>();

			String id = params.get("id");	
			adjustAccount.setStatus(1);
			//adjustAccount.setApprover("admin");
			try
				{
			int i =	adjustAccountService.updateadjustExamineDate(adjustAccount);
			 if(i>0){
				 msg.put("state",true);
				 msg.put("msg", "提交审核成功!");
				 log.info(msg.toString());
			 }
			} catch (Exception e) {
				 msg.put("msg","提交审核失败！");
				 log.error(msg.toString());
				log.error("异常:",e);
				log.error("提交审核失败！",e);
			}
		 return msg;
	}
	

	
	//审核
	@RequestMapping(value= "/updateAdjustExamine.do")
	@ResponseBody
	public  Map<String,Object> updateAdjustExamine(@ModelAttribute AdjustAccount adjustAccount,@RequestParam Map<String, String> params){
			log.info("进入审核通过不通过控制层");
			Map<String,Object> msg=new HashMap<>();
			String url = "" ;

			String id = params.get("id");	
			String status = params.get("status");
			String approveRemark = params.get("approveRemark");
			log.info(approveRemark);
			//adjustAccount.setApproveTime(new Date()); //更新数据时在dao层已经处理
			
			try{
				msg = adjustAccountService.updateadjustExamine(adjustAccount);
			} catch (Exception e) {
				 log.error("审核失败！",e);
				 msg.put("state", false) ;
				 msg.put("msg","审核失败！");
				 log.error(msg.toString());
				 log.error("异常:",e);
			}
		 return msg;
	}
	
	
	//只修改 AdjustAccount（即点击提交修改按钮而没有上传调账文件时）
		@ResponseBody
		@RequestMapping (value="/updateAdjustAccount.do", method = RequestMethod.POST) 
		public Map<String,Object> updateAdjustAccount(@ModelAttribute AdjustAccount adjustAccount) {
				Map<String,Object> result=new HashMap<String, Object>();
				if(adjustAccount.getApprover() == null || "".equals(adjustAccount.getApprover())){
					result.put("statu",false);
					result.put("msg","请指定审核人！");
					log.info(result.toString());
					return result;
				}
				try{
					String msg = adjustAccountService.updateAdjustAccount(adjustAccount);
					if(msg == null){
						result.put("statu",true);
						result.put("msg","修改成功");
						log.info(result.toString());
					}else{
						result.put("statu",false);
						result.put("msg",msg);
						log.info(result.toString());
					}
					
				}catch(Exception e){
					result.put("statu",false);
					result.put("msg",e.getMessage());
					log.error(result.toString());
					log.error(e.getMessage());
				}
			return result;
		}	
		
	
}
