package cn.eeepay.boss.action;


import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.ExtAccount;
import cn.eeepay.framework.model.bill.ExtUserTypeSubject;
import cn.eeepay.framework.model.bill.InsAccount;
import cn.eeepay.framework.model.bill.Subject;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.service.bill.ExtAccountService;
import cn.eeepay.framework.service.bill.ExtUserTypeSubjectService;
import cn.eeepay.framework.service.bill.InsAccountService;
import cn.eeepay.framework.service.bill.SubjectService;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.util.UrlUtil;

/**
 * 科目控制管理
 * 
 * by zouruijin
 * email rjzou@qq.com zrj@eeepay.cn
 * 2016年5月18日16:49:01
 *
 */
@Controller
@RequestMapping(value = "/subjectAction")
public class SubjectAction {
		@Resource
		public SubjectService subjectService;
		@Resource
		public SysDictService sysDictService;
		@Resource
		public ExtAccountService extAccountService;
		@Resource
		public InsAccountService inputAccountService;
		@Resource
		public ExtUserTypeSubjectService extUserTypeSubjectService;
		
		private static final Logger log = LoggerFactory.getLogger(SubjectAction.class);
		//新增科目page
		@PreAuthorize("hasAuthority('subjectAdd:query')")
		@RequestMapping(value = "/toSubjectAdd.do")
		public String subjectAdd(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
			List<SysDict> subjectType = sysDictService.findSysDictGroup("sys_subject_type");
			model.put("subjectTypes", subjectType);
			
			List<SysDict> subjectLevel = sysDictService.findSysDictGroup("sys_subject_level");
			model.put("subjectLevels", subjectLevel);
			List<SysDict> balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
			model.put("balanceFromList", balanceFromList);

			List<SysDict> innerDayBalFlagList = sysDictService.findSysDictGroup("inner_day_bal_flag");//修改余额标志
			model.put("innerDayBalFlagList", innerDayBalFlagList);
			List<SysDict> innerSumFlagList = sysDictService.findSysDictGroup("inner_sum_flag");//明细处理方式
			model.put("innerSumFlagList", innerSumFlagList);
			
			return "subject/subjectAdd";
		}
		@PreAuthorize("hasAuthority('subjectListInfo:query')")
		@RequestMapping(value = "/toSubjectListInfo.do")
		public String subjectListInfo(ModelMap model, @RequestParam Map<String, String> params) {
			List<SysDict> subjectTypeList = null;
			List<SysDict> subjectLevelList = null;
			List<SysDict> balanceFromList = null;
			List<SysDict> isInnerAccountList = null;
			List<Subject> subjects  = null;
			
			params.put("pageNo", "1");
			params.put("pageSize", "10");
			params.put("sortname", "");
			params.put("sortorder", "");
			
			String queryParams = params.get("queryParams");
			if (StringUtils.isNotBlank(queryParams)) {
				log.info(queryParams);
			    byte[] b = Base64.decode(queryParams.getBytes());
			    String s=new String(b);
			    log.info(s);
			    Map<String, Object> queryParamsMap= UrlUtil.getUrlParams(s);
		    	for (Map.Entry<String, Object> entry : queryParamsMap.entrySet()) {
					log.info("{}={}", new Object[] { entry.getKey(), entry.getValue() });
					params.put(entry.getKey(), entry.getValue().toString());
				}
			}
			try {
				subjectTypeList = sysDictService.findSysDictGroup("sys_subject_type");
				subjectLevelList = sysDictService.findSysDictGroup("sys_subject_level");
				balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
				isInnerAccountList = sysDictService.findSysDictGroup("sys_is_inner_account");
			    subjects = subjectService.findSubjectList();
			} catch (Exception e) {
				log.error("异常:",e);
//				e.printStackTrace();
			}
			model.put("subjectTypeList", subjectTypeList);
			model.put("subjectLevelList", subjectLevelList);
			model.put("balanceFromList", balanceFromList);
			model.put("isInnerAccountList", isInnerAccountList);
			model.put("subjectList", subjects) ;
			model.put("params", params);
			return "subject/subjectListInfo";
		}
		@PreAuthorize("hasAuthority('subjectListInfo:detail')")
		@RequestMapping(value = "/toSubjectDetail.do")
		public String subjectDetail(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
			String subjectNo = params.get("subjectNo");
			Subject subject = subjectService.getSubjectAndParentSubject(subjectNo);
			params.put("subjectNo", subject.getSubjectNo());
			params.put("subjectLevel", subject.getSubjectLevel().toString());
			params.put("subjectName", subject.getSubjectName());
			params.put("subjectAlias", subject.getSubjectAlias()) ;
			String parentSubjectNo = "";
			String parentSubjectName = "";
			if (subject != null && subject.getParentSubject() != null) {
				parentSubjectNo = subject.getParentSubjectNo();
				parentSubjectName = subject.getParentSubject().getSubjectName();
			}
			params.put("parentSubjectNo", parentSubjectNo);
			params.put("parentSubjectName", parentSubjectName);
			
			params.put("subjectType", subject.getSubjectType());
			
			String bf = subject.getBalanceFrom();
			SysDict balanceFrom = sysDictService.findSysDictByKeyValue("sys_balance_from",bf);
			params.put("balanceFrom", balanceFrom.getSysName());//余额方向：debit-借方,credit-贷方
			
			String dcf = subject.getDebitCreditFlag();
			SysDict debitCreditFlag = sysDictService.findSysDictByKeyValue("sys_debit_credit_flag",dcf);
			params.put("debitCreditFlag", debitCreditFlag.getSysName());//参加借贷平衡检查标志：0-没参加，1-参加
			
			String iia = subject.getIsInnerAccount();
			SysDict isInnerAccount = sysDictService.findSysDictByKeyValue("sys_is_inner_account",iia);
			params.put("isInnerAccount", isInnerAccount.getSysName());//是否开立内部账户：0-否，1-是
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
			params.put("creator", subject.getCreator());
			params.put("createTime",subject.getCreateTime()==null?"":sdf.format(subject.getCreateTime()));
			
			params.put("updator", subject.getUpdator());
			params.put("updateTime",subject.getUpdateTime()==null?"":sdf.format(subject.getUpdateTime()));
			//获取科目类别的文本
			String st = subject.getSubjectType();
			SysDict subjectType = sysDictService.findSysDictByKeyValue("sys_subject_type",st);
			
			params.put("subjectTypeName", subjectType.getSysName());
			//获取科目级别的文本
			String sl = subject.getSubjectLevel().toString();
			SysDict subjectLevel = sysDictService.findSysDictByKeyValue("sys_subject_level",sl);
			params.put("subjectLevelName", subjectLevel.getSysName());
			
			//修改余额标志
			SysDict innerDayBalFlag = sysDictService.findSysDictByKeyValue("inner_day_bal_flag",subject.getInnerDayBalFlag());
			params.put("innerDayBalFlag", innerDayBalFlag.getSysName());

			//明细处理方式
			SysDict innerSumFlag = sysDictService.findSysDictByKeyValue("inner_sum_flag",subject.getInnerSumFlag());
			params.put("innerSumFlag", innerSumFlag.getSysName());
			
			
			
			model.put("params", params);
			return "subject/subjectDetail";
		}
		
		
		/**
		 * 维护科目信息
		 * @return
		 */
		@PreAuthorize("hasAuthority('subjectAdd:update')")
		@RequestMapping(value = "/toSubjectUpdate.do")
		public String maintenanceSunbjectInfo(ModelMap model, @RequestParam Map<String, String> params)throws Exception{	
				List<SysDict> balanceFromList = null;
				balanceFromList = sysDictService.findSysDictGroup("sys_balance_from");
				List<SysDict> subjectType = sysDictService.findSysDictGroup("sys_subject_type");
				model.put("subjectTypes", subjectType);		
				List<SysDict> subjectLevel = sysDictService.findSysDictGroup("sys_subject_level");
				model.put("subjectLevels", subjectLevel);	

				List<SysDict> innerDayBalFlagList = sysDictService.findSysDictGroup("inner_day_bal_flag");//修改余额标志
				model.put("innerDayBalFlagList", innerDayBalFlagList);
				List<SysDict> innerSumFlagList = sysDictService.findSysDictGroup("inner_sum_flag");//明细处理方式
				model.put("innerSumFlagList", innerSumFlagList);
				
				String subjectNo = params.get("subjectNo");
				System.out.println("------------------------------");
				List<Subject> subjects = subjectService.findSubjectList();
				Subject subject = subjectService.getSubject(subjectNo);
	//			params.put("subjectLegalNo", subject.getSubjectLegalNo());
				params.put("subjectNo", subject.getSubjectNo());
				params.put("subjectLevel", subject.getSubjectLevel().toString());
				params.put("subjectName", subject.getSubjectName());
				params.put("subjectType", subject.getSubjectType());
				params.put("parentSubjectNo", subject.getParentSubjectNo());
				params.put("balanceFrom", subject.getBalanceFrom());
				params.put("isInnerAccount", subject.getIsInnerAccount());
				params.put("debitCreditFlag", subject.getDebitCreditFlag());
				params.put("subjectAlias", subject.getSubjectAlias()) ;
				model.put("subjectList", subjects);
				model.put("balanceFromList", balanceFromList);			
				model.put("params", params);
				return "subject/subjectUpdate";				
		}
		
		/**
		 * 科目信息保存
		 * @param subject
		 * @return
		 */
		@PreAuthorize("hasAuthority('subjectAdd:insert')")
		@RequestMapping(value = "/saveSubjectInfo.do")
		@Logs(description="科目信息保存")
		@ResponseBody
		public Map<String,Object> SaveSubjectInfo(@ModelAttribute Subject subject){		
			Map<String,Object> msg=new HashMap<>();
			if (StringUtils.isBlank(subject.getParentSubjectNo())) {
				subject.setSubjectLevel(1);
			}
			else{
				String psNo = subject.getParentSubjectNo();
				try {
					Subject sub = subjectService.getSubject(psNo);
					subject.setSubjectLevel(sub.getSubjectLevel() + 1 );
				} catch (Exception e) {
					 log.error("查询上级科目级别报错",e);
					 msg.put("msg","查询上级科目级别报错！");
					 log.error(msg.toString());
					 return msg;
				}
				
			}
			
			//获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			subject.setCreator(userInfo.getUsername());//创建者
			//subject.setCreateTime(new Date());//创建时间       dao层处理
			try{
				//查询科目名称是否为空
				if(StringUtils.isBlank(subject.getSubjectName()) && "".equals(subject.getSubjectName())){
					//msg.put("state",false);
					msg.put("msg", "科目名称不能为空!");
					log.info(msg.toString());
					return msg ;
				}
				 //查询科目别名是否存在
				if(!StringUtils.isBlank(subject.getSubjectAlias()) && !"".equals(subject.getSubjectAlias())){
					Subject subjectQ = subjectService.findSubjectAliasExist(subject.getSubjectAlias()) ;
					if(subjectQ != null){
						msg.put("state",false);
						msg.put("msg", "新增失败！科目别名已存在!");
						log.info(msg.toString());
						return msg ;
					}
				}else{
					//msg.put("state",false);
					msg.put("msg", "科目别名不能为空!");
					log.info(msg.toString());
					return msg ;
				}
				 msg =subjectService.insertSubject(subject);
			}catch(Exception e){
				 log.error("新增科目保存失败！",e);
				 msg.put("msg","新增科目保存失败！");
				 log.error(msg.toString());
			}
			return msg;
		}
		@PreAuthorize("hasAuthority('subjectListInfo:query')")
		@RequestMapping(value = "findSubjectListInfo.do",method = RequestMethod.POST)
		@ResponseBody
		public Page<Subject> findSubjectListInfo(@ModelAttribute Subject subject,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<Subject> page){
			try {
				subjectService.findSubjectListInfo(subject,sort,page);
			} catch (Exception e) {
				log.error("异常:",e);
//				e.printStackTrace();
			}	
			return page;
		}
		
		@RequestMapping(value = "queryParentSubjectName.do")
		@ResponseBody
		public List<Map<String, String>> queryParentSubjectName(String q) throws Exception {
			System.out.println(" queryParentSubjectName testJson");
			q = URLDecoder.decode(q, "UTF-8");
			log.info("queryParentSubjectName:"+q);
			Subject subject = new Subject();
			subject.setSubjectNo(q);
			subject.setSubjectName(q);
			List<Subject> subjectList = null;
			List<Map<String, String>> maps = new ArrayList<>();
			try {
				subjectList = subjectService.findSelectSubject(subject);
			} catch (Exception e) {
				log.error("异常:",e);
//				e.printStackTrace();
			}	
			Map<String, String> thenMap = null;
			for (Subject s : subjectList) {
				thenMap = new HashMap<String, String>();
				thenMap.put("id", s.getSubjectNo());
				thenMap.put("text", s.getSubjectName()); //+"("+s.getSubjectNo()+")")
				maps.add(thenMap);
			}
			return maps;
		}
		//进行更新操作
		@PreAuthorize("hasAuthority('subjectAdd:update')")
		@RequestMapping(value = "/subjectUpdate.do")
		@ResponseBody
		public Map<String,Object> subjectUpdate(ModelMap model, @RequestParam Map<String, String> params,@ModelAttribute Subject subject) throws Exception{
		
			Map<String,Object> msg=new HashMap<>();
			String subjectNo = subject.getSubjectNo();
			subject.setSubjectAlias(subject.getSubjectAlias().trim());
			subject.setSubjectName(subject.getSubjectName().trim());

			//获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			try{
				 Subject sub = subjectService.getSubject(subjectNo);

				 //查询科目名称是否为空
				if(StringUtils.isBlank(subject.getSubjectName()) && "".equals(subject.getSubjectName())){
					//msg.put("state",false);
					msg.put("msg", "科目名称不能为空!");
					log.info(msg.toString());
					return msg ;
				}
				 //查询科目别名是否存在
				if(!StringUtils.isBlank(subject.getSubjectAlias()) && !"".equals(subject.getSubjectAlias())){
					if(!subject.getSubjectAlias().equalsIgnoreCase(sub.getSubjectAlias())){
						Subject subjectQ = subjectService.findSubjectAliasExist(subject.getSubjectAlias()) ;
						if(subjectQ != null){
							msg.put("state",false);
							msg.put("msg", "新增失败！科目别名已存在!");
							log.info(msg.toString());
							return msg ;
						}
					}
				}else{
					//msg.put("state",false);
					msg.put("msg", "科目别名不能为空!");
					log.info(msg.toString());
					return msg ;
				}
				 sub.setSubjectName(subject.getSubjectName());
				 sub.setSubjectLevel(subject.getSubjectLevel());
				 sub.setSubjectType(subject.getSubjectType());
				 sub.setParentSubjectNo(subject.getParentSubjectNo());
				 sub.setBalanceFrom(subject.getBalanceFrom());
				 sub.setDebitCreditFlag(subject.getDebitCreditFlag());
				 sub.setIsInnerAccount(subject.getIsInnerAccount());
				 sub.setUpdator(userInfo.getUsername());//修改者
				//sub.setUpdateTime(new Date());//修改时间       dao层处理
				 sub.setSubjectAlias(subject.getSubjectAlias());//别名
				 sub.setInnerSumFlag(subject.getInnerSumFlag());//明细处理方式
				 sub.setInnerDayBalFlag(subject.getInnerDayBalFlag());//修改余额标志

				 msg =subjectService.updateSubject(sub);
			}catch(Exception e){
				 log.error("修改科目失败！",e);
				 msg.put("msg","修改科目失败！");
				 log.error(msg.toString());
			}
			log.info(msg.toString());
			return msg;
	
		}
	
		@PreAuthorize("hasAuthority('subjectAdd:delete')")
		@RequestMapping(value = "/deleteSubjectById.do")
		@Logs(description="删除科目")
		@ResponseBody
		public Map<String,Object> deleteSubjectById(ModelMap model, @RequestParam Map<String, String> params,@ModelAttribute Subject subject) throws Exception{
		
			Map<String,Object> msg=new HashMap<>();
			Integer id = subject.getId();
			
			try{
				 subject = subjectService.getSubjectById(id);
				 String subjectNo = subject.getSubjectNo();
				 List<ExtAccount> outAccountList  = extAccountService.findExtAccountBySubjectNo(subjectNo);
				 if (outAccountList.size() > 0) {
					 msg.put("state",false);
					 msg.put("msg", "删除科目失败,科目已经被使用!");
					 log.info(msg.toString());
					 return msg;
				 }
				 List<InsAccount> inputAccountList  = inputAccountService.findInputAccountBySubjectNo(subjectNo);
				 if (inputAccountList.size() > 0) {
					 msg.put("state",false);
					 msg.put("msg", "删除科目失败,科目已经被使用!");
					 log.info(msg.toString());
					 return msg;
				 }
				 int i =subjectService.deleteSubjectById(id);
				 if(i>0){
					 msg.put("state",true);
					 msg.put("msg", "删除成功!");
					 log.info(msg.toString());
				 }
			}catch(Exception e){
				 log.error("删除科目失败！",e);
				 msg.put("msg","删除科目失败！");
				 log.error(msg.toString());
			}
			return msg;
		}
		//新增用户类型科目关联page
		@PreAuthorize("hasAuthority('userTypeSubjectAdd:query')")
		@RequestMapping(value = "/toUserTypeSubjectAdd.do")
		public String userTypeSubjectAdd(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
			List<SysDict> userType = sysDictService.findSysDictGroup("sys_account_type");
			model.put("userTypes", userType);
			return "subject/userTypeSubjectAdd";
		}
		/**
		 * 保存用户类型科目关联
		 * @param subjectExt
		 * @return
		 */
		@PreAuthorize("hasAuthority('userTypeSubjectAdd:insert')")
		@RequestMapping(value = "/saveUserTypeSubject.do")
		@Logs(description="保存用户类型科目关联")
		@ResponseBody
		public Map<String,Object> saveUserTypeSubject(@ModelAttribute ExtUserTypeSubject subjectExt){	
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String,Object> msg=new HashMap<>();
			String userTypeValue = subjectExt.getUserType() ;
			String subjectNo = subjectExt.getSubjectNo() ;
			try{
				ExtUserTypeSubject querySubjectExt = extUserTypeSubjectService.existExtUserTypeSubject(userTypeValue,subjectNo) ;
				 if(querySubjectExt!=null){
					 msg.put("msg", "该条关联已存在!");
					 return msg ;
				 }
			}catch(Exception e){
				log.error("验证用户类型科目关联是否存在失败！",e);
				msg.put("msg","验证用户类型科目关联是否存在失败！");
				log.error(msg.toString());
				return msg ;
			}
			subjectExt.setCreator(userInfo.getUsername());
			try{
				 int i = extUserTypeSubjectService.insertExtUserTypeSubject(subjectExt);
				 if(i>0){
					 msg.put("state",true);
					 msg.put("msg", "保存成功!");
				 }
			}catch(Exception e){
				 log.error("新增用户类型科目关联保存失败！",e);
				 //msg.put("state",false);
				 msg.put("msg","新增用户类型科目关联保存失败！");
				 log.error(msg.toString());
			}
			return msg;
		}
		//用户类型科目关联列表查询page
		@PreAuthorize("hasAuthority('userTypeSubjectList:query')")
		@RequestMapping(value = "/toUserTypeSubjectList.do")
		public String toUserTypeSubjectList(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
			List<SysDict> userType = sysDictService.findSysDictGroup("sys_account_type");
			model.put("userTypes", userType);
			return "subject/userTypeSubjectList";
		}
		@PreAuthorize("hasAuthority('userTypeSubjectList:query')")
		@RequestMapping(value = "/findUserTypeSubjectList.do")
		@ResponseBody
		public Page<ExtUserTypeSubject> findUserTypeSubjectList(@ModelAttribute ExtUserTypeSubject subjectExt,@ModelAttribute("sort")Sort sort,@ModelAttribute("page")Page<ExtUserTypeSubject> page){
			try {
				extUserTypeSubjectService.findUserTypeSubjectList(subjectExt,sort,page);
			} catch (Exception e) {
				log.error("异常:",e);
//				e.printStackTrace();
			}	
			return page;
		}
		@PreAuthorize("hasAuthority('userTypeSubjectList:delete')")
		@RequestMapping(value = "/deleteUserTypeSubject.do")
		@Logs(description="删除用户类别科目关联")
		@ResponseBody
		public Map<String,Object> deleteUserTypeSubject(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
			Map<String,Object> msg=new HashMap<>();
			String id = params.get("id") ;
			try{
				 int i = extUserTypeSubjectService.deleteUserTypeSubject(id);
				 if(i>0){
					 msg.put("state",true);
					 msg.put("msg", "删除用户类别科目关联成功!");
					 log.info(msg.toString());
				 }
			}catch(Exception e){
				 log.error("删除用户类别科目关联失败！",e);
				 msg.put("state",false);
				 msg.put("msg","删除用户类别科目关联失败！");
				 log.error(msg.toString());
			}
			return msg;
		}
}
