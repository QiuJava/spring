package cn.eeepay.boss.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.model.MerchantIncomeBean;
import cn.eeepay.framework.model.ResponseBean;
import cn.eeepay.framework.model.UserCouponBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.Result;

@Controller
@RequestMapping(value = "/activityDetail")
public class ActivityDetailAction {
	private static final Logger log = LoggerFactory.getLogger(ActivityDetailAction.class);
	
	public static final String ZERO_ORDER = "SK000000000000000000";

	@Resource
	private ActivityDetailService activityDetailServiceNoTran;
	@Resource
	private AgentInfoService agentInfoService;
	
	@RequestMapping(value = "/selectHappyBackDetailInfo")
	@ResponseBody
	public Result selectHappyBackDetailInfo(String ids) {
		Result result = new Result();
		try {
			ActivityDetail activityDetail =activityDetailServiceNoTran.getHappyBackDetailById(Integer.parseInt(ids));
			HashMap<String, Object> map = new HashMap<>();
			map.put("mbp", activityDetail);
			if (ZERO_ORDER.equals(activityDetail.getActiveOrder())) {
				List<CashBackDetail> agentCashBackDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(ZERO_ORDER,activityDetail.getId(),1);
				map.put("agentCashBackDetailList", agentCashBackDetailList);
				List<CashBackDetail> agentFullPrizeDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(ZERO_ORDER,activityDetail.getId(),2);
				map.put("agentFullPrizeDetailList", agentFullPrizeDetailList);
				List<CashBackDetail> agentNotFullDeductDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(ZERO_ORDER,activityDetail.getId(),3);
				for (CashBackDetail cashBackDetail : agentNotFullDeductDetailList) {
					if (cashBackDetail.getPreTransferStatus() != null && cashBackDetail.getPreTransferStatus() == 1) {
						cashBackDetail.setEntryStatus(null);
						cashBackDetail.setEntryTime(null);
					}
				}
				map.put("agentNotFullDeductDetailList", agentNotFullDeductDetailList);
			}else {
				List<CashBackDetail> agentCashBackDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(activityDetail.getId(),1);
				map.put("agentCashBackDetailList", agentCashBackDetailList);
				List<CashBackDetail> agentFullPrizeDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(activityDetail.getId(),2);
				map.put("agentFullPrizeDetailList", agentFullPrizeDetailList);
				List<CashBackDetail> agentNotFullDeductDetailList =activityDetailServiceNoTran.queryAgentReturnCashDetailAll(activityDetail.getId(),3);
				for (CashBackDetail cashBackDetail : agentNotFullDeductDetailList) {
					if (cashBackDetail.getPreTransferStatus() != null  && cashBackDetail.getPreTransferStatus() == 1) {
						cashBackDetail.setEntryStatus(null);
						cashBackDetail.setEntryTime(null);
					}
				}
				map.put("agentNotFullDeductDetailList", agentNotFullDeductDetailList);
			}
			result.setData(map);
			result.setStatus(true);
		} catch (Exception e) {
			result.setStatus(false);
			log.error("系统异常", e);
		}
		return result;
	}

	// 欢乐送
	@RequestMapping(value = "/selectActivityDetail.do")
	@ResponseBody
	public Object selectActivityDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) throws Exception {
		ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			activityDetailServiceNoTran.selectActivityDetail(page,principal.getUserEntityInfo().getEntityId(),activityDetail);
		} catch (Exception e) {
			log.error("查询失败----", e);
			e.printStackTrace();
		}
		return page;
	}
	
	//导出业务活动 tgh329
	@SystemLog(description = "导出欢乐送")
	@RequestMapping(value="/exportExcel.do")
	public String exportExcel(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			 HttpServletResponse response){
        try {
        	final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        	ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
        	activityDetailServiceNoTran.exportExcel(page,activityDetail,principal.getUserEntityInfo().getEntityId(),response);
        } catch (Exception e) {
        	log.error("导出业务活动记录失败",e);
        	return "error";
        }
        return null;
    }
	
	/**
	 * 欢乐返活动查询
	 * @param param
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/selectHappyBackDetail.do")
	@ResponseBody
	public Object selectHappyBackDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) throws Exception {
		ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
		// 判断当前登录用户是否有设置业务范围，防止非法操作
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
		if(StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(activityDetail.getMerTeamId())){
			return false;
		}
		try {
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			activityDetailServiceNoTran.selectHappyBackDetail(page,loginAgent,activityDetail);
		} catch (Exception e) {
			log.error("查询失败----", e);
			e.printStackTrace();
		}
		return page;
	}
	
	//导出欢乐返活动明细
	@SystemLog(description = "导出欢乐返")
	@RequestMapping(value="/exportHappyBack.do")
	@ResponseBody
	public void exportHappyBack(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,HttpServletResponse response){
        try {
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
			if(StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(activityDetail.getMerTeamId())){
				log.error("导出欢乐返失败---非法操作");
				return ;
			}
        	activityDetailServiceNoTran.exportHappyBack(page,activityDetail,loginAgent,response);
        } catch (Exception e) {
        	log.error("导出欢乐返失败",e);
        	return ;
        }
        return ;
    }

	//查询总金额
	@RequestMapping(value="/selectTotalMoney.do")
	@ResponseBody
	public Map<String, Object> selectTotalMoney(@RequestParam("baseInfo")String param){
		Map<String,Object> map = new HashMap<>();
		try {
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
			// 判断当前登录用户是否有设置业务范围，防止非法操作
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String accessTeamId = principal.getUserEntityInfo().getAccessTeamId();
			if(StringUtils.isNotBlank(accessTeamId) && !accessTeamId.equals(activityDetail.getMerTeamId())){
				return null;
			}
			map.put("totalData", activityDetailServiceNoTran.selectTotalMoney(activityDetail,loginAgent));
			System.out.println(map);
		} catch (Exception e) {
			log.error("统计总金额失败",e);
		}
		return map;
	}
	
	
	//查询总金额
		@RequestMapping(value="/threeSelectTotalMoney.do")
		@ResponseBody
		public Map<String, Object> threeSelectTotalMoney(@RequestParam("baseInfo")String param){
			Map<String,Object> map = new HashMap<>();
			try {
				AgentInfo loginAgent = agentInfoService.selectByPrincipal();
				ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
				String agentNo = activityDetail.getAgentNode();
				AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
				 boolean auth = agentInfoService.isAuth(loginAgent.getAgentNo(), agentNo);
					if (!auth) {
						return map;
					}
				activityDetail.setAgentNode(agentInfo.getAgentNode());
				map.put("totalData", activityDetailServiceNoTran.selectTotalMoney(activityDetail,agentInfo));
			} catch (Exception e) {
				log.error("统计总金额失败",e);
			}
			return map;
		}
		

    @RequestMapping("/listUserCoupons")
	@ResponseBody
	public ResponseBean listUserCoupons(@RequestBody UserCouponBean userCouponBean, int pageNo, int pageSize){
		try {
			log.info("参数:" + userCouponBean + ",pageNo:" + pageNo + ",pageSize:" + pageSize);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String currentAgentNo =principal.getUserEntityInfo().getEntityId();
			Page<UserCouponBean> page = new Page<>();
			page.setPageNo(pageNo);
			page.setPageSize(pageSize);
			List<UserCouponBean> result = activityDetailServiceNoTran.listUserCouponsByPage(userCouponBean,currentAgentNo, page);
			return new ResponseBean(result,page.getTotalCount());
		}catch (Exception e){
			log.error(e.getMessage());
			return new ResponseBean(e);
		}

	}

	@RequestMapping("/countUserCoupons")
	@ResponseBody
	public ResponseBean countUserCoupons(@RequestBody UserCouponBean userCouponBean){
		try {
			log.info("参数:" + userCouponBean);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String currentAgentNo =principal.getUserEntityInfo().getEntityId();
			UserCouponBean result = activityDetailServiceNoTran.countUserCoupons(userCouponBean, currentAgentNo);
			return new ResponseBean(result);
		}catch (Exception e){
			log.error(e.getMessage());
			return new ResponseBean(e);
		}

	}

	@RequestMapping("/listMerchantIncome")
	@ResponseBody
	public ResponseBean listMerchantIncome(@RequestBody MerchantIncomeBean bean,
										   @RequestParam(defaultValue = "1") int pageNo,
										   @RequestParam(defaultValue = "20")int pageSize){
		try {
			AgentInfo loginAgent = agentInfoService.selectByPrincipal();
			Page<MerchantIncomeBean> page = new Page<>(pageNo,pageSize);
			bean.setLoginAgentNode(loginAgent.getAgentNode());
			List<MerchantIncomeBean> result = activityDetailServiceNoTran.listMerchantIncome(bean, page);
			return new ResponseBean(result, page.getTotalCount());
		}catch (Exception e){
			log.error("listMerchantIncome --> ", e);
			return new ResponseBean(e);
		}
	}
	
	/**
	 * 欢乐返子类型列表
	 * @return
	 */
	@RequestMapping("/queryByactivityTypeNoList")
	@ResponseBody
	public Object queryByactivityTypeNoList(@RequestBody String activityCode) {
		Map<String, Object> msg = new HashMap<>();
		try {
			List<Map<String,Object>> info=activityDetailServiceNoTran.queryByactivityTypeNoList(activityCode);
			msg.put("info", info);
			msg.put("status", true);
		} catch (Exception e) {
			log.error("查询失败！", e);
			msg.put("status", false);
		}
		return msg;
	}
	
		/**
		 * 欢乐返活动查询
		 * @param param
		 * @param page
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/threeSelectHappyBackDetail.do")
		@ResponseBody
		public Object threeSelectHappyBackDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) throws Exception {
			ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
			try {
				String agentNo = activityDetail.getAgentNode();
				AgentInfo agentInfo = agentInfoService.selectByagentNo(agentNo);
				activityDetail.setAgentNode(agentInfo.getAgentNode());
				AgentInfo loginAgent = agentInfoService.selectByPrincipal();
				
				 boolean auth = agentInfoService.isAuth(loginAgent.getAgentNo(), agentNo);
					if (!auth) {
						return page;
					}
				activityDetailServiceNoTran.selectHappyBackDetail(page,agentInfo,activityDetail);
			} catch (Exception e) {
				log.error("查询失败----", e);
				e.printStackTrace();
			}
			return page;
		}
	
}
