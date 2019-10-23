package cn.eeepay.framework.service.bill.impl;

import cn.eeepay.framework.dao.bill.DuiAccountDetailMapper;
import cn.eeepay.framework.dao.nposp.TransInfoMapper;
import cn.eeepay.framework.db.pagination.Page;
import cn.eeepay.framework.db.pagination.Sort;
import cn.eeepay.framework.enums.DuiAccountStatus;
import cn.eeepay.framework.enums.TransStatus;
import cn.eeepay.framework.enums.TransType;
import cn.eeepay.framework.model.bill.*;
import cn.eeepay.framework.model.bill.SysDict;
import cn.eeepay.framework.model.nposp.*;
import cn.eeepay.framework.service.bill.*;
import cn.eeepay.framework.service.nposp.PosCardBinService;
import cn.eeepay.framework.service.nposp.RiskRollService;
import cn.eeepay.framework.service.nposp.TransInfoService;
import cn.eeepay.framework.util.FtpUtil;
import cn.eeepay.framework.util.HttpConnectUtil;

import com.auth0.jwt.JWTSigner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service("duiAccountDetailService")
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DuiAccountDetailServiceImpl implements DuiAccountDetailService {

	private static final Logger log = LoggerFactory.getLogger(DuiAccountDetailServiceImpl.class);

	@Resource
	public DuiAccountDetailMapper duiAccountDetailMapper;
	@Resource
	public SysDictService sysDictService;
	@Resource
	public DuiAccountBatchService duiAccountBatchService;
	@Resource
	public TransInfoService transInfoService;
	@Resource
	public ExtAccountService extAccountService;
	@Resource
	public DuiAccountAssemblyOrParsing duiAccountAssemblyOrParsing;

	@Resource
	public PosCardBinService posCardBinService;

	@Resource
	public DuiAccountDetailService duiAccountDetailService;
	
	@Resource
	private TransInfoMapper transInfoMapper;

	@Value("${accountApi.http.url}")
	private String accountApiHttpUrl;

	@Value("${accountApi.http.secret}")
	private String accountApiHttpSecret;

	@Autowired
	private RiskRollService riskRollService;

	@Override
	public int insertDuiAccountDetail(DuiAccountDetail dui) throws Exception {
		return duiAccountDetailMapper.insertDuiAccountDetail(dui);
	}

	@Override
	public int updateDuiAccountDetail(DuiAccountDetail dui) {
		return duiAccountDetailMapper.updateDuiAccountDetail(dui);
	}


	@Override
	public int deleteByCheckBatchNo(String checkBatchNo) {
		return duiAccountDetailMapper.deleteByCheckBatchNo(checkBatchNo);
	}

	@Override
	public int deleteDuiAccountDetail(Integer id) {
		return duiAccountDetailMapper.deleteDuiAccountDetail(id);
	}

	@Override
	public int deleteDuiAccountDetailByParams(String acqMerchantNo, String acqTerminalNo, String acqBatchNo,
			String acqSerialNo, String acqAccountNo) {
		return duiAccountDetailMapper.deleteDuiAccountDetailByParams(acqMerchantNo, acqTerminalNo, acqBatchNo,
				acqSerialNo, acqAccountNo);
	}

	@Override
	public List<DuiAccountDetail> findDuiAccountDetailListByStartEndTransTime(String startTime, String endTime) {
		return duiAccountDetailMapper.findDuiAccountDetailListByStartEndTransTime(startTime, endTime);
	}

	@Override
	public int saveDuiAccountDetail(DuiAccountDetail dui) throws Exception {
		String acqMerchantNo = dui.getAcqMerchantNo();
		String acqTerminalNo = dui.getAcqTerminalNo();
		String acqBatchNo = dui.getAcqBatchNo();
		String acqSerialNo = dui.getAcqSerialNo();
		String acqAccountNo = dui.getAcqAccountNo();
		if (dui.getSettleStatus() == null) {
			dui.setSettleStatus(0);
		}
		int i = 0, j = 0;
		i = duiAccountDetailMapper.deleteDuiAccountDetailByParams(acqMerchantNo, acqTerminalNo, acqBatchNo, acqSerialNo,
				acqAccountNo);
		dui.setCreateTime(new Date());
		j = duiAccountDetailMapper.insertDuiAccountDetail(dui);
		i = i + j;
		return i;
	}

	@Override
	public String doDuiAccount(String acqEnname, List<TransInfo> transInfos, Map<String, Object> map) throws Exception {
		// String mySettle
		// =DictCache.getDict("acq_channel",acqEnname).get("remark");
		SysDict sysDict = sysDictService.findSysDictByKeyName("acq_channel_my_settle", acqEnname);
		String mySettle = sysDict.getSysValue();
		DuiAccountBatch checkAccountBatch = new DuiAccountBatch();
		List<DuiAccountDetail> checkAccountDetails = (List<DuiAccountDetail>) map.get("checkAccountDetails");
		String fileName = (String) map.get("fileName");
		String checkFileDate = (String) map.get("checkFileDate");

		DuiAccountBatch batch = duiAccountBatchService.findDuiAccountBatchByFileNameAndAcqEnname(fileName, acqEnname);
		if (batch != null) {
			return "1";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		SimpleDateFormat shortSdf = new SimpleDateFormat("yyyyMMdd");
		int random = (int) (Math.random() * 1000);
		String checkBatchNo = acqEnname + shortSdf.format(new Date()) + random;
		checkAccountBatch.setCheckBatchNo(checkBatchNo);
		try {
			checkAccountBatch.setCheckFileDate(sdf.parse(checkFileDate));
		} catch (ParseException e) {
			log.error("异常:", e);
		}
		checkAccountBatch.setCheckFileName(fileName);
		checkAccountBatch.setCheckTime(new Date());
		checkAccountBatch.setOperator((String) map.get("uname"));

		// AcqOrg acqOrg = acqMerchantService.queryAcqOrgByEnname(acqEnname);
		SysDict acqOrg = sysDictService.findSysDictByKeyValue("sys_acq_org", acqEnname);
		checkAccountBatch.setAcqEnname(acqEnname);
		checkAccountBatch.setAcqCnname(acqOrg.getSysName());
		BigDecimal acqTotalAmount = new BigDecimal("0.00");
		BigDecimal totalAmount = new BigDecimal("0.00");
		for (int i = 0; i < checkAccountDetails.size(); i++) {
			acqTotalAmount = acqTotalAmount.add(checkAccountDetails.get(i).getAcqTransAmount());
		}

		checkAccountBatch.setAcqTotalAmount(acqTotalAmount);

		for (int i = 0; i < transInfos.size(); i++) {
			totalAmount = totalAmount.add(transInfos.get(i).getTransAmount());
		}
		checkAccountBatch.setTotalAmount(totalAmount);

		checkAccountBatch.setAcqTotalItems(Long.parseLong(String.valueOf(checkAccountDetails.size())));
		checkAccountBatch.setTotalItems(Long.parseLong(String.valueOf(transInfos.size())));

		int acqTotalSuccessItems = 0;
		int acqTotalFailedItems = 0;
		int totalSuccessItems = 0;
		int totalFailedItems = 0;
		int count = 0;
		log.info("对账逻辑开始:" + count++);
		for (int i = 0; i < checkAccountDetails.size(); i++) {
			DuiAccountDetail detail = checkAccountDetails.get(i);
			detail.setAcqEnname(acqEnname);
			detail.setCheckBatchNo(checkBatchNo);
			for (int j = 0; j < transInfos.size(); j++) {
				TransInfo info = transInfos.get(j);
				// 设置 从字典表获取mysettle的值
				info.setMySettle(Integer.valueOf(mySettle));
				boolean flag = false;
				if ("bill".equals(acqEnname) || "hypay".equals(acqEnname) || "halpay".equals(acqEnname)
						|| "qlhdpay".equals(acqEnname) || "newqlhdpay".equals(acqEnname) || "xdjkpay".equals(acqEnname)
						|| "xlink".equals(acqEnname)) {
					if (info.getAcqMerchantNo().equals(detail.getAcqMerchantNo())
							&& info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getAcqReferenceNo().equals(detail.getAcqReferenceNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())) {
						flag = true;
					}
				} else if ("zypay".equals(acqEnname)) {
					if (info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getAcqBatchNo().equals(detail.getAcqBatchNo())
							&& info.getAcqReferenceNo().equals(detail.getAcqReferenceNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())) {
						flag = true;
					}
				} else if ("ubs".equals(acqEnname) || "szchinamus".equals(acqEnname)) {
					if (info.getAcqMerchantNo().equals(detail.getAcqMerchantNo())
							&& info.getAcqReferenceNo().equals(detail.getAcqReferenceNo())
							&& info.getAccountNo().equals(detail.getAcqAccountNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())) {
						flag = true;
					}
				} else if ("xmcmbc".equals(acqEnname) || "xmcmbcys".equals(acqEnname)) {
					if (info.getAcqMerchantNo().equals(detail.getAcqMerchantNo())
							&& info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())
							&& info.getAcqReferenceNo().equals(detail.getAcqReferenceNo())) {
						flag = true;
					}
				} else if ("bypay".equals(acqEnname)) {
					if (info.getAcqMerchantNo().equals(detail.getAcqMerchantNo())
							&& info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getTransId().equals(detail.getAcqTransSerialNo())) {
						flag = true;
					}
				} else if ("zftpay".equals(acqEnname)) {
					if (info.getAcqAuthNo().equals(detail.getAcqTransSerialNo())
							&& info.getAcqMerchantNo().equals(detail.getAcqMerchantNo())
							&& info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())) {
						flag = true;
					}

				} else {
					if (info.getAcqTerminalNo().equals(detail.getAcqTerminalNo())
							&& info.getAccountNo().equals(detail.getAcqAccountNo())
							&& info.getAcqBatchNo().equals(detail.getAcqBatchNo())
							&& info.getAcqSerialNo().equals(detail.getAcqSerialNo())) {
						flag = true;
					}
				}
				if (flag) {
					installDetailByInfo(detail, info);
					BigDecimal transAmount = detail.getAcqTransAmount();
					if (info.getTransAmount().compareTo(transAmount) == 0) {
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalSuccessItems++;
						totalSuccessItems++;
						// checkAccountDetailService.updateAcclStatus(info.getId());//交易表增加对账状态20160217
						//transInfoService.updateTransInfoAccStatus(info.getId());
					} else {
						detail.setCheckAccountStatus(DuiAccountStatus.AMOUNT_FAILED.toString());
						checkAccountDetails.remove(i);
						transInfos.remove(j);
						acqTotalFailedItems++;
						totalFailedItems++;
					}
					detail.setErrorHandleStatus("pendingTreatment");
					this.saveDuiAccountDetail(detail);
					i--;
					j--;
					break;
				}
			}

			if (detail.getCheckAccountStatus() == null || detail.getCheckAccountStatus().equals("")) {

				String acqMerchantNo = detail.getAcqMerchantNo();
				String acqTerminalNo = detail.getAcqTerminalNo();
				String acqBatchNo = detail.getAcqBatchNo();
				String acqSerialNo = detail.getAcqSerialNo();
				String acqAccountNo = detail.getAcqAccountNo();
				String acqReferenceNo = detail.getAcqReferenceNo();
				BigDecimal transAmount = detail.getAcqTransAmount();

				TransInfo transInfo = new TransInfo();
				if ("bypay".equals(acqEnname)) {// 由于翰鑫不提供参考号，但提供商户订单号
					acqReferenceNo = detail.getAcqTransSerialNo();
				}
				if ("ubs".equals(acqEnname)) {
					// transInfo =
					// transService.queryByAcqTransInfoRyx(acqReferenceNo,
					// acqEnname, transAmount, acqMerchantNo, acqAccountNo);
					transInfo = transInfoService.findAcqTransInfoRyxByParams(acqReferenceNo, acqEnname, transAmount,
							acqMerchantNo, acqAccountNo);
				} else {
					// transInfo =
					// transService.queryByAcqTransInfo(acqMerchantNo,
					// acqTerminalNo, acqBatchNo, acqSerialNo,
					// acqAccountNo,acqEnname,acqReferenceNo);
					transInfo = transInfoService.findAcqTransInfoByParams(acqMerchantNo, acqTerminalNo, acqBatchNo,
							acqSerialNo, acqAccountNo, acqEnname, acqReferenceNo);
				}

				if (transInfo != null) {
					if (TransStatus.SUCCESS.toString().equals(transInfo.getTransStatus())) {
						detail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
						installDetailByInfo(detail, transInfo);
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems() + 1);
						totalSuccessItems++;
						acqTotalSuccessItems++;
						// checkAccountDetailService.updateAcclStatus(transInfo.getId());//交易表增加对账状态20160217
						//transInfoService.updateTransInfoAccStatus(transInfo.getId());
					} else {
						detail.setCheckAccountStatus(DuiAccountStatus.FAILED.toString());// 核对有误，上游单边，平台失败
						installDetailByInfo(detail, transInfo);
						checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems() + 1);
						totalFailedItems++;
						acqTotalFailedItems++;
					}

				} else {
					detail.setCheckAccountStatus(DuiAccountStatus.ACQ_SINGLE.toString());
					acqTotalFailedItems++;
				}

				detail.setCheckBatchNo(checkBatchNo);
				checkAccountDetails.remove(i);
				detail.setErrorHandleStatus("pendingTreatment");
				this.saveDuiAccountDetail(detail);
				i--;
			}

		}

		if (transInfos.size() >= 0) {
			for (int i = 0; i < transInfos.size(); i++) {
				TransInfo info = transInfos.get(i);
				info.setMySettle(Integer.valueOf(mySettle));
				if (judgeCheckEd(info)) {
					checkAccountBatch.setTotalItems(checkAccountBatch.getTotalItems() - 1);
					continue;
				}
				DuiAccountDetail detail = new DuiAccountDetail();
				installDetailByInfo(detail, info);
				detail.setAcqEnname(acqEnname);
				detail.setCheckBatchNo(checkBatchNo);
				detail.setCheckAccountStatus(DuiAccountStatus.PLATE_SINGLE.toString());
				detail.setErrorHandleStatus("pendingTreatment");
				this.saveDuiAccountDetail(detail);
				// System.out.println("平台单边ID"+detail.getId());
				/*
				 * //平台单边记存疑-----------开始(调用账户系统Api) Map<String,Object> msg =
				 * this.platformSingleMarkSuspect(detail ,info, null, null) ;
				 * if((boolean)msg.get("state") == false){ throw new
				 * RuntimeException(msg.get("msg").toString()) ; }
				 */
				// 平台单边记存疑-----------结束

				// 3、修改对账详细信息中的 差错处理状态 为'系统自动冻结'
				// detail.setErrorHandleStatus("checkForzen");
				totalFailedItems++;

			}
			totalFailedItems = totalFailedItems + transInfos.size();
		}

		checkAccountBatch.setAcqTotalSuccessItems(Long.parseLong(String.valueOf(acqTotalSuccessItems)));
		checkAccountBatch.setAcqTotalFailedItems(Long.parseLong(String.valueOf(acqTotalFailedItems)));
		checkAccountBatch.setTotalSuccessItems(Long.parseLong(String.valueOf(totalSuccessItems)));
		checkAccountBatch.setTotalFailedItems(Long.parseLong(String.valueOf(totalFailedItems)));

		if (acqTotalFailedItems > 0 || totalFailedItems > 0) {
			checkAccountBatch.setCheckResult(DuiAccountStatus.FAILED.toString());
		} else {
			checkAccountBatch.setCheckResult(DuiAccountStatus.SUCCESS.toString());
		}
		checkAccountBatch.setCreateTime(new Date());
		duiAccountBatchService.insertDuiAccountBatch(checkAccountBatch);

		/*
		 * log.info("对账完成,下一步调用记账程序"); //查询所有未成功的对账数据 List<DuiAccountDetail>
		 * duiacc =
		 * duiAccountDetailDao.findErrorDuiAccountDetailList(checkBatchNo); for
		 * (int i = 0; i < duiacc.size(); i++) { DuiAccountDetail errorDetail =
		 * duiacc.get(i); String acqMerchantNo = errorDetail.getAcqMerchantNo();
		 * String acqTerminalNo = errorDetail.getAcqTerminalNo(); String
		 * acqBatchNo = errorDetail.getAcqBatchNo(); String acqSerialNo =
		 * errorDetail.getAcqSerialNo(); String acqAccountNo =
		 * errorDetail.getAcqAccountNo(); String acqReferenceNo =
		 * errorDetail.getAcqReferenceNo(); BigDecimal transAmount =
		 * errorDetail.getAcqTransAmount();
		 * if("PLATE_SINGLE".equals(errorDetail.getCheckAccountStatus())){//
		 * 调用记账接口，平台单边记存疑 TransInfo transInfo = new TransInfo(); //transInfo =
		 * transInfoService.findAcqTransInfoByParams(acqMerchantNo,
		 * acqTerminalNo, acqBatchNo, acqSerialNo,
		 * acqAccountNo,acqEnname,acqReferenceNo); transInfo =
		 * transInfoService.findErrorTransInfoByDuiAccountDetail(errorDetail);//
		 * 对账的交易数据
		 *
		 * Map<String,Object> msg = this.platformSingleMarkSuspect(errorDetail
		 * ,transInfo, null, acqOrg.getOrderNo()) ; if((boolean)msg.get("state")
		 * == false){ log.info("平台单边记存疑记账失败原因："+msg.get("msg").toString()); //
		 * throw new RuntimeException(msg.get("msg").toString()) ; }
		 * //3、修改对账详细信息中的 差错处理状态 为'系统自动冻结'
		 * errorDetail.setErrorHandleStatus("checkForzen"); }else
		 * if("FAILED".equals(errorDetail.getCheckAccountStatus()) ||
		 * "ACQ_SINGLE".equals(errorDetail.getCheckAccountStatus())){//调用记账接口，
		 * 收单单边 TransInfo transInfo = new TransInfo(); transInfo =
		 * transInfoService.findTransInfoByDuiAccountDetail(errorDetail);
		 * Map<String,Object> msg = this.acqSingleMarkSuspect(errorDetail
		 * ,transInfo,acqOrg.getOrderNo()) ; if((boolean)msg.get("state") ==
		 * false){ // throw new RuntimeException(msg.get("msg").toString()) ;
		 * log.info("收单单边记账失败原因："+msg.get("msg").toString()); } //3、修改对账详细信息中的
		 * 差错处理状态 为'系统自动冻结' // errorDetail.setErrorHandleStatus("checkForzen");
		 * }
		 *
		 * }
		 */

		return "0";
	}

	/**
	 * 通过平台交易数据赋值对账明细
	 * 
	 */
	private void installDetailByInfo(DuiAccountDetail detail, TransInfo info) {
		String acqEnname = info.getAcqEnname();
		// String mySettle
		// =DictCache.getDict("acq_channel",acqEnname).get("remark");

		detail.setPlateAcqMerchantNo(info.getAcqMerchantNo());
		detail.setPlateAcqTerminalNo(info.getAcqTerminalNo());
		detail.setPlateAgentNo(info.getAgentNo());
		detail.setPlateMerchantNo(info.getMerchantNo());
		detail.setPlateTerminalNo(info.getTerminalNo());
		detail.setPlateAcqBatchNo(info.getAcqBatchNo());
		detail.setPlateAcqSerialNo(info.getAcqSerialNo());
		detail.setPlateBatchNo(info.getBatchNo());
		detail.setPlateSerialNo(info.getSerialNo());
		detail.setPlateAccountNo(info.getAccountNo());
		if (TransType.PURCHASE_REFUND.equals(info.getTransType())
				|| TransType.PURCHASE_VOID.equals(info.getTransType())) {
			detail.setPlateTransAmount(
					new BigDecimal("-1").multiply(info.getTransAmount()).setScale(2, RoundingMode.HALF_UP));
		} else {
			detail.setPlateTransAmount(info.getTransAmount());
		}
		detail.setPlateAcqReferenceNo(info.getAcqReferenceNo());
		detail.setPlateAcqTransTime(info.getTransTime());
		detail.setPlateTransType(info.getTransType().toString());
		detail.setPlateTransStatus(info.getTransStatus().toString());
		detail.setPlateAcqMerchantFee(info.getAcqMerchantFee());
		detail.setPlateMerchantFee(info.getMerchantFee());
		detail.setPlateAcqMerchantRate(info.getAcqMerchantRate());
		detail.setPlateMerchantRate(info.getMerchantRate());
		detail.setPlateTransSource(info.getTransSource().toString());// 增加交易来源
		detail.setPlateAgentNo(info.getAgentNo());// 增加代理商编号

		detail.setBagSettle(info.getBagSettle());
		detail.setPosType(info.getPosType());// 增加设备类型2015/12/15
		detail.setPlateTransId(info.getId());
		detail.setPlateAgentShareAmount(info.getSingleShareAmount());// 代理商分润金额
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			if (info.getMerchantSettleDate() != null) {
				String merchantSettleDate = sdf.format(info.getMerchantSettleDate());
				detail.setPlateMerchantSettleDate(sdf.parse(merchantSettleDate));
			}
			SysDict sysDict = sysDictService.findSysDictByKeyName("acq_channel_my_settle", acqEnname);
			String mySettle = sysDict.getSysValue();
			detail.setMySettle(mySettle);

		} catch (ParseException e) {
			log.error("异常:", e);
		} catch (Exception e) {
			log.error("异常:", e);
		}
	}

	/**
	 * 判断该交易是否已经对账（避免由于时间差异造成的单笔记录）
	 * 
	 */
	private boolean judgeCheckEd(TransInfo transInfo) {
		DuiAccountDetail detail = null;
		try {
			detail = this.findDuiAccountDetailByTransInfo(transInfo);
		} catch (Exception e) {
			log.error("异常:", e);
		}
		if (detail == null) {
			return false;
		}
		return true;
	}

	@Override
	public DuiAccountDetail findDuiAccountDetailByTransInfo(TransInfo transInfo) throws Exception {
		return duiAccountDetailMapper.findDuiAccountDetailByTransInfo(transInfo);
	}

	@Override
	public List<DuiAccountDetail> getYinShengCheckDetailTransInfos(String acqEnname, Date transDate) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transDate);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		Date date = calendar.getTime();
		
		String transTimeBegin = sdf.format(date) + " 23:00:00";
		String transTimeEnd = sdf.format(transDate) + " 22:59:59";
	    //String jhTimeStart = sdf.format(transDate) + " 22:00:00";
		//String jhTimeEnd = sdf.format(transDate) + " 22:59:59";
		String begin = sdf.format(transDate) + " 00:00:00";
		String end = sdf.format(transDate) + " 23:59:59";
		if ("xmcmbcys".equals(acqEnname)) {
			String xmcmbcysBegin = sdf.format(date) + " 23:00:00";
			String xmcmbcysEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, xmcmbcysBegin, xmcmbcysEnd);
		} else if ("bill".equals(acqEnname)) {
			String billBegin = sdf.format(date) + " 00:00:00";
			String billEnd = sdf.format(date) + " 23:59:59";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, billBegin, billEnd);
		} else if ("zftpay".equals(acqEnname)) {
			String zftBegin = sdf.format(date) + " 00:00:00";
			String zftEnd = sdf.format(date) + " 23:59:59";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, zftBegin, zftEnd);
		} else if ("qlhdpay".equals(acqEnname) || "newqlhdpay".equals(acqEnname)) {
			String qlBegin = sdf.format(date) + " 23:00:00";
			String qlEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, qlBegin, qlEnd);
		} else if ("zypay".equals(acqEnname) || "bypay".equals(acqEnname) || "xlink".equals(acqEnname)) {
			String zyBegin = sdf.format(transDate) + " 00:00:00";
			String zyEnd = sdf.format(transDate) + " 23:59:59";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, zyBegin, zyEnd);
		} else if ("hypay".equals(acqEnname)) {
			String zyBegin = sdf.format(date) + " 22:58:00";
			String zyEnd = sdf.format(transDate) + " 22:58:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, zyBegin, zyEnd);
		} else if ("xdjkpay".equals(acqEnname)) {// 现代金控
			String xdjkBegin = sdf.format(date) + " 23:00:00";
			String xdjkEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, xdjkBegin, xdjkEnd);
		} else if ("szchinamus".equals(acqEnname) || "szcmbc".equals(acqEnname)) {
			String szchinamusBegin = sdf.format(date) + " 23:00:00";
			String szchinamusEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, szchinamusBegin, szchinamusEnd);
		} else if ("xmcmbc".equals(acqEnname)) {// 厦门民生
			String xmBegin = sdf.format(date) + " 23:00:00";
			String xmEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, xmBegin, xmEnd);
		} else if ("ubs".equals(acqEnname)) {
			String zyBegin = sdf.format(date) + " 23:00:00";
			String zyEnd = sdf.format(transDate) + " 23:00:00";
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, zyBegin, zyEnd);
		} else if ("eptok".equals(acqEnname) || "neweptok".equals(acqEnname)|| "YS_ZQ".equals(acqEnname)) {
			List<DuiAccountDetail> list = duiAccountDetailService.getCheckDetailTransInfos(acqEnname, transTimeBegin, transTimeEnd);
			/*
			  List<DuiAccountDetail> jhList = duiAccountDetailService.getCheckDetailTransInfos(acqEnname, jhTimeStart, jhTimeEnd); 
			  if(jhList.size()>0){ 
			    for (int i = 0; i <jhList.size(); i++) {
			        DuiAccountDetail t =jhList.get(i); 
			        String accountNo = t.getPlateAccountNo(); 
			        log.info("accountNo --- > " +accountNo); 
			      	PosCardBin cardBin = posCardBinService.findPosCardBinByCardNo(accountNo);
			 		if(!cardBin.getBankName().contains("建设银行")){ 
			     		list.add(t); 
			     	}
			  	} 
			  }
			 */
			return list;
		} else {
			return duiAccountDetailService.getCheckDetailTransInfos(acqEnname, begin, end);
		}
	}

	@Override
	public List<TransInfo> findDuiTransInfos(String acqEnname, Date transDate) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(transDate);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day - 1);
		Date date = calendar.getTime();

		String transTimeBegin = sdf.format(date) + " 23:00:00";
		String transTimeEnd = sdf.format(transDate) + " 21:59:59";
		String jhTimeStart = sdf.format(transDate) + " 22:00:00";
		String jhTimeEnd = sdf.format(transDate) + " 22:59:59";
		String begin = sdf.format(transDate) + " 00:00:00";
		String end = sdf.format(transDate) + " 23:59:59";

		if ("xmcmbcys".equals(acqEnname)) {
			String xmcmbcysBegin = sdf.format(date) + " 23:00:00";
			String xmcmbcysEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, xmcmbcysBegin, xmcmbcysEnd);
		} else if ("bill".equals(acqEnname)) {
			String billBegin = sdf.format(date) + " 00:00:00";
			String billEnd = sdf.format(date) + " 23:59:59";
			return transInfoService.findCheckData(acqEnname, billBegin, billEnd);
		} else if ("zftpay".equals(acqEnname)) {
			String zftBegin = sdf.format(date) + " 00:00:00";
			String zftEnd = sdf.format(date) + " 23:59:59";
			return transInfoService.findCheckData(acqEnname, zftBegin, zftEnd);
		} else if ("qlhdpay".equals(acqEnname) || "newqlhdpay".equals(acqEnname)) {
			String qlBegin = sdf.format(date) + " 23:00:00";
			String qlEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, qlBegin, qlEnd);
		} else if ("zypay".equals(acqEnname) || "bypay".equals(acqEnname) || "xlink".equals(acqEnname)) {
			String zyBegin = sdf.format(transDate) + " 00:00:00";
			String zyEnd = sdf.format(transDate) + " 23:59:59";
			return transInfoService.findCheckData(acqEnname, zyBegin, zyEnd);
		} else if ("hypay".equals(acqEnname)) {
			String zyBegin = sdf.format(date) + " 22:58:00";
			String zyEnd = sdf.format(transDate) + " 22:58:00";
			return transInfoService.findCheckData(acqEnname, zyBegin, zyEnd);
		} else if ("xdjkpay".equals(acqEnname)) {// 现代金控
			String xdjkBegin = sdf.format(date) + " 23:00:00";
			String xdjkEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, xdjkBegin, xdjkEnd);
		} else if ("szchinamus".equals(acqEnname) || "szcmbc".equals(acqEnname)) {
			String szchinamusBegin = sdf.format(date) + " 23:00:00";
			String szchinamusEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, szchinamusBegin, szchinamusEnd);
		} else if ("xmcmbc".equals(acqEnname)) {// 厦门民生
			String xmBegin = sdf.format(date) + " 23:00:00";
			String xmEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, xmBegin, xmEnd);
		} else if ("ubs".equals(acqEnname)) {
			String zyBegin = sdf.format(date) + " 23:00:00";
			String zyEnd = sdf.format(transDate) + " 23:00:00";
			return transInfoService.findCheckData(acqEnname, zyBegin, zyEnd);
		} else if ("eptok".equals(acqEnname) || "neweptok".equals(acqEnname) || "YS_ZQ".equals(acqEnname)) {
			List<TransInfo> list = transInfoService.findCheckData(acqEnname, transTimeBegin, transTimeEnd);
			List<TransInfo> jhList = transInfoService.findCheckData(acqEnname, jhTimeStart, jhTimeEnd);
			if (jhList.size() > 0) {
				for (int i = 0; i < jhList.size(); i++) {
					TransInfo t = jhList.get(i);
					String accountNo = t.getAccountNo();
					PosCardBin cardBin = posCardBinService.findPosCardBinByCardNo(accountNo);
					if (!cardBin.getBankName().contains("建设银行")) {
						list.add(t);
					}
				}
			}
			return list;
		} else {
			return transInfoService.findCheckData(acqEnname, begin, end);
		}
	}

	@Override
	public List<DuiAccountDetail> findDuiAccountDetailList(String createTimeStart, String createTimeEnd,
			DuiAccountDetail duiAccountDetail, Sort sort, Page<DuiAccountDetail> page) throws Exception {
		if (StringUtils.isNotBlank(createTimeStart)) {
			createTimeStart = createTimeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			createTimeEnd = createTimeEnd + " 23:59:59";
		}
		//优化sql 
		//(IFNULL(d.plate_trans_amount,0)-IFNULL(d.plate_merchant_fee,0))as taskAmount,
		//(IFNULL(d.plate_merchant_fee,0)+IFNULL(d.deduction_fee,0))as merFee2
		List<DuiAccountDetail> list = duiAccountDetailMapper.findDuiAccountDetailList1(createTimeStart, createTimeEnd, duiAccountDetail, sort,
				page);
		BigDecimal taskAmount = BigDecimal.ZERO, merFee2 = BigDecimal.ZERO ;
        for (DuiAccountDetail duiAccountDetail2 : list) {
            if("zfyl_zq".toUpperCase().equals(duiAccountDetail2.getAcqEnname().toUpperCase())){     //如果是中付银联直清，则去查是否黑名单
                String plateMerchantNo = duiAccountDetail2.getPlateMerchantNo();
                RiskRoll merchantBlackByMerchantNo = riskRollService.findMerchantBlackByMerchantNo(plateMerchantNo);
                if(merchantBlackByMerchantNo != null){
                    duiAccountDetail2.setMerchantBlack("(黑名单冻结)");
                }
            }
			taskAmount = judgeAmountIsNull(duiAccountDetail2.getPlateTransAmount(),duiAccountDetail2.getPlateMerchantFee(),true);
			//最终 + 抵扣
			merFee2 = judgeAmountIsNull(duiAccountDetail2.getPlateMerchantFee(), duiAccountDetail2.getDeductionFee(),false);

			merFee2 = judgeAmountIsNull(merFee2,duiAccountDetail2.getMerchantPrice(),true);

			merFee2 = judgeAmountIsNull(merFee2,duiAccountDetail2.getDeductionMerFee(),false);

			//原始 = 最终 - 自选商户手续费+抵扣自选商户手续费

			duiAccountDetail2.setActualFee(merFee2);		//原始交易手续费，已经不维护的字段没有意义
			duiAccountDetail2.setTaskAmount(taskAmount);
			duiAccountDetail2.setMerFee2(merFee2.toString());

			duiAccountDetail2.setMerchantPrice(duiAccountDetail2.getMerchantPrice()==null?BigDecimal.ZERO:duiAccountDetail2.getMerchantPrice());
			duiAccountDetail2.setDeductionMerFee(duiAccountDetail2.getDeductionMerFee()==null?BigDecimal.ZERO:duiAccountDetail2.getDeductionMerFee());


			duiAccountDetail2.setActualMerFee(duiAccountDetail2.getMerchantPrice().subtract(duiAccountDetail2.getDeductionMerFee()));		//实际自选商户手费=自选商户手续费-抵扣自选商户手续费
		}
		return list;
	}
	
	@Override
	public List<DuiAccountDetail> findExportDuiAccountDetailList(String createTimeStart, String createTimeEnd,
			DuiAccountDetail duiAccountDetail) throws Exception {
		if (StringUtils.isNotBlank(createTimeStart)) {
			createTimeStart = createTimeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			createTimeEnd = createTimeEnd + " 23:59:59";
		}
		//优化sql
		List<DuiAccountDetail> list = duiAccountDetailMapper.findExportDuiAccountDetailList(createTimeStart, createTimeEnd, duiAccountDetail);
		BigDecimal taskAmount = BigDecimal.ZERO ,merFee2 = BigDecimal.ZERO ;
		for (DuiAccountDetail duiAccountDetail2 : list) {
			taskAmount = judgeAmountIsNull(duiAccountDetail2.getPlateTransAmount(),duiAccountDetail2.getPlateMerchantFee(),true);

			//最终 + 抵扣
			merFee2 = judgeAmountIsNull(duiAccountDetail2.getPlateMerchantFee(), duiAccountDetail2.getDeductionFee(),false);

			merFee2 = judgeAmountIsNull(merFee2,duiAccountDetail2.getMerchantPrice(),true);

			merFee2 = judgeAmountIsNull(merFee2,duiAccountDetail2.getDeductionMerFee(),false);

			//原始 = 最终 - 自选商户手续费+抵扣自选商户手续费

			duiAccountDetail2.setActualFee(merFee2);		//原始交易手续费，已经不维护的字段没有意义
			duiAccountDetail2.setTaskAmount(taskAmount);
			duiAccountDetail2.setMerFee2(merFee2.toString());

			duiAccountDetail2.setMerchantPrice(duiAccountDetail2.getMerchantPrice()==null?BigDecimal.ZERO:duiAccountDetail2.getMerchantPrice());
			duiAccountDetail2.setDeductionMerFee(duiAccountDetail2.getDeductionMerFee()==null?BigDecimal.ZERO:duiAccountDetail2.getDeductionMerFee());

			duiAccountDetail2.setActualMerFee(duiAccountDetail2.getMerchantPrice().subtract(duiAccountDetail2.getDeductionMerFee()));		//实际自选商户手费=自选商户手续费-抵扣自选商户手续费
		}
		return list;
	}

	private BigDecimal judgeAmountIsNull(BigDecimal amount1, BigDecimal amount2,boolean flag) {
		BigDecimal amount = BigDecimal.ZERO;
		if(amount1 == null) amount1 = BigDecimal.ZERO;
		if(amount2 == null) amount2 = BigDecimal.ZERO;
		if(flag){
			amount = amount1.subtract(amount2);
		}else{
			amount = amount1.add(amount2);
		}
		return amount;
	}

	@Override
	public List<DuiAccountDetail> findErrorDuiAccountDetailList(String createTimeStart, String createTimeEnd,
			DuiAccountDetail duiAccountDetail, Sort sort, Page<DuiAccountDetail> page) throws Exception {
		if (StringUtils.isNotBlank(createTimeStart)) {
			createTimeStart = createTimeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			createTimeEnd = createTimeEnd + " 23:59:59";
		}
		//平台交易日
		String plateAcqTransTime1 = duiAccountDetail.getPlateAcqTransTime1();
		String plateAcqTransTime2 = duiAccountDetail.getPlateAcqTransTime2();
		if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime1())) {
			duiAccountDetail.setPlateAcqTransTime1(plateAcqTransTime1 + " 00:00:00");
		}
		if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime2())) {
			duiAccountDetail.setPlateAcqTransTime2(plateAcqTransTime2 + " 23:59:59");
		}

		//机构交易日
		String acqTransTime1 = duiAccountDetail.getAcqTransTime1();
		String acqTransTime2 = duiAccountDetail.getAcqTransTime2();
		if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime1())) {
			duiAccountDetail.setAcqTransTime1(acqTransTime1 + " 00:00:00");
		}
		if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime2())) {
			duiAccountDetail.setAcqTransTime2(acqTransTime2 + " 23:59:59");
		}

		String acqReferenceNo = duiAccountDetail.getAcqReferenceNo();
		if(StringUtils.isNotBlank(acqReferenceNo)){
			String[] split = acqReferenceNo.split(",");
			List<String> strings = Arrays.asList(split);
			List<String> collect = strings.stream().map(T -> "'" + T + "'").collect(Collectors.toList());
			duiAccountDetail.setAcqReferenceNo(String.join(",",collect));
		}

		//优化sql
		List<DuiAccountDetail> list = duiAccountDetailMapper.findErrorDuiAccountDetailList1(createTimeStart, createTimeEnd, duiAccountDetail,
						sort, page);
		BigDecimal taskAmount = BigDecimal.ZERO;
		for (DuiAccountDetail duiAccountDetail2 : list) {
			taskAmount = judgeAmountIsNull(duiAccountDetail2.getPlateTransAmount(),duiAccountDetail2.getPlateMerchantFee(),true);
			duiAccountDetail2.setTaskAmount(taskAmount);
		}
		return list;
	}

	@Override
	public List<DuiAccountDetail> findErrorExportDuiAccountDetailList(String createTimeStart, String createTimeEnd,
																 DuiAccountDetail duiAccountDetail) throws Exception {
		if (StringUtils.isNotBlank(createTimeStart)) {
			createTimeStart = createTimeStart + " 00:00:00";
		}
		if (StringUtils.isNotBlank(createTimeEnd)) {
			createTimeEnd = createTimeEnd + " 23:59:59";
		}
		//平台交易日
		String plateAcqTransTime1 = duiAccountDetail.getPlateAcqTransTime1();
		String plateAcqTransTime2 = duiAccountDetail.getPlateAcqTransTime2();
		if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime1())) {
			duiAccountDetail.setPlateAcqTransTime1(plateAcqTransTime1 + " 00:00:00");
		}
		if (StringUtils.isNotBlank(duiAccountDetail.getPlateAcqTransTime2())) {
			duiAccountDetail.setPlateAcqTransTime2(plateAcqTransTime2 + " 23:59:59");
		}

		//机构交易日
		String acqTransTime1 = duiAccountDetail.getAcqTransTime1();
		String acqTransTime2 = duiAccountDetail.getAcqTransTime2();
		if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime1())) {
			duiAccountDetail.setAcqTransTime1(acqTransTime1 + " 00:00:00");
		}
		if (StringUtils.isNotBlank(duiAccountDetail.getAcqTransTime2())) {
			duiAccountDetail.setAcqTransTime2(acqTransTime2 + " 23:59:59");
		}
		String acqReferenceNo = duiAccountDetail.getAcqReferenceNo();
		if(StringUtils.isNotBlank(acqReferenceNo)){
            acqReferenceNo = URLDecoder.decode(acqReferenceNo,"UTF-8");
			String[] split = acqReferenceNo.split(",");
			List<String> strings = Arrays.asList(split);
			List<String> collect = strings.stream().map(T -> "'" + T + "'").collect(Collectors.toList());
			duiAccountDetail.setAcqReferenceNo(String.join(",",collect));
		}

		//优化sql
		List<DuiAccountDetail> list = duiAccountDetailMapper.findErrorExportDuiAccountDetailList(createTimeStart, createTimeEnd, duiAccountDetail);
		BigDecimal taskAmount = BigDecimal.ZERO;
		for (DuiAccountDetail duiAccountDetail2 : list) {
			taskAmount = judgeAmountIsNull(duiAccountDetail2.getPlateTransAmount(),duiAccountDetail2.getPlateMerchantFee(),true);
			duiAccountDetail2.setTaskAmount(taskAmount);
		}
		return list;
	}
	@Override
	public DuiAccountDetail findDuiAccountDetailById(String id) throws Exception {
		return duiAccountDetailMapper.findDuiAccountDetailById(id);
	}

	/**
	 * 平台单边记存疑 11 (系统自动冻结调)√
	 */
	@Override
	public Map<String, Object> platformSingleMarkSuspect(DuiAccountDetail duiAccountDetail, TransInfo transInfo,
			MerchantInfo merInfo, String acqOrgId) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.8平台单边记存疑
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (transInfo == null) {
			msg.put("state", false);
			msg.put("msg", "找不到交易源！");
			return msg;
		}
		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", sdf.format(transInfo.getTransTime()));
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount",
					transInfo.getTransAmount() == null ? null : transInfo.getTransAmount().toString());// 交易金额
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			/*
			 * claims.put("directAgentNo", merInfo.getAgentNo());
			 * claims.put("oneAgentNo", merInfo.getOneAgentNo());
			 */
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("transTypeCode", "000008");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("exp", exp);
			claims.put("iat", iat);
			claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/platformSingleMarkSuspect.do";
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边记存疑返回结果：" + response);

			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				msg.put("state", false);
				msg.put("msg", "平台单边记存疑返回为空！");
				return msg;
			} else {
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if ((boolean) resp.get("status") == false) {
					// 平台单边确认是日切
					msg.put("state", false);
					msg.put("msg", "平台单边记存疑失败，reason:" + resp.get("msg").toString());
					return msg;
				}
			}
			/*
			 * //3、修改对账详细信息中的 差错处理状态 为'系统自动冻结'
			 * duiAccountDetail.setErrorHandleStatus("checkForzen");
			 * duiAccountDetailDao.updateDuiAccountDetail(duiAccountDetail) ;
			 */
			msg.put("state", true);
			msg.put("msg", "平台单边记存疑成功！");
		} catch (Exception e) {
			log.info("平台单边赔付商户异常" + e.getMessage());
			msg.put("state", false);
			msg.put("msg", "平台单边记存疑异常！");
			log.error("异常:", e);
		}
		return msg;
	}

	/**
	 * 上游单边记存疑问(上传对账文件时，系统自动对账，自动调用)
	 */
	@Override
	public Map<String, Object> acqSingleMarkSuspect(DuiAccountDetail duiAccountDetail, TransInfo transInfo,
			String acqOrgId) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.8上游单边记存疑
		final String secret = accountApiHttpSecret;

		final long iat = System.currentTimeMillis() / 1000l; // issued at claim
		final long exp = iat + 180L; // expires claim. In this case the token
										// expires in 60 seconds
		final String jti = UUID.randomUUID().toString();
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		if (transInfo == null) {
			msg.put("state", false);
			msg.put("msg", "无交易来源！");
			return msg;
		}
		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", transInfo.getTransTime() == null ? null : sdf.format(transInfo.getCreateTime()));// 交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ? new BigDecimal("0.00").toString()
					: transInfo.getTransAmount().toString());// 交易金额
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			// claims.put("directAgentNo", "248");
			// claims.put("oneAgentNo", "248");
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("transTypeCode", "000004");
			claims.put("acqOrgId", acqOrgId);
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("exp", exp);
			claims.put("iat", iat);
			claims.put("jti", jti);

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/acqSingleMarkSuspect.do";
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边记存疑问返回结果：" + response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response) || (boolean) resp.get("status") == false) {
				// 上游单边，结算给商户失败
				msg.put("state", false);
				msg.put("msg", "上游单边记存疑问失败,reason:" + resp.get("msg").toString());
				return msg;
			}
			/*
			 * //3、修改对账详细信息中的 差错处理状态 为'上游单边记存疑问'
			 * duiAccountDetail.setErrorHandleStatus("upstreamDoubt");
			 * duiAccountDetailDao.updateDuiAccount(duiAccountDetail) ;
			 */
			msg.put("state", true);
			msg.put("msg", "上游单边记存疑问成功！");
		} catch (Exception e) {
			log.info("上游单边记存疑问异常");
			msg.put("state", false);
			msg.put("msg", "上游单边记存疑问异常！");
			log.error("异常:", e);
		}
		return msg;
	}

	/**
	 * 平台单边确认是日切(解冻调)【平台单边正常解冻结算】
	 */
	@Override
	public Map<String, Object> platformSingleForDayCut(DuiAccountDetail duiAccountDetail,
			CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.13平台单边确认是日切
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (transInfo == null) {
			String errorMsg = "找不到交易源！";
			msg.put("state", false);
			msg.put("msg", errorMsg);
			duiAccountDetail.setErrorMsg(errorMsg);
			duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
			return msg;
		}

		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", transInfo.getTransTime() == null ? null : sdf.format(transInfo.getTransTime()));
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", transInfo.getTransAmount() == null ? new BigDecimal("0.00").toString()
					: transInfo.getTransAmount().toString());
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("merchantFee", transInfo.getMerchantFee().toString()); // 商户费率
			claims.put("directAgentNo", merInfo.getAgentNo());
			claims.put("oneAgentNo", merInfo.getOneAgentNo()); // 一级代理商
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString()); // 收单服务
			claims.put("transTypeCode", "000009");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", transInfo.getOrderNo().toString());
			claims.put("cardType", transInfo.getCardType());
			claims.put("agentShareAmount",
					transInfo.getProfits1() == null ? BigDecimal.ZERO.toString() : transInfo.getProfits1().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/platformSingleForDayCut.do";
			log.info("平台单边确认是日切【平台单边正常解冻结算】url：" + url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边确认是日切【平台单边正常解冻结算】返回结果：" + response);

			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				String errorMsg = "平台单边确认是日切【平台单边正常解冻结算】 返回为空";
				msg.put("state", false);
				msg.put("msg", errorMsg);
				// duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			} else {
				ObjectMapper om = new ObjectMapper();
				Map<String, Object> resp = om.readValue(response, Map.class);
				if ((boolean) resp.get("status") == false) {
					// 平台单边确认是日切
					String errMsg = "";
					if (resp.get("msg") == null || resp.get("msg") == "") {
						errMsg = "平台单边确认是日切【平台单边正常解冻结算】，message:返回为空";
					} else {
						errMsg = "平台单边确认是日切【平台单边正常解冻结算】记账失败，reason：" + resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state", false);
					msg.put("msg", errMsg);

					// duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				} else {
					// 3、修改对账详细信息中的 差错处理状态 为'解冻结算'
//					duiAccountDetail.setErrorHandleStatus("thawSettle");
					duiAccountDetail.setErrorMsg("记账成功");

					msg.put("state", true);
					msg.put("msg", "平台单边确认是日切【平台单边正常解冻结算】成功！");
				}
			}

		} catch (Exception e) {
			log.info("平台单边确认是日切【平台单边正常解冻结算】异常" + e.getMessage());
			String errMsg = e.getMessage();
			errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
			// duiAccountDetail.setErrorHandleStatus("accountFailed");
			duiAccountDetail.setErrorMsg(errMsg);

			msg.put("state", false);
			msg.put("msg", "平台单边确认是日切【平台单边正常解冻结算】异常！");
			log.error("异常:", e);
		}
//		duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		return msg;
	}

	/**
	 * 平台单边赔付商户 (平台单边赔付商户 )调【交易解冻】
	 */
	@Override
	public Map<String, Object> platformSingleSettleToMerchant(DuiAccountDetail duiAccountDetail,
			CollectiveTransOrder transInfo, MerchantInfo merInfo, AcqOrg acqOrg) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.13平台单边赔付商户
		final String secret = accountApiHttpSecret;

		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (transInfo == null) {
			String errorMsg = "找不到交易源！";
			msg.put("state", false);
			msg.put("msg", errorMsg);
			duiAccountDetail.setErrorMsg(errorMsg);
//			duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
			return msg;
		}
		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate", sdf.format(transInfo.getTransTime()));
			claims.put("transAmount", transInfo.getTransAmount() == null ? new BigDecimal("0.00").toString()
					: transInfo.getTransAmount().toString());
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transOrderNo", transInfo.getId().toString());
			claims.put("oneAgentNo", merInfo.getOneAgentNo());
			claims.put("directAgentNo", merInfo.getAgentNo());
			claims.put("merchantNo", transInfo.getMerchantNo());
			claims.put("transTypeCode", "000014"); // 交易解冻
			claims.put("serviceId", transInfo.getServiceId().toString());
			claims.put("acqServiceId", transInfo.getAcqServiceId().toString());
			claims.put("cardNo", transInfo.getAccountNo());
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("acqEnname", transInfo.getAcqEnname());
			claims.put("agentShareAmount",
					transInfo.getProfits1() == null ? BigDecimal.ZERO.toString() : transInfo.getProfits1().toString());// 单笔分润金额
			claims.put("merchantFee", transInfo.getMerchantFee().toString());
			claims.put("cardType", transInfo.getCardType());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/transFreeze.do";
			log.info("平台单边赔付商户【交易解冻】url：" + url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("平台单边赔付商户【交易解冻】返回结果：" + response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				String errorMsg = "平台单边赔付商户【交易解冻】 返回为空";
				msg.put("state", false);
				msg.put("msg", errorMsg);
				// duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			} else {
				if ((boolean) resp.get("status") == false) {
					String errMsg = "";
					if (resp.get("msg") == null || resp.get("msg") == "") {
						errMsg += "平台单边赔付商户【交易解冻】;message:返回为空";
					} else {
						errMsg = "平台单边赔付商户【交易解冻】记账失败,reason:" + resp.get("msg").toString();
						errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					}
					msg.put("state", false);
					msg.put("msg", errMsg);

					// duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				} else {
					// 3、修改对账详细信息中的 差错处理状态 为'平台单边赔付商户'
//					duiAccountDetail.setErrorHandleStatus("platformPayment");
					duiAccountDetail.setErrorMsg("记账成功");

					msg.put("state", true);
					msg.put("msg", "平台单边赔付商户【交易解冻】修改成功！");
				}
			}

		} catch (Exception e) {
			String errMsg = "平台单边赔付商户【交易解冻】异常";
			// duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state", false);
			msg.put("msg", errMsg);
			log.error("异常:", e);

		}
//		duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		return msg;
	}

	/**
	 * 上游单边补记账结算商户
	 */
	@Override
	public Map<String, Object> acqSingleSettleToMerchant(DuiAccountDetail duiAccountDetail, AcqOrg acqOrg)
			throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.8上游单边补记账结算商户
		/*final String secret = accountApiHttpSecret;
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");*/
		if (duiAccountDetail == null) {
			String errorMsg = "找不到交易源！";
			msg.put("state", false);
			msg.put("msg", errorMsg);
			return msg;
		}
		/*		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate",
					duiAccountDetail.getAcqTransTime() == null ? null : sdf.format(duiAccountDetail.getAcqTransTime()));// 交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString()
					: duiAccountDetail.getAcqTransAmount().toString());// 交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
//			claims.put("merchantNo", "000007");
			claims.put("transTypeCode", "000007");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", duiAccountDetail.getAcqSerialNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/acqSingleSettleToMerchant.do";
			String response = "";
			log.info("上游单边补记账结算商户url：" + url);
			response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边补记账结算商户返回信息：" + response);
			// duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");
			// duiAccountDetail.setErrorMsg("记账成功");
			// msg.put("state",true);
			// msg.put("msg","修改差错账状态成功，请手工进行调账扣减余额");
			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				String errorMsg = "上游单边补记账结算账户 返回为空";
				msg.put("state", false);
				msg.put("msg", errorMsg);
				// duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			} else {
				if ((boolean) resp.get("status") == false) {
					String errMsg = "";
					if (resp.get("msg") == null || resp.get("msg") == "") {
						errMsg += "上游单边补记账结算账户,message:返回为空";
					} else {
						errMsg = "上游单边补记账结算账户记账失败,reason:" + resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state", false);
					msg.put("msg", errMsg);

					// duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				} else {
					// 3、修改对账详细信息中的 差错处理状态 为'上游单边补记账结算商户'
					duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state", true);
					msg.put("msg", "上游单边补记账结算账户成功！");
				}
			}
		} catch (Exception e) {
			String errMsg = "上游单边补记账结算账户异常";
			// duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state", false);
			msg.put("msg", errMsg);
			log.error("异常:", e);
		}*/
		
		duiAccountDetail.setErrorHandleStatus("upstreamRecordAccount");
		duiAccountDetail.setErrorMsg("记账成功");
		int returNum = duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		if(returNum > 0){
			msg.put("state", true);
			msg.put("msg", "上游单边补记账结算商户成功！");
		}else{
			msg.put("state", false);
			msg.put("msg", "上游单边补记账结算商户失败！");
		}
		return msg;
	}

	/**
	 * 上游单边退款给持卡人
	 */
	@Override
	public Map<String, Object> acqSingleBackMoneyToOwner(DuiAccountDetail duiAccountDetail, AcqOrg acqOrg)
			throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.8上游单边退款给持卡人
/*		final String secret = accountApiHttpSecret;
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");*/
		if (duiAccountDetail == null) {
			String errorMsg = "找不到交易源！";
			msg.put("state", false);
			msg.put("msg", errorMsg);
			return msg;
		}
/*		try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate",
					duiAccountDetail.getAcqTransTime() == null ? null : sdf.format(duiAccountDetail.getAcqTransTime()));// 交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString()
					: duiAccountDetail.getAcqTransAmount().toString());// 交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000006");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", duiAccountDetail.getAcqSerialNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/acqSingleBackMoneyToOwner.do";
			log.info("上游单边退款给持卡人url：" + url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边退款给持卡人返回结果：" + response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				String errorMsg = "上游单边退款给持卡人 返回为空";
				msg.put("state", false);
				msg.put("msg", errorMsg);
				// duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			} else {
				if ((boolean) resp.get("status") == false) {
					String errMsg = "";
					if (resp.get("msg") == null || resp.get("msg") == "") {
						errMsg += "上游单边退款给持卡人,message:返回为空";
					} else {
						errMsg = "上游单边退款给持卡人记账失败,reason:" + resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state", false);
					msg.put("msg", errMsg);

					// duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				} else {
					// 3、修改对账详细信息中的 差错处理状态 为'财务退款'
					duiAccountDetail.setErrorHandleStatus("upstreamRefund");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state", true);
					msg.put("msg", "上游单边退款给持卡人成功！");
				}
			}
			// ******财务自己线下处理*******
		} catch (Exception e) {
			String errMsg = "上游单边退款给持卡人异常";
			// duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state", false);
			msg.put("msg", errMsg);
			log.error("异常:", e);
		}*/
		
		duiAccountDetail.setErrorHandleStatus("upstreamRefund");
		duiAccountDetail.setErrorMsg("记账成功");
		int returNum = duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		if(returNum > 0){
			msg.put("state", true);
			msg.put("msg", "上游单边退款给持卡人成功！");
		}else{
			msg.put("state", false);
			msg.put("msg", "上游单边退款给持卡人失败！");
		}
		return msg;
	}

	/**
	 * 上游单边确认是日切
	 */
	@Override
	public Map<String, Object> acqSingleThaw(DuiAccountDetail duiAccountDetail, AcqOrg acqOrg) throws Exception {
		Map<String, Object> msg = new HashMap<>();

		// 调用6.4.9上游单边确认是日切
/*		final String secret = accountApiHttpSecret;
		final JWTSigner signer = new JWTSigner(secret);
		final HashMap<String, Object> claims = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");*/
		if (duiAccountDetail == null) {
			String errorMsg = "找不到交易源！";
			msg.put("state", false);
			msg.put("msg", errorMsg);
			return msg;
		}
		
		/*try {
			claims.put("fromSystem", "accountWeb");
			claims.put("transDate",
					duiAccountDetail.getAcqTransTime() == null ? null : sdf.format(duiAccountDetail.getAcqTransTime()));// 交易日期
			claims.put("fromSerialNo", duiAccountDetail.getId().toString());
			claims.put("transAmount", duiAccountDetail.getAcqTransAmount() == null ? BigDecimal.ZERO.toString()
					: duiAccountDetail.getAcqTransAmount().toString());// 交易金额
			claims.put("acqEnname", duiAccountDetail.getAcqEnname());
			claims.put("transTypeCode", "000005");
			claims.put("acqOrgId", acqOrg.getId().toString());
			claims.put("transOrderNo", duiAccountDetail.getAcqSerialNo());
			claims.put("acqServiceCostMoney", duiAccountDetail.getAcqRefundAmount().toString());

			final String token = signer.sign(claims);
			String url = accountApiHttpUrl + "/recordAccountController/acqSingleForDayCut.do";
			log.info("上游单边确认是日切url：" + url);
			String response = HttpConnectUtil.postHttp(url, "token", token);
			log.info("上游单边确认是日切返回结果：" + response);

			ObjectMapper om = new ObjectMapper();
			Map<String, Object> resp = om.readValue(response, Map.class);
			if (response == null || "".equals(response)) {
				// 平台单边确认是日切
				String errorMsg = "上游单边确认是日切 返回为空";
				msg.put("state", false);
				msg.put("msg", errorMsg);
				// duiAccountDetail.setErrorHandleStatus("accountFailed");
				duiAccountDetail.setErrorMsg(errorMsg);
			} else {
				if ((boolean) resp.get("status") == false) {
					String errMsg = "";
					if (resp.get("msg") == null || resp.get("msg") == "") {
						errMsg += "上游单边确认是日切,message:返回为空";
					} else {
						errMsg = "上游单边确认是日切记账失败,reason:" + resp.get("msg").toString();
					}
					errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
					msg.put("state", false);
					msg.put("msg", errMsg);

					// duiAccountDetail.setErrorHandleStatus("accountFailed");
					duiAccountDetail.setErrorMsg(errMsg);
				} else {
					// 3、修改对账详细信息中的 差错处理状态 为'财务退款'
					duiAccountDetail.setErrorHandleStatus("upstreamThaw");
					duiAccountDetail.setErrorMsg("记账成功");
					msg.put("state", true);
					msg.put("msg", "上游单边确认是日切成功！");
				}
			}
			// ******财务自己线下处理*******
		} catch (Exception e) {
			String errMsg = "上游单边确认是日切异常";
			// duiAccountDetail.setErrorHandleStatus("accountFailed");//记账失败
			duiAccountDetail.setErrorMsg(errMsg);
			log.info(errMsg);
			msg.put("state", false);
			msg.put("msg", errMsg);
			log.error("异常:", e);
		}*/
		
		duiAccountDetail.setErrorHandleStatus("upstreamThaw");
		duiAccountDetail.setErrorMsg("记账成功");

		int returNum = duiAccountDetailMapper.updateDuiAccount(duiAccountDetail);
		if(returNum > 0){
			msg.put("state", true);
			msg.put("msg", "上游单边确认是日切成功！");
		}else{
			msg.put("state", false);
			msg.put("msg", "上游单边确认是日切失败！");
		}
		return msg;
	}

	/**
	 * 自动下载
	 */
	@Override
	public boolean duiAccountFileDown(Map<String, String> params) {
		String fileName = params.get("fileName");
		String acqOrg = params.get("acqOrg");

		String tempurl = "/account.temp/";// 本地地址
		String filePath = tempurl + fileName;
		FtpUtil fu = null;
		boolean download = false;
		try {
			SysDict sysDict = sysDictService.findSysDictByKeyValue("ftp", acqOrg + ":ftp");
			String ftp = sysDict.getSysName();
			String[] ftpInfo = ftp.split(",");
			fu = new FtpUtil(ftpInfo[0], Integer.parseInt(ftpInfo[1]), ftpInfo[2], ftpInfo[3]);
			fu.connect();
			download = fu.download(fileName, filePath);// 下载
			System.out.println(download + "-----------------");
		} catch (IOException e) {
			log.error("异常:", e);
		} catch (Exception e) {
			log.error("异常:", e);
		} finally {
			try {
				fu.disconnect();
			} catch (IOException e) {
				log.error("异常:", e);
			} // 关闭连接
		}
		return download;
	}


	// 对账之后，差错帐调用记账接口
	public Map<String, Object> confirmAccount(String checkBatchNo, String orgId) throws Exception {
		Map<String, Object> accMsg = new HashMap<String, Object>();
		// 查询所有未成功的对账数据
		List<DuiAccountDetail> duiacc = duiAccountDetailMapper.findErrorDuiAccountDetailList(checkBatchNo);
		if (duiacc.size() > 0) {
			int successCount = 0;
			int failedCount = 0;

			for (int i = 0; i < duiacc.size(); i++) {
				DuiAccountDetail errorDetail = duiacc.get(i);
				try {
					if ("PLATE_SINGLE".equals(errorDetail.getCheckAccountStatus())) {// 调用记账接口，平台单边记存疑
						TransInfo transInfo = new TransInfo();
						// transInfo =
						// transInfoService.findAcqTransInfoByParams(acqMerchantNo,
						// acqTerminalNo, acqBatchNo, acqSerialNo,
						// acqAccountNo,acqEnname,acqReferenceNo);
						transInfo = transInfoService.findErrorTransInfoByPlateTransId(
								errorDetail.getPlateTransId().toString(), errorDetail.getAcqEnname());// 对账的交易数据

						Map<String, Object> msg = this.platformSingleMarkSuspect(errorDetail, transInfo, null, orgId);
						if ((boolean) msg.get("state") == false) {
							log.info("平台单边记存疑记账失败原因：" + msg.get("msg").toString());
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");// 记账失败
							errorDetail.setErrorMsg(errMsg);
							failedCount++;
						} else {
							errorDetail.setErrorHandleStatus("checkForzen");
							errorDetail.setErrorMsg("记账成功");
							successCount++;
						}

					} else if ("FAILED".equals(errorDetail.getCheckAccountStatus())
							|| "ACQ_SINGLE".equals(errorDetail.getCheckAccountStatus())) {// 调用记账接口，收单单边
						TransInfo transInfo = new TransInfo();
						transInfo = transInfoService.findTransInfoByDuiAccountDetail(errorDetail);
						Map<String, Object> msg = this.acqSingleMarkSuspect(errorDetail, transInfo, orgId);
						if ((boolean) msg.get("state") == false) {
							String errMsg = msg.get("msg").toString();
							errMsg = errMsg.length() > 240 ? errMsg.substring(0, 240) : errMsg;
							errorDetail.setErrorHandleStatus("accountFailed");// 记账失败
							errorDetail.setErrorMsg(errMsg);
							failedCount++;
							log.info("收单单边记账失败原因：" + msg.get("msg").toString());
						} else {
							errorDetail.setErrorHandleStatus("upstreamDoubt");
							errorDetail.setErrorMsg("记账成功");
							successCount++;
						}
					}
				} catch (Exception e) {
					errorDetail.setErrorHandleStatus("accountFailed");// 记账失败
					errorDetail.setErrorMsg("记账过程异常");
					failedCount++;
				}
				duiAccountDetailMapper.updateDuiAccount(errorDetail);
			}
			accMsg.put("state", "true");
			accMsg.put("msg", "记账成功" + successCount + "条，记账失败" + failedCount + "条");
		} else {
			accMsg.put("state", "true");
			accMsg.put("msg", "无差错数据需要处理！");
		}
		return accMsg;
	}

	@Override
	public List<DuiAccountDetail> findErrorDuiAccountDetailList(String checkBatchNo) {
		return duiAccountDetailMapper.findErrorDuiAccountDetailList(checkBatchNo);
	}
	@Override
	public DuiAccountDetail findError(String orderReferenceNo,String acqEnname) {
		return duiAccountDetailMapper.findError(orderReferenceNo,acqEnname);
	}

	@Override
	public DuiAccountDetail findAcq(DuiAccountDetail duiAccountDetail) {
		return duiAccountDetailMapper.findAcq( duiAccountDetail);
	}
	@Override
	public DuiAccountDetail findPlate(DuiAccountDetail duiAccountDetail) {
		return duiAccountDetailMapper.findPlate( duiAccountDetail);
	}
	@Override
	public int updateDuiAccount(DuiAccountDetail dui) {
		return duiAccountDetailMapper.updateDuiAccount(dui);
	}

	@Override
	public int updateRemark(DuiAccountDetail detail) {
		return duiAccountDetailMapper.updateRemark(detail);
	}
	@Override
	public int updateErrorCheck(DuiAccountDetail detail) {
		return duiAccountDetailMapper.updateErrorCheck(detail);
	}

	@Override
	public List<DuiAccountDetail> getCheckDetailTransInfos(String acqEnname, String beginTime, String endtime)
			throws Exception {
		List<DuiAccountDetail> da = duiAccountDetailMapper.getCheckDetailTransInfos(acqEnname, "PURCHASE", beginTime,
				endtime);
		return da;
	}

	@Override
	public DuiAccountDetail queryByAcqDbDetailInfo(String acqOrderNo, String acqEnname) {
		DuiAccountDetail dbDetail = duiAccountDetailMapper.queryByAcqDbDetailInfo(acqOrderNo, acqEnname);
		return dbDetail;
	}
	@Override
	public DuiAccountDetail queryByAcqDbDetailInfoNoChecked(String acqOrderNo, String acqEnname) {
		DuiAccountDetail dbDetail = duiAccountDetailMapper.queryByAcqDbDetailInfoNoChecked(acqOrderNo, acqEnname);
		return dbDetail;
	}
    @Override
    public DuiAccountDetail queryByAcqDbDetailInfo1(String acqReferenceNo, String acqEnname) {
        DuiAccountDetail dbDetail = duiAccountDetailMapper.queryByAcqDbDetailInfo1(acqReferenceNo, acqEnname);
        return dbDetail;
    }
	@Override
	public DuiAccountDetail queryByAcqDbDetail(String acqReferenceNo,String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo) {
		DuiAccountDetail dbDetail = duiAccountDetailMapper.queryByAcqDbDetail(acqReferenceNo,acqMerchantNo, acqEnname,acqSerNo,acqTerNo);
		return dbDetail;
	}

	@Override
	public int updateDuiAccountDetailByOrderReNum(SubOutBillDetail subOutBillDetail, String isAddBill) {
		return duiAccountDetailMapper.updateDuiAccountDetailByOrderReNum(subOutBillDetail, isAddBill);
	}

	@Override
	public List<DuiAccountDetail> findAllTranByAcqNameAndDate(String acqEnname, Date transTime, String startTime,
			String endTime) {
		return duiAccountDetailMapper.findAllTranByAcqNameAndDate(acqEnname, transTime, startTime, endTime);
	}

	@Override
	public DuiAccountDetail queryDuiAccountDetailByOrderRefNum(SubOutBillDetail subOutBillDetailExcel) {
		return duiAccountDetailMapper.queryDuiAccountDetailByOrderRefNum(subOutBillDetailExcel);
	}

	@Override
	public void installKqzqDetailByInfo(TransInfo info, String orderNo, DuiAccountDetail detail, String acqEnname) {
		try {

				log.info(acqEnname+":上游单边重新抓数开始");
//				log.info("detail=before" + ToStringBuilder.reflectionToString(detail, ToStringStyle.MULTI_LINE_STYLE));
				installKqzqDetailByInfo(detail, info);
//				log.info("detail=after" + ToStringBuilder.reflectionToString(detail, ToStringStyle.MULTI_LINE_STYLE));
				if(detail.getCheckAccountStatus()!=null && detail.getCheckAccountStatus().equals(DuiAccountStatus.ACQ_SINGLE.toString())){
					detail.setOrderReferenceNo(null);
				}
				duiAccountDetailMapper.insertDuiAccountDetail(detail);
				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(acqEnname+":重新抓上游数据："+ e);
		}
	}
	
	/**
	 * 快钱
	 * 
	 * @param detail
	 * @param info
	 */
	private void installKqzqDetailByInfo(DuiAccountDetail detail, TransInfo info) {


		detail.setPlateAcqMerchantNo(info.getAcqMerchantNo());//银联报备商户号
		detail.setAcqMerchantNo(info.getAcqMerchantNo());//收单机构商户号
		
		
		detail.setCheckAccountStatus(DuiAccountStatus.NO_CHECKED.toString());// 对账状态未核对
		detail.setOrderReferenceNo(info.getOrderNo());// 中付的订单参考号 用平台订单号
		detail.setRecordStatus(2);//默认未记账
		
		String acqEnname = info.getAcqEnname();
		// 收单机构授权码
		detail.setAcqAuthNo(info.getAcqAuthNo());
		detail.setPlateAcqMerchantNo(info.getAcqMerchantNo());
		detail.setPlateAcqTerminalNo(info.getAcqTerminalNo());
		detail.setPlateAgentNo(info.getAgentNo());
		detail.setPlateMerchantNo(info.getMerchantNo());
		
		detail.setPlateTerminalNo(info.getTerminalNo());
		detail.setPlateAcqBatchNo(info.getAcqBatchNo());
		detail.setPlateAcqSerialNo(info.getAcqSerialNo());
		detail.setPlateAccountNo(info.getAccountNo());
		
		detail.setPlateBatchNo(info.getBatchNo());
		detail.setPlateSerialNo(info.getSerialNo());
		detail.setPlateAccountNo(info.getAccountNo());
		detail.setPlateCardNo(info.getAccountNo());//交易卡号
		if (TransType.PURCHASE_REFUND.equals(info.getTransType())
				|| TransType.PURCHASE_VOID.equals(info.getTransType())) {
			detail.setPlateTransAmount(
					new BigDecimal("-1").multiply(info.getTransAmount()).setScale(2, RoundingMode.HALF_UP));
		} else {
			detail.setPlateTransAmount(info.getTransAmount());
		}
		detail.setPlateAcqReferenceNo(info.getAcqReferenceNo());
		detail.setPlateAcqTransTime(info.getTransTime());
		detail.setPlateTransType(info.getTransType().toString());
		detail.setPlateTransStatus(info.getTransStatus().toString());
		detail.setPlateAcqMerchantFee(info.getAcqMerchantFee());
		detail.setPlateMerchantFee(info.getMerchantFee());
		detail.setPlateAcqMerchantRate(info.getAcqMerchantRate());
		detail.setPlateMerchantRate(info.getMerchantRate());
		detail.setPlateTransSource(info.getTransSource().toString());// 增加交易来源
		detail.setPlateAgentNo(info.getAgentNo());// 增加代理商编号

		detail.setBagSettle(info.getBagSettle());
		detail.setPosType(info.getPosType());// 增加设备类型2015/12/15
		detail.setPlateTransId(info.getId());
		detail.setAcqMerchantOrderNo(info.getOrderNo());
		detail.setPlateOrderNo(info.getOrderNo());//平台订单号
		detail.setPlateAgentShareAmount(info.getSingleShareAmount());// 代理商分润金额

		detail.setSettleStatus(info.getSettleStatus() == null ? 0 : Integer.valueOf(info.getSettleStatus()));
		detail.setSettlementMethod(info.getSettlementMethod());
		detail.setAccount(info.getAccount());
		detail.setAcqEnname(acqEnname);//上游名称
		detail.setCreateTime(new Date());//创建时间
		detail.setFreezeStatus(info.getFreezeStatus());//冻结标识
		detail.setOutBillStatus("0");// 0 表示未出款
		detail.setSettleType(info.getSettleType());//出款类型
		detail.setPlateOrderNo(info.getOrderNo());//平台订单号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			if (info.getMerchantSettleDate() != null) {
				String merchantSettleDate = sdf.format(info.getMerchantSettleDate());
				detail.setPlateMerchantSettleDate(sdf.parse(merchantSettleDate));
			}
			SysDict sysDict = sysDictService.findSysDictByKeyName("acq_channel_my_settle", acqEnname);
			String mySettle = sysDict.getSysValue();
			detail.setMySettle(mySettle);

		} catch (Exception e) {
			log.error("中付异常:", e);
		}

	}
    

	@Override
	public int updateDuiAccountDetailBatchByPlateOrderNo(List<DuiAccountDetail> list) {
		return duiAccountDetailMapper.updateDuiAccountDetailBatchByPlateOrderNo(list);
	}

	@Override
	public List<DuiAccountDetail> findAllDuiAccountDetailList() {
		return duiAccountDetailMapper.findAllDuiAccountDetailList();
	}

	@Override
	public List<DuiAccountDetail> findDuiAccountDetailListByTransTime(String transTime) {
		return duiAccountDetailMapper.findDuiAccountDetailListByTransTime(transTime);
	}

	@Override
	public List<DuiAccountDetail> findNoCheckedDuiAccountDetailListByTransTime(String transTime) {
		String date1 = transTime+" 00:00:00";
		String date2 = transTime+" 23:59:59";
		return duiAccountDetailMapper.findNoCheckedDuiAccountDetailListByTransTime(date1,date2);
	}

    /**
     * 短款自动匹配，补装平台数据
     * @param dbDetail
     * @param detail
     */
	private void installCheckDetailByInfo(DuiAccountDetail dbDetail, DuiAccountDetail detail) {
		dbDetail.setPlateAcqMerchantNo(detail.getPlateAcqMerchantNo());
		if(StringUtils.isNotBlank(detail.getPlateMerchantEntryNo()))
			dbDetail.setPlateMerchantEntryNo(detail.getPlateMerchantEntryNo());//平台商户进件编号
		if(StringUtils.isNotBlank(detail.getAcqMerchantNo()))
			dbDetail.setAcqMerchantNo(detail.getAcqMerchantNo());//收单机构商户号


		dbDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
		dbDetail.setOrderReferenceNo(detail.getOrderReferenceNo());
		dbDetail.setRecordStatus(detail.getRecordStatus());

		if(StringUtils.isNotBlank(detail.getAcqAuthNo()))
			dbDetail.setAcqAuthNo(detail.getAcqAuthNo());
		if(StringUtils.isNotBlank(detail.getPlateAcqMerchantNo()))
			dbDetail.setPlateAcqMerchantNo(detail.getPlateAcqMerchantNo());
		if(StringUtils.isNotBlank(detail.getPlateAcqTerminalNo()))
			dbDetail.setPlateAcqTerminalNo(detail.getPlateAcqTerminalNo());
		dbDetail.setPlateAgentNo(detail.getPlateAgentNo());
		dbDetail.setPlateMerchantNo(detail.getPlateMerchantNo());

		if(StringUtils.isNotBlank(detail.getPlateTerminalNo()))
			dbDetail.setPlateTerminalNo(detail.getPlateTerminalNo());

		if(StringUtils.isNotBlank(detail.getPlateAcqBatchNo()))
			dbDetail.setPlateAcqBatchNo(detail.getPlateAcqBatchNo());

		if(StringUtils.isNotBlank(detail.getPlateAcqSerialNo()))
			dbDetail.setPlateAcqSerialNo(detail.getPlateAcqSerialNo());

		dbDetail.setPlateAccountNo(detail.getPlateAccountNo());

		if(StringUtils.isNotBlank(detail.getPlateBatchNo()))
			dbDetail.setPlateBatchNo(detail.getPlateBatchNo());
		if(StringUtils.isNotBlank(detail.getPlateSerialNo()))
			dbDetail.setPlateSerialNo(detail.getPlateSerialNo());

		dbDetail.setPlateTransAmount(detail.getPlateTransAmount());
		if(StringUtils.isNotBlank(detail.getPlateAcqReferenceNo()))
			dbDetail.setPlateAcqReferenceNo(detail.getPlateAcqReferenceNo());
		dbDetail.setPlateAcqTransTime(detail.getPlateAcqTransTime());
		dbDetail.setPlateTransType(detail.getPlateTransType());
		dbDetail.setPlateTransStatus(detail.getPlateTransStatus());
		dbDetail.setPlateAcqMerchantFee(detail.getPlateAcqMerchantFee());
		dbDetail.setPlateMerchantFee(detail.getPlateMerchantFee());
		dbDetail.setPlateAcqMerchantRate(detail.getPlateAcqMerchantRate());
		dbDetail.setPlateMerchantRate(detail.getPlateMerchantRate());
		dbDetail.setPlateTransSource(detail.getPlateTransSource());// 增加交易来源

		dbDetail.setBagSettle(detail.getBagSettle());
		dbDetail.setPosType(detail.getPosType());
		dbDetail.setPlateTransId(detail.getPlateTransId());
		if(StringUtils.isNotBlank(detail.getAcqMerchantOrderNo()))
			dbDetail.setAcqMerchantOrderNo(detail.getAcqMerchantOrderNo());
		dbDetail.setPlateOrderNo(detail.getPlateOrderNo());//平台订单号
		dbDetail.setPlateAgentShareAmount(detail.getPlateAgentShareAmount());// 代理商分润金额

        dbDetail.setIsAddBill(detail.getIsAddBill());
		dbDetail.setSettleStatus(detail.getSettleStatus());
		dbDetail.setSettlementMethod(detail.getSettlementMethod());
		dbDetail.setAccount(detail.getAccount());
		dbDetail.setFreezeStatus(detail.getFreezeStatus());//冻结标识
		dbDetail.setOutBillStatus(detail.getOutBillStatus());// 0 表示未出款
		dbDetail.setSettleType(detail.getSettleType());//出款类型
        dbDetail.setMySettle(detail.getMySettle());
		if(detail.getPlateMerchantSettleDate()!=null)
			dbDetail.setPlateMerchantSettleDate(detail.getPlateMerchantSettleDate());
        if(detail.getDeductionFee()!=null)
            dbDetail.setDeductionFee(detail.getDeductionFee());
        if(detail.getActualFee()!=null)
            dbDetail.setActualFee(detail.getActualFee());
        if(detail.getCouponNos()!=null)
            dbDetail.setCouponNos(detail.getCouponNos());
        if(StringUtils.isNotEmpty(detail.getPayMethod()))
        	dbDetail.setPayMethod(detail.getPayMethod());

	}

	/**
	 * 对账表中，当前交易覆盖上一日交易，当前交易的上游信息使用上一日记录的数据
	 * @param dbDetail
	 * @param detail
	 */
	private void installAcqCheckDetailByInfo(DuiAccountDetail dbDetail, DuiAccountDetail detail) {
		dbDetail.setCheckAccountStatus(DuiAccountStatus.SUCCESS.toString());
		if(StringUtils.isNotBlank(detail.getAcqTransSerialNo()))
			dbDetail.setAcqTransSerialNo(detail.getAcqTransSerialNo());
		if(StringUtils.isNotBlank(detail.getAccessOrgNo()))
			dbDetail.setAccessOrgNo(detail.getAccessOrgNo());
		if(StringUtils.isNotBlank(detail.getAccessOrgName()))
			dbDetail.setAccessOrgName(detail.getAccessOrgName());
		if(StringUtils.isNotBlank(detail.getAcqMerchantName()))
			dbDetail.setAcqMerchantName(detail.getAcqMerchantName());
		if(StringUtils.isNotBlank(detail.getAcqCardSequenceNo()))
			dbDetail.setAcqCardSequenceNo(detail.getAcqCardSequenceNo());
		if(detail.getAcqSettleDate()!=null)
			dbDetail.setAcqSettleDate(detail.getAcqSettleDate());
		if(StringUtils.isNotBlank(detail.getAcqTransCode()))
			dbDetail.setAcqTransCode(detail.getAcqTransCode());
		if(detail.getAcqCheckDate()!=null)
			dbDetail.setAcqCheckDate(detail.getAcqCheckDate());
		if(StringUtils.isNotBlank(detail.getAcqMerchantNo()))
			dbDetail.setAcqMerchantNo(detail.getAcqMerchantNo());//收单机构商户号

		if(StringUtils.isNotBlank(detail.getAcqAuthNo()))
			dbDetail.setAcqAuthNo(detail.getAcqAuthNo());
		if(StringUtils.isNotBlank(detail.getAcqMerchantNo()))
			dbDetail.setAcqMerchantNo(detail.getAcqMerchantNo());
		if(StringUtils.isNotBlank(detail.getAcqTerminalNo()))
			dbDetail.setAcqTerminalNo(detail.getAcqTerminalNo());

		if(StringUtils.isNotBlank(detail.getAcqBatchNo()))
			dbDetail.setAcqBatchNo(detail.getAcqBatchNo());

		if(StringUtils.isNotBlank(detail.getAcqSerialNo()))
			dbDetail.setAcqSerialNo(detail.getAcqSerialNo());
		if(StringUtils.isNotBlank(detail.getAcqSerialNo()))
			dbDetail.setAcqAccountNo(detail.getAcqAccountNo());

		dbDetail.setAcqTransAmount(detail.getAcqTransAmount());
		if(StringUtils.isNotBlank(detail.getAcqReferenceNo()))
			dbDetail.setAcqReferenceNo(detail.getAcqReferenceNo());
		dbDetail.setAcqTransTime(detail.getAcqTransTime());
		if(StringUtils.isNotBlank(detail.getAcqTransCode()))
			dbDetail.setAcqTransCode(detail.getAcqTransCode());
		dbDetail.setAcqTransStatus(detail.getAcqTransStatus());
		dbDetail.setAcqRefundAmount(detail.getAcqRefundAmount());
		if(StringUtils.isNotBlank(detail.getAcqTransType()))
			dbDetail.setAcqTransType(detail.getAcqTransType());

		if(StringUtils.isNotBlank(detail.getAcqMerchantOrderNo()))
			dbDetail.setAcqMerchantOrderNo(detail.getAcqMerchantOrderNo());

		if(StringUtils.isNotBlank(detail.getPayMethod()))
			dbDetail.setPayMethod(detail.getPayMethod());



	}
	/**
	 * 对账表中，当前交易覆盖上一日交易，当前交易的上游信息使用上一日记录的数据，对账核对状态处理为核对成功，“单边日切标识”处理为“长款自动匹配”
	 * @param dbDetail
	 * @param detail
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateDuiAccountDetailAuto(DuiAccountDetail dbDetail, DuiAccountDetail detail) throws Exception {
		installAcqCheckDetailByInfo(dbDetail,detail);
		int s = duiAccountDetailMapper.deleteDuiAccountDetail(detail.getId().intValue());
		int re = duiAccountDetailMapper.updateDuiAccountDetail(dbDetail);
		duiAccountDetailMapper.updateDuiAccCut(dbDetail.getId().intValue(),"MoreCashAuto");
		return re;

	}

	/**
	 * 对账表中，当前交易覆盖上一日交易，当前交易的平台信息使用上一日记录的数据，对账核对状态处理为核对成功，“单边日切标识”处理为“短款自动匹配”
	 * @param dbDetail
	 * @param detail
	  * @return
	 * @throws Exception
	 */
	@Override
	public int updatePlateDuiAccountDetailAuto(DuiAccountDetail dbDetail, DuiAccountDetail detail) throws Exception {
		installCheckDetailByInfo(dbDetail,detail);
		int s = duiAccountDetailMapper.deleteDuiAccountDetail(detail.getId().intValue());
        int re = 0;
        if(s>0) {
            re = duiAccountDetailMapper.updatePlateAcqDuiAccountDetail(dbDetail);
            duiAccountDetailMapper.updateDuiAccCut(dbDetail.getId().intValue(), "ShortCashAuto");
        }
		return re;

	}
	@Override
	public int updateDuiAccError(Integer id,String treatmentMethod,String freezeStatus) throws Exception {
		return duiAccountDetailMapper.updateDuiAccError(id,treatmentMethod,freezeStatus);

	}

	@Override
	public DuiAccountDetail findDuiAccountDetailByOrderReferenceNo(String orderReferenceNo) {
		return duiAccountDetailMapper.findDuiAccountDetailByOrderReferenceNo(orderReferenceNo);
	}

	@Override
	public DuiAccountDetail findDuiAccountDetailByAcqOrderNoAndDate(String orderNo,String date1,String date2) {
		return duiAccountDetailMapper.findDuiAccountDetailByAcqOrderNoAndDate(orderNo,date1,date2);
	}

	@Override
	public DuiAccountDetail findDuiAccountDetailByAcqReferenceNoAndDate(String orderNo,String date1,String date2) {
		return duiAccountDetailMapper.findDuiAccountDetailByAcqReferenceNoAndDate(orderNo,date1,date2);
	}

	public DuiAccountDetail findDuiAccountDetailByAcqMerchantOrderNoAndDate(String orderNo,String date1,String date2) {
		return duiAccountDetailMapper.findDuiAccountDetailByAcqMerchantOrderNoAndDate(orderNo,date1,date2);
	}

	@Override
	public int updateDuiAccountForT1(DuiAccountDetail dui) {
		return duiAccountDetailMapper.updateDuiAccountForT1(dui);
	}

	@Override
	public int updateDuiAccountStatus(DuiAccountDetail dui) {
		return duiAccountDetailMapper.updateDuiAccountStatus(dui);
	}


}
