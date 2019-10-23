package cn.eeepay.boss.action;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.eeepay.framework.model.*;
import cn.eeepay.framework.util.StringUtil;
import com.alibaba.fastjson.JSONArray;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.eeepay.boss.system.DataSource;
import cn.eeepay.boss.system.SystemLog;
import cn.eeepay.framework.dao.AgentInfoDao;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.service.SettleOrderInfoService;
import cn.eeepay.framework.service.SysDictService;
import cn.eeepay.framework.util.Constants;
import cn.eeepay.framework.util.ListDataExcelExport;

@Controller
@RequestMapping(value="/settleOrderInfoAction")
public class SettleOrderInfoAction {
	private static final Logger log = LoggerFactory.getLogger(SettleOrderInfoAction.class);

	@Resource
	private SettleOrderInfoService settleOrderInfoService;

	@Resource
	private SysDictService sysDictService;

	@Resource
	private  AgentInfoDao agentInfoDao;
	/**
	 * 全局缓存一级代理商
	 */
	private Map<String,AgentInfo> oneAgentMap = new HashMap<>();

	/**
	 * 初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectAllInfo")
	@ResponseBody
	public Object selectAllInfo(@ModelAttribute("page")Page<SettleOrderInfo> page,@RequestParam("info") String param) {
		if(this.oneAgentMap.size()==0){
			log.info("缓存一级代理商-------------");
			List<AgentInfo> list = agentInfoDao.selectByLevelOne();
			for (AgentInfo agentInfo : list) {
				this.oneAgentMap.put(agentInfo.getAgentNo(), agentInfo);
			}
		}
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			List<SettleOrderInfo> orderInfos = settleOrderInfoService.selectAllInfo(page, soi);
			for (SettleOrderInfo orderInfo: orderInfos) {
				reLoadOrderInfo(orderInfo);
			}
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 查询总金额
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping("/getTotalNumAndTotalMoney")
	@ResponseBody
	public Object getTotalNumAndTotalMoney(@RequestParam("info") String param) {
		Map<String, Object> map=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			String money = settleOrderInfoService.getTotalNumAndTotalMoney(soi);
			if(money==null){
				map.put("totalMoney", "0");
			}else{
				map.put("totalMoney", money);
			}
			map.put("bols", true);
		} catch (Exception e) {
			log.error("查询报错",e);
			map.put("bols", false);
			map.put("msg", "查询报错");
		}
		return map;
	}

	/**
	 * 详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectInfoDetail")
	@ResponseBody
	public Object selectInfoDetail(@RequestParam("ids") String ids) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = settleOrderInfoService.selectInfo(ids);
			if(soi==null){
				jsonMap.put("bols", false);
				jsonMap.put("msg","查询为空");
				return jsonMap;
			}
			List<SettleTransfer> slist=settleOrderInfoService.selectSettleInfo(ids);
			jsonMap.put("bols", true);
			jsonMap.put("soi",soi);
			jsonMap.put("slist",slist);
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg","查询详情报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportAllInfo")
	@ResponseBody 
	public void exportAllInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		//20171011,mys,优化
		HashMap<String, String> settleNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_TYPE"));//出款类型
		HashMap<String, String> settleStatusNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_STATUS"));//结算状态

		SettleOrderInfo soi=JSON.parseObject(info,SettleOrderInfo.class);
		List<SettleOrderInfo> list=settleOrderInfoService.importAllInfo(soi);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "出款记录"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("settleOrder",null);
			maps.put("settleType", null);
			maps.put("settleUserType", null);
			maps.put("settleUserName", null);
			maps.put("settleUserNo", null);
			maps.put("settleAmount", null);
			maps.put("outAmount", null);
			maps.put("feeAmount", null);
			maps.put("settleStatus", null);
			maps.put("agentNoOne",null);
			maps.put("agentNameOne",null);
			maps.put("sourceSystem", null);
			maps.put("sourceBatchNo", null);
			maps.put("sourceOrderNo", null);
			maps.put("createTime", null);
			data.add(maps);
		}else{
			Map<String, String> sourceSystemMap = sysDictService.selectMapByKey("SOURCE_SYSTEM");
			for (SettleOrderInfo settleOrderInfo : list) {
				reLoadOrderInfo(settleOrderInfo);
				if (settleOrderInfo.getSettleType() != null) {
					settleOrderInfo.setSettleName(settleNameMap.get(settleOrderInfo.getSettleType()));
				}
				if (settleOrderInfo.getSettleStatus() != null) {
					settleOrderInfo.setSettleStatusName(settleStatusNameMap.get(settleOrderInfo.getSettleStatus()));
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("settleOrder",settleOrderInfo.getSettleOrder());
				maps.put("settleType", null==settleOrderInfo.getSettleName()?"":settleOrderInfo.getSettleName());
				maps.put("settleUserType", null==settleOrderInfo.getSettleUserType()?"":settleOrderInfo.getSettleUserType());
				maps.put("settleUserName", null==settleOrderInfo.getSettleUserName()?"":settleOrderInfo.getSettleUserName());
				maps.put("settleUserNo", null==settleOrderInfo.getSettleUserNo()?"":settleOrderInfo.getSettleUserNo());
				maps.put("settleAmount", null==settleOrderInfo.getSettleAmount()?"":settleOrderInfo.getSettleAmount().toString());
				maps.put("outAmount", null==settleOrderInfo.getOutAmount() ? "" : settleOrderInfo.getOutAmount().toString());
				maps.put("feeAmount", null==settleOrderInfo.getFeeAmount() ? "" : settleOrderInfo.getFeeAmount().toString());
				maps.put("settleStatus", null==settleOrderInfo.getSettleStatusName()?"":settleOrderInfo.getSettleStatusName());
				maps.put("agentNoOne", null==settleOrderInfo.getAgentNoOne()?"":settleOrderInfo.getAgentNoOne());
				maps.put("agentNameOne", null==settleOrderInfo.getAgentNameOne()?"":settleOrderInfo.getAgentNameOne());
				String sourceSystem = settleOrderInfo.getSourceSystem();
				if(sourceSystemMap != null){
					sourceSystem = sourceSystemMap.get(sourceSystem);
				}
				maps.put("sourceSystem", sourceSystem);
				maps.put("sourceBatchNo", null==settleOrderInfo.getSourceBatchNo()?"":settleOrderInfo.getSourceBatchNo());
				maps.put("sourceOrderNo", null==settleOrderInfo.getSourceOrderNo()?"":settleOrderInfo.getSourceOrderNo());
				maps.put("createTime", null==settleOrderInfo.getCreateTime()?"":sdf1.format(settleOrderInfo.getCreateTime()));
				maps.put("errMsg", StringUtils.hasLength(settleOrderInfo.getErrMsg())?settleOrderInfo.getErrMsg():settleOrderInfo.getSettleMsg());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"settleOrder","settleType","settleUserType","settleUserName","settleUserNo","settleAmount",
				"outAmount","feeAmount","settleStatus","agentNoOne","agentNameOne","sourceSystem","sourceBatchNo","sourceOrderNo","createTime","errMsg"};
		String[] colsName = new String[]{"出款记录ID","来源类型","用户类型","用户简称","用户编号","出款金额(元)","到账金额（元）",
				"出款手续费（元）","结算状态","一级代理商编号","一级代理商名称","来源系统","来源系统批次号","来源订单号","创建时间","结算错误信息"};
		OutputStream ouputStream = response.getOutputStream();
		export.export(cols, colsName, data, response.getOutputStream());
		ouputStream.close();
	}

	/***
	 *	批量变更订单状态
	 * @param currSettleStatus 变更后的状态
	 * @param oldSettleStatus 变更前的状态
	 * @param changeSettleStatusReason 变更原因
	 * @param ids 变更订单号
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_MASTER)
	@RequestMapping(value="/changeSettleStatus")
	@ResponseBody
	@SystemLog(description = "出款明细订单状态变更",operCode = "settleOrderInfoAction.changeSettleStatus")
	public Object changeSettleStatus(@RequestParam("currSettleStatus") String currSettleStatus,
									 @RequestParam("oldSettleStatus") String oldSettleStatus,
									 @RequestParam("changeSettleStatusReason") String changeSettleStatusReason,
									 @RequestParam("ids") String[] ids,
									 @RequestParam("orderOrigin") Integer orderOrigin) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String msg = "";
		try {
			if(StringUtil.isEmpty(changeSettleStatusReason)){
				jsonMap.put("msg","变更原因为空");
				jsonMap.put("status", false);
				return jsonMap;
			}
			msg = settleOrderInfoService.changeSettleStatus(currSettleStatus, oldSettleStatus, changeSettleStatusReason, ids, principal.getUsername(),orderOrigin);
			jsonMap.put("msg", msg);
		} catch (Exception e) {
			log.error("变更出款订单明细状态出错",e);
			jsonMap.put("msg","变更出款订单明细状态出错");
			jsonMap.put("status", false);
		}
		return jsonMap;
	}

	/***
	 *	检查订单状态是否已经修改过
	 * @param ids 订单ids
	 *  @param orderOrigin 订单来源 0：订单 1：出款明细
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/checkCanChangeSettleStatus")
	@ResponseBody
	public Object checkCanChangeSettleStatus( @RequestParam("ids") String[] ids,@Param("orderOrigin") Integer orderOrigin) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();

		boolean hasChanged = false;
		try {
			hasChanged = settleOrderInfoService.checkCanChangeSettleStatus(ids,orderOrigin);
			jsonMap.put("checkCanChangeSettleStatusFlag",hasChanged);
		} catch (Exception e) {
			log.error("检查订单状态是否已经修改过出错",e);
			jsonMap.put("msg","检查订单状态是否已经修改过出错");
			jsonMap.put("status",false);
		}

		return jsonMap;
	}

	/**
	 * 出款明细初始化和模糊查询分页
	 * @param page
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectOutDetailAllInfo")
	@ResponseBody
	public Object selectOutDetailAllInfo(@ModelAttribute("page")Page<SettleOrderInfo> page,@RequestParam("info") String param) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			settleOrderInfoService.selectOutDetailAllInfo(page, soi);
			List<SettleOrderInfo> orderInfos = page.getResult();
			for (SettleOrderInfo orderInfo: orderInfos) {
				//屏蔽敏感信息
				orderInfo.setMobile(StringUtil.sensitiveInformationHandle(orderInfo.getMobile(),0));
				reLoadOrderInfo(orderInfo);
			}
			jsonMap.put("bols", true);
			jsonMap.put("page", page);
		} catch (Exception e) {
			log.error("查询出款明细报错",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 出款明细初始化和模糊查询分页
	 * 获取统计金额
	 * @param param
	 * @return
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/getOutDetailTotalMoney")
	@ResponseBody
	public Object getOutDetailTotalMoney(@RequestParam("info") String param) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
			Map<String, String> count = settleOrderInfoService.getOutDetailTotalMoney(soi);
			jsonMap.put("bols", true);
			jsonMap.put("count", count);
		} catch (Exception e) {
			log.error("查询出款明细统计报错",e);
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * 查询出款明细总金额
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/getOutDetailTotalMoney")
//	@ResponseBody
//	public Object getOutDetailTotalMoney(@RequestParam("info") String param) {
//		Map<String, Object> map=new HashMap<String, Object>();
//		try {
//			SettleOrderInfo soi = JSON.parseObject(param, SettleOrderInfo.class);
//			String money = settleOrderInfoService.getOutDetailTotalMoney(soi);
//			if(money==null){
//				map.put("totalMoney", "0");
//			}else{
//				map.put("totalMoney", money);
//			}
//			map.put("bols", true);
//		} catch (Exception e) {
//			log.error("查询出款明细总金额",e);
//			map.put("bols", false);
//		}
//		return map;
//	}

	/**
	 * 出款明细详情查询
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/selectOutInfoDetail")
	@ResponseBody
	public Object selectOutInfoDetail(@RequestParam("ids") String ids) {
		Map<String, Object> jsonMap=new HashMap<String, Object>();
		try {
			SettleTransfer st=settleOrderInfoService.selectOutSettleInfo(ids);
			if(st!=null){
				SettleOrderInfo soi=new SettleOrderInfo();
				if(st.getOrderNo()==null||st.getOrderNo().equals("")){
					soi = settleOrderInfoService.selectInfo(st.getTransId());
				}else{
					soi = settleOrderInfoService.selectOrderNoInfo(st.getOrderNo());
				}
				jsonMap.put("bols", true);
				jsonMap.put("soi",soi);
				jsonMap.put("st",st);
			}else{
				jsonMap.put("bols", false);
				jsonMap.put("msg", "没找出到对应数据");
			}
		} catch (Exception e) {
			log.error("报错!!!",e);
			jsonMap.put("msg","查询详情报错");
			jsonMap.put("bols", false);
		}
		return jsonMap;
	}

	/**
	 * mys,20171011
	 * 把数据字典查询的结果转成map
	 */
	private HashMap<String, String> getSysDictMap(List<SysDict> list) {
		HashMap<String, String> map = new HashMap<>();
		for (SysDict sysDict : list) {
			map.put(sysDict.getSysValue(), sysDict.getSysName());
		}
		return map;
	}

	@DataSource(Constants.DATA_SOURCE_SLAVE)
	@RequestMapping(value="/exportInfo")
	@ResponseBody 
	public void exportInfo(@RequestParam("info") String info,HttpServletResponse response,HttpServletRequest request) throws Exception{
		//20171011,mys,优化
		HashMap<String, String> statusNameMap = getSysDictMap(sysDictService.selectByKey("OUT_STATUS"));//出款状态
		HashMap<String, String> settleNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_TYPE"));//出款类型
		HashMap<String, String> settleStatusNameMap = getSysDictMap(sysDictService.selectByKey("SETTLE_STATUS"));//结算状态

		SettleOrderInfo soi=JSON.parseObject(info,SettleOrderInfo.class);
		List<SettleOrderInfo> list=settleOrderInfoService.exportOutDetailAllInfo(soi);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;
		String fileName = "出款明细记录"+sdf.format(new Date())+".xlsx" ;
		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);   
		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
		if(list.size()<1){
			Map<String, String> maps = new HashMap<String, String>();
			maps.put("id",null);
			maps.put("accountSerialNo", null);
			maps.put("transId", null);
			maps.put("orderNo", null);
			maps.put("settleStatusName", null);
			maps.put("statusName", null);
			maps.put("settleName", null);
			maps.put("settleUserNo", null);
			maps.put("settleUserName", null);
			maps.put("unionpayMerNo", null);
			maps.put("mobile", null);
			maps.put("amount", null);
			maps.put("feeAmount", null);
			maps.put("deductionFee", null);
			maps.put("perAgentFee", null);
			maps.put("outAmount", null);
			maps.put("actualFee", null);
			maps.put("acqEnname", null);
			maps.put("outServiceName", null);
			maps.put("sourceBatchNo", null);
			maps.put("createTime", null);
			maps.put("inAccName", null);
			maps.put("inAccNo", null);
			maps.put("inBankName", null);
			data.add(maps);
		}else{
			for (SettleOrderInfo settleOrderInfo : list) {
				//屏蔽敏感信息
				settleOrderInfo.setMobile(StringUtil.sensitiveInformationHandle(settleOrderInfo.getMobile(),0));

				reLoadOrderInfo(settleOrderInfo);
				if (settleOrderInfo.getStatus() != null) {
					settleOrderInfo.setStatusName(statusNameMap.get(settleOrderInfo.getStatus()));
				}
				if (settleOrderInfo.getSettleType() != null) {
					settleOrderInfo.setSettleName(settleNameMap.get(settleOrderInfo.getSettleType()));
				}
				if (settleOrderInfo.getSettleStatus() != null) {
					settleOrderInfo.setSettleStatusName(settleStatusNameMap.get(settleOrderInfo.getSettleStatus()));
				}
				Map<String, String> maps = new HashMap<String, String>();
				maps.put("id",settleOrderInfo.getId());
				maps.put("accountSerialNo", null==settleOrderInfo.getAccountSerialNo()?"":settleOrderInfo.getAccountSerialNo());
				maps.put("transId", null==settleOrderInfo.getTransId()?"":settleOrderInfo.getTransId());
				maps.put("orderNo", null==settleOrderInfo.getOrderNo()?"":settleOrderInfo.getOrderNo());
				maps.put("settleStatusName", null==settleOrderInfo.getSettleStatusName()?"":settleOrderInfo.getSettleStatusName());
				maps.put("statusName", null==settleOrderInfo.getStatusName()?"":settleOrderInfo.getStatusName());
				maps.put("settleName", null==settleOrderInfo.getSettleName()?"":settleOrderInfo.getSettleName());
				maps.put("agentNoOne", null==settleOrderInfo.getAgentNoOne()?"":settleOrderInfo.getAgentNoOne());
				maps.put("agentNameOne", null==settleOrderInfo.getAgentNameOne()?"":settleOrderInfo.getAgentNameOne());
				maps.put("settleUserNo", null==settleOrderInfo.getSettleUserNo()?"":settleOrderInfo.getSettleUserNo());
				maps.put("settleUserName", null==settleOrderInfo.getSettleUserName()?"":settleOrderInfo.getSettleUserName());
				maps.put("unionpayMerNo", null==settleOrderInfo.getUnionpayMerNo()?"":settleOrderInfo.getUnionpayMerNo());
				maps.put("mobile", null==settleOrderInfo.getMobile()?"":settleOrderInfo.getMobile());
				maps.put("amount", null==settleOrderInfo.getAmount()?"0.00":settleOrderInfo.getAmount().toString());
				maps.put("feeAmount", null==settleOrderInfo.getFeeAmount()?"0.00":settleOrderInfo.getFeeAmount().toString());
				maps.put("deductionFee", null==settleOrderInfo.getDeductionFee()?"0.00":settleOrderInfo.getDeductionFee().toString());
				maps.put("perAgentFee", null==settleOrderInfo.getPerAgentFee()?"0.00":settleOrderInfo.getPerAgentFee().toString());
				maps.put("outAmount", null==settleOrderInfo.getOutAmount()?"0.00":settleOrderInfo.getOutAmount().toString());
				maps.put("actualFee", null==settleOrderInfo.getActualFee()?"0.00":settleOrderInfo.getActualFee().toString());
				maps.put("acqEnname", null==settleOrderInfo.getAcqEnname()?"":settleOrderInfo.getAcqEnname());
				maps.put("outServiceName", null==settleOrderInfo.getOutServiceName()?"":settleOrderInfo.getOutServiceName());
				maps.put("sourceBatchNo", null==settleOrderInfo.getSourceBatchNo()?"":settleOrderInfo.getSourceBatchNo());
				maps.put("createTime", null==settleOrderInfo.getCreateTime()?"":sdf1.format(settleOrderInfo.getCreateTime()));
				maps.put("inAccName", null==settleOrderInfo.getInAccName()?"":settleOrderInfo.getInAccName());
				maps.put("inAccNo", null==settleOrderInfo.getInAccNo()?"":settleOrderInfo.getInAccNo());
				maps.put("inBankName", null==settleOrderInfo.getInBankName()?"":settleOrderInfo.getInBankName());
				data.add(maps);
			}
		}
		ListDataExcelExport export = new ListDataExcelExport();
		String[] cols = new String[]{"id","accountSerialNo","transId","orderNo","settleStatusName","statusName",
				"settleName","agentNoOne","agentNameOne","settleUserNo","settleUserName","unionpayMerNo","mobile","amount","feeAmount","deductionFee","perAgentFee","outAmount","actualFee","acqEnname",
				"outServiceName","sourceBatchNo","createTime","inAccName","inAccNo","inBankName"};
		String[] colsName = new String[]{"出款记录ID","出款流水号","来源订单号","交易订单号","结算状态","出款明细状态","出款类型","一级代理商编号","一级代理商名称","结算用户编号",
				"结算用户简称","银联报备商户编号","用户手机号","出款金额（元）","出款手续费（元）","抵扣手续费（元）","税费（元）","实际出款金额（元）","实际出款手续费（元）",
				"出款通道","出款服务名称","来源批次号","出款时间","收款账户名称","结算卡号","结算银行名称"};
		OutputStream outputStream = response.getOutputStream();
		export.export(cols, colsName, data, outputStream);
		outputStream.close();
	}

	/**
	 * 确认出款
	 * @author tans
	 * @date 2017年6月5日 下午3:32:18
	 * @param param
	 * @return
	 */
	@RequestMapping("/confimPayment")
	@ResponseBody
	@SystemLog(description = "确认出款",operCode="outOrderQuery.confimPayment")
	public Map<String, Object> confimPayment(@RequestBody String param){
		Map<String, Object> msg = new HashMap<>();
		msg.put("status", false);
		msg.put("msg", "操作失败");
		log.info("确认出款，出款订单号：[{}]",param);
		try {
			List<String> settleOrderList = JSONObject.parseArray(param, String.class);
			int sum = 0;
			//检查出款订单是否符合出款条件
			//1.出款类型：5 通用代付
			//2.结算状态：0 未结算
			SettleOrderInfo info = new SettleOrderInfo();
			int successCount =0;
			for(String settleOrder: settleOrderList){
				info = settleOrderInfoService.getBySettleOrder(settleOrder);
				if(info!=null && "5".equals(info.getSettleType()) && "0".equals(info.getSettleStatus())){
					int num = settleOrderInfoService.updateSettleOrderStatus(settleOrder,1);
					sum += num;
					if(num>0){
						//调用出款接口
						try {
							List<NameValuePair> list=new ArrayList<NameValuePair>();
							list.add(new BasicNameValuePair("transferId", settleOrder));
							list.add(new BasicNameValuePair("settleType", "5"));
							Request.Post(Constants.NOWTRANSFER_HOST).body(new UrlEncodedFormEntity(list, "utf-8")).execute();
							successCount++;
						} catch (Exception e) {
							log.error("确认出款失败,已终止后续确认出款,出款订单号:{}",settleOrder);
							log.error(e.toString());
							break;
						}
					}
				}
			}
			if(sum>0){
				msg.put("status", true);
				msg.put("msg", "操作成功,成功条数："+successCount);
			}
			return msg;
		} catch (Exception e) {
			log.error("确认出款失败,",e);
		}
		return msg;
	}

	/**
	 * 出款订单查询/导出时重新设置一代信息
	 * @param orderInfo
	 * @return
	 */
	private SettleOrderInfo reLoadOrderInfo(SettleOrderInfo orderInfo) {
		if (orderInfo != null) {
			Integer subType = orderInfo.getSubType();
			String orderNo = orderInfo.getId();
			if(org.apache.commons.lang3.StringUtils.isNotBlank(orderNo)){
				orderNo = "ID:" + orderNo;
			}else{
				orderNo = orderInfo.getOrderNo();
				if(org.apache.commons.lang3.StringUtils.isNotBlank(orderNo)){
					orderNo = "OrderNo:" + orderNo;
				}else{
					orderNo = orderInfo.getSettleOrder();
					if(org.apache.commons.lang3.StringUtils.isNotBlank(orderNo)){
						orderNo = "SettleOrder:" + orderNo;
					}else{
						orderNo = "SourceOrderNo:" + orderInfo.getSourceOrderNo();
					}
				}
			}
			log.info(String.format("订单[%s]出款子类型[%s]", orderNo, subType));
			if (Objects.equals(4, subType) || Objects.equals(5, subType) || Objects.equals(17, subType)) {
				String agentNo = orderInfo.getAgentNode();
				log.info(String.format("订单[%s]代理商节点[%s]", orderNo, agentNo));
				if (org.apache.commons.lang3.StringUtils.isNotBlank(agentNo)) {
					String[] strs = agentNo.split("-");
					agentNo = strs.length > 1 ? strs[1] : agentNo;
					log.info(String.format("订单[%s]代理商编号[%s]", orderNo, agentNo));
					AgentInfo agentInfo = this.oneAgentMap.get(agentNo);
					if (agentInfo != null) {
						log.info(String.format("订单[%s]重新设置一代信息", agentNo));
						orderInfo.setAgentNoOne(agentInfo.getAgentNo());
						orderInfo.setAgentNameOne(agentInfo.getAgentName());
					}
				}
			}
		}
		return orderInfo;
	}
}
