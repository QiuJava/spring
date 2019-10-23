package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.ChuAccountMapper;
import cn.eeepay.framework.dao.bill.DuiAccountDetailMapper;
import cn.eeepay.framework.dao.bill.OutBillDetailMapper;
import cn.eeepay.framework.dao.bill.SubOutBillDetailMapper;
import cn.eeepay.framework.dao.nposp.AcqOrgMapper;
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
import cn.eeepay.framework.service.nposp.MerchantInfoService;
import cn.eeepay.framework.util.DateUtil;
import cn.eeepay.framework.util.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;



@Service("chuAccountService")
@Transactional
public class ChuAccountServiceImpl implements ChuAccountService{
	
	private static final Logger log = LoggerFactory.getLogger(ChuAccountServiceImpl.class);
	@Resource
	public ChuAccountMapper chuAccountMapper;
	@Resource
	public OutAccountTaskService outAccountTaskService;
	@Resource
	public OutAccountTaskDetailService outAccountTaskDetailService;
	@Resource
	public OutBillService outBillService;
	@Resource
	public OutBillDetailService outBillDetailService;
	@Resource
	public ExtAccountService  extAccountService;
	@Resource
	public AcqOutBillService acqOutBillService;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public GenericTableService genericTableService;
	@Resource
	public OutBillDetailMapper outBillDetailMapper;
	
	@Resource
	public SubOutBillDetailMapper subOutBillDetailMapper;
	@Resource
	private MerchantInfoMapper merchantInfoMapper;
	@Resource
	private MerchantCardInfoMapper merchantCardInfoMapper;
	
	@Resource
	private DuiAccountDetailService duiAccountDetailService;
	
	@Resource
	private DuiAccountDetailMapper duiAccountDetailMapper;
	
	@Resource
	private AcqOrgMapper acqOrgMapper;
	
	@Resource
	private MerchantInfoService merchantInfoService;

	@Override
	public List<SettleTransferFile> findSettleTransferFileList(SettleTransferFile settleTransferFile, Sort sort,
			Page<SettleTransferFile> page) throws Exception {
		return chuAccountMapper.findSettleTransferFileList(settleTransferFile, sort, page);
	}

	@Override
	public SettleTransferFile findSettleTransferFileById(Integer id) {
		return chuAccountMapper.findSettleTransferFileById(id);
	}



	@Override
	public int judgeCreateOutBill(Integer outAccountTaskId) throws Exception {
		OutAccountTask outAccountTask = outAccountTaskService.findOutAccountTaskById(outAccountTaskId);
		//对应时间内的 对账详情 即是：该短时间的交易数据
		//判断出账范围
		String startTime = "";
		String endTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if ("T1".equals(outAccountTask.getOutBillRange())) {
			startTime = acqOrgMapper.findDayAlteredStartTimeByAcqEnname(outAccountTask.getAcqEnname());
			endTime = acqOrgMapper.findDayAlteredEndTimeByAcqEnname(outAccountTask.getAcqEnname());
		} else if ("Tn".equals(outAccountTask.getOutBillRange())) {
			startTime = acqOrgMapper.findDayAlteredStartTimeByAcqEnname(outAccountTask.getAcqEnname());
//			startTime = DateUtil.subDayFormatLong(sdf.parse(endTime),7);
		}else{
			endTime = sdf.format(new Date());
			startTime = DateUtil.subDayFormatLong(new Date(),7);
		}
		List<DuiAccountDetail> duiAccountDetailList = duiAccountDetailService.findAllTranByAcqNameAndDate(outAccountTask.getAcqEnname(), outAccountTask.getTransTime(), startTime, endTime);
		for (DuiAccountDetail duiAccountDetail : duiAccountDetailList) {
			  if ("NO_CHECKED".equals(duiAccountDetail.getCheckAccountStatus())) {
			    		return 1;
				}
		}
		return 0;
	}

	
	@Override
	public int createOutBill(Integer outAccountTaskId, UserInfo userInfo)  {
		log.info("开始生成出账单!");
		OutAccountTask outAccountTask = outAccountTaskService.findOutAccountTaskById(outAccountTaskId);
		OutBill outBill = new OutBill();
		BigDecimal outAccountTaskAmount = outAccountTask.getOutAccountTaskAmount();
		
		List<OutAccountTaskDetail> outAccountTaskDetailList = outAccountTaskDetailService.findOutAccountTaskDetailByTaskId(outAccountTaskId);
		
		outBill.setOutAccountTaskId(outAccountTaskId);
		outBill.setOutAccountTaskAmount(outAccountTaskAmount);
		outBill.setAcqEnname(outAccountTask.getAcqEnname());
		BigDecimal calcOutAmount = BigDecimal.ZERO;
		List<SubOutBillDetail> subOutBillDetailList = new ArrayList<SubOutBillDetail>();
		List<String> acqOrgNoList = new ArrayList<String>();
		List<String> balanceMerchantCountList = new ArrayList<String>();
		
		//判断出账范围
		String startTime = "";
		String endTime = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if("T1".equals(outAccountTask.getOutBillRange())){
			startTime = acqOrgMapper.findDayAlteredStartTimeByAcqEnname(outAccountTask.getAcqEnname());
			endTime = acqOrgMapper.findDayAlteredEndTimeByAcqEnname(outAccountTask.getAcqEnname());
		}else if("Tn".equals(outAccountTask.getOutBillRange())){
			startTime = acqOrgMapper.findDayAlteredStartTimeByAcqEnname(outAccountTask.getAcqEnname());
//			try {
//				startTime = DateUtil.subDayFormatLong(sdf.parse(endTime),7);
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
		}else{
			endTime = sdf.format(new Date());
			startTime = DateUtil.subDayFormatLong(new Date(),7);
		}
		log.info("startTime -- {},endTime -- {}",startTime,endTime);
		List<DuiAccountDetail> duiAccountDetailList = duiAccountDetailService.findAllTranByAcqNameAndDate(outAccountTask.getAcqEnname(), outAccountTask.getTransTime(), startTime, endTime);		

		//生成子出账单
		ExtAccountInfo extAccountInfo = null;
		MerchantInfo merchantInfo = null;
		List<MerchantCardInfo> merchantCardInfos = null;
		String currencyNo = "";
		String accountType = "M";
		String accountOwner = "";  //机构组织id
		BigDecimal judgeOutAccountTaskAmount = BigDecimal.ZERO; //预判断单笔出账金额
		Map<Integer, BigDecimal> tempData = new HashMap<Integer, BigDecimal>();  //用来临时存储出款通道的出账任务金额
		BigDecimal oddAmount = BigDecimal.ZERO;
		BigDecimal availableAmount = BigDecimal.ZERO;//可用余额
		String merchantNo = "";
		String subOutBillDetailId = "";
		boolean full = false;
		int i = 0 , j = 0 , k = 0 ;
		try {
			for (DuiAccountDetail duiAccountDetail : duiAccountDetailList) {
				merchantNo = duiAccountDetail.getPlateMerchantNo();
				extAccountInfo = extAccountService.findExtAccountInfoByParams(accountType, merchantNo, accountOwner, "", "224101001", currencyNo);
				if (extAccountInfo == null) {
					continue;
				}
				ExtAccount extAccount = extAccountService.getExtAccount(extAccountInfo.getAccountNo());
				if (extAccount == null) {
					continue;
				}
				merchantInfo = merchantInfoMapper.findMerchantInfoByUserIdRiskStatus(merchantNo);
				merchantCardInfos = merchantCardInfoMapper.getByMerchantNo(merchantNo);
				if (merchantInfo == null || merchantCardInfos == null || merchantCardInfos.isEmpty()) {
					continue;
				}
				// 出账任务金额为0，不加入出账单,出账任务金额 等于   交易金额  - 平台商户手续费
				judgeOutAccountTaskAmount = duiAccountDetail.getPlateTransAmount().subtract(duiAccountDetail.getPlateMerchantFee());
				if(judgeOutAccountTaskAmount.compareTo(BigDecimal.ZERO) == 0){
					continue;
				}

				BigDecimal settlingAmount = BigDecimal.ZERO;//可用余额
				settlingAmount = extAccount.getSettlingAmount()==null?BigDecimal.ZERO:extAccount.getSettlingAmount();
				//计算上游余额商户数量
				if (settlingAmount.compareTo(BigDecimal.ZERO) > 0) {
					if(!balanceMerchantCountList.contains(merchantNo)){
						balanceMerchantCountList.add(merchantNo);
					}
				}
				//可用余额
				availableAmount = (extAccount.getCurrBalance()==null?BigDecimal.ZERO:extAccount.getCurrBalance()
						.subtract(extAccount.getControlAmount()==null?BigDecimal.ZERO:extAccount.getControlAmount()))
						.subtract(extAccount.getSettlingAmount()==null?BigDecimal.ZERO:extAccount.getSettlingAmount()) ;

				if (full) {
					break;  //已经刚好凑完，则直接中断循环
				}
				for (OutAccountTaskDetail outAccountTaskDetail : outAccountTaskDetailList) {
					if (!tempData.containsKey(outAccountTaskDetail.getId())) {
						tempData.put(outAccountTaskDetail.getId(), outAccountTaskDetail.getOutAccountAmount());
					}
					oddAmount = tempData.get(outAccountTaskDetail.getId());
					if(oddAmount.compareTo(BigDecimal.ZERO) > 0){
					//商户可用余额必须大于0，并且满足当前出款通道的剩余出账任务金额
					if (availableAmount.compareTo(BigDecimal.ZERO) > 0 && !full) {
							SubOutBillDetail subOutBillDetail = new SubOutBillDetail();
							subOutBillDetailId = genericTableService.subOutBillDetailNo();
							subOutBillDetail.setId(subOutBillDetailId);
							subOutBillDetail.setMerchantNo(merchantNo);
							//subOutBillDetail.setMerchantBalance(duiAccountDetail.getPlateTransAmount());
							subOutBillDetail.setOrderReferenceNo(duiAccountDetail.getOrderReferenceNo());//订单参考号
							subOutBillDetail.setTransTime(duiAccountDetail.getPlateAcqTransTime());//交易时间
							subOutBillDetail.setTransAmount(duiAccountDetail.getPlateTransAmount());//交易金额
							// 出账任务金额等于   交易金额  - 平台商户手续费
							outAccountTaskAmount = duiAccountDetail.getPlateTransAmount().subtract(duiAccountDetail.getPlateMerchantFee());

							if (outAccountTaskAmount.compareTo(oddAmount) <= 0) {
								subOutBillDetail.setOutAccountTaskAmount(outAccountTaskAmount);
								//outAccountTaskAmount = oddAmount.subtract(outAccountTaskAmount);
								calcOutAmount = calcOutAmount.add(outAccountTaskAmount);
							} else {
								//subOutBillDetail.setOutAccountTaskAmount(oddAmount);
								//outAccountTaskAmount = outAccountTaskAmount.subtract(oddAmount);
								//calcOutAmount = calcOutAmount.add(oddAmount);
								full = true;
								break;
							}
							subOutBillDetail.setAcqOrgNo(outAccountTaskDetail.getAcqOrgNo());
							subOutBillDetail.setAcqEnname(outAccountTaskDetail.getAcqOrgNo());

							  if (AccountStatus.DESTROY.toString().equals(extAccount.getAccountStatus())) {
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
							  subOutBillDetail.setRecordStatus(OutBillRecordStatus.NORCORD.toString());
							  subOutBillDetail.setOutBillStatus(0);//未出账

							  if ("ZF_ZQ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo())
									  || "YS_ZQ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo())
									  || "SFT_ZQ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo())
									  || "HLB_KJ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo()) 
									  || "ZFYL_ZQ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo()) ){
								  Map<String,Object> zfMer = null;
								  if("HLB_KJ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo())){
									  zfMer = merchantInfoService.findMerchantInfoByBpmIdAndChannelCode(duiAccountDetail.getPlateMerchantEntryNo(),duiAccountDetail.getAcqEnname());
								  }else{
									  zfMer = merchantInfoService.queryQrMerchantInfo(duiAccountDetail.getPlateAcqMerchantNo());
								  }
								  if(zfMer != null){
									 // log.info("zfMer.get(sync_status) " + zfMer.get("sync_status"));
									  if(!"1".equals(zfMer.get("sync_status"))){
										  subOutBillDetail.setVerifyFlag("2");
										  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"未与上游同步状态");
									  }
								  }else{
									  subOutBillDetail.setVerifyFlag("2");
									  subOutBillDetail.setVerifyMsg("账户"+merchantNo+"不存在直清商户信息表");
								  }
							  }
                              
							  if("YS_ZQ".equalsIgnoreCase(outAccountTaskDetail.getAcqOrgNo())){
								  if(StringUtil.isBlank(duiAccountDetail.getPlateMerchantEntryNo())){
									  log.info(" ---- 订单号：" + duiAccountDetail.getOrderReferenceNo() + " 的进件编号为空，不能加入出账单，需要补齐数据 " );
									  continue;
								  }
							  }
							  //中付需求   新增平台商户进件编号
							  subOutBillDetail.setPlateMerchantEntryNo(duiAccountDetail.getPlateMerchantEntryNo());
							  //中付需求   收单机构商户号
							  subOutBillDetail.setAcqMerchantNo(duiAccountDetail.getAcqMerchantNo());

							  availableAmount = (extAccount.getCurrBalance()==null?BigDecimal.ZERO:extAccount.getCurrBalance()
										.subtract(extAccount.getControlAmount()==null?BigDecimal.ZERO:extAccount.getControlAmount()))
										.subtract(extAccount.getSettlingAmount()==null?BigDecimal.ZERO:extAccount.getSettlingAmount().add(outAccountTaskAmount)) ;
							  int returnNum = 0;
							  if (availableAmount.compareTo(BigDecimal.ZERO) >= 0) {
									// update 商户的结算中金额
									log.info("累加结算中金额："+outAccountTaskAmount +" ---- 商户号：" + merchantNo );
									returnNum = extAccountService.updateAddSettlingAmount(outAccountTaskAmount, merchantNo);
									if(returnNum > 0){
										subOutBillDetailList.add(subOutBillDetail);
										if (!acqOrgNoList.contains(outAccountTaskDetail.getAcqOrgNo())) {
											acqOrgNoList.add(outAccountTaskDetail.getAcqOrgNo());
										}
										oddAmount = oddAmount.subtract(outAccountTaskAmount); // 减去该笔交易的出账任务金额
										tempData.put(outAccountTaskDetail.getId(), oddAmount); // 存入临时一个map集合
									}
								}
						break;
						}
					}
				}
			}

			//if (subOutBillDetailList == null || subOutBillDetailList.isEmpty()) {
			//	return -2;
			//}


			Date now = new Date();
			AcqOutBill acqOutBill = new AcqOutBill();

			outBill.setCalcOutAmount(calcOutAmount);
			outBill.setBalanceUpCount(1);
			outBill.setBalanceMerchantCount(balanceMerchantCountList.size());
			outBill.setSysTime(now);
			outBill.setCreateTime(now);
			outBill.setCreator(userInfo.getUsername());

			OutBill outBillEntity = outBillService.findOutBillByTaskId(outAccountTaskId);
			if (outBillEntity != null) {
				log.info("生成出账单：out_bill中已存在"+outAccountTaskId+"的任务ID，执行出账单更新!");
				outBill.setId(outBillEntity.getId());
				outBill.setUpdator(userInfo.getUsername());
				i = outBillService.updateOutBillById(outBill);
			}
			else{
				log.info("生成出账单：创建出账单，出账任务ID："+outAccountTaskId);
				i = outBillService.insertOutBill(outBill);
				//出账单生成之后，需要将返回的id插入到出账任务表
				outAccountTask.setOutAccountId(outBill.getId());
				outAccountTask.setBillStatus(1);
				outAccountTask.setUpdator(userInfo.getUsername());
				outAccountTaskService.update(outAccountTask);
			}
			if (i > 0) {
				//subOutBillDetailMapper.deleteSubOutBillDetailByOutBillId(outBill.getId());

				for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
					subOutBillDetail.setOutBillId(outBill.getId());
					subOutBillDetail.setIsAddBill("1");
					subOutBillDetail.setCreateTime(outAccountTask.getTransTime());
				}
				for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
					j = j + subOutBillDetailMapper.insertSubOutBillDetail(subOutBillDetail);
					//交易加入出账单之后要去  对账信息详情里面  更新  该笔交易的  状态 即：是否加入出账单
					String isAddBill = "1";
					k = k + duiAccountDetailMapper.updateDuiAccountDetailByOrderReNum(subOutBillDetail, isAddBill);
				}
				i = i + j;
				BigDecimal calcOutAmount1 = BigDecimal.ZERO;
				BigDecimal upBalance = BigDecimal.ZERO;
				BigDecimal outAccountTaskAmount_ = BigDecimal.ZERO;
				int m = 0 , n = 0;
				acqOutBillService.deleteAcqOutBillByOutBillId(outBill.getId());  //先删除记录


				if(acqOrgNoList.size() ==0 && acqOrgNoList!=null){
					for (OutAccountTaskDetail outAccountTaskDetail : outAccountTaskDetailList) {
						if (!acqOrgNoList.contains(outAccountTaskDetail.getAcqOrgNo())) {
							acqOrgNoList.add(outAccountTaskDetail.getAcqOrgNo());
						}
					}
				}

				for (String acqOrgNo : acqOrgNoList) {
					calcOutAmount1 = BigDecimal.ZERO;
					upBalance = BigDecimal.ZERO;
					outAccountTaskAmount_ = BigDecimal.ZERO;
					for (SubOutBillDetail subOutBillDetail : subOutBillDetailList) {
						if (subOutBillDetail.getAcqOrgNo().equals(acqOrgNo)) {
							calcOutAmount1 = calcOutAmount1.add(subOutBillDetail.getOutAccountTaskAmount());
						}
					}
					for (OutAccountTaskDetail oatd : outAccountTaskDetailList) {
						if (oatd.getAcqOrgNo().equals(acqOrgNo)) {
							upBalance = upBalance.add(oatd.getUpBalance());
							outAccountTaskAmount_ = outAccountTaskAmount_.add(oatd.getOutAccountAmount());
						}
					}
					acqOutBill.setAcqOrgNo(acqOrgNo);
					acqOutBill.setCalcOutAmount(calcOutAmount1);
					acqOutBill.setOutBillId(outBill.getId());
					acqOutBill.setUpBalance(upBalance);
					acqOutBill.setCreateTime(now);
					acqOutBill.setOutAccountTaskAmount(outAccountTaskAmount_);
					n = acqOutBillService.insertAcqOutBill(acqOutBill);
					m = m +n;
				}
				i = i + m;
			}
		} catch (Exception e) {
			log.error("生成出账单异常 + " +e);
			//e.printStackTrace();
			throw new RuntimeException("生成出账单异常 + " +e);
		}
		log.info("生成出账单结束!");
		return i;
		
	}

	@Override
	public List<SettleTransfer> findSettleTransferList(SettleTransfer settleTransfer, Sort sort,
			Page<SettleTransfer> page) throws Exception {
		return chuAccountMapper.findSettleTransferList(settleTransfer, sort, page);
	}

	@Override
	public List<SettleTransferFile> findSubmitChuKuanChannelList(SettleTransferFile settleTransferFile,
			String createDate1, String createDate2, Sort sort, Page<SettleTransferFile> page) throws Exception {
		return chuAccountMapper.findSubmitChuKuanChannelList(settleTransferFile, createDate1, createDate2, sort, page);
	}


	
}
