package cn.eeepay.framework.service.bill.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.eeepay.framework.dao.bill.TransImportInfoMapper;
import cn.eeepay.framework.dao.nposp.TransInfoMapper;
import cn.eeepay.framework.enums.TransType;
import cn.eeepay.framework.model.bill.TransImportInfo;
import cn.eeepay.framework.model.nposp.OutAccountServiceRate;
import cn.eeepay.framework.model.nposp.TransInfo;
import cn.eeepay.framework.service.bill.TransImportInfoService;
import cn.eeepay.framework.service.nposp.OutAccountServiceRateService;
@Service("transImportInfoService")
@Transactional
public class TransImportInfoServiceImpl  implements TransImportInfoService{
	
	private static final Logger log = LoggerFactory.getLogger(TransImportInfoServiceImpl.class);
	
	@Resource
	public TransInfoMapper  transInfoMapper;
	@Resource
	public TransImportInfoMapper  transImportInfoMapper;
	@Resource
	private OutAccountServiceRateService outAccountServiceRateService;
	
	@Override
	public int updateTransInfoAccStatus(String orderReferenceNo) {
		return transInfoMapper.updateTransInfoAccStatus(orderReferenceNo);
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
	public BigDecimal countByParam(String acqEName, String reverseFlag,
			String reverseStatus, Date transDate) {
		return transImportInfoMapper.countByParam(acqEName, reverseFlag, reverseStatus, transDate);
	}
	
	@Override
	public TransImportInfo findServiceFeeByMonth(String d1, String d2, String outId,Integer day) {
		return transImportInfoMapper.findServiceFeeByMonth(d1, d2, outId,day);
	}
	@Override
	public TransImportInfo findDianFeeByMonth(String d1, String d2, String outId, Integer day) {
		return transImportInfoMapper.findDianFeeByMonth(d1, d2, outId, day);
	}
	@Override
	public BigDecimal countOutFeel(Integer num,Integer serviceId,Integer days,BigDecimal money) {
		BigDecimal outMoney=new BigDecimal(0);
		List<OutAccountServiceRate> list= outAccountServiceRateService.getByServiceIdInfo(serviceId);
		for (OutAccountServiceRate outAccountServiceRate : list) {
			if(num==1){
				if(outAccountServiceRate.getAgentRateType()==5){
					outMoney=getMoney(outAccountServiceRate,days,money);
				}
			}
			if(num==2){
				if(outAccountServiceRate.getCostRateType()==5){
					outMoney=getMoney(outAccountServiceRate,days,money);
				}
			}
		}
		return outMoney;
	}
	
	public static BigDecimal getMoney(OutAccountServiceRate outAccountServiceRate,Integer days,BigDecimal money){
		if(money.compareTo(outAccountServiceRate.getLadder2Max())!=-1){
			return outAccountServiceRate.getLadder2Max().multiply(outAccountServiceRate.getLadder1Rate().divide(new BigDecimal(100))
					.multiply(new BigDecimal(days))
					.multiply(new BigDecimal(10000)));
		}
		if(money.compareTo(outAccountServiceRate.getLadder3Max())!=-1 && money.compareTo(outAccountServiceRate.getLadder2Max())==-1){
			return outAccountServiceRate.getLadder2Max().multiply(outAccountServiceRate.getLadder1Rate().divide(new BigDecimal(100)))
					.add(outAccountServiceRate.getLadder2Max().subtract(outAccountServiceRate.getLadder1Max()).multiply(outAccountServiceRate.getLadder2Rate()
							.divide(new BigDecimal(100))))
					.multiply(new BigDecimal(days))
					.multiply(new BigDecimal(10000));
		}
		if(money.compareTo(outAccountServiceRate.getLadder4Max())!=-1 && money.compareTo(outAccountServiceRate.getLadder3Max())==-1){
			return outAccountServiceRate.getLadder2Max().multiply(outAccountServiceRate.getLadder1Rate().divide(new BigDecimal(100)))
					.add(outAccountServiceRate.getLadder2Max().subtract(outAccountServiceRate.getLadder1Max()).multiply(outAccountServiceRate.getLadder2Rate()
							.divide(new BigDecimal(100))))
					.add(outAccountServiceRate.getLadder3Rate().subtract(outAccountServiceRate.getLadder2Max()).multiply(outAccountServiceRate.getLadder3Rate()
							.divide(new BigDecimal(100))))
					.multiply(new BigDecimal(days))
					.multiply(new BigDecimal(10000));
		}
		if(money.compareTo(outAccountServiceRate.getLadder4Max())==-1){
			return outAccountServiceRate.getLadder2Max().multiply(outAccountServiceRate.getLadder1Rate().divide(new BigDecimal(100)))
					.add(outAccountServiceRate.getLadder2Max().subtract(outAccountServiceRate.getLadder1Max()).multiply(outAccountServiceRate.getLadder2Rate()
							.divide(new BigDecimal(100))))
					.add(outAccountServiceRate.getLadder3Rate().subtract(outAccountServiceRate.getLadder2Max()).multiply(outAccountServiceRate.getLadder3Rate()
							.divide(new BigDecimal(100))))
					.add(outAccountServiceRate.getLadder4Rate().subtract(outAccountServiceRate.getLadder3Max()).multiply(outAccountServiceRate.getLadder4Rate()
							.divide(new BigDecimal(100))))
					.multiply(new BigDecimal(days))
					.multiply(new BigDecimal(10000));
		}
		return new BigDecimal(0);
	}
}
