package cn.eeepay.framework.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import cn.eeepay.boss.action.ActivityDetailAction;
import cn.eeepay.framework.dao.ActivityDetailDao;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.ActivityDetail;
import cn.eeepay.framework.model.AgentInfo;
import cn.eeepay.framework.model.CashBackDetail;
import cn.eeepay.framework.model.MerchantIncomeBean;
import cn.eeepay.framework.model.TerminalInfo;
import cn.eeepay.framework.model.UserCouponBean;
import cn.eeepay.framework.model.UserLoginInfo;
import cn.eeepay.framework.service.ActivityDetailService;
import cn.eeepay.framework.service.AgentInfoService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import cn.eeepay.framework.util.ToolUtils;

public class ActivityDetailServiceImpl{
	
	private ActivityDetailDao activityDetailDao;

	
	private AgentInfoDao agentInfoDao;
	
	private SysDictDao sysDictDao;
	
	
	private AgentInfoService agentInfoService;
	
	
	private ActivityDetailService activityDetailService;

	
	public List<ActivityDetail> selectActivityDetail(Page<ActivityDetail> page,String loginAgentNo,ActivityDetail activityDetail) {
		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
		if (loginAgent == null){
			return null;
		}
		return activityDetailDao.selectActivityDetail(page,loginAgent.getAgentNode(),activityDetail);
	}

	
	public List<TerminalInfo> selectTermi(String phone) {
		return activityDetailDao.selectTermi(phone);
	}
	
	
	public void exportExcel(Page<ActivityDetail> page, ActivityDetail activityDetail,String loginAgentNo, HttpServletResponse response) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(loginAgentNo);
    	List<ActivityDetail> list = activityDetailDao.selectActivityDetail(page,loginAgent.getAgentNode(),activityDetail);
    	String fileName = "欢乐送"+sdf.format(new Date())+".xls" ;
 	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
	    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);    
	    List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
	    Map<String,String> map = null;
	    for(ActivityDetail item: list){
	    	map = new HashMap<>();
	    	map.put("id", String.valueOf(item.getId()));
	    	map.put("activeOrder", item.getActiveOrder());
	    	map.put("cashOrder", item.getCashOrder());
	    	map.put("activeTime", item.getActiveTime()==null?"":sdfTime.format(item.getActiveTime()));
	    	map.put("merchantName", item.getMerchantName());
	    	map.put("enterTime", item.getEnterTime()==null?"":sdfTime.format(item.getEnterTime()));
	    	map.put("agentNo", item.getAgentNo());
	    	map.put("agentName", item.getAgentName());
	    	map.put("oneAgentNo", item.getOneAgentNo());
	    	map.put("oneAgentName", item.getOneAgentName());
	    	map.put("transTotal", StringUtil.filterNull(item.getTransTotal()));
	    	map.put("frozenAmout", StringUtil.filterNull(item.getFrozenAmout()));
	    	
	    	//=======优化tgh502======v
//	    	map.put("status", item.getStatusStr());
	    	List<Map<String, Object>> statusMap = sysDictDao.selectFromSysDict("ACTIVITY_STATUS");
	    	Integer status = item.getStatus();
	    	for (Map<String, Object> map2 : statusMap) {
	    		if (status != null) {
	    			if (map2.get("sys_value").equals(status.toString())) {
	    				map.put("status", map2.get("sys_name") == null ? "" : map2.get("sys_name").toString());
	    			}
	    		}
			}
//	    	map.put("checkStatus", item.getCheckStatusStr());
	    	List<Map<String, Object>> checkStatusMap = sysDictDao.selectFromSysDict("CHECK_STATUS");
	    	Integer checkStatus = item.getCheckStatus();
	    	for (Map<String, Object> map2 : checkStatusMap) {
	    		if (checkStatus != null) {
	    			if (map2.get("sys_value").equals(checkStatus.toString())) {
	    				map.put("checkStatus", map2.get("sys_name") == null ? "" : map2.get("sys_name").toString());
	    			}
	    		}
			}
	    	//==============^
	    	
	    	map.put("targetAmout", StringUtil.filterNull(item.getTargetAmout()));
	    	map.put("dicountStatus", item.getDiscountStatus()!=null
	    			&&item.getDiscountStatus()==1?"已扣回":"未扣回");
	    	map.put("acqEnname", item.getAcqEnname());
	    	map.put("cashTime", item.getCashTime()==null?"":sdfDate.format(item.getCashTime()));
	    	map.put("settleTransferId", item.getSettleTransferId());
	    	map.put("merchantFee", StringUtil.filterNull(item.getMerchantFee()));
	    	map.put("merchantFeeAmount", StringUtil.filterNull(item.getMerchantFeeAmount()));
	    	map.put("merchantOutAmount", StringUtil.filterNull(item.getMerchantOutAmount()));
	    	map.put("merchantSettleDate", item.getMerchantSettleDate()==null?"":sdfDate.format(item.getMerchantSettleDate()));
	    	data.add(map);
	    }
	    ListDataExcelExport export = new ListDataExcelExport();
	    String[] cols = new String[]{"id","activeOrder","cashOrder","activeTime","merchantName"
	    		,"enterTime","transTotal","merchantFee","frozenAmout","cashTime","status",
	    		"targetAmout","agentName","agentNo","oneAgentName","oneAgentNo","checkStatus",
	    		"dicountStatus"};
	    String[] colsName = new String[]{"序号","激活流水号","提现流水号","激活时间","商户名称","进件时间","交易金额","交易手续费",
	    		"冻结金额","商户提现时间","活动状态","活动任务金额","所属代理商名称","所属代理商编号",
	    		"一级代理商名称","一级代理商编号","核算状态","是否返回"};
	    OutputStream ouputStream = response.getOutputStream();
        export.export(cols, colsName, data, response.getOutputStream());
        ouputStream.close();
     
	}

	
	public String getServiceId(String activityCode) {
		return activityDetailDao.getServiceId(activityCode);
	}
	/**
	 * 截取代理商节点的前level级的agentNode
	 * <ul>
	 *     <li>
	 *         agentNode = "0-1572-"
	 *         searchAgent.agentLevel = 2
	 *         return "0-1572-"
	 *     </li>
	 *      <li>
	 *         agentNode = "0-1572-1574-"
	 *         searchAgent.agentLevel = 2
	 *         return "0-1572-1574-"
	 *     </li>
	 *     <li>
	 *         agentNode = "0-1572-1574-1576-"
	 *         searchAgent.agentLevel = 2
	 *         return "0-1572-1574-"
	 *     </li>
	 * </ul>
	 * param agentNode
	 * param searchAgent
	 * return
	 */
	private static String cutOutTopNAgentNode(String agentNode, AgentInfo searchAgent){
		if (StringUtils.isBlank(agentNode) || searchAgent == null){
			return "";
		}
		Pattern pattern = Pattern.compile("^(0-(?:(?:\\d+)-){1," + (searchAgent.getAgentLevel() + 1) + "}).*$");
		Matcher matcher = pattern.matcher(agentNode);
		if (matcher.matches()){
			return matcher.group(1);
		}
		return "";
	}
	
	public List<ActivityDetail>  selectHappyBackDetail(Page<ActivityDetail> page, AgentInfo loginAgent, ActivityDetail activityDetail) {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		List<ActivityDetail> list = activityDetailDao.selectHappyBackDetail(page,loginAgent,activityDetail);
		String agentNode = activityDetail.getAgentNode();//传入的代理商节点条件
		Map<String, AgentInfo> searchAgentChildren = null;
		AgentInfo searchAgent = null;
		if (StringUtils.isBlank(agentNode)) {
			agentNode = agentInfo.getAgentNode();//如果查询的全部,就是查询当前代理商的所有数据
		}
//		if (StringUtils.isNotBlank(agentNode)) {
			searchAgent = agentInfoDao.selectByAgentNode(agentNode);
			//找到条件节点下所有的直属下级代理商
			searchAgentChildren = ToolUtils.collection2Map(agentInfoService.selectSelfAndDirectChildren(searchAgent.getAgentNo()), new ToolUtils.Transformer<AgentInfo>() {
				
				public String transform(AgentInfo value) {
					return value.getAgentNode();
				}
			});
//		}
		List<Map<String,Object>> listMap = activityDetailDao.selectActivityTypeNo();
		for (ActivityDetail detail : list) {
			if (agentInfo.getAgentLevel() != 1) {
				detail.setFullAmount("");
				detail.setEmptyAmount("");
			} 
			
			if (ActivityDetailAction.ZERO_ORDER.equals(detail.getActiveOrder())) {
				CashBackDetail agentFullPrizeDetail =activityDetailService.queryAgentReturnCashDetailAll(ActivityDetailAction.ZERO_ORDER,detail.getId(),2,entityId);
				if (agentFullPrizeDetail != null ) {
					BigDecimal fullPrizAmount = agentFullPrizeDetail.getCashBackAmount();
					if (fullPrizAmount != null) {
						detail.setFullAmount(fullPrizAmount.toString());
					}
				}
				
				CashBackDetail agentNotFullDeductDetail =activityDetailService.queryAgentReturnCashDetailAll(ActivityDetailAction.ZERO_ORDER,detail.getId(),3,entityId);
				if (agentNotFullDeductDetail!=null) {
					BigDecimal fotFullDeductAmount = agentNotFullDeductDetail.getCashBackAmount();
					if (fotFullDeductAmount != null) {
						detail.setEmptyAmount(fotFullDeductAmount.toString());
					}
				}
			}else {
				CashBackDetail agentFullPrizeDetail =activityDetailService.queryAgentReturnCashDetailAll(detail.getId(),2,entityId);
				if (agentFullPrizeDetail != null ) {
					BigDecimal fullPrizAmount = agentFullPrizeDetail.getCashBackAmount();
					if (fullPrizAmount != null) {
						detail.setFullAmount(fullPrizAmount.toString());
					}
				}
				CashBackDetail agentNotFullDeductDetail =activityDetailService.queryAgentReturnCashDetailAll(detail.getId(),3,entityId);
				if (agentNotFullDeductDetail!=null) {
					BigDecimal fotFullDeductAmount = agentNotFullDeductDetail.getCashBackAmount();
					if (fotFullDeductAmount != null) {
						detail.setEmptyAmount(fotFullDeductAmount.toString());
					}
				}
			}
			
			detail.setAgentLevel(loginAgent.getAgentLevel());
			detail.setAgentType(loginAgent.getAgentType());
			detail.setCurrentAgentNo(entityId);
			String activeOrder = detail.getActiveOrder();//激活订单号
			ActivityDetail info = activityDetailDao.selectCashBackDetail(agentInfo.getAgentNo(),detail.getId());
			if (info != null) {
				detail.setEntryStatus(info.getEntryStatus());//当前登录代理商返现入账状态
				detail.setCashBackAmount(info.getCashBackAmount());
				detail.setCashBackSwitch(info.getCashBackSwitch());
			}
			for (Map<String, Object> map : listMap) {
				if (map.get("activity_type_no").toString().equals(detail.getActivityTypeNo())) {
					detail.setActivityTypeNo(map.get("activity_type_name").toString());
					break;
				}
			}
			//显示直属下级代理商相关信息 (当前订单所对应的当前登录代理商的下级代理商,如果就是当前登录代理商,显示为空)
			AgentInfo childrenAgent = searchAgentChildren.get(cutOutTopNAgentNode(detail.getAgentNode(), agentInfo));//直属下级代理商
			if (childrenAgent != null) {
				String directAgentNo = childrenAgent.getAgentNo();
				if (!agentInfo.getAgentNode().equals(childrenAgent.getAgentNode())) {//如果节点等于当前登录代理商的节点,直属下级显示为空
					ActivityDetail info2 = activityDetailDao.selectCashBackDetail(directAgentNo,detail.getId());
					if (info2 != null) {
						detail.setDirectEntryStatus(info2.getEntryStatus());
						detail.setDirectCashBackAmount(info2.getCashBackAmount());
						detail.setDirectCashBackSwitch(info2.getCashBackSwitch());
					}
					AgentInfo directAgentInfo = agentInfoDao.selectByAgentNo(directAgentNo);
					if (directAgentInfo != null) {
						detail.setDirectAgentNo(directAgentInfo.getAgentNo());
						detail.setDirectAgentName(directAgentInfo.getAgentName());
					}
				}
			}
		}
		return list;
	}

	private static Map<String, String> activityCodeMap = new HashMap<>();
	private static Map<String, String> statusMap = new HashMap<>();
	private static Map<String, String> settleStatusMap = new HashMap<>();
	private static Map<String, String> isStandardMap = new HashMap<>();
	private static Map<String, String> repeatRegisterMap = new HashMap<>();
	static {
		activityCodeMap.put("008", "欢乐返-循环送");
		activityCodeMap.put("009", "欢乐返");
		activityCodeMap.put("021", "欢乐返128");
		statusMap.put("1", "未激活");
		statusMap.put("2", "已激活");
		statusMap.put("6", "已返鼓励金");
		statusMap.put("7", "已扣分润账户");
		statusMap.put("8", "预调账已发起");
		statusMap.put("9", "已奖励");
		settleStatusMap.put("1", "同意");
		settleStatusMap.put("2", "不同意");
		settleStatusMap.put("3", "未核算");
		isStandardMap.put("0", "未达标");
		isStandardMap.put("1", "已达标");
		repeatRegisterMap.put("0", "否");
		repeatRegisterMap.put("1", "是");
	}

	
	public void exportHappyBack(Page<ActivityDetail> page, ActivityDetail activityDetail, AgentInfo loginAgent, HttpServletResponse response) throws IOException {
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String entityId = principal.getUserEntityInfo().getEntityId();
		AgentInfo agentInfo = agentInfoDao.selectByAgentNo(entityId);
		boolean isFirstAgent = agentInfo != null && agentInfo.getAgentLevel().equals(1);

    	List<ActivityDetail> list = activityDetailDao.selectHappyBackDetail(page, loginAgent,activityDetail);
    	String fileName = "欢乐返"+new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())+".xls" ;
 	    String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
	    response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);    
	    List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
	    List<Map<String,Object>> listMap = activityDetailDao.selectActivityTypeNo();
	    Map<String,String> map = null;
		Map<String, AgentInfo> searchAgentChildren = null;
		AgentInfo searchAgent = null;
		String agentNode = activityDetail.getAgentNode();//传入的代理商节点条件
		if (StringUtils.isBlank(agentNode)) {
			agentNode = agentInfo.getAgentNode();//如果查询的全部,就是查询当前代理商的所有数据
		}
		searchAgent = agentInfoDao.selectByAgentNode(agentNode);
		//找到条件节点下所有的直属下级代理商
		searchAgentChildren = ToolUtils.collection2Map(agentInfoService.selectSelfAndDirectChildren(searchAgent.getAgentNo()), new ToolUtils.Transformer<AgentInfo>() {
			
			public String transform(AgentInfo value) {
				return value.getAgentNode();
			}
		});
	    for(ActivityDetail item: list){

			if (agentInfo.getAgentLevel() != 1) {
				item.setFullAmount("");
				item.setEmptyAmount("");
			}

			if (ActivityDetailAction.ZERO_ORDER.equals(item.getActiveOrder())) {
				CashBackDetail agentFullPrizeDetail =activityDetailService.queryAgentReturnCashDetailAll(ActivityDetailAction.ZERO_ORDER,item.getId(),2,entityId);
				if (agentFullPrizeDetail != null ) {
					BigDecimal fullPrizAmount = agentFullPrizeDetail.getCashBackAmount();
					if (fullPrizAmount != null) {
						item.setFullAmount(fullPrizAmount.toString());
					}
				}

				CashBackDetail agentNotFullDeductDetail =activityDetailService.queryAgentReturnCashDetailAll(ActivityDetailAction.ZERO_ORDER,item.getId(),3,entityId);
				if (agentNotFullDeductDetail!=null) {
					BigDecimal fotFullDeductAmount = agentNotFullDeductDetail.getCashBackAmount();
					if (fotFullDeductAmount != null) {
						item.setEmptyAmount(fotFullDeductAmount.toString());
					}
				}
			}else {
				CashBackDetail agentFullPrizeDetail =activityDetailService.queryAgentReturnCashDetailAll(item.getId(),2,entityId);
				if (agentFullPrizeDetail != null ) {
					BigDecimal fullPrizAmount = agentFullPrizeDetail.getCashBackAmount();
					if (fullPrizAmount != null) {
						item.setFullAmount(fullPrizAmount.toString());
					}
				}
				CashBackDetail agentNotFullDeductDetail =activityDetailService.queryAgentReturnCashDetailAll(item.getId(),3,entityId);
				if (agentNotFullDeductDetail!=null) {
					BigDecimal fotFullDeductAmount = agentNotFullDeductDetail.getCashBackAmount();
					if (fotFullDeductAmount != null) {
						item.setEmptyAmount(fotFullDeductAmount.toString());
					}
				}
			}

			ActivityDetail info = activityDetailDao.selectCashBackDetail(agentInfo.getAgentNo(),item.getId());
			if (info != null) {
				item.setEntryStatus(info.getEntryStatus());//当前登录代理商返现入账状态
				item.setCashBackAmount(info.getCashBackAmount());
				item.setCashBackSwitch(info.getCashBackSwitch());
			}

			//显示直属下级代理商相关信息 (当前订单所对应的当前登录代理商的下级代理商,如果就是当前登录代理商,显示为空)
			AgentInfo childrenAgent = searchAgentChildren.get(cutOutTopNAgentNode(item.getAgentNode(), agentInfo));//直属下级代理商
			if (childrenAgent != null) {
				String directAgentNo = childrenAgent.getAgentNo();
				if (!agentInfo.getAgentNode().equals(childrenAgent.getAgentNode())) {//如果节点等于当前登录代理商的节点,直属下级显示为空
					ActivityDetail info2 = activityDetailDao.selectCashBackDetail(directAgentNo,item.getId());
					if (info2 != null) {
						item.setDirectEntryStatus(info2.getEntryStatus());
						item.setDirectCashBackAmount(info2.getCashBackAmount());
						item.setDirectCashBackSwitch(info2.getCashBackSwitch());
					}
					AgentInfo directAgentInfo = agentInfoDao.selectByAgentNo(directAgentNo);
					if (directAgentInfo != null) {
						item.setDirectAgentNo(directAgentInfo.getAgentNo());
						item.setDirectAgentName(directAgentInfo.getAgentName());
					}
				}
			}

	    	map = new HashMap<>();
	    	map.put("id", String.valueOf(item.getId()));
	    	map.put("activeOrder", item.getActiveOrder());
	    	map.put("activeTime", formatData(item.getActiveTime()));
			map.put("activityCode", StringUtils.trimToEmpty(activityCodeMap.get(item.getActivityCode())));
			//欢乐返子类型显示
			String activityTypeNo = item.getActivityTypeNo();
			for (Map<String, Object> m : listMap) {
				if (m.get("activity_type_no").toString().equals(item.getActivityTypeNo())) {
					activityTypeNo = m.get("activity_type_name").toString();
					break;
				}
			}
			map.put("activityTypeNo",activityTypeNo);
	    	map.put("merchantName", item.getMerchantName());
	    	map.put("merchantNo", item.getMerchantNo());
	    	map.put("merGroup", item.getMerGroup());
	    	map.put("agentName", item.getAgentName());
	    	map.put("agentNo", item.getAgentNo());
			map.put("repeatRegister", StringUtils.trimToEmpty(repeatRegisterMap.get(item.getRepeatRegister()+"")));
	    	map.put("enterTime", formatData(item.getEnterTime()));
	    	map.put("transTotal", StringUtil.filterNull(item.getTransTotal()));
//	    	map.put("merchantFee",StringUtil.filterNull(item.getMerchantFee()));
			map.put("cumulateTransAmount", item.getCumulateTransAmount());
			map.put("overdueTime", formatData(item.getOverdueTime()));
			map.put("cumulateAmountMinus", item.getCumulateAmountMinus());
			map.put("cumulateAmountAdd", item.getCumulateAmountAdd());
			map.put("currentAgentNo", item.getCurrentAgentNo());
			map.put("cashBackAmount",StringUtil.filterNull(item.getCashBackAmount()));
			if (isFirstAgent){
				map.put("emptyAmount",StringUtil.filterNull(item.getEmptyAmount()));
				map.put("fullAmount",StringUtil.filterNull(item.getFullAmount()));
			}
//			if (isFirstAgent){
//				map.put("cashBackAmount",StringUtil.filterNull(item.getCashBackAmount()));
//				map.put("emptyAmount",StringUtil.filterNull(item.getEmptyAmount()));
//				map.put("fullAmount",StringUtil.filterNull(item.getFullAmount()));
//			}
			map.put("cashBackSwitch", OpenOrCloseStatus(item.getCashBackSwitch()));//返现开关 
			map.put("entryStatus", isEnter(item.getEntryStatus()));
			map.put("directAgentNo", item.getDirectAgentNo());
			map.put("directAgentName", item.getDirectAgentName());
			map.put("directCashBackSwitch",OpenOrCloseStatus(item.getDirectCashBackSwitch()));
			map.put("directCashBackAmount",StringUtil.filterNull(item.getDirectCashBackAmount()));
			map.put("directEntryStatus",isEnter(item.getDirectEntryStatus()));
			map.put("status", StringUtils.trimToEmpty(statusMap.get(item.getStatus() + "")));
			if (isFirstAgent){
				String isStandard = item.getIsStandard();
				if ("1".equals(isStandard)) {
					isStandard = "已达标";
				}else if ("0".equals(isStandard)) {
					isStandard = "未达标";
				}
				map.put("isStandard",isStandard);
				map.put("standardTime", formatData(item.getStandardTime()));
				map.put("minusAmountTime", formatData(item.getMinusAmountTime()));
				map.put("addAmountTime", formatData(item.getAddAmountTime()));
			}
//	    	map.put("oneAgentName", item.getOneAgentName());
//	    	map.put("oneAgentNo", item.getOneAgentNo());
			map.put("liquidationStatus", StringUtils.trimToEmpty(settleStatusMap.get(item.getLiquidationStatus())));
			map.put("accountCheckStatus", StringUtils.trimToEmpty(settleStatusMap.get(item.getAccountCheckStatus())));
//	    	map.put("liquidationTime",formatData(item.getLiquidationTime()));
//	    	map.put("accountCheckTime",formatData(item.getAccountCheckTime()));
	    	data.add(map);
	    }
	    ListDataExcelExport export = new ListDataExcelExport();
		String[] cols ;
		String[] colsName;
	    if (!isFirstAgent){
			cols = new String[]{"id","activeOrder","activeTime","activityCode","activityTypeNo","merchantName","merchantNo","merGroup","agentName","agentNo","repeatRegister","enterTime","transTotal",
					"cumulateTransAmount","overdueTime","cumulateAmountMinus","cumulateAmountAdd",
					"currentAgentNo","cashBackAmount",
//					"emptyAmount","fullAmount",
					"cashBackSwitch","entryStatus","directAgentNo","directAgentName","directCashBackSwitch","directCashBackAmount","directEntryStatus",
					"status",
//					"isStandard","standardTime","minusAmountTime","addAmountTime",
					"liquidationStatus","accountCheckStatus"
					};
			colsName = new String[]{"序号","激活流水号","激活时间","活动类型","欢乐返子类型","商户名称","商户编号","商户组织","所属代理商名称","所属代理商编号","是否重复注册","进件时间","交易金额",
					"累计交易金额","截止累计日期","累计交易（扣）","累计交易（奖）",
					"当前代理商编号","当前代理商返现金额",
//					"代理商未满扣N元","代理商满奖M元",
					"当前代理商返现开关状态","当前代理商返现入账状态",
					"直属下级代理商编号","直属下级代理商名称","直属下级返现开关状态","直属下级返现金额","直属下级返现入账状态",
					"活动状态",
//					"奖励是否达标","奖励达标时间","扣款时间","奖励时间",
					"清算核算状态",
					"账务核算状态",
					};
		}else{
			cols = new String[]{"id","activeOrder","activeTime","activityCode","activityTypeNo","merchantName","merchantNo","merGroup","agentName","agentNo","enterTime","transTotal",
					"cumulateTransAmount","overdueTime","cumulateAmountMinus","cumulateAmountAdd",
					"currentAgentNo","cashBackAmount","emptyAmount","fullAmount",
					"cashBackSwitch","entryStatus","directAgentNo","directAgentName","directCashBackSwitch","directCashBackAmount","directEntryStatus",
					"status",
					"isStandard","standardTime","minusAmountTime","addAmountTime",
					"liquidationStatus","accountCheckStatus"
					};
			colsName = new String[]{"序号","激活流水号","激活时间","活动类型","欢乐返子类型","商户名称","商户编号","商户组织","所属代理商名称","所属代理商编号","进件时间","交易金额",
					"累计交易金额","截止累计日期","累计交易（扣）","累计交易（奖）",
					"当前代理商编号","当前代理商返现金额","代理商未满扣N元","代理商满奖M元",
					"当前代理商返现开关状态","当前代理商返现入账状态",
					"直属下级代理商编号","直属下级代理商名称","直属下级返现开关状态","直属下级返现金额","直属下级返现入账状态",
					"活动状态",
					"奖励是否达标","奖励达标时间","扣款时间","奖励时间",
					"清算核算状态",
					"账务核算状态",
					};
		}
	    OutputStream ouputStream = response.getOutputStream();
        export.export(cols, colsName, data, response.getOutputStream());
        ouputStream.close();
	}

	private String OpenOrCloseStatus(String status) {
		if ("1".equals(status)) {
			status = "打开";
		}else if ("0".equals(status)) {
			status = "关闭";
		}
		return status;
	}
	private String isEnter(String status) {
		if ("1".equals(status)) {
			status = "已入账";
		}else if ("0".equals(status)) {
			status = "未入账";
		}
		return status;
	}

	private String formatData(Date date){
		if (date == null){
			return "";
		}
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
	}

	
	public Map<String,Object> selectTotalMoney(ActivityDetail activityDetail,AgentInfo loginAgent) {
		return activityDetailDao.selectTotalMoney(activityDetail, loginAgent);
	}
	
	public List<UserCouponBean> listUserCouponsByPage(UserCouponBean userCouponBean, String currentAgentNo, Page<UserCouponBean> page) {
		if (StringUtils.isBlank(currentAgentNo)){
			return null;
		}
		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(currentAgentNo);
		currentAgentNo = loginAgent == null ? currentAgentNo : loginAgent.getAgentNode();
		if (StringUtils.isNotBlank(userCouponBean.getAgentNo())){
			AgentInfo searchAgent = agentInfoDao.selectByAgentNo(userCouponBean.getAgentNo());
			if (searchAgent != null){
				userCouponBean.setAgentNo(searchAgent.getAgentNode());
			}
		}

		return activityDetailDao.listUserCouponsByPage(userCouponBean, currentAgentNo, page);
	}

	
	public UserCouponBean countUserCoupons(UserCouponBean userCouponBean, String currentAgentNo) {
		if (StringUtils.isBlank(currentAgentNo)){
			return null;
		}
		AgentInfo loginAgent = agentInfoDao.selectByAgentNo(currentAgentNo);
		currentAgentNo = loginAgent == null ? currentAgentNo : loginAgent.getAgentNode();
		if (StringUtils.isNotBlank(userCouponBean.getAgentNo())){
			AgentInfo searchAgent = agentInfoDao.selectByAgentNo(userCouponBean.getAgentNo());
			if (searchAgent != null){
				userCouponBean.setAgentNo(searchAgent.getAgentNode());
			}
		}
		return activityDetailDao.countUserCoupons(userCouponBean, currentAgentNo);
	}

	
	public List<MerchantIncomeBean> listMerchantIncome(MerchantIncomeBean bean, Page<MerchantIncomeBean> page) {
		return activityDetailDao.listMerchantIncome(bean, page);
	}

	
	public List<Map<String, Object>> queryByactivityTypeNoList(String activityCode) {
		return activityDetailDao.finfByActivityCode(activityCode);
	}

	
	public ActivityDetail getHappyBackDetailById(int parseInt) {
		 return activityDetailDao.selectHappyBackDetailById(parseInt);
	}

	
	public List<CashBackDetail> queryAgentReturnCashDetailAll(Integer adId,int amountType) {
		List<CashBackDetail> list = activityDetailDao.selectAgentReturnCashDetailAll(adId,amountType);
		for (CashBackDetail cashBackDetail : list) {
			if ("0".equals(cashBackDetail.getCashBackSwitch())) {
				cashBackDetail.setEntryTime(null);
			} 
			
		}
		return list;
	}

	
	public List<CashBackDetail> queryAgentReturnCashDetailAll(String zeroOrder, Integer id, int i) {
		List<CashBackDetail> list = activityDetailDao.findAllAgentReturnCashDetail(id,i);
		for (CashBackDetail cashBackDetail : list) {
			if ("0".equals(cashBackDetail.getCashBackSwitch())) {
				cashBackDetail.setEntryTime(null);
			} 
			
		}
		return list;
	}

	
	public CashBackDetail queryAgentReturnCashDetailAll(String order, Integer adId, int amountType, String curAgentNo) {
		return activityDetailDao.findAgentReturnCashDetail(adId,amountType,curAgentNo);
	}

	
	public CashBackDetail queryAgentReturnCashDetailAll(Integer id, int i, String entityId) {
		return activityDetailDao.selectAgentReturnCashDetail(id,i,entityId);
	}

}
