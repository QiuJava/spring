package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.eeepay.framework.dao.bill.AcqOutBillMapper;
import cn.eeepay.framework.dao.bill.DuiAccountDetailMapper;
import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.dao.bill.OutBillDetailMapper;
import cn.eeepay.framework.dao.bill.OutBillMapper;
import cn.eeepay.framework.dao.bill.SubOutBillDetailLogsMapper;
import cn.eeepay.framework.dao.bill.SubOutBillDetailMapper;
import cn.eeepay.framework.dao.nposp.AcqOrgMapper;
import cn.eeepay.framework.dao.nposp.MerchantCardInfoMapper;
import cn.eeepay.framework.dao.nposp.MerchantInfoMapper;
import cn.eeepay.framework.dao.nposp.OutAccountServiceMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.AcqEnname;
import cn.eeepay.framework.model.bill.AcqOrg;
import cn.eeepay.framework.model.bill.OutBill;
import cn.eeepay.framework.model.bill.OutBillDetail;
import cn.eeepay.framework.model.bill.SubOutBillDetail;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.model.nposp.OutAccountService;
import cn.eeepay.framework.service.bill.OutBillDetailService;
import cn.eeepay.framework.util.HttpConnectUtil;
import cn.eeepay.framework.util.JSONUtil;

@Service("outBillDetailService")
@Transactional
public class OutBillDetailServiceImpl implements OutBillDetailService {
	private static final Logger log = LoggerFactory.getLogger(OutBillDetailServiceImpl.class);
	@Resource
	public OutBillDetailMapper outBillDetailMapper;
	@Resource
	private MerchantCardInfoMapper merchantCardInfoMapper;
	@Resource
	private MerchantInfoMapper merchantInfoMapper;
	@Resource
	private OutBillMapper outBillMapper;
	@Resource
	private AcqOutBillMapper acqOutBillMapper;
	@Resource
	private OutAccountServiceMapper outAccountServiceMapper;
	@Resource
	private AcqOrgMapper acqOrgMapper;
	
	@Resource
	private ExtAccountMapper extAccountMapper;
	
	@Resource
	public SubOutBillDetailMapper subOutBillDetailMapper;

	@Resource
	public DuiAccountDetailMapper duiAccountDetailMapper;

	@Resource
	public SubOutBillDetailLogsMapper subOutBillDetailLogsMapper;

	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;
	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;
	@Value("${core2.http.url}")
	private String core2ApiHttpUrl;
	@Value("${core2.http.secret}")
	private String core2HttpSecret;

	@Override
	public int insertOutBillDetail(OutBillDetail outBillDetail) throws Exception {
		return outBillDetailMapper.insertOutBillDetail(outBillDetail);
	}

	@Override
	public int updateOutBillDetailById(OutBillDetail outBillDetail) throws Exception {
		return outBillDetailMapper.updateOutBillDetailById(outBillDetail);
	}

	@Override
	public int deleteOutBillDetailById(Integer id) throws Exception {
		return outBillDetailMapper.deleteOutBillDetailById(id);
	}

	@Override
	public int deleteOutBillDetailByOutBillId(Integer outBillId) throws Exception {
		return outBillDetailMapper.deleteOutBillDetailByOutBillId(outBillId);
	}

	@Override
	public List<OutBillDetail> findOutBillDetailList(OutBillDetail outBillDetail, String merchantNo, String acqOrgNo,
			String merchantBalance1, String merchantBalance2, String outAccountTaskAmount1,
			String outAccountTaskAmount2, String isChangeRemark, Sort sort, Page<OutBillDetail> page) throws Exception {
		return outBillDetailMapper.findOutBillDetailList(outBillDetail, merchantNo, acqOrgNo, merchantBalance1,
				merchantBalance2, outAccountTaskAmount1, outAccountTaskAmount2, isChangeRemark, sort, page);
	}

	@Override
	public OutBillDetail findOutBillDetailById(String id) {
		return outBillDetailMapper.findOutBillDetailById(id);
	}

	@Override
	public Map<String, Object> updateTransactionImport(List<String> fdetailIdList, List<String> sdetailIdList,
			List<Map<String, Object>> tlist, OutBill outBill, String uname) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		String detailIds = "";
		ObjectMapper om = new ObjectMapper();
		// 线上单笔代付
		// 继续调用T1线下结算接口
		JWTSigner signer2 = new JWTSigner(core2HttpSecret);
		String t1online = core2ApiHttpUrl + "/settle/commitT1DownSettleMoney";
//		Map<String, Object> t1offparams = null;
		OutBillDetail detail = null;
		Integer detailId = -1;
//		String token2 = "";
//		String t1response = "";
		//T1/Tn线下结算接口
		String settleType = "4";
		OutAccountService os = outAccountServiceMapper.getById(outBill.getOutAccountBillMethod());
		if(os.getServiceType()==6){
			settleType = "6";
		}
		for (String detailIdStr : sdetailIdList) {
			// detailId = Integer.valueOf(detailIdStr);
			detail = outBillDetailMapper.findOutBillDetailById(detailIdStr);
			if (detail == null) {
				continue;
			}
			Map<String, Object> t1offparams = new HashMap<String, Object>();
			t1offparams.put("settleUserNo", detail.getMerchantNo());
			t1offparams.put("settleUserType", "M");
			t1offparams.put("settleType", settleType);//4：T1线下结算   6：Tn线下结算
			t1offparams.put("sourceSystem", "account");
			t1offparams.put("sourceOrderNo", detail.getId().toString());
			t1offparams.put("sourceBatchNo", detail.getOutBillId().toString());
			t1offparams.put("outBillStatus", "1");
			String token2 = signer2.sign(t1offparams);
			log.info("出款服务类型："+os.getServiceType()+"/线下回盘导入接口调用url:" + t1online);
			String t1response = HttpConnectUtil.postHttp(t1online, "token", token2);
			log.info("出款服务类型："+os.getServiceType()+"/线下回盘导入接口调用response:" + t1response);

			Map<String, Object> resp = om.readValue(t1response, Map.class);
			if (resp == null || "".equals(resp) || (boolean) resp.get("status") == false) {
				result.put("success", false);
				result.put("msg", "回盘导入失败:" + resp.get("msg"));
				return result;
				// outBillDetailMapper.updateOutBillResultById("T1线下结算：出款失败",2,
				// detail.getId()); //出账失败
			} else {
				// outBillDetailMapper.updateOutBillResultById("T1线下结算：出款成功", 1,
				// detail.getId()); // 出账成功
			}
		}

		for (String detailIdStr : fdetailIdList) {
			// detailId = Integer.valueOf(detailIdStr);
			detail = outBillDetailMapper.findOutBillDetailById(detailIdStr);
			if (detail == null) {
				continue;
			}
			Map<String, Object> t1offparams = new HashMap<String, Object>();
			t1offparams.put("settleUserNo", detail.getMerchantNo());
			t1offparams.put("settleUserType", "M");
			t1offparams.put("settleType", settleType);
			t1offparams.put("sourceSystem", "account");
			t1offparams.put("sourceOrderNo", detail.getId().toString());
			t1offparams.put("sourceBatchNo", detail.getOutBillId().toString());
			t1offparams.put("outBillStatus", "2");
			String token2 = signer2.sign(t1offparams);
			log.info("出款服务类型："+os.getServiceType()+"/线下回盘导入接口调用url:" + t1online);
			String t1response = HttpConnectUtil.postHttp(t1online, "token", token2);
			log.info("出款服务类型："+os.getServiceType()+"/线下回盘导入接口调用response:" + t1response);

			Map<String, Object> resp = om.readValue(t1response, Map.class);
			if (resp == null || "".equals(resp) || (boolean) resp.get("status") == false) {
				result.put("success", false);
				result.put("msg", "回盘导入失败:" + resp.get("msg"));
				return result;
				// outBillDetailMapper.updateOutBillResultById("T1线下结算：出款失败",2,
				// detail.getId()); //出账失败
			} else {
				// outBillDetailMapper.updateOutBillResultById("T1线下结算：出款失败", 2,
				// detailId);
				log.info("不知道为什么不处理 ，打个日志 no handle exception");
			}
		}


		if (fdetailIdList != null && fdetailIdList.size() > 0) {
			detailIds = StringUtils.join(fdetailIdList.toArray(), ",");
			outBillDetailMapper.updateByOutBillDetailIds(3, detailIds);
		}
		if (sdetailIdList != null && sdetailIdList.size() > 0) {
			detailIds = StringUtils.join(sdetailIdList.toArray(), ",");
			outBillDetailMapper.updateByOutBillDetailIds(2, detailIds);
		}

		// 回盘成功之后需要更新数据 回盘人 回盘操作时间
		outBillMapper.updateBackOperator(outBill.getId(), uname);

		result.put("success", true);
		result.put("msg", "回盘导入成功");
		return result;
	}

	private static String getStringCell(Cell cell) {
		if (cell != null)
			cell.setCellType(Cell.CELL_TYPE_STRING);
		return cell != null ? cell.getStringCellValue() : null;
	}

	@Override
	public List<OutBillDetail> findByOutBillIdAndBank(Integer outBillId, String bank) {
		return outBillDetailMapper.findByOutBillIdAndBank(outBillId, bank);
	}

	@Override
	public List<OutBillDetail> findByParams(OutBillDetail outBillDetail, Sort sort, Page<OutBillDetail> page) {
		// 查询出账单明细
		if (StringUtils.isNotBlank(outBillDetail.getStartTime())) {
			outBillDetail.setStartTime(outBillDetail.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(outBillDetail.getEndTime())) {
			outBillDetail.setEndTime(outBillDetail.getEndTime() + " 23:59:59");
		}
		List<OutBillDetail> list = outBillDetailMapper.findByParams(outBillDetail, sort, page);
		return list;
	}

	@Override
	public int updateExportStatusAndSerial(Integer status, String serial, String detailIds) {
		return outBillDetailMapper.updateExportStatusAndSerial(status, serial, detailIds);
	}

	@Override
	public List<OutBillDetail> findByOutBillIdAndBank(Integer outBillId, String bank, Integer status, String date) {
		return outBillDetailMapper.findByOutBillIdAndBank2(outBillId, bank, status, date);
	}

	@Override
	public List<OutBillDetail> findPartByOutBillId(Integer id) {
		return outBillDetailMapper.findPartByOutBillId(id);
	}

	@Override
	public List<OutBillDetail> findAcqOrgByOutBillId(Integer id) {
		return outBillDetailMapper.findAcqOrgByOutBillId(id);
	}

	@Override
	public List<OutBillDetail> findPartByOutBillIdAndAcq(Integer id, String acqEnname) {
		return outBillDetailMapper.findPartByOutBillIdAndAcq(id, acqEnname);
	}

	// @Override
	// public int insertBatch(List<OutBillDetail> list) {
	// return outBillDetailMapper.insertBatch(list);
	// }

	@Override
	public BigDecimal updateOutAccountAmount(OutBillDetail detail) {
		Integer outBillId = detail.getOutBillId(); // 获取出账单id
		outBillDetailMapper.updateOutBillDetailById(detail);

		String uname = detail.getChangeOperatorName();// 获得更改的用户名

		BigDecimal temp = outBillDetailMapper.countOutAccountTaskAmount(outBillId);

		outBillMapper.updateCalcOutAmount(outBillId, temp, uname);
		acqOutBillMapper.udpateCalcOutAmount(temp, outBillId);

		return temp;
	}

	@Override
	public List<OutBillDetail> findExportOutBillDetailList(Map<String, String> params, OutBillDetail detail) {
		return outBillDetailMapper.findExportOutBillDetailList(params, detail);
	}

	@Override
	public BigDecimal updateBatch(Integer outBillId, List<OutBillDetail> detailList, String uname) {
		// 1. 删除所有的出账单详情
		outBillDetailMapper.deleteOutBillDetailByOutBillId(outBillId);
		// 2. 批量插入excel导入的出账单
		// outBillDetailMapper.insertBatch(detailList);

		for (OutBillDetail outBillDetail : detailList) {
			outBillDetailMapper.insertOutBillDetail(outBillDetail);
		}

		BigDecimal totalAmount = outBillDetailMapper.countOutAccountTaskAmount(outBillId);

		outBillMapper.updateCalcOutAmount(outBillId, totalAmount, uname);
		acqOutBillMapper.udpateCalcOutAmount(totalAmount, outBillId);
		return totalAmount;
		/**
		 * outBillDetailDao.updateBatch(list); //只更新出账任务金额(包括大于商户余额的也更新了)和备注
		 * //再次执行更新，如果出账任务金额大于商户余额的则设置出账任务金额为商户余额
		 * outBillDetailDao.updateBatchAgain(outBillId);
		 * 
		 * BigDecimal totalAmount =
		 * outBillDetailDao.countOutAccountTaskAmount(outBillId);
		 * 
		 * outBillDao.updateCalcOutAmount(outBillId, totalAmount);
		 * acqOutBillDao.udpateCalcOutAmount(totalAmount, outBillId);
		 */
	}

	@Override
	public int updateOutBillResultByBillId(String outBillResult, Integer outBillStatus, Integer outBillId) {
		return outBillDetailMapper.updateOutBillResultByBillId(outBillResult, outBillStatus, outBillId);
	}

	@Override
	public List<OutBillDetail> findByOutBillStatusAndAcqEnname(Integer outBillStatus, String acqEnname) {
		return outBillDetailMapper.findByOutBillStatusAndAcqEnname(outBillStatus, acqEnname);
	}

	@Override
	public List<OutBillDetail> findFailedAcqOrg() {
		return outBillDetailMapper.findFailedAcqOrg();
	}

	@Override
	public BigDecimal countOutBillAmount(String acqEnname) {
		return outBillDetailMapper.countOutBillAmount(acqEnname);
	}

	@Override
	public int updateOutBillResultById(String outBillResult, Integer outBillStatus, String id) {
		return outBillDetailMapper.updateOutBillResultById(outBillResult, outBillStatus, id);
	}

	@Override
	public int updateByOutBillDetailIds(Integer status, String detailIds) {
		return outBillDetailMapper.updateByOutBillDetailIds(status, detailIds);
	}

	@Override
	public List<OutBillDetail> findByOutBillIdAndStatus(Integer outBillId, Integer status) {
		return outBillDetailMapper.findByOutBillIdAndStatus(outBillId, status);
	}

	@Override
	public int updateRecordStatusById(String recordStatus, Integer id) {
		return outBillDetailMapper.updateRecordStatusById(recordStatus, id);
	}

	@Override
	public List<OutBillDetail> findTransactionFileByParam(Integer outBillId, String acqEnname) {
		return outBillDetailMapper.findTransactionFileByParam(outBillId, acqEnname);
	}

	@Override
	public List<OutBillDetail> findNotInDetailIds(Integer outBillId, String detailIds) {
		return outBillDetailMapper.findNotInDetailIds(outBillId, detailIds);
	}

	@Override
	public int syncOutBillDetailStatus() {
//		String url = core2ApiHttpUrl + "/settle/queryT1SettleInfo";
//		JWTSigner signer = new JWTSigner(core2HttpSecret);
//		ObjectMapper om = new ObjectMapper();
//		Map<String, Object> param = null;
//		String token = "";
//		String response = "";
//		List<OutBillDetail> outBillDetailList = null;
//		List<OutAccountService> outAccountServiceList = null;
//		String acqEnname = "";
//		List<AcqOrg> acqOrgList = acqOrgMapper.findAll();
//		if (acqOrgList != null && acqOrgList.size() > 0) {
//			for (AcqOrg acq : acqOrgList) {
//				outAccountServiceList = outAccountServiceMapper
//						.findOutAccountServiceListByAcqEnname(acq.getAcqEnname());
//				for (OutAccountService outAccountService : outAccountServiceList) {
//
//					if (outAccountService != null && outAccountService.getServiceType() == 4) {
//						//T1线上出款查询开始
//						onlineOutMoneyForT1(outAccountService,acq.getAcqEnname());
//					} else if (outAccountService != null && outAccountService.getServiceType() == 5) {
//						//T1线下出款查询开始
//						onfflineOutMoneyForT1(outAccountService,acq.getAcqEnname());
//					}
//
//				}
//
//			}
//		}
		
		List<AcqOrg> acqOrgList = acqOrgMapper.findAll();
		for (AcqOrg acq : acqOrgList) {
			String acqEnname = acq.getAcqEnname();
			Map<String, Object> resultMap = outMoneyForT1(acqEnname);
			Boolean resultStatus = (Boolean) resultMap.get("status");
			String resultMsg = (String) resultMap.get("msg");
			if (!resultStatus) {
				log.error("通道={}同步失败,原因:{}",new Object[]{acqEnname,resultMsg});
			}
			else{
				log.info("通道={}同步成功",new Object[]{acqEnname});
			}
		}
		return 1;
	}
	
	
	/**
	 * 
	 * @param acqEnname
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> outMoneyForT1(String acqEnname){
		log.info("T1出款查询开始,通道={}",acqEnname);
		Map<String, Object> msg = new HashMap<>();
		String url = core2ApiHttpUrl + "/settle/queryT1SettleInfo";
		JWTSigner signer = new JWTSigner(core2HttpSecret);
		ObjectMapper om = new ObjectMapper();
		List<OutBillDetail> outBillDetailList = null;
//		String acqEnname = AcqEnname.neweptok.toString();
		outBillDetailList = outBillDetailMapper.findT1AllStatusByAcqEnname(acqEnname);
		for (OutBillDetail detail : outBillDetailList) {
			// 调用出款系统查询接口，依据订单号及类型来查询 订单号的出款状态
			// 如果出款成功，失败，出款中则更新到本地
			OutBill outBill = outBillMapper.findOutBillById(detail.getOutBillId());
			Integer outAccountBillMethod = outBill.getOutAccountBillMethod();
			try {
				if (outAccountBillMethod != null) {
					OutAccountService outAccountService = outAccountServiceMapper.getById(outAccountBillMethod);
					if (outAccountService != null) {
						Integer serviceType = outAccountService.getServiceType();
						Map<String, Object> param = new HashMap<String, Object>();
						if (serviceType  == 4) {
							//T1线上出款查询开始
							param.put("sourceOrderNo", detail.getId());
							param.put("sourceBatchNo", detail.getOutBillId());
							param.put("sourceSystem", "account");
						} 
						else if (serviceType  == 5 || serviceType  == 6){
							//T1线下出款查询开始
							MerchantInfo merInfo = merchantInfoMapper
									.findMerchantInfoByUserId(detail.getMerchantNo());
							param.put("settleUserNo", detail.getMerchantNo());
							param.put("settleUserType", "M");
							param.put("settleType", "4");
							param.put("sourceSystem", "account");
							param.put("createUser", "system");
							param.put("settleAmount", detail.getOutAccountTaskAmount().toString());
							param.put("agentNode", merInfo.getAgentNo());
							param.put("acqenname", detail.getAcqOrgNo());
							param.put("sourceOrderNo", detail.getId().toString());
							param.put("sourceBatchNo", detail.getOutBillId().toString());
							param.put("outBillStatus", "");
							param.put("holidaysMark", "0");
						}
		
						String token = signer.sign(param);
						String response = HttpConnectUtil.postHttp(url, "token", token);
						Boolean isMaybeJson = JSONUtil.mayBeJSON(response);
						if (!isMaybeJson) {
							log.info(url + "连接不上");
							continue;
						}
						Map<String, Object> resp = om.readValue(response, Map.class);
						if (resp == null || "".equals(resp) || (boolean) resp.get("status") == false) {
							// 查询接口调用失败
							log.error("T1结算：执行失败" + resp.toString());
							continue;
						} else {
							OutBillDetail outBillDetail = outBillDetailMapper
									.findOutBillDetailById(detail.getId());
							if (resp.get("recordStatus") != null) {
								String recordStatus = resp.get("recordStatus").toString();
								outBillDetail.setRecordStatus(recordStatus);
							}
							if (resp.get("outBillStatus") != null) {
								Integer outBillStatus = Integer.valueOf(resp.get("outBillStatus").toString());
								String outBillResult = "";
								if (outBillStatus == 1) {
									outBillResult = "T1出款：出款成功";
								} else if (outBillStatus == 2) {
									outBillResult = "T1出款：出款失败";
								} else if (outBillStatus == 3) {
									outBillResult = "T1出款：出款中";
								}
								outBillDetail.setOutBillStatus(outBillStatus);
								outBillDetail.setOutBillResult(outBillResult);
							}
							
						    //如果直接还没到出款，就因为  ‘银行开户名称为空’ ‘银联号’ 为空 直接返回失败结果的，要扣减商户的结算中金额
							if (resp.get("recordStatus") != null && resp.get("outBillStatus") != null) {
								Integer outBillStatus = Integer.valueOf(resp.get("outBillStatus").toString());
								String recordStatus = resp.get("recordStatus").toString();
								log.info("出款订单ID  -- > " + outBillDetail.getId());
								log.info("商户号  -- > " + outBillDetail.getMerchantNo());
								log.info("出款状态  -- > " + outBillStatus);
								log.info("记账状态  -- > " + recordStatus);
								if(outBillStatus == 2 && "NORECORD".equals(recordStatus)){
									log.info("update结算中金额  -- > " + outBillDetail.getOutAccountTaskAmount());
									extAccountMapper.updateSubtractSettlingAmount(outBillDetail.getOutAccountTaskAmount(),outBillDetail.getMerchantNo());
								}
							}
							// 更新到本地
							outBillDetailMapper.updateOutBillDetailById(outBillDetail);// 更新出账单明细表
		
							// 按交易出账时，实时检查状态，实时更新本地的出账状态
							List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
							subOutBillDetailList = subOutBillDetailMapper
									.querySubOutBillListByOutBillDetailId(outBillDetail.getId());
							for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
								subOutBillDetail.setOutBillStatus(outBillDetail.getOutBillStatus());
								subOutBillDetail.setAcqEnname(detail.getAcqOrgNo());
								subOutBillDetail.setSettleTime(new Date());
								if (subOutBillDetail.getOutBillStatus() == 2) {
									// 插入商户出账失败日志
									subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
									subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
									subOutBillDetail.setIsAddBill("0");
								} else {
									subOutBillDetail.setIsAddBill("1");
								}
								// 更新子出账单明细表
								subOutBillDetailMapper.updateOutBillStatusBySubOutBillDetailId(subOutBillDetail);
								// 更新对账详情表交易的出账状态
								duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail, serviceType);
							}
		
							log.info("T1线上出款修改成功");
						}
					}
				}
			} catch (Exception e) {
				// 接口返回参数异常
//				log.error("T1线上出款查询接口异常!", e);
//				throw new RuntimeException("T1线上出款查询接口异常!", e);
//				return false;
				msg.put("status", false);
				msg.put("msg", e.getMessage());
				msg.put("timestamp", String.valueOf(new Date().getTime()));
				log.error(msg.toString());
				return msg;
			}
		}
		msg.put("status", true);
		msg.put("msg", "同步成功");
		msg.put("timestamp", String.valueOf(new Date().getTime()));
		
		return msg;
	
	}

//	/**
//	 * 
//	 * @param outAccountService
//	 * @param acqEnname
//	 * @return
//	 */
//	private Boolean onlineOutMoneyForT1(OutAccountService outAccountService,String acqEnname){
//
//		log.info("T1线上出款查询开始");
//		String url = core2ApiHttpUrl + "/settle/queryT1SettleInfo";
//		JWTSigner signer = new JWTSigner(core2HttpSecret);
//		ObjectMapper om = new ObjectMapper();
////		Map<String, Object> param = null;
////		String token = "";
////		String response = "";
//		List<OutBillDetail> outBillDetailList = null;
////		List<OutAccountService> outAccountServiceList = null;
////		String acqEnname = "";
////		acqEnname = acq.getAcqEnname();
//		outBillDetailList = outBillDetailMapper.findT1AllStatusByAcqEnname(acqEnname);
//		for (OutBillDetail detail : outBillDetailList) {
//			// 调用出款系统查询接口，依据订单号及类型来查询 订单号的出款状态
//			// 如果出款成功，失败，出款中则更新到本地
//			try {
//				Map<String, Object> param = new HashMap<String, Object>();
//				param.put("sourceOrderNo", detail.getId());
//				param.put("sourceBatchNo", detail.getOutBillId());
//				param.put("sourceSystem", "account");
//
//				String token = signer.sign(param);
//				String response = HttpConnectUtil.postHttp(url, "token", token);
//				Boolean isMaybeJson = JSONUtil.mayBeJSON(response);
//				if (!isMaybeJson) {
//					log.info(url + "连接不上");
//					continue;
//				}
//				Map<String, Object> resp = om.readValue(response, Map.class);
//				if (resp == null || "".equals(resp) || (boolean) resp.get("status") == false) {
//					// 查询接口调用失败
//					log.error("T1线上结算：执行失败" + resp.toString());
//					continue;
//				} else {
//					OutBillDetail outBillDetail = outBillDetailMapper
//							.findOutBillDetailById(detail.getId());
//					if (resp.get("recordStatus") != null) {
//						String recordStatus = resp.get("recordStatus").toString();
//						outBillDetail.setRecordStatus(recordStatus);
//					}
//					if (resp.get("outBillStatus") != null) {
//						Integer outBillStatus = Integer.valueOf(resp.get("outBillStatus").toString());
//						String outBillResult = "";
//						if (outBillStatus == 1) {
//							outBillResult = "T1线上出款：出款成功";
//						} else if (outBillStatus == 2) {
//							outBillResult = "T1线上出款：出款失败";
//						} else if (outBillStatus == 3) {
//							outBillResult = "T1线上出款：出款中";
//						}
//						outBillDetail.setOutBillStatus(outBillStatus);
//						outBillDetail.setOutBillResult(outBillResult);
//					}
//					// 更新到本地
//					outBillDetailMapper.updateOutBillDetailById(outBillDetail);// 更新出账单明细表
//
//					// 按交易出账时，实时检查状态，实时更新本地的出账状态
//					List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
//					subOutBillDetailList = subOutBillDetailMapper
//							.querySubOutBillListByOutBillDetailId(outBillDetail);
//					for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
//						subOutBillDetail.setOutBillStatus(outBillDetail.getOutBillStatus());
//						subOutBillDetail.setAcqEnname(detail.getAcqOrgNo());
//						if (subOutBillDetail.getOutBillStatus() == 2) {
//							// 插入商户出账失败日志
//							subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
//							subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
//							subOutBillDetail.setIsAddBill("0");
//						} else {
//							subOutBillDetail.setIsAddBill("1");
//						}
//						// 更新子出账单明细表
//						subOutBillDetailMapper
//								.updateOutBillStatusBySubOutBillDetailId(subOutBillDetail);
//						// 更新对账详情表交易的出账状态
//						duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail,
//								outAccountService.getServiceType());
//					}
//
//					log.info("T1线上出款修改成功");
//				}
//			} catch (Exception e) {
//				// 接口返回参数异常
//				log.error("T1线上出款查询接口异常!", e);
////				throw new RuntimeException("T1线上出款查询接口异常!", e);
//				return false;
//			}
//		}
//		return true;
//	
//	}
	
//	/**
//	 * 
//	 * @param outAccountService
//	 * @param acqEnname
//	 * @return
//	 */
//	private Boolean onfflineOutMoneyForT1(OutAccountService outAccountService,String acqEnname){
//
//		log.info("T1线下出款查询开始");
//		String url = core2ApiHttpUrl + "/settle/queryT1SettleInfo";
//		JWTSigner signer = new JWTSigner(core2HttpSecret);
//		ObjectMapper om = new ObjectMapper();
////		Map<String, Object> param = null;
////		String token = "";
////		String response = "";
//		List<OutBillDetail> outBillDetailList = null;
////		List<OutAccountService> outAccountServiceList = null;
////		String acqEnname = "";
////		acqEnname = acq.getAcqEnname();
//		outBillDetailList = outBillDetailMapper.findT1AllStatusByAcqEnname(acqEnname);
//		for (OutBillDetail item : outBillDetailList) {
//			try {
//				MerchantInfo merInfo = merchantInfoMapper
//						.findMerchantInfoByUserId(item.getMerchantNo());
//				Map<String, Object> t1offparams = new HashMap<String, Object>();
//				t1offparams.put("settleUserNo", item.getMerchantNo());
//				t1offparams.put("settleUserType", "M");
//				t1offparams.put("settleType", "4");
//				t1offparams.put("sourceSystem", "account");
//				t1offparams.put("createUser", "system");
//				t1offparams.put("settleAmount", item.getOutAccountTaskAmount().toString());
//				t1offparams.put("agentNode", merInfo.getAgentNo());
//				t1offparams.put("acqenname", item.getAcqOrgNo());
//				t1offparams.put("sourceOrderNo", item.getId().toString());
//				t1offparams.put("sourceBatchNo", item.getOutBillId().toString());
//				t1offparams.put("outBillStatus", "");
//				t1offparams.put("holidaysMark", "0");
//
//				String token = signer.sign(t1offparams);
//				String response = HttpConnectUtil.postHttp(url, "token", token);
//				Boolean isMaybeJson = JSONUtil.mayBeJSON(response);
//				if (!isMaybeJson) {
//					log.info(url + "连接不上");
//					continue;
//				}
//				Map<String, Object> resp2 = om.readValue(response, Map.class);
//				OutBillDetail outBillDetail = outBillDetailMapper.findOutBillDetailById(item.getId());
//				if (resp2 == null || "".equals(resp2) || (boolean) resp2.get("status") == false) {
//					// outBillDetail.setOutBillStatus(4);
//					// outBillDetail.setOutBillResult("T1线下结算：执行失败");
//					log.error("T1线下结算：执行失败" + resp2.toString());
//					continue;
//				} else {
//					if (resp2.get("recordStatus") != null) {
//						String recordStatus = resp2.get("recordStatus").toString();
//						outBillDetail.setRecordStatus(recordStatus);
//					}
//					if (resp2.get("outBillStatus") != null) {
//						Integer outBillStatus = Integer.valueOf(resp2.get("outBillStatus").toString());
//						String outBillResult = "";
//						if (outBillStatus == 1) {
//							outBillResult = "T1线下出款：出款成功";
//						} else if (outBillStatus == 2) {
//							outBillResult = "T1线下出款：出款失败";
//						} else if (outBillStatus == 3) {
//							outBillResult = "T1线下出款：出款中";
//						}
//						outBillDetail.setOutBillStatus(outBillStatus);
//						outBillDetail.setOutBillResult(outBillResult);
//					}
//
//					// outBillDetail.setOutBillStatus(3);
//					// outBillDetail.setOutBillResult("T1线下结算：出款中");
//				}
//
//				outBillDetailMapper.updateOutBillDetailById(outBillDetail);
//
//				// 按交易出账时，实时检查状态，实时更新本地的出账状态
//				List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
//				subOutBillDetailList = subOutBillDetailMapper
//						.querySubOutBillListByOutBillDetailId(outBillDetail);
//				for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
//					subOutBillDetail.setOutBillStatus(outBillDetail.getOutBillStatus());
//					subOutBillDetail.setAcqEnname(item.getAcqOrgNo());
//					if (subOutBillDetail.getOutBillStatus() == 2) {
//						// 插入商户出账失败日志
//						subOutBillDetail.setSubOutBillDetailId(subOutBillDetail.getId());
//						subOutBillDetailLogsMapper.insertOutBillDetailLogs(subOutBillDetail);
//						subOutBillDetail.setIsAddBill("0");
//					} else {
//						subOutBillDetail.setIsAddBill("1");
//					}
//					// 更新子出账单明细表
//					subOutBillDetailMapper.updateOutBillStatusBySubOutBillDetailId(subOutBillDetail);
//					// 更新对账详情表交易的出账状态
//					duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail,
//							outAccountService.getServiceType());
//				}
//
//				log.info("T1线下出款修改成功");
//			} catch (Exception e) {
//				// 接口返回参数异常
//				log.error("T1线下出款查询接口异常!", e);
////				throw new RuntimeException("T1线下出款查询接口异常!", e);
//				return false;
//			}
//		}
//		return true;
//	}
	@Override
	public int insertTestBatch(List<OutBillDetail> list) {
		return outBillDetailMapper.insertTestBatch(list);
	}

	@Override
	public int insertTestBatch2(List<OutBillDetail> list) {
		return outBillDetailMapper.insertTestBatch2(list);
	}

	@Override
	public int insertTestBatch3(List<OutBillDetail> list) {
		return outBillDetailMapper.insertTestBatch3(list);
	}

	@Override
	public List<OutBillDetail> findByOutBillId(Integer id) {
		return outBillDetailMapper.findByOutBillId(id);
	}


}
