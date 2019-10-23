package cn.eeepay.boss.action;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.service.ActivityDetailBackstageService;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.unTransactionalImpl.HappyReturnJobServiceImpl;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/activityDetail")
public class ActivityDetailAction {
	private static final Logger log = LoggerFactory.getLogger(ActivityDetailAction.class);

    @Resource
    private HappyReturnJobServiceImpl happyReturnJobService;

	@Resource
	private ActivityDetailService activityDetailService;

	@Resource
	private ActivityDetailBackstageService activityDetailBackstageService;

	// 业务活动明细
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectActivityDetail.do")
	@ResponseBody
	public Object selectActivityDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) {
		Map<String, Object> msg = new HashMap<>();
		ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
		try {
			String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
			activityDetailService.selectActivityDetail(page,activityDetail);
			int accountCheckTotal=activityDetailBackstageService.countActivityDetailBackstage("1");
			msg.put("page",page);
			msg.put("accountCheckTotal",accountCheckTotal);
			msg.put("status",true);
		} catch (Exception e) {
			log.error("初始化失败----", e);
			e.printStackTrace();
		}
		return msg;
	}
	
	/**
	 * 
	 * @author tans
	 * @date 2017年3月28日 下午5:46:41
	 * @param param
	 * @param page
	 * @param response
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportExcel.do")
	public void exportExcel(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
		 HttpServletResponse response){
		try {
			ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
			String orderNoStr = ad.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				ad.setActiveOrder(newOrderNo);
			}
			activityDetailService.exportExcel(page,ad,response);
		} catch (Exception e) {
			log.error("导出业务活动记录失败");
		    e.printStackTrace();
	    }
	}
	
	/**
	 * 回盘文件模板下载
	 * @author tans
	 * @date 2017年3月28日 下午5:46:49
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/downloadTemplate")
	public String downloadAdjustAccTemplate(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getServletContext().getRealPath("/")+File.separator+"template"+File.separator+"activityDiscountTemplate.xlsx";
		log.info(filePath);
		ResponseUtil.download(response, filePath,"欢乐送补贴扣回回盘模板.xlsx");
		return null;
	}
	
	/**
	 * 回盘导入
	 * 改写activity_detail里面的扣回状态
	 * @author tans
	 * @date 2017年3月28日 下午7:43:59
	 * @return
	 */
	@RequestMapping(value="/importDiscount")
	@ResponseBody
	@SystemLog(description = "欢乐送商户查询回盘导入",operCode="activity.importDiscount")
	public Map<String, Object> importDiscount(@RequestParam("file") MultipartFile file){
		Map<String, Object> msg = new HashMap<>();
		try {
			if (!file.isEmpty()) {
				String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
				if(!format.equals(".xls") && !format.equals(".xlsx")){
					msg.put("status", false);
					msg.put("msg", "回盘导入文件格式错误");
					return msg;
				}
			}
			msg = activityDetailService.importDiscount(file);
		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "回盘导入失败");
			log.error("回盘导入失败",e);
		}
		return msg;
	}
	
	/**
	 * 欢乐送业务批量核算
	 * @author Ivan
	 * @date   2017/03/28
	 * @return
	 */
	@RequestMapping(value = "/happySendActivityAdjust")
	@SystemLog(description = "欢乐送商户查询核算",operCode="activity.happySendActivityAdjust")
	public @ResponseBody Map<String, Object> happySendActivityAdjust(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam("status") String status) {

		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",","','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg=activityDetailService.updateAdjust(ad,page,status);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","核算操作异常!");
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
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyBackDetail.do")
	@ResponseBody
	public Object selectHappyBackDetail(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page) {
		Map<String, Object> msg = new HashMap<String, Object>();
		msg.put("status", false);
		msg.put("msg", "查询失败");
		try {
			ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
			String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
			activityDetailService.selectHappyBackDetail(page,activityDetail);
			Map<String, Object> totalData = activityDetailService.selectHappyBackTotalAmount(activityDetail);
			int accountCheckTotal=activityDetailBackstageService.countActivityDetailBackstage("2");
			int liquidationTotal=activityDetailBackstageService.countActivityDetailBackstage("3");
			int rewardIsBookedTotal=activityDetailBackstageService.countActivityDetailBackstage("4");
			msg.put("accountCheckTotal", accountCheckTotal);//财务核算
			msg.put("liquidationTotal", liquidationTotal);//清算
			msg.put("rewardIsBookedTotal", rewardIsBookedTotal);//奖励入账

			msg.put("status", true);
			msg.put("msg", "查询成功");
			msg.put("page", page);
			msg.put("totalData", totalData);
		} catch (Exception e) {
			log.error("查询失败----", e);
		}
		return msg;
	}


	/**
	 * 获取欢乐返商户详情
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyBackDetailInfo")
	@ResponseBody
	public Map<String, Object> selectHappyBackDetailInfo(String ids) throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {

			ActivityDetail activityDetail =activityDetailService.selectHappyBackDetailById(Integer.parseInt(ids));
			if(activityDetail!=null){
				List<CashBackDetail> list =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),1);
				List<CashBackDetail> acbfpList =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),2);
				List<CashBackDetail> acbnfbList =activityDetailService.selectAgentReturnCashDetailAll(activityDetail.getId(),3);
				msg.put("acbList", list);
				msg.put("acbfpList", acbfpList);
				msg.put("acbnfbList", acbnfbList);
				msg.put("mbp", activityDetail);
				msg.put("status", true);
				msg.put("msg", "查询成功");
			}else{
				msg.put("status", false);
				msg.put("msg", "查询失败");
			}

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("ids----", e);
		}
		return msg;
	}

	/**
	 * 获取欢乐返商户详情
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value = "/selectHappyTixianSwitch")
	@ResponseBody
	public Map<String, Object> selectHappyTixianSwitch() throws Exception {
		Map<String, Object> msg = new HashMap<String, Object>();
		try {
			String happyTixianSwitch = activityDetailService.selectHappyTixianSwitch();
			if(happyTixianSwitch!=null){
				msg.put("happyTixianSwitch", happyTixianSwitch);
			}else{
				msg.put("happyTixianSwitch", "0");
			}
			msg.put("status", true);
			msg.put("msg", "查询成功");
			log.info("\nhappyTixianSwitch="+happyTixianSwitch);

		} catch (Exception e) {
			msg.put("status", false);
			msg.put("msg", "查询失败");
			log.error("ids----", e);
		}
		return msg;
	}
	
	//导出欢乐返活动明细
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportHappyBack.do")
	public String exportHappyBack(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,HttpServletResponse response){
        try {
        	ActivityDetail activityDetail = JSON.parseObject(param,ActivityDetail.class);
        	String orderNoStr = activityDetail.getActiveOrder();
			String newOrderNo ="";
			if(orderNoStr != null && !"".equals(orderNoStr)){
				newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
				activityDetail.setActiveOrder(newOrderNo);
			}
        	activityDetailService.exportHappyBack(page,activityDetail,response);
        } catch (Exception e) {
        	log.error("导出欢乐返失败",e);
        	return "error";
        }
        return null;
    }
	
	/**
	 * 欢乐返清算核算
	 * @author tans
	 * @date 2017年6月27日 上午9:51:02
	 * @return
	 */
	@RequestMapping(value="/liquidation.do")
	@ResponseBody
	@SystemLog(description = "欢乐返清算核算",operCode="activity.liquidation")
	public Map<String, Object> liquidation(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam String liquidationStatus){

		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			 msg = activityDetailService.updateLiquidation(ad,page,liquidationStatus);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返清算核算操作异常!");
			msg.put("status", false);
		}
		return msg;
	}
	
	/**
	 * 欢乐返财务核算
	 * @author tans
	 * @date 2017年6月27日 上午9:51:02
	 * @param
	 * @return
	 */
	@RequestMapping(value="/accountCheck.do")
	@ResponseBody
	@SystemLog(description = "欢乐返财务核算",operCode="activity.accountCheck")
	public Map<String, Object> accountCheck(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page,
			@RequestParam String accountCheckStatus){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}

		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.updateAccountCheck(ad,page,accountCheckStatus);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返财务核算操作异常!");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 批量奖励入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/rewardIsBooked")
	@ResponseBody
	@SystemLog(description = "批量奖励入账",operCode="activity.rewardIsBooked")
	public Map<String, Object> rewardIsBooked(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.rewardIsBooked(ad,page);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","批量奖励入账操作异常!");
			msg.put("status", false);
		}
		return msg;
	}

	/**
	 * 奖励入账
	 * @param
	 * @return
	 */
	@RequestMapping(value="/oneRewardIsBooked")
	@ResponseBody
	@SystemLog(description = "奖励入账",operCode="activity.oneRewardIsBooked")
	public Map<String, Object> oneRewardIsBooked(@RequestParam Integer id){
		Map<String, Object> msg=null;
		try {
			msg=activityDetailService.oneRewardIsBooked(id);
		}catch (Exception e){
			e.printStackTrace();
		}
		return msg;
	}

	/**
	 * 欢乐返批量奖励入账
	 * @param param
	 * @return
	 */
	@RequestMapping(value="/joyToAccount")
	@ResponseBody
	@SystemLog(description = "欢乐返批量奖励入账",operCode="activity.joyToAccount")
	public Map<String, Object> joyToAccount(
			@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<ActivityDetail> page){
		ActivityDetail ad = JSON.parseObject(param,ActivityDetail.class);
		String orderNoStr = ad.getActiveOrder();
		String newOrderNo ="";
		if(orderNoStr != null && !"".equals(orderNoStr)){
			newOrderNo="'"+orderNoStr.replaceAll(",", "','")+"'";
			ad.setActiveOrder(newOrderNo);
		}
		Map<String, Object> msg = new HashMap<String, Object>();
		try{
			msg = activityDetailService.joyToAccount(ad,page);
			return msg;
		}catch (Exception e){
			e.printStackTrace();
			msg.put("msg","欢乐返批量奖励入账异常!");
			msg.put("status", false);
		}
		return msg;
	}

}
