package cn.eeepay.framework.service.impl;

import cn.eeepay.framework.dao.ActivityDao;
import cn.eeepay.framework.dao.SysDictDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.ActivityService;
import cn.eeepay.framework.util.ListDataExcelExport;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("activityService")
@Transactional
public class ActivityServiceImpl implements ActivityService {
	private final static Logger log = LoggerFactory.getLogger(ActivityServiceImpl.class);
	@Resource
	private ActivityDao activityDao;

	@Resource
	private SeqService seqService;
	
	@Resource
	private SysDictDao sysDictDao;

	@Override
	public List<ActivityHardware> selectActivityHardware(Page<ActivityHardware> page, String activityCode) {
		return activityDao.selectActivityHardware(page, activityCode);
	}

	// 这里有毒,默默加个注释就走
	@Override
	public List<ActivityHardware> selectActivityConfig(String activityCode) {
		return activityDao.selectActivityConfig(activityCode);
	}

	@Override
	public int insertActivityHardware(ActivityHardware activityInfo) {
		return activityDao.insertActivityHardware(activityInfo);
	}

	@Override
	public int updateActivityConfig(ActivityConfig activityInfo) {
		return activityDao.updateActivityConfig(activityInfo);
	}

	@Override
	public int insetActivityConfig(ActivityConfig activityInfo) {
		return activityDao.insetActivityConfig(activityInfo);
	}

	@Override
	public ActivityConfig selectActivityCofig(String activityCode) {
		return activityDao.selectActivityMainCofig(activityCode);
	}

	@Override
	public int updateActivityHardware(ActivityHardware activityHardware) {
		return activityDao.updateActivityHardware(activityHardware);
	}

	// -------------------------- 以下是欢乐返活动 !!! ----------------------------
	@Override
	public List<HlfHardware> selectHlfHardware(String activityCode) {
		/*ActivityConfig activityConfig = selectActivityCofig(activityCode);*/
		List<HlfHardware> list = activityDao.selectHlfHardware(activityCode);
		/*if(list != null && list.size() > 0){
			for (HlfHardware item: list) {
				if(item.getDefaultStatus() != null && item.getDefaultStatus() == 1){
					item.setCumulateTransDay(activityConfig.getCumulateTransDay());
					item.setCumulateAmountMinus(activityConfig.getCumulateAmountMinus());
					item.setCumulateAmountAdd(activityConfig.getCumulateAmountAdd());
					item.setCumulateTransMinusDay(activityConfig.getCumulateTransDay());
				}
			}
		}*/
		return list;
	}

	@Override
	public List<HlfHardware> selectHlfActivityHardware(HlfHardware hlfHardware){
		//默认配置
		ActivityConfig activityConfig = activityDao.selectActivityMainCofig(hlfHardware.getActivityCode());
		//默认重复配置去001
		ActivityConfig activityConfig001 = activityDao.selectActivityMainCofig("001");

		List<HlfHardware> list = activityDao.selectHlfActivityHardware(hlfHardware);
		if(list != null && list.size() > 0){
			for (HlfHardware item: list) {
				if(StringUtil.isNotBlank(item.getActivityRewardConfigId())){
					//取0元活动配置
					ActivityRewardConfig activityRewardConfig = selectHappyReturnRewardActivityById(item.getActivityRewardConfigId().toString());
					if(item.getDefaultStatus() != null && item.getDefaultStatus() == 1 && activityRewardConfig!=null){
						item.setCumulateTransDay(activityRewardConfig.getCumulateTransDay());
						item.setCumulateAmountMinus(activityRewardConfig.getCumulateAmountMinus());
						item.setCumulateAmountAdd(activityRewardConfig.getCumulateAmountAdd());
						item.setCumulateTransMinusDay(activityRewardConfig.getCumulateTransMinusDay());
						item.setRepeatCumulateTransDay(activityRewardConfig.getRepeatCumulateTransDay());
						item.setRepeatCumulateAmountMinus(activityRewardConfig.getRepeatCumulateAmountMinus());
						item.setRepeatCumulateAmountAdd(activityRewardConfig.getRepeatCumulateAmountAdd());
						item.setRepeatCumulateTransMinusDay(activityRewardConfig.getRepeatCumulateTransMinusDay());
					}
				}else{
					if(item.getDefaultStatus() != null && item.getDefaultStatus() == 1){
						item.setCumulateTransDay(activityConfig.getCumulateTransDay());
						item.setCumulateAmountMinus(activityConfig.getCumulateAmountMinus());
						item.setCumulateAmountAdd(activityConfig.getCumulateAmountAdd());
						item.setCumulateTransMinusDay(activityConfig.getCumulateTransDay());
						item.setRepeatCumulateTransDay(activityConfig001.getCumulateTransDay());
						item.setRepeatCumulateAmountMinus(activityConfig001.getCumulateAmountMinus());
						item.setRepeatCumulateAmountAdd(activityConfig001.getCumulateAmountAdd());
						item.setRepeatCumulateTransMinusDay(activityConfig001.getCumulateTransMinusDay());
					}
				}
			}
		}
		return list;
	}

	@Override
	public HlfHardware selectHlfHardwareByHardId(HlfHardware hlfHardware) {
		return activityDao.selectHlfHardwareByHardId(hlfHardware);
	}

	@Override
	public HlfHardware selectHlfHardwareById(Integer id){
		return activityDao.selectHlfHardwareById(id);
	}

	@Override
	public HlfHardware selectHlfHardwareInfo(HlfHardware hlfHardware){
		return activityDao.selectHlfHardwareInfo(hlfHardware);
	}

	@Override
	public int updateHlfHardware(HlfHardware hlfHardware) {
		ActivityHardwareType activityHardwareType=activityDao.queryByActivityHardwareType(hlfHardware.getActivityTypeNo());
		if(activityHardwareType!=null) {
			hlfHardware.setTransAmount(activityHardwareType.getTransAmount());
			hlfHardware.setCashBackAmount(activityHardwareType.getCashBackAmount());
			activityDao.updateAgentSctivityCashBackAmount(activityHardwareType);
		}
		return activityDao.updateHlfHardware(hlfHardware);
	}

	@Override
	public boolean insertHlfConfig(ActivityConfig activityConfig) {
		activityConfig.setActivityCode("008");
		int insert80 = activityDao.insetActivityConfig(activityConfig);
		activityConfig.setActivityCode("009");
		int insert150 = activityDao.insetActivityConfig(activityConfig);
		activityConfig.setActivityCode("021");
		int insert128 = activityDao.insetActivityConfig(activityConfig);
		return insert80 == 1 && insert150 == 1 && insert128 == 1;
	}

	@Override
	public boolean updateHlfConfig(ActivityConfig activityConfig) {
		activityConfig.setActivityCode("008");
		int update80 = activityDao.updateActivityConfig(activityConfig);
		activityConfig.setActivityCode("009");
		int update150 = activityDao.updateActivityConfig(activityConfig);
		activityConfig.setActivityCode("021");
		int update128 = activityDao.updateActivityConfig(activityConfig);
		return update80 == 1 && update150 == 1 && update128 == 1;
	}

	@Override
	public boolean isExistHardware(HlfHardware hlfHardware) {
		Integer i = activityDao.isExistHardware(hlfHardware);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public boolean isExistHardwareByNoId(HlfHardware hlfHardware) {
		Integer i = activityDao.isExistHardwareByNoId(hlfHardware);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public int insertHlfHardware(HlfHardware hlfHardware,List<HardwareProduct> hards) {
		int i=0;
		ActivityHardwareType activityHardwareType=activityDao.queryByActivityHardwareType(hlfHardware.getActivityTypeNo());
		if(activityHardwareType!=null) {
			hlfHardware.setTransAmount(activityHardwareType.getTransAmount());
			hlfHardware.setCashBackAmount(activityHardwareType.getCashBackAmount());
		}
		for (HardwareProduct h:hards){
			hlfHardware.setHardId(h.getHpId());
			hlfHardware.setTeamId(h.getOrgId());
			i+=activityDao.insertHlfHardware(hlfHardware);
		}
		return i;
	}

	@Override
	public List<ActivityHardwareType> selectHappyReturnType(ActivityHardwareType activityHardwareType, Page<ActivityHardwareType> page) {
		return activityDao.selectHappyReturnType(activityHardwareType,page);
	}

	@Override
	public void exportActivityHardwareType(ActivityHardwareType info, HttpServletResponse response)  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OutputStream ouputStream = null;

		Page<ActivityHardwareType> page = new Page<>();
		page.setPageSize(Integer.MAX_VALUE);
		List<ActivityHardwareType> list = selectHappyReturnType(info,page);
		String fileName = "欢乐返子类型"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		Map<String,String> map = null;

		Map<String, String> typeMap = new HashMap<>();
		typeMap.put("009", "欢乐返");
		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("0", "否");
		statusMap.put("1", "是");

		for(ActivityHardwareType item: list){
			map = new HashMap<>();
			map.put("activityTypeNo", item.getActivityTypeNo());
			map.put("activityTypeName",  item.getActivityTypeName());
			map.put("activityCode", StringUtils.trimToEmpty(typeMap.get(item.getActivityCode()+"")));
			map.put("ruleId", StringUtil.isNotBlank(item.getRuleId())?item.getRuleId()+"":"未参与");
			map.put("ruleName",  item.getRuleName());
			map.put("transAmount", item.getTransAmount()+"");
			map.put("cashBackAmount", item.getCashBackAmount()+"");
			map.put("repeatRegisterAmount", item.getRepeatRegisterAmount()+"");
			map.put("emptyAmount", item.getEmptyAmount()+"");
			map.put("fullAmount", item.getFullAmount()+"");
			map.put("repeatEmptyAmount", item.getRepeatEmptyAmount()+"");
			map.put("repeatFullAmount", item.getRepeatFullAmount()+"");
			map.put("updateAgentStatus", StringUtils.trimToEmpty(statusMap.get(item.getUpdateAgentStatus()+"")));
			map.put("remark", item.getRemark());
			data.add(map);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"activityTypeNo","activityTypeName","activityCode"
				,"ruleId","ruleName","transAmount"
				,"cashBackAmount","repeatRegisterAmount","emptyAmount","fullAmount","repeatEmptyAmount"
				,"repeatFullAmount","updateAgentStatus","remark"};
		String[] colsName = new String[]{"欢乐返子类型编号","欢乐返子类型名称","欢乐返类型"
				,"活跃商户活动ID","活跃商户活动名称","交易金额"
				,"返现金额","重复注册返现金额","首次注册不满扣(元)","首次注册满奖(元)","重复注册不满扣(元)"
				,"重复注册满奖(元)","允许代理商更改","备注"};
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出欢乐返子类型失败,param:{}",JSONObject.toJSONString(info));
			e.printStackTrace();
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean queryByActivityTypeName(ActivityHardwareType activityHardwareType){
		Integer i =  activityDao.queryByActivityTypeName(activityHardwareType);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean insertHappyReturnType(ActivityHardwareType activityHardwareType) {
		activityHardwareType.setActivityTypeNo(seqService.createKey("actity_type_no","%05d"));
		Integer i =  activityDao.insertHappyReturnType(activityHardwareType);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean updateHappyReturnType(ActivityHardwareType activityHardwareType) {
		Integer i = activityDao.updateHappyReturnType(activityHardwareType);
		activityDao.updateActivityHardwareByActivityTypeNo(activityHardwareType);
		activityDao.updateAgentSctivityCashBackAmount(activityHardwareType);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean delHappyReturnType(String activityTypeNo) {
		Integer i =  activityDao.delHappyReturnType(activityTypeNo);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean updateAgentStatusSwitch(ActivityHardwareType activityHardwareType){
		Integer i =  activityDao.updateAgentStatusSwitch(activityHardwareType);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean updateCountTradeScope(String id,String countTradeScope){
		Integer i =  activityDao.updateCountTradeScope(id,countTradeScope);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean queryActivityCount(String activityTypeNo){
		Integer i =  activityDao.queryActivityCount(activityTypeNo);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public List<ActivityHardwareType> queryByactivityTypeNoList(String activityCode){
		List<ActivityHardwareType> list=activityDao.queryByactivityTypeNoList(activityCode);
		return list;
	}

	public ActivityHardwareType queryByActivityHardwareType(String activityTypeNo){
		ActivityHardwareType activityHardwareType=activityDao.queryByActivityHardwareType(activityTypeNo);
		return activityHardwareType;
	}

	public boolean insertHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig) {
		Integer i =  activityDao.insertHappyReturnRewardActivity(activityRewardConfig);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean updateHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig) {
		Integer i =  activityDao.updateHappyReturnRewardActivity(activityRewardConfig);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean deleteHappyReturnRewardActivity(String id) {
		Integer i =  activityDao.deleteHappyReturnRewardActivity(id);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean queryHappyReturnAgentActivityCount(String id) {
		Integer i =  activityDao.queryHappyReturnAgentActivityCount(id);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public AgentActivityRewardConfig queryHappyReturnAgentActivityByAgentNo(String agentNo) {
		return activityDao.queryHappyReturnAgentActivityByAgentNo(agentNo);
	}

	public boolean insertHappyReturnAgentActivity(AgentActivityRewardConfig info) {
		Integer i =  activityDao.insertHappyReturnAgentActivity(info);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	public boolean deleteHappyReturnAgentActivity(String id) {
		Integer i =  activityDao.deleteHappyReturnAgentActivity(id);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public List<ActivityRewardConfig> selectHappyReturnRewardActivity(ActivityRewardConfig activityRewardConfig, Page<ActivityRewardConfig> page) {
		return activityDao.selectHappyReturnRewardActivity(activityRewardConfig,page);
	}

	@Override
	public ActivityRewardConfig selectHappyReturnRewardActivityById(String id) {
		return activityDao.selectHappyReturnRewardActivityById(id);
	}

	@Override
	public List<AgentActivityRewardConfig> selectHappyReturnAgentActivity(AgentActivityRewardConfig agentActivityRewardConfig, Page<AgentActivityRewardConfig> page) {
		return activityDao.selectHappyReturnAgentActivity(agentActivityRewardConfig,page);
	}
	// -------------------------- 以上是欢乐返活动 !!! ----------------------------
	@Override
	public List<Map> queryActivityVipList(Map map, Page<Map> page){
		return activityDao.queryActivityVipList(map,page);
	}

	@Override
	public void exportActivityVip(Map info, HttpServletResponse response)  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OutputStream ouputStream = null;

		Page<Map> page = new Page<>();
		page.setPageSize(Integer.MAX_VALUE);
		List<Map> list = queryActivityVipList(info,page);
		String fileName = "VIP优享订单"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		Map<String,String> map = null;

		Map<String, String> subscribeStatuses = new HashMap<>();
		subscribeStatuses.put("SUCCESS", "成功");
		subscribeStatuses.put("FAILED", "失败");
		subscribeStatuses.put("INIT", "未付款");
		Map<String, String> paymentTypes = new HashMap<>();
		paymentTypes.put("alipay", "支付宝");
		paymentTypes.put("wechat", "微信");
		paymentTypes.put("byCard", "刷卡");
		paymentTypes.put("demo", "休验");

		Map<String, String> teamIds = new HashMap<>();
		List<SysDict> selectListByKey = sysDictDao.selectListByKey("AGENT_OEM");
		
		for (SysDict sysDict : selectListByKey) {
			teamIds.put(sysDict.getSysValue(), sysDict.getSysName());
		}
		
		for(Map item: list){
			map = new HashMap<>();
			map.put("id", item.get("id").toString());
			map.put("order_no",  StringUtil.isBlank(item.get("order_no"))?"":item.get("order_no").toString());
			map.put("merchant_name", StringUtil.isBlank(item.get("merchant_name"))?"":item.get("merchant_name").toString());
			map.put("merchant_no", StringUtil.isBlank(item.get("merchant_no"))?"":item.get("merchant_no").toString());
			map.put("mobilephone", StringUtil.isBlank(item.get("mobilephone"))?"":item.get("mobilephone").toString());
			map.put("agent_name", StringUtil.isBlank(item.get("agent_name"))?"":item.get("agent_name").toString());
			map.put("one_agent_name", StringUtil.isBlank(item.get("one_agent_name"))?"":item.get("one_agent_name").toString());
			map.put("name", StringUtil.isBlank(item.get("name"))?"":item.get("name").toString());
			map.put("amount", StringUtil.isBlank(item.get("amount"))?"":item.get("amount").toString());
			map.put("validity_days", StringUtil.isBlank(item.get("validity_days"))?"":item.get("validity_days").toString());
			map.put("subscribe_status", StringUtil.isBlank(item.get("subscribe_status"))?"":subscribeStatuses.get(item.get("subscribe_status").toString()));
			map.put("payment_type", StringUtil.isBlank(item.get("payment_type"))?"":paymentTypes.get(item.get("payment_type").toString()));
			map.put("payment_order_no", StringUtil.isBlank(item.get("payment_order_no"))?"":item.get("payment_order_no").toString());
			map.put("create_time", StringUtil.isBlank(item.get("create_time"))?"":sdfTime.format(item.get("create_time")));
			
			//支付时间,类别,到期时间
			map.put("team_id", StringUtil.isBlank(item.get("team_id"))?"":teamIds.get(item.get("team_id").toString()));
			map.put("validity_end", StringUtil.isBlank(item.get("validity_end"))?"":sdfTime.format(item.get("validity_end")));
			map.put("trans_time", StringUtil.isBlank(item.get("trans_time"))?"":sdfTime.format(item.get("trans_time")));
			data.add(map);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","order_no","merchant_name","merchant_no","mobilephone","agent_name"
				,"one_agent_name","name","amount","validity_days","subscribe_status","payment_type","payment_order_no"
				,"create_time","validity_end","trans_time","team_id"};
		String[] colsName = new String[]{"序号","业务订单编号","商户名称","商户编号","商户手机号","所属代理商"
				,"一级代理商","服务名称","交易金额","有效期（天）","订单状态","支付方式","支付订单号"
				,"创建时间","到期时间","支付时间","类别"};
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出VIP优享订单失败,param:{}",JSONObject.toJSONString(info));
			e.printStackTrace();
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public List<ActivityRewardConfig> queryByActivityRewardConfigList(){
		List<ActivityRewardConfig> list=activityDao.queryByActivityRewardConfigList();
		return list;
	}

	@Override
	public List<ActivityHardwareType> getActivityTypeNoList() {
		List<ActivityHardwareType> list=activityDao.getActivityTypeNoList();
		return list;
	}

	@Override
	public List<HlfHardware> selectHBActivityHardwareList(String hardId, String activityCode) {
		return activityDao.selectHBActivityHardwareList(hardId, activityCode);
	}

	public void exportHlfActivityHardware(HlfHardware info, HttpServletResponse response)  throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		OutputStream ouputStream = null;

		List<HlfHardware> list = selectHlfActivityHardware(info);
		String fileName = "欢乐返活动设置"+sdf.format(new Date())+".xls" ;
		String fileNameFormat = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		Map<String,String> map = null;

		Map<String, String> statusMap = new HashMap<>();
		statusMap.put("1", "是");
		statusMap.put("0", "否");


		for(HlfHardware item: list){
			map = new HashMap<>();
			map.put("typeName", item.getTypeName());
			map.put("activityTypeName", item.getActivityTypeName());
			map.put("teamName", item.getTeamName());
			map.put("activityMerchantId", StringUtil.isNotBlank(item.getActivityMerchantId())?item.getActivityMerchantId()+"":"未参加");
			map.put("transAmount", item.getTransAmount()+"");
			map.put("cashBackAmount", item.getCashBackAmount()+"");
			map.put("cashLastAllyAmount", item.getCashLastAllyAmount()+"");
			map.put("emptyAmount", item.getEmptyAmount()+"");
			map.put("fullAmount", item.getFullAmount()+"");
			map.put("repeatEmptyAmount", item.getRepeatEmptyAmount()+"");
			map.put("repeatFullAmount", item.getRepeatFullAmount()+"");
			map.put("defaultStatus", StringUtils.trimToEmpty(statusMap.get(item.getDefaultStatus()+"")));
			map.put("cumulateTransMinusDay", StringUtil.isBlank(item.getCumulateTransMinusDay())?"":item.getCumulateTransMinusDay()+"");
			map.put("cumulateTransDay", StringUtil.isBlank(item.getCumulateTransDay())?"":item.getCumulateTransDay()+"");
			map.put("repeatCumulateTransMinusDay", StringUtil.isBlank(item.getRepeatCumulateTransMinusDay())?"":item.getRepeatCumulateTransMinusDay()+"");
			map.put("repeatCumulateTransDay", StringUtil.isBlank(item.getRepeatCumulateTransDay())?"":item.getRepeatCumulateTransDay()+"");
			data.add(map);
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"typeName","activityTypeName","teamName","activityMerchantId","transAmount"
				,"cashBackAmount","cashLastAllyAmount","emptyAmount","fullAmount","repeatEmptyAmount","repeatFullAmount"
				,"defaultStatus","cumulateTransMinusDay","cumulateTransDay","repeatCumulateTransMinusDay","repeatCumulateTransDay"};
		String[] colsName = new String[]{"设备类型","欢乐返子类型","所属组织","活跃商户活动ID","交易金额"
				,"返现金额","返盟主金额","首次注册不满扣N值","首次注册满奖M值","重复注册不满扣N值","重复注册满奖M值"
				,"是否取默认活动内容","首次注册累计交易扣费时间(天)","首次注册累计交易奖励时间(天)","重复注册累计交易扣费时间(天)","重复注册累计交易奖励时间(天)"};
		try {
			ouputStream = response.getOutputStream();
			export.export(cols, colsName, data, response.getOutputStream());
		} catch (Exception e) {
			log.error("导出欢乐返活动设置失败,param:{}",JSONObject.toJSONString(info));
			e.printStackTrace();
		} finally {
			try {
				if(ouputStream!=null){
					ouputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean insertHlfActivityMerchantRule(HlfActivityMerchantRule info) {
		Integer i =  activityDao.insertHlfActivityMerchantRule(info);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public boolean updateHlfActivityMerchantRule(HlfActivityMerchantRule info) {
		Integer i =  activityDao.updateHlfActivityMerchantRule(info);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public boolean deleteHlfActivityMerchantRule(String ruleId) {
		Integer i =  activityDao.deleteHlfActivityMerchantRule(ruleId);
		if (i == null)
			return false;
		else
			return i > 0;
	}

	@Override
	public List<HlfActivityMerchantRule> selectHlfActivityMerchantRule(HlfActivityMerchantRule info, Page<HlfActivityMerchantRule> page) {
		return activityDao.selectHlfActivityMerchantRule(info,page);
	}

	@Override
	public HlfActivityMerchantRule selectHlfActivityMerchantRuleById(String ruleId) {
		return activityDao.selectHlfActivityMerchantRuleById(ruleId);
	}

	@Override
	public List<HlfActivityMerchantRule> selectHlfActivityMerchantRuleAllInfo(String item){
		if(StringUtil.isBlank(item)){
			return activityDao.selectHlfActivityMerchantRuleAllInfo();
		}else{
			return activityDao.selectHlfActivityMerchantRuleAllInfo2(item);
		}
	}

	@Override
	public int findActivityHardwareTypeByRuleIdCount(String ruleId){
		return activityDao.findActivityHardwareTypeByRuleIdCount(ruleId);
	}
}
