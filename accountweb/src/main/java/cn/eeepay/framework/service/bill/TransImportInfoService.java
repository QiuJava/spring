package cn.eeepay.framework.service.bill;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.eeepay.framework.model.bill.TransImportInfo;
import cn.eeepay.framework.model.nposp.TransInfo;

public interface TransImportInfoService {
	int updateTransInfoAccStatus(String orderReferenceNo) throws Exception;
	TransInfo findAcqTransInfoByParams(String acqMerchantNo, String acqTerminalNo, String acqBatchNo, 
			String acqSerialNo, String acqAccountNo,String acqEnname,String acqReferenceNo) throws Exception;
	
	TransInfo findAcqTransInfoRyxByParams(String acqReferenceNo,String acqEnname,
			BigDecimal transAmount, String acqMerchantNo,String accountNo) throws Exception;
	
	TransInfo findRefundByAcqTransInfo(String acqMerchantNo, String acqTerminalNo, String acqBatchNo, 
			String acqSerialNo, String acqAccountNo,String acqEnname,String acqReferenceNo,String transType) throws Exception;
	
	List<TransInfo> findCheckData(String acqEnname, String transTimeBegin, String transTimeEnd)  throws Exception;
	
	/**
	 * 根据收单机构id， 冲销交易标志-0正常交易，冲销状态-正常，记账日期-上一个交易日 统计交易金额
	 * @param acqEName:收单机构英文名称
	 * @param reverseFlag：冲销交易标志
	 * @param reverseStatus: 冲销状态
	 * @param tarnsDate：记账日期
	 * @return
	 */
	BigDecimal countByParam(String acqEName, String reverseFlag, String reverseStatus, Date transDate);
	
	//账户交易金额查询
	TransImportInfo findServiceFeeByMonth(String d1,String d2,String outId,Integer day);
	
	//账户交易金额查询
	TransImportInfo findDianFeeByMonth(String d1,String d2,String outId,Integer day);
	
	BigDecimal countOutFeel(Integer num,Integer serviceId,Integer days,BigDecimal money);
}
