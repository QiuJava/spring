package cn.eeepay.framework.service.nposp;

import cn.eeepay.framework.model.bill.DuiAccountDetail;
import cn.eeepay.framework.model.bill.FastCheckAccDetail;
import cn.eeepay.framework.model.nposp.CollectiveTransOrder;
import cn.eeepay.framework.model.nposp.TransInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TransInfoService {
	int updateTransInfoAccStatus(String orderReferenceNo) throws Exception;
	int updateTransInfoAcc(String acqEnname,String acqReferenceNo,String acqMerNo,String acqSerNo,String acqTerno) throws Exception;

	public int updateCollectiveTransOrder(CollectiveTransOrder transInfo);
	public int updateScanCodeTrans(CollectiveTransOrder transInfo);
	public int updateTransInfo(CollectiveTransOrder transInfo,DuiAccountDetail dui);

	TransInfo findAcqTransInfoByParams(String acqMerchantNo, String acqTerminalNo, String acqBatchNo,
			String acqSerialNo, String acqAccountNo,String acqEnname,String acqReferenceNo) throws Exception;
	
	TransInfo findAcqTransInfoRyxByParams(String acqReferenceNo,String acqEnname,
			BigDecimal transAmount, String acqMerchantNo,String accountNo) throws Exception;
	
	TransInfo findRefundByAcqTransInfo(String acqMerchantNo, String acqTerminalNo, String acqBatchNo, 
			String acqSerialNo, String acqAccountNo,String acqEnname,String acqReferenceNo,String transType) throws Exception;
	
	List<TransInfo> findCheckData(String acqEnname, String transTimeBegin, String transTimeEnd)  throws Exception;
	
	TransInfo findTransInfoByDuiAccountDetail(DuiAccountDetail duiAccountDetail) throws Exception;
	
	TransInfo findErrorTransInfoByPlateTransId(
			String plateTransId, String acqEnname) throws Exception;
	
	TransInfo findErrorTransInfoByFastDuiAccountDetail(
			FastCheckAccDetail duiAccountDetail) throws Exception;
	
	TransInfo findTransInfoByFastDuiAccountDetail(FastCheckAccDetail duiAccountDetail) throws Exception;
	CollectiveTransOrder findAcqTransInfoByOrderRefNoAndAcqName(String orderReferenceNo, String acqEnname);
	TransInfo getYinshengTranData(String orderNo, String acqEnname);
	TransInfo getTranData(String orderNo,String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo,String acqAccountNo);
	CollectiveTransOrder getCollectiveTransData(String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo,String acqAccountNo);
	int updateTransInfoAccById(String id);
	CollectiveTransOrder getCollectiveTransDataForT1(String acqMerchantNo, String acqEnname,String acqSerNo,String acqTerNo,String acqAccountNo,BigDecimal acqTransAmount);
	CollectiveTransOrder findOrderSomeStatus(String orderNo);
	public Map<String,Object> getAcqTerminalStore(String unionMerNo);
	TransInfo getTranDataByAcqReferenceNo(String acqReferenceNo);


}

