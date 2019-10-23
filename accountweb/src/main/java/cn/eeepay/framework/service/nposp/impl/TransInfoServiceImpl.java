package cn.eeepay.framework.service.nposp.impl;

import cn.eeepay.framework.dao.nposp.TransInfoMapper;
import cn.eeepay.framework.enums.TransType;
import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.service.nposp.TransInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Service("transInfoService")
@Transactional("nposp")
public class TransInfoServiceImpl  implements TransInfoService{
	
	private static final Logger log = LoggerFactory.getLogger(TransInfoServiceImpl.class);
	
	@Resource
	public TransInfoMapper  transInfoMapper;
	
	@Override
	public int updateTransInfoAccStatus(String orderReferenceNo) {
		return transInfoMapper.updateTransInfoAccStatus(orderReferenceNo);
	}
	@Override
	public int updateTransInfoAcc(String acqEnname,String acqReferenceNo,String acqMerNo,String acqSerNo,String acqTerNo) {
		return transInfoMapper.updateTransInfoAcc(acqEnname,acqReferenceNo,acqMerNo,acqSerNo,acqTerNo);
	}


	@Override
	public int updateCollectiveTransOrder(CollectiveTransOrder transInfo) {
		return transInfoMapper.updateCollectiveOrder(transInfo);
	}

	@Override
	public int updateScanCodeTrans(CollectiveTransOrder transInfo) {
		return transInfoMapper.updateScanCodeTrans(transInfo);
	}

	@Override
	public int updateTransInfo(CollectiveTransOrder transInfo,DuiAccountDetail dui) {
		return transInfoMapper.updateTransInfo(transInfo,dui);
	}

	@Override
	public int updateTransInfoAccById(String id) {
		return transInfoMapper.updateTransInfoAccById(id);
	}



	@Override
	public TransInfo findAcqTransInfoByParams(String acqMerchantNo, String acqTerminalNo, String acqBatchNo,
			String acqSerialNo, String acqAccountNo, String acqEnname, String acqReferenceNo) throws Exception {
		String transType = TransType.PURCHASE.toString();
		String acqAccountNoEnd = acqAccountNo.substring(acqAccountNo.length()-4,acqAccountNo.length());
		return transInfoMapper.findAcqTransInfoByParams(transType,acqMerchantNo, acqTerminalNo, acqBatchNo, acqSerialNo, acqAccountNo,acqAccountNoEnd, acqEnname, acqReferenceNo);
	}
	@Override
	public TransInfo findAcqTransInfoRyxByParams(String acqReferenceNo, String acqEnname, BigDecimal transAmount,
			String acqMerchantNo, String accountNo) throws Exception {
		String transType = TransType.PURCHASE.toString();
		return transInfoMapper.findAcqTransInfoRyxByParams(transType,acqReferenceNo, acqEnname, transAmount, acqMerchantNo, accountNo);
	}
	@Override
	public TransInfo findRefundByAcqTransInfo(String acqMerchantNo, String acqTerminalNo, String acqBatchNo,
			String acqSerialNo, String acqAccountNo, String acqEnname, String acqReferenceNo, String transType) throws Exception {
		return transInfoMapper.findRefundByAcqTransInfo(acqMerchantNo, acqTerminalNo, acqBatchNo, acqSerialNo, acqAccountNo, acqEnname, acqReferenceNo, transType);
	}
	@Override
	public List<TransInfo> findCheckData(String acqEnname, String transTimeBegin, String transTimeEnd) throws Exception {
		String transType = TransType.PURCHASE.toString();
		return transInfoMapper.findCheckData(acqEnname,transType, transTimeBegin, transTimeEnd);
	}
	@Override
	public TransInfo findTransInfoByDuiAccountDetail(DuiAccountDetail duiAccountDetail) throws Exception {
		return transInfoMapper.findTransInfoByDuiAccountDetail(duiAccountDetail);
	}
	@Override
	public TransInfo findErrorTransInfoByPlateTransId(String plateTransId, String acqEnname) throws Exception {
		return transInfoMapper.findErrorTransInfoByDuiAccountDetail(plateTransId, acqEnname);
	}
	@Override
	public TransInfo findErrorTransInfoByFastDuiAccountDetail(
			FastCheckAccDetail duiAccountDetail) throws Exception {
		return transInfoMapper.findErrorTransInfoByFastDuiAccountDetail(duiAccountDetail);
	}
	@Override
	public TransInfo findTransInfoByFastDuiAccountDetail(
			FastCheckAccDetail duiAccountDetail) throws Exception {
		return transInfoMapper.findTransInfoByFastDuiAccountDetail(duiAccountDetail);
	}
	@Override
	public CollectiveTransOrder findAcqTransInfoByOrderRefNoAndAcqName(String orderReferenceNo, String acqEnname) {
		return transInfoMapper.findAcqTransInfoByOrderRefNoAndAcqName(orderReferenceNo,acqEnname);
	}
	@Override
	public TransInfo getYinshengTranData(String orderNo, String acqEnname) {
		return transInfoMapper.getYinshengTranData(orderNo,acqEnname);
	}
	@Override
	public TransInfo getTranData(String orderNo,String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo,String acqAccountNo) {
		return transInfoMapper.getTranData(orderNo,acqMerchantNo,acqEnname,acqSerNo,acqTerNo,acqAccountNo);
	}

	@Override
	public CollectiveTransOrder getCollectiveTransData(String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo,String acqAccountNo) {
		return transInfoMapper.getCollectiveTranData(acqMerchantNo,acqEnname,acqSerNo,acqTerNo,acqAccountNo);
	}

	@Override
	public CollectiveTransOrder getCollectiveTransDataForT1(String acqMerchantNo, String acqEnname, String acqSerNo, String acqTerNo, String acqAccountNo,BigDecimal acqTransAmount) {
		return transInfoMapper.getCollectiveTransDataForT1(acqMerchantNo,acqEnname,acqSerNo,acqTerNo,acqAccountNo,acqTransAmount);
	}

	@Override
	public CollectiveTransOrder findOrderSomeStatus(String orderNo) {
		return transInfoMapper.findOrderSomeStatus(orderNo);
	}

	//查询终端号
	@Override
	public Map<String,Object> getAcqTerminalStore(String unionMerNo) {
		return transInfoMapper.getAcqTerminalStore(unionMerNo);
	}
	@Override
	public TransInfo getTranDataByAcqReferenceNo(String acqReferenceNo) {
		return transInfoMapper.getTranDataByAcqReferenceNo(acqReferenceNo);
	}
}
