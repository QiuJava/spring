package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.DuiAccountDetailMapper;
import cn.eeepay.framework.dao.bill.ExtAccountMapper;
import cn.eeepay.framework.dao.bill.OutBillDetailMapper;
import cn.eeepay.framework.dao.bill.SubOutBillDetailMapper;
import cn.eeepay.framework.dao.nposp.MerchantCardInfoMapper;
import cn.eeepay.framework.dao.nposp.MerchantInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.AccountStatus;
import cn.eeepay.framework.enums.OutBillRecordStatus;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.nposp.MerchantCardInfo;
import cn.eeepay.framework.model.nposp.MerchantInfo;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("subOutBillDetailService")
@Transactional
public class SubOutBillDetailServiceImpl implements SubOutBillDetailService {
	private static final Logger log = LoggerFactory.getLogger(SubOutBillDetailServiceImpl.class);
	@Resource
	public SubOutBillDetailMapper subOutBillDetailMapper;
	@Resource
	public DuiAccountDetailMapper duiAccountDetailMapper;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public AcqOutBillService acqOutBillService;
	@Resource
	public OutBillService outBillService;
	@Resource
	public ExtAccountService  extAccountService;
	@Resource
	private MerchantInfoMapper merchantInfoMapper;
	@Resource
	private MerchantCardInfoMapper merchantCardInfoMapper;
	@Resource
	private ExtAccountMapper extAccountMapper;
	@Resource
	private OutBillDetailMapper outBillDetailMapper;
	
	@Override
	public int insertOutBillDetail(SubOutBillDetail subOutBillDetail) throws Exception {
		return subOutBillDetailMapper.insertSubOutBillDetail(subOutBillDetail);
	}
	@Override
	public List<SubOutBillDetail> findSubOutBillDetailList(SubOutBillDetail subOutBillDetail, String merchantNo,
			String acqOrgNo, String merchantBalance1, String merchantBalance2, String outAccountTaskAmount1,
			String outAccountTaskAmount2, String isChangeRemark, String timeStart, String timeEnd, Sort sort, Page<SubOutBillDetail> page) {
		if (StringUtils.isNotBlank(timeStart)) {
			timeStart = timeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			timeEnd = timeEnd + " 23:59:59";
		}
		return subOutBillDetailMapper.findSubOutBillDetailList(subOutBillDetail, merchantNo,
				acqOrgNo, merchantBalance1, merchantBalance2, outAccountTaskAmount1,
				outAccountTaskAmount2, isChangeRemark, timeStart, timeEnd,sort, page);
	}
	@Override
	public int updateOutBillDetailChangeRemark(SubOutBillDetail subOutBillDetail) {
		return subOutBillDetailMapper.updateOutBillDetailChangeRemark(subOutBillDetail);
	}
	
	@Override
	public  Map<String, Object> updateRemarkBacthOutBillDetailById(List<SubOutBillDetail> subOutBillDetailList) {
		
		
		Map<String,Object> msg=new HashMap<>();
		int i = 0;
		int batchCount = 50;
		List<SubOutBillDetail> asdList = new ArrayList<>();
		List<List<?>> subOutBillDetailSplitList = ListUtil.batchList(subOutBillDetailList, batchCount);
		for (List<?> clist : subOutBillDetailSplitList) {
			if (clist.size() > 0) {
				for (Object object : clist) {
					SubOutBillDetail asd = (SubOutBillDetail) object;
					asdList.add(asd);
				}
				log.info("修改代理商分润日结表{}条",asdList.size());
				int j = subOutBillDetailMapper.updateRemarkBacthOutBillDetailById(asdList);
				if (j > 0) {
					i = i + j;
				}
				if (!asdList.isEmpty()) {
					asdList.clear();
				}
			}
		}
		if (i > 0) {
			msg.put("status", true);
			msg.put("msg", "执行成功");
		}
		else{
			msg.put("status", true);
			msg.put("msg", "没有修改任何数据");
		}
		return msg;
	}
	
	@Override
	public int updateOutBillDetailById(List<SubOutBillDetail> subOutBillDetailList) {
		int returnNum = 0;
		String isAddBill = "0";
		OutBill outBill = new OutBill();

		try {
			for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
				synchronized(this){
				// 更新 出账单收单机构表
				List<AcqOutBill> list = acqOutBillService.findByOutBillId(subOutBillDetail.getOutBillId());
				for (AcqOutBill acqOutBill : list) {
					BigDecimal calcOutAmount = BigDecimal.ZERO;
					BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
					calcOutAmount = acqOutBill.getCalcOutAmount();
					outAccountTaskAmount = acqOutBill.getOutAccountTaskAmount();
					calcOutAmount = calcOutAmount.subtract(subOutBillDetail.getOutAccountTaskAmount());
					outAccountTaskAmount = outAccountTaskAmount.subtract(subOutBillDetail.getOutAccountTaskAmount());
					acqOutBill.setCalcOutAmount(calcOutAmount);
					acqOutBill.setOutAccountTaskAmount(outAccountTaskAmount);
					acqOutBillService.updateAcqOutBillById(acqOutBill);

				}
				outBill = outBillService.findOutBillById(subOutBillDetail.getOutBillId());
				// 更新 出账单
				if (outBill != null) {
					BigDecimal calcOutAmount = BigDecimal.ZERO;
					BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
					calcOutAmount = outBill.getCalcOutAmount();
					outAccountTaskAmount = outBill.getOutAccountTaskAmount();
					calcOutAmount = calcOutAmount.subtract(subOutBillDetail.getOutAccountTaskAmount());
					outAccountTaskAmount = outAccountTaskAmount.subtract(subOutBillDetail.getOutAccountTaskAmount());
					outBill.setCalcOutAmount(calcOutAmount);
					outBill.setOutAccountTaskAmount(outAccountTaskAmount);
					outBillService.updateOutBillById(outBill);
				}
				log.info("该子出账单的出款状态是  " + subOutBillDetail.getOutBillStatus() + "参考号是： "
						+ subOutBillDetail.getOrderReferenceNo());
				if (subOutBillDetail.getOutBillStatus() == 0) {// 如果是未出款状态，则直接删除，如果是从失败记录重新添加，则
																// update
																// 是否添加出账单，出账单id
																// 就好
					duiAccountDetailMapper.updateDuiAccountDetailByOrderReNum(subOutBillDetail, isAddBill);
					returnNum = subOutBillDetailMapper.deleteSubOutBillDetailById(subOutBillDetail);
				} else {
					OutBillDetail outBillDetail = outBillDetailMapper
							.findOutBillDetailById(subOutBillDetail.getOutBillDetailId());
					if (outBillDetail != null) {
						subOutBillDetail.setOutBillId(outBillDetail.getOutBillId());
					}
					returnNum = subOutBillDetailMapper.updateOutBillDetailById(subOutBillDetail);
				}
				if (returnNum > 0) {
					// 更新商户的结算中金额
					extAccountMapper.updateSubtractSettlingAmount(subOutBillDetail.getOutAccountTaskAmount(),
							subOutBillDetail.getMerchantNo());
				}
			 }
			}
		} catch (Exception e) {
			log.error("删除子出账单异常!", e);
			throw new RuntimeException("删除子出账单异常!", e);
		}

		return returnNum;
	}

	@Override
	public List<SubOutBillDetail> findSubMerChuAccountList(SubOutBillDetail subOutBillDetail, Sort sort,
			Page<SubOutBillDetail> page) {

		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetail.getStartTime())) {
			subOutBillDetail.setStartTime(subOutBillDetail.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetail.getEndTime())) {
			subOutBillDetail.setEndTime(subOutBillDetail.getEndTime() + " 23:59:59");
		}

		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetail.getTransTimeStart())) {
			subOutBillDetail.setTransTimeStart(subOutBillDetail.getTransTimeStart() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetail.getTransTimesEnd())) {
			subOutBillDetail.setTransTimesEnd(subOutBillDetail.getTransTimesEnd() + " 23:59:59");
		}
		return subOutBillDetailMapper.findSubMerChuAccountList(subOutBillDetail, sort, page);
	}
	@Override
	public int updateOutBillDetailByOrderReNum(SubOutBillDetail subOutBillDetail, String isAddBill) {
		return subOutBillDetailMapper.updateOutBillDetailByOrderReNum(subOutBillDetail, isAddBill);
	}
	@Override
	public SubOutBillDetail queryOutBillDetailById(SubOutBillDetail subOutBillDetail) {
		return subOutBillDetailMapper.queryOutBillDetailById(subOutBillDetail);
	}
	
	
	@Override
	public Map<String, Object> judgeIsAddSubOutDetail(List<String> subDetailIdList,Integer outBillId) {
		String currencyNo = "";
		String accountType = "M";
		String accountOwner = "";  //机构组织id
		String merchantNo = "";
		Map<String,Object> judgemsg=new HashMap<>();
		try {
		for (String subOutBillDetailId : subDetailIdList) {
			synchronized(this){
			SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
			subOutBillDetail.setId(subOutBillDetailId);
			subOutBillDetail.setOutBillId(outBillId);
			SubOutBillDetail dbsubOutBillDetail = subOutBillDetailMapper.queryOutBillDetailById(subOutBillDetail);
			BigDecimal availableAmount = BigDecimal.ZERO;//可用余额
			//判断商户可用余额
			merchantNo = dbsubOutBillDetail.getMerchantNo();
			ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, merchantNo, accountOwner, "", "224101001", currencyNo);
			ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
			if(extAccount != null ){
				
				availableAmount = (extAccount.getCurrBalance()==null?BigDecimal.ZERO:extAccount.getCurrBalance()
						.subtract(extAccount.getControlAmount()==null?BigDecimal.ZERO:extAccount.getControlAmount()))
						.subtract(extAccount.getSettlingAmount()==null?BigDecimal.ZERO:extAccount.getSettlingAmount().add(dbsubOutBillDetail.getOutAccountTaskAmount())) ;
				
				if (availableAmount.compareTo(BigDecimal.ZERO) < 0) {
					judgemsg.put("status", false);
					judgemsg.put("msg", dbsubOutBillDetail.getMerchantNo()+"可用余额不足！");
					return judgemsg;
				}else{
					judgemsg.put("status", true);
				}
			 }else{
					judgemsg.put("status", false);
					judgemsg.put("msg", dbsubOutBillDetail.getMerchantNo() + "不存在或者不合法！");
					return judgemsg;
			  }
			 }
			}
		} catch (Exception e) {
			judgemsg.put("status", false);
			judgemsg.put("msg", "加入出账单异常！");
		}
		return judgemsg;
	}
	
	@Override
	public int updateSubOutDetaiAndCheckOutDetail(List<String> subDetailIdList, BigDecimal subTotal, Integer outBillId) {
		int i = 0 , j = 0;
		String isAddBill = "1";
		OutBill outBill = new OutBill();
		try {
        outBill = outBillService.findOutBillById(outBillId);
		for (String subOutBillDetailId : subDetailIdList) {
			SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
			subOutBillDetail.setId(subOutBillDetailId);
			subOutBillDetail.setOutBillId(outBillId);
			SubOutBillDetail dbsubOutBillDetail = subOutBillDetailMapper.queryOutBillDetailById(subOutBillDetail);
			int returnNum = 0;
			//update 商户的结算中金额
			returnNum = extAccountService.updateAddSettlingAmount(dbsubOutBillDetail.getOutAccountTaskAmount(), dbsubOutBillDetail.getMerchantNo());
            if(returnNum > 0){
            	i = i + subOutBillDetailMapper.updateOutBillDetailByOrderReNum(subOutBillDetail, isAddBill);
    			j = j + duiAccountDetailMapper.updateDuiAccountDetailByOrderReNum(dbsubOutBillDetail, isAddBill);
            }
			
		}

		//更新 出账单收单机构表
		List<AcqOutBill> list = acqOutBillService.findByOutBillId(outBillId);
		for (AcqOutBill acqOutBill : list) {
			 BigDecimal calcOutAmount = BigDecimal.ZERO;
			 BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
			 calcOutAmount = acqOutBill.getCalcOutAmount();
			 outAccountTaskAmount = acqOutBill.getOutAccountTaskAmount();
			 calcOutAmount = calcOutAmount.add(subTotal);
			 outAccountTaskAmount = outAccountTaskAmount.add(subTotal);
			 acqOutBill.setCalcOutAmount(calcOutAmount);
			 acqOutBill.setOutAccountTaskAmount(outAccountTaskAmount);
			 acqOutBillService.updateAcqOutBillById(acqOutBill);
		}
		// 更新 出账单
		if(outBill != null ){
			BigDecimal calcOutAmount = BigDecimal.ZERO;
			BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
			calcOutAmount = outBill.getCalcOutAmount();
			outAccountTaskAmount = outBill.getOutAccountTaskAmount();
			calcOutAmount = calcOutAmount.add(subTotal);
			outAccountTaskAmount = outAccountTaskAmount.add(subTotal); 
			outBill.setCalcOutAmount(calcOutAmount);
			outBill.setOutAccountTaskAmount(outAccountTaskAmount);
			outBillService.updateOutBillById(outBill);
		}
		} catch (Exception e) {
			throw new RuntimeException("加入出账单异常 ： " +e);
		}
		return i + j;
	}
	@Override
	public List<SubOutBillDetail> exportOutBillDetailList(SubOutBillDetail subOutBillDetail) {
		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetail.getStartTime())) {
			subOutBillDetail.setStartTime(subOutBillDetail.getStartTime() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetail.getEndTime())) {
			subOutBillDetail.setEndTime(subOutBillDetail.getEndTime() + " 23:59:59");
		}

		// 查询子出账单明细
		if (StringUtils.isNotBlank(subOutBillDetail.getTransTimeStart())) {
			subOutBillDetail.setTransTimeStart(subOutBillDetail.getTransTimeStart() + " 00:00:00");
		}
		if (StringUtils.isNotBlank(subOutBillDetail.getTransTimesEnd())) {
			subOutBillDetail.setTransTimesEnd(subOutBillDetail.getTransTimesEnd() + " 23:59:59");
		}
		return subOutBillDetailMapper.exportOutBillDetailList(subOutBillDetail);
	}
	@Override
	public SubOutBillDetail queryOutBillDetailByOrderRefNum(SubOutBillDetail subOutBillDetail) {
		return subOutBillDetailMapper.queryOutBillDetailByOrderRefNum(subOutBillDetail);
	}
	@Override
	public List<SubOutBillDetail> exportSubOutBillDetailList(SubOutBillDetail subOutBillDetail, String merchantNo,
			String acqOrgNo, String merchantBalance1, String merchantBalance2, String outAccountTaskAmount1,
			String outAccountTaskAmount2, String isChangeRemark, String timeStart, String timeEnd) {
		if (StringUtils.isNotBlank(timeStart)) {
			timeStart = timeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(timeEnd)) {
			timeEnd = timeEnd + " 23:59:59";
		}
		return subOutBillDetailMapper.exportSubOutBillDetailList(subOutBillDetail, merchantNo, acqOrgNo, merchantBalance1, merchantBalance2, outAccountTaskAmount1, outAccountTaskAmount2, isChangeRemark, timeStart, timeEnd);
	}

	@Override
	public String insertAndUpdateRecordStatus(DuiAccountDetail duiAccountDetail, Integer outBillId) {
		MerchantInfo merchantInfo = null;
		List<MerchantCardInfo> merchantCardInfos = null;
		String currencyNo = "";
		String accountType = "M";
		String accountOwner = "";  //机构组织id
		String merchantNo = "";
		String subOutBillDetailId = "";
		OutBill outBill = new OutBill();
		SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
		try {
			outBill = outBillService.findOutBillById(outBillId);
			
			subOutBillDetailId = genericTableService.subOutBillDetailNo();
			subOutBillDetail.setId(subOutBillDetailId);
			subOutBillDetail.setOutBillId(outBillId);
			subOutBillDetail.setMerchantNo(duiAccountDetail.getPlateMerchantNo());
			subOutBillDetail.setMerchantBalance(duiAccountDetail.getPlateTransAmount());
			subOutBillDetail.setOrderReferenceNo(duiAccountDetail.getOrderReferenceNo());//订单参考号
			subOutBillDetail.setTransTime(duiAccountDetail.getPlateAcqTransTime());//交易时间
			subOutBillDetail.setTransAmount(duiAccountDetail.getPlateTransAmount());//交易金额
			BigDecimal tranOutAmount = BigDecimal.ZERO;
			tranOutAmount = tranOutAmount.add(duiAccountDetail.getPlateTransAmount().subtract(duiAccountDetail.getPlateMerchantFee()));
			subOutBillDetail.setOutAccountTaskAmount(tranOutAmount);
			subOutBillDetail.setAcqOrgNo(outBill.getAcqEnname());

			//直清通道 必改这里
			if("ZF_ZQ".equals(outBill.getAcqEnname())|| "ZG_ZQ".equals(outBill.getAcqEnname()) ){
				subOutBillDetail.setAcqMerchantNo(duiAccountDetail.getAcqMerchantNo());
				subOutBillDetail.setPlateMerchantEntryNo(duiAccountDetail.getPlateMerchantEntryNo());
			}


			//判断商户是否存在，合法
			merchantNo = duiAccountDetail.getPlateMerchantNo();
			ExtAccountInfo extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, merchantNo, accountOwner, "", "224101001", currencyNo);
			ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
			merchantInfo = merchantInfoMapper.findMerchantInfoByUserId(merchantNo);
			merchantCardInfos = merchantCardInfoMapper.getByMerchantNo(merchantNo);
			if (merchantInfo == null || merchantCardInfos == null || merchantCardInfos.isEmpty()) {
				return "0";
			}
			
			 if (extAccount == null) {
				  subOutBillDetail.setVerifyFlag("2");
				  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"销户");
				  return "-3";//商户不合法
	         }	
			 else  if (AccountStatus.DESTROY.toString().equals(extAccount.getAccountStatus())) {
				  subOutBillDetail.setVerifyFlag("2");
				  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"销户");
	          }
			  else  if (AccountStatus.FREEZE_ONLY_IN_DENY_OUT.toString().equals(extAccount.getAccountStatus())) {
				  subOutBillDetail.setVerifyFlag("2");
				  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"冻结只进不出");
	          }
	          else if (AccountStatus.FREEZE_DENY_IN_DENY_OUT.toString().equals(extAccount.getAccountStatus())) {
	        	  subOutBillDetail.setVerifyFlag("2");
	        	  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"冻结不进不出");
		      }else {
		    	  subOutBillDetail.setVerifyFlag("1");
		    	  subOutBillDetail.setVerifyMsg("校验成功");
	          }
			 BigDecimal availableAmount = BigDecimal.ZERO;//可用余额
			 availableAmount = (extAccount.getCurrBalance()==null?BigDecimal.ZERO:extAccount.getCurrBalance()
						.subtract(extAccount.getControlAmount()==null?BigDecimal.ZERO:extAccount.getControlAmount()))
						.subtract(extAccount.getSettlingAmount()==null?BigDecimal.ZERO:extAccount.getSettlingAmount().add(tranOutAmount)) ;
			 
			if (availableAmount.compareTo(BigDecimal.ZERO) < 0) {
				return "-2";//商户可用余额不足
			}
			 
		    subOutBillDetail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
		    subOutBillDetail.setOutBillStatus(0);//未出账
		    subOutBillDetail.setIsAddBill("1");
		    
			subOutBillDetailMapper.insertSubOutBillDetail(subOutBillDetail);
			//更新对账详情表交易的出账状态 以及是否 添加出账单的状态
			duiAccountDetailMapper.updateOutBillStatusByOrderRefNum(subOutBillDetail,0);
			
			//更新 出账单收单机构表
			List<AcqOutBill> list = acqOutBillService.findByOutBillId(outBillId);
			for (AcqOutBill acqOutBill : list) {
				 BigDecimal calcOutAmount = BigDecimal.ZERO;
				 BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
				 calcOutAmount = acqOutBill.getCalcOutAmount();
				 outAccountTaskAmount = acqOutBill.getOutAccountTaskAmount();
				 calcOutAmount = calcOutAmount.add(subOutBillDetail.getOutAccountTaskAmount());
				 outAccountTaskAmount = outAccountTaskAmount.add(subOutBillDetail.getOutAccountTaskAmount());
				 acqOutBill.setCalcOutAmount(calcOutAmount);
				 acqOutBill.setOutAccountTaskAmount(outAccountTaskAmount);
				 acqOutBillService.updateAcqOutBillById(acqOutBill);
			}
			// 更新 出账单
			if(outBill != null ){
				 BigDecimal calcOutAmount = BigDecimal.ZERO;
				 BigDecimal outAccountTaskAmount = BigDecimal.ZERO;
				 calcOutAmount = outBill.getCalcOutAmount();
				 outAccountTaskAmount = outBill.getOutAccountTaskAmount();
				 calcOutAmount = calcOutAmount.add(subOutBillDetail.getOutAccountTaskAmount());
				 outAccountTaskAmount = outAccountTaskAmount.add(subOutBillDetail.getOutAccountTaskAmount());
				 outBill.setCalcOutAmount(calcOutAmount);
				 outBill.setOutAccountTaskAmount(outAccountTaskAmount);
				 outBillService.updateOutBillById(outBill);
			}
			//update 商户的结算中金额
			extAccountService.updateAddSettlingAmount(subOutBillDetail.getOutAccountTaskAmount(), merchantNo);
			
		} catch (Exception e) {
			log.error("导入记录失败!", e);
			throw new RuntimeException("导入记录失败!",e);
		}
		return "1";
	}
	@Override
	public int findVerifyFlag(Integer id) {
		return subOutBillDetailMapper.findVerifyFlag(id);
	}


}
