package cn.eeepay.boss.action;

import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.model.*;
import cn.eeepay.framework.service.SuperPushService;
import cn.eeepay.framework.util.CreateRow;
import cn.eeepay.framework.util.ExcelUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/superPushAction")
public class SuperPushAction {
    private static final Logger log = LoggerFactory.getLogger(SuperPushAction.class);

	@Resource
	private SuperPushService superPushService;


	@ResponseBody
	@RequestMapping("/listSuperPushMerchant")
	public ResponseBean listSuperPushMerchant(@RequestBody SuperPush superPush,
											  @RequestParam(defaultValue = "1") int pageNo,
											  @RequestParam(defaultValue = "10") int size){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			Page<SuperPush> page = new Page<>(pageNo, size);
			List<SuperPush> resultList = superPushService.listSuperPushMerchant(superPush, loginAgentNo, page);
			return new ResponseBean(resultList, page.getTotalCount());
		}catch (Exception e){
			log.error("异常信息: " + e);
			return new ResponseBean(e);
		}
	}

	@ResponseBody
	@RequestMapping("/getSuperPushMerchantDetail")
	public ResponseBean getSuperPushMerchantDetail(String userId){
		try {
			Map<String,Object> result = superPushService.getSuperPushMerchantDetail(userId);
			return new ResponseBean(result);
		}catch (Exception e){
			return new ResponseBean(e);
		}
	}

	@ResponseBody
	@RequestMapping("/listSuperPushShare")
	public ResponseBean listSuperPushShare(@RequestBody SuperPushShare superPushShare,
												@RequestParam(defaultValue = "1") int pageNo,
												@RequestParam(defaultValue = "10") int size){
		try {
			Page<SuperPushShare> page = new Page<>(pageNo, size);
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			List<SuperPushShare> resultList = superPushService.listSuperPushShare(superPushShare,loginAgentNo, page);
			return new ResponseBean(resultList, page.getTotalCount());
		}catch (Exception e){
			log.error("异常信息: " , e);
			return new ResponseBean(e);
		}
	}

	@RequestMapping("/exportSuperPushShare")
	public void exportSuperPushShare(SuperPushShare superPushShare, HttpServletResponse response){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			List<SuperPushShare> resultList = superPushService.exportSuperPushShare(superPushShare,loginAgentNo);
			String fileName = "微创业查询" + new SimpleDateFormat("YYYYMMddHHmmss").format(new Date());
			String[] columnNames = {"分润创建时间", "分润金额", "分润级别", "分润百分比", "分润商户/代理商编号",
					"分润商户/代理商名称", "交易金额", "交易订单号", "交易商户编号", "入账状态", "入账时间"};
			Workbook workbook = ExcelUtils.createWorkBook("交易查询", resultList, columnNames, createSuperPushShareCreateRow());
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xls").getBytes(), "ISO8859-1"));
			workbook.write(response.getOutputStream());
			response.flushBuffer();
		}catch (Exception e){
			log.error("异常信息: " , e);
		}
	}

	private static CreateRow<SuperPushShare> superPushShareCreateRow = null;
	private CreateRow<SuperPushShare> createSuperPushShareCreateRow() {
		if(superPushShareCreateRow != null){
			return superPushShareCreateRow;
		}
		superPushShareCreateRow = new CreateRow<SuperPushShare>() {
			private final Map<String, String> shareTypeMap = new HashMap<>();
			private final Map<String, String> shareStatusMap = new HashMap<>();
			private final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			{
				shareTypeMap.put("0", "一级代理商分润");
				shareTypeMap.put("1", "直属代理商");
				shareTypeMap.put("2", "一级分润");
				shareTypeMap.put("3", "二级分润");
				shareTypeMap.put("4", "三级分润");
				shareStatusMap.put("0", "未入账");
				shareStatusMap.put("1", "已入账");
				shareStatusMap.put("2", "入账失败");
			}
			@Override
			public void writeRow(Row row, SuperPushShare superPushShare) {
				int index = 0;
				String createTime = superPushShare.getCreateTime() == null ? "" : sdf.format(superPushShare.getCreateTime());
				row.createCell(index ++).setCellValue(createTime);
				row.createCell(index ++).setCellValue(superPushShare.getShareAmount());
				row.createCell(index ++).setCellValue(shareTypeMap.get(superPushShare.getShareType()));
				row.createCell(index ++).setCellValue(superPushShare.getShareRate());
				row.createCell(index ++).setCellValue(superPushShare.getShareNo());
				row.createCell(index ++).setCellValue(superPushShare.getShareName());
				row.createCell(index ++).setCellValue(superPushShare.getTransAmount());
				row.createCell(index ++).setCellValue(superPushShare.getOrderNo());
				row.createCell(index ++).setCellValue(superPushShare.getMerchantNo());
				row.createCell(index ++).setCellValue(shareStatusMap.get(superPushShare.getShareStatus()));
				String shareTime = superPushShare.getShareTime() == null ? "" : sdf.format(superPushShare.getShareTime());
				row.createCell(index ++).setCellValue(shareTime);
			}
		};
		return superPushShareCreateRow;
	}

	@ResponseBody
	@RequestMapping("/countSuperPushShare")
	public ResponseBean countSuperPushShare(@RequestBody SuperPushShare superPushShare){
		try {
			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String loginAgentNo = principal.getUserEntityInfo().getEntityId();
			Map<String,Object> resultMap = superPushService.countSuperPushShare(superPushShare,loginAgentNo);
			return new ResponseBean(resultMap);
		}catch (Exception e){
			log.error("异常信息: " , e);
			return new ResponseBean(e);
		}
	}























//
//	@Resource
//	private MerchantCardInfoService merchantCardInfoService;
//
//	//超级推活动查询
//	@RequestMapping(value = "/selectSuperPush.do")
//	@ResponseBody
//	public Object selectSuperPush(@RequestParam("baseInfo")String param,@ModelAttribute("page") Page<SuperPush> page){
//		SuperPush superPush = JSON.parseObject(param,SuperPush.class);
//		try {
//			final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			superPushService.selectSuperPush(page,principal.getTelNo(),superPush);
//		} catch (Exception e) {
//			log.error("查询失败----", e);
//		}
//		return page;
//	}
//	//超级推商户详情
//	@RequestMapping(value = "/selectMerchantDetail.do")
//	@ResponseBody
//	public Object selectMerchantDetail(@RequestParam("mertId")String mertId){
//		Map<String,Object> map = new HashMap<>();
//		try {
//			map = superPushService.selectMerchantDetail(mertId);
//		} catch (Exception e) {
//			log.error("查询失败----", e);
//		}
//		return map;
//	}
//
//	//超级推分润详情
//	@RequestMapping(value = "/selectShareDetail.do")
//	@ResponseBody
//	public Object selectShareDetail(@ModelAttribute("page") Page<SuperPush> page,@RequestParam("info")String param){
//		List<SuperPushShare> list = new ArrayList<>();
//		try {
//			SuperPushShare info = JSONObject.parseObject(param, SuperPushShare.class);
//			list = superPushService.selectShareDetail(page,info);
//		} catch (Exception e) {
//			log.error("查询失败----", e);
//		}
//		return page;
//	}
//
//	//导出分润详情
//	@RequestMapping(value="/exportTransInfo.do")
//	public void exportTransInfo(@RequestParam("info")String param,HttpServletResponse response){
//        try {
//        	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss") ;
//    		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        	SuperPushShare info = JSONObject.parseObject(param, SuperPushShare.class);
//        	List<SuperPushShare> list = superPushService.exportTransInfo(info);
//        	String fileName = "分润详情"+sdf.format(new Date())+".xls" ;
//    		String fileNameFormat = new String(fileName.getBytes("GBK"),"ISO-8859-1");
//    		response.setHeader("Content-disposition", "attachment;filename="+fileNameFormat);
//    		List<Map<String, String>> data = new ArrayList<Map<String,String>>() ;
//    		Map<String,String> map = null;
//    		if (list.size()<1) {
//    			map = new HashMap<>();
//    			map.put("id", null);
//    			map.put("transTime", null);
//    			map.put("merchantNo", null);
//    			map.put("merchantName", null);
//    			map.put("level", null);
//    			map.put("share", null);
//    			map.put("rule", null);
//    			map.put("amount", null);
//    			data.add(map);
//			}
//    		for(SuperPushShare item: list){
//    			map = new HashMap<>();
//    			map.put("id", String.valueOf(item.getId()));
//    			map.put("transTime", item.getTransTime()==null?"":sdfTime.format(item.getTransTime()));
//    			map.put("merchantNo", item.getMerchantNo());
//    			map.put("merchantName", item.getMerchantName());
//    			map.put("level", item.getLevel());
//    			map.put("share", item.getShare().toString());
//    			map.put("rule", item.getRule());
//    			map.put("amount", item.getAmount().toString());
//    			data.add(map);
//    		}
//    		ListDataExcelExport export = new ListDataExcelExport();
//    		String[] cols = new String[]{"id","transTime","merchantNo","merchantName","level","share","rule","amount"};
//    		String[] colsName = new String[]{"序号","统计日期","下级商户编号","下级商户名称","下级商户级别","贡献分润金额","分润比例","交易金额"};
//    		OutputStream ouputStream = response.getOutputStream();
//    		export.export(cols, colsName, data, response.getOutputStream());
//    		ouputStream.close();
//        } catch (Exception e) {
//        	log.error("导出分润详情失败",e);
//        }
//    }
//	/**
//	 * 超级推提现详情
//	 * @param merchantNo
//	 * @return
//	 */
//	@RequestMapping(value="/superPushCashDetail")
//	@ResponseBody
//	public Map<String,Object> superPushCashDetail(@RequestParam("mertId") String merchantNo)
//			{
//		Map<String, Object> msg = new HashMap<>();
//		msg.put("status", false);
//		try {
//			//基本信息
//			SuperPush baseInfo = superPushService.getCashMerchantDetail(merchantNo);
//			//收益总额
//			BigDecimal totalAmount = superPushService.getTotalAmount(merchantNo);
//			//可用余额
//			BigDecimal avaliBalance = superPushService.getSuperPushUserBalance(merchantNo);
//			baseInfo.setTotalAmount(totalAmount);
//			baseInfo.setAvaliBalance(avaliBalance);
//			//结算信息
//			MerchantCardInfo merchantCardInfo = merchantCardInfoService.selectByMertId(merchantNo);
//			if(merchantCardInfo!=null){
//				merchantCardInfo.setAccountArea(merchantCardInfo.getAccountProvince()
//						+"-"+ merchantCardInfo.getAccountCity()
//						+"-"+ merchantCardInfo.getAccountDistrict()
//						);
//			}
//			msg.put("status", true);
//			msg.put("baseInfo", baseInfo);
//			msg.put("merchantCardInfo", merchantCardInfo);
//		} catch (Exception e) {
//			log.error("超级推提现详情查询失败,参数：[{}]",merchantNo);
//			log.error("超级推提现详情查询失败,",e);
//		}
//		return msg;
//	}
//	/**
//	 * 超级推商户提现详情-提现流水
//	 * @author
//	 * @version 创建时间：2017年5月16日 下午2:28:48
//	 */
//	@RequestMapping(value="/getCashPage")
//	@ResponseBody
//	public Map<String,Object> getCashPage(@RequestParam("mertId") String merchantNo, @Param("page") Page<SuperPush> page){
//		Map<String, Object> msg = new HashMap<>();
//		msg.put("status", false);
//		try {
//			SettleOrderInfo settleOrderInfo = new SettleOrderInfo();
//			settleOrderInfo.setSubType("6");//超级推商户
//			settleOrderInfo.setSettleUserNo(merchantNo);
//			settleOrderInfo.setSettleUserType("M");
//			settleOrderInfo.setSettleType("2");
//			superPushService.getCashPage(settleOrderInfo,page);
//			msg.put("status", true);
//			msg.put("page", page);
//		} catch (Exception e) {
//			log.error("超级推商户提现详情-提现流水,商户号：[{}]",merchantNo);
//			log.error("超级推商户提现详情-提现流水,",e);
//		}
//		return msg;
//	}
		
}
