package cn.eeepay.boss.action;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import cn.eeepay.boss.annotation.Logs;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.bill.UserInfo;
import cn.eeepay.framework.model.peragent.PaBrand;
import cn.eeepay.framework.model.peragent.PaShareDetail;
import cn.eeepay.framework.service.bill.SysDictService;
import cn.eeepay.framework.service.peragent.PaShareDetailAccountService;
import cn.eeepay.framework.service.peragent.PaShareDetailService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.ExportFormat;
import cn.eeepay.framework.util.NewListDataExcelExport;

/**
 * 超级盟主分润入账
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/peragentAtion")
public class PeragentAtion {	
	
	
	private static final Logger log = LoggerFactory.getLogger(PeragentAtion.class);
	@Resource
	public SysDictService sysDictService;

	@Resource
	public PaShareDetailService paShareDetailService;
	
	@Resource
	public PaShareDetailAccountService paShareDetailAccountService;
	/**
	 * 超级盟主报表
	 * 
	 * @param model
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('peragentAtion:query')")
	@RequestMapping(value = "/toPeragent.do")
	public String toPeragent(ModelMap model, @RequestParam Map<String, String> params) throws Exception{
		log.info("进入超级盟主报表");
		// 用户类别
		List<SysDict> userTypeList = null;
		// 分润类别
		List<SysDict> shareTypeList = null;
		// 所属品牌
		List<SysDict> enterAccountStatusList = null;
		// 入账状态
		List<PaBrand> brandTypeList = null;
		try {
			enterAccountStatusList = sysDictService.findSysDictGroup("enter_account_status");
			
			userTypeList  = sysDictService.findSysDictGroup("peragent_user_type");
			
			shareTypeList  = sysDictService.findSysDictGroup("peragent_share_type");
			
			brandTypeList  = paShareDetailService.findPaBrandList();
			
		} catch (Exception e) {
			log.error("进入超级盟主报表异常:", e);
		}
		model.put("userTypeList", userTypeList);
		model.put("shareTypeList", shareTypeList);
		model.put("brandTypeList", brandTypeList);
		model.put("enterAccountStatusList", enterAccountStatusList);
		
		model.put("params", params);
		
		return "peragent/peragentMonthSettle";
	}

	/**
	 * 查询超级盟主报表
	 * 
	 * @param bankAccount
	 * @param sort
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('peragentAtion:query')")
	@RequestMapping(value = "/findPeragentList.do")
	@ResponseBody
	public Page<PaShareDetail> findPeragentList(
			@ModelAttribute("paShareDetail") PaShareDetail paShareDetail, @ModelAttribute("sort") Sort sort,
			@ModelAttribute("page") Page<PaShareDetail> page) {
		log.info("进入查询查询超级盟主报表grid--->");
		try {
			paShareDetailService.findPeragentList(paShareDetail, sort, page);
		} catch (Exception e) {
			log.error("进入查询查询超级盟主报表grid异常:",e);
		}	
		return page;
	}
	
    /**
     * 超级盟主 汇总内容
     * @param model
     * @param paShareDetail
     * @param params
     * @return
     */
	@PreAuthorize("hasAuthority('peragentAtion:collectionData')")
	@RequestMapping(value = "/findPeragentListCollection.do")
	@ResponseBody
	public Map<String, Object> findPeragentListCollection(
			ModelMap model,@ModelAttribute("paShareDetail") PaShareDetail paShareDetail, @RequestParam Map<String, String> params) {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			Map<String,Object> map = paShareDetailService.findPeragentListCollection(paShareDetail);
			Object allShareTotalAmount = "0";
			Object allNoEnterShareTotalAmount =  "0";
			Object allAccountedShareTotalAmount =  "0";
			if (map != null) {
				allShareTotalAmount = map.get("allShareTotalAmount");
				allNoEnterShareTotalAmount = map.get("allNoEnterShareTotalAmount");
				allAccountedShareTotalAmount = map.get("allAccountedShareTotalAmount");
			}
			msg.put("allShareTotalAmount", allShareTotalAmount);//累计分润总金额
			msg.put("allNoEnterShareTotalAmount", allNoEnterShareTotalAmount);//已入账
			msg.put("allAccountedShareTotalAmount", allAccountedShareTotalAmount);//未入账
			
		} catch (Exception e) {
			log.error("异常:",e);
		}	
		log.info("超级盟主报表 汇总内容 ---> " + msg.toString());
		return msg;
	}
	
	/**
	 * 单个入账
	 * @param paShareDetail
	 * @return
	 */
	@PreAuthorize("hasAuthority('peragentAtion:singleEnterAccount')")
	@RequestMapping(value = "singleEnterAccount.do")
	@Logs(description="单个超级盟主分润入账")
	@ResponseBody
	public Map<String,Object> singleEnterAccount(@ModelAttribute("paShareDetail") PaShareDetail paShareDetail) {
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if(paShareDetail != null){
			if (paShareDetail.getId()==null) {
				msg.put("msg","入账错误");
				msg.put("status",false);
				isReturn = true;
			}
		}else{
			msg.put("msg","入账错误");
			msg.put("status",false);
			isReturn = true;
		}
		PaShareDetail paShareDetailQuery = paShareDetailService.findPaShareDetailById(paShareDetail.getId());
		if (!isReturn) {
			try {
				UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
					result = paShareDetailAccountService.singleEnterAccount(paShareDetailQuery ,userInfo.getUsername());
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
					msg.put("status",true);
					log.info(msg.toString());
					return msg;
				} else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
				log.error("异常:",e);
			}	
		}
		return msg;
	}
	
	
	
	
	
	/**
	 * 批量入账前校验
	 * @param accountMonth
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('peragentAtion:batchEnterAccount')")
	@RequestMapping(value = "comfirmBacthAccount.do")
	@Logs(description="入账前校验")
	@ResponseBody
	public Map<String,Object> comfirmBacthAccount(String accountMonth) throws Exception{
		log.info("进入入账前校验------> ");
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(accountMonth)) {
			msg.put("msg","入账日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			// 获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			try {
				String username = userInfo.getUsername();
				Map<String,Object> result = paShareDetailService.comfirmBacthAccount(accountMonth,username);
				Object enterAmount =  "0";
				if (result != null) {
					enterAmount = result.get("enterAmount");
				}
				msg.put("enterAmount", enterAmount);//准备入账金额
			} catch (Exception e) {
				msg.put("status",false);
				msg.put("msg",e.toString());
				log.error("入账前校验获取入账金额异常:",e);
			}	
		}
		
		return msg;
	}
	
	
	
	/**
	 * 批量入账
	 * @param accountMonth
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasAuthority('peragentAtion:batchEnterAccount')")
	@RequestMapping(value = "bacthAccount.do")
	@Logs(description="批量入账")
	@ResponseBody
	public Map<String,Object> bacthAccount(String accountMonth) throws Exception{
		log.info("进入批量入账------> ");
		Map<String,Object> msg=new HashMap<>();
		boolean isReturn = false;
		if (StringUtils.isBlank(accountMonth)) {
			msg.put("msg","入账日期不能为空");
			msg.put("status",false);
			isReturn = true;
		}
		if (!isReturn) {
			// 获取到登录者信息
			UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			try {
				String username = userInfo.getUsername();
				Map<String, Object> result = new HashMap<>();
				synchronized(this){
				   result = paShareDetailAccountService.bacthAccount(accountMonth,username);
				}
				Boolean resultStatus = (Boolean) result.get("status");
				String resultMsg = (String) result.get("msg");
				if (resultStatus) {
					msg.put("msg","操作成功");
					msg.put("status",true);
					log.info(msg.toString());
					return msg;
				}
				else {
					msg.put("msg",resultMsg);
					msg.put("status",false);
					log.info(msg.toString());
				}
			} catch (Exception e) {
				//e.printStackTrace();
				msg.put("status",false);
				msg.put("msg",e.getMessage());
				log.error(msg.toString());
				log.error("异常:",e);
			}	
		}
		
		return msg;
	}
	
	
	
	/**
	 * 导出数据
	 * 
	 * @param params
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@PreAuthorize("hasAuthority('peragentAtion:export')")
	@RequestMapping(value = "peragentExport.do", method = RequestMethod.POST)
	public void peragentExport(@RequestParam Map<String, String> params,
			@ModelAttribute("paShareDetail")  PaShareDetail paShareDetail, HttpServletResponse response, HttpServletRequest request)
			throws IOException {

		//用于对数据字典的数据进行格式化显示
		ExportFormat format = new ExportFormat() ;
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileName = "超级盟主月结报表导出_" + sdf.format(new Date()) + ".xlsx";
		String fileNameFormat = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileNameFormat);
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		
		
		// 用户类别
		List<SysDict> userTypeList = null;
		// 分润类别
		List<SysDict> shareTypeList = null;
		// 入账状态
		List<SysDict> enterAccountStatusList = null;
		
		try {
			
			enterAccountStatusList = sysDictService.findSysDictGroup("enter_account_status");
			userTypeList  = sysDictService.findSysDictGroup("peragent_user_type");
			shareTypeList  = sysDictService.findSysDictGroup("peragent_share_type");
			Map<String, String> tempMap = null;

	        if(!StringUtils.isBlank(paShareDetail.getRealName())){
	        	paShareDetail.setRealName(URLDecoder.decode(paShareDetail.getRealName(),"UTF-8"));
	        }
	        if(!StringUtils.isBlank(paShareDetail.getUserCode())){
	        	paShareDetail.setUserCode(URLDecoder.decode(paShareDetail.getUserCode(),"UTF-8"));
	        }
	        
	        if(!StringUtils.isBlank(paShareDetail.getOneUserCode())){
	        	paShareDetail.setOneUserCode(URLDecoder.decode(paShareDetail.getOneUserCode(),"UTF-8"));
	        }
	        if(!StringUtils.isBlank(paShareDetail.getTwoUserCode())){
	        	paShareDetail.setTwoUserCode(URLDecoder.decode(paShareDetail.getTwoUserCode(),"UTF-8"));
	        }
			// 查询超级盟主月结报表
			List<PaShareDetail> list = paShareDetailService.peragentExport(paShareDetail);

			if (list != null && list.size() > 0) {
				for (PaShareDetail info : list) {
					tempMap = new HashMap<String, String>();
					tempMap.put("id", String.valueOf(info.getId()));
					tempMap.put("shareAmount", info.getShareAmount()==null?"0.00":String.valueOf(info.getShareAmount()));
					
			        tempMap.put("transAmount", info.getTransAmount() == null ? "" : String.valueOf(info.getTransAmount()));
			        tempMap.put("transNo", info.getTransNo());
			        
			       // tempMap.put("setterAmount", info.getTransAmount() == null ? "" : String.valueOf(info.getTransAmount()));
			        tempMap.put("setterNo", info.getSetterNo());
			        
					tempMap.put("shareType", format.formatSysDict(info.getShareType(), shareTypeList));
					
					tempMap.put("teamTotalAmount", info.getTeamTotalAmount()==null?"0.00":String.valueOf(info.getTeamTotalAmount()));
					tempMap.put("totalAmount", info.getTotalAmount()==null?"0.00":String.valueOf(info.getTotalAmount()));
					tempMap.put("userType",  format.formatSysDict(info.getUserType(), userTypeList));
					tempMap.put("realName", info.getRealName());
					tempMap.put("userCode", String.valueOf(info.getUserCode()));
					
					tempMap.put("shareLevelStr", info.getShareLevelStr());
			        tempMap.put("shareRatioStr", info.getShareRatioStr());//交易分润比例
			        tempMap.put("honourShareRatioStr", info.getHonourShareRatioStr());//荣耀奖金分润比例
					PaBrand paBrand = paShareDetailService.findByBrandCode(info.getBrandCode());
					if(paBrand != null){
						tempMap.put("brandCode", paBrand.getBrandName());
					}else{
						tempMap.put("brandCode", "");
					}

					tempMap.put("oneUserCode", info.getOneUserCode());
					tempMap.put("twoUserCode", info.getTwoUserCode());
					
					String createTime = null;
					if (info.getCreateTime() != null) {
						createTime = DateUtil.getLongFormatDate(info.getCreateTime());
					}
					tempMap.put("createTime", createTime);
					tempMap.put("accStatus",  format.formatSysDict(info.getAccStatus(), enterAccountStatusList));
					String accTime = null;
					if (info.getAccTime() != null) {
						accTime = DateUtil.getLongFormatDate(info.getAccTime());
					}
					tempMap.put("accTime", accTime);
					tempMap.put("accMessage", info.getAccMessage());
					data.add(tempMap);
				}
			}
		} catch (Exception e) {
			log.error("导出超级盟主月结报表异常:", e);
		}

		NewListDataExcelExport export = new NewListDataExcelExport();
	    String[] cols = { "id", "shareAmount", "transAmount", "transNo", "setterNo", "shareType", "teamTotalAmount", "totalAmount", "userType", "realName", "userCode", "shareLevelStr", "shareRatioStr", "honourShareRatioStr","brandCode", "twoUserCode", "oneUserCode", "createTime", "accStatus", "accTime", "accMessage" };

	    String[] colsName = { "序号", "分润金额", "交易/结算金额", "交易订单号", "结算订单号", "分润类别", "当月团队总流水（元）", "当月直营商户总流水（元）", "用户类别", "用户名称", "用户编号", "交易分润等级", "交易分润比例", "荣耀奖金分润比例","所属品牌", "所属大盟主编号", "所属机构编号", "分润创建时间", "入账状态", "入账时间", "入账信息" };

	    OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();

	}
	
	
}
