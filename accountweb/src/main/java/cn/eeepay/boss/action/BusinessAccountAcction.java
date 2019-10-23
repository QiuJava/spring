package cn.eeepay.boss.action;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import cn.eeepay.framework.model.bill.BusinessAccount;
import cn.eeepay.framework.model.bill.BusinessAccountDetail;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.BusinessAccountService;
import cn.eeepay.framework.service.bill.SysDictService;

/**
 * 业务调账
 * @author yl
 *
 */
@Controller
@RequestMapping("/business")
public class BusinessAccountAcction {
	private static final Logger log = LoggerFactory.getLogger(BusinessAccountAcction.class);
	@Autowired
	private BusinessAccountService businessAccountService;
	@Autowired
	private SysDictService sysDictService;

	//记账导入界面adjustAdd:query
	@PreAuthorize("hasAuthority('businessAdd:query')")
	@RequestMapping(value="/businessAdd.do")
	public String businessAdd(ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountTypeList = null;
		try {
			adjustAccountTypeList = sysDictService.findSysDictGroup("sys_adjust_account_type");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccountTypeList", adjustAccountTypeList);
		return "business/adjustAdd";
	}

	//调账列表
	@PreAuthorize("hasAuthority('businessRecordListQuery:query')")
	@RequestMapping(value="/businessRecordListQuery.do")
	public String businessRecordListQuery(ModelMap model, @RequestParam Map<String, String> params){
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
		return "business/adjustRecordListQuery";
	}

	@PreAuthorize("hasAuthority('businessRecordListQuery:query')")
	@RequestMapping(value = "findBusinessAccountRecord.do")
	@ResponseBody
	public Page<BusinessAccount> findBusinessAccountRecord(@ModelAttribute("account")BusinessAccount adjustAccount,@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<BusinessAccount> page){
		try {
			String beginDate = params.get("beginDate");
			String endDate = params.get("endDate");
			if (beginDate != null && StringUtils.isNotBlank(beginDate)) {
				beginDate += " 00:00:00";
				params.put("beginDate", beginDate);
			}
			if (endDate != null && StringUtils.isNotBlank(endDate)) {
				endDate += " 23:59:59";
				params.put("endDate", endDate);
			}

			businessAccountService.findBusinessAccount(adjustAccount,params,sort,page);
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		return page;
	}

	//调账明细
	@PreAuthorize("hasAuthority('businessExamine:query')")
	@RequestMapping(value="/businessExamine.do")
	public String businessExamine(ModelMap model){
		List<SysDict> adjustAccountStatusList = null;
		try {
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "business/adjustExamine";
	}


	@PreAuthorize("hasAuthority('businessRecordListQuery:query')")
	@RequestMapping(value="/businessRecordDetail.do")
	public String businessRecordDetail(@ModelAttribute BusinessAccount account,ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountStatusList = null;//状态：0待提交，1待审批，2审批通过，3审批不通过，4已记账，5记账失败
		try {
			account = businessAccountService.getBusinessAccount(account.getId());
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccount", account);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "business/adjustRecordDetail";
	}
	
	@PreAuthorize("hasAuthority('businessRecordListQuery:query')")
	@RequestMapping(value="/businessRecordExamine.do")
	public String businessRecordExamine(@ModelAttribute BusinessAccount account,ModelMap model, @RequestParam Map<String, String> params){
		List<SysDict> adjustAccountStatusList = null;//状态：0待提交，1待审批，2审批通过，3审批不通过，4已记账，5记账失败
		try {
			account = businessAccountService.getBusinessAccount(account.getId());
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("异常 " + e.getMessage());
		}
		model.put("adjustAccount", account);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "business/adjustRecordExamine";
	}
	

	//下载模板
	@PreAuthorize("hasAuthority('businessAdd:downloadTpl')")
	@RequestMapping(value="/downloadAdjustAccTemplate.do")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getSession().getServletContext().getRealPath("/")+ Constants.BUSINESS_TEMPLATE;
		DownloadUtil.download(response, filePath,"调账明细模板.xls");
		return null;
	}

	/**
	 * 新增调账提交
	 * @param request
	 * @param adjustAccount
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessAdd:insert')")
	@ResponseBody
	@RequestMapping (value="/insertAdjust.do", method = RequestMethod.POST) 
	public Map<String,Object> insertAdjust(HttpServletRequest request,@ModelAttribute BusinessAccount adjustAccount) {
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
			adjustAccount.setStatus(0);			//状态：0--待提交，1--待审核，2--审核通过，3--审核不通过，4--已记账，5--记账失败
			adjustAccount.setApplicant(userInfo.getUsername());		//系统登录者名称
			adjustAccount.setApplicantTime(new Date());
			String msg = businessAccountService.insertBusinessAccount(adjustAccount, newFile);
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

	/**
	 * 进入业务调账修改页面
	 * @param adjustAccount
	 * @param model
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessRecordListQuery:update')")
	@RequestMapping(value="/businessUpdate.do")
	public String businessUpdate(@ModelAttribute BusinessAccount adjustAccount,ModelMap model, @RequestParam Map<String, String> params){
		try {
			adjustAccount = businessAccountService.getBusinessAccount(adjustAccount.getId());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("adjustAccount", adjustAccount);
		return "business/adjustUpdate";
	}

	/**
	 * 业务调账更新下载模板文件
	 * @param adjustAccount
	 * @param request
	 * @param response
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessRecordListQuery:download')")
	@RequestMapping(value="/downloadBusinessAccFile.do")
	public String downloadBusinessAccFile(@ModelAttribute BusinessAccount adjustAccount,HttpServletRequest request, HttpServletResponse response){
		try {
			adjustAccount = businessAccountService.getBusinessAccount(adjustAccount.getId());
		} catch (Exception e) {
			log.error("异常:",e);
		}
		String filePath = adjustAccount.getFilePath();
		DownloadUtil.download(response, filePath,"调账明细.xls");
		return null;
	}

	//只修改 AdjustAccount（即点击提交修改按钮而没有上传调账文件时）
	@ResponseBody
	@RequestMapping (value="/updateBusinessAccount.do", method = RequestMethod.POST) 
	public Map<String,Object> updateBusinessAccount(@ModelAttribute BusinessAccount adjustAccount) {
		Map<String,Object> result=new HashMap<String, Object>();
		if(adjustAccount.getApprover() == null || "".equals(adjustAccount.getApprover())){
			result.put("statu",false);
			result.put("msg","请指定审核人！");
			log.info(result.toString());
			return result;
		}
		try{
			String msg = businessAccountService.updateBusinessAccount(adjustAccount);
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
			log.info(result.toString());
			log.error(e.getMessage());
		}
		return result;
	}	

	/**
	 * 业务调账更新操作
	 * @param request
	 * @param adjustAccount
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessRecordListQuery:update')")
	@ResponseBody
	@RequestMapping (value="/updateBusiness.do", method = RequestMethod.POST) 
	public Map<String,Object> updateBusiness(HttpServletRequest request,@ModelAttribute BusinessAccount adjustAccount) {
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
			String msg = businessAccountService.updateBusiness(adjustAccount, newFile);
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
	 * 查询业务调账详情
	 * @param adjustDetail
	 * @param params
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessRecordListQuery:query')")
	@RequestMapping(value = "findBusinessDetail.do")
	@ResponseBody
	public Page<BusinessAccountDetail> findBusinessDetail(@ModelAttribute BusinessAccountDetail adjustDetail,
			@RequestParam Map<String, String> params ,@ModelAttribute("sort")Sort sort,
			@ModelAttribute("page")Page<BusinessAccountDetail> page){
		try {
			businessAccountService.findBusinessAccountDetail(adjustDetail,params,sort,page);
		} catch (Exception e) {
			log.error("异常:",e); 
		}	
		return page;
	}

	//提交审核
	@PreAuthorize("hasAuthority('businessRecordListQuery:submitExamine')")
	@RequestMapping(value= "/updateRecordExamine.do")
	@ResponseBody
	public  Map<String,Object> updateRecordExamine(@ModelAttribute BusinessAccount adjustAccount,@RequestParam Map<String, String> params){
		Map<String,Object> msg=new HashMap<>();
		adjustAccount.setStatus(1);  //待审核
		try
		{
			int i =	businessAccountService.updateBusinessExamineDate(adjustAccount);
			if(i>0){
				msg.put("state",true);
				msg.put("msg", "提交审核成功!");
				log.info(msg.toString());
			}
		} catch (Exception e) {
			msg.put("msg","提交审核失败！");
			log.info(msg.toString());
			log.error("异常:",e);
			log.error("提交审核失败！",e);
		}
		return msg;
	}

	/**
	 * 进入审核列表页
	 * @param model
	 * @param params
	 * @return
	 */
	@PreAuthorize("hasAuthority('businessExamine:query')")
	@RequestMapping(value="/examineList.do")
	public String examineList(ModelMap model,@RequestParam Map<String, String> params){
		BusinessAccount adjustAccount = new BusinessAccount();
		String id = params.get("adjustId");
		List<SysDict> balanceFromList = null;
		List<SysDict> sysAccountFalgList = null;
		List<SysDict> adjustAccountStatusList = null;
		try {
			balanceFromList = sysDictService.findSysDictGroup("adjust_amount_from");
			sysAccountFalgList = sysDictService.findSysDictGroup("sys_account_falg");
			adjustAccount = businessAccountService.getBusinessAccount(Integer.parseInt(id));
			adjustAccountStatusList = sysDictService.findSysDictGroup("sys_adjust_account_status");
		} catch (Exception e) {
			log.error("异常:",e);
		}
		model.put("balanceFromList", balanceFromList);
		model.put("sysAccountFalgList", sysAccountFalgList);
		model.put("adjustAccount", adjustAccount);
		model.put("adjustAccountStatusList", adjustAccountStatusList);
		return "business/examineList";		
	}

	//审核
	@RequestMapping(value= "/updateBusinessExamine.do")
	@ResponseBody
	public  Map<String,Object> updateBusinessExamine(@ModelAttribute BusinessAccount adjustAccount,@RequestParam Map<String, String> params){
		Map<String,Object> msg=new HashMap<>();
		String url = "" ;

		String id = params.get("id");	
		String status = params.get("status");
		String approveRemark = params.get("approveRemark");

		try{
			msg = businessAccountService.updateBusinessExamine(adjustAccount);
		} catch (Exception e) {
			msg.put("state", false) ;
			msg.put("msg","审核失败！");
			log.info(msg.toString());
			log.error("审核失败！",e);
			log.error("异常:",e);
		}
		return msg;
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
}
