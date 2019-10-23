package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.*;
import cn.eeepay.framework.service.AccessService;
import cn.eeepay.framework.service.AgentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.SuperBankService;
import cn.eeepay.framework.service.ZxProductOrderService;
import cn.eeepay.framework.util.ClientInterface;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.DateUtils;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.Result;
import cn.eeepay.framework.util.StringUtil;

@Controller
@RequestMapping(value = "/superBank")
public class SuperBankAction {
	
	private static final Logger log = LoggerFactory.getLogger(SuperBankAction.class);
	
	@Resource
	public SuperBankService superBankService;
	
	@Resource
	private ZxProductOrderService zxProductOrderService;
	@Resource
	private AgentInfoService agentInfoService;

	/**
	 * 用户管理查询
	 * @param params
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectByCondions")
	@ResponseBody
	public Object selectByCondions(@RequestParam("info")String params,@Param("page")Page<UserInfoSuperBank> page) {
		try{
			UserInfoSuperBank userInfoSuperBank = JSONObject.parseObject(params, UserInfoSuperBank.class);
			superBankService.selectByCondions(userInfoSuperBank,page);
		}catch(Exception e){
			log.error("用户管理查询异常!",e);
		}
		return page;
	}
	/**
	 * 用户统计查询
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/selectTotal")
	@ResponseBody
	public UserInfoSuperBank selectTotal(@RequestParam("info")String params) {
		UserInfoSuperBank superBankNum = new UserInfoSuperBank();
		try{
			UserInfoSuperBank userInfoSuperBank = JSONObject.parseObject(params, UserInfoSuperBank.class);
			superBankNum = superBankService.selectTotal(userInfoSuperBank);
		}catch(Exception e){
			log.error("用户管理统计查询异常!",e);
		}
		return superBankNum;
	}
	/**
	 * 开二级代理后台
	 * @param params
	 * @return
	 */
	@SystemLog(description = "银行家开二级代理后台")
	@RequestMapping(value = "/openTwoAgent")
	@ResponseBody
	public Map<String,Object> openTwoAgent(@RequestParam("info")String params) {
		Map<String,Object> map = new HashMap<>();
		boolean flag = false;
		String msg = "操作异常";
		try{
			UserInfoSuperBank userInfoSuperBank = JSONObject.parseObject(params, UserInfoSuperBank.class);
			int i = superBankService.updateOpenTwoAgent(userInfoSuperBank);
			if (i == 3) {
				flag = true;
				msg = "操作成功";
			}
			map.put("status", flag);
			map.put("msg", msg);
		}catch(Exception e){
			msg = "该用户手机号已经存在，开二级代理后台失败，请联系该用户在公众号->会员中心->个人信息，更改手机号";
			map.put("status", flag);
			map.put("msg", msg);
			log.error("开二级代理后台出现异常!",e);
		}
		return map;
	}
	/**
	 * 用户管理详情
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectDetail")
	@ResponseBody
	public Object selectDetail(@RequestParam("userId")String param) {
		Map<String,Object> map = new HashMap<>();
		try{
			String userId = JSONObject.parseObject(param, String.class);
			map = superBankService.selectDetail(userId);
			//权限验证
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			UserInfoSuperBank userInfoSuperBank = (UserInfoSuperBank)map.get("userInfoSuperBank");
			if(userInfoSuperBank != null){
				if(!StringUtils.equals(userInfoSuperBank.getOrgId().toString(), loginAgent.getAgentNo())){
					Map<String,Object> result = new HashMap<>();
					result.put("msg", "非法操作");
					return result;
				}
			}
		}catch(Exception e){
			map.put("msg", "查询异常");
			log.error("用户详情查询异常!",e);
		}
		return map;
	}
	//获取商户账号信息
	@RequestMapping(value="/getAccountInfo")
	@ResponseBody
	public Object getAccountInfo(@RequestBody String param) {
		Map<String, Object> maps=new HashMap<String, Object>();
		maps.put("bols", true);
		maps.put("msg", "获取商户账号信息异常");
		try{
			JSONObject json=JSON.parseObject(param);
			String merNo=json.getString("userCode");
//				String merNo="211000000742";
			//账户信息
			String userType = "M";
			String subjectNo = Constants.SUPER_BANK_SUBJECT_NO_MER;
			String str = ClientInterface.getBalance(merNo, userType, subjectNo);
			if(StringUtils.isNotBlank(str)){
				JSONObject jsons=JSON.parseObject(str);
				if(jsons==null || !jsons.getBoolean("status")){
					maps.put("bols", false);
					maps.put("msg", "获取商户账号信息失败");
					return maps;
				}
				UserCard ainfo=JSON.parseObject(str, UserCard.class);
				if(ainfo == null){
					maps.put("bols", false);
					maps.put("msg", "获取商户账号信息失败");
					return maps;
				}
				List<UserCard> alist = new ArrayList<>();
				alist.add(ainfo);
				maps.put("bols", true);
				maps.put("alist", alist);
			}
			//账户交易记录
//			String str1 = ClientInterface.getAccountDetail(merNo, userType,subjectNo,null, null, null, 1, 10);
//			if(StringUtils.isNotBlank(str1)){
//				JSONObject jsons1=JSON.parseObject(str1);
//				JSONObject jsons2=JSON.parseObject(jsons1.getString("data"));
//				if(jsons2!=null){
//					List<UserCard> slist=JSON.parseArray(jsons2.getJSONArray("list").toJSONString(),UserCard.class);
//					if(slist.size()<1){
//						if(!jsons1.getBoolean("status")){
//							maps.put("bols", false);
//							maps.put("msg", "获取商户账号交易记录信息失败");
//							return maps;
//						}
//					}
//					maps.put("list", slist);
//					maps.put("total", jsons2.getString("total"));
//					maps.put("bols", true);
//				}
//			}
		}catch(Exception e){
			log.error("获取商户账号信息异常",e);
			maps.put("bols", false);
			maps.put("msg", "获取商户账号信息异常");
		}
		return maps;
	}
	
	//获取商户账户交易记录
	@RequestMapping(value="/getAccountTranInfo")
	@ResponseBody
	public Object getAccountTranInfo(@RequestParam("info") String param) throws Exception{
		Map<String, Object> maps=new HashMap<String, Object>();
		maps.put("bols", false);
		maps.put("msg", "取商户账户交易记录异常");
		try{
			JSONObject json=JSON.parseObject(param);
	//				System.out.println("aa====="+json);
			String merNo=json.getString("merNo");
	//				String merNo="211000000742";
			Date sdate=json.getDate("sdate");
			Date edate=json.getDate("edata");
			String operation=json.getString("debitCreditSide");
			Integer pageNo=json.getInteger("pageNo");
			Integer pageSize=json.getInteger("pageSize");
			
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd"); 
			String sd="";
			String ed="";
			if(sdate!=null && !sdate.equals("")){
				sd = sdf.format(sdate);  
			}
			if(edate!=null && !edate.equals("")){
				ed = sdf.format(edate);  
			}
			//账户交易记录
	//				System.out.println(merNo+"\t"+sd+"\t"+ed+"\t"+operation+"\t"+pageNo+"\t"+pageSize);
			String userType = "M";
			String subjectNo = Constants.SUPER_BANK_SUBJECT_NO_MER;
			String str1 = ClientInterface.getAccountDetail(merNo, userType,subjectNo,sd, ed, operation, pageNo, pageSize);
			if(str1 == null || "".equals(str1)){
				return maps;
			}
			JSONObject jsons1=JSON.parseObject(str1);
			if(!jsons1.getBoolean("status")){
				return maps;
			}
			JSONObject jsons2=JSON.parseObject(jsons1.getString("data"));
			List<UserCard> slist=JSON.parseArray(jsons2.getJSONArray("list").toJSONString(),UserCard.class);
			maps.put("bols", true);
			maps.put("msg", "查询成功");
			maps.put("list", slist);
			maps.put("total", jsons2.getString("total"));
		}catch(Exception e){
			System.out.println(e);
			log.error("取商户账户交易记录异常",e);
			maps.put("bols", false);
			maps.put("msg", "取商户账户交易记录异常");
		}
		return maps;
		}
	
	/**
	 * 用户查询导出 
	 * @param info
	 * @param response
	 * @param request
	 * @throws Exception
	 */
	@SystemLog(description = "银行家用户查询导出 ")
	@RequestMapping(value="/exportUserInfoSuperBank")
	@ResponseBody
	public void exportUserInfoSuperBank(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String agentNo = principal.getUserEntityInfo().getEntityId();
		UserInfoSuperBank userInfoSuperBank = JSON.parseObject(info,UserInfoSuperBank.class);
		List<UserInfoSuperBank> list = superBankService.exportUserInfoSuperBank(userInfoSuperBank, agentNo);
		String fileName = "用户管理列表"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		List<Map<String, String>> data = new ArrayList<>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("userCode", null);
			maps.put("orgId", null);
			maps.put("secondUserNode", null);
			maps.put("userName", null);
			maps.put("phone", null);
			maps.put("weixinCode", null);
			maps.put("totalProfit", null);
			maps.put("userType", null);
			maps.put("payBack", null);
			maps.put("repaymentUserNo", null);
			maps.put("receiveUserNo", null);
			maps.put("topOneCode", null);
			maps.put("topTwoCode", null);
			maps.put("topThreeCode", null);
			maps.put("openProvince", null);
			maps.put("openCity", null);
			maps.put("openRegion", null);
			maps.put("remark", null);
			data.add(maps);
		}else{
			for (UserInfoSuperBank order : list) {
				Map<String, String> maps = new HashMap<>();
				maps.put("userCode", order.getUserCode());
				maps.put("orgId", StringUtils.trimToEmpty(order.getOrgName().toString()));
				maps.put("secondUserNode", order.getSecondUserNode());
				maps.put("userName", StringUtils.trimToEmpty(order.getUserName()));
				maps.put("phone", StringUtils.trimToEmpty(order.getPhone()));
				maps.put("weixinCode", StringUtils.trimToEmpty(order.getWeixinCode()));
				maps.put("totalProfit", order.getTotalAmount() != null ? order.getTotalAmount().toString() : "" );
				String userType = StringUtils.trimToEmpty(order.getUserType());
				switch (userType) {
				case "10":
					userType = "普通用户";
					break;
				case "20":
					userType = "专员";
					break;
				case "30":
					userType = "经理";
					break;
				case "40":
					userType = "银行家";
					break;
				case "50":
					userType = "OEM";
					break;
				case "60":
					userType = "平台";
					break;	
				default:
					userType = "";
					break;
				}
				maps.put("userType",userType );
				maps.put("payBack", "1".equals(StringUtils.trimToEmpty(order.getPayBack())) ? "已退款" : "未退款");
				maps.put("repaymentUserNo", StringUtils.trimToEmpty(order.getRepaymentUserNo()));
				maps.put("receiveUserNo", StringUtils.trimToEmpty(order.getReceiveUserNo()));
				maps.put("topOneCode", StringUtils.trimToEmpty(order.getTopOneCode()));
				maps.put("topTwoCode", StringUtils.trimToEmpty(order.getTopTwoCode()));
				maps.put("topThreeCode", StringUtils.trimToEmpty(order.getTopThreeCode()));
				maps.put("openProvince", order.getOpenProvince());
				maps.put("openCity", order.getOpenCity());
				maps.put("openRegion", order.getOpenRegion());
				maps.put("remark", order.getRemark());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"userCode","orgId","secondUserNode","userName","phone","weixinCode","totalProfit","userType","payBack","repaymentUserNo","receiveUserNo",
				"topOneCode","topTwoCode","topThreeCode","openProvince","openCity","openRegion","remark"};
		String[] colsName = new String[]{"用户ID","所属组织","二级代理节点","姓名", "手机号","微信号", "总收益","代理身份","是否退款","超级还用户编号","收款商户编号",
				"上级代理ID","上上级代理ID","上上上级代理ID","省","市","区","备注"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, ouputStream);
		ouputStream.close();
	}
	
	  /**
     * 获取所有的银行家组织
     * @return
     */
	@RequestMapping("/getOrgInfoList")
    @ResponseBody
    public Result getOrgInfoList(){
        Result result = new Result();
        try {
            List<OrgInfo> list = superBankService.getOrgInfoList();
            result.setStatus(true);
            result.setData(list);
        } catch (Exception e){
            log.error("获取所有的银行家组织异常", e);
        }
        return result;
    }
	 /**
     * 模糊查询所有的用户
     * @param userCode
     * @return
     */
    @RequestMapping("/selectUserInfoList")
    @ResponseBody
    public Result selectUserInfoList(String userCode){
        Result result = new Result();
        try {
            List<UserInfoSuperBank> userInfoList = superBankService.selectUserInfoList(userCode);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(userInfoList);
        } catch (Exception e){
            log.error("模糊查询所有的用户异常", e);
        }
        return result;
    }
    /**
     * 订单分页条件查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/orderManager")
    @ResponseBody
    public Result orderManager(@RequestBody OrderMain baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            Page<OrderMain> page = new Page<>(pageNo, pageSize);
            superBankService.selectOrderPage(baseInfo, page);
            OrderMainSum orderMainSum = superBankService.selectOrderSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setData(map);
        } catch (Exception e){
        	result.setStatus(false);
        	result.setMsg("查询数据异常!");
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }
    /**
     * 导出超级银行家代理商授权订单
     * @param baseInfo
     */
    @RequestMapping("/exportAgentOrder")
    @SystemLog(description = "导出超级银行家订单")
    public void exportAgentOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportAgentOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家订单异常", e);
        }
    }
    /**
     * 超级银行家订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/orderDetail")
    @ResponseBody
    public Result orderDetail(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            OrderMain orderMain = superBankService.selectOrderDetail(orderNo);
            //权限验证
			if(orderMain != null){
				AgentInfo loginAgent = agentInfoService.selectByPrincipal();
				if(!loginAgent.getAgentNo().equals(orderMain.getOrgId().toString())){
					result.setStatus(false);
					result.setMsg("非法操作");
					return result;
				}
			}
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(orderMain);
        } catch (Exception e){
            log.error("超级银行家订单详情", e);
        }
        return result;
    }
	@RequestMapping("/insuranceOrderDetail")
	@ResponseBody
	public Result insuranceOrderDetail(String orderNo){
		Result result = new Result();
		try {
			if(StringUtil.isBlank(orderNo)){
				result.setMsg("订单号不能为空");
				return result;
			}
			OrderMain orderMain = superBankService.selectInsuranceOrderDetail(orderNo);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(orderMain);
		} catch (Exception e){
			log.error("超级银行家订单详情", e);
		}
		return result;
	}
    /**
     * 导出超级银行家办理信用卡订单
     * @param baseInfo
     */
    @RequestMapping("/exportCreditOrder")
    @SystemLog(description = "导出超级银行家订单")
    public void exportCreditOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportCreditOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家订单异常", e);
        }
    }
    /**
     * 获取所有的贷款机构
     * @return
     */
    @RequestMapping("/getLoanList")
    @ResponseBody
    public Result getLoanList(){
        Result result = new Result();
        try {
            List<LoanSource> list = superBankService.getLoanList();
            result.setStatus(true);
            result.setData(list);
        } catch (Exception e){
            log.error("获取所有的贷款机构异常", e);
        }
        return result;
    }

	/**
	 * 获取所有的兑换机构
	 * @return
	 */
	@RequestMapping("/getBonusConfList")
	@ResponseBody
	public Result getBonusConfList(){
		Result result = new Result();
		try {
			List<BonusConf> list = superBankService.getBonusConfList();
			result.setStatus(true);
			result.setData(list);
		} catch (Exception e){
			log.error("获取所有的银行家组织异常", e);
		}
		return result;
	}

	/**
	 * 获取所有保险公司别名
	 * @return
	 */
	@RequestMapping("/getCompanyNickNameList")
	@ResponseBody
	public Result getCompanyNickNameList(){
    	Result result = new Result();
    	try{
    		List<InsuranceCompany> list = superBankService.getCompanyNickNameList();
    		result.setStatus(true);
    		result.setData(list);
		}catch (Exception e){
    		log.error("获取所有保险公司别名异常");
		}
		return result;
	}

	/**
	 * 查询所有支持的银行
	 * @return
	 */
	@RequestMapping("/banksList")
	@ResponseBody
	public Result banksList(){
		Result result = new Result();
		try{
			List<CreditcardSource> list = superBankService.banksList();
			result.setData(list);
			result.setStatus(true);
			result.setMsg("查询成功");
		}catch(Exception e){
			log.info("获取银行列表失败",e);
		}

		return result;
	}

    /**
     * 导出超级银行家贷款订单
     * @param baseInfo
     */
    @RequestMapping("/exportLoanOrder")
    @SystemLog(description = "导出超级银行家贷款订单")
    public void exportLoanOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportLoanOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }

	/**
	 * 导出超级银行家保险订单
	 * @param baseInfo
	 * @param response
	 */
    @RequestMapping("/exportInsuranceOrder")
    @SystemLog(description = "导出超级银行家保险订单")
    public void exportInsuranceOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportInsuranceOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }
    /**
     * 导出超级银行家收款订单
     * @param baseInfo
     */
    @RequestMapping("/exportReceiveOrder")
    @SystemLog(description = "导出超级银行家收款订单")
    public void exportReceiveOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportReceiveOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家收款订单异常", e);
        }
    }
    /**
     * 导出超级银行家还款订单
     * @param baseInfo
     */
    @RequestMapping("/exportRepayOrder")
    @SystemLog(description = "导出超级银行家还款订单")
    public void exportRepayOrder(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportRepayOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家还款订单异常", e);
        }
    }
    /**
     * 导出超级银行家订单分润明细
     * @param baseInfo
     */
    @RequestMapping("/exportProfitDetail")
    @SystemLog(description = "导出超级银行家订单分润明细")
    public void exportProfitDetail(String baseInfo, HttpServletResponse response){
        try {
            UserProfit order = JSONObject.parseObject(baseInfo, UserProfit.class);
            superBankService.exportProfitDetail(response, order);
        } catch (Exception e){
            log.error("导出超级银行家还款订单异常", e);
        }
    }
    /**
     * 导出超级银行家开通办理信用卡订单
     * @param baseInfo
     */
    @RequestMapping("/exportOpenCredit")
    @SystemLog(description = "导出超级银行家开通办理信用卡订单")
    public void exportOpenCredit(String baseInfo, HttpServletResponse response){
        try {
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportOpenCredit(response, order);
        } catch (Exception e){
            log.error("导出超级银行家开通办理信用卡订单", e);
        }
    }
    /**
     * 导出超级银行家积分超级兑订单
     * @param baseInfo
     */
    @RequestMapping("/exportSuperExchangeOrder")
    @SystemLog(description = "导出超级银行家积分超级兑订单")
    public void exportSuperExchangeOrder(String baseInfo, HttpServletResponse response) throws UnsupportedEncodingException {
		if(StringUtils.isNotBlank(baseInfo)){
			baseInfo = URLDecoder.decode(baseInfo, "utf-8");
		}
        try {
			System.out.println(baseInfo);
            OrderMain order = JSONObject.parseObject(baseInfo, OrderMain.class);
            superBankService.exportSuperExchangeOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家开通办理信用卡订单", e);
        }
    }
    /**
     * 分润明细订单查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/profitDetailOrder")
    @ResponseBody
    public Result profitDetailManager(@RequestBody UserProfit baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
//            String createTimeStart = baseInfo.getCreateDateStart();
//            String createTimeEnd = baseInfo.getCreateDateEnd();
//            if (checkOrderDate(createTimeStart,createTimeEnd, result)) return result;
            Page<UserProfit> page = new Page<>(pageNo, pageSize);
            superBankService.selectProfitDetailPage(baseInfo, page);
            OrderMainSum orderMainSum = superBankService.selectProfitDetailSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询分润明细订单异常", e);
        }
        return result;
    }
    /**
     * 校验查询时间间隔不能超过三个月
     * @param createTimeStartStr
     * @param createTimeEndStr
     * @param result
     * @return
     */
    private boolean checkOrderDate(String createTimeStartStr,String createTimeEndStr, Result result) {
        if(StringUtils.isBlank(createTimeStartStr)
                || StringUtils.isBlank(createTimeEndStr)) {
            result.setMsg("创建时间不能为空");
            return true;
        }
        Date createTimeStart = DateUtils.parseDateTime(createTimeStartStr);
        Date createTimeEnd = DateUtils.parseDateTime(createTimeEndStr);
        if(createTimeEnd.getTime() < createTimeStart.getTime()){
            result.setMsg("创建结束时间时间不能小于起始时间");
            return true;
        }
        if(createTimeEnd.getTime() - createTimeStart.getTime() > 3*30*24*60*60*1000L){
            result.setMsg("创建时间间隔不能超过三个月");
            return true;
        }
        return false;
    }
    
    /**
     * 根据条件查询贷款奖励配置
     * @param conf
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/loanConfList")
    @ResponseBody
    public Result getLoanConfByPager(@RequestBody LoanBonusConf conf,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize){
    	Result result = new Result();
    	log.info("贷款机构ID--------------:"+conf.getSourceId());
    	try{
    		Page<LoanBonusConf> page = new Page<>(pageNo, pageSize);
    		List<LoanBonusConf> list = superBankService.getLoanBonusConfList(conf,page);
    		page.setResult(list);
    		result.setData(page);
    		result.setStatus(true);
            result.setMsg("查询成功");
    	}catch(Exception e){
    		log.info("获取贷款机构列表失败",e);
    	}
    	
    	return result;
    }
    /**
     * 超级银行家组织详情
     * @return
     */
    @RequestMapping("/orgInfoDetail")
    @ResponseBody
    public Result orgInfoDetail(){
        Result result = new Result();
        result.setMsg("详情查询失败");
        try {
            result = superBankService.orgInfoDetail();
        } catch (Exception e){
            log.error("查看超级银行家组织详情异常", e);
        }
        return result;
    }
    /**
     * 查询开发配置明细
     * @return
     */
    @RequestMapping("/selectDevelopmentConfiguration")
    @ResponseBody
    public Result selectDevelopmentConfiguration(){
    	Result result = new Result();
    	try {
    		result = superBankService.selectDevelopmentConfiguration();
    	} catch (Exception e){
    		log.error("查询开发配置异常", e);
    		result.setMsg("开发配置查询异常");
    	}
    	return result;
    }
    /**
     * 信用卡奖励查询
     * @param creditCardConf
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/getCreditCardConf")
    @ResponseBody
    public Result getCreditCardConfDatas(@RequestBody CreditCardBonus creditCardConf,
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize){
    	 Result result = new Result();
    	 try {
    		 log.info("\n-------------- " + JSON.toJSONString(creditCardConf) + "-----------------\n");
    		 
             Page<CreditCardBonus> page = new Page<>(pageNo, pageSize);
            
             result = superBankService.getCreditBonusConf(creditCardConf, page);
             result.setStatus(true);
             result.setMsg("查询成功");
         } catch (Exception e) {
             log.error("信用卡奖金配置查询异常", e);
             result.setStatus(false);
             result.setMsg("查询异常");
         }
    	 return result;
    }
    
    @RequestMapping("/lotteryOrder")
    @ResponseBody
    public Result lotteryOrder(@RequestBody LotteryOrder baseInfo,
                               @RequestParam(defaultValue = "1") int pageNo,
                               @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            if(baseInfo == null){
                result.setMsg("参数不能为空");
                return result;
            }
            Page<LotteryOrder> page = new Page<>(pageNo, pageSize);
            
            System.out.println("-----参数-------"+JSON.toJSON(baseInfo) + "--------------");
            
            superBankService.qryLotteryOrder(baseInfo, page);
            LotteryOrder sumOrder = superBankService.qrySumOrder(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("sumOrder", sumOrder);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }
    
    @RequestMapping("/lotteryOrderDetail")
    @ResponseBody
    public Result lotteryOrderDetail(@Param("orderNo")String orderNo) {
        Result result = new Result();
        try {
        	
        	System.out.println("-------查询订单--------" + orderNo);
        	
        	LotteryOrder info = new LotteryOrder();
        	info.setOrderNo(orderNo);
        	Page<LotteryOrder> page = new Page<>(1, 10);
        	List<LotteryOrder> list = superBankService.qryLotteryOrder(info, page);
        	
            LotteryOrder order = list.size() > 0 ? list.get(0):null;
            
            System.out.println("---------------查询结果----------" + JSON.toJSONString(order));
            
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(order);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }
    
    @RequestMapping("/exportLotteryOrder")
    @SystemLog(description = "导出彩票代购订单")
    public void exportLotteryOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            LotteryOrder info = JSONObject.parseObject(baseInfo, LotteryOrder.class);
            superBankService.exportLotteryOrder(response, info);
        } catch (Exception e){
            log.error("导出彩票代购订单异常", e);
        }
    }

	/**
	 * 查询排行榜信息
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectRankingRecord")
	@ResponseBody
	public Result selectRankingRecord(@RequestBody RankingRecordInfo baseInfo,
							   @RequestParam(defaultValue = "1") int pageNo,
							   @RequestParam(defaultValue = "10") int pageSize) {
		Result result = new Result();
		try {
			if(baseInfo == null){
				result.setMsg("参数不能为空");
				return result;
			}
			Page<LotteryOrder> page = new Page<>(pageNo, pageSize);

			System.out.println("-----参数-------"+JSON.toJSON(baseInfo) + "--------------");

			superBankService.queryRankingRecord(baseInfo, page);
			Map<String,Object> sumMap = superBankService.queryRankingRecordSum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("sum", sumMap);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		} catch (Exception e){
			log.error("分页条件查询订单异常", e);
		}
		return result;
	}
	/**
	 * 查询排行榜 用户奖金发放记录
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getRankingPushRecord")
	@ResponseBody
	public Result getRankingPushRecord(@RequestBody RankingPushRecordInfo baseInfo,
									   @RequestParam(defaultValue = "1") int pageNo,
									   @RequestParam(defaultValue = "10") int pageSize) {
		Result result = new Result();

		try {
			if(baseInfo == null){
				result.setMsg("参数不能为空");
				return result;
			}
			System.out.println("-----参数-------"+JSON.toJSON(baseInfo) + "--------------");
			Page<RankingPushRecordInfo> page = new Page<>(pageNo, pageSize);
			superBankService.queryRankingPushRecordPage(baseInfo, page);
			List<RankingPushRecordInfo> rankingPushRecordInfoList = page.getResult();
			if (!CollectionUtils.isEmpty(rankingPushRecordInfoList)){

				for (int i = 0; i < rankingPushRecordInfoList.size(); i++) {
					RankingPushRecordInfo rankingPushRecordInfo = rankingPushRecordInfoList.get(i);
					if(StringUtils.isNotEmpty(rankingPushRecordInfo.getNickName())){
						String deNickName = null;
						try {
							deNickName = URLDecoder.decode(rankingPushRecordInfo.getNickName(), "utf-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						rankingPushRecordInfo.setDeNickName(deNickName);
					}
					rankingPushRecordInfoList.set(i,rankingPushRecordInfo);
				}

			}

			String totalMoneyCount = superBankService.selectRankingPushRecordTotalMoneySum(baseInfo);
			String pushTotalMoneyCount = superBankService.selectRankingPushRecordPushTotalMoneySum(baseInfo);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("rankingPushRecordInfoList", rankingPushRecordInfoList);
			map.put("peopleCount", page.getTotalCount());
			map.put("totalMoneyCount", totalMoneyCount);
			map.put("pushTotalMoneyCount", pushTotalMoneyCount);
			result.setMsg("查询成功");
			result.setStatus(true);
			result.setData(map);
		} catch (Exception e){
			log.error("分页条件查询用户提现记录异常", e);
		}
		return result;
	}

	/**
	 * 导出排行榜 用户奖金发放记录
	 * @param baseInfo
	 */
	@RequestMapping("/exportRankingPushRecord")
	public void exportRankingPushRecord(@RequestParam("baseInfo") String baseInfo, HttpServletResponse response) throws Exception{
		if(StringUtils.isNotBlank(baseInfo)){
			baseInfo = URLDecoder.decode(baseInfo, "utf-8");
		}
		try {
			RankingPushRecordInfo params = JSONObject.parseObject(baseInfo, RankingPushRecordInfo.class);
			superBankService.exportRankingPushRecord(response, params);
		} catch (Exception e){
			log.error("导出超级银行家用户奖金发放记录异常", e);
		}
	}
	/**
	 * 排行榜 详情
	 * @param recordId
	 * @return
	 */
	@RequestMapping("/getRankingRecordById")
	@ResponseBody
	public Result getRankingRecordById(String recordId){
		Result result = new Result();
		try {
			RankingRecordInfo rankingRecordInfo = superBankService.getRankingRecordById(recordId);
			result.setData(rankingRecordInfo);
			result.setMsg("详情查询成功");
			result.setStatus(true);
		} catch (Exception e){
			log.error("查看排行榜详情异常", e);
		}
		return result;
	}

	/**
	 * 查询排行榜 榜单明细
	 * @param recordId
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/queryRankingRecordDetail")
	@ResponseBody
	public Result queryRankingRecordDetail(@RequestParam String recordId,
									   @RequestParam(defaultValue = "1") int pageNo,
									   @RequestParam(defaultValue = "10") int pageSize) {
		Result result = new Result();

		try {
			if (recordId == null) {
				result.setMsg("参数不能为空");
				return result;
			}
			Page<RankingRecordDetailInfo> page = new Page<>(pageNo, pageSize);
			superBankService.queryRankingRecordDetailPage(recordId, page);
			List<RankingRecordDetailInfo> rankingRecordDetailInfoList = page.getResult();
			if (!CollectionUtils.isEmpty(rankingRecordDetailInfoList)){

				for (int i = 0; i < rankingRecordDetailInfoList.size(); i++) {
					RankingRecordDetailInfo rankingRecordDetailInfo = rankingRecordDetailInfoList.get(i);
					if(StringUtils.isNotEmpty(rankingRecordDetailInfo.getNickName())){
						String deNickName = null;
						try {
							deNickName = URLDecoder.decode(rankingRecordDetailInfo.getNickName(), "utf-8");
						} catch (UnsupportedEncodingException e1) {
							e1.printStackTrace();
						}
						rankingRecordDetailInfo.setDeNickName(deNickName);
					}
					rankingRecordDetailInfoList.set(i,rankingRecordDetailInfo);
				}

			}

			String userTotalAmountCount = superBankService.queryRankingRecordDetailUserTotalAmountSum(recordId);
			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("rankingRecordDetailInfoList", rankingRecordDetailInfoList);
			map.put("peopleCount", page.getTotalCount());
			map.put("userTotalAmountCount", userTotalAmountCount);
			result.setMsg("查询成功");
			result.setStatus(true);
			result.setData(map);
		} catch (Exception e){
			log.error("查询排行榜 榜单明细 异常", e);
		}
		return result;
	}

	/**违章代缴订单查询*/
    @RequestMapping("/findCarOrder")
    @ResponseBody
    public Result findCarOrder(@RequestBody CarOrder order,@RequestParam( defaultValue = "1")int pageNo,
            @RequestParam( defaultValue = "10")int pageSize){
    	Result result = new Result();
    	try{
    		Page<CarOrder> page = new Page<CarOrder>(pageNo,pageSize);
        	
        	order = superBankService.getCarOrders(order, page);
        	
        	Map<String,Object> data = new HashMap<String,Object>();
        	data.put("orderSum", order);
        	data.put("page", page);
        	
        	result.setStatus(true);
        	result.setData(data);
        	result.setMsg("查询成功");
    	}catch(Exception e){
    		e.printStackTrace();
    		result.setStatus(false);
        	result.setMsg("查询异常");
    	}
    	
    	
    	return result;
    }
    
    /**
     * 违章代缴订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/carOrderDetail")
    @ResponseBody
    public Result carOrderDetail(String orderNo){
        Result result = new Result();
        result.setMsg("详情查询失败");
        try {
            result = superBankService.carOrderDetail(orderNo);
        } catch (Exception e){
            log.error("查看超级银行家组织详情异常", e);
        }
        return result;
    }
    
    /**导出违章订单*/
    @RequestMapping("/exportCarOrder")
    @ResponseBody
    public Result exportCarOrder(@RequestParam("baseInfo") String baseInfo,HttpServletResponse response,HttpServletRequest request){
        Result result = new Result();
        result.setMsg("导出异常");
        try {
        	if(StringUtils.isNotBlank(baseInfo)){
        		baseInfo = URLDecoder.decode(baseInfo, "utf-8");
        		CarOrder order = JSON.parseObject(baseInfo, CarOrder.class);
        		superBankService.exportCarOrder(response,order);
            }
        } catch (Exception e){
            log.error("导出异常", e);
        }
        return result;
    }
    
    /**
     * 征信订单分页条件查询
     * @param baseInfo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/selectOrderInquiryPage")
    @ResponseBody
    public Result selectOrderInquiryPage(@RequestBody ZxProductOrder baseInfo,
                                         @RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        Result result = new Result();
        try {
            Page<ZxProductOrder> page = new Page<>(pageNo, pageSize);
            zxProductOrderService.selectByPage(baseInfo, page);
            OrderMainSum orderMainSum = zxProductOrderService.selectOrderSum(baseInfo);
            Map<String, Object> map = new HashMap<>();
            map.put("page", page);
            map.put("orderMainSum", orderMainSum);
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(map);
        } catch (Exception e){
            log.error("分页条件查询订单异常", e);
        }
        return result;
    }
    /**
     * 导出超级银行家征信订单
     * @param baseInfo
     */
    @RequestMapping("/exportInquiryOrder")
    @SystemLog(description = "导出超级银行家贷款订单")
    public void exportInquiryOrder(String baseInfo, HttpServletResponse response){
        try {
            if(StringUtils.isNotBlank(baseInfo)){
                baseInfo = URLDecoder.decode(baseInfo, "utf-8");
            }
            ZxProductOrder order = JSONObject.parseObject(baseInfo, ZxProductOrder.class);
            superBankService.exportInquiryOrder(response, order);
        } catch (Exception e){
            log.error("导出超级银行家贷款订单异常", e);
        }
    }
    /**
     * 征信订单详情
     * @param orderNo
     * @return
     */
    @RequestMapping("/orderInquiryDetail")
    @ResponseBody
    public Result orderInquiryDetail(String orderNo){
        Result result = new Result();
        try {
            if(StringUtil.isBlank(orderNo)){
                result.setMsg("订单号不能为空");
                return result;
            }
            ZxProductOrder zxProductOrder = zxProductOrderService.selectByOrderNo(orderNo);
            if (zxProductOrder == null) {
            	result.setMsg("数据为空");
            	return result;
			}
            result.setStatus(true);
            result.setMsg("查询成功");
            result.setData(zxProductOrder);
        } catch (Exception e){
            log.error("征信订单详情查询异常", e);
        }
        return result;
    }

	/**
	 * 获取Mpos机具信息
	 * @return
	 */
	@RequestMapping("/selectMposMachinesList")
	@ResponseBody
	public Result selectMposMachinesList(@RequestBody MposMachines baseInfo,
										 @RequestParam(defaultValue = "1") int pageNo,
										 @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<MposMachines> page = new Page<>(pageNo, pageSize);

			log.info("--------------传入参数----------------" + JSON.toJSONString(baseInfo) + "-----------------------");
			if(StringUtils.isNotEmpty(baseInfo.getSnStart())&&StringUtils.isNotEmpty(baseInfo.getSnEnd())){
				if(baseInfo.getSnStart().length()!=baseInfo.getSnEnd().length()){
					result.setMsg("起始SN号与结束SN号位数不一致");
					return result;
				}
			}
			superBankService.selectMposMachinesList(baseInfo,page);

			HashMap<String,Object> map = new HashMap<String,Object>();
			map.put("page", page);
			result.setStatus(true);
			result.setMsg("操作成功");
			result.setData(map);
		} catch (Exception e){
			log.info("获取Mpos机具信息查询异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	/**
	 * 机具导出
	 * @param baseInfo
	 * @param response
	 */
	@RequestMapping("/mposMachinesExport")
	public void mposMachinesExport(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}

			MposMachines mposMachines = JSONObject.parseObject(baseInfo, MposMachines.class);
			if(StringUtils.isNotEmpty(mposMachines.getSnStart())&&StringUtils.isNotEmpty(mposMachines.getSnEnd())){
				if(mposMachines.getSnStart().length()!=mposMachines.getSnEnd().length()){
					throw new Exception("起始SN号与结束SN号位数不一致");
				}
			}
			superBankService.mposMachinesExport(response, mposMachines);
		} catch (Exception e){
			log.error("导出超级银行家贷款订单异常", e);
		}
	}


	/**
	 * 获取Mpos订单信息
	 * @return
	 */
	@RequestMapping("/selectMposOrderList")
	@ResponseBody
	public Result selectMposOrderList(@RequestBody MposOrder baseInfo,
									  @RequestParam(defaultValue = "1") int pageNo,
									  @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<MposOrder> page = new Page<>(pageNo, pageSize);

			log.info("--------------传入参数----------------" + JSON.toJSONString(baseInfo) + "-----------------------");
			if("-1".equals(baseInfo.getOrgId())){
				baseInfo.setOrgId(null);
			}
			superBankService.selectMposOrderList(baseInfo,page);

			MposOrderSum mposOrderSum = superBankService.selectMposOrderSum(baseInfo);
			HashMap<String,Object> map = new HashMap<>();
			map.put("page", page);
			map.put("mposOrderSum", mposOrderSum);
			result.setStatus(true);
			result.setMsg("操作成功");
			result.setData(map);
		} catch (Exception e){
			log.info("获取Mpos订单信息查询异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}
	/**
	 * 获取Mpos订单信息详情
	 * @return
	 */
	@RequestMapping("/mposOrderDetail")
	@ResponseBody
	public Result mposOrderDetail(@RequestParam String orderNo){
		Result result = new Result();
		try {

			log.info("--------------传入参数----------------" + orderNo + "-----------------------");
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();
			MposOrder mposOrder = superBankService.mposOrderDetail(orderNo);
			if(StringUtils.isNotBlank(mposOrder.getImgUrl())){
				mposOrder.setImgUrl(CommonUtil.getImgUrlAgent(mposOrder.getImgUrl()));
			}
			if(!entityId.equals(String.valueOf(mposOrder.getOrgId()))){
                result.setStatus(false);
                result.setMsg("订单不属于当前组织");
            }else {
                List<MposMachines> mposMachinesList = superBankService.selectMposMachinesByOrderId(mposOrder.getId());
				if(mposMachinesList!=null&& mposMachinesList.size()>0){
					MposMachines mposMachines = mposMachinesList.get(0);
					mposOrder.setShipOrg(mposMachines.getOrgName());
				}
                mposOrder.setMposMachines(mposMachinesList);
                result.setStatus(true);
                result.setMsg("操作成功");
                result.setData(mposOrder);
            }
		} catch (Exception e){
			log.info("获取Mpos订单详情查询异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	/**
	 * 发货
	 * @param baseInfo
	 * @return
	 */
	@RequestMapping("/mposOrderShip")
	@ResponseBody
	public Result mposOrderShip(@RequestBody MposOrder baseInfo){
		Result result = new Result();
		try {

			log.info("--------------传入参数----------------" + JSON.toJSONString(baseInfo) + "-----------------------");

			if(baseInfo.getOrderNo()==null){
				result.setStatus(false);
				result.setMsg("订单编号为空");
				return result;
			}
			MposOrder mposOrder = superBankService.mposOrderDetail(baseInfo.getOrderNo());
			if(mposOrder==null){
				result.setStatus(false);
				result.setMsg("订单不存在");
				return result;
			}
			if (!"2".equals(mposOrder.getStatus())) {
				result.setStatus(false);
				result.setMsg("订单状态不是待发货状态");
				return result;
			}
			if(mposOrder.getShipper()!=null && mposOrder.getShipper()==1){
				result.setStatus(false);
				result.setMsg("该订单已委托平台发货");
				return result;
			}
			UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String entityId = principal.getUserEntityInfo().getEntityId();

			if(StringUtils.isEmpty(entityId)){
				result.setStatus(false);
				result.setMsg("获取组织编号异常");
				return result;
			}
			if(!((mposOrder.getShipper()!=null&&mposOrder.getShipper()==2)&&entityId.equals(String.valueOf(mposOrder.getOrgId())))){
				result.setStatus(false);
				result.setMsg("订单发货方不属于当前组织，不能发货");
				return result;
			}

			if(mposOrder.getShipWay()!=null && mposOrder.getShipWay()==1){
				if(StringUtils.isEmpty(baseInfo.getShipExpress())){
					result.setStatus(false);
					result.setMsg("请选择快递公司");
					return result;
				}
				if(StringUtils.isEmpty(baseInfo.getShipExpressNo())){
					result.setStatus(false);
					result.setMsg("请填写物流编号");
					return result;
				}
			}
			mposOrder.setShipExpress(baseInfo.getShipExpress());
			mposOrder.setShipExpressNo(baseInfo.getShipExpressNo());

			List<String> snList = new ArrayList<>();
			if(StringUtils.isEmpty(baseInfo.getMachinesSnNo())){
				result.setStatus(false);
				result.setMsg("发货sn号为空");
			}else {
				String[] snArray = baseInfo.getMachinesSnNo().split(",");
				if(snArray!=null&&snArray.length>0) {
					int snLenght=0;
					for (int i = 0; i < snArray.length; i++) {
						String sn = snArray[i];
						if(StringUtils.isNotEmpty(sn)){
							snList.add(sn);
							if(i==0){
								snLenght = sn.length();
							}else{
								if(snLenght!=sn.length()){
									result.setStatus(false);
									result.setMsg("起始SN号与结束SN号位数不一致");
									return result;
								}
							}
						}
					}
					if(snList!=null && snList.size()>0) {
						if(snList.size()!=mposOrder.getBuyNum()){
							result.setMsg("发货机具数量与订单购买台数不相符");
							result.setStatus(false);
						}else {
							result = superBankService.mposOrderShip(mposOrder, snList);
						}
					}
				}
			}
		} catch (Exception e){
			log.info("Mpos订单发货异常", e);
			result.setStatus(false);
			result.setMsg("发货异常");
		}
		return result;
	}

	@RequestMapping("/exportMposOrder")
	@SystemLog(description = "导出Mpos订单")
	public void exportMposOrder(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}
			MposOrder mposOrder = JSONObject.parseObject(baseInfo, MposOrder.class);
			superBankService.exportMposOrder(response, mposOrder);
		} catch (Exception e){
			log.error("导出超级银行家mpos订单异常", e);

		}
	}

	@RequestMapping("/mposOrderConsignShip")
	@ResponseBody
	public Result mposOrderConsignShip(@RequestParam String snNos){
		Result result = new Result();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {

			log.info("--------------传入参数----------------" + snNos + "-----------------------");

			if(StringUtils.isEmpty(snNos)){
				result.setStatus(false);
				result.setMsg("参数异常");
				return result;
			}
            UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String entityId = principal.getUserEntityInfo().getEntityId();

            List<String> snList = new ArrayList<>();

			UserLoginInfo loginInfo = CommonUtil.getLoginUser();
			String[] snArray = snNos.split(",");
			if(snArray!=null&&snArray.length>0) {
				for (String sn :snArray){
					if(StringUtils.isNotEmpty(sn)){
						snList.add(sn);
					}
				}
				if(snList!=null && snList.size()>0) {
					for (String sn :snList){
						MposOrder mposOrder = superBankService.mposOrderDetail(sn);

                        if(mposOrder!=null && "2".equals(mposOrder.getStatus())&& mposOrder.getShipper()!=null && mposOrder.getShipper()==2 && entityId.equals(String.valueOf(mposOrder.getOrgId()))){
							mposOrder.setShipper(1);
							mposOrder.setUpdateDate(new Date());
							mposOrder.setUpdateBy(loginInfo.getUsername());
							//操作者：  时间:   操作次订单-委托平台发货
							mposOrder.setConsignRemark("操作者:"+principal.getUsername()+"  时间:"+simpleDateFormat.format(new Date())+"   操作次订单-委托平台发货");
							superBankService.mposOrderUpdate(mposOrder);
						}
					}
				}
			}
			result.setStatus(true);
			result.setMsg("委托成功");



		} catch (Exception e){
			log.info("Mpos订单发货异常", e);
			result.setStatus(false);
			result.setMsg("发货异常");
		}
		return result;
	}

	/**
	 * mpos分润明细查询
	 * @param baseInfo
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/mposProfitDetail")
	@ResponseBody
	public Result mposProfitDetail(@RequestBody UserProfit baseInfo,
								   @RequestParam(defaultValue = "1") int pageNo,
								   @RequestParam(defaultValue = "10") int pageSize) {
		Result result = new Result();
		try {
			if(baseInfo == null){
				result.setMsg("参数不能为空");
				return result;
			}


			Page<UserProfit> page = new Page<>(pageNo, pageSize);
			superBankService.selectMposProfitDetailPage(baseInfo, page);


			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(map);
		} catch (Exception e){
			log.error("分页条件查询Mpos分润明细异常", e);
		}
		return result;
	}

	/**
	 * 导出超级银行家mpos分润明细
	 * @param baseInfo
	 */
	@RequestMapping("/mposProfitDetailExprort")
	public void mposProfitDetailExprort(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "UTF-8");
			}
			UserProfit order = JSONObject.parseObject(baseInfo, UserProfit.class);
			superBankService.mposProfitDetailExprort(response, order);
		} catch (Exception e){
			log.error("导出超级银行家mpos分润明细异常", e);
		}
	}

	@RequestMapping("/selectMposActiveOrderList")
	@ResponseBody
	public Result selectMposActiveOrderList(@RequestBody MposActiveOrder mposActiveOrder,
											@RequestParam(defaultValue = "1") int pageNo,
											@RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<MposActiveOrder> page = new Page<>(pageNo, pageSize);

			log.info("--------------传入参数----------------" + JSON.toJSONString(mposActiveOrder) + "-----------------------");
			if(null != mposActiveOrder.getOrgId() && -1 == mposActiveOrder.getOrgId()){
				mposActiveOrder.setOrgId(null);
			}
			superBankService.selectMposActiveOrderList(mposActiveOrder,page);
			List<MposActiveOrder> list = page.getResult();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					MposActiveOrder mposActiveOrder1 = list.get(i);
					if(StringUtils.isNotEmpty(mposActiveOrder1.getV2MerchantPhone())){
						String phoneMask = mask(mposActiveOrder1.getV2MerchantPhone(),3,4);
						mposActiveOrder1.setV2MerchantPhone(phoneMask);
					}
					list.set(i,mposActiveOrder1);
				}
				page.setResult(list);
			}

			MposActiveOrder mposActiveOrderSum = new MposActiveOrder();
			if(page.getTotalCount() > 0) {
				mposActiveOrderSum = superBankService.selectMposActiveOrderSum(mposActiveOrder);
			}

			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("mposActiveOrderSum", mposActiveOrderSum);

			result.setStatus(true);
			result.setMsg("操作成功");
			result.setData(map);
		} catch (Exception e){
			log.info("获取Mpos激活信息查询异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	@RequestMapping("/exportMposActiveOrder")
	public void exportMposActiveOrder(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}
			MposActiveOrder mposActiveOrder = JSONObject.parseObject(baseInfo, MposActiveOrder.class);
			if(null != mposActiveOrder.getOrgId() && -1 == mposActiveOrder.getOrgId()){
				mposActiveOrder.setOrgId(null);
			}

			superBankService.exportMposActiveOrder(response, mposActiveOrder);
		} catch (Exception e){
			log.error("导出Mpos激活信息异常", e);
		}
	}

	@RequestMapping("/selectMposTradeOrderList")
	@ResponseBody
	public Result selectMposTradeOrderList(@RequestBody MposTradeOrder mposTradeOrder,
										   @RequestParam(defaultValue = "1") int pageNo,
										   @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<MposTradeOrder> page = new Page<>(pageNo, pageSize);

			log.info("--------------传入参数----------------" + JSON.toJSONString(mposTradeOrder) + "-----------------------");
			if(null != mposTradeOrder.getOrgId() && -1 == mposTradeOrder.getOrgId()){
				mposTradeOrder.setOrgId(null);
			}
			superBankService.selectMposTradeOrderList(mposTradeOrder,page);

			List<MposTradeOrder> list = page.getResult();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					MposTradeOrder mposTradeOrder1 = list.get(i);
					if(StringUtils.isNotEmpty(mposTradeOrder1.getV2MerchantPhone())){
						String phoneMask = mask(mposTradeOrder1.getV2MerchantPhone(),3,4);
						mposTradeOrder1.setV2MerchantPhone(phoneMask);
					}
					list.set(i,mposTradeOrder1);
				}
				page.setResult(list);
			}

			MposTradeOrder mposTradeOrderSum = new MposTradeOrder();
			if(page.getTotalCount() > 0) {
				mposTradeOrderSum = superBankService.selectMposTradeOrderSum(mposTradeOrder);
			}

			Map<String, Object> map = new HashMap<>();
			map.put("page", page);
			map.put("mposTradeOrderSum", mposTradeOrderSum);


			result.setStatus(true);
			result.setMsg("操作成功");
			result.setData(map);
		} catch (Exception e){
			log.info("获取Mpos交易信息异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	@RequestMapping("/selectMposTradeOrderDetail")
	@ResponseBody
	public Result selectMposTradeOrderList(@RequestParam String orderNo) {
		Result result = new Result();

		try {
			if (StringUtils.isEmpty(orderNo)) {
				result.setMsg("订单编号为空");
			} else {
				log.info("--------------传入参数----------------" + orderNo + "-----------------------");
				MposTradeOrder mposTradeOrder  = superBankService.selectMposTradeOrderDetail(orderNo);

				result.setStatus(true);
				result.setMsg("操作成功");
				result.setData(mposTradeOrder);
			}
		} catch (Exception e) {
			log.info("获取Mpos交易信息异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	@RequestMapping("/exportMposTradeOrder")
	public void exportMposTradeOrder(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}
			MposTradeOrder mposTradeOrder = JSONObject.parseObject(baseInfo, MposTradeOrder.class);
			if(null != mposTradeOrder.getOrgId() && -1 == mposTradeOrder.getOrgId()){
				mposTradeOrder.setOrgId(null);
			}

			superBankService.exportMposTradeOrder(response, mposTradeOrder);
		} catch (Exception e){
			log.error("导出Mpos交易信息异常", e);
		}
	}

	@RequestMapping("/selectMposMerchantTradeCountList")
	@ResponseBody
	public Result selectMposMerchantTradeCountList(@RequestBody MposMerchantTradeCount mposMerchantTradeCount,
												   @RequestParam(defaultValue = "1") int pageNo,
												   @RequestParam(defaultValue = "10") int pageSize){
		Result result = new Result();
		try {
			Page<MposMerchantTradeCount> page = new Page<>(pageNo, pageSize);

			log.info("--------------传入参数----------------" + JSON.toJSONString(mposMerchantTradeCount) + "-----------------------");
			if(null != mposMerchantTradeCount.getOrgId() && -1 == mposMerchantTradeCount.getOrgId()){
				mposMerchantTradeCount.setOrgId(null);
			}
			superBankService.selectMposMerchantTradeCountList(mposMerchantTradeCount,page);
			List<MposMerchantTradeCount> list = page.getResult();
			if(list!=null && list.size()>0){
				for (int i = 0; i < list.size(); i++) {
					MposMerchantTradeCount mposMerchantTradeCount1 = list.get(i);
					if(StringUtils.isNotEmpty(mposMerchantTradeCount1.getV2MerchantPhone())){
						String phoneMask = mask(mposMerchantTradeCount1.getV2MerchantPhone(),3,4);
						mposMerchantTradeCount1.setV2MerchantPhone(phoneMask);
					}
					list.set(i,mposMerchantTradeCount1);
				}
				page.setResult(list);
			}

			result.setStatus(true);
			result.setMsg("操作成功");
			result.setData(page);
		} catch (Exception e){
			log.info("获取商户交易数据汇总信息异常", e);
			result.setStatus(false);
			result.setMsg("查询异常");
		}
		return result;
	}

	@RequestMapping("/exportMposMerchantTradeCount")
	public void exportMposMerchantTradeCount(String baseInfo, HttpServletResponse response){
		try {
			if(StringUtils.isNotBlank(baseInfo)){
				baseInfo = URLDecoder.decode(baseInfo, "utf-8");
			}
			MposMerchantTradeCount mposMerchantTradeCount = JSONObject.parseObject(baseInfo, MposMerchantTradeCount.class);
			if(null != mposMerchantTradeCount.getOrgId() && -1 == mposMerchantTradeCount.getOrgId()){
				mposMerchantTradeCount.setOrgId(null);
			}

			superBankService.exportMposMerchantTradeCount(response, mposMerchantTradeCount);
		} catch (Exception e){
			log.error("导出商户交易数据汇总信息异常", e);
		}
	}

	/**
	 * Mpos业务设置-获取所有的硬件设备列表
	 */
	@RequestMapping("/getMposProductTypeListAll")
	@ResponseBody
	public Result getMposProductTypeListAll() {
		Result result = new Result();
		try {
			List<MposProductType> list=superBankService.getMposProductTypeListAll();
			result.setStatus(true);
			result.setMsg("查询成功");
			result.setData(list);
		} catch (Exception e) {
			e.printStackTrace();
			result.setMsg("查询所有的硬件设备列表异常");
		}
		return result;
	}

	/**
	 * 给数字打码加*
	 * @param num  数字
	 * @param front 显示前几位
	 * @param end 显示末几位
	 * @return
	 */
	public static String mask(String num, int front, int end) {
		//身份证不能为空
		if (StringUtils.isEmpty(num)) {
			return null;
		}
		//需要截取的长度不能大于要截取字符的长度
		if ((front + end) > num.length()) {
			return null;
		}
		//需要截取的不能小于0
		if (front < 0 || end < 0) {
			return null;
		}
		//计算*的数量
		int asteriskCount = num.length() - (front + end);
		StringBuffer asteriskStr = new StringBuffer();
		for (int i = 0; i < asteriskCount; i++) {
			asteriskStr.append("*");
		}
		String regex = "(\\w{" + String.valueOf(front) + "})(\\w+)(\\w{" + String.valueOf(end) + "})";
		return num.replaceAll(regex, "$1" + asteriskStr + "$3");
	}


}
